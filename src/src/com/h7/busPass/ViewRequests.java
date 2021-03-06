package com.h7.busPass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewRequests {

    //This method is internally called by PendingBusPassRequests: To check: Stop exist in Route. If selected stop by user is in route than go to next step else user stay in pending state
    public ArrayList<String> StopExistsInRoute(String stop) throws SQLException {
        String SQL = "select distinct route, stop from stop_info a join route_info b on a.stop = b.stops where stop = '" + stop + "'";
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);
        ArrayList<String> RoutesFromDB = new ArrayList();
        while (rs.next()) {
            RoutesFromDB.add(rs.getString("route"));
        }
        return RoutesFromDB;
    }

    //This method is internally called by PendingBusPassRequests: To Check If Routes has any allocated bus? If yes get all the buses with their seater type else go to next user
    public HashMap<String, Integer> ReturnBusCapacity(ArrayList<String> RoutesFromDB) throws SQLException {
        StringBuilder intlist = new StringBuilder();

        for (int count1 = 0; count1 < RoutesFromDB.size(); count1++) {
            if (count1 == RoutesFromDB.size() - 1) {
                intlist.append("'").append(RoutesFromDB.get(count1)).append("'");
            } else {
                intlist.append("'").append(RoutesFromDB.get(count1)).append("',");
            }
        }

        String SQL2 = "Select bus_id, category_id from bus_table where route in (" + intlist + ")";
        SQLSelect sqlRun2 = new SQLSelect();
        ResultSet rs2 = sqlRun2.SqlSelectStatement(SQL2);
        HashMap<String, Integer> busCap = new HashMap<>();

        try {
            while (rs2.next()) {
                busCap.put(rs2.getString("bus_id"), rs2.getInt("category_id"));
            }
        } catch (Exception e) {
            return busCap;
        }
        return busCap;
    }

    //This method is internally called by PendingBusPassRequests: To check seats are available in the busses?
    public HashMap<String, Integer> UserOccupyingBus(HashMap<String, Integer> busCap) throws SQLException {
        StringBuilder busses = new StringBuilder();

        for (int count1 = 0; count1 < busCap.size(); count1++) {
            if (count1 == busCap.size() - 1) {
                busses.append("'").append(busCap.keySet().toArray()[count1]).append("'");
            } else {
                busses.append("'").append(busCap.keySet().toArray()[count1]).append("',");
            }
        }

        String SQL3 = "Select bus_id, count(distinct a.login) as num from pass_details a join user_info b on a.login = b.login where bus_id in (" + busses + ") and status = 'APPROVED' group by 1";
        SQLSelect sqlRun3 = new SQLSelect();
        ResultSet rs3 = sqlRun3.SqlSelectStatement(SQL3);
        HashMap<String, Integer> userInBus = new HashMap<>();

        try {
            while (rs3.next()) {
                userInBus.put(rs3.getString("bus_id"), rs3.getInt("num"));
            }
        } catch (Exception e) {
            return userInBus;
        }
        return userInBus;
    }

    //This method is internally called by PendingBusPassRequests: to generate the pass i.e. enter the details in pass_details and user_info table
    boolean GenerateBussPass(String login, Object object) throws SQLException {
        SQLUpdate su = new SQLUpdate();
        HashMap<String, String> colValues = new HashMap<>();
        HashMap<String, String> where = new HashMap<>();
        String tableName = "user_info";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        boolean isUploaded;

        colValues.put("status", "'" + "APPROVED" + "'");
        colValues.put("change_date", "'" + dtf.format(localDate) + "'");
        where.put("login", "'" + login + "'");
        isUploaded = su.ExecuteUpdate(tableName, colValues, where);

        SQLSelect sqlRun = new SQLSelect();
        String SQL = "select distinct route from bus_table where bus_id = '" + object + "'";
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);
        String route = null;

        while (rs.next())
            route = rs.getString("route");

        SQLInsert si = new SQLInsert();
        HashMap<String, String> colValuesInsert = new HashMap<>();
        String tableName2 = "pass_details";
        colValuesInsert.put("bus_id", (String) object);
        colValuesInsert.put("route", route);
        colValuesInsert.put("login", login);
        isUploaded = si.ExecuteInsert(tableName2, colValuesInsert);
        System.out.println("Bus pass created for user: " + login);
        return isUploaded;
    }

    //This method act as notification for the Admin. To inform him about the pending user requests when he logins.
    public void Notifications() {
        SQLSelect sqlRun = new SQLSelect();
        String SQL = "select distinct login from user_info where type = 'user' and status = 'PENDING'  ";
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);
        ArrayList<String> pendingLogins = new ArrayList();

        try {
            while (rs.next()) {
                pendingLogins.add(rs.getString("login"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pendingLogins.size() != 0) {
            System.out.println("\n" + "Notifications: " + pendingLogins.size() + " pass requests");
        }
    }

    /*
     * PendingBusPassRequests method helps Admin to view pending requests. Pending here means status of user as "PENDING" in user_info table.
     * Status changed to Pending 1. when new user is created. 2. if existing user change the stop.
     * Now, this method works in various steps.
     * 1. To get the list of all the users and their selected stops. Run loop to approve/reject the user request based on checks.
     * 2. Check: Stop exist in Route: method: StopExistsInRoute. If selected stop by user is in route than go to next step else user stay in pending state
     * 3. Check If Routes has any allocated bus? If yes get all the buses with their seater type else go to next user.
     * 4. Check seats are available in the busses? If yes go to next step else go to next user.
     * 5. At last Allocate the bus to user and generate the pass i.e. enter the details in pass_details and user_info table
     */
    public boolean PendingBusPassRequests() throws SQLException {
        SQLSelect sqlRun = new SQLSelect();
        String SQL = "select login, stop from user_info where type = 'user' and status = 'PENDING' order by date(change_date) ";

        //1. To get the list of all the users and their selected stops.
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);
        ArrayList<String> pendingLogins = new ArrayList();
        ArrayList<String> pendingStops = new ArrayList();

        while (rs.next()) {
            pendingLogins.add(rs.getString("login"));
            pendingStops.add(rs.getString("stop"));
        }
        System.out.println("\n" + "There are total: " + pendingLogins.size() + " pending requests" + "\n");

        for (int count = 0; count < pendingLogins.size(); count++) {
            String login = pendingLogins.get(count);
            String stop = pendingStops.get(count);
            //2. Check: Stop exist in Route: method: StopExistsInRoute
            ArrayList<String> RoutesFromDB = StopExistsInRoute(stop);

            if (RoutesFromDB.size() > 0) {
                System.out.println("Fetching details for user: '" + login + "'" + "\n");
                System.out.println("There are total: " + RoutesFromDB.size() + " route/s: " + RoutesFromDB + " for stop: " + stop + ".");

                //3.Check If Routes has any allocated bus?
                HashMap<String, Integer> busCap = ReturnBusCapacity(RoutesFromDB);

                if (busCap.isEmpty()) {
                    System.out.println("\n" + "No Bus Assign to this route");
                    System.out.println("User: " + login + " request will remain in pending state as there is no bus assign" + "\n");
                    continue;
                }

                //4. Check seats are available in the busses? If yes go to next step else go to next user.
                HashMap<String, Integer> userInBus = UserOccupyingBus(busCap);

                if (userInBus.isEmpty()) {
                    GenerateBussPass(login, busCap.keySet().toArray()[0]);
                    System.out.println("Bus Allocated" + "\n");
                    continue;
                }
                boolean flag = true;

                for (int count1 = 0; count1 < userInBus.size(); count1++)    // This is to allocate bus which already has users in it and have empty seats
                {
                    int bus = busCap.get(userInBus.keySet().toArray()[count1]);
                    int user = userInBus.getOrDefault(userInBus.keySet().toArray()[count1], 0);

                    if (user < bus) {
                        System.out.println("Allocating Bus: " + userInBus.keySet().toArray()[count1]);
                        System.out.println("Available seats: " + (bus - user));
                        flag = false;
                        GenerateBussPass(login, userInBus.keySet().toArray()[count1]);
                        break;
                    } else {
                        System.out.println("User: " + login + " request will remain in pending state as there is no Capacity in any bus in the route" + "\n");
                    }
                }

                if (flag) {
                    for (int count1 = 0; count1 < busCap.size(); count1++) { // This is to allocate NEW bus when there is no empty seats in current buses of the route 
                        int bus = busCap.get(busCap.keySet().toArray()[count1]);
                        int user = userInBus.getOrDefault(busCap.keySet().toArray()[count1], 0);

                        if (user < bus) {
                            System.out.println("Allocating Bus: " + userInBus.keySet().toArray()[count1]);
                            System.out.println("Remaining seats: " + (bus - user));
                            this.GenerateBussPass(login, userInBus.keySet().toArray()[count1]);
                            break;
                        } else {
                            System.out.println("User: " + login + " request will remain in pending state as there is no Capacity in any bus in the route");
                        }
                    }
                }
            } else {
                System.out.println("\n" + "Fetching details for user: '" + login + "'" + "\n");
                System.out.println("User: " + login + " request will remain in pending state as there are no active routes for mentioned stop: " + stop + " \n");
            }
        }
        return true;
    }
}

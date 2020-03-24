import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminFactory {

    BusMaster calling_busMaster = new BusMaster();
    SQLSelect sel = new SQLSelect();
    Scanner input = new Scanner(System.in);
    String regNumber;

    void viewRequests() {
        ViewRequests ob = new ViewRequests();
        try {
            ob.PendingBusPassRequests();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addNewRoute() {
        System.out.println("Below are the active Routes" + "\n");
        RouteMaster ob = new RouteMaster();
        ob.viewAllRoutes();

        System.out.println("\n" + "Select direction of new route:  EAST , WEST , NORTH , SOUTH" + "\n");
        String direction = input.next().toUpperCase();

        if (JdbcConnect.connect() != null) // provide available stops
        {
            String sql = "select stop, direction from stop_info where stop not in(select distinct stops from route_info) and direction='" + direction + "'"; //
            try (
                    Statement stmt = JdbcConnect.connect().createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
            ) {
                System.out.println("Available stops at :" + direction);
                while (rs.next()) {
                    System.out.print("Stop Name -" + rs.getString("stop") + "\n");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Write ' , ' seperated Stop names from available Stops" + "\n");

        String[] stoplist = null;

        stoplist = input.next().toUpperCase().split(",");

        //sql statement to update user's selected route
        int s = routes.size() + 1; // error on this line...
        String route = "R" + s;

        for (String obj1 : stoplist) {
            String sql = "INSERT INTO route_info (route,stops,direction)"
                    + "VALUES ('" + route + "','" + obj1 + "','" + direction + "')";

            if (JdbcConnect.connect() != null) {
                try (Connection conn = JdbcConnect.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Added route :" + route);

    }

    boolean removeRoute() {
        RouteMaster ob = new RouteMaster();

        ob.viewAllRoutes();
        System.out.println("Provide the route you would like to delete");
        String route = input.next().toUpperCase();

        String sql = "SELECT status FROM user_info where status = 'ACTIVE' AND stop IN (select stops from route_info where route = '" + route + "') ";

        if (JdbcConnect.connect() != null) //
        {
            try (
                    Statement stmt = JdbcConnect.connect().createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
            ) {
                while (rs.next()) {
                    System.out.print("Route cannot be deleted as it is " + rs.getString("status") + "\n");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Route is not in use. Delete will be executed");
            }
        }

        sql = "DELETE FROM route_info WHERE route=  '" + route + "'";

        if (JdbcConnect.connect() != null) {
            try (Connection conn = JdbcConnect.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Deleted :" + route);
        return true;
    }


    String regNumberCheck() {
        regNumber = input.nextLine();
        while (regNumber.length() != 4) {
            System.out.print("Please provide valid 4 digit Reg Number: ");
            regNumber = input.nextLine();
        }
        return regNumber;
    }

    void registerBus() {
        Connection con = JdbcConnect.connect();
        Vehicle ob = null;
        System.out.println("\n" + "Please Select a vehicle type");
        System.out.println("ThreeSeater Select 3 ");
        System.out.println("FiveSeater Select 5");
        System.out.println("SevenSeater Select 7");
        int choice = 0;
        boolean error;
        do {
            try {
                System.out.print("Input: ");
                input = new Scanner(System.in);
                choice=input.nextInt();
                if(choice == 3 || choice == 5 || choice == 7)
                    error = false;
                else {
                    System.out.println("Invalid Input :-( Select 3, 5, or 7" + "\n");
                    error=true;
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid Input :-( Select 3, 5, or 7");
                error=true;
            }
        } while(error);

        String regNumber;
        switch (choice) {
            case 3:
                regNumber = regNumberCheck();
                ob = new ThreeSeater(regNumber);
                break;
            case 5:
                regNumber = regNumberCheck();
                ob = new FiveSeater(regNumber);
                break;
            case 7:
                regNumber = regNumberCheck();
                ob = new SevenSeater(regNumber);
                break;
            default:
                System.out.println("Invalid Input :-( ");
                break;
        }

        String check = "SELECT DISTINCT number_plate FROM bus_table";
        String sqlQuery = "INSERT INTO bus_table(number_plate, category_id) values(?,?)";
        PreparedStatement pstIns;
        if(con != null)
        {
            try {
                pstIns = con.prepareStatement(check);
                ResultSet rs = pstIns.executeQuery();
                ArrayList<String>  vehNum= new ArrayList<>();
                while(rs.next()) {
                    vehNum.add(rs.getString("number_plate"));
                }
                if(vehNum.contains(ob.vehicleNumber)) {
                    System.out.println("Vehicle already Registered with ATS");
                }
                else{
                    try {
                        pstIns = con.prepareStatement(sqlQuery);
                        pstIns.setString(1, ob.vehicleNumber);
                        pstIns.setInt(2, ob.capacity);
                        pstIns.executeUpdate();
                        System.out.println("Vehicle Successfully Registered");
                    } catch (SQLException e) { System.out.println(e.getMessage());}
                }
            } catch (SQLException e) {System.out.println(e.getMessage());}
            finally {JdbcConnect.closeCon(con);}
        }
    }

    String UserRouteValue() {
        ArrayList<String> routes = new ArrayList<>();
        String sql = "select distinct route from route_info";
        ResultSet rs;
        try {
            rs = sel.SqlSelectStatement(sql);
            while(rs.next()) {
                routes.add(rs.getString("route"));
            }
        } catch (SQLException e) {e.printStackTrace();}

        System.out.println("Available routes are:");
        for (String s: routes) {
            System.out.print(s + "\t");
        }

        String route;
        do{
            System.out.print("\n" + "Input: ");
            route = input.nextLine().toUpperCase();
        }while(!routes.contains(route));
        return route;
    }

    void assignBusToRoute() {
        String route = UserRouteValue();
        try {
            calling_busMaster.AddBusInRoute(route);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void ChangeVehicleTypeofRoute() {
        String route = UserRouteValue();
        try {
            calling_busMaster.ChangeBusTypeOfRoute(route);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


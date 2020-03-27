import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusMaster {

    Connection conn;
    Scanner input = new Scanner(System.in);

    public BusMaster(Connection conn) {
        this.conn = conn;
    }

    public HashMap<String, ArrayList<String>> BusesInRoute(String route, String sql) throws SQLException {

        ArrayList<String> al = new ArrayList<>();
        HashMap<String, ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        while (rs.next()) {
            al.add(rs.getString("bus_id"));
        }
        rs.close();

        System.out.println("In " + route + "-route, there are total " + al.size() + " buses.");
        System.out.println();

        ArrayList<String> al2 = new ArrayList<>();
        String sql2 = "select category_id from bus_table where route =  '" + route + "' ";
        ResultSet rs2 = sqlRun.SqlSelectStatement(sql2);
        while (rs2.next()) {
            al2.add(rs2.getString("category_id"));
        }
        rs2.close();

        for (int counter = 0; counter < al.size(); counter++) {
            System.out.println("Line:" + counter + "-> Bus number: " + al.get(counter) + " Category: " + al2.get(counter) + "-Seater");
        }

        send.put("numberPlate", al);
        send.put("category", al2);


        return send;
    }

    public HashMap<String, ArrayList<String>> vehicleDifferentTypes(String SQL) throws SQLException {
        String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";
        ArrayList<String> number = new ArrayList<>();
        ArrayList<String> typeBus = new ArrayList<>();

        HashMap<String, ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);


        while (rs.next()) {
            number.add(rs.getString("num"));
            typeBus.add(rs.getString("category_id"));
        }

        int sum = 0;
        for (String s : number)
            sum = sum + Integer.parseInt(s);  // Array list declared as String bcz of heterogeneous hash map

        System.out.println();
        System.out.println("Total available buses are: " + sum);
        System.out.println("Combinations Like: ");

        for (int i = 0; i < number.size(); i++)
            System.out.println("category: " + typeBus.get(i) + "-seaters" + " total of " + number.get(i) + " vehicle available.");

        send.put("countOfBuses", number);
        send.put("typeOfVehicle", typeBus);
        return send;
    }

    public String AvailableBus(String enterCategory) throws SQLException {

        String sql = "select bus_id from bus_table where route is null and category_id = '" + enterCategory + "'";
        String busNum = null;
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        while (rs.next())
            busNum = rs.getString("bus_id");
        return busNum;
    }

    boolean allocateBus(String busNum, String route) {
        SQLUpdate su = new SQLUpdate();
        HashMap<String, String> colValues = new HashMap<>();
        HashMap<String, String> where = new HashMap<>();
        String tableName = "bus_table";
        boolean isUploaded;

        if (route == null) {
            colValues.put("route", null);

        } else {
            colValues.put("route", "'" + route + "'");
        }
        where.put("bus_id", "'" + busNum + "'");

        isUploaded = su.ExecuteUpdate(tableName, colValues, where);

        return isUploaded;
    }

    public void AddBusInRoute(String route) throws SQLException {
        ArrayList<String> al;
        HashMap<String, ArrayList<String>> rec; //
        String sql1 = "select bus_id from bus_table where route =  '" + route + "' ";
        rec = BusesInRoute(route, sql1); // To check How many busses are there in route asked by user CALLING
        al = rec.get("numberPlate");


        if (al.size() < 3) {
            String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";

            HashMap<String, ArrayList<String>> receive;
            receive = vehicleDifferentTypes(sql);        // CALLING
//            ArrayList<String> number = new ArrayList<>();
            ArrayList<String> typeBus = receive.get("typeOfVehicle");
            System.out.println();
            System.out.println("Enter the category number from above available category to assign the bus.");

            boolean flag = true;

            Scanner input = new Scanner(System.in);

            while (flag) {
                String categoryAllocate = input.nextLine();
                String busNum;

                if (typeBus.contains(categoryAllocate)) {

                    busNum = this.AvailableBus(categoryAllocate); // CALLING

                    boolean isUploaded = allocateBus(busNum, route);// CALLING
                    if (isUploaded)
                        System.out.println("Bus: " + busNum + " is allocated to the route: " + route);
                    flag = false;
                } else {
                    System.out.println("Please enter the valid category from above ");
                }
            }
        } else {
            System.out.println("Sorry, you can't allocate bus to this route as it already has three buses.");
        }
        System.out.println();

    }

    public void ChangeBusTypeOfRoute(String route) throws SQLException {
        ArrayList<String> al;
        ArrayList<String> al2;
        String sql = "select bus_id from bus_table where route =  '" + route + "' ";
        HashMap<String, ArrayList<String>> rec = BusesInRoute(route, sql);
        al = rec.get("numberPlate");
        al2 = rec.get("category");

        System.out.println();
        System.out.println("Enter the line number of the bus to change the type.");

        Scanner input = new Scanner(System.in);

        int busSelection = input.nextInt();

        System.out.println("Enter the new category.");
        System.out.println("For 3-Seater enter: 3");
        System.out.println("For 5-Seater enter: 5");
        System.out.println("For 7-Seater enter: 7");

        SQLSelect sq = new SQLSelect();
        String sql1 = "Select distinct category_id from bus_table where route is null";
        ResultSet rs = sq.SqlSelectStatement(sql1);
        ArrayList<Integer> categoryIds = new ArrayList<>();
        while (rs.next()) {
            categoryIds.add(rs.getInt("category_id"));
        }
        boolean flagCat = true;
        while (flagCat) {
            int catSelection = input.nextInt();

            if (Integer.parseInt(al2.get(busSelection)) == catSelection) {
                System.out.println("Bus belongs to same category. Please enter new category.");
            } else if (catSelection != 3 && catSelection != 5 && catSelection != 7) {
                System.out.println("Please enter as category 3 5 or 7.");
            } else if (!categoryIds.contains(catSelection)) {
                System.out.println("Please enter some other category. As of now, we don't have bus of this categoty.");
            } else {
                //check selected category bus without any route available ? if available allocate: 1. null->route 2 curr route ko null kar do if not ask him to choose other category.
//                String SQL = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";
//                HashMap<String,ArrayList<String>> receive = VehicleDifferentTypes(SQL);    	// CALLING
//                ArrayList<String> number = new ArrayList<>();
//                ArrayList<String> typeBus = new ArrayList<>();
//
//                typeBus = receive.get("typeOfVehicle");
//                number =  receive.get("countOfBuses");
                System.out.println("Allocating bus of the category: " + catSelection);
                String busNum = AvailableBus(Integer.toString(catSelection));
                allocateBus(busNum, route);

                allocateBus(al.get(busSelection), null);
                SQLUpdate su = new SQLUpdate();
                String tableName = "pass_details";
                HashMap<String, String> columnValueMappingForSet = new HashMap<>();
                HashMap<String, String> columnValueMappingForCondition = new HashMap<>();
                columnValueMappingForSet.put("bus_id", busNum);
                columnValueMappingForCondition.put("bus_id", al.get(busSelection));
                su.ExecuteUpdate(tableName, columnValueMappingForSet, columnValueMappingForCondition);
                flagCat = false;
            }
        }


    }

    void registerBus() {
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
                choice = input.nextInt();
                if (choice == 3 || choice == 5 || choice == 7)
                    error = false;
                else {
                    System.out.println("Invalid Input :-( Select 3, 5, or 7" + "\n");
                    error = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input :-( Select 3, 5, or 7");
                error = true;
            }
        } while (error);
        String regNumber = BasicUtility.regNumberCheck();

        switch (choice) {
            case 3:
                ob = new ThreeSeater(regNumber);
                break;
            case 5:
                ob = new FiveSeater(regNumber);
                break;
            case 7:
                ob = new SevenSeater(regNumber);
                break;
            default:
                System.out.println("Invalid Input :-( ");
                break;
        }

        String check = "SELECT DISTINCT number_plate FROM bus_table";
        String sqlQuery = "INSERT INTO bus_table(number_plate, category_id) values(?,?)";
        PreparedStatement pstIns;
        if (conn != null) {
            try {
                pstIns = conn.prepareStatement(check);
                ResultSet rs = pstIns.executeQuery();
                ArrayList<String> vehNum = new ArrayList<>();
                while (rs.next()) {
                    vehNum.add(rs.getString("number_plate"));
                }
                if (vehNum.contains(ob.vehicleNumber)) {
                    System.out.println("Vehicle already Registered with ATS");
                } else {
                    try {
                        pstIns = conn.prepareStatement(sqlQuery);
                        pstIns.setString(1, ob.vehicleNumber);
                        pstIns.setInt(2, ob.capacity);
                        pstIns.executeUpdate();
                        System.out.println("Vehicle Successfully Registered");
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BusMaster {


    public Map<String, ArrayList<String>> BusesInRoute(String route, String sql) throws SQLException {

        ArrayList<String> al = new ArrayList<String>();
        Map<String, ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        while (rs.next()) {
            al.add(rs.getString("bus_id"));
        }
        rs.close();

        System.out.println("In " + route + "-route, there are total " + al.size() + " buses.");
        System.out.println();

        ArrayList<String> al2 = new ArrayList<String>();
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

    public Map<String, ArrayList<String>> VehicleDifferentTypes(String SQL) throws SQLException {

        ArrayList<String> number = new ArrayList<String>();
        ArrayList<String> typeBus = new ArrayList<String>();

        Map<String, ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);


        while (rs.next()) {
            number.add(rs.getString("num"));
            typeBus.add(rs.getString("category_id"));
        }

        int sum = 0;
        for (int i = 0; i < number.size(); i++)
            sum = sum + Integer.parseInt(number.get(i));  // Array list declared as String bcz of heterogeneous hash map

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

        String sql = "select bus_id from bus_table where route is null and category_id = '"+enterCategory+"'";
        String busNum;
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        busNum = rs.getString("bus_id");
        rs.close();

        return busNum;

    }

    boolean allocateBus(String busNum, String route) {

        SQLUpdate su = new SQLUpdate();
        HashMap<String, String> colValues = new HashMap<String, String>();
        HashMap<String, String> where = new HashMap<String, String>();
        String tableName = "bus_table";
        boolean isUploaded = false;

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
        ArrayList<String> al = new ArrayList<String>();
        Map<String, ArrayList<String>> rec = new HashMap(); //
        String sql1 = "select bus_id from bus_table where route =  '" + route + "' ";
        rec = this.BusesInRoute(route, sql1); // To check How many busses are there in route asked by user CALLING
        al = rec.get("numberPlate");

        if (al.size() < 3) {
            String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";

            Map<String, ArrayList<String>> receive = new HashMap();
            receive = this.VehicleDifferentTypes(sql);        // CALLING
            ArrayList<String> number = new ArrayList<String>();
            ArrayList<String> typeBus = new ArrayList<String>();

            typeBus = receive.get("typeOfVehicle");
            number = receive.get("countOfBuses");

            System.out.println();
            System.out.println("Enter the category number from above available category to assign the bus.");

            boolean flag = true;

            Scanner input = new Scanner(System.in);

            while(flag) {
                String categoryAllocate = input.nextLine();
                String busNum = null;

                if(typeBus.contains(categoryAllocate)) {

                    busNum = this.AvailableBus(categoryAllocate); // CALLING

                    boolean isUploaded = false;

                    isUploaded = this.allocateBus(busNum, route);// CALLING
                    if (isUploaded == true)
                        System.out.println("Bus: " + busNum + " is allocated to the route: " + route);

                    flag = false;
                }

                else {
                    System.out.println("Please enter the valid category from above ");
                }
            }
        }
        else
        {
            System.out.println("Sorry, you can't allocate bus to this route as it already has three buses.");
        }
        System.out.println();

    }

    public void ChangeBusTypeOfRoute(String route) throws SQLException {
        ArrayList<String> al = new ArrayList<String>();
        ArrayList<String> al2 = new ArrayList<String>();
        Map<String, ArrayList<String>> rec = new HashMap(); //
        String sql = "select bus_id from bus_table where route =  '" + route + "' ";
        rec = this.BusesInRoute(route, sql);
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
        ArrayList<Integer> categoryIds = new ArrayList<Integer>();
        while (rs.next()) {
            categoryIds.add(rs.getInt("category_id"));
        }


        boolean flagCat = true;

        while (flagCat) {
            Integer catSelection = input.nextInt();


            if (Integer.parseInt(al2.get(busSelection)) == catSelection) {
                System.out.println("Bus belongs to same category. Please enter new category.");
            } else if (catSelection != 3 && catSelection != 5 && catSelection != 7) {
                System.out.println("Please enter as category 3 5 or 7.");
            } else if (!categoryIds.contains(catSelection)) {
                System.out.println("Please enter some other category. As of now, we don't have bus of this categoty.");
            } else {
                //check selected category bus without any route available ? if available allocate: 1. null->route 2 curr route ko null kar do if not ask him to choose other category.
                String SQL = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";
                Map<String, ArrayList<String>> receive = new HashMap();
                receive = this.VehicleDifferentTypes(SQL);        // CALLING
                ArrayList<String> number = new ArrayList<String>();
                ArrayList<String> typeBus = new ArrayList<String>();

                typeBus = receive.get("typeOfVehicle");
                number = receive.get("countOfBuses");
                System.out.println("Allocating bus of the category: " + catSelection);

                String busNum = this.AvailableBus(catSelection.toString());
                this.allocateBus(busNum, route);

                this.allocateBus(al.get(busSelection), null);
                SQLUpdate su = new SQLUpdate();
                String tableName = "pass_details";
                HashMap<String, String> columnValueMappingForSet = new HashMap<String, String>();
                HashMap<String, String> columnValueMappingForCondition = new HashMap<String, String>();
                columnValueMappingForSet.put("bus_id", busNum);
                columnValueMappingForCondition.put("bus_id", al.get(busSelection));
                su.ExecuteUpdate(tableName, columnValueMappingForSet, columnValueMappingForCondition);


                flagCat = false;
            }
        }


    }

//    public static void main(String[] args) throws SQLException {
//        BusMaster bm = new BusMaster();
//        bm.ChangeBusTypeOfRoute("R1");
////		bm.AddBusInRoute("R1");


}

//}
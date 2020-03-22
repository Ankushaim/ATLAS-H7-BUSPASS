import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BusMaster {

    public Map<String,ArrayList<String>> BusesInRoute(String route) throws SQLException {
        String sql = "select number_plate from bus_table where route =  '"+route+"' ";
        ArrayList<String> al = new ArrayList<>();
        Map<String,ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        while (rs.next())
        {
            al.add(rs.getString("number_plate"));
        }
        rs.close();

        System.out.println("In "+ route+ "-route, there are total "+al.size()+" buses.");
        System.out.println();

        ArrayList<String> al2 = new ArrayList<>();
        String sql2 = "select category_id from bus_table where route =  '"+route+"' ";
        ResultSet rs2 = sqlRun.SqlSelectStatement(sql2);
        while (rs2.next())
        {
            al2.add(rs2.getString("category_id"));
        }
        rs2.close();

        for (int counter = 0; counter < al.size(); counter++) {
            System.out.println("Line:"+counter+"-> Bus number: "+al.get(counter)+" Category: "+al2.get(counter)+"-Seater");
        }

        send.put("numberPlate", al);
        send.put("category", al2);



        return send;
    }

    public Map<String,ArrayList<String>> VehicleDifferentTypes(String SQL) throws SQLException{

        ArrayList<String> number = new ArrayList<>();
        ArrayList<String> typeBus = new ArrayList<>();

        Map<String,ArrayList<String>> send = new HashMap(); //

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);

        while (rs.next())
        {
            number.add(rs.getString("num"));
            typeBus.add(rs.getString("category_id"));
        }

        int sum = 0;
        for (String s : number)
            sum = sum + Integer.parseInt(s);  // Array list declared as String bcz of heterogeneous hash map

        System.out.println();
        System.out.println("Total available buses are: "+sum );
        System.out.println("Combinations Like: ");

        for(int i = 0; i < number.size(); i++)
            System.out.println("category: "+typeBus.get(i)+"-seaters"+" total of "+number.get(i)+" vehicle available.");

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
        HashMap<String, String> colValues = new HashMap<>();
        HashMap<String, String> where = new HashMap<>();
        String tableName = "bus_table";
        boolean isUploaded;

        colValues.put("route", route);
        where.put("bus_id", busNum);
        isUploaded = su.ExecuteUpdate(tableName, colValues, where);

        return isUploaded;

    }


    public void AddBusInRoute(String route) throws SQLException
    {
        ArrayList<String> al;
        Map<String,ArrayList<String>> rec; //

        rec = this.BusesInRoute(route); // To check How many busses are there in route asked by user CALLING
        al = rec.get("numberPlate");

        if(al.size() <3) {
            String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";

            Map<String,ArrayList<String>> receive;
            receive = this.VehicleDifferentTypes(sql);    	// CALLING
            ArrayList<String> typeBus;

            typeBus = receive.get("typeOfVehicle");
            ArrayList<String> number = receive.get("countOfBuses");

            System.out.println();
            System.out.println("Enter the category number from above available category to assign the bus.");

            boolean flag = true;

            Scanner input = new Scanner(System.in);

            while(flag) {
                String categoryAllocate = input.nextLine();
                String busNum;

                if(typeBus.contains(categoryAllocate)){

                    busNum = this.AvailableBus(categoryAllocate); // CALLING

                    boolean isUploaded;

                    isUploaded = this.allocateBus(busNum, route);// CALLING
                    if(isUploaded)
                        System.out.println("Bus: "+busNum+" is allocated to the route: "+route);

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
        ArrayList<String> al;
        ArrayList<String> al2;
        Map<String,ArrayList<String>> rec; //
        rec = this.BusesInRoute(route);
        al = rec.get("numberPlate");
        al2 = rec.get("category");

        System.out.println();
        System.out.println("Enter the line number of the bus to change the type.");

        Scanner input = new Scanner(System.in);

        int busSelection = input.nextInt();
        System.out.println(al.get((busSelection)));//
        System.out.println("Enter the new category.");
        System.out.println("For 3-Seater enter: 3");
        System.out.println("For 5-Seater enter: 5");
        System.out.println("For 7-Seater enter: 7");

        boolean flagCat = true;

        while(flagCat) {
            int catSelection = input.nextInt();
            if(Integer.parseInt(al2.get(busSelection)) == catSelection) {
                System.out.println("Bus belongs to same category. Please enter new category.");
            }
            else if(catSelection != 3 && catSelection != 5 && catSelection != 7){
                System.out.println("Please enter as category 3 5 or 7.");
            }
            else {
                SQLUpdate su = new SQLUpdate();
                Map<String,String> colValues = new HashMap(); //
                Map<String,String> where = new HashMap(); //
                colValues.put("category_id",String.valueOf(catSelection));
                where.put("number_plate", "'"+al.get(busSelection)+"'");
                su.ExecuteUpdate("bus_table", colValues, where);
                System.out.println("Bus is Allocated");
                flagCat = false;
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        BusMaster bm = new BusMaster();
        //bm.ChangeBusTypeOfRoute("R1");
		//bm.AddBusInRoute("R1");
    }
}
package com.h7.busPass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusMaster {

    Scanner input = new Scanner(System.in);
    
  //This method is internally called by AddBusInRoute: To check in the current route how many buses are there as our assumption here is only 3 buses can be there in a route.
  //and, this method is also internally called by ChangeBusTypeOfRoute: To check in the current route how many buses are there
    public HashMap<String, ArrayList<String>> BusesInRoute(String route, String sql) throws SQLException {

        ArrayList<String> al = new ArrayList<>();
        HashMap<String, ArrayList<String>> send = new HashMap();

        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);

        while (rs.next()) {
            al.add(rs.getString("bus_id"));
        }
        rs.close();

        System.out.println("In " + route + "-route, there are total " + al.size() + " buses.");
        System.out.println();
        
        if(al.size()==0) {
        	return send;
        }
        
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
    
  //This method is internally called by AddBusInRoute: To check how many buses are available and what are there types like 3,5 or 7 seater
    public HashMap<String, ArrayList<String>> vehicleDifferentTypes(String SQL) throws SQLException {

        ArrayList<String> number = new ArrayList<>();
        ArrayList<String> typeBus = new ArrayList<>();
        HashMap<String, ArrayList<String>> send = new HashMap();

        String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(SQL);
        try {
	        while (rs.next()) {
	            number.add(rs.getString("num"));
	            typeBus.add(rs.getString("category_id"));
	        }
        }catch(Exception e) {
        	System.out.println("There is no available bus.");
        	return send;
        };

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

    
  //This method is internally called by AddBusInRoute: To allocate the selected bus by admin to the route.  
    boolean allocateBus(String busNum, String route) {
        HashMap<String, String> colValues = new HashMap<>();
        HashMap<String, String> where = new HashMap<>();

        SQLUpdate su = new SQLUpdate();
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
    
    
 /* AddBusInRoute method is a functionality of Admin. To add available bus in any route with various checks like.
  * 1. To check in the current route how many buses are there as our assumption here is only 3 buses can be there in a route. If no bus or lesser than three buses than got to next step.
  * 2. To check how many buses are available and what are there types like 3,5 or 7 seater
  * 3. At last allocate the selected bus by admin to the route.   
  */
    public boolean AddBusInRoute(String route) throws SQLException {
        ArrayList<String> al = null;
        HashMap<String, ArrayList<String>> rec;

        String sql1 = "select bus_id from bus_table where route =  '" + route + "' ";
        //1. To check in the current route how many buses are there as our assumption here is only 3 buses can be there in a route
        rec = BusesInRoute(route, sql1); 

        if (!rec.isEmpty()) {
            al = rec.get("numberPlate");
        }

        if (al == null || al.size() < 3) {
            String sql = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";

            HashMap<String, ArrayList<String>> receive;
            
            //2. To check in how many are the available buses and what are there types like 3,5 or 7 seater
            receive = vehicleDifferentTypes(sql);
            if(receive.isEmpty()) {
            	return false;
            }

            ArrayList<String> typeBus = receive.get("typeOfVehicle");
            System.out.println();
            System.out.println("Enter the category number from above available category to assign the bus.");
            boolean flag = true;
            Scanner input = new Scanner(System.in);

            while (flag) {
                String categoryAllocate = input.nextLine();
                String busNum;

                if (typeBus.contains(categoryAllocate)) {
                    busNum = this.AvailableBus(categoryAllocate);
                    //3. At last allocate the selected bus by admin to the route.   
                    boolean isUploaded = allocateBus(busNum, route);

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
		return true;
    }

/*ChangeBusTypeOfRoute method is a functionality of Admin.
 * 1. To check in the current route how many buses are there.  
 * 2. select the bus to change type.
 * 3. change only if selection is 3,5 or 7 seater as category and if selection bus is available with Amazon.  
 */

    public boolean ChangeBusTypeOfRoute(String route) throws SQLException {
        ArrayList<String> al;
        ArrayList<String> al2;
        String sql = "select bus_id from bus_table where route =  '" + route + "' ";
        //1. To check in the current route how many buses are there.  
        HashMap<String, ArrayList<String>> rec = BusesInRoute(route, sql);

        if(rec.isEmpty()) {
        	System.out.println("In this route there are zero buses.");
        	return false;
        }

        al = rec.get("numberPlate");
        al2 = rec.get("category");

        System.out.println();
        System.out.println("Enter the line number of the bus to change the type.");
        //2. select the bus to change type.
        Scanner input = new Scanner(System.in);
        boolean error = true;
        int busSelection = 0;

        do {
            try {
            	System.out.print("Input: ");
                input = new Scanner(System.in);
                busSelection = input.nextInt();
                if (busSelection != 1 && busSelection != 0 && busSelection != 2) {
                    System.out.println("Wrong input");
                    error = true;
                } else {
                    error = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input 😞 only Integers allowed");
                error = true;
            }
        } while (error);

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
            int catSelection = 0;
            do {
                try {
                    System.out.print("Input: ");
                    input = new Scanner(System.in);
                    catSelection = input.nextInt();
                    error = false;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input 😞 only Integers allowed");
                    error = true;
                }
            } while (error);
            //3. change only if selection is 3,5 or 7 seater as category and if selected bus is available with Amazon.  
            if (Integer.parseInt(al2.get(busSelection)) == catSelection) {
                System.out.println("Bus belongs to same category. Please enter new category.");
            } else if (catSelection != 3 && catSelection != 5 && catSelection != 7) {
                System.out.println("Please enter as category 3 5 or 7.");
            } else if (!categoryIds.contains(catSelection)) {
                System.out.println("Please enter some other category. As of now, we don't have bus of this categoty.");
            } else {
                System.out.println("Allocating bus of the category: " + catSelection);

                String busNum = AvailableBus(Integer.toString(catSelection));
                allocateBus(busNum, route);
                allocateBus(al.get(busSelection), null);
                SQLUpdate su = new SQLUpdate();
                
                // Update the pass detail of all the users from the current to new bus with new category 3,5 or 7 seater
                String tableName = "pass_details";
                HashMap<String, String> columnValueMappingForSet = new HashMap<>();
                HashMap<String, String> columnValueMappingForCondition = new HashMap<>();
                columnValueMappingForSet.put("bus_id", busNum);
                columnValueMappingForCondition.put("bus_id", al.get(busSelection));
                su.ExecuteUpdate(tableName, columnValueMappingForSet, columnValueMappingForCondition);
                flagCat = false;
            }
        }
		return flagCat;
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
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(check);
        ArrayList<String> vehNum = new ArrayList<>();
        try {
			while (rs.next()) {
				vehNum.add(rs.getString("number_plate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

        if (vehNum.contains(ob.vehicleNumber)) {
            System.out.println("Vehicle already Registered with ATS");

        } else {
            SQLInsert si = new SQLInsert();
            HashMap<String, String> colValues = new HashMap<>();
            colValues.put("number_plate", ob.vehicleNumber);
            colValues.put("category_id", Integer.toString(ob.capacity));
            si.ExecuteInsert("bus_table", colValues);
            System.out.println("Vehicle Successfully Registered");
        }
    }
}

    


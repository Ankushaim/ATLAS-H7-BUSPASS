import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminFactory {
    private String userId;

    public AdminFactory(String userId) {
        this.userId = userId;
    }

    Scanner input = new Scanner(System.in);
    String regNumber;

    String regNumberCheck() {
        regNumber = input.nextLine();
        while(regNumber.length() != 4)
        {
            System.out.print("Please provide valid 4 digit Reg Number: ");
            regNumber = input.nextLine();
        }
        return regNumber;
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

        JdbcConnect jdbc = new JdbcConnect();
        Connection con = jdbc.connect();
        String check = "SELECT DISTINCT number_plate FROM bus_table";
        String sqlQuery = "INSERT INTO bus_table(number_plate, category_id) values(?,?)";
        PreparedStatement pstIns;
        if(con != null)
        {
            try {
                pstIns = con.prepareStatement(check);
                ResultSet rs = pstIns.executeQuery();
                ArrayList<String>  vehNum= new ArrayList<String>();
                while(rs.next()) {
                    vehNum.add(rs.getString("number_plate"));
                }
                if(vehNum.contains(ob.vehicleNumber)) {
                    System.out.println("Vehicle already Registered with ATS");
                    con.close();
                }
                else{
                    try {
                        pstIns = con.prepareStatement(sqlQuery);
                        pstIns.setString(1, ob.vehicleNumber);
                        pstIns.setInt(2, ob.capacity);
                        pstIns.executeUpdate();
                        System.out.println("Vehicle Successfully Registered");
                        con.close();
                    } catch (SQLException e) { System.out.println(e.getMessage());}
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}


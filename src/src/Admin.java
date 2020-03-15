import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    private String userId;

    public Admin(String userId) {
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
        System.out.println("Please Select a vehicle type");
        System.out.println("1. ThreeSeater: ");
        System.out.println("2. FiveSeater: ");
        System.out.println("3. SevenSeater: ");
        System.out.println("Input: ");
        int choice = input.nextInt();
        String regNumber;
        switch (choice) {
            case 1:
                regNumber = regNumberCheck();
                ob = new ThreeSeater(regNumber);
                break;
            case 2:
                regNumber = regNumberCheck();
                ob = new FiveSeater(regNumber);
                break;
            case 3:
                regNumber = regNumberCheck();
                ob = new SevenSeater(regNumber);
                break;
            default:
                System.out.println("Invalid Input :-( ");
                break;
        }

        JdbcConnect jbc = new JdbcConnect();
        String check = "SELECT DISTINCT number_plate FROM bus_table";
        String sql = "INSERT INTO bus_table(number_plate, category_id) VALUES('"+ob.vehicleNumber+"','"+ob.capacity+"')";
        if(jbc.connect() != null)
        {
            //To check and select value from bus table
            try (Statement stmt = jbc.connect().createStatement();
                 ResultSet rs = stmt.executeQuery(check)) {
                ArrayList<String>  vehNum= new ArrayList<String>();
                while(rs.next()) {
                    vehNum.add(rs.getString("number_plate"));
                }
                if(vehNum.contains(ob.vehicleNumber)) {
                    System.out.println("Vehicle already Registered with ATS");
                }
                else{
                    //To insert into bus table
                    try (Connection conn = jbc.connect();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.executeUpdate();
                        System.out.println(" Vehicle Successfully Registered");
                    } catch (SQLException e) { System.out.println(e.getMessage());}
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}


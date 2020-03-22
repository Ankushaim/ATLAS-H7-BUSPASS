import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class GuestFactory {

    RouteMaster calling_route = new RouteMaster();
    Scanner input = new Scanner(System.in);

    public void getRouteFromRouteMaster() {
        calling_route.viewAllRoutes();
    }

    //This method will check if user already exists in data base also close the DB connection..
    boolean userCheck(String login, Connection con) throws SQLException {
        PreparedStatement pstSel = con.prepareStatement("select * from user_info where login = ?");
        pstSel.setString(1,login);
        ResultSet r1=pstSel.executeQuery();
        return r1.next();
    }

    void register() throws SQLException {
        Connection con = JdbcConnect.connect();
        String sqlQuery = "INSERT INTO user_info(user_name, login, password, phone_num, address, city, status, type, create_date, change_date) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstIns = con.prepareStatement(sqlQuery);

        ArrayList<String> regDetails = new ArrayList<>();
        System.out.println("Create your ATS Account to continue to ATS");
        boolean flag = true;
        while(flag) {
            String name;
            do{
                System.out.print("Name: ");
                name = input.nextLine().toUpperCase();
            }
            while(name.length() == 0);
            regDetails.add(name);

            String login;
            do{
                System.out.print("Login: ");
                login = input.nextLine();
            }
            while(login.length() == 0);
            if(userCheck(login, con))  //To check is login already registered..
            {
                System.out.println("UserFactory Already Exists");
                break;
            }
            else
                regDetails.add(login);

            String password;
            do{
                System.out.print("Password: ");
                password = input.nextLine();
            }
            while(password.length() == 0);
            regDetails.add(password);

            String phNumber;
            do{
                System.out.print("Phone Number: ");
                phNumber = input.nextLine();
            }
            while(phNumber.length() == 0);
            regDetails.add(phNumber);

            String address;
            do{
                System.out.print("Address: ");
                address = input.nextLine();
            }
            while(address.length() == 0);
            regDetails.add(address);

            String city;
            do{
                System.out.print("City: ");
                city = input.nextLine().toUpperCase();
            }
            while(city.length() == 0);
            regDetails.add(city);

            regDetails.add("PENDING"); // to set application in pending..
            regDetails.add("user");


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            regDetails.add(dtf.format(localDate));
            regDetails.add(dtf.format(localDate));
            int  i = 1;
            for (String c: regDetails
                 ) {
                pstIns.setString(i, c);
                i++;
            }
            pstIns.executeUpdate();
            System.out.println("Registration Successful");
            if (calling_route.selectStop(login)) {
                System.out.println("Please wait for Admin's approval on your ATS pass request :-)");
            }
            else{
                System.out.println("Registration Successful. However bus pass application failed");
                System.out.println("To continue application go to login-> Edit or Change Details-> Change Stop");
            }
            con.close();
            flag = false;
        }
    }
}

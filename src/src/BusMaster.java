import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class BusMaster {

    //	boolean AddBusInRoute(String route){
    public void AddBusInRoute(String route) throws SQLException{
        JdbcConnect jbc = new JdbcConnect();
        if(jbc.connect() != null)
        {
            String sql = "select distinct bus_id from bus_table where route =  '"+route+"' ";
            try(Statement stmt = jbc.connect().createStatement();
                ResultSet rs = stmt.executeQuery(sql))
            {
                ArrayList<String> al = new ArrayList<String>();
                while (rs.next())
                {
                    al.add(rs.getString("bus_id"));
                }

                System.out.println("In "+ route+ " route there are total "+al.size()+" buses. Bus number: "+al );

                if(al.size() <3)
                {
                    String sql2 = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is null group by 1";
                    ArrayList<Integer> number = new ArrayList<Integer>();
                    ArrayList<String> typeBus = new ArrayList<String>();
                    try(Statement stmt2 = jbc.connect().createStatement();
                        ResultSet rs2 = stmt.executeQuery(sql2))
                    {
                        while (rs2.next())
                        {
                            number.add(rs.getInt("num"));
                            typeBus.add(rs.getString("category_id"));
                        }

                        int sum = 0;
                        for(int i = 0; i < number.size(); i++)
                            sum = sum + number.get(i);
                        System.out.println();
                        System.out.println("Total available buses are: "+sum);
                        System.out.println();

                        for(int i = 0; i < number.size(); i++)
                            System.out.println("category: "+typeBus.get(i)+"-seaters"+" total of "+number.get(i)+" vehicle available.");

                        System.out.println();
                        System.out.println("Enter the category number from above available category to assign the bus.");
                        boolean flag = true;
                        Scanner input = new Scanner(System.in);
                        while(flag) {

                            String categoryAllocate = input.nextLine();
                            String busNum=null;

                            if(typeBus.contains(categoryAllocate)){
                                String sql3 = "select bus_id from bus_table where route is null and category_id = '"+categoryAllocate+"'";
                                if(jbc.connect() != null)
                                {
                                    try (Statement stmt3 = jbc.connect().createStatement();
                                         ResultSet rs3 = stmt.executeQuery(sql3)) {
                                        busNum = rs3.getString("bus_id");
                                    }

                                    catch (SQLException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                                String sql4 = "update  bus_table  set route = '"+route+"' where bus_id = '"+busNum +"'";

                                try (Connection conn = jbc.connect();
                                     PreparedStatement pstmt = conn.prepareStatement(sql4)) {
                                    pstmt.executeUpdate();
                                } catch (SQLException e) { System.out.println(e.getMessage());}
                                flag = false;
                            }
                            else {
                                System.out.println("Please enter the valid category from above ");
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("Sorry, you can't allocate bus to this route as it already has three buses.");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        BusMaster bm = new BusMaster();
        bm.AddBusInRoute("R1");
    }
}
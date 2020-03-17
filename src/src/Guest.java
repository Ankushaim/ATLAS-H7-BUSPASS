import java.sql.SQLException;

public class Guest {
    //First SRS requirement
    GuestFactory calling_route = new GuestFactory();

    public void viewRoute() {
        calling_route.getRouteFromRouteMaster();
    }

    public void signUp() throws SQLException {
        calling_route.register();
    }
}

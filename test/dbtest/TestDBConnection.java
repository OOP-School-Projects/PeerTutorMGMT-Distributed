package dbtest;
import server.database.DBConnection;

public class TestDBConnection {
    public static void main (String[ ] args ){
        DBConnection db = new DBConnection();
        db.CloseConnection();
    }
}

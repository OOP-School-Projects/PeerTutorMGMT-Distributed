
package app;
import server.database.DBOperationsImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", "192.168.100.24");
        //try catch to avoid crashing
        try {
            //create impl object
            DBOperationsImpl dbImpl = new DBOperationsImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("PeerTutorDB", dbImpl);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

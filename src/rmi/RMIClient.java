package rmi;
import server.database.DBOperationsRemote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static String SERVER_IP = "192.168.100.24"; // to be changed to June's IP

    public static DBOperationsRemote getStub() throws Exception {
        Registry registry = LocateRegistry.getRegistry(SERVER_IP, 1099);
        return (DBOperationsRemote) registry.lookup("PeerTutorDB");
    }
}

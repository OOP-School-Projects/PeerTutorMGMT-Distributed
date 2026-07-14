
package server.database;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DBOperationsRemote extends Remote{
    void insertOperation(Object obj) throws RemoteException;
    Object selectOperation(Object id, String table) throws RemoteException;
    List<Object> selectAllOperation(String table) throws RemoteException;
    void updateOperation(Object obj) throws RemoteException;
    void deleteOperation(Object id, String table) throws RemoteException;
}

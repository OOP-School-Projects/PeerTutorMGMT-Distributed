package server.database;

import java.util.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//calls the diff classs
public class DBOperationsImpl extends UnicastRemoteObject implements DBOperationsRemote {
    public DBOperationsImpl() throws RemoteException{
        super();
    }
    //variables to hold instances of the DB operations 
    public DBInsert dbInsert = new DBInsert();
    private DBSelect dbSelect = new DBSelect();
    private DBSelectAll dbSelectAll = new DBSelectAll();
    private DBUpdate dbUpdate = new DBUpdate();
    private DBDelete dbDelete = new DBDelete();    
    
   @Override
   public void insertOperation(Object obj){
       dbInsert.insertOperation(obj);
   }
   
   @Override
   public Object selectOperation(Object id, String table){    
       return dbSelect.selectOperation(id, table);
       
   }
   
   @Override
   public List<Object> selectAllOperation(String table){     
       return dbSelectAll.selectAllOperation(table);
       
   }
   
   @Override
   public void updateOperation(Object obj){
       dbUpdate.updateOperation(obj);
   }
   
   @Override
   public void deleteOperation(Object id, String table){
       dbDelete.deleteOperation(id, table);
   }
}
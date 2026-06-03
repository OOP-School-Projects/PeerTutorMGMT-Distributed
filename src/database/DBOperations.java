package database;

import java.util.List;

public interface DBOperations {
    public void insertOperation(Object obj);
    public Object selectOperation(String id);
    public List selectAllOperation();
    public void updateOperation(Object obj);
    public void deleteOperation(String id);
}
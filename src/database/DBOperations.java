package database;

import java.util.List;

public interface DBOperations {
    public void insertOperation(Object obj);
    public Object selectOperation(Object id, String table);
    public List<Object> selectAllOperation(String table);
    public void updateOperation(Object obj);
    public void deleteOperation(Object id, String table);
}
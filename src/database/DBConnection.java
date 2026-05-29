package database;
import java.sql.*;


public class DBConnection {
    //TODO: Add dotenv-java and remove hardcoded credintials 
    //using transaction pooler (shared pooler)
    //Added a config file to avoid hard coding values (kinda like .env but .env for java is hard )
    // url = jdbc:postgres://host:port/databasename
    public String url = Config.BD_Url;
    public String username = Config.DB_Username;
    public String password =  Config.DB_Password;
    public Connection con;
    
    public DBConnection(){
        //1. Load driver 
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loading sucessful");
        }catch( ClassNotFoundException cnfe){
            System.out.println("failed to load driver"+ cnfe.getMessage());
        }
        //2. establic connection
        try{
            con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Successful!");
        }catch(SQLException sqle){
            System.out.println("Connection Failed!" + sqle);
        }
    }
    
    public void CloseConnection(){
        try{
           if (con != null && !con.isClosed() ){
               con.close();
               System.out.println("connection closed successfully!");
           } 
        }catch(SQLException sqle){
            System.out.println("failed to close connection" + sqle);
        }
    }
}


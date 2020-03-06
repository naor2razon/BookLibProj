
package library.assistants.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;



public final class DataBaseHandler {
    private static DataBaseHandler handler=null;
    
    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn=null;
    private static Statement stmt=null;
    
    private DataBaseHandler(){
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
    }
    
    public static DataBaseHandler getInstance(){
        if(handler==null){
            handler = new DataBaseHandler();
        }
        return handler;
    }
    
    void createConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn=DriverManager.getConnection(DB_URL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //SEE TASK 1
    void setupBookTable(){
       String TABLE_NAME = "BOOK";
       try{
           stmt = conn.createStatement();
          
           DatabaseMetaData dbm = conn.getMetaData();
           ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null); //SEE NOTE 1
           if(tables.next()){
               System.out.println("Table"+TABLE_NAME+"already exists. Ready for go!");
           }else{
               stmt.execute("CREATE TABLE "+TABLE_NAME+"("
               +"       id varchar(200) primary key,\n"
               +"       title varchar(200),\n"
               +"       author varchar(200),\n"
               +"       publisher varchar(100),\n"
               +"       intcode varchar(100),\n"        
               +"       isAvail boolean default true"        
               +" )");
           }
       }catch(SQLException e){
        System.err.println(e.getMessage()+"... setupDatabase");
    }finally{  
    }
    }
    
     private void setupMemberTable() {
        String TABLE_NAME = "MEMBER";
       try{
           stmt = conn.createStatement();
          
           DatabaseMetaData dbm = conn.getMetaData();
           ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null); //SEE NOTE 1
           if(tables.next()){
               System.out.println("Table"+TABLE_NAME+"already exists. Ready for go!");
           }else{
               stmt.execute("CREATE TABLE "+TABLE_NAME+"("
               +"       id varchar(200) primary key,\n"
               +"       name varchar(200),\n"
               +"       mobile varchar(20),\n"
               +"       email varchar(100),\n"
               +"       intcode varchar(100)\n"        
               +" )");
           }
       }catch(SQLException e){
        System.err.println(e.getMessage()+"... setupDatabase");
    }finally{  
    }
    }
    
     
    //reading from the DB, like select
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }
      
    //execute action like insert, return boolean if the action success or not
    public boolean execAction(String qu) {
      return true;
//        try {
//            stmt = conn.createStatement();
//            stmt.execute(qu);
//            return true;
//        }
//        catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
//            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
//            return false;
//        }
//        finally {
//        }
    }

   void setupIssueTable(){
       String TABLE_NAME="ISSUE";
       try {
           stmt = conn.createStatement();
           DatabaseMetaData dbm = conn.getMetaData();
           ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
           if(tables.next()){
               System.out.println("Table "+ TABLE_NAME + "Already exist, ready for go!");
           }else{
               stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
               + "       bookID varchar(200) primary key,\n"
               + "       memberID varchar(200),\n"
               + "       issueTime timestamp default CURRENT_TIMESTAMP,\n"
               + "       renew_Count integer default 0,\n"
               + "       FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
               + "       FOREIGN KEY (memberID) REFERENCES MEMBER(id)\n"
               + " )");
           }
           
       } catch (SQLException e) {
           System.err.println(e.getMessage()+".......setupDatabase");
       }finally{
       }
   }



}






//*********************************NOTS******************************//
/*
    NOTE 1:ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)throws SQLException:
    it returns the description of the tables of the specified catalog.
    The table type can be TABLE, VIEW, ALIAS, SYSTEM TABLE, SYNONYM etc.
*/

//*********************************TASKS******************************//
/*
TASK 1:IMPROVE IT TO AVOID SQL INJECTION "setupBookTable"
*/


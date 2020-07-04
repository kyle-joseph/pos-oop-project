
package posystem;
import java.sql.*;
public class msqlAccess {
    Connection connection = null;
    static final String con = "jdbc:mysql://localhost/dbmeatshop?useTimezone=true&serverTimezone=UTC";
    static final String uname = "root";
    static final String pass = "";

    public void connect(){
        try{
            connection = DriverManager.getConnection(con, uname, pass);
        }
        catch(SQLException e){
          System.out.println("ERROR connecting to database!");
          System.out.println(e.toString());
        }
    }

    public ResultSet select(String query){
        try{
          Statement statement = connection.createStatement();
          ResultSet result = statement.executeQuery(query);
          return result;
        }
        catch(SQLException e){
          System.out.println("ERROR while executing select query!");
          System.out.println(e.toString());
          return null;
        }
    }
    public boolean loginSelect(String query){
        try{
          Statement statement = connection.createStatement();
          ResultSet result = statement.executeQuery(query);
          return result.next() != false;
        }
        catch(SQLException e){
          System.out.println("ERROR while executing select query!");
          System.out.println(e.toString());
          return false;
        }
    }

    public int update(String query){
        try{
          Statement statement = connection.createStatement();
          return statement.executeUpdate(query);
        }
        catch(SQLException e){
          System.out.println("ERROR while executing update query");
          System.out.println(e.toString());
          return -1;
        }
    }
    public int insert(String query){
        try{
          Statement statement = connection.createStatement();
          return statement.executeUpdate(query);
        }
        catch(SQLException e){
          System.out.println("ERROR while executing update query");
          System.out.println(e.toString());
          return -1;
        }
    }

    public int delete(String query){
        try{
          Statement statement = connection.createStatement();
          return statement.executeUpdate(query);
        }
        catch(SQLException e){
          System.out.println("ERROR while deleting line!");
          System.out.println(e.toString());
          return -1;
        }
    }

    public void close(){
        try{
          connection.close();
        }
        catch(SQLException e){
          System.out.println("ERROR while closing connections!");
          System.out.println(e.toString());
        }
    }
}

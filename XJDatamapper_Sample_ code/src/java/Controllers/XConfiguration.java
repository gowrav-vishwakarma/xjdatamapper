package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XConfiguration {
    
    String dbName="student record";
    String userName="root";
    String password="";
    String portNo="3306";
    static Connection con;

    public XConfiguration() throws ClassNotFoundException, SQLException {
         
        // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();

        Class.forName("com.mysql.jdbc.Driver");
        if(con == null)
            //con=DriverManager.getConnection("jdbc:odbc:DataSource");
            con=DriverManager.getConnection("jdbc:mysql://localhost:" + portNo + "/" + dbName ,userName,password);      
    }
}

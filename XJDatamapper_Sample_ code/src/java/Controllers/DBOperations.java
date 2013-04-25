package Controllers;

import java.sql.*;

public class DBOperations extends XConfiguration{
    
    static Connection con;
    public DBOperations() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        if(con ==null)
            con = DriverManager.getConnection("jdbc:mysql://localhost:"+portNo+"/"+dbName,userName,password);
    }
    
    public ResultSet SelectQuery(String sql) throws SQLException{
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(sql);
        return rs;
    }
    
    public int ModifyQuery(String sql) throws SQLException{
        Statement st=con.createStatement();
        return st.executeUpdate(sql);
    }
}

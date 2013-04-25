package Controllers.Models;

import Controllers.XJDataMapper;

import java.sql.SQLException;

public class StudInfo extends XJDataMapper {
    
    public StudInfo() throws ClassNotFoundException, SQLException {
        super();
        hasOne = "{{'abc'->ds}"
                + "{'CompanyDetail'->officialdetail,s_id,ID,officialdetail,EmployeeOf}"
                + "}";
        table="studinfo";
        hasMany="";
          
    }
    void getu(){}
}

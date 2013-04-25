package Controllers.Models;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class OfficialDetail extends XJDataMapper
{
    public OfficialDetail() throws ClassNotFoundException, SQLException 
    {
        super();
        hasOne="{{'EmployeeOf'->StudInfo,ID,s_id,studinfo,CompanyDetail}}";
        hasMany="{{'branches'->companyaddress,o_id,ID,companyaddress,headOffice}}";
        table="officialdetail";
        
    }    
}

    

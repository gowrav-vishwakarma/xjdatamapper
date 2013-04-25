package Controllers.Models;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class companyaddress extends XJDataMapper
{
    public companyaddress() throws ClassNotFoundException, SQLException 
    {
        super();
        hasOne="{"
                + "{'headOffice'->OfficialDetail,ID,o_id,officialdetail,branches}"
                + "}";
        hasMany="";
        table="companyaddress";
        
    }    
}

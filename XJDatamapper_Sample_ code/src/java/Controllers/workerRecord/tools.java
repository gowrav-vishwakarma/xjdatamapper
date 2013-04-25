package Controllers.workerRecord;
import Controllers.XJDataMapper;

import java.sql.SQLException;

public class tools  extends XJDataMapper{

    public tools() throws ClassNotFoundException, SQLException {
    super();
    hasOne = "{'Workplace'->workplaces,workplace_id,id,workplaces,tools,'Tools'}";        
    hasMany="";
    table="tools"
            + "";
    
    }
}

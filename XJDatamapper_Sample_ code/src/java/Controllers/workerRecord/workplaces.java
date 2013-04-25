package Controllers.workerRecord;

import Controllers.XJDataMapper;

import java.sql.SQLException;

public class workplaces extends XJDataMapper{

    public workplaces() throws ClassNotFoundException, SQLException {
    super();
    hasOne = "{'Worker'->workers,id,workplace_id,workers,workplaces,'Workplace'}";
    hasMany = "{'Tools'->tools,id,workplace_id,tools,workplaces,'Workplace'}";        
    
    table="workplaces";
    
    }
}

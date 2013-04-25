package Controllers.workerRecord;

import Controllers.XJDataMapper;

import java.sql.SQLException;

public class workers extends XJDataMapper {

    public workers() throws ClassNotFoundException, SQLException {
    super();
    hasOne = "{'Workplace'->workplaces,workplace_id,id,workplaces,workers,'Worker'}"
            + "}";
    hasMany ="{'Skills'->skills_workers,id,workers_id,skills_workers,workers,'Workers'} ";
    table="workers";
    
    }
}

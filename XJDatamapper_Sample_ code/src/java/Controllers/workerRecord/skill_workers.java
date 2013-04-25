package Controllers.workerRecord;

import Controllers.XJDataMapper;

import java.sql.SQLException;

public class skill_workers extends XJDataMapper {

    public skill_workers() throws ClassNotFoundException, SQLException {
    super();
    hasMany ="{'Skills'->skill_workers,workers_id,skills_id,skill_workers,'Workers'}"
            + "{'Workers'->skill_workers,skills_id,workers_id,skill_workers,'Skills'} ";
    table="workers";
    
    }
}

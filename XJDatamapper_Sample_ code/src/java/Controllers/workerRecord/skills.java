package Controllers.workerRecord;

import Controllers.XJDataMapper;

import java.sql.SQLException;

public class skills extends XJDataMapper{

    public skills() throws ClassNotFoundException, SQLException {
    super();
    hasOne = "";
    hasMany ="{'Workers'->skills_workers,id,skills_id,skills_workers,skills,'Skills'} ";
    table="skills";
    
    }
}

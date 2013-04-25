package Controllers.Models;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class StudentInformation extends XJDataMapper {

    public StudentInformation() throws ClassNotFoundException, SQLException {
        
        super();
        table="studinfo";
        tablePrefix="";
        hasMany = " {'Subjects'->Subject,s_id,ID,subject,'allotedTo'},"
                + " {'Teachers'->Teacher,s_id,ID,teacher,'teaches'}";
                
        
    }
}

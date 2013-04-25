package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class rollno extends XJDataMapper{

    public rollno() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_student_rollno";
        selfClass = "rollno";
        hasOne = "{'student'->student,student_id,id,xj_student,xj_student_rollno,rollno,rollno}";
    }
    
    
    
}

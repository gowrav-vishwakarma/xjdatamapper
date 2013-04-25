
package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class student  extends XJDataMapper{

    public student() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_student";
        selfClass = "student";
        hasOne = "{'class'->class_session,class_session_id,id,xj_class_session,xj_student,student,students}"
               + "{'scholar'->scholar,scholar_id,id,xj_scholar,xj_student,student,students}"
               + "{'rollno'->rollno,id,student_id,xj_student_rollno,xj_student,student,student}"
               + "{'session'->session,session_id,id,xj_session,xj_student,student,students}"
               + "{'studentrecord'->feesapplicable,id,student_id,xj_feesapplicable,xj_student,student,studentinfo}";
        
        hasMany = "{'fees'->feesapplicable,id,student_id,xj_feesapplicable,xj_student,student,students}";
        
    }
    
}

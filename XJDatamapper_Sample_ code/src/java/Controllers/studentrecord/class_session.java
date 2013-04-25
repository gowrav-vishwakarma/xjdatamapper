package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class class_session extends XJDataMapper{

    public class_session() throws ClassNotFoundException, SQLException, InstantiationException {
        
        super();
        table = "xj_class_session";
        selfClass = "class_session";
        hasOne = "{'classinfo'->class_detail,class_id,id,xj_class_detail,xj_class_session,class_session,classrecord)}"
               + "{'sessioninfo'->session,session_id,id,xj_session,xj_class_session,class_session,sessionrecord)}";
        hasMany ="{'students'->student,id,class_session_id,xj_student,xj_class_session,class_session,class}";
        
                

    }
    
    
    
}

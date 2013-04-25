package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class session extends XJDataMapper{

    public session() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_session";
        selfClass = "session";
        hasOne = "{'sessionrecord'->class_session,id,session_id,xj_class_session,xj_session,class_detail,sessioninfo)}";
        hasMany = "{'classes'->class_session,id,session_id,xj_class_session,xj_session,session,sessions}"
                + "{'students'->student,id,session_id,xj_student,xj_session,session,session}"
                + "{'scholars'->student,id,session_id,xj_session,xj_student,session,sessions}";
                
    }
    
    
    
}


package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;


public class scholar extends XJDataMapper{

    public scholar() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_scholar";
        selfClass = "scholar";
        hasMany = "{'students'->student,id,scholar_id,xj_student,xj_scholar,scholar,scholar}"
                + "{'sessions'->student,id,scholar_id,xj_student,xj_session,scholar,scholars}";
        
    }
    
    
}

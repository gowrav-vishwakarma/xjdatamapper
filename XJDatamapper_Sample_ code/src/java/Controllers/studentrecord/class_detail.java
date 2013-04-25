package Controllers.studentrecord;
import Controllers.XJDataMapper;
import java.sql.SQLException;

public class class_detail extends XJDataMapper{
    
    public class_detail () throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
    {
        super();
        table = "xj_class_detail";
        selfClass = "class_detail";
        hasOne = "{'classrecord'->class_session,id,class_id,xj_class_session,xj_class_detail,class_detail,classinfo)}";       
        hasMany = "{'sessions'->class_session,id,class_id,xj_class_session,xj_class_detail,class_detail,classes}";
        
    }
    
}

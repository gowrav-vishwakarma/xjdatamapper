    package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class fees extends XJDataMapper{

    public fees() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_fees";
        selfClass = "fees";
        hasOne = "{'feesmaster'->fees,feeshead_id,id,xj_fees,xj_feeshead,fees,fees}"
               + "{'feesrecord'->feesapplicable,id,fees_id,xj_feesapplicable,xj_fees,fees,feeinfo}";
        hasMany = "{'students'->student,student_id,id,xj_student,xj_feesapplicable,feesapplicable,fees}";
    }
    
    
    
}

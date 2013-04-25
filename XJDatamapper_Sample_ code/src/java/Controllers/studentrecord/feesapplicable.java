package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;

public class feesapplicable extends XJDataMapper{

    public feesapplicable() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_feesapplicable";
        selfClass = "feesapplicable";
        hasOne = "{'feeinfo'->fees,fees_id,id,xj_fees,xj_feesapplicable,feesapplicable,feesrecord}"
               + "{'studentinfo'->student,student_id,id,xj_student,xj_feesapplicable,feesapplicable,studentrecord}";
    }
    
    
}

package Controllers.studentrecord;

import Controllers.XJDataMapper;
import java.sql.SQLException;


public class feeshead extends XJDataMapper{

    public feeshead() throws ClassNotFoundException, SQLException {
        
        super();
        table = "xj_feeshead";
        selfClass = "feeshead";
        hasMany = "{'fees'->fees,id,feeshead_id,xj_fees,xj_feeshead,feeshead,feesmaster}";
    }
    
    
    
}

package Controllers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class XJData extends XConfiguration {

    private int cond;
    private int hcond;
    private int ocond;
    private int gcond;
    private int group_Start;
    private int group_Or_Start;
    private int group_Not_Start;
    private int group_Or_Not_Start;
    private int affectedRows;
    private int set;
    private String limit = "";
    private String select;
    private String where;
    protected String table;
    protected String tablePrefix = "";
    private String completeQuery;
    private String space;
    private String orderby;
    private String groupby;
    private String having;
    private String delete;
    private String update;
    private String aggrValue;
    private String offset = "";
    protected static String hasOne = "";
    protected static String hasMany = "";
    private String relatedClass = "";
    private String join_self_as = "";
    private String join_other_as = "";
    private String relatedTable = "";
    private String otherRel = "";
    private String selfTable = "";
    protected String selfClass = "";
    public XJData[] data;
    private ArrayList<String> whereList = new ArrayList<String>();
    private ArrayList<String> insertList = new ArrayList<String>();
    private ArrayList<String> updateList = new ArrayList<String>();
    private ArrayList<String> updateFieldList = new ArrayList<String>();
    private ArrayList<String> updateValueList = new ArrayList<String>();
    private ArrayList<String> groupbyList = new ArrayList<String>();
    private ArrayList<String> field = new ArrayList<String>();
    private ArrayList<String> value = new ArrayList<String>();
    private ArrayList<String> selected = new ArrayList<String>();
    private ArrayList<String> selectedField = new ArrayList<String>();
    public HashMap<String, String> fieldValue = new HashMap<String, String>();
    private HashMap<String, String> updateHashList = new HashMap<String, String>();
    private HashMap<String, String> orderHashList = new HashMap<String, String>();
    private static Connection conn;
    

    /* constructor */
    
    public XJData() throws ClassNotFoundException, SQLException {
       
        super();
        this.completeQuery = "";
        this.where = " where ";
        this.space = " ";
        this.orderby = " order by ";
        this.groupby = " group by ";
        this.having = " having ";
        this.select = " select ";
        this.delete = " delete from ";
        this.update = " update ";
        this.selected.clear();
        this.selectedField.clear();

    }

    //~~~~~~~~~~~~~~save methods~~~~~~~~~~~~~~
    
    /* save record, values can be stored in any order */
    public int save() throws SQLException, ClassNotFoundException {

        String sql = " insert into " + this.tablePrefix + this.table + "(";
        for (int i = 0; i < field.size(); i++) {
            sql += "" + field.get(i) + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql += ") values (";
        for (int i = 0; i < value.size(); i++) {
            sql += "'" + value.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql += ")";
        DBOperations dbobj = new DBOperations();
        this.completeQuery = sql;
        this.affectedRows = dbobj.ModifyQuery(sql);
        this.clear();
        return this.affectedRows;

    }
    
    /*save record, values are stored as per the order of fields, skipping field(id) that is entered automatically*/

    public int save(ArrayList al) throws SQLException, ClassNotFoundException {

        this.insertList = al;
        String sql = " insert into " + this.tablePrefix + this.table + "(";
        ResultSet colName, colNo;
        colName = findingColumns(this.tablePrefix + this.table);
        colName.next();
        colNo = findingNumColumns(this.tablePrefix + this.table);
        while (colName.next()) {
            sql += "" + colName.getString("COLUMN_NAME") + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql += ") values (";
        for (int i = 0; i < insertList.size(); i++) {
            sql += "'" + insertList.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql += ")";

        DBOperations dbobj = new DBOperations();
        this.completeQuery = sql;
        this.affectedRows = dbobj.ModifyQuery(sql);
        this.clear();
        return this.affectedRows;

    }

   /* save multiple records , values are stored as per the order of fields, skipping field(id) that is entered automatically*/
    
    public int saveMultiple(String list) throws SQLException, ClassNotFoundException {

        String sql = " insert into " + this.tablePrefix + this.table + "(";
        ResultSet colName, colNo;
        colName = findingColumns(this.tablePrefix + this.table);
        colName.next();
        colNo = findingNumColumns(this.tablePrefix + this.table);
        while (colName.next()) {
            sql += "" + colName.getString("COLUMN_NAME") + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql += ") values ";
        sql += list;
        DBOperations dbobj = new DBOperations();
        this.completeQuery = sql;
        this.affectedRows = dbobj.ModifyQuery(sql);
        this.clear();
        return this.affectedRows;

    }

    //~~~~~~~~~~~~~~update methods~~~~~~~~~~~~~~
    
    /* update record extracted using get() in self table*/
    
    public int update() throws SQLException, ClassNotFoundException {

        String table = this.tablePrefix + this.table;
        String sql = "";
        this.update += this.tablePrefix + this.space + table + " set ";
        for (int i = 0; i < this.field.size(); i++) {
            sql += this.space + table + "." + this.field.get(i) + " = " + "'" + this.value.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.selfClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.update;
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.clear();
        return this.affectedRows;

    }

    /* update record extracted using get() in related table*/
    
    public int Rupdate() throws SQLException, ClassNotFoundException {

        String table = this.relatedTable;
        String sql = "";
        this.update += this.tablePrefix + this.space + table + " set ";
        for (int i = 0; i < this.field.size(); i++) {
            sql += this.space + table + "." + this.field.get(i) + " = " + "'" + this.value.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.relatedClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.update;
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.clear();
        return this.affectedRows;

    }
    
    /* update record extracted using get in self table, it will update fields stored in arraylist with the values stored in arraylist   */

    public int update(ArrayList fields, ArrayList values) throws ClassNotFoundException, SQLException {

        this.updateFieldList = fields;
        this.updateValueList = values;
        String table = this.table;
        String sql = "";
        this.update += this.tablePrefix + table + " set ";
        for (int i = 0; i < this.updateFieldList.size(); i++) {
            sql += this.space + table + "." + this.updateFieldList.get(i) + " = " + "'" + this.updateValueList.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.selfClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.update;
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.clear();
        return this.affectedRows;

    }

    /* update record extracted using get in related table,it will update fields stored in arraylist with the values stored in arraylist   */
    
    public int Rupdate(ArrayList fields, ArrayList values) throws ClassNotFoundException, SQLException {

        this.updateFieldList = fields;
        this.updateValueList = values;
        String table = this.relatedTable;
        String sql = "";
        this.update += this.tablePrefix + table + " set ";
        for (int i = 0; i < this.updateFieldList.size(); i++) {
            sql += this.space + table + "." + this.updateFieldList.get(i) + " = " + "'" + this.updateValueList.get(i) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.relatedClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.update;
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.clear();
        return this.affectedRows;

    }
    
/* update record extracted using get in self table, it will update fields with the values stored in arraylist  
   each array element contains the field and its updated values
   field and value is seperated by -> symbol   */
    
    public int update(ArrayList<String> values) throws ClassNotFoundException, SQLException {

        this.updateList = values;
        String table = this.table;
        String newSql = "";
        this.update += this.tablePrefix + table + " set ";
        for (int i = 0; i < this.updateList.size(); i++) {
            String temp = this.updateList.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + "=" + this.space + "'" + splitted[1] + "',";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf(","));
        this.update += newSql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.selfClass + ".id");
            this.update += whr;
        }

        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.completeQuery = this.update;
        this.clear();
        return this.affectedRows;

    }
    
    /* update record extracted using get in related table, it will update fields with the values stored in arraylist .
   each array element contains the field and its updated values
   field and value is seperated by -> symbol   */


    public int Rupdate(ArrayList<String> values) throws ClassNotFoundException, SQLException {

        this.updateList = values;
        String newSql = "";
        this.update += this.tablePrefix + this.table + " set ";
        for (int i = 0; i < this.updateList.size(); i++) {
            String temp = this.updateList.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + this.table + "." + splitted[0] + this.space + "=" + this.space + "'" + splitted[1] + "',";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf(","));
        this.update += newSql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.relatedClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.completeQuery = this.update;
        this.clear();
        return this.affectedRows;

    }
    
    /* update record extracted using get in self table, it will update fields with the values stored in hashmap  
   each hashmap element contains the field and its updated values */
   

    public int update(HashMap updateList) throws ClassNotFoundException, SQLException {

        this.updateHashList = updateList;
        String table = this.table;
        String sql = "";
        this.update += this.tablePrefix + table + " set ";
        Iterator iterator = updateHashList.keySet().iterator();
        String str[] = new String[updateHashList.size()];
        int index = 0;
        while (iterator.hasNext()) {
            str[index] = (String) iterator.next();
            index++;
        }
        for (int i = 0; i < updateHashList.size(); i++) {
            sql += this.space + table + "." + str[i] + " = " + "'" + updateHashList.get(str[i]) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.selfClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.completeQuery = this.update;
        this.clear();
        return this.affectedRows;

    }
    
    /* update record extracted using get in related table, it will update fields with the values stored in hashmap  
   each hashmap element contains the field and its updated values */
   
    public int Rupdate(HashMap updateList) throws ClassNotFoundException, SQLException {

        this.updateHashList = updateList;
        String table = this.relatedTable;
        String sql = "";
        this.update += this.tablePrefix + table + " set ";
        Iterator iterator = updateHashList.keySet().iterator();
        String str[] = new String[updateHashList.size()];
        int index = 0;
        while (iterator.hasNext()) {
            str[index] = (String) iterator.next();
            index++;
        }
        for (int i = 0; i < updateHashList.size(); i++) {
            sql += this.space + table + "." + str[i] + " = " + "'" + updateHashList.get(str[i]) + "',";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        this.update += sql;
        if (cond > 0) {
            this.update += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.relatedClass + ".id");
            this.update += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.update);
        this.completeQuery = this.update;
        this.clear();
        return this.affectedRows;

    }

    //~~~~~~~~~~~~~~remove methods~~~~~~~~~~~~~~
    
    /* remove record extracted using get in self table */
    public int remove() throws ClassNotFoundException, SQLException {

        String table = this.table;
        this.delete += this.tablePrefix + this.space + table;
        if (cond > 0) {
            this.delete += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.selfClass + ".id");
            this.delete += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.delete);
        this.completeQuery = this.delete;
        this.clear();
        return this.affectedRows;

    }
/* remove record extracted using get in related table */
    public int Rremove() throws ClassNotFoundException, SQLException {

        String table = this.relatedTable;
        this.delete += this.tablePrefix + this.space + table;
        if (cond > 0) {
            this.delete += this.where;
        } else {
            String whr = " where " + this.space + table + ".id" + this.space + "=" + this.space + this.getSingle(this.relatedClass + ".id");
            this.delete += whr;

        }
        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(this.delete);
        this.completeQuery = this.delete;
        this.clear();
        return this.affectedRows;

    }

    //~~~~~~~~~~~~~~where methods~~~~~~~~~~~~~~
    
    /* where condition in self table*/
    
    public XJData where(String field, String op, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition in related table*/
    
    public XJData Rwhere(String field, String op, String value) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /*where field_id = (id of record contained by obj) in self table*/
    
    public XJData where(String field_id, XJData obj) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field_id + this.space + "=" + this.space + obj.getSingle(obj.getClassName() +".id") + ")";
        } else {
            this.where += " and (" + this.space + table + "." + field_id + this.space + "=" + this.space + obj.getSingle(obj.getClassName() +".id") + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /*where field_id = (id of record contained by obj) in related table*/
    
    public XJData Rwhere(String field_id, XJData obj) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field_id + this.space + "=" + this.space + obj.getSingle(obj.getRelatedClassName() +".id") + ")";
        } else {
            this.where += " and (" + this.space + table + "." + field_id + this.space + "=" + this.space + obj.getSingle(obj.getRelatedClassName() +".id") + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for self table */

    public XJData where(ArrayList<String> array) {
        
        this.whereList = array;
        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
/* 'where' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for related table */
    
    public XJData Rwhere(ArrayList<String> array) {

        this.whereList = array;
        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where_or  condition in self table*/
    
    public XJData where_Or(String field, String op, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.where += " or( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where_or condition in related table*/
    
    public XJData Rwhere_Or(String field, String op, String value) {

        String table = relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.where += " or( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_or' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for self table */

    public XJData where_Or(ArrayList<String> array) {
        
        this.whereList = array;
        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_or' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for related table */
    
    public XJData Rwhere_Or(ArrayList<String> array) {

        this.whereList = array;
        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is in array. works for self table */
    
    public XJData where_In(String field, ArrayList values) {

        this.whereList = values;
        String table = this.table;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " in " + str + ")";
        } else {
            this.where += " and ( " + this.space + table + "." + field + " in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is in array. works for related table */
    
    public XJData Rwhere_In(String field, ArrayList values) {

        this.whereList = values; 
        String table = relatedTable;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " in " + str + ")";
        } else {
            this.where += " and ( " + this.space + table + "." + field + " in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_or_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is in array. works for self table */
    
    public XJData where_Or_In(String field, ArrayList values) {
        
        this.whereList = values;
        String table = this.table;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " in " + str + ")";
        } else {
            this.where += " or ( " + this.space + table + "." + field + " in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_or_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is in array. works for related table */
    
    public XJData Rwhere_Or_In(String field, ArrayList values) {

        String table = relatedTable;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " in " + str + ")";
        } else {
            this.where += " or ( " + this.space + table + "." + field + " in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_not_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is not in array. works for self table */
    
    public XJData where_Not_In(String field, ArrayList values) {

        this.whereList = values;
        String table = this.table;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " not in " + str + ")";
        } else {
            this.where += " and ( " + this.space + table + "." + field + " not in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_not_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is not in array. works for related table */
    
    public XJData Rwhere_Not_In(String field, ArrayList values) {

        this.whereList = values;
        String table = relatedTable;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " not in " + str + ")";
        } else {
            this.where += " and ( " + this.space + table + "." + field + " not in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_or_not_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is not in array. works for self table */
    
    public XJData where_Or_Not_In(String field, ArrayList values) {
        
        this.whereList = values;
        String table = this.table;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " not in " + str + ")";
        } else {
            this.where += " or ( " + this.space + table + "." + field + " not in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_or_not_in' condition specified in array. each array element specifies one value. field value is compared with the values in array list . where value is not in array. works for related table */
    
    public XJData Rwhere_Or_Not_In(String field, ArrayList values) {

        this.whereList = values;
        String table = relatedTable;
        String str = "(";
        for (int i = 0; i < values.size(); i++) {
            str += "'" + values.get(i) + "',";
        }
        str = str.substring(0, str.lastIndexOf(","));
        str += ")";
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + " not in " + str + ")";
        } else {
            this.where += " or ( " + this.space + table + "." + field + " not in " + str + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_between' tells where field is between value1 and value2 . works for self table */

    public XJData where_Between(String field, String value1, String value2) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_between' tells where field is between value1 and value2 . works for related table */
    
    public XJData Rwhere_Between(String field, String value1, String value2) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_or_between' tells where field is between value1 and value2 . works for self table */

    public XJData where_Or_Between(String field, String value1, String value2) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " or (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_or_between' tells where field is between value1 and value2 . works for related table */
    
    public XJData Rwhere_Or_Between(String field, String value1, String value2) {

        String table = relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " or (" + this.space + table + "." + field + this.space + "between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'where_not_between' tells where field is not between value1 and value2 . works for self table */
    
    public XJData where_Not_Between(String field, String value1, String value2) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_not_between' tells where field is not between value1 and value2 . works for related table */

    public XJData Rwhere_Not_Between(String field, String value1, String value2) {

        String table = relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " and (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_or_not_between' tells where field is not between value1 and value2 . works for self table */

    public XJData where_Or_Not_Between(String field, String value1, String value2) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " or (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'where_or_not_between' tells where field is not between value1 and value2 . works for related table */

    public XJData Rwhere_Or_Not_Between(String field, String value1, String value2) {

        String table = relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        } else {
            this.where += " or (" + this.space + table + "." + field + this.space + " not between" + this.space + "'" + value1 + "'" + this.space + " and " + this.space + "'" + value2 + "')";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    //~~~~~~~~~~~~~~order by methods~~~~~~~~~~~~~~
    
    /* order field by the given asc or desc type in self table */
    
    public XJData orderby(String field, String type) {

        String table = this.table;
        if (ocond == 0) {
            this.orderby += this.space + table + "." + field + this.space + type;
        } else {
            this.orderby += "," + this.space + table + "." + field + this.space + type;
        }
        this.ocond++;
        this.completeQuery = this.orderby;
        return this;

    }

    /* order field by the given asc or desc type  in related table */
    
    public XJData Rorderby(String field, String type) {

        String table = this.relatedTable;
        if (ocond == 0) {
            this.orderby += this.space + table + "." + field + this.space + type;
        } else {
            this.orderby += "," + this.space + table + "." + field + this.space + type;
        }
        this.ocond++;
        this.completeQuery = this.orderby;
        return this;

    }
    
    /* order field by the given asc or desc type given in the hash list. first arguement of orderbyList is the field , second is  type. works for self table */

    public XJData orderby(HashMap orderbyList) {

        this.orderHashList = orderbyList;
        String table = this.table;
        String sql = "";
        Iterator iterator = orderbyList.keySet().iterator();
        String str[] = new String[orderbyList.size()];
        int index = 0;
        while (iterator.hasNext()) {
            str[index] = (String) iterator.next();
            index++;
        }
        for (int i = 0; i < orderbyList.size(); i++) {
            sql += this.space + table + "." + str[i] + this.space + "" + orderbyList.get(str[i]) + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        if (ocond == 0) {
            this.orderby += this.space + sql;
        } else {
            this.orderby += "," + this.space + sql;
        }
        this.ocond++;
        this.completeQuery = this.orderby;
        return this;
    }

    /* order field by the given asc or desc type given in the hash list. first arguement of orderbyList is the field , second is  type. works for related table */
    
    public XJData Rorderby(HashMap orderbyList) {

        this.orderHashList = orderbyList;
        String table = this.relatedTable;
        String sql = "";
        Iterator iterator = orderbyList.keySet().iterator();
        String str[] = new String[orderbyList.size()];
        int index = 0;
        while (iterator.hasNext()) {
            str[index] = (String) iterator.next();
            index++;
        }
        for (int i = 0; i < orderbyList.size(); i++) {
            sql += this.space + table + "." + str[i] + this.space + "" + orderbyList.get(str[i]) + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        if (ocond == 0) {
            this.orderby += this.space + sql;
        } else {
            this.orderby += "," + this.space + sql;
        }
        this.ocond++;
        this.completeQuery = this.orderby;
        return this;

    }

    //~~~~~~~~~~~~~~group by methods~~~~~~~~~~~~~~
    
    /*group records on the basis of field in self table*/
    
    public XJData groupby(String field) {

        String table = this.table;
        if (gcond == 0) {
            this.groupby += this.space + table + "." + field;
        } else {
            this.groupby += "," + this.space + table + "." + field;
        }
        this.gcond++;
        this.completeQuery = this.groupby;
        return this;

    }

    /*group records on the basis of field in related table*/
    
    public XJData Rgroupby(String field) {

        String table = this.relatedTable;
        if (gcond == 0) {
            this.groupby += this.space + table + "." + field;
        } else {
            this.groupby += "," + this.space + table + "." + field;
        }
        this.gcond++;
        this.completeQuery = this.groupby;
        return this;

    }

    /*group records on the basis of fields contained in the array. works for self table*/
    
    public XJData groupby(ArrayList<String> array) {

        this.groupbyList = array;
        String table = this.table;
        String sql = "";
        for (int i = 0; i < array.size(); i++) {
            sql += this.space + table + "." + array.get(i) + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        if (gcond == 0) {
            this.groupby += this.space + sql;
        } else {
            this.groupby += "," + this.space + sql;
        }
        this.gcond++;
        this.completeQuery = this.groupby;
        return this;

    }

    /*group records on the basis of fields contained in the array. works for related table*/
    
    public XJData Rgroupby(ArrayList<String> array) {

        this.groupbyList = array;
        String table = this.relatedTable;
        String sql = "";
        for (int i = 0; i < array.size(); i++) {
            sql += this.space + table + "." + array.get(i) + ",";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        if (gcond == 0) {
            this.groupby += this.space + sql;
        } else {
            this.groupby += "," + this.space + sql;
        }
        this.gcond++;
        this.completeQuery = this.groupby;
        return this;

    }

    //~~~~~~~~~~~~~~having methods~~~~~~~~~~~~~~
    
    /* works for self table*/
    
    public XJData having(String field, String op, String value) {

        String table = this.table;
        if (this.hcond == 0) {
            this.having += "(" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " and( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* works for related table*/
    
    public XJData Rhaving(String field, String op, String value) {

        String table = this.relatedTable;
        if (this.hcond == 0) {
            this.having += "(" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " and( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for self table */
        
    public XJData having(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " and (" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for related table */
    
    public XJData Rhaving(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and ";
        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " and (" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_or condition . works for self table*/
    
    public XJData having_Or(String field, String op, String value) {

        String table = this.table;
        if (this.hcond == 0) {
            this.having += "(" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " or( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_or condition . works for related table*/
    
    public XJData Rhaving_Or(String field, String op, String value) {

        String table = this.relatedTable;
        if (this.hcond == 0) {
            this.having += "(" + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " or( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having_or' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for self table */    
    
    public XJData having_Or(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " or (" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having_or' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. works for related table */
    
    public XJData Rhaving_Or(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " or (" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_not condition. field is not having value. works for self table */
    
    public XJData having_Not(String field, String op, String value) {

        String table = this.table;
        if (this.hcond == 0) {
            this.having += "( not " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " and not ( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_not condition. field is not having value.  works for related table */
    
    public XJData Rhaving_Not(String field, String op, String value) {

        String table = this.relatedTable;
        if (this.hcond == 0) {
            this.having += "( not " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " and not ( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having_not' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. field is not having value.  works for self table */
    
    public XJData having_Not(ArrayList<String> array) {

        String table = this.table;
        String newSql = " not ";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and not ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and not"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " and not (" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having_not' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. field is not having value.  works for related table */
    
    public XJData Rhaving_Not(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = " not ";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' and not ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and not"));

        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " and not(" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_or_not condition . field is not having value.  works for self table*/
    
    public XJData having_Or_Not(String field, String op, String value) {

        String table = this.table;
        if (this.hcond == 0) {
            this.having += "( not " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " or not ( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* having_or_not condition . field is not having value.  works for related table*/
    
    public XJData Rhaving_Or_Not(String field, String op, String value) {

        String table = this.relatedTable;
        if (this.hcond == 0) {
            this.having += "( not " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        } else {
            this.having += " or not ( " + this.space + table + "." + field + this.space + op + this.space + "'" + value + "')";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    /* 'having_or_not' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. field is not having value.  works for self table */
    
    public XJData having_Or_Not(ArrayList<String> array) {

        String table = this.table;
        String newSql = " not ";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or not ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or not"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " or not(" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }
    
    /* 'having_or_not' condition specified in array. each array element specifies one condition. field , condition operator, value are seperated  using -> symbol. field is not having value.  works for related table */
    

    public XJData Rhaving_Or_Not(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = " not ";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + this.space + splitted[1] + this.space + "'" + splitted[2] + "' or not ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or not"));
        if (hcond == 0) {
            this.having += "(" + newSql + ")";
        } else {
            this.having += " or not(" + newSql + ")";
        }
        hcond++;
        this.completeQuery = this.having;
        return this;

    }

    //~~~~~~~~~~~~~~where , get related methods~~~~~~~~~~~~~~
    
    /* sets the relation on where condition. field and value are of related table */
    
    public XJData where_Related(String relation, String field, String op, String value) throws Exception {

        setRelation(relation);
        Rwhere(field, op, value);
        return this;

    }

    /* sets the relation on given condition. field and value are of related table. returns the joined records  */

    public XJData[] get_By_Related(String relation, String field, String op, String value) throws Exception {

        setRelation(relation);
        Rwhere(field, op, value);
        return get();

    }
    
   /* sets the relation on where condition. condition is given in terms of array elements. */
    
    public XJData where_Related(String relation, ArrayList<String> array) throws Exception {

        setRelation(relation);
        Rwhere(array);
        return this;

    }
    
    /* sets the relation as per condition given in array list. returns the joined records */
    
    public XJData[] get_By_Related(String relation, ArrayList<String> array) throws Exception {

        setRelation(relation);
        Rwhere(array);
        return get();

    }

    /* sets the relation on where condition. field value is compared with the values given in the array list. field and value are of related table  */
    
    public XJData where_Related_In(String relation, String field, ArrayList values) throws Exception {

        setRelation(relation);
        Rwhere_In(field, values);
        return this;

    }
    
    /* sets the relation on where condition. field value is compared with the values given in the array list. field and value are of related table. returns joined  records */

    public XJData[] get_By_Related_In(String relation, String field, ArrayList values) throws Exception {

        setRelation(relation);
        Rwhere_In(field, values);
        return get();

    }

    /* sets the relation on where condition. field value is compared with the values given in the array list. field and value are of related table  */
    
    public XJData where_Related_Not_In(String relation, String field, ArrayList values) throws Exception {

        setRelation(relation);
        Rwhere_Not_In(field, values);
        return this;

    }
    
    /* sets the relation on where condition. field value is compared with the values given in the array list. field and value are of related table. returns joined records  */

    public XJData[] get_By_Related_Not_In(String relation, String field, ArrayList values) throws Exception {

        setRelation(relation);
        Rwhere_Not_In(field, values);
        return get();

    }

    //~~~~~~~~~~~~~~like methods~~~~~~~~~~~~~~
    
    
    /* where field like value. works when there is exact match. works for self table*/
    
    public XJData like(String field, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "')";
        } else {
            this.where += " and( " + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where field like value. works when there is exact match. works for related table*/
    
    public XJData Rlike(String field, String value) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "')";
        } else {
            this.where += " and( " + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition is givem in array. field and value is seperated by -> symbol. works when there is exact match. works for self table*/
    
    public XJData like(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " like " + "'" + splitted[1] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition is givem in array. field and value is seperated by -> symbol. works when there is exact match. works for related table*/
    
    public XJData Rlike(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " like " + "'" + splitted[1] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where field like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern  works for self table*/

    public XJData like(String field, String value, String place) {

        String table = this.table;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where field like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern  works for related table*/
    
    public XJData Rlike(String field, String value, String place) {

        String table = this.relatedTable;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'like_or' tells where field like value. works when there is exact match. works for self table*/
    
    public XJData like_Or(String field, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "')";
        } else {
            this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* 'like_or' tells where field like value. works when there is exact match. works for related table*/
    
    public XJData Rlike_Or(String field, String value) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "')";
        } else {
            this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where condition is givem in array. field and value is seperated by -> symbol. works when there is exact match. works for self table*/

    public XJData like_Or(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " like " + "'" + splitted[1] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition is givem in array. field and value is seperated by -> symbol. works when there is exact match. works for related table*/
    
    public XJData Rlike_Or(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " like " + "'" + splitted[1] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where field like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern  works for self table*/
    
    public XJData like_Or(String field, String value, String place) {

        String table = this.table;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where field like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern  works for self table*/
    
    public XJData Rlike_Or(String field, String value, String place) {

        String table = this.relatedTable;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'like_not' tells where field not like value. works when there is exact match. works for self table*/

    public XJData like_Not(String field, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "')";
        } else {
            this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "') ";
        }
        cond++;
        return this;

    }

    /* 'like_not' tells where field not like value. works when there is exact match. works for related table*/
    
    public XJData Rlike_Not(String field, String value) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "')";
        } else {
            this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition is givem in array. like_not tells where field not like value. field and value is seperated by -> symbol. works when there is exact match. works for self table*/
    
    public XJData like_Not(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " not like " + "'" + splitted[1] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    /* where condition is givem in array. like_not tells where field not like value. field and value is seperated by -> symbol. works when there is exact match. works for related table*/
    
    public XJData Rlike_Not(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " not  like " + "'" + splitted[1] + "' and ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("and"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " and (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where field not like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern . works for self table*/

    public XJData like_Not(String field, String value, String place) {

        String table = this.table;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where field not like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern . works for related table*/

    public XJData Rlike_Not(String field, String value, String place) {

        String table = this.relatedTable;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " and(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    
    /* 'like_or_not' tells where field not like value. works when there is exact match. works for self table*/

    public XJData like_Or_Not(String field, String value) {

        String table = this.table;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "')";
        } else {
            this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* 'like_or_not' tells where field not like value. works when there is exact match. works for related table*/

    public XJData Rlike_Or_Not(String field ,String value) {

        String table = this.relatedTable;
        if (cond == 0) {
            this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "')";
        } else {
            this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "') ";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where condition is givem in array. like_or_not tells where field not like value. field and value is seperated by -> symbol. works when there is exact match. works for self table*/

    public XJData like_Or_Not(ArrayList<String> array) {

        String table = this.table;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " not like " + "'" + splitted[1] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
        /* where condition is givem in array. like_or_not tells where field not like value. field and value is seperated by -> symbol. works when there is exact match. works for related table*/

    public XJData Rlike_Or_Not(ArrayList<String> array) {

        String table = this.relatedTable;
        String newSql = "";
        for (int i = 0; i < array.size(); i++) {
            String temp = array.get(i);
            String[] splitted = temp.split("->");
            newSql += this.space + table + "." + splitted[0] + " not like " + "'" + splitted[1] + "' or ";

        }
        newSql = newSql.substring(0, newSql.lastIndexOf("or"));
        if (cond == 0) {
            this.where += "(" + newSql + ")";
        } else {
            this.where += " or (" + newSql + ")";
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where field not like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern . works for self table*/

    public XJData like_Or_Not(String field, String value, String place) {

        String table = this.table;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value+ "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }
    
    /* where field not like value. works also when there is not exact match. place(after, before, both) decides where to look the pattern . works for related table*/

    public XJData Rlike_Or_Not(String field, String value, String place) {

        String table = this.relatedTable;
        if (cond == 0) {
            if (place.equalsIgnoreCase("before")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " (" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";

            }
        } else {
            if (place.equalsIgnoreCase("before")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "')";
            }
            if (place.equalsIgnoreCase("after")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'" + value + "%')";
            }
            if (place.equalsIgnoreCase("both")) {
                this.where += " or(" + this.space + table + "." + field + this.space + " not like " + this.space + "'%" + value + "%')";
            }
        }
        cond++;
        this.completeQuery = this.where;
        return this;

    }

    //~~~~~~~~~~~~~ get methods ~~~~~~~~~~~~~~~``````
    
    
    /* return the aggregate function value */
    
    public String getAggregate() {

        return this.aggrValue;

    }

    /* return the affected number of rows */
    
    public int getAffectedRows() {

        return this.affectedRows;

    }
    
    /* returns the record where field is having value [=,<,<=,>,>=,!=] the value passed as parameter . works for self table */

    
    public XJData[] get_By(String field, String value) throws ClassNotFoundException, SQLException, Exception {

        where(field, "=", value);
        return get();

    }
    
    /* returns the record where field is having value [=,<,<=,>,>=,!=] the value passed as parameter . works for self table */

    public XJData[] get_Where(String field, String op, String value) throws ClassNotFoundException, SQLException, Exception {

        where(field, op, value);
        return get();

    }
    
    /* returns the records */

    public XJData[] get() throws ClassNotFoundException, SQLException, Exception {

        if (!selected.isEmpty()) {
            return get(selected);
        }
        if (!selectedField.isEmpty()) {
            String table = "";
            if (!this.relatedTable.isEmpty()) {
                table = this.relatedTable;
            } else {
                table = this.table;
            }
            this.select += " " + this.selectedField.get(0) + "(" + this.selectedField.get(1) + ") from " + this.tablePrefix + this.space + table;
            if (cond > 0) {
                this.select += this.where;
            }
            if (gcond > 0) {
                this.select += this.groupby;
            }
            if (hcond > 0) {
                this.select += this.having;
            }
            if (ocond > 0) {
                this.select += this.orderby;
            }
            if (!this.limit.isEmpty() && !this.offset.isEmpty()) {
                this.select += " limit" + this.space + this.offset + "," + this.limit + "";
            } else if (!this.limit.isEmpty()) {
                this.select += " limit " + "" + this.limit + "";
            }
            System.out.println("select aggregate " + this.select);
            DBOperations dbobj = new DBOperations();
            this.completeQuery = this.select;
            return getAndSet(dbobj.SelectQuery(this.select));
        }
        if (!this.relatedTable.isEmpty()) {

            if (set == 0) {
                this.select += " " + this.tablePrefix + this.relatedTable + " .* from " + this.tablePrefix + this.relatedTable + "";
            }
            if (cond > 0) {
                this.select += this.where;
            }
            if (gcond > 0) {
                this.select += this.groupby;
            }
            if (hcond > 0) {
                this.select += this.having;
            }
            if (ocond > 0) {
                this.select += this.orderby;
            }
            if (!this.limit.isEmpty() && !this.offset.isEmpty()) {
                this.select += " limit" + this.space + this.offset + "," + this.limit + "";
            } else if (!this.limit.isEmpty()) {
                this.select += " limit " + "" + this.limit + "";
            }
            System.out.println("select related " + this.select);
            DBOperations dbobj = new DBOperations();
            this.completeQuery = this.select;
            return getAndSet(dbobj.SelectQuery(this.select));
        } else {
            this.select += " " + this.tablePrefix + this.table + " .* from " + this.tablePrefix + this.table + "";
            if (cond > 0) {
                this.select += this.where;
            }
            if (gcond > 0) {
                this.select += this.groupby;
            }
            if (hcond > 0) {
                this.select += this.having;
            }
            if (ocond > 0) {
                this.select += this.orderby;
            }
            if (!this.limit.isEmpty() && !this.offset.isEmpty()) {
                this.select += " limit" + this.space + this.offset + "," + this.limit + "";
            } else if (!this.limit.isEmpty()) {
                this.select += " limit " + "" + this.limit + "";
            }
            System.out.println("select normal " + this.select);
            DBOperations dbobj = new DBOperations();
            this.completeQuery = this.select;
            return getAndSet(dbobj.SelectQuery(this.select));

        }

    }
    
    /* returns the record upto the limit specified */

    public XJData[] get(int limit) throws ClassNotFoundException, SQLException, Exception {
        
        if (!selected.isEmpty()) {
            this.limit=""+limit;
            return get(selected);
        }

        String table = "";
        if (!this.relatedTable.isEmpty()) {
            table = this.relatedTable;
        } else {
            table = this.table;
        }
        this.select += " " + this.tablePrefix + this.space + table + " .* from " + this.tablePrefix + this.space + table + "";
        if (cond > 0) {
            this.select += this.where;
        }
        if (gcond > 0) {
            this.select += this.groupby;
        }
        if (hcond > 0) {
            this.select += this.having;
        }
        if (ocond > 0) {
            this.select += this.orderby;
        }
        this.select += " limit " + "" + limit + "";
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.select;
        return getAndSet(dbobj.SelectQuery(select));

    }
    
    /* returns the record upto the limit & offset specified */

    public XJData[] get(int limit, int offset) throws ClassNotFoundException, SQLException, Exception {

        if (!selected.isEmpty()) {
            this.limit=""+limit;
            this.offset=""+offset;
            return get(selected);
        }
        String table = "";
        if (!this.relatedTable.isEmpty()) {
            table = this.relatedTable;
        } else {
            table = this.table;
        }
        this.select += " " + this.tablePrefix + this.space + table + " .* from " + this.tablePrefix + this.space + table + "";
        if (cond > 0) {
            this.select += this.where;
        }
        if (gcond > 0) {
            this.select += this.groupby;
        }
        if (hcond > 0) {
            this.select += this.having;
        }
        if (ocond > 0) {
            this.select += this.orderby;
        }
        this.select += " limit " + "" + offset + "," + limit + "";
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.select;
        return getAndSet(dbobj.SelectQuery(this.select));
    }
    
    /* returns the record as per the columns specified */

    public XJData[] get(ArrayList<String> colArray) throws ClassNotFoundException, SQLException, Exception {

        String table = "";
        if (!this.relatedTable.isEmpty()) {
            table = this.relatedTable;
        } else {
            table = this.table;
        }
        String str = "";
        for (int i = 0; i < colArray.size(); i++) {
            str += "" + this.tablePrefix + table + "." + colArray.get(i) + ",";
        }
        str = str.substring(0, str.lastIndexOf(","));
        this.select += str + " from " + this.tablePrefix + this.space + table + "";
        if (cond > 0) {
            this.select += this.where;
        }
        if (gcond > 0) {
            this.select += this.groupby;
        }
        if (hcond > 0) {
            this.select += this.having;
        }
        if (ocond > 0) {
            this.select += this.orderby;
        }
        if (!this.limit.isEmpty() && !this.offset.isEmpty()) {
            this.select += " limit" + this.space + this.offset + "," + this.limit + "";
        } else if (!this.limit.isEmpty()) {
            this.select += " limit " + "" + this.limit + "";
        }
        System.out.println("select colArray " + this.select);
        DBOperations dbobj = new DBOperations();
        this.completeQuery = this.select;
        return getAndSet(dbobj.SelectQuery(this.select), colArray);

    }

    //~~~~~~~~~~ get and set methods ~~~~~~~~~~~~~~~
    public XJData[] getAndSet(ResultSet resGet) throws ClassNotFoundException, SQLException, Exception {

        ResultSetMetaData rsMeta = resGet.getMetaData();
        int colCount = rsMeta.getColumnCount();
        if (!selectedField.isEmpty()) {
            data = new XJData[1];
            data[0] = (XJData) getChild();
            if (resGet.next()) {
                data[0].aggrValue = resGet.getString(1);
                this.aggrValue = resGet.getString(1);
            }
            this.clear();
            return data;
        }
        int cntSelf = 0, cntRelated = 0, i = 0, j = 0, k = 0, count = 0, end = 0, start = 0;
        while (resGet.next()) {
            count++;
        }
        ResultSet colNumInfoSelf = findingNumColumns(this.table);
        if (colNumInfoSelf.next()) {
            cntSelf = colNumInfoSelf.getInt(1);
        }
        String[] colNamesSelf = new String[cntSelf];
        ResultSet colInfoSelf = findingColumns(this.table);
        while (colInfoSelf.next()) {
            colNamesSelf[i] = colInfoSelf.getString("COLUMN_NAME");
            i++;
        }
        i = 0;
        ResultSet colNumInfoRelated = findingNumColumns(this.relatedTable);
        if (colNumInfoRelated.next()) {
            cntRelated = colNumInfoRelated.getInt(1);
        }
        String[] colNamesRelated = new String[cntRelated];
        ResultSet colInfoRelated = findingColumns(this.relatedTable);
        while (colInfoRelated.next()) {
            colNamesRelated[i] = colInfoRelated.getString("COLUMN_NAME");
            i++;
        }
        data = new XJData[count];
        for (j = 0; j < count; j++) {
            data[j] = (XJData) getChild();
        }
        j = 0;
        resGet.beforeFirst();
        String colMerge[] = null;
        if (!relatedTable.isEmpty()) {
            colMerge = new String[cntSelf + cntRelated];
            int indxSelf = 0;
            for (; indxSelf < cntSelf; indxSelf++) {
                colMerge[indxSelf] = this.selfClass + "." + colNamesSelf[indxSelf];

            }
            int indxRelated = 0;
            for (; indxRelated < cntRelated; indxRelated++) {
                colMerge[indxRelated + indxSelf] = this.relatedClass + "." + colNamesRelated[indxRelated];

            }
            start = 1;
            end = cntSelf + cntRelated;
        } else {
            colMerge = new String[cntSelf];
            int indx = 0;
            for (; indx < cntSelf; indx++) {
                colMerge[indx] = this.selfClass + "." + colNamesSelf[indx];
            }
            start = 1;
            end = cntSelf;
        }
        int counter = 0;
       
        while (resGet.next()) {
            int index = 0;
            counter++;
            
            for (k = start; k <= end; k++) {
                String s = resGet.getString(k);
                data[j].fieldValue.put(colMerge[index], s);
                if (counter == 1) {
                    this.fieldValue.put(colMerge[index], s);
                }
                index++;
            }
            j++;
        }
        this.clear();
        return this.data;

    }

    private XJData[] getAndSet(ResultSet resGet, ArrayList<String> colArray) throws Exception {

        int cnt = 0, i = 0, j = 0, k = 0, count = 0, end = 0, start = 0;
        while (resGet.next()) {
            count++;
        }
        cnt = colArray.size();
        String[] colNames = new String[cnt];
        while (i < colArray.size()) {
            colNames[i] = this.getClassName()+"."+colArray.get(i);
            i++;
        }
        data = new XJData[count];
        for (j = 0; j < count; j++) {
            data[j] = (XJData) getChild();
        }
        j = 0;
        resGet.beforeFirst();
        int counter = 0;
        while (resGet.next()) {
            counter++;
            for (k = 1; k <= cnt; k++) {
                data[j].fieldValue.put(colNames[k - 1], resGet.getString(k));
                if (counter == 1) {
                    this.fieldValue.put(colNames[k - 1], resGet.getString(k));
                }
            }
            j++;
        }
        this.clear();
        return this.data;

    }

    //~~~~~~~~~~~~ group start end methods ~~~~~~~~~~~~~~~~~~
    public XJData group_Start() {

        if (this.cond == 0) {
            this.where += "(";
        } else {
            group_Start++;
            this.where += " and (";
        }
        this.cond = 0;
        this.completeQuery = this.where;
        return this;

    }

    public XJData group_Or_Start() {

        if (this.cond == 0) {
            this.where += "(";
        } else {
            group_Or_Start++;
            this.where += " or (";
        }
        this.cond = 0;
        this.completeQuery = this.where;
        return this;

    }

    public XJData group_Not_Start() {

        if (this.cond == 0) {
            this.where += "(";
        } else {
            group_Not_Start++;
            this.where += " and not (";
        }
        this.cond = 0;
        this.completeQuery = this.where;
        return this;

    }

    public XJData group_Or_Not_Start() {

        if (this.cond == 0) {
            this.where += "(";
        } else {
            group_Or_Not_Start++;
            this.where += " or not(";
        }
        this.cond = 0;
        this.completeQuery = this.where;
        return this;

    }

    public XJData group_End() {

        if (cond == 0) {
            if (group_Start > 0) {
                this.where = this.where.substring(0, this.where.lastIndexOf(" and ("));
            } else if (group_Or_Start > 0) {
                this.where = this.where.substring(0, this.where.lastIndexOf(" or ("));
            } else if (group_Not_Start > 0) {
                this.where = this.where.substring(0, this.where.lastIndexOf(" and not ("));
            } else if (group_Or_Not_Start > 0) {
                this.where = this.where.substring(0, this.where.lastIndexOf(" or not("));
            } else {
                this.where = this.where.substring(0, this.where.lastIndexOf(")"));
            }
        } else {
            this.where += ")";
        }
        this.cond++;
        this.completeQuery = this.where;
        return this;

    }

    //~~~~~~~~ own query ~~~~~~~~~~~~~
    public int executeQuery(String sql) throws SQLException, ClassNotFoundException {

        DBOperations dbobj = new DBOperations();
        this.affectedRows = dbobj.ModifyQuery(sql);
        return this.affectedRows;

    }

    public ResultSet selectQuery(String sql) throws SQLException, ClassNotFoundException {

        DBOperations dbobj = new DBOperations();
        return dbobj.SelectQuery(sql);

    }

    //~~~~~~~~~~ setting relation methods ~~~~~~~~~~~~~
    
    /* sets relation */
    
    public XJData setRelation(String relation) throws Exception {

        String relDetail[] = relation.split("->");
        String className = relDetail[0];
        String relName = relDetail[1];
        String clss = this.getClass().toString();
        String classPath = clss.substring(clss.indexOf(" ") + 1, clss.length());
        String classPckg = classPath.substring(0, classPath.lastIndexOf(".") + 1);
        Class cls = Class.forName(classPckg + className);
        Constructor co = cls.getConstructor();
        Object o = co.newInstance();
        String[] relSplit = null;
        String[] relAttributes = null;
        if (this.hasOne.contains("'" + relName + "'" + "->")) {
            relSplit = this.hasOne.split("}");
            for (int i = 0; i < relSplit.length; i++) {

                if (relSplit[i].contains("'" + relName + "'")) {
                    relSplit[i] = relSplit[i].substring(relSplit[i].indexOf("->") + 2, relSplit[i].length());
                    relAttributes = relSplit[i].split(",");
                    break;
                }
            }
        } else if (this.hasMany.contains("'" + relName + "'" + "->")) {

            relSplit = this.hasMany.split("}");
            for (int i = 0; i < relSplit.length; i++) {

                if (relSplit[i].contains("'" + relName + "'")) {
                    relSplit[i] = relSplit[i].substring(relSplit[i].indexOf("->") + 2, relSplit[i].length());
                    relAttributes = relSplit[i].split(",");

                    break;
                }
            }
        }
        else return this;
        this.relatedClass = relAttributes[0];
        this.join_self_as = relAttributes[1];
        this.join_other_as = relAttributes[2];
        this.relatedTable = relAttributes[3];
        this.selfTable = relAttributes[4];
        this.selfClass = relAttributes[5];
        this.otherRel = relAttributes[5];
        

        this.table = this.selfTable;

        innerSetRelation();
        set++;
        return this;

    }

    private void innerSetRelation() throws ClassNotFoundException, SQLException {

        String t1 = "";
        ResultSet rs1 = findingColumns(this.table);
        while (rs1.next()) {
            t1 += this.tablePrefix + this.table + "." + rs1.getString("COLUMN_NAME") + " as " + this.table + rs1.getString("COLUMN_NAME") + ",";
        }
        
        
        String t2 = "";
        ResultSet rs2 = findingColumns(this.relatedTable);
        while (rs2.next()) {
            t2 += this.tablePrefix + this.relatedTable + "." + rs2.getString("COLUMN_NAME") + " as " + this.relatedTable + rs2.getString("COLUMN_NAME") + ",";
        
        }
        

        t2 = t2.substring(0, t2.lastIndexOf(","));
        System.out.println(" this.selfClass  "+ this.selfClass);
        System.out.println(this.getSingle(this.selfClass + ".id"));
        this.select = " select " + t1 + t2 + " from " + this.tablePrefix + this.table + " inner join " + this.tablePrefix + this.relatedTable + " on " + this.tablePrefix + this.table + "." + this.join_self_as + this.space + "=" + this.space + this.tablePrefix + this.relatedTable + "." + this.join_other_as + " where " + this.space + this.tablePrefix + this.table + ".id" + this.space + " = " + this.getSingle(this.selfClass + ".id");
        
        this.completeQuery = this.select;

        cond++;
        this.where = "";

    }

    //~~~~~~~~~~~~~~other methods~~~~~~~~~~~~~~
    public Object getChild() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String className = this.getClass().toString();
        className = className.substring(className.indexOf(" ") + 1, className.length());
        Class clss = Class.forName(className);
        Constructor cons = clss.getConstructor();
        Object o = cons.newInstance();
        return o;

    }

    private void clear() {

        this.set = 0;
        this.cond = 0;
        this.hcond = 0;
        this.ocond = 0;
        this.gcond = 0;
        this.groupby = " group by ";
        this.having = " having ";
        this.orderby = " order by ";
        this.select = " select ";
        this.where = " where ";
        this.delete = " delete from ";
        this.update = " update ";
        this.selected.clear();
        this.selectedField.clear();
        this.relatedTable = "";
        this.relatedClass = "";
        this.selfTable = "";
        this.otherRel = "";
        this.join_self_as = "";
        this.join_other_as = "";
        this.field.clear();
        this.value.clear();
        this.insertList.clear();
        this.updateList.clear();
        this.updateFieldList.clear();
        this.updateValueList.clear();
        this.updateHashList.clear();
        this.whereList.clear();

    }

    /* when we want to have selected columns */
    public XJData select(ArrayList<String> array) {

        this.selected = array;
        return this;

    }
    
    /* when we want to use aggregate function. type is the type of function and field is the column name */
    
    public XJData select(String type, String field) {

        this.selectedField.add(type);
        this.selectedField.add(field);
        return this;

    }

    /* when we want to have distinct records */
    public XJData distinct() {

        this.select = " select distinct ";
        return this;

    }
    
    /* to set the limit of records*/

    public XJData limit(String limit) {

        this.limit = limit;
        return this;

    }
    
    /* to set the limit & offset of records*/

    public XJData limit(String limit, String offset) {

        this.limit = limit;
        this.offset = offset;
        return this;

    }

    private ResultSet findingColumns(String table) throws ClassNotFoundException, SQLException {

        
        Class.forName("com.mysql.jdbc.Driver");
        if(conn == null)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/information_schema", "root", "");
        Statement st = conn.createStatement();
        String s = "select columns.column_name from columns where columns.table_name='" + this.tablePrefix + table + "' and columns.table_schema='" + super.dbName + "' ";
        
        ResultSet rs = st.executeQuery(s);
        return rs;

    }

    private ResultSet findingNumColumns(String table) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        if(conn == null)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/information_schema", "root", "");
        Statement st = conn.createStatement();
        String s = "select count(columns.column_name) from columns where columns.table_name='" + this.tablePrefix + table + "' and columns.table_schema='" + super.dbName + "' ";
        
        ResultSet rs = st.executeQuery(s);
        return rs;

    }

    /* to set value in the field*/
    
    public void setValues(String field, String value) {

        this.field.add(field);
        this.value.add(value);

    }
    
    /* returns value of the field */

    public String getSingle(String field) {

        return this.fieldValue.get(field);

    }
    
    /* sets name of the table */

    public void setTableName(String table) {

        this.table = this.tablePrefix + table;

    }
    
    /* returns self table name */
    
    public String getTableName() {

        return this.table;

    }
    
    /* returns related table name */
    
    public String getRelatedTableName() {

        return this.relatedTable;

    }
    
    /* returns self class name */
    
    public String getClassName() {

        return this.selfClass;

    }
    
    /* returns related class name */
    
    public String getRelatedClassName() {

        return this.relatedClass;

    }


    /* returns the last query execured */
    
    public String completeQuery() {

        return "last query " + this.completeQuery;

    }

    /* creates duplicate copy of record */
    
    public XJData copy() throws Exception {

        XJData other = (XJData) getChild();
        HashMap clone = (HashMap) this.fieldValue.clone();
        HashMap<String, String> newClone = new HashMap<String, String>();
        int i = 0;

        String key[] = new String[clone.size()];
        Iterator iterator1 = clone.keySet().iterator();
        while (iterator1.hasNext()) {

            key[i] = iterator1.next().toString();
            key[i] = key[i].substring(key[i].indexOf(".") + 1, key[i].length());
            i++;


        }

        String values[] = new String[clone.size()];
        i = 0;
        Iterator iterator2 = clone.values().iterator();
        while (iterator2.hasNext()) {

            values[i++] = iterator2.next().toString();

        }

        select("max", "id").get();
        String aggregate = getAggregate();
        int newId = Integer.parseInt(aggregate) + 1;
        setValues("id", "" + newId);
        newClone.put(this.table + ".id", "" + newId);
        for (i = 1; i < clone.size(); i++) {
            setValues(key[i], values[i]);
            newClone.put(this.table + "." + key[i], values[i]);
        }

        save();
        other.fieldValue = newClone;
        return other;

    }

    /* closes the connection */
    
    public void connectionClose() throws SQLException {

        this.con.close();
    }
}
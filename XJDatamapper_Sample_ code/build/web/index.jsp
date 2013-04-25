<%@page import="java.util.HashMap"%>
<%@page import="Controllers.studentrecord.fees"%>
<%@page import="Controllers.studentrecord.class_session"%>
<%@page import="Controllers.studentrecord.session"%>
<%@page import="Controllers.studentrecord.rollno"%>
<%@page import="Controllers.studentrecord.feesapplicable"%>
<%@page import="Controllers.studentrecord.student"%>
<%@page import="Controllers.studentrecord.feeshead"%>
<%@page import="Controllers.studentrecord.scholar"%>
<%@page import="Controllers.XJData"%>
<%@page import="Controllers.studentrecord.class_detail" %>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
       XJData data = new student();
       
       data.where("id","=","1").get();
       out.println("before "+data.fieldValue.entrySet());
       data.setRelation("student->rollno").get();
       out.println(data.completeQuery());
       out.println("after "+data.fieldValue.entrySet());
       out.println(data.getSingle("st.id"));
       out.println(data.getSingle("rollno.id"));
       
        
        
        
        
           /* out.println("<table  border = 1 cellpadding = 2>");
            out.println("<tr><td align = 'center'>2005-2006 scholar</td><td align = 'center'>2005-2006 student</td></tr>");
            XJData sesn = new session();
            out.println("sesn.getClass() "+ sesn.getClass());
            
            sesn.where("name","=","2005-2006").get();
            XJData class_detail = new class_detail();
            class_detail.where("name","=","1a").get();
            XJData class_session = new class_session();
            class_session.where("class_id",class_detail).where("session_id",sesn).get();
            XJData student = new student();
            student.where("class_session_id",class_session).get();
            
            
            ArrayList values = new ArrayList<String>();
            for(XJData stdata  : student.data)
            {
                out.println("stdata.getClass() "+ stdata.getClass());
                values.add(stdata.getSingle("student.scholar_id"));
                out.println("<tr>");
                out.println("<br><td>"+stdata.getSingle("student.scholar_id")+"</td>&nbsp;<td>"+stdata.getSingle("student.id")+"</td>");                            
                out.println("</tr>");
            }
            out.println("</table><br>");
            out.println("<table  border = 1 cellpadding = 2>");
            out.println("<tr><td align = 'center'>2006-2007 scholar</td><td align = 'center'>2006-2007 student</td></tr>");
            sesn.where("name","=","2006-2007").get();
            class_detail.where("name","=","1a").get();
            class_session.where("class_id",class_detail).where("session_id",sesn).get();
                       
            student.where("class_session_id",class_session).where_In("scholar_id", values).get();            
            for( XJData re  : student.data)
            {
                out.println("<tr>");
                out.println("<br><td>"+re.getSingle("student.scholar_id")+"</td>&nbsp;<td>"+re.getSingle("student.id")+"</td>");                            
                out.println("</tr>");
            }
            out.println("</table><br>");*/
        
        %>
  
    </body>
</html>
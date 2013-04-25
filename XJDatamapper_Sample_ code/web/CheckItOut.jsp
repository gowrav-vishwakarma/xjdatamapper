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


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>XJData mapper test</title>
    </head>
    <body>
        
        <%
        
           out.println("<table border=1 cellpacing=1>");
           out.println("<tr><th>id</th><th>class name</th><th>session name</th></tr>");
           XJData scholar = new scholar();
           scholar.get();
           for(XJData result1 : scholar.data){
               
               result1.setRelation("scholar->students").get();
               for(XJData result2 : result1.data){
                   out.println("<tr><td>"+result2.getSingle("scholar.id")+"</td><td>"+result2.getSingle("scholar.name")+"</td><td>"+result2.getSingle("student.id")+"</td></tr>");                      
               }
          }
           
           
               
           
        %>
    </body>
</html>

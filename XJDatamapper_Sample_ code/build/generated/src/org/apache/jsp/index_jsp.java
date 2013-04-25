package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.HashMap;
import Controllers.studentrecord.fees;
import Controllers.studentrecord.class_session;
import Controllers.studentrecord.session;
import Controllers.studentrecord.rollno;
import Controllers.studentrecord.feesapplicable;
import Controllers.studentrecord.student;
import Controllers.studentrecord.feeshead;
import Controllers.studentrecord.scholar;
import Controllers.XJData;
import Controllers.studentrecord.class_detail;
import java.util.ArrayList;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
      out.write("   \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        ");

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
        
        
      out.write("\n");
      out.write("  \n");
      out.write("    </body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

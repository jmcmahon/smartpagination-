<%@page language="java" isErrorPage="true" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<title>Error</title>
<%@ page import="org.apache.log4j.Logger" %>
<%
if (exception != null) //from JSP
{
    //Exception from JSP didn't log yet ,should log it here.
    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
    Logger.getLogger("org.powerstone.smartpagination"+requestUri).error(exception.getMessage(), exception);
}
else if (request.getAttribute("exception") != null) //from Spring
{
    exception = (Exception) request.getAttribute("exception");
    Logger.getLogger("com.ema.bms.Error_From_Error_Page").error(exception.getMessage(), exception);
}
%>
</head>
<body bgcolor="#ffffff" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<br>
<br>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td><div align="center" class="err"><strong>系统出错,请与系统管理员联系!</strong> <br>
        <br>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td> <div align="center"> 
            <input name="Submit" type="button" class="button" value="查看" onclick="readMore(); return false">
                <input name="Submit" type="submit" class="button" value="返回" onClick="JavaScript:history.go(-1);">
              </div></td>
          </tr>
        </table>
      </div></td>
  </tr>
  <tr> 
    <td>
  <div id="readmore" style="display:none">
  <b><%= (exception!= null)?exception.getMessage():"" %></b>
    <p>
<%
if (exception!= null){
    exception.printStackTrace(new java.io.PrintWriter(out));
}else{
%>
No exception message!
<%}%>
    </p>
</div></td>
  </tr>
</table>
<br>

<script type="text/javascript">
function readMore() {
    var content = document.getElementById("readmore");
    if (content.style.display == "") {
        content.style.display = "none";
    } else {
        content.style.display = "";
    }
}
</script>
</body>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>down</title>
  </head>
  <body>
   <form action="${pageContext.request.contextPath}/DownloadServlet?wid=32442342" method="post"> 
	 <!-- <form action="${pageContext.request.contextPath}/StudentServlet" method="post"> -->   
	 <!--<input type="hidden" name="method" value="findByPraise">-->   
<input type="submit" value="下载">
    </form>
  </body>
</html>
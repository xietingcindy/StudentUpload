<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
</head>
<body>
<form action="${pageContext.request.contextPath}/TeacherServlet" method="post">
 <input type="hidden" name="method" value="login"/>
用户名：<input name="loginname" type="text"/>
密码：<input name="password" type="password">
<input type="submit" value="登录">
</form>
</body>
</html>
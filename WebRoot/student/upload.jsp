<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
  <head>  
    <title>文件上传</title>  
      
    <meta http-equiv="pragma" content="no-cache">  
    <meta http-equiv="cache-control" content="no-cache">  
    <meta http-equiv="expires" content="0">  
    <!-- 
    <link rel="stylesheet" type="text/css" href="styles.css"> 
    -->  
  
  </head>  
    
  <body>  
    <form action="${pageContext.request.contextPath}/UploadServlet" method="post" enctype="multipart/form-data">  
	
	学生姓名:<input name="sname"/><br/>
	学号:<input name="number"/><br/>  
	班级:<input name="sclass"/><br/>
	作品名称:<input name="wname"/><br>
	作品类型:<input name="type"/><br>
	简介:<input name="introduce"/><br>	
	封面图片:<input type="file" name="picture" /><br/>
	作品文件<input type="file" name="work"/><br/>  
	
          
        <input type="submit" value="上传">  
    </form>  
  </body>  
</html>
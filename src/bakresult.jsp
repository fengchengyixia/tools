<%@page import="org.apache.velocity.runtime.directive.Foreach"%>
<%@page import="com.sinosoft.undwrt.servlet.MapBaking"%>
<%@page import="com.sinosoft.undwrt.servlet.CreatSqlContext"%>
<%@page import="com.sinosoft.undwrt.servlet.Dbuser"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
    String isNew="";
	isNew=request.getParameter("isNew");
	String[] leixings = request.getParameterValues("leixing");
	MapBaking mapBaking =new MapBaking();
	if(isNew.equals("1")){
		mapBaking.setNew(true);
	}
	String result="运行结果：";
	for(int i=0;i<leixings.length;i++){
		if(leixings[i].equals("0")){
			result=result+"\n</br>"+mapBaking.baking();
		}else if(leixings[i].equals("1")){
			result=result+"\n</br>"+mapBaking.codeBaking();
		}else if(leixings[i].equals("2")){
			result=result+"\n</br>"+mapBaking.MappingBaking();
		}
	}
	
	

	//System.out.println(sql.replaceAll("</br>", ""));
   
	//sql=sql.replaceAll("?", " &lowast; ");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>result</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>

    <%=result%>

  </body>
</html>

<%@page import="java.io.Writer"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.FileWriter"%>
<%@page import="com.sinosoft.undwrt.servlet.CreatSqlContext"%>
<%@page import="com.sinosoft.undwrt.servlet.Dbuser"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	String tableName="";
	String tableComments="";
	String bddjType="";
	String procedureName="";
	String sql="";
	String isCreateAll="";
	isCreateAll=request.getParameter("isCreateAll");
	tableName=request.getParameter("tablename");
	tableComments=request.getParameter("tablecoments");
	bddjType=request.getParameter("bddjType");
	procedureName=request.getParameter("procedureName");
	tableName=tableName==null?"":tableName.trim().toUpperCase();
	procedureName=procedureName==null?"":procedureName.trim().toUpperCase();
	bddjType=bddjType==null?"1":bddjType.trim().length()==0?"1":bddjType.trim();
	tableComments=tableComments==null?"":tableComments.trim();
	if(isCreateAll.equals("1")){
		String[] procedureNames=new String[]{"BDDJ_MONITOR_PREPOLICY","BDDJ_MONITOR_RENEWPOLICY","BDDJ_MONITOR_ENDOR","BDDJ_MONITOR_RENEWAL","BDDJ_MONITOR_EXPIRE"};
		CreatSqlContext creatSqlContext=new CreatSqlContext();
		creatSqlContext.setCreat(true);
		String temp="";
		String cleanSql="";
		String txtsql="";
		for(int i=0;i<procedureNames.length;i++){
			procedureName=procedureNames[i];
			sql=creatSqlContext.creatProcedureSql(procedureName);
			cleanSql=sql.replaceAll("\n<lineFeed>", "\n").replaceAll("</br>", "\n");
			creatSqlContext.creatProcedureBySql(cleanSql);
			txtsql=txtsql+cleanSql.replaceAll("\n", "\r\n");
			txtsql+="\r\n";
			temp=temp+procedureName+"存储过程已经生成成功！\n</br>";
		}
		String txtPath="C:\\Users\\pc\\Desktop\\temp\\proceduals.txt";
		Writer writer=new BufferedWriter( new FileWriter(txtPath));
		writer.write(txtsql) ;
		writer.flush();
		writer.close();
		sql=temp;
	}else{
		if(tableName.length()>0){
			Dbuser dbuser=new Dbuser(false);
			sql=dbuser.getSqlByTableName(tableName, tableComments,bddjType,"0");
			sql=sql.replaceAll("<lineFeed>", "</br>");
			System.out.println(sql.replaceAll("</br>", ""));
		}else if(procedureName.length()>0){
			CreatSqlContext creatSqlContext=new CreatSqlContext();
			if(tableComments.length()>0){
				creatSqlContext.setCreat(true);
				sql=creatSqlContext.creatProcedureSql(procedureName);
				creatSqlContext.creatProcedureBySql(sql.replaceAll("\n<lineFeed>", "\n").replaceAll("</br>", "\n"));
				sql="生成存储过程成功！";
			}else{
				sql=creatSqlContext.creatProcedureSql(procedureName);
				sql=sql.replaceAll("<lineFeed>", "</br>");
			}
		}
	}
	//System.out.println(sql.replaceAll("</br>", ""));
   
	//sql=sql.replaceAll("?", " &lowast; ");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>creatSqlresult</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>

    <%=sql%>

  </body>
</html>

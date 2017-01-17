<%@page import="com.sinosoft.undwrt.servlet.Dbuser"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>creatSql</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
  	<h1>自动生成sql模板</h1>
  	<form action="creatSqlresult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>
    			请输入要生成sql的表名：
    		</td>
    		<td>
    			<input type="text" name="tablename" id="tablename" value=""/>
    		</td>
    	</tr>
    	<tr>
    		<td>
    			请输入要生成sql的表的注释：
    		</td>
    		<td>
    			<input type="text" name="tablecoments" id="tablecoments" value=""/>
    		</td>
    	</tr>
    	 <tr>
    	 	<td>
    			请输入要生成sql的表的保单登记类型：
    		</td>
    	 	<td>
    		 	<input type="text" name="bddjType" id="bddjType" value=""/>  	
    		</td>
    	</tr>
    	<tr>
    	 	<td>
    			请输入要生成sql的存储过程名称：
    		</td>
    	 	<td>
    		 	<input type="text" name="procedureName" id="procedureName" value=""/>  	
    		</td>
    	</tr>
    	<tr>
    		<td>是否一键生成所有的存储过程</td>
    		<td>
    		 	<select name="isCreateAll">
    	 			<option value ="0">否</option>
  					<option value ="1">是</option>
    			</select>   	
    		</td>
    	</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename" value="生成" />   	
    		</td>
    	</tr>
    	<tr><td>BDDJ_MONITOR_PRODUCTADD    </td> <td>产品新增</td> <td> 01 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PRODUCTCHANGE </td> <td>产品变更</td> <td> 02 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PRODUCTDELIST </td> <td>产品退市</td> <td> 03 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREPOLICY     </td> <td>新单承保</td> <td> 04 </td> </tr>    
		<tr><td>BDDJ_MONITOR_RENEWPOLICY   </td> <td>续保    </td> <td> 05 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREGETFEE     </td> <td>预收保费</td> <td> 06 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREPAYFEE     </td> <td>预退保费</td> <td> 07 </td> </tr>    
		<tr><td>BDDJ_MONITOR_ENDOR         </td> <td>批改    </td> <td> 08 </td> </tr>    
		<tr><td>BDDJ_MONITOR_RENEWAL       </td> <td>续期    </td> <td> 09 </td> </tr>    
		<tr><td>BDDJ_MONITOR_EXPIRE        </td> <td>满期    </td> <td> 10 </td> </tr>    
    	
    </table>

    </form>
  	<form action="bakresult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>是否重新备份今天数据</td>
    		<td>
    	 	<select name="isNew">
    	 		<option value ="0">否</option>
  				<option value ="1">是</option>
    		</select>
    		</td>
    	</tr>
    	<tr>
	    	<label><input name="leixing" type="checkbox" value="0" />元数据备份 </label> 
			<label><input name="leixing" type="checkbox" value="1" />源程序备份 </label> 
			<label><input name="leixing" type="checkbox" value="2" />映射备份</label> 
		</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename1" value="备份元数据" />   	
    		</td>
    	</tr>

    </table>

    </form>
      	<form action="recoverResult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>还原的文件路径：</td>
    		<td>
    	 		<input type="text" name="path" id="path" value=""/>  	
    		</td>
    	</tr>
    	<tr>
	    	<td>还原的表名：</td>
    		<td>
    	 		<input type="text" name="tableName" id="tableName" value=""/>  	
    		</td>
		</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename2" value="还原" />   	
    		</td>
    	</tr>
    </table>

    </form>
    
  </body>
</html>

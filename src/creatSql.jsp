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
  	<h1>�Զ�����sqlģ��</h1>
  	<form action="creatSqlresult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>
    			������Ҫ����sql�ı�����
    		</td>
    		<td>
    			<input type="text" name="tablename" id="tablename" value=""/>
    		</td>
    	</tr>
    	<tr>
    		<td>
    			������Ҫ����sql�ı��ע�ͣ�
    		</td>
    		<td>
    			<input type="text" name="tablecoments" id="tablecoments" value=""/>
    		</td>
    	</tr>
    	 <tr>
    	 	<td>
    			������Ҫ����sql�ı�ı����Ǽ����ͣ�
    		</td>
    	 	<td>
    		 	<input type="text" name="bddjType" id="bddjType" value=""/>  	
    		</td>
    	</tr>
    	<tr>
    	 	<td>
    			������Ҫ����sql�Ĵ洢�������ƣ�
    		</td>
    	 	<td>
    		 	<input type="text" name="procedureName" id="procedureName" value=""/>  	
    		</td>
    	</tr>
    	<tr>
    		<td>�Ƿ�һ���������еĴ洢����</td>
    		<td>
    		 	<select name="isCreateAll">
    	 			<option value ="0">��</option>
  					<option value ="1">��</option>
    			</select>   	
    		</td>
    	</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename" value="����" />   	
    		</td>
    	</tr>
    	<tr><td>BDDJ_MONITOR_PRODUCTADD    </td> <td>��Ʒ����</td> <td> 01 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PRODUCTCHANGE </td> <td>��Ʒ���</td> <td> 02 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PRODUCTDELIST </td> <td>��Ʒ����</td> <td> 03 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREPOLICY     </td> <td>�µ��б�</td> <td> 04 </td> </tr>    
		<tr><td>BDDJ_MONITOR_RENEWPOLICY   </td> <td>����    </td> <td> 05 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREGETFEE     </td> <td>Ԥ�ձ���</td> <td> 06 </td> </tr>    
		<tr><td>BDDJ_MONITOR_PREPAYFEE     </td> <td>Ԥ�˱���</td> <td> 07 </td> </tr>    
		<tr><td>BDDJ_MONITOR_ENDOR         </td> <td>����    </td> <td> 08 </td> </tr>    
		<tr><td>BDDJ_MONITOR_RENEWAL       </td> <td>����    </td> <td> 09 </td> </tr>    
		<tr><td>BDDJ_MONITOR_EXPIRE        </td> <td>����    </td> <td> 10 </td> </tr>    
    	
    </table>

    </form>
  	<form action="bakresult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>�Ƿ����±��ݽ�������</td>
    		<td>
    	 	<select name="isNew">
    	 		<option value ="0">��</option>
  				<option value ="1">��</option>
    		</select>
    		</td>
    	</tr>
    	<tr>
	    	<label><input name="leixing" type="checkbox" value="0" />Ԫ���ݱ��� </label> 
			<label><input name="leixing" type="checkbox" value="1" />Դ���򱸷� </label> 
			<label><input name="leixing" type="checkbox" value="2" />ӳ�䱸��</label> 
		</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename1" value="����Ԫ����" />   	
    		</td>
    	</tr>

    </table>

    </form>
      	<form action="recoverResult.jsp" method="post" target="_blank"  >
    <table>
    	<tr>
    		<td>��ԭ���ļ�·����</td>
    		<td>
    	 		<input type="text" name="path" id="path" value=""/>  	
    		</td>
    	</tr>
    	<tr>
	    	<td>��ԭ�ı�����</td>
    		<td>
    	 		<input type="text" name="tableName" id="tableName" value=""/>  	
    		</td>
		</tr>
    	 <tr>
    	 	<td>
    		 <input type="submit" name="submit" id="tablename2" value="��ԭ" />   	
    		</td>
    	</tr>
    </table>

    </form>
    
  </body>
</html>

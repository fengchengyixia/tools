package com.sinosoft.undwrt.servlet;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class CreatSqlContext {
	private boolean isCreat=false;
	/**
	 * 读取模板文件，并将变量替换掉，返回最终的sql代码。
	 */
	public String creatContextByVelocity(String modelName,
			VelocityContext context) {
		StringWriter StrWriter = new StringWriter();// 声明接收变量
		try {
			Properties p = new Properties();
			String modlePath = "";
			modlePath = "D:/mht/sqlTemplate";
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, modlePath);// 初始化
																				// 参数
			Velocity.init(p);
			Velocity.mergeTemplate(modelName, "GBK", context, StrWriter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 初始化 Velocity
		return StrWriter.toString();
	}
	
	/**
	 * 根据存储过程名生成存储过程的sql
	 * @param procedureName 存储过程名
	 * @return 
	 * @author wzs
	 * @throws Exception 
	 */
	public String  creatProcedureSql(String procedureName) throws Exception {
		procedureName=procedureName==null?"":procedureName.trim().toUpperCase();
		if (procedureName.length()==0) {
			throw new Exception("存储过程名不能为空，请确认！");
		}
		String modelName="BDDJ_MONITOR.html";
		Dbuser dbuser = new Dbuser(isCreat);
		VelocityContext context = new VelocityContext(); 
		List<ProcedureDto> insertSqls = new ArrayList<ProcedureDto>(); 
		ProcedureDto procedureDto =null;
		String insertSql="";
		String sql ="SELECT * from map_proprp_procedure WHERE procedure_name= ? ORDER BY flag DESC";
		String procedureCName ="";
		boolean isHasCName=false;
		List<String[]> procedureBaseTables = dbuser.querybySqlFacade(sql, new String[]{procedureName}, 6);
		if (procedureBaseTables==null || procedureBaseTables.size()==0) {
			throw new Exception("该存储过程没有配置，请确认！");
		}
		for (String[] procedureBaseTable : procedureBaseTables) {
			
			if (!procedureBaseTable[5].equals("2")) {
				procedureDto= new ProcedureDto();
				if (!isHasCName) {
					procedureCName=procedureBaseTable[1];
					if (procedureCName!=null&&procedureCName.length()>0) {
						isHasCName=true;
					}
				}
				procedureDto.setProcedureName(procedureBaseTable[0]);
				procedureDto.setTableName(procedureBaseTable[2]);
				procedureDto.setTableCName(procedureBaseTable[3]);
				insertSql = dbuser.getSqlByTableName(procedureBaseTable[2], procedureBaseTable[3], procedureBaseTable[4],procedureBaseTable[5]);
				if (insertSql != null && insertSql.length() > 0) {
					procedureDto.setInsertSql(insertSql);
				} else {
					throw new Exception(procedureBaseTable[2] + "该 表没有生成sql，请确认！");
				}
				insertSqls.add(procedureDto);
			}
		}
		context.put("ProcedureName", procedureName);
		context.put("ProcedureCName", procedureCName);
		context.put("insertSqlList", insertSqls);
		return creatContextByVelocity(modelName, context);
	}
	
	/**
	 * 根据存储过程sql创建存储过程
	 * @param sql
	 * @throws Exception
	 * @author wzs
	 */
	public void  creatProcedureBySql(String sql) throws Exception {
		sql=sql==null?"":sql.trim();
		if (sql.length()==0) {
			throw new Exception("存储过程sql不能为空，请确认！");
		}
		Dbuser dbuser = new Dbuser();
		int creatProcedure = dbuser.creatProcedure(sql);
		System.out.println("运行结果是："+creatProcedure);
	}

	public boolean isCreat() {
		return isCreat;
	}

	public void setCreat(boolean isCreat) {
		this.isCreat = isCreat;
	}
	
}

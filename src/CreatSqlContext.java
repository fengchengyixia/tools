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
	 * ��ȡģ���ļ������������滻�����������յ�sql���롣
	 */
	public String creatContextByVelocity(String modelName,
			VelocityContext context) {
		StringWriter StrWriter = new StringWriter();// �������ձ���
		try {
			Properties p = new Properties();
			String modlePath = "";
			modlePath = "D:/mht/sqlTemplate";
			p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, modlePath);// ��ʼ��
																				// ����
			Velocity.init(p);
			Velocity.mergeTemplate(modelName, "GBK", context, StrWriter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ��ʼ�� Velocity
		return StrWriter.toString();
	}
	
	/**
	 * ���ݴ洢���������ɴ洢���̵�sql
	 * @param procedureName �洢������
	 * @return 
	 * @author wzs
	 * @throws Exception 
	 */
	public String  creatProcedureSql(String procedureName) throws Exception {
		procedureName=procedureName==null?"":procedureName.trim().toUpperCase();
		if (procedureName.length()==0) {
			throw new Exception("�洢����������Ϊ�գ���ȷ�ϣ�");
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
			throw new Exception("�ô洢����û�����ã���ȷ�ϣ�");
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
					throw new Exception(procedureBaseTable[2] + "�� ��û������sql����ȷ�ϣ�");
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
	 * ���ݴ洢����sql�����洢����
	 * @param sql
	 * @throws Exception
	 * @author wzs
	 */
	public void  creatProcedureBySql(String sql) throws Exception {
		sql=sql==null?"":sql.trim();
		if (sql.length()==0) {
			throw new Exception("�洢����sql����Ϊ�գ���ȷ�ϣ�");
		}
		Dbuser dbuser = new Dbuser();
		int creatProcedure = dbuser.creatProcedure(sql);
		System.out.println("���н���ǣ�"+creatProcedure);
	}

	public boolean isCreat() {
		return isCreat;
	}

	public void setCreat(boolean isCreat) {
		this.isCreat = isCreat;
	}
	
}

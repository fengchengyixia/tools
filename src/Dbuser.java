package com.sinosoft.undwrt.servlet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sinosoft.policyregister.dto.domain.ProcblacklistDto;
import com.sinosoft.sysframework.common.datatype.DateTime;
import com.sinosoft.sysframework.reference.DBManager;
import com.sinosoft.undwrt.bl.action.domain.BLWfFlowMainHisAction;
import com.sinosoft.undwrt.dto.domain.WfFlowMainHisDto;

public class Dbuser {
	private static final long serialVersionUID = 1L;
	private static final  String OWNER="HICIS"; 
	private boolean isCreat=false;
	/**
     * ��Դ�������ʵ�����������ݿ����.
     */
    protected DBManager dbManager = null;
    public Dbuser(){
    	DBManager dbManager = new DBManager();
    	this.dbManager=dbManager;
    }
    public Dbuser(boolean isCreat){
    	DBManager dbManager = new DBManager();
    	this.dbManager=dbManager;
    	this.isCreat=isCreat;
    }
    public Dbuser(DBManager dbManager){
        this.dbManager = dbManager;
    }
    /**
     * ����������һ�����ݣ��м�Ԫ���ݣ�
     * @param tabName  ����
     * @return 0������1��ע�ͣ�2���������ͣ�3�����ݳ��ȣ�4��Ӧ������5��Ӧ��������6�Ƿ�ת����־��7ת��sql ��8��Ϊnull��־
     * @throws Exception
     * @author wzs
     */
    public List<String[]> queryBySql(String tabName) throws Exception{
        //����DTO
        List<String[]> result=null;
        try{
            dbManager.open("undwrtDataSource");
            //��ѯ����,��ֵ��DTO
            result = findByTabName(tabName);
        }catch(Exception exception){
        	exception.printStackTrace();
            throw exception;
        }finally{
            dbManager.close();
        }
        return result;
    }
    
    /**
     * ��ѯ�м���Ԫ����
     * @param tabName ƽ̨����
     * @return 0������1��ע�ͣ�2���������ͣ�3�����ݳ��ȣ�4��Ӧ������5��Ӧ��������6�Ƿ�ת����־��7ת��sql ��8��Ϊnull��־
     * @throws Exception
     * @author wzs
     */
	public List<String[]> findByTabName(String tabName) throws Exception {
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer buffer = new StringBuffer(200);
		buffer.append("SELECT a.column_name ,(select comments from user_col_comments  WHERE table_name=a.table_name AND column_name=a.column_name )�� a.data_type, '('||a.DATA_LENGTH||')', pro.prptab_name ,pro.prpcol_name,pro.conversionflag,pro.conversionsql,pro.isnonempty from user_tab_columns  a LEFT JOIN map_proprp_col  pro  ON  a.table_name=pro.protab_name  and a.column_name=pro.procol_name	" + "WHERE   a.table_name= ? ORDER BY a.column_name");
		dbManager.prepareStatement(buffer.toString());
		dbManager.setString(1, tabName);
		ResultSet resultSet = dbManager.executePreparedQuery();
		while (resultSet.next()) {
			String[] aStrings = new String[9];
			aStrings[0] = dbManager.getString(resultSet, 1);
			aStrings[1] = dbManager.getString(resultSet, 2);
			aStrings[2] = dbManager.getString(resultSet, 3);
			aStrings[3] = dbManager.getString(resultSet, 4);
			aStrings[4] = dbManager.getString(resultSet, 5);
			aStrings[5] = dbManager.getString(resultSet, 6);
			aStrings[6] = dbManager.getString(resultSet, 7);
			aStrings[7] = dbManager.getString(resultSet, 8);
			aStrings[8] = dbManager.getString(resultSet, 9);
			aStrings[0]=aStrings[0]==null?"":aStrings[0].trim().toUpperCase();
        	aStrings[4]=aStrings[4]==null?"":aStrings[4].trim().toUpperCase();
        	aStrings[5]=aStrings[5]==null?"":aStrings[5].trim().toUpperCase();
        	aStrings[6]=aStrings[6]==null?"":aStrings[6].trim();
        	aStrings[7]=aStrings[7]==null?"":aStrings[7].trim();
        	aStrings[8]=aStrings[8]==null?"":aStrings[8].trim();
        	aStrings[7]=aStrings[7].replaceAll("OWNER",OWNER);
        	if (!isCreat) {
				aStrings[7] = aStrings[7].replaceAll("<", " &lt; ");
				aStrings[7] = aStrings[7].replaceAll(">", " &gt; ");
			}
			result.add(aStrings);
		}
		resultSet.close();
		return result;
	}
	/**
	 * ��ѯ��ӳ��
	 * @param tabName ����
	 * @param bddjType ����
	 * @return map_proprp_tab�����
	 * @throws Exception
	 * @author wzs
	 */
    public List<String[]> queryFromBySql(String tabName,String bddjType) throws Exception{
        //����DTO
        List<String[]> result=null;
        try{
            dbManager.open("undwrtDataSource");
            //��ѯ����,��ֵ��DTO
            result = findFromByTabName(tabName,bddjType);
        }catch(Exception exception){
            throw exception;
        }finally{
            dbManager.close();
        }
        return result;
    }
	/**
	 * ��ѯ��ӳ��
	 * @param tabName ����
	 * @param bddjType ����
	 * @return map_proprp_tab�����
	 * @throws Exception
	 * @author wzs
	 */
    public List<String[]> findFromByTabName(String tabName,String bddjType)
            throws Exception{
    		ArrayList<String[]> result = new ArrayList<String[]>();
            StringBuffer buffer = new StringBuffer(200);
            buffer.append("SELECT * from map_proprp_tab WHERE protable_name= ?  AND linktype= ? ORDER BY linkorder ");
            dbManager.prepareStatement(buffer.toString());
            dbManager.setString(1,tabName);
            dbManager.setString(2,bddjType);
            ResultSet resultSet = dbManager.executePreparedQuery();
            while (resultSet.next()){
            	String[] aStrings=new String[11];
            	aStrings[0]=dbManager.getString(resultSet,1); //PROTABLE_NAME ƽ̨�����
            	aStrings[1]=dbManager.getString(resultSet,2); //PRPTABLE_NAME ��Ҫ���ӵ�ҵ����������Ҫ��Ӧselect�еı�����
            	aStrings[2]=dbManager.getString(resultSet,3); // REALPRPTABLE_NAME ��Ҫ���ӵ�ҵ�����ʵ����
            	aStrings[3]=dbManager.getString(resultSet,4); // SELFLINKCOLUMU ��ҵ���������ӵ��У�������Ҫ#�ŷָ���
            	aStrings[4]=dbManager.getString(resultSet,5); // BASELINKCOLUMU ��ҵ���Ҫ���ӵ��У�������Ҫ#�ŷָ������磨PRPCMAIN.POLICYNO#PRPCMAIN.PROSALNO��
            	aStrings[5]=dbManager.getString(resultSet,6); //LINKORDER  ����˳��
            	aStrings[6]=dbManager.getString(resultSet,7); // LINKTYPE 	�Ǽ����ͣ�1���µ��б�...��
            	aStrings[7]=dbManager.getString(resultSet,10); // FLAG Ϊ1ʱ��������ѯ
            	aStrings[8]=dbManager.getString(resultSet,8);  //PRPTABLE_OWNER ���ӱ��������û�
            	aStrings[9]=dbManager.getString(resultSet,9); //CONDITION ��������WHERE�־�
            	aStrings[10]=dbManager.getString(resultSet,11);  //PHONEYTABLE ��flagΪ1ʱ�õ������
            	aStrings[0]=aStrings[0]==null?"":aStrings[0].trim().toUpperCase();
            	aStrings[1]=aStrings[1]==null?"":aStrings[1].trim().toUpperCase();
            	aStrings[2]=aStrings[2]==null?"":aStrings[2].trim().toUpperCase();
            	aStrings[3]=aStrings[3]==null?"":aStrings[3].trim().toUpperCase();
            	aStrings[4]=aStrings[4]==null?"":aStrings[4].trim().toUpperCase();
            	aStrings[8]=aStrings[8]==null?"":aStrings[8].trim().toUpperCase();
            	aStrings[9]=aStrings[9]==null?"":aStrings[9].trim();
            	aStrings[7]=aStrings[7]==null?"":aStrings[7].trim();
            	aStrings[10]=aStrings[10]==null?"":aStrings[10].trim();
            	if (aStrings[7].equals("1")) {
            		if (aStrings[10].length() > 0) {
            			if (aStrings[8].length() > 0) {
            				aStrings[10]=aStrings[10].replaceAll("OWNER", aStrings[8]);
						}
            			aStrings[2]="("+aStrings[10]+")";
					}else {
						throw new Exception(aStrings[0]+"���Ӧ����ҵ���"+aStrings[1]+"��flag���ò���ȷ��");
					}
				}else {
					if (aStrings[8].length() > 0 && aStrings[2].length() > 0) {
						aStrings[2] = aStrings[8] + "." + aStrings[2];
						//��������ñ��û�������ϣ�û����Ĭ��Ĭ���˻����򲻼�
					}
				}
            	if (aStrings[9].length()>0) {
					if (aStrings[8].length()>0) {
						aStrings[9]=aStrings[9].replaceAll("OWNER", aStrings[8]);
					}
				}
				result.add(aStrings);
            }
            resultSet.close();
            return result;
      }
//    public List<Object[]> query(String sql,String[] pram,int[] res) throws Exception {
//    	ArrayList<Object[]> result = new ArrayList<Object[]>();
//    	 dbManager.prepareStatement(sql);
//    	 for (int i = 0; i < pram.length; i++) {
//    		 dbManager.setString(1,pram[i]);
//		}
//    	ResultSet resultSet = dbManager.executePreparedQuery();
//    	while (resultSet.next()){
//    		Object[] objects=new Object[res.length];
//         	for (int i = 0; i < res.length; i++) {
//         		if (res[i]==1) {
//         			objects[i]=dbManager.getString(resultSet,i+1);
//				}
//			}
//         	result.add(objects);
//         }
//    	return result;
//	}
    /**
     * ���ݱ�����ע�ͣ�����������insert into sql���
     * @param tabName ����
     * @param zhusi ע��
     * @param bddjType ����
     * @param flag ��־
     * @return insert into  sql���
     * @throws Exception
     * @author wzs
     */
    public String  getSqlByTableName (String tabName,String zhusi,String bddjType,String flag) throws Exception {
		StringBuffer sb = new StringBuffer(500);
		List<String[]> queryBySql = queryBySql(tabName);
		boolean isE=true;  //�Ƿ�����ҳ��ʾ
		boolean isSpecialDeal=false; // �Ƿ���Ҫ�����⴦��(������ͻ����)
		String lineFeed="";
		if (isE) {
			lineFeed="\n<lineFeed>";
		}else {
			lineFeed="\n";
		}
		
		sb.append("--"+tabName+": "+zhusi+lineFeed);
		sb.append("INSERT INTO "+tabName+" ("+lineFeed+"    ");
		int size = queryBySql.size();
		int i=1;
		for (String[] strings : queryBySql) {
			if (size!=i) {
				sb.append(strings[0] + ", "+lineFeed);
			}else {
				sb.append(strings[0] + " ) "+lineFeed);
			}
			if (strings[6].equals("9")) {
				isSpecialDeal=true;
			}
			i++;
		}
		if (isSpecialDeal) {
			sb.append("SELECT"+lineFeed);
			i=1;
			String specialDealStr="";
			for (String[] strings : queryBySql) {
				if (strings[6].equals("9")) {
					if (strings[7].length()>0) {
						sb.append(strings[0]+"||'-'||");
					}
					specialDealStr=getSpecialDealStr(tabName,strings[6]);
					sb.append(specialDealStr+" ");
				}
				if (size!=i) {
					sb.append(strings[0] + ", "+lineFeed);
				}else {
					sb.append(strings[0] + " "+lineFeed);
				}
				i++;
			}
			sb.append("FROM ( ");
		}
		sb.append("SELECT"+lineFeed);
		if (isSpecialDeal) {
			sb.append("DISTINCT"+lineFeed);
		}
		i=1;
		HashSet<String> hashSet = new HashSet<String>();
		for (String[] strings : queryBySql) {
			String prpString="";   //select��ֵ����
			strings[4]=strings[4]==null?"":strings[4].trim();
			strings[5]=strings[5]==null?"":strings[5].trim();
			strings[6]=strings[6]==null?"":strings[6].trim();
			strings[7]=strings[7]==null?"":strings[7].trim();
			String defalt="";
			if (strings[2].equals("DATE")) {
				defalt = "to_date('1900-01-01','yyyy-mm-dd') ";
			} else if (strings[2].equals("NUMBER")) {
				defalt = "0";
			} else {
				defalt = "'000000'";
			}
			if (strings[6].equals("1")||strings[6].equals("9")){
				if (strings[7].length()>0) {
					prpString=strings[7];
					if(strings[8].equals("N")){
						if (strings[4].length() > 0 && strings[5].length() > 0) {
							prpString ="CASE WHEN "+strings[4] + "." + strings[5]+" IS NOT NULL THEN "+prpString +" ELSE "+defalt+ " END";
						}else {
//							prpString ="CASE WHEN "+prpString+" IS NOT NULL THEN "+prpString +" ELSE "+defalt+ " END";
						}
					}
				}else {
					prpString=getDefaultValue(strings[8], strings[2], tabName+"."+strings[0]);
				}
			}else if (strings[6].equals("0")||strings[6].length()==0) {
				if (strings[4].length() > 0 && strings[5].length() > 0) {
					prpString = strings[4] + "." + strings[5];
					if(strings[8].equals("N")){
						prpString ="CASE WHEN "+prpString+" IS NOT NULL THEN "+prpString +" ELSE "+defalt+ " END";
					}
					hashSet.add(strings[4]);
				} else {
					prpString=getDefaultValue(strings[8], strings[2], tabName+"."+strings[0]);
				}
			} else if (strings[6].equals("8")) {
				if (strings[7].length()>0) {
					prpString=strings[7];
				}
			}else {
				prpString=getDefaultValue(strings[8], strings[2], tabName+"."+strings[0]);
			}
			if (size!=i) {
				sb.append(prpString+" "+strings[0] + ",   --" + strings[1] + "  " + strings[2] + strings[3]+lineFeed);
			}else {
				sb.append(prpString+" "+strings[0] + "   --" + strings[1] + "  " + strings[2] + strings[3]+lineFeed);
			}
			i++;
		}
		sb.append("FROM  "+lineFeed+" ");
		List<String[]> proPrpT2T_w = queryFromBySql(tabName, bddjType);
		String[] proPrpT2T=null;
		HashSet<String> prpTableNameReal = new HashSet<String>();
		String[] selfLinkColumns=null;
		String[] baseLinkColumns=null;
		for (int j = 0; j < proPrpT2T_w.size(); j++) {
			proPrpT2T=proPrpT2T_w.get(j);
			if (j==0) {
				sb.append(proPrpT2T[2]+" "+proPrpT2T[1]+lineFeed);
			}else {
				sb.append("LEFT JOIN "+proPrpT2T[2]+" "+proPrpT2T[1]+" on  ");
				if (proPrpT2T[3].indexOf("#")>-1&&proPrpT2T[4].indexOf("#")>-1) {
					selfLinkColumns = proPrpT2T[3].split("#"); 
					baseLinkColumns = proPrpT2T[4].split("#"); 
					for (int j2 = 0; j2 < selfLinkColumns.length; j2++) {
						if (j2==0) {
							sb.append(proPrpT2T[1]+"." +selfLinkColumns[j2] +"="+ baseLinkColumns[j2]);
						}else {
							sb.append(" AND "+proPrpT2T[1]+"." +selfLinkColumns[j2] +"="+ baseLinkColumns[j2]);
						}
					}
					sb.append(lineFeed);
				}else {
					sb.append(proPrpT2T[1]+"." +proPrpT2T[3] +"="+ proPrpT2T[4]+lineFeed);
				}
			}
			prpTableNameReal.add(proPrpT2T[1]);
		}
		for (String prpTableName : hashSet) {
			if (prpTableName!=null&&prpTableName.trim().length()>0) {
				if (prpTableNameReal.add(prpTableName)) {
					throw new Exception("From ��ȱʧ�������������ݿ�������Ϣ("+tabName+zhusi+prpTableName+")");
				}
			}
		}
		sb.append("WHERE BDDJ_TEMP.TRANSACTIONREASON = '"+bddjType+"'"+lineFeed);
		for (String[] stringW : proPrpT2T_w) {
			if (stringW[9].length()>0) {
				sb.append("AND "+stringW[9]+lineFeed);
			}
		}
		if (isSpecialDeal) {
			sb.append(");"+lineFeed);
		}else {
			sb.append(";"+lineFeed);
		}
		return sb.toString();
	}
    /**
     * ���ݱ����ͱ�־�ó���Ӧ���⴦���sql
     * @param tabName ����
     * @param flag ��־
     * @return ���⴦��ĵ�sql
     * @author wzs
     * @throws Exception 
     */
    public String getSpecialDealStr(String tabName,String flag) throws Exception {
    	StringBuffer dealStr=new StringBuffer(100);
    	if (flag.equals("9")) {
    		List<String[]> primaryKeys = getPrimaryKey(tabName);
    		if (primaryKeys==null||primaryKeys.size()==0) {
				return "";
			}
    		String[] primaryKey=null;
    		for (int i = 0; i < primaryKeys.size(); i++) {
    			primaryKey=primaryKeys.get(i);
    			if (i==primaryKeys.size()-1) {
    				dealStr.append(primaryKey[0]+"");
				}else {
					dealStr.append(primaryKey[0]+",");
				}
			}
    		String primaryKeyString=dealStr.toString();
    		dealStr=new StringBuffer(200);
    		dealStr.append("row_number() over(PARTITION BY ");
    		dealStr.append(primaryKeyString);
    		dealStr.append(" ORDER BY "+primaryKeyString+")");
		}
		return dealStr.toString();
	}
    /**
     * ���ݱ�����ȡ��������������
     * @param tabName ����
     * @return ������Ϣ
     * @throws Exception
     * @author wzs
     */
    public List<String[]> getPrimaryKey (String tabName) throws Exception {
    	if (tabName==null) {
			return null;
		}
		String sqlString = "SELECT a.column_name " 
				+ "from user_cons_columns a, user_constraints b " 
				+ "where a.constraint_name = b.constraint_name " 
				+ "and b.constraint_type = 'P' " 
				+ "and a.table_name = ? ORDER BY a.position";
		return querybySqlFacade(sqlString, new String[]{tabName.trim().toUpperCase()}, 1);
    }
    
    /**
     * ����sql��ѯ���ݽ����Ҫ�������ֶ�����ΪString���ͣ�
     * @param sql  ��ѯ��sql
     * @param condition  ��Ӧ�Ĳ���
     * @param resultNum  ���ص�����
     * @return ��ѯ���
     * @throws Exception
     * @author wzs
     */
    public List<String[]> querybySql(String sql,String[] condition,int resultNum) throws Exception{
    		List<String[]> result = new LinkedList<String[]>();
            dbManager.prepareStatement(sql);
            if (condition!=null&&condition.length>0) {
				for (int i = 0; i < condition.length; i++) {
					dbManager.setString(i + 1, condition[i]);
				}
			}
			ResultSet resultSet = dbManager.executePreparedQuery();
            while (resultSet.next()){
            	String[] aStrings=new String[resultNum];
            	for (int i = 0; i < resultNum; i++) {
            		aStrings[i]=dbManager.getString(resultSet, i+1);
            		aStrings[i]=aStrings[i]==null?"":aStrings[i].trim();
				}
            	result.add(aStrings);
            }
            resultSet.close();
            resultSet=null;
            return result;
      }
    /**
     * ����sql��ѯ���ݽ����Ҫ�������ֶ�����ΪString���ͣ�facade��
     * @param sql  ��ѯ��sql
     * @param condition  ��Ӧ�Ĳ���
     * @param resultNum  ���ص�����
     * @return ��ѯ���
     * @throws Exception
     * @author wzs
     */
    public List<String[]> querybySqlFacade(String sql,String[] condition,int resultNum) throws Exception{
    	List<String[]> result = null;
    	try {
    		dbManager.open("undwrtDataSource");
    		result= querybySql(sql,condition,resultNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
            dbManager.close();
        }
    	return result;
    }
    /**
     * ����sql��ѯ���ݽ����Ҫ�������ֶ�����ΪString���ͣ�facade��
     * @param sql  ��ѯ��sql
     * @param condition  ��Ӧ�Ĳ���
     * @param resultNum  ���ص�����
     * @return ��ѯ���
     * @throws Exception
     * @author wzs
     */
    public List<String[]> querybySqlFacade(String sql,String[] condition,int resultNum,boolean isPingtai) throws Exception{
    	List<String[]> result = null;
    	try {
    		dbManager.open("visaDataSource");
    		result= querybySql(sql,condition,resultNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
            dbManager.close();
        }
    	return result;
    }
    
    /**
     * ��ȡ��Ĭ��ֵ
     * @param isNonNull �Ƿ��Ϊnull��־
     * @param type  ����������
     * @param excep �������Ϣ
     * @return Ĭ��ֵ
     * @throws Exception
     * @author wzs
     */
    public String getDefaultValue(String isNonNull,String type,String excep) throws Exception {
    	String prpString="";
    	if (isNonNull.equals("Y")) {
			if (type.equals("DATE")) {
				prpString = "null";
			} else if (type.equals("NUMBER")) {
				prpString = "0";
			} else {
				prpString = "''";
			}
		}else if (isNonNull.equals("N")) {
			if (type.equals("DATE")) {
				prpString = "to_date('1900-01-01','yyyy-mm-dd') ";
			} else if (type.equals("NUMBER")) {
				prpString = "0";
			} else {
				prpString = "'000000'";
			}
		}else {
			throw new Exception(excep+"�Ƿ��Ϊnullû�����ã���ȷ�ϣ�");
		}
		return prpString;
	}
    
    /**
     * ִ��ddl���
     * @author wzs
     */
    public int creatProcedure(String sql) throws Exception {
    	int executePreparedUpdate=-1;
    	 try{
             dbManager.open("undwrtDataSource");
             //��ѯ����,��ֵ��DTO
             dbManager.prepareStatement(sql);
             executePreparedUpdate = dbManager.executePreparedUpdate();
         }catch(Exception exception){
         	exception.printStackTrace();
             throw exception;
         }finally{
             dbManager.close();
         }
    	 return executePreparedUpdate;
	}
    /**
     * ��ѯ�������
     * @param tableName
     * @return ���������
     * @throws Exception
     * @author wzs
     */
    public int getColNum(String tableName) throws Exception {
    	int result=0;
    	try{
            dbManager.open("visaDataSource");
            //��ѯ����,��ֵ��DTO
            String sql="select to_char(COUNT(1)) from user_tab_columns where Table_Name= ? ";
            dbManager.prepareStatement(sql);
            dbManager.setString(1, tableName);
            ResultSet a = dbManager.executePreparedQuery();
            if (a.next()) {
            	result=Integer.parseInt(dbManager.getString(a, 1));
			}
        }catch(Exception exception){
        	exception.printStackTrace();
            throw exception;
        }finally{
            dbManager.close();
        }
		return result;
	}
    
    /**
     * �����������ݿ�����  action����
     * @param tableName ����
     * @param dtos  ���������
     * @param totalCol  ������
     * @throws Exception
     * @author wzs
     */
    public void insertAllAction(String tableName,List<List<String[]>> dtoss,int totalCol)
            throws Exception{
    	try{
            dbManager.open("visaDataSource");
            //��ѯ����,��ֵ��DTO
            int totalNum=0;
            for (List<String[]> dtos : dtoss) {
            	insertAll (tableName,dtos,totalCol);
            	totalNum=totalNum+dtos.size();
            	System.out.println("�Ѿ��ύ������Ϊ��"+dtos.size()+"!  ���ύ����Ϊ��"+totalNum);
			}
        }catch(Exception exception){
        	exception.printStackTrace();
            throw exception;
        }finally{
            dbManager.close();
        }
    }
    /**
     * �����������ݿ�����
     * @param tableName ����
     * @param dtos ���������
     * @param totalCol ������
     * @throws Exception
     * @author wzs
     */
    public void insertAll(String tableName,List<String[]> dtos,int totalCol)
            throws Exception{
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("INSERT INTO ");
        buffer.append(tableName);
        buffer.append("  VALUES(? ");
        for (int i = 1; i < totalCol; i++) {
        	buffer.append(",?");
		}
        buffer.append(")");
        dbManager.prepareStatement(buffer.toString());
        System.out.println(buffer.toString());
		for (String[] dto : dtos) {
			for (int i = 0; i < totalCol; i++) {
				dbManager.setString(i + 1, dto[i]);
			}
			dbManager.addBatch();
		}
		dbManager.executePreparedUpdateBatch();
    }
}

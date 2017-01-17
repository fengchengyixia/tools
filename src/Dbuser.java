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
     * 资源管理类的实例，处理数据库操作.
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
     * 按主键查找一条数据（列级元数据）
     * @param tabName  表名
     * @return 0列名，1列注释，2列数据类型，3列数据长度，4对应表名，5对应表列名，6是否转换标志，7转换sql ，8可为null标志
     * @throws Exception
     * @author wzs
     */
    public List<String[]> queryBySql(String tabName) throws Exception{
        //声明DTO
        List<String[]> result=null;
        try{
            dbManager.open("undwrtDataSource");
            //查询数据,赋值给DTO
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
     * 查询列级表元数据
     * @param tabName 平台表名
     * @return 0列名，1列注释，2列数据类型，3列数据长度，4对应表名，5对应表列名，6是否转换标志，7转换sql ，8可为null标志
     * @throws Exception
     * @author wzs
     */
	public List<String[]> findByTabName(String tabName) throws Exception {
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuffer buffer = new StringBuffer(200);
		buffer.append("SELECT a.column_name ,(select comments from user_col_comments  WHERE table_name=a.table_name AND column_name=a.column_name )， a.data_type, '('||a.DATA_LENGTH||')', pro.prptab_name ,pro.prpcol_name,pro.conversionflag,pro.conversionsql,pro.isnonempty from user_tab_columns  a LEFT JOIN map_proprp_col  pro  ON  a.table_name=pro.protab_name  and a.column_name=pro.procol_name	" + "WHERE   a.table_name= ? ORDER BY a.column_name");
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
	 * 查询表级映射
	 * @param tabName 表名
	 * @param bddjType 类型
	 * @return map_proprp_tab表对象
	 * @throws Exception
	 * @author wzs
	 */
    public List<String[]> queryFromBySql(String tabName,String bddjType) throws Exception{
        //声明DTO
        List<String[]> result=null;
        try{
            dbManager.open("undwrtDataSource");
            //查询数据,赋值给DTO
            result = findFromByTabName(tabName,bddjType);
        }catch(Exception exception){
            throw exception;
        }finally{
            dbManager.close();
        }
        return result;
    }
	/**
	 * 查询表级映射
	 * @param tabName 表名
	 * @param bddjType 类型
	 * @return map_proprp_tab表对象
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
            	aStrings[0]=dbManager.getString(resultSet,1); //PROTABLE_NAME 平台表表名
            	aStrings[1]=dbManager.getString(resultSet,2); //PRPTABLE_NAME 需要连接的业务表别名（需要对应select中的表名）
            	aStrings[2]=dbManager.getString(resultSet,3); // REALPRPTABLE_NAME 需要连接的业务表真实表名
            	aStrings[3]=dbManager.getString(resultSet,4); // SELFLINKCOLUMU 此业务表对外连接的列（多列需要#号分隔）
            	aStrings[4]=dbManager.getString(resultSet,5); // BASELINKCOLUMU 此业务表要连接的列（多列需要#号分隔），如（PRPCMAIN.POLICYNO#PRPCMAIN.PROSALNO）
            	aStrings[5]=dbManager.getString(resultSet,6); //LINKORDER  连接顺序
            	aStrings[6]=dbManager.getString(resultSet,7); // LINKTYPE 	登记类型（1：新单承保...）
            	aStrings[7]=dbManager.getString(resultSet,10); // FLAG 为1时用虚拟表查询
            	aStrings[8]=dbManager.getString(resultSet,8);  //PRPTABLE_OWNER 连接表所属的用户
            	aStrings[9]=dbManager.getString(resultSet,9); //CONDITION 连接条件WHERE字句
            	aStrings[10]=dbManager.getString(resultSet,11);  //PHONEYTABLE 当flag为1时用的虚拟表
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
						throw new Exception(aStrings[0]+"表对应连接业务表："+aStrings[1]+"的flag配置不正确！");
					}
				}else {
					if (aStrings[8].length() > 0 && aStrings[2].length() > 0) {
						aStrings[2] = aStrings[8] + "." + aStrings[2];
						//如果有配置表用户，则加上，没有则默认默认账户，则不加
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
     * 根据表名，注释，和类型生成insert into sql语句
     * @param tabName 表名
     * @param zhusi 注释
     * @param bddjType 类型
     * @param flag 标志
     * @return insert into  sql语句
     * @throws Exception
     * @author wzs
     */
    public String  getSqlByTableName (String tabName,String zhusi,String bddjType,String flag) throws Exception {
		StringBuffer sb = new StringBuffer(500);
		List<String[]> queryBySql = queryBySql(tabName);
		boolean isE=true;  //是否是网页显示
		boolean isSpecialDeal=false; // 是否需要做特殊处理(主键冲突处理)
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
			String prpString="";   //select的值处理
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
					throw new Exception("From 后缺失表对象，请添加数据库配置信息("+tabName+zhusi+prpTableName+")");
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
     * 根据表名和标志得出对应特殊处理的sql
     * @param tabName 表名
     * @param flag 标志
     * @return 特殊处理的的sql
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
     * 根据表名获取主键的所有列名
     * @param tabName 表名
     * @return 主键信息
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
     * 根据sql查询数据结果（要求所有字段类型为String类型）
     * @param sql  查询的sql
     * @param condition  对应的参数
     * @param resultNum  返回的列数
     * @return 查询结果
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
     * 根据sql查询数据结果（要求所有字段类型为String类型）facade层
     * @param sql  查询的sql
     * @param condition  对应的参数
     * @param resultNum  返回的列数
     * @return 查询结果
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
     * 根据sql查询数据结果（要求所有字段类型为String类型）facade层
     * @param sql  查询的sql
     * @param condition  对应的参数
     * @param resultNum  返回的列数
     * @return 查询结果
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
     * 获取列默认值
     * @param isNonNull 是否可为null标志
     * @param type  列数据类型
     * @param excep 表和列信息
     * @return 默认值
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
			throw new Exception(excep+"是否可为null没有配置，请确认！");
		}
		return prpString;
	}
    
    /**
     * 执行ddl语句
     * @author wzs
     */
    public int creatProcedure(String sql) throws Exception {
    	int executePreparedUpdate=-1;
    	 try{
             dbManager.open("undwrtDataSource");
             //查询数据,赋值给DTO
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
     * 查询表的列数
     * @param tableName
     * @return 表的总列数
     * @throws Exception
     * @author wzs
     */
    public int getColNum(String tableName) throws Exception {
    	int result=0;
    	try{
            dbManager.open("visaDataSource");
            //查询数据,赋值给DTO
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
     * 批量插入数据库数据  action方法
     * @param tableName 表名
     * @param dtos  插入的数据
     * @param totalCol  总列数
     * @throws Exception
     * @author wzs
     */
    public void insertAllAction(String tableName,List<List<String[]>> dtoss,int totalCol)
            throws Exception{
    	try{
            dbManager.open("visaDataSource");
            //查询数据,赋值给DTO
            int totalNum=0;
            for (List<String[]> dtos : dtoss) {
            	insertAll (tableName,dtos,totalCol);
            	totalNum=totalNum+dtos.size();
            	System.out.println("已经提交的行数为："+dtos.size()+"!  共提交行数为："+totalNum);
			}
        }catch(Exception exception){
        	exception.printStackTrace();
            throw exception;
        }finally{
            dbManager.close();
        }
    }
    /**
     * 批量插入数据库数据
     * @param tableName 表名
     * @param dtos 插入的数据
     * @param totalCol 总列数
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

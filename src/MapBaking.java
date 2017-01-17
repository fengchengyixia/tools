package com.sinosoft.undwrt.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapBaking {
	private static DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
	private boolean isNew=false;
	/**
	 * 源程序备份
	 * @return
	 * @author wzs
	 */
	public String codeBaking() {
		String codeSoucePath="D:\\workSpace\\undwrt\\modules\\component\\com\\sinosoft\\undwrt\\servlet\\";
		String pageSoucePath="D:\\workSpace\\undwrt\\modules\\webapps\\undwrt\\";
		String tagetPath="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\源代码备份\\";
		String nowDate = dateFormat.format(new Date());
		List<File> fileList = new ArrayList<File>();
		fileList.add(new File(codeSoucePath+"CreatSqlContext.java"));
		fileList.add(new File(codeSoucePath+"Dbuser.java"));
		fileList.add(new File(codeSoucePath+"IoUtil.java"));
		fileList.add(new File(codeSoucePath+"UtilTxt.java"));
		fileList.add(new File(codeSoucePath+"MapBaking.java"));
		fileList.add(new File(codeSoucePath+"ProcedureDto.java"));
		fileList.add(new File(pageSoucePath+"bakresult.jsp"));
		fileList.add(new File(pageSoucePath+"creatSql.jsp"));
		fileList.add(new File(pageSoucePath+"creatSqlresult.jsp"));
		fileList.add(new File(pageSoucePath+"recoverResult.jsp"));
		File tagetFile=new File(tagetPath+nowDate);
		if (isNew) {
			int i=1;
			while (tagetFile.exists()) {
				 tagetFile=new File(tagetPath+nowDate+"_"+i);
				i++;
			}
		}else {
			if (tagetFile.exists()) {
				tagetFile.delete();
			}
		}
		tagetFile.mkdirs();
		for (File file : fileList) {
			IoUtil.copyFile(file, tagetFile.getAbsolutePath());
		}
		String codeSoucePathETL="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\ETL备份\\bddj";
		String tagetPathETL="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\ETL备份\\";
		File tagetFileETL=new File(tagetPath+nowDate);
		if (isNew) {
			int i=1;
			while (tagetFileETL.exists()) {
				tagetFileETL=new File(tagetPath+nowDate+"_"+i);
				i++;
			}
		}else {
			if (tagetFileETL.exists()) {
				tagetFileETL.delete();
			}
		}
		tagetFileETL.mkdirs();
		tagetPathETL=tagetPathETL+tagetFileETL.getName();
		IoUtil.copyFolder(codeSoucePathETL, tagetPathETL);
		return "源程序和源ETL程序备份成功！";
	}
	
	/**
	 * 映射备份
	 * @return 成功标志
	 * @throws Exception
	 * @author pc
	 */
	public String  MappingBaking() throws Exception {
		String filePath="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\映射备份";
		File modelFile = new File(filePath+"\\映射备份模板.xlsx");
		String nowDate = dateFormat.format(new Date());
		File tagetFile=new File(filePath+"\\"+nowDate+".xlsx13");
		if (isNew) {
			int i=1;
			while (tagetFile.exists()) {
				 tagetFile=new File(filePath+"\\"+nowDate+"_"+i+".xlsx13");
				i++;
			}
		}else {
			if (tagetFile.exists()) {
				tagetFile.delete();
			}
		}
		String tagetFileName=tagetFile.getName();
		IoUtil.copyFile(modelFile, filePath, tagetFileName);
//		tagetFile.renameTo(new File(tagetFile.getAbsolutePath()+"13"));
//		tagetFileName=tagetFile.getName();
		Dbuser dbuser = new Dbuser();
		//8 6 9 12 lie
		String map_ldcodeSql="SELECT a.*,b.codealias from  MAP_LDCODE	a LEFT JOIN ldcode b ON a.codetype=b.codetype AND a.targetcode=b.code ORDER BY a.codetype,a.targetcode";
		String ldcodeSql="SELECT * from ldcode ORDER BY codetype,code";
		String ldcodeMappingSql="SELECT * from ldcodemapping ORDER BY codetype,targetcode"; 
		String dpmTableColumnsSql="SELECT * from dpmtablecolumns ORDER BY cid,column_name";
		List<String[]> ldcodeMappingList = dbuser.querybySqlFacade(ldcodeMappingSql, null,9,true);
//		MapBaking mapBaking = new MapBaking();
		writer(filePath,tagetFileName, "xlsx", ldcodeMappingList, null, "LDCODEMAPPING");
		ldcodeMappingList=null;
		System.out.println("ldcodeMappingSql已经完成");
		List<String[]> map_ldcodeList = dbuser.querybySqlFacade(map_ldcodeSql, null,8,true);
//		mapBaking = new MapBaking();
		writer(filePath, tagetFileName, "xlsx", map_ldcodeList, null, "MAP_LDCODE");
		map_ldcodeList=null;
		System.out.println("map_ldcodeSql已经完成");
		List<String[]> ldcodeList = dbuser.querybySqlFacade(ldcodeSql, null,6,true);
//		mapBaking = new MapBaking();
		writer(filePath,tagetFileName, "xlsx", ldcodeList, null, "LDCODE");
		ldcodeList=null;
		System.out.println("ldcodeSql已经完成");
		List<String[]> dpmTableColumnsList = dbuser.querybySqlFacade(dpmTableColumnsSql, null,12,true);
//		mapBaking = new MapBaking();
		writer(filePath,tagetFileName, "xlsx", dpmTableColumnsList, null, "DPMTABLECOLUMNS");
		dpmTableColumnsList=null;
		System.out.println("dpmTableColumnsSql已经完成");

		return "映射备份成功";
	}
	/**
	 * 映射备份成Txt文件
	 * @return 成功标志
	 * @throws Exception
	 * @author pc
	 */
	public String  MappingTxtBaking() throws Exception {
		String filePath="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\映射备份\\";
		String nowDate = dateFormat.format(new Date());
		File tagetFile=new File(filePath+nowDate);
		if (isNew) {
			int i=1;
			while (tagetFile.exists()) {
				 tagetFile=new File(filePath+nowDate+"_"+i);
				i++;
			}
		}else {
			if (tagetFile.exists()) {
				tagetFile.delete();
			}
		}
		tagetFile.mkdirs();
		String tagetFilePath=tagetFile.getAbsolutePath();
		Dbuser dbuser = new Dbuser();
		UtilTxt utilTxt = new UtilTxt();
		//8 6 9 12 lie
		String map_ldcodeSql="SELECT a.*,b.codealias from  MAP_LDCODE	a LEFT JOIN ldcode b ON a.codetype=b.codetype AND a.targetcode=b.code ORDER BY a.codetype,a.targetcode";
		String ldcodeSql="SELECT * from ldcode ORDER BY codetype,code";
		String ldcodeMappingSql="SELECT * from ldcodemapping ORDER BY codetype,targetcode"; 
		String dpmTableColumnsSql="SELECT * from dpmtablecolumns ORDER BY cid,column_name";
		List<String[]> ldcodeMappingList = dbuser.querybySqlFacade(ldcodeMappingSql, null,9,true);
		utilTxt.write(tagetFilePath, "ldcodeMapping.txt", ldcodeMappingList);
		ldcodeMappingList=null;
		System.out.println("ldcodeMappingSql已经完成");
		List<String[]> map_ldcodeList = dbuser.querybySqlFacade(map_ldcodeSql, null,8,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "map_ldcode.txt", map_ldcodeList);
		map_ldcodeList=null;
		System.out.println("map_ldcodeSql已经完成");
		List<String[]> ldcodeList = dbuser.querybySqlFacade(ldcodeSql, null,6,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "ldcode.txt", ldcodeList);
		ldcodeList=null;
		System.out.println("ldcodeSql已经完成");
		List<String[]> dpmTableColumnsList = dbuser.querybySqlFacade(dpmTableColumnsSql, null,12,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "dpmTableColumns.txt", dpmTableColumnsList);
		dpmTableColumnsList=null;
		System.out.println("dpmTableColumnsSql已经完成");

		return "映射备份成功";
	}
	
	/**
	 * 元数据备份
	 * @author wzs
	 */
	public String  baking() throws Exception {
		String modelFilePath="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\元数据备份\\元数据备份2017_模板.xlsx";
		File modelFile = new File(modelFilePath);
		String nowDate = dateFormat.format(new Date());
		String bakFilePathStr="E:\\g工作记录文档\\20161228保单登记\\基础数据备份\\元数据备份";
		String bakFileName="元数据备份"+nowDate+".xlsx";
		String bakFilePath=bakFilePathStr+"\\"+bakFileName;
		File bakFile = new File(bakFilePath);
		if (isNew) {
			int i=1;
			while (bakFile.exists()) {
				bakFileName = "元数据备份" + nowDate + "_" + i + ".xlsx";
				bakFilePath = bakFilePathStr + "\\" + bakFileName;
				bakFile = new File(bakFilePath);
				i++;
			}
		}else {
			if (bakFile.exists()) {
				bakFile.delete();
			}
		}
		IoUtil.copyFile(modelFile, bakFilePathStr, bakFileName);
		
		Dbuser dbuser = new Dbuser();
		String lieMapSql="SELECT * from MAP_PROPRP_COL ORDER BY protab_name,procol_name";
		List<String[]> lieList = dbuser.querybySqlFacade(lieMapSql, null, 11);
		writer(bakFilePathStr, bakFileName, "xlsx", lieList, null, "列级映射");
		
		String biaoMapSql="SELECT * from map_proprp_tab  ORDER BY linktype,protable_name ,linkorder";
		List<String[]> biaoList = dbuser.querybySqlFacade(biaoMapSql, null, 11);
		writer(bakFilePathStr, bakFileName, "xlsx", biaoList, null,"表级映射");
		
		String procedureMapSql="SELECT * FROM  map_proprp_procedure ORDER BY procedure_name";
		List<String[]> procedureList = dbuser.querybySqlFacade(procedureMapSql, null, 6);
		writer(bakFilePathStr, bakFileName, "xlsx", procedureList, null, "存储过程映射");
		
		return "源数据备份成功" ;
	}
	
	/**
	 * 向excle文件里写入数据
	 * @param path  文件路径
	 * @param fileName 文件名
	 * @param fileType 文件类型
	 * @param list 具体数据
	 * @param titleRow 列名
	 * @throws Exception 异常
	 * @author wzs
	 */
	public  void writer(String path, String fileName, String fileType, List<String[]> list, String titleRow[],String sheetName) throws Exception {
		Workbook wb = null;
		String excelPath = path + File.separator + fileName;
		File file = new File(excelPath);
		Sheet sheet = null;
		// 创建工作文档对象
		if (!file.exists()) {
			throw new Exception("文件不存在 （错误代码01）");
//			if (fileType.equals("xls")) {
//				wb = new HSSFWorkbook();
//
//			} else if (fileType.equals("xlsx")) {
//
//				wb = new XSSFWorkbook();
//			} else {
//				throw new Exception("文件格式不正确");
//			}
//			// 创建sheet对象
//			sheet = (Sheet) wb.createSheet("sheet1");
//			OutputStream outputStream = new FileOutputStream(excelPath);
//			wb.write(outputStream);
//			outputStream.flush();
//			outputStream.close();

		} else {
			FileInputStream fileInputStream = new FileInputStream(file);
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(fileInputStream);

			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(fileInputStream);

			} else {
				throw new Exception("文件格式不正确");
			}
		}
		// 创建sheet对象
		if (sheet == null) {
//			sheet = (Sheet) wb.createSheet("sheet1");
			sheet = wb.getSheet(sheetName);
		}

		// 添加表头
//		Row row = sheet.createRow(0);
//		Cell cell = row.createCell(0);
//		row.setHeight((short) 540);
//		cell.setCellValue("被保险人员清单"); // 创建第一行
//
//		CellStyle style = wb.createCellStyle(); // 样式对象
//		// 设置单元格的背景颜色为淡蓝色
//		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
//		style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
//		style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
//
//		cell.setCellStyle(style); // 样式，居中
//
//		Font font = wb.createFont();
//		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		font.setFontName("宋体");
//		font.setFontHeight((short) 280);
//		style.setFont(font);
//		// 单元格合并
//		// 四个参数分别是：起始行，起始列，结束行，结束列
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
//		sheet.autoSizeColumn(5200);
//
//		row = sheet.createRow(1); // 创建第二行
//		for (int i = 0; i < titleRow.length; i++) {
//			cell = row.createCell(i);
//			cell.setCellValue(titleRow[i]);
//			cell.setCellStyle(style); // 样式，居中
//			sheet.setColumnWidth(i, 20 * 256);
//		}
//		row.setHeight((short) 540);
		Row row =null;
		// 循环写入行数据
		String[] cellsStrings=null;
		Cell cell=null;
		for (int i = 0; i < list.size(); i++) {
			row = (Row) sheet.getRow(i+1);
			if (row==null) {
				row = sheet.createRow(i+1);
			}
			cellsStrings=list.get(i);
			for (int j = 0; j < cellsStrings.length+1; j++) {
				cell=row.getCell(j);
				if (cell==null) {
					cell = row.createCell(j);
				}
				if (j==0) {
					cell.setCellValue(i+1);
				}else {
					cell.setCellValue(cellsStrings[j-1]);
				}
			}
			System.out.println("已处理行数"+i);
//			row.setHeight((short) 500);
//			row.createCell(0).setCellValue((list.get(i)).getInsuraceUser());
//			row.createCell(1).setCellValue((list.get(i)).getIdCard());
//			row.createCell(2).setCellValue((list.get(i)).getType());
//			row.createCell(3).setCellValue((list.get(i)).getBankCardId());
//			row.createCell(4).setCellValue((list.get(i)).getMoney());
//			row.createCell(5).setCellValue((list.get(i)).getBuyTime());
//			row.createCell(6).setCellValue((list.get(i)).getInsStartTime());
//			row.createCell(7).setCellValue((list.get(i)).getInsEndTime());
		}

		// 创建文件流
		OutputStream stream = new FileOutputStream(excelPath);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
	}
	List<List<String[]>> partList=null;
	String tablename1="";
	int colNum=0;
	/**
	 * 将备份的excel文件的内容恢复到数据库
	 * @param filePath 文件路径
	 * @param tablename 表名
	 * @return 
	 * @throws Exception
	 * @author wzs
	 */
	public String recover(String filePath,String tablename) throws Exception {
		tablename=tablename==null?"":tablename.trim().toUpperCase();
		this.tablename1=tablename;
		List<List<String[]>> wb=null;
		Dbuser dbuser = new Dbuser();
		this.colNum = dbuser.getColNum(tablename);
		wb = read(filePath, tablename, 2, 2,1000,colNum);
		int partNum=0;
		for (int i = 0; i < wb.size(); i++,partNum++) {
			if (partNum==10) {
				partNum=0;
				new Thread(new Runnable() {
					@Override
					public void run() {
						Dbuser dbuser = new Dbuser();
						try {
							dbuser.insertAllAction(tablename1, partList, colNum);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			if (partNum==0) {
				this.partList=new LinkedList<List<String[]>>();
			}
			partList.add(wb.get(i));
		}
		return "还原成功！";
	}
	
	
	/**
	 * 读取excel sheet页的内容 ，
	 * @param path excel绝对路径
	 * @param sheetName sheet页名
	 * @param beginRow 数据开始行
	 * @param beginCol 数据开始列
	 * @param partRows 一个微集合的总行数（数据量单位）
	 * @param totalCol 数据收集的总列数（数据量单位）
	 * @return List<List<String[]>>  里面的集合是一个单位的数据
	 * @throws Exception 
	 * @author wzs
	 */
	public List<List<String[]>> read(String path,String sheetName,int beginRow,int beginCol,int partRows,int totalCol) throws Exception {
		List<List<String[]>> result = new LinkedList<List<String[]>>();
		FileInputStream fileInputStream=null;
		try {
			Workbook wb = null;
			File file = new File(path);
			if (!file.exists()) {
				throw new Exception("文件不存在，请确认！");
			}
			fileInputStream = new FileInputStream(file);
			if (path.indexOf(".xlsx") > -1) {
				wb = new XSSFWorkbook(fileInputStream);
			} else if (path.indexOf(".xls") > -1) {
				wb = new HSSFWorkbook(fileInputStream);
			} else {
				throw new Exception("文件格式不正确");
			}
			Sheet sheet = wb.getSheet(sheetName);
			int rows = 1;
			List<String[]> rowList = null;
			String[] cells = null;
			for (int i = beginRow - 1; i < sheet.getLastRowNum(); i++) {
				if (rows == 1) {
					rowList = new LinkedList<String[]>();
				}
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				cells = new String[totalCol];
				for (int j = beginCol - 1; j < totalCol + beginCol - 1; j++) {
					Cell cell = row.getCell(j);
					cells[j - beginCol + 1] = cell.getStringCellValue();
				}
				rowList.add(cells);
				if (rows == partRows) {
					result.add(rowList);
					rows = 0;
				}
				rows++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if (fileInputStream!=null) {
				fileInputStream.close();
			}
		}
		return result;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}

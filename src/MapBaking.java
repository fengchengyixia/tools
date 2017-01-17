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
	 * Դ���򱸷�
	 * @return
	 * @author wzs
	 */
	public String codeBaking() {
		String codeSoucePath="D:\\workSpace\\undwrt\\modules\\component\\com\\sinosoft\\undwrt\\servlet\\";
		String pageSoucePath="D:\\workSpace\\undwrt\\modules\\webapps\\undwrt\\";
		String tagetPath="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\Դ���뱸��\\";
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
		String codeSoucePathETL="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\ETL����\\bddj";
		String tagetPathETL="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\ETL����\\";
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
		return "Դ�����ԴETL���򱸷ݳɹ���";
	}
	
	/**
	 * ӳ�䱸��
	 * @return �ɹ���־
	 * @throws Exception
	 * @author pc
	 */
	public String  MappingBaking() throws Exception {
		String filePath="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\ӳ�䱸��";
		File modelFile = new File(filePath+"\\ӳ�䱸��ģ��.xlsx");
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
		System.out.println("ldcodeMappingSql�Ѿ����");
		List<String[]> map_ldcodeList = dbuser.querybySqlFacade(map_ldcodeSql, null,8,true);
//		mapBaking = new MapBaking();
		writer(filePath, tagetFileName, "xlsx", map_ldcodeList, null, "MAP_LDCODE");
		map_ldcodeList=null;
		System.out.println("map_ldcodeSql�Ѿ����");
		List<String[]> ldcodeList = dbuser.querybySqlFacade(ldcodeSql, null,6,true);
//		mapBaking = new MapBaking();
		writer(filePath,tagetFileName, "xlsx", ldcodeList, null, "LDCODE");
		ldcodeList=null;
		System.out.println("ldcodeSql�Ѿ����");
		List<String[]> dpmTableColumnsList = dbuser.querybySqlFacade(dpmTableColumnsSql, null,12,true);
//		mapBaking = new MapBaking();
		writer(filePath,tagetFileName, "xlsx", dpmTableColumnsList, null, "DPMTABLECOLUMNS");
		dpmTableColumnsList=null;
		System.out.println("dpmTableColumnsSql�Ѿ����");

		return "ӳ�䱸�ݳɹ�";
	}
	/**
	 * ӳ�䱸�ݳ�Txt�ļ�
	 * @return �ɹ���־
	 * @throws Exception
	 * @author pc
	 */
	public String  MappingTxtBaking() throws Exception {
		String filePath="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\ӳ�䱸��\\";
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
		System.out.println("ldcodeMappingSql�Ѿ����");
		List<String[]> map_ldcodeList = dbuser.querybySqlFacade(map_ldcodeSql, null,8,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "map_ldcode.txt", map_ldcodeList);
		map_ldcodeList=null;
		System.out.println("map_ldcodeSql�Ѿ����");
		List<String[]> ldcodeList = dbuser.querybySqlFacade(ldcodeSql, null,6,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "ldcode.txt", ldcodeList);
		ldcodeList=null;
		System.out.println("ldcodeSql�Ѿ����");
		List<String[]> dpmTableColumnsList = dbuser.querybySqlFacade(dpmTableColumnsSql, null,12,true);
//		mapBaking = new MapBaking();
		utilTxt.write(tagetFilePath, "dpmTableColumns.txt", dpmTableColumnsList);
		dpmTableColumnsList=null;
		System.out.println("dpmTableColumnsSql�Ѿ����");

		return "ӳ�䱸�ݳɹ�";
	}
	
	/**
	 * Ԫ���ݱ���
	 * @author wzs
	 */
	public String  baking() throws Exception {
		String modelFilePath="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\Ԫ���ݱ���\\Ԫ���ݱ���2017_ģ��.xlsx";
		File modelFile = new File(modelFilePath);
		String nowDate = dateFormat.format(new Date());
		String bakFilePathStr="E:\\g������¼�ĵ�\\20161228�����Ǽ�\\�������ݱ���\\Ԫ���ݱ���";
		String bakFileName="Ԫ���ݱ���"+nowDate+".xlsx";
		String bakFilePath=bakFilePathStr+"\\"+bakFileName;
		File bakFile = new File(bakFilePath);
		if (isNew) {
			int i=1;
			while (bakFile.exists()) {
				bakFileName = "Ԫ���ݱ���" + nowDate + "_" + i + ".xlsx";
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
		writer(bakFilePathStr, bakFileName, "xlsx", lieList, null, "�м�ӳ��");
		
		String biaoMapSql="SELECT * from map_proprp_tab  ORDER BY linktype,protable_name ,linkorder";
		List<String[]> biaoList = dbuser.querybySqlFacade(biaoMapSql, null, 11);
		writer(bakFilePathStr, bakFileName, "xlsx", biaoList, null,"��ӳ��");
		
		String procedureMapSql="SELECT * FROM  map_proprp_procedure ORDER BY procedure_name";
		List<String[]> procedureList = dbuser.querybySqlFacade(procedureMapSql, null, 6);
		writer(bakFilePathStr, bakFileName, "xlsx", procedureList, null, "�洢����ӳ��");
		
		return "Դ���ݱ��ݳɹ�" ;
	}
	
	/**
	 * ��excle�ļ���д������
	 * @param path  �ļ�·��
	 * @param fileName �ļ���
	 * @param fileType �ļ�����
	 * @param list ��������
	 * @param titleRow ����
	 * @throws Exception �쳣
	 * @author wzs
	 */
	public  void writer(String path, String fileName, String fileType, List<String[]> list, String titleRow[],String sheetName) throws Exception {
		Workbook wb = null;
		String excelPath = path + File.separator + fileName;
		File file = new File(excelPath);
		Sheet sheet = null;
		// ���������ĵ�����
		if (!file.exists()) {
			throw new Exception("�ļ������� ���������01��");
//			if (fileType.equals("xls")) {
//				wb = new HSSFWorkbook();
//
//			} else if (fileType.equals("xlsx")) {
//
//				wb = new XSSFWorkbook();
//			} else {
//				throw new Exception("�ļ���ʽ����ȷ");
//			}
//			// ����sheet����
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
				throw new Exception("�ļ���ʽ����ȷ");
			}
		}
		// ����sheet����
		if (sheet == null) {
//			sheet = (Sheet) wb.createSheet("sheet1");
			sheet = wb.getSheet(sheetName);
		}

		// ��ӱ�ͷ
//		Row row = sheet.createRow(0);
//		Cell cell = row.createCell(0);
//		row.setHeight((short) 540);
//		cell.setCellValue("��������Ա�嵥"); // ������һ��
//
//		CellStyle style = wb.createCellStyle(); // ��ʽ����
//		// ���õ�Ԫ��ı�����ɫΪ����ɫ
//		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//
//		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// ��ֱ
//		style.setAlignment(CellStyle.ALIGN_CENTER);// ˮƽ
//		style.setWrapText(true);// ָ������Ԫ��������ʾ����ʱ�Զ�����
//
//		cell.setCellStyle(style); // ��ʽ������
//
//		Font font = wb.createFont();
//		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//		font.setFontName("����");
//		font.setFontHeight((short) 280);
//		style.setFont(font);
//		// ��Ԫ��ϲ�
//		// �ĸ������ֱ��ǣ���ʼ�У���ʼ�У������У�������
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
//		sheet.autoSizeColumn(5200);
//
//		row = sheet.createRow(1); // �����ڶ���
//		for (int i = 0; i < titleRow.length; i++) {
//			cell = row.createCell(i);
//			cell.setCellValue(titleRow[i]);
//			cell.setCellStyle(style); // ��ʽ������
//			sheet.setColumnWidth(i, 20 * 256);
//		}
//		row.setHeight((short) 540);
		Row row =null;
		// ѭ��д��������
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
			System.out.println("�Ѵ�������"+i);
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

		// �����ļ���
		OutputStream stream = new FileOutputStream(excelPath);
		// д������
		wb.write(stream);
		// �ر��ļ���
		stream.close();
	}
	List<List<String[]>> partList=null;
	String tablename1="";
	int colNum=0;
	/**
	 * �����ݵ�excel�ļ������ݻָ������ݿ�
	 * @param filePath �ļ�·��
	 * @param tablename ����
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
		return "��ԭ�ɹ���";
	}
	
	
	/**
	 * ��ȡexcel sheetҳ������ ��
	 * @param path excel����·��
	 * @param sheetName sheetҳ��
	 * @param beginRow ���ݿ�ʼ��
	 * @param beginCol ���ݿ�ʼ��
	 * @param partRows һ��΢���ϵ�����������������λ��
	 * @param totalCol �����ռ�������������������λ��
	 * @return List<List<String[]>>  ����ļ�����һ����λ������
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
				throw new Exception("�ļ������ڣ���ȷ�ϣ�");
			}
			fileInputStream = new FileInputStream(file);
			if (path.indexOf(".xlsx") > -1) {
				wb = new XSSFWorkbook(fileInputStream);
			} else if (path.indexOf(".xls") > -1) {
				wb = new HSSFWorkbook(fileInputStream);
			} else {
				throw new Exception("�ļ���ʽ����ȷ");
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

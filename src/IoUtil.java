package com.sinosoft.undwrt.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class IoUtil {
	/**
	 * ���Ƶ����ļ���Ŀ���ļ���
	 * @param oldFile ԭ�ļ�
	 * @param newPath Ŀ���ļ��о���·��
	 * @return �ɹ���ʧ��
	 * @author wzs
	 */
	public static boolean copyFile(File oldFile, String newPath) {
		InputStream inStream=null;
		FileOutputStream fs =null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File newFilePath = new File(newPath);
			if (!newFilePath.exists()) {
				newFilePath.mkdirs();
			}
			String oldFileName = oldFile.getName();
			String newFileName = newPath + File.separator + oldFileName;
			if (oldFile.exists()) { // �ļ�����ʱ
				inStream = new FileInputStream(oldFile); // ����ԭ�ļ�
				fs= new FileOutputStream(newFileName);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();
			return false;
		}finally{
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (fs != null) {
					fs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * ���Ƶ����ļ���Ŀ���ļ���
	 * @param oldFile ԭ�ļ�
	 * @param newPath Ŀ���ļ��о���·��
	 * @param newFile_Name Ŀ���ļ���
	 * @return �ɹ���ʧ��
	 * @author wzs
	 */
	public static boolean copyFile(File oldFile, String newPath,String newFile_Name) {
		InputStream inStream=null;
		FileOutputStream fs =null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File newFilePath = new File(newPath);
			if (!newFilePath.exists()) {
				newFilePath.mkdirs();
			}
			String oldFileName = oldFile.getName();
			if (newFile_Name != null && newFile_Name.trim().length() > 0) {
				
			}
			String newFileName = newPath + File.separator + newFile_Name;
			if (oldFile.exists()) { // �ļ�����ʱ
				inStream = new FileInputStream(oldFile); // ����ԭ�ļ�
				fs= new FileOutputStream(newFileName);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();
			return false;
		}finally{
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (fs != null) {
					fs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * ���������ļ�������
	 * @param oldPath String ԭ�ļ�·�� �磺c:/fqf
	 * @param newPath String ���ƺ�·�� �磺f:/fqf/ff
	 * @return boolean
	 */
	public static boolean  copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // ����ļ��в����� �������ļ���
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// ��������ļ���
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("���������ļ������ݲ�������");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

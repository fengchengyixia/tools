package com.sinosoft.undwrt.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class IoUtil {
	/**
	 * 复制单个文件到目标文件夹
	 * @param oldFile 原文件
	 * @param newPath 目标文件夹绝对路径
	 * @return 成功或失败
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
			if (oldFile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldFile); // 读入原文件
				fs= new FileOutputStream(newFileName);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
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
	 * 复制单个文件到目标文件夹
	 * @param oldFile 原文件
	 * @param newPath 目标文件夹绝对路径
	 * @param newFile_Name 目标文件名
	 * @return 成功或失败
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
			if (oldFile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldFile); // 读入原文件
				fs= new FileOutputStream(newFileName);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
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
	 * 复制整个文件夹内容
	 * @param oldPath String 原文件路径 如：c:/fqf
	 * @param newPath String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static boolean  copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
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
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

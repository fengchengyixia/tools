package com.sinosoft.undwrt.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class UtilTxt {
	/**
	*@pram path
	*/
	public String  write(String path, String fileName,List<String[]> list) throws Exception {
		String txtPath = path + File.separator + fileName;
		BufferedWriter Writer=null;
		try {
			File file = new File(txtPath);
			Writer = new BufferedWriter(new FileWriter(file));
			StringBuffer bf = new StringBuffer();
			for (String[] strings : list) {
				bf.setLength(0);
				bf.append(strings[0]);
				for (int i = 1; i < strings.length; i++) {
					bf.append("#" + strings[i]);
				}
				bf.append("\r\n");
				Writer.write(bf.toString());
				Writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e ;
		}finally{
			if (Writer!=null) {
				Writer.flush();
				Writer.close();
			}
		}
		return null;
	}
}

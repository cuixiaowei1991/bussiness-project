package com.cn.common;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class WriteExcelFile {
	private static WritableCellFormat dateFormat;
	private static WritableCellFormat menuTitleFormat;
	private static WritableCellFormat titleFormat;

	/**
	 * 写excel文件；
	 *
	 * @param data
	 *            数据；
	 * @param sheetHead
	 *            表头；
	 * @param sheetName
	 *            表名；
	 * @param contextPath
	 *            文件储存路径
	 * @return
	 */

	public static String writeExcelFile(List<String[]> data,String filename,
										String[] sheetHead, String sheetName, 
										String contextPath,String saveExcelPath)throws Exception {
		WritableWorkbook book = null;
		File file = new File(saveExcelPath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 生成excel文件的完整路径（以当前时间生成名字）
		String name = filename+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+ ".xls";
		String pathName =contextPath+name;

		String saveName=saveExcelPath+name;
		try {
			book = Workbook.createWorkbook(new File(saveName));
			setSheetStyle();// 设置样式；
			WritableSheet sheet = WriteExcelFile.createSheet(book, sheetName,
					sheetHead);// 创建表头
			int row = 1;
			for (String[] rowData : data) {
				WriteExcelFile.writeData(sheet, rowData, row);
				row++;
			}
			book.write();
		}finally {
			if (book != null) {
				book.close();
			}
		}
		return pathName;
	}
	/**
	 * 
	 * @param data
	 * @param filename
	 * @param sheetHead
	 * @param sheetName
	 * @param response
	 */
	public static void writeExcel(List<String[]> data,String filename,String[] sheetHead,String sheetName,
			String saveExcelPath,HttpServletResponse response) throws Exception {

		WritableWorkbook book = null;
		File file = new File(saveExcelPath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 生成excel文件的完整路径（以当前时间生成名字）
		String name = filename
				+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
						.format(new Date()) + ".xls";
		String saveName = saveExcelPath + name;
		try {
			book = Workbook.createWorkbook(new File(saveName));
			setSheetStyle();// 设置样式；
			WritableSheet sheet = WriteExcelFile.createSheet(book, sheetName,
					sheetHead);// 创建表头
			int row = 1;
			for (String[] rowData : data) {
				WriteExcelFile.writeData(sheet, rowData, row);
				row++;
			}
			book.write();
		} finally {
			if (book != null) {
				book.close();
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		response.setHeader("content-disposition", "attachment;filename="
				+ URLEncoder.encode(name, "UTF-8"));
		InputStream in = new FileInputStream(saveName);
		int lenExcel = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        while ((lenExcel = in.read(buffer)) > 0) {
            out.write(buffer, 0, lenExcel);
        }
        in.close();
        /**删除掉文件**/
        File fileToDelete = new File(saveName);
		if (fileToDelete.isFile() && fileToDelete.exists()) {
			fileToDelete.delete();
		}
	}
	
	

	private static void setSheetStyle() {
		jxl.write.WritableFont titleFont = new jxl.write.WritableFont(
				WritableFont.createFont("宋体"), 12, WritableFont.BOLD); // 字体
		titleFormat = new WritableCellFormat(titleFont); // 表头格式；
		dateFormat = new WritableCellFormat();// 内容格式
		menuTitleFormat = new WritableCellFormat();// 标题格式
		try {
			dateFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			dateFormat.setAlignment(Alignment.CENTRE); // 水平居中
			dateFormat.setWrap(true);

			titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			titleFormat.setAlignment(Alignment.CENTRE); // 水平居中

			menuTitleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			menuTitleFormat.setAlignment(Alignment.LEFT);  // 水平居中

		} catch (WriteException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 创建表头
	 *
	 * @param book
	 * @param sheetName
	 *            表名
	 * @param head
	 *            表头
	 * @return
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private static WritableSheet createSheet(WritableWorkbook book,
			String sheetName, String[] head) throws RowsExceededException,
			WriteException {
		// 生成第一张表及设置表名
		WritableSheet sheet = book.createSheet(sheetName, 0);
		CellView cellView = new CellView();
		cellView.setAutosize(true); //设置自动大小
		for (int i = 0; i < head.length; i++) {
			Label label = new Label(i, 0, head[i], titleFormat);
			sheet.addCell(label);
			sheet.setColumnView(i, cellView);//根据内容自动设置列宽
		}
		return sheet;
	}

	/**
	 * 写txt文件
	 *
	 * @param name
	 *            文件名
	 * @param codes
	 *            串码集合
	 * @param contextPath
	 *            保存路径
	 * @param saveExcelPath
	 *            读取路径
	 * @return
	 */
	public static String writerTxt(String name, List<String> codes,
			String contextPath, String saveExcelPath) {
		BufferedWriter fw = null;
		String na = name
				+ new SimpleDateFormat("yyyyMMddHHmmss")
						.format(new Date()) + ".txt";
		try {
			File file = new File(saveExcelPath + na);
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));// 指定编码格式，以免读取时中文字符异常
			if (codes != null && codes.size() != 0) {
				for (String s : codes) {
					fw.append(s);
					fw.newLine();
				}
			}
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return contextPath + na;
	}

	/**
	 * 写数据
	 *
	 * @param sheet
	 * @param data
	 *            数据；
	 * @param row
	 *            行数（表示数据写入的目标行);
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private static void writeData(WritableSheet sheet, String[] data, int row)
			throws RowsExceededException, WriteException {
		for (int i = 0; i < data.length; i++) {
			Label label = new Label(i, row, data[i], dateFormat);
			sheet.addCell(label);
		}
	}
	
	public static void main(String[] args) {
		
	  File fileToDelete = new File("/home/song/catalina.out");
	  if (fileToDelete.isFile() && fileToDelete.exists()) {
		   fileToDelete.delete();
	  }
	}
}


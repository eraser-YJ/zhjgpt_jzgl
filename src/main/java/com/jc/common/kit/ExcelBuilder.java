package com.jc.common.kit;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Excel文件
 * 
 * @author lc  liubq
 * @since 2017年12月22日
 */
public class ExcelBuilder {
	// Excel
	private HSSFWorkbook wb;
	// sheet
	private HSSFSheet sheet;
	// 标题样式
	private HSSFCellStyle titleStyle;
	// 无边框
	private HSSFCellStyle cellLeftStyle0;
	private HSSFCellStyle cellCenterStyle0;
	private HSSFCellStyle cellRightStyle0;
	// 单元格样式
	private HSSFCellStyle cellStyleCenter;
	private HSSFCellStyle cellStyleLeft;
	private HSSFCellStyle cellStyleRight;

	/**
	 * 打开文件
	 * 
	 * @throws Exception
	 */
	public void openFile() throws Exception {
		openFile("result");
	}

	/**
	 * 打开文件
	 * 
	 * @param title 第一个sheet 标题
	 * @throws Exception
	 */
	public void openFile(String title) throws Exception {
		openFile(title, 18);
	}

	/**
	 * 打开excel文件
	 * 
	 * @param title sheet名称
	 * @param colWidth 列宽
	 * @throws Exception
	 */
	public void openFile(String title, int colWidth) throws Exception {
		// 创建HSSFWorkbook对象
		wb = new HSSFWorkbook();
		// 创建HSSFSheet对象
		sheet = wb.createSheet(title);
		sheet.setDefaultColumnWidth(colWidth);
	}

	/**
	 * 标题表格样式 字体变大，加粗 没有边框，不换行
	 * 
	 * @return
	 */
	private HSSFCellStyle titleStyle() {
		if (titleStyle == null) {
			titleStyle = wb.createCellStyle();
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
			// 把字体应用到当前的样式
			titleStyle.setFont(font);
		}
		return titleStyle;
	}

	private HSSFCellStyle cellStyleCenter() {
		if (cellStyleCenter == null) {
			cellStyleCenter = wb.createCellStyle();
			cellStyleCenter.setWrapText(true);
			cellStyleCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyleCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyleCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyleCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		}
		return cellStyleCenter;
	}

	private HSSFCellStyle cellStyleLeft() {
		if (cellStyleLeft == null) {
			cellStyleLeft = wb.createCellStyle();
			cellStyleLeft.setWrapText(true);
			cellStyleLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyleLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyleLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyleLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
		}
		return cellStyleLeft;
	}

	private HSSFCellStyle cellStyleRight() {
		if (cellStyleRight == null) {
			cellStyleRight = wb.createCellStyle();
			cellStyleRight.setWrapText(true);
			cellStyleRight.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyleRight.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyleRight.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyleRight.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 居中
		}
		return cellStyleRight;
	}

	private HSSFCellStyle cellStyleLeft0() {
		if (cellLeftStyle0 == null) {
			cellLeftStyle0 = wb.createCellStyle();
			cellLeftStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellLeftStyle0.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setFontName("宋体");
			cellCenterStyle0.setFont(font);
			// 单元格格式
			HSSFDataFormat df = wb.createDataFormat();
			cellCenterStyle0.setDataFormat(df.getFormat("TEXT"));
		}
		return cellLeftStyle0;
	}

	private HSSFCellStyle cellStyleRight0() {
		if (cellRightStyle0 == null) {
			cellRightStyle0 = wb.createCellStyle();
			cellRightStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellRightStyle0.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 居中
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setFontName("宋体");
			cellCenterStyle0.setFont(font);
			// 单元格格式
			HSSFDataFormat df = wb.createDataFormat();
			cellCenterStyle0.setDataFormat(df.getFormat("TEXT"));
		}
		return cellRightStyle0;
	}

	private HSSFCellStyle cellStyleCenter0() {
		if (cellCenterStyle0 == null) {
			cellCenterStyle0 = wb.createCellStyle();
			cellCenterStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellCenterStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setFontName("宋体");
			cellCenterStyle0.setFont(font);
			// 单元格格式
			HSSFDataFormat df = wb.createDataFormat();
			cellCenterStyle0.setDataFormat(df.getFormat("TEXT"));
		}
		return cellCenterStyle0;
	}

	private HSSFCellStyle cellStyleCenter00() {
		if (cellCenterStyle0 == null) {
			cellCenterStyle0 = wb.createCellStyle();
			cellCenterStyle0.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 居中
			cellCenterStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setFontName("宋体");
			cellCenterStyle0.setWrapText(true);
			cellCenterStyle0.setFont(font);
			// 单元格格式
			HSSFDataFormat df = wb.createDataFormat();
			cellCenterStyle0.setDataFormat(df.getFormat("TEXT"));
		}
		return cellCenterStyle0;
	}

	/**
	 * 行
	 * 
	 * @param rowNum
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public HSSFRow getRow(int rowNum, int height) throws Exception {
		HSSFRow row = sheet.createRow(rowNum);
		row.setHeight((short) (25 * height));
		return row;
	}

	/**
	 * 标题单元格
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getTitle(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(titleStyle());
		return cell;
	}

	/**
	 * 无边线靠左
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellLeft0(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleLeft0());
		return cell;
	}

	/**
	 * 无边线靠右
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellRight0(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleRight0());
		return cell;
	}

	/**
	 * 无边线居中
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellCenter0(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleCenter0());
		return cell;
	}

	/**
	 * 无边线居中
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellCenter00(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleCenter00());
		return cell;
	}

	/**
	 * 单元格居中
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCell(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleCenter());
		return cell;
	}

	/**
	 * 单元格靠右左
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellLeft(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleLeft());
		return cell;
	}

	/**
	 * 单元格靠右
	 * 
	 * @param row
	 * @param colNum 列序号
	 * @return
	 * @throws Exception
	 */
	public HSSFCell getCellRight(HSSFRow row, int colNum) throws Exception {
		HSSFCell cell = row.createCell(colNum);
		cell.setCellStyle(cellStyleRight());
		return cell;
	}

	/**
	 * 合并单元格
	 * 
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public void mergeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress cra = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(cra);
	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 * @param response
	 * @throws Exception
	 */
	public void saveFile(String fileName, HttpServletResponse response) throws Exception {
		String name = fileName;
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(), "ISO8859-1"));
		// 输出Excel文件
		OutputStream fileOut = response.getOutputStream();
		wb.write(fileOut);
		fileOut.flush();
	}

	public void createFile(String filePath, String fileName) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath, fileName));
		wb.write(fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * 设定列宽
	 * 
	 * @param columnIndex
	 * @param colWidth
	 * @throws Exception
	 */
	public void setColumnWidth(int columnIndex, int colWidth) throws Exception {
		sheet.setColumnWidth(columnIndex, 500 * colWidth);
	}

}

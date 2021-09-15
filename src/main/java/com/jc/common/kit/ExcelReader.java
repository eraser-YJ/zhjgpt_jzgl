package com.jc.common.kit;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//
// HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls
// XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx
// 对于不同版本的EXCEL文档要使用不同的工具类，如果使用错了，会提示如下错误信息。
// org.apache.poi.openxml4j.exceptions.InvalidOperationException
// org.apache.poi.poifs.filesystem.OfficeXmlFileException
//

/**
 * Excel 导入导出
 * 
 * 
 * @author lc  liubq
 */
public class ExcelReader {

	/**
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 流的方式POI会比文件方式耗用更多的内存
	 * 
	 * @param inputStream Excel文件流
	 * @return 数据
	 * @throws Exception
	 */
	public static String[][] getExcelData(File excelFile) throws Exception {
		return getExcelData(excelFile, 0, 1);
	}

	/**
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 流的方式POI会比文件方式耗用更多的内存
	 * 
	 * @param inputStream Excel文件流
	 * @param indexSheet 页序号 0 开始
	 * @param startRow 开始行号 0开始，一般是 1
	 * @return 数据
	 * @throws Exception
	 */
	public static String[][] getExcelData(File excelFile, int indexSheet, int startRow) throws Exception {
		int columnSize = 0;
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(excelFile);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		int sheetNum = wb.getNumberOfSheets();
		if (indexSheet >= sheetNum) {
			return null;
		}
		Sheet st = null;
		Row row = null;
		Cell cell = null;
		List<String[]> result = new ArrayList<String[]>();
		List<String> rowData = null;
		st = wb.getSheetAt(indexSheet);
		for (int rowIndex = startRow; rowIndex <= st.getLastRowNum(); rowIndex++) {
			row = st.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			rowData = new ArrayList<String>();
			for (short columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
				cell = row.getCell(columnIndex);
				if (cell == null) {
					rowData.add("");
					continue;
				}
				String value = "";
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					try {
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							if (date != null) {
								SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								value = f.format(date);
							} else {
								value = "";
							}
						} else {
							value = getNumber(cell.getNumericCellValue());
						}
					} catch (Exception e) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						value = String.valueOf(cell.getStringCellValue());
					}
					break;
				case Cell.CELL_TYPE_FORMULA:
					try {
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							value = f.format(date);
							break;
						} else {
							value = getNumber(cell.getNumericCellValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
						cell.setCellType(Cell.CELL_TYPE_STRING);
						value = String.valueOf(cell.getRichStringCellValue());
					}
					break;
				case Cell.CELL_TYPE_BLANK:
					break;
				case Cell.CELL_TYPE_ERROR:
					value = "";
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					value = (cell.getBooleanCellValue() == true ? "Y" : "N");
					break;
				default:
					value = "";
				}
				rowData.add(rightTrim(value));
			}
			boolean needAdd = false;
			for (String value : rowData) {
				if (value == null || "".equals(value)) {
					continue;
				}
				needAdd = true;
				break;
			}

			if (needAdd) {
				if (columnSize == 0) {
					columnSize = rowData.size();
				}
				result.add(rowData.toArray(new String[0]));
			}
		}
		String[][] returnArray = new String[result.size()][columnSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		// 有些EXCEL读取进来全部是TRUE,FALSE 则EXCEL格式有问题，不识别
		boolean allError = true;
		for (String[] vArr : returnArray) {
			for (String v : vArr) {
				if ("FALSE".equalsIgnoreCase(v) || "TRUE".equalsIgnoreCase(v)) {
					continue;
				}
				allError = false;
				break;
			}
		}
		if (allError) {
			throw new Exception("读取的Excel文件包含特殊格式，不能正确读取数据，请调整后重新导入！");
		}
		return returnArray;
	}

	/**
	 * 去掉右侧空格
	 * 
	 * @param str
	 * @return
	 */
	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	/**
	 * 取得数字
	 * 
	 * @param d
	 * @return
	 */
	private static String getNumber(double d) {
		String value = String.valueOf(BigDecimal.valueOf(d));
		if (value.startsWith(".")) {
			value = "0" + value;
		}
		// 这里应该用
		if (value.indexOf(".") > 0) {
			while (value.endsWith("0")) {
				value = value.substring(0, value.length() - 1);
			}
		}
		if (value.endsWith(".")) {
			value = value.substring(0, value.length() - 1);
		}
		return value;
	}
}
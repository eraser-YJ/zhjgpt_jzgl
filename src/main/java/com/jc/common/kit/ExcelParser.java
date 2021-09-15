package com.jc.common.kit;

import com.jc.common.kit.ExcelSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lc  Administrator
 */
public class ExcelParser {
	/**
	 * 转换表格,默认为转换第一个表格（注意：poi官方文档建议文件方式参数，流的方式消耗内存会更大）
	 * 
	 * @param file 表格
	 * @param columsLength 读取多少列
	 * @return 表格转换器
	 */
	public static List<String[]> parse(File file, int columsLength) throws Exception {
		return parse(file, 1, columsLength);
	}

	/**
	 * 转换表格（注意：poi官方文档建议文件方式参数，流的方式消耗内存会更大）
	 * 
	 * @param file 表格
	 * @param startRow 从1开始
	 * @param columsLength 读取多少列
	 * @return 表格转换器
	 */
	public static List<String[]> parse(File file, int startRow, int columsLength) throws Exception {
		// 打开表格文件输入流
		OPCPackage pkg = OPCPackage.open(file);
		InputStream shellStream = null;
		try {
			// 创建表阅读器
			XSSFReader reader = new XSSFReader(pkg);
			// 转换指定单元表
			shellStream = reader.getSheet("rId" + 1);
			InputSource sheetSource = new InputSource(shellStream);
			StylesTable styles = reader.getStylesTable();
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
			// 事件处理类
			DefaultSheetHandler handler = new DefaultSheetHandler(startRow, columsLength);
			// 获取转换器
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(new ExcelSheetXMLHandler(styles, strings, handler, false));
			parser.parse(sheetSource);
			return handler.getDatas();
		} finally {
			try {
				if (shellStream != null) {
					shellStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				pkg.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 默认表格解析handder
	 */
	static class DefaultSheetHandler implements SheetContentsHandler {

		// 读取数据
		private List<String[]> datas = new ArrayList<String[]>();
		// 读取行信息,采用map主要是担心读取每行的单元格的顺序
		private Map<Integer, String> rowMap = new HashMap<Integer, String>();
		// 读取的列数
		private int columsLength;
		// 读取起止行
		private int startRow;
		// 现在处理的行
		private int nowRowIndex;

		/**
		 * 构造方法
		 * 
		 * @param inRowIndex
		 * @param inColumsLength
		 */
		public DefaultSheetHandler(int inStartRow, int inColumsLength) {
			columsLength = inColumsLength;
			startRow = inStartRow;
		}

		/**
		 * Excel数据
		 * 
		 * @return
		 */
		public List<String[]> getDatas() {
			return datas;
		}

		@Override
		public void startRow(int rowNum) {
			nowRowIndex = rowNum;
			rowMap.clear();
		}

		@Override
		public void endRow() {
			if (nowRowIndex >= startRow) {
				String[] rowData = new String[columsLength];
				for (Map.Entry<Integer, String> entry : rowMap.entrySet()) {
					if (entry.getKey() < columsLength) {
						rowData[entry.getKey()] = entry.getValue() == null ? "" : entry.getValue();
					}
				}
				datas.add(rowData);
				rowMap.clear();
			}
			System.out.println(nowRowIndex + "行读取完成");
		}

		@Override
		public void cell(String cellReference, String formattedValue) {
			if (nowRowIndex >= startRow) {
				int index = getCellIndex(cellReference);// 转换A1,B1,C1等表格位置为真实索引位置
				if (index < columsLength) {
					if (formattedValue == null) {
						rowMap.put(index, "");
					} else {
						rowMap.put(index, formattedValue);
					}

				}
			}
		}

		@Override
		public void headerFooter(String text, boolean isHeader, String tagName) {
		}

		/**
		 * 转换表格引用为列编号
		 * 
		 * @param cellReference 列引用
		 * @return 表格列位置，从0开始算
		 */
		public int getCellIndex(String cellReference) {
			String ref = cellReference.replaceAll("\\d+", "");
			int num = 0;
			int result = 0;
			for (int i = 0; i < ref.length(); i++) {
				char ch = cellReference.charAt(ref.length() - i - 1);
				num = (int) (ch - 'A' + 1);
				num *= Math.pow(26, i);
				result += num;
			}
			return result - 1;
		}
	}
}
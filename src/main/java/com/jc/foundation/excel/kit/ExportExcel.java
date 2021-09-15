package com.jc.foundation.excel.kit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportExcel {

	/**
	 * 上报审批统计分析
	 * 
	 * @param parm
	 * @return
	 */
	public static HSSFWorkbook getModelForSbsptjfx(Map<String, Object> parm) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();

		HSSFCellStyle titleStyle = wb.createCellStyle();

		// 指定单元格居中对齐，边框为细
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(titleFont);

		CellRangeAddress region = new CellRangeAddress(0, 0, 0, 1);
		sheet.addMergedRegion(region);
		CellRangeAddress region_1 = new CellRangeAddress(0, 0, 2, 3);
		sheet.addMergedRegion(region_1);

		HSSFRow hr_0 = sheet.createRow(0);
		hr_0.setHeight((short) 500);

		HSSFCell hc_0 = hr_0.createCell(0);
		hc_0.setCellValue("申报");
		hc_0.setCellStyle(titleStyle);
		sheet.setColumnWidth(0, 60 * 160);
		sheet.setColumnWidth(1, 60 * 160);

		HSSFCell hc_1 = hr_0.createCell(2);
		hc_1.setCellValue("审批");
		hc_1.setCellStyle(titleStyle);
		sheet.setColumnWidth(2, 60 * 160);
		sheet.setColumnWidth(3, 60 * 160);

		HSSFRow hr_1 = sheet.createRow(1);
		hr_1.setHeight((short) 500);
		HSSFCell hr_1_hc_0 = hr_1.createCell(0);
		hr_1_hc_0.setCellValue("申报来源");
		hr_1_hc_0.setCellStyle(titleStyle);
		HSSFCell hr_1_hc_1 = hr_1.createCell(1);
		hr_1_hc_1.setCellValue("数量");
		hr_1_hc_1.setCellStyle(titleStyle);
		HSSFCell hr_1_hc_2 = hr_1.createCell(2);
		hr_1_hc_2.setCellValue("审批方式");
		hr_1_hc_2.setCellStyle(titleStyle);
		HSSFCell hr_1_hc_3 = hr_1.createCell(3);
		hr_1_hc_3.setCellValue("数量");
		hr_1_hc_3.setCellStyle(titleStyle);

		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		Long hj = 0L;
		HSSFRow hr_2 = sheet.createRow(2);
		hr_2.setHeight((short) 500);
		HSSFCell hr_2_hc_0 = hr_2.createCell(0);
		hr_2_hc_0.setCellValue("省市政务大厅");
		hr_2_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_2_hc_1 = hr_2.createCell(1);
		Object sszwdtObj = parm.get("sszwdt");
		if (sszwdtObj != null) {
			hj += (long) sszwdtObj;
			hr_2_hc_1.setCellValue(sszwdtObj + "");
		} else {
			hr_2_hc_1.setCellValue("0");
		}
		hr_2_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_2_hc_2 = hr_2.createCell(2);
		hr_2_hc_2.setCellValue("网上许可审批系统");
		hr_2_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_2_hc_3 = hr_2.createCell(3);
		Object pcObj = parm.get("pc");
		if (pcObj != null) {
			// hj += (long)pcObj;
			hr_2_hc_3.setCellValue(pcObj + "");
		} else {
			hr_2_hc_3.setCellValue("0");
		}
		hr_2_hc_3.setCellStyle(cellStyle);
		HSSFRow hr_3 = sheet.createRow(3);
		hr_3.setHeight((short) 500);
		HSSFCell hr_3_hc_0 = hr_3.createCell(0);
		hr_3_hc_0.setCellValue("网络上报");
		hr_3_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_3_hc_1 = hr_3.createCell(1);
		Object wlsbObj = parm.get("wlsb");
		if (wlsbObj != null) {
			hj += (long) wlsbObj;
			hr_3_hc_1.setCellValue(wlsbObj + "");
		} else {
			hr_3_hc_1.setCellValue("0");
		}
		hr_3_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_3_hc_2 = hr_3.createCell(2);
		hr_3_hc_2.setCellValue("移动版许可审批系统");
		hr_3_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_3_hc_3 = hr_3.createCell(3);
		Object sjObj = parm.get("sj");
		if (sjObj != null) {
			// hj += (long)sjObj;
			hr_3_hc_3.setCellValue(sjObj + "");
		} else {
			hr_3_hc_3.setCellValue("0");
		}
		hr_3_hc_3.setCellStyle(cellStyle);
		HSSFRow hr_4 = sheet.createRow(4);
		hr_4.setHeight((short) 500);
		HSSFCell hr_4_hc_0 = hr_4.createCell(0);
		hr_4_hc_0.setCellValue("网络上报(移动端)");
		hr_4_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_4_hc_1 = hr_4.createCell(1);
		Object wlsbyddObj = parm.get("wlsbydd");
		if (wlsbyddObj != null) {
			hj += (long) wlsbyddObj;
			hr_4_hc_1.setCellValue(wlsbyddObj + "");
		} else {
			hr_4_hc_1.setCellValue("0");
		}
		hr_4_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_4_hc_2 = hr_4.createCell(2);
		hr_4_hc_2.setCellValue("");
		hr_4_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_4_hc_3 = hr_4.createCell(3);
		hr_4_hc_3.setCellValue("");
		hr_4_hc_3.setCellStyle(cellStyle);

		HSSFRow hr_5 = sheet.createRow(5);
		hr_5.setHeight((short) 500);
		HSSFCell hr_5_hc_0 = hr_5.createCell(0);
		hr_5_hc_0.setCellValue("市县上报");
		hr_5_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_5_hc_1 = hr_5.createCell(1);
		Object sxsbObj = parm.get("sxsb");
		if (sxsbObj != null) {
			hj += (long) sxsbObj;
			hr_5_hc_1.setCellValue(sxsbObj + "");
		} else {
			hr_5_hc_1.setCellValue("0");
		}
		hr_5_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_5_hc_2 = hr_5.createCell(2);
		hr_5_hc_2.setCellValue("");
		hr_5_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_5_hc_3 = hr_5.createCell(3);
		hr_5_hc_3.setCellValue("");
		hr_5_hc_3.setCellStyle(cellStyle);

		HSSFRow hr_6 = sheet.createRow(6);
		hr_6.setHeight((short) 500);
		HSSFCell hr_6_hc_0 = hr_6.createCell(0);
		hr_6_hc_0.setCellValue("超限站上报");
		hr_6_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_6_hc_1 = hr_6.createCell(1);
		Object cxzsbObj = parm.get("cxzsb");
		if (cxzsbObj != null) {
			hj += (long) cxzsbObj;
			hr_6_hc_1.setCellValue(cxzsbObj + "");
		} else {
			hr_6_hc_1.setCellValue("0");
		}

		hr_6_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_6_hc_2 = hr_6.createCell(2);
		hr_6_hc_2.setCellValue("");
		hr_6_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_6_hc_3 = hr_6.createCell(3);
		hr_6_hc_3.setCellValue("");
		hr_6_hc_3.setCellStyle(cellStyle);

		HSSFRow hr_7 = sheet.createRow(7);
		hr_7.setHeight((short) 500);
		HSSFCell hr_7_hc_0 = hr_7.createCell(0);
		hr_7_hc_0.setCellValue("合计");
		hr_7_hc_0.setCellStyle(cellStyle);
		HSSFCell hr_7_hc_1 = hr_7.createCell(1);
		hr_7_hc_1.setCellValue(hj + "");
		hr_7_hc_1.setCellStyle(cellStyle);
		HSSFCell hr_7_hc_2 = hr_7.createCell(2);
		hr_7_hc_2.setCellValue("");
		hr_7_hc_2.setCellStyle(cellStyle);
		HSSFCell hr_7_hc_3 = hr_7.createCell(3);
		hr_7_hc_3.setCellValue("");
		hr_7_hc_3.setCellStyle(cellStyle);

		return wb;
	}

	public static HSSFWorkbook getModel(List<Map<String, Object>> parm, String[] titles) {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;
		// String[] titles = { "项目名称",
		// "接件数","不予受理数","办件数","办结数","退办件数","承诺时限","提前办结效率","提前办结率" };
		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		// 接件总计
		Long zj = 0L;
		// 办结总计
		Long bjzj = 0L;
		// 审批中
		Long spzzj = 0L;
		//不予受理书
		long countNoEnter = 0L;
		// 退回总计
		Long thzj = 0L;
		int r = 0;
		for (; r < parm.size(); r++) {
			Map<String, Object> mapx = parm.get(r);
			HSSFRow row = sheet.createRow(r + 1);
			HSSFCell cell = null;
			int c = 0;
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object num = mapx.get("num");
			if (num == null) {
				num = mapx.get("num1");
			}
			cell.setCellValue(num + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object catother_name = mapx.get("catother_name");
			if (catother_name == null || "合计".equalsIgnoreCase(catother_name.toString())) {
				catother_name = "合计";
				Object countAll = mapx.get("countAll");
				if (countAll == null) {
					countAll = mapx.get("countAll1");
				}

				zj += (Long) countAll;
				Object countEnd = mapx.get("countEnd");
				if (countEnd == null) {
					countEnd = mapx.get("countEnd1");
				}

				bjzj += (Long) countEnd;
				Object spz = mapx.get("spz");
				if (spz == null) {
					spz = mapx.get("spz1");
				}
				spzzj += (Long) spz;
				Object countBack = mapx.get("countBack");
				if (countBack == null) {
					countBack = mapx.get("countBack1");
				}
				thzj += (Long) countBack;
			}
			Object countNoEnterObj = mapx.get("countNoEnter");
			if (!"合计".equals(catother_name) && countNoEnterObj != null) {
				countNoEnter += Integer.valueOf(countNoEnterObj.toString());
			}
			cell.setCellValue(catother_name + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countAll = mapx.get("countAll");
			if (countAll == null) {
				countAll = mapx.get("countAll1");
			}
			cell.setCellValue(countAll + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			if(countNoEnterObj != null){
				cell.setCellValue(countNoEnterObj.toString()); // 设置 countNoEnter 不予受理数
			} else {
				cell.setCellValue("0"); // 设置 countNoEnter 不予受理数
			}


			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countSub = mapx.get("countSub");
			if (countSub == null) {
				countSub = mapx.get("countSub1");
			}
			cell.setCellValue(countSub + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countEnd = mapx.get("countEnd");
			if (countEnd == null) {
				countEnd = mapx.get("countEnd1");
			}
			cell.setCellValue(countEnd + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object spz = mapx.get("spz");
			if (spz == null) {
				spz = mapx.get("spz1");
			}
			cell.setCellValue(spz + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countBack = mapx.get("countBack");
			if (countBack == null) {
				countBack = mapx.get("countBack1");
			}
			cell.setCellValue(countBack + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("timeLimit") + "");
			if (c < titles.length) {
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("100%");
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("100%");
			}
		}
		HSSFRow row = sheet.createRow(r + 1);

		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");

		cell = row.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("总计");

		cell = row.createCell(2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(zj + "");

		cell = row.createCell(3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(countNoEnter+"");

		cell = row.createCell(4);
		cell.setCellStyle(cellStyle);
		long bjs = zj  - countNoEnter;
		if(bjs<0){
			bjs = 0;
		}
		cell.setCellValue(bjs);

		cell = row.createCell(5);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bjzj + "");

		cell = row.createCell(6);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(spzzj + "");

		cell = row.createCell(7);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(thzj + "");

		cell = row.createCell(8);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");

		if (titles.length > 8) {
			cell = row.createCell(9);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("");

			cell = row.createCell(10);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("");
		}

		System.out.println("Done");
		return wb;
	}

	public static HSSFWorkbook getBusinessVolModelNoAll(List<Map<String, Object>> parm, String[] titles) {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;
		// String[] titles = { "项目名称",
		// "接件数","不予受理数","办件数","办结数","退办件数","承诺时限","提前办结效率","提前办结率" };
		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		// 接件总计
		Long zj = 0L;
		// 办结总计
		Long bjzj = 0L;
		// 审批中
		Long spzzj = 0L;
		// 退回总计
		Long thzj = 0L;
		int r = 0;
		for (; r < parm.size(); r++) {
			Map<String, Object> mapx = parm.get(r);
			HSSFRow row = sheet.createRow(r + 1);
			HSSFCell cell = null;
			int c = 0;
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object num = mapx.get("num");
			if (num == null) {
				num = mapx.get("num1");
			}
			cell.setCellValue(num + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object catother_name = mapx.get("catother_name");
			if (catother_name == null) {
				catother_name = "合计";
				Object countAll = mapx.get("countAll");
				if (countAll == null) {
					countAll = mapx.get("countAll1");
				}
				zj += (Long) countAll;
				Object countEnd = mapx.get("countEnd");
				if (countEnd == null) {
					countEnd = mapx.get("countEnd1");
				}
				bjzj += (Long) countEnd;
				Object spz = mapx.get("spz");
				if (spz == null) {
					spz = mapx.get("spz1");
				}
				spzzj += (Long) spz;
				Object countBack = mapx.get("countBack");
				if (countBack == null) {
					countBack = mapx.get("countBack1");
				}
				thzj += (Long) countBack;
			}
			cell.setCellValue(catother_name + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countAll = mapx.get("countAll");
			if (countAll == null) {
				countAll = mapx.get("countAll1");
			}
			cell.setCellValue(countAll + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(0); // 设置 countNoEnter 不予受理数

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countSub = mapx.get("countSub");
			if (countSub == null) {
				countSub = mapx.get("countSub1");
			}
			cell.setCellValue(countSub + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countEnd = mapx.get("countEnd");
			if (countEnd == null) {
				countEnd = mapx.get("countEnd1");
			}
			cell.setCellValue(countEnd + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object spz = mapx.get("spz");
			if (spz == null) {
				spz = mapx.get("spz1");
			}
			cell.setCellValue(spz + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countBack = mapx.get("countBack");
			if (countBack == null) {
				countBack = mapx.get("countBack1");
			}
			cell.setCellValue(countBack + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("timeLimit") + "");
			if (c < titles.length) {
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("100%");
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue("100%");
			}
		}


		System.out.println("Done");
		return wb;
	}

	public static HSSFWorkbook getBusinessVolModel(List<Map<String, Object>> parm, String[] titles) {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;
		// String[] titles = { "项目名称",
		// "接件数","不予受理数","办件数","办结数","退办件数","承诺时限","提前办结效率","提前办结率" };
		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);

		for (int r = 0; r < parm.size(); r++) {
			Map<String, Object> mapx = parm.get(r);
			HSSFRow row = sheet.createRow(r + 1);
			HSSFCell cell = null;
			cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("catotherName") + "");
			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("countAll") + "");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("windowApply") + "");
			cell = row.createCell(3);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("networkApply") + "");
			cell = row.createCell(4);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("countNoEnter") + "");
			cell = row.createCell(5);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("countEnd") + "");
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("spz") + "");
			cell = row.createCell(7);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("countBack") + "");
		}

		System.out.println("Done");
		return wb;
	}
	/**
	 * 个人统计使用
	 * 
	 * @param parm
	 * @param titles
	 * @return
	 */
	public static HSSFWorkbook getModel2(List<Map<String, Object>> parm, String[] titles) {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;
		// String[] titles = { "项目名称",
		// "接件数","不予受理数","办件数","办结数","退办件数","承诺时限","提前办结效率","提前办结率" };
		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		// 接件总计
		Long zj = 0L;
		// 办结总计
		Long bjzj = 0L;
		// 审批中
		Long spzzj = 0L;
		// 退回总计
		Long thzj = 0L;

		int r = 0;
		for (; r < parm.size(); r++) {
			Map<String, Object> mapx = parm.get(r);
			HSSFRow row = sheet.createRow(r + 1);
			HSSFCell cell = null;
			int c = 0;
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object num = mapx.get("num");
			if (num == null) {
				num = mapx.get("num1");
			}
			cell.setCellValue(num + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object catother_name = mapx.get("catother_name");
			if (catother_name == null) {
				catother_name = "合计";
				Object countAll = mapx.get("countAll");
				if (countAll == null) {
					countAll = mapx.get("countAll1");
				}
				zj += (Long) countAll;
				Object countEnd = mapx.get("countEnd");
				if (countEnd == null) {
					countEnd = mapx.get("countEnd1");
				}
				bjzj += (Long) countEnd;
				Object spz = mapx.get("spz");
				if (spz == null) {
					spz = mapx.get("spz1");
				}
				spzzj += (Long) spz;
				Object countBack = mapx.get("countBack");
				if (countBack == null) {
					countBack = mapx.get("countBack1");
				}
				thzj += (Long) countBack;
			}
			cell.setCellValue(catother_name + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countAll = mapx.get("countAll");
			if (countAll == null) {
				countAll = mapx.get("countAll1");
			}
			cell.setCellValue(countAll + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(0); // 设置 countNoEnter 不予受理数

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);

			cell.setCellValue(countAll + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countEnd = mapx.get("countEnd");
			if (countEnd == null) {
				countEnd = mapx.get("countEnd1");
			}
			cell.setCellValue(countEnd + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object spz = mapx.get("spz");
			if (spz == null) {
				spz = mapx.get("spz1");
			}
			cell.setCellValue(spz + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			Object countBack = mapx.get("countBack");
			if (countBack == null) {
				countBack = mapx.get("countBack1");
			}
			cell.setCellValue(countBack + "");

			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx.get("timeLimit") + "");
			if (c < titles.length) {
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(mapx.get("tqbjxl") + "");
				cell = row.createCell(c++);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(mapx.get("tqbjl") + "");
			}
		}

		HSSFRow row = sheet.createRow(r + 1);

		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");

		cell = row.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("总计");

		cell = row.createCell(2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(zj + "");

		cell = row.createCell(3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("0");

		cell = row.createCell(4);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(zj + "");

		cell = row.createCell(5);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(bjzj + "");

		cell = row.createCell(6);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(spzzj + "");

		cell = row.createCell(7);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(thzj + "");

		cell = row.createCell(8);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("");

		System.out.println("Done");
		return wb;
	}

	/**
	 * 日均业务受理量分析
	 * @param parm
	 * @param titles
	 * @return
	 */
	public static HSSFWorkbook getGrwothDayDataModel(List<Integer> parm, String[] titles) {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;

		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);

		HSSFRow row = sheet.createRow(1);//第二行
		HSSFCell cell = null;
		cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("办件数");
		//循环第二行数据列
		for (int r = 0; r < parm.size(); r++) {
			Integer mapx = parm.get(r);
			cell = row.createCell(r+1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(mapx + "");
		}
		return wb;
	}

	public static void main(String[] args) throws IOException {
		// 创建
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		// 创建单元格样式
		HSSFCellStyle titleCellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置填充色
		titleCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 指定当单元格内容显示不下时自动换行
		titleCellStyle.setWrapText(true);
		// 设置单元格字体
		HSSFFont titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 12);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleCellStyle.setFont(titleFont);
		HSSFRow headerRow = sheet.createRow(0);
		HSSFCell headerCell = null;
		String[] titles = { "项目名称", "接件数", "不予受理数", "办件数", "办结数", "退办件数", "承诺时限", "提前办结效率", "提前办结率" };
		for (int c = 0; c < titles.length; c++) {
			headerCell = headerRow.createCell(c);
			headerCell.setCellStyle(titleCellStyle);
			headerCell.setCellValue(titles[c]);
			sheet.setColumnWidth(c, (30 * 160));
		}
		// ------------------------------------------------------------------
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 指定单元格居中对齐，边框为细
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		titleFont.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);

		ArrayList<Map<String, Object>> al = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x", "x");
		map.put("x1", "x1");
		map.put("x2", "x2");
		map.put("x3", "x3");
		map.put("x4", "x4");
		map.put("x5", "x5");
		map.put("x6", "x6");
		map.put("x7", "x7");
		map.put("x8", "x8");
		al.add(map);
		for (int r = 0; r < al.size(); r++) {
			Map<String, Object> mapx = al.get(r);
			HSSFRow row = sheet.createRow(r + 1);
			HSSFCell cell = null;
			int c = 0;
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x1") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x2") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x3") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x4") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x5") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x6") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x7") + "");
			cell = row.createCell(c++);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get("x8") + "");
		}

		FileOutputStream fileOut = new FileOutputStream("E:/test/test.xls");
		wb.write(fileOut);
		fileOut.close();
		System.out.println("Done");
	}

}

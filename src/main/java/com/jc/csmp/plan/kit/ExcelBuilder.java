package com.jc.csmp.plan.kit;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * Excel文件
 *
 * @author liubq
 * @since 2017年12月22日
 */
public class ExcelBuilder {
    // Excel
    private XSSFWorkbook wb;
    // sheet
    private XSSFSheet sheet;
    //画图的顶级管理器
    private XSSFDrawing patriarch;

    // 标题样式
    private XSSFCellStyle titleStyle;
    // 无边框
    private XSSFCellStyle cellLeftStyle0;
    private XSSFCellStyle cellCenterStyle0;
    private XSSFCellStyle cellRightStyle0;
    // 单元格样式
    private XSSFCellStyle cellStyleCenter;
    private XSSFCellStyle cellStyleLeft;
    private XSSFCellStyle cellStyleRight;


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
     * @param title    sheet名称
     * @param colWidth 列宽
     * @throws Exception
     */
    public void openFile(String title, int colWidth) throws Exception {
        // 创建XSSFWorkbook对象
        wb = new XSSFWorkbook();
        // 创建XSSFSheet对象
        sheet = wb.createSheet(title);
        sheet.setDefaultColumnWidth(colWidth);
    }

    /**
     * 标题表格样式 字体变大，加粗 没有边框，不换行
     *
     * @return
     */
    private XSSFCellStyle titleStyle() {
        if (titleStyle == null) {
            titleStyle = wb.createCellStyle();
            titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            // 生成一个字体
            XSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            // 把字体应用到当前的样式
            titleStyle.setFont(font);
        }
        return titleStyle;
    }

    private XSSFCellStyle cellStyleCenter() {
        if (cellStyleCenter == null) {
            cellStyleCenter = wb.createCellStyle();
            cellStyleCenter.setWrapText(true);
            cellStyleCenter.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            cellStyleCenter.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            cellStyleCenter.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            cellStyleCenter.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            cellStyleCenter.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellStyleCenter.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        }
        return cellStyleCenter;
    }

    private XSSFCellStyle cellStyleLeft() {
        if (cellStyleLeft == null) {
            cellStyleLeft = wb.createCellStyle();
            cellStyleLeft.setWrapText(true);
            cellStyleLeft.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            cellStyleLeft.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            cellStyleLeft.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            cellStyleLeft.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            cellStyleLeft.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellStyleLeft.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 居中
        }
        return cellStyleLeft;
    }

    private XSSFCellStyle cellStyleLeft2() {
        if (cellStyleLeft == null) {
            cellStyleLeft = wb.createCellStyle();
            cellStyleLeft.setWrapText(true);
            cellStyleLeft.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            cellStyleLeft.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            cellStyleLeft.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            cellStyleLeft.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            cellStyleLeft.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP); // 居中
            cellStyleLeft.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 居中
        }
        return cellStyleLeft;
    }

    private XSSFCellStyle cellStyleRight() {
        if (cellStyleRight == null) {
            cellStyleRight = wb.createCellStyle();
            cellStyleRight.setWrapText(true);
            cellStyleRight.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            cellStyleRight.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            cellStyleRight.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            cellStyleRight.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            cellStyleRight.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellStyleRight.setAlignment(XSSFCellStyle.ALIGN_RIGHT); // 居中
        }
        return cellStyleRight;
    }

    private XSSFCellStyle cellStyleLeft0() {
        if (cellLeftStyle0 == null) {
            cellLeftStyle0 = wb.createCellStyle();
            cellLeftStyle0.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellLeftStyle0.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 居中
            // 生成一个字体
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            cellLeftStyle0.setFont(font);
            // 单元格格式
            XSSFDataFormat df = wb.createDataFormat();
            cellLeftStyle0.setDataFormat(df.getFormat("TEXT"));
        }
        return cellLeftStyle0;
    }

    private XSSFCellStyle cellStyleRight0() {
        if (cellRightStyle0 == null) {
            cellRightStyle0 = wb.createCellStyle();
            cellRightStyle0.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellRightStyle0.setAlignment(XSSFCellStyle.ALIGN_RIGHT); // 居中
            // 生成一个字体
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            cellRightStyle0.setFont(font);
            // 单元格格式
            XSSFDataFormat df = wb.createDataFormat();
            cellRightStyle0.setDataFormat(df.getFormat("TEXT"));
        }
        return cellRightStyle0;
    }

    private XSSFCellStyle cellStyleCenter0() {
        if (cellCenterStyle0 == null) {
            cellCenterStyle0 = wb.createCellStyle();
            cellCenterStyle0.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellCenterStyle0.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            // 生成一个字体
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            cellCenterStyle0.setFont(font);
            // 单元格格式
            XSSFDataFormat df = wb.createDataFormat();
            cellCenterStyle0.setDataFormat(df.getFormat("TEXT"));
        }
        return cellCenterStyle0;
    }

    private XSSFCellStyle cellStyleCenter00() {
        if (cellCenterStyle0 == null) {
            cellCenterStyle0 = wb.createCellStyle();
            cellCenterStyle0.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER); // 居中
            cellCenterStyle0.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            // 生成一个字体
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            cellCenterStyle0.setWrapText(true);
            cellCenterStyle0.setFont(font);
            // 单元格格式
            XSSFDataFormat df = wb.createDataFormat();
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
    public XSSFRow getRow(int rowNum, int height) throws Exception {
        XSSFRow row = sheet.createRow(rowNum);
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
    public XSSFCell getTitle(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCellLeft0(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCellRight0(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCellCenter0(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCellCenter00(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCell(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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
    public XSSFCell getCellLeft(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
        cell.setCellStyle(cellStyleLeft());
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
    public XSSFCell getCellLeft2(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
        cell.setCellStyle(cellStyleLeft2());
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
    public XSSFCell getCellRight(XSSFRow row, int colNum) throws Exception {
        XSSFCell cell = row.createCell(colNum);
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

    /**
     * 导出图片
     *
     * @param rowIndex
     * @param columnIndex
     * @throws Exception
     */
    public void setImage(byte[] img,int format, int rowIndex, int columnIndex) throws Exception {
        try {
            if (patriarch == null) {
                patriarch = sheet.createDrawingPatriarch();
            }
            //anchor主要用于设置图片的属性
            //设置图片的属性
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) columnIndex, rowIndex, (short) columnIndex + 1, rowIndex + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.byId(0));
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(img, format));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

//    /**
//     * 导出图片
//     * @param rowIndex
//     * @param columnIndex
//     * @throws Exception
//     */
//    public void setImage(byte[] img,int rowIndex,int columnIndex) throws Exception {
//
//        BufferedImage bufferImg = null;
//        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
//        try {
//            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//            bufferImg = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\111\\sss.png"));
//            ImageIO.write(bufferImg, "png", byteArrayOut);
//
//            if(patriarch == null){
//                patriarch = sheet.createDrawingPatriarch();
//            }
//            //anchor主要用于设置图片的属性
//            //设置图片的属性
//            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 255, 255,(short) columnIndex, rowIndex, (short) columnIndex+1, rowIndex+1);
//            anchor.setAnchorType(ClientAnchor.AnchorType.byId(0));
//            //插入图片
//            patriarch.createPicture(anchor,wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }

}

package com.jc.system.common.util;

import com.jc.foundation.domain.BaseBean;
import com.jc.system.security.SystemSecurityUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class ExcuteExcelUtil {

    private ExcuteExcelUtil() {
        throw new IllegalStateException("ExcuteExcelUtil class");
    }

    private static final Logger logger = Logger.getLogger(ExcuteExcelUtil.class);
    //输入流
    private static FileInputStream fileIn = null;
    //输出流
    private static OutputStream fileOut = null;
    //workbook对象
    private static Workbook wb = null;
    //模板位置
    private static String templatePath = "C://template.xls";
    //日期输入格式
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    private static boolean headerStyleVisible = true;
    private static boolean bodyStyleVisible = true;

    // header默认式样
    private static short headerCellAlignment = HSSFCellStyle.ALIGN_CENTER;
    private static short headerCellVerticalAlignment = HSSFCellStyle.ALIGN_CENTER;
    private static short headerCellFillPattern = HSSFCellStyle.SOLID_FOREGROUND;
    private static short headerCellFillForegroundColor = HSSFColor.GREY_25_PERCENT.index;
    private static short headerCellBorder = HSSFCellStyle.BORDER_THIN;
    private static short headerFontColor = HSSFColor.BLACK.index;
    private static short headerFontHeight = HSSFFont.BOLDWEIGHT_BOLD;
    private static short headerFontSize = 12;

    // body默认式样
    private static short bodyCellAlignment = HSSFCellStyle.ALIGN_CENTER;
    private static short bodyCellVerticalAlignment = HSSFCellStyle.ALIGN_CENTER;
    private static short bodyCellFillPattern = HSSFCellStyle.SOLID_FOREGROUND;
    private static short bodyCellFillForegroundColor = HSSFColor.WHITE.index;
    private static short bodyCellBorder = HSSFCellStyle.BORDER_THIN;
    private static short bodyFontColor = HSSFColor.BLACK.index;
    private static short bodyFontHeight = HSSFFont.BOLDWEIGHT_BOLD;
    private static short bodyFontSize = 12;

    /**
     * 创建HSSFWorkbook对象
     */
    public static void createExcel2003() {

        wb = new HSSFWorkbook();
    }

    /**
     * 创建XSSFWorkbook对象
     */
    public static void createExcel2007() {

        wb = new XSSFWorkbook();

    }

    /**
     * 创建HSSFWorkbook对象
     * @throws IOException
     */
    public static void createExcel2003(String path) throws IOException {
        fileIn = new FileInputStream(path);
        wb = new HSSFWorkbook(new POIFSFileSystem(fileIn));
        fileIn.close();
    }

    /**
     * 创建XSSFWorkbook对象
     * @throws IOException
     */
    public static void createExcel2007(String path) throws IOException {
        fileIn = new FileInputStream(path);
        wb = new XSSFWorkbook(fileIn);
        fileIn.close();
    }

    /**
     * 根据模板创建2003Excel对象
     * @throws IOException
     */
    public static void createExcel2003ByTemplate(String path) throws IOException {
        createExcel2003(path);
    }

    /**
     * 根据模板创建2007Excel对象
     * @throws IOException
     */
    public static void createExcel2007ByTemplate(File file) throws IOException {

        fileIn = new FileInputStream(file);
        wb = new XSSFWorkbook(fileIn);
    }

    /**
     * 创建标题行外观
     */
    public static CellStyle getHeaderStyle() {

        CellStyle cs = wb.createCellStyle();

        if(headerStyleVisible){
            cs.setAlignment(headerCellAlignment);
            cs.setVerticalAlignment(headerCellVerticalAlignment);
            cs.setFillForegroundColor(headerCellFillForegroundColor);
            cs.setFillPattern(headerCellFillPattern);

            cs.setBorderBottom(headerCellBorder);
            cs.setBorderLeft(headerCellBorder);
            cs.setBorderRight(headerCellBorder);
            cs.setBorderTop(headerCellBorder);

            Font ff = wb.createFont();
            ff.setColor(headerFontColor);
            ff.setFontHeight(headerFontHeight);
            ff.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
            ff.setFontHeightInPoints(headerFontSize);

            cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 设置单元格字体显示居中(上下方向)
            cs.setFont(ff);
        }

        return cs;
    }

    /**
     * 创建内容行外观
     */
    public static CellStyle getBodyStyle() {
        CellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        if(bodyStyleVisible){
            cs.setAlignment(bodyCellAlignment);
            cs.setVerticalAlignment(bodyCellVerticalAlignment);
            cs.setFillForegroundColor(bodyCellFillForegroundColor);
            cs.setFillPattern(bodyCellFillPattern);

            cs.setBorderBottom(bodyCellBorder);
            cs.setBorderLeft(bodyCellBorder);
            cs.setBorderRight(bodyCellBorder);
            cs.setBorderTop(bodyCellBorder);

            Font ff = wb.createFont();
            ff.setColor(bodyFontColor);
            ff.setFontHeight(bodyFontHeight);
            ff.setFontHeightInPoints(bodyFontSize);

            cs.setFont(ff);

            cs.setWrapText(true);
        }
        return cs;
    }

    /**
     * 设置模板路径
     * @param path 模板路径
     */
    public static void setTemplatePath(String path){
        templatePath = path;
    }

    /**
     * 设置日期格式
     * @param format yyyy-MM-dd HH:mm:ss或者yyyy-MM-dd
     */
    public static void setDateFormat(String format){
        dateFormat = format;
    }

    /**
     * 设置response参数
     * @param response response对象
     */
    public static void setResponse(HttpServletResponse response){
        Object excelKey = SystemSecurityUtils.getSession().getAttribute("excelKey");
        //System.out.println(excelKey+"***************1");
        if (excelKey == null || "".equals(excelKey.toString())){
            excelKey = "excel.xls";
        }
        //System.out.println(excelKey+"***************2");
        // 设置response参数，可以打开下载页面
        String filename = "";
        try {
            filename = URLEncoder.encode(excelKey.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("content-disposition", "attachment;filename = " + filename);

        //System.out.println(excelKey+"***************3");
        if (excelKey != null && !"".equals(excelKey.toString())){
            SystemSecurityUtils.getSession().setAttribute("excelKey","");
        }
    }

    /**
     * 设置标题
     * @param lstHead 列名list
     * @param sheet ExcelSheet对象
     */
    private static void exportHeader(List<String> lstHead, Sheet sheet, CellStyle headerCellStyle) {

        // 创建行:标题为第一行
        Row rowHead = sheet.createRow(0);
        rowHead.setHeightInPoints((short) 28);
        sheet.createFreezePane( 0, 1, 0, 1 );
        for (int i = 0; i < lstHead.size(); i++) {
            sheet.setColumnWidth(i,5000);
            // 创建列
            Cell cell = rowHead.createCell(i);

            if(headerCellStyle == null){
                headerCellStyle = getHeaderStyle();
            }
            // 设置外观
            cell.setCellStyle(getHeaderStyle());

            cell.setCellValue(lstHead.get(i));
        }
    }

    /**
     * 根据标题导出excel
     * @param lstHead 列名
     * @param lstProp 需要导出的属性list
     * @param lstObj 需要导出的实体beanlist
     * @param response response
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public static void exportExcel(List<String> lstHead, List<String> lstProp,
                                   List<? extends BaseBean> lstObj, HttpServletResponse response) throws IOException, NoSuchFieldException {
        exportExcel(lstHead, lstProp, lstObj, response, null);
    }

    /**
     * 根据标题导出excel
     * @param lstHead 列名
     * @param lstProp 需要导出的属性list
     * @param lstObj 需要导出的实体beanlist
     * @param response response
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public static void exportExcel(List<String> lstHead, List<String> lstProp,
                                   List<? extends BaseBean> lstObj, HttpServletResponse response, CellStyle headerCellStyle) throws IOException, NoSuchFieldException {

        //目前只支持Excel2003
        createExcel2003();

        try {

            //默认sheet名为sheet1,数据大于6W条时,追加多页处理
            Sheet sheet = wb.createSheet("sheet1");

            // 导出标题
            exportHeader(lstHead, sheet,headerCellStyle);

            // 导出数据
            exportBody(lstObj, lstProp, sheet);

            //设置response
            setResponse(response);

            fileOut = response.getOutputStream();

            // 写入Excel
            wb.write(fileOut);

            fileOut.flush();

            // 关闭输出流
            fileOut.close();
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (fileOut != null) {
                // 关闭输出流
                fileOut.close();
            }
        }
    }

    /**
     * 导出excel模板
     * @param bBean
     * @param response response
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public static void exportExcelTemplet(List<? extends BaseBean> lstObj, BaseBean bBean, HttpServletResponse response) throws IOException, NoSuchFieldException {

        //目前只支持Excel2003
        createExcel2003();

        try {

            //默认sheet名为sheet1,数据大于6W条时,追加多页处理
            Sheet sheet = wb.createSheet("sheet1");

            Field[] field = bBean.getClass().getDeclaredFields();

            List<String> lstHead = new ArrayList<String>();
            for (Field fl:field){
                if ("serialVersionUID".equals(fl.getName())) {
                    continue;
                }
                lstHead.add(fl.getName());
            }
            // 导出标题
            exportHeader(lstHead, sheet, null);

            if (lstObj != null && lstObj.size() > 0){
                List<BaseBean> exlist = new ArrayList<>();
                exlist.add(lstObj.get(0));
                // 导出数据
                exportBody(lstObj, lstHead, sheet);
            }

            //设置response
            setResponse(response);

            fileOut = response.getOutputStream();

            // 写入Excel
            wb.write(fileOut);

            fileOut.flush();

            // 关闭输出流
            fileOut.close();
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (fileOut != null) {
                // 关闭输出流
                fileOut.close();
            }
        }
    }

    /**
     * 根据模板导出excel
     * @param lstObj 需要导出的实体beanlist
     * @param lstProp 需要导出的属性list
     * @param response response
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public static void exportExcel(List<? extends BaseBean> lstObj, List<String> lstProp, HttpServletResponse response) throws IOException, NoSuchFieldException {

        //目前只支持Excel2003
        createExcel2003ByTemplate(templatePath);
        //createExcel2003();
        try {

            //默认sheet名为sheet1,数据大于6W条时,追加多页处理
            Sheet sheet = wb.getSheetAt(0);

            // 导出数据
            exportBody(lstObj, lstProp, sheet);

            //设置response
            setResponse(response);

            fileOut = response.getOutputStream();

            // 写入Excel
            wb.write(fileOut);

            // 关闭输出流
            fileOut.close();
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (fileOut != null) {
                // 关闭输出流
                fileOut.close();
            }
        }

    }

    /**
     * 导出数据
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
//    private static void exportBody(List<? extends BaseBean> lstObj, Sheet sheet)
//            throws NoSuchMethodException, SecurityException,
//            IllegalAccessException, IllegalArgumentException,
//            InvocationTargetException {
//
//        for (int i = 0; i < lstObj.size(); i++) {
//
//            BaseBean bBean = lstObj.get(i);
//
//            // 创建行
//            Row rowBody = sheet.createRow(i + 1);
//
//            // 获取实体类的所有属性，返回Field数组
//            Field[] field = bBean.getClass().getDeclaredFields();
//
//            // 开始写入列的位置
//            int nCellStart = 0;
//
//            // 遍历所有属性
//            for (int j = 0; j < field.length; j++) {
//
//                // 自适应列宽度
//                // sheet.autoSizeColumn((short)j);
//
//                // 获取属性的名字
//                String name = field[j].getName();
//
//                // 获得属性的类型
//                String type = field[j].getGenericType().toString();
//
//                if ("serialVersionUID".equals(name)) {
//                    continue;
//                }
//
//                // 将属性的首字符大写，构造get方法
//                name = name.substring(0, 1).toUpperCase() + name.substring(1);
//
//                // 调用get方法
//                Method getMethod = bBean.getClass().getMethod("get" + name);
//
//                // 创建列
//                Cell cell = rowBody.createCell(nCellStart);
//
//                // 设置外观
//                cell.setCellStyle(getBodyStyle());
//
//                //日期时特殊处理
//                if(type.equals("class java.util.Date")){
//                    SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    cell.setCellValue(getMethod.invoke(bBean) != null ? sFormat.format((Date)getMethod.invoke(bBean)) : "");
//                }
//                else{
//                    cell.setCellValue(getMethod.invoke(bBean) != null ? getMethod.invoke(bBean) + "": "");
//                }
//
//                nCellStart++;
//            }
//
//        }
//    }

    /**
     * 导出数据
     * @param lstObj 需要导出的实体beanlist
     * @param lstProp 需要导出的属性list
     * @param sheet Sheet 对象
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static void exportBody(List<? extends BaseBean> lstObj, List<String> lstProp, Sheet sheet)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchFieldException {
        CellStyle cellStyle = getBodyStyle();
        for (int i = 0; i < lstObj.size(); i++) {

            BaseBean bBean = lstObj.get(i);

            // 创建行
            Row rowBody = sheet.createRow(i + 1);

            // 获取实体类的所有属性，返回Field数组
            //Field[] field = bBean.getClass().getDeclaredFields();

            // 开始写入列的位置
            int nCellStart = 0;


            // 遍历所有属性
            for (int j = 0; j < lstProp.size(); j++) {

                // 自适应列宽度
                // sheet.autoSizeColumn((short)j);

                // 获取属性的名字
                String name = lstProp.get(j);

                Field field = bBean.getClass().getDeclaredField(name);

                // 获得属性的类型
                String type = field.getGenericType().toString();


                // 将属性的首字符大写，构造get方法
                name = name.substring(0, 1).toUpperCase() + name.substring(1);

                // 调用get方法
                Method getMethod = bBean.getClass().getMethod("get" + name);

                // 创建列
                Cell cell = rowBody.createCell(nCellStart);

                // 设置外观
                cell.setCellStyle(cellStyle);

                //日期时特殊处理
                if("class java.util.Date".equals(type)){
                    SimpleDateFormat sFormat = new SimpleDateFormat(dateFormat);
                    cell.setCellValue(getMethod.invoke(bBean) != null ? sFormat.format((Date)getMethod.invoke(bBean)) : "");
                }
                else{
                    String value = "";
                    Object objValue = getMethod.invoke(bBean);
                    if(objValue != null){
                        value = objValue +"";
                        value = value.replaceAll("&nbsp;", " ");
                    }

                    cell.setCellValue(getMethod.invoke(bBean) != null ? value: "");
                }

                nCellStart++;
            }

        }
    }

    public static List<BaseBean> importExcel(Boolean isExcel2007, InputStream inputStream, BaseBean bBean, Map excelToMap)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ParseException, IOException, InstantiationException {
        try {
            Workbook wb;
            if(isExcel2007){
                wb = new XSSFWorkbook(inputStream);
            }else{
                wb = new HSSFWorkbook(inputStream);
            }
             return importExcel(wb, inputStream,bBean,excelToMap);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally{
            if(inputStream != null){
                inputStream.close();
            }
        }
        return  new ArrayList<BaseBean>();
    }

    public static List<BaseBean> importExcel(InputStream inputStream, BaseBean bBean, Map excelToMap)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ParseException, IOException, InstantiationException {
             return importExcel(false,inputStream,bBean,excelToMap);
    }

    /**
     * 导入Excel
     * @param inputStream 文件输入流
     * @param bBean BaseBean
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ParseException
     * @throws IOException
     * @throws InstantiationException
     */
    public static List<BaseBean> importExcel(Workbook wb,InputStream inputStream, BaseBean bBean, Map excelToMap)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ParseException, IOException, InstantiationException {
        List<BaseBean> bbList = new ArrayList<BaseBean>();
        try {

            // 获取实体类的所有属性，返回Field数组
            Field[] field = bBean.getClass().getDeclaredFields();
            Map<String,Field> fields = new HashMap<>();
            for (Field fl:field){
                if ("serialVersionUID".equals(fl.getName())) {
                    continue;
                }
                fields.put(fl.getName(),fl);
            }

            // 读取页数
//            int nSheetNum = wb.getNumberOfSheets();

            //只有一个sheet
            Sheet sheet = wb.getSheetAt(0);

            Row columnRow = sheet.getRow(0);//获取列名称

            // 读取行数(取得的是行标,比行数小1.)
            int nRowCount = sheet.getLastRowNum();
            for (int i = 1; i <= nRowCount; i++) {
                bBean = bBean.getClass().newInstance();
                Row row = sheet.getRow(i);

                // 读取列数(取得的是列标,比列数大1.)
                int nCellCount = row.getLastCellNum();

                // 开始列的位置
                int nCellStart = 0;
                for (int j = 0; j < nCellCount; j++) {
                    Cell cell = row.getCell(nCellStart);
                    Cell columnCell = columnRow.getCell(nCellStart);

                    // 获取属性的名字
                    String name = columnCell.getStringCellValue();

                    // 获得属性的类型
                    String type = fields.get(name).getGenericType().toString();

                    if ("serialVersionUID".equals(name)) {
                        continue;
                    }

                    // 将属性的首字符大写，构造get方法
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);

                    // 调用set方法
                    Method setMethod = null;
                    if("class java.lang.String".equals(type)){
                        setMethod = bBean.getClass().getMethod("set" + name, String.class);
                        setMethod.invoke(bBean, cell.getStringCellValue());

                    }else if("class java.lang.Integer".equals(type)){
                        setMethod = bBean.getClass().getMethod("set" + name, Integer.class);

                        if("".equals(cell.getStringCellValue())){
                            setMethod.invoke(bBean, (Integer)null);
                        }else{
                            setMethod.invoke(bBean, Integer.parseInt(cell.getStringCellValue()));
                        }

                    }else if("class java.lang.Double".equals(type)){
                        setMethod = bBean.getClass().getMethod("set" + name, Integer.class);

                        if("".equals(cell.getStringCellValue())){
                            setMethod.invoke(bBean, (Double)null);
                        }else{
                            setMethod.invoke(bBean, Double.parseDouble(cell.getStringCellValue()));
                        }

                    }else if("class java.util.Date".equals(type)){
                        setMethod = bBean.getClass().getMethod("set" + name, Date.class);

                        //格式化日期
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date date = null;

                        if(!"".equals(cell.getStringCellValue())){
                            date = sdf.parse(cell.getStringCellValue());
                        }
                        setMethod.invoke(bBean, date);

                    }else if("class java.lang.Boolean".equals(type)){
                        setMethod = bBean.getClass().getMethod("set" + name, Boolean.class);
                        setMethod.invoke(bBean, cell.getBooleanCellValue());
                    }
                    nCellStart++;
                }
                bbList.add(bBean);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally{
            if(inputStream != null){
                inputStream.close();
            }
        }
        return bbList;
    }
}
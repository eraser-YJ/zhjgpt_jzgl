package com.jc.foundation.excel.kit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;

import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;

public class ExcelOperateUtil {
	
	private static Logger log = Logger.getLogger(ExcelOperateUtil.class);
	private static IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
	
	//输入流
    @SuppressWarnings("unused")
	private static FileInputStream fileIn = null;
    //输出流
    private static OutputStream fileOut = null;
	//导出
	 private static SXSSFWorkbook sxwb = null;
    //日期输入格式
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    
    private static boolean xheaderStyleVisible = false;
    private static boolean xbodyStyleVisible = false;
    //office 2007
    //private static short xheaderCellAlignment = XSSFCellStyle.ALIGN_CENTER;   //水平居中
    private static short xheaderCellAlignment = XSSFCellStyle.ALIGN_CENTER;   //水平居中
    private static short xheaderCellVerticalAlignment = XSSFCellStyle.VERTICAL_CENTER; //垂直居中
    private static short xheaderCellFillPattern = XSSFCellStyle.SOLID_FOREGROUND;   //填充图案
    private static short xheaderCellFillForegroundColor = IndexedColors.WHITE.index;   //背景颜色
    private static short xheaderCellBorder = XSSFCellStyle.BORDER_THIN;   //设置边框
    private static short xheaderFontColor = IndexedColors.BLUE.index;   //BLACK.index;   //设置股字体颜色
	@SuppressWarnings("unused")
	private static short xheaderFontBold =XSSFFont.BOLDWEIGHT_BOLD;     //设置字体加粗
    @SuppressWarnings("unused")
    private static short xheaderFontHeight =XSSFFont.BOLDWEIGHT_BOLD;     //设置字体高度
    private static short xheaderFontSize = 12;					//设置字体大小
    @SuppressWarnings("unused")
    private static String xheaderFontName = "宋体";					//设置字体
    
 // body默认式样
    private static short xbodyCellAlignment = XSSFCellStyle.ALIGN_CENTER;
    private static short xbodyCellVerticalAlignment = XSSFCellStyle.VERTICAL_CENTER;
    @SuppressWarnings("unused")
    private static short xbodyCellFillPattern = XSSFCellStyle.SOLID_FOREGROUND;
    @SuppressWarnings("unused")
    private static short xbodyCellFillForegroundColor = IndexedColors.WHITE.index;//TURQUOISE.index;
    private static short xbodyCellBorder = XSSFCellStyle.BORDER_THIN;
    private static short xbodyFontColor = IndexedColors.BLACK.index;
    @SuppressWarnings("unused")
	private static short xbodyFontBold = XSSFFont.BOLDWEIGHT_BOLD;
    @SuppressWarnings("unused")
	private static short xbodyFontHeight =XSSFFont.BOLDWEIGHT_BOLD;     //设置字体高度
    private static short xbodyFontSize = 11;
    @SuppressWarnings("unused")
	private static String xbodyFontName = "宋体";					//设置字体
    
    /**
     * 创建XSSFWorkbook对象
     */
    public static void createExcel(int count) {
    	
    	sxwb = new SXSSFWorkbook(count);
    	
    }
    
    /**
     * 创建标题行外观
     */
    public static CellStyle getHeaderStyle() {
    	
    	CellStyle cs = sxwb.createCellStyle();
    	
    	if(xheaderStyleVisible){
    		cs.setAlignment(xheaderCellAlignment);
    		cs.setVerticalAlignment(xheaderCellVerticalAlignment);
//    		cs.setFillForegroundColor(xheaderCellFillForegroundColor);
//    		cs.setFillPattern(xheaderCellFillPattern);
//    		
//    		cs.setBorderBottom(xheaderCellBorder);
//    		cs.setBorderLeft(xheaderCellBorder);
//    		cs.setBorderRight(xheaderCellBorder);
//    		cs.setBorderTop(xheaderCellBorder);
    		
    		Font ff = sxwb.createFont();
    		ff.setFontName("Arial");
//    		ff.setColor(xheaderFontColor);
    		ff.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
//    		ff.setFontHeight(xheaderFontHeight);
    		//         ff.setBoldweight(xheaderFontBold);
    		ff.setFontHeightInPoints((short)10);
    		
    		cs.setFont(ff);
    	}
    	return cs;
    }

    /**
     * 创建内容行外观
     */
    public static CellStyle getBodyStyle() {
    	CellStyle cs = sxwb.createCellStyle();
//    	cs.setWrapText(true);
    	if(xbodyStyleVisible){
//    		cs.setAlignment(xbodyCellAlignment);
    		cs.setVerticalAlignment(xbodyCellVerticalAlignment);
    		cs.setWrapText(true);
//    		cs.setBorderBottom(xbodyCellBorder);
//    		cs.setBorderLeft(xbodyCellBorder);
//    		cs.setBorderRight(xbodyCellBorder);
//    		cs.setBorderTop(xbodyCellBorder);
    		
    		Font ff = sxwb.createFont();
//    		ff.setColor(xbodyFontColor);
    		ff.setFontName("Arial");
    		ff.setFontHeightInPoints((short)10);
    		
    		cs.setFont(ff);
    	}
    	return cs;
    }
    /**
     * 设置标题是否有样式
     * @param bool true或者false
     */
    public static void setXheaderStyleVisible(Boolean bool){
    	xheaderStyleVisible = bool;
    }
    
    /**
     * 设置文本是否有样式
     * @param bool true或者false
     */
    public static void setXbodyStyleVisible(Boolean bool){
    	xbodyStyleVisible = bool;
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
     * @throws UnsupportedEncodingException 
     */
    public static void setResponse(HttpServletResponse response, String name) throws UnsupportedEncodingException{
    	name = name+".xlsx";
    	// 设置response参数，可以打开下载页面
    	response.reset();
//    	response.setHeader("Connection", "close");
//		response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
    	response.setContentType("text/xml");
    	response.setCharacterEncoding("UTF-8");
    	response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(name, "UTF-8"));
    }
	
	@SuppressWarnings("unused")
	private static Connection getJdbcConnection() throws Exception{
		Connection conn = null;
		//反射机制，创建数据库Driver
		String database = GlobalContext.getProperty("jdbc.url");
		String driver = GlobalContext.getProperty("jdbc.driver");
		
		String username = GlobalContext.getProperty("jdbc.username");
		String password = GlobalContext.getProperty("jdbc.password");
		
		Class.forName(driver);
		conn = java.sql.DriverManager.getConnection(database, username, password); //数据库database,username，password
		conn.setAutoCommit(false); //取消自动提交
		System.out.println("数据库连接成功");
		return conn;
	}


	

    /**
	 * 根据指定字符分割字符串
	 * 
	 * @@param str
	 * @@param c
	 * @@return
	 */
	public static String[] splitStr(String str, char c) {
		if (str == null) {
			return null;
		}
		str += c;
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				n++;
			}
		}
		String out[] = new String[n];
		for (int i = 0; i < n; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0, index);
			str = str.substring(index + 1, str.length());
		}
		return out;
	}
    

    
	/**
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 *
	 * @param startRow
	 *            开始行数
	 * @param endRow
	 *            结束行数
	 * @return 读出的Excel中数据的内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException 
	 */
	public static String[][] getExcelData(InputStream inputStream, int startRow, int endRow)
			throws FileNotFoundException, IOException, InvalidFormatException {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		
//		File file = new File(filePath);// 文件输出流
//		if(file.exists()){
			Workbook wb = null;
//			InputStream inputStream = new FileInputStream(file);
			try {
				wb = WorkbookFactory.create(inputStream); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
//			int sheetNum = wb.getNumberOfSheets();
			int sheetNum = 1;
			Sheet st = null;
			Row row = null;
			Cell cell = null;
			String[] values = null;
//			String[] tempValues = null;
			for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
				st = wb.getSheetAt(sheetIndex);
				for (int rowIndex = startRow; rowIndex <= st.getLastRowNum(); rowIndex++) {
					row = st.getRow(rowIndex);
					if (row == null) {
						continue;
					}
					int tempRowSize = row.getLastCellNum();
					if (tempRowSize > rowSize) {
						rowSize = tempRowSize;
					}
					values = new String[rowSize];
					Arrays.fill(values, "");
//					boolean hasValue = false;
					for (short columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
						String value = "";
						cell = row.getCell(columnIndex);
						if (cell != null) {
							// 注意：一定要设成这个，否则可能会出现乱码
	//						((Object) cell).setEncoding(HSSFCell.ENCODING_UTF_16);
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									Date date = cell.getDateCellValue();
									if (date != null) { 
										value = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss") ;
//										value = new SimpleDateFormat("yyyy-MM-dd").format(date);
									} else {
										value = "";
									}
								} else {
									value = new DecimalFormat("#.####").format(cell.getNumericCellValue());
								}
								break;
							case Cell.CELL_TYPE_FORMULA:
								try {
									/*
									 * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.
									 * isCellDateFormatted(cell)判断过程中cell
									 * .getNumericCellValue
									 * ();方法会抛出java.lang.NumberFormatException异常
									 */
									if (DateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										value = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
										break;
									} else {
										value = String.valueOf(cell.getNumericCellValue());
									}
								} catch (IllegalStateException e) {
									value = String.valueOf(cell.getRichStringCellValue());
								}
//								// 导入时如果为公式生成的数据则无值
//								if (!cell.getStringCellValue().equals("")) {
//									value = cell.getStringCellValue();
//								} else {
//									value = cell.getNumericCellValue() + "";
//								}
								break;
							case Cell.CELL_TYPE_BLANK:
								break;
							case Cell.CELL_TYPE_ERROR:
								value = "";
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue() == true ? "Y"
										: "N");
								break;
							default:
								value = "";
							}
						}
//						if (columnIndex == 0 && value.trim().equals("")) {
//							break;
//						}
						values[columnIndex] = rightTrim(value);
//						hasValue = true;
					}
					boolean needAdd = false;
					for(String value:values){
						if(value == null || "".equals(value)){
							continue;
						}
						needAdd = true;
						break;
					}
					if(needAdd){
						result.add(values);
					}
	
//					if (hasValue) {
//						result.add(values);
//					}
				}
			}
			inputStream.close();
//		}
		
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		return returnArray;
	}
	
	/**
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 处理后的字符串
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
	
	public static String list2Str(List<String> list) {
		String str = "";
		StringBuffer strTmp = new StringBuffer();
		for(int i=0;i<list.size();i++){
			strTmp.append(list.get(i)).append(",");
		}
		str = strTmp.substring(0, (strTmp.length()-1));
		return str;
	}
	
	public static String pointStr(List<String> list) {
		String str = "";
		StringBuffer strTmp = new StringBuffer();
		for(int i=0;i<list.size();i++){
			strTmp.append("?").append(",");
		}
		str = strTmp.substring(0, (strTmp.length()-1));
		return str;
	}
	
	public static String dataStr(String[] dataArr, List<Integer> columnNos) {
		String str = "";
		StringBuffer strTmp = new StringBuffer();
		for(int i=0;i<columnNos.size();i++){
			if(dataArr[columnNos.get(i)].trim().equals("")){
				strTmp.append("null").append(",");
			}else{
				strTmp.append("'").append(dataArr[columnNos.get(i)]).append("'").append(",");
			}
			
		}
		str = strTmp.substring(0, (strTmp.length()-1));
		return str;
	}
	
	public static String fillData(BaseBean bean, boolean modify) {
		Date now = new Date();
		if ((bean.getCreateDate() == null) && (!modify)) {
			bean.setCreateDate(now);
		}
		if (bean.getModifyDate() == null) {
			bean.setModifyDate(now);
		}
		if (modify) {
			bean.setModifyDateNew(now);
		}
		User user = SystemSecurityUtils.getUser();
		if (!modify) {
			if (bean.getCreateUser() == null) {
				bean.setCreateUser(user.getId());
			}
			if (bean.getCreateUserDept() == null) {
				bean.setCreateUserDept(user.getDeptId());
			}
			if (bean.getCreateUserOrg() == null) {
				bean.setCreateUserOrg(user.getOrgId());
			}
		}
		if (bean.getModifyUser() == null)
			bean.setModifyUser(user.getId());
		return "";
	}
	private static class TrustAnyTrustManager implements X509TrustManager {  
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)  
                throws CertificateException {  
        }
		@Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)  
                throws CertificateException {  
        }
		@Override
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[] {};  
        }  
    }  
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		@Override
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    } 

}

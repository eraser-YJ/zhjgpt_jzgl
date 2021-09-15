package com.jc.foundation.util;

import com.jc.common.kit.vo.PageManagerEx;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.PageManager;
import com.jc.system.common.util.FileUtil;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.gateway.utils.TokenGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @Author 常鹏
 * @Date 2020/7/6 16:01
 * @Version 1.0
 */
public class GlobalUtil {
    /**判断是否需要选择进行操作*/
    public static boolean needChange(String departmentCode) {
        if (departmentCode == null) {
            return false;
        }
        if (departmentCode.indexOf(GlobalContext.getProperty("companyDepartmentCode")) > -1) {
            return true;
        }
        return false;
    }

    public static String getDicValue(DicKeyEnum dicKeyEnum, String value) {
        IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
        Dic dic = dicManager.getDic(dicKeyEnum.getTypeCode(), dicKeyEnum.getParentCode(), value);
        return dic == null ? "" : dic.getValue();
    }

    public static void resultToMap(Result result, Map<String, Object> resultMap) {
        resultToMap(result, resultMap, null);
    }

    /**
     * 返回列表中第一个元素
     * @param dataList
     * @param <T>
     * @return
     */
    public static <T> T getFirstItem(List<T> dataList){
        if(dataList != null && dataList.size() > 0){
            return dataList.get(0);
        }
        return null;
    }

    public static void setTableRowNo(PageManager page, Integer pageRows) {
        int no = page.getPage() * pageRows + 1;
        if (page.getPage() != 0) {
            no = (page.getPage() - 1) * pageRows + 1;
        }
        List<? extends BaseBean> dataList = page.getData();
        if (dataList != null) {
            for (BaseBean obj : dataList) {
                obj.setTableRowNo(no++);
            }
        }
    }

    public static void setTableRowNoEx(PageManagerEx<Map<String, Object>> page, Integer pageRows) {
        int no = page.getPage() * pageRows + 1;
        if (page.getPage() != 0) {
            no = (page.getPage() - 1) * pageRows + 1;
        }
        List<Map<String, Object>> dataList = page.getData();
        if (dataList != null) {
            for (Map<String, Object> obj : dataList) {
                obj.put("tableRowNo",no++);
            }
        }
    }

    /**
     * Result转Map
     * @param result
     * @param resultMap
     * @param token
     * @return
     */
    public static void resultToMap(Result result, Map<String, Object> resultMap, String token) {
        if (result.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
            resultMap.put("success", "true");
            String msg = (result.getMsg() != null && result.getMsg().equals(ResultCode.SUCCESS.message())) ? "操作成功" : result.getMsg();
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, msg);
            if (result.getData() != null) {
                resultMap.put("callbackData", result.getData());
            }
        } else {
            resultMap.put("success", "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, result.getMsg());
        }
        if (!StringUtil.isEmpty(token)) {
            resultMap.put(GlobalContext.SESSION_TOKEN, token);
        }
    }

    private static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    /**
     * 查收指定方法
     * @param methods
     * @param attName
     * @return
     */
    private static Method findMethod(Method[] methods, String attName, String methodType) {
        if (methods == null) {
            return null;
        }
        for (Method m : methods) {
            String mName = m.getName();
            if (isMethodEquals(mName, attName, methodType)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 判断是否是指定的get/set方法
     * @param methodName
     * @param name
     * @param
     * @return
     */
    private static boolean isMethodEquals(String methodName, String name, String methodType) {
        if (!methodName.startsWith(methodType)) {
            return false;
        }
        String mName = methodName.toLowerCase();
        String nameLow = name.toLowerCase();
        String nameLowGet = methodType + nameLow;
        if (mName.equals(nameLow) || mName.equals(nameLowGet)) {
            return true;
        }
        return false;
    }

    /**
     * map转bean
     * @param map: 数据
     * @param clz: 返回类对象
     * @param <T>
     * @return
     */
    public static <T> T map2bean(Map<String,Object> map, Class<T> clz) {
        try{
            T obj = clz.newInstance();
            Method[] ms = obj.getClass().getMethods();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                Method targetMethod = findMethod(ms, entry.getKey(), "set");
                if(targetMethod == null){
                    continue;
                }
                Class<?>[] paramClz = targetMethod.getParameterTypes();
                if (paramClz.length != 1) {
                    continue;
                }
                if (paramClz[0] == Date.class) {
                    if (entry.getKey().equals("createDate")) {
                        System.out.println("1111");
                    }
                    String temp = replaceStr(value.toString(), "T", " ");
                    temp = replaceStr(temp, "Z", "");
                    value = dateString2Util(temp, "yyyy-MM-dd HH:mm:ss");

                }
                targetMethod.invoke(obj, value);
            }
            return obj;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> bean2map(Object bean) {
        try {
            Map<String, Object> map = new HashMap<>();
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for(PropertyDescriptor pd : pds) {
                map.put(pd.getName(), pd.getReadMethod().invoke(bean));
            }
            return map;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static String[] listToArray_string(List<String> idList) {
        if (idList == null) {
            return new String[]{};
        }
        String[] str = new String[idList.size()];
        int index = 0;
        for (String s : idList) {
            str[index++] = s;
        }
        return str;
    }

    public static Date dateString2Util(String dateStr, String format) {
        if(isEmpty(dateStr)){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(dateStr, new ParsePosition(0));
    }

    public static String dateUtil2String(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static boolean isEmpty(String word){
        if(word == null || word.trim().length() == 0){
            return true;
        }
        return false;
    }

    public static String replaceStr(String source, String oldString, String newString) {
        if(source == null) {
            return source;
        }
        StringBuffer output = new StringBuffer();
        int lengthOfSource = source.length();
        int lengthOfOld = oldString.length();
        int posStart = 0;
        int pos;
        String lower_s=source.toLowerCase();
        String lower_o=oldString.toLowerCase();
        while ((pos = lower_s.indexOf(lower_o, posStart)) >= 0) {
            output.append(source.substring(posStart, pos));
            output.append(newString);
            posStart = pos + lengthOfOld;
        }
        if (posStart < lengthOfSource) {
            output.append(source.substring(posStart));
        }
        return output.toString();
    }

    public static String[] splitStr(String str , char c){
        if(str == null){
            return null;
        }
        str += c;
        int n = 0;
        for(int i = 0 ; i < str.length() ; i++)	{
            if(str.charAt(i) == c){
                n++;
            }
        }
        String out[] = new String[n];
        for(int i = 0 ; i < n ; i++) {
            int index = str.indexOf(c);
            out[i] = str.substring(0 , index);
            str = str.substring(index + 1 , str.length());
        }
        return out;
    }

    public static String toHtmlOutput(String html){
        if(isEmpty(html)){
            return html;
        }
        html = replaceStr(html , "&amp;" , "&");
        html = replaceStr(html, "&lt;", "<");
        html = replaceStr(html, "&gt;", ">");
        html = replaceStr(html,"&acute;","'");
        html = replaceStr(html,"&quot;","\"");
        html = replaceStr(html,"&#46;",".");
        return html;
    }

    public static String toHtmlInput(String html){
        if(isEmpty(html)){
            return html;
        }
        html = replaceStr(html , "&" , "&amp;");
        html = replaceStr(html, "<", "&lt;");
        html = replaceStr(html, ">", "&gt;");
        html = replaceStr(html, "\t", "    ");
        html = replaceStr(html,"'","&acute;");
        html = replaceStr(html,"\"","&quot;");
        //html = replaceStr(html,".","&#46;");
        return html;
    }

    public static String tohtmlNewLine(String html) {
        if (isEmpty(html)) {
            return html;
        }
        html = replaceStr(html, "\r\n", "<br />");
        html = replaceStr(html, "\n", "<br />");
        html = replaceStr(html, "\r", "<br />");
        return html;
    }

    /**
     * 将字符串转为Integer
     * @@param s
     * @@return
     */
    public static Integer toInteger(String s){
        return toInteger(s , null);
    }

    public static Integer toInteger(String s , Integer defaultValue){
        if (s == null || "".equals(s.trim())) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(s.trim());
        }catch(NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * 将字符串转为long
     * @@param s
     * @@return
     */
    public static Long toLong(String s){
        return toLong(s , null);
    }

    public static Long toLong(String s , Long defaultValue){
        if (s == null || "".equals(s.trim())) {
            return defaultValue;
        }
        try {
            return Long.parseLong(s.trim());
        }catch(NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Float toFloat(String s){
        return toFloat(s , null);
    }

    public static Float toFloat(String s , Float defaultValue){
        if (s == null || "".equals(s.trim())) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(s.trim());
        }catch(NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Double toDouble(String s , Double defaultValue){
        if (s == null || "".equals(s.trim())) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(s.trim());
        }catch(NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Float toDouble(String s){
        return toFloat(s , null);
    }

    public static String getValue(String value, Object[] array){
        try {
            if(array == null || array.length == 0){
                return value;
            }
            String regEx = "\\{\\d+\\}";
            Pattern p = Pattern.compile(regEx);
            Matcher matcher = p.matcher(value);
            StringBuffer sb = new StringBuffer();
            Integer i = 0;
            while (matcher.find()) {
                if(i < array.length ){
                    String arrar_tmp = array[i].toString();
                    if(arrar_tmp == null){
                        arrar_tmp = "";
                    }
                    arrar_tmp = arrar_tmp.replaceAll("$", "");
                    matcher.appendReplacement(sb, arrar_tmp);
                    i++;
                }
            }
            matcher.appendTail(sb);
            value = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String stringNotnull(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }

    public static BigDecimal ratioParamLong(Long arg0 , Long arg1, int roundNum){
        if(arg0 == null) {
            arg0 = 0L;
        }
        if(arg1 == null) {
            arg1 = 0L;
        }
        BigDecimal value1 = new BigDecimal(arg0);
        BigDecimal value2 = new BigDecimal(arg1);
        if(value2.compareTo(new BigDecimal(0)) == 0){
            return new BigDecimal(0);
        }
        return multiply(divide(value1, value2, roundNum + 2), BigDecimal.valueOf(100)).setScale(roundNum, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal arg1 , BigDecimal arg2){
        if (arg1 == null) {
            arg1 = new BigDecimal("0");
        }
        if (arg2 == null) {
            arg2 = new BigDecimal("0");
        }
        return arg1.add(arg2);
    }

    public static BigDecimal subtract(BigDecimal arg1 , BigDecimal arg2){
        return arg1.subtract(arg2);
    }

    public static BigDecimal multiply(BigDecimal arg1 , BigDecimal arg2){
        return arg1.multiply(arg2);
    }

    /**
     * 除法运算，四舍五入
     * @param arg1: 被除数
     * @param arg2: 除数
     * @return
     */
    public static BigDecimal divide(BigDecimal arg1 , BigDecimal arg2, int roundNum){
        if(arg2.compareTo(new BigDecimal(0)) == 0){
            return new BigDecimal(0);
        }
        return arg1.divide(arg2, roundNum, RoundingMode.HALF_UP);
    }

    public static String convertContent(String value, Object objClazz) {
        if (objClazz == null || StringUtil.isEmpty(value)) {
            return value;
        }
        Map<String, Object> columnMap = new HashMap<>(10);
        try {
            BeanInfo info = Introspector.getBeanInfo(objClazz.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for(PropertyDescriptor pd : pds) {
                columnMap.put(pd.getName(), pd.getReadMethod().invoke(objClazz));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String regEx = "\\$\\{\\w+\\}";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            for(int i = 0; i <= matcher.groupCount(); i++){
                String word = matcher.group(i);
                word = replaceStr(word, "${", "");
                word = replaceStr(word, "}", "");
                String columnValue = columnMap.get(word) == null ? "" : columnMap.get(word) + "";
                matcher.appendReplacement(sb, columnValue);
            }
        }
        matcher.appendTail(sb);
        value = sb.toString();
        return value;
    }

    /**
     * 抽取字符串中的变量，并按照顺序将替换的值进行赋值操作
     * @param value
     * @param objClazz
     * @return
     */
    public static List<Object> getValueParam(String value, Object objClazz) {
        if (objClazz == null || StringUtil.isEmpty(value)) {
            return null;
        }
        Map<String, Object> columnMap = new HashMap<>(10);
        try {
            BeanInfo info = Introspector.getBeanInfo(objClazz.getClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            for(PropertyDescriptor pd : pds) {
                columnMap.put(pd.getName(), pd.getReadMethod().invoke(objClazz));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String regEx = "\\$\\{\\w+\\}";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(value);
        List<Object> valueList = new ArrayList<>();
        while (matcher.find()) {
            for(int i = 0; i <= matcher.groupCount(); i++){
                String word = matcher.group(i);
                word = replaceStr(word, "${", "");
                word = replaceStr(word, "}", "");
                valueList.add(columnMap.get(word) == null ? "" : columnMap.get(word) + "");
            }
        }
        return valueList;
    }

    public static String firstUpper(String word) {
        char[] chars = word.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    public static String returnEmpty(String word) {
        if (word == null || word.trim().length() == 0 || word.equals("null")) {
            return "";
        }
        return word;
    }

    public static Date getDateByAfterDay(Date current, int day, String holiday) {
        int count = 0;
        Date result = null;
        while (true) {
            count++;
            String date = GlobalUtil.dateUtil2String(new Date(current.getTime() + (1000L * 60 * 60 * 24 * count)), "yyyy-MM-dd");
            if (holiday.indexOf("," + date + ",") < 0) {
                day--;
                result = GlobalUtil.dateString2Util(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
            if (day == 0) {
                break;
            }
        }
        return result;
    }

    /**
     * 获取指定日期中除去假期外的日期树
     * @param current
     * @param endTime
     * @param holiday
     * @return
     */
    public static String getDayByDate(Date current, Date endTime, String holiday) {
        if (endTime == null) {
            return "无限期";
        }
        if (current.getTime() > endTime.getTime()) {
            return "已超期";
        }
        int result = 0, count = 0;
        while (true) {
            count++;
            long time = current.getTime() + (1000L * 60 * 60 * 24 * count);
            String date = GlobalUtil.dateUtil2String(new Date(time), "yyyy-MM-dd");
            if (holiday.indexOf("," + date + ",") < 0) {
                result++;
            }
            if (time > endTime.getTime()) {
                break;
            }
        }
        return result + "";
    }

    public static int getYear() {
        return new GregorianCalendar().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return new GregorianCalendar().get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        return new GregorianCalendar().get(Calendar.DATE);
    }

    public static int getHour() {
        return new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return new GregorianCalendar().get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return new GregorianCalendar().get(Calendar.SECOND);
    }

    public static String UrlEncoder(String url){
        try {
            if(url == null) {
                url = "";
            }
            String tmp = URLEncoder.encode(url,"UTF-8");
            return tmp;
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static String UrlDecoder(String url){
        try {
            if(url != null){
                return URLDecoder.decode(url,"UTF-8");
            }else{
                return null;
            }
        }catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w <= 0) {
            w = 7;
        }
        return w;
    }

    public static String getChineseDate() {
        int week = getWeekOfDate(new Date(System.currentTimeMillis()));
        String value = "";
        if (week == 1) { value = "一"; }
        else if (week == 2) { value = "二"; }
        else if (week == 3) { value = "三"; }
        else if (week == 4) { value = "四"; }
        else if (week == 5) { value = "五"; }
        else if (week == 6) { value = "六"; }
        else if (week == 7) { value = "日"; }
        return GlobalUtil.dateUtil2String(new Date(System.currentTimeMillis()), "yyyy年MM月dd日") + "星期" + value;
    }

    public static Result writeFile(String id, Date createDate, String content) {
        String filePath = GlobalContext.getProperty("FILE_TOOL_PATH") + "/" + GlobalUtil.dateUtil2String(createDate, "yyyy/MM/dd");
        String fileName = filePath + "/" + id + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileUtil.createNewFile(fileName, content);
        return Result.success();
    }

    public static String getFileContent(String id, Date createDate) {
        String filePath = GlobalContext.getProperty("FILE_TOOL_PATH") + "/" + GlobalUtil.dateUtil2String(createDate, "yyyy/MM/dd");
        String fileName = filePath + "/" + id + ".txt";
        File file = new File(fileName);
        if (file.exists()) {
            StringBuffer str = new StringBuffer();
            try {
                InputStream is = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                try {
                    String data = "";
                    while ((data = br.readLine()) != null) {
                        str.append(data);
                    }
                } catch (Exception e) {
                    str.append(e.toString());
                }
                br.close();
                isr.close();
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return str.toString();
        }
        return null;
    }

    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    /**
     * 获取周一日期
     * @return
     */
    public static String getMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public static String getMonday(String format) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        return GlobalUtil.dateUtil2String(monday, format);
    }

    /**
     * 获取当前周周日的日期
     * @return
     */
    public static String getSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public static String getSunday(String format) {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus +6);
        Date monday = currentDate.getTime();
        return GlobalUtil.dateUtil2String(monday, format);
    }

    public static String getCurrentWeek() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return ((calendar.get(Calendar.WEEK_OF_YEAR)) - 2) + "";
    }

    public static void setExtendInfomation(PageManager page) {
        List<CmMessageInfo> dataList = (List<CmMessageInfo>) page.getData();
        if (dataList != null) {
            for (CmMessageInfo obj : dataList) {
                IWorkflowTodoService service = WorkflowContentEnum.getByCode(obj.getWorkflowTitle()).getService();
                if (service != null) {
                    TodoExtendBean extend = service.getByPid(obj.getWorkflowBean().getBusiness_Key_());
                    if (extend != null) {
                        obj.setBusinessCode(extend.getCode());
                        obj.setBusinessDesc(extend.getName());
                    }
                }
            }
        }
    }

    public static Object convertData(Method method, String value) {
        if (value == null) {
            return null;
        }
        String clazzName = method.getParameterTypes()[0].getSimpleName();
        if ("String".equals(clazzName)) {
            return value;
        } else if ("BigDecimal".equals(clazzName)) {
            return new BigDecimal(value);
        } else if ("Integer".equals(clazzName)) {
            return GlobalUtil.toInteger(value);
        } else if ("Long".equals(clazzName)) {
            return GlobalUtil.toLong(value);
        } else if ("Date".equals(clazzName)) {
            if (value.indexOf(" ") > -1) {
                return GlobalUtil.dateString2Util(value, "yyyy-MM-dd HH:mm:ss");
            } else {
                return GlobalUtil.dateString2Util(value, "yyyy-MM-dd");
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        /*BigDecimal s = new BigDecimal(10000);
        BigDecimal e = new BigDecimal(9000);
        BigDecimal r = GlobalUtil.subtract(s, e);
        System.out.println(r);*/
        /*StringBuffer sbuf = new StringBuffer();
        sbuf.append("function getDaysBetween(start){");
        sbuf.append(" if (start.indexOf('-') > -1) {");
        sbuf.append(" start = start.replace('-', '/');");
        sbuf.append(" }");
        sbuf.append(" var startDate = Date.parse(start);");
        sbuf.append(" var endDate = new Date().getTime();");
        sbuf.append(" return (endDate - startDate)/(1*24*60*60*1000);");
        sbuf.append(" }");
        ScriptEngineManager factory = new ScriptEngineManager();
        // 每次生成一个engine实例
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        engine.eval(sbuf.toString());
        Object value = ((Invocable) engine).invokeFunction("getDaysBetween", "2020-07-01");
        System.out.println(value);*/
       /* Date expireTime = new Date(System.currentTimeMillis() + 12 * 1000);
        String token = TokenGenerator.getInstance().generateToken("391f4266d4bc4e73b2880ee3da78c410", "com.yx.mobile", expireTime);
        System.out.println(token);
        System.out.println(TokenGenerator.getInstance().resolveToken(token)[0]);*/
        System.out.println(getMonday() + "  " + getSunday() + "  " + getCurrentWeek());
    }
}

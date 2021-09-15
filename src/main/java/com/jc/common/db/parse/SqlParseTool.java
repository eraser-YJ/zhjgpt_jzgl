package com.jc.common.db.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.aviator.AviatorEvaluator;
import com.jc.common.db.parse.domain.FieldFragment;
import com.jc.common.db.parse.domain.IFragment;
import com.jc.common.db.parse.domain.ISegment;
import com.jc.common.db.parse.domain.SqlExePara;
import com.jc.common.db.parse.domain.SqlVO;
import com.jc.common.db.parse.domain.StringFragment;

/**
 * @author Administrator
 * @description 替换工具
 */
public class SqlParseTool {

    /**
     * @param sql
     * @return
     * @description 分析出Count语句
     */
    public static String pageCountSql(String sql) {
        String newSql = sql.trim();
        String newSqlUp = newSql.toUpperCase();
        int index = newSqlUp.indexOf("FROM ");
        return "select count(*) " + newSql.substring(index);
    }

    /**
     * @param sql
     * @return
     * @description 分析出Query语句
     */
    public static String pageQuerySql(String sql) {
        String newSql = sql.trim();
        return newSql;
    }

    /**
     * @param sql
     * @return
     * @description 分析片段
     */
    public static List<IFragment> processFragment(String sql) {
        String newSql = sql;
        Pattern p = Pattern.compile("((\\$\\{[\\s\\w,=\"\'-]*\\})|(\\#\\{[\\s\\w,=\"\'-]*\\}))");
        Matcher m = p.matcher(newSql);
        List<IFragment> voList = new ArrayList<IFragment>();
        String preStr;
        String fieldStr;
        int begin;
        int end;
        char type;
        while (m.find()) {
            begin = m.start();
            if (begin > 0) {
                preStr = newSql.substring(0, begin);
                if (preStr != null && preStr.trim().length() > 0) {
                    voList.add(new StringFragment(preStr.trim()));
                }
            }
            fieldStr = m.group().trim();
            type = fieldStr.charAt(0);
            voList.add(new FieldFragment(m.group(), '$' == type));
            end = m.end();
            if (end < newSql.length()) {
                newSql = newSql.substring(end);
                m = p.matcher(newSql);
                continue;
            } else {
                newSql = "";
            }
            break;
        }
        if (newSql.trim().length() > 0) {
            voList.add(new StringFragment(newSql));
        }
        return voList;
    }

    /**
     * @param condition
     * @return
     * @description 条件判断
     */
    public static boolean predicate(String condition, Map<String, Object> paramters) {
        if (condition == null || condition.trim().length() == 0) {
            return true;
        }
        Map<String,Object> objects = new HashMap<String,Object>();
        if(paramters!=null){
            String key;
            Object value;
            for(Map.Entry<String,Object> entry:paramters.entrySet()){
                key = entry.getKey().toUpperCase();
                value = entry.getValue();
                if(value == null){
                    objects.put(entry.getKey(),value);
                    continue;
                }
                if(key.endsWith("_INT")||key.endsWith("_LONG")){
                    String tempValue = value.toString().trim();
                    if(tempValue.length() == 0){
                        objects.put(entry.getKey(),value);
                    } else {
                        try{
                            objects.put(entry.getKey(),Long.valueOf(tempValue));
                        }catch (Exception ex){
                            objects.put(entry.getKey(),value);
                        }
                    }

                } else if(key.endsWith("_FLOAT")||key.endsWith("_DOUBLE")){
                    String tempValue = value.toString().trim();
                    if(tempValue.length() == 0){
                        objects.put(entry.getKey(),value);
                    } else {
                        try{
                            objects.put(entry.getKey(),Double.valueOf(tempValue));
                        }catch (Exception ex){
                            objects.put(entry.getKey(),value);
                        }
                    }
                }   else {
                    objects.put(entry.getKey(),value);
                }

            }
        }
        // 预处理
        String newCond = aviatorPreProcess(condition);
        // 执行表达式
        Boolean result2 = (Boolean) AviatorEvaluator.execute(newCond, objects);
        return result2;
    }

    /**
     * @param condition
     * @return
     * @description 预处理
     */
    private static String aviatorPreProcess(String condition) {
        String newCond = condition;
        newCond = replaceAll3(newCond, " and ", " && ");
        newCond = replaceAll3(newCond, " or ", " || ");
        newCond = replaceAll3(newCond, "null", "nil");
        newCond = replaceAll3(newCond, "&gt;", ">");
        newCond = replaceAll3(newCond, "&lt;", "<");
        return newCond;
    }

    /***
     * replaceAll,忽略大小写
     *
     * @param input
     * @param regex
     * @param replacement
     * @return
     */
    public static String replaceAll3(String input, String regex, String replacement) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        String result = m.replaceAll(replacement);
        return result;
    }

    /**
     * @param sql
     * @return
     * @description 预处理
     */
    public static String formart(String sql) throws Exception {
        String newSql = sql;
        // 回车替换
        newSql = newSql.replaceAll("\r\n", " ");
        newSql = newSql.replaceAll("\r", " ");
        newSql = newSql.replaceAll("\n", " ");
        if (sql.indexOf("/*") > 0 || sql.indexOf("--") > 0) {
            throw new Exception("sql:" + sql + "，包含注解信息，目前不支持");
        }
        while (newSql.indexOf("< ") >= 0) {
            newSql = newSql.replace("< ", "<");
        }
        while (newSql.indexOf(" <") >= 0) {
            newSql = newSql.replace(" <", "<");
        }
        while (newSql.indexOf("</ ") >= 0) {
            newSql = newSql.replace("</ ", "</");
        }
        while (newSql.indexOf(" </") >= 0) {
            newSql = newSql.replace(" </", "</");
        }
        while (newSql.indexOf(" >") >= 0) {
            newSql = newSql.replace(" >", ">");
        }
        while (newSql.indexOf("> ") >= 0) {
            newSql = newSql.replace("> ", ">");
        }
        newSql = newSql.trim();

        return newSql;
    }

    /**
     * @param segmentList
     * @param paramters
     * @return
     * @throws Exception
     * @description 绑定参数
     */
    public static SqlVO bound(List<ISegment> segmentList, Map<String, Object> paramters) throws Exception {
        List<SqlExePara> list = new ArrayList<>();
        List<SqlExePara> subList;
        for (ISegment temp : segmentList) {
            subList = temp.boundSql(paramters);
            if (subList != null && subList.size() > 0) {
                list.addAll(subList);
            }
        }
        StringBuilder sql = new StringBuilder();
        String fragment;
        List<Object> paramList = new ArrayList<Object>();
        for (SqlExePara fvo : list) {
            fragment = "";
            if (fvo == null) {
                continue;
            }
            fragment = fvo.getSqlFragment();

            sql.append(fragment).append(" ");
            if (fvo.getValue() != null) {
                paramList.add(fvo.getValue());
            }
        }
        return new SqlVO(sql.toString(), paramList);
    }

    /**
     * @param inContent
     * @param key
     * @param begin
     * @return
     * @description 查询解析
     */
    public static int findKeyEnd(String inContent, String key, int begin) {
        String content = inContent.toUpperCase();
        int tempIndex;
        int tempBegin = begin;
        String beginKey = "<" + key + " ";
        String endKey = "</" + key + ">";
        int pair = 1;
        // 寻找结束位置
        while (tempBegin < inContent.length()) {
            tempIndex = content.indexOf(">", tempBegin);
            String nowValue = content.substring(tempBegin, tempIndex + 1);
            if (nowValue.indexOf(beginKey) >= 0) {
                pair++;
            } else if (nowValue.indexOf(endKey) >= 0) {
                pair--;
                if (pair <= 0) {
                    return tempBegin + nowValue.indexOf(endKey);
                }
            }
            tempBegin = tempIndex + 1;
        }
        throw new RuntimeException("表达式错误");
    }

    /**
     * @param inSql 输入SQL
     * @return
     * @description 解析SQL
     */
    public static List<ISegment> parse(String inSql) throws Exception {
        String newSql = inSql;
        // 前处理
        newSql = SqlParseTool.formart(newSql);
        // 封装成对象
        String upNewSql = newSql.toUpperCase();
        int nowIndex = 0;
        int lastIndex = 0;
        int keyEnd;
        int end;
        String key;
        String endKey;
        ISegment segment;
        char now;
        List<ISegment> segmentList = new ArrayList<ISegment>();
        while (nowIndex < upNewSql.length()) {
            now = upNewSql.charAt(nowIndex);
            if (now == '<') {
                keyEnd = upNewSql.indexOf(" ", nowIndex + 1);
                if (keyEnd < 0) {
                    nowIndex++;
                    continue;
                }
                key = upNewSql.substring(nowIndex + 1, keyEnd);
                if (!ISegment.isSupportSegment(key)) {
                    nowIndex++;
                    continue;
                }
                endKey = "</" + key + ">";
                // 查询结束位置
                end = findKeyEnd(upNewSql, key, keyEnd);
                if (lastIndex + 1 < nowIndex) {
                    segment = ISegment.buildString(newSql.substring(lastIndex, nowIndex));
                    segmentList.add(segment);
                }
                segment = ISegment.build(key, newSql.substring(nowIndex, end + endKey.length()));
                segmentList.add(segment);
                lastIndex = end + endKey.length();
                nowIndex = end + endKey.length();
            } else {
                nowIndex++;
            }

        }

        if (lastIndex + 1 < nowIndex) {
            String right = newSql.substring(lastIndex, nowIndex);
            if (right != null && right.trim().length() > 0) {
                segment = ISegment.buildString(right);
                segmentList.add(segment);
            }
        }
        return segmentList;

    }

}

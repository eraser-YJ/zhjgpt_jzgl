package com.jc.common.def;


import com.jc.common.kit.PathUtil;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 取得定义
 */
public class DefUtil {

    //显示列数
    public final static int PUB_DIS_COLUMN_NUM = 2;

    //key value 显示对，这个应该是不会变的
    public final static int PUB_ATT_LEN = 2;

    //缓存启动
    private static String cacheStart = GlobalContext.getProperty("def.cache.start");

    //基础路径
    private static String basePath = GlobalContext.getProperty("def.path");

    //缓存
    private static Map<String, DefVO> cache = new HashMap<>();

    /**
     * 取得地址
     *
     * @param subPath
     * @param url
     * @return
     * @throws Exception
     */
    public static DefVO getXml(String subPath, String url) throws Exception {
        String defxml;
        if (subPath != null && subPath.trim().length() > 0) {
            defxml = PathUtil.trim(basePath) + "/" + PathUtil.trim(subPath) + "/" + PathUtil.trim(url) + ".xml";
        } else {
            defxml = PathUtil.trim(basePath) + "/" + PathUtil.trim(url) + ".xml";
        }
        DefVO defVO = null;
        defVO = cache.get(defxml);
        if (defVO != null) {
            return defVO;
        }
        defVO = (DefVO) XMLUtil.convertXmlStrToObject(DefVO.class, new String(Files.readAllBytes(Paths.get(defxml)), Charset.forName("UTF-8")));
        if (defVO == null) {
            return null;
        }
        if ("true".equalsIgnoreCase(cacheStart) || "Y".equalsIgnoreCase(cacheStart)) {
            cache.put(defxml, defVO);
        }
        return defVO;
    }


    /**
     * 取得SQL
     *
     * @param subPath
     * @param url
     * @return
     * @throws Exception
     */
    public static DefVO getSQL(String subPath, String url) throws Exception {
        String sqltxt;
        if (subPath != null && subPath.trim().length() > 0) {
            sqltxt = PathUtil.trim(basePath) + "/" + PathUtil.trim(subPath) + "/" + PathUtil.trim(url) + ".txt";
        } else {
            sqltxt = PathUtil.trim(basePath) + "/" + PathUtil.trim(url) + ".txt";
        }
        DefVO defVO = null;
        defVO = cache.get(sqltxt);
        if (defVO != null) {
            return defVO;
        }
        defVO = new DefVO();
        defVO.setQuerySql(new String(Files.readAllBytes(Paths.get(sqltxt)), Charset.forName("UTF-8")));
        if ("true".equalsIgnoreCase(cacheStart) || "Y".equalsIgnoreCase(cacheStart)) {
            cache.put(sqltxt, defVO);
        }
        return defVO;
    }

    /**
     * 取得SQL和XML
     *
     * @param subPath
     * @param url
     * @return
     * @throws Exception
     */
    public static DefVO getXmlAndSQL(String subPath, String url) throws Exception {
        String sqltxt;
        String defxml;
        if (subPath != null && subPath.trim().length() > 0) {
            sqltxt = PathUtil.trim(basePath) + "/" + PathUtil.trim(subPath) + "/" + PathUtil.trim(url) + ".txt";
            defxml = PathUtil.trim(basePath) + "/" + PathUtil.trim(subPath) + "/" + PathUtil.trim(url) + ".xml";
        } else {
            sqltxt = PathUtil.trim(basePath) + "/" + PathUtil.trim(url) + ".txt";
            defxml = PathUtil.trim(basePath) + "/" + PathUtil.trim(url) + ".xml";
        }
        DefVO defVO = null;
        defVO = cache.get(defxml);
        if (defVO != null) {
            return defVO;
        }
        File file = new File(defxml);
        System.out.println(file.getAbsolutePath());
        defVO = (DefVO) XMLUtil.convertXmlStrToObject(DefVO.class, new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.forName("UTF-8")));
        if (defVO == null) {
            return null;
        }
        file = new File(sqltxt);
        System.out.println(file.getAbsolutePath());
        defVO.setQuerySql(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), Charset.forName("UTF-8")));
        if ("true".equalsIgnoreCase(cacheStart) || "Y".equalsIgnoreCase(cacheStart)) {
            cache.put(defxml, defVO);
        }
        return defVO;
    }


    /**
     * 取得显示数据
     *
     * @param defItemVO
     * @param itemValue
     * @return
     */
    public static Object getItemShowValue(DefItemVO defItemVO, Object itemValue) {
        try {
            Object value = getItemRealValue(defItemVO, itemValue);
            if (value == null) {
                return "";
            }
            return itemValue;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    //正则
    private static Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

    /**
     * 取得数字
     *
     * @param input
     * @return
     */
    public static String getLastNum(String input) {
        if (input == null || input.trim().length() <= 0) {
            return null;
        }
        Matcher matcher = pattern.matcher(input);
        String num = null;
        while (matcher.find()) {
            num = matcher.group();
        }
        if (input.startsWith("-")) {
            return "-" + num;
        } else {
            return num;
        }
    }

    /**
     * 取得数字
     *
     * @param input
     * @return
     */
    public static String getLastNum1(Object input) {
        if (input == null || input.toString().length() <= 0) {
            return null;
        }
        Matcher matcher = pattern.matcher(input.toString());
        String num = null;
        while (matcher.find()) {
            num = matcher.group();
        }
        if (input.toString().startsWith("-")) {
            return "-" + num;
        } else {
            return num;
        }

    }

    /**
     * 取得显示数据
     *
     * @param defItemVO
     * @param itemValue
     * @return
     */
    public static Object getItemRealValue(DefItemVO defItemVO, Object itemValue) {
        if (itemValue == null) {
            return null;
        }
        if (defItemVO == null) {
            return null;
        }
        try {
//            if ("Date".equalsIgnoreCase(defItemVO.getItemType())) {
//                if (itemValue instanceof Date && defItemVO.getDateFormat() != null) {
//                    SimpleDateFormat f = new SimpleDateFormat(defItemVO.getDateFormat());
//                    return f.format((Date) itemValue);
//                }
//            } else if ("Dic".equalsIgnoreCase(defItemVO.getItemType())) {
//                if (defItemVO.getDicCode() != null && defItemVO.getDicParentId() != null) {
//                    IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
//                    Dic dic = dicManager.getDic(defItemVO.getDicCode(), defItemVO.getDicParentId(), itemValue.toString());
//                    return dic == null ? "" : dic.getValue();
//                }
//            } else if ("Long".equalsIgnoreCase(defItemVO.getItemType())) {
//                String num = getLastNum(itemValue.toString());
//                if (num != null) {
//                    return BigDecimal.valueOf(Long.valueOf(num));
//                } else {
//                    return 0;
//                }
//
//            } else if ("Integer".equalsIgnoreCase(defItemVO.getItemType())) {
//                String num = getLastNum(itemValue.toString());
//                if (num != null) {
//                    return BigDecimal.valueOf(Integer.valueOf(num));
//                } else {
//                    return 0;
//                }
//
//            } else if ("Double".equalsIgnoreCase(defItemVO.getItemType())) {
//                String num = getLastNum(itemValue.toString());
//                if (num != null) {
//                    return BigDecimal.valueOf(Double.valueOf(num));
//                } else {
//                    return 0;
//                }
//            }
            return itemValue;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }

    }


    /**
     * 列表排序组装
     *
     * @param inList
     * @return
     * @throws Exception
     */
    public static List<DefItemVO> assemblyListTable(List<DefItemVO> inList) throws Exception {
        //过滤加排序
        List<DefItemVO> list = inList.stream().filter(item -> item.getListSeq() != null).sorted(new Comparator<DefItemVO>() {
            @Override
            public int compare(DefItemVO t0, DefItemVO t1) {
                return t0.getListSeq().compareTo(t1.getListSeq());
            }
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 条件排序组装
     *
     * @param inList
     * @return
     * @throws Exception
     */
    public static List<PageAttributeRow> assemblyCondTable(List<DefItemVO> inList) throws Exception {
        return assemblyTable(inList, new IRunAssemblyItf() {

            @Override
            public void setDisLen(DefItemVO item, Integer value) {
                item.setCondLen(value);
            }

            @Override
            public Integer getDisLen(DefItemVO item) {
                return item.getCondLen();
            }

            @Override
            public void setDisSeq(DefItemVO item, Integer value) {
                item.setCondSeq(value);
            }

            @Override
            public Integer getDisSeq(DefItemVO item) {
                return item.getCondSeq();
            }
        });
    }


    /**
     * 卡片排序组装
     *
     * @param inList
     * @return
     * @throws Exception
     */
    public static List<PageAttributeRow> assemblyFormTable(List<DefItemVO> inList) throws Exception {
        return assemblyTable(inList, new IRunAssemblyItf() {

            @Override
            public void setDisLen(DefItemVO item, Integer value) {
                item.setFormLen(value);
            }

            @Override
            public Integer getDisLen(DefItemVO item) {
                return item.getFormLen();
            }

            @Override
            public void setDisSeq(DefItemVO item, Integer value) {
                item.setFormSeq(value);
            }

            @Override
            public Integer getDisSeq(DefItemVO item) {
                return item.getFormSeq();
            }
        });
    }

    /**
     * 卡片排序组装
     *
     * @param inList
     * @return
     * @throws Exception
     */
    public static List<PageAttributeRow> assemblyTable(List<DefItemVO> inList, IRunAssemblyItf itf) throws Exception {
        //过滤加排序
        List<DefItemVO> list = inList.stream().filter(item -> itf.getDisSeq(item) != null).sorted(new Comparator<DefItemVO>() {
            @Override
            public int compare(DefItemVO t0, DefItemVO t1) {
                return itf.getDisSeq(t0).compareTo(itf.getDisSeq(t1));
            }
        }).collect(Collectors.toList());
        List<PageAttributeRow> resList = new ArrayList<>();
        int pageRow = PUB_DIS_COLUMN_NUM;
        int nowLeft = pageRow;
        PageAttributeRow nowRow = new PageAttributeRow();
        int width = 0;
        for (DefItemVO item : list) {
            width = itf.getDisLen(item);
            if (width > nowLeft) {
                if (nowRow.getRowList().size() > 0) {
                    if (nowLeft > 0) {
                        DefItemVO temp = nowRow.getRowList().get(nowRow.getRowList().size() - 1);
                        itf.setDisLen(temp, (itf.getDisLen(temp) + nowLeft));
                    }
                    resList.add(nowRow);
                }
                nowRow = new PageAttributeRow();
                nowRow.getRowList().add(item);
                nowLeft = pageRow - width;
            } else {
                nowRow.getRowList().add(item);
                nowLeft = nowLeft - width;
            }
        }
        if (nowRow.getRowList().size() > 0) {
            if (nowLeft > 0) {
                DefItemVO temp = nowRow.getRowList().get(nowRow.getRowList().size() - 1);
                itf.setDisLen(temp, (itf.getDisLen(temp) + nowLeft));
            }
            resList.add(nowRow);
        }
        return resList;
    }
}

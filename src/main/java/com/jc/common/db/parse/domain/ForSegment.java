package com.jc.common.db.parse.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jc.common.db.parse.SqlParseTool;

//<foreach collection="primaryKeys" item="primaryKey" index="index" open="(" close=")" separator=",">
//#{primaryKey}
//</foreach>
public class ForSegment implements ISegment {
    private static final String FOR_ATT_COLLECTION = "collection";

    private static final String FOR_ATT_ITEM = "item";

    private static final String FOR_ATT_SEPARATOR = "separator";

    private static final String FOR_ATT_OPEN = "open";

    private static final String FOR_ATT_CLOSE = "close";

    // 原始SQL
    private String sqlSegment;
    // Sql
    private List<ISegment> segmentList;

    private Map<String, String> propertyMap = new HashMap<>();

    @Override
    public List<SqlExePara> boundSql(Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        String collection = propertyMap.get(FOR_ATT_COLLECTION);
        if (collection == null || collection.toString().trim().length() < 0) {
            return resList;
        }
        String key = propertyMap.get(FOR_ATT_ITEM);
        if (key == null || key.toString().trim().length() < 0) {
            return resList;
        }
        Object collectionValue = paraMap.get(collection);
        if (collectionValue == null || collectionValue.toString().trim().length() < 0) {
            return resList;
        }

        String[] values = collectionValue.toString().trim().split(",");

        String open = propertyMap.get(FOR_ATT_OPEN);
        if (open != null && open.trim().length() > 0) {
            resList.addAll(ISegment.buildString(open).boundSql(paraMap));
        }
        String separator = propertyMap.get(FOR_ATT_SEPARATOR);
        int i = -1;
        for (String value : values) {
            i++;
            paraMap.put(key, value);
            List<SqlExePara> listTemp = null;
            if (i != 0 && key != null && key.trim().length() > 0) {
                resList.addAll(ISegment.buildString(separator).boundSql(paraMap));
            }
            for (ISegment temp : segmentList) {
                listTemp = temp.boundSql(paraMap);
                if (listTemp != null && listTemp.size() > 0) {
                    resList.addAll(listTemp);
                }
            }
        }
        String close = propertyMap.get(FOR_ATT_CLOSE);
        if (close != null && close.trim().length() > 0) {
            resList.addAll(ISegment.buildString(close + " ").boundSql(paraMap));
        }
        return resList;
    }

    public ForSegment(String inSql) throws Exception {
        this.sqlSegment = inSql;
        if (this.sqlSegment == null) {
            this.sqlSegment = "";
        }
        this.sqlSegment = this.sqlSegment.trim();
        // 条件
        int attBegin = sqlSegment.indexOf(" ");
        int attEnd = sqlSegment.indexOf(">", attBegin);
        String property = sqlSegment.substring(attBegin, attEnd);

        property = property.trim();
        // 去掉多余的空格和符合
        property = property.replace("\"", "");
        while (property.indexOf("  ") > 0) {
            property = property.replace("  ", " ");
        }
        String[] atts = property.split(" ");
        String att;
        String[] attKeyValue;
        for (int i = 0; i < atts.length; i++) {
            att = atts[i].trim();
            attKeyValue = att.split("=");
            if (attKeyValue.length > 1) {
                propertyMap.put(attKeyValue[0].trim().toLowerCase(), attKeyValue[1].trim());
            } else {
                propertyMap.put(attKeyValue[0].trim().toLowerCase(), "");
            }

        }
        int txtBegin = sqlSegment.indexOf(">", attEnd + 1);
        String endKey = "</FOREACH>";
        String newSql = sqlSegment.substring(txtBegin + 1, sqlSegment.length() - endKey.length());
        segmentList = SqlParseTool.parse(newSql);
    }

}

package com.jc.common.db.parse.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jc.common.db.parse.SqlParseTool;

public class IFSegment implements ISegment {

    // 原始SQL
    private StringBuilder sqlSegment;
    // 条件
    private String testCond;
    // Sql
    private List<ISegment> segmentList;

    public IFSegment(String inSql) throws Exception {
        if (inSql == null) {
            this.sqlSegment = new StringBuilder();
        } else {
            this.sqlSegment = new StringBuilder(inSql.trim());
        }

        if (inSql.toUpperCase().indexOf("TEST") < 0) {
            throw new Exception("if语句不符合规范,必须含有test");
        }
        // 条件
        int condBegin = sqlSegment.indexOf("\"");
        int condEnd = sqlSegment.indexOf("\"", condBegin + 1);
        if (condBegin != -1 && condEnd != -1 && condBegin + 1 < condEnd) {
            String newTestCond = sqlSegment.substring(condBegin + 1, condEnd);
            newTestCond = newTestCond.trim();
            if (newTestCond.trim().length() > 0) {
                testCond = newTestCond;
            }
        }
        int txtBegin = sqlSegment.indexOf(">", condEnd + 1);
        String endKey = "</IF>";
        String newSql = sqlSegment.toString().substring(txtBegin + 1, sqlSegment.length() - endKey.length());
        segmentList = SqlParseTool.parse(newSql);

    }

    @Override
    public List<SqlExePara> boundSql(Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        if (SqlParseTool.predicate(testCond, paraMap)) {
            List<SqlExePara> listTemp;
            for (ISegment temp : segmentList) {
                listTemp = temp.boundSql(paraMap);
                if (listTemp != null && listTemp.size() > 0) {
                    resList.addAll(listTemp);
                }
            }
        }
        return resList;
    }

    public void addsqlSegment(String nowChar) {
        this.sqlSegment.append(nowChar);
    }

    public void addsqlSegment(char nowChar) {
        this.sqlSegment.append(nowChar);
    }
}

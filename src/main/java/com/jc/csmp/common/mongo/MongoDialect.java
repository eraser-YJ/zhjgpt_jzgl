package com.jc.csmp.common.mongo;

import com.jc.common.kit.vo.PageManagerEx;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.warn.info.domain.RunInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * mongo操作
 */
public class MongoDialect {

    //日志
    private static Uog log = Uog.getInstanceOnMongo();

    //保存传感器数据
    public void appendSensorRecord(Map<String, Object> line) throws CustomException {
        appendRecord(RunInfo.TABLE_NAME, line);
    }

    //保存数据
    public void appendRecord(String table, Map<String, Object> line) throws CustomException {

        try {
            // 当前时间
            MongoCollection<Document> dataDocument = MongoDao.getInstance().getMongoCollection(table);
            // 插入新数据
            Document document2 = new Document();
            document2.putAll(line);
            dataDocument.insertOne(document2);

            log.debug("**插入（" + table + "）完成");
        } catch (Exception ex) {
            log.error("发送异常，异常信息：" + ex.getMessage(), ex);
        }
    }


    /**
     * 分页查询
     *
     * @param info
     * @param page
     * @return
     * @throws CustomException
     */
    public PageManagerEx<Map<String, Object>> query(RunInfo info, PageManager page) throws CustomException {
        // 创建连接
        MongoCollection<Document> dataDocument = MongoDao.getInstance().getMongoCollection(RunInfo.TABLE_NAME);
        BasicDBObject queryObject = new BasicDBObject();
        if (info.getRunTimeBegin() != null || info.getRunTimeEnd() != null) {
            BasicDBObject dateScopeObject = new BasicDBObject();
            if (info.getRunTimeBegin() != null) {
                dateScopeObject.put("$gte", info.getRunTimeBegin());
            }
            if (info.getRunTimeEnd() != null) {
                dateScopeObject.put("$lte", info.getRunTimeEnd());
            }
            queryObject.put(RunInfo.RUN_TIME, dateScopeObject);
        }
        queryObject.put(RunInfo.EQUI_KEY, info.getEquiId());
        //查询数量
        long count = dataDocument.countDocuments(queryObject);
        Integer rowsCount = Long.valueOf(count).intValue();
        PageManagerEx<Map<String, Object>> page_ = new PageManagerEx<Map<String, Object>>();
        page_.setTotalCount(rowsCount);
        if (page.getPageRows() == -1) {
            page.setPageRows(Integer.MAX_VALUE);
        }
        // 计算页数 page.getRows()获得每页显示条数，系统中固定值
        int pageCount = rowsCount / page.getPageRows();
        // 分页数据查询开始锚点值
        int pageStart = pageCount;
        if (rowsCount % page.getPageRows() > 0) {
            pageCount++;
        }
        // 如果传过来的当前页码大于总页码 则把当前页码设置为最大页码
        if (page.getPage() > pageCount) {
            page.setPage(pageCount);
        } else if (page.getPage() == pageCount && pageCount != 0) {
            page.setPage(pageCount - 1);
            pageStart = page.getPage();
        } else {
            pageStart = page.getPage();
        }

        // 将页面传过来的当前页传回到前台
        if (rowsCount % page.getPageRows() == 0 && page.getPage() == pageCount) {
            page_.setPage(page.getPage());
        } else {
            page_.setPage(page.getPage() + 1);
        }

        FindIterable<Document> dataSet = dataDocument.find(queryObject).sort(new Document().append(RunInfo.RUN_TIME, -1)).skip((pageStart) * page.getPageRows()).limit(page.getPageRows());
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataSet.forEach(new Consumer<Document>() {
            @Override
            public void accept(Document doc) {
                Map<String, Object> line = new HashMap<String, Object>();
                doc.forEach((key, value) -> {
                    line.put(key, value);
                });
                dataList.add(line);
            }

        });
        if (dataList.size() > 0) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 转换显示的值
            List<Map<String, Object>> newList = new ArrayList<>();
            Map<String, Object> newLine;
            for (Map<String, Object> line : dataList) {
                newLine = new HashMap<>();
                Object value;
                for (Map.Entry<String, Object> item : line.entrySet()) {
                    value = item.getValue();
                    if (value != null) {
                        if (value instanceof java.util.Date) {
                            newLine.put(item.getKey(), f.format((java.util.Date) value));
                        } else {
                            newLine.put(item.getKey(), value);
                        }
                    } else {
                        newLine.put(item.getKey(), "");
                    }

                }
                newList.add(newLine);
            }
            page_.setData(newList);
        } else {
            page_.setData(dataList);
        }
        page_.setTotalPages(pageCount);
        page_.setsEcho(page.getsEcho());
        return page_;

    }


}

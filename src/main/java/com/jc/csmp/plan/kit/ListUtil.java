package com.jc.csmp.plan.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class ListUtil {

    /**
     * 分组
     *
     * @param dataList
     * @return
     */
    public static <T> String join(Collection<T> dataList) {
        StringBuilder s = new StringBuilder();
        for (T item : dataList) {
            s.append(",").append(item);
        }
        return s.substring(1);
    }

    /**
     * 分组
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, V> MapList<T, V> getMapList(final Collection<V> list, Function<? super V, ? extends T> keyFun) {
        MapList<T, V> dataMap = new MapList<T, V>();
        if (list == null || list.size() == 0) {
            return dataMap;
        }
        // 取得第一个
        V obj = null;
        for (V e : list) {
            obj = e;
            break;
        }

        Object keyValue;
        for (V item : list) {
            try {
                keyValue = keyFun.apply(item);
                dataMap.put((T) keyValue, item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return dataMap;
    }


    /**
     * 数据（列表）拆分，按照指定大小
     *
     * @param datas 数据
     * @param size  等分每组大小
     * @return 拆分后集合
     */
    public static <T> List<List<T>> split(List<T> datas, int size) {
        return split(datas, size, size);
    }

    /**
     * 数据（列表）拆分，按照指定大小
     *
     * @param datas     数据
     * @param firstSize 第一组大小
     * @param size      以后各组大小
     * @return 拆分后集合
     */
    public static <T> List<List<T>> split(List<T> datas, int firstSize, int size) {
        List<List<T>> resList = new ArrayList<List<T>>();
        if (datas == null || datas.size() == 0) {
            return resList;
        }
        boolean begin = true;
        List<T> rowList = new ArrayList<T>();
        for (int i = 0; i < datas.size(); i++) {
            if (begin) {
                if (rowList.size() == firstSize) {
                    resList.add(rowList);
                    rowList = new ArrayList<T>();
                    begin = false;
                }
            } else {
                if (rowList.size() == size) {
                    resList.add(rowList);
                    rowList = new ArrayList<T>();
                }
            }
            rowList.add(datas.get(i));
        }
        if (rowList.size() > 0) {
            resList.add(rowList);
        }
        return resList;
    }

}

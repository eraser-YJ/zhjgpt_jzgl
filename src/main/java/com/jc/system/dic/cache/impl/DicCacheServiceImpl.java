package com.jc.system.dic.cache.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.cache.IDicCacheService;
import com.jc.system.dic.cache.init.InitDicType;
import com.jc.system.dic.cache.refresh.DicRefreshManage;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;
import com.jc.system.dic.service.IDicService;
import com.jc.system.dic.service.IDicTypeService;

import java.util.*;

import com.jc.system.security.util.UserUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DicCacheServiceImpl implements IDicCacheService {
    private static final Logger logger = Logger.getLogger(DicCacheServiceImpl.class);

    IDicService service = SpringContextHolder.getBean(IDicService.class);
    IDicTypeService typeService = SpringContextHolder.getBean(IDicTypeService.class);

    static CacheDate cacheData = new CacheDate();
    private static DicCacheServiceImpl cacheService = null;

    private DicCacheServiceImpl() {
    }

    public static DicCacheServiceImpl getInstance() {
        if (cacheService == null) {
            cacheService = new DicCacheServiceImpl();
        }
        return cacheService;
    }

    @Override
    public void init() {
        logger.info("开始初始化数据字典");
        List<Dic> list = service.query(new Dic());
        InitDicType initType = new InitDicType();
        initType.init(list, cacheData);
        logger.info("初始化数据字典完成");
    }

    /**
     * @param typeCode 字典类型码
     * @description 根据字典类型获得标志可以使用的字典
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Dic> getDicsByTypeCode(String typeCode, String parentCode) {
        if (StringUtil.isEmpty(typeCode) || StringUtil.isEmpty(parentCode)) {
            return null;
        }
        String dicMapStr = CacheClient.getCache("dicMap");
        Map<String, Map<String, Object>> dicMap;
        if (dicMapStr == null) {
            dicMap = new HashMap<>();
        } else {
            dicMap = (Map<String, Map<String, Object>>) JsonUtil.json2Java(dicMapStr, Map.class);
        }
        Map<String, Object> map = dicMap.get(typeCode + "=" + parentCode);
        if (map == null) {
            //先从数据库中获得此类型码的字典类型，如果存在则刷新缓存，如果不存在则返回null
            DicType dicType = new DicType();
            dicType.setCode(typeCode);
            dicType.setParentId(parentCode);
            dicType = typeService.get(dicType);
            if (dicType == null) {
                //数据库中没有该字典类型，则为调用错误，直接返回null
                return null;
            }
            Dic dic = new Dic();
            dic.setParentType(typeCode + "=" + parentCode);
            dic.addOrderByField("code");
            List<Dic> list = service.query(dic);
            Map<String, Object> newMap = new LinkedHashMap<String, Object>();
            for (Dic item : list) {
                newMap.put(item.getCode(), item);
            }
            dicMap.put(typeCode, newMap);
            CacheClient.putCache("dicMap", JsonUtil.java2Json(dicMap));
            map = newMap;
        }
        Collection<Object> collection = map.values();
        Iterator<Object> itor = collection.iterator();
        List<Dic> result = new ArrayList<Dic>();
        while (itor.hasNext()) {
            Object item = itor.next();
            Dic dicItem = (Dic) JsonUtil.json2Java(JsonUtil.java2Json(item), Dic.class);
            if (Dic.USE_FLAG_TRUE == dicItem.getUseFlag().intValue()) {
                result.add(dicItem);
            }
        }
        return result;
    }

    /**
     * @param typeCode 字典类型码
     * @description 根据字典类型获得（包括标志不可用）字典
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Dic> getDicsByTypeCodeAll(String typeCode, String parentCode) {
        if (StringUtil.isEmpty(typeCode) || StringUtil.isEmpty(parentCode)) {
            return null;
        }
        String dicMapStr = CacheClient.getCache("dicMap");
        Map<String, Map<String, Object>> dicMap = null;
        if (dicMapStr == null) {
            dicMap = new HashMap<String, Map<String, Object>>();
        } else {
            dicMap = (Map<String, Map<String, Object>>) JsonUtil.json2Java(dicMapStr, Map.class);
        }
        Map<String, Object> map = dicMap.get(typeCode + "=" + parentCode);
        if (map == null) {
            //先从数据库中获得此类型码的字典类型，如果存在则刷新缓存，如果不存在则返回null
            DicType dicType = new DicType();
            dicType.setCode(typeCode);
            dicType.setParentId(parentCode);
            dicType = typeService.get(dicType);
            if (dicType == null) {
                //数据库中没有该字典类型，则为调用错误，直接返回null
                return null;
            }
            Dic dic = new Dic();
            dic.setParentId(typeCode);
            List<Dic> list = service.query(dic);
            Map<String, Object> newMap = new HashMap<String, Object>();
            for (Dic item : list) {
                newMap.put(item.getCode(), item);
            }
            dicMap.put(typeCode, newMap);
            CacheClient.putCache("dicMap", JsonUtil.java2Json(dicMap));
            map = newMap;
        }
        List<Dic> result = converMapToDicList(map);
        Collections.sort(result, new Comparator<Dic>() {
            @Override
            public int compare(Dic a, Dic b) {
                //第一次比较行标
                int i = a.getCode().compareTo(b.getCode());
                return i;
            }
        });
        return result;
    }

    /**
     * @param typeCode 字典类型code
     * @param dicCode  字典code
     * @description 获得字典
     */
    @Override
    @SuppressWarnings("unchecked")
    public Dic getDic(String typeCode, String parentCode, String dicCode) {
        String parentType = typeCode + "=" + parentCode;
        String userType = parentType + "=" + dicCode;
        if (UserUtils.getUserDic(userType) != null) {
            Dic dic = (Dic)UserUtils.getUserDic(userType);
            return dic;
        }
        String dicMapStr = CacheClient.getCache("dicMap");
        Map<String, Map<String, Object>> dicMap = null;
        if (dicMapStr == null) {
            dicMap = new HashMap<String, Map<String, Object>>();
        } else {
            dicMap = (Map<String, Map<String, Object>>) JsonUtil.json2Java(dicMapStr, Map.class);
        }

        Map<String, Object> map = dicMap.get(parentType);
        if (map == null) {
            //先从数据库中获得此字典码的字典，如果存在则刷新缓存，如果不存在则返回null
            Dic dic = new Dic();
            dic.setCode(dicCode);
            dic.setParentId(typeCode);
            dic.setParentType(parentType);
            dic = service.get(dic);
            if (dic == null) {
                //数据库中没有该字典，则为调用错误，直接返回null
                return null;
            }
            dic = new Dic();
            dic.setParentId(typeCode);
            dic.setParentType(parentType);
            List<Dic> list = service.query(dic);
            Map<String, Object> newMap = new HashMap<String, Object>();
            for (Dic item : list) {
                newMap.put(item.getCode(), item.convertToMap());
            }
            dicMap.put(typeCode, newMap);
            CacheClient.putCache("dicMap", JsonUtil.java2Json(dicMap));
            map = newMap;
        }
        Map<String, Object> newDicMap = (Map<String, Object>) map.get(dicCode);
        if (newDicMap == null) {
            logger.debug("类型值为" + typeCode + ",字典值为" + dicCode + "的字典不存在");
            return null;
        }
        Dic dic = (Dic) JsonUtil.json2Java(JsonUtil.java2Json(newDicMap), Dic.class);
        if (UserUtils.getUserDic(userType) == null) {
            UserUtils.setUserDic(userType, dic);
        }
        return dic;
    }


    /**
     * @return 树形结构（子节点放入到children中）
     * @description 获得所有类型
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DicType> getTypes() {
        List<DicType> list = new ArrayList<DicType>();
        try {
            String dicTypeMapStr = CacheClient.getCache("dicTypeMap");
            Map<String, Object> typeMap = null;
            if (StringUtil.isEmpty(dicTypeMapStr)) {
                typeMap = new HashMap<String, Object>();
                DicType dicType = new DicType();
                List<DicType> typeList = typeService.query(dicType);
                for (DicType item : typeList) {
                    typeMap.put(item.getCode() + "=" + item.getParentId(), item);
                }
                CacheClient.putCache("dicTypeMap", JsonUtil.java2Json(typeMap));
            } else {
                typeMap = (Map<String, Object>) JsonUtil.json2Java(dicTypeMapStr, Map.class);
            }
            List<DicType> typeList = converMapToDicTypeList(typeMap);
            for (DicType type : typeList) {
                if ("-1".equals(type.getParentId())) {
                    addTypeChildren(type, typeList);
                    list.add(type);
                }
            }
        } catch (CustomException e) {
            logger.error(e);
        }
        return list;
    }

    /**
     * @description 获得自定义的字典类型
     */
    @Override
    @SuppressWarnings("unchecked")
    public DicType getType(String code, String parentCode) {
        String dicTypeMapStr = CacheClient.getCache("dicTypeMap");
        Map<String, Object> typeMap = null;
        try {
            if (StringUtil.isEmpty(dicTypeMapStr)) {
                //先从数据库中获得此类型码的字典类型，如果存在则刷新缓存，如果不存在则返回null
                DicType dicType = new DicType();
                dicType.setCode(code);
                dicType.setParentId(parentCode);
                dicType = typeService.get(dicType);
                if (dicType == null) {
                    //数据库中没有该字典类型，则为调用错误，直接返回null
                    return null;
                }
                typeMap = new HashMap<String, Object>();
                dicType = new DicType();
                List<DicType> typeList = typeService.query(dicType);
                for (DicType item : typeList) {
                    typeMap.put(item.getCode() + "=" + item.getParentId(), item);
                }
                CacheClient.putCache("dicTypeMap", JsonUtil.java2Json(typeMap));
            } else {
                typeMap = (Map<String, Object>) JsonUtil.json2Java(dicTypeMapStr, Map.class);
            }
        } catch (CustomException e) {
            logger.error(e);
            return new DicType();
        }
        String parentType = code + "=" + parentCode;
        if (typeMap.get(parentType) == null) {
            return null;
        }
        return (DicType) JsonUtil.json2Java(JsonUtil.java2Json(typeMap.get(parentType)), DicType.class);
    }

    /**
     * @description 把type的子节点增加到children中
     */
    private void addTypeChildren(DicType type, List<DicType> typeList) {
        String parentCode = type.getCode();
        for (DicType item : typeList) {
            if (item.getParentId().equals(parentCode)) {
                type.addChildren(item);
                addTypeChildren(item, typeList);
            }
        }
    }

    /**
     * @description 刷新缓存（项目和类型）
     */
    @Override
    public void refreshDicItem(Dic dic, Dic oldDic) {
        new DicRefreshManage().refresh(dic, oldDic, cacheData);
    }

    /**
     * @param map value为object类型
     * @description: 将map中value转换为dic的list
     */
    private List<Dic> converMapToDicList(Map<String, Object> map) {
        List<Dic> result = new ArrayList<Dic>();
        Collection<Object> values = map.values();
        for (Object item : values) {
            Dic dic = (Dic) JsonUtil.json2Java(JsonUtil.java2Json(item), Dic.class);
            result.add(dic);
        }
        return result;
    }

    /**
     * @param map value为object类型
     * @description: 将map中value转换为dicType的list
     */
    private List<DicType> converMapToDicTypeList(Map<String, Object> map) {
        List<DicType> result = new ArrayList<DicType>();
        Collection<Object> values = map.values();
        for (Object item : values) {
            DicType dicType = (DicType) JsonUtil.json2Java(JsonUtil.java2Json(item), DicType.class);
            result.add(dicType);
        }
        return result;
    }
}

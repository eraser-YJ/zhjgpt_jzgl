package com.jc.csmp.dic.util;

import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.service.ICmCustomDicService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.common.util.CacheUtils;

/**
 * 自定义字段缓存
 * @Author 常鹏
 * @Date 2020/7/9 13:26
 * @Version 1.0
 */
public class CmCustomDicCacheUtil {
    private CmCustomDicCacheUtil() {
        throw new IllegalStateException("CmCustomDicCacheUtil class");
    }

    private static final String CACHEKEY = "cm_custom_dic_";

    private static ICmCustomDicService cmCustomDicService = SpringContextHolder.getBean(ICmCustomDicService.class);


    public static CmCustomDic getById(String id)  {
        if (StringUtil.isEmpty(id) || id.equals("0")) {
            return null;
        }
        String key = CACHEKEY + id;
        CmCustomDic dic = (CmCustomDic) CacheUtils.get(key);
        if (dic == null) {
            dic = cmCustomDicService.getById(id);
            if(dic != null){
                CacheUtils.put(key, dic);
            }
        }
        return dic;
    }

    public static void delete(String id) {
        String key = CACHEKEY + id;
        CacheUtils.remove(key);
    }

    public static void add(CmCustomDic entity) {
        String key = CACHEKEY + entity.getId();
        CacheUtils.put(key, entity);
    }

    public static void update(String id) {
        String key = CACHEKEY + id;
        CmCustomDic dic = cmCustomDicService.getById(id);
        if(dic != null){
            CacheUtils.put(key, dic);
        }
    }
}

package com.jc.common.db;

import com.jc.common.def.DefVO;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;

import java.util.List;
import java.util.Map;

public class BusiService {


    public static List<Map<String, Object>> query(DefVO object, Map<String, Object> paraMap) throws CustomException {
        // 创建连接
        BusiDao dao = BusiDao.getInstance();
        return dao.queryForList(object.getQuerySql(), paraMap);

    }

    public static PageManagerEx<Map<String, Object>> queryByPage(DefVO object, Map<String, Object> paraMap, PageManager inPage) throws CustomException {
        // 创建连接
        BusiDao dao = BusiDao.getInstance();
        return dao.queryByPage(object.getQuerySql(), paraMap, inPage);
    }

}

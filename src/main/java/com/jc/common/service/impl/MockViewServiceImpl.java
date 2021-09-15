package com.jc.common.service.impl;

import com.jc.common.db.BusiService;
import com.jc.common.def.DefVO;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.common.service.IMockViewService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Service
public class MockViewServiceImpl implements IMockViewService {


    public MockViewServiceImpl() {
    }


    @Override
    public Map<String, Object> get(Map<String, Object> entity, DefVO def) throws CustomException {
        List<Map<String, Object>> dataList = BusiService.query(def, entity);
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getList(Map<String, Object> entity, DefVO def) throws CustomException {
        List<Map<String, Object>> dataList = BusiService.query(def, entity);
        return dataList;
    }

    @Override
    public PageManagerEx<Map<String, Object>> query(Map<String, Object> entity, DefVO def, PageManager page) throws CustomException {
        return BusiService.queryByPage(def, entity, page);
    }

}


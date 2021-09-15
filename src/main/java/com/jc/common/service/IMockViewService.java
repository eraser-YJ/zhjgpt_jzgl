package com.jc.common.service;

import com.jc.common.def.DefVO;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
public interface IMockViewService {

    /**
     * 单挑查询
     *
     * @param entity
     * @param def
     * @return
     * @throws CustomException
     */
    Map<String, Object> get(Map<String, Object> entity, DefVO def) throws CustomException;
    /**
     * 多挑查询
     *
     * @param entity
     * @param def
     * @return
     * @throws CustomException
     */
    List<Map<String, Object>> getList(Map<String, Object> entity, DefVO def) throws CustomException;
    /**
     * 分页查询
     * @param entity
     * @param def
     * @param page
     * @return
     * @throws CustomException
     */
    PageManagerEx<Map<String, Object>> query(Map<String, Object> entity, DefVO def, PageManager page) throws CustomException;
}

package com.jc.csmp.warn.rule.service;

import com.jc.csmp.warn.rule.domain.WarnRule;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IWarnRuleService extends IBaseService<WarnRule> {
    /**
     * 根据主键删除多条记录方法
     *
     * @return
     * @throws CustomException
     */
    WarnRule getByCode(String code) throws CustomException;


    /**
     * 根据主键删除多条记录方法
     *
     * @param entity
     * @return
     * @throws CustomException
     */
    Integer deleteByIds(WarnRule entity) throws CustomException;

    /**
     * 保存方法
     *
     * @param entity
     * @return
     * @throws CustomException
     */
    Result saveEntity(WarnRule entity) throws CustomException;

    /**
     * 修改方法
     *
     * @param entity
     * @return
     * @throws CustomException
     */
    Result updateEntity(WarnRule entity) throws CustomException;
}

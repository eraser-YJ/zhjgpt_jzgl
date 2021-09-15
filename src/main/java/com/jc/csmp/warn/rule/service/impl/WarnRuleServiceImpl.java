package com.jc.csmp.warn.rule.service.impl;

import com.jc.csmp.common.tool.RuleContext;
import com.jc.csmp.warn.rule.domain.WarnRule;
import com.jc.csmp.warn.rule.dao.IWarnRuleDao;
import com.jc.csmp.warn.rule.domain.WarnRule;
import com.jc.csmp.warn.rule.service.IWarnRuleService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Version 1.0
 */
@Service
public class WarnRuleServiceImpl extends BaseServiceImpl<WarnRule> implements IWarnRuleService {

    private IWarnRuleDao warnRuleDao;

    public WarnRuleServiceImpl() {
    }

    @Autowired
    public WarnRuleServiceImpl(IWarnRuleDao warnRuleDao) {
        super(warnRuleDao);
        this.warnRuleDao = warnRuleDao;
    }

    @Override
    public WarnRule getByCode(String code) throws CustomException {
        if (code == null || code.trim().length() <= 0) {
            return null;
        }
        WarnRule cond = new WarnRule();
        cond.setRuleCode(code);
        return warnRuleDao.get(cond);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(WarnRule entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = warnRuleDao.delete(entity);
            //规则清理
            RuleContext.removeAll();
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(WarnRule entity) throws CustomException {
        if (!StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            this.save(entity);
            return Result.success(MessageUtils.getMessage("JC_SYS_001"));
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result updateEntity(WarnRule entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            Integer flag = this.update(entity);
            //规则清理
            RuleContext.remove(entity.getRuleCode());
            if (flag == 1) {
                return Result.success(MessageUtils.getMessage("JC_SYS_003"));
            } else {
                return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

}


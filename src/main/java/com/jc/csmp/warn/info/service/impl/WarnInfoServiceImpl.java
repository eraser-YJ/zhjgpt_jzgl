package com.jc.csmp.warn.info.service.impl;

import com.jc.csmp.warn.info.common.WarnStatusEnum;
import com.jc.csmp.warn.info.dao.IWarnInfoDao;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.service.IWarnInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Version 1.0
 */
@Service
public class WarnInfoServiceImpl extends BaseServiceImpl<WarnInfo> implements IWarnInfoService {

    private IWarnInfoDao warnInfoDao;

    public WarnInfoServiceImpl() {
    }

    @Autowired
    public WarnInfoServiceImpl(IWarnInfoDao warnInfoDao) {
        super(warnInfoDao);
        this.warnInfoDao = warnInfoDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(WarnInfo entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = warnInfoDao.delete(entity);
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(WarnInfo entity) throws CustomException {
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
    public Result updateEntity(WarnInfo entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            Integer flag = this.update(entity);
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

    @Override
    public Result updateResult(WarnInfo entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            entity.setWarnStatus(WarnStatusEnum.processed.toString());
            entity.setProcessTime(new java.util.Date());
            User user = SystemSecurityUtils.getUser();
            if (user != null) {
                entity.setProcessUser(user.getId());
                entity.setProcessUserName(user.getDisplayName());
            }
            Integer flag = warnInfoDao.updateResult(entity);
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


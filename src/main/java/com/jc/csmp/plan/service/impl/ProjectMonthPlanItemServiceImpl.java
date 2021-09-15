package com.jc.csmp.plan.service.impl;

import com.jc.csmp.plan.dao.IProjectMonthPlanItemDao;
import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.service.IProjectMonthPlanItemService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Version 1.0
 */
@Service
public class ProjectMonthPlanItemServiceImpl extends BaseServiceImpl<ProjectMonthPlanItem> implements IProjectMonthPlanItemService {

    private IProjectMonthPlanItemDao projectMonthPlanItemDao;

    public ProjectMonthPlanItemServiceImpl() {
    }

    @Autowired
    public ProjectMonthPlanItemServiceImpl(IProjectMonthPlanItemDao projectMonthPlanItemDao) {
        super(projectMonthPlanItemDao);
        this.projectMonthPlanItemDao = projectMonthPlanItemDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(ProjectMonthPlanItem entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = projectMonthPlanItemDao.delete(entity, false);
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(ProjectMonthPlanItem entity) throws CustomException {
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
    public Result updateEntity(ProjectMonthPlanItem entity) throws CustomException {
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
    public void deleteByHeadId(String headId) throws CustomException {
        ProjectMonthPlanItem entity = new ProjectMonthPlanItem();
        entity.setHeadId(headId);
        projectMonthPlanItemDao.delete(entity, false);
    }


    @Override
    public List<Attach> getAttachList(ProjectMonthPlanItem cond) {
        return projectMonthPlanItemDao.getAttachList(cond);
    }

    @Override
    public ProjectMonthPlanItem queryNewXxdj(ProjectMonthPlanItem cond) {
        return projectMonthPlanItemDao.queryNewXxdj(cond);
    }

    @Override
    public List<ProjectMonthPlanItem> queryNewQuestion(ProjectMonthPlanItem cond) {
        return projectMonthPlanItemDao.queryNewQuestion(cond);
    }

    @Override
    public ProjectMonthPlanItem queryNewNum(ProjectMonthPlanItem cond) {
        return projectMonthPlanItemDao.queryNewNum(cond);
    }
}


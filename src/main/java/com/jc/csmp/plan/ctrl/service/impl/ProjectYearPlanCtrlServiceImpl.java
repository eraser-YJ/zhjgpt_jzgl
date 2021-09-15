package com.jc.csmp.plan.ctrl.service.impl;

import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.csmp.plan.ctrl.dao.IProjectYearPlanCtrlDao;
import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.csmp.plan.ctrl.service.IProjectYearPlanCtrlService;
import com.jc.csmp.plan.kit.ReqType;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @Version 1.0
 */
@Service
public class ProjectYearPlanCtrlServiceImpl extends BaseServiceImpl<ProjectYearPlanCtrl> implements IProjectYearPlanCtrlService {

    private IProjectYearPlanCtrlDao projectYearPlanCtrlDao;

    public ProjectYearPlanCtrlServiceImpl() {
    }

    @Autowired
    public ProjectYearPlanCtrlServiceImpl(IProjectYearPlanCtrlDao projectYearPlanCtrlDao) {
        super(projectYearPlanCtrlDao);
        this.projectYearPlanCtrlDao = projectYearPlanCtrlDao;
    }

    public ProjectYearPlanCtrl getReqCrtl(int year) {
        ProjectYearPlanCtrl cond = new ProjectYearPlanCtrl();
        cond.setPlanYear(year);
        cond.setSeqno(ReqType.CODE_1.getCode());
        cond.addOrderByField("t.request_start_date");
        List<ProjectYearPlanCtrl> ctrlList = projectYearPlanCtrlDao.queryAll(cond);
        if (ctrlList != null && ctrlList.size() > 0) {
            ProjectYearPlanCtrl item = ctrlList.get(0);
            Long now = System.currentTimeMillis();
            if (item.getRequestStartDate().getTime() <= now && item.getRequestEndDate().getTime() >= now) {
                return item;
            }
        }
        return null;
    }
    public ProjectYearPlanCtrl getChangeCrtl(int year) {
        ProjectYearPlanCtrl cond = new ProjectYearPlanCtrl();
        cond.setSeqno(ReqType.CODE_2.getCode());
        cond.setPlanYear(year);
        cond.addOrderByField("t.request_start_date");
        List<ProjectYearPlanCtrl> ctrlList = projectYearPlanCtrlDao.queryAll(cond);
        if (ctrlList != null && ctrlList.size() > 0) {
            ProjectYearPlanCtrl item = ctrlList.get(0);
            Long now = System.currentTimeMillis();
            if (item.getRequestStartDate().getTime() <= now && item.getRequestEndDate().getTime() >= now) {
                return item;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(ProjectYearPlanCtrl entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = projectYearPlanCtrlDao.delete(entity,false);
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(ProjectYearPlanCtrl entity) throws CustomException {
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
    public Result updateEntity(ProjectYearPlanCtrl entity) throws CustomException {
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

}


package com.jc.csmp.plan.service.impl;

import com.jc.csmp.plan.dao.IProjectMonthPlanDao;
import com.jc.csmp.plan.domain.ProjectMonthPlan;
import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.kit.ListUtil;
import com.jc.csmp.plan.service.IProjectMonthPlanItemService;
import com.jc.csmp.plan.service.IProjectMonthPlanService;
import com.jc.csmp.plan.service.IProjectYearPlanItemService;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Service

public class ProjectMonthPlanServiceImpl extends BaseServiceImpl<ProjectMonthPlan> implements IProjectMonthPlanService {

    private IProjectMonthPlanDao projectMonthPlanDao;
    @Autowired
    private IProjectMonthPlanItemService projectMonthPlanItemService;
    @Autowired
    private IProjectYearPlanItemService projectYearPlanItemService;

    public ProjectMonthPlanServiceImpl() {
    }

    @Autowired
    public ProjectMonthPlanServiceImpl(IProjectMonthPlanDao projectMonthPlanDao) {
        super(projectMonthPlanDao);
        this.projectMonthPlanDao = projectMonthPlanDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(ProjectMonthPlan entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = projectMonthPlanDao.delete(entity, false);
            String[] headIds = entity.getPrimaryKeys();
            if (headIds != null) {
                //父表不会批量删除，删子表一条一条删就可以了
                for (String headId : headIds) {
                    //删除子表
                    projectMonthPlanItemService.deleteByHeadId(headId);
                }
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    /**
     * 更新
     *
     * @param month
     * @param itemList
     * @throws CustomException
     */
    private void updateYearItem(ProjectMonthPlan month, List<ProjectMonthPlanItem> itemList) throws CustomException {
        Map<String, String> map = new HashMap<>();
        for(ProjectMonthPlanItem item:itemList){
            if (item.getProjectId() != null && item.getProjectId().trim().length() > 0) {
                map.put(item.getProjectName(), item.getProjectId());
            }
        }
        ProjectYearPlanItem cond = new ProjectYearPlanItem();
        cond.setQueryYear(month.getPlanYear());
        cond.setQueryAreaCode(month.getPlanAreaCode());
        cond.setEmptyProjectId("Y");
        List<ProjectYearPlanItem> dataList = projectYearPlanItemService.queryAll(cond);
        if (dataList != null) {
            List<ProjectYearPlanItem> newDataList = new ArrayList<>();
            String projectId;
            for (ProjectYearPlanItem item : dataList) {
                projectId = map.get(item.getProjectName());
                if (projectId != null && projectId.trim().length() > 0) {
                    ProjectYearPlanItem nvo = new ProjectYearPlanItem();
                    nvo.setId(item.getId());
                    nvo.setModifyDate(item.getModifyDate());
                    nvo.setProjectId(projectId);
                    try {
                        projectYearPlanItemService.update(nvo);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(ProjectMonthPlan entity) throws CustomException {
        long start = System.currentTimeMillis();
        try {
            entity.setId(null);
            this.save(entity);

            //子表操作
            if ("Y".equalsIgnoreCase(entity.getItemChange())) {
                //删除子表
                projectMonthPlanItemService.deleteByHeadId(entity.getId());
                //保存子表
                String content = entity.getItemListContent();
                if (content != null && content.trim().length() > 0) {
                    List<ProjectMonthPlanItem> itemList = JsonUtil.json2Array(content, ProjectMonthPlanItem.class);
                    if (itemList != null && itemList.size() > 0) {
                        int index = 0;
                        for (ProjectMonthPlanItem item : itemList) {
                            item.setHeadId(entity.getId());
                            item.setId(null);
                            //前台有编码再重算
                            item.setExtNum1(BigDecimal.valueOf(index++));
                        }
                        //批量保存
                        List<List<ProjectMonthPlanItem>> itemSaveList = ListUtil.split(itemList, 300);
                        for (List<ProjectMonthPlanItem> itemSaveData : itemSaveList) {
                            projectMonthPlanItemService.saveList(itemSaveData);
                        }
                        //更新年度关联
                        updateYearItem(entity, itemList);
                    }
                }
            }

            return Result.success(MessageUtils.getMessage("JC_SYS_001"));
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        } finally {
            System.out.println("ProjectMonthPlan saveEntity 耗时：" + (System.currentTimeMillis() - start));
            log.debug("ProjectMonthPlan saveEntity 耗时：" + (System.currentTimeMillis() - start));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result updateEntity(ProjectMonthPlan entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        long start = System.currentTimeMillis();
        try {
            Integer flag = this.update(entity);

            //子表操作
            if ("Y".equalsIgnoreCase(entity.getItemChange())) {
                //删除子表
                projectMonthPlanItemService.deleteByHeadId(entity.getId());
                //保存子表
                String content = entity.getItemListContent();
                if (content != null && content.trim().length() > 0) {
                    List<ProjectMonthPlanItem> itemList = JsonUtil.json2Array(content, ProjectMonthPlanItem.class);
                    if (itemList != null && itemList.size() > 0) {
                        int index = 0;
                        for (ProjectMonthPlanItem item : itemList) {
                            item.setHeadId(entity.getId());
                            item.setId(null);
                            //前台有编码再重算
                            item.setExtNum1(BigDecimal.valueOf(index++));
                        }
                        //批量保存
                        List<List<ProjectMonthPlanItem>> itemSaveList = ListUtil.split(itemList, 300);
                        for (List<ProjectMonthPlanItem> itemSaveData : itemSaveList) {
                            projectMonthPlanItemService.saveList(itemSaveData);
                        }
                        //更新年度关联
                        updateYearItem(entity, itemList);
                    }
                }
            }

            if (flag == 1) {
                return Result.success(MessageUtils.getMessage("JC_SYS_003"));
            } else {
                return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        } finally {
            System.out.println("ProjectMonthPlan updateEntity 耗时：" + (System.currentTimeMillis() - start));
            log.debug("ProjectMonthPlan updateEntity 耗时：" + (System.currentTimeMillis() - start));
        }
    }

}


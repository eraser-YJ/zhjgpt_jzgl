package com.jc.csmp.plan.service.impl;

import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.plan.dao.IProjectYearPlanDao;
import com.jc.csmp.plan.domain.*;
import com.jc.csmp.plan.kit.ListUtil;
import com.jc.csmp.plan.service.IProjectYearPlanItemService;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.dic.domain.Dic;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 2020-07-09
 * @title
 */
@Service
public class ProjectYearPlanServiceImpl extends BaseServiceImpl<ProjectYearPlan> implements IProjectYearPlanService {

    @Autowired
    private IInstanceService instanceService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IProjectYearPlanItemService projectYearPlanItemService;

    private IProjectYearPlanDao projectPlanDao;

    public ProjectYearPlanServiceImpl() {
    }

    @Autowired
    public ProjectYearPlanServiceImpl(IProjectYearPlanDao projectPlanDao) {
        super(projectPlanDao);
        this.projectPlanDao = projectPlanDao;
    }

    /**
     * @return Integer 增加的记录数
     * @description 发起流程方法
     * @author
     * @version 2020-07-09
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer saveWorkflow(ProjectYearPlan projectPlan) throws CustomException {
        long start = System.currentTimeMillis();
        try {
            projectPlan.setPiId(projectPlan.getWorkflowBean().getBusiness_Key_());
            //save 暂存，submit 是提交
            if (!"SAVE".equalsIgnoreCase(projectPlan.getWorkflowBean().getSubmitType_())) {
                projectPlan.setPlanStatus(2);
            } else {
                projectPlan.setPlanStatus(1);
            }
            Integer result = 0;
            if (projectPlan.getModifyDate() == null) {
                propertyService.fillProperties(projectPlan, false);
                result = projectPlanDao.save(projectPlan);
            } else {
                propertyService.fillProperties(projectPlan, true);
                result = projectPlanDao.update(projectPlan);
            }
            //子表操作
            if ("Y".equalsIgnoreCase(projectPlan.getItemChange())) {
                //删除子表
                projectYearPlanItemService.deleteByHeadId(projectPlan.getId());
                //保存子表
                String content = projectPlan.getItemListContent();
                if (content != null && content.trim().length() > 0) {
                    List<ProjectYearPlanItem> itemList = JsonUtil.json2Array(content, ProjectYearPlanItem.class);
                    if (itemList != null && itemList.size() > 0) {
                        int index = 0;
                        for (ProjectYearPlanItem item : itemList) {
                            item.setHeadId(projectPlan.getId());
                            item.setId(null);
                            //前台有编码再重算
                            item.setExtNum1(BigDecimal.valueOf(index++));
                        }
                        //批量保存
                        List<List<ProjectYearPlanItem>> itemSaveList = ListUtil.split(itemList, 300);
                        for (List<ProjectYearPlanItem> itemSaveData : itemSaveList) {
                            projectYearPlanItemService.saveList(itemSaveData);
                        }
                    }
                }
            }
            //save 暂存，submit 是提交
            if ("SAVE".equalsIgnoreCase(projectPlan.getWorkflowBean().getSubmitType_())) {
                return result;
            }
            projectPlan.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.ndjh.toString());
            Map<String, Object> resultMap = instanceService.startProcess(projectPlan.getWorkflowBean());
            if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
                return Integer.parseInt(resultMap.get("code").toString());
            }
            return result;
        } finally {
            System.out.println("ProjectYearPlan saveWorkflow 耗时：" + (System.currentTimeMillis() - start));
            log.debug("ProjectYearPlan saveWorkflow 耗时：" + (System.currentTimeMillis() - start));
        }


    }

    /**
     * @return Integer 修改的记录数量
     * @description 修改方法
     * @author
     * @version 2020-07-09
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer updateWorkflow(ProjectYearPlan projectPlan) throws CustomException {
        long start = System.currentTimeMillis();
        try {
            propertyService.fillProperties(projectPlan, true);
            Integer result = projectPlanDao.update(projectPlan);
            if (!"SAVE".equalsIgnoreCase(projectPlan.getWorkflowBean().getSubmitType_())) {
                projectPlan.setPlanStatus(2);
            } else {
                projectPlan.setPlanStatus(1);
            }
            //子表操作
            if ("Y".equalsIgnoreCase(projectPlan.getItemChange())) {
                //删除子表
                projectYearPlanItemService.deleteByHeadId(projectPlan.getId());
                //保存子表
                String content = projectPlan.getItemListContent();
                if (content != null && content.trim().length() > 0) {
                    List<ProjectYearPlanItem> itemList = JsonUtil.json2Array(content, ProjectYearPlanItem.class);
                    if (itemList != null && itemList.size() > 0) {
                        int index = 0;
                        for (ProjectYearPlanItem item : itemList) {
                            item.setHeadId(projectPlan.getId());
                            item.setId(null);
                            item.setExtNum1(BigDecimal.valueOf(index++));
                        }
                        //批量保存
                        List<List<ProjectYearPlanItem>> itemSaveList = ListUtil.split(itemList, 300);
                        for (List<ProjectYearPlanItem> itemSaveData : itemSaveList) {
                            projectYearPlanItemService.saveList(itemSaveData);
                        }
                    }
                }
            }
            projectPlan.getWorkflowBean().setBusiness_Key_(projectPlan.getPiId());
            Map<String, Object> resultMap = taskService.excute(projectPlan.getWorkflowBean());
            if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
                return Integer.parseInt(resultMap.get("code").toString());
            }
            return result;
        } finally {
            System.out.println("ProjectYearPlan updateWorkflow 耗时：" + (System.currentTimeMillis() - start));
            log.debug("ProjectYearPlan updateWorkflow 耗时：" + (System.currentTimeMillis() - start));
        }

    }


    @Override
    public PageManager workFlowTodoQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return projectPlanDao.workFlowTodoQueryByPage(projectPlan, page);
    }


    @Override
    public PageManager workFlowDoneQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return projectPlanDao.workFlowDoneQueryByPage(projectPlan, page);
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return projectPlanDao.workFlowInstanceQueryByPage(projectPlan, page);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(ProjectYearPlan projectPlan) throws CustomException {

        Integer result = -1;
        try {
            propertyService.fillProperties(projectPlan, true);
            result = projectPlanDao.delete(projectPlan, false);
            String[] headIds = projectPlan.getPrimaryKeys();
            if (headIds != null) {
                //父表不会批量删除，删子表一条一条删就可以了
                for (String headId : headIds) {
                    //删除子表
                    projectYearPlanItemService.deleteByHeadId(headId);
                }
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(projectPlan);
            throw ce;
        }
        return result;

    }


    public List<Dic> selectPPTDic() {
        return projectPlanDao.selectPPTDic();
    }

    @Override
    public List<SuggestVO> querySuggest(ProjectYearPlan cond) {
        return projectPlanDao.querySuggest(cond);
    }

    public StatisItemVO queryStatisList(StatisVO cond) {
        return projectPlanDao.queryStatisList(cond);
    }

    @Override
    public Long queryCount(ProjectYearPlan cond) {
        return projectPlanDao.queryCount(cond);
    }

    @Override
    public ProjectYearPlan queryByPiid(String piid) {
        if (piid == null || piid.trim().length() <= 0) {
            return null;
        }
        ProjectYearPlan cond = new ProjectYearPlan();
        cond.setPiId(piid);
        return projectPlanDao.get(cond);
    }

    @Override
    public ProjectYearPlan queryById(String id) {
        if (id == null || id.trim().length() <= 0) {
            return null;
        }
        ProjectYearPlan cond = new ProjectYearPlan();
        cond.setId(id);
        return projectPlanDao.get(cond);
    }

    @Override
    public List<ProjectYearPlan> queryByYear(Integer year) {
        if (year == null ) {
            return null;
        }
        ProjectYearPlan cond = new ProjectYearPlan();
        cond.setPlanYear(year);
        return projectPlanDao.queryAll(cond);
    }
}
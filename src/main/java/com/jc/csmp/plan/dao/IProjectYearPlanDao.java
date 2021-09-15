package com.jc.csmp.plan.dao;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.domain.StatisItemVO;
import com.jc.csmp.plan.domain.StatisVO;
import com.jc.csmp.plan.domain.SuggestVO;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;
import com.jc.system.dic.domain.Dic;

import java.util.List;

/**
 * @author
 * @version 2020-07-09
 * @title 临时项目
 */

public interface IProjectYearPlanDao extends IBaseDao<ProjectYearPlan> {

    PageManager workFlowTodoQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    PageManager workFlowDoneQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    PageManager workFlowInstanceQueryByPage(ProjectYearPlan projectPlan, PageManager page);

    List<Dic> selectPPTDic();

    List<SuggestVO> querySuggest(ProjectYearPlan cond);

    StatisItemVO queryStatisList(StatisVO cond);

    Long queryCount(ProjectYearPlan cond);
}

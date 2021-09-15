package com.jc.csmp.plan.dao.impl;

import com.jc.csmp.plan.dao.IProjectYearPlanDao;
import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.domain.StatisItemVO;
import com.jc.csmp.plan.domain.StatisVO;
import com.jc.csmp.plan.domain.SuggestVO;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.DBException;
import com.jc.system.dic.domain.Dic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 2020-07-09
 * @title
 */
@Repository
public class ProjectYearPlanDaoImpl extends BaseClientDaoImpl<ProjectYearPlan> implements IProjectYearPlanDao {

    public ProjectYearPlanDaoImpl() {
    }

    public Integer save(ProjectYearPlan o) throws DBException {
        Integer result = -1;

        try {
            result = this.getTemplate().insert(this.getNameSpace(o) + "." + "insert", o);
            return result;
        } catch (Exception var5) {
            this.log.error(var5, var5);
            DBException exception = new DBException(var5);
            exception.setLogMsg("数据库添加数据发生错误");
            throw exception;
        }
    }


    @Override
    public PageManager workFlowTodoQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return queryByPage(projectPlan, page, "workflowTodoQueryCount", "workflowTodoQuery");
    }


    @Override
    public PageManager workFlowDoneQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return queryByPage(projectPlan, page, "workflowDoneQueryCount", "workflowDoneQuery");
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(ProjectYearPlan projectPlan, PageManager page) {
        return queryByPage(projectPlan, page, "workflowInstanceQueryCount", "workflowInstanceQuery");
    }

    public List<Dic> selectPPTDic() {
        return this.getTemplate().selectList(this.getNameSpace(new ProjectYearPlan()) + ".selectPPTDic");
    }

    public List<SuggestVO> querySuggest(ProjectYearPlan cond) {
        return this.getTemplate().selectList(this.getNameSpace(cond) + ".querySuggest", cond);
    }
    public StatisItemVO queryStatisList(StatisVO cond) {
        return this.getTemplate().selectOne( "ProjectYearPlanStatisDao.statisQuery", cond);
    }

    @Override
    public Long queryCount(ProjectYearPlan cond) {
        return this.getTemplate().selectOne(this.getNameSpace(cond) + "." + "queryCount", cond);
    }
}
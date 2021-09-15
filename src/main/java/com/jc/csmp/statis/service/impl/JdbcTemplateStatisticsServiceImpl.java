package com.jc.csmp.statis.service.impl;

import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.statis.service.IJdbcTemplateStatisticsService;
import com.jc.foundation.util.GlobalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 直接使用sql进行统计的service
 * @Author 常鹏
 * @Date 2020/8/3 13:34
 * @Version 1.0
 */
@Service
public class JdbcTemplateStatisticsServiceImpl implements IJdbcTemplateStatisticsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ICmProjectPlanService cmProjectPlanService;

    @Override
    public List<Map<String, Object>> projectQuestionByYear(Long year, String projectId) {
        StringBuffer sbuf = new StringBuffer("select ");
        sbuf.append("left(create_date, 7) as month, question_type as type, count(id) as cnum ");
        sbuf.append("from cm_project_question ");
        sbuf.append("where project_id = ? and left(create_date, 4) = ?");
        sbuf.append(" and audit_status = 'finish' and DELETE_FLAG = 0 ");
        sbuf.append(" group by question_type, left(create_date, 7) order by month asc ");
        List<Map<String, Object>> dbList = this.jdbcTemplate.queryForList(sbuf.toString(), new Object[]{ projectId, year });
        if (dbList == null || dbList.size() == 0) {
            return Collections.emptyList();
        }
        List<String> monthList = new ArrayList<>();
        Map<String, Map<String, Object>> resultMap = new HashMap<>(12);
        for (int i = 1; i <= 12; i++) {
            String key = year + "-" + (i < 10 ? "0" + i : i + "");
            monthList.add(key);
            Map<String, Object> result = new HashMap<>(4);
            result.put("month", key); result.put("quality", 0);
            result.put("safe", 0);  result.put("scene", 0);
            resultMap.put(key, result);
        }
        for (Map<String, Object> row : dbList) {
            Map<String, Object> result = resultMap.get(row.get("month"));
            result.put((String) row.get("type"), (row.get("cnum") == null ? 0 : row.get("cnum")));
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (String s : monthList) {
            resultList.add(resultMap.get(s));
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> projectPassTimePlan(String projectId) {
        StringBuffer sbuf = new StringBuffer("select ");
        sbuf.append("plan.plan_name planName, stage.stage_name stageName, plan.plan_end_date planDate, plan.actual_end_date actualDate ");
        sbuf.append(" , plan.plan_start_date planStartDate, plan.actual_start_date actualStartDate ");
        sbuf.append(" from cm_project_plan plan ");
        sbuf.append(" join cm_project_plan_stage stage on stage.id = plan.stage_id ");
        sbuf.append(" where plan.project_id = ? and plan.DELETE_FLAG = 0 ");
        sbuf.append(" and (");
        sbuf.append(" (plan.actual_end_date is null and plan.plan_end_date < now()) ");
        sbuf.append(" or ");
        sbuf.append(" (plan.actual_end_date is not null and plan.actual_end_date > plan.plan_end_date) ");
        sbuf.append(") order by plan.queue asc ");
        List<Map<String, Object>> dbList = this.jdbcTemplate.queryForList(sbuf.toString(), new Object[]{projectId});;
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (dbList != null) {
            for (Map<String, Object> row : dbList) {
                String planName = (String) row.get("planName");
                String stageName = (String) row.get("stageName");
                Date planDate = (Date) row.get("planDate");
                Date actualDate = (Date) row.get("actualDate");
                Map<String, Object> result = new HashMap<>(3);
                result.put("planName", planName);
                result.put("stageName", stageName);
                int day = 0;
                if (actualDate == null) {
                    day = GlobalUtil.differentDays(planDate, new Date(System.currentTimeMillis()));
                } else {
                    day = GlobalUtil.differentDays(planDate, actualDate);
                }
                if (day < 0) {
                     day = 0;
                }
                result.put("day", day);
                resultList.add(result);
            }
        }
        return resultList;
    }
}

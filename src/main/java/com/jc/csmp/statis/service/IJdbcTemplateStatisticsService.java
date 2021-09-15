package com.jc.csmp.statis.service;

import java.util.List;
import java.util.Map;

/**
 * @Author 常鹏
 * @Date 2020/8/3 13:34
 * @Version 1.0
 */
public interface IJdbcTemplateStatisticsService {
    /**
     * 根据年份统计每个月的问题数量
     * @param year
     * @param projectId
     * @return
     */
    List<Map<String, Object>> projectQuestionByYear(Long year, String projectId);

    /**
     * 获取项目超期的计划
     * @param projectId
     * @return
     */
    List<Map<String, Object>> projectPassTimePlan(String projectId);
}

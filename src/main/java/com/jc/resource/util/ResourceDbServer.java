package com.jc.resource.util;

import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.resource.enums.ResourceEnums;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取资源表的辅助类
 * @Author 常鹏
 * @Date 2020/8/13 11:00
 * @Version 1.0
 */
public class ResourceDbServer {
    /**项目通用唯一标识*/
    public static String COMMON_PROJECT_UNIQUE = "projectNumber";
    private static JdbcTemplate jdbcTemplate;
    public static ResourceDbServer instance;

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new ResourceDbServer();
            jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
        }
    }

    public static ResourceDbServer getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    /***
     * 根据项目编号查询参与人员
     * @param projectNumber
     * @return
     */
    public Long getPersonCountByProject(String projectNumber) {
        if (StringUtil.isEmpty(projectNumber)) {
            return 0L;
        }
        String sql = "select count(*) as cnum from " + ResourceTableEnums.pt_person_achievement.getTableName() + " where " + ResourceTableEnums.pt_person_achievement.getColumn(ResourceDbServer.COMMON_PROJECT_UNIQUE) + " = ?";
        return runCountSql(sql, new Object[]{ projectNumber });
    }

    private Long runCountSql(String sql, Object[] param) {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, param);
        if (resultList != null && resultList.size() > 0) {
            Map<String, Object> map = resultList.get(0);
            if (map != null) {
                return GlobalUtil.toLong(map.get("cnum") + "", 0L);
            }
        }
        return 0L;
    }

    public String getProjectName(String projectNumber) {
        String sql = "select projectName from " + ResourceEnums.pt_project_info.toString() + " where " + COMMON_PROJECT_UNIQUE + " = ?";
        if (!StringUtil.isEmpty(projectNumber)) {
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, new Object[]{projectNumber});
            if (resultList != null && resultList.size() > 0) {
                return (String)resultList.get(0).get("projectName");
            }
        }
        return "";
    }

    /**
     * 获取从业人员
     * @return
     */
    public Long getPersonCountByPtPersonInfo() {
        String sql = " SELECT COUNT(*) AS cnum FROM pt_person_info WHERE 1 = 1 AND person_jslx LIKE '%person_jslx_01%' AND dlh_data_src_ = 'pt_person_info' ";
        return runCountSql(sql, null);
    }

    public Long getPersonCountByPtPersonInfo(String companyId) {
        String sql = "select count(distinct person_cert_num) as cnum from pt_person_info where EXT_STR1 = ?";
        return runCountSql(sql, new Object[] { companyId });
    }

    /**
     * 获取企业数量
     * @return
     */
    public Long getCountByPtCompanyInfo() {
        return runCountSql("select count(*) as cnum from pt_company_info", null);
    }

    /**
     * 获取项目数量
     * @return
     */
    public Long getCountByPtProjectInfo() {
        return runCountSql("select count(*) cnum from " + ResourceEnums.pt_project_info.toString(), null);
    }

    public Long getCountByPtProjectInfoBuildDept(String buildDeptId) {
        return runCountSql("select count(*) cnum from " + ResourceEnums.pt_project_info.toString() + " where buildDeptId = ?", new Object[]{buildDeptId});
    }

    /**
     * 获取产值
     * @return
     */
    public Map<String, Double> getProductionTotalByProjectCate() {
        String sql = "select sum(projectMoney) as cnum, projectCate from pt_project_info group by projectCate";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        Double zhengfu = 0D, qiye = 0D;
        if (resultList != null && resultList.size() > 0) {
            for (Map<String, Object> map : resultList) {
                String key = (String) map.get("projectCate");
                if ("政府".equals(key)) {
                    zhengfu += GlobalUtil.toDouble(map.get("cnum") + "", 0D);
                } else {
                    qiye += GlobalUtil.toDouble(map.get("cnum") + "", 0D);
                }
            }
        }
        Map<String, Double> result = new HashMap<>(2);
        result.put("qiye", qiye);
        result.put("zhengfu", zhengfu);
        return result;
    }

    /**
     * 获取预警信息
     * @return
     */
    public Long getWarningCount() {
        Long warningCount = runCountSql("select count(*) cnum from cm_supervision_warning where status <> 'finish' and delete_flag = 0", null);
        return warningCount;
    }

    /**
     * 根据登陆人所在部门的资源查询预警信息
     * @param buildDeptId
     * @return
     */
    public Long getWarningCountByBuildDeptId(String buildDeptId) {
        String sql = "select count(*) cnum from cm_supervision_warning w join cm_project_info p on w.project_id = p.id "
                + "where w.status <> 'finish' and w.delete_flag = 0 and p.build_dept_id = ?";
        Long warningCount = runCountSql(sql, new Object[]{buildDeptId});
        return warningCount;
    }
}

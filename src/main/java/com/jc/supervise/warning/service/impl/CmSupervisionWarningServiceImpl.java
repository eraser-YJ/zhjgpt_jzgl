package com.jc.supervise.warning.service.impl;

import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.resource.util.ResourceUtil;
import com.jc.supervise.point.domain.CmSupervisionPoint;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import com.jc.supervise.point.service.ICmSupervisionPointColumnService;
import com.jc.supervise.point.service.ICmSupervisionPointService;
import com.jc.supervise.point.vo.SupervisionPointInformation;
import com.jc.supervise.warning.dao.ICmSupervisionWarningDao;
import com.jc.supervise.warning.domain.CmSupervisionWarning;
import com.jc.supervise.warning.enums.SupervisionWarningStatusEnums;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-监管预警管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmSupervisionWarningServiceImpl extends BaseServiceImpl<CmSupervisionWarning> implements ICmSupervisionWarningService {

	private ICmSupervisionWarningDao cmSupervisionWarningDao;
	@Autowired
	private ICmSupervisionPointService cmSupervisionPointService;
	@Autowired
	private ICmProjectInfoService cmProjectInfoService;
	@Autowired
	private ICmMessageInfoService cmMessageInfoService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CmSupervisionWarningServiceImpl(){}

	@Autowired
	public CmSupervisionWarningServiceImpl(ICmSupervisionWarningDao cmSupervisionWarningDao){
		super(cmSupervisionWarningDao);
		this.cmSupervisionWarningDao = cmSupervisionWarningDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmSupervisionWarning entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmSupervisionWarningDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmSupervisionWarning entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmSupervisionWarning entity) throws CustomException {
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
	public CmSupervisionWarning getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmSupervisionWarning param = new CmSupervisionWarning();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmSupervisionWarningDao.queryAll(param));
	}

	public Result saveOrUpdate(SupervisionPointInformation point, CmProjectInfo project) {
		CmSupervisionWarning db = new CmSupervisionWarning();
		db.setSupervisionPointId(point.getId());
		db.setExtStr1(project.getProjectNumber());
		//db.setProjectId(project.getId());
		Result res = null;
		try {
			List<CmSupervisionWarning> warningList = this.cmSupervisionWarningDao.queryAll(db);
			db = null;
			if (warningList != null && warningList.size() > 0) {
				db = warningList.get(0);
			}
			if (db == null) {
				//新增
				db = new CmSupervisionWarning();
				db.setSupervisionPointId(point.getId());
				db.setProjectId(project.getId());
				db.setExtStr1(project.getProjectNumber());
				db.setStatus(SupervisionWarningStatusEnums.no.toString());
				db.setSupervisionDate(new Date(System.currentTimeMillis()));
				db.setSupervisionDeptId(project.getSuperviseDeptId());
				if(project.getSuperviseDeptId()==null){
					project.setSuperviseDeptId("100025");
				}
				Department supervisionDept = DeptCacheUtil.getDeptById(project.getSuperviseDeptId());
				if (supervisionDept != null) {
					db.setSupervisionDeptCode(supervisionDept.getCode());
				}
				if(project.getBuildDeptId()==null){
					project.setBuildDeptId("100025");
				}
				Department buildDept = DeptCacheUtil.getDeptById(project.getBuildDeptId());
				db.setBuildDeptId(project.getBuildDeptId());
				if (buildDept != null) {
					db.setBuildDeptCode(buildDept.getCode());
				}
				db.setWarningContent(point.getErrorText());
				res = this.saveEntity(db);
			} else {
				//已存在预警情况，如果完结了则再次打开预警，否则直接结束，不进行操作
				if (db.getStatus().equals(SupervisionWarningStatusEnums.finish.toString())) {
					//已经完结了，重新打开，设置预警时间和状态
					db.setStatus(SupervisionWarningStatusEnums.reopen.toString());
					db.setSupervisionDate(new Date(System.currentTimeMillis()));
					res = this.updateEntity(db);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (res != null && res.isSuccess()) {
			//给项目的监管单位发送消息
			try {
				Department department = DeptCacheUtil.getDeptById(project.getSuperviseDeptId());
				if (department != null) {
					this.cmMessageInfoService.sendMessage("有关项目: " + project.getProjectName() + "的预警信息", db.getWarningContent(),
							department.getLeaderId(), department.getId(), department.getCode());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			res = Result.success();
		}
		return res;
	}

	@Override
	public void jobHandler() {
		//获取全部监测点与监管机构的关系
		Map<String, List<SupervisionPointInformation>> deptPointMap = this.cmSupervisionPointService.getDeptIdAndPoint();
		if (deptPointMap.size() == 0) {
			return;
		}
		//根据配置过监测点的监管机构查询可以过滤的项目
		for (Map.Entry<String, List<SupervisionPointInformation>> entry : deptPointMap.entrySet()) {
			//这个是获取本地的项目
			//List<CmProjectInfo> projectList = this.cmProjectInfoService.getListBySuperviseDeptId(entry.getKey());
			//这个是获取监管的项目
			List<CmProjectInfo> projectList = ResourceUtil.getNoFinishProject();
			if (projectList == null || projectList.size() == 0) {
				continue;
			}
			//循环项目扫描监测点
			for (CmProjectInfo project : projectList) {
				List<SupervisionPointInformation> errorPointList = scanProject(entry.getValue(), project.getProjectNumber());
				if (errorPointList.size() > 0) {
					//开始预警
					for (SupervisionPointInformation p : errorPointList) {
						System.out.println("项目名: " + project.getProjectName() + " 预警描述: " + p.getErrorText());
						this.saveOrUpdate(p, project);
					}
				}
			}
		}
	}

	@Override
	public Result checkScan(SupervisionPointInformation information, String projectNumber) {
		List<SupervisionPointInformation> pointList = new ArrayList<>();
		pointList.add(information);
		List<Object> paramList = getScanParamList(information.getColumnList(), projectNumber);
		StringBuffer message = new StringBuffer();
		message.append("project: " + projectNumber + "<br />参数：");
		for (Object o : paramList) {
			message.append(o + ", ");
		}
		message.append("<br />");
		try {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("JavaScript");
			engine.eval(information.getJsContent());
			Object value = ((Invocable) engine).invokeFunction(information.getFunctionName(), paramList.toArray());
			message.append("直接结果： " + value + "<br />");
			Result result = Result.success();
			System.out.println(message.toString());
			result.setMsg(message.toString());
			return result;
		} catch (Exception ex) {
			return Result.failure(1, ex.getMessage());
		}
	}

	private List<SupervisionPointInformation> scanProject(List<SupervisionPointInformation> pointList, String projectNumber) {
		StringBuffer message = new StringBuffer();
		List<SupervisionPointInformation> errorPoint = new ArrayList<>();
		for (SupervisionPointInformation point : pointList) {
			// 获取脚本执行参数值
			try {
				List<CmSupervisionPointColumn> columnList = point.getColumnList();
				List<Object> paramList = getScanParamList(columnList, projectNumber);
				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("JavaScript");
				engine.eval(point.getJsContent());
				String value = ((Invocable) engine).invokeFunction(point.getFunctionName(), paramList.toArray()) + "";
				message.append("project: " + projectNumber + ", 参数：");
				for (Object o : paramList) {
					message.append(o + ", ");
				}
				message.append("运行结果: " + value + "<br />");
				if (value != null && value.equals(GlobalContext.FALSE)) {
					//检查出现问题了，进行预警
					errorPoint.add(point);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		log.warn(message.toString());
		return errorPoint;
	}

	/**
	 * 获取参数列表
	 * @param columnList
	 * @param projectNumber
	 * @return
	 */
	private List<Object> getScanParamList(List<CmSupervisionPointColumn> columnList, String projectNumber) {
		List<Object> paramList = new ArrayList<>();
		for (CmSupervisionPointColumn column : columnList) {
			if (column.getDataSource().equals(ICmSupervisionPointColumnService.SQL)) {
				//sql的情况
				runSql(paramList, column, projectNumber);
			} else if (column.getDataSource().equals(ICmSupervisionPointColumnService.BEAN)) {
				//程序数据源
				runProgram(paramList, column, projectNumber);
			}
		}
		return paramList;
	}

	/**
	 * 执行sql数据源
	 * @param paramList
	 * @param column
	 * @param projectNumber
	 */
	private void runSql(List<Object> paramList, CmSupervisionPointColumn column, String projectNumber) {
		String[] aliasArray = GlobalUtil.splitStr(column.getDataClass(), ',');
		System.out.println(column.getDataValueFile());
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(column.getDataValueFile(), new Object[]{projectNumber});
		if (resultList != null && resultList.size() > 0) {
			Map<String, Object> map = resultList.get(0);
			if (map != null && map.size() > 0) {
				for (String s : aliasArray) {
					paramList.add(map.get(s));
				}
			}
		} else {
			for (String s : aliasArray) {
				paramList.add(null);
			}
		}
	}

	/**
	 * 执行程序数据源
	 * @param paramList
	 * @param column
	 * @param projectNumber
	 */
	private void runProgram(List<Object> paramList, CmSupervisionPointColumn column, String projectNumber) {
		try {
			Object clazz = Class.forName(column.getDataClass()).newInstance();
			Method m = clazz.getClass().getDeclaredMethod(column.getDataValue(), String.class);
			paramList.add(m.invoke(clazz, projectNumber));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

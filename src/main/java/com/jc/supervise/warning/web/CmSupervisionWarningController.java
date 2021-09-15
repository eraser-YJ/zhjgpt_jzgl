package com.jc.supervise.warning.web;

import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.supervise.warning.domain.CmSupervisionWarning;
import com.jc.supervise.warning.domain.validator.CmSupervisionWarningValidator;
import com.jc.supervise.warning.enums.SupervisionWarningStatusEnums;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 建设管理-监测预警管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervision/warning")
public class CmSupervisionWarningController extends BaseController{

	@Autowired
	private ICmSupervisionWarningService cmSupervisionWarningService;
	@Autowired
	private ICmProjectPersonService cmProjectPersonService;

	@org.springframework.web.bind.annotation.InitBinder("cmSupervisionWarning")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmSupervisionWarningValidator());
    }

	public CmSupervisionWarningController() {
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		map.put(GlobalContext.SESSION_TOKEN, getToken(request));
		model.addAttribute("data", map);
		return "supervise/warning/superviseWarningForm";
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage() throws Exception{
		return "supervise/warning/superviseWarningList";
	}

	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String search() throws Exception{
		return "supervise/warning/superviseWarningSearch";
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmSupervisionWarning get(CmSupervisionWarning entity) throws Exception{
		return cmSupervisionWarningService.get(entity);
	}

	@RequestMapping(value="loadWarningInfo.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public Map<String, Object> loadWarningInfo(String id) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		List<CmProjectPerson> personList = null;
		CmSupervisionWarning warning = this.cmSupervisionWarningService.getById(id);
		if (warning != null && !StringUtil.isEmpty(warning.getProjectId())) {
			personList = this.cmProjectPersonService.getByProjectId(warning.getProjectId());
		}
		if (personList == null) {
			personList = new ArrayList<>();
		}
		resultMap.put("warning", warning);
		resultMap.put("personList", personList);
		return resultMap;
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmSupervisionWarning entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.supervision_date ");
		}
		entity.setDeptCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		PageManager page_ = cmSupervisionWarningService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="dispose.action",method=RequestMethod.POST)
	@ResponseBody
	public Result dispose(CmSupervisionWarning entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getDisploseResult())) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmSupervisionWarning db = this.cmSupervisionWarningService.getById(entity.getId());
		if (db == null) {
			return Result.failure(ResultCode.RESULE_DATA_NONE);
		}
		db.setDisploseResult(entity.getDisploseResult());
		db.setDisploseDate(new Date(System.currentTimeMillis()));
		db.setDisploseUserId(SystemSecurityUtils.getUser().getId());
		db.setStatus(SupervisionWarningStatusEnums.finish.toString());
		return this.cmSupervisionWarningService.updateEntity(db);
	}
}

package com.jc.supervise.warning.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.supervise.warning.domain.CmSupervisionMessage;
import com.jc.supervise.warning.domain.CmSupervisionWarning;
import com.jc.supervise.warning.domain.validator.CmSupervisionMessageValidator;
import com.jc.supervise.warning.enums.SupervisionWarningStatusEnums;
import com.jc.supervise.warning.service.ICmSupervisionMessageService;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 建设管理-督办管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervision/message")
public class CmSupervisionMessageController extends BaseController{

	@Autowired
	private ICmSupervisionMessageService cmSupervisionMessageService;
	@Autowired
	private ICmSupervisionWarningService cmSupervisionWarningService;

	@org.springframework.web.bind.annotation.InitBinder("cmSupervisionMessage")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmSupervisionMessageValidator());
    }

	public CmSupervisionMessageController() {
	}

	@RequestMapping(value = "sendMessage.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
	public Result save(CmSupervisionMessage entity, HttpServletRequest request) throws Exception{
		Department department = DeptCacheUtil.getDeptById(entity.getReceiveDeptId());
		if (department != null) {
			entity.setReceiveDeptCode(department.getCode());
		}
		entity.setHandleStatus("0");
		entity.setSenderId(SystemSecurityUtils.getUser().getId());
		entity.setSenderDeptId(SystemSecurityUtils.getUser().getDeptId());
		Department sender = DeptCacheUtil.getDeptById(entity.getSenderDeptId());
		if (sender != null) {
			entity.setSenderDeptCode(sender.getCode());
		}
		return cmSupervisionMessageService.saveEntity(entity);
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		map.put(GlobalContext.SESSION_TOKEN, getToken(request));
		model.addAttribute("data", map);
		return "supervise/message/superviseMessageForm";
	}

	@RequestMapping(value="sendForm.action",method=RequestMethod.GET)
	public String sendForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		map.put(GlobalContext.SESSION_TOKEN, getToken(request));
		model.addAttribute("data", map);
		return "supervise/message/superviseSendMessageForm";
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmSupervisionMessage get(CmSupervisionMessage entity) throws Exception{
		return cmSupervisionMessageService.get(entity);
	}

	@RequestMapping(value="getLeaderIdByDeptId.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getLeaderIdByDeptId(String id) throws Exception{
		Department department = DeptCacheUtil.getDeptById(id);
		Map<String, String> resultMap = new HashMap<>(2);
		String leaderId = "";
		String leaderName = "";
		if (department != null && !StringUtil.isEmpty(department.getLeaderId())) {
			leaderId = department.getLeaderId();
			User user = UserUtils.getUser(leaderId);
			if (user != null) {
				leaderName = user.getDisplayName();
			}
		}
		resultMap.put("leaderId", leaderId);
		resultMap.put("leaderName", leaderName);
		return resultMap;
	}

	@RequestMapping(value="send.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String send() throws Exception{
		return "supervise/message/superviseMessageSendList";
	}

	@RequestMapping(value="manageSendList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageSendList(CmSupervisionMessage entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.create_date ");
		}
		entity.setSenderId(SystemSecurityUtils.getUser().getId());
		PageManager page_ = cmSupervisionMessageService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="receive.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String receive() throws Exception{
		return "supervise/message/superviseMessageReceiveList";
	}

	@RequestMapping(value="manageReceiveList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageReceiveList(CmSupervisionMessage entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.create_date ");
		}
		entity.setReceiveId(SystemSecurityUtils.getUser().getId());
		PageManager page_ = cmSupervisionMessageService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="dispose.action",method=RequestMethod.POST)
	@ResponseBody
	public Result dispose(CmSupervisionMessage entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getHandleResult())) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmSupervisionMessage db = this.cmSupervisionMessageService.getById(entity.getId());
		if (db == null) {
			return Result.failure(ResultCode.RESULE_DATA_NONE);
		}
		db.setHandleResult(entity.getHandleResult());
		db.setHandleStatus("1");
		db.setHandleDate(new Date(System.currentTimeMillis()));
		Result result = this.cmSupervisionMessageService.updateEntity(db);
		if (result.isSuccess()) {
			//根据预警id同步预警的处理状态和结果
			if (!StringUtil.isEmpty(db.getWarningId())) {
				CmSupervisionWarning warning = this.cmSupervisionWarningService.getById(db.getWarningId());
				if (warning != null) {
					warning.setDisploseResult(entity.getHandleResult());
					warning.setDisploseDate(new Date(System.currentTimeMillis()));
					warning.setDisploseUserId(SystemSecurityUtils.getUser().getId());
					warning.setStatus(SupervisionWarningStatusEnums.finish.toString());
					this.cmSupervisionWarningService.updateEntity(warning);
				}
			}
		}
		return result;
	}
}

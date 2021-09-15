package com.jc.csmp.message.web;

import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.domain.validator.CmMessageInfoValidator;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
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
 * 建设管理-消息管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/message/info")
public class CmMessageInfoController extends BaseController{

	@Autowired
	private ICmMessageInfoService cmMessageInfoService;

	@org.springframework.web.bind.annotation.InitBinder("cmMessageInfo")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmMessageInfoValidator());
    }

	public CmMessageInfoController() {
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		map.put(GlobalContext.SESSION_TOKEN, getToken(request));
		model.addAttribute("data", map);
		return "csmp/message/messageInfoForm";
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage() throws Exception{
		return "csmp/message/messageInfoList";
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmMessageInfo get(CmMessageInfo entity) throws Exception{
		return cmMessageInfoService.get(entity);
	}

	@RequestMapping(value="look.action",method=RequestMethod.GET)
	@ResponseBody
	public CmMessageInfo look(String id) throws Exception{
		CmMessageInfo entity = this.cmMessageInfoService.getById(id);
		if (entity != null) {
			if (entity.getReadStatus() == null || "0".equals(entity.getReadStatus())) {
				entity.setReadStatus("1");
				entity.setReadDate(new Date(System.currentTimeMillis()));
				this.cmMessageInfoService.updateEntity(entity);
			}
		}
		return entity;
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmMessageInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.create_date ");
		}
		entity.setReceiveId(SystemSecurityUtils.getUser().getId());
		PageManager page_ = cmMessageInfoService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}
}

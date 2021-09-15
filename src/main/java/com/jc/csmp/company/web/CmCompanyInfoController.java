package com.jc.csmp.company.web;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.company.domain.CmCompanyInfo;
import com.jc.csmp.company.domain.validator.CmCompanyInfoValidator;
import com.jc.csmp.company.service.ICmCompanyInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-单位管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/company/info")
public class CmCompanyInfoController extends BaseController{

	@Autowired
	private ICmCompanyInfoService cmCompanyInfoService;
	@Autowired
	private IDicManager dicManager;

	@org.springframework.web.bind.annotation.InitBinder("cmCompanyInfo")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmCompanyInfoValidator());
    }

	public CmCompanyInfoController() {
	}

	/**
	 * 保存方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
	public Map<String,Object> save(@Valid CmCompanyInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(cmCompanyInfoService.saveEntity(entity), resultMap, getToken(request));
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(CmCompanyInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmCompanyInfoService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmCompanyInfo get(CmCompanyInfo entity) throws Exception{
		return cmCompanyInfoService.get(entity);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "csmp/company/cmCompanyForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmCompanyInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = cmCompanyInfoService.query(entity, page);
		GlobalUtil.setTableRowNo(page_,page_.getPageRows());
		return page_;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage() throws Exception{
		return "csmp/company/cmCompanyList";
	}

	@RequestMapping(value="change.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String change() throws Exception{
		return "csmp/company/cmCompanyChange";
	}

	/**
	 * 删除方法
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(CmCompanyInfo entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmCompanyInfoService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value = "checkCreditCode.action")
	@ResponseBody
	public boolean checkCreditCode(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String creditCode = request.getParameter("creditCode");
		Result result = cmCompanyInfoService.checkCreditCode(id, creditCode);
		return result.getCode().intValue() == ResultCode.SUCCESS.code().intValue() ? true : false;
	}

	@RequestMapping(value = "companyTree.action")
	@ResponseBody
	public List<Department> projectTree(HttpServletRequest request) {
		List<Department> resultList = new ArrayList<>();
		CmCompanyInfo param = new CmCompanyInfo();
		String companyType = request.getParameter("companyType");
		if (!StringUtil.isEmpty(companyType)) {
			Dic typeDic = this.dicManager.getDic(DicKeyEnum.companyType.getTypeCode(), DicKeyEnum.companyType.getParentCode(), companyType);
			if (typeDic != null) {
				resultList.add(Department.createTreeData("0", "-1", typeDic.getName()));
			}
			param.setCompanyTypeLike(companyType);
		}
		try {
			List<CmCompanyInfo> dbList = this.cmCompanyInfoService.queryAll(param);
			if (dbList != null) {
				for (CmCompanyInfo cmCompanyInfo : dbList) {
					resultList.add(Department.createTreeData(cmCompanyInfo.getId(), "0", cmCompanyInfo.getCompanyName()));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultList;
	}
}

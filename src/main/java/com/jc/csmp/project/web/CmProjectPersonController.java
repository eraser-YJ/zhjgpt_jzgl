package com.jc.csmp.project.web;

import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.csmp.project.domain.validator.CmProjectPersonValidator;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
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
import java.util.*;

/**
 * 建设管理-项目人员分配Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/person")
public class CmProjectPersonController extends BaseController{

	@Autowired
	private ICmProjectPersonService cmProjectPersonService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectPerson")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectPersonValidator());
	}

	public CmProjectPersonController() {
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
	public Map<String,Object> save(@Valid CmProjectPerson entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			entity.setCanShow("1");
			GlobalUtil.resultToMap(cmProjectPersonService.saveEntity(entity), resultMap, getToken(request));
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
	public Map<String, Object> update(CmProjectPerson entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmProjectPersonService.updateEntity(entity), resultMap, getToken(request));
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
	public CmProjectPerson get(CmProjectPerson entity) throws Exception{
		return cmProjectPersonService.get(entity);
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
		return "csmp/project/person/cmProjectPersonForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPerson entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByField("t.CREATE_DATE");
		}
		PageManager page_ = cmProjectPersonService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
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
	public String manage(HttpServletRequest request) throws Exception{
		request.setAttribute("projectId", request.getParameter("projectId"));
		return "csmp/project/person/cmProjectPersonList";
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
	public  Map<String, Object> deleteByIds(CmProjectPerson entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectPersonService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 根据项目获取项目参与单位
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "personTreeByProjectId.action")
	@ResponseBody
	public List<Department> projectTree(HttpServletRequest request) throws Exception {
		String projectId = request.getParameter("projectId");
		if (StringUtil.isEmpty(projectId)) {
			return Collections.emptyList();
		}
		CmProjectPerson param = new CmProjectPerson();
		param.setProjectId(projectId);
		List<CmProjectPerson> dbList = this.cmProjectPersonService.queryAll(param);
		List<Department> resultList = new ArrayList<>();
		if (dbList != null) {
			for (CmProjectPerson person : dbList) {
				resultList.add(Department.createTreeData(person.getCompanyId(), "0", person.getCompanyName()));
			}
		}
		return resultList;
	}

    /**
     * 分页查询方法
     * @param entity
     * @param entity
     * @return
     */
    @RequestMapping(value="queryAll.action",method=RequestMethod.GET)
    @ResponseBody
    public List<CmProjectPerson> queryAll(CmProjectPerson entity) throws CustomException {
        List<CmProjectPerson> list;
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByField("t.CREATE_DATE");
        }
        list = cmProjectPersonService.queryAll(entity);
        return list;
    }


}


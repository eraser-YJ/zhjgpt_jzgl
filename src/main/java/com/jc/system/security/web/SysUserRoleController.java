package com.jc.system.security.web;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.WebDataBinder;

import com.jc.foundation.domain.PageManager;
import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.domain.validator.SysUserRoleValidator;
import com.jc.system.security.service.ISysUserRoleService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sysUserRole")
public class SysUserRoleController extends BaseController {
	
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	
	@org.springframework.web.bind.annotation.InitBinder("sysUserRole")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SysUserRoleValidator()); 
    }
	
	public SysUserRoleController() {
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SysUserRole sysUserRole,final PageManager page, HttpServletRequest request) {
		return sysUserRoleService.query(sysUserRole, page);
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage() throws Exception{
		return "sys/sysUserRole/sysUserRoleAaa"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer deleteByIds(SysUserRole sysUserRole,String ids) throws Exception{
		sysUserRole.setPrimaryKeys(ids.split(","));
		sysUserRoleService.delete(sysUserRole);
		return 1;
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(@Valid SysUserRole sysUserRole,BindingResult result) throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (result.hasErrors()) {
			String errorStr ="";
			List<ObjectError> errorList=result.getAllErrors();
			for(ObjectError error:errorList){
				errorStr+=error.getDefaultMessage()+",";
			}
			resultMap.put("errorMessage", errorStr);
			resultMap.put("success", "false");
			return resultMap;
		}
		sysUserRoleService.save(sysUserRole);
		resultMap.put("success", "true");
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer update(SysUserRole sysUserRole) throws Exception{
		Integer flag = sysUserRoleService.update(sysUserRole);
		return flag;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public SysUserRole get(SysUserRole sysUserRole) throws Exception{
		return sysUserRoleService.get(sysUserRole);
	}

	@RequestMapping(value="getUserIdByRoleId.action",method=RequestMethod.GET)
	@ResponseBody
	public List<SysUserRole> getUserIdByRoleId(SysUserRole sysUserRole) throws Exception{
		List<SysUserRole> sysUserRoles = sysUserRoleService.queryAll(sysUserRole);
		if (sysUserRoles != null){
			return sysUserRoles;
		} else {
			return new ArrayList<>();
		}
	}
	
	@RequestMapping(value="getUserIdByRoleId.action",method=RequestMethod.POST)
	@ResponseBody
	public List<SysUserRole> getUserIdByRoleIdPOST(SysUserRole sysUserRole) throws Exception{
		return getUserIdByRoleId(sysUserRole);
	}

}
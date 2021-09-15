package com.jc.system.security.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import com.jc.system.security.domain.SysRole;
import com.jc.system.security.domain.validator.SysRoleValidator;
import com.jc.system.security.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/system/sysRole")
public class SysRoleController extends BaseController {
	
	@Autowired
	private ISysRoleService sysRoleService;
	
	@org.springframework.web.bind.annotation.InitBinder("sysRole")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SysRoleValidator()); 
    }
	
	public SysRoleController() {
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SysRole sysRole,final PageManager page, HttpServletRequest request) {
		return sysRoleService.query(sysRole, page);
	}
	
	@RequestMapping(value="roleList.action",method=RequestMethod.GET)
	@ResponseBody
	public List<SysRole> roleList(SysRole sysRole,final PageManager page, HttpServletRequest request) {
		List<SysRole> list = sysRoleService.queryAll(sysRole);
		return list;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage() throws Exception{
		return "sys/sysRole/sysRoleAaa"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer deleteByIds(SysRole sysRole,String ids) throws Exception{
		sysRole.setPrimaryKeys(ids.split(","));
		sysRoleService.deleteByIds(sysRole);
		return 1;
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(@Valid SysRole sysRole,BindingResult result) throws Exception{
		Map<String,Object> resultMap = new HashMap<>();
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
		sysRoleService.save(sysRole);
		resultMap.put("success", "true");
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer update(SysRole sysRole) throws Exception{
		Integer flag = sysRoleService.update(sysRole);
		return flag;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public SysRole get(SysRole sysRole) throws Exception{
		return sysRoleService.get(sysRole);
	}

}
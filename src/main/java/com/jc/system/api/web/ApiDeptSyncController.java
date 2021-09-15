package com.jc.system.api.web;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.api.service.IDeptSync;
import com.jc.system.api.util.DeptSyncUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.service.IDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/dept")
public class ApiDeptSyncController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiDeptSyncController.class);

    @Autowired(required=false)
    private IDeptSync apiDeptSync;

    public ApiDeptSyncController() {
    }

	/**
	 * 单一组织机构推送接口
	 * @param deptid
	 */
	@RequestMapping(value = "pushDept.action",method=RequestMethod.GET)
	@ResponseBody
	public void pushUser(String deptid){
		IDepartmentService deptService = SpringContextHolder.getBean(IDepartmentService.class);
		if(GlobalContext.isSysCenter()) {
			try {
				Department deptTemp = new Department();
				deptTemp.setId(deptid);
				Department dept = deptService.get(deptTemp);
				DeptSyncUtil.push(dept);
			} catch (Exception e) {
				LOGGER.error("组织机构信息推送失败！");
			}
		}
	}

	/**
	 * 组织机构同步接口
	 * @param jsonparams
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "sync",method=RequestMethod.GET)
    @ResponseBody
    public String sync(String jsonparams) throws Exception {
		String returnJson = "";
		if(apiDeptSync == null){
			Map<String,String> errorMap = new HashMap<>(2);
	    	errorMap.put("status", "1");
	    	errorMap.put("msg", "接口未实现");
	    	return JsonUtil.java2Json(errorMap);
		}
		try{
			Department dept = (Department) JsonUtil.json2Java(jsonparams, Department.class);
			boolean result = apiDeptSync.sync(dept);
			if(result == true){
				Map<String,String> successMap = new HashMap<>(2);
				successMap.put("status", "0");
				successMap.put("msg", "接口调用成功");
    	    	returnJson = JsonUtil.java2Json(successMap);
			}else{
				Map<String,String> errorMap = new HashMap<>(2);
    	    	errorMap.put("status", "2");
    	    	errorMap.put("msg", "接口调用失败");
    	    	returnJson = JsonUtil.java2Json(errorMap);
			}
		}catch(Exception e){
			LOGGER.error(e.toString());
			Map<String,String> errorMap = new HashMap<>(2);
	    	errorMap.put("status", "2");
	    	errorMap.put("msg", "接口调用失败");
	    	returnJson = JsonUtil.java2Json(errorMap);
		}
		return returnJson;
	}
    
}
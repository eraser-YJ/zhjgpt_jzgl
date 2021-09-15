package com.jc.busi.standard.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.common.kit.StringUtil;
import com.jc.foundation.util.JsonUtilOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.busi.standard.util.UserContext;
import com.jc.common.kit.vo.ResVO;
import com.jc.common.log.Uog;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhUserService;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @author lc 
 * @version 2020-03-10
 */

@Controller
@RequestMapping(value = "/api/dlh/push")
public class DataPushController extends BaseController {
	// 日志
	private Uog log = Uog.getInstanceOnSoap();

	@Autowired
	private IDlhDataobjectService dataobjectService;

	@Autowired
	private IDlhUserService userService;

	@RequestMapping(value = "manage.action", method = RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception {
		return "dlh/dlhDatamodel/dlhDatamodelList";
	}

	@RequestMapping(value = "save.action")
	@ResponseBody
	public ResVO saveData(HttpServletResponse response, HttpServletRequest request) throws Exception {
		// 初始化
		Map<String, String[]> data=request.getParameterMap();
		UserContext.init();
		log.error("客户端IP：" + request.getRemoteAddr() + "的日志记录号：" + UserContext.getLogProcessId());

		String objUrl = request.getParameter("objUrl");
		try {
			String json = data.get("info").toString();
			List<Map> dataObjectList = JsonUtil.json2Array(json, Map.class);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			if (dataObjectList == null || dataObjectList.size() == 0) {
				return ResVO.buildFail("数据为空");
			}
			Map<String, Object> lineMap;
			for (Map dataObj : dataObjectList) {
				lineMap = new HashMap<String, Object>();
				for (Object key : dataObj.keySet()) {
					lineMap.put(key.toString(), dataObj.get(key));
				}
				dataList.add(lineMap);
			}
			DlhDataobject cond = new DlhDataobject();
			cond.setObjUrl(objUrl);
			dataobjectService.modify(cond, dataList);
			return ResVO.buildSuccess();
		} catch (Exception ex) {
			log.error("srcapyd推送数据异常：" + ex.getMessage(), ex);
			ex.printStackTrace();
			return ResVO.buildFail(ex.getMessage());
		}
	}

	@RequestMapping(value = "save2.action")
	@ResponseBody
	public ResVO action(@RequestBody Map<String,Object> data, HttpServletRequest request) throws Exception {
		// 初始化
//		Map<String, String[]> data=request.getParameterMap();
		UserContext.init();
		log.error("客户端IP：" + request.getRemoteAddr() + "的日志记录号：" + UserContext.getLogProcessId());
		DlhUser condUser = new DlhUser();
		condUser.setDlhUsername("srcapyd");
		DlhUser user = userService.get(condUser);
		/*if (user == null) {
			log.error("srcapyd用户不存在");
			return ResVO.buildFail("srcapyd用户不存在");
		}
		UserContext.setUser(user);
		String objUrl = request.getParameter("objUrl");
		if (objUrl == null || objUrl.trim().length() <= 0) {
			log.error("srcapyd传入地址参数为空");
			return ResVO.buildFail("srcapyd传入地址参数为空");
		}
		if (data == null || data.size() <= 0 || !data.containsKey("info")) {
			log.error("srcapyd传入参数为空");
			return ResVO.buildFail("srcapyd传入参数为空");
		}*/
		String query_modelId = request.getParameter("query_modelId");
		DlhDataobject dlhDataobject = new DlhDataobject();
		dlhDataobject.setModelId(query_modelId);
		List<DlhDataobject> dlhDataobjectList =this.dataobjectService.queryAll(dlhDataobject);
		if(dlhDataobjectList==null||dlhDataobjectList.size()==0){
			return ResVO.buildFail("数据对象关不存在");
		}
		dlhDataobject = dlhDataobjectList.get(0);
		String objUrl = dlhDataobject.getObjUrl();
		if(StringUtil.isEmpty(objUrl)){
			return ResVO.buildFail("数据对象路径为空");
		}
		try {
			String json = data.get("info").toString();

			List<Map> dataObjectList = JsonUtil.json2Array(json, Map.class);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			if (dataObjectList == null || dataObjectList.size() == 0) {
				return ResVO.buildFail("数据为空");
			}
			Map<String, Object> lineMap;
			for (Map dataObj : dataObjectList) {
				lineMap = new HashMap<String, Object>();
				for (Object key : dataObj.keySet()) {
					lineMap.put(key.toString(), dataObj.get(key));
				}
				dataList.add(lineMap);
			}
			DlhDataobject cond = new DlhDataobject();
			cond.setObjUrl(objUrl);
			dataobjectService.modify(cond, dataList);
			return ResVO.buildSuccess();
		} catch (Exception ex) {
			log.error("srcapyd推送数据异常：" + ex.getMessage(), ex);
			return ResVO.buildFail(ex.getMessage());
		}
	}

    public static void main(String[] args) {
        String aa = "{fydm:fydm}";
        aa = "[{\"name\":\"id\", \"value\":\"1\"}, {\"name\":\"company_name\", \"value\":\"22\"}]";
        String b="[{“operationAction“:“like“,“operationKey“:“company_name“,“operationType“:“varchar“,“value“:“建设单位“}]";
		b=b.replaceAll("“","\"");
		b=b.replaceAll("”","\"");

		List<Map> dataObjectList = JsonUtil.json2Array(b, Map.class);
        System.out.println(aa);
    }
}
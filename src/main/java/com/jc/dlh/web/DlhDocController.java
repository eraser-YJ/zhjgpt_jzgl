package com.jc.dlh.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.domain.DlhUserRole;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhUserRoleService;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.web.BaseController;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;

/**
 * @title 统一数据资源中心
 * @description controller类
 * @author lc
 * @version 2020-03-18
 */

@Controller
@RequestMapping(value = "/api/dlh/doc")
public class DlhDocController extends BaseController {

	@Autowired
	private IDlhUserRoleService dlhUserRoleService;

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;

	@Autowired
	private IDlhDataobjectFieldService dlhDataobjectFieldService;

	@RequestMapping(value = "index.action")
	public String index(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DlhUserRole cond = new DlhUserRole();
		cond.setUserId(id);
		List<DlhUserRole> roleList = dlhUserRoleService.queryAll(cond);
		List<String> objList = new ArrayList<String>();
		for (DlhUserRole role : roleList) {
			objList.add(role.getDataId());
		}
		if (objList.size() > 0) {
			DlhDataobject objCond = new DlhDataobject();
			objCond.setPrimaryKeys(objList.toArray(new String[0]));
			List<DlhDataobject> objDisList = dlhDataobjectService.queryAll(objCond);
			request.setAttribute("objList", objDisList);
		}
		return "dlh/doc/index";
	}

	/**
	 * @description 接入指南
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "guide.action")
	public String guide(HttpServletRequest request) throws Exception {
		return "dlh/doc/guide";
	}

	@RequestMapping(value = "itf.action")
	public String itf(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		DlhDataobject objCond = new DlhDataobject();
		objCond.setId(id);
		DlhDataobject head = dlhDataobjectService.get(objCond);
		DlhDataobjectField fCond = new DlhDataobjectField();
		fCond.setObjectId(head.getId());
		List<DlhDataobjectField> fList = dlhDataobjectFieldService.queryAll(fCond);
		if (fList != null) {
			for (DlhDataobjectField field : fList) {
				field.setDicList(getDic(field.getItemDicCode()));
			}
		}
		request.setAttribute("head", head);
		request.setAttribute("itemList", fList);
		return "dlh/doc/itf";
	}

	/**
	 * @description 字典
	 * @return
	 * @throws Exception
	 */
	private List<Dic> getDic(String typeCode) throws Exception {
		if (typeCode == null || typeCode.trim().length() <= 0) {
			return new ArrayList<>();
		}
		IDicService service = SpringContextHolder.getBean(IDicService.class);
		Dic cond = new Dic();
		cond.setParentId(typeCode);
		List<Dic> dicList = service.query(cond);
		if (dicList == null) {
			dicList = new ArrayList<>();
		}
		return dicList;
	}
}
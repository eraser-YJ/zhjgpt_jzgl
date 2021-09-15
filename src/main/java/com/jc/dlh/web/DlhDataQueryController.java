package com.jc.dlh.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.dlh.domain.DlhTableMap;
import com.jc.dlh.filemanage.domain.DlhFile;
import com.jc.dlh.filemanage.service.IDlhFileManageService;
import com.jc.dlh.service.IDlhTableMapService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.content.service.IAttachService;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
import com.jc.util.MindDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.common.db.dialect.mysql.MysqlColumnType;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhDateQueryService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类
 * @author lc 
 * @version 2020-03-10
 */

@Controller
@RequestMapping(value = "/dlh/dlhQuery/")
public class DlhDataQueryController extends BaseController {

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;
	@Autowired
	private IDlhDataobjectFieldService dlhDataobjectFieldService;

	@Autowired
	private IDlhDateQueryService queryService;

	@Autowired
	private IDlhTableMapService dlhTableMapService;

    @Autowired
    private IDicService dicService;

	@Autowired
	private IDlhFileManageService dlhFileManageService;

	@Autowired
	private IAttachService attachService;


    public DlhDataQueryController() {
	}

	/**
	 * @description 分页查询方法
	 * @param dlhDataobject 实体类
	 * @param   page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDataobject dlhDataobject, PageManager page) {
		try {
			// 默认排序
			if (StringUtil.isEmpty(dlhDataobject.getOrderBy())) {
				dlhDataobject.addOrderByFieldDesc("t.CREATE_DATE");
			}
			PageManager page_ = dlhDataobjectService.query(dlhDataobject, page);
			return page_;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manage.action", method = RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception {
		return "dlh/query/dlhDataQueryList";
	}

	/**
	 * 分页查询方法
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "manageDetailList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManagerEx<Map<String, Object>> manageDetailList(PageManager page, HttpServletRequest request) {
		try {
			String dataSrc = request.getParameter("data_src");
			if (dataSrc == null || dataSrc.trim().length() <= 0) {
				dataSrc = (String) request.getSession().getAttribute("data_src_");
				if (dataSrc == null || dataSrc.trim().length() <= 0) {
					throw new Exception("Session 中的 data_src_ 不能为空");
				}
			}
			String json = request.getParameter("condJson");
			DlhDataobject cond = new DlhDataobject();
			cond.setObjUrl(dataSrc);
			PageManagerEx page_ = queryService.query(cond, page, json);

			GlobalUtil.setTableRowNoEx(page_,page_.getPageRows());
			return page_;
		} catch (Exception e) {
			e.printStackTrace();
			PageManagerEx<Map<String, Object>> res = new PageManagerEx<Map<String, Object>>();
			res.setExtStr1(e.getMessage());
			return res;
		}
	}

	/**
	 * 数据下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "excelDownload.action")
	public void excelDownload( HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String dataSrc = request.getParameter("data_src_");
			String json = request.getParameter("condJson");
			this.queryService.exportExcelFile(dataSrc,json,response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Session 中的 data_src_ 不能为空");
		}
	}

	/*获取子表数据*/
	@RequestMapping(value = "loadPromeryData.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManagerEx<Map<String, Object>> loadPromeryData(PageManager page, HttpServletRequest request) {
		try {
			String dataSrc = request.getParameter("data_src");
			if (dataSrc == null || dataSrc.trim().length() <= 0) {
				dataSrc = (String) request.getSession().getAttribute("data_src_");
				if (dataSrc == null || dataSrc.trim().length() <= 0) {
					throw new Exception("Session 中的 data_src_ 不能为空");
				}
			}
			String json = request.getParameter("condJson");

//			String json = request.getParameter("condJson");
			DlhDataobject cond = new DlhDataobject();
			cond.setObjUrl(dataSrc);
			PageManagerEx page_ = queryService.query(cond, page, json);
			GlobalUtil.setTableRowNoEx(page_, page_.getPageRows());
			return page_;
		} catch (Exception e) {
			e.printStackTrace();
			PageManagerEx<Map<String, Object>> res = new PageManagerEx<Map<String, Object>>();
			res.setExtStr1(e.getMessage());
			return res;
		}
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageDetail.action", method = RequestMethod.GET)
	public String manageDetail(DlhDataobject cond, HttpServletRequest request) throws Exception {
		DlhDataobject obj = dlhDataobjectService.get(cond);
		DlhDataobjectField fieldCond = new DlhDataobjectField();
		fieldCond.setObjectId(obj.getId());
		fieldCond.addOrderByField("t.field_seq");
		List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
		if (fieldList != null) {
//			buildCondArea(fieldList);
			String key = request.getParameter("key");
			String value = request.getParameter("vl");
//            request.setAttribute("key",key);
//            request.setAttribute("value",value);
			List<Map<String, Object>> info = buildCondArea(fieldList, key, value);
			request.setAttribute("pageCondArea", info);
			request.setAttribute("pageCondAreaSize", info.size());
			request.setAttribute("pageListArea", buildListArea(fieldList));
		}
		//地址栏中配置了参数source,为project时，进入判断
		String source = request.getParameter("source");
		String pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > </span>";
		if(obj.getObjName()!=null&&!obj.getObjName().equals("")){
			if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_evaluation_info")) {
				pageHeader = "<span>资源中心</span><span> 诚信信息库> "+obj.getObjName()+" </span>";
			}
			if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_evaluation_person")) {
				pageHeader = "<span>资源中心</span><span> 诚信信息库> "+obj.getObjName()+" > </span>";
			}

		}
		if (source != null && source.equals("project")) {
			pageHeader = "<span>项目管理</span><span>招投标监管";
			if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_bidding_info")) {
				pageHeader += " > </span><span>招标信息</span>";
			} else if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_winbidding_info")) {
				pageHeader += " > </span><span>中标信息</span>";
			} else {
				pageHeader += "</span>";
			}
		}
		if (source != null && source.equals("safe")) {
			pageHeader = "<span>全生命周期</span>";
			if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_company_info")) {
				pageHeader += "<span>数据串联监管 ></span><span>生命周期监管信息 ></span><span>企业动态监管</span>";
			}
			if (cond.getObjUrl() != null && cond.getObjUrl().equals("pt_person_info")) {
				pageHeader += "<span>数据串联监管 ></span><span>生命周期监管信息 ></span>`<span>人员动态监管</span>";
			}

		}
		if(request.getParameter("vl")!=null&&!request.getParameter("vl").equals("")){
			if("person_jslx_01".equals(request.getParameter("vl"))){
				pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > 注册人员 </span>";
			}
			if("person_jslx_02".equals(request.getParameter("vl"))){
				pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > 三类人员 </span>";
			}
			if("person_jslx_03".equals(request.getParameter("vl"))){
				pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > 八大员 </span>";
			}
			if("person_jslx_04".equals(request.getParameter("vl"))){
				pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > 技术工人 </span>";
			}
			if("person_jslx_05".equals(request.getParameter("vl"))){
				pageHeader = "<span>资源中心</span><span>"+obj.getObjName()+" > 其他人员 </span>";
			}
		}
		request.setAttribute("pageHeader", pageHeader);
		request.setAttribute("objectTitle",obj.getObjName());


        request.getSession().setAttribute("data_src_", obj.getObjUrl());
		request.getSession().setAttribute("data_field_", sort(fieldList));
		return "dlh/query/dlhDataDetailList";
	}

	/**
	 * @description 加載附件列表
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "queryFileList.action", method = RequestMethod.GET)
	public String queryFileList(DlhDataobject cond, HttpServletRequest request) throws Exception {
//		DlhDataobject obj = dlhDataobjectService.get(cond);
		String objUrl = request.getParameter("objUrl");
		if (objUrl == null || objUrl.trim().length() == 0) {
			throw new Exception("模型路径不能为空");
		}
		String dataId = request.getParameter("dataId");
		if (dataId == null || dataId.trim().length() == 0) {
			throw new Exception("数据ID不能为空");
		}
		/*标识字段名称*/
		String yewucol = request.getParameter("cl");
		if (yewucol == null || yewucol.trim().length() == 0) {
			throw new Exception("业务标识不能为空");
		}
		DlhFile dlhFile = new DlhFile();
		dlhFile.setObjurl(objUrl);
		dlhFile.setDeleteFlag(0);
		dlhFile.setYewucolname(yewucol);
		dlhFile.setYewuid(dataId);
		List<DlhFile> dlhFileList = this.dlhFileManageService.queryAll(dlhFile);
		request.setAttribute("dlhFileList",dlhFileList);



		/*List<Attach> attachList = new ArrayList<Attach>();
		if(dlhFileList!=null&&dlhFileList.size()>0){
			for(DlhFile temp: dlhFileList){
				String value = temp.getId();
				Attach attach = new Attach();
				if(value==null||"".equals(value)){
					value = "0";
				}
				attach.setBusinessId(value);
				attach.setBusinessTable(objUrl);
				attach.setIsPaged("1");
				attach.setCategory(objUrl);
				attachList = attachService.queryAll(attach);
			}
		}
		request.setAttribute("attachList",attachList);*/

		return "dlh/query/dlhAttachList";
	}


	/*获取子表数据*/
	@RequestMapping(value = "getMindData.action", method = RequestMethod.GET)
	@ResponseBody
	public List<MindDataModel> getMindData(HttpServletRequest request){
		String dlh_data_src_ =request.getParameter("dlh_data_src_");
		String dlhDataId =request.getParameter("dlhDataId");
		DlhDataobject dlhDataobject = new DlhDataobject();
		dlhDataobject.setObjUrl(dlh_data_src_);
		List<MindDataModel> mindDataModelList = new ArrayList<MindDataModel>();
		try {
			dlhDataobject=dlhDataobjectService.get(dlhDataobject);
			DlhDataobjectField fieldCond = new DlhDataobjectField();
			fieldCond.setObjectId(dlhDataobject.getId());
			fieldCond.addOrderByField("t.field_seq");
			List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
			List<Map<String, String>> dataMapList1 = queryService.queryById(dlhDataId, fieldList, null);

			MindDataModel mindDataModel = new MindDataModel();
			mindDataModel.setId(dlhDataId);
			mindDataModel.setIsroot(true);
			mindDataModel.setObjUrl(dlh_data_src_);
			mindDataModel.setTopic(dlhDataobject.getObjName());
			mindDataModel.setTableCode(dlhDataobject.getTableCode());

			mindDataModel.setData(dataMapList1);
			mindDataModelList.add(mindDataModel);
			getMindChildrenData(mindDataModel,mindDataModelList);

		} catch (CustomException e) {
			e.printStackTrace();
		}
		return mindDataModelList;

	}

	public void getMindChildrenData(MindDataModel mindDataModel,List<MindDataModel> mindDataModelList){


		/*获取子表*/
		DlhTableMap tableMap = new DlhTableMap();
		tableMap.setTableNameK(mindDataModel.getTableCode());
		try {
			List<DlhTableMap> dlhTableMapList = dlhTableMapService.queryAll(tableMap);
			if(dlhTableMapList==null||dlhTableMapList.size()<=0){
				return ;
			}
			/*便利所有子表*/
			for (DlhTableMap tm :dlhTableMapList){
				String tableNameV = tm.getTableNameV();
				DlhDataobject tem = new DlhDataobject();
				tem.setTableCode(tableNameV);
				/*获取子表对象*/
				List<DlhDataobject> dlhDataobjectList=dlhDataobjectService.queryAll(tem);
				if(dlhDataobjectList==null||dlhDataobjectList.size()<=0){
					return;
				}
				tem = dlhDataobjectList.get(0);
				/*获取子表所有字段*/
				DlhDataobjectField fieldCond = new DlhDataobjectField();
				fieldCond.setObjectId(dlhDataobjectList.get(0).getId());
				fieldCond.addOrderByField("t.field_seq");
				List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
				for (DlhDataobjectField field:fieldList){
					if(field.getItemName().equals(tm.getColumnNameV())){
						fieldCond = field;
					}
				}
				/*获取子表数据*/
				List<Map<String,String>> condlist = new ArrayList<Map<String,String>>();
				Map<String,String> condMap = new HashMap<>();
				condMap.put("operationKey",tm.getColumnNameV());
				condMap.put("operationType",fieldCond.getItemType());
				condMap.put("value",mindDataModel.getValueByName(tm.getColumnNameK()));
				condlist.add(condMap);
				String json = JsonUtil.java2Json(condlist);
				PageManager page = new PageManager();
				page.setPageRows(999);
				PageManagerEx<Map<String, Object>> query = queryService.query(tem, page, json);
				List<Map<String, Object>> pageData = query.getData();

				for (Map<String, Object> datamap:pageData){
					MindDataModel mindDataModelnew = new MindDataModel();
					mindDataModelnew.setId(datamap.get("dlh_data_id_")+"");
					mindDataModelnew.setIsroot(false);
					mindDataModelnew.setObjUrl(tem.getObjUrl());
					mindDataModelnew.setTopic(tem.getObjName());
					mindDataModelnew.setTableCode(tem.getTableCode());
					mindDataModelnew.setParentid(mindDataModel.getId());
					mindDataModelnew.setExpanded(true);
//					mapObjectToString(datamap,)
					mindDataModelnew.setData(mapObjectToString(datamap));
//					mindDataModelnew.getData().add()
					mindDataModelList.add(mindDataModelnew);
					getMindChildrenData(mindDataModelnew,mindDataModelList);
				}
//				List<Map<String, String>> dataMapList1 = queryService.queryById(mindDataModel.getId(), fieldList, null);


			}

		} catch (CustomException e) {
			e.printStackTrace();
		}

		return ;
	}

	public List<Map<String,String>> mapObjectToString(Map<String, Object> objectMap){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for (Map.Entry<String, Object> item : objectMap.entrySet()) {

			Map<String,String> map = new HashMap<>();

			map.put("itemName",item.getKey());
			map.put("value",item.getValue().toString());
			list.add(map);

		}
		return list;
	}


	/**
	 * @description 子表显示
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "loadTableMapPage.action", method = RequestMethod.GET)
	public String loadTableMapPage(DlhDataobject cond, HttpServletRequest request) throws Exception {
		DlhDataobject obj = dlhDataobjectService.get(cond);
		DlhDataobjectField fieldCond = new DlhDataobjectField();
		request.getSession().setAttribute("data_src_", obj.getObjUrl());
		request.setAttribute("dlh_data_src_",obj.getObjUrl());
		String dlhDataId = request.getParameter("dlhDataId");
		String dlh_data_src_ = request.getParameter("dlh_data_src_");
		request.setAttribute("dlhDataId",dlhDataId);
		request.setAttribute("dlh_data_src_",dlh_data_src_);
		if(!StringUtil.isEmpty(obj.getObjUrl())&&obj.getObjUrl().equals("pt_relations_img")){
			return "dlh/query/relationsImgView";
		}

		fieldCond.setObjectId(obj.getId());
		fieldCond.addOrderByField("t.field_seq");
		List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(fieldCond);
		if (fieldList != null) {
			List<Map<String, Object>> info = buildCondArea(fieldList, null,null);
//			request.setAttribute("pageCondArea", info);
//			request.setAttribute("pageCondAreaSize", info.size());
			request.setAttribute("pageListArea", buildListArea(fieldList));
		}

		request.getSession().setAttribute("data_field_", sort(fieldList));
		return "dlh/query/loadTableMapPage";
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "loadDetail.action", method = RequestMethod.GET)
	public String loadDetail(HttpServletRequest request) throws Exception {
		@SuppressWarnings("unchecked")
//		List<DlhDataobjectField> fieldList = (List<DlhDataobjectField>) request.getSession().getAttribute("data_field_");
//		if (fieldList == null || fieldList.size() <= 0) {
//			throw new Exception("Session 中的 data_field_ 不能为空");
//		}
		String dlhDataId = request.getParameter("dlhDataId");
		if (dlhDataId == null || dlhDataId.trim().length() == 0) {
			throw new Exception("不能为空");
		}

		request.setAttribute("dlhDataId",dlhDataId);

		DlhTableMap tableMap = new DlhTableMap();
		String dlh_data_src_ =request.getParameter("dlh_data_src_");
		request.setAttribute("dlh_data_src_",dlh_data_src_);
		/*平台关系图特殊处理*/
		if(dlh_data_src_.equalsIgnoreCase("pt_relations_img")){

		}
		DlhDataobject dlhDataobject = new DlhDataobject();
		dlhDataobject.setObjUrl(dlh_data_src_);
		dlhDataobject=dlhDataobjectService.get(dlhDataobject);
		tableMap.setTableNameK(dlhDataobject.getTableCode());
		tableMap.addOrderByField("t.EXT_NUM1");
		List<DlhTableMap> dlhTableMapList = dlhTableMapService.queryAll(tableMap);
		List<Map<String,String>> mapList = new ArrayList<Map<String, String>>() ;
		for (DlhTableMap tm :dlhTableMapList){
			Map<String,String> map = new HashMap<String ,String>();
			String tableNameV = tm.getTableNameV();
			DlhDataobject tem = new DlhDataobject();
			tem.setTableCode(tableNameV);
			List<DlhDataobject> dlhDataobjectList=dlhDataobjectService.queryAll(tem);
			if(dlhDataobjectList!=null&&dlhDataobjectList.size()>0){
				tem=dlhDataobjectList.get(0);
				String objName = tem.getObjName();
				map.put("tabName",objName);
				map.put("tableCode",tm.getTableNameV());
				map.put("colNameV",tm.getColumnNameV());
				map.put("colNameK",tm.getColumnNameK());
				map.put("value","");
				map.put("itemType","");
				map.put("objUrl",tem.getObjUrl());
				map.put("modelId",tem.getModelId());
				map.put("id",tem.getId());
			}else{
				map.put("tabName","");
				map.put("tableCode","");
				map.put("colName","");
				map.put("objUrl","");
				map.put("colNameK",tm.getColumnNameK());
				map.put("modelId","");
				map.put("id","");
				map.put("value","");
				map.put("itemType","");
			}
			mapList.add(map);
		}
		List<Map<String, String>> formDataList = new ArrayList<Map<String,String>>() ;
		DlhDataobjectField field = new DlhDataobjectField();
		field.setObjectId(dlhDataobject.getId());
		field.addOrderByField("t.field_seq");
		List<DlhDataobjectField> fieldList = dlhDataobjectFieldService.queryAll(field);
		List<Map<String, String>> dataMapList1 = queryService.queryById(dlhDataId, fieldList, null);
		if(dataMapList1!=null&&dataMapList1.size()>0){
			for(Map<String,String> tempmap :dataMapList1){
				String colunName = tempmap.get("itemName");
				if(mapList!=null&&mapList.size()>0){

					for(Map<String,String> keyMap:mapList){
						String colunK = keyMap.get("colNameK");
						if(colunName.equalsIgnoreCase(colunK)){
							keyMap.put("value",tempmap.get("value"));
							keyMap.put("itemType",tempmap.get("itemType"));
						}else{

						}
					}
				}
				if(tempmap.get("formShow")!=null&&!tempmap.get("formShow").equals("null")&&!tempmap.get("formShow").equals("")){
					if (Integer.parseInt(tempmap.get("formShow"))>0) {
						formDataList.add(tempmap);
					} else {
						continue;
					}
				}
			}
		}




		request.setAttribute("tabList",mapList);
		request.setAttribute("listSize",mapList==null?0:mapList.size());
		request.setAttribute("formData",formDataList);
		request.setAttribute("objectTitle",dlhDataobject.getObjName());
		String view = request.getParameter("viewType");
		if (!StringUtil.isEmpty(view)&&view.equals("view")) {
			return "dlh/query/dlhTableMapForm";
		} else {
			//地址栏中配置了参数source,为project时，进入判断
			String source = request.getParameter("source");
			String pageHeader = "<span>资源中心</span><span>"+dlhDataobject.getObjName()+" > </span><span>详细信息</span>";


				if (dlhDataobject.getObjUrl() != null && dlhDataobject.getObjUrl().equals("pt_bidding_info")) {
					pageHeader = "<span>项目管理</span><span>招投标监管 ></span><span>招标信息 ></span><span>详细信息</span>";
				}
				if (dlhDataobject.getObjUrl() != null && dlhDataobject.getObjUrl().equals("pt_winbidding_info")) {
					pageHeader = "<span>项目管理</span><span>招投标监管 ></span><span>中标信息 ></span><span>详细信息</span>";
				}

			request.setAttribute("objectTitle",dlhDataobject.getObjName());
			request.setAttribute("pageHeader", pageHeader);
			return "dlh/query/dlhDataQueryForm";
		}
	}

	/**
	 * @Document 条件区
	 * @param fieldList
	 * @param key
     * @param value
     * @return
	 */
	private List<Map<String, Object>> buildCondArea(List<DlhDataobjectField> fieldList, String key, String value) {
		List<DlhDataobjectField> fieldListNew = new ArrayList<>();
		for (DlhDataobjectField field : fieldList) {
			if (field.getFieldCondShow() != null && field.getFieldCondShow() > 0) {
				fieldListNew.add(field);
			}else {
				if(!StringUtil.isEmpty(key)&&!StringUtil.isEmpty(value)){
					if(key.equals(field.getItemName())){
						fieldListNew.add(field);
					}
				}
			}
		}
		Collections.sort(fieldListNew, new Comparator<DlhDataobjectField>() {
			@Override
			public int compare(DlhDataobjectField o1, DlhDataobjectField o2) {
				return o1.getFieldSeq().compareTo(o2.getFieldSeq());
			}

		});
		List<Map<String, Object>> jspCondInfo = new ArrayList<Map<String, Object>>();
		for (DlhDataobjectField field : fieldListNew) {
			String title = field.getFieldName();
			if (title == null || title.trim().length() <= 0) {
				title = field.getFieldCode();
			}

			MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
			Map<String, Object> condLine = new HashMap<String, Object>();

			//是否为默认查询条件
			condLine.put("defaultCon","");
            if(!StringUtil.isEmpty(key)&&!StringUtil.isEmpty(value)){
                if(key.equals(field.getItemName())){
                    condLine.put("defaultCon",key);
                    condLine.put("defaultValue",value);
                }
            }
            if (field.getItemType()!=null&&field.getItemType().equals("dic")){
                String dicCode = field.getItemDicCode();
                Dic dic = new Dic();
                dic.setParentId(dicCode);
                List<Dic> dicList = dicService.query(dic);
                condLine.put("dicList", dicList);
            }
			condLine.put("title", title);
			condLine.put("operationType", field.getItemType());
			Map<String, Map<String, Object>> condList = new HashMap<String, Map<String, Object>>();
			condList.putAll(ctype.getService().getCondOnDisplay(field.getItemName(), field.getItemType()));
			condLine.put("cond", condList);
			jspCondInfo.add(condLine);
		}
		return jspCondInfo;

	}

	/**
	 * @document 列表区域
	 * @param fieldList
	 * @return
	 */
	private List<Map<String, Object>> buildListArea(List<DlhDataobjectField> fieldList) {
		List<DlhDataobjectField> fieldListNew = new ArrayList<>();
		for (DlhDataobjectField field : fieldList) {
			if (field.getFieldListShow() != null && field.getFieldListShow() > 0) {
				fieldListNew.add(field);
			}
		}
		Collections.sort(fieldListNew, new Comparator<DlhDataobjectField>() {
			@Override
			public int compare(DlhDataobjectField o1, DlhDataobjectField o2) {
				return o1.getFieldSeq().compareTo(o2.getFieldSeq());
			}

		});
		List<Map<String, Object>> jspListInfo = new ArrayList<Map<String, Object>>();
		for (DlhDataobjectField field : fieldListNew) {
			String title = field.getFieldName();
			if (title == null || title.trim().length() <= 0) {
				title = field.getFieldCode();
			}
			Map<String, Object> line = new HashMap<String, Object>();
			line.put("title", title);
			line.put("operationType", field.getItemType());
			line.put("code", field.getItemName());
			jspListInfo.add(line);
		}
		return jspListInfo;

	}

	/**
	 * @document 排序
	 * @param fieldList
	 * @return
	 */
	private List<DlhDataobjectField> sort(List<DlhDataobjectField> fieldList) {

		Collections.sort(fieldList, new Comparator<DlhDataobjectField>() {
			@Override
			public int compare(DlhDataobjectField o1, DlhDataobjectField o2) {
				if (o1.getFieldFormShow() == null && o2.getFieldFormShow() == null) {
					return 0;
				}
				if (o1.getFieldFormShow() == null) {
					return -1;
				}
				if (o2.getFieldFormShow() == null) {
					return 1;
				}
				return o1.getFieldFormShow().compareTo(o2.getFieldFormShow());

			}

		});
		return fieldList;

	}

}
package com.jc.supervise.gis.web;

import com.jc.common.db.dialect.mysql.MysqlColumnType;
import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.service.IProjectInfoService;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.domain.DlhTableMap;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhDateQueryService;
import com.jc.dlh.service.IDlhTableMapService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.supervise.gis.vo.GisVo;
import com.jc.supervise.gis.vo.ProjectGisVo;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * 监管GIS相关信息
 * @Author 常鹏
 * @Date 2020/8/14 18:39
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervise/gis")
public class SuperviseGisController extends BaseController {

    @Autowired
    private IDicManager dicManager;
    @Autowired
    private IProjectInfoService projectInfoService;//项目信息

    //数据查询
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

    private String arcGisAddress = GlobalContext.getProperty("arc.gis.address");//地图服务地址
    private String scsAddress = GlobalContext.getProperty("scs.server");//scs地址

    /**
     * gis关联项目
     * @param request
     * @param model
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "/project/page.action", method = RequestMethod.GET)
    public String pageProject(HttpServletRequest request,Model model,ProjectInfo projectInfo){

        //在建竣工-数据字典
        List<Dic> compList = dicManager.getDicsByTypeCode(DicKeyEnum.completion.getTypeCode(),DicKeyEnum.completion.getParentCode());
        model.addAttribute("compList",compList);

        /*********企业占比begin********/
        //行政区-数据字典
        List<Dic> regionList = dicManager.getDicsByTypeCode(DicKeyEnum.region.getTypeCode(),DicKeyEnum.region.getParentCode());
//        model.addAttribute("regionList",regionList);

        Map<String,GisVo> areaMap = new LinkedHashMap<String,GisVo>();
        //初始化区域map
        for(Dic dic : regionList){
            areaMap.put(dic.getValue(),new GisVo());
        }

        List<GisVo> gisVoList = projectInfoService.queryCompanyForArea();//查询区域各合同分类企业数量

        Integer companyTotal = 0;//企业总数
        //计算合同分类个企业数量
        for(GisVo gv : gisVoList){
            GisVo gisVo = areaMap.get(gv.getAreaName());
            companyTotal += gv.getCc();//企业总数
            gisVo.setCompanyTotal(companyTotal);
            if(gv.getPcpHtlb().equals("1")){
                gisVo.setCompType1(gisVo.getCompType1() + gv.getCc());
            }else if(gv.getPcpHtlb().equals("2")){
                gisVo.setCompType2(gisVo.getCompType2() + gv.getCc());
            }else if(gv.getPcpHtlb().equals("3")){
                gisVo.setCompType3(gisVo.getCompType3() + gv.getCc());
            }else if(gv.getPcpHtlb().equals("4")){
                gisVo.setCompType4(gisVo.getCompType4() + gv.getCc());
            }
            gisVo.setAreaTotal(gisVo.getCompType1() + gisVo.getCompType2() + gisVo.getCompType3() + gisVo.getCompType4());//区域企业总数
            areaMap.put(gv.getAreaName(),gisVo);
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        //计算企业占比
        for(Dic dic : regionList){
            GisVo gisVo = areaMap.get(dic.getValue());
            Integer areaTotal = gisVo.getAreaTotal();
            String proportion1 = "0%";
            if(areaTotal!=0){
                proportion1 = numberFormat.format((float)areaTotal/(float)companyTotal* 100)+"%";//所占百分比
            }
            gisVo.setProportion1(proportion1);
            areaMap.put(dic.getValue(),gisVo);
        }

        model.addAttribute("areaMap",areaMap);
        /*********企业占比end********/

        //高层建筑-数据字典
        List<Dic> highList = dicManager.getDicsByTypeCode(DicKeyEnum.highBuild.getTypeCode(),DicKeyEnum.highBuild.getParentCode());
        model.addAttribute("highList",highList);

        //投资分类-数据字典
        List<Dic> investmentList = dicManager.getDicsByTypeCode(DicKeyEnum.investmentCategory.getTypeCode(),DicKeyEnum.investmentCategory.getParentCode());
        model.addAttribute("investmentList",investmentList);

        //项目分类-数据字典
        List<Dic> projectCateList = dicManager.getDicsByTypeCode("projectCate","csmp");
        model.addAttribute("projectCateList",projectCateList);


        /*********产值/计划投资完成情况begin********/
        Map<String,GisVo> areaCzMap = new LinkedHashMap<String,GisVo>();
        //初始化区域map
        for(Dic dic : regionList){
            areaCzMap.put(dic.getValue(),new GisVo());
        }
        List<GisVo> gisVoCzList = projectInfoService.queryMoneyForArea();//查询投资完成情况及产值

        BigDecimal czTotal = new BigDecimal(0);//总产值
        BigDecimal wcjeTotal = new BigDecimal(0);//总完成金额

        //计算总产值及总完成金额
        for(GisVo gv : gisVoCzList) {
            GisVo gisVo = areaCzMap.get(gv.getAreaName());
            czTotal = czTotal.add(gv.getProductionTotal());
            wcjeTotal = wcjeTotal.add(gv.getAmountCompleted());
            areaCzMap.put(gv.getAreaName(),gv);
        }

        //计算占比
        for(Dic dic : regionList){
            GisVo gisVo = areaCzMap.get(dic.getValue());

            BigDecimal productionTotal = gisVo.getProductionTotal();//区域产值
            BigDecimal amountCompleted = gisVo.getAmountCompleted();//区域完成金额

            String proportion1 = "0%";
            if(productionTotal.compareTo(BigDecimal.ZERO)!=0){
                proportion1 = numberFormat.format(productionTotal.floatValue() /czTotal.floatValue()* 100)+"%";//产值所占百分比
            }
            gisVo.setProportion1(proportion1);

            String proportion2 = "0%";
            if(amountCompleted.compareTo(BigDecimal.ZERO)!=0){
                proportion2 = numberFormat.format(amountCompleted.floatValue() /wcjeTotal.floatValue()* 100)+"%";//投资完成金额所占百分比
            }
            gisVo.setProportion2(proportion2);

            areaCzMap.put(dic.getValue(),gisVo);
        }

        model.addAttribute("areaCzMap",areaCzMap);
        /*********产值/计划投资完成情况end********/

        request.setAttribute("arcGisAddress", arcGisAddress);

        return "supervise/gis/project/gisProject";
    }

    /**
     * gis关联企业
     * @param request
     * @param model
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "/company/page.action", method = RequestMethod.GET)
    public String pageCompany(HttpServletRequest request,Model model, ProjectInfo projectInfo) {
        //企业类型-数据字典
        List<Dic> compList = dicManager.getDicsByTypeCode(DicKeyEnum.companyType.getTypeCode(),DicKeyEnum.companyType.getParentCode());
        model.addAttribute("compList",compList);

        request.setAttribute("arcGisAddress", arcGisAddress);

        return "supervise/gis/company/gisCompany";
    }

    /**
     * gis关联工地
     * @param request
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "/site/page.action", method = RequestMethod.GET)
    public String pageSite(HttpServletRequest request,ProjectInfo projectInfo) {
        request.setAttribute("arcGisAddress", arcGisAddress);
        request.setAttribute("scsAddress", scsAddress);
        return "supervise/gis/site/gisSite";
    }

    /**
     * 查询所有项目信息
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "projectInfoList.action")
    @ResponseBody
    public List<ProjectInfo> projectInfoList(ProjectInfo projectInfo){
        //项目信息
        List<ProjectInfo> projectList = null;
        try {
            projectList = projectInfoService.queryAll(projectInfo);
        } catch (CustomException e) {
            e.printStackTrace();
        }

        return projectList;
    }

    /**
     * 查询项目坐标(移动端接口)
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "projectGisList.action")
    @ResponseBody
    public String projectGis(ProjectInfo projectInfo){
        String projectGis = "";
        List<ProjectGisVo> projectGisList = new ArrayList<ProjectGisVo>();
        //项目信息
        List<ProjectInfo> projectList = null;
        try {
            projectInfo.addOrderByFieldDesc("t.projectNumber");
            projectList = projectInfoService.projectJwdList(projectInfo);
            for(int i=0; i < projectList.size(); i++){
                ProjectInfo pinfo = projectList.get(i);
                ProjectGisVo projectGisVo = new ProjectGisVo();
                projectGisVo.setId(String.valueOf(i+1));
                projectGisVo.setXpoint(pinfo.getLongitude());//经度
                projectGisVo.setYpoint(pinfo.getLatitude());//纬度
                projectGisVo.setTitle(pinfo.getProjectname());//项目名称
//                projectGisVo.setContent("http://124.235.224.2:6080/arcgis/rest/services/CHCGML/A04M_1W/MapServer");
                projectGisVo.setAttrBox("test");
                projectGisVo.setProjectId(pinfo.getDlhDataId());//项目ID
                projectGisVo.setProjectNumber(pinfo.getProjectnumber());//项目编号
                projectGisList.add(projectGisVo);
            }
            projectGis = JsonUtil.java2Json(projectGisList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return projectGis;
    }

    /**
     * 根据字典分类查询项目坐标信息
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "projectListByType.action")
    @ResponseBody
    public List<ProjectInfo> projectListByType(ProjectInfo projectInfo){
        List<ProjectInfo> projectList = null;
        try {
            projectInfo.addOrderByFieldDesc("t.projectNumber");
            projectList = projectInfoService.projectJwdList(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * 根据阶段查询项目坐标信息
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "projectListByStage.action")
    @ResponseBody
    public List<ProjectInfo> projectListByStage(ProjectInfo projectInfo){
        List<ProjectInfo> projectList = null;
        try {
            projectInfo.addOrderByFieldDesc("t.projectNumber");
            projectList = projectInfoService.projectByStageList(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * 查询项目企业坐标
     * @param projectInfo
     * @return
     */
    @RequestMapping(value = "projectCompByType.action")
    @ResponseBody
    public List<ProjectInfo> projectCompByType(ProjectInfo projectInfo){
        //项目信息
        List<ProjectInfo> projectList = null;
        try {
//            projectInfo.addOrderByFieldDesc("t.projectNumber");
            projectList = projectInfoService.projectCompJwdList(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }

    /**
     * 工地栏目页面
     * @return
     */
    @RequestMapping(value = "/loadSite.action", method = RequestMethod.GET)
    public String loadSite() {
        return "supervise/gis/site/loadSite";
    }

    /**
     *塔式起重机查看
     * @return
     */
    @RequestMapping(value = "/tower/page.action", method = RequestMethod.GET)
    public String pageTower() {
        return "supervise/gis/site/towerView";
    }

    /**
     * 施工升降机查看
     * @return
     */
    @RequestMapping(value = "/hoist/page.action", method = RequestMethod.GET)
    public String pageHoist() {
        return "supervise/gis/site/hoistView";
    }


    /**************数据模型-数据查询begin***************/

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
        return "supervise/gis/dlh/tableMapPageView";
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
        if (!StringUtil.isEmpty(view)&&view.equals("queryView")) {
            return "supervise/gis/dlh/dlhDataQueryView";
        } else if(!StringUtil.isEmpty(view)&&view.equals("childView")) {
            return "supervise/gis/dlh/dlhDataQueryChildView";
        }
        return "supervise/gis/dlh/dlhDataQueryView";
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
    /**************数据模型-数据查询end***************/

}

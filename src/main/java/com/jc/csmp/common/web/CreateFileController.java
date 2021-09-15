package com.jc.csmp.common.web;

import com.jc.archive.ArchiveException;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Folder;
import com.jc.archive.service.IArchiveFolderService;
import com.jc.common.kit.ExcelBuilder;
import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.common.WordUtil;
import com.jc.csmp.common.enums.CreateFileEnum;
import com.jc.csmp.common.enums.ProjectStageForder;
import com.jc.csmp.project.domain.*;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.service.*;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成文件并归档
 * @Author 常鹏
 * @Date 2020/8/31 11:09
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/create/file")
public class CreateFileController extends BaseController {
    @Autowired
    private ICmProjectRelationOrderService cmProjectRelationOrderService;
    @Autowired
    private ICmProjectChangeOrderService cmProjectChangeOrderService;
    @Autowired
    private ICmProjectVisaOrderService cmProjectVisaOrderService;
    @Autowired
    private ICmProjectQuestionService cmProjectQuestionService;
    @Autowired
    private ICmProjectPlanService cmProjectPlanService;
    @Autowired
    private ICmProjectInfoService cmProjectInfoService;

    @Autowired
    private IArchiveFolderService folderService;

    /**
     * 创建文件及归档
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="createFile.action",method= RequestMethod.GET)
    @ResponseBody
    public Result createFile(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        String busId = request.getParameter("busId");
        Map<String, Object> fileParam = null;
        try {
            if (type.equals(ProjectStageForder.relation.toString())) {
                //工程联系单
                fileParam = createRelation(busId, type);
            } else if (ProjectStageForder.change.getCode().equals(type)) {
                //变更单
                fileParam = createChange(busId, type);
            } else if (ProjectStageForder.visa.getCode().equals(type)) {
                fileParam = createVisa(busId, type);
            } else if (ProjectStageForder.sceneQuestion.getCode().equals(type)) {
                fileParam = sceneQuestion(busId, type);
            } else if (ProjectStageForder.plan.getCode().equals(type)) {
                fileParam = projectPlan(busId, type);
            }
            ResVO resVO = saveFolder(fileParam);
            Result res = Result.success(fileParam);
            if(resVO.getSuccess()){
                try {
                    folderService.uploadAttach(fileParam,request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.failure(1, e.getMessage());
                }
                resVO = this.uploadDocsApi(fileParam,request);
                if(resVO.getSuccess()){
                    res.setMsg("归档成功");
                }else{
                    res = Result.failure(-1,resVO.getMessage());
                }
            }else{
                res = Result.failure(-1,resVO.getMessage());
            }
            return res;
        } catch (Exception ex) {
            return Result.failure(1, ex.getMessage());
        }
    }

    //创建文档目录
    public ResVO saveFolder(Map<String,Object> data){

        Map<String, Object> infoMap = data;

        String folderType=infoMap.get("folderType")+"";
        if(StringUtil.isEmpty(folderType)){
            folderType="5";
        }
        Folder checkFolder = new Folder();
        checkFolder.setDeleteFlag(0);
        checkFolder.setExtStr1(infoMap.get("projectNumber")+"");
        checkFolder.setExtStr2("-1");
        checkFolder.setFolderName((String) infoMap.get("projectName"));
        checkFolder.setFolderType(folderType);

        try {
            folderService.saveFolder(infoMap);

        } catch (CustomException e) {
            return ResVO.buildFail("校验重名出现错误！"+e.getMessage());
        }


        return ResVO.buildSuccess();
    }

    public  ResVO uploadDocsApi( Map<String,Object> data, HttpServletRequest request){

        String folderType = (String) data.get("folderType");//文档类型0-资料管理，5-文档管理
        if(StringUtil.isEmpty(folderType)){
            folderType="5";
        }
        String fileids = (String) data.get("fileids");//文件ID用，号隔开
        if (StringUtil.isEmpty(fileids)){
            return ResVO.buildFail("文件ID不能为空！");
        }
        String code = (String) data.get("type");//文件夹编码
        if (StringUtil.isEmpty(code)){
            return ResVO.buildFail("文件code不能为空！");
        }
		String businessId = (String) data.get("businessId");//业务ID
        String projectNum = (String) data.get("projectNumber");//项目编码
        Folder folder = new Folder();

        ProjectStageForder em = ProjectStageForder.getFolderByCode(code);
        if(!folderService.checkAttachName(folder)) {
            return ResVO.buildFail(MessageUtils.getMessage("JC_OA_ARCHIVE_039"));
        }
        try {
            folder.setExtStr1(projectNum);
            folder.setExtStr2(code);
            List<Folder> folderList = folderService.queryAll(folder);
            if (folderList!=null&&folderList.size()>0){
                folder = folderList.get(0);
            }else{
                /*如果没有找到对应文件夹则查找项目根目录*/
                folder.setExtStr1(projectNum);
                folder.setExtStr2("-1");
                folderList = folderService.queryAll(folder);
                if (folderList!=null&&folderList.size()>0){
                    folder = folderList.get(0);
                    /*创建文件夹*/
                    Folder newfolder = new Folder();
                    newfolder.setFolderPath("根目录/" + folder.getFolderName() + "/" + em.getName());
                    newfolder.setFolderName(em.getName());
                    newfolder.setParentFolderId(folder.getId());
                    newfolder.setFolderType(folderType);
                    newfolder.setKmAppFlag("0");
                    newfolder.setDmInRecycle(0);
                    newfolder.setModel(0);
                    newfolder.setExtStr1(projectNum);
                    newfolder.setExtStr2(code);
                    newfolder.setFileids(fileids);
                    folderService.save(newfolder);
                    folder=newfolder;

                }else{
                    return ResVO.buildFail("文件夹未找到:"+projectNum+" code:"+code);
                }

            }
            folder.setFileids(fileids);
            List<Document> documents = folderService.uploadDocs(folder, request);
            request.setAttribute("documents",documents);
        } catch (ArchiveException e) {
            e.printStackTrace();
            return ResVO.buildFail("上传文件错误!"+e.getMessage());
        } catch (CustomException e) {
            return ResVO.buildFail(e.getMessage());
        }
        return ResVO.buildSuccess();
    }

    /**
     * 工程联系单
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> createRelation(String id, String type) throws Exception {
        CmProjectRelationOrder entity = this.cmProjectRelationOrderService.getById(id);
        if (null == entity) {
            return null;
        }
        String filePath = getAbsolutePath();
        Map<String, Object> dataMap = new HashMap<>(5);
        dataMap.put("code", entity.getCode());
        dataMap.put("projectName", entity.getProjectName());
        dataMap.put("signerDeptValue", entity.getSignerDeptValue());
        dataMap.put("content", setEmpty(entity.getContent()));
        List<Map<String, Object>> receiveMap = entity.getReceiverDeptValue();
        String receive = "";
        if (receiveMap != null && receiveMap.size() > 0) {
            for (Map<String, Object> map : receiveMap) {
                receive += "," + map.get("text");
            }
        }
        if (receive.length() > 0) {
            dataMap.put("otherDept", receive.substring(1));
        } else {
            dataMap.put("otherDept", "");
        }
        String fileName = entity.getCode() + ".doc";
        WordUtil.createWord(dataMap, "project_relation.xml", filePath, fileName);
        File file = new File(filePath, fileName);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("businessId", entity.getId());
        resultMap.put("projectId", entity.getProjectId());
        resultMap.put("projectNumber", entity.getProjectNumber());
        resultMap.put("projectName", entity.getProjectName());
        resultMap.put("type", type);
        resultMap.put("fileName", fileName);
        resultMap.put("fileType", "doc");
        resultMap.put("fileSize", file.length());
        resultMap.put("filePath", filePath + "/" + fileName);


        return resultMap;
    }

    /**
     * 工程变更单
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> createChange(String id, String type) throws Exception {
        CmProjectChangeOrder entity = this.cmProjectChangeOrderService.getById(id);
        if (null == entity) {
            return null;
        }
        String filePath = getAbsolutePath();
        Map<String, Object> dataMap = new HashMap<>(5);
        dataMap.put("code", entity.getCode());
        dataMap.put("projectName", entity.getProjectName());
        dataMap.put("companyName", entity.getDeptName());
        dataMap.put("changeDate", GlobalUtil.dateUtil2String(entity.getChangeDate(), "yyyy-MM-dd"));
        dataMap.put("reason", setEmpty(entity.getChangeReason()));
        dataMap.put("content", setEmpty(entity.getChangeContent()));
        String fileName = entity.getCode() + ".doc";
        String templateName = "project_change.xml";
        if (entity.getChangeType().equals("2")) {
            dataMap.put("contractCode", entity.getContractCode());
            dataMap.put("contractName", entity.getContractName());
            templateName = "contract_change.xml";
        }
        WordUtil.createWord(dataMap, templateName, filePath, fileName);
        File file = new File(filePath, fileName);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("businessId", entity.getId());
        resultMap.put("projectId", entity.getProjectId());
        resultMap.put("projectNumber", entity.getProjectNumber());
        resultMap.put("projectName", entity.getProjectName());
        resultMap.put("type", type);
        resultMap.put("fileName", fileName);
        resultMap.put("fileType", "doc");
        resultMap.put("fileSize", file.length());
        resultMap.put("filePath", filePath + "/" + fileName);
        return resultMap;
    }

    /**
     * 工程签证单
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> createVisa(String id, String type) throws Exception {
        CmProjectVisaOrder entity = this.cmProjectVisaOrderService.getById(id);
        if (null == entity) {
            return null;
        }
        String filePath = getAbsolutePath();
        Map<String, Object> dataMap = new HashMap<>(5);
        dataMap.put("code", entity.getCode());
        dataMap.put("date", GlobalUtil.dateUtil2String(entity.getVisaDate(), "yyyy-MM-dd"));
        dataMap.put("projectName", entity.getProjectName());
        dataMap.put("companyName", entity.getCreateUserDeptValue());
        dataMap.put("reason", setEmpty(entity.getVisaReason()));
        dataMap.put("content", setEmpty(entity.getVisaChange()));
        String fileName = entity.getCode() + ".doc";
        WordUtil.createWord(dataMap, "project_visa.xml", filePath, fileName);
        File file = new File(filePath, fileName);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("businessId", entity.getId());
        resultMap.put("projectId", entity.getProjectId());
        resultMap.put("projectNumber", entity.getProjectNumber());
        resultMap.put("projectName", entity.getProjectName());
        resultMap.put("type", type);
        resultMap.put("fileName", fileName);
        resultMap.put("fileType", "doc");
        resultMap.put("fileSize", file.length());
        resultMap.put("filePath", filePath + "/" + fileName);
        return resultMap;
    }

    /**
     * 安全问题
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    public Map<String, Object> sceneQuestion(String id, String type) throws Exception {
        CmProjectQuestion entity = this.cmProjectQuestionService.getById(id);
        if (null == entity) {
            return null;
        }
        String filePath = getAbsolutePath();
        Map<String, Object> dataMap = new HashMap<>(5);
        dataMap.put("code", entity.getCode());
        dataMap.put("projectName", entity.getProjectName());
        dataMap.put("contractName", entity.getContractName());
        dataMap.put("companyName", entity.getRectificationCompanyValue());
        dataMap.put("safeFailureTypeValue", entity.getSafeFailureTypeValue() == null ? "" : entity.getSafeFailureTypeValue());
        dataMap.put("title", entity.getTitle() == null ? "" : entity.getTitle());
        dataMap.put("questionMeta", entity.getQuestionMeta() == null ? "" : entity.getQuestionMeta());
        dataMap.put("rectificationAsk", entity.getRectificationAsk() == null ? "" : entity.getRectificationAsk());
        dataMap.put("rectificationResult", entity.getRectificationResult() == null ? "" : entity.getRectificationResult());
        dataMap.put("problemDept", entity.getProblemDeptValue() == null ? "" : entity.getProblemDeptValue());
        dataMap.put("problemHandling", entity.getProblemHandling() == null ? "" : entity.getProblemHandling());
        dataMap.put("remark", entity.getRemark() == null ? "" : entity.getRemark());
        String fileName = entity.getCode() + ".doc";
        WordUtil.createWord(dataMap, "scene_question.xml", filePath, fileName);
        File file = new File(filePath, fileName);
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("businessId", id);
        resultMap.put("projectId", entity.getProjectId());
        resultMap.put("projectNumber", entity.getProjectNumber());
        resultMap.put("projectName", entity.getProjectName());
        resultMap.put("type", type);
        resultMap.put("fileName", fileName);
        resultMap.put("fileType", "doc");
        resultMap.put("fileSize", file.length());
        resultMap.put("filePath", filePath + "/" + fileName);
        return resultMap;
    }

    public Map<String, Object> projectPlan(String id, String type) throws Exception {
        CmProjectInfo project = this.cmProjectInfoService.getById(id);
        if (project == null) {
            return null;
        }
        String filePath = getAbsolutePath();
        String fileName = project.getProjectNumber() + ".xls";
        CmProjectPlan param = new CmProjectPlan();
        param.setProjectId(id);
        param.addOrderByField(" stage.queue ");
        param.addOrderByField(" t.queue ");
        List<CmProjectPlan> dataList = this.cmProjectPlanService.queryAll(param);
        ExcelBuilder builder = new ExcelBuilder();
        builder.openFile("数据");
        int rowIndex = 0;
        int columnIndex = 0;
        HSSFRow row = builder.getRow(rowIndex++, 8);
        row.setHeight((short) 500);
        builder.getCell(row, columnIndex++).setCellValue("序号");
        builder.getCell(row, columnIndex++).setCellValue("所属阶段");
        builder.getCell(row, columnIndex++).setCellValue("计划编码");
        builder.getCell(row, columnIndex++).setCellValue("计划名称");
        builder.getCell(row, columnIndex++).setCellValue("责任单位");
        builder.getCell(row, columnIndex++).setCellValue("责任人");
        builder.getCell(row, columnIndex++).setCellValue("计划开始时间");
        builder.getCell(row, columnIndex++).setCellValue("计划结束时间");
        if (dataList != null && dataList.size() > 0) {
            int no = 1;
            for (CmProjectPlan plan : dataList) {
                row = builder.getRow(rowIndex++, 8);
                row.setHeight((short) 500);
                columnIndex = 0;
                builder.getCell(row, columnIndex++).setCellValue(no++);
                builder.getCell(row, columnIndex++).setCellValue(plan.getStageName());
                builder.getCell(row, columnIndex++).setCellValue(plan.getPlanCode());
                builder.getCell(row, columnIndex++).setCellValue(plan.getPlanName());
                builder.getCell(row, columnIndex++).setCellValue(plan.getDutyCompanyValue());
                builder.getCell(row, columnIndex++).setCellValue(plan.getDutyPerson());
                builder.getCell(row, columnIndex++).setCellValue(GlobalUtil.dateUtil2String(plan.getPlanStartDate(), "yyyy-MM-dd"));
                builder.getCell(row, columnIndex++).setCellValue(GlobalUtil.dateUtil2String(plan.getPlanEndDate(), "yyyy-MM-dd"));
            }
        }
        builder.createFile(filePath, fileName);
        Map<String, Object> resultMap = new HashMap<>(4);
        File file = new File(filePath, fileName);
        resultMap.put("projectId", project.getId());
        resultMap.put("projectNumber", project.getProjectNumber());
        resultMap.put("projectName", project.getProjectName());
        resultMap.put("type", type);
        resultMap.put("fileName", fileName);
        resultMap.put("fileType", "xls");
        resultMap.put("fileSize", file.length());
        resultMap.put("filePath", filePath + "/" + fileName);
        return resultMap;
    }

    public String getContextPath(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    public String getAbsolutePath(){
        return (GlobalContext.getProperty("FILE_PATH") + "/" + getContextPath()).replace('\\','/');
    }

    public String setEmpty(String value) {
        return value == null ? "" : value;
    }
}

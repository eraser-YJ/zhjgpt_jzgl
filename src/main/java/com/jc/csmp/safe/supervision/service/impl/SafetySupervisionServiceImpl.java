package com.jc.csmp.safe.supervision.service.impl;

import com.jc.csmp.common.WordUtil;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.safe.supervision.dao.ISafetySupervisionDao;
import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.csmp.safe.supervision.service.ISafetySupervisionService;
import com.jc.csmp.safe.supervision.service.ISafetyUnitService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.common.util.CharConventUtils;
import com.jc.system.content.service.IUploadService;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.task.service.ITaskService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Version 1.0
 */
@Service
public class SafetySupervisionServiceImpl extends BaseServiceImpl<SafetySupervision> implements ISafetySupervisionService {

    @Autowired
    IInstanceService instanceService;


    @Autowired
    ITaskService taskService;
    @Autowired
    private ISafetyUnitService safetyUnitService;
    @Autowired
    private IUploadService uploadService;


    private ISafetySupervisionDao safetySupervisionDao;
    public String rootPath = GlobalContext.getProperty("ROOT_PATH");

    public String adviceNotePath = GlobalContext.getProperty("ADVICE_NOTE_PATH");

    public SafetySupervisionServiceImpl() {
    }

    @Autowired
    public SafetySupervisionServiceImpl(ISafetySupervisionDao safetySupervisionDao) {
        super(safetySupervisionDao);
        this.safetySupervisionDao = safetySupervisionDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(SafetySupervision entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = safetySupervisionDao.delete(entity);
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(SafetySupervision entity) throws CustomException {
        if (!StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            this.save(entity);
            return Result.success(MessageUtils.getMessage("JC_SYS_001"));
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result updateEntity(SafetySupervision entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            Integer flag = this.update(entity);
            if (flag == 1) {
                return Result.success(MessageUtils.getMessage("JC_SYS_003"));
            } else {
                return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

    /**
     * @return Integer 增加的记录数
     * @description 发起流程方法
     * @author
     * @version 2020-07-09
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer saveWorkflow(SafetySupervision cond) throws CustomException {

        propertyService.fillProperties(cond, false);
        cond.setPiId(cond.getWorkflowBean().getBusiness_Key_());
        Integer result = safetySupervisionDao.save(cond);
        uploadService.managerForAttach(cond.getId(), "cm_safety_supervision", cond.getAttachFile(), cond.getDeleteAttachFile(), 1);
        if (result > 0) {
            if (cond.getSafetyUnitList() != null && cond.getSafetyUnitList().size() > 0) {
                String projectId = cond.getId();
                List<SafetyUnit> list = cond.getSafetyUnitList();
                if (list != null && list.size() > 0) {
                    /*	list.removeIf(list.get:)*/
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setProjectId(projectId);
                        list.get(i).setId(null);
                    }
                }
                safetyUnitService.saveList(list);
            }
        }
        cond.getWorkflowBean().setWorkflowTitle_(WorkflowContentEnum.gcbj.toString());
        Map<String, Object> resultMap = instanceService.startProcess(cond.getWorkflowBean());
        if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
            return Integer.parseInt(resultMap.get("code").toString());
        }
        return result;

    }

    /**
     * @return Integer 修改的记录数量
     * @description 修改方法
     * @author
     * @version 2020-07-09
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer updateWorkflow(SafetySupervision cond) throws CustomException {
        propertyService.fillProperties(cond, true);
        Integer result = safetySupervisionDao.update(cond);
        /**首先删除*/
        if (null != cond.getSafetyUnitList()) {
            String[] primaryKeys = new String[cond.getSafetyUnitList().size()];
            if (cond.getSafetyUnitList().size() > 0) {
                for (int i = 0; i < cond.getSafetyUnitList().size(); i++) {
                    primaryKeys[i] = cond.getSafetyUnitList().get(i).getId();
                    cond.getSafetyUnitList().get(i).setProjectId(cond.getId());
                }
            }
            SafetyUnit deleteSafetyUnit = new SafetyUnit();
            deleteSafetyUnit.setPrimaryKeys(primaryKeys);
            safetyUnitService.deleteByIds(deleteSafetyUnit, false);
            safetyUnitService.saveList(cond.getSafetyUnitList());
        }
        uploadService.managerForAttach(cond.getId(), "cm_safety_supervision", cond.getAttachFile(), cond.getDeleteAttachFile(), 1);
        cond.getWorkflowBean().setBusiness_Key_(cond.getPiId());
        Map<String, Object> resultMap = taskService.excute(cond.getWorkflowBean());
        if (resultMap.get("code") != null && !resultMap.get("code").toString().equals("0")) {
            return Integer.parseInt(resultMap.get("code").toString());
        }
        return result;
    }


    @Override
    public PageManager workFlowTodoQueryByPage(SafetySupervision cond, PageManager page) {
        return safetySupervisionDao.workFlowTodoQueryByPage(cond, page);
    }


    @Override
    public PageManager workFlowDoneQueryByPage(SafetySupervision cond, PageManager page) {
        return safetySupervisionDao.workFlowDoneQueryByPage(cond, page);
    }


    @Override
    public PageManager workFlowInstanceQueryByPage(SafetySupervision cond, PageManager page) {
        return safetySupervisionDao.workFlowInstanceQueryByPage(cond, page);
    }

    @Override
    public boolean createAdviceNote(SafetySupervision entity) throws CustomException {
        boolean result = false;
        try {
            entity = safetySupervisionDao.get(entity);
            if (null != entity) {
                //文件路径
                String filePath = rootPath + adviceNotePath;
                /*INumber number=new Number();
               *//* number.getNumber("SUPERVISOR", 1, 2, null);*//*
                String no = number.getNumber("SUPERVISOR", 1, 2, null);
                no = no.substring(1,no.length() );*/
                //文件名称
                SafetyUnit safetyUnit=new SafetyUnit();
                safetyUnit.setPartakeType("supervisor");
                safetyUnit.setProjectId(entity.getId());
                List<SafetyUnit> safetyUnitlist= safetyUnitService.queryAll(safetyUnit);
               String  unitName="";
                if(null!=safetyUnitlist&&safetyUnitlist.size()>0){
                    unitName=safetyUnitlist.get(0).getUnitNameValue();
                }
                String fileName = System.currentTimeMillis() + ".doc";
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("supervisor", unitName);
                dataMap.put("projectName", entity.getProjectName());
                dataMap.put("buildUnitName", entity.getBuildUnitName());
                dataMap.put("year", DateUtils.getYear());
                dataMap.put("month", DateUtils.getMonth());
                dataMap.put("day", DateUtils.getDay());
                dataMap.put("projecNumber", null==entity.getProjectNumber()?"":entity.getProjectNumber());
                WordUtil.createWord(dataMap, entity.getItemCode() + ".ftl", filePath, fileName);
                SafetySupervision updatEntity = new SafetySupervision();
                updatEntity.setId(entity.getId());
                updatEntity.setAdviceFileName(fileName);
                updatEntity.setAdviceOldName("建设工程安全监督通知书.doc");
                updatEntity.setIsAdvice(new BigDecimal(1));
                updatEntity.setExtStr1(null==entity.getProjectNumber()?"":entity.getProjectNumber());
                safetySupervisionDao.update(updatEntity);
                result = true;
            } else {
                result = false;
            }


        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;

    }

    @Override
    public void downAdviceNote(SafetySupervision entity, HttpServletResponse response, HttpServletRequest request) throws CustomException {
        entity = safetySupervisionDao.get(entity);
        if (null != entity) {
            String filename = CharConventUtils.encodingFileName(entity.getAdviceOldName());
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + filename + "\"");
                downFile(entity.getAdviceFileName(), response, request);

        }
    }

    private Boolean downFile(String resourcesName, HttpServletResponse response, HttpServletRequest request) {
        boolean success = true;
        /*String root = rootPath;*/
        InputStream inputStream = null;
        OutputStream os = null;
        try {
            File file = new File(rootPath + adviceNotePath + File.separatorChar + resourcesName);
            inputStream = new FileInputStream(file);
            os = response.getOutputStream();
            byte[] b = new byte[Integer.parseInt(GlobalContext.getProperty("STREAM_SLICE"))];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            log.error("下载文件不存在 path:" + rootPath + adviceNotePath + File.separatorChar + resourcesName);
            success = false;
        } catch (IOException e) {
            log.error("下载异常" + e);
            success = false;
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(inputStream);
        }
        return success;
    }

    @Override
    public void exportExcelAdviceNote(SafetySupervision entity, HttpServletResponse response, HttpServletRequest request) throws CustomException, IOException {
        PageManager page = new PageManager();
        page.setPageRows(-1);
        List<SafetySupervision> list = (List<SafetySupervision>) safetySupervisionDao.workFlowInstanceQueryByPage(entity, page).getData();

        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
        List<Dic> dictList = dicManager.getAllDicsByTypeCode("company_type", "csmp", true);
        Map<String, String> dicMap = new HashMap<String, String>();


        columnMap.put("projectName", "工程名称");
        columnMap.put("projectAddress", "工程地点");
        columnMap.put("buildArea", "建筑面积");
        columnMap.put("structureTypeValue", "结构");
        /*columnMap.put("buildPropertiesValue", "建设性质");*/
        columnMap.put("projectTypeValue", "工程类型");
        columnMap.put("planStartDate", "计划开工日期");
        columnMap.put("planEndDate", "计划竣工日期");
        columnMap.put("investmentAmount", "造价（亿）");
        columnMap.put("projectBuild", "建设单位");
        columnMap.put("projectBuild_leader", "建设单位负责人");
        columnMap.put("projectBuild_phone", "建设单位电话");
        columnMap.put("projectSupervise", "监管单位");
        columnMap.put("projectSupervise_leader", "监管单位负责人");
        columnMap.put("projectSupervise_phone", "监管单位电话");

        for (int i = 0; i < dictList.size(); i++) {
            columnMap.put(dictList.get(i).getCode(), dictList.get(i).getValue());
            columnMap.put(dictList.get(i).getCode() + "_leader", dictList.get(i).getValue() + "负责人");
            columnMap.put(dictList.get(i).getCode() + "_phone", dictList.get(i).getValue() + "电话");
        }

        for (int i = 0; i < dictList.size(); i++) {
            dicMap.put(dictList.get(i).getCode(), dictList.get(i).getValue());
        }
        // 创建excel工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建第一个sheet页，并命名
        HSSFSheet sheet = wb.createSheet("Sheet1");
        int titleIndex = 0;
        HSSFRow titleRow = sheet.createRow((short) 0);
        for (Map.Entry<String, String> entry : columnMap.entrySet()) {
            // 创建第一行标题
            HSSFCell cell = titleRow.createCell(titleIndex);
            cell.setCellValue(entry.getValue());
            titleIndex++;
        }
        for (int i = 0; i < list.size(); i++) {
            /*创建第N行，从第二行开始*/
            SafetySupervision safetySupervision = list.get(i);
            HSSFRow contentRow = sheet.createRow((short) i + 1);
            Method[] methods = safetySupervision.getClass().getMethods();
            Map<String, Method> methodMap = new HashMap<>();
            for (Method m : methods) {
                methodMap.put(m.getName(), m);
            }
            /*创建列，为其赋值数据*/
            int contentIndex = 0;
            for (Map.Entry<String, String> entry : columnMap.entrySet()) {

                /* 特殊情况赋值*/
                if ("projectBuild".equals(entry.getKey()) || "projectSupervise".equals(entry.getKey()) || dicMap.containsKey(entry.getKey())) {
                    HSSFCell contentCell = contentRow.createCell(contentIndex);
                    SafetyUnit paramSafetyUnit = new SafetyUnit();
                    paramSafetyUnit.setProjectId(safetySupervision.getId());
                    List<SafetyUnit> safetyUnitList = safetyUnitService.queryAll(paramSafetyUnit);
                    if (null != safetyUnitList && safetyUnitList.size() > 0) {
                        for (SafetyUnit unit : safetyUnitList) {
                            if (null != unit.getPartakeType() && unit.getPartakeType().equals(entry.getKey())) {
                                contentCell.setCellValue(unit.getUnitNameValue());
                                HSSFCell leaderCell = contentRow.createCell((contentIndex+1));
                                HSSFCell phoneCell = contentRow.createCell((contentIndex+2));
                                leaderCell.setCellValue(unit.getProjectLeader());
                                phoneCell.setCellValue(unit.getPhone());
                                break;
                            }
                        }
                    }
                } else {

                    String methodName = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
                    Method targetMethod = methodMap.get("get" + methodName);
                    if (null != targetMethod) {
                        HSSFCell contentCell = contentRow.createCell(contentIndex);
                        Object value = null;
                        try {
                            value = targetMethod.invoke(safetySupervision, null);

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        if (value instanceof Integer) {
                            contentCell.setCellValue(value.toString());
                        } else if (value instanceof BigDecimal) {
                            contentCell.setCellValue(value.toString());
                        } else if (value instanceof String) {
                            contentCell.setCellValue(value.toString());
                        } else if (value instanceof Double) {
                        } else if (value instanceof Float) {
                        } else if (value instanceof Long) {
                        } else if (value instanceof Boolean) {
                        } else if (value instanceof Date) {
                            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
                            contentCell.setCellValue(value != null ? sFormat.format(value) : "");
                        }
                    }
                }
                contentIndex++;
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            wb.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数
            response.reset();
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("工程报监.xls").getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}


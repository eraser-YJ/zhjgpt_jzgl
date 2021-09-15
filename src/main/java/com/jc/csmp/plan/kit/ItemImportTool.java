package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.domain.XyDicAgg;
import com.jc.foundation.excel.kit.ExcelOperateUtil;
import com.jc.foundation.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入工具类
 *
 * @author liubq
 * @since 2018年6月14日
 */
public class ItemImportTool {

    /**
     * 取得数据
     *
     * @param excelFile
     * @return
     * @throws CustomException
     */
    public static List<ProjectYearPlanItem> assembleList(MultipartFile excelFile, XyDicAgg agg) throws CustomException {
        String fileName = excelFile.getOriginalFilename();
        int pos = fileName.lastIndexOf(".");
        String fileNameExt = fileName.substring(pos + 1);
        long fileSize = excelFile.getSize();
        if (fileNameExt == "xls" && fileNameExt == "xlsx") {
            throw new CustomException("请选择后缀名为xls或xlsx的文件");
        } else if (fileSize > 2 * 1024 * 1024) {
            throw new CustomException("请上传文件大小不超过2M");
        }
        InputStream inputStream = null;
        try {
            inputStream = excelFile.getInputStream();
            String[][] excelData = ExcelOperateUtil.getExcelData(inputStream, 1, 0);
            // 构建数据
            List<ProjectYearPlanItem> dataList = ItemExcelUtil.build(excelData,agg);
            if (dataList == null || dataList.size() == 0) {
                dataList = new ArrayList<ProjectYearPlanItem>();
            }
            List<ProjectYearPlanItem> resList = new ArrayList<>();
            String dicCode="";
            String dicName="";
            for(ProjectYearPlanItem item:dataList){
                if(item.getProjectType()!=null&&item.getProjectType().length()>0){
                    dicCode = item.getProjectType();
                    dicName = item.getProjectTypeName();
                } else {
                    item.setProjectType(dicCode);
                    item.setProjectTypeName(dicName);
                    resList.add(item);
                }
            }
            return resList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ex) {
                }
            }
        }
    }
}

package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.csmp.plan.domain.XyDic;
import com.jc.csmp.plan.domain.XyDicAgg;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 导出excel
 */
public class YearExportExcel {

    private ProjectYearPlan head;

    private List<ProjectYearPlanItem> itemList;

    private MapList<String, ProjectYearPlanItem> itemMap;

    private XyDicAgg dicAgg;

    private Integer rowIndex = 0;

    /**
     * 构造方法
     *
     * @param headPlan
     * @param itemList
     * @param dicAgg
     */
    public YearExportExcel(ProjectYearPlan headPlan, List<ProjectYearPlanItem> itemList, XyDicAgg dicAgg) {
        this.head = headPlan;
        this.itemList = itemList;
        this.itemMap = ListUtil.getMapList(itemList, item -> item.getProjectType());
        this.dicAgg = dicAgg;
    }

    /**
     * 构建
     *
     * @throws Exception
     */
    public void build(HttpServletResponse response) throws Exception {
        //构造加过
        ExcelBuilder builder = new ExcelBuilder();
        builder.openFile("月度计划");
        int len = 13;
        buildHead(builder, len);
        buildStatic(builder, len, itemList);
        buildBody(builder, len);
        builder.saveFile(head.getPlanAreaName() + head.getPlanYear() + "年计划.xlsx", response);
    }

    /**
     * 构建表体
     *
     * @param builder
     * @param len
     * @throws Exception
     */
    private void buildBody(ExcelBuilder builder, int len) throws Exception {
        buildDic(null, null, dicAgg.getDicTree(), builder, len);
    }

    /**
     * 构建分类和数据
     *
     * @param pre
     * @param parent
     * @param dicList
     * @param builder
     * @param len
     * @throws Exception
     */
    private void buildDic(String pre, XyDic parent, List<XyDic> dicList, ExcelBuilder builder, int len) throws Exception {
        XyDic dic;
        if (dicList != null && dicList.size() > 0) {
            String title;
            for (int i = 0; i < dicList.size(); i++) {
                dic = dicList.get(i);
                if (pre != null) {
                    title = "（" + pre + "." + (i + 1) + "）" + dic.getValue();
                    writeDic(title, builder, len);
                    buildDic(pre + "." + (i + 1), dic, dic.getNextDic(), builder, len);
                } else {
                    title = "（" + (i + 1) + "）" + dic.getValue();
                    writeDic(title, builder, len);
                    buildDic(String.valueOf(i + 1), dic, dic.getNextDic(), builder, len);
                }


            }
        } else {
            if (parent != null) {
                writeData(builder, parent.getCode());
            }

        }

    }

    /**
     * 写分类
     *
     * @param title
     * @param builder
     * @param len
     * @throws Exception
     */
    private void writeDic(String title, ExcelBuilder builder, int len) throws Exception {
        XSSFRow row = builder.getRow(rowIndex++, 30);
        for (int i = 0; i < len; i++) {
            builder.getCellLeft(row, i).setCellValue(title);
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
    }

    /**
     * 写数据
     *
     * @param builder
     * @param leafCode
     * @throws Exception
     */
    private void writeData(ExcelBuilder builder, String leafCode) throws Exception {

        List<ProjectYearPlanItem> itemList = itemMap.get(leafCode);
        ProjectYearPlanItem data;
        if (itemList != null) {
            for (int index = 0; index < itemList.size(); index++) {
                int colIndex = 0;
                data = itemList.get(index);
                XSSFRow row = builder.getRow(rowIndex++, 30);
                builder.getCell(row, colIndex++).setCellValue(index + 1);
                builder.getCell(row, colIndex++).setCellValue(index + 1);
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getProjectName()));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectTotalInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectUsedInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectNowInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectAfterInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectTotalDay())));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getProjectNowDesc()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getDutyCompany()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getDutyPerson()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getProjectDesc()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getRemark()));
            }
        }

    }

    private String[] indexChina = {"一、", "二、", "三、", "四、", "五、"};

    private String nvl(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }


    /**
     * 构建表头
     *
     * @param builder
     * @throws Exception
     */
    private void buildHead(ExcelBuilder builder, int len) throws Exception {

        rowIndex = 0;
        XSSFCell cell;
        int colIndex = 0;
        XSSFRow row = builder.getRow(rowIndex++, 30);
        for (int index = 0; index < len; index++) {
            cell = builder.getTitle(row, colIndex++);
            cell.setCellValue(head.getPlanAreaName() + head.getPlanYear() + "年基础设施及功能类项目建设计划表");
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
        colIndex = 0;
        row = builder.getRow(rowIndex++, 30);
        for (int index = 0; index < len; index++) {
            builder.getCellRight0(row, colIndex++).setCellValue("单位：万元");
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
        builder.setColumnWidth(0, 5);
        builder.setColumnWidth(1, 5);
        colIndex = 0;
        row = builder.getRow(rowIndex++, 30);
        builder.getCell(row, colIndex++).setCellValue("序号");
        builder.getCell(row, colIndex++).setCellValue("序号");
        builder.getCell(row, colIndex++).setCellValue("项目名称");
        builder.getCell(row, colIndex++).setCellValue("总投资（万元）");
        builder.getCell(row, colIndex++).setCellValue("累计已完成投资(万元)");
        builder.getCell(row, colIndex++).setCellValue("计划投资（万元）");
        builder.getCell(row, colIndex++).setCellValue("以后投资（万元）");
        builder.getCell(row, colIndex++).setCellValue("工期(天)");
        builder.getCell(row, colIndex++).setCellValue("主要工作内容及规模");
        builder.getCell(row, colIndex++).setCellValue("实施必要性");
        builder.getCell(row, colIndex++).setCellValue("责任部门");
        builder.getCell(row, colIndex++).setCellValue("责任人");
        builder.getCell(row, colIndex++).setCellValue("备注\n（PPP项目标注为新型城镇化或其他PPP项目）");


        builder.setColumnWidth(0, 4);
        builder.setColumnWidth(1, 8);
        builder.setColumnWidth(2, 15);
        builder.setColumnWidth(3, 10);
        builder.setColumnWidth(4, 12);
        builder.setColumnWidth(5, 10);
        builder.setColumnWidth(6, 10);
        builder.setColumnWidth(7, 10);
        builder.setColumnWidth(8, 18);
        builder.setColumnWidth(9, 15);
        builder.setColumnWidth(10, 12);
        builder.setColumnWidth(11, 8);
        builder.setColumnWidth(12, 20);
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, 1);


    }

    /**
     * 计算统计数据
     *
     * @param itemList
     * @param beginType
     * @param endType
     * @return
     * @throws Exception
     */
    public BigDecimal[] staticData(List<ProjectYearPlanItem> itemList, String beginType, String endType) throws Exception {
        BigDecimal[] dataSet = new BigDecimal[4];
        dataSet[0] = BigDecimal.valueOf(0);
        dataSet[1] = BigDecimal.valueOf(0);
        dataSet[2] = BigDecimal.valueOf(0);
        dataSet[3] = BigDecimal.valueOf(0);
        for (ProjectYearPlanItem item : itemList) {
            if (beginType != null && !item.getProjectType().startsWith(beginType)) {
                continue;
            }
            if (endType != null && !item.getProjectType().endsWith(endType)) {
                continue;
            }
            if (item.getProjectTotalInvest() != null) {
                dataSet[0] = dataSet[0].add(item.getProjectTotalInvest());
            }
            if (item.getProjectUsedInvest() != null) {
                dataSet[1] = dataSet[1].add(item.getProjectUsedInvest());
            }
            if (item.getProjectNowInvest() != null) {
                dataSet[2] = dataSet[2].add(item.getProjectNowInvest());
            }
            if (item.getProjectAfterInvest() != null) {
                dataSet[3] = dataSet[3].add(item.getProjectAfterInvest());
            }
        }
        return dataSet;
    }

    public void buildStatic(ExcelBuilder builder, int len, List<ProjectYearPlanItem> itemList) throws Exception {
        int colIndex = 0;
        String pre = "0_";
        XSSFRow row = builder.getRow(rowIndex++, 30);
        builder.getCellLeft(row, colIndex++).setCellValue(" 投资总计");
        builder.getCellLeft(row, colIndex++).setCellValue(" 投资总计");
        builder.getCellLeft(row, colIndex++).setCellValue(" 投资总计");
        BigDecimal[] dataSet = staticData(itemList, pre, null);
        builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[0])));
        builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[1])));
        builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[2])));
        builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[3])));
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, 2);
        XyDic topItem;
        for (int j = 0; j < dicAgg.getDicTree().size(); j++) {
            topItem = dicAgg.getDicTree().get(j);
            row = builder.getRow(rowIndex++, 30);
            colIndex = 0;
            builder.getCellLeft(row, colIndex++).setCellValue(" " + indexChina[j] + topItem.getValue());
            builder.getCellLeft(row, colIndex++).setCellValue("");
            builder.getCellLeft(row, colIndex++).setCellValue("");
            dataSet = staticData(itemList, topItem.getCode(), null);
            builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[0])));
            builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[1])));
            builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[2])));
            builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[3])));
            builder.getCell(row, colIndex++).setCellValue("");
            builder.getCell(row, colIndex++).setCellValue("");
            builder.getCell(row, colIndex++).setCellValue("");
            builder.getCell(row, colIndex++).setCellValue("");
            builder.getCell(row, colIndex++).setCellValue("");
            builder.getCell(row, colIndex++).setCellValue("");
            builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, 2);
            for (XyDic nowItem : dicAgg.getXsDicList()) {
                row = builder.getRow(rowIndex++, 30);
                colIndex = 0;
                builder.getCell(row, colIndex++).setCellValue("按建\n设类\n别统\n计");
                builder.getCell(row, colIndex++).setCellValue(nowItem.getValue());
                builder.getCell(row, colIndex++).setCellValue(nowItem.getValue());
                dataSet = staticData(itemList, topItem.getCode(), nowItem.getCode());
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[0])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[1])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[2])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[3])));
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
            }
            builder.mergeCell(rowIndex - dicAgg.getXsDicList().size(), rowIndex - 1, 0, 0);
            for (XyDic nextItem : topItem.getNextDic()) {
                row = builder.getRow(rowIndex++, 30);
                colIndex = 0;
                builder.getCell(row, colIndex++).setCellValue("按项\n目类\n别统\n计");
                builder.getCell(row, colIndex++).setCellValue(nextItem.getValue());
                builder.getCell(row, colIndex++).setCellValue(nextItem.getValue());
                dataSet = staticData(itemList, nextItem.getCode(), null);
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[0])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[1])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[2])));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(dataSet[3])));
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
                builder.getCell(row, colIndex++).setCellValue("");
            }
            builder.mergeCell(rowIndex - topItem.getNextDic().size(), rowIndex - 1, 0, 0);
        }
        row = builder.getRow(rowIndex++, 30);
        for (int index = 0; index < len; index++) {
            builder.getCellLeft(row, index++).setCellValue("详细数据");
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
    }

}

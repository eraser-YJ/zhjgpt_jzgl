package com.jc.csmp.plan.kit;

import com.jc.csmp.plan.domain.ProjectMonthPlan;
import com.jc.csmp.plan.domain.ProjectMonthPlanItem;
import com.jc.csmp.plan.domain.XyDic;
import com.jc.csmp.plan.domain.XyDicAgg;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 导出excel
 */
public class MonthExportExcel {

    private String resourcePath;
    private ProjectMonthPlan head;

    private List<ProjectMonthPlanItem> itemList;

    private MapList<String, ProjectMonthPlanItem> itemMap;

    private MapList<String, ImgVO> imgMap = new MapList<>();

    private XyDicAgg dicAgg;

    private Integer rowIndex = 0;

    /**
     * 构造方法
     *
     * @param headPlan
     * @param itemList
     * @param dicAgg
     */
    public MonthExportExcel(String resourcePath, ProjectMonthPlan headPlan, List<ProjectMonthPlanItem> itemList, XyDicAgg dicAgg) {
        this.resourcePath = resourcePath;
        this.head = headPlan;
        this.itemList = itemList;
        this.itemMap = ListUtil.getMapList(itemList, item -> item.getProjectType());
        this.buildImg(itemList);
        this.dicAgg = dicAgg;
    }

    /**
     * 查询所有图片数据
     *
     * @throws Exception
     */
    public void buildImg(List<ProjectMonthPlanItem> itemList) {
        try {
            String pre = resourcePath;
            String[] attachList;
            String resourceName;
            for (ProjectMonthPlanItem item : itemList) {
                if (item.getXxjdAttchList() == null || item.getXxjdAttchList().trim().length() <= 0) {
                    continue;
                }
                attachList = item.getXxjdAttchList().split("#");
                for (String attach : attachList) {
                    resourceName = attach.split(",")[2];
                    if (resourceName.toUpperCase().endsWith("PNG")) {
                        imgMap.put(item.getId(), new ImgVO(Files.readAllBytes(Paths.get(pre, resourceName)), XSSFWorkbook.PICTURE_TYPE_PNG));
                    } else {
                        imgMap.put(item.getId(), new ImgVO(Files.readAllBytes(Paths.get(pre, resourceName)), XSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        int len = 17;
        buildHead(builder, len);
        buildBody(builder, len);
        builder.saveFile(head.getPlanAreaName() + head.getPlanYear() + "年" + head.getPlanMonth() + "月计划表.xlsx", response);
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
        XSSFRow row = builder.getRow(rowIndex++, 20);
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

        List<ProjectMonthPlanItem> itemList = itemMap.get(leafCode);
        ProjectMonthPlanItem data;
        if (itemList != null) {
            for (int index = 0; index < itemList.size(); index++) {
                int colIndex = 0;
                data = itemList.get(index);
                XSSFRow row = builder.getRow(rowIndex++, 20);
                builder.getCell(row, colIndex++).setCellValue(index + 1);
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getProjectName()));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectTotalInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectUsedInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectNowInvest())));
                builder.getCellLeft2(row, colIndex++).setCellValue(nvl(data.getXxjd()));


                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectMonthPlanInvest())));
                builder.getCell(row, colIndex++).setCellValue(String.valueOf(nvl(data.getProjectMonthActInvest())));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getSolveProblem()));
                builder.getCell(row, colIndex++).setCellValue(nvl(data.getTudiCardValue()));
                builder.getCell(row, colIndex++).setCellValue(nvl(data.getYdghxkCardValue()));
                builder.getCell(row, colIndex++).setCellValue(nvl(data.getGcghxkCardValue()));
                builder.getCell(row, colIndex++).setCellValue(nvl(data.getKgxkCardValue()));
                builder.getCell(row, colIndex++).setCellValue(nvl(data.getXmxzyjsValue()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getDutyCompany()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getDutyPerson()));
                builder.getCellLeft(row, colIndex++).setCellValue(nvl(data.getRemark()));
                row.setHeightInPoints(80);
                List<ImgVO> imgList = imgMap.get(data.getId());

                if (imgList != null && imgList.size() != 0) {
                    for (ImgVO img : imgList) {
                        colIndex = 0;
                        row = builder.getRow(rowIndex++, 20);
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCellLeft(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCellLeft2(row, colIndex++).setCellValue("");
                        builder.setImage(img.getData(), img.getFormat(), rowIndex - 1, colIndex - 1);
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCellLeft(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCell(row, colIndex++).setCellValue("");
                        builder.getCellLeft(row, colIndex++).setCellValue("");
                        builder.getCellLeft(row, colIndex++).setCellValue("");
                        builder.getCellLeft(row, colIndex++).setCellValue("");
                        row.setHeightInPoints(3000);
                    }
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 0, 0);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 1, 1);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 2, 2);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 3, 3);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 4, 4);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 6, 6);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 7, 7);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 8, 8);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 9, 9);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 10, 10);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 11, 11);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 12, 12);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 13, 13);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 14, 14);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 15, 15);
                    builder.mergeCell(rowIndex - imgList.size() - 1, rowIndex - 1, 16, 16);
                }


            }
        }

    }

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
     * @param len
     * @throws Exception
     */
    private void buildHead(ExcelBuilder builder, int len) throws Exception {
        rowIndex = 0;
        XSSFCell cell;
        int colIndex = 0;
        XSSFRow row = builder.getRow(rowIndex++, 30);
        for (int index = 0; index < len; index++) {
            cell = builder.getTitle(row, colIndex++);
            cell.setCellValue("长春新区" + head.getPlanYear() + "年" + head.getPlanMonth() + "月基础设施及功能类项目建设计划表——" + head.getPlanAreaName());
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
        colIndex = 0;
        row = builder.getRow(rowIndex++, 20);
        for (int index = 0; index < len; index++) {
            builder.getCellRight0(row, colIndex++).setCellValue("单位：万元");
        }
        builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, len - 1);
        builder.setColumnWidth(0, 5);
        builder.setColumnWidth(1, 5);
        colIndex = 0;
        row = builder.getRow(rowIndex++, 20);
        builder.getCell(row, colIndex++).setCellValue("序号");
        builder.getCell(row, colIndex++).setCellValue("项目名称");
        builder.getCell(row, colIndex++).setCellValue("总投资（万元）");
        builder.getCell(row, colIndex++).setCellValue("累计已完成投资(万元)");
        builder.getCell(row, colIndex++).setCellValue("计划投资（万元）");
        builder.getCell(row, colIndex++).setCellValue("形象进度");
        builder.getCell(row, colIndex++).setCellValue("本月计划投资");
        builder.getCell(row, colIndex++).setCellValue("本月实际完成投资");
        builder.getCell(row, colIndex++).setCellValue("存在问题及解决措施");
        builder.getCell(row, colIndex++).setCellValue("前期手续完成情况（是或否）");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("责任部门");
        builder.getCell(row, colIndex++).setCellValue("责任人");
        builder.getCell(row, colIndex++).setCellValue("备注");


        row = builder.getRow(rowIndex++, 20);
        colIndex = 0;
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("土地证");
        builder.getCell(row, colIndex++).setCellValue("用地规划许可证");
        builder.getCell(row, colIndex++).setCellValue("工程规划许可证");
        builder.getCell(row, colIndex++).setCellValue("开工许可证");
        builder.getCell(row, colIndex++).setCellValue("项目选址意见书");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");

        builder.setColumnWidth(1, 40);
        builder.setColumnWidth(2, 12);
        builder.setColumnWidth(3, 15);
        builder.setColumnWidth(4, 12);
        builder.setColumnWidth(5, 20);
        builder.setColumnWidth(6, 12);
        builder.setColumnWidth(7, 12);
        builder.setColumnWidth(8, 30);
        builder.setColumnWidth(9, 12);
        builder.setColumnWidth(10, 12);
        builder.setColumnWidth(11, 12);
        builder.setColumnWidth(12, 12);
        builder.setColumnWidth(13, 12);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 0, 0);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 1, 1);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 2, 2);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 3, 3);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 4, 4);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 5, 5);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 6, 6);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 7, 7);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 8, 8);
        builder.mergeCell(rowIndex - 2, rowIndex - 2, 9, 13);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 14, 14);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 15, 15);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 16, 16);

    }

}

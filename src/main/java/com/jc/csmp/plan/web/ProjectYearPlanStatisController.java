package com.jc.csmp.plan.web;

import com.jc.csmp.plan.domain.*;
import com.jc.csmp.plan.kit.DicUtil;
import com.jc.csmp.plan.kit.ExcelBuilder;
import com.jc.csmp.plan.kit.NumUtil;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.util.Result;
import com.jc.foundation.web.BaseController;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @version 2020-07-09
 * @title
 */

@Controller
@RequestMapping(value = "/plan/projectYearPlan/statis")
public class ProjectYearPlanStatisController extends BaseController {
    @Autowired
    private IDicService dicService;


    @Autowired
    private IProjectYearPlanService projectPlanService;


    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    public String showItem(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/projectYearPlan/projectYearPlan_statis";
    }


    /**
     * 查询指定年份数据
     *
     * @param year
     * @return
     * @throws Exception
     */
    private Map<String, Object> queryData(int year) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        Dic condDic = new Dic();
        condDic.setParentId("plan_area");
        condDic.addOrderByField("t.order_flag");
        //查询区域
        List<Dic> dicAreaList = dicService.query(condDic);
        List<XyDic> areaList = DicUtil.buildList(dicAreaList);
        resMap.put("areaList", areaList);
        //查询分类
        List<Dic> dicList = projectPlanService.selectPPTDic();
        XyDicAgg typeAgg = DicUtil.buildTree(dicList);

        //统计结果

        StatisItemVO item;
        StatisItemVO xtItem;

        XyDic typeDic;


        for (XyDic xtDic : typeAgg.getDicTree()) {
            List<XyDic> nextList = new ArrayList<>();
            Map<String, StatisItemVO> xtItemMap = new HashMap<>();
            for (XyDic xcDic : xtDic.getNextDic()) {
                List<StatisItemVO> dataList = new ArrayList<>();
                for (XyDic dic : areaList) {
                    StatisVO cond = new StatisVO();
                    cond.setPlanYear(year);
                    cond.setProjectType(xtDic.getCode());
                    cond.setProjectTypeEx(xcDic.getCode());
                    cond.setPlanAreaCode(dic.getCode());
                    item = projectPlanService.queryStatisList(cond);
                    if (item == null) {
                        item = new StatisItemVO();
                    }
                    xtItem = xtItemMap.get(dic.getCode());
                    if (xtItem == null) {
                        xtItem = new StatisItemVO();
                        xtItemMap.put(dic.getCode(), xtItem);
                    }
                    xtItem.setProjectUsedInvest(NumUtil.add(item.getProjectUsedInvest(), xtItem.getProjectUsedInvest()));
                    xtItem.setProjectNowInvest(NumUtil.add(item.getProjectNowInvest(), xtItem.getProjectNowInvest()));
                    item.setPlanYear(cond.getPlanYear());
                    item.setProjectType(xcDic.getCode());
                    item.setPlanAreaCode(cond.getPlanAreaCode());
                    xtItem.setPlanYear(cond.getPlanYear());
                    xtItem.setProjectType(xtDic.getCode());
                    xtItem.setPlanAreaCode(cond.getPlanAreaCode());
                    dataList.add(item);
                }
                XyDic newDic = xcDic.clone();
                newDic.setSubInfo(dataList);
                nextList.add(newDic);
            }
            xtDic.setNextDic(nextList);
            List<StatisItemVO> xtItemList = new ArrayList<>();
            for (XyDic areaDic : areaList) {
                xtItemList.add(xtItemMap.get(areaDic.getCode()));
            }
            xtDic.setSubInfo(xtItemList);
        }
        StatisVO main = new StatisVO();
        main.setPlanYear(Integer.valueOf(year));
        resMap.put("main", main);
        resMap.put("rowList", typeAgg.getDicTree());
        return resMap;
    }

    @RequestMapping(value = "manageList.action")
    @ResponseBody
    public Result manageList(HttpServletRequest request) throws Exception {
        String year = request.getParameter("selectYear");
        if (year == null || year.trim().length() <= 0) {
            year = String.valueOf(LocalDate.now().getYear());
        }
        //查询数据
        Map<String, Object> resMap = queryData(Integer.valueOf(year));
        return Result.success(resMap);
    }


    @RequestMapping(value = "export.action")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //查询
        Result result = manageList(request);
        //构造加过
        ExcelBuilder builder = new ExcelBuilder();
        builder.openFile("计划");
        Map<String, Object> resMap = (Map<String, Object>) result.getData();
        StatisVO main = (StatisVO) resMap.get("main");
        List<XyDic> areaList = (List<XyDic>) resMap.get("areaList");
        List<XyDic> resTypeList = (List<XyDic>) resMap.get("rowList");

        int len = areaList.size() * 2 + 5;
        int rowIndex = 0;

        XSSFCell cell;
        int colIndex = 0;
        XSSFRow row = builder.getRow(rowIndex++, 30);
        for (int index = 0; index < len; index++) {
            cell = builder.getTitle(row, colIndex++);
            cell.setCellValue(main.getPlanYear() + "年基础设施及功能类项目建设汇总表");
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
        builder.getCell(row, colIndex++).setCellValue("项目类别");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("累计完成投资");
        builder.getCell(row, colIndex++).setCellValue("计划投资");
        for (int index = 0; index < areaList.size(); index++) {
            builder.getCell(row, colIndex++).setCellValue(areaList.get(index).getValue());
            builder.getCell(row, colIndex++).setCellValue(areaList.get(index).getValue());
        }
        colIndex = 0;
        row = builder.getRow(rowIndex++, 20);
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        builder.getCell(row, colIndex++).setCellValue("");
        for (int index = 0; index < areaList.size(); index++) {
            builder.getCell(row, colIndex++).setCellValue("累计完成投资");
            builder.getCell(row, colIndex++).setCellValue("计划投资");
        }
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 0, 2);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 3, 3);
        builder.mergeCell(rowIndex - 2, rowIndex - 1, 4, 4);
        for (int index = 0; index < areaList.size(); index++) {
            builder.mergeCell(rowIndex - 2, rowIndex - 2, index * 2 + 5, index * 2 + 5 + 1);
        }
        XyDic typeDic;
        XyDic nextDic;
        for (int tIndex = 0; tIndex < resTypeList.size(); tIndex++) {
            BigDecimal sumUsed =BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
            BigDecimal sumNow = BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
            typeDic = resTypeList.get(tIndex);
            List<StatisItemVO> dataList = (List<StatisItemVO>) typeDic.getSubInfo();
            for (StatisItemVO data : dataList) {
                if (data.getProjectUsedInvest() != null) {
                    sumUsed = NumUtil.add(data.getProjectUsedInvest(), sumUsed);
                }
                if (data.getProjectNowInvest() != null) {
                    sumNow = NumUtil.add(data.getProjectNowInvest(), sumNow);
                }
            }
            colIndex = 0;
            row = builder.getRow(rowIndex++, 20);
            builder.getCell(row, colIndex++).setCellValue(typeDic.getValue());
            builder.getCell(row, colIndex++).setCellValue(typeDic.getValue());
            builder.getCell(row, colIndex++).setCellValue(typeDic.getValue());
            builder.mergeCell(rowIndex - 1, rowIndex - 1, 0, 2);
            builder.getCell(row, colIndex++).setCellValue(sumUsed.toString());
            builder.getCell(row, colIndex++).setCellValue(sumNow.toString());
            for (StatisItemVO data : dataList) {
                if (data.getProjectUsedInvest() != null) {
                    builder.getCell(row, colIndex++).setCellValue(data.getProjectUsedInvest().toString());
                } else {
                    builder.getCell(row, colIndex++).setCellValue(BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                }
                if (data.getProjectNowInvest() != null) {
                    builder.getCell(row, colIndex++).setCellValue(data.getProjectNowInvest().toString());
                } else {
                    builder.getCell(row, colIndex++).setCellValue(BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                }
            }

            for (int tNextIndex = 0; tNextIndex < typeDic.getNextDic().size(); tNextIndex++) {
                nextDic = typeDic.getNextDic().get(tNextIndex);
                BigDecimal sumNextUsed = BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
                BigDecimal sumNextNow = BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
                List<StatisItemVO> dataNextList = (List<StatisItemVO>) nextDic.getSubInfo();
                for (StatisItemVO nextdata : dataNextList) {
                    if (nextdata.getProjectUsedInvest() != null) {
                        sumNextUsed = NumUtil.add(nextdata.getProjectUsedInvest(), sumNextUsed);
                    }
                    if (nextdata.getProjectNowInvest() != null) {
                        sumNextNow = NumUtil.add(nextdata.getProjectNowInvest(), sumNextNow);
                    }
                }
                colIndex = 0;
               row = builder.getRow(rowIndex++, 20);
                builder.getCell(row, colIndex++).setCellValue("按项目\n类别\n统计");
                builder.getCell(row, colIndex++).setCellValue(tNextIndex + 1);
                builder.getCell(row, colIndex++).setCellValue(nextDic.getValue());
                builder.getCell(row, colIndex++).setCellValue(sumNextUsed.toString());
                builder.getCell(row, colIndex++).setCellValue(sumNextNow.toString());
                for (StatisItemVO nextData : dataNextList) {
                    if (nextData.getProjectUsedInvest() != null) {
                        builder.getCell(row, colIndex++).setCellValue(nextData.getProjectUsedInvest().toString());
                    } else {
                        builder.getCell(row, colIndex++).setCellValue(BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                    }
                    if (nextData.getProjectNowInvest() != null) {
                        builder.getCell(row, colIndex++).setCellValue(nextData.getProjectNowInvest().toString());
                    } else {
                        builder.getCell(row, colIndex++).setCellValue(BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }


            }
            builder.mergeCell(rowIndex - typeDic.getNextDic().size(), rowIndex - 1, 0, 0);
        }


        builder.saveFile(main.getPlanYear() + "年项目建设计划汇总表.xlsx", response);

    }
}
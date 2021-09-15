package com.jc.mobile.web;

import com.jc.csmp.ptProject.service.IProjectInfoService;
import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.service.ICompanyProjectsZtbService;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import org.apache.cxf.jaxws.javaee.CString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value="/mobile/statis")
public class MobileStatisController extends MobileController {

    @Autowired
    private ICompanyProjectsZtbService companyProjectsZtbService;
    @Autowired
    private IDicManager dicManager;
    @Autowired
    private IProjectInfoService projectInfoService;
    /**
     * 按类型统计招投标投诉、异议情况
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="ztbComplain.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse ztbComplain(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = companyProjectsZtbService.queryEchartsForZ();
        return MobileApiResponse.ok(list);
    }

    /**
     * 招投标行政监督汇总
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="ztbSupervision.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse ztbSupervision(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        List<Dic> jdTypeList = dicManager.getDicsByTypeCode("jd_type", "csmp");
        for(Dic item:jdTypeList){
            Integer time[] = {1,2,3,4,5,6,7,8,9,10,11,12};
            for (int i = 0; i < time.length; i++){
                EchartsVo echartsVo = new EchartsVo();
                echartsVo.setName(item.getValue());
                WinbiddingVo winbiddingVo = new WinbiddingVo();
                winbiddingVo.setJdType(item.getCode());
                winbiddingVo.setJdTimeBegin(getFirstDayOfMonth(time[i]));
                winbiddingVo.setJdTimeEnd(getLastDayOfMonth(time[i]));
                Long a = companyProjectsZtbService.queryXzjdCount(winbiddingVo);
                echartsVo.setData(String.valueOf(a));
                echartsVo.setNamey(item.getValue());
                echartsVo.setName(time[i]+"月");
                list.add(echartsVo);
            }
        }
        return MobileApiResponse.ok(list);
    }
    /**
     * 绿建节能统计汇总
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="greenTravel.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse greenTravel(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        CompanyProjectsZtb entity = new CompanyProjectsZtb();
        list = companyProjectsZtbService.queryEchartsForLjjn(entity);
        return MobileApiResponse.ok(list);
    }
    /**
     * 项目办理情况
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryProjectJd.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryProjectJd(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        list = projectInfoService.queryProjectJd();
        return MobileApiResponse.ok(list);
    }
    /**
     * 项目办理平均用时
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryAverageDay.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryAverageDay(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        list = projectInfoService.queryAverageDay();
        return MobileApiResponse.ok(list);
    }
    /**
     * 项目审批情况
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryProjectPass.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryProjectPass(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        list = projectInfoService.queryProjectPass();
        return MobileApiResponse.ok(list);
    }
    /**
     * 项目办理平均受理次数
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryAverageAccept.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryAverageAccept(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        EchartsVo echartsVo = projectInfoService.queryAverageAccept();
        EchartsVo echartsVo1 = new EchartsVo();
        echartsVo1.setName("立项许可");
        echartsVo1.setCc(echartsVo.getCc());
        list.add(echartsVo1);
        EchartsVo echartsVo2 = new EchartsVo();
        echartsVo2.setName("施工许可");
        echartsVo2.setCc(String.valueOf(echartsVo.getCc1()));
        list.add(echartsVo2);
        EchartsVo echartsVo3 = new EchartsVo();
        echartsVo3.setName("竣工验收");
        echartsVo3.setCc(String.valueOf(echartsVo.getCc2()));
        list.add(echartsVo3);
        return MobileApiResponse.ok(list);
    }
    /**
     * 施工许可证办理情况统计
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryEchartsForSgxk.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryEchartsForSgxk(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        EchartsVo echartsVo = projectInfoService.queryEchartsForSgxk();
        EchartsVo echartsVo1 = new EchartsVo();
        echartsVo1.setName("未办理");
        Integer a = Integer.valueOf(echartsVo.getAllCc())-Integer.valueOf(echartsVo.getCc());
        echartsVo1.setCc(String.valueOf(a));
        list.add(echartsVo1);
        EchartsVo echartsVo2 = new EchartsVo();
        echartsVo2.setName("已办理");
        echartsVo2.setCc(String.valueOf(echartsVo.getCc()));
        list.add(echartsVo2);
        return MobileApiResponse.ok(list);
    }
    /**
     * 各区域施工许可证办理数量统计
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="queryEchartsForArea.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse queryEchartsForArea(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
        list = projectInfoService.queryEchartsForArea();
        return MobileApiResponse.ok(list);
    }
    /**
     * fabricated
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="fabricated.action", method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse fabricated(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        List<EchartsVo> list = new ArrayList<>();
            EchartsVo echartsVo = new EchartsVo();
            echartsVo.setData("60");
            echartsVo.setName("装配式钢结构");
            EchartsVo echartsVo1 = new EchartsVo();
            echartsVo1.setData("30");
            echartsVo1.setName("装配式木结构");
            EchartsVo echartsVo2 = new EchartsVo();
            echartsVo2.setData("20");
            echartsVo2.setName("装配式混凝土结构");
            EchartsVo echartsVo3 = new EchartsVo();
            echartsVo3.setData("120");
            echartsVo3.setName("部品部件生产类");
            list.add(echartsVo);
            list.add(echartsVo1);
            list.add(echartsVo2);
            list.add(echartsVo3);
        return MobileApiResponse.ok(list);
    }



    public static Date getFirstDayOfMonth(int month) throws ParseException {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime())+" 00:00:00";
        Date a = sdf.parse(firstDayOfMonth);
        return a;
    }
    /**
     * 获得该月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int month) throws ParseException {
        Calendar cal = Calendar.getInstance();
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay=0;
        //2月的平年瑞年天数
        if(month==2) {
            lastDay = cal.getLeastMaximum(Calendar.DAY_OF_MONTH);
        }else {
            lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime())+" 23:59:59";
        Date a = sdf.parse(lastDayOfMonth);
        return a;
    }

}

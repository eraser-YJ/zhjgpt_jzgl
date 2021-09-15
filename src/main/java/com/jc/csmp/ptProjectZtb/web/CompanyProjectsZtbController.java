package com.jc.csmp.ptProjectZtb.web;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.domain.validator.CompanyProjectsZtbValidator;
import com.jc.csmp.ptProjectZtb.service.ICompanyProjectsZtbService;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 招投标信息处理
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/csmp/ptProjectZtb/")
public class CompanyProjectsZtbController extends BaseController{

	@Autowired
	private ICompanyProjectsZtbService companyProjectsZtbService;
	@Autowired
	private IDicManager dicManager;
	@org.springframework.web.bind.annotation.InitBinder("companyProjectsZtb")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CompanyProjectsZtbValidator());
	}

	public CompanyProjectsZtbController() {
	}

	/**
	 * 保存方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="招投标信息",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid CompanyProjectsZtb entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(companyProjectsZtbService.saveEntity(entity), resultMap, getToken(request));
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="招投标信息",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(CompanyProjectsZtb entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(companyProjectsZtbService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public CompanyProjectsZtb get(CompanyProjectsZtb entity) throws Exception{
		return companyProjectsZtbService.get(entity);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "csmp/ptProjectZtb/companyProjectsZtbForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CompanyProjectsZtb entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
		PageManager page_ = companyProjectsZtbService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageDljgPmList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageDljgPmList(CompanyProjectsZtb entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
        PageManager page_ = companyProjectsZtbService.queryWinbiddingForPm(entity,page);
//        List<WinbiddingVo> list = (List<WinbiddingVo>)page_.getData();
//        for(WinbiddingVo item:list){
//			BigDecimal total = BigDecimal.ZERO;
//			total = total.add(item.getJlCc()).add(item.getHwclCc()).add(item.getKcCc()).add(item.getKcsjCc()).add(item.getSgCc()).add(item.getSjCc());
//			item.setTotal(total);
//        }
		GlobalUtil.setTableRowNo(page_, page_.getPageRows());
		return page_;
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageXzjdPmList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageXzjdPmList(CompanyProjectsZtb entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
        PageManager list = companyProjectsZtbService.queryBiddingXzjd(entity,page);
		GlobalUtil.setTableRowNo(list, list.getPageRows());
		return list;
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageProjectApproverList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageProjectAcompanyProjectsSgxkListpproverList(CompanyProjectsZtb entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
        PageManager list = companyProjectsZtbService.queryProjectApprover(entity,page);
		GlobalUtil.setTableRowNo(list, list.getPageRows());
		return list;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage() throws Exception{
		return "csmp/ptProjectZtb/companyProjectsZtbList";
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="manageTspm.action",method=RequestMethod.GET)
	public String manageTspm() throws Exception{
		return "csmp/ptProjectZtb/companyProjectsZtbTspmList";
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manageXzjd.action",method=RequestMethod.GET)
	public String manageXzjd() throws Exception{
		return "csmp/ptProjectZtb/companyProjectsZtbXzjdList";
	}

    /**
     * @description 跳转方法
     * @return String 跳转的路径
     * @throws Exception
     * @author
     * @version  2020-04-10
     */
    @RequestMapping(value="manageDljgPm.action",method=RequestMethod.GET)
    public String manageDljgPm() throws Exception{
        return "csmp/ptProjectZtb/companyProjectsZtbDljgPmList";
    }
    /**
     * @description 跳转方法
     * @return String 跳转的路径
     * @throws Exception
     * @author
     * @version  2020-04-10
     */
    @RequestMapping(value="manageProjectApprover.action",method=RequestMethod.GET)
    public String manageProjectApprover() throws Exception{
        return "csmp/ptProjectZtb/companyProjectsApproverList";
    }
    /**
     * @description 跳转方法
     * @return String 跳转的路径
     * @throws Exception
     * @author
     * @version  2020-04-10
     */
    @RequestMapping(value="manageLjjn.action",method=RequestMethod.GET)
    public String manageLjjn() throws Exception{
        return "csmp/ptProjectZtb/companyProjectsLjjnList";
    }

	/**
	 * 删除方法
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="招投标信息",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(CompanyProjectsZtb entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(companyProjectsZtbService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="manageForLjjn.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageForLjjn(PageManager page){
        List<EchartsVo> list = new ArrayList<>();
        List<Dic> ljjnDicList = dicManager.getDicsByTypeCode("region", "csmp");
        for(Dic item:ljjnDicList){
            CompanyProjectsZtb entity = new CompanyProjectsZtb();
            entity.setPcpArea(item.getCode());
            List<EchartsVo> echartsVo = companyProjectsZtbService.queryEchartsForLjjn(entity);
            for(EchartsVo result:echartsVo){
                result.setValue(item.getValue());
            }
            list.addAll(echartsVo);
        }
        page.setData(list);
        page.setPage(0);
        page.setPageRows(10);
        page.setTotalCount(list.size());
        page.setTotalPages(list.size()/10);
        GlobalUtil.setTableRowNo(page, page.getPageRows());
		return page;
	}

	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryEchartsForLjjn.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryEchartsForLjjn(PageManager page){
        List<EchartsVo> list = new ArrayList<>();
        CompanyProjectsZtb entity = new CompanyProjectsZtb();
        list = companyProjectsZtbService.queryEchartsForLjjn(entity);
		return list;
	}

	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryEchartsForZ.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryEchartsForZ(){
		List<EchartsVo> echartsVo = companyProjectsZtbService.queryEchartsForZ();
		return echartsVo;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryXzjdCount.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryXzjdCount() throws ParseException {
		List<Dic> jdTypeList = dicManager.getDicsByTypeCode("jd_type", "csmp");
		Calendar now = Calendar.getInstance();
		List<EchartsVo> list = new ArrayList<>();
		for(Dic item:jdTypeList){
			EchartsVo echartsVo = new EchartsVo();
			echartsVo.setName(item.getValue());
			Integer time[] = {1,2,3,4,5,6,7,8,9,10,11,12};
			Long[] value = new Long[12];
			for (int i = 0; i < time.length; i++){
				WinbiddingVo winbiddingVo = new WinbiddingVo();
				winbiddingVo.setJdType(item.getCode());
				winbiddingVo.setJdTimeBegin(getFirstDayOfMonth(time[i]));
				winbiddingVo.setJdTimeEnd(getLastDayOfMonth(time[i]));
				Long a = companyProjectsZtbService.queryXzjdCount(winbiddingVo);
				value[i]=a;
			}
			echartsVo.setArr(value);
			list.add(echartsVo);
		}

		return list;
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

	@RequestMapping(value="excel.action",method=RequestMethod.GET)
	public void excel(HttpServletRequest request, CompanyProjectsZtb entity, HttpServletResponse response) throws Exception{
		List<CompanyProjectsZtb> list = companyProjectsZtbService.queryAll(entity);
		response.setCharacterEncoding("UTF-8");
		//创建excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = wb.createSheet("招投标投诉及异议情况统计表");

		//创建标题行
		HSSFRow titleRow = sheet.createRow(0);
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 9000);
		titleRow.createCell(0).setCellValue("项目编号");
		titleRow.createCell(1).setCellValue("项目名称");
		titleRow.createCell(2).setCellValue("招标类型");
		titleRow.createCell(3).setCellValue("异议数量");
		titleRow.createCell(4).setCellValue("投诉数量");
		titleRow.createCell(5).setCellValue("备注");

		String formatDate = null;
		for (CompanyProjectsZtb item : list) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			dataRow.createCell(0).setCellValue(item.getPcpProjectNum());
			dataRow.createCell(1).setCellValue(item.getProjectName());
			dataRow.createCell(2).setCellValue(item.getBiddingTypeValue());
			dataRow.createCell(3).setCellValue(item.getBiddingYysl());
			dataRow.createCell(4).setCellValue(item.getBiddingTsss());
			dataRow.createCell(5).setCellValue(item.getBiddingRemark());
		}
		// 设置下载时客户端Excel的名称
		String filename ="招投标投诉及异议情况统计表";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	@RequestMapping(value="excelTspm.action",method=RequestMethod.GET)
	public void excelTspm(HttpServletRequest request, CompanyProjectsZtb entity, HttpServletResponse response) throws Exception{
		List<CompanyProjectsZtb> list = companyProjectsZtbService.queryAll(entity);
		response.setCharacterEncoding("UTF-8");
		//创建excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = wb.createSheet("招投排名情况统计表");

		//创建标题行
		HSSFRow titleRow = sheet.createRow(0);
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 9000);
		titleRow.createCell(0).setCellValue("招标代理机构编码");
		titleRow.createCell(1).setCellValue("招标代理机构名称");
		titleRow.createCell(2).setCellValue("异议数量");
		titleRow.createCell(3).setCellValue("投诉数量");
		titleRow.createCell(4).setCellValue("备注");

		String formatDate = null;
		for (CompanyProjectsZtb item : list) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			dataRow.createCell(0).setCellValue(item.getBiddingDljgbm());
			dataRow.createCell(1).setCellValue(item.getBiddingDljgmc());
			dataRow.createCell(2).setCellValue(item.getBiddingYysl());
			dataRow.createCell(3).setCellValue(item.getBiddingTsss());
			dataRow.createCell(4).setCellValue(item.getBiddingRemark());
		}
		// 设置下载时客户端Excel的名称
		String filename ="招投排名情况统计表";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	@RequestMapping(value="excelDljgPm.action",method=RequestMethod.GET)
	public void excelDljgPm(HttpServletRequest request, CompanyProjectsZtb entity, PageManager page, HttpServletResponse response) throws Exception{
			response.setCharacterEncoding("UTF-8");
			//创建excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			//创建sheet页
			HSSFSheet sheet = wb.createSheet("代理机构排名统计表");

			//创建标题行
			HSSFRow titleRow = sheet.createRow(0);
			sheet.setColumnWidth(0, 8000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			titleRow.createCell(0).setCellValue("招标代理机构名称");
			titleRow.createCell(1).setCellValue("勘察中标通知书");
			titleRow.createCell(2).setCellValue("设计中标通知书");
			titleRow.createCell(3).setCellValue("施工中标通知书");
			titleRow.createCell(4).setCellValue("监理中标通知书");
			titleRow.createCell(5).setCellValue("勘察设计中标通知书");
			titleRow.createCell(6).setCellValue("货物材料中标通知书");
			titleRow.createCell(7).setCellValue("总中标金额（万元）");

			String formatDate = null;
			PageManager page_ = companyProjectsZtbService.queryWinbiddingForPm(entity,page);
			List<WinbiddingVo> list = (List<WinbiddingVo>)page_.getData();
			for(WinbiddingVo item:list){
				BigDecimal total = BigDecimal.ZERO;
				total = total.add(item.getJlCc()).add(item.getHwclCc()).add(item.getKcCc()).add(item.getKcsjCc()).add(item.getSgCc()).add(item.getSjCc());
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
				dataRow.createCell(0).setCellValue(item.getBiddingDljgmc());
				dataRow.createCell(1).setCellValue(String.valueOf(item.getKcCc()));
				dataRow.createCell(2).setCellValue(String.valueOf(item.getSjCc()));
				dataRow.createCell(3).setCellValue(String.valueOf(item.getSgCc()));
				dataRow.createCell(4).setCellValue(String.valueOf(item.getJlCc()));
				dataRow.createCell(5).setCellValue(String.valueOf(item.getKcsjCc()));
				dataRow.createCell(6).setCellValue(String.valueOf(item.getHwclCc()));
				dataRow.createCell(7).setCellValue(String.valueOf(total));
			}
			// 设置下载时客户端Excel的名称
			String filename ="代理机构排名统计表";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}
	@RequestMapping(value="excelXzjd.action",method=RequestMethod.GET)
	public void excelXzjd(HttpServletRequest request, CompanyProjectsZtb entity, PageManager page, HttpServletResponse response) throws Exception{
			response.setCharacterEncoding("UTF-8");
			//创建excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			//创建sheet页
			HSSFSheet sheet = wb.createSheet("行政监督汇总表");

			//创建标题行
			HSSFRow titleRow = sheet.createRow(0);
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 9000);
			titleRow.createCell(0).setCellValue("项目编号");
			titleRow.createCell(1).setCellValue("项目名称");
			titleRow.createCell(2).setCellValue("监督类型");
			titleRow.createCell(3).setCellValue("日期");
			titleRow.createCell(4).setCellValue("监督机构");
			titleRow.createCell(5).setCellValue("备注");

			String formatDate = null;
            PageManager page_ = companyProjectsZtbService.queryBiddingXzjd(entity,page);
            List<WinbiddingVo> list = (List<WinbiddingVo>)page_.getData();
			for(WinbiddingVo item:list){
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
				dataRow.createCell(0).setCellValue(item.getProjectNum());
				dataRow.createCell(1).setCellValue(item.getProjectName());
				dataRow.createCell(2).setCellValue(item.getJdTypeValue());
                if(item.getJdTime()!=null){
                    formatDate = DateFormatUtils.format(item.getJdTime(), "yyyy-MM-dd");
                    dataRow.createCell(3).setCellValue(formatDate);
                }else{
                    dataRow.createCell(3).setCellValue("");
                }
				dataRow.createCell(4).setCellValue(item.getJdOrg());
				dataRow.createCell(5).setCellValue(item.getJdRemark());
			}
			// 设置下载时客户端Excel的名称
			String filename ="行政监督汇总表";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}
	@RequestMapping(value="excelApprover.action",method=RequestMethod.GET)
	public void excelApprover(HttpServletRequest request, CompanyProjectsZtb entity, PageManager page, HttpServletResponse response) throws Exception{
			response.setCharacterEncoding("UTF-8");
			//创建excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			//创建sheet页
			HSSFSheet sheet = wb.createSheet("项目办理阶段统计表");

			//创建标题行
			HSSFRow titleRow = sheet.createRow(0);
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			titleRow.createCell(0).setCellValue("项目编号");
			titleRow.createCell(1).setCellValue("项目名称");
			titleRow.createCell(2).setCellValue("阶段类型");
			titleRow.createCell(3).setCellValue("区域");
			titleRow.createCell(4).setCellValue("编号");
			titleRow.createCell(5).setCellValue("日期");

			String formatDate = null;
            PageManager page_= companyProjectsZtbService.queryProjectApprover(entity,page);
            List<CompanyProjectsZtb> list = (List<CompanyProjectsZtb>)page_ .getData();
			for(CompanyProjectsZtb item:list){
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
				dataRow.createCell(0).setCellValue(item.getPcpProjectNum());
				dataRow.createCell(1).setCellValue(item.getProjectName());
				dataRow.createCell(2).setCellValue(item.getProjectType());
				dataRow.createCell(3).setCellValue(item.getPcpAreaValue());
				dataRow.createCell(4).setCellValue(item.getBiddingDljgbm());
				if(item.getPcpZbrq()!=null){
					formatDate = DateFormatUtils.format(item.getPcpZbrq(), "yyyy-MM-dd");
					dataRow.createCell(5).setCellValue(formatDate);
				}else{
					dataRow.createCell(5).setCellValue("");
				}
			}
			// 设置下载时客户端Excel的名称
			String filename ="项目办理阶段统计表";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}
	@RequestMapping(value="excelLjjn.action",method=RequestMethod.GET)
	public void excelLjjn(HttpServletRequest request, HttpServletResponse response) throws Exception{
			response.setCharacterEncoding("UTF-8");
			//创建excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			//创建sheet页
			HSSFSheet sheet = wb.createSheet("项目绿建节能统计表");

			//创建标题行
			HSSFRow titleRow = sheet.createRow(0);
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			titleRow.createCell(0).setCellValue("区域");
			titleRow.createCell(1).setCellValue("年份");
			titleRow.createCell(2).setCellValue("一星级");
			titleRow.createCell(3).setCellValue("二星级");
			titleRow.createCell(4).setCellValue("三星级");

			String formatDate = null;
            List<EchartsVo> list = new ArrayList<>();
            List<Dic> ljjnDicList = dicManager.getDicsByTypeCode("region", "csmp");
            for(Dic item:ljjnDicList){
                CompanyProjectsZtb entity = new CompanyProjectsZtb();
                entity.setPcpArea(item.getCode());
                List<EchartsVo> echartsVo = companyProjectsZtbService.queryEchartsForLjjn(entity);
                for(EchartsVo result:echartsVo){
                    result.setValue(item.getValue());
                }
                list.addAll(echartsVo);
            }
			for(EchartsVo item:list){
				HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
				dataRow.createCell(0).setCellValue(item.getValue());
				dataRow.createCell(1).setCellValue(item.getName());
				dataRow.createCell(2).setCellValue(item.getCc());
				dataRow.createCell(3).setCellValue(item.getCc1());
				dataRow.createCell(4).setCellValue(item.getCc2());
			}
			// 设置下载时客户端Excel的名称
			String filename ="项目绿建节能统计表";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}

}


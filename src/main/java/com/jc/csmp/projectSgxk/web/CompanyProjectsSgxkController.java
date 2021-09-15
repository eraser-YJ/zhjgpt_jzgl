package com.jc.csmp.projectSgxk.web;

import com.jc.csmp.projectSgxk.domain.CompanyProjectsSgxk;
import com.jc.csmp.projectSgxk.domain.validator.CompanyProjectsSgxkValidator;
import com.jc.csmp.projectSgxk.service.ICompanyProjectsSgxkService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 施工许可处理
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/csmp/projectSgxk/")
public class CompanyProjectsSgxkController extends BaseController{

	@Autowired
	private ICompanyProjectsSgxkService companyProjectsSgxkService;

	@org.springframework.web.bind.annotation.InitBinder("companyProjectsSgxk")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CompanyProjectsSgxkValidator());
	}

	public CompanyProjectsSgxkController() {
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
	@ActionLog(operateModelNm="施工许可",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid CompanyProjectsSgxk entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(companyProjectsSgxkService.saveEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="施工许可",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(CompanyProjectsSgxk entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(companyProjectsSgxkService.updateEntity(entity), resultMap, getToken(request));
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
	public CompanyProjectsSgxk get(CompanyProjectsSgxk entity) throws Exception{
		return companyProjectsSgxkService.get(entity);
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
		return "csmp/projectSgxk/companyProjectsSgxkForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CompanyProjectsSgxk entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
		PageManager page_ = companyProjectsSgxkService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
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
		return "csmp/projectSgxk/companyProjectsSgxkList";
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
	@ActionLog(operateModelNm="施工许可",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(CompanyProjectsSgxk entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(companyProjectsSgxkService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}


	@RequestMapping(value="excel.action",method=RequestMethod.GET)
	public void excel(HttpServletRequest request, CompanyProjectsSgxk entity,HttpServletResponse response) throws Exception{
		List<CompanyProjectsSgxk> list = companyProjectsSgxkService.queryAll(entity);
		response.setCharacterEncoding("UTF-8");
		//创建excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = wb.createSheet("许可信息表");

		//创建标题行
		HSSFRow titleRow = sheet.createRow(0);
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		titleRow.createCell(0).setCellValue("施工许可证号");
		titleRow.createCell(1).setCellValue("工程名称");
		titleRow.createCell(2).setCellValue("区域");
		titleRow.createCell(3).setCellValue("建设单位");
		titleRow.createCell(4).setCellValue("施工单位");
		titleRow.createCell(5).setCellValue("项目经理");
		titleRow.createCell(6).setCellValue("办证人员");
		titleRow.createCell(7).setCellValue("工程地址");
		titleRow.createCell(8).setCellValue("面积（平方米)");
		titleRow.createCell(9).setCellValue("投资金额（万元）");
		titleRow.createCell(10).setCellValue("许可证发放日期");
		titleRow.createCell(11).setCellValue("栋号");

		String formatDate = null;
		for (CompanyProjectsSgxk item : list) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			dataRow.createCell(0).setCellValue(item.getPcpBdmc());
			dataRow.createCell(1).setCellValue(item.getProjectname());
			dataRow.createCell(2).setCellValue(item.getProjectAreaValue());
			dataRow.createCell(3).setCellValue(item.getBuilddept());
			dataRow.createCell(4).setCellValue(item.getConstructionOrganization());
			dataRow.createCell(5).setCellValue(item.getProjectmanager());
			dataRow.createCell(6).setCellValue(item.getPcpBzry());
			dataRow.createCell(7).setCellValue(item.getProjectaddress());
			dataRow.createCell(8).setCellValue(item.getPcpHtbh());
			if(item.getProjectmoney()!=null){
				dataRow.createCell(9).setCellValue(String.valueOf(item.getProjectmoney()));
			}else{
				dataRow.createCell(9).setCellValue("");
			}
			if(item.getPcpQdrq()!=null){
				formatDate = DateFormatUtils.format(item.getPcpQdrq(), "yyyy-MM-dd");
				dataRow.createCell(10).setCellValue(formatDate);
			}else{
				dataRow.createCell(10).setCellValue("");
			}
			dataRow.createCell(11).setCellValue(item.getPcpDh());
		}
		// 设置下载时客户端Excel的名称
		String filename ="许可信息表";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}


}


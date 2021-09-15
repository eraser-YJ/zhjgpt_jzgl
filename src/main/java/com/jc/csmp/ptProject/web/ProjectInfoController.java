package com.jc.csmp.ptProject.web;

import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.domain.validator.ProjectInfoValidator;
import com.jc.csmp.ptProject.service.IProjectInfoService;
import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
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
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目表处理
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/csmp/ptProject/")
public class ProjectInfoController extends BaseController{

	@Autowired
	private IProjectInfoService projectInfoService;

	@org.springframework.web.bind.annotation.InitBinder("projectInfo")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ProjectInfoValidator());
	}

	public ProjectInfoController() {
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
	@ActionLog(operateModelNm="项目表",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid ProjectInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(projectInfoService.saveEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="项目表",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(ProjectInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(projectInfoService.updateEntity(entity), resultMap, getToken(request));
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
	public ProjectInfo get(ProjectInfo entity) throws Exception{
		return projectInfoService.get(entity);
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
		return "csmp/ptProject/projectInfoForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(ProjectInfo entity, PageManager page){
//		if(StringUtil.isEmpty(entity.getOrderBy())) {
//			entity.addOrderByFieldDesc("t.CREATE_DATE");
//		}
		PageManager page_ = projectInfoService.query(entity, page);
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
		return "csmp/ptProject/projectInfoList";
	}
	/**
	* @description 装配式统计跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author sunjx
	* @version  2020-12-23
	*/
	@RequestMapping(value="manageFabricated.action",method=RequestMethod.GET)
	public String manageFabricated() throws Exception{
		return "csmp/ptProject/projectInfoFabricatedList";
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
	@ActionLog(operateModelNm="项目表",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(ProjectInfo entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(projectInfoService.deleteByIds(entity) != 0){
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
	@RequestMapping(value="queryEchartsForArea.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryEchartsForArea(){
		List<EchartsVo> page_ = projectInfoService.queryEchartsForArea();
		return page_;
	}
	@RequestMapping(value="queryEchartsForBuildingTypes.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryEchartsForBuildingTypes(){
		List<EchartsVo> page_ = projectInfoService.queryEchartsForBuildingTypes();
		return page_;
	}
	@RequestMapping(value="queryEchartsForStructureType.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryEchartsForStructureType(){
		List<EchartsVo> page_ = projectInfoService.queryEchartsForStructureType();
		return page_;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryAverageDay.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryAverageDay(){
		List<EchartsVo> page_ = projectInfoService.queryAverageDay();
		return page_;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryProjectJd.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryProjectJd(){
		List<EchartsVo> page_ = projectInfoService.queryProjectJd();
		return page_;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryProjectPass.action",method=RequestMethod.GET)
	@ResponseBody
	public List<EchartsVo> queryProjectPass(){
		List<EchartsVo> page_ = projectInfoService.queryProjectPass();
		return page_;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryEchartsForSgxk.action",method=RequestMethod.GET)
	@ResponseBody
	public EchartsVo queryEchartsForSgxk(){
		EchartsVo page_ = projectInfoService.queryEchartsForSgxk();
		return page_;
	}
	/**
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value="queryAverageAccept.action",method=RequestMethod.GET)
	@ResponseBody
	public EchartsVo queryAverageAccept(){
		EchartsVo page_ = projectInfoService.queryAverageAccept();
		return page_;
	}

	@RequestMapping(value="download.action")
	public HttpServletResponse download(String path,HttpServletRequest request, HttpServletResponse response) {
	   try {
	 String filePath =request.getSession().getServletContext().getRealPath("/app/project.apk");
	 // path是指欲下载的文件的路径。
	 File file = new File(filePath);
	 // 取得文件名。
	 String filename = file.getName();
	 // 取得文件的后缀名。
	 String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

	 // 以流的形式下载文件。
	 InputStream fis = new BufferedInputStream(new FileInputStream(file));
	 byte[] buffer = new byte[fis.available()];
	 fis.read(buffer);
	 fis.close();
	 // 清空response
	 response.reset();
	 // 设置response的Header
	 response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
	 response.addHeader("Content-Length", "" + file.length());
	 OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	 response.setContentType("application/octet-stream");
	 toClient.write(buffer);
	 toClient.flush();
	 toClient.close();
	 } catch (IOException ex) {
	 ex.printStackTrace();
	 }
	 return response;
	 }

}


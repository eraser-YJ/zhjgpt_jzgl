package com.jc.system.content.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.UploadUtils;
import com.jc.foundation.web.BaseController;
import com.jc.system.content.domain.validator.AttachValidator;
import com.jc.system.content.service.IAttachService;
import com.jc.system.content.service.IOfficeChangeService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件控制器
 * @author Administrator
 * @date 2020-06-30
 */
@Controller
@RequestMapping(value= UploadUtils.REQUEST_URI)
public class AttachController extends BaseController{

	private static final Logger logger = Logger.getLogger(AttachController.class);

	@Autowired
	private IUploadService uploadService;
	@Autowired
	private IAttachService attachService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOfficeChangeService officeChangeService;

	@org.springframework.web.bind.annotation.InitBinder("attach")
    public void initBinder(WebDataBinder binder) {
        	binder.setValidator(new AttachValidator());
    }

	public AttachController() {
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Attach attach,PageManager page){
		PageManager rePage = attachService.query(attach, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage() throws Exception{
		return "attach/attachManage";
	}

	@RequestMapping(value="delete/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Boolean> deleteByIds(Attach attach, @PathVariable String ids) throws Exception{
		Map<String,Boolean> result = new HashMap<>();
		try {
			uploadService.deleteFileByIds(ids);
			result.put(ids, true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put(ids, false);
		}
		return result;
	}

	@RequestMapping(value="deleteDocumentAttach.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Boolean> deleteDocumentAttach(String ids, HttpServletRequest request) throws Exception{
		Map<String,Boolean> result = new HashMap<>(1);
		try {
			uploadService.deleteDocumentAttach(ids,request);
			result.put("state", true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("state", false);
		}
		return result;
	}

	@RequestMapping(value="delete.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Boolean> deleteByIds(String ids) throws Exception{
		Map<String,Boolean> result = new HashMap<>(1);
		try {
			uploadService.deleteFileByIds(ids);
			result.put(ids, true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put(ids, false);
		}
		return result;
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(@Valid Attach attach,BindingResult result) throws Exception{
		Map<String,Object> resultMap = validateBean(result);
		if(!"false".equals(resultMap.get("success"))){
			attachService.save(attach);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer update(Attach attach) throws Exception{
		Integer flag = attachService.update(attach);
		return flag;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public Attach get(Attach attach) throws Exception{
		return attachService.get(attach);
	}

	@RequestMapping(value = "upload.action",method=RequestMethod.POST)
	public void upload(HttpServletRequest request,HttpServletResponse response,String category,String grade) throws Exception {
		String method = request.getMethod() ;
		Map<String,Object>  result = new HashMap<>();
		List<Attach> attachList = new java.util.ArrayList<>();
	    response.setContentType("text/plain");
		if("POST".equals(method.toUpperCase())){
			Attach attach = null;
			if (grade != null && !"".equals(grade)){
				attach = uploadService.uploadFile(request, category,grade);
			} else {
				attach = uploadService.uploadFile(request, category);
			}
			attachList.add(attach);
		}
		if("true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category)){
			if(!GlobalContext.isSysCenter()){
				result.put("httpUrl",GlobalContext.getProperty("api.dataServer"));
			}
		}
		result.put("files",attachList);
		result.put("filePath",GlobalContext.getProperty("FILE_PATH"));
		ObjectMapper mapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(result));
	}

	@RequestMapping(value="deleteUserPhoto.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Boolean> deleteUserPhoto(String id, String fileFolderName, HttpServletRequest request) throws Exception{
		Map<String,Boolean> result = new HashMap<>();
		if(id != null){
			User user = new User();
			user.setId(id);
			user = userService.get(user);
			if(user != null){
				boolean state = uploadService.deleteUserPhoto(user.getId());
				if(state){
					User u = new User();
					u.setId(id);
					u.setPhoto("");
					userService.update2(u);
				}
				result.put("state", state);
			}
		}
		Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
		if(principal != null){
			principal.setPhoto(null);
		}
		return result;
	}


	@RequestMapping(value = "/thumbnail/{id}",method=RequestMethod.GET)
    public void thumbnail(HttpServletRequest request,HttpServletResponse response, @PathVariable String id) throws Exception{
		uploadService.getThumbnail(id, request, response);
    }

	@RequestMapping(value = "/originalRead/{id}",method=RequestMethod.GET)
	public void originalRead(HttpServletRequest request,HttpServletResponse response, @PathVariable String id) throws Exception{
		uploadService.getOriginalImg(id, request, response);
	}

	@RequestMapping(value = {"/originalFile/{id}", "/originalFile/{id}/{type}", "/originalFile/{id}/{type}/{subFile:.+}"},method=RequestMethod.GET)
	public void originalRead(HttpServletRequest request,HttpServletResponse response, @PathVariable String id,
			@PathVariable(required = false) String type, @PathVariable(required = false) String subFile) throws Exception{
		uploadService.getOriginalFile(id, type, subFile, request, response);
	}

	/**
	 * 根据路径直接加载图片
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/originalPath.action", method = RequestMethod.GET)
	public void originalPath(HttpServletRequest request, HttpServletResponse response) {
		uploadService.getOriginalImgByPath(request.getParameter("url"), request, response);
	}

	@RequestMapping(value = "download.action",method=RequestMethod.GET)
	public void downloadFile(HttpServletRequest request,String attachId, HttpServletResponse response) {
		try {
			Attach attach = new Attach();
			attach.setId(attachId);
			attach = attachService.get(attach);
			String fileName = attach.getFileName();
			String resourcesName = attach.getResourcesName();
			uploadService.downloadFile(null, fileName, resourcesName, response, request);
		} catch (CustomException e) {
			logger.error(e);
		}
	}

	@RequestMapping(value="showDocument.action",method=RequestMethod.GET)
	public String showDocument(HttpServletRequest request,String fileId,String documentType) throws Exception{
		Attach attach = new Attach();
		attach.setId(fileId);
		attach = attachService.get(attach);
		String path = GlobalContext.getProperty("FILE_PATH")+attach.getResourcesName();
		String extension = attach.getExt();
		String ipAddress = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		boolean absolutePath = Boolean.parseBoolean(GlobalContext.getProperty("ABSOLUTE_PATH"));
		String root = !absolutePath ? request.getRealPath("/") : "";
		if ("pdf".equals(documentType)) {
			if (!"pdf".equals(extension)) {
				String inputFilePath = root+path;
				String outputFilePath = inputFilePath.replaceAll("."+extension, ".pdf");
				officeChangeService.office2Pdf(inputFilePath, outputFilePath);
				request.setAttribute("path", ipAddress+"/content/attach/originalFile/"+fileId+"/pdf");
			}
			return "/office/playPdf";
		} else {
			String inputFilePath = root+path;
			String outputFilePath = inputFilePath.replaceAll("."+extension, ".html");
			String fileName = outputFilePath.substring(outputFilePath.lastIndexOf('/') + 1, outputFilePath.lastIndexOf('.'));
			outputFilePath = outputFilePath.substring(0, outputFilePath.lastIndexOf('/') + 1) + fileName + "/" + outputFilePath.substring(outputFilePath.lastIndexOf('/') + 1, outputFilePath.length());
			officeChangeService.office2html(inputFilePath, outputFilePath);
			request.setAttribute("path", "/content/attach/originalFile/"+fileId+"/html/");
			return "/office/playHtml";
		}
	}

	@RequestMapping(value = "attachlist.action",method=RequestMethod.GET)
	public void showAttachlist(HttpServletRequest request, HttpServletResponse response,String businessId,String businessTable,String isPaged,String category)  throws Exception {
		Attach attach = new Attach();
		if(businessId==null||"".equals(businessId)){
			businessId = "0";
		}
		attach.setBusinessId(businessId);
		attach.setBusinessTable(businessTable);
		attach.setIsPaged(isPaged);
		attach.setCategory(category);
		Map<String,Object>  result = new HashMap<>();
		List<Attach> attachList = null;
		if ("true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category)) {
			attachList = attachService.queryUserPhoto(attach);
			if (!GlobalContext.isSysCenter()) {
				result.put("httpUrl",GlobalContext.getProperty("api.dataServer"));
			}
		} else {
			try {
				attachList = attachService.queryAll(attach);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	    response.setContentType("text/plain");
		result.put("files", attachList);
		result.put("filePath", GlobalContext.getProperty("FILE_PATH"));
		ObjectMapper mapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(result));
	}

	@RequestMapping(value = "attachlistCustom.action",method=RequestMethod.GET)
	public Map<String, Object> attachlistCustom(HttpServletRequest request, HttpServletResponse response,String businessId,String businessTable,String isPaged,String category)  throws Exception {
		Attach attach = new Attach();
		if (businessId == null || "".equals(businessId)) {
			businessId = "0";
		}
		attach.setBusinessId(businessId);
		attach.setBusinessTable(businessTable);
		attach.setIsPaged(isPaged);
		attach.setCategory(category);
		Map<String,Object>  result = new HashMap<>();
		List<Attach> attachList = null;
		if ("true".equals(GlobalContext.getProperty("cas.start")) && category!=null && "userPhotoImg".equals(category)) {
			attachList = attachService.queryUserPhoto(attach);
			if (!GlobalContext.isSysCenter()) {
				result.put("httpUrl",GlobalContext.getProperty("api.dataServer"));
			}
		} else {
			try {
				attachList = attachService.queryAll(attach);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		result.put("files",attachList);
		return result;
	}

	@RequestMapping(value = "copyAttachlist.action")
	public void copyAttachlist(HttpServletRequest request, HttpServletResponse response,String businessId,String businessTable,String isPaged,String category)  throws Exception {
		Attach attach = new Attach();
		if(businessId==null||"".equals(businessId)){
			businessId = "0";
		}
		attach.setBusinessId(businessId);
		attach.setBusinessTable(businessTable);
		attach.setIsPaged(isPaged);
		attach.setCategory(category);
		Map<String,Object>  result = new HashMap<>();
	    response.setContentType("text/plain");
		List<Attach> attachList = attachService.queryAll(attach);
	    attachList = uploadService.copyAttachList(request, attachList);
		result.put("files",attachList);
		result.put("filePath",GlobalContext.getProperty("FILE_PATH"));
		ObjectMapper mapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(result));
	}

	@RequestMapping(value = "copyAttachAndTextList.action",method=RequestMethod.POST)
	public void copyAttachAndTextList(HttpServletRequest request, HttpServletResponse response,String businessId,String businessTable,String isPaged,String category)  throws Exception {
		Attach attach = new Attach();
		if(businessId==null||"".equals(businessId)){
			businessId = "0";
		}
		attach.setBusinessId(businessId);
		attach.setBusinessTable(businessTable);
		attach.setIsPaged(isPaged);
		attach.setCategory(category);
		Map<String,Object>  result = new HashMap<>(2);
	    response.setContentType("text/plain");
		List<Attach> attachList = attachService.queryAll(attach);
	    attachList = uploadService.copyAttachAndTextList(request, attachList, businessId);
		result.put("files",attachList);
		result.put("filePath",GlobalContext.getProperty("FILE_PATH"));
		ObjectMapper mapper = new ObjectMapper();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(result));
	}
}

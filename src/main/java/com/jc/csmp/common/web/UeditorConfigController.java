package com.jc.csmp.common.web;

import com.baidu.ueditor.ActionEnter;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * ueditor上传
 * @author 常鹏
 * @version 2020-07-27
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UeditorConfigController {

	@Autowired
	private IUploadService uploadService;

	@RequestMapping("config.action")
	public void config(HttpServletRequest request , HttpServletResponse response) throws Exception {
		try{
			String action = request.getParameter("action");
			System.out.println(action);
			if(!StringUtil.isEmpty(action) && "uploadimage".equals(action)){
				request.getRequestDispatcher("/ueditor/upload.action").forward(request, response);
			}else if(!StringUtil.isEmpty(action) && "uploadfile".equals(action)){
				request.getRequestDispatcher("/ueditor/uploadUpFile.action").forward(request, response);
			}else{
				response.setContentType("application/json");
				request.setCharacterEncoding("utf-8");
				response.setHeader("Content-Type", "text/html");
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				String exec = new ActionEnter(request , rootPath).exec();
				PrintWriter pw = response.getWriter();
				pw.write(exec);
				pw.flush();
				pw.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@RequestMapping("upload.action")
	public void upload(@RequestParam(value = "imgFile") MultipartFile imgFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Writer writer = response.getWriter();
		try {
			String fileName = imgFile.getOriginalFilename();
			if (fileName == null || fileName.equals("null") || fileName.trim().length() == 0) {
				writer.write("{'state' : '" + MessageUtils.getMessage("JC_WORDER_001") + "'}");
				return;
			}
			if (!checkExt(fileName)) {
				writer.write("{'state' : '" + MessageUtils.getMessage("JC_WORDER_002") + "'}");
				return;
			}
			Attach attach = uploadService.upload(imgFile, "", request);
			String url = request.getContextPath() + "/content/attach/originalRead/" + attach.getId();
			writer.write("{'original':'" + attach.getFileName() + "','url':'" + url + "','state':'SUCCESS','imgId':'" + attach.getId() + "'}");
			return;
		} finally {
			writer.flush();
			writer.close();
		}
	}

	@RequestMapping("uploadUpFile.action")
	public void uploadUpFile(@RequestParam(value = "upfile") MultipartFile upfile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Writer writer = response.getWriter();
		try {
			String fileName = upfile.getOriginalFilename();
			if (fileName == null || fileName.equals("null") || fileName.trim().length() == 0) {
				writer.write("{'state' : '" + MessageUtils.getMessage("JC_WORDER_001") + "'}");
				return;
			}
			Attach attach = uploadService.upload(upfile, "", request);
			String url = request.getContextPath() + "/content/attach/download.action?attachId=" + attach.getId();
			response.setCharacterEncoding("UTF-8");
			writer.write("{\"original\" : \"" + new String(fileName.getBytes("utf-8"),"ISO-8859-1") + "\" , \"url\" : \"" + url + "\" , \"state\" : \"SUCCESS\"}");
			return;
		} finally {
			writer.flush();
			writer.close();
		}
	}

	@RequestMapping("uploadImg.action")
	public void uploadImg(@RequestParam(value = "changeImg") MultipartFile imgFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Writer writer = response.getWriter();
		try {
			String fileName = imgFile.getOriginalFilename();
			if (fileName == null || fileName.equals("null") || fileName.trim().length() == 0) {
				writer.write("{'state' : '" + MessageUtils.getMessage("JC_SRCOL_020") + "'}");
				return;
			}
			if (!checkExt(fileName)) {
				writer.write("{'state' : '" + MessageUtils.getMessage("JC_SRCOL_021") + "'}");
				return;
			}
			Attach attach = uploadService.upload(imgFile, "", request);
			String url = request.getContextPath() + "/content/attach/originalRead/" + attach.getId();
			writer.write("{'original':'" + attach.getFileName() + "','url':'" + url + "','state':'SUCCESS','imgId':'" + attach.getId() + "'}");
			return;
		} finally {
			writer.flush();
			writer.close();
		}
	}

	@RequestMapping("uploadFile.action")
	public void uploadFile(@RequestParam(value = "changeFile") MultipartFile changeFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Writer writer = response.getWriter();
		try {
			String fileName = changeFile.getOriginalFilename();
			if (fileName == null || fileName.equals("null") || fileName.trim().length() == 0) {
				writer.write("{'state' : '请选择一个文件进行上传！'}");
				return;
			}
			if (!checkExtFile(fileName)) {
				writer.write("{'state' : '只支持txt|doc|docx|xls|xlsx|ppt|pptx等格式的文件！'}");
				return;
			}
			Attach attach = uploadService.upload(changeFile, "", request);
			String url = request.getContextPath() + "/content/attach/download.action?attachId=" + attach.getId();
			writer.write("{'original':'" + attach.getFileName() + "','url':'" + url + "','state':'SUCCESS','imgId':'" + attach.getId() + "'}");
			return;
		} finally {
			writer.flush();
			writer.close();
		}
	}

	public boolean checkExt(String fileName) {
		String word = ",jpg,jpeg,gif,png,";
		if (fileName.indexOf(".") > -1) {
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if (word.indexOf(prefix) > -1) {
				return true;
			}
		}
		return false;
	}

	public boolean checkExtFile(String fileName) {
		String word = ",txt,doc,docx,xls,xlsx,ppt,pptx,";
		if (fileName.indexOf(".") > -1) {
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if (word.indexOf(prefix) > -1) {
				return true;
			}
		}
		return false;
	}
}

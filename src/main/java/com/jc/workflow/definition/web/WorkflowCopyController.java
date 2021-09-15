package com.jc.workflow.definition.web;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.workflow.definition.bean.Definition;
import com.jc.workflow.definition.service.AbstractDeployHandler;
import com.jc.workflow.definition.service.IDefinitionService;
import com.jc.workflow.definition.service.impl.ParseXmlForImportDeploy;
import com.jc.workflow.util.OrganizationUtil;
import com.jc.workflow.util.WorkflowContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by Administrator on 2020-1-9.
 */
@Controller
@RequestMapping(value = "/workflowCopy")
public class WorkflowCopyController extends BaseController {

	Logger logger = Logger.getLogger(WorkflowCopyController.class);
	@Autowired
	private IDefinitionService definitionService;

	/**
	 * @param file
	 * @param newProcessId
	 * @param newProcessName
	 * @param newTypeId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping({"importJson.action"})
	@ResponseBody
	public Map<String, Object> importJson(@RequestParam("file") MultipartFile file, String newProcessId, String newProcessName, String newTypeId) throws IOException {
		String jsonStr = new String(file.getBytes());
		jsonStr = unicodeToString(jsonStr);
		String userId = OrganizationUtil.getLoginUser();
		Map<String, Object> result = importJson(userId, jsonStr, newProcessId, newProcessName, newTypeId);
		result.put("code", "000000");
		return result;
	}

	/**
	 * 发布流程定义
	 * @param jsonStr 流程对应文件
	 * @return
	 */
	@Transactional
	public Map<String,Object> importJson(String userId,String jsonStr, String newProcessId, String newProcessName, String newTypeId) {
		logger.debug("开始的导入流程");
		Map<String,Object> result = null;
		try {
			Map<String,Object> jsonMap = (Map<String,Object>) JsonUtil.json2Java(jsonStr,Map.class);
			Map<String,Object> properties = (Map<String,Object>)jsonMap.get("properties");
			Map<String,Object> processType = (Map<String,Object>)properties.get("process_type");

			String definitionKey = properties.get("process_id").toString();
			String name = properties.get("process_name").toString();
			String typeId = processType.get("id").toString();
			Definition definition = new Definition();
			definition.setJsonStr(jsonStr);
			if(!StringUtil.trimIsEmpty(newProcessId)){
				definition.setDefinitionKey(newProcessId);
			}else{
				definition.setDefinitionKey(definitionKey);
			}
			if(!StringUtil.trimIsEmpty(newProcessName)){
				definition.setName(newProcessName);
			}else{
				definition.setName(name);
			}
			if(!StringUtil.trimIsEmpty(newTypeId)){
				definition.setTypeId(newTypeId);
			}else{
				definition.setTypeId(typeId);
			}
			definition.setCreateUser(userId);
			AbstractDeployHandler deployHandler = new ParseXmlForImportDeploy();
			result =  deployHandler.excute(AbstractDeployHandler.EXECUTE_TYPE_DEPLOY,definition);
			if(WorkflowContext.SUCCESS==result.get("code")){
				logger.info("导入流程成功,流程key为"+definition.getDefinitionKey());
			}else{
				logger.error("导入流程失败,流程key为"+definition.getDefinitionKey()+",失败原因:"+result.get("message").toString());
			}
		}catch(Exception e) {
			logger.error("文件解析失败");
			logger.error("文件为:" + jsonStr);
			e.printStackTrace();
		}
		return result;
	}

	public static String unicodeToString(String str) {
		Pattern pattern = compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	@RequestMapping(value = "loadForm.action")
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		model.addAttribute("data", map);
		return "workflowCopy/workflowCopy";
	}
}

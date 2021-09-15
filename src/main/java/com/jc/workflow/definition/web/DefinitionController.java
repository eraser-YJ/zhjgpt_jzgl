//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jc.workflow.definition.web;

import com.jc.workflow.definition.bean.Definition;
import com.jc.workflow.definition.service.IDefinitionService;
import com.jc.workflow.system.web.BaseController;
import com.jc.workflow.type.bean.Type;
import com.jc.workflow.type.service.ITypeService;
import com.jc.workflow.util.OrganizationUtil;
import com.jc.workflow.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 工作流
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping({"/def"})
public class DefinitionController extends BaseController {
	@Autowired
	private IDefinitionService definitionService;
	@Autowired
	private ITypeService typeService;

	public DefinitionController() {
	}

	@RequestMapping({"openDesignerWindow.action"})
	public String openDesignerWindow() {
		String url = "workflow/auth/unauthorized";
		String userId = OrganizationUtil.getLoginUser();
		if (OrganizationUtil.isWorkflowAdmin(userId)) {
			url = "workflow/def/openDesignerWindow";
		}

		return url;
	}

	@RequestMapping({"getAllDefinitions.action"})
	@ResponseBody
	public Map<String, Object> getAllDefinitions() {
		Map<String, Object> result = new HashMap();
		Map<String, Object> data = new HashMap();
		List<Definition> definitions = this.definitionService.queryAll(new Definition());
		List<Definition> definitionsById = new ArrayList();
		Iterator var6 = definitions.iterator();

		while (var6.hasNext()) {
			Definition definition = (Definition) var6.next();
			Definition definitionById = new Definition();
			definitionById.setDefinitionId(definition.getDefinitionId());
			definitionById.setName(definition.getName() + "-" + definition.getVersion());
			definitionById.setDefinitionKey(definition.getDefinitionKey());
			definitionById.setVersion(definition.getVersion());
			definitionsById.add(definitionById);
		}

		data.put("definitionsById", definitionsById);
		List<Definition> definitionsByKey = this.definitionService.queryByKey(new Definition());
		data.put("definitionsByKey", definitionsByKey);
		result.put("code", "0");
		result.put("data", data);
		return result;
	}

	@RequestMapping({"designer.action"})
	public String designer() {
		return "workflow/def/designer";
	}

	@RequestMapping({"manage.action"})
	public String manage() {
		return "workflow/def/manage";
	}

	@RequestMapping({"getDefAndTypeTreeData.action"})
	@ResponseBody
	public List<Map<String, Object>> getDefAndTypeTreeData() {
		this.definitionService.getStartDefinitions(new Definition(), OrganizationUtil.getLoginUser());
		List<Map<String, Object>> result = new ArrayList();
		List<Definition> definitions = this.definitionService.queryAll(new Definition());
		Iterator var4 = definitions.iterator();

		while (var4.hasNext()) {
			Definition definition = (Definition) var4.next();
			Map<String, Object> item = new HashMap();
			item.put("name", definition.getName());
			item.put("id", "def_" + definition.getId());
			item.put("pId", "type_" + definition.getTypeId());
			item.put("type", "def");
			result.add(item);
		}

		List<Type> types = this.typeService.queryAll(new Type());
		Iterator var7 = types.iterator();

		while (var7.hasNext()) {
			Type type = (Type) var7.next();
			Map<String, Object> item = new HashMap();
			item.put("name", type.getName());
			item.put("id", "type_" + type.getId());
			item.put("pId", "type_-1");
			item.put("type", "type");
			result.add(item);
		}

		return result;
	}

	@RequestMapping({"get.action"})
	@ResponseBody
	public Definition get(String definitionId) {
		if (StringUtil.isEmpty(definitionId)) {
			return null;
		} else {
			Definition definition = new Definition();
			definition.setDefinitionId(definitionId);
			definition = this.definitionService.get(definition);
			definition.setXml("");
			return definition;
		}
	}

	@RequestMapping({"deploy.action"})
	@ResponseBody
	public Map<String, Object> deploy(String jsonStr, String definitionId, String typeId) {
		String userId = OrganizationUtil.getLoginUser();
		Map<String, Object> result = this.definitionService.deploy(jsonStr, typeId, userId);
		return result;
	}

	@RequestMapping({"updateDefinition.action"})
	@ResponseBody
	public Map<String, Object> updateDefinition(String definitionId, String jsonStr) {
		String userId = OrganizationUtil.getLoginUser();
		Map<String, Object> result = this.definitionService.update(definitionId, jsonStr, userId);
		return result;
	}

	@RequestMapping({"delete.action"})
	@ResponseBody
	public Map<String, Object> delete(String id) {
		String userId = OrganizationUtil.getLoginUser();
		Map<String, Object> result = this.definitionService.delete(id, userId);
		return result;
	}

	@RequestMapping({"exportJson.action"})
	@ResponseBody
	public void exportJson(String definitionId, HttpServletResponse response) throws IOException {
		Definition definition = new Definition();
		definition.setDefinitionId(definitionId);
		definition = this.definitionService.get(definition);
		String json = convertUp(definition.getJsonStr());
		response.setContentType("application/octet-stream");
		response.setHeader("content-disposition", "attachment;filename=" + definitionId + ".json");
		FileCopyUtils.copy(json.getBytes(), response.getOutputStream());
	}
	public static String convertUp(String string) {
		StringBuilder unicode = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			unicode.append(String.format("\\u%04x", (int) string.charAt(i)));
		}
		return unicode.toString();
	}
	public static String convert(String gbString) {
		char[] utfBytes = gbString.toCharArray();
		StringBuffer unicodeBytes = new StringBuffer();

		for (int byteIndex = 0; byteIndex < utfBytes.length; ++byteIndex) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}

			unicodeBytes.append("\\u" + hexB);
		}

		return unicodeBytes.toString();
	}

	@RequestMapping({"importJson.action"})
	@ResponseBody
	public Map<String, Object> importJson(@RequestParam("file") MultipartFile file) throws IOException {
		String jsonStr = new String(file.getBytes());
		jsonStr = unicodeToString(jsonStr);
		String userId = OrganizationUtil.getLoginUser();
		Map<String, Object> result = this.definitionService.importJson(userId, jsonStr);
		result.put("code", "000000");
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

	public static String decodeUnicode(String str) {
		Charset set = Charset.forName("UTF-16");
		Pattern p = compile("\\\\u([0-9a-fA-F]{4})");
		Matcher m = p.matcher(str);
		int start = 0;
		int start2 = 0;
		StringBuffer sb = new StringBuffer();
		while (m.find(start)) {
			start2 = m.start();
			if (start2 > start) {
				String seg = str.substring(start, start2);
				sb.append(seg);
			}
			String code = m.group(1);
			int i = Integer.valueOf(code, 16);
			byte[] bb = new byte[4];
			bb[0] = (byte) ((i >> 8) & 0xFF);
			bb[1] = (byte) (i & 0xFF);
			ByteBuffer b = ByteBuffer.wrap(bb);
			sb.append(String.valueOf(set.decode(b)).trim());
			start = m.end();
		}
		start2 = str.length();
		if (start2 > start) {
			String seg = str.substring(start, start2);
			sb.append(seg);
		}
		return sb.toString();
	}

	@RequestMapping({"startPage.action"})
	public String workflow() {
		return "workflow/startPage";
	}
}

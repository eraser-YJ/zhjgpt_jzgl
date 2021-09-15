package com.jc.foundation.excel.web;

import com.jc.foundation.excel.kit.ExcelOperateUtil;
import com.jc.foundation.web.BaseController; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/common/export")
public class ExportController extends BaseController {

	@RequestMapping(value = "choose.action")
	public String choose(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		model.addAttribute("data", map);
		return "common/export/export-choose-param";
	}


	@RequestMapping(value = "monitorExportState.action")
	@ResponseBody
	public Map<String, Object> monitorExportState(HttpServletRequest request, String sessionName) throws Exception {
		HttpSession session = request.getSession();
		String state = (String) session.getAttribute(sessionName);
		if (state == null || state.trim().length() == 0) {
			request.getSession().setAttribute(sessionName, "init");
		}
		Map<String, Object> _reult = new HashMap<String, Object>();
		_reult.put("exportState", state);
		return _reult;
	}

	@RequestMapping(value = "removeExportSession.action")
	@ResponseBody
	public Map<String, Object> removeExportSession(HttpServletRequest request, String sessionName) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute(sessionName);
		return new HashMap<String, Object>();
	}

	// <jsp:include page="/WEB-INF/web/common/export/include/export.jsp">
	// <jsp:param value="crInfoOutList" name="exportJsClassName"/>
	// <jsp:param value="exportExcel" name="exportBtnId"/>
	// <jsp:param value="户籍迁出一览" name="excelName"/>
	// </jsp:include>
}

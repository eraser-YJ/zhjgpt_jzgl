package com.jc.system.security.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jc.foundation.web.BaseController;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.util.menuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Phrase;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.PhraseValidator;
import com.jc.system.security.service.IPhraseService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/phrase")
public class PhraseController extends BaseController {
	
	@Autowired
	private IPhraseService phraseService;

	@org.springframework.web.bind.annotation.InitBinder("phrase")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new PhraseValidator()); 
    }
	
	public PhraseController() {
	}
	
	private PhraseValidator pv = new PhraseValidator();

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="manageList",operateDescribe="对常用词进行查询") 
	public PageManager manageList(Phrase phrase,PageManager page,
			HttpServletRequest request){
		request.getParameterMap();
		phrase.addOrderByFieldDesc("t.CREATE_DATE");
		//获取用户登录信息
		User user = SystemSecurityUtils.getUser();
		if(user.getIsAdmin() == 1){
			phrase.setPhraseType("0");
			phrase.setCreateUser(user.getId());
			return phraseService.queryPhraseForUser(phrase, page);
		}else {
			phrase.setPhraseType("1");
			phrase.setCreateUser(user.getId());
			return phraseService.queryPhraseForUser(phrase, page);
		}
	}
	@ActionLog(operateModelNm="常用词",operateFuncNm="manage",operateDescribe="对常用词进行访问") 
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/sys/phrase/manage.action",request);
		return "sys/phrase/phrase"; 
	}
	
	@RequestMapping(value = "phraseEdit.action",method=RequestMethod.GET)
	public String phraseEdit(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		model.addAttribute("loguservo", SystemSecurityUtils.getUser());
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/phrase/phraseEdit";
	}
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="deleteByIds",operateDescribe="对常用词进行删除") 
	public Integer deleteByIds(Phrase phrase,String ids,HttpServletRequest request) throws Exception{
		phrase.setPrimaryKeys(ids.split(","));
		phrase.setModifyDate(DateUtils.getSysDate());
		return phraseService.delete(phrase);
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="save",operateDescribe="对常用词进行保存") 
	public Map<String,Object> save(@Valid Phrase phrase,BindingResult result,HttpServletRequest request) throws Exception{
		
		pv.validate(phrase, result);
		
		Map<String,Object> resultMap = validateBean(result);
		// 验证bean
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			phrase.setCreateDate(DateUtils.getSysDate());
			phrase.setModifyDate(DateUtils.getSysDate());
			phraseService.save(phrase);
			resultMap.put("success", "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value = "saveForDataTables.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="saveForDataTables",operateDescribe="对常用词进行保存(dataTables)") 
	public Map<String,Object> saveForDataTables(@Valid Phrase phrase,BindingResult result,HttpServletRequest request) throws Exception{
		phrase.setPhraseType("1");
		pv.validate(phrase, result);
		
		Map<String,Object> resultMap = validateBean(result);
		// 验证bean
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			phrase.setCreateDate(DateUtils.getSysDate());
			phrase.setModifyDate(DateUtils.getSysDate());
			phraseService.save(phrase);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="update",operateDescribe="对常用词进行修改")  
	public Map<String, Object> update(Phrase phrase,BindingResult result,HttpServletRequest request) throws Exception{
		pv.validate(phrase, result);
		// 验证bean
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = phraseService.update(phrase);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="updateForDataTables.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="updateForDataTables",operateDescribe="对常用词进行修改(dataTables)")  
	public Phrase updateForDataTables(Phrase phrase,HttpServletRequest request) throws Exception{
		phraseService.update(phrase);
		return phrase;
	}

	@RequestMapping(value="get.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="get",operateDescribe="对常用词进行对象查询")
	public Phrase get(Phrase phrase,HttpServletRequest request) throws Exception{
		return phraseService.get(phrase);
	}
	
	/**
	 * @description 获取当前人的常用词
	 * @return Map<String,List<Phrase>>查询结果  key:person为个人，key:common为公共
	 */
	@RequestMapping(value="queryPhrase.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="常用词",operateFuncNm="queryPhrase",operateDescribe="获取当前人的常用词(包括个人与公共)")
	public  Map<String,List<Phrase>>  queryPhrase(HttpServletRequest request) throws Exception{
		return  phraseService.queryPhrase(SystemSecurityUtils.getUser().getId());
	}
}
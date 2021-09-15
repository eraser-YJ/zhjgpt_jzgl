package com.jc.oa.click.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.web.BaseController;
import com.jc.oa.click.domain.Click;
import com.jc.oa.click.service.IClickService;
@Controller
@RequestMapping(value="/click/clicks")
public class ClickController extends BaseController{
	 
	@Autowired
	private IClickService clickService;
	
	@RequestMapping(value="queryMenuTopFive.action")
	@ResponseBody
	public Map<String, Object> queryTopFive(Model model){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Click click = new Click();
		click.setClickCount(4L);
		List<Click> topFive = clickService.queryAll(click);
		resultMap.put("menuTopFive", topFive);
		model.addAttribute("menuTopFive", topFive);
		return resultMap;
	}
	
}

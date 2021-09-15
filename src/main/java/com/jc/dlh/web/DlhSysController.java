package com.jc.dlh.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.common.kit.vo.ResVO;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类
 * @author lc
 * @version 2020-03-18
 */
@Controller
@RequestMapping(value = "/dlh/sys")
public class DlhSysController extends BaseController {

	@RequestMapping(value = "init.action")
	public String init(HttpServletRequest request) throws Exception {
		return "dlh/dlhSys/index";
	}

	@RequestMapping(value = "clear.action")
	@ResponseBody
	public ResVO clear(HttpServletRequest request) throws Exception {
		return ResVO.buildSuccess();
	}

}
package com.jc.system.api.service;

import java.io.Serializable;
import java.util.List;

import com.jc.foundation.util.JsonResult;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IApiCallback extends Serializable{
	
	/**
	 * 接口服务调用完成后的回调方法
	 * @return
	 */
	boolean callback(List<JsonResult> resultList);
}

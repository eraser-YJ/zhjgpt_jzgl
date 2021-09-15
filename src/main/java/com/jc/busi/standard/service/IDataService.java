package com.jc.busi.standard.service;

import javax.jws.WebService;

@WebService(targetNamespace = "http://webservice.dlh.com")
public interface IDataService {

	/**
	 * @document 发送数据
	 * @param action 行为
	 * @param base64jsonOri 加密后的字符串
	 * @return
	 */
	public String sendData(String action, String base64jsonOri);
}

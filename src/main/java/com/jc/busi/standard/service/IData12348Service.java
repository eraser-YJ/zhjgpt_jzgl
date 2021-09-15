package com.jc.busi.standard.service;

import javax.jws.WebService;

@WebService(targetNamespace = "http://Service_12348.webservice.com")
public interface IData12348Service {

	public String getCallLoss(String json);

	public String getCallRecord(String json);

	public String getCallsCause(String json);

	public String getRecordVisit(String json);

	public String getRecordVisitRes(String json);

	public String getRecordCNT(String json);

	public String sendData(String action, String json);
}

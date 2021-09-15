package com.jc.archive.domain;

import java.util.Date;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;


/**
 * @title  GOA2.0源代码
 * @description OA_知识管理_知识阅读信息表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Readinfo extends BaseBean{
	private static final long serialVersionUID = 1L;
	private Long toaId;   /*主键ID*/
	private Long documentId;   /*文档ID*/
	private Long kmUserId;   /*知识管理_阅读人ID*/
	private Date kmReadtime;   /*知识管理_首次阅读时间*/
	private Date kmReadtimeBegin;   /*知识管理_首次阅读时间开始*/
	private Date kmReadtimeEnd;   /*知识管理_首次阅读时间结束*/
	private Date kmFirstread;   /*知识管理_首次阅读时间*/
	private Date kmFirstreadBegin;   /*知识管理_首次阅读时间开始*/
	private Date kmFirstreadEnd;   /*知识管理_首次阅读时间结束*/
	private Date kmLastread;   /*知识管理_最后阅读时间*/
	private Date kmLastreadBegin;   /*知识管理_最后阅读时间开始*/
	private Date kmLastreadEnd;   /*知识管理_最后阅读时间结束*/
	private Integer kmReadFlag;   /*知识管理_阅读状态(0未读;1已读)*/
	private Integer weight;   /**/

	public Long getToaId(){
		return toaId;
	}
	
	public void setToaId(Long toaId){
		this.toaId = toaId;
	}
	
	public Long getDocumentId(){
		return documentId;
	}
	
	public void setDocumentId(Long documentId){
		this.documentId = documentId;
	}
	
	public Long getKmUserId(){
		return kmUserId;
	}
	
	public void setKmUserId(Long kmUserId){
		this.kmUserId = kmUserId;
	}
	
	public Date getKmReadtime(){
		return kmReadtime;
	}
	
	public void setKmReadtime(Date kmReadtime){
		this.kmReadtime = kmReadtime;
	}
	
	public Date getKmReadtimeBegin(){
		return kmReadtime;
	}
	
	public void setKmReadtimeBegin(Date kmReadtime){
		this.kmReadtime = kmReadtime;
	}
	
	public Date getKmReadtimeEnd(){
		return kmReadtime;
	}
	
	public void setKmReadtimeEnd(Date kmReadtime){
	   
		this.kmReadtime = DateUtils.fillTime(kmReadtime);
	}
	public Date getKmFirstread(){
		return kmFirstread;
	}
	
	public void setKmFirstread(Date kmFirstread){
		this.kmFirstread = kmFirstread;
	}
	
	public Date getKmFirstreadBegin(){
		return kmFirstread;
	}
	
	public void setKmFirstreadBegin(Date kmFirstread){
		this.kmFirstread = kmFirstread;
	}
	
	public Date getKmFirstreadEnd(){
		return kmFirstread;
	}
	
	public void setKmFirstreadEnd(Date kmFirstread){
	   
		this.kmFirstread = DateUtils.fillTime(kmFirstread);
	}
	public Date getKmLastread(){
		return kmLastread;
	}
	
	public void setKmLastread(Date kmLastread){
		this.kmLastread = kmLastread;
	}
	
	public Date getKmLastreadBegin(){
		return kmLastread;
	}
	
	public void setKmLastreadBegin(Date kmLastread){
		this.kmLastread = kmLastread;
	}
	
	public Date getKmLastreadEnd(){
		return kmLastread;
	}
	
	public void setKmLastreadEnd(Date kmLastread){
	   
		this.kmLastread = DateUtils.fillTime(kmLastread);
	}
	public Integer getKmReadFlag(){
		return kmReadFlag;
	}
	
	public void setKmReadFlag(Integer kmReadFlag){
		this.kmReadFlag = kmReadFlag;
	}
	
	@Override
    public Integer getWeight(){
		return weight;
	}
	
	@Override
	public void setWeight(Integer weight){
		this.weight = weight;
	}
	
}
package com.jc.archive.domain;

import com.jc.foundation.domain.BaseBean;


/**
 * @title  GOA2.0源代码
 * @description OA_知识管理_知识推荐表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Recommend extends BaseBean{
	private static final long serialVersionUID = 1L;
	private Long toaId;   /*主键ID*/
	private Long documentId;   /*文档ID*/
	private String fileType;   /*文件存放类型：0文档 1 知识 2 链接 3 收藏 */
	private Integer kmRemind;   /*知识管理_通知提醒[0-不提醒;1-邮件;2-短信]*/
	private Long kmReceive;   /*知识管理_推荐接收人ID*/
	private String kmReason;   /*知识管理_订阅原因*/
	private Long kmSender;   /*知识管理_推荐人ID*/
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
	
	public String getFileType(){
		return fileType;
	}
	
	public void setFileType(String fileType){
		this.fileType = fileType;
	}
	
	public Integer getKmRemind(){
		return kmRemind;
	}
	
	public void setKmRemind(Integer kmRemind){
		this.kmRemind = kmRemind;
	}
	
	public Long getKmReceive(){
		return kmReceive;
	}
	
	public void setKmReceive(Long kmReceive){
		this.kmReceive = kmReceive;
	}
	
	public String getKmReason(){
		return kmReason;
	}
	
	public void setKmReason(String kmReason){
		this.kmReason = kmReason;
	}
	
	public Long getKmSender(){
		return kmSender;
	}
	
	public void setKmSender(Long kmSender){
		this.kmSender = kmSender;
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
package com.jc.archive.domain;

import java.util.Date;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;


/**
 * @title  GOA2.0源代码
 * @description OA_知识管理_知识订阅表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Subscribe extends BaseBean{
	private static final long serialVersionUID = 1L;
	private Long subsFolderId;   /*订阅文件夹ID*/
	private Long kmUserId;   /*知识管理_阅读人ID*/
	private Date endDate;   /*知识管理_订阅截止日期*/
	private Date endDateBegin;   /*知识管理_订阅截止日期开始*/
	private Date endDateEnd;   /*知识管理_订阅截止日期结束*/
	private String kmReason;   /*知识管理_订阅原因*/
	private Integer kmRemind;   /*知识管理_通知提醒(0-邮件1-短信)*/
	private Date kmEndDate;   /**/
	private Date kmEndDateBegin;   /*开始*/
	private Date kmEndDateEnd;   /*结束*/
	private Integer weight;   /**/

	public Long getSubsFolderId(){
		return subsFolderId;
	}
	
	public void setSubsFolderId(Long subsFolderId){
		this.subsFolderId = subsFolderId;
	}
	
	public Long getKmUserId(){
		return kmUserId;
	}
	
	public void setKmUserId(Long kmUserId){
		this.kmUserId = kmUserId;
	}
	
	public Date getEndDate(){
		return endDate;
	}
	
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}
	
	public Date getEndDateBegin(){
		return endDate;
	}
	
	public void setEndDateBegin(Date endDate){
		this.endDate = endDate;
	}
	
	public Date getEndDateEnd(){
		return endDate;
	}
	
	public void setEndDateEnd(Date endDate){
	   
		this.endDate = DateUtils.fillTime(endDate);
	}
	public String getKmReason(){
		return kmReason;
	}
	
	public void setKmReason(String kmReason){
		this.kmReason = kmReason;
	}
	
	public Integer getKmRemind(){
		return kmRemind;
	}
	
	public void setKmRemind(Integer kmRemind){
		this.kmRemind = kmRemind;
	}
	
	public Date getKmEndDate(){
		return kmEndDate;
	}
	
	public void setKmEndDate(Date kmEndDate){
		this.kmEndDate = kmEndDate;
	}
	
	public Date getKmEndDateBegin(){
		return kmEndDate;
	}
	
	public void setKmEndDateBegin(Date kmEndDate){
		this.kmEndDate = kmEndDate;
	}
	
	public Date getKmEndDateEnd(){
		return kmEndDate;
	}
	
	public void setKmEndDateEnd(Date kmEndDate){
	   
		this.kmEndDate = DateUtils.fillTime(kmEndDate);
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
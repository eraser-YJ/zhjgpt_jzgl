package com.jc.archive.domain;

import java.util.Date;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;


/**
 * @title  GOA2.0源代码
 * @description OA_知识管理_知识评论表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Comment extends BaseBean{
	private static final long serialVersionUID = 1L;
	private Long toaId;   /*主键ID*/
	private Long documentId;   /*知识管理_知识信息ID*/
	private String kmComment;   /*知识管理_评论内容*/
	private Long parentCommId;   /*评论父ID*/
	private Long kmCriticid;   /*知识管理_评论人ID*/
	private Date kmCommentdate;   /*知识管理_评论时间*/
	private Date kmCommentdateBegin;   /*知识管理_评论时间开始*/
	private Date kmCommentdateEnd;   /*知识管理_评论时间结束*/
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
	
	public String getKmComment(){
		return kmComment;
	}
	
	public void setKmComment(String kmComment){
		this.kmComment = kmComment;
	}
	
	public Long getParentCommId(){
		return parentCommId;
	}
	
	public void setParentCommId(Long parentCommId){
		this.parentCommId = parentCommId;
	}
	
	public Long getKmCriticid(){
		return kmCriticid;
	}
	
	public void setKmCriticid(Long kmCriticid){
		this.kmCriticid = kmCriticid;
	}
	
	public Date getKmCommentdate(){
		return kmCommentdate;
	}
	
	public void setKmCommentdate(Date kmCommentdate){
		this.kmCommentdate = kmCommentdate;
	}
	
	public Date getKmCommentdateBegin(){
		return kmCommentdate;
	}
	
	public void setKmCommentdateBegin(Date kmCommentdate){
		this.kmCommentdate = kmCommentdate;
	}
	
	public Date getKmCommentdateEnd(){
		return kmCommentdate;
	}
	
	public void setKmCommentdateEnd(Date kmCommentdate){
	   
		this.kmCommentdate = DateUtils.fillTime(kmCommentdate);
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
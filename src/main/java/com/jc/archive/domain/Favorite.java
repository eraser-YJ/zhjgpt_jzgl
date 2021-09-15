package com.jc.archive.domain;

import java.util.Date;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;


/**
 * @title  GOA2.0源代码
 * @description OA_文档管理/知识管理_收藏信息表 实体类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */

public class Favorite extends BaseBean{
	private static final long serialVersionUID = 1L;
	private Long collectId;   /*收藏人ID*/
	private Long favoriteId;   /*收藏文件ID*/
	private String fileType;   /*文件存放类型：0文档 1 知识 2 链接 3 收藏 */
	private String physicalPath;   /*文件物理地址*/
	private String dmName;   /*文档管理_文档名称*/
	private String dmAlias;   /*文档在服务器中的名称已当前时间的long值名称*/
	private String kmContent;   /*知识管理_知识内容*/
	private String keyWords;   /*关键字(文档/知识)*/
	private String dmType;   /*文档管理_文档类型(字典项)*/
	private String author;   /*作者*/
	private String kmTitle;   /*知识标题*/
	private Integer currentVersion;   /*版本号*/
	private String dmSize;   /*文档大小*/
	private String seq;   /*编号*/
	private Integer weight;   /**/
	private Long dmCreateUser;   /*创建人ID*/
	private Date kmReleaseDate;   /*创建时间*/
	private Date kmReleaseDateBegin;   /*创建时间开始*/
	private Date kmReleaseDateEnd;   /*创建时间结束*/

	public Long getCollectId(){
		return collectId;
	}
	
	public void setCollectId(Long collectId){
		this.collectId = collectId;
	}
	
	public Long getFavoriteId(){
		return favoriteId;
	}
	
	public void setFavoriteId(Long favoriteId){
		this.favoriteId = favoriteId;
	}
	
	public String getFileType(){
		return fileType;
	}
	
	public void setFileType(String fileType){
		this.fileType = fileType;
	}
	
	public String getPhysicalPath(){
		return physicalPath;
	}
	
	public void setPhysicalPath(String physicalPath){
		this.physicalPath = physicalPath;
	}
	
	public String getDmName(){
		return dmName;
	}
	
	public void setDmName(String dmName){
		this.dmName = dmName;
	}
	
	public String getDmAlias(){
		return dmAlias;
	}
	
	public void setDmAlias(String dmAlias){
		this.dmAlias = dmAlias;
	}
	
	public String getKmContent(){
		return kmContent;
	}
	
	public void setKmContent(String kmContent){
		this.kmContent = kmContent;
	}
	
	public String getKeyWords(){
		return keyWords;
	}
	
	public void setKeyWords(String keyWords){
		this.keyWords = keyWords;
	}
	
	public String getDmType(){
		return dmType;
	}
	
	public void setDmType(String dmType){
		this.dmType = dmType;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getKmTitle(){
		return kmTitle;
	}
	
	public void setKmTitle(String kmTitle){
		this.kmTitle = kmTitle;
	}
	
	public Integer getCurrentVersion(){
		return currentVersion;
	}
	
	public void setCurrentVersion(Integer currentVersion){
		this.currentVersion = currentVersion;
	}
	
	public String getDmSize(){
		return dmSize;
	}
	
	public void setDmSize(String dmSize){
		this.dmSize = dmSize;
	}
	
	public String getSeq(){
		return seq;
	}
	
	public void setSeq(String seq){
		this.seq = seq;
	}
	
	@Override
    public Integer getWeight(){
		return weight;
	}
	
	@Override
	public void setWeight(Integer weight){
		this.weight = weight;
	}
	
	public Long getDmCreateUser(){
		return dmCreateUser;
	}
	
	public void setDmCreateUser(Long dmCreateUser){
		this.dmCreateUser = dmCreateUser;
	}
	
	public Date getKmReleaseDate(){
		return kmReleaseDate;
	}
	
	public void setKmReleaseDate(Date kmReleaseDate){
		this.kmReleaseDate = kmReleaseDate;
	}
	
	public Date getKmReleaseDateBegin(){
		return kmReleaseDate;
	}
	
	public void setKmReleaseDateBegin(Date kmReleaseDate){
		this.kmReleaseDate = kmReleaseDate;
	}
	
	public Date getKmReleaseDateEnd(){
		return kmReleaseDate;
	}
	
	public void setKmReleaseDateEnd(Date kmReleaseDate){
	   
		this.kmReleaseDate = DateUtils.fillTime(kmReleaseDate);
	}
}
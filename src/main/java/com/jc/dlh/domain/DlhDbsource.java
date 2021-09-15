package com.jc.dlh.domain;

import com.jc.foundation.domain.BaseBean;



/**
 * @title 统一数据资源中心
 * @description 数据源 实体类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-04-07
 */

public class DlhDbsource extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	
	public DlhDbsource() {
	}	
	
	/**数据源地址*/
	private String dbAddress;   
	/**数据源编码*/
	private String dbCode;   
	/**数据源驱动*/
	private String dbDriver;   
	/**密码*/
	private String dbPassword;   
	/**数据源状态*/
	private String dbStatus;   
	/**数据源描述*/
	private String dbTxt;   
	/**数据源类型*/
	private String dbType;   
	/**用户名称*/
	private String dbUsername;   


	
	public String getDbAddress(){
		return dbAddress;
	}
	
	public void setDbAddress(String dbAddress){
		this.dbAddress = dbAddress;
	}
	
	
	public String getDbCode(){
		return dbCode;
	}
	
	public void setDbCode(String dbCode){
		this.dbCode = dbCode;
	}
	
	
	public String getDbDriver(){
		return dbDriver;
	}
	
	public void setDbDriver(String dbDriver){
		this.dbDriver = dbDriver;
	}
	
	
	public String getDbPassword(){
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword){
		this.dbPassword = dbPassword;
	}
	
	
	public String getDbStatus(){
		return dbStatus;
	}
	
	public void setDbStatus(String dbStatus){
		this.dbStatus = dbStatus;
	}
	
	
	public String getDbTxt(){
		return dbTxt;
	}
	
	public void setDbTxt(String dbTxt){
		this.dbTxt = dbTxt;
	}
	
	
	public String getDbType(){
		return dbType;
	}
	
	public void setDbType(String dbType){
		this.dbType = dbType;
	}
	
	
	public String getDbUsername(){
		return dbUsername;
	}
	
	public void setDbUsername(String dbUsername){
		this.dbUsername = dbUsername;
	}
	


}
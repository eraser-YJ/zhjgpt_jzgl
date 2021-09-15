package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class Unique extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public Unique() {
	}

	/**唯一编号*/
	private String uuid;
	/**用途*/
	private String purpose;
	/**批量生成数量*/
	private String state;

	public String getUuid(){
		return uuid;
	}
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	public String getPurpose(){
		return purpose;
	}
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	public String getState(){
		return state;
	}
	public void setState(String state){
		this.state = state;
	}
}
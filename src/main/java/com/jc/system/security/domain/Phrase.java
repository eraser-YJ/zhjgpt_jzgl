package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 常用词
 * @author Administrator
 * @date 2020-06-30
 */
@ApiModel("常用词")
public class Phrase extends BaseBean{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "常用词内容", name = "content")
	private String content;
	@ApiModelProperty(value = "常用词类型", name = "phraseType")
	private String phraseType;
	private String userName;

	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getPhraseType(){
		return phraseType;
	}
	public void setPhraseType(String phraseType){
		this.phraseType = phraseType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
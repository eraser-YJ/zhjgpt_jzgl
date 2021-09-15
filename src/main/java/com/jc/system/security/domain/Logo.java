package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.IDicManager;
import org.apache.log4j.Logger;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class Logo extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public Logo() {
	}

	/**登录页样式名称*/
	private String logoName;
	/**登录页样式标识*/
	private String logoSign;
	/**登录页预览图片路径*/
	private String logoPath;
	/**登录页选定标记 1 选中 0 未选中*/
	private Integer logoFlag;
	/**附件Id*/
	private String fileids;
	/**删除附件Id*/
	private String delattachIds;

	public String getLogoName(){
		return logoName;
	}
	public void setLogoName(String logoName){
		this.logoName = logoName;
	}
	public String getLogoSign(){
		return logoSign;
	}
	public void setLogoSign(String logoSign){
		this.logoSign = logoSign;
	}
	public String getLogoPath(){
		return logoPath;
	}
	public void setLogoPath(String logoPath){
		this.logoPath = logoPath;
	}
	public Integer getLogoFlag(){
		return logoFlag;
	}
	public void setLogoFlag(Integer logoFlag){
		this.logoFlag = logoFlag;
	}
	public String getFileids() {
		return fileids;
	}
	public void setFileids(String fileids) {
		this.fileids = fileids;
	}
	public String getDelattachIds() {
		return delattachIds;
	}
	public void setDelattachIds(String delattachIds) {
		this.delattachIds = delattachIds;
	}
}
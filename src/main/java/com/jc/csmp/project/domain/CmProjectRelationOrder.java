package com.jc.csmp.project.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;

import java.util.*;

/**
 * 建设管理-项目工程联系单管理
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmProjectRelationOrder extends BaseBean {

	private static final long serialVersionUID = 1L;
	public CmProjectRelationOrder() {
	}
	/**单号*/
	private String code;
	/**项目ID*/
	private String projectId;
	/**发起部门*/
	private String sendDept;
	/**签收部门*/
	private String signerDept;
	/**主题*/
	private String title;
	/**内容*/
	private String content;
	/**签收时间*/
	private Date signDate;
	private Date signDateBegin;
	private Date signDateEnd;
	/**签收状态*/
	private String signStatus;

	private String projectNumber;
	private String projectName;
	/**部门负责人*/
	private String signerDeptLeaderId;
	/**数据权限查看条件*/
	private String deptCondition;
	/**签收部门*/
	private String receiverDept;

	/**联系单id*/
	private String relationId;
	/**人员类型*/
	private String personType;
	/**签收部门*/
	private String deptId;

	/**
	 * 附件信息
	 */
	private String attachFile;
	private String deleteAttachFile;

	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setSignerDept(String signerDept) {
		this.signerDept = signerDept;
	}
	public String getSignerDept() {
		return signerDept;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setSignDateBegin(Date signDateBegin) {
		this.signDateBegin = signDateBegin;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getSignDateBegin() {
		return signDateBegin;
	}
	public void setSignDateEnd(Date signDateEnd) {
		if(signDateEnd == null){
			return;
		}
		this.signDateEnd = DateUtils.fillTime(signDateEnd);
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getSignDateEnd() {
		return signDateEnd;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd",  timezone = "GMT+8")
	public Date getSignDate() {
		return signDate;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public String getSignStatusValue() {
		String value = "";
		if (this.getSignStatus() != null) {
			if (this.getSignStatus().equals("0")) {
				value = "未签收";
			} else if (this.getSignStatus().equals("1")) {
				value = "已签收";
			}
		}
		return value;
	}
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSignerDeptValue() {
		return DeptCacheUtil.getNameById(this.getSignerDept());
	}

	public String getSignerDeptLeaderId() {
		return signerDeptLeaderId;
	}

	public void setSignerDeptLeaderId(String signerDeptLeaderId) {
		this.signerDeptLeaderId = signerDeptLeaderId;
	}

	public String getSendDept() {
		return sendDept;
	}

	public void setSendDept(String sendDept) {
		this.sendDept = sendDept;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getPersonType() {
		return personType;
	}

	public String getPersonTypeValue() {
		if (!StringUtil.isEmpty(this.getPersonType())) {
			if (this.getPersonType().equals("1")) {
				return "签收组织";
			} else if (this.getPersonType().equals("2")) {
				return "抄送组织";
			}
		}
		return "";
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getReceiverDept() {
		return receiverDept;
	}

	public void setReceiverDept(String receiverDept) {
		this.receiverDept = receiverDept;
	}

	public String getDeptCondition() {
		return deptCondition;
	}

	public void setDeptCondition(String deptCondition) {
		this.deptCondition = deptCondition;
	}

	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}

	public String getDeleteAttachFile() {
		return deleteAttachFile;
	}

	public void setDeleteAttachFile(String deleteAttachFile) {
		this.deleteAttachFile = deleteAttachFile;
	}

	public List<Map<String, Object>> getReceiverDeptValue() {
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (!StringUtil.isEmpty(this.getReceiverDept())) {
			String[] deptIdArray = GlobalUtil.splitStr(this.getReceiverDept(), ',');
			if (deptIdArray != null) {
				for (String s : deptIdArray) {
					try {
						Department department = DeptCacheUtil.getDeptById(s);
						if (department != null) {
							Map<String, Object> map = new HashMap<>(2);
							map.put("id", department.getId());
							map.put("text", department.getName());
							resultList.add(map);
						}
					} catch (CustomException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return resultList;
	}
}
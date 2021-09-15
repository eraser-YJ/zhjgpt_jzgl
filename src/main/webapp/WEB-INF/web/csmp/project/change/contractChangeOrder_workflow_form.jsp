<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ include file="/WEB-INF/web/include/workflowHide.jsp"%>
<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>
<input type="hidden" id="defaultApplyDept" name="defaultApplyDept" value="${defaultApplyDept}" />
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>变更及签证管理 > </span><span>变更管理 > </span><span>合同变更单申请</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap m-20-auto" style="margin-top: -5px;" id="entityForm">
			<div>
				<h3 class="tc">基本信息</h3>
				<input type="hidden" id="businessJson"	value='${businessJson}'/>
				<input type="hidden" id="token" name="token" value="${token}"/>
				<input type="hidden" id="id" name="id" value="0"/>
				<input type="hidden" id="piId" name="piId" value='${piId}'/>
				<input type="hidden" id="changeType" name="changeType" value="${changeType}" />
				<input type="hidden" id="partaUnitLeaderId" itemName="partaUnitLeaderId" workFlowForm="hidden" flowVariable="partaUnitLeaderId"/>
				<input type="hidden" id="modifyDate" name="modifyDate"/>
				<table class="table form-table table-td-striped">
					<tbody>
					<tr>
						<td class="w100"><span class='required'>*</span>单号</td>
						<td><input type="text" id="code" name="code" workFlowForm='textinput' itemName="code" readonly="readonly" value="${applyCode}" /></td>
						<td class="w100"><span class='required'>*</span>申请单位</td>
						<td>
							<input type="text" id="deptName" name="deptName" workFlowForm='textinput' itemName="deptId" readonly="readonly" />
							<input type="hidden" id="deptId" name="deptId" />
						</td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>所属项目</td>
						<td>
							<input type="text" id="projectName" name="projectName" workFlowForm='textinput' itemName="projectId" readonly="readonly" />
							<input type="hidden" id="projectId" name="projectId" />
						</td>
						<td class="w100"><span class='required'>*</span>所属合同</td>
						<td>
							<span workFlowForm='select' itemName="contractId">
								<select id="contractId" name="contractId"></select>
							</span>
						</td>
					</tr>
					<tr id="buildDeptTrContainer" style="display: none;">
						<td class="w100">审批人姓名</td>
						<td><input type="text" id="buildDeptPersion" name="buildDeptPersion" readonly="readonly" /></td>
						<td class="w100">审批人电话</td>
						<td><input type="text" id="buildDeptPersionMobile" name="buildDeptPersionMobile" readonly="readonly" /></td>
					</tr>
					<tr>
						<td class="w100">变更费用(万元)</td>
						<td><input type="text" id="changeAmount" name="changeAmount" workFlowForm='textinput' itemName="changeAmount" /></td>
						<td class="w100"><span class='required'>*</span>变更日期</td>
						<td><input class="datepicker-input" type="text" id="changeDate" name="changeDate" value="<%=com.jc.foundation.util.GlobalUtil.dateUtil2String(new java.util.Date(System.currentTimeMillis()), "yyyy-MM-dd") %>" data-pick-time="false" data-date-format="yyyy-MM-dd" workFlowForm='textinput' itemName="changeDate" /></td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>变更类型</td>
						<td>
							<span workFlowForm='radio' itemName="modifyType">
								<label class="radio inline" for="modifyType0"><input type="radio" name="modifyType" id="modifyType0" value="0" onclick="$('#entityForm #modifyTypeContrainer').val(0);" label="增加" />增加</label>
								<label class="radio inline" for="modifyType1"><input type="radio" name="modifyType" id="modifyType1" value="1" onclick="$('#entityForm #modifyTypeContrainer').val(1);" label="减少" />减少</label>
							</span>
							<input type="hidden" name="modifyTypeContrainer" id="modifyTypeContrainer" />
						</td>
						<td class="w100"><span class='required'>*</span>经办人</td>
						<td><input type="text" id="handleUser" name="handleUser" workFlowForm='textinput' itemName="handleUser" /></td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>变更原因</td>
						<td colspan="3"><textarea id="changeReason" name="changeReason" style="height: 80px" workFlowForm='textinput' itemName="changeReason"></textarea></td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>变更内容</td>
						<td colspan="3"><textarea id="changeContent" name="changeContent" style="height: 80px" workFlowForm='textinput' itemName="changeContent"></textarea></td>
					</tr>
					</tbody>
				</table>
			</div>
			<div><h3 class="tc">附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
			<div itemName="optionContainer">
				<h3>办理意见</h3>
				<table class="table form-table table-td-striped">
					<tr>
						<td class="w100"><span class='required'>*</span>办理意见</td>
						<td colspan="3"><span class="workflowText"><workflow:suggest itemId="opinionCtrl" showLast="false" order="createTime" showWritePannel="false" classStr="" maxLength="500"/></span></td>
					</tr>
				</table>
			</div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center;">
			<section id="workflowFormButton" class=" form-btn statistics-box"></section>
		</div>
	</section>

	<div id="chooseCompanyModule"></div>
</section>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src="${sysPath}/js/com/jc/csmp/project/change/contractChangeOrder_workflow_form.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ include file="/WEB-INF/web/include/workflowHide.jsp"%>
<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>问题协同管理 > </span><span id="sectionTitleOne"></span><span id="sectionTitleTwo"></span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap m-20-auto" id="entityForm" style="margin-top: -5px;">
			<div>
				<h3 class="tc">基本信息</h3>
				<input type="hidden" id="businessJson"	value='${businessJson}'/>
				<input type="hidden" id="attchBusinessTable" name="attchBusinessTable" value="${attchBusinessTable}" />
				<input type="hidden" id="token" name="token" value="${token}"/>
				<input type="hidden" id="id" name="id" value="0"/>
				<input type="hidden" id="piId" name="piId" value='${piId}'/>
				<input type="hidden" id="partaUnitLeaderId" name="partaUnitLeaderId" itemName="partaUnitLeaderId" workFlowForm="hidden" flowVariable="partaUnitLeaderId"/>
				<input type="hidden" id="superviseLeaderId" name="superviseLeaderId" itemName="superviseLeaderId" workFlowForm="hidden" flowVariable="superviseLeaderId"/>
				<input type="hidden" id="modifyDate" name="modifyDate"/>
				<input type="hidden" id="questionType" name="questionType" value="${queryType}" />
				<input type="hidden" id="rectificationCompany" name="rectificationCompany" />
				<table class="table form-table table-td-striped">
					<tbody>
					<tr>
						<td style="width: 10%"><span class='required'>*</span>编号</td>
						<td style="width: 40%"><input type="text" id="code" name="code" workFlowForm='textinput' value="${applyCode}" readonly="readonly" itemName="code" /></td>
						<td style="width: 10%"><span class='required'>*</span>所属项目</td>
						<td style="width: 40%">
							<input type="text" id="projectName" name="projectName" workFlowForm='textinput' itemName="projectId" readonly="readonly" />
							<input type="hidden" id="projectId" name="projectId" />
						</td>
					</tr>
					<tr>
						<td><span class='required'>*</span>所属合同</td>
						<td>
							<span workFlowForm='select' itemName="contractId">
								<select id="contractId" name="contractId"><option value="">-请选择-</option></select>
							</span>
						</td>
						<td>整改单位</td>
						<td id="rectificationCompanyHtml"></td>
					</tr>
					<tr id="buildDeptTrContainer" style="display: none;">
						<td class="w100">审批人姓名</td>
						<td><input type="text" id="buildDeptPersion" name="buildDeptPersion" readonly="readonly" /></td>
						<td class="w100">审批人电话</td>
						<td><input type="text" id="buildDeptPersionMobile" name="buildDeptPersionMobile" readonly="readonly" /></td>
					</tr>
					<tr>
						<td><span class='required'>*</span>标题</td>
						<td><input type="text" id="title" name="title" workFlowForm='textinput' itemName="title" /></td>
						<td id="safeFailureTypeTitle"><span class='required'>*</span>安全事故</td>
						<td>
							<span workFlowForm='radio' itemName="safeFailureType">
								<label class="radio inline" for="safeFailureType1"><input type="radio" name="safeFailureType" id="safeFailureType1" onclick="$('#entityForm #safeFailureTypeContrainer').val(1);" value="1" label="问题" />问题</label>
								<label class="radio inline" for="safeFailureType2"><input type="radio" name="safeFailureType" id="safeFailureType2" onclick="$('#entityForm #safeFailureTypeContrainer').val(2);" value="2" label="隐患" />隐患</label>
							</span>
							<input type="hidden" name="safeFailureTypeContrainer" id="safeFailureTypeContrainer" />
						</td>
					</tr>
					<tr>
						<td><span class='required'>*</span>问题描述</td>
						<td colspan="3"><textarea id="questionMeta" name="questionMeta" workFlowForm='textinput' style="height: 80px;" itemName="questionMeta"></textarea></td>
					</tr>
					<tr>
						<td>整改要求</td>
						<td colspan="3"><textarea id="rectificationAsk" name="rectificationAsk" workFlowForm='textinput' style="height: 80px;" itemName="rectificationAsk"></textarea></td>
					</tr>
					<tr>
						<td>整改结果</td>
						<td colspan="3"><textarea id="rectificationResult" name="rectificationResult" workFlowForm='textinput' style="height: 80px;" itemName="rectificationResult"></textarea></td>
					</tr>
					<tr itemName="problemDeptContainer">
						<td>处置负责人</td>
						<td colspan="3">
							<span workFlowForm='select' itemName="problemDept">
								<select id="problemDept" name="problemDept"><option value="">-请选择-</option></select>
							</span>
						</td>
					</tr>
					<tr itemName="problemHandlingContainer">
						<td><span class='required'>*</span>问题处置</td>
						<td colspan="3"><textarea id="problemHandling" name="problemHandling" workFlowForm='textinput' style="height: 80px;" itemName="problemHandling"></textarea></td>
					</tr>
					<tr>
						<td>备注</td>
						<td colspan="3"><textarea id="remark" name="remark" workFlowForm='textinput' style="height: 80px;" itemName="remark"></textarea></td>
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
			<section id="workflowFormButton" class=" form-btn statistics-box">

			</section>
		</div>
	</section>

</section>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/question/projectQuestion_form.js' type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
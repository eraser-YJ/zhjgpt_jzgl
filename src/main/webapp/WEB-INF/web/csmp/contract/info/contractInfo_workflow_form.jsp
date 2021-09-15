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
			<span>合同管理 > </span><span>合同备案</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap m-20-auto" style="margin-top: -5px;" id="entityForm">
			<div>
				<h3 class="tc">合同备案申请</h3>
				<input type="hidden" id="businessJson"	value='${businessJson}'/>
				<input type="hidden" id="token" name="token" value="${token}"/>
				<input type="hidden" id="id" name="id" value="0"/>
				<input type="hidden" id="piId" name="piId" value='${piId}'/>
				<input type="hidden" id="partyaUnit" name="partyaUnit" />
				<input type="hidden" id="modifyDate" name="modifyDate"/>
				<table class="table form-table table-td-striped">
					<tbody>
						<tr>
							<td class="w100"><span class='required'>*</span>所属项目</td>
							<td class="w300">
								<input type="text" id="projectName" name="projectName" workFlowForm='textinput' itemName="projectId" readonly="readonly" />
								<input type="hidden" id="projectId" name="projectId" />
							</td>
							<td class="w100"><span class='required'>*</span>合同编号</td>
							<td class="w300"><input type="text" id="contractCode" name="contractCode" workFlowForm='textinput' itemName="contractCode" /></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>合同名称</td>
							<td class="w300"><input type="text" id="contractName" name="contractName" workFlowForm='textinput' itemName="contractName" /></td>
							<td class="w100"><span class='required'>*</span>合同类型</td>
							<td class="w300">
								<span workFlowForm='select' itemName="contractType">
									<dic:select name="contractType" id="contractType" dictName="contract_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>合同金额(万元)</td>
							<td class="w300"><input type="text" id="contractMoney" name="contractMoney" workFlowForm='textinput' itemName="contractMoney" /></td>
							<td class="w100"><span class='required'>*</span>工期(天)</td>
							<td class="w300"><input type="text" id="constructionPeriod" name="constructionPeriod" workFlowForm='textinput' itemName="constructionPeriod" /></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>发包单位</td>
							<td class="w300" id="partyaUnitTdHtml"></td>
							<td class="w100"><span class='required'>*</span>中标单位</td>
							<td class="w300">
								<input type="text" id="partybUnitValue" name="partybUnitValue" workFlowForm='textinput' itemName="partybUnit" readonly="readonly" />
								<input type="hidden" id="partybUnit" name="partybUnit" />
							</td>
						</tr>
						<tr>
							<td class="w100">付款方式</td>
							<td class="w300">
								<span workFlowForm='select' itemName="paymentMethod">
									<dic:select name="paymentMethod" id="paymentMethod" dictName="payment_method" parentCode="csmp" defaultValue="" headType="2" headValue="" />
								</span>
							</td>
							<td class="w100">结算是否要审计</td>
							<td class="w300">
								<span workFlowForm='select' itemName="needAudit">
									<dic:select name="needAudit" id="needAudit" dictName="yes_or_no" parentCode="csmp" defaultValue="" headType="2" headValue="" />
								</span>
							</td>
						</tr>
						<tr>
							<td class="w100">合同内容</td>
							<td colspan="3">
								<textarea id="contractContent" name="contractContent" style="height: 60px" workFlowForm='textinput' itemName="contractContent"></textarea>
							</td>
						</tr>
						<tr>
							<td class="w100">合同开始时间</td>
							<td class="w300">
								<input class="datepicker-input" type="text" id="startDate" name="startDate"
									   data-pick-time="false" data-date-format="yyyy-MM-dd" workFlowForm='textinput' itemName="startDate" />
							</td>
							<td class="w100">合同结束时间</td>
							<td class="w300">
								<input class="datepicker-input" type="text" id="endDate" name="endDate"
									   data-pick-time="false" data-date-format="yyyy-MM-dd" workFlowForm='textinput' itemName="endDate" />
							</td>
						</tr>
						<tr>
							<td class="w100">合同签订时间</td>
							<td class="w300">
								<input class="datepicker-input" type="text" id="signDate" name="signDate"
									   data-pick-time="false" data-date-format="yyyy-MM-dd" workFlowForm='textinput' itemName="signDate" />
							</td>
							<td class="w100"><span class='required'>*</span>经办人</td>
							<td class="w300"><input type="text" id="handleUser" name="handleUser" workFlowForm='textinput' itemName="handleUser" /></td>
						</tr>
						<tr>
							<td class="w100">备注</td>
							<td colspan="3"><textarea id="remark" name="remark" style="height: 60px" workFlowForm='textinput' itemName="remark"></textarea></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div><h3 class="tc">附件信息</h3><table class="table form-table table-td-striped" id="attachTable"></table></div>

			<div itemName="optionContainer">
				<h3>办理意见</h3>
				<table class="table form-table table-td-striped">
					<tr>
						<td class="w100"><span class='required'>*</span>办理意见</td>
						<td colspan="3">
							<span class="workflowText">
								<workflow:suggest itemId="opinionCtrl" showLast="false" order="createTime" showWritePannel="false" classStr="" maxLength="500"/>
							</span>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center;">
			<section id="workflowFormButton" class=" form-btn statistics-box"></section>
		</div>
	</section>
</section>
<div id="chooseCompanyModule"></div>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src="${sysPath}/js/com/jc/csmp/contract/info/contractInfo_workflow_form.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

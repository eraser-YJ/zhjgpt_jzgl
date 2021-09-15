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
			<span>合同管理 > </span><span>合同支付申请</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap m-20-auto" id="entityForm">
			<div>
				<h3 class="tc">合同支付申请</h3>
				<input type="hidden" id="businessJson"	value='${businessJson}'/>
				<input type="hidden" id="token" name="token" value="${token}"/>
				<input type="hidden" id="id" name="id" value="0"/>
				<input type="hidden" id="piId" name="piId" value='${piId}'/>
				<input type="hidden" id="modifyDate" name="modifyDate"/>
				<table class="table form-table table-td-striped">
					<tbody>
					<tr>
						<td class="w100"><span class='required'>*</span>所属合同</td>
						<td colspan="3">
							<input type="text" id="contractName" name="contractName" workFlowForm='textinput' itemName="contractId" readonly="readonly" />
							<input type="hidden" id="contractId" name="contractId" />
						</td>
					</tr>
					<tr id="payContainer" style="display: none;">
						<td class="w100">合同金额</td>
						<td><input type="text" id="contractMoney" name="contractMoney" readonly="readonly" /></td>
						<td class="w100">已支付总额</td>
						<td><input type="text" id="totalPayment" name="totalPayment" readonly="readonly" /></td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>单据编号</td>
						<td colspan="3"><input type="text" id="payNo" name="payNo" workFlowForm='textinput' itemName="payNo" value="${applyCode}" readonly="readonly" /></td>
					</tr>
					<tr>
						<td class="w100"><span class='required'>*</span>申请金额</td>
						<td colspan="3"><input type="text" id="applyMoney" name="applyMoney" workFlowForm='textinput' itemName="applyMoney" /></td>
					</tr>
					<tr itemName="replyMoneyContainer">
						<td class="w100">批复金额</td>
						<td colspan="3"><input type="text" id="replyMoney" name="replyMoney" workFlowForm='textinput' itemName="replyMoney" /></td>
					</tr>
					<tr>
						<td class="w100">备注</td>
						<td colspan="3"><textarea id="remark" name="remark" style="height: 60px" workFlowForm='textinput' itemName="remark"></textarea></td>
					</tr>
					</tbody>
				</table>
			</div>
			<div><h3>附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>

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
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-contract.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src="${sysPath}/js/com/jc/csmp/contract/pay/contractPay_workflow_form.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

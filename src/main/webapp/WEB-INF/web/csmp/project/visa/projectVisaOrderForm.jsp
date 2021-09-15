<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<div style="margin-top: -20px;">
						<h3 class="tc">基本信息</h3>
						<input type="hidden" id="id" name="id" />
						<input type="hidden" id="token" name="token" value="${data.token}">
						<input type="hidden" id="modifyDate" name="modifyDate" />
						<table class="table table-td-striped">
							<tbody>
							<tr>
								<td>签证编号</td>
								<td><input type="text" id="code" name="code" readonly="readonly" /></td>
								<td>发生时间</td>
								<td><input type="text" id="visaDate" name="visaDate" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>所属项目</td>
								<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
								<td>所属合同</td>
								<td><input type="text" id="contractName" name="contractName" readonly="readonly"/></td>
							</tr>
							<tr>
								<td>变更类型</td>
								<td colspan="3"><input type="text" id="modifyTypeValue" name="modifyTypeValue" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>签证原因</td>
								<td colspan="3"><input type="text" id="visaReason" name="visaReason" readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="w100">发生部位或范围</td>
								<td colspan="3"><textarea id="visaScope" name="visaScope" readonly="readonly" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td class="w100">变更签证做法及原因</td>
								<td colspan="3"><textarea id="visaChange" name="visaChange" readonly="readonly" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td class="w100">工程量计算费用</td>
								<td colspan="3"><textarea id="projectAmount" name="projectAmount" readonly="readonly" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td class="w100">签证费用</td>
								<td><input type="text" id="visaAmount" name="visaAmount" readonly="readonly" /></td>
								<td class="w100">核准费用</td>
								<td><input type="text" id="checkedAmount" name="checkedAmount" readonly="readonly" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div><h3 class="tc">附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
					<div><h3 class="tc">办理意见</h3><table class="table table-striped tab_height" id="opinionTable"></table></div>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="printBtn">打 印</button>
				<shiro:hasPermission name='projectVisaFileBtn'><button class="btn dark" id="fileBtn" type="button" onclick="lodopFunction.createFile({type: 'visa', busId: $('#entityForm #id').val()})">归 档</button></shiro:hasPermission>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/project/visa/projectVisaOrderForm.js'></script>
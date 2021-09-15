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
								<td style="width: 10%">单号</td>
								<td style="width: 40%"><input type="text" id="code" name="code" readonly="readonly" /></td>
								<td style="width: 10%">申请单位</td>
								<td style="width: 40%"><input type="text" id="deptName" name="deptName" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>所属项目</td>
								<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
								<td>所属合同</td>
								<td><input type="text" id="contractName" name="contractName" readonly="readonly"/></td>
							</tr>
							<tr>
								<td>变更费用(万元)</td>
								<td><input type="text" id="changeAmount" name="changeAmount" readonly="readonly" /></td>
								<td>变更日期</td>
								<td><input type="text" id="changeDate" name="changeDate" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>变更类型</td>
								<td><input type="text" id="modifyTypeValue" name="modifyTypeValue" readonly="readonly" /></td>
								<td>经办人</td>
								<td><input type="text" id="handleUser" name="handleUser" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>变更原因</td>
								<td colspan="3"><textarea id="changeReason" name="changeReason" readonly="readonly" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td>变更内容</td>
								<td colspan="3"><textarea id="changeContent" name="changeContent" readonly="readonly" style="height: 80px;"></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div><h3 class="tc">附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
					<div><h3 class="tc">办理意见</h3><table class="table table-striped tab_height" id="opinionTable"></table></div>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="printBtn" style="display: none;">打 印</button>
				<shiro:hasPermission name='contractChangeFileBtn'><button class="btn dark" type="button" id="fileBtn" onclick="lodopFunction.createFile({type: 'change', busId: $('#entityForm #id').val()})">归 档</button></shiro:hasPermission>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/project/change/projectChangeOrderForm.js'></script>
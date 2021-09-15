<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<div style="margin-top: -20px;">
						<h3>合同详细信息</h3>
						<input type="hidden" id="id" name="id "/>
						<input type="hidden" id="contractId" name="contractId" value="${data.contractId}" />
						<input type="hidden" id="token" name="token" value="${data.token}">
						<input type="hidden" id="modifyDate" name="modifyDate" />
						<table class="table table-td-striped">
							<tbody>
							<tr>
								<td class="w100">单据编号</td>
								<td><input type="text" id="payNo" name="payNo" /></td>
							</tr>
							<tr>
								<td class="w100">申请金额</td>
								<td><input type="text" id="applyMoney" name="applyMoney" /></td>
							</tr>
							<tr>
								<td class="w100">批复金额</td>
								<td><input type="text" id="replyMoney" name="replyMoney" /></td>
							</tr>
							<tr>
								<td class="w100">累计支付金额</td>
								<td><input type="text" id="payMoney" name="payMoney" /></td>
							</tr>
							<tr>
								<td class="w100">经办人</td>
								<td><input type="text" id="handleUserValue" name="handleUserValue" /></td>
							</tr>
							<tr>
								<td class="w100">备注</td>
								<td><textarea id="remark" name="remark" style="height: 60px" ></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>

					<h3>附件信息</h3>
					<table class="table table-td-striped" id="attachTable"></table>

					<h3>办理意见</h3>
					<div><table class="table table-striped tab_height" id="opinionTable"></table></div>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js?n=1" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/contract/pay/cmContractPayForm.js?n=1'></script>
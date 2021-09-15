<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w1100">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="tempId" name="tempId" value="${data.tempId}" />
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="createDate" name="createDate" />
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td class="w100"><span class='required'>*</span>监测项</td>
							<td><input type="text" id="pointName" name="pointName" /></td>
							<td class="w100"><span class='required'>*</span>脚本方法名</td>
							<td><input type="text" id="functionName" name="functionName" /></td>
						</tr>
						<tr>
							<td class="w100">描述</td>
							<td><input type="text" id="pointDesc" name="pointDesc" /></td>
							<td class="w100">监测结果错误描述</td>
							<td><input type="text" id="errorText" name="errorText" /></td>
						</tr>
						<tr>
							<td class="w100">js脚本</td>
							<td colspan="5"><textarea id="jsContent" name="jsContent" style="height: 80px;"></textarea></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-body">
				<table class="table table-striped tab_height" id="columnGridTable"></table>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn dark" type="button" id="checkBtn">验证脚本</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/supervise/point/supervisionPointForm.js'></script>
<script src='${sysPath}/js/com/jc/supervise/point/supervisionPointColumnList.js'></script>
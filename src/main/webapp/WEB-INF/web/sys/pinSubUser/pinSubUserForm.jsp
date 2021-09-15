<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">用户拼音表</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="pinUserForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="userId" name = "userId">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100">
			用户名称
		</td>
		<td class="w300">
			<input type="text" id="userName" name = "userName" value="" readonly>
		</td>
		<td class="w100">
			拼音全拼
		</td>
		<td class="w300">
			<input type="text" id="userFull" name = "userFull" value="">
		</td>
	</tr>
	<tr >
		<td class="w100">
			拼音首字母
		</td>
		<td class="w300">
			<input type="text" id="userInitials" name = "userInitials" value="">
		</td>
		<td class="w100">
			拼音首字母缩写
		</td>
		<td class="w300">
			<input type="text" id="userAbbreviate" name = "userAbbreviate" value="">
		</td>
	</tr>
	</tbody>
</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/pinSubUser/pinSubUserForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/pinSubUser/pinSubUser.validate.js" type="text/javascript"></script>

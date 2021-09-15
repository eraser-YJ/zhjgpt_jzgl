<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleSubGroupId">维护</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="subRoleGroupForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w100">
									<span class='required'>*</span>角色组名称
								</td>
								<td class="w300">
									<input type="text" id="groupName" name="groupName" maxlength="10" value="" placeholder="必填">

								</td>
							</tr>
							<tr>
								<td class="w100">角色组描述</td>
								<td class="w300">
									<textarea id="groupDescription" name="groupDescription" rows="4"></textarea>
									<div>您还可以输入<span id="count"></span>个文字。 </div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveAndClose">保存</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/subRoleGroup/module/subRoleGroupForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subRoleGroup/subRoleGroup.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit1" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">用户角色维护</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="subUserRoleForm">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="userId" name="userId" value="${subUser.id}">
					<input type="hidden" id="selDeptId" name="selDeptId" value="${subUser.deptId}">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w100">姓名</td>
								<td class="w300">${subUser.displayName}</td>
								<td class="w100">用户职务</td>
								<td class="w300">${subUser.dutyId}</td>
							</tr>
							<tr>
								<td class="w100">直接领导</td>
								<td class="w300">${subUser.leaderIdValue}</td>
								<td class="w100">序号</td>
								<td class="w300">${subUser.orderNo}</td>
							</tr>
							<tr>
								<td class="w100">角色</td>
								<td class="w300" colspan="3">
									<div id="roleIdFormDiv"></div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="save">保 存</button>
				<button class="btn" type="button" id="close">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/subUserRole/module/subUserRoleForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subUserRole/subUserRole.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit1" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">角色菜单维护</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="subRoleMenuForm">
					<input type="hidden" id="parentMenuId" name="parentMenuId" value="${parentMenuId}">
					<input type="hidden" id="roleId" name="roleId" value="${subRole.id}">
					<input type="hidden" id="menuNames" name="menuNames" value="">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td class="w100">角色</td>
								<td class="w300">${subRole.roleName}</td>
							</tr>
							<tr>
								<td class="w100">菜单</td>
								<td class="w300">
									<div id="menuIdFormDiv"></div>
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

<script src="${sysPath}/js/com/sys/subRoleMenu/module/subRoleMenuForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subRoleMenu/subRoleMenu.validate.js" type="text/javascript"></script>

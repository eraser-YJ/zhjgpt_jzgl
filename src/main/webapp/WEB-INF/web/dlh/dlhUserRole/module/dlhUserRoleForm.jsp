<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">用户权限</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhUserRoleForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="userId" name = "userId" value="">
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td   style="width:40px;">
								数据权限
							</td>
							<td class="w300">
					             <div id="controlTree1"></div>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn"> 
				<button class="btn dark" type="button" id="saveAndClose">保 存</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/dlh/dlhUserRole/module/dlhUserRoleForm.js?q=1" type="text/javascript"></script>

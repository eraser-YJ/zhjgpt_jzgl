<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="update-dept" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">编辑</h4>
			</div>
			<div class="modal-body">
				<form  id="subDepartmentUpdateForm" name="subDepartmentUpdateForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="deptToken" name="deptToken" value="${token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="deptType" name="deptType" >
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td style="width:30%;">
								<span class='required'>*</span>部门名称
							</td>
							<td>
								<input type="text" readonly id="name" name = "name" value="" class="w-p100">
							</td>
						</tr>
						<tr >
							<td >
								上级名称
							</td>
							<td id="thisNodeName">
							</td>
							<input type="hidden" id="parentId" name = "parentId" value="">
						</tr>
						<tr >
							<td >
								负责人
							</td>
							<td >
								<div id="leaderIdEditDiv"></div>
							</td>
						</tr>
						<tr >
							<td>
								<span class='required'>*</span>部门排序
							</td>
							<td>
								<input type="text" maxlength="3" id="queue" name = "queue" value="" class="w-p100">
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="updateDept">保 存</button>
				<button class="btn" type="button" onclick="$('#update-dept').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/subDepartment/subDepartmentEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subDepartment/subDepartment.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">部门拼音表</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="pinDepartmentForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="deptId" name = "deptId" value="">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100">
			部门名称
		</td>
		<td class="w300">
			<input type="text" id="deptName" name = "deptName" value="" readonly>
		</td>
		<td class="w100">
			拼音全拼
		</td>
		<td class="w300">
			<input type="text" id="deptFull" name = "deptFull" value="">
		</td>
	</tr>
	<tr >
		<td class="w100">
			拼音首字母
		</td>
		<td class="w300">
			<input type="text" id="deptInitials" name = "deptInitials" value="">
		</td>
		<td class="w100">
			拼音首字母缩写
		</td>
		<td class="w300">
			<input type="text" id="deptAbbreviate" name = "deptAbbreviate" value="">
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

<script src="${sysPath}/js/com/sys/pinSubDepartment/pinSubDepartmentForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/pinSubDepartment/pinSubDepartment.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="update-dept" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content" style="width: 1100px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">编辑</h4>
			</div>
			<div class="modal-body">
				<form id="departmentUpdateForm" name="departmentUpdateForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="deptToken" name="deptToken" value="${token}">
					<input id="parentId" name="parentId" type="hidden" />
					<input id="parentWeight" name="parentWeight" type="hidden" />
					<div class="table-wrap form-table">
						<table class="table table-td-striped">
							<tbody>
								<tr>
									<td style="width:10%;"></td>
									<td colspan="3" style="width: 90%"><span class="required">编码由“上级组织编码” + “输入的编码”组成，输入时请注意</span></td>
								</tr>
								<tr>
									<td style="width:10%;"><span class="required">*</span>机构编码</td>
									<td style="width:40%;"><input type="text" id="code" name="code" readonly="readonly" /></td>
									<td style="width:10%;"><span class="required">*</span>名称</td>
									<td style="width:40%;"><input type="text" id="name" name="name" class="w-p100" /></td>
								</tr>
								<tr>
									<td>上级名称</td>
									<td id="thisNodeName"></td>
									<td>负责人</td>
									<td><div id="userUpdateDivId"></div></td>
								</tr>
								<tr>
									<td><span class="required">*</span>权重系数</td>
									<td><input type="text" id="weight" name="weight" class="w-p100"/></td>
									<td><span class="required">*</span>排序值</td>
									<td><input type="text" id="queue" name="queue" class="w-p100"/></td>
								</tr>
								<tr>
									<td>描述</td>
									<td colspan="3"><textarea id="deptDesc" name="deptDesc" rows="2"></textarea></td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="updateDept">保 存</button>
				<button class="btn" type="button" onclick="$('#update-dept').modal('hide');">关 闭</button>
				<input type="reset" style="display:none;"/>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/department/departmentUserEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/department/departmentUser.validate.js?n=2" type="text/javascript"></script>

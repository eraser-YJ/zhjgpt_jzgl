<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w530">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="parentId" name="parentId" />
					<input type="hidden" id="dataType" name="dataType" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width: 15%"><span class='required'>*</span>类别代码</td>
								<td><input type="text" id="code" name="code" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>类别名称</td>
								<td><input type="text" id="name" name="name" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>排序</td>
								<td><input type="text" id="queue" name="queue" /></td>
							</tr>
							<tr>
								<td>说明</td>
								<td colspan="3"><textarea id="scope" name="scope" style="height: 80px;"></textarea></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/dic/cmCustomDicForm.js'></script>
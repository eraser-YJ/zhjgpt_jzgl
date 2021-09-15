<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td><span class='required'>*</span>单位名称</td>
							<td>
								<input type="text" id="unitName" name="unitName"/>
							</td>
							<td><span class='required'>*</span>项目负责人</td>
							<td>
								<input type="text" id="projectLeader" name="projectLeader"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>联系电话</td>
							<td>
								<input type="text" id="phone" name="phone"/>
							</td>
							<td><span class='required'>*</span>单位类别(字典)</td>
							<td>
								<input type="text" id="partakeType" name="partakeType"/>
							</td>
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
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetyUnitForm.js'></script>
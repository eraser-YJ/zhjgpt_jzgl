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
							<td><span class='required'>*</span>附件名称</td>
							<td>
								<input type="text" id="itemAttach" name="itemAttach"/>
							</td>
							<td><span class='required'>*</span>项目分类</td>
							<td>
								<select  name="itemId" id="itemId">
									<option value ="">请选择</option>
									<c:forEach items="${itemClassifyList}" var="itemClassify" varStatus="status">
										<option value ="${itemClassify.id}">${itemClassify.itemClassify}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>是否必填</td>
							<td>
								<label><input name="isRequired" type="radio" value="0" />是</label>
								<label><input name="isRequired" type="radio" value="1" />否</label>
							</td>
							<td><span class='required'>*</span>是否多个</td>
							<td>
								<label><input name="isCheckbox" type="radio" value="0" />是</label>
								<label><input name="isCheckbox" type="radio" value="1" />否</label>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>排序</td>
							<td>
								<input type="text" id="extNum1" name="extNum1"/>
							</td>
							<td></td>
							<td>

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
<script src='${sysPath}/js/com/jc/csmp/item/itemClassifyAttachForm.js'></script>
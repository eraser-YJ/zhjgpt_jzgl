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
							<td><span class='required'>*</span>文件名称</td>
							<td>
								<input type="text" id="filename" name="filename"/>
							</td>
							<td><span class='required'>*</span>数据对象路径</td>
							<td>
								<input type="text" id="objurl" name="objurl"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>列名</td>
							<td>
								<input type="text" id="yewucolname" name="yewucolname"/>
							</td>
							<td><span class='required'>*</span>备注</td>
							<td>
								<input type="text" id="remark" name="remark"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>表名</td>
							<td>
								<input type="text" id="tableName" name="tableName"/>
							</td>
							<td><span class='required'>*</span>主键值</td>
							<td>
								<input type="text" id="userId" name="userId"/>
							</td>
						</tr>
						<tr>
							<td>附件信息</td>
							<td colspan="3">
								<div class="uploadButt">
									<a class="btn dark" type="button" data-target="#file-edit1"  id="file-edit_a_1"  role="button" data-toggle="modal">上传</a>
								</div>
								<div class="fjList">
									<ul class="fjListTop nobt">
										<li>
											<span class="enclo">已上传附件</span>
											<span class="enclooper">操作</span>
										</li>
									</ul>
									<ul class="fjListTop" id="attachList1"></ul>
								</div>
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
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload-load/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/dlh/filemanage/dlhFileForm.js'></script>
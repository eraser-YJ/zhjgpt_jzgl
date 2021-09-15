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
							<td>单号</td>
							<td><input type="text" id="code" name="code" readonly="readonly" /></td>
							<td>整改单位</td>
							<td><input type="text" id="rectificationCompanyValue" name="rectificationCompanyValue" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>所属项目</td>
							<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
							<td>所属合同</td>
							<td><input type="text" id="contractName" name="contractName" readonly="readonly"/></td>
						</tr>
						<tr>
							<td>标题</td>
							<td colspan="3"><input type="text" id="title" name="title" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>问题描述</td>
							<td colspan="3"><textarea id="questionMeta" name="questionMeta" style="height: 80px;" readonly="readonly"></textarea></td>
						</tr>
						<tr>
							<td class="w100">整改要求</td>
							<td colspan="3"><textarea id="rectificationAsk" name="rectificationAsk" readonly="readonly" style="height: 80px;"></textarea></td>
						</tr>
						<tr>
							<td class="w100">整改结果</td>
							<td colspan="3"><textarea id="rectificationResult" name="rectificationResult" readonly="readonly" style="height: 80px;"></textarea></td>
						</tr>
						<tr>
							<td class="w100">备注</td>
							<td colspan="3"><textarea id="remark" name="remark" readonly="readonly" style="height: 80px;"></textarea></td>
						</tr>
						<tr style="display: none;">
							<td>监理核验结果</td>
							<td colspan="3"><textarea id="checkResult" name="checkResult" style="height: 80px;" readonly="readonly"></textarea></td>
						</tr>
						<tr>
							<td class="w100">附件信息</td>
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
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/project/question/projectQuestionForm.js'></script>
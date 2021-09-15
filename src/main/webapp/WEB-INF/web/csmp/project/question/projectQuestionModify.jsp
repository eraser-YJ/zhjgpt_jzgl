<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<input type="hidden" id="paramBusinessTable" name="paramBusinessTable" value="${data.businessTable}">
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<div style="margin-top: -20px;">
						<h3 class="tc">基本信息</h3>
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
								<td><input type="text" id="title" name="title" /></td>
								<td>安全事故</td>
								<td>
									<label class="radio inline" for="safeFailureType1"><input type="radio" name="safeFailureType" id="safeFailureType1" value="1" label="问题" />问题</label>
									<label class="radio inline" for="safeFailureType2"><input type="radio" name="safeFailureType" id="safeFailureType2" value="2" label="隐患" />隐患</label>
								</td>
							</tr>
							<tr>
								<td class="w100">整改要求</td>
								<td colspan="3"><textarea id="rectificationAsk" name="rectificationAsk" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td class="w100">整改结果</td>
								<td colspan="3"><textarea id="rectificationResult" name="rectificationResult" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td>处置负责人</td>
								<td colspan="3"><select id="problemDept" name="problemDept"><option value="">-请选择-</option></select></td>
							</tr>
							<tr>
								<td>问题处置</td>
								<td colspan="3"><textarea id="problemHandling" name="problemHandling" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td class="w100">备注</td>
								<td colspan="3"><textarea id="remark" name="remark" style="height: 80px;"></textarea></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div><h3 class="tc">附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
					<div><h3 class="tc">办理意见</h3><table class="table table-striped tab_height" id="opinionTable"></table></div>
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
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/project/question/projectQuestionModify.js'></script>
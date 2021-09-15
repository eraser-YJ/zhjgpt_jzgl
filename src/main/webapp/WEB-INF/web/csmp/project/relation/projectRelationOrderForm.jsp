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
				<form class="table-wrap form-table" id="entityForm" style="margin-top: -20px;">
					<div>
						<h3>基本信息</h3>
						<input type="hidden" id="id" name="id" />
						<input type="hidden" id="token" name="token" value="${data.token}">
						<input type="hidden" id="modifyDate" name="modifyDate" />
						<table class="table table-td-striped">
							<tbody>
							<tr>
								<td><span class='required'>*</span>工程联系单编号</td>
								<td colspan="3"><input type="text" id="code" name="code" readonly="readonly" value="${data.code}" /></td>
							</tr>
							<tr>
								<td style="width: 10%"><span class='required'>*</span>所属项目</td>
								<td style="width: 40%">
									<input type="text" id="projectName" name="projectName" readonly="readonly"/>
									<input type="hidden" id="projectId" name="projectId" />
								</td>
								<td style="width: 10%"><span class='required'>*</span>接收单位</td>
								<td style="width: 40%"><div id="signerDeptFormDiv"></div></td>
							</tr>
							<tr id="signContainer" style="display: none;">
								<td>签收时间</td>
								<td><input class="datepicker-input" type="text" id="signDate" name="signDate" data-pick-time="false" data-date-format="yyyy-MM-dd"></td>
								<td>签收状态</td>
								<td><input type="text" id="signStatusValue" name="signStatusValue" readonly="readonly" /></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>工程联系单名称</td>
								<td colspan="3"><input type="text" id="title" name="title"/></td>
							</tr>
							<tr>
								<td><span class='required'>*</span>内容</td>
								<td colspan="3"><textarea  id="content" name="content" style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td>抄送组织</td>
								<td colspan="3"><div id="receiverDeptFormDiv"></div></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div><h3>附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn dark" type="button" id="printBtn" style="display: none;">打 印</button>
				<shiro:hasPermission name='projectRelationFileBtn'><button class="btn dark" type="button" onclick="lodopFunction.createFile({type: 'relation', busId: $('#entityForm #id').val()})">归 档</button></shiro:hasPermission>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/relation/projectRelationOrderForm.js?n=2'></script>
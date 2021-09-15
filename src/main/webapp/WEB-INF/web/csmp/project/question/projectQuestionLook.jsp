<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>问题协同管理 > </span>${data.title}
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap form-table" id="entityForm">
			<input type="hidden" id="id" name="id" value="${data.id}" />
			<input type="hidden" id="questionType" name="questionType" value="${data.questionType}" />
			<div>
				<h3 class="tc">基本信息</h3>
				<table class="table table-td-striped"><tbody>
				<tr>
					<td class="w100">编号</td>
					<td><input type="text" id="code" name="code" readonly="readonly" value="${applyCode}"  /></td>
					<td class="w100">所属项目</td>
					<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>所属合同</td>
					<td><input type="text" id="contractName" name="contractName" readonly="readonly" /></td>
					<td>整改单位</td>
					<td><input type="text" id="rectificationCompanyValue" name="rectificationCompanyValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>标题</td>
					<td><input type="text" id="title" name="title" readonly="readonly" /></td>
					<td id="safeFailureTypeTitleTr">安全事故</td>
					<td id="safeFailureTypeContentTr"><input type="text" id="safeFailureTypeValue" name="safeFailureTypeValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>问题描述</td>
					<td colspan="3"><textarea id="questionMeta" name="questionMeta" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				<tr>
					<td>整改要求</td>
					<td colspan="3"><textarea id="rectificationAsk" name="rectificationAsk" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				<tr>
					<td>整改结果</td>
					<td colspan="3"><textarea id="rectificationResult" name="rectificationResult" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				<tr style="display: none;">
					<td>监理核验结果</td>
					<td colspan="3"><textarea id="checkResult" name="checkResult" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				<tr id="problemDeptContainer" style="display: none;">
					<td>处置负责人</td>
					<td colspan="3"><input type="text" id="problemDeptValue" name="problemDeptValue" readonly="readonly" /></td>
				</tr>
				<tr id="problemHandlingContainer" style="display: none;">
					<td>问题处置</td>
					<td colspan="3"><textarea id="problemHandling" name="problemHandling" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				<tr>
					<td>备注</td>
					<td colspan="3"><textarea id="remark" name="remark" readonly="readonly" style="height: 80px;"></textarea></td>
				</tr>
				</tbody></table>
			</div>
			<div><h3 class="tc">附件信息</h3><table class="table table-td-striped" id="attachTable"></table></div>
			<div><h3 class="tc">办理意见</h3><table class="table table-striped tab_height" id="opinionTable"></table></div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center; height: 100px;">
			<shiro:hasPermission name='sceneQuestionFileBtn'><button class="btn dark" style="display: none;" type="button" id="fileBtn" onclick="lodopFunction.createFile({type: 'sceneQuestion', busId: $('#entityForm #id').val()})">归 档</button></shiro:hasPermission>
			<button class="btn" id="closeBtn" type="button">返 回</button>
		</div>
	</section>
</section>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
	var projectQuestionLookModule = {};

	projectQuestionLookModule.attach = new AttachControl.AttachListControl({
		renderElement: 'attachTable',
		businessTable: '${data.businessTable}'
	});

	$(document).ready(function() {
		console.log($('#entityForm #id').val());
		$('#closeBtn') .click(function () { parent.JCF.LoadPage({url: '/project/question/search.action?questionType=' + $('#entityForm #questionType').val() }); });
		$.ajax({
			type: "GET", data: {id: $('#entityForm #id').val()}, dataType: "json",
			url: getRootPath() + "/project/question/get.action",
			success : function(data) {
				if (data) {
					$("#entityForm").fill(data);
					if (data.questionType == 'scene') {
						$('#problemHandlingContainer').show();
						$('#problemDeptContainer').show();
						if (data.auditStatus == 'finish') {
							$('#fileBtn').show();
						}
					} else {
						$('#safeFailureTypeTitleTr').html('');
						$('#safeFailureTypeContentTr').html('');
					}
					projectQuestionLookModule.attach.initAttachOnView($('#entityForm #id').val());
					projectQuestionLookModule.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
				}
			}
		});
	});
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
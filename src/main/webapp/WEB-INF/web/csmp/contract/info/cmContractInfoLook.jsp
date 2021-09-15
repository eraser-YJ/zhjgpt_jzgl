<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>合同管理 > </span><span>合同详细信息</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap form-table" id="entityForm">
			<input type="hidden" id="id" name="id" value="${data.id}" />
			<h3>合同详细信息</h3>
			<table class="table table-td-striped"><tbody>
				<tr>
					<td class="w100">所属项目</td>
					<td class="w300">
						<input type="text" id="projectName" name="projectName" readonly="readonly" />
					</td>
					<td class="w100">合同编号</td>
					<td class="w300"><input type="text" id="contractCode" name="contractCode" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">合同名称</td>
					<td class="w300"><input type="text" id="contractName" name="contractName"  readonly="readonly" /></td>
					<td class="w100">合同类型</td>
					<td class="w300"><input type="text" id="contractTypeValue" name="contractTypeValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">合同金额(万元)</td>
					<td class="w300"><input type="text" id="contractMoney" name="contractMoney" readonly="readonly" /></td>
					<td class="w100">工期(天)</td>
					<td class="w300"><input type="text" id="constructionPeriod" name="constructionPeriod" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">发包单位</td>
					<td class="w300"><input type="text" id="partyaUnitValue" name="partyaUnitValue" readonly="readonly" /></td>
					<td class="w100">中标单位</td>
					<td class="w300"><input type="text" id="partybUnitValue" name="partybUnitValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">付款方式</td>
					<td class="w300"><input type="text" id="paymentMethodValue" name="paymentMethodValue" readonly="readonly" /></td>
					<td class="w100">结算是否要审计</td>
					<td class="w300"><input type="text" id="needAuditValue" name="needAuditValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">合同内容</td>
					<td colspan="3">
						<textarea id="contractContent" name="contractContent" style="height: 60px" readonly="readonly"></textarea>
					</td>
				</tr>
				<tr>
					<td class="w100">合同开始时间</td>
					<td class="w300"><input type="text" id="startDate" name="startDate" readonly="readonly" /></td>
					<td class="w100">合同结束时间</td>
					<td class="w300"><input type="text" id="endDate" name="endDate" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">合同签订时间</td>
					<td class="w300"><input type="text" id="signDate" name="signDate" readonly="readonly" /></td>
					<td class="w100">经办人</td>
					<td class="w300"><input type="text" id="handleUser" name="handleUser" readonly="readonly" /></td>
				</tr>
				<tr>
					<td class="w100">备注</td>
					<td colspan="3"><textarea id="remark" name="remark" style="height: 60px" readonly="readonly"></textarea></td>
				</tr>
			</tbody></table>

			<h3>附件信息</h3>
			<table class="table table-td-striped" id="attachTable"></table>

			<h3>办理意见</h3>
			<div><table class="table table-striped tab_height" id="opinionTable"></table></div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center; height: 100px;">
			<button class="btn" id="closeBtn" type="button">返 回</button>
		</div>
	</section>
</section>
<script src="${sysPath}/js/com/jc/common/common-tools.js" type="text/javascript"></script>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script>
	var contractInfoLookModule = {};

	contractInfoLookModule.attach = new AttachControl.AttachListControl({
		renderElement: 'attachTable',
		businessTable: 'cm_contract_info'
	});

	$(document).ready(function() {
		$('#closeBtn') .click(function () { parent.JCF.LoadPage({url: '/contract/info/search.action' }); });
		$.ajax({
			type: "GET", data: {id: $('#entityForm #id').val()}, dataType: "json",
			url: getRootPath() + "/contract/info/get.action",
			success : function(data) {
				if (data) {
					$("#entityForm").fill(data);
					contractInfoLookModule.attach.initAttachOnView(data.id);
					contractInfoLookModule.attach.loadOpinionCtrl(data.piId, 'opinionCtrl', 'opinionTable');
				}
			}
		});
	});
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
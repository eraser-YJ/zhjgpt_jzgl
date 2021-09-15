<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>进度及周报管理 > </span><span>周报上报</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<form class="table-wrap" id="entityForm">
			<div>
				<h3 class="tc">周报上报</h3>
				<input type="hidden" id="id" name="id" value="${data.id}" />
				<input type="hidden" id="token" name="token" value="${data.token}">
				<input type="hidden" id="modifyDate" name="modifyDate" />
				<input type="hidden" id="projectId" name="projectId" value="${data.projectId}" />
				<table class="table form-table table-td-striped">
					<tbody>
					<tr>
						<td class="w140"><span class='required'>*</span>周报名称</td>
						<td><input type="text" id="reportName" name="reportName" /></td>
					</tr>
					<tr>
						<td class="w140">项目名称</td>
						<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
					</tr>
					<tr>
						<td><span class='required'>*</span>周报信息</td>
						<td><textarea id="reportRemark" name="reportRemark" style="height: 80px;"></textarea></td>
					</tr>
					<tr id="feedbackTr" style="display:none;">
						<td>反馈</td>
						<td><textarea id="feedback" name="feedback" style="height: 80px;" readonly="readonly"></textarea></td>
					</tr>
					</tbody>
				</table>
			</div>

			<div>
				<h3>进度上报</h3>
				<table class="table table-striped tab_height" id="gridTable">
					<thead>
					<tr>
						<th style="width: 60px;">序号</th>
						<th>所属阶段</th>
						<th>计划名称</th>
						<th>完成比例(%)</th>
						<th style="width: 135px;"><button class="btn dark" type="button" id="addItemBen">添 加</button></th>
					</tr>
					</thead>
				</table>
			</div>

			<div style="margin-top: 20px;">
				<h3>附件信息</h3>
				<table class="table table-td-striped" id="attachTable"></table>
			</div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center;">
			<section class="form-btn statistics-box">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn dark" type="button" id="saveReleaseBtn">保存并上报</button>&nbsp;
				<button class="btn" type="button" onclick="cmcmProjectWeeklyItemForm.back();">返 回</button>
			</section>
		</div>
	</section>
	<div id="chooseModule"></div>
</section>
<!-- form -->
<div class="modal fade panel" id="item-form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="$('#item-form-modal').modal('hide');">×</button>
				<h4 class="modal-title">进度上报</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="weeklyItemForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="planId" name="planId" />
					<input type="hidden" id="weeklyId" name="weeklyId" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td class="w100">计划编码</td>
							<td><input type="text" id="planCode" name="planCode" readonly="readonly" /></td>
							<td class="w100">计划名称</td>
							<td><input type="text" id="planName" name="planName" readonly="readonly" /></td>
						</tr>
						<tr>
							<td class="w100">计划开始时间</td>
							<td><input type="text" id="planStartDate" name="planStartDate" readonly="readonly" /></td>
							<td class="w100">计划结束时间</td>
							<td><input type="text" id="planEndDate" name="planEndDate" readonly="readonly" /></td>
						</tr>
						<tr>
							<td class="w100"><span class='required'>*</span>完成比例</td>
							<td><input type="text" id="finishRatio" name="finishRatio" /></td>
							<td class="w100">备注</td>
							<td><input type="text" id="remark" name="remark" /></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" onclick="cmProjectWeeklyItemForm.saveOrUpdate();">保 存</button>
				<button class="btn" type="button" onclick="$('#item-form-modal').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/weekly/cmProjectWeeklyForm.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

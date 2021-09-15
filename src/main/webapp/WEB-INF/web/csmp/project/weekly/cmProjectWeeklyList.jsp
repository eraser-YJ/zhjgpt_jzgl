<%@ page language="java" pageEncoding="UTF-8" import="com.jc.foundation.util.GlobalUtil" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>进度及周报管理 > </span><span>周报上报</span>
		</div>
	</header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto; width: 300px !important;">
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<section class="panel">
				<form class="table-wrap form-table" id="searchForm">
					<input type="hidden" id="searchFormQuery_projectId" name="searchFormQuery_projectId">
					<input type="hidden" id="projectName" name="projectName">
					<input type="hidden" id="currentPaperTitle" name="currentPaperTitle" value="[项目周报-第<%=GlobalUtil.getCurrentWeek() %>周](<%=GlobalUtil.getMonday("MM.dd") %>-<%=GlobalUtil.getSunday("MM.dd") %>)" >
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td style="width: 10%">上报时间</td>
							<td style="width: 40%">
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="searchFormQuery_releaseDateBegin" name="searchFormQuery_releaseDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#searchFormQuery_releaseDateEnd lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="searchFormQuery_releaseDateEnd" name="searchFormQuery_releaseDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#searchFormQuery_releaseDateBegin gt">
								</div>
							</td>
							<td style="width: 10%">周报名称</td>
							<td style="width: 40%"><input type="text" id="searchFormQuery_reportName" name="searchFormQuery_reportName"/></td>
						</tr>
						</tbody>
					</table>
					<div  class="btn-tiwc">
						<button class="btn" type="button" id="queryBtn">查 询</button>
						<button class="btn" type="button" id="resetBtn">重 置</button>
					</div>
				</form>
			</section>
			<section class="panel tab-content">
				<section class="form-btn fl m-l" style="margin-top: 10px; margin-left: 15px;">
					<shiro:hasPermission name='projectAdd'>
						<a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a>
					</shiro:hasPermission>
				</section>
				<div class="tab-pane fade active in">
					<div class="table-wrap">
						<table class="table table-striped tab_height" id="gridTable"></table>
					</div>
					<section class="clearfix">
						<section class="pagination m-r fr m-t-none"></section>
					</section>
				</div>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<div class="modal fade panel" id="title-modal" aria-hidden="false">
	<div class="modal-dialog w530">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" onclick="$('#title-modal').modal('hide');">×</button>
				<h4 class="modal-title">请输入周报名称</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="titleForm">
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td><input type="text" id="reportName" name="reportName" value="【项目周报-第<%=GlobalUtil.getCurrentWeek() %>周】(<%=GlobalUtil.getMonday("MM.dd") %>-<%=GlobalUtil.getSunday("MM.dd") %>)" /></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" onclick="cmProjectWeeklyJsList.saveReportName();">保 存</button>
				<button class="btn" type="button" onclick="$('#title-modal').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/project/weekly/cmProjectWeeklyList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

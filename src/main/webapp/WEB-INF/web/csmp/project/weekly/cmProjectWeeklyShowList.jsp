<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<input type="hidden" id="weeklyId" name="weeklyId" />
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>进度及周报管理 > </span><span>周报查阅</span>
		</div>
	</header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto; width: 400px !important;">
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<section class="panel tab-content">
				<div class="tab-pane fade active in">
					<div class="table-wrap" id="emptyDiv" style="width: 100%; font-size: 24px; text-align: center; display: none;">暂无周报信息</div>
					<div class="table-wrap" id="weeklyDiv">
						<form class="table-wrap" id="entityForm">
							<input type="hidden" id="id" name="id" />
							<div>
								<h3 class="tc" id="reportName"></h3>
								<table class="table form-table table-td-striped">
									<tbody>
									<tr>
										<td class="w140">项目名称</td>
										<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
									</tr>
									<tr>
										<td class="w140">周报信息</td>
										<td><textarea id="reportRemark" name="reportRemark" style="height: 80px;"></textarea></td>
									</tr>
									<shiro:hasPermission name='weekly_feedback'>
										<tr>
											<td>周报反馈</td>
											<td><textarea id="feedback" name="feedback" style="height: 80px;"></textarea></td>
										</tr>
									</shiro:hasPermission>
									</tbody>
								</table>
							</div>

							<div>
								<h3>进度上报</h3>
								<div class="table-wrap" style="padding: 5px 0 !important;">
									<table class="table table-striped tab_height" id="gridTable">
										<thead>
										<tr>
											<th style="width: 60px;">序号</th>
											<th>所属阶段</th>
											<th>计划名称</th>
											<th>完成比例(%)</th>
										</tr>
										</thead>
									</table>
								</div>
							</div>

							<div style="margin-top: 20px;">
								<h3>附件信息</h3>
								<table class="table table-td-striped" id="attachTable"></table>
							</div>
						</form>
						<div id="formFoorDiv" class="m-l-md" style="text-align: center; height: 100px;">
							<shiro:hasPermission name='weekly_feedback'><button class="btn dark"type="button" id="feedbackBtn" onclick="cmProjectWeeklyShowListPanel.saveFeedback();">反 馈</button></shiro:hasPermission>
						</div>
					</div>
				</div>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/project/weekly/cmProjectWeeklyShowList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>变更及签证管理 > </span><span>变更管理 > </span><span>变更单审批</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
					<tr>
						<td style="width: 10%">项目统一编号</td>
						<td style="width: 40%"><input type="text" id="projectNumber" name="projectNumber" /></td>
						<td style="width: 10%">项目名称</td>
						<td style="width: 40%"><input type="text" id="projectName" name="projectName" /></td>
					</tr>
					<tr>
						<td>合同编号</td>
						<td><input type="text" id="contractCode" name="contractCode" /></td>
						<td>合同名称</td>
						<td><input type="text" id="contractName" name="contractName" /></td>
					</tr>
					<tr>
						<td class="w100">变更单编号</td>
						<td><input type="text" id="code" name="code" /></td>
						<td>变更日期</td>
						<td>
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="changeDateBegin" name="changeDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#changeDateEnd lt">
								<div class="input-group-btn w30">-</div>
								<input class="datepicker-input" type="text" id="changeDateEnd" name="changeDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#changeDateBegin gt">
							</div>
						</td>
					</tr>
					<tr>
						<td>变更类别</td>
						<td><dic:select name="changeType" id="changeType" dictName="project_change_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td></td>
						<td></td>
						<td></td>
					</tr>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel clearfix" id="sendPassTransact-list">
		<div class="table-wrap">
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"><section class="form-btn fl m-l"></section></section>
	</section>
</section>
<script src='${sysPath}/js/com/jc/csmp/project/change/projectChangeOrder_workflow_todoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
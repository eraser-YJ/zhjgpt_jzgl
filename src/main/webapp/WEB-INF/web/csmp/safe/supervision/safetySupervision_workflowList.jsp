<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>安全监督</span>
			<span>工程报监 > </span><span>统计查询</span>
		</div>
	</header>
	<!--start 查询条件-->
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td class="w140">工程名称</td>
						<td >
							<input type="text" id="query_projectName" name="query_projectName"/>
						</td>
						<td style="width:10%;">申请时间</td>
						<td style="width:40%;">
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-ref-obj="#createDateEnd lt" data-pick-time="false" data-date-format="yyyy-MM-dd">
								<div class="input-group-btn w30"> -</div>
								<input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-ref-obj="#createDateBegin gt" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</div>
						</td>
					</tr>
					<tr>
					<td class="w140">结构</td>
						<td><dic:select name="query_structureType" id="query_structureType" dictName="structure_type" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
						<td class="w140">工程类型</td>
						<td><dic:select name="query_projectType" id="query_projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
					</tr>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn dark query-jump" type="button" id="queryBtn">查 询</button>
					<button class="btn dark query-jump" type="button" id="excelAdviceNote">导 出</button>
					<button class="btn" type="reset" id="queryReset">重 置</button>
				</div>
			</form>
		</div>
		<%--<%@include file= "/WEB-INF/web/include/searchConditionHide.jsp"%>--%>
	</section>
	<script >
		//设置每行按钮
		function oTableSetButtones (source) {
			debugger;
			var buttonStr = "";
			var opt ={};
			opt.workId = source.workId;
			opt.flowStatus = source.flowStatus;
			opt.processStatus = source.processStatus;
			opt.workflowId = source.piId;
			opt.entrance = $("#entrance").val();
			opt.entranceType = $("#entranceType").val();
			opt.action="/project/projectPlan/openProjectPlanWorkflow.action";
			buttonStr = getWorkflowListButton(opt);
			return buttonStr;
		};
	</script>
	<section class="panel clearfix" id="sendPassTransact-list">
		<div class="table-wrap">
			<table class="table table-striped tab_height" id="gridTable">
			</table>
		</div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
			</section>
		</section>
	</section>
</section>
 
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision_workflowList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>年度计划</span>
			<span>年度计划 > </span><span>年度计划审批</span>
		</div>
	</header>
	 <!--start 查询条件-->
	 <section class="panel clearfix search-box search-shrinkage">
	    <div class="search-line">
	        <form class="table-wrap form-table" id="projectPlanQueryForm">
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td style="width:10%;">年度</td>
						<td style="width:40%;">
							<input type="text" id="query_planYear" name = "query_planYear" value="">
						</td>
						<td style="width:10%;">地区</td>
						<td style="width:40%;">
							<dic:select name="query_planAreaCode" id="query_planAreaCode" dictName="plan_area" parentCode="project_year_plan" headName="-请选择-" headValue="" defaultValue="" />
						</td>
					</tr>
				</table>
				<div  class="btn-tiwc">
					<button class="btn dark query-jump" type="button" id="queryProjectPlan">查 询</button>
					<button class="btn" type="reset" id="queryReset">重 置</button>
				</div>
	        </form>
	    </div>
	 </section>
	 <!--end 查询条件-->
     <!--start 查询结果-->
	 <section class="panel clearfix" id="sendPassTransact-list">
		<!--  <h2 class="panel-heading clearfix">查询结果</h2>  -->
	    <div class="table-wrap">
	         <table class="table table-striped tab_height first-td-tc" id="projectPlanTable">
	             <thead>
					<tr>
						<th style="width:70px;">序号</th>
						<th>年度</th>
						<th>地区</th>
						<th>类型</th>
						<th>创建时间</th>
						<th class="w170">操作</th>
					</tr>
				</thead>
	            <tbody>
	            </tbody>
	         </table>
	     </div>
	     <section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
			</section>
		 </section>
     </section>
 </section>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlan_workflow_todoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
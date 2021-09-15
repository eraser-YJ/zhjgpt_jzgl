<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>年度计划</span>
			<span>年度计划 > </span><span>年度计划统计</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
					    <tr>
							<td style="width:8%;">年度</td>
							<td style="width:25%;">
								<input type="text" id="query_planYear" name="query_planYear"/>
							</td>
							<td style="width:8%;">月度</td>
							<td style="width:25%;">
								<select id="query_planMonth" name="query_planYear">
									<option value="">-全部-</option>
									<option value="1">1月</option>
									<option value="2">2月</option>
									<option value="3">3月</option>
									<option value="4">4月</option>
									<option value="5">5月</option>
									<option value="6">6月</option>
									<option value="7">7月</option>
									<option value="8">8月</option>
									<option value="9">9月</option>
									<option value="10">10月</option>
									<option value="11">11月</option>
									<option value="12">12月</option>
								</select>
							</td>
							<td  style="width:8%;">地区</td>
							<td  style="width:25%;">
								<dic:select name="query_planAreaCode" id="query_planAreaCode" dictName="plan_area" parentCode="project_year_plan" headName="-请选择-" headValue="" defaultValue="" />
							</td>
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
	<section class="panel ">
		<div class="table-wrap">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addBtn" style="margin-left: 0 !important;">生 成</a>
			</section>
			<table class="table table-striped tab_height first-td-tc" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height">
		</section>
	</section>
	<div id="formModule"></div>
	<div id="projectDiv"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/plan/month/projectMonthPlanList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>年度计划</span>
			<span>年度计划 > </span><span>年度计划设置</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width:10%;">年度</td>
							<td style="width:40%;">
								<select id="query_planYear" name="query_planYear"></select>
							</td>
							<td style="width:10%;">类型</td>
							<td style="width:40%;">
								<dicex:reqTypeTag name="query_seqno" id="query_seqno"  headName="-请选择-" headValue=""/>
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
				<a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a>
			</section>
			<table class="table table-striped tab_height first-td-tc" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/plan/ctrl/projectYearPlanCtrlList.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
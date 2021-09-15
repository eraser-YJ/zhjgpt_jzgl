<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>全生命周期</span>
			<span>数据串联监管 > </span><span>监管规则库</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td class="w140">监测项</td>
						<td><input type="text" id="pointName" name="pointName" /></td>
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
			<section class="form-btn fl m-l"><a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a></section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
	<div id="formColumnModule"></div>
	<div id="formCheckModule"></div>
</section>
<script src='${sysPath}/js/com/jc/supervise/point/supervisionPointList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>合同管理 > </span><span>合同支付查询</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage" style="display: none;">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" style="display: none;">
				<input type="hidden" id="contractId" name="contractId" value="${contractId}" />
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap">
			<section class="form-btn fl m-l"><a class="btn dark" role="button" id="backBtn" style="margin: 0 !important;">返 回</a></section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l"></section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/contract/pay/cmContractPayList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
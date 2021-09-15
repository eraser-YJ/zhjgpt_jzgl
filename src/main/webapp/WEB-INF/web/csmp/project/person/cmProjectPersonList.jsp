<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程信息管理 > </span><span>人员分配</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage" style="display: none;">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<input type="hidden" id="paramProjectId" name="paramProjectId" value="${projectId}" />
				<section class="form-btn m-b-lg">

				</section>
			</form>
		</div>
	</section>
	<section class="panel ">
		<script>
			function oTableSetButtons(source) {
				var edit = "<shiro:hasPermission name='projectPersonUpdate'><a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"cmProjectPersonJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">修改</a></shiro:hasPermission>";
				var del = "<shiro:hasPermission name='projectPersonDelete'><a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"cmProjectPersonJsList.delete('"+ source.id+ "')\">删除</a></shiro:hasPermission>";
				return edit + del;
			}
		</script>
		<div class="table-wrap">
			<section class="form-btn fl m-l">
				<shiro:hasPermission name='projectPersonAdd'><button class="btn dark" type="button" id="addBtn" style="margin: 0 !important;">新 增</button></shiro:hasPermission>
				<button class="btn" type="button" id="backBtn">返 回</button>
			</section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height">

		</section>
	</section>
</section>
<div id="formModule"></div>
<div id="chooseCompanyModule"></div>
<script src='${sysPath}/js/com/jc/csmp/project/person/cmProjectPersonList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
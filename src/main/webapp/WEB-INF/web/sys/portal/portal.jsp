<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script>
	$.ajaxSetup({
		cache : false
	//设置成false将不会从浏览器缓存读取信息
	});
</script>
<script>
	//设置每行按钮
	function oTableSetButtonsportal(source) {
		var share = "<a class=\"a-icon i-new\" href=\"#myModal-share\" onclick=\"portal.loadshareHtml('"
				+ source.id + "')\" data-toggle=\"modal\">共享范围</a>";
		var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"portal.loadUpdateHtml('"
				+ source.id
				+ "')\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
		var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"portal.deleteportal('"
				+ source.id
				+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
		return share + edit + del;
	};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
	<div class="con-heading fl" id="navigationMenu">
		<h1></h1>
		<div class="crumbs"></div>
	</div>
	<a class="btn dark fr" type="button" href="#myModal-edit"
		id="portalTop" role="button" data-toggle="modal">新 增</a>
</header>
<section class="panel clearfix search-box search-shrinkage">
<div class="search-line hide">
	<form class="table-wrap form-table" id="portalListForm">
		<table class="table table-td-striped">
			<tbody>
				<tr>
					<td class="w140">门户名称</td>
					<td style="width:40%;"><input type="text" id="portalName"
						name="portalName" /></td>
					<td class="w140">门户状态</td>
					<td><dic:select name="portalStatus" id="portalStatus"
							dictName="portal_status" parentCode="portal" defaultValue="" headName="-全部-"
							headValue="" /></td>
				</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark" type="button" id="queryportal">查 询</button>
			<button class="btn" id="queryReset" type="button">重 置</button>
		</section>
	</form>
</div>
<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%> 
</section>
<section class="panel clearfix">
	<!--<h2 class="panel-heading clearfix">查询结果</h2>-->
	<div class="table-wrap">
		<table class="table table-striped tab_height first-td-tc"
			id="portalTable">
			<thead>
				<tr>
					<th class="w46"><input type="checkbox" /></th>
					<th class="w115">门户名称</th>
					<th style="width:16%;">门户状态</th>
					<th class="w115">门户类型</th>
					<th class="w115">排序号</th>
					<th class="w170">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<section class="clearfix">
		<section class="form-btn fl m-l">
			<a class="btn dark" type="button" href="#myModal-edit"
				id="portalBottom" role="button" data-toggle="modal">新 增</a>
			<button class="btn" type="button" id="deleteportals">批量删除</button>
		</section>
		<section class="pagination m-r fr m-t-none"></section>
	</section>
</section>
</section>
<div id="portalEdit"></div> 
<div id="portalshareEdit"></div>
<input type="hidden" id="potalParentId" value="${menuId}"/>
<script>
	jQuery.getScript("${sysPath}/js/com/sys/portal/portal.js");
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
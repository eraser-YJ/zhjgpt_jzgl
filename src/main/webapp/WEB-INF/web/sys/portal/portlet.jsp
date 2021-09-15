<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script >
//设置每行按钮
function oTableSetButtonsportlet (source) {
	var share = "<a class=\"a-icon i-new\" href=\"#myModal-share\" onclick=\"portlet.loadshareHtml("
		+ source.id + ")\" data-toggle=\"modal\">共享范围</a>";
	var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"portlet.loadUpdateHtml("+ source.id+ ")\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"portlet.deleteportlet("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	return share+edit + del ;
};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
	<div class="con-heading fl" id="navigationMenu">
		<h1></h1>
		<div class="crumbs"></div>
	</div>
	<a class="btn dark fr" type="button" href="#myModal-edit" id="portletTop" role="button" data-toggle="modal">新 增</a>
</header>
<section class="panel clearfix search-box search-shrinkage">
<div class="search-line hide">
	 <form class="table-wrap form-table" id="portletListForm">
		<table class="table table-td-striped">
			<tbody>
				<tr>
					<td class="w140">门户名称</td>
					<td style="width:40%;">
							<select id="portalId" name="portalId">
								<option value="">-全部-</option>
								<c:forEach items="${portalList}" var="portalvo" varStatus="status">
									<option value="${portalvo.id }">${portalvo.portalName }</option>
								</c:forEach>
							</select>
					</td>
					<td class="w140">功能名称</td>
					<td><input type="text" id="functionName" name="functionName">
					</td>
				</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark" type="button" id="queryportlet">查 询</button><button class="btn" id="queryReset" type="reset">重 置</button>
		</section>
	</form>
</div>
<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%> 
</section>  
<section class="panel clearfix">
	<!--<h2 class="panel-heading clearfix">查询结果</h2>-->
	<div class="table-wrap">
		<table class="table table-striped tab_height first-td-tc" id="portletTable">
			<thead>
				<tr>
					<th class="w46"><input type="checkbox" /></th>
					<th class="w115">门户名称</th>
					<th class="w115">门户组件标题</th>
                    <th style="width:16%;">功能名称</th>
                    <th class="w115">组件视图类型</th>
					<th class="w170">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<section class="clearfix">
		<section class="form-btn fl m-l">
			<a class="btn dark" type="button" href="#myModal-edit" id="portletBottom" role="button" data-toggle="modal">新 增</a>
			<button class="btn" type="button" id="deleteportlets">批量删除</button>
		</section>
		<section class="pagination m-r fr m-t-none">
		</section>
	</section>
</section>
</section>
<div id="portletEdit">
</div> 
<div id="portletshareEdit">
</div>
<script>
jQuery.getScript("${sysPath}/js/com/sys/portal/portlet.js");
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
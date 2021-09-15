<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script >
$.ajaxSetup ({
	 cache: false //设置成false将不会从浏览器缓存读取信息
	});
</script>
<script >
//设置每行按钮
function oTableSetButtonspFunction (source) {
	var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"pFunction.loadUpdateHtml("+ source.id+ ")\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"pFunction.deletepFunction("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	return edit + del ;
};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
	<div class="con-heading fl" id="navigationMenu">
		<h1></h1>
		<div class="crumbs"></div>
	</div>
	<a class="btn dark fr" type="button" href="#myModal-edit" id="pFunctionTop" role="button" data-toggle="modal">新 增</a>
</header>
<section class="panel clearfix search-box search-shrinkage">
<div class="search-line hide">
	 <form class="table-wrap form-table" id="pFunctionListForm">
		<table class="table table-td-striped">
			<tbody>
				<tr>
					<td class="w140">功能名称</td>
					<td style="width:40%;"><input type="text" id="funName" name="funName"/></td>
					<td class="w140">功能编码</td>
					<td><input type="text" id="funCode" name="funCode"/></td>
				</tr>
				<tr>
					<td class="w140">功能链接URL</td>
					<td style="width:40%;"><input type="text" id="funUrl" name="funUrl"/></td>
					<td class="w140">功能视图类型</td>
					<td><dic:select name="viewType" id="viewType" dictName="pviewType" parentCode="portalFunction" headName="-全部-" headValue="" /></td>
				</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark" type="button" id="querypFunction">查 询</button><button class="btn" id="queryReset" type="reset">重 置</button>
		</section>
	</form>
</div>
<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%> 
</section>  
<section class="panel clearfix">
	<h2 class="panel-heading clearfix">查询结果</h2>
	<div class="table-wrap">
		<table class="table table-striped tab_height first-td-tc" id="pFunctionTable">
			<thead>
				<tr>
					<th class="w46"><input type="checkbox" /></th>
					<th class="w115">功能名称</th>
                    <th style="width:16%;">功能编码</th>
                    <th>功能链接URL</th>
                    <th style="width:16%;">功能视图类型</th>
					<th class="w170">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<section class="clearfix">
		<section class="form-btn fl m-l">
			<a class="btn dark" type="button" href="#myModal-edit" id="pFunctionBottom" role="button" data-toggle="modal">新 增</a>
			<button class="btn" type="button" id="deletepFunctions">批量删除</button>
		</section>
		<section class="pagination m-r fr m-t-none">
		</section>
	</section>
</section>
</section>
<div id="functionEdit">
</div> 
<script>
jQuery.getScript("${sysPath}/js/com/sys/portal/portalFunction.js");
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
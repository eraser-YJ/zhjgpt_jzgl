<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<script>
//设置每行按钮
function oTableSetButtones (source) {
	var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"phrase.loadUpdateHtml('"+ source.id+ "')\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"phrase.deletephrase('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	return edit + del ;
};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in">
		<div class="con-heading fl" >
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel clearfix">
		<h2 class="panel-heading clearfix ">常用词</h2>
		<div class="table-wrap">
			<table class="table table-striped tab_height first-td-tc" id="phraseTable">
			<thead>
				<tr>
					<th class="w46"><input type="checkbox" /></th>
					<th style="width:20%;">类别</th>
					<th style="width:20%;">创建人</th>
					<th>内容</th>
					<th class="w170">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<section class="clearfix">    
		<section class="form-btn fl m-l">
			<a class="btn dark" href="#myModal-edit" role="button" id="phraseBottom" data-toggle="modal">新 增</a><button class="btn " id="deletephrases" type="button">批量删除</button>
		</section>
		<section class="pagination m-r fr m-t-none">
		</section>
	</section>
</section>
</section>
<div id="phraseEdit"></div>
<script src="${sysPath}/js/com/sys/phrase/phrase.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/phrase/phrase.validate.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
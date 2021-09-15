<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" >
			<h1></h1>
			<div class="crumbs"></div>
		</div>
		<%--<a class="btn dark fr" href="#" id="showAddDiv_t" role="button">新 增</a>--%>
	</header>
	<section class="panel m-t-md" id="body">
	    <script >
			//设置每行按钮
			function oTableSetButtones (source) {
				var buttonStr = "";
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"tabTreeList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"tabTreeList.deleteTabTree('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit + del;
				return buttonStr;
			};
			</script>
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="tabTreeTable">
					<thead>
						<tr>
							<th class="w46"><input type="checkbox" /></th>
							<th>子系统标识</th>
							<th>系统页签树标题</th>
							<th>系统页签树链接</th>
							<th>系统页签树位置标记</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addTabTreeButton">新 增</a>
					<button class="btn " id="deleteTabTrees" type="button">批量删除</button>
				</section>
			</section>
	</section>
	<div id="tabTreeModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/tabTree/tabTreeList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
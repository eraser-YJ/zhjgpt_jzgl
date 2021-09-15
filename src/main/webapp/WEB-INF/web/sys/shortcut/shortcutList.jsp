<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1>快捷方式表</h1>
			<div class="crumbs"></div>
		</div>
		<a class="btn dark fr" href="#" id="showAddDiv_t" role="button">新 增</a>
	</header>
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="shortcutQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width: 20%">快捷方式名称</td>
							<td style="width: 30%">
						 			<input type="text" id="name" name = "name" value="">
 
							</td>
							<td style="width: 20%"></td>
							<td style="width: 30%">
						 			<!-- <input type="text" id="subsystemid" name = "subsystemid" value=""> -->
 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryShortcut">查 询</button>
		            <button class="btn" type="button" id="queryReset">重 置</button>
		        </section>
		    </form>
	    </div>
	    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body">
	    <script >
			//设置每行按钮
			function oTableSetButtones (source) {
				var buttonStr = "";
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"shortcutList.loadModuleForUpdate("+ source.id+ ")\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"shortcutList.deleteShortcut("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit + del;
				return buttonStr;
			};
			</script>
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="shortcutTable">
					<thead>
						<tr>
							<th class="w46"><input type="checkbox" /></th>
							<th>快捷方式名称</th>
							<th>子系统名称</th>
							<th>菜单名称</th>
							<th>排序</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addShortcutButton">新 增</a>
					<button class="btn " id="deleteShortcuts" type="button">批量删除</button>
				</section>
			</section>
	</section>
	<div id="shortcutModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/shortcut/shortcutList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
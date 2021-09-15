<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in">
		<div class="con-heading fl">
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
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"logoList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"logoList.deleteLogo('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit + del;
				return buttonStr;
			};
			</script>
			<h2 class="panel-heading clearfix"></h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="logoTable">
					<thead>
						<tr>
							<th>登录页样式名称</th>
							<th>登录页样式标识</th>
							<th>登录页选定标记</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addLogoButton">新 增</a>
				</section>
			</section>
	</section>
	<div id="logoModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/logo/logoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
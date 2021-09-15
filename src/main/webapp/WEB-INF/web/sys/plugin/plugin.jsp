<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
		<!-- <a class="btn dark fr" href="#" id="showAddDiv_t" role="button">新 增</a> -->
	</header>
	<section class="panel search-shrinkage clearfix">
		<div class="search-line hide">
			<form class="table-wrap form-table" id="pluginQueryForm">
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td>插件名字</td>
						<td>
							<input type="text" id="name" name = "name" value="">

						</td>
						<td>状态标识</td>
						<td>
							<input type="radio" id='state1' name='state' value='1' label='启用'/>启用
							<input type="radio" id='state2' name='state' value='0' label='停用'/>停用


						</td>
					</tr>
					</tbody>
				</table>
				<section class="form-btn m-b-lg">
					<button class="btn dark" type="button" id="queryPlugin">查 询</button>
					<button class="btn" type="button" id="queryReset">重 置</button>
				</section>
			</form>
		</div>
		<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel" id="body">
		<script >
			//设置每行按钮
			function oTableSetButtones (source) {
				var buttonStr = "";
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"pluginList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" style=\"width:78px;\" data-original-title=\"依赖\">依赖</i></a>";
				if(source.name=="Plugin initial"||source.name=="centre"||source.name=="Operlog"||source.name=="Setting"){
					edit = "";
				}

				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"pluginList.deletePlugin('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit /*+ del*/;
				return buttonStr;
			};
		</script>
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap">
			<table class="table table-striped tab_height first-td-tc" id="pluginTable">
				<thead>
				<tr>
					<th>插件名字</th>
					<th>描述</th>
					<th>版本号</th>
					<th>jar包名</th>
					<th>数据表已录入</th>
					<th>服务已创建</th>
					<th>菜单已创建</th>
					<th>状态标识</th>
					<th>创建时间</th>
					<th class="w170">操作</th>
				</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div style="height:40px;"></div>
		</div>

	</section>
	<div id="pluginModuleDiv"></div>
</section>

<script src='${sysPath}/js/com/sys/plugin/pluginList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
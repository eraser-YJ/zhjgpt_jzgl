<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1>子系统角色组表</h1>
		</div>
	</header>
	<section class="panel search-shrinkage clearfix">
		<div class="search-line hide">
			<form class="table-wrap form-table" id="subRoleGroupQueryForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td style="width: 15%;">创建时间</td>
							<td style="width: 35%;">
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-pick-time="true"
										data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#createDateEnd lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-pick-time="true"
										data-date-format="yyyy-MM-dd HH:mm:ss" data-ref-obj="#createDateBegin gt">
								</div>
							</td>
							<td style="width: 15%;">角色组名称</td>
							<td style="width: 35%;">
								<input type="text" id="groupName" name="groupName" value="">
							</td>
						</tr>
					</tbody>
				</table>
				<section class="form-btn m-b-lg">
					<button class="btn dark" type="button" id="querySubRoleGroup">查 询</button>
					<button class="btn" type="button" id="queryReset">重 置</button>
				</section>
			</form>
		</div>
		<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
	</section>
	<section class="panel m-t-md" id="body">
		<script>
			//设置每行按钮
			function oTableSetButtones(source) {
				var buttonStr = "";
				var edit = "<a class=\"a-icon i-new\" href=\"javascript:void(0)\" onclick=\"subRoleGroupList.loadModuleForUpdate('"
						+ source.id + "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var del = "<a class=\"a-icon i-remove m-r-xs\" href=\"javascript:void(0)\" onclick=\"subRoleGroupList.deleteSubRoleGroup('"
						+ source.id + "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				var config = "<a class=\"a-icon i-new\" href=\"javascript:void(0)\" onclick=\"subRoleGroupList.loadConfigMenu('"
						+ source.id + "')\">菜单</a>";
				buttonStr = edit + del + config;
				return buttonStr;
			};
		</script>
		<h2 class="panel-heading clearfix">查询结果</h2>
		<div class="table-wrap">
			<table class="table table-striped tab_height first-td-tc" id="subRoleGroupTable">
				<thead>
					<tr>
						<!-- 						<th class="w46">
							<input type="checkbox" />
						</th> -->
						<th>角色组名称</th>
						<!--<th>角色组描述</th>						 <th>状态</th> -->
						<th style="width:180px;">创建时间</th>
						<th style="width:180px;">操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<a class="btn dark" role="button" id="addSubRoleGroupButton">新 增</a>
				<!-- <button class="btn " id="deleteSubRoleGroups" type="button">批量删除</button> -->
			</section>
		</section>
	</section>
	<div id="subRoleGroupModuleDiv"></div>
	<div id="configMenuModuleDiv"></div>
</section>
</section>
<script src='${sysPath}/js/com/sys/subRoleGroup/subRoleGroupList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<script>
	//设置每行按钮
	function oTableSetButtones(source) {
		var buttonStr = "";
		var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"subUserList.loadModuleForUpdate('"
				+ source.id
				+ "','"
				+ source.deptId
				+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
		var del = "<a class=\"a-icon i-remove m-r-xs\" href=\"#\" onclick=\"subUserList.deleteSubUser('"
				+ source.id
				+ "','"
				+ source.deptId
				+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
		var role = " <a class=\"a-icon i-new\" href=\"javascript:void(0)\" onclick=\"subUserList.loadUserRoleFrom('"
				+ source.id + "','"
				+ source.deptId
				+ "')\">角色</a>";
		buttonStr = edit + del + role;
		return buttonStr;
	};
</script>
<script src='${sysPath}/js/com/sys/subUser/subUserList.js'></script>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="tree-fluid">
		<aside class="tree-wrap m-t-md" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto;">
				<div class="panel-heading clearfix" id="header_2">
					<h2>选择部门</h2>
				</div>
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<section class="panel search-shrinkage clearfix">
				<div class="search-line hide">
					<form class="table-wrap form-table" id="subUserQueryForm">
						<input type="hidden" id="deptId" name="deptId">
						<input type="hidden" id="deptIds" name="deptIds">
						<table class="table table-td-striped">
							<tbody>
								<tr>
									<td>姓名</td>
									<td>
										<input type="text" id="displayName" maxlength="10" name="displayName" />
									</td>
									<td>用户职务</td>
									<td>
										<input type="text" id="dutyId" name="dutyId" value="">
									</td>
								</tr>
							</tbody>
						</table>
						<section class="form-btn m-b-lg">
							<button class="btn dark" type="button" id="querySubUser">查 询</button>
							<button class="btn" type="button" id="queryReset">重 置</button>
						</section>
					</form>
				</div>
				<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
			</section>
			<section class="panel">

				<h2 class="panel-heading clearfix">查询结果</h2>
				<div class="table-wrap">
					<table class="table table-striped first-td-tc tab_height" id="subUserTable">
						<thead>
							<tr>
								<th>姓名</th>
								<th>用户职务</th>
								<th>所在部门</th>
								<th class="w170">操作</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<section class="clearfix" id="footer_height">
					<section class="form-btn fl m-l">
						<button class="btn dark button fr" type="button" href="#" role="button" data-toggle="modal" id="addSubUserButton">新 增</button>
					</section>
				</section>
			</section>
		</section>
	</section>
</section>
<div id="subUserModuleDiv"></div>
<div id="subUserRoleModuleDiv"></div>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
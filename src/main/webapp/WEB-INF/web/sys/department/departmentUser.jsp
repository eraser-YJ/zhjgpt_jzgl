<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1"><div class="con-heading fl" ><h1></h1><div class="crumbs"></div></div></header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto;position: inherit;">
				<div class="panel-heading clearfix" id="header_2"><h2>选择组织</h2></div>
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<section class="panel">
				<form id="deptForm">
					<input id="deptId" name="deptId" type="hidden"/>
					<div class="table-wrap form-table">
						<table class="table table-td-striped tab_height">
							<tbody><tr>
								<td class="w140">名称</td>
								<td id="deptName" class="w140"></td>
								<td class="w140">负责人</td>
								<td id="leaderName" class="w140"></td>
								<td class="w140">组织类型</td>
								<td id="deptType" class="w140"></td>
								<td style="width: 150px; text-align: center;">
									<button id="editDept" class="btn dark" type="button" role="button" data-toggle="modal" onclick='department.showDeptEditHtml($("#deptId").val())'>编 辑</button>
									<button id="delDept" class="btn" type="button" onclick='department.deleteDepartment($("#deptId").val())'>删 除</button>
								</td>
							</tr></tbody>
						</table>
					</div>
				</form>
			</section>
			<section class="tabs-wrap m-t-md">
				<ul class="nav nav-tabs">
					<li class="active"><a id="tabOrg" href="#messages1" data-toggle="tab">组 织</a></li>
					<li><a id="tabUser" href="#messages2" data-toggle="tab">人 员</a></li>
				</ul>
			</section>
			<section class="panel tab-content">
				<!-- 组织表格 -->
				<div class="tab-pane fade active in" id="messages1">
					<div class="table-wrap"><table class="table table-striped tab_height " id="departmentTable"></table></div>
					<section class="clearfix">
						<section class="form-btn fl m-l">
							<button class="btn dark" type="button" role="button" data-toggle="modal" onclick='department.showDeptInsertHtml();'>新 增</button>
						</section>
					</section>
				</div>
				<!-- 用户表格 -->
				<div class="tab-pane fade" id="messages2">
					<div class="table-wrap" style="margin-bottom: 70px;"><table class="table table-striped" id="uTable"></table></div>
					<section class="clearfix">
						<section class="pagination m-r fr m-t-none"></section>
					</section>
				</div>

				<!-- 删除组织表格 -->
				<div class="tab-pane fade" id="messages3">
					<div class="table-wrap" style="margin-bottom: 70px;"><table class="table table-striped tab_height " id="delDepartmentTable"></table></div>
					<section class="clearfix">
						<section class="pagination m-r fr m-t-none"></section>
					</section>
				</div>
			</section>
		</section>
	</section>
</section>
<input id="deptIds" name="deptIds" type="hidden"/>
<input id="userDeptId" name="userDeptId" type="hidden"/>
<div id="deptInsertHtml"></div>
<div id="deptEditHtml"></div>
<div id="changeResourceHtml"></div>
<script src="${sysPath}/js/com/sys/department/departmentUser.js?n=1" type="text/javascript"></script>
<script src="${sysPath}/js/common-tree.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
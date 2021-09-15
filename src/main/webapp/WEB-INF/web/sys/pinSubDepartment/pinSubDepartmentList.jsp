<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1>部门拼音表</h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="pinDepartmentQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td>部门名称</td>
							<td>
								<input type="text" id="deptName" name = "deptName" value="">
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryPinDepartment">查 询</button>
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
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"pinDepartmentList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				//var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"pinDepartmentList.deletePinDepartment("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit;
				return buttonStr;
			};
			</script>
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="pinDepartmentTable">
					<thead>
						<tr>
							<th>部门名称</th>
							<th>拼音首字母</th>
							<th>拼音首字母缩写</th>
							<th>拼音全拼</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark fr" href="#" id="info_loading" role="button">导入数据</a>
				</section>
			</section>
	</section>
	<div id="pinDepartmentModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/pinSubDepartment/pinSubDepartmentList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
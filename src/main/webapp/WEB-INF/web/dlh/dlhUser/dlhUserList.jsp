<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="dlhUserQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td style="width:150px;">编码</td>
							<td>
						 			<input type="text" id="dlhUsercode" name = "dlhUsercode" value="">
 
							</td>
							<td  style="width:150px;">用户</td>
							<td>
						 		<input type="text" id="dlhUsername" name = "dlhUsername" value=""> 
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryDlhUser">查 询</button>
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
				var edit = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhUserList.loadModuleForUpdate('"+ source.id+ "')\" ><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var help = "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"dlhUserList.loadModuleForHelp('"+ source.id+ "')\" ><i class=\"fa fa-books\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"帮助\"></i></a>";
				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"dlhUserList.deleteDlhUser('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit + help + del;
				return buttonStr;
			};
			</script>
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height " id="dlhUserTable">
					<thead>
						<tr>
							<th>系统简述</th>
							<th>用户</th>
							<th>一次最大量</th>
							<th>创建时间</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addDlhUserButton">新 增</a>
				</section>
			</section>
	</section>
	<div id="dlhUserModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/dlh/dlhUser/dlhUserList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
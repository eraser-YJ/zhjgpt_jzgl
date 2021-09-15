<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
	</header>
	<section class="panel search-shrinkage clearfix">
	    <div class="search-line hide">
	    	<form class="table-wrap form-table" id="uniqueQueryForm">
		        <table class="table table-td-striped">
		            <tbody>
						<tr>
							<td class="w100">唯一编号</td>
							<td class="w200">
								<input type="text" id="uuid" name="uuid"/>
							</td>
							<td class="w100">启用状态</td>
							<td class="w200">
								<select id="state" name="state">
									<option value="" selected>-全部-</option>
									<option value="1">已启用</option>
									<option value="0">未启用</option>
								</select>
							</td>
						</tr>
		            </tbody>
		        </table>
		        <section class="form-btn m-b-lg">
		            <button class="btn dark" type="button" id="queryUnique">查 询</button>
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
				var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"uniqueList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
				var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"uniqueList.deleteUnique('"+ source.id+ "')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
				buttonStr = edit + del;
				return del;
			};
			</script>
			<h2 class="panel-heading clearfix">查询结果</h2>
			<div class="table-wrap">
				<table class="table table-striped tab_height first-td-tc" id="uniqueTable">
					<thead>
						<tr>
							<th>唯一编号</th>
							<th class="w170">启用状态</th>
							<th class="w200">创建时间</th>
							<th class="w170">操作</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="addUniqueButton">批量生成</a>
				</section>
			</section>
	</section>
	<div id="uniqueModuleDiv"></div>
	</section>
</section>
<script src='${sysPath}/js/com/sys/unique/uniqueList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
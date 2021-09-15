<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script src="${sysPath}/js/com/sys/group/group.js" type="text/javascript"></script>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
	<div class="con-heading fl" id="navigationMenu">
		<h1></h1>
		<div class="crumbs"></div>
	</div>
	<a class="btn dark fr" type="button" href="#myModal-edit" id="groupTop" role="button" data-toggle="modal">新 增</a>
</header>
<section class="panel clearfix search-box search-shrinkage">
	<div class="search-line hide">
	<!-- <h2 class="panel-heading clearfix">查询条件</h2> -->
	 <form class="table-wrap form-table" id="groupListForm">
		<table class="table table-td-striped">
			<tbody>
				<tr>
					<td style="width:30%">组名称</td>
					<td style="width:70%"><div><input type="text" name="name"/></div></td>
				</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark" type="button" id="queryGroup">查 询</button><button class="btn" type="reset">重 置</button>
		</section>
	</form>
	</div>
	<%@ include file="/WEB-INF/web/include/searchConditionHide.jsp" %>
</section>  
<section class="panel clearfix">
	<!--<h2 class="panel-heading clearfix">查询结果</h2>-->
	<div class="table-wrap">
		<table class="table table-striped tab_height first-td-tc" id="groupTable">
			<thead>
				<tr>
					<th style="width:3%;"><input type="checkbox" /></th>
					<th style="width:45%;">组名称</th>
					<th style="width:10%;">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<section class="clearfix">
		<section class="form-btn fl m-l">
			<a class="btn dark" type="button" href="#myModal-edit" id="groupBottom" role="button" data-toggle="modal">新 增</a>
			<button class="btn" type="button" id="deleteGroups">批量删除</button>
		</section>
		<section class="pagination m-r fr m-t-none">
		</section>
	</section>
</section>
</section>
<div id="groupEdit"></div>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
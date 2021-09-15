<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable"> 
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<h1></h1>
			<div class="crumbs"></div>
		</div>
		<a class="btn dark fr" href="#" id="showAddDiv_t" role="button" data-toggle="modal">新 增</a>
	</header>
<section class="panel search-shrinkage clearfix">
    <div class="search-line hide">
    	<form class="table-wrap form-table" id="portalFriendlylinkQueryForm">
	        <table class="table table-td-striped">
	            <tbody>
					<tr>
						<td style="width: 15%;">所属门户</td>
						<td style="width: 35%;">
					 		<select id="portalId" name="portalId" onchange="portalFriendlylink.selportlets(this.value)">
								<option value="0">-全部-</option>
								<c:forEach items="${seldata.portallist}" var="portalvo" varStatus="status">
									<option value="${portalvo.id }">${portalvo.portalName }</option>
								</c:forEach>
							</select>
						</td>
						<td style="width: 15%;">所属门户业务组件</td>
						 <td style="width: 35%;">
						 	<select id="portletId" name="portletId">
								<option value="0">-全部-</option>
							</select>
							<select id="tempportlet" name="tempportlet" style="display: none;">
								<c:forEach items="${seldata.portletlist}" var="portletvo" varStatus="status">
									<option name="${portletvo.portalId }" value="${portletvo.id }">${portletvo.letTitle }</option>
								</c:forEach>
							</select>
						 </td>
					</tr>
					
	            </tbody>
	        </table>
	        <section class="form-btn m-b-lg">
	            <button class="btn dark" type="button" id="queryPortalFriendlylink">查 询</button>
	            <button class="btn" type="button" id="queryReset">重 置</button>
	        </section>
	    </form>
    </div>
    <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
</section>
<section class="panel" id="body">
	<div id="manageListTable" method="ManageList">
<script >
//设置每行按钮
function oTableSetButtones (source) {
	var buttonStr = "";
	var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"portalFriendlylink.loadModuleForUpdate("+ source.id+ ")\" role=\"button\" data-toggle=\"modal\"><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i></a>";
	var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"portalFriendlylink.deletePortalFriendlylink("+ source.id+ ")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
	buttonStr = edit + del;
	return buttonStr ;
};
</script>
		<!--<h2 class="panel-heading clearfix"></h2>-->
<div class="table-wrap">
	
	<table class="table table-striped tab_height first-td-tc" id="portalFriendlylinkTable">
		<thead>
			<tr>
				<th class="w46"><input type="checkbox" /></th>
				<th>友情链接名称</th>
				<th>友情链接地址</th>
				<th>所属门户</th>
				<th>所属门户业务组件</th>
				<th>排序号</th>
				<th class="w170">操作</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>

<section class="clearfix" id="footer_height">

	<section class="form-btn fl m-l">
		<a class="btn dark" href="#myModal-edit" role="button" id="addPortalFriendlylinkButton" data-toggle="modal">新 增</a><button class="btn " id="deletePortalFriendlylinks" type="button">批量删除</button>
	</section>

</section>

</div>
			<div id="formModuleDiv" method="FormMethod"></div>
		
	</section>
</section>

 <script src='${sysPath}/js/com/sys/portal/portalFriendlylink.js'></script>

<%@ include file="/WEB-INF/web/include/foot.jsp"%>
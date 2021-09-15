<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<h1></h1><div class="crumbs"></div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table flex-container" id="searchForm">
				<table class="project-marger flex-item" style="width:80%;">
					<tbody>
						<tr class="flex-container">
							<td class="flex-item">项目统一编号</td>
							<td class="flex-item"><input type="text" id="projectNumber" name="projectNumber" /></td>
							<td class="flex-item">项目名称</td>
							<td class="flex-item"><input type="text" id="projectName" name="projectName" /></td>
							<td class="flex-item">招投标方式</td>
							<td class="flex-item"><dic:select name="biddingMethod" id="biddingMethod" dictName="bidding_method" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
						</tr>
					</tbody>
				</table>
				<div style="text-align: center; background: #FFFFFF;" class="flex-item btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
				<shiro:hasPermission name='projectBiddingAdd'><a class="btn dark" role="button" id="addBtn">新 增</a></shiro:hasPermission>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<script>
	function oTableSetButtons(source) {
		var look = "<shiro:hasPermission name='projectBiddingLook'><a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectBiddingJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">查看</a></shiro:hasPermission>";
		var edit = "<shiro:hasPermission name='projectBiddingUpdate'><a class=\"a-icon i-new\"  href=\"#myModal-edit\"  onclick=\"projectBiddingJsList.loadModuleForUpdate('"+ source.id+ "')\" role=\"button\">修改</a></shiro:hasPermission>";
		var del = "<shiro:hasPermission name='projectBiddingDelete'><a class=\"a-icon i-remove\"  href=\"#\"  onclick=\"projectBiddingJsList.delete('"+ source.id+ "')\">删除</a></shiro:hasPermission>";
		return look + edit + del;
	}
</script>
<script src='${sysPath}/js/com/jc/csmp/project/bidding/projectBiddingList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
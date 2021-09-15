<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<c:choose>
			<c:when test="${open eq 'ycjz' }">
				<div class="crumbs">
					<span>项目管理</span>
					<span>项目管理 > </span><span>远程监造</span>
				</div>
			</c:when>
			<c:otherwise>
				<div class="crumbs">
					<span>视频监控预警</span>
					<span>视频监控预警 > </span><span>实时视频</span>
				</div>
			</c:otherwise>
		</c:choose>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
						<tr>
							<td class="w140">项目编号</td>
							<td>
								<input type="text" id="query_projectNumber" name="query_projectNumber"/>
							</td>
							<td class="w140">项目名称</td>
							<td>
								<input type="text" id="query_projectName" name="query_projectName"/>
							</td>
						</tr>
					</tbody>
				</table>
				<div  class="btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap">
			<div class="table-wrap">
				<section class="form-btn fl m-l">
					<a class="btn dark" role="button" id="realtimeBtn" style="margin: 0 !important;">查看实时视频</a>
				</section>
				<table class="table table-striped tab_height  first-td-tc" id="gridTable"></table>
			</div>
			<section class="clearfix" id="footer_height">
				<section class="form-btn fl m-l">
				</section>
			</section>
		</div>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/warn/video/realtimeProjectList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
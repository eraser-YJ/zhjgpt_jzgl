<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<h1></h1><div class="crumbs"></div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td class="w140">项目统一编号</td>
						<td><input type="text" id="projectNumber" name="projectNumber" /></td>
						<td class="w140">项目名称</td>
						<td><input type="text" id="projectName" name="projectName" /></td>
						<td style="text-align: center; background: #FFFFFF;" colspan="2">
							<button class="btn" type="button" id="queryBtn">查 询</button>
							<button class="btn" type="button" id="resetBtn">重 置</button>
						</td>
					</tr>
					</tbody>
				</table>
			</form>
		</div>
	</section>
	<section class="panel ">
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l"></section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/supervise/basic/superviseSafeList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
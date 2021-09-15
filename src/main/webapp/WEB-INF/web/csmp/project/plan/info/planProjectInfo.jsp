<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>进度及周报管理 > </span><span>项目计划制定</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
						<tr class="flex-container">
							<td style="width: 8%;">项目统一编号</td>
							<td style="width: 25%;"><input type="text" id="projectNumber" name="projectNumber" /></td>
							<td style="width: 8%;" class="w100">项目名称</td>
							<td style="width: 25%;"><input type="text" id="projectName" name="projectName" /></td>
							<td style="width: 8%;">所属区域</td>
							<td style="width: 25%;"><dic:select name="region" id="region" dictName="region" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
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
	<section class="panel">
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height"><section class="form-btn fl m-l"></section></section>
	</section>
</section>
<div id="chooseModule"></div>
<script src='${sysPath}/js/com/jc/csmp/project/plan/info/planProjectInfo.js?n=1'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

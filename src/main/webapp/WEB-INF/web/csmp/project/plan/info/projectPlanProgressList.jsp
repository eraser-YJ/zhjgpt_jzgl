<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>进度及周报管理 > </span><span>实际进度管理</span>
		</div>
	</header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto; width: 300px !important;">
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<section class="panel" style="height: 50px; display:none;">
				<form id="searchForm">
					<input type="hidden" id="progress_paramProjectId" name="progress_paramProjectId" data-column="projectId" />
					<input type="hidden" id="progress_paramStageId" name="progress_paramStageId" data-column="stageId" />
				</form>
			</section>
			<section class="panel tab-content">
				<div class="tab-pane fade active in">
					<div class="table-wrap" id="emptyDiv" style="text-align: center; font-weight: bold; font-size: 20px;">选择项目或阶段进行查看</div>
					<div class="table-wrap" id="tableDiv" style="display: none;">
						<table class="table table-striped tab_height" id="progressTable"></table>
					</div>
					<section class="clearfix"><section class="pagination m-r fr m-t-none"></section></section>
				</div>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/plan/info/cmProjectPlanProgress.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
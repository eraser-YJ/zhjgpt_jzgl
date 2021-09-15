<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1"><div class="con-heading fl"><h1></h1><div class="crumbs"></div></div></header>
	<section class="tree-fluid m-t-md">
		<aside class="tree-wrap" id="LeftHeight_box">
			<section class="panel" id="LeftHeight" style="overflow-y: auto;">
				<div id="treeDemo" class="ztree"></div>
			</section>
		</aside>
		<section class="tree-right">
			<form id="searchForm" style="display: none;">
				<input type="hidden" id="paramDataType" name="paramDataType" value="${dataType}" />
				<input type="hidden" id="paramParentId" name="paramParentId" />
			</form>
			<%--<section class="panel" style="height: 50px;">--%>
				<%--<section class="form-btn m-b-lg m-l" style="margin-top: 5px;">--%>
					<%--<button class="btn dark" type="button" id="addBtn">新 增</button>--%>
					<%--<button class="btn" type="button" id="refreshBtn">刷 新</button>--%>
				<%--</section>--%>
			<%--</section>--%>
			<section class="panel tab-content">
				<section class="panel" style="height:auto;margin-bottom: 0 !important;padding: 15px 0 0 5px !important; text-align: left !important;">
					<section class="form-btn m-b-lg m-l" style="text-align: left !important; margin-bottom: 0 !important;">
						<button class="btn dark" type="button" id="addBtn">新 增</button>
						<button class="btn" type="button" id="refreshBtn">刷 新</button>
					</section>
				</section>
				<div class="tab-pane fade active in">
					<div class="table-wrap">
						<table class="table table-striped tab_height" id="gridTable"></table>
					</div>
					<section class="clearfix">
						<section class="pagination m-r fr m-t-none"></section>
					</section>
				</div>
			</section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/dic/cmCustomDicList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

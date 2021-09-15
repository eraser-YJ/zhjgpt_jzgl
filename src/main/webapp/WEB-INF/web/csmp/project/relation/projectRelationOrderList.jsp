<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程联系单管理 > </span><span>我发送的</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
					<tr>
						<td class="w100">项目统一编号</td>
						<td><input type="text" id="projectNumber" name="projectNumber" /></td>
						<td class="w100">项目名称</td>
						<td><input type="text" id="projectName" name="projectName" /></td>
						<td class="w100">工程联系单编号</td>
						<td><input type="text" id="code" name="code" /></td>
					</tr>
					<tr>
						<td class="w100">工程联系单名称</td>
						<td><input type="text" id="title" name="title" /></td>
						<td class="w100">签收状态</td>
						<td>
							<select name="signStatus" id="signStatus">
								<option value="">-请选择-</option>
								<option value="0">未签收</option>
								<option value="1">已签收</option>
							</select>
						</td>
						<td></td><td></td>
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
			<section class="form-btn fl m-l"><a class="btn dark" role="button" id="addBtn" style="margin: 0 !important;">新 增</a></section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
</section>
<%@ include file="/WEB-INF/web/print/projectRelation.jsp" %>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<script src='${sysPath}/js/com/jc/csmp/project/relation/projectRelationOrderList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
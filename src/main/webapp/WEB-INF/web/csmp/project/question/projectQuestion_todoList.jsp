<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>问题协同管理 > </span><span id="sectionTitleOne"></span><span id="sectionTitleTwo"></span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<input type="hidden" id="questionType" name="questionType" value="${questionType}" />
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
					<tr>
						<td style="width: 10%;">项目统一编号</td>
						<td style="width: 40%;"><input type="text" id="projectNumber" name="projectNumber" /></td>
						<td style="width: 10%;">项目名称</td>
						<td style="width: 40%;"><input type="text" id="projectName" name="projectName" /></td>
					</tr>
					<tr>
						<td>问题编号</td>
						<td><input type="text" id="code" name="code" /></td>
						<td>登记时间</td>
						<td>
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateEnd lt">
								<div class="input-group-btn w30">-</div>
								<input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateBegin gt">
							</div>
						</td>
					</tr>
					</tbody>
				</table>
				<div class="btn-tiwc">
					<button class="btn" type="button" id="queryBtn">查 询</button>
					<button class="btn" type="button" id="resetBtn">重 置</button>
				</div>
			</form>
		</div>
	</section>
	<section class="panel clearfix" id="sendPassTransact-list">
		<div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l">
			</section>
		</section>
	</section>
</section>
<script src='${sysPath}/js/com/jc/csmp/project/question/projectQuestion_todoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
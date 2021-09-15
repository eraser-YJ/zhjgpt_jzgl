<%@ page language="java" pageEncoding="UTF-8" import="com.jc.csmp.workflow.enums.WorkflowContentEnum" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>首页 > </span><span>待办任务</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
						<tr>
							<td class="w100">任务类型</td>
							<td>
								<select id="workflowTitle" name="workflowTitle">
									<%
										WorkflowContentEnum[] arrayEnum = WorkflowContentEnum.values();
										for (WorkflowContentEnum workflowContentEnum : arrayEnum) {
											%><option value="<%=(workflowContentEnum.toString().equals("empty") ? "" : workflowContentEnum.toString()) %>"><%=workflowContentEnum.getValue() %></option><%
										}
									%>
								</select>
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
	<section class="panel">
		<div class="table-wrap">
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
</section>
<script src='${sysPath}/js/com/jc/csmp/workflow/workflowTodoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

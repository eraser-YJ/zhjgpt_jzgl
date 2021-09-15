<%@ page language="java" pageEncoding="UTF-8" import="com.jc.csmp.common.enums.WorkflowAuditStatusEnum"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs" id="crumbsHeaderTitle"></div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm">
				<input type="hidden" id="createUser" name="createUser" value="${createUser}" />
				<table class="table table-td-striped">
					<tbody>
					<tr>
						<td style="width: 8%;">合同编号</td>
						<td style="width: 25%;"><input type="text" id="contractCode" name="contractCode" /></td>
						<td style="width: 8%;">合同名称</td>
						<td style="width: 25%;"><input type="text" id="contractName" name="contractName" /></td>
						<td style="width: 8%;">审核状态</td>
						<td style="width: 25%;">
							<select id="auditStatus" name="auditStatus">
								<option value="">- 请选择 -</option>
								<option value="<%=WorkflowAuditStatusEnum.ing.toString() %>"><%=WorkflowAuditStatusEnum.ing.getValue() %></option>
								<option value="<%=WorkflowAuditStatusEnum.finish.toString() %>"><%=WorkflowAuditStatusEnum.finish.getValue() %></option>
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
	<section class="panel ">
		<div class="table-wrap">
			<section class="form-btn fl m-l"><a class="btn dark" role="button" id="addBtn" style="display: none; margin: 0 !important;">新 增</a></section>
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/csmp/contract/pay/cmContractPaySearchList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
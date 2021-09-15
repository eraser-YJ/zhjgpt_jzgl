<%@ page language="java" pageEncoding="UTF-8" import="com.jc.csmp.common.enums.WorkflowAuditStatusEnum" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
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
							<td style="width: 8%">项目统一编号</td>
							<td style="width: 25%"><input type="text" id="projectNumber" name="projectNumber" /></td>
                            <td style="width: 8%">项目名称</td>
                            <td style="width: 25%"><input type="text" id="projectName" name="projectName" /></td>
                            <td style="width: 8%">合同编号</td>
                            <td style="width: 25%"><input type="text" id="contractCode" name="contractCode" /></td>
						</tr>
                        <tr>
                            <td>合同名称</td>
                            <td><input type="text" id="contractName" name="contractName" /></td>
							<td>合同类型</td>
							<td><dic:select name="contractType" id="contractType" dictName="contract_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
							<td>结算是否需要审计</td>
							<td><dic:select name="needAudit" id="needAudit" dictName="yes_or_no" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
                        </tr>
						<tr>
							<td>合同开始结束时间</td>
							<td>
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="startDate" name="startDate" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#endDate lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="endDate" name="endDate" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#startDate gt">
								</div>
							</td>
							<td>签订时间</td>
							<td>
								<div class="input-group w-p100">
									<input class="datepicker-input" type="text" id="signDateBegin" name="signDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#signDateEnd lt">
									<div class="input-group-btn w30">-</div>
									<input class="datepicker-input" type="text" id="signDateEnd" name="signDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#signDateBegin gt">
								</div>
							</td>
							<td>审核状态</td>
							<td><dic:select name="auditStatus" id="auditStatus" dictName="workflow_audit_state" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
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
	<div id="chooseCompanyModule"></div>
</section>
<script>
	function oTableSetButtons(source) {
		var look = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"cmContractInfoJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">查看</a>";
		var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
		return look + history;
	}
</script>
<script src='${sysPath}/js/com/jc/csmp/contract/info/cmContractInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
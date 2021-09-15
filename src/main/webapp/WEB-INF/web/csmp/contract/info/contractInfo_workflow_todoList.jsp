<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>合同管理 > </span><span>合同备案审核</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
	            	<tbody>
					<tr>
						<td>项目统一编号</td>
						<td><input type="text" id="projectNumber" name="projectNumber" /></td>
						<td>项目名称</td>
						<td><input type="text" id="projectName" name="projectName" /></td>
						<td>合同编号</td>
						<td><input type="text" id="contractCode" name="contractCode" /></td>
					</tr>
					<tr>
						<td>合同名称</td>
						<td><input type="text" id="contractName" name="contractName" /></td>
						<td>合同类型</td>
						<td><dic:select name="contractType" id="contractType" dictName="contract_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
						<td>结算审计</td>
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
						<td></td>
						<td></td>
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
		<div class="table-wrap">
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
</section>
<script src='${sysPath}/js/com/jc/csmp/contract/info/contractInfo_workflow_todoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
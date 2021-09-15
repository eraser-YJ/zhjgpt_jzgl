<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs" id="crumbsHeaderTitle">
			<span>项目管理</span>
			<span>工程信息管理 > </span><span>项目查看 > </span><span>签证单查询</span>
		</div>
	</header>
	<section class="panel clearfix search-box search-shrinkage">
		<div style="background: #ededed;margin-top:10px;">
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=detail&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">详细信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhaobiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">招标信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhongbiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">中标信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongbeian&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同备案</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongzhifu&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同支付情况</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=biangengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">变更单</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl gxxxgl-active">工程签证单</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">问题协同</a>
		</div>
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
					<tr>
						<td class="w140">合同编号</td>
						<td><input type="text" id="contractCode" name="contractCode" /></td>
						<td class="w140">合同名称</td>
						<td><input type="text" id="contractName" name="contractName" /></td>
						<td class="w140">签证编号</td>
						<td><input type="text" id="code" name="code" /></td>
					</tr>
					<tr>
						<td class="w140">发生时间</td>
						<td colspan="3">
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="visaDateBegin" name="visaDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#visaDateEnd lt">
								<div class="input-group-btn w30">-</div>
								<input class="datepicker-input" type="text" id="visaDateEnd" name="visaDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#visaDateBegin gt">
							</div>
						</td>
						<td class="w140">审核状态</td>
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
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height"></section>
	</section>
	<div id="formModule"></div>
</section>
<%@ include file="/WEB-INF/web/print/projectVisa.jsp" %>
<script src='${sysPath}/js/com/jc/csmp/project/info/detail/qianzhengdan.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
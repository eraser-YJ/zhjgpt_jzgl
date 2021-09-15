<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="navigationMenu">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程信息管理 > </span><span>项目查看 > </span><span>问题协同</span>
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
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">工程签证单</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl gxxxgl-active">问题协同</a>
		</div>
		<div class="search-line">
			<form class="table-wrap form-table" id="searchForm" >
				<table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
					<tbody>
					<tr>
						<td class="w100">问题编号</td>
						<td><input type="text" id="code" name="code" /></td>
						<td class="w140">登记时间</td>
						<td colspan="3">
							<div class="input-group w-p100">
								<input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateEnd lt">
								<div class="input-group-btn w30">-</div>
								<input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#createDateBegin gt">
							</div>
						</td>
						<td class="w140">审核状态</td>
						<td><dic:select name="auditStatus" id="auditStatus" dictName="workflow_audit_state" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
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
	<section class="panel ">
		<div class="table-wrap">
			<table class="table table-striped tab_height" id="gridTable"></table>
		</div>
		<section class="clearfix" id="footer_height">
			<section class="form-btn fl m-l"></section>
		</section>
	</section>
	<div id="formModule"></div>
</section>
<script>
function oTableSetButtons(source) {
	var look = "<a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectQuestionListPanel.loadModuleForLook('"+ source.id+ "', '" + source.questionType + "')\" role=\"button\">" + finalTableBtnText('查看', 'fa-infor-search') + "</a>";
	var edit = "", del = "";
	if (source.questionType == 'scene') {
		if (source.auditStatus == 'ing') {
			edit = "<shiro:hasPermission name='projectQuestionEdit'><a class=\"a-icon i-new\" href=\"javascript:void(0);\" onclick=\"projectQuestionListPanel.loadModuleForUpdate('"+ source.id+ "', '" + source.questionType + "')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a></shiro:hasPermission>";
		} else if (source.auditStatus == 'fail') {
			del = "<shiro:hasPermission name='projectQuestionDelete'><a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"projectQuestionListPanel.delete('"+ source.id+ "')\">" + finalTableBtnText('删除', 'fa-remove') + "</a></shiro:hasPermission>";
		}
	}
	var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
	return look + edit + del + history;
}
</script>
<%@ include file="/WEB-INF/web/print/projectQuestion.jsp" %>
<script src='${sysPath}/js/com/jc/csmp/project/info/detail/wenti.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
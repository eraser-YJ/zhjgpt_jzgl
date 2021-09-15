<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程信息管理 > </span><span>项目查看</span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<div style="background: #ededed;margin-top:10px;">
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=detail&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl gxxxgl-active">详细信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhaobiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">招标信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhongbiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">中标信息</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongbeian&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同备案</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongzhifu&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同支付情况</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=biangengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">变更单</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">工程签证单</a>
			<a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">问题协同</a>
		</div>
		<form class="table-wrap form-table" id="entityForm">
			<h3>项目查看</h3>
			<table class="table table-td-striped"><tbody>
				<tr>
					<td class="w100">项目文号</td>
					<td><input type="text" id="approvalNumber" name="approvalNumber" readonly="readonly" /></td>
					<td class="w100">立项时间</td>
					<td><input type="text" id="projectApprovalDate" name="projectApprovalDate" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>项目名称</td>
					<td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
					<td>项目统一编号</td>
					<td><input type="text" id="projectNumber" name="projectNumber" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>项目类型</td>
					<td><input type="text" id="projectTypeValue" name="projectTypeValue" readonly="readonly" /></td>
					<td>建设类型</td>
					<td><input type="text" id="buildTypeValue" name="buildTypeValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>所属区域</td>
					<td><input type="text" id="regionValue" name="regionValue" readonly="readonly" /></td>
					<td>项目地址</td>
					<td><input type="text" id="projectAddress" name="projectAddress" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>国标行业</td>
					<td><input type="text" id="projectTradeValue" name="projectTradeValue" readonly="readonly" /></td>
					<td>用地性质</td>
					<td><input type="text" id="landNatureValue" name="landNatureValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>监管单位</td>
					<td><input type="text" id="superviseDeptIdValue" name="superviseDeptIdValue" readonly="readonly" /></td>
					<td>建设单位</td>
					<td><input type="text" id="buildDeptIdValue" name="buildDeptIdValue" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>拟投金额(万元)</td>
					<td><input type="text" id="planAmount" name="planAmount" readonly="readonly" /></td>
					<td>投资金额(万元)</td>
					<td><input type="text" id="investmentAmount" name="investmentAmount" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>建筑面积(㎡)</td>
					<td><input type="text" id="buildArea" name="buildArea" readonly="readonly" /></td>
					<td>用地面积(㎡)</td>
					<td><input type="text" id="landArea" name="landArea" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>拟开工时间</td>
					<td><input type="text" id="planStartDate" name="planStartDate" readonly="readonly" /></td>
					<td>拟建成时间</td>
					<td><input type="text" id="planEndDate" name="planEndDate" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>建设规模及内容</td>
					<td><textarea id="projectContent" name="projectContent" style="height: 60px" readonly="readonly"></textarea></td>
					<td>备注</td>
					<td><textarea id="remark" name="remark" style="height: 60px" readonly="readonly"></textarea></td>
				</tr>
			</tbody></table>

			<h3>附件信息</h3>
			<table class="table table-td-striped" id="attachTable"></table>

			<div id="personDiv" style="display: none;">
				<h3>分配人员信息</h3>
				<table class="table table-td-striped" id="personTable"></table>
				<section class="clearfix" id="footer_height"></section>
			</div>
		</form>
		<div id="formFoorDiv" class="m-l-md" style="text-align: center; height: 100px;">
			<button class="btn" id="closeBtn" type="button">返 回</button>
		</div>
	</section>
</section>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/info/detail/detail.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
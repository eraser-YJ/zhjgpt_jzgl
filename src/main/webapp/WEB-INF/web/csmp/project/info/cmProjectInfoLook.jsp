<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<input type="hidden" id="loginUserDeptName" name="loginUserDeptName" value="${data.loginUserDeptName}" />
<input type="hidden" id="loginUserDeptId" name="loginUserDeptId" value="${data.loginUserDeptId}" />
<input type="hidden" id="canBandBuild" name="canBandBuild" value="${data.canBandBuild}" />
<input type="hidden" id="resourceData" name="resourceData" value='${data.resourceData}' />
<input type="hidden" id="paramResourceDataId" name="paramResourceDataId" value='${data.resourceDataId}' />
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>项目管理</span>
			<span>工程信息管理 > </span><span id="headerTitle"></span>
		</div>
	</header>
	<section class="panel m-t-md clearfix">
		<div>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=detail">详细信息</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=zhaobiao">招标信息</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=zhongbiao">中标信息</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=hetongbeian">合同备案</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=hetongzhifu">合同支付情况</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=biangengdan">变更单</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=qianzhengdan">工程签证单</a>
			<a href="${sysPath}/project/info/detailTab.action?id=${id}&p=wenti">问题协同</a>
		</div>
		<form class="table-wrap form-table" id="entityForm">
			<h3 id="entityTitle"></h3>
			<input type="hidden" id="operator" name="operator" value="${data.operator}" />
			<input type="hidden" id="id" name="id" value="${data.id}" />
			<input type="hidden" id="token" name="token" value="${data.token}">
			<input type="hidden" id="modifyDate" name="modifyDate" />
			<input type="hidden" id="resourceDataId" name="resourceDataId" />
			<table class="table table-td-striped"><tbody>
				<tr>
					<td class="w100">项目文号<span class='required'>*</span></td>
					<td><input type="text" id="approvalNumber" name="approvalNumber" /></td>
					<td class="w100">立项时间</td>
					<td><input type="text" id="projectApprovalDate" name="projectApprovalDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td>
				</tr>
				<tr>
					<td>项目名称<span class='required'>*</span></td>
					<td><input type="text" id="projectName" name="projectName" /></td>
					<td>项目统一编号<span class='required'>*</span></td>
					<td><input type="text" id="projectNumber" name="projectNumber" /></td>
				</tr>
				<tr>
					<td><span class='required'>*</span>项目类型<span class='required'>*</span></td>
					<td><dic:select name="projectType" id="projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
					<td><span class='required'>*</span>建设类型</td>
					<td><dic:select name="buildType" id="buildType" dictName="build_type" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
				</tr>
				<tr>
					<td>所属区域<span class='required'>*</span></td>
					<td><dic:select name="region" id="region" dictName="region" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
					<td>项目地址</td>
					<td><input type="text" id="projectAddress" name="projectAddress" /></td>
				</tr>
				<tr>
					<td>国标行业<span class='required'>*</span></td>
					<td><div id="projectTradeFormDiv"></div></td>
					<td>用地性质<span class='required'>*</span></td>
					<td><div id="landNatureFormDiv"></div></td>
				</tr>
				<tr>
					<td>监管单位<span class='required'>*</span></td>
					<td><div id="superviseDeptIdFormDiv"></div></td>
					<td>建设单位<span class='required'>*</span></td>
					<td>
						<input type="hidden" id="buildDeptId" name="buildDeptId" />
						<input type="text" id="buildDeptIdValue" name="buildDeptIdValue" readonly="readonly" style="width: 70%" />
						<input type="button" class="btn" value="选择" id="buildDeptChangeBtn" />
					</td>
				</tr>
				<tr>
					<td>拟投金额(万元)</td>
					<td><input type="text" id="planAmount" name="planAmount" /></td>
					<td>投资金额(万元)</td>
					<td><input type="text" id="investmentAmount" name="investmentAmount" /></td>
				</tr>
				<tr>
					<td>建筑面积(㎡)</td>
					<td><input type="text" id="buildArea" name="buildArea" /></td>
					<td>用地面积(㎡)</td>
					<td><input type="text" id="landArea" name="landArea" /></td>
				</tr>
				<tr>
					<td>拟开工时间</td>
					<td><input type="text" id="planStartDate" name="planStartDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td>
					<td>拟建成时间</td>
					<td><input type="text" id="planEndDate" name="planEndDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td>
				</tr>
				<tr>
					<td>建设规模及内容</td>
					<td><textarea id="projectContent" name="projectContent" style="height: 60px"></textarea></td>
					<td>备注</td>
					<td><textarea id="remark" name="remark" style="height: 60px"></textarea></td>
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
			<button class="btn dark" id="saveBtn" type="button">保 存</button>
			<button class="btn" id="closeBtn" type="button">返 回</button>
		</div>
	</section>
</section>
<div id="chooseCompanyModule"></div>
<script src="${sysPath}/js/com/jc/csmp/common/plugin/choose-company.js" type="text/javascript"></script>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/info/cmProjectInfoForm.js?n=1'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
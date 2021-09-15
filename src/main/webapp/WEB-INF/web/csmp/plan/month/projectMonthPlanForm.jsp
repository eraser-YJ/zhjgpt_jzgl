<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src="${sysPath}/js/com/jc/csmp/plan/month/month_assembly.js" type="text/javascript"></script>
<c:forEach items="${workflowConfig}"  var="entry" varStatus="status">
	<input type="hidden" id="${entry.key}_wfvar" name="${entry.key}_wfvar" value="${entry.value}"/>
</c:forEach>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <div style="width:100%;overflow-x:auto;position:relative;"  id="pageDisplayDiv">
	<!--start 表格-->
	<section class="panel m-t-md clearfix" style="width:3200px;">

		<form class="table-wrap m-20-auto" style="width:100%;" id="projectPlanFormxxxxxxxxx">
			<div style="width:100%;text-align:center">
				<h3 class="tc" ><span id="planNameTitle1"></span></h3>
				<div style="text-align:right;margin: auto"><span>单位：万元</span></div>
				<table style="text-align:center;margin: auto;table-layout:fixed;width:100%" border="1px">
					<thead>
					<tr>
						<td colspan="2" rowspan="2" style="width:120px;">序号</td>
						<td rowspan="2" >项目名称</td>
						<td rowspan="2" style="width:150px;">总投资(万元)</td>
						<td rowspan="2" style="width:150px;">累计已完成投资(万元)</td>
						<td rowspan="2" style="width:150px;">计划投资(万元)</td>
						<td rowspan="2" style="width:250px;">形象进度</td>
						<td rowspan="2" style="width:150px;">本月计划投资(万元)</td>
						<td rowspan="2" style="width:180px;">实际本月完成投资(万元)</td>
						<td rowspan="2" style="width:300px;">存在问题及解决措施</td>
						<td colspan="5" style="width:550px;">前期手续完成情况（是或否）</td>
						<td rowspan="2" style="width:150px;">责任部门</td>
						<td rowspan="2" style="width:150px;">责任人</td>
						<td rowspan="2" style="width:150px;">备注</td>
					</tr>
					<tr>
						<td style="width:110px;">土地证</td>
						<td style="width:110px;">用地规划许可证</td>
						<td style="width:110px;">工程规划许可证</td>
						<td style="width:110px;">开工许可证</td>
						<td style="width:110px;">项目选址意见书</td>
					</tr>
					</thead>
					<tbody id="playBody00">

					</tbody>
				</table>
			</div>
		</form>
		<form class="table-wrap m-20-auto wplanpage"  id="projectPlanForm">
			<input type="hidden" id="id" name="id" value="${id}"/>
			<input type="hidden" id="pageMode" name="pageMode" value="${pageMode}"/>
			<input type="hidden" id="planYear" name="planYear" value=""/>
			<input type="hidden" id="planMonth" name="planMonth" value=""/>
			<input type="hidden" id="planAreaCode" name="planAreaCode" value=""/>
			<input type="hidden" id="planAreaName" name="planAreaName" value=""/>
			<input type="hidden" id="planName" name="planName" value=''/>
			<input type="hidden" id="modifyDate" name="modifyDate"/>
			<input type="hidden" id="planType" name="planType"/>
			<input type="hidden" id="itemListContent" name="itemListContent"/>
			<input type="hidden" id="itemChange" name="itemChange"/>
		</form>
	</section>
	</div>
	<div id="formFoorDiv" class="m-l-md" style="margin-top: 10px; position: relative;overflow: auto;">
		<section class="form-btn fl m-l" style="width: 360px; margin: 0 auto; float: inherit;">
			<a class="btn dark" role="button" id="addMonthBtn">保 存</a>
			<a class="btn" role="button" id="closeMonthBtn">关 闭</a>
		</section>
	</div>


	<!--end 表格-->
</section>
<div id="itemshowdiv"></div>
<div id="projectDiv"></div>
<script src="${sysPath}/js/com/jc/csmp/plan/month/month_form.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>


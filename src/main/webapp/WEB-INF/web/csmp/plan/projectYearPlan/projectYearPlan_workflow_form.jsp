<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ include file="/WEB-INF/web/include/workflowHide.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src="${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlan_table_assembly.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>
<c:forEach items="${workflowConfig}"  var="entry" varStatus="status">
	<input type="hidden" id="${entry.key}_wfvar" name="${entry.key}_wfvar" value="${entry.value}"/>
</c:forEach>
<section class="scrollable padder jcGOA-section" id="scrollable"> 

	<!--start 表格-->
	<div style="width:100%;overflow-x:auto;position:relative;" id="pageDisplayDiv">
	<section class="panel m-t-md clearfix" id="pageDisplaySelect" style="width:3000px;">
		<input type="hidden" id="businessJson"	value='${businessJson}'/>

		<form class="table-wrap m-20-auto" style="width:100%;" id="projectPlanFormxxxxxxxxx">

			<div style="text-align:center;background-color: #ffffff;">
			<h3 class="tc" ><span id="planNameTitle1"></span></h3>
			<div style="text-align:right;margin: auto"><span>单位：万元</span></div>
			<table  style="text-align:center;margin: auto;table-layout:fixed;width:100%" border="1px">
				<thead>

				<tr>

					<td colspan="2" style="width:120px;">序号</td>
					<td>项目名称</td>
					<td style="width:150px;">总投资(万元)</td>
					<td style="width:150px;">累计已完成投资(万元)</td>
					<td style="width:150px;">计划投资(万元)</td>
					<td style="width:150px;">以后投资(万元)</td>
					<td style="width:150px;">工期(天)</td>
					<td style="width:600px;">主要工作内容及规模</td>
					<td style="width:150px;">责任部门</td>
					<td style="width:150px;">责任人</td>
					<td style="width:150px;">实施必要性</td>
					<td style="width:320px;">备注<br>（PPP项目标注为新型城镇化或其他PPP项目）</td>
				</tr>
				</thead>
				<tbody id="playBody00">

				</tbody>
			</table>
			<div style="padding-top: 10px;padding-left: 0px;padding-right: 0px; display: none" id="remarkDiv">
				<table style="width:100%">
					<tr>
						<th nowrap="nowrap" style="width:140px;">说明</th>
						<td colspan="3" style="text-align: left;">
							<textarea id="disSemark"  name="disSemark" rows="4" readonly="readonly"></textarea>
						</td>
					</tr>
				</table>
			</div>
			</div>

		</form>
		<form class="table-wrap m-20-auto wplanpage"  id="projectPlanForm">
			<input type="hidden" id="token" name="token" value="${token}"/>
			<input type="hidden" id="id" name="id" value="${selectId}"/>
			<input type="hidden" id="pageMode" name="pageMode" value="${pageMode}"/>
			<input type="hidden" id="selectId" name="selectId" value="${selectId}"/>
			<input type="hidden" id="selectYear" name="selectYear" value="${selectYear}"/>
			<input type="hidden" id="areaCode" name="areaCode" value="${areaCode}"/>
			<input type="hidden" id="areaName" name="areaName" value="${areaName}"/>
			<input type="hidden" id="planYear" name="planYear" value="${selectYear}"/>
			<input type="hidden" id="planSeqno" name="planSeqno" value="${planSeqno}"/>
			<input type="hidden" id="planAreaCode" name="planAreaCode" value="${areaCode}"/>
			<input type="hidden" id="planAreaName" name="planAreaName" value="${areaName}"/>
			<input type="hidden" id="piId" name="piId" value='${piId}'/>
			<input type="hidden" id="planName" name="planName" value=''/>
			<input type="hidden" id="modifyDate" name="modifyDate"/>
			<input type="hidden" id="planType" name="planType"/>
			<input type="hidden" id="itemListContent" name="itemListContent"/>
			<input type="hidden" id="itemChange" name="itemChange"/>
			<input type="hidden" id="remark"  name="remark"/>
			<c:choose>
				<c:when test="${pageMode eq 'VIEW' }">
					<!-- 办理意见 -->
					<div style="padding-left: 0px;padding-right: 0px;display: none" class="table-wrap m-20-auto" id="suggestAreaDiv" >
						<table class="formTable">
							<tr>
								<th rowspan="2" nowrap="nowrap" style="width:140px;">办理意见</th>
								<td colspan="3" style="text-align: left;">
									<div id="suggestListDiv" style="width:100%;"></div>
								</td>
							</tr>
						</table>
					</div>
				</c:when>
				<c:when test="${pageMode eq 'EDIT' }">
					<!-- 办理意见 -->
					<div style="padding-left: 10px;padding-right: 10px;">
						<table class="formTable">
							<tr>
								<th rowspan="2" nowrap="nowrap" style="width:140px;">办理意见</th>
								<td colspan="3" style="text-align: left;">
									<workflow:suggest itemId="projectPlanSuggestAreaCtrl" showLast="false" order="createTime" classStr="" showWritePannel="false" style=""/>
								</td>
							</tr>
						</table>
					</div>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</form>
	</section>
	</div>
	<div id="formFoorDiv" class="m-l-md" style="margin-top: 10px;">
        <section id="workflowFormButton" class=" form-btn statistics-box">

        </section>
    </div>
    <!--end 表格-->
</section>
<div id="itemshowdiv"></div>

<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlan_workflow_form.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlan.validate.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/csmp/plan/include/selectFile.jsp"%>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ include file="/WEB-INF/web/include/workflowHide.jsp"%>

<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="crumbs">
			<span>安全监督</span>
			<span>工程报监 > </span><span>工程报监申请</span>
		</div>
	</header>
	<!--start 表格-->

	<section class="panel m-t-md clearfix">
        <form class="table-wrap form-table" id="entityForm">
			<h3 class="tc">工程报监申请表</h3>
			<input type="hidden" id="businessJson"	value='${businessJson}'/>
			<input type="hidden" id="token" name="token" value="${token}"/>
			<input type="hidden" id="id" name="id" value="0"/>
			<input type="hidden" id="piId" name="piId" value='${piId}'/>
			<input type="hidden" id="modifyDate" name="modifyDate"/>
			<input type="hidden" id="companyTypeDic"	value='${dictList}'/>
			<input type="hidden" id="itemCode"	name="itemCode" value="safetySupervision"/>
			<input type="hidden" id="supervisionUnit"	name="supervisionUnit"/>
			<input type="hidden" id="buildUnit"	name="buildUnit"/>

			<!-- 归档文书档案开关隐藏域  -->

			<table class="table table-td-striped">
				<tbody>
					<tr>
							<td><span class='required'>*</span>工程名称</td>
							<td>
								<input type="text" id="projectName" name="projectName" workFlowForm='textinput' itemName="projectName" />
								<input type="hidden" id="projectId" name="projectId" />
								<input type="hidden" id="projectNumber" name="projectNumber" />
							</td>
							<td><span class='required'>*</span>工程地点</td>
							<td>
								<input type="text" id="projectAddress" name="projectAddress" workFlowForm='textinput' itemName="projectAddress"/>
							</td>
					</tr>
					<tr>
						<td>
							<span class='required'>*</span>安全文明施工目标
						</td>
						<td colspan="3">
							<input type="text" id="extStr1" name="extStr1" workFlowForm='textinput' itemName="extStr1"/>

						</td>
					</tr>
					<tr>
						<td><span class='required'>*</span>开工日期</td>
						<td>
							<input class="datepicker-input" type="text" id="planStartDate" name="planStartDate" workFlowForm='textinput' itemName="planStartDate" data-pick-time="false" data-date-format="yyyy-MM-dd">
						</td>
						<td><span class='required'>*</span>竣工日期</td>
						<td>
							<input class="datepicker-input" type="text" id="planEndDate" name="planEndDate" workFlowForm='textinput' itemName="planEndDate" data-pick-time="false" data-date-format="yyyy-MM-dd">
						</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>建筑面积</td>
							<td>
								<input type="text" id="buildArea" name="buildArea" workFlowForm='textinput' itemName="buildArea"/>
							</td>
							<td><span class='required'>*</span>工程造价</td>
							<td>
								<input type="text" id="investmentAmount" name="investmentAmount" workFlowForm='textinput' itemName="investmentAmount"/>
							</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>工程类型</td>
							<td>
								<span workFlowForm='select' itemName="projectType">
								<dic:select name="projectType" id="projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
								</span>
							</td>
							<td><span class='required'>*</span>报监时间</td>
							<td>
								<input class="datepicker-input" type="text" id="extDate1" name="extDate1" workFlowForm='textinput' itemName="extDate1" data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
					</tr>
					<tr>
						<td><span class='required'>*</span>层次</td>
						<td>
							<input type="text" id="extStr2" name="extStr2" workFlowForm='textinput' itemName="extStr2"/>
						</td>
						<td><span class='required'>*</span>结构</td>
						<td>
								<span workFlowForm='select' itemName="structureType">
								<dic:select name="structureType" id="structureType" dictName="structure_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
								</span>
						</td>
					</tr>


					<c:forEach items="${itemClassify.itemClassifyAttachList}" var="itemClassifyAttach" varStatus="status">
						<tr>
							<td class="w100">${itemClassifyAttach.itemAttach}</td>
							<td colspan="3">
							<span workFlowForm='attach' itemName="uploadAtt achContainer">
								<div class="uploadButt">
									<a class="btn dark" type="button" data-target="#file-edit${status.index+1}" id="file-edit_a_${status.index+1}"
									   role="button" data-toggle="modal">上传</a>
								</div>
								<div class="fjList">
									<ul class="fjListTop nobt">
										<li>
											<span class="enclo">已上传附件</span>
											<span class="enclooper">操作</span>
										</li>
									</ul>
									<ul class="fjListTop" id="attachList${status.index+1}"></ul>
								</div>
							</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
					<!-- 办理意见 -->
					<div style="padding-left: 0px;padding-right: 0px;display: none" class="table-wrap m-20-auto" id="suggestAreaDiv" >
						<table class="formTable" workFlowForm="autoRow" itemName="familyMemberTable">
							<tr>
								<th rowspan="2" nowrap="nowrap" style="width:140px;">办理意见</th>
								<td colspan="3">
									<div id="suggestListDiv" style="width:100%;"></div>
								</td>
							</tr>
						</table>
					</div>


		 <h2 class="panel-heading clearfix">责任主体</h2>
		<div class="table-wrap" id="companyTypeDiv">
			<table class="table table-striped tab_height" id="safetyUnitTable" workFlowForm="autoRow" itemName="safetyUnitTable">
				<thead>
				<tr id="safetyUniTr">
					<th style="text-align: center">责任主体类型</th>
					<th style="text-align: center">单位名称</th>
					<th style="text-align: center">项目负责人</th>
					<th style="text-align: center">联系电话</th>
					<th style="text-align: center" workflowform='autoRow-Column' itemName='safetyUnit_add' >
						<input type="button" value="添加" id='addRow' workflowform='button' itemName='safetyUnitAddButton'/>
					</th>
				</tr>

				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<script id="autoRowSafetyUnitTmpl" type="text/x-jquery-tmpl">
			<tr id="{{safetySupervisionJsForm.safetyUnitIndex}}">
				<input type="hidden" id="unitId{{safetySupervisionJsForm.safetyUnitIndex}}" name="safetyUnitList[{{ safetySupervisionJsForm.safetyUnitIndex}}].id" value="">
				<td id="partakeTypeTD{{ safetySupervisionJsForm.safetyUnitIndex}}" style="text-align: center">
				<span autoFlowForm='select'>
				<dic:select name="safetyUnitList[{{ safetySupervisionJsForm.safetyUnitIndex}}].partakeType" id="partakeType{{ safetySupervisionJsForm.safetyUnitIndex}}" dictName="company_type" parentCode="csmp" defaultValue="" headType="1" headValue="" />
				</span>
				</td>
				<td >
				<div id="companyFormDiv{{ safetySupervisionJsForm.safetyUnitIndex}}"  autoFlowForm='userSelect' itemId="companyFormDiv{{ safetySupervisionJsForm.safetyUnitIndex}}-unitId-{{ safetySupervisionJsForm.safetyUnitIndex}}"></div>
					<input type="hidden" id="unitName{{safetySupervisionJsForm.safetyUnitIndex}}" name="safetyUnitList[{{ safetySupervisionJsForm.safetyUnitIndex}}].unitName" >

				</td>
				<td>
				<span autoFlowForm='textinput'>
					<input class="length50" maxlength="50" style="width:99%;" type="text" id="projectLeader{{ safetySupervisionJsForm.safetyUnitIndex}}" name = "safetyUnitList[{{ safetySupervisionJsForm.safetyUnitIndex}}].projectLeader" value="" >
				</td>
				<td >
				<span autoFlowForm='textinput'>
					<input class="length50 " maxlength="50" style="width:99%;" type="text" id="phone{{ safetySupervisionJsForm.safetyUnitIndex}}" name = "safetyUnitList[{{ safetySupervisionJsForm.safetyUnitIndex}}].phone" value="" >
				</td>
				</span>
				<td >
   					<input type="button" name="autoRowSafetyUnit" class="btn btn-error delete" value="删除" workflowform='button' itemName='safetyUnitDeleteButton'>
				</td>
			</tr>
		</script>




			<div>
				<table class="table table-td-striped" >
					<tr workFlowForm="container" itemName="approvalSuggest">
						<td class="w140">
							<span class='required'>*</span>办理意见
						</td>
						<td >
							<workflow:suggest itemId="approvalSuggest" showLast="false" order="createTime" showWritePannel="false" classStr="" style=""/>
						</td>
					</tr>
				</table>
			</div>

		<div id="formFoorDiv" class="m-l-md" style="text-align: center;">
			<section id="workflowFormButton" class=" form-btn statistics-box"></section>


			<%--<button class="btn dark" id="createAdviceNote" type="button">生成通知书</button>
			<button class="btn dark" id="downAdviceNote" type="button">下载通知书</button>--%>
		</div>

    </form>
	</section>
    <!--startprint1-->
    <div id="printDiv" style="display: none">
		<p>
        <h2 style="text-align: center">工程报监申请表</h2>
		<p>
		<p>
        <table width="95%" border="1" id="printTable">
            <tbody>
            <tr>
                <td width="20%" style="text-align: center">工程名称</td>
                <td id="projectName_print" >

                </td>
                <td width="20%" style="text-align: center">工程地点</td>
                <td id="projectAddress_print">
                </td>
            </tr>
            <tr>
                <td style="text-align: center">安全文明<br>施工目标</td>
                <td colspan="3" id="extStr1_print">

                </td>
            </tr>
            <tr>
                <td style="text-align: center">开工日期</td>
                <td  id="planStartDate_print">
                </td>
                <td style="text-align: center">竣工日期</td>
                <td id="planEndDate_print">
                </td>
            </tr>
            <tr>
                <td style="text-align: center">建筑面积</td>
                <td id="buildArea_print">
                </td>
                <td style="text-align: center">工程造价</td>
                <td id="investmentAmount_print">
                </td>
            </tr>
            <tr>
                <td style="text-align: center">工程类型</td>
                <td id="projectType_print">
                </td>
                <td style="text-align: center">报监时间</td>
                <td id="extDate1_print">
                </td>
            </tr>
            <tr>
                <td style="text-align: center">层次</td>
                <td id="extStr2_print">
                </td>
                <td style="text-align: center">结构</td>
                <td id="structureType_print">
                </td>
            </tr>
            <tr>
                <td colspan="4" style="text-align: center"><h4>责任主体</h4></td>

            </tr>
            <tr>
                <td style="text-align: center">责任主体类型</td>
                <td style="text-align: center">单位名称</td>
                <td style="text-align: center">项目负责人</td>
                <td style="text-align: center">联系电话</td>
            </tr>

            </tbody>
        </table>

    </div>
    <!--endprint1-->
</section>

<%@ include file="/WEB-INF/web/csmp/safe/common/choose-safety-project.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/lib/common/dynrow.min.js"></script>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision_form.js?v=123' type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision.validate.js?v=123' type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
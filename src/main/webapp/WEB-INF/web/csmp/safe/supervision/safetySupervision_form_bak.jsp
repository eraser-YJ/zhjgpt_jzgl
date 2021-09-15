<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ include file="/WEB-INF/web/include/workflowHide.jsp"%>

<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in" id="header_1">
		<div class="con-heading fl" id="navigationMenu">
			<div class="crumbs"></div>
		</div>
	</header>
	<!--start 表格-->

	<section class="panel m-t-md clearfix">
        <form class="table-wrap form-table" id="entityForm">
			<h3 class="tc">工程报监申请表1</h3>
			<input type="hidden" id="businessJson"	value='${businessJson}'/>
			<input type="hidden" id="token" name="token" value="${token}"/>
			<input type="hidden" id="id" name="id" value="0"/>
			<input type="hidden" id="piId" name="piId" value='${piId}'/>
			<input type="hidden" id="modifyDate" name="modifyDate"/>
			<input type="hidden" id="companyTypeDic"	value='${dictList}'/>
			<!-- 归档文书档案开关隐藏域  -->

			<table class="table table-td-striped">
				<tbody>
					<tr>
							<td><span class='required'>*</span>工程名称</td>
							<td>
								<input type="text" id="projectName" name="projectName" workFlowForm='textinput' />
								<input type="hidden" id="projectId" name="projectId" />
							</td>
							<td><span class='required'>*</span>工程地址</td>
							<td>
								<input type="text" id="projectAddress" name="projectAddress" workFlowForm='textinput'/>
							</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>建设性质</td>
						<td><dic:select name="buildProperties" id="buildProperties" dictName="build_property" parentCode="csmp" defaultValue="" headType="2" headValue="" /></td>
							</td>
							<td><span class='required'>*</span>投资类别</td>
							<td>
								<dic:select name="investmentCategory" id="investmentCategory" dictName="investment_category" parentCode="csmp" defaultValue="" headType="2" headValue="" />
							</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>建筑面积</td>
							<td>
								<input type="text" id="buildArea" name="buildArea" workFlowForm='textinput'/>
							</td>
							<td><span class='required'>*</span>合同造价</td>
							<td>
								<input type="text" id="investmentAmount" name="investmentAmount" workFlowForm='textinput'/>
							</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>工程类型</td>
							<td>
								<dic:select name="projectType" id="projectType" dictName="project_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
							</td>
							<td><span class='required'>*</span>结构类型</td>
							<td>
								<dic:select name="structureType" id="structureType" dictName="structure_type" parentCode="csmp" defaultValue="" headType="2" headValue="" />
							</td>
					</tr>
					<tr>
							<td><span class='required'>*</span>开工日期</td>
							<td>
								<input class="datepicker-input" type="text" id="planStartDate" name="planStartDate" workFlowForm='textinput' data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
							<td><span class='required'>*</span>竣工日期</td>
							<td>
								<input class="datepicker-input" type="text" id="planEndDate" name="planEndDate" workFlowForm='textinput' data-pick-time="false" data-date-format="yyyy-MM-dd">
							</td>
					</tr>
					<tr>
						<td class="w100">附件信息</td>
						<td colspan="3">
							<span workFlowForm='attach' itemName="uploadAttachContainer">
								<div class="uploadButt">
									<a class="btn dark" type="button" data-target="#file-edit1"  id="file-edit_a_1"  role="button" data-toggle="modal">上传</a>
								</div>
								<div class="fjList">
									<ul class="fjListTop nobt">
										<li>
											<span class="enclo">已上传附件</span>
											<span class="enclooper">操作</span>
										</li>
									</ul>
									<ul class="fjListTop" id="attachList1"></ul>
								</div>
							</span>
						</td>
					</tr>

				</tbody>
			</table>
					<!-- 办理意见 -->
					<div style="padding-left: 0px;padding-right: 0px;display: none" class="table-wrap m-20-auto" id="suggestAreaDiv" >
						<table class="formTable">
							<tr>
								<th rowspan="2" nowrap="nowrap" style="width:140px;">办理意见</th>
								<td colspan="3">
									<div id="suggestListDiv" style="width:100%;"></div>
								</td>
							</tr>
						</table>
					</div>


		 <h2 class="panel-heading clearfix">单位信息</h2>
		<div class="table-wrap" id="companyTypeDiv">
			<table class="table table-striped tab_height" id="projectPlanTable">
				<thead>
				<tr>
					<th style="text-align: center">单位类型</th>
					<th style="text-align: center">单位名称</th>
					<th style="text-align: center">项目负责人</th>
					<th style="text-align: center">联系电话</th>

				</tr>

                <tr>

                    <td style="text-align: center">
						建设单位
						<input type="hidden" id="partakeType0" name="safetyUnitList[0].partakeType" style="width: 95%"   value="projectBuild">
						<input type="hidden" id="extNum0" name="safetyUnitList[0].extNum1" style="width: 95%"   value="0">
					</td>
					<td>
						<div id="companyBuildFormDiv"></div>
						<input type="hidden" id="unitName0" name="safetyUnitList[0].unitName" style="width: 95%"  >
					</td>
                    <td style="text-align: center">
       					<input type="text" id="projectLeader0" name="safetyUnitList[0].projectLeader" style="width: 95%"  >


                    </td>
					<td style="text-align: center">
						<input type="text" id="phone0" name="safetyUnitList[0].phone" style="width: 95%" >
                    </td>
                </tr>
				<tr>

					<td style="text-align: center">
						监管单位
						<input type="hidden" id="partakeType1" name="safetyUnitList[1].partakeType" style="width: 95%" value="projectSupervise" >
						<input type="hidden" id="extNum1" name="safetyUnitList[1].extNum1" style="width: 95%"   value="1">
					</td>
					<td>
						<div id="companySuperviseFormDiv"></div>
						<input type="hidden" id="unitName1" name="safetyUnitList[1].unitName" style="width: 95%"  >
					</td>

					<td style="text-align: center">
						<input type="text" id="projectLeader1" name="safetyUnitList[1].projectLeader" style="width: 95%" >

					</td>
					<td style="text-align: center">
						<input type="text" id="phone1" name="safetyUnitList[1].phone" style="width: 95%" >
					</td>
				</tr>

				<c:forEach items="${dictList}" var="companyType" varStatus="status">
					<tr>

						<td style="text-align: center">
							${companyType.value}
							<input type="hidden" id="partakeType${status.index+2}" name="safetyUnitList[${status.index+2}].partakeType" style="width: 95%" value="${companyType.code}" >
							<input type="hidden" id="extNum${status.index+2}" name="safetyUnitList[${status.index+2}].extNum1" style="width: 95%"   value="${status.index+2}">
						</td>
						<td>
							<div id="companyFormDiv${companyType.code}"></div>
							<input type="hidden" id="unitName${status.index+2}" name="safetyUnitList[${status.index+2}].unitName" style="width: 95%"  >
						</td>
						<td style="text-align: center">
							<input type="text" id="projectLeader${status.index+2}" name="safetyUnitList[${status.index+2}].projectLeader" style="width: 95%" >
						</td>
						<td style="text-align: center">
							<input type="text" id="phone${status.index+2}" name="safetyUnitList[${status.index+2}].phone" style="width: 95%" >

						</td>
					</tr>
				</c:forEach>


				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
			<div itemName="optionContainer">
				<table width="100%">
					<tr >
						<td class="w100"><span class='required'>*</span>办理意见</td>
						<td>
							<span class="workflowText">
							</span>									<workflow:suggest itemId="opinionCtrl" showLast="false" order="createTime" showWritePannel=" " classStr="" maxLength="500"/>

						</td>
			</tr>
				</table>
			</div>

		<div id="formFoorDiv" class="m-l-md" style="text-align: center;">
			<section id="workflowFormButton" class=" form-btn statistics-box"></section>
		</div>
    </form>
	</section>

</section>
<%@ include file="/WEB-INF/web/csmp/common/plugin/choose-project.jsp" %>
<%@ include file="/WEB-INF/web/include/workflowLayer.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision_form_bak.js?v=123' type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/csmp/safe/supervision/safetySupervision.validate.js?v=123' type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
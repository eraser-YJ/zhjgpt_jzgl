<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="id" name="id" />
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td><span class='required'>*</span>项目编号</td>
							<td>
								<input type="text" readonly id="projectNumber" name="projectNumber"/>
							</td>
							<td><span class='required'>*</span>项目名称</td>
							<td>
								<input type="text" readonly id="projectName" name="projectName"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>项目地址</td>
							<td>
								<input type="text" readonly id="projectAddress" name="projectAddress"/>
							</td>
							<td><span class='required'>*</span>国标行业(字典)</td>
							<td>
								<input type="text" readonly id="projectTrade" name="projectTrade"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>拟投金额</td>
							<td>
								<input type="text" readonly id="planAmount" name="planAmount"/>
							</td>
							<td><span class='required'>*</span>建筑面积</td>
							<td>
								<input type="text" readonly id="buildArea" name="buildArea"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>用地性质(字典)</td>
							<td>
								<input type="text" readonly id="landNature" name="landNature"/>
							</td>
							<td><span class='required'>*</span>用地面积</td>
							<td>
								<input type="text" readonly id="landArea" name="landArea"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>行政区</td>
							<td>
								<input type="text" readonly id="region" name="region"/>
							</td>
							<td><span class='required'>*</span>投资金额</td>
							<td>
								<input type="text" readonly id="investmentAmount" name="investmentAmount"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>拟开工时间</td>
							<td>
								<input type="text" readonly id="planStartDate" name="planStartDate">
							</td>
							<td><span class='required'>*</span>拟建成时间</td>
							<td>
								<input type="text" readonly id="planEndDate" name="planEndDate">
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>建设规模及内容</td>
							<td>
								<input type="text" readonly id="projectContent" name="projectContent"/>
							</td>
							<td><span class='required'>*</span>备注</td>
							<td>
								<input type="text" readonly id="remark" name="remark"/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>监管单位</td>
							<td>
								<input type="text" readonly id="superviseDeptId" name="superviseDeptId"/>
							</td>
							<td><span class='required'>*</span>建设单位</td>
							<td>
								<input type="text" readonly id="buildDeptId" name="buildDeptId"/>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/doc/project/projectInfoForm.js'></script>
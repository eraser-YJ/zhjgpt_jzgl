<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
	<div class="modal-dialog w900" style="width:1200px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="entityFormTitle"></h4>
			</div>
			<div class="modal-body">

				<form class="table-wrap form-table" id="entityForm">
					<input type="hidden" id="itemId" name="itemId" />
					<table class="table table-td-striped">
						<tbody>
						<tr>
							<td style="width:90px;"><span class='required'>*</span>项目类型</td>
							<td colspan="7">
								<input type="hidden" id="itemSelectType" name="itemSelectType" value=""/>
								<input type="hidden" id="itemSelectTypeName" name="itemSelectTypeName" value=""/>
								<input type="text" id="itemSelectTypeDisName" name="itemSelectTypeName" value="" readonly/>
							</td>

						</tr>
						<tr>
							<td><span class='required'>*</span>项目名称</td>
							<td colspan="3">
								<input type="text" id="projectName" name="projectName" value=""/>
							</td>
							<td><span class='required'>*</span>责任部门</td>
							<td >
								<input type="text" id="dutyCompany" name="dutyCompany" value=""/>
							</td>
							<td><span class='required'>*</span>责任人</td>
							<td >
								<input type="text" id="dutyPerson" name="dutyPerson" value=""/>
							</td>
						</tr>

						<tr>
							<td><span class='required'>*</span>总投资<br>（万元）</td>
							<td>
								<input type="text" id="projectTotalInvest" name="projectTotalInvest"  value=""/>
							</td>
							<td><span class='required'>*</span>累计已完成投资<br>（万元）</td>
							<td>
								<input type="text" id="projectUsedInvest" name="projectUsedInvest"  value=""/>
							</td>
							<td><span class='required'>*</span>年度计划投资<br>（万元）</td>
							<td>
								<input type="text" id="projectNowInvest" name="projectNowInvest"  value=""/>
							</td>
							<td><span class='required'>*</span>以后投资（万元）</td>
							<td>
								<input type="text" id="projectAfterInvest" name="projectAfterInvest"  value=""/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>内容及规模</td>
							<td colspan="5">
								<input type="text" id="projectNowDesc" name="projectNowDesc" value=""/>
							</td>
							<td><span class='required'>*</span>工期（天）</td>
							<td>
								<input type="text" id="projectTotalDay" name="projectTotalDay" value="">
							</td>
						</tr>
						<tr>
							<td>实施必要性</td>
							<td colspan="7">
								<input type="text" id="projectDesc" name="projectDesc" value=""/>
							</td>

						</tr>
						<tr>
							<td>备注</td>
							<td colspan="7">
								<input type="text" id="remark" name="remark" value=""/>
								<span style="font-size:8px;color: red">（PPP项目标注为新型城镇化或其他PPP项目）</span>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveBtn">保 存</button>
				<button class="btn" type="button" id="closeBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlanItemForm.js'></script>
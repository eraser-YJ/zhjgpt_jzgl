<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<style>
	.delete{
		width:20px;
		height:20px;
		border-radius:60%;
		position:absolute;
		top:-10px;
		right:-10px;
	}
</style>
<div class="modal fade panel" id="plan-month-form-modal" aria-hidden="false">
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
							<td colspan="5">
								<input type="hidden" id="itemSelectType" name="itemSelectType" value=""/>
								<input type="hidden" id="extStr5" name="extStr5" value=""/>
								<input type="hidden" id="itemSelectTypeName" name="itemSelectTypeName" value=""/>
								<input type="text" id="itemSelectTypeDisName" name="itemSelectTypeName" value="" readonly/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>项目编码</td>
							<td colspan="5">
								<input type="text" id="projectId" name="projectId" value="" style = "width:90%"/>
								&nbsp;<a href="#"  class="btn dark" onclick="projectMonthPlanItemJsForm.projectBtnFun()" >选择</a>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>项目名称</td>
							<td colspan="5">
								<input type="text" id="projectName" name="projectName" value="" readonly/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>总投资<br>（万元）</td>
							<td>
								<input type="text" id="projectTotalInvest" name="projectTotalInvest"  value="" readonly/>
							</td>
							<td><span class='required'>*</span>累计已完成投资<br>（万元）</td>
							<td>
								<input type="text" id="projectUsedInvest" name="projectUsedInvest"  value="" readonly/>
							</td>
							<td><span class='required'>*</span>年度计划投资<br>（万元）</td>
							<td>
								<input type="text" id="projectNowInvest" name="projectNowInvest"  value="" readonly/>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>形象进度</td>
							<td colspan="5">
								<textarea id="xxjd" name="xxjd" rows="3"></textarea>
								<input type="hidden" id="xxjdAttchList" name="xxjdAttchList" value=""/>
								<div id="xxjdAttchListDiv"></div>
							</td>
						</tr>
						<tr>
							<td><span class='required'>*</span>本月计划投资<br>（万元）</td>
							<td>
								<input type="text" id="projectMonthPlanInvest" name="projectMonthPlanInvest"  value=""/>
							</td>
							<td><span class='required'>*</span>实际完成投资<br>（万元）</td>
							<td>
								<input type="text" id="projectMonthActInvest" name="projectMonthActInvest"  value=""/>
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td>存在问题</td>
							<td colspan="5">
								<textarea id="solveProblem" name="solveProblem" rows="4"></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="10" style="text-align:center">
								<span><span class='required'>*</span>前期手续完成情况（是或否）</span>
							</td>
						</tr>
						<tr>
							<td>土地证</td>
							<td>
								<select id="tudiCard" name="tudiCard"  value="">
									<option value="N">否</option>
									<option value="Y">是</option>
								</select>
							</td>
							<td>用地规划许可证</td>
							<td>
								<select id="ydghxkCard" name="ydghxkCard"  value="">
									<option value="N">否</option>
									<option value="Y">是</option>
								</select>
							</td>
							<td>工程规划许可证</td>
							<td>
								<select id="gcghxkCard" name="gcghxkCard"  value="">
									<option value="N">否</option>
									<option value="Y">是</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>开工许可证</td>
							<td>
								<select id="kgxkCard" name="kgxkCard"  value="">
									<option value="N">否</option>
									<option value="Y">是</option>
								</select>
							</td>
							<td>项目选址意见书</td>
							<td>
								<select id="xmxzyjs" name="xmxzyjs"  value="">
									<option value="N">否</option>
									<option value="Y">是</option>
								</select>
							</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td><span class='required'>*</span>责任部门</td>
							<td colspan="3">
								<input type="text" id="dutyCompany" name="dutyCompany" value=""/>
							</td>
							<td><span class='required'>*</span>责任人</td>
							<td >
								<input type="text" id="dutyPerson" name="dutyPerson" value=""/>
							</td>
						</tr>
						<tr>
							<td>备注</td>
							<td colspan="5">
								<textarea id="remark" name="remark" rows="3"></textarea>
							</td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn" style="text-align: center; width: 100%;">
				<button class="btn dark" type="button" id="saveMonthItemBtn">保 存</button>
				<button class="btn" type="button" id="closeMonthItemBtn">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/month/monthItemForm.js'></script>
<script>
	$(window).ready(function(){
		$('.delet-image').mouseover(function () {
			$('.delet-image-remove').css('display','block')
		});
		$('.delet-image').mouseout(function () {
			$('.delet-image-remove').css('display','none')
		});

		$('.fa-remove-sign').click(function () {
			$('.delet-image').css('display','none')
		});
	});
</script>
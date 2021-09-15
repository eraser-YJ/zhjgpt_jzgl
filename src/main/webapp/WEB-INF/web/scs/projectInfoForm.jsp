<%@ page language="java" pageEncoding="UTF-8" %>

<form class="table-wrap form-table" id="entityProjectInfoForm">
	<table class="table table-td-striped">
		<tbody>
		<tr>
			<td style="width:150px;">工程编号</td>
			<td>
				<input type="text" readonly id="projectNumber" name="projectNumber"/>
			</td>
			<td style="width:150px;">工程类型</td>
			<td>
				<input type="text" readonly id="projectTypeValue" name="projectTypeValue"/>
			</td>
		</tr>
		<tr>
			<td  style="width:150px;">工程名称</td>
			<td colspan="3">
				<input type="text" readonly id="projectName" name="projectName"/>
			</td>
		</tr>
		<tr>
			<td>工程地址</td>
			<td  >
				<input type="text" readonly id="projectAddress" name="projectAddress"/>
			</td>
			<td>建设单位</td>
			<td  >
				<input type="text" readonly id="buildDept" name="buildDept"/>
			</td>
		</tr>
		<tr>
			<td>用地面积</td>
			<td>
				<input type="text" readonly id="buildArea" name="buildArea"/>
			</td>

			<td>投资金额</td>
			<td>
				<input type="text" readonly id="projectMoney" name="projectMoney"/>
			</td>
		</tr>
		<tr>
			<td>实际开工日期</td>
			<td>
				<input type="text" readonly id="realStartDate" name="realStartDate"/>
			</td>
			<td>实际竣工日期</td>
			<td>
				<input type="text" readonly id="realFinishDate" name="realFinishDate"/>
			</td>
		</tr>
		 </tbody>
	</table>
</form>
<script src='${sysPath}/js/com/jc/scs/projectInfoForm.js'></script>
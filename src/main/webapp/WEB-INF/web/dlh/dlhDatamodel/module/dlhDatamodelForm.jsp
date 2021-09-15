<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">数据模型</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhDatamodelForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td class="w100">
								<span class='required'>*</span>表名
							</td>
							<td class="w300">
								<input type="text" id="tableCode" name = "tableCode" value="" placeholder="必填">
					 
							</td>
							<td class="w100">
								<span class='required'>*</span>数据源编码
							</td>
							<td class="w300">
								<input type="text" id="dbSource" name = "dbSource" value="" placeholder="必填">					 
							</td>
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>描述
							</td>
							<td class="w300" colspan="3">
								<input type="text" id="tableName" name = "tableName" value="" placeholder="必填">					 
							</td>
						</tr>
						<tr >
							<td class="w100">
								备注
							</td>
							<td class="w300" colspan="3">
								<input type="text" id="remark" name = "remark" value="">					 
							</td>  
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveAndClose">保 存</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/dlh/dlhDatamodel/module/dlhDatamodelForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/dlh/dlhDatamodel/dlhDatamodel.validate.js" type="text/javascript"></script>

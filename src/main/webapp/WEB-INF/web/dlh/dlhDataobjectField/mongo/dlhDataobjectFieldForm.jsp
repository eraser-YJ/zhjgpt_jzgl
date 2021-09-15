<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">数据对象属性</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhDataobjectFieldForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					
		        <input type="hidden" id="modelId" name = "modelId" value="${modelId}">
		        <input type="hidden" id="objectId" name = "objectId" value="${objectId}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td class="w100">
								<span class='required'>*</span>属性编码
							</td>
							<td class="w300">
								<input type="text" id="fieldCode" name = "fieldCode" value="" placeholder="必填">					 
							</td>
							<td class="w100">
								<span class='required'>*</span>列名
							</td>
							<td class="w300">
								<input type="hidden" id="itemId" name = "itemId"  >		
								<input type="text" id="itemName" name = "itemName"  style="width:80%" readonly>
								<input type="button" id="dlhDataobjectFieldModuleShowSel" onclick="dlhDataobjectFieldModule.showItem()" value="选择">				 
							</td>
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>属性名
							</td>
							<td class="w300"  >
								<input type="text" id="fieldName" name = "fieldName" value="" placeholder="必填">					 
							</td>							 
						 
							<td class="w100">
								字典编码
							</td>
							<td class="w300"  >
								<input type="text" id="itemDicCode" name = "itemDicCode" value="" readonly>					 
							</td>							 
						</tr>
						<tr >
						<td class="w100">
								列表显示
							</td>
							<td class="w300">
								<input type="text" id="fieldListShow" name = "fieldListShow" value="">					 
							</td>
							<td class="w100">
								<span class='required'>*</span>排序
							</td>
							<td class="w300">
								<input type="text" id="fieldSeq" name = "fieldSeq" value="" placeholder="必填">				 
							</td>							
						</tr>						
						<tr >
							<td class="w100">
								检查
							</td>
							<td class="w300" colspan="3">
								<input type="text" id="fieldCheck" name = "fieldCheck" value="" style="width:92%" readonly>	
								<input type="button" id="dlhDataobjectFieldCheckRuleSel" onclick="dlhDataobjectFieldModule.checkRuleSel()" value="选择">				 
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
<div id="dlhDataobjectFieldModuleSelDiv"></div>
<div id="CheckRuleDiv">
	<%@ include file="/WEB-INF/web/dlh/dlhDataobjectField/include/dlhCheckRule.jsp"%>
</div>
<script src="${sysPath}/js/com/dlh/dlhDataobjectField/mongo/dlhDataobjectFieldForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/dlh/dlhDataobjectField/mongo/dlhDataobjectField.validate.js" type="text/javascript"></script>

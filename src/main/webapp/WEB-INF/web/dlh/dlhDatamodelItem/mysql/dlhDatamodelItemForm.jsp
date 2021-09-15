<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">数据模型属性</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhDatamodelItemForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="modelId" name = "modelId" value="">
					<table class="table table-td-striped">
						<tbody>
						<tr >						 
							<td class="w100">
								<span class='required'>*</span>列名
							</td>
							<td class="w300">
								<input type="text" id="itemName" name = "itemName" value="" placeholder="必填">					 
							</td>
							<td class="w100">
								是否主键
							</td>
							<td class="w300">
								<select id="itemKey" name = "itemKey" >
									<option value="N">N</option>
									<option value="Y">Y</option>							
								</select>		 
							</td>							
						</tr>
						<tr >						
							<td class="w100">
								<span class='required'>*</span>列描述
							</td>
							<td class="w300" colspan="3">
								<input type="text" id="itemComment" name = "itemComment" value="" placeholder="必填">					 
							</td>
							
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>列类型
							</td>
							<td class="w300">
								<dicex:typeDic name="itemType" id="itemType"  headName="-请选择-"  headValue=""/>	
							</td>
							<td class="w100">
								列长度
							</td>
							<td class="w300">
								<input type="text" id="itemLen" name = "itemLen" value="">					 
							</td>							
						</tr>
						<tr >							
							<td class="w100">字典编码</td>
							<td class="w300">
								<input type="text" id="dicCode" name = "dicCode" value="">					 
							</td>
							<td class="w100">
								<span class='required'>*</span>排序
							</td>
							<td class="w300">
								<input type="text" id="itemSeq" name = "itemSeq" value="">					 
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

<script src="${sysPath}/js/com/dlh/dlhDatamodelItem/mysql/dlhDatamodelItemForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/dlh/dlhDatamodelItem/mysql/dlhDatamodelItem.validate.js" type="text/javascript"></script>

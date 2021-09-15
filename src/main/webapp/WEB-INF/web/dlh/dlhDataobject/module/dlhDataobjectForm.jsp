<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">数据对象</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="dlhDataobjectForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
						<tr >							
							<td class="w100">
								<span class='required'>*</span>地址
							</td>
							<td class="w300">
								<input type="text" id="objUrl" name = "objUrl" value="" placeholder="必填">					 
							</td>
							<td class="w100">
								<span class='required'>*</span>模型编码
							</td>
							<td class="w300">
								<input type="text" id="modelId" name = "modelId" value="" placeholder="必填">					 
							</td>
						</tr>
						<tr >
							<td class="w100">
								<span class='required'>*</span>说明
							</td>
							<td class="w300" colspan="3">
								<input type="text" id="objName" name = "objName" value="" placeholder="必填">					 
							</td>
							
						</tr>
						<tr >
							<td class="w100">
								描述
							</td>
							<td class="w300" colspan="3">
								<textarea type="text" id="remark" name = "remark"  rows="5"></textarea>			 
							</td>
							
						</tr>
<!-- 						<tr > -->
<!-- 							<td class="w100"> -->
<!-- 								列表样式 -->
<!-- 							</td> -->
<!-- 							<td class="w300"> -->
<!-- 								<input type="text" id="disListStyle" name = "disListStyle" value=""> -->
					 
<!-- 							</td> -->
<!-- 							<td class="w100"> -->
<!-- 								表单样式 -->
<!-- 							</td> -->
<!-- 							<td class="w300"> -->
<!-- 								<input type="text" id="disFormStyle" name = "disFormStyle" value="">					  -->
<!-- 							</td>							  -->
<!-- 						</tr> -->
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

<script src="${sysPath}/js/com/dlh/dlhDataobject/module/dlhDataobjectForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/dlh/dlhDataobject/dlhDataobject.validate.js" type="text/javascript"></script>

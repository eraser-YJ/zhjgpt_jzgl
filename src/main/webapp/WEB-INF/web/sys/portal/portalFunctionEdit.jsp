<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titlepFunction">门户功能组件管理</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="pFunctionForm">
				<input type="hidden" id="id" name="id">
				<input type="hidden" id="token" name="token" value="${data.token}">
				<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width:30%;"><span class="required">*</span>功能名称</td>
								<td><input type="text" id="funName" name = "funName" ></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>功能编码</td>
								<td><input type="text" id="funCode" name = "funCode" ></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>功能链接URL</td>
								<td><input type="text" id="funUrl" name = "funUrl" ></td>
							</tr>
							<tr>  
								<td><span class="required"></span>参数类型</td>
								<td><lable>正常参数</lable>
									<!-- <select name="funParametertype" id="funParametertype" > onchange="pFunction.changeparameter(this.value)"
										<option value="">-请选择-</option>
										<option value="1">正常参数</option>
										<option value="2">信息管理栏目参数</option>
									</select> -->
									<input type="hidden" id="funParametertype" name = "funParametertype" value="1">
								</td>
							</tr>
							<tr>  
								<td><span class="required"></span>链接参数</td>
								<td><div id="loadUrlparameter">
								<input type="text" id="funUrlparameter" name = "funUrlparameter" >
								<input type="hidden" id="funUrlParametername" name = "funUrlParametername" >
								</div></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>链接更多URL</td>
								<td><input type="text" id="funUrlmore" name = "funUrlmore" ></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>显示行数</td>
								<td><input type="text" id="funRows" name = "funRows" ></td>
							</tr>
							<%-- <tr>  
								<td>链接方式</td>
								<td><dic:select name="lineType" id="lineType" dictName="plineType" /></td>
							</tr> --%>
							<tr>  
								<td><span class="required">*</span>功能视图类型</td>
								<td><dic:select name="viewType" id="viewType" dictName="pviewType" parentCode="portalFunction"
									defaultValue="" headName="-请选择-" headValue="" /></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="pFunctionbtn">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/portal/portalFunctionEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/portalFunction.validate.js" type="text/javascript"></script>

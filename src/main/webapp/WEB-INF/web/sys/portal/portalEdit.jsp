<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn"
					data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleportal">门户管理</h4>
			</div>
			
			<div class="modal-body">
				<form class="table-wrap form-table" id="portalForm">
					<input type="hidden" id="id" name="id"> 					
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="token" name="token" value="${data.token}">  
					<input type="hidden" id="portalmenuId" name="portalmenuId" value="1">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width:30%;"><span class="required">*</span>门户名称</td>
								<td><input type="text" id="portalName" name="portalName"></td>
							</tr>
							<tr>
								<td><span class="required">*</span>门户状态</td>
								<td><dic:select name="portalStatus" id="portalStatus"
										dictName="portal_status" parentCode="portal" defaultValue="" headName="-请选择-"
										headValue="" /></td>
							</tr>
							<tr>
								<td><span class="required">*</span>门户类型</td>
								<td><dic:select name="portalType" id="portalType" dictName="portal_type"  
										parentCode="portal" defaultValue="" headName="-请选择-" headValue="" /></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>排序号</td>
								<td><input type="text" id="sequence" maxlength="3" name="sequence"></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="portalbtn">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/portal/portalEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/portal/portal.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closebtn" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleuserIp">IP绑定管理</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="userIpForm">
				<input type="hidden" id="id" name="id">
				<input type="hidden" id="token" name="token" value="${data.token}">
				<input type="hidden" id="modifyDate" name="modifyDate">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width:30%;"><span class="required">*</span>绑定用户</td>
								<td><div id="userFormTree"></div></td>
							</tr>
							<tr>  
								<td>绑定类型
								</td>
								<td>
									<label class="radio inline">
										<input type="radio" name="bindType" checked id="zdip" onclick="userIpEdit.ipclear()" value="1"/> 指定IP
									</label>
									<label class="radio inline">
										<input type="radio" name="bindType" onclick="userIpEdit.ipclear()" value="2"/> IP段
									</label>
								</td>
							</tr>
							<tr>  
								<td><span class="required">*</span>绑定IP</td>
								<td><div class="input-group w-p100" id="ipz">
									<input type="text" id="StartIp1" name="userStartIp" />
									</div>
									<div class="input-group w-p100" style="display:none" id="userIpDiv">
										<input type="text" id="StartIp2" name="userStartIp" /><div class="input-group-btn w30"> -</div><input type="text" id="EndIp" name="userEndIp" />
									</div>
								</td>
							</tr>
							<tr>  
								<td>描述</td>
								<td><textarea rows="2" id="userIpDesc" name="userIpDesc" placeholder="备注..."></textarea></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="userIpbtn">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/userIp/userIpEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/userIp/userIp.validate.js" type="text/javascript"></script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="myModal-share" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" 
					data-dismiss="modal" >×</button>
				<h4 class="modal-title" id="titleportal">共享范围</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="portalShareForm">
					<input type="hidden" id="portalId" name="portalId"/>
					<input type="hidden" id="tokens" name="tokens" value="${datas.token}">
					<table class="table table-td-striped">
						<tbody>
							<!-- <tr>
								<td style="width:30%;"><span class="required">*</span>类型选择</td>
								<td>
								<select id="sharetype">
										<option value="user">人员</option>
										<option value="role">角色</option>
										<option value="dept">部门</option>
										<option value="organ">机构</option>
								</select><button class="btn dark" type="button"  id="portalbtn">新增</button>
									<a class="btn dark fr" type="button" href="#myModal-share-add"
									role="button" data-toggle="modal">新 增</a> 
								</td>
							</tr> -->
							<tr>
							<td style="width: 80px">用户</td>
								<td>
									<div id="UserResultDiv" ></div>
								</td>
							</tr>
							<%--<tr>
								<td>角色</td>
								<td>
									<div id="RoleResultDiv" ></div>
								</td>
							</tr>--%>
							<tr>
								<td>部门</td>
								<td>
									<div id="DeptResultDiv" ></div>
								</td>
							</tr>
							<%--<tr>
								<td>机构</td>
								<td>
									<div id="OrganResultDiv" ></div>
								</td>
							</tr>--%>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="portalsharebtn" data-dismiss="modal">保 存</button>
				<button class="btn" type="button" onclick="$('#myModal-share').modal('hide');">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/portal/rolePortalEdit.js" type="text/javascript"></script>

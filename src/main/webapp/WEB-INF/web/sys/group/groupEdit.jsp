<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="subTitle">新增</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="groupForm">
				<input type="hidden" id="id" name="id">
				<input type="hidden" id="token" name="token" value="${token}">
				<input type="hidden" id="modifyDate" name="modifyDate">
				<input type="hidden" id="membersId" name="membersId">
				<input type="hidden" id="userJson" name="userJson">
					<table class="table table-td-striped">
						<tbody>
							<tr>
								<td style="width:18%;"><span class="required">*</span>组名称</td>
								<td><div><input type="text" name="name" id="name"/></div></td>
							</tr>
							<tr>  
								<td><span class="required">*</span>组成员</td>
								<td><div id="groupMember"></div></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="groupBtn">保 存</button>
				<button type="button" class="btn" data-dismiss="modal">关 闭</button>
			</div>
		</div>
	</div>
</div>
<script src="${sysPath}/js/com/sys/group/groupEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/group/group.validate.js" type="text/javascript"></script>
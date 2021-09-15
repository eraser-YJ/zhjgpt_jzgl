<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="titleSubUserId">用户表（公共）</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="subUserForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
					<input type="hidden" id="code" name = "code" value="">
					<input type="hidden" id="sex" name = "sex" value="" >
					<input type="hidden" id="deptId" name = "deptId" value="">
					<table class="table table-td-striped">
						<tbody>
						<tr >
							<td class="w100">
								<span class="required">*</span>姓名
							</td>
							<td class="w300">
								<div id="displayNameFormDiv"></div>
							</td>
							<td class="w100">
								用户职务
							</td>
							<td class="w300">
								<input type="text" id="dutyId" name = "dutyId" value="">
							</td>
						</tr>
						<tr >
							<td class="w100">
								直接领导
							</td>
							<td class="w300">
								<div id="leaderIdFormDiv"></div>
							</td>
							<td><span class="required">*</span>序号</td>
							<td><input type="text" id="orderNo" name = "orderNo"/></td>
						</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/subUser/module/subUserForm.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/subUser/subUser.validate.js" type="text/javascript"></script>

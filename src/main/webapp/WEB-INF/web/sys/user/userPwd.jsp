<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<script src="${sysPath}/js/com/sys/user/userPwd.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userPwd.validate.js" type="text/javascript"></script>
<section class="panel m-t-md search-box clearfix" style="margin-top: 0px;">
	<form class="table-wrap form-table" id="userPwdForm">
		<input type="hidden" id="token" name="token" value="${token}">
		<input type="hidden" id="pwdType" name="pwdType" value="${pwdType}">
		<table class="table table-td-striped">
			<tbody>
				<tr>
					<td class="w240"><span class="required">*</span>旧密码</td>
					<td><input type="password" id="password" name="password" style="width:30%;"/></td>
				</tr>
				<tr>
					<td><span class="required">*</span>新密码</td>
					<td><input type="password" id="newPassword" name="newPassword" style="width:30%;"/></td>
				</tr>
				<tr>
					<td><span class="required">*</span>确认密码</td>
			  		<td><input  type="password" id="confirmPassword" name = "confirmPassword" style="width:30%;"/></td>
				</tr>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark ok" type="button" id="saveBtn">保 存</button>
		</section>
	</form>
</section>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
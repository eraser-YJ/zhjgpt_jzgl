<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="dic" uri="/dic-tags" %>
<%@ taglib prefix="c" uri="c"%>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>

<c:if test="${sysPath=='/'}">
	<c:set var="sysPath" value="" />
</c:if>
<c:set var="errorMessage" scope="page" value="${errorMessage}"/>
<c:set var="timeout" scope="page" value="${timeout}"/>
<!DOCTYPE html>

<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8">
	<meta charset="utf-8">
	<title>长春新区智慧建管平台</title>
	<link rel="stylesheet" type="text/css" href="css/login/css/iconfont.css">
	<link rel="shortcut icon" href="${sysPath }/favicon.ico" type="image/x-icon" />

	<link href="${sysPath }/css/login/yixun_login.css" rel="stylesheet" type="text/css" />
	<style>.fa, .table-sort:after {display: inline-block;font-family: YixunFont;font-style: normal;font-weight: normal;line-height: 1;-webkit-font-smoothing: antialiased;-moz-osx-font-smoothing: grayscale;}</style>
	<script type="text/javascript" src='${sysPath}/js/lib/app/jquery.min.js'></script>
	<script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery.cookie.js'></script>
	<script type="text/javascript" src="${sysPath}/js/jbox/jbox.min.js"></script>
	<script type='text/javascript' src='${sysPath}/js/lib/common/common.all.js'></script>
	<script type="text/javascript">setRootPath('${sysPath}');</script>
	<script type="text/javascript" src='${sysPath}/js/lib/jquery-validation/jquery.validate.all.js?v=b58d900620'></script>
	<script type="text/javascript" src="${sysPath}/js/lib/app/app.v2.min.js"></script>
	<script type="text/javascript" src="${sysPath}/js/com/common/Safety.js" ></script>
	<script>
		var errorMessage = "${errorMessage}";
		var timeout = "${timeout}";
		var kickoutMes = ${!empty sessionScope.kickoutMes};

	</script>
	<script src="${sysPath }/js/com/sys/login/login.js"></script>
</head>
<jsp:include page="/WEB-INF/web/sys/login-${loginBody}.jsp"/>
<script type="text/javascript">
	// 如果在框架中，则跳转刷新上级页面
	if(self.frameElement && self.frameElement.tagName=="IFRAME"){
		parent.location.reload();
	}

</script>
<!--start 找回密码-->
<div class="modal fade panel" id="myModal" aria-hidden="false">
	<div class="modal-dialog" style="width:500px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">密码找回</h4>
			</div>
			<div class="modal-body">
				<form id="forgetPwdForm" class="form-table">
					<table class="loginModal table table-td-striped">
						<tr>
							<td><span class="required">*</span>登录名</td>
							<td><input type="text" name="loginName" id="loginName"></td>
						</tr>
						<tr>
							<td><span class="required">*</span>密码提示问题</td>
							<td>
								<dic:select name="question" id="question" dictName="question" parentCode="user" headName="-请选择-" headValue="" defaultValue=""/>
							</td>
						</tr>
						<tr>
							<td><span class="required">*</span>密码提示答案</td>
							<td><input type="text" name="answer" id="answer"></td>
						</tr>
					</table>
				</form>
				<div></div>
			</div>

			<div class="modal-footer form-btn">
				<a class="btn dark" type="button" onClick="javascript:(forgetSubmit())">确 定</a>
				<a class="btn" type="reset" data-dismiss="modal">取 消</a>
			</div>
		</div>
	</div>
</div>
<!--end 找回密码-->
</html>
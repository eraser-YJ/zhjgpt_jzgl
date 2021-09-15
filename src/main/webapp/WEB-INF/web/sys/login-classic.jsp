<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8,chrome=1">
	<title>长春新区智慧建管平台</title>
	<link rel="stylesheet" type="text/css" href="${sysPath}/css/login/css/iconfont.css">
	<link rel="stylesheet" type="text/css" href="${sysPath}/css/login/css/login-classic.css">
	<style type="text/css">
<!--
.thumbnail{position:relative;z-index:0;}
.thumbnail:hover{background-color:transparent;z-index:50;}
.thumbnail span{position:absolute;;left:-1000px;border:0px dashed gray;visibility:hidden;color:#000;text-decoration:none;padding:5px;}
.thumbnail span img{border-width:0;padding:2px;}
.thumbnail:hover span{visibility:visible;top:80px;left:-110px;}

-->
</style>
</head>
<body class="big">
<header style="width:100%;height:80px; line-height:80px; background: #1572e8;">
	<div style="width:1200px; margin:0 auto;">
		<h1 style="float: left;">
			<img src="${sysPath}/css/login/images/logo9.png" style="margin-top:28px; width:450px;"/>
		</h1>
		<span style="display: block;width: 1px; height: 25px;background: #448eed;float: left; margin:30px 30px;"></span>
		<p style="float: left; color: #fff;font-size: 18px; line-height: 80px;">登录</p>

		<div style="position:fixed;right:40px;text-align:center;display:flex;flex;flex-direction: column;height: 80px;align-items: center;"><a class="thumbnail" href="${sysPath}/csmp/ptProject/download.action" download="project.apk" style="color:#fff;font-size: 14px;">建设管理APP点击下载</a></div>
<%--		<span><img src="${sysPath}/css/login/images/app.png" /></span>--%>
		
	</div>
	
</header>
<div id="content">
	<div class="info">
		<div class="title">
			<div class="logo"></div>
		</div>
		<form id="loginForm" action="" method="post">
			<div id="form" class="form">
				<h3>请登录</h3>
				<div class="userInfo">
					<input type="text" id="username" name="username" autocomplete="off" oncopy=”return false” onpaste=”return false” oncut=”return false” placeholder="用户名"/>
					<label for="username">
						<i class="iconfont icon-user"></i>
					</label>
				</div>
				<div class="userInfo">
					<input typecompanyProjectsSgxkList="password" id="password" name="password" type="password" autocomplete="off" oncontextmenu="return false" oncopy=”return false” onpaste=”return false” oncut=”return false” placeholder="密 码"/>
					<label for="password">
						<i class="iconfont icon-kaisuo"></i>
					</label>
				</div>
				<input type="hidden" id="valCaptcha" value="110">
				<%--<div class="remember">
                    <input type="text" id="valCaptcha" placeholder="验证码" autocomplete="off" style="border:2px solid #d1d1d1;height: 26px;">
					<label class="label">
						<img id="captcha_img" alt="点击更换" title="点击更换"
							 onclick="changeVerifyCode()" />
					</label>
				</div>--%>
				<div id="errormsg" class="error" style="display:none"></div>
				<%--<div class="remember">
					<label class="label">
						<input id="remember" type="checkbox" tabindex="4">记住用户名
					</label>
					<a id="showForget">找回密码</a>
				</div>--%>
			</div>
			<div class="userInfo submit">
				<a id="loginBtn" href="#">登录</a>
			</div>
		</form>
	</div>
</div>
<%--<footer style="border-top:1px solid #dddddd; width: 100%; line-height:70px;text-align: center; color:#999999;font-size: 14px;position: fixed; bottom:0;background: #fff;">--%>
	<%--Copyright © 2019 ChangChun YiXun Information Technology CO.,LTD.All Rights Reserved.&nbsp;&nbsp;长春奕迅建筑智能化工程有限公司版权所有--%>
<%--</footer>--%>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		
		var date = new Date();
		var seperator1 = "-";
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var a = new Array("日", "一", "二", "三", "四", "五", "六");  
var week = new Date().getDay();  
var str = "星期"+ a[week]; 
		var currentdate = year + seperator1 + month + seperator1 + strDate;
		document.getElementById('newDate').innerHTML = currentdate + "          " +str;
	});
</script>
</html>

	

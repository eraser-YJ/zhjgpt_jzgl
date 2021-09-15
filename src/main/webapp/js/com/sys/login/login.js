
$(document).ready(function(){
	// 获取窗口高度
	var winHeight = 0;
	if (window.innerHeight){
		winHeight = window.innerHeight;
	}else if ((document.body) && (document.body.clientHeight)){
		winHeight = document.body.clientHeight;
	}
	if(winHeight){
		$("#login_bg").css({
			'position':'absolute',
			'width'  : '100%',
			'height' : winHeight+'px',
			'z-index': -1
		});
	}
	loginInit();
	getCookie();
	/*forgetValidateRule();*/
	$("#showForget").click(showForget);
});


function loginFormSubmit(){
	/*flushLoginTicket();*/
	if(!$("#username").val()){
		$("#errormsg").html("用户名不能为空");
		$("#errormsg").show();
		return;
	}
	if(!$("#password").val()){
		$("#errormsg").html("密码不能为空");
		$("#errormsg").show();
		return;
	}
	if($("#password").val().length < 4){
		$("#errormsg").html("密码不能少于4个字符");
		$("#errormsg").show();
		return;
	}

    if(!$("#valCaptcha").val()){
        $("#errormsg").html("验证码不能为空");
        $("#errormsg").show();
        return;
    }

    var username = $("#username").val();
    var pwd = $("#password").val();
    var captcha = $("#valCaptcha").val();

    // 获取
    jQuery.ajax({
        type : 'POST',
        url:getRootPath()+"/rsaKey.action",
        data : {"username": username,"captcha":captcha},
        dataType : "json",
        success:function(data){
            if(data.valResule == "true"){
                var pwdKey = new RSAUtils.getKeyPair(data.exponent,"",data.modulus);
                var reversedPwd = pwd.split("").reverse().join("");
                var encrypedPwd = RSAUtils.encryptedString(pwdKey,reversedPwd);
                $("#password").val(encrypedPwd);
                $('#loginForm').submit();
                setCookie();
                return false;
            } else {
                setCookie();
                $("#errormsg").html("验证码错误,请重新输入");
                $("#errormsg").show();
                return false;
            }

        },
        error: function(result, status, xhr) {
            setCookie();
            return false;
        }
    });
}

function setCookie(){
    /*if($("#username").val()){
        localStorage.setItem("username",$("#username").val());
    }
    if($("#remember").is(":checked")){
        $.cookie("username", $("#username").val(), { expires: 3000 });
    } else {
        $.cookie("username", '');
    }*/
}

function getCookie(){
	/*var username = $.cookie("username");
	if(username){
		$("#username").val(username);
		$("input[id='remember']").attr("checked","checked");
	}
	localStorage.removeItem("username");*/
}

function loginInit(){
	$("#loginForm").prop("action",getRootPath()+"/login");
	if(errorMessage != "") {
		$("#errormsg").html(errorMessage);
		$("#errormsg").show();
	}else if(kickoutMes == true){
		$("#errormsg").html("您的账号已在其他客户端登录，如非本人操作，请及时修改密码或联系管理员");
		$("#errormsg").show();
	}else if(timeout != ""){
		$("#errormsg").html("系统已超时，请重新登录");
		$("#errormsg").show();
	}else{
		$("#errormsg").hide();
	}
	var $p = $("#loginBtn");
	$p.bind('click', function (e) {
		e.preventDefault();
		loginFormSubmit();
	});
	var $p = $("#password");
	$p.bind('keydown', function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			loginFormSubmit();
		}
	});
	var $u = $("#username");
	$u.bind('keydown', function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			loginFormSubmit();
		}
	});
    var $v = $("#valCaptcha");
    $v.bind('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            loginFormSubmit();
        }
    });
}

function forgetSubmit(){
	if (forgetValidateRule()) {
		var url = getRootPath()+"/sys/user/forgetPwd.action";
		var formData = $("#forgetPwdForm").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : formData,
			success : function(data) {
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					$('#myModal').modal('hide');
				} else {
					msgBox.info({
						content: data.errorMessage
					});
				}
			},
			error : function() {
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		$("#loginError").html("");
	}
}

function forgetValidateRule(){
	if($("#loginName").val() == '' ){
		msgBox.info({
			content: "登录名不能为空"
		});
		return false;
	}
	if($("#question").val() == '' ){
		msgBox.info({
			content: "提示问题不能为空"
		});
		return false;
	}
	if($("#answer").val() == '' ){
		msgBox.info({
			content: "提示答案不能为空"
		});
		return false;
	}
	return true;
}


function showForget(){
	forgetFormClear();
	//hideErrorMessage();
	$('#myModal').modal('show');
}

/*function showForget(){
	forgetFormClear();
	//hideErrorMessage();
	$('#myModal').modal('show');
	var url = getRootPath()+"/sys/user/forgetPwd.action";
	var formData = $("#forgetPwdForm").serializeArray();
	jQuery.ajax({
		url : url,
		type : 'POST',
		data : formData,
		success : function(data) {
			if(data.success == "true"){
				msgBox.tip({
					type:"success",
					content: data.successMessage
				});
				$('#myModal').modal('hide');
			} else {
				msgBox.info({
					content: data.errorMessage
				});
			}
		},
		error : function() {
			msgBox.tip({
				content: $.i18n.prop("JC_SYS_002")
			});
		}
	});
}*/

function forgetFormClear(){
	$('#forgetPwdForm')[0].reset();
}

function hideErrorMessage(){
	$("label.error").remove();
	$(".error").removeClass("error");
}

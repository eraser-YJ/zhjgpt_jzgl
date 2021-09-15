$(document).ready(function(){

//初始化校验方法
	$("#userPwdForm").validate({
        rules: {
           password: 
 		   {
 			   required: true,
 			   minlength: 4,
 			   maxlength: 20,
 			   pwd: true,
 			   remote:{
                  url: getRootPath()+"/sys/user/checkUserPwd.action",
                  type: "get",
                  dataType: 'json',
                  data: {
                      'password': function(){return $("#password").val();}
                  }
			    }
 		   },
 		   newPassword: 
		   {
			   required: true,
			   minlength: 10,
			   maxlength: 20,
			   noEqualTo: "#password",
			   pwdN: true
		   },
		   confirmPassword: 
 		   {
 			   required: true,
 			   minlength: 10,
 			   maxlength: 20,
 			   equalTo: "#newPassword",
			   pwdN: true
 		   },
	    },
		messages: {
			confirmPassword: {equalTo: $.i18n.prop("JC_SYS_021")},
			newPassword: {noEqualTo: "不能输入和旧密码相同的新密码"},
			password: {remote: "旧密码输入错误"}
		}
	});

});
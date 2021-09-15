var userInfo = {};

//重复提交标识
userInfo.subState = false;

//获取修改信息
userInfo.get = function () {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/user/getUserInfo.action",
		dataType : "json",
		success : function(data) {
			if (data) {
				$("#userInfoForm").fill(data.user);
				$("#token").val(data.token);
				userInfo.initPhoto();
			}
		}
	});
};
userInfo.photoUrl = [];
//添加修改公用方法
userInfo.modify = function () {
	if(userInfo.subState)return;
	userInfo.subState = true;
	if ($("#userInfoForm").valid()) {
		var url = getRootPath()+"/sys/user/userInfoUpdate.action";
		var formData = $("#userInfoForm").serializeArray();
		var flies = attachPool.getFiles();
		if(flies) {
			formData.push({"name": "fileids", "value": flies});
			formData.push({"name": "photo", "value": $("#userPhotoImgFile").attr("path")});
		}
		jQuery.ajax({
			url : url,
			type : 'POST',
//			contentType : "application/json;charset=UTF-8",// 一对多关系必填否则去掉
			data : serializeJson(formData),
			success : function(data) {
				userInfo.subState = false;
				//getToken();
				$("#token").val(data.token);
				if(data.success == "true"){
					$("#userInfoForm #modifyDate").val(data.modifyDate);
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					$("#loginuserphoto", window.parent.document).attr("src",$("#userPhotoImg").attr("src").replace('images/demoimg/iphoto.jpg','images/demoimg/userPhoto.png'));
				} else {
					if(data.labelErrorMessage){
						showErrors("userInfoForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							content: data.errorMessage
						});
					}
				}
			},
			error : function() {
				userInfo.subState = false;
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			},
//			contentType: "application/json;charset=UTF-8"	//一对多关系必填否则去掉
		});
	} else {
		userInfo.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};
userInfo.initPhoto = function(){
	//上传头像实例
	attachPool.add({
		containerId: '#userUploader',
		uploadInitId: '#userFilePicker',
		uploadInitLabel: '上传头像',
		//限制只能上传一个文件
		fileNumLimit: 1,
		//限制只可以上传图片
		accept: {
			title: '指定格式',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/jpg,image/jpeg,image/png,image/bmp'
		},
		dndArea: '#dndArea',
		form: {
			businessId: $('#id').val(),
			businessTable: 'tty_sys_user',
			category: 'userPhotoImg'
		},
		isEcho: true,
		error:function(id,name){
			//删除业务逻辑
			jQuery.ajax({
				url : getRootPath()+'/content/attach/deleteUserPhoto.action',
				type : 'POST',
				data : {id: $('#id').val()},
				success : function(result) {
					if(result.state){
    					msgBox.tip({
    						type:"success",
    						content: "删除头像成功"
    					});
    				} else {
    					msgBox.tip({
    						content: "删除失败！"
    					});
    				}
				},
				error : function() {
					msgBox.tip({
						content: $.i18n.prop("JC_SYS_002")
					});
				}
			});
		}
	});
}
//初始化
jQuery(function($){
	$("#saveBtn").click(userInfo.modify);
	//日历控件重新初始化
	$(".datepicker-input").datepicker();
	//getToken();
	userInfo.get();
});

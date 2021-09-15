//初始化方法
var logoModule = {};




//重复提交标识
logoModule.subState = false;

//显示表单弹出层
logoModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	var deptToken = $("#token").val();
	logoModule.clearForm();
	$("#token").val(deptToken);
	$('#myModal-edit').modal('show');
	logoModule.loadpic();
};

//获取修改信息
logoModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/logo/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				logoModule.clearForm();

				$("#logoForm").fill(data);
				logoModule.loadpic();

				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');

			}
		}
	});
};

//添加修改公用方法
logoModule.saveOrModify = function (hide) {
	if(logoModule.subState)return;
		logoModule.subState = true;
	if ($("#logoForm").valid()) {
		var url = getRootPath()+"/sys/logo/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/logo/update.action";
		}
		var formData = $("#logoForm").serializeArray();
		var flies = attachPool.getFiles();
		if(flies) {
			formData.push({"name": "fileids", "value": flies});
			formData.push({"name": "logoPath", "value": $("#logoImgFile").attr("path")});
		}
		var delFiles = attachPool.getDeleteFiles();
		if(delFiles) {
			formData.push({"name": "delattachIds", "value": delFiles});
		}
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : serializeJson(formData),
			success : function(data) {
				logoModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						logoModule.clearForm();
					}
					$("#token").val(data.token);
					logoList.logoList();
				} else {
					if(data.labelErrorMessage){
						showErrors("logoForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							type:"fail",
							content: data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				logoModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		logoModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
logoModule.clearForm = function(){
	var token = $("#logoForm #token").val();
	$('#logoForm')[0].reset();
	$("#photo").val("");
	$("#logoForm #token").val(token);
	hideErrorMessage();
};

logoModule.loadpic = function(){
	attachPool.clearFileQueue('#uploader2');
	//单上传实例，限制只能上传图片
	attachPool.add({
		containerId: '#uploader2',
		uploadInitId: '#filePicker2',
		toButtonView : "#logoImgUpload",
		uploadInitLabel: '上传图片',
		//限制只能上传一个文件
		fileNumLimit: 1,
		//限制只可以上传图片
		accept: {
			title: '指定格式',
			extensions: 'gif,jpg,jpeg,bmp,png',
			mimeTypes: 'image/*'
		},
		dndArea: '#dndArea2',
		form: {
			businessId: $('#id').val(),
			businessTable: 'tty_sys_logo',
			category: 'logoImg'
		},
		isEcho: true
	});
}

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});

	$("#saveAndClose").click(function(){logoModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){logoModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});

});
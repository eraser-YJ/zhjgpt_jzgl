$(document).ready(function(){
	
	ie8StylePatch();
	$("#saveClose").click(function(){portalFriendlylinkModule.saveOrModify(true);});
	//$("#saveAndClose").click(function(){portalFriendlylinkModule.saveOrModify(true);});
	//$("#saveOrModify").click(function(){portalFriendlylinkModule.saveOrModify(false);});
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
	



});
  	

	
//初始化方法
var portalFriendlylinkModule = {};

//重复提交标识
portalFriendlylinkModule.subState = false;

//重复提交token
portalFriendlylinkModule.subtoken = null;

//显示表单弹出层
portalFriendlylinkModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	portalFriendlylinkModule.clearForm();
	$('#myModal-edit').modal('show');
	if(portalFriendlylinkModule.subtoken != null)
		$("#token").val(portalFriendlylinkModule.subtoken);
	$("#actionTitle").html("新增");
};

//验证名称是否重复
portalFriendlylinkModule.valNameEcho = function(){
	var returnvalNameEcho = false;
	var formData = $("#portalFriendlylinkForm").serializeArray();
	jQuery.ajax({
		url: getRootPath()+"/sys/portalFriendlylink/valNameEcho.action?time="+(+new Date().getTime()),
		type: "post",
		async:false,
		data: formData,
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.info({content: "同一友情链接组件内连接名称重复，请重新命名", type:'fail'});
				returnvalNameEcho = false;
			}else {
				returnvalNameEcho = true;
			}
		}
	});
	return returnvalNameEcho;
};

//获取修改信息
portalFriendlylinkModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/portalFriendlylink/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				portalFriendlylinkModule.clearForm();
				$("#portalFriendlylinkForm").fill(data);
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
				$("#actionTitle").html("编辑");
			}
		}
	});
};

//添加修改公用方法
portalFriendlylinkModule.saveOrModify = function (hide) {
	if(portalFriendlylinkModule.subState)return;
		portalFriendlylinkModule.subState = true;
	if ($("#portalFriendlylinkForm").valid() && portalFriendlylinkModule.valNameEcho()) {
		var url = getRootPath()+"/sys/portalFriendlylink/saveone.action?time="+(+new Date().getTime());
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/portalFriendlylink/update.action?time="+(+new Date().getTime());
		}
		var formData = $("#portalFriendlylinkForm").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				portalFriendlylinkModule.subState = false;
				$("#token").val(data.token);
				portalFriendlylinkModule.subtoken = data.token;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						portalFriendlylinkModule.clearForm();
					}
					
					portalFriendlylink.portalFriendlylinkList();
				} else {
					if(data.labelErrorMessage){
						showErrors("portalFriendlylinkForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							content: data.errorMessage
						});
					}
				}
				
			},
			error : function() {
				portalFriendlylinkModule.subState = false;
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		portalFriendlylinkModule.subState = false;
	}
};

portalFriendlylinkModule.findportlets = function(portalid){
	$("#portalFriendlylinkForm #portletId").find("option").remove();
	
	$("#portalFriendlylinkForm #portletId").append("<option value=''>-请选择-</option>");
	
	$("#portalFriendlylinkForm #tempportlet").find("option").each(function(){
		if($(this).attr("name").indexOf(portalid) != -1){
			$("#portalFriendlylinkForm #portletId").append($(this).clone());
		}
	});
};


//清空表单数据
portalFriendlylinkModule.clearForm = function(){
	$('#portalFriendlylinkForm')[0].reset();
	//清除所属业务组件的下拉框信息 start
	$("#portalFriendlylinkForm #portletId").find("option").remove();
	$("#portalFriendlylinkForm #portletId").append("<option value=''>-请选择-</option>");
	//end
	hideErrorMessage();
};
  	



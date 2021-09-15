//初始化方法
var shortcutModule = {};




//重复提交标识
shortcutModule.subState = false;

//显示表单弹出层
shortcutModule.show = function (){
	$("#saveAndClose").attr("class","btn");
	$("#saveAndClose").html($.i18n.prop("JC_SYS_097"));
	$("#saveOrModify").show();
	$("#id").val(0);
	shortcutModule.clearForm();
	$('#myModal-edit').modal('show');
	shortcutModule.menulist(2492,"管理中心",0);
};

//获取修改信息
shortcutModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/shortcut/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				shortcutModule.clearForm();

$("#shortcutForm").fill(data);
				$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
				$("#saveAndClose").html($.i18n.prop("JC_SYS_095"));
				$('#myModal-edit').modal('show');
				
				shortcutModule.menulist(data.subsystemid,data.subsystemName,data.menuid,data.menuName);
			}
		}
	});
};

//添加修改公用方法
shortcutModule.saveOrModify = function (hide) {
	if(shortcutModule.subState)return;
		shortcutModule.subState = true;
	if ($("#shortcutForm").valid()) {
		var url = getRootPath()+"/sys/shortcut/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/shortcut/update.action";
		}
		var formData = $("#shortcutForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				shortcutModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						shortcutModule.clearForm();
					}
					$("#token").val(data.token);
					shortcutList.shortcutList();
				} else {
					if(data.labelErrorMessage){
						showErrors("shortcutForm", data.labelErrorMessage);
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
				shortcutModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		shortcutModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
shortcutModule.clearForm = function(){
	var token = $("#shortcutForm #token").val();
	$('#shortcutForm')[0].reset();

	$("#shortcutForm #token").val(token);
	hideErrorMessage();
};

shortcutModule.menulist = function(menuid,menuName,parentid,parentName){
	JCTree.init({
		container: 'menuTreeForOne',
		controlId: 'subsystemid-subsystemid',
		urls : {
			//所有机构和组织
			org: getRootPath()+"/sys/menu/menuTreeForOne.action"
		},
		isCheckOrRadio: false,
		isPersonOrOrg: false,
		echoData : {
	        "id": String(menuid),
	        "text": menuName
	    },
		callback:function(obj){
			if(parentid != 0 && menuid == obj.id){
				JCTree.init({
					container: 'menuTree',
					controlId: 'menuid-menuid',
					urls : {
						//所有机构和组织
						org: getRootPath()+"/sys/menu/menuTreeForOther.action?parentid="+obj.id
					},
					isCheckOrRadio: false,
					isPersonOrOrg: false,
					echoData : {
				        "id": String(parentid),
				        "text": parentName
				    }
				});
			} else {
				JCTree.init({
					container: 'menuTree',
					controlId: 'menuid-menuid',
					urls : {
						//所有机构和组织
						org: getRootPath()+"/sys/menu/menuTreeForOther.action?parentid="+obj.id
					},
					isCheckOrRadio: false,
					isPersonOrOrg: false
				});
			}
		}
	});
	
	//menuOne.setData(String(menuid));	
	//menulist.setData(String(parentid));
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	
	$("#saveAndClose").click(function(){shortcutModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){shortcutModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});
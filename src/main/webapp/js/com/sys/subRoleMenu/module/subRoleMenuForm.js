//初始化方法
var subRoleMenuModule = {};

var menuIdJcTree = {};

// 重复提交标识
subRoleMenuModule.subState = false;

// 显示表单弹出层
subRoleMenuModule.show = function() {
	subRoleMenuModule.clearForm();
	$('#myModal-edit1').modal('show');
};

// 获取修改信息
subRoleMenuModule.get = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/sys/subRoleMenu/get.action",
		data : {
			"roleId" : id
		},
		dataType : "json",
		success : function(data) {
			if (data) {
				// 清除验证信息
				hideErrorMessage();
				subRoleMenuModule.clearForm();

				$("#subRoleMenuForm").fill(data);
				if (data.menuIds && data.menuNames) {
					var menuIdArray = data.menuIds.split(",");
					var menuNameArray = data.menuNames.split(",");
					var data = $.map(menuIdArray, function(data, index) {
						return {
							id : menuIdArray[index],
							text : menuNameArray[index]
						}
					});
					menuIdJcTree.setData(data);
				}
				$('#myModal-edit1').modal('show');
			}
		}
	});
};

// 添加修改公用方法
subRoleMenuModule.saveOrModify = function(hide) {
	if (subRoleMenuModule.subState)
		return;
	subRoleMenuModule.subState = true;
	if ($("#subRoleMenuForm").valid()) {
		var url = getRootPath() + "/sys/subRoleMenu/save.action";
		var formData = $("#subRoleMenuForm").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			success : function(data) {
				subRoleMenuModule.subState = false;
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});
					$('#myModal-edit1').modal('hide');
					subRoleList.subRoleList();
				} else {
					if (data.labelErrorMessage) {
						showErrors("subRoleMenuForm", data.labelErrorMessage);
					} else {
						msgBox.info({
							type : "fail",
							content : data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				subRoleMenuModule.subState = false;
				msgBox.tip({
					type : "fail",
					content : $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subRoleMenuModule.subState = false;
		msgBox.info({
			content : $.i18n.prop("JC_SYS_067")
		});
	}
};

// 清空表单数据
subRoleMenuModule.clearForm = function() {
	var token = $("#subRoleMenuForm #token").val();
	$('#subRoleMenuForm')[0].reset();

	$("#subRoleMenuForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	$("#save").click(function() {
		subRoleMenuModule.saveOrModify();
	});
	$("#close").click(function() {
		$('#myModal-edit1').modal('hide');
	});

	menuIdJcTree = JCTree.init({
		container : "menuIdFormDiv",
		controlId : "subRoleMenu-menuIds",
		isPersonOrOrg : false,
		disabled : true,
		callback : function(data) {
		},
		urls : {
			org : getRootPath() + "/sys/menu/menuTreeForOther.action?parentid=" + $("#parentMenuId").val()
		},
		setting : {
			check : {
				enable : true,
				chkStyle : "checkbox",
				chkboxType : {
					"Y" : "ps",
					"N" : "ps"
				}
			},
			view : {
				selectedMulti : false,
				showLine : false
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : function(id, node) {
				}
			}
		}
	});
});
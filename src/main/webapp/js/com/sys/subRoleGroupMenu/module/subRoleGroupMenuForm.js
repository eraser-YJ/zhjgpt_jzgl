//初始化方法
var subRoleGroupMenuModule = {};

var menuIdJcTree = {};

// 重复提交标识
subRoleGroupMenuModule.subState = false;

// 显示表单弹出层
subRoleGroupMenuModule.show = function() {
	subRoleGroupMenuModule.clearForm();
	$('#myModal-edit').modal('show');
};

// 获取修改信息
subRoleGroupMenuModule.get = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/sys/subRoleGroupMenu/get.action",
		data : {
			"roleGroupId" : id
		},
		dataType : "json",
		success : function(data) {
			if (data) {
				// 清除验证信息
				hideErrorMessage();
				subRoleGroupMenuModule.clearForm();

				$("#subRoleGroupMenuForm").fill(data);
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
subRoleGroupMenuModule.saveOrModify = function(hide) {
	if (subRoleGroupMenuModule.subState)
		return;
	subRoleGroupMenuModule.subState = true;
	if ($("#subRoleGroupMenuForm").valid()) {
		var url = getRootPath() + "/sys/subRoleGroupMenu/save.action";
		var formData = $("#subRoleGroupMenuForm").serializeArray();

		jQuery.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			success : function(data) {
				subRoleGroupMenuModule.subState = false;
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});
					subRoleGroupMenuModule.clearForm();
					$('#myModal-edit1').modal('hide');
					subRoleGroupList.subRoleGroupList();
				} else {
					if (data.labelErrorMessage) {
						showErrors("subRoleGroupMenuForm", data.labelErrorMessage);
					} else {
						msgBox.info({
							type : "fail",
							content : data.errorMessage
						});
					}
				}
			},
			error : function() {
				subRoleGroupMenuModule.subState = false;
				msgBox.tip({
					type : "fail",
					content : $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subRoleGroupMenuModule.subState = false;
		msgBox.info({
			content : $.i18n.prop("JC_SYS_067")
		});
	}
};

// 清空表单数据
subRoleGroupMenuModule.clearForm = function() {
	$('#subRoleGroupMenuForm')[0].reset();

	hideErrorMessage();
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	$("#save").click(function() {
		subRoleGroupMenuModule.saveOrModify();
	});
	$("#close").click(function() {
		$('#myModal-edit1').modal('hide');
	});

	menuIdJcTree = JCTree.init({
		container : "menuIdFormDiv",
		controlId : "subRoleGroupMenu-menuIds",
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
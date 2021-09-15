//初始化方法
var subUserRoleModule = {};

var roleIdJcTree = {};

// 重复提交标识
subUserRoleModule.subState = false;

// 显示表单弹出层
subUserRoleModule.show = function() {
	subUserRoleModule.clearForm();
	$('#myModal-edit1').modal('show');
};

// 获取修改信息
subUserRoleModule.get = function(id,deptId) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/sys/subUserRole/get.action",
		data : {
			"userId" : id,"deptId":deptId
		},
		dataType : "json",
		success : function(data) {
			if (data) {
				// 清除验证信息
				hideErrorMessage();
				subUserRoleModule.clearForm();
				$("#subUserRoleForm").fill(data);
				if (data.roleIds && data.roleNames) {
					var roleIdArray = data.roleIds.split(",");
					var roleNameArray = data.roleNames.split(",");
					var data = $.map(roleIdArray, function(data, index) {
						return {
							id : roleIdArray[index],
							text : roleNameArray[index]
						}
					});
					roleIdJcTree.setData(data);
				}
				$('#myModal-edit1').modal('show');
			}
		}
	});
};

// 添加修改公用方法
subUserRoleModule.saveOrModify = function() {
	if (subUserRoleModule.subState)
		return;
	subUserRoleModule.subState = true;
	if ($("#subUserRoleForm").valid()) {
		var url = getRootPath() + "/sys/subUserRole/save.action";
		var formData = $("#subUserRoleForm").serializeArray();

		jQuery.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			success : function(data) {
				subUserRoleModule.subState = false;
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});
					$('#myModal-edit1').modal('hide');
					subUserRoleModule.clearForm();
					subUserList.subUserList();
				} else {
					if (data.labelErrorMessage) {
						showErrors("subUserRoleForm", data.labelErrorMessage);
					} else {
						msgBox.info({
							type : "fail",
							content : data.errorMessage
						});
					}
				}
			},
			error : function() {
				subUserRoleModule.subState = false;
				msgBox.tip({
					type : "fail",
					content : $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subUserRoleModule.subState = false;
		msgBox.info({
			content : $.i18n.prop("JC_SYS_067")
		});
	}
};

// 清空表单数据
subUserRoleModule.clearForm = function() {
	$('#subUserRoleForm')[0].reset();
	hideErrorMessage();
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	$("#save").click(function() {
		subUserRoleModule.saveOrModify(false);
	});
	$("#close").click(function() {
		$('#myModal-edit1').modal('hide');
	});

	roleIdJcTree = JCTree.init({
		container : "roleIdFormDiv",
		controlId : "subUserRole-roleIds",
		isPersonOrOrg : false,
		disabled : true,
		callback : function(data) {
		},
		urls : {
			org : getRootPath() + "/sys/subRole/queryAll.action?deptId="+$('#selDeptId').val()
		},
		parseData : function(data) {
			return {
				id : data.id, // ID
				text : data.roleName, // select2用于搜索数据
				name : data.roleName, // ztree显示显示的内容
				// queue : data.orderNum, // 排序*没有实现
				pId : -1, // 组织父节点
				isChecked : data.isLeaf ? false : true, // 下拉是否被选中
				chkDisabled : data.isLeaf,// 根节点不可选
				jp : Pinyin.GetJP(data.roleName)
			// 汉字的简拼
			}
		}
	});
});
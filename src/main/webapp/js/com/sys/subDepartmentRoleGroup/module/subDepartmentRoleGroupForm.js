//初始化方法
var subDepartmentRoleGroupModule = {};

var roleGroupIdJcTree = {};

// 重复提交标识
subDepartmentRoleGroupModule.subState = false;

// 显示表单弹出层
subDepartmentRoleGroupModule.show = function() {
	subDepartmentRoleGroupModule.clearForm();
	$('#myModal-edit1').modal('show');
};

// 获取修改信息
subDepartmentRoleGroupModule.get = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/sys/subDepartmentRoleGroup/get.action",
		data : {
			"deptId" : id
		},
		dataType : "json",
		success : function(data) {
			if (data) {
				// 清除验证信息
				hideErrorMessage();
				subDepartmentRoleGroupModule.clearForm();
				$("#subDepartmentRoleGroupForm").fill(data);
				if (data.roleGroupIds && data.roleGroupNames) {
					var roleGroupIdArray = data.roleGroupIds.split(",");
					var roleGroupNameArray = data.roleGroupNames.split(",");
					var data = $.map(roleGroupIdArray, function(data, index) {
						return {
							id : roleGroupIdArray[index],
							text : roleGroupNameArray[index]
						}
					});
					roleGroupIdJcTree.setData(data);
				}
				$('#myModal-edit1').modal('show');
			}
		}
	});
};

// 添加修改公用方法
subDepartmentRoleGroupModule.saveOrModify = function() {
	if (subDepartmentRoleGroupModule.subState)
		return;
	subDepartmentRoleGroupModule.subState = true;
	if ($("#subDepartmentRoleGroupForm").valid()) {
		var url = getRootPath() + "/sys/subDepartmentRoleGroup/save.action";
		var formData = $("#subDepartmentRoleGroupForm").serializeArray();

		jQuery.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			success : function(data) {
				subDepartmentRoleGroupModule.subState = false;
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});
					$('#myModal-edit1').modal('hide');
					subDepartmentRoleGroupModule.clearForm();
					subDepartmentList.subDepartmentList();
				} else {
					if (data.labelErrorMessage) {
						showErrors("subDepartmentRoleGroupForm", data.labelErrorMessage);
					} else {
						msgBox.info({
							type : "fail",
							content : data.errorMessage
						});
					}
				}
			},
			error : function() {
				subDepartmentRoleGroupModule.subState = false;
				msgBox.tip({
					type : "fail",
					content : $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subDepartmentRoleGroupModule.subState = false;
		msgBox.info({
			content : $.i18n.prop("JC_SYS_067")
		});
	}
};

// 清空表单数据
subDepartmentRoleGroupModule.clearForm = function() {
	$('#subDepartmentRoleGroupForm')[0].reset();
	hideErrorMessage();
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	roleGroupIdJcTree = JCTree.init({
		container : "roleGroupIdFormDiv",
		controlId : "subDepartmentRoleGroup-roleGroupIds",
		isPersonOrOrg : false,
		disabled : true,
		callback : function(data) {
		},
		urls : {
			org : getRootPath() + "/sys/subRoleGroup/queryAll.action"
		},
		parseData : function(data) {
			return {
				id : data.id, // ID
				text : data.groupName, // select2用于搜索数据
				name : data.groupName, // ztree显示显示的内容
				// queue : data.orderNum, // 排序*没有实现
				pId : -1, // 组织父节点
				isChecked : data.isLeaf ? false : true, // 下拉是否被选中
				chkDisabled : data.isLeaf,// 根节点不可选
				jp : Pinyin.GetJP(data.groupName)
			// 汉字的简拼
			}
		}
	});

	$("#save").click(function() {
		subDepartmentRoleGroupModule.saveOrModify();
	});
	$("#close").click(function() {
		$('#myModal-edit1').modal('hide');
	});
});
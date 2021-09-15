var subDepartmentRoleGroupList = {};

// 分页处理 start
// 分页对象
subDepartmentRoleGroupList.oTable = null;

subDepartmentRoleGroupList.oTableAoColumns = [ {
	mData : function(source) {
		return "<input type=\"checkbox\" name=\"ids\" value=" + source.id + ">";
	}
}, {
	'mData' : 'deptId'
}, {
	'mData' : 'roleGroupId'
}, {
	'mData' : 'roleGroupName'
}, {
	'mData' : 'weight'
}, {
	'mData' : 'secret'
}, {
	mData : function(source) {
		return oTableSetButtones(source);
	}
} ];

subDepartmentRoleGroupList.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subDepartmentRoleGroupList.oTable, aoData);

	var createDateBegin = $("#createDateBegin").val();
	if (createDateBegin.length > 0) {
		aoData.push({
			"name" : "createDateBegin",
			"value" : createDateBegin
		});
	}
	var createDateEnd = $("#createDateEnd").val();
	if (createDateEnd.length > 0) {
		aoData.push({
			"name" : "createDateEnd",
			"value" : createDateEnd
		});
	}

};

// 分页查询
subDepartmentRoleGroupList.subDepartmentRoleGroupList = function() {
	if (subDepartmentRoleGroupList.oTable == null) {
		subDepartmentRoleGroupList.oTable = $('#subDepartmentRoleGroupTable').dataTable({
			"iDisplayLength" : subDepartmentRoleGroupList.pageCount,// 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subDepartmentRoleGroup/manageList.action",
			"fnServerData" : oTableRetrieveData,// 查询数据回调函数
			"aoColumns" : subDepartmentRoleGroupList.oTableAoColumns,// table显示列
			// 传参
			"fnServerParams" : function(aoData) {
				subDepartmentRoleGroupList.oTableFnServerParams(aoData);
			},
			aaSorting : []// 设置表格默认排序列
			// 默认不排序列
			,
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 6 ]
			} ]
		});
	} else {
		subDepartmentRoleGroupList.oTable.fnDraw();
	}
};

// 删除对象
subDepartmentRoleGroupList.deleteSubDepartmentRoleGroup = function(id) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({
			content : $.i18n.prop("JC_SYS_061")
		});
		return;
	}
	msgBox.confirm({
		content : $.i18n.prop("JC_SYS_034"),
		success : function() {
			subDepartmentRoleGroupList.deleteCallBack(ids);
		}
	});
};

// 删除用户回调方法
subDepartmentRoleGroupList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath() + "/sys/subDepartmentRoleGroup/deleteByIds.action",
		data : {
			"ids" : ids
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type : "success",
					content : data.successMessage
				});
			} else {
				msgBox.info({
					content : data.errorMessage
				});
			}
			subDepartmentRoleGroupList.subDepartmentRoleGroupList();
		}
	});
};

subDepartmentRoleGroupList.queryReset = function() {
	$('#subDepartmentRoleGroupQueryForm')[0].reset();
};

// 加载添加DIV
subDepartmentRoleGroupList.loadModule = function() {
	if ($.trim($("#subDepartmentRoleGroupModuleDiv").html()).length == 0) {
		$("#subDepartmentRoleGroupModuleDiv").load(getRootPath() + "/sys/subDepartmentRoleGroup/loadForm.action", null,
				function() {
					subDepartmentRoleGroupModule.show();
				});
	} else {
		subDepartmentRoleGroupModule.show();
	}
};

// 加载修改div
subDepartmentRoleGroupList.loadModuleForUpdate = function(id) {
	if ($.trim($("#subDepartmentRoleGroupModuleDiv").html()).length == 0) {
		$("#subDepartmentRoleGroupModuleDiv").load(getRootPath() + "/sys/subDepartmentRoleGroup/loadForm.action", null,
				function() {
					subDepartmentRoleGroupModule.get(id);
				});
	} else {
		subDepartmentRoleGroupModule.get(id);
	}
};

$(document).ready(function() {

	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	// 计算分页显示条数
	subDepartmentRoleGroupList.pageCount = TabNub > 0 ? TabNub : 10;

	// 初始化列表方法
	subDepartmentRoleGroupList.subDepartmentRoleGroupList();
	$("#addSubDepartmentRoleGroupButton").click(subDepartmentRoleGroupList.loadModule);
	$("#showAddDiv_t").click(subDepartmentRoleGroupList.loadModule);
	$("#querySubDepartmentRoleGroup").click(subDepartmentRoleGroupList.subDepartmentRoleGroupList);
	$("#queryReset").click(subDepartmentRoleGroupList.queryReset);
	$("#deleteSubDepartmentRoleGroups").click("click", function() {
		subDepartmentRoleGroupList.deleteSubDepartmentRoleGroup(0);
	});
});
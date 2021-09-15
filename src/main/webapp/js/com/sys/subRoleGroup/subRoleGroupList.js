var subRoleGroupList = {};

// 分页处理 start
// 分页对象
subRoleGroupList.oTable = null;

subRoleGroupList.oTableAoColumns = [ /*
										 * { mData : function(source) { return "<input
										 * type=\"checkbox\" name=\"ids\"
										 * value=" + source.id + ">"; } },
										 */{
	'mData' : 'groupName'
},
/*
 * {'mData':'groupDescription' }, {'mData':'status' },
 */
{
	'mData' : 'createDate'
}, {
	mData : function(source) {
		return oTableSetButtones(source);
	}
} ];

subRoleGroupList.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subRoleGroupList.oTable, aoData);

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

	var groupName = $('#groupName').val();
	if (groupName.length > 0) {
		aoData.push({
			'name' : 'groupName',
			'value' : groupName
		});
	}
};

// 分页查询
subRoleGroupList.subRoleGroupList = function() {
	if (subRoleGroupList.oTable == null) {
		subRoleGroupList.oTable = $('#subRoleGroupTable').dataTable({
			"iDisplayLength" : subRoleGroupList.pageCount,// 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subRoleGroup/manageList.action",
			"fnServerData" : oTableRetrieveData,// 查询数据回调函数
			"aoColumns" : subRoleGroupList.oTableAoColumns,// table显示列
			// 传参
			"fnServerParams" : function(aoData) {
				subRoleGroupList.oTableFnServerParams(aoData);
			},
			aaSorting : []// 设置表格默认排序列
			// 默认不排序列
			,
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 2 ]
			} ]
		});
	} else {
		subRoleGroupList.oTable.fnDraw();
	}
};

// 删除对象
subRoleGroupList.deleteSubRoleGroup = function(id) {
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
			subRoleGroupList.deleteCallBack(ids);
		}
	});
};

// 删除用户回调方法
subRoleGroupList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath() + "/sys/subRoleGroup/deleteByIds.action",
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
			subRoleGroupList.subRoleGroupList();
		}
	});
};

subRoleGroupList.queryReset = function() {
	$('#subRoleGroupQueryForm')[0].reset();
};

// 加载添加DIV
subRoleGroupList.loadModule = function() {
	$("#subRoleGroupModuleDiv").html();
	$("#subRoleGroupModuleDiv").load(getRootPath() + "/sys/subRoleGroup/loadForm.action", null, function() {
		subRoleGroupModule.show();
	});
};

// 加载修改div
subRoleGroupList.loadModuleForUpdate = function(id) {
	$("#subRoleGroupModuleDiv").html();
	$("#subRoleGroupModuleDiv").load(getRootPath() + "/sys/subRoleGroup/loadForm.action", null, function() {
		subRoleGroupModule.get(id);
	});
};

// 加载菜单配置div
subRoleGroupList.loadConfigMenu = function(id) {
	$("#configMenuModuleDiv").html();
	$("#configMenuModuleDiv").load(getRootPath() + "/sys/subRoleGroupMenu/loadForm.action?roleGroupId=" + id, null,
			function() {
				subRoleGroupMenuModule.get(id);
			});
};

$(document).ready(function() {

	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	// 计算分页显示条数
	subRoleGroupList.pageCount = TabNub > 0 ? TabNub : 10;

	// 初始化列表方法
	subRoleGroupList.subRoleGroupList();
	$("#addSubRoleGroupButton").click(subRoleGroupList.loadModule);
	$("#showAddDiv_t").click(subRoleGroupList.loadModule);
	$("#querySubRoleGroup").click(subRoleGroupList.subRoleGroupList);
	$("#queryReset").click(subRoleGroupList.queryReset);
	$("#deleteSubRoleGroups").click("click", function() {
		subRoleGroupList.deleteSubRoleGroup(0);
	});
});
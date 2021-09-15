var subRoleList = {};

// 分页处理 start
// 分页对象
subRoleList.oTable = null;

subRoleList.oTableAoColumns = [ {
	'mData' : 'deptName'
}, {
	'mData' : 'roleName'
}, {
	'mData' : 'createDate'
}, {
	mData : function(source) {
		return oTableSetButtones(source);
	}
} ];

subRoleList.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subRoleList.oTable, aoData);

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
	var roleName = $('#roleName').val();
	if (roleName.length > 0) {
		aoData.push({
			'name' : 'roleName',
			'value' : roleName
		});
	}
	var deptIds = $('#deptIds').val();
	if (deptIds.length > 0) {
		aoData.push({
			'name' : 'deptIds',
			'value' : deptIds
		});
	}
};

// 分页查询
subRoleList.subRoleList = function() {
	if (subRoleList.oTable == null) {
		subRoleList.oTable = $('#subRoleTable').dataTable({
			"iDisplayLength" : subRoleList.pageCount,// 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subRole/manageList.action",
			"fnServerData" : oTableRetrieveData,// 查询数据回调函数
			"aoColumns" : subRoleList.oTableAoColumns,// table显示列
			// 传参
			"fnServerParams" : function(aoData) {
				subRoleList.oTableFnServerParams(aoData);
			},
			aaSorting : []// 设置表格默认排序列
			// 默认不排序列
			,
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 3 ]
			} ]
		});
	} else {
		subRoleList.oTable.fnDraw();
	}
};

// 删除对象
subRoleList.deleteSubRole = function(id) {
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
			subRoleList.deleteCallBack(ids);
		}
	});
};

// 删除用户回调方法
subRoleList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath() + "/sys/subRole/deleteByIds.action",
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
			subRoleList.subRoleList();
		}
	});
};

subRoleList.queryReset = function() {
	$('#subRoleQueryForm')[0].reset();
};

// 加载添加DIV
subRoleList.loadModule = function() {
	$("#subRoleModuleDiv").html();
	$("#subRoleModuleDiv").load(getRootPath() + "/sys/subRole/loadForm.action", null, function() {
		subRoleModule.show();
	});
};

// 加载修改div
subRoleList.loadModuleForUpdate = function(id) {
	$("#subRoleModuleDiv").html();
	$("#subRoleModuleDiv").load(getRootPath() + "/sys/subRole/loadForm.action", null, function() {
		subRoleModule.get(id);
	});
};

// 加载菜单配置div
subRoleList.loadConfigMenu = function(id) {
	$("#configMenuModuleDiv").html();
	$("#configMenuModuleDiv").load(getRootPath() + "/sys/subRoleMenu/loadForm.action?roleId=" + id, null, function() {
		subRoleMenuModule.get(id);
	});
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	// 计算分页显示条数
	subRoleList.pageCount = TabNub > 0 ? TabNub : 10;
	// 初始化列表
	subRoleList.usertree = JCTree.ztree({
		url : getRootPath() + "/api/subDepartment/managerDeptTree.action",
		container : 'treeDemo',
		expand : true,
		rootNode : true,
		onClick : function(jObj, id, tree) {
			tree.checkState = true;
			$("#subRoleQueryForm #deptIds").val(subRoleList.usertree.getChildNodesId(tree));
			$("#subRoleQueryForm #currentDeptId").val(tree.id);
			$("#subRoleQueryForm #deptName").val(tree.name);

            if (tree.deptType == 1){
                $('#addSubRoleButton').attr('disabled',"true");
            } else {
                $('#addSubRoleButton').removeAttr("disabled");
            }

			subRoleList.subRoleList();
		}
	});
	$("#addSubRoleButton").click(subRoleList.loadModule);
	$("#showAddDiv_t").click(subRoleList.loadModule);
	$("#querySubRole").click(subRoleList.subRoleList);
	$("#queryReset").click(subRoleList.queryReset);
});
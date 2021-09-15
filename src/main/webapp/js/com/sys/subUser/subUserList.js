var subUserList = {};

// 分页处理 start
// 分页对象
subUserList.oTable = null;

subUserList.oTableAoColumns = [
	{'mData': 'displayName'},
	{'mData':'dutyId' },
	{mData: "deptId", mRender : function(mData, type, full) {
		return full.deptName;
	}
}, {
	mData : function(source) {
		return oTableSetButtones(source);
	}
} ];

subUserList.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subUserList.oTable, aoData);

	var displayName = $("#subUserQueryForm #displayName").val();
	if (displayName.length > 0) {
		aoData.push({
			"name" : "displayName",
			"value" : displayName
		});
	}

	var dutyId = $('#subUserQueryForm #dutyId').val();
	if (dutyId.length > 0) {
		aoData.push({
			'name' : 'dutyId',
			'value' : dutyId
		});
	}

	var deptIds = $("#subUserQueryForm #deptIds").val();
	if (deptIds.length > 0) {
		aoData.push({
			"name" : "deptIds",
			"value" : deptIds
		});
	}

};

// 分页查询
subUserList.subUserList = function() {
	if (subUserList.oTable == null) {
		subUserList.oTable = $('#subUserTable').dataTable({
			"iDisplayLength" : subUserList.pageCount,// 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subUser/manageList.action",
			"fnServerData" : oTableRetrieveData,// 查询数据回调函数
			"aoColumns" : subUserList.oTableAoColumns,// table显示列
			// 传参
			"fnServerParams" : function(aoData) {
				subUserList.oTableFnServerParams(aoData);
			},
			aaSorting : []// 设置表格默认排序列
			// 默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2,3]}
	        ]
		});
	} else {
		subUserList.oTable.fnDraw();
	}
};

// 删除对象
subUserList.deleteSubUser = function(id,deptId) {
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
			subUserList.deleteCallBack(ids,deptId);
		}
	});
};

// 删除用户回调方法
subUserList.deleteCallBack = function(ids,deptId) {
	$.ajax({
		type : "POST",
		url : getRootPath() + "/sys/subUser/deleteByIds.action",
		data : {
			"ids" : ids,"deptId":deptId
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
			subUserList.subUserList();
		}
	});
};

subUserList.queryReset = function() {
	$('#subUserQueryForm')[0].reset();
};

// 加载添加DIV
subUserList.loadModule = function() {
	if ($.trim($("#subUserModuleDiv").html()).length == 0) {
		$("#subUserModuleDiv").load(getRootPath() + "/sys/subUser/loadForm.action", null, function() {
			subUserModule.show();
		});
	} else {
		subUserModule.show();
	}
};

// 加载修改div
subUserList.loadModuleForUpdate = function(id, deptId) {
	if ($.trim($("#subUserModuleDiv").html()).length == 0) {
		$("#subUserModuleDiv").load(getRootPath() + "/sys/subUser/loadForm.action", null, function() {
			subUserModule.get(id, deptId);
		});
	} else {
		subUserModule.get(id, deptId);
	}
};

/**
 * 角色div
 */
subUserList.loadUserRoleFrom = function(userId,deptId) {
	$("#subUserRoleModuleDiv").html();
	$("#subUserRoleModuleDiv").load(getRootPath() + "/sys/subUserRole/loadForm.action?userId=" + userId+"&deptId="+deptId, null,
			function() {
				subUserRoleModule.get(userId,deptId);
			});
}

$(document).ready(function() {

	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});

	// 计算分页显示条数
	subUserList.pageCount = TabNub > 0 ? TabNub : 10;

	// 初始化列表
	subUserList.usertree = JCTree.ztree({
		container : 'treeDemo',
		url : getRootPath() + "/api/subDepartment/managerDeptTree.action",
		expand : true,
		rootNode : true,
		onClick : function(jObj, id, tree) {
			tree.checkState = true;
			$("#subUserQueryForm #deptIds").val(subUserList.usertree.getChildNodesId(tree));
			$("#subUserQueryForm #deptId").val(tree.id);
			$("#subUserQueryForm #deptName").val(tree.name);

			if (tree.deptType == 1){
                $('#addSubUserButton').attr('disabled',"true");
			} else {
                $('#addSubUserButton').removeAttr("disabled");
            }

			subUserList.subUserList();
		}
	});

	// 初始化列表方法
	$("#addSubUserButton").click(subUserList.loadModule);
	$("#showAddDiv_t").click(subUserList.loadModule);
	$("#querySubUser").click(subUserList.subUserList);
	$("#queryReset").click(subUserList.queryReset);
	$("#deleteSubUsers").click("click", function() {
		subUserList.deleteSubUser(0);
	});
});
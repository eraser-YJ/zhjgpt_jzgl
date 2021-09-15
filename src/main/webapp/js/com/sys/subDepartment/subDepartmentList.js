var subDepartmentList = {}, subDepartmentUser = {};

subDepartmentList.pageCount = 10;
// 重复提交标识
subDepartmentList.subState = false;

// 分页处理 start
// 分页对象
subDepartmentUser.oTable = null;
subDepartmentList.oTable = null;

subDepartmentList.oTableAoColumns = [
	{ "mData": "name"},
	{
	"mData" : "deptType",
	"mRender" : function(mData, type, full) {
		return full.deptType == 0 ? "部门" : "机构";
	}
}, {
	mData : function(source) {
		return oTableSetButtones(source);
	}
} ];

// 人员
subDepartmentUser.oTableAoColumns = [
	{ 'mData': 'displayName' },
	{ 'mData': 'dutyId'},
	{ "mData": "sex", "mRender" : function(mData, type, full) { return full.sexValue; }}
];

subDepartmentList.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subDepartmentList.oTable, aoData);
	// 查询条件
	if ($("#deptIds").val().length > 0) {
		aoData.push({
			"name" : "deptIds",
			"value" : $("#deptIds").val()
		});
	}

};

// 组装后台参数
subDepartmentUser.oTableFnServerParams = function(aoData) {
	// 排序条件
	getTableParameters(subDepartmentUser.oTable, aoData);
	// 查询条件
	var deptIds = $("#userDeptId").val();
	if (deptIds.length > 0) {
		aoData.push({
			"name" : "deptIds",
			"value" : deptIds
		});
	}
};

// 分页查询
subDepartmentList.subDepartmentList = function() {
	if (subDepartmentList.oTable == null) {
		subDepartmentList.oTable = $('#subDepartmentTable').dataTable({
			"iDisplayLength" : subDepartmentList.pageCount,// 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subDepartment/manageList.action",
			"fnServerData" : oTableRetrieveData,// 查询数据回调函数
			"aoColumns" : subDepartmentList.oTableAoColumns,// table显示列
			// 传参
			"fnServerParams" : function(aoData) {
				subDepartmentList.oTableFnServerParams(aoData);
			},
			aaSorting : []// 设置表格默认排序列
			// 默认不排序列
	        ,"aoColumnDefs": [
				{"bSortable": false, "aTargets": [0,1,2]}
	        ],
			fnDrawCallback : function(oSettings) {
				if ($("#treeDemo")[0]) {
					var content = $("#content").height();
					var headerHeight_1 = $('#header_1').height() || 0;
					var headerHeight_2 = $("#header_2").height() || 0;
					$(".tree-right").css("padding-left", "215px");
					$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
					var lh = $("#LeftHeight").height()
					if ($("#scrollable").scrollTop() >= 113) {
						$("#LeftHeight").addClass("fixedNav");
						$("#LeftHeight").height(lh + 113)
					} else {
						var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop()
						$("#LeftHeight").height(lh + a)
						$("#LeftHeight").removeClass("fixedNav");
					}
				}
			}
		});
	} else {
		subDepartmentList.oTable.fnDraw();
	}
};

subDepartmentUser.departmentUserList = function() {
	if (subDepartmentUser.oTable == null) {
		subDepartmentUser.oTable = $('#uTable').dataTable({
			"iDisplayLength" : subDepartmentList.pageCount, // 每页显示多少条记录
			"sAjaxSource" : getRootPath() + "/sys/subUser/manageList.action", // 查询URL
			"fnServerData" : oTableRetrieveData, // 查询数据回调函数
			"aoColumns" : subDepartmentUser.oTableAoColumns, // table显示列
			"fnServerParams" : function(aoData) { // 传参
				subDepartmentUser.oTableFnServerParams(aoData);
			},
			aaSorting : [],
			"aoColumnDefs" : [ {
				"bSortable" : false,
				"aTargets" : [ 0, 1, 2 ]
			} ], // 默认不排序
			fnDrawCallback : function(oSettings) {
				if ($("#treeDemo")[0]) {
					var content = $("#content").height();
					var headerHeight_1 = $('#header_1').height() || 0;
					var headerHeight_2 = $("#header_2").height() || 0;
					$(".tree-right").css("padding-left", "215px");
					$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
					var lh = $("#LeftHeight").height()
					if ($("#scrollable").scrollTop() >= 113) {
						$("#LeftHeight").addClass("fixedNav");
						$("#LeftHeight").height(lh + 113)
					} else {
						var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop()
						$("#LeftHeight").height(lh + a)
						$("#LeftHeight").removeClass("fixedNav");
					}
				}
			}
		});
	} else {
		subDepartmentUser.oTable.fnDraw();
	}
};
// 分页处理 end

/**
 * 添加--添加部门html
 */
subDepartmentList.showDeptInsertHtml = function() {
	if ($.trim($("#deptInsertHtml").html()).length == 0) {
		$("#deptInsertHtml").load(getRootPath() + "/sys/subDepartment/showDeptInsertHtml.action", null, function() {
			subDepartmentList.showAddDepartmentDiv();
		});
	} else {
		subDepartmentList.showAddDepartmentDiv();
	}
}

/**
 * 显示部门添加DIV
 */
subDepartmentList.showAddDepartmentDiv = function() {
	var deptToken = $("#subDepartmentForm #deptToken").val();
	$("#subDepartmentForm")[0].reset();
	$("#subDepartmentForm #deptToken").val(deptToken);
	hideErrorMessage();
	subDepartmentInsert.deptNewObj.clearValue();
	if ($("#deptId").val() == null || $("#deptId").val() == "") {
		msgBox.info({
			content : $.i18n.prop("JC_SYS_117"),
			type : 'fail'
		});
		return;
	}
	var params = {
		id : $("#deptId").val()
	};
	$.ajax({
		url : getRootPath() + "/sys/subDepartment/queryOne.action",
		type : 'post',
		data : params,
		async : false,
		success : function(data, textStatus, xhr) {
			if (data.errorMessage != null) {
				msgBox.info({
					content : data.errorMessage,
					type : 'fail'
				});
			} else {
				// 显示选中部门名称写入部门ID--thisNodeName,thisNodeId
				if (data.name != null && data.name != "") {
					$("#thisNodeName").html(data.name);
					$("#parentId").val(data.id);
					$('#code').val(data.code);
					// $("#parentWeight").val(data.weight);
				} else {
					// 目前没有用到
					$("#thisNodeName").html("无上级组织");
				}
				$("#add-dept").modal("show");
				ie8StylePatch();
			}
		},
		error : function() {
			msgBox.info({
				content : $.i18n.prop("JC_SYS_116"),
				type : 'fail'
			});
		}
	});
};
/**
 * 角色组
 */
subDepartmentList.loadRoleGroupFrom = function(deptId, deptName) {
	$("#deptRoleGroupHtml").html();
	$("#deptRoleGroupHtml").load(
			getRootPath() + "/sys/subDepartmentRoleGroup/loadForm.action?deptId=" + deptId + "&deptName=" + encodeURI(deptName),
			null, function() {
				$(document).ready(function () {
					subDepartmentRoleGroupModule.get(deptId);
				});

			});
}

/**
 * 修改--添加部门html
 */
subDepartmentList.showSubDeptEditHtml = function(deptId) {
	if ($.trim($("#deptEditHtml").html()).length == 0) {
		$("#deptEditHtml").load(getRootPath() + "/sys/subDepartment/showDeptEditHtml.action", null, function() {
			subDepartmentList.updateDepartment(deptId);
		});
	} else {
		subDepartmentList.updateDepartment(deptId);
	}
}

/**
 * 显示修改部门DIV
 */
subDepartmentList.updateDepartment = function(deptId) {
	subDepartmentEdit.deptUpdateObj.clearValue();
	if (deptId == "") {
		msgBox.info({
			content : $.i18n.prop("JC_SYS_119"),
			type : 'fail'
		});
		return;
	}
	hideErrorMessage();
	var url = getRootPath() + "/sys/subDepartment/queryOne.action";
	var params = {
		id : deptId
	};
	$.ajax({
		url : url,
		type : 'post',
		data : params,
		async : false,
		success : function(data, textStatus, xhr) {
			var $form = $("#subDepartmentUpdateForm");
			$form.find("#id").val(data.id);
			$form.find("#name").val(data.name);
			$form.find("#deptType").val(data.deptType);
			// $form.find("#weight").val(data.weight);
			$form.find("#queue").val(data.queue);
			if (data.parentName != null && data.parentName != "") {
				$form.find("#thisNodeName").html(data.parentName);
				$form.find("#parentId").val(data.parentId);
				// $form.find("#parentWeight").val(data.weight);
			} else {
				$form.find("#thisNodeName").html("全局组织");
				$form.find("#parentId").val("");
			}
			$("#update-dept").modal("show");
			if (data.leaderId) {
				subDepartmentEdit.deptUpdateObj.setData({
					id : data.leaderId,
					text : data.displayName
				});
			}
			ie8StylePatch();
		},
		error : function() {
			msgBox.info({
				content : $.i18n.prop("JC_SYS_004"),
				type : 'fail'
			});
		}
	});
}

// 删除对象
subDepartmentList.deleteSubDepartment = function(id) {
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
			subDepartmentList.deleteCallBack(ids);
		}
	});
};

// 删除用户回调方法
subDepartmentList.deleteCallBack = function(ids) {
	$.ajax({
		type : "POST",
		url : getRootPath() + "/sys/subDepartment/deleteByIds.action",
		data : {
			"ids" : ids
		},
		dataType : "json",
		success : function(data) {
			if (data.success == "true") {
				msgBox.tip({
					type : "success",
					content : $.i18n.prop("JC_SYS_005"),
					callback : function() {
						var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						var node = treeObj.getNodeByParam("id", ids, null);
						var pNode = node.getParentNode();
						treeObj.removeNode(node, true);
						treeObj.selectNode(pNode, false);
						treeObj.setting.callback.onClick(null, pNode.id, pNode);
						pagingDataDeleteAllForGoBack("subDepartmentTable", ids.split(','));
					}
				});
			} else {
				if (data.labelErrorMessage != null && data.labelErrorMessage != "") {
					msgBox.info({
						content : data.labelErrorMessage,
						type : 'fail'
					});
				} else {
					msgBox.tip({
						content : $.i18n.prop("JC_SYS_006"),
						type : 'fail'
					});
				}
			}
		},
		error : function() {
			msgBox.tip({
				content : $.i18n.prop("JC_SYS_006"),
				type : 'fail'
			});
		}
	});
};

/**
 * 机构左侧组织树
 */
subDepartmentList.tree = function() {
	/**
	 * 获取可操作的子节点
	 */
	function getChildNodesId(node) {
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var childNodes = treeObj.transformToArray(node);
		var sNodes = "";
		var aNodes = "";
		for (var i = 0; i < childNodes.length; i++) {
            if(childNodes[i].deptType == 1 && node[0].userType == 1){
                sNodes = sNodes + childNodes[i].id + ",";
            }else if(childNodes[i].deptType == 1 && node[0].userType == 2){
                sNodes = sNodes + childNodes[i].id + ",";
            }else if(childNodes[i].deptType == 0){
                aNodes = aNodes + childNodes[i].id + ",";
            }
		}
		if (node[0].userType == 1 || node[0].userType == 2) {
			return (sNodes + aNodes).length > 0 ? (sNodes + aNodes).substring(0, (sNodes + aNodes).length - 1) : "";
		} else {
			return aNodes.length > 0 ? aNodes.substring(0, aNodes.length - 1) : "";
		}
	}

	function getChildNodesIdForUser(node) {
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var childNodes = treeObj.transformToArray(node);
		var nodes = "";
		for (var i = 0; i < childNodes.length; i++) {
			if (childNodes[i].isChecked == 1) {
				nodes = nodes + childNodes[i].id + ",";
			}
		}
		return nodes.substring(0, nodes.length - 1);
	}
	;

	function onBeforeClick(id, node) {
		if (node.isChecked == 0 && node.pId != null) {
			return false;
		} else {
			return true;
		}
	}

	function onClick(event, treeId, treeNode) {
		// $("#userDeptId").val(treeNode.id);只查询部门下的人员
		$("#id").val(treeNode.id);
		$("#deptName").html(treeNode.name); // 部门名称
		$("#leaderName").html(treeNode.leaderName);// 负责人
		$("#deptType").html(treeNode.deptType == 0 ? "部门" : "机构");// 组织类型
		$("#deptId").val(treeNode.id);
		var nodes = treeNode.children;
		if (nodes != undefined && nodes.length > 0) {
			$("#deptIds").val(getChildNodesId(nodes));// 获取根节点下所有节点
			$("#userDeptId").val(getChildNodesIdForUser(nodes) + "," + treeNode.id);
		} else {
			$("#deptIds").val("0");
			$("#userDeptId").val(treeNode.id);
		}
		subDepartmentList.subDepartmentList();
		subDepartmentUser.departmentUserList();
		checkNode(treeNode);
	}

	function onRemove(event, treeId, treeNode) {
		$("#deptId").val("");
		$("#deptName").html(""); // 部门名称
		$("#leaderName").html("");// 负责人
		$("#deptType").html("");// 组织类型
		subDepartmentList.subDepartmentList();
		subDepartmentUser.departmentUserList();
	}

	// 初始化列表
	JCTree.ztree({
		container : 'treeDemo',
		url : getRootPath() + "/api/subDepartment/managerDeptTree.action",
		expand : true,
		rootNode : true,
		onClick : onClick,
		onBeforeClick : onBeforeClick
	});

}

/**
 * 检查节点可操作的动作
 * @param node	节点
 */
function checkNode(node) {
	if (node.pId == null || node.pId == 0) { // 根节点
		$("#delDept").attr("disabled", true);
		if (node.userType == 1 || node.userType == 2) {
			$("#newDept").attr("disabled", false);
			$("#newDept-foot").attr("disabled", false);
			$("#editDept").attr("disabled", false);
			//$("#subDepartmentForm input[id=deptType][value=1]").attr("disabled", false);
			$("#copy").attr("disabled", false);
		} else {
			$("#newDept").attr("disabled", true);
			$("#newDept-foot").attr("disabled", true);
			$("#editDept").attr("disabled", true);
			$("#copy").attr("disabled", true);
		}
	} else { // 子节点
		if (node.deptType == 1) { // 机构
			if (node.userType == 1) {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", false);
				$("#delDept").attr("disabled", false);
				$("#copy").attr("disabled", true);
			} else {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", true);
				$("#delDept").attr("disabled", true);
				$("#copy").attr("disabled", true);
			}
		} else { // 部门
			if (node.userType == 1 || node.userType == 2) {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", false);
				$("#delDept").attr("disabled", false);
				$("#copy").attr("disabled", true);
			} else {
				$("#newDept").attr("disabled", true);
				$("#newDept-foot").attr("disabled", true);
				$("#editDept").attr("disabled", true);
				$("#delDept").attr("disabled", true);
				$("#copy").attr("disabled", true);
			}
		}
	}
}

subDepartmentList.tabOrg = function() {
	$("#newDept").show();
}

subDepartmentList.tabUser = function() {
	$("#newDept").hide();
}

subDepartmentList.copyDeptTree = function() {
	msgBox.confirm({
		content : "复制中心部门、用户数据后将清空所有自定义部门、自定义用户相关数据，是否继续？",
		success : function() {
			$.ajax({
				type : "POST",
				url : getRootPath() + "/sys/subDepartment/copyDeptTree.action",
				success : function(data) {
					if (data.success == "true") {
						msgBox.tip({
							type : "success",
							content : "复制成功",
							callback : function() {
								subDepartmentList.tree();
							}
						});
					} else {
						if (data.labelErrorMessage != null && data.labelErrorMessage != "") {
							msgBox.info({
								content : data.labelErrorMessage,
								type : 'fail'
							});
						} else {
							msgBox.tip({
								content : "复制失败",
								type : 'fail'
							});
						}
					}
				},
				error : function() {
					msgBox.tip({
						content : "复制失败",
						type : 'fail'
					});
				}
			});
		}
	});
}

$(document).ready(function() {

	// 计算分页显示条数
	subDepartmentList.pageCount = TabNub > 0 ? TabNub : subDepartmentList.pageCount;

	// 初始化列表方法
	$("#newDept-foot").click(subDepartmentList.showDeptInsertHtml);// 显示新增界面
	$("#tabOrg").click(subDepartmentList.tabOrg);// tab组织
	$("#tabUser").click(subDepartmentList.tabUser);// tab人员
	subDepartmentList.tree();
});

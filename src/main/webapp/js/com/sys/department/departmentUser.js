var department = {}, departmentUser = {}, departmentDel = {};
department.pageCount = 10;
department.subState = false;
department.oTable = null;
departmentUser.oTable = null;
departmentDel.oTable = null;

/**
 * 组织表格
 */
department.departmentList = function () {
	if (department.oTable == null) {
		department.oTable = $('#departmentTable').dataTable({
			"iDisplayLength": department.pageCount,
			"sAjaxSource": getRootPath() + "/department/searchManageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: 'parentId', bVisible: false},
				{mData: 'name', sTitle: '名称', bSortable: false},
                {mData: 'code', sTitle: '编码', bSortable: false},
				{mData: 'displayName', sTitle: '负责人', bSortable: false},
				{mData: 'deptType', sTitle: '组织类型', bSortable: false, mRender : function(mData, type, full) { return full.deptType == 0 ? "部门":"机构"; } },
				{mData: "queue", sTitle: '排序', bSortable: false},
				{mData: 'id', sTitle: '操作', bSortable: false, mRender: function(mData, type, full) {
					var edit = "<a class=\"a-icon i-new\" href=\"#\" onclick=\"department.showDeptEditHtml('"+ full.id+ "')\" role=\"button\" data-toggle=\"modal\">" + finalParamEditText + "</a>";
					var del = "<a class=\"a-icon i-remove\" href=\"javascript:;\" onclick=\"department.deleteDepartment('"+ full.id+ "')\">" + finalParamDeleteText + "</a>";
					var button = edit;
					if (full.parentId != 0) {
						button = edit + del;
					}
					return button;
				}, sWidth: 130}
			],
			"fnServerParams": function ( aoData ) {
				getTableParameters(department.oTable, aoData);
				if ($("#deptId").val().length > 0) {
					aoData.push({ "name": "likeIdToCode", "value": $("#deptId").val() });
				}
			},
			aaSorting:[],
			aoColumnDefs: [],
			fnDrawCallback : function(oSettings) {
				if ($("#treeDemo")[0]) {
					var content = $("#content").height();
					var headerHeight_1 = $('#header_1').height() || 0;
					var headerHeight_2 = $("#header_2").height() || 0;
					$(".tree-right").css("padding-left","215px");
					$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
					var lh = $("#LeftHeight").height();
					if ($("#scrollable").scrollTop() >= 113) {
						$("#LeftHeight").addClass("fixedNav");
						$("#LeftHeight").height(lh + 113)
					} else {
						var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop();
						$("#LeftHeight").height(lh + a);
						$("#LeftHeight").removeClass("fixedNav");
					}
				}
			}
		});
	} else {
		department.oTable.fnDraw();
	}
};

/**
 * 删除组织的表格
 */
departmentDel.departmentList = function () {
	if (departmentDel.oTable == null) {
		departmentDel.oTable = $('#delDepartmentTable').dataTable({
			"iDisplayLength": departmentDel.pageCount,
			"sAjaxSource": getRootPath() + "/department/searchManageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: "parentId", bVisible: false },
				{mData: "name", sTitle: '名称', bSortable: false},
                {mData: 'code', sTitle: '编码', bSortable: false},
				{mData: "displayName", sTitle: '负责人', bSortable: false},
				{mData: "deptType", sTitle: '组织类型', bSortable: false, mRender : function(mData, type, full) { return full.deptType == 0 ? "部门":"机构"; }},
				{mData: "id", sTitle: '操作', bSortable: false, mRender : function(mData, type, full) {
					return "<a class=\"a-icon i-new\" href=\"#\" onclick=\"departmentDel.delBackDepartments('"+ full.id +"','"+full.name+"','"+full.parentId+"','"+full.queue+"')\">恢复数据</a><input type=\"hidden\" name=\"status\" value="+ full.id + " >";
				}}
			],
			"fnServerParams": function ( aoData ) {
				getTableParameters(departmentDel.oTable, aoData);
				if ($("#deptId").val() != null) {
					aoData.push({ "name": "likeIdToCode", "value": $("#deptId").val()});
					aoData.push({ "name": "deleteFlag", "value": 1});
				}
			},
			aaSorting:[],
			aoColumnDefs: [],
			fnDrawCallback : function(oSettings) {
				if ($("#treeDemo")[0]) {
					var content = $("#content").height();
					var headerHeight_1 = $('#header_1').height() || 0;
					var headerHeight_2 = $("#header_2").height() || 0;
					$(".tree-right").css("padding-left", "215px");
					$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
					var lh = $("#LeftHeight").height();
					if ($("#scrollable").scrollTop() >= 113) {
						$("#LeftHeight").addClass("fixedNav");
						$("#LeftHeight").height(lh + 113);
					} else {
						var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop();
						$("#LeftHeight").height(lh + a)
						$("#LeftHeight").removeClass("fixedNav");
					}
				}
			}
		});
	} else {
		departmentDel.oTable.fnDraw();
	}
};

/**
 * 组织用户的表格
 */
departmentUser.departmentUserList = function () {
	if (departmentUser.oTable == null) {
		departmentUser.oTable = $('#uTable').dataTable({
			"iDisplayLength": department.pageCount,
			"sAjaxSource": getRootPath() + "/department/userManageList.action",
			"fnServerData": oTableRetrieveData,
			"aoColumns": [
				{mData: "displayName", sTitle: '姓名', bSortable: false},
				{mData: "extStr1", sTitle: '密级级别', bSortable: false, mRender : function(mData, type, full) { return full.extStr1Value; }},
				{mData: "sex", sTitle: '性别', bSortable: false, mRender : function(mData, type, full) { return full.sexValue; }}
			],
			"fnServerParams": function (aoData) {
				getTableParameters(departmentUser.oTable, aoData);
				var deptIds = $("#userDeptId").val();
				if (deptIds.length > 0) {
					aoData.push({ "name": "deptIds", "value": deptIds});
				}
			},
			aaSorting:[],
			aoColumnDefs: [],
			fnDrawCallback : function(oSettings) {
				if ($("#treeDemo")[0]) {
					var content = $("#content").height();
					var headerHeight_1 = $('#header_1').height() || 0;
					var headerHeight_2 = $("#header_2").height() || 0;
					$(".tree-right").css("padding-left", "215px");
					$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
					var lh = $("#LeftHeight").height();
					if ($("#scrollable").scrollTop() >= 113) {
						$("#LeftHeight").addClass("fixedNav");
						$("#LeftHeight").height(lh + 113);
					} else {
						var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop();
						$("#LeftHeight").height(lh + a)
						$("#LeftHeight").removeClass("fixedNav");
					}
				}
			}
		});
	} else {
		departmentUser.oTable.fnDraw();
	}
};

//新建一个promise对象
var deptDefer = $.Deferred();

/**
 * 添加--添加部门html
 */
department.showDeptInsertHtml = function(){
	deptDefer = $.Deferred();
	if($("#deptInsertHtml").html()){
		department.showAddDepartmentDiv();
		return false;
	}
	$("#deptInsertHtml").load(getRootPath() + "/department/showDeptInsertHtml.action" ,null ,function(){
		$.when(deptDefer.promise()).done(function(){
			department.showAddDepartmentDiv();
		});
	});
};

/**
 * 显示部门添加DIV
 */
department.showAddDepartmentDiv = function(){
	var deptToken = $("#departmentForm #deptToken").val();
	$("#departmentForm")[0].reset();
	$("#departmentForm #deptToken").val(deptToken);
	hideErrorMessage();
	departmentInsert.deptNewObj.clearValue();
	if ($("#deptId").val() == null || $("#deptId").val() == "") {
		msgBox.info({content:$.i18n.prop("JC_SYS_117"),type:'fail'});
		return;
	}
	$.ajax({
		url: getRootPath() + "/department/queryOne.action", type: 'post', data: {id: $("#deptId").val()}, async: false,
        success: function(data, textStatus, xhr) {
        	if (data.errorMessage != null) {
        		msgBox.info({content: data.errorMessage, type:'fail'});
        		return;
      	  	}
			if (data.deptType == 0) {
				$("#departmentForm input[id=deptType][value=0]").attr("disabled", false);
				$("#departmentForm input[id=deptType][value=1]").attr("disabled", true);
			} else {
				$("#departmentForm input[id=deptType][value=1]").attr("disabled", false);
			}
			if (data.name != null && data.name != "") {
				$("#thisNodeName").html(data.name);
				$("#parentId").val(data.id);
				$("#parentWeight").val(data.weight);
				if (data.needChagne) {
					//只能在资源库进行选择
					$('#departmentForm #name').attr("readonly", "readonly");
					/*$('#departmentForm #name').click(function () {
						if ($("#changeResourceHtml").html() == "") {
							$("#changeResourceHtml").load(getRootPath() + "/company/info/change.action", null, function() {
								cmCompanyChangePanel.init(function (changeData) {
									$('#departmentForm #name').val(changeData.text);
									$('#departmentForm #resourceId').val(changeData.id);
								});
							});
						} else {
							cmCompanyChangePanel.again();
						}
					})*/
					$('#departmentForm #name').click(function () {
						if ($("#changeResourceHtml").html() == "") {
							$("#changeResourceHtml").load(getRootPath() + "/resource/system/company/change.action", null, function() {
								resourceCompanyChangePanel.init({callback: function(changeData) {
									$('#departmentForm #name').val(changeData.text);
									$('#departmentForm #resourceId').val(changeData.id);
								}});
							});
						} else {
							resourceCompanyChangePanel.again();
						}
					})
				} else {
					$('#departmentForm #name').removeAttr("readonly");
					$('#departmentForm #name').unbind("click");
				}
			} else {
				$("#thisNodeName").html("无上级组织");
			}
			$("#add-dept").modal("show");
			ie8StylePatch();
			$("#departmentUpdateForm #deptToken").val(deptToken);
			$("#departmentForm #deptToken").val(deptToken);
        },
		error: function(){
        	msgBox.info({content: $.i18n.prop("JC_SYS_116"), type:'fail'});
        }
    });
};

/**
 * 修改--添加部门html
 */
department.showDeptEditHtml = function(deptId){
	deptDefer = $.Deferred();
	if ($("#deptEditHtml").html()) {
		department.updateDepartment(deptId);
		return false;
	}
	$("#deptEditHtml").load(getRootPath() + "/department/showDeptEditHtml.action", null, function() {
		$.when(deptDefer.promise()).done(function(){
			department.updateDepartment(deptId);
		});
	});
};

/**
 * 显示修改部门DIV
 */
department.updateDepartment = function(deptId) {
	departmentEdit.deptUpdateObj.clearValue();
	if (deptId == "") {
		msgBox.info({content:$.i18n.prop("JC_SYS_119"), type:'fail'});
		return;
	}
	hideErrorMessage();
	$.ajax({
		url: getRootPath() + "/department/queryOne.action", type: 'post', data: { id: deptId }, async: false,
        success: function(data, textStatus, xhr) {
			var $form = $("#departmentUpdateForm");
			$form.find("#id").val(data.id);
			$form.find("#name").val(data.name);
			$form.find("#weight").val(data.weight);
			$form.find("#queue").val(data.queue);
			/*var formCode = data.code;
			if (data.code != null && data.code.length > 0 && data.code.length > 4) {
			    formCode = formCode.substr(formCode.length - 4, formCode.length);
            }*/
			$form.find("#code").val(data.code);
        	if (data.parentName != null && data.parentName != "") {
				$form.find("#thisNodeName").html(data.parentName);
				$form.find("#parentId").val(data.parentId);
				$form.find("#parentWeight").val(data.parentWeight);
  	  		} else {
				$form.find("#thisNodeName").html("全局组织");
				$form.find("#parentId").val("");
  	  		}
			if (data.needChagne) {
				//不允许修改名字
				$('#departmentUpdateForm #name').attr("readonly", "readonly");
			} else {
				$('#departmentUpdateForm #name').removeAttr("readonly");
			}
			$form.find("#deptDesc").val(data.deptDesc);
      	  	$("#update-dept").modal("show");
			if (data.leaderId) {
				data.displayName = (data.displayName == null ? "" : data.displayName);
                departmentEdit.deptUpdateObj.setData({id:data.leaderId,text:data.displayName});
			}
      	  	ie8StylePatch();
        },
		error:function(){
        	msgBox.info({content: $.i18n.prop("JC_SYS_004"), type:'fail'});
        }
    });
}

/**
 * 删除组织
 */
department.deleteDepartment = function(deptId){
	if (deptId == "") {
		msgBox.info({content:$.i18n.prop("JC_SYS_120"), type:'fail'});
		return;
	}
	var idsArr = [];
	idsArr.push(deptId);
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"), type: 'fail',
		success: function() {
			jQuery.ajax({
				url: getRootPath() + "/department/logicDeleteById.action", type: 'POST', data: {id: deptId},
				success : function(data) {
					if(data.success == "true"){
						msgBox.tip({
							content: $.i18n.prop("JC_SYS_005"), type:'success',
							callback:function(){
								var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var node = treeObj.getNodeByParam("id", deptId, null);
								var pNode = node.getParentNode();
								treeObj.removeNode(node, true);
								treeObj.selectNode(pNode, false);
								treeObj.setting.callback.onClick(null, pNode.id, pNode);
								pagingDataDeleteAllForGoBack("departmentTable",idsArr);
							}
						});
					} else {
						if(data.labelErrorMessage != null && data.labelErrorMessage != ""){
							msgBox.info({content: data.labelErrorMessage, type:'fail'});
						} else {
							msgBox.tip({content: $.i18n.prop("JC_SYS_006"), type:'fail'});
						}
					}
				},
				error : function() {
					msgBox.tip({content: $.i18n.prop("JC_SYS_006"), type:'fail'});
				}
			});
		}
	});
};

/**
 * 机构左侧组织树
 */
department.tree = function(){
	/**
	 * 获取可操作的子节点
	 */
	function getChildNodesId(node){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var childNodes = treeObj.transformToArray(node);
	    var sNodes = "";
	    var aNodes = "";
	    for (var i = 0; i < childNodes.length; i++) {
            if (childNodes[i].deptType == 1 && node[0].userType == 1) {
                sNodes = sNodes + childNodes[i].id + ",";
            } else if(childNodes[i].deptType == 1 && node[0].userType == 2) {
                sNodes = sNodes + childNodes[i].id + ",";
            } else if(childNodes[i].deptType == 0) {
                aNodes = aNodes + childNodes[i].id + ",";
            }
	    }
	    if (node[0].userType == 1 || node[0].userType == 2) {
	    	return (sNodes+aNodes).length > 0 ? (sNodes+aNodes).substring(0, (sNodes+aNodes).length-1) : "";
	    } else {
	    	return aNodes.length > 0 ? aNodes.substring(0, aNodes.length -1 ) : "";
	    }
	}

	function getChildNodesIdForUser(node){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var childNodes = treeObj.transformToArray(node);
	    var nodes = "";
	    for(var i = 0; i < childNodes.length; i++) {
	    	if(childNodes[i].isChecked == 1){
	    		nodes = nodes + childNodes[i].id + ",";
	    	}
	    }
	    return nodes.substring(0, nodes.length - 1);
	}

	function onBeforeClick(id, node){
		if (node.isChecked == 0 && node.pId != null) {
			return false;
		} else {
			return true;
		}
	}

	function onClick(event, treeId, treeNode) {
		//$("#userDeptId").val(treeNode.id);只查询部门下的人员
	    $("#id").val(treeNode.id);
	    $("#deptName").html(treeNode.name);
		$("#leaderName").html(treeNode.leaderName);
		$("#deptType").html(treeNode.deptType == 0 ? "部门" : "机构");
		$("#deptId").val(treeNode.id);
		$("#deptIds").val(treeNode.id);
		$("#userDeptId").val(treeNode.id);
		/*
		var nodes = treeNode.children;
		if(nodes != undefined && nodes.length > 0){
			$("#deptIds").val(getChildNodesId(nodes));//获取根节点下所有节点
			$("#userDeptId").val(getChildNodesIdForUser(nodes)+","+treeNode.id);
		}else{
			$("#deptIds").val("0");
			$("#userDeptId").val(treeNode.id);
		}
		*/
		department.departmentList();
		departmentUser.departmentUserList();
		departmentDel.departmentList();
		checkNode(treeNode);
	}

	function onRemove(event, treeId, treeNode) {
		$("#deptId").val("");
	    $("#deptName").html("");
		$("#leaderName").html("");
		$("#deptType").html("");
		department.departmentList();
		departmentUser.departmentUserList();
		departmentDel.departmentList();
	}

	//初始化列表
	JCTree.ztree({
	    container  : 'treeDemo',
	    expand : true,
	    rootNode : true,
	    onClick: onClick,
	    onBeforeClick : onBeforeClick
	});

};

departmentDel.delBackDepartments = function(id, name, parentId, queue) {
	msgBox.confirm({
		content: '是否恢复数据？', type: 'fail',
		success: function(){
			jQuery.ajax({
				url: getRootPath() + "/department/update.action", type: 'POST',
				data: {id: id, deleteFlag: 0, name: name, parentId: parentId, queue: queue},
				success: function(data) {
					$("#departmentUpdateForm #deptToken").val(data.token);
					$("#departmentForm #deptToken").val(data.token);
					if (data.success == "true") {
						department.tree();
					}
				},
				error: function() {
					msgBox.tip({content: '数据恢复失败', type:'fail'});
				}
			});
		}
	});
};

/**
 * 检查节点可操作的动作
 * @param node	节点
 */
function checkNode(node){
	if (node.pId == null || node.pId == 0) {
		$("#delDept").attr("disabled", true);
		if (node.userType == 1 || node.userType == 2) {
			$("#newDept").attr("disabled", false);
			$("#newDept-foot").attr("disabled", false);
			$("#editDept").attr("disabled", false);
			$("#departmentForm input[id=deptType][value=1]").attr("disabled", false);
		} else {
			$("#newDept").attr("disabled", true);
			$("#newDept-foot").attr("disabled", true);
			$("#editDept").attr("disabled", true);
		}
	} else {
		if (node.deptType == 1) {
			if (node.userType == 1) {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", false);
				$("#delDept").attr("disabled", false);
				$("#departmentForm input[id=deptType][value=0]").attr("checked", true);
				$("#departmentForm input[id=deptType][value=1]").attr("checked", false);
				$("#departmentForm input[id=deptType][value=1]").attr("disabled", false);
			} else {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", true);
				$("#delDept").attr("disabled", true);
				$("#departmentForm input[id=deptType][value=0]").attr("checked", true);
				$("#departmentForm input[id=deptType][value=1]").attr("checked", false);
		  		$("#departmentForm input[id=deptType][value=1]").attr("disabled", true);
			}
		} else {
			if (node.userType == 1 || node.userType == 2) {
				$("#newDept").attr("disabled", false);
				$("#newDept-foot").attr("disabled", false);
				$("#editDept").attr("disabled", false);
				$("#delDept").attr("disabled", false);
				$("#departmentForm input[id=deptType][value=0]").attr("checked", true);
				$("#departmentForm input[id=deptType][value=1]").attr("checked", false);
		  		$("#departmentForm input[id=deptType][value=1]").attr("disabled", true);
			} else {
				$("#newDept").attr("disabled", true);
				$("#newDept-foot").attr("disabled", true);
				$("#editDept").attr("disabled", true);
				$("#delDept").attr("disabled", true);
				$("#departmentForm input[id=deptType][value=0]").attr("checked", true);
				$("#departmentForm input[id=deptType][value=1]").attr("checked", false);
		  		$("#departmentForm input[id=deptType][value=1]").attr("disabled", true);
			}
		}
	}
};

department.tabOrg = function(){
	$("#newDept").show();
};

department.tabUser = function(){
	$("#newDept").hide();
};

department.delTabOrg = function(){
	$("#newDept").hide();
};

/**
 * 初始化
 */
jQuery(function($) {
	department.pageCount = TabNub > 0 ? TabNub : department.pageCount;
	$("#tabOrg").click(department.tabOrg);
	$("#tabUser").click(department.tabUser);
	$("#delTabOrg").click(department.delTabOrg);
	department.tree();
});
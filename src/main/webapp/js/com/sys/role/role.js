var role = {};
var roleButton = {};
role.otherDeptCount = 1;
role.pageRows = null;
role.subState = false;
role.custom = false;
roleButton.deleteRole = function (id,personNum) {
	var ids = String(id);
	if (id == 0) {
		var idsArr = [];
		$("[name='ids']:checked").each(function() {
			idsArr.push($(this).val());
		});
		ids = idsArr.join(",");
	}
	if (ids.length == 0) {
		msgBox.info({ type:"fail", content:"请选择要删除的数据" });
		return;
	}
	if (personNum > 0) {
		msgBox.confirm({
			content: $.i18n.prop("JC_SYS_072"),
			fontSize: 's',
			success: function(){
				role.deleteCallBack(ids);
			}
		});
	} else {
		msgBox.confirm({
			content: $.i18n.prop("JC_SYS_034"),
			success: function(){
				role.deleteCallBack(ids,idsArr);
			}
		});
	}
};

role.deleteCallBack = function(ids,idsArr) {
	$.ajax({
		type: "POST", url: getRootPath() + "/sys/role/deleteByIds.action", data: {"ids": ids}, dataType: "json",
		success: function(data) {
			if (data > 0) {
				msgBox.tip({
					type:"success",
					content:"删除成功",
					callback:function(){
						role.roleList();
						pagingDataDeleteAllForGoBack("roleTable",idsArr);
					}
				});
			}
		}
	});
};

roleButton.get = function (id) {
	getToken();
	$.ajax({
		type: "GET", url: getRootPath() + "/sys/role/get.action", data: {"id": id}, dataType: "json",
		success: function(data) {
			if (data) {
				hideErrorMessage();
				$("#roleForm").fill(data);
				$("#roleForm #nameOld").val(data.name);
				$("#savaAndClose").addClass("dark");
				$("#savaAndClose").html("保  存");
				$("#roleClickType").html("编辑");
				//填充权限
				$("#savaAndKeep").hide();
				$('#myModal-edit').modal('show');
			}
		}
	});
};

role.oTable = null;
role.oTableAoColumns = [
	{mData: "name"},
	{mData: "deptName"},
	{mData: "personNum"},
	{mData: function(source) {
		return oTableSetButtons(source);
	}}
];

role.roleList = function () {
	role.oTableFnServerParams = function(aoData){
		getTableParameters(role.oTable, aoData);
		var deptId = $("#roleListForm #deptId").val();
		if (deptId.length > 0) { aoData.push({ "name": "deptIdToCode", "value": deptId}); }
	};
	//table对象为null时初始化
	if (role.oTable == null) {
		role.oTable = $('#roleTable').dataTable( {
			iDisplayLength: role.pageRows,
			sAjaxSource: getRootPath() + "/sys/role/manageList.action",
			fnServerData: oTableRetrieveData,
			aoColumns: role.oTableAoColumns,
			fnServerParams: function ( aoData ) {
				role.oTableFnServerParams(aoData);
			},
			aaSorting:[],
	        aoColumnDefs: [],
	    	fnDrawCallback: function(oSettings) {
	    	    if($("#treeDemo")[0]){
	    	    	var content = $("#content").height();
	    	        var headerHeight_1 = $('#header_1').height() || 0;
	    	        var headerHeight_2 = $("#header_2").height() || 0;
	    	        $(".tree-right").css("padding-left","215px");
	    			$("#LeftHeight").height(content-80-headerHeight_1-headerHeight_2);
	    	        var lh = $("#LeftHeight").height()
	    	        if($("#scrollable").scrollTop()>=113){
	    	            $("#LeftHeight").addClass("fixedNav");
	    	            $("#LeftHeight").height(lh + 113)
	    	        }else{
	    	            var a = $("#scrollable").scrollTop()>=113?113:$("#scrollable").scrollTop()
	    	            $("#LeftHeight").height(lh + a)
	    	            $("#LeftHeight").removeClass("fixedNav");
	    	        }
	    	    }
	    	}
		} );
	} else {
		role.oTable.fnDraw();
	}
};

roleButton.loadUpdateAuthorizeHtml = function (id,isManager,isSecurity,isAudit,weight,remark1){
	if(true){
		$("#roleAuthorize").load(getRootPath() + "/sys/role/roleAuthorize.action", null, function(responseTxt,statusTxt,xhr) {
			if (statusTxt == "success") {
				roleButton.showAuthorize(id,isManager,isSecurity,isAudit,weight,remark1);
			}
		});
	} else {
		roleButton.showAuthorize(id,isManager,isSecurity,isAudit,weight,remark1);
	}
};

roleButton.showAuthorize = function(roleId,isManager,isSecurity,isAudit,roleWeight,remark1){
	$('#myModal-authroize').modal('show');
	roleAuthorize.initRoleMenusForm();
	if(role.custom){
		if(roleAuthorize.roleTree){
			roleAuthorize.roleTree.clearValue();
		}else{
			roleAuthorize.roleTree = JCTree.init({
				container: 'deptTreeDiv',
				controlId: 'deptTreeId-deptTreeName',
				isPersonOrOrg: false,
				urls: {
					org: getRootPath() + "/system/managerDeptTree.action"
				}
			});
		}
	}
	var menuzTree = new menuTree(roleId);
	menuzTree.show("menuTreeDiv",roleWeight);
	if (roleAuthorize && roleAuthorize.person) {
		roleAuthorize.person.clearValue();
		roleButton.showData(roleId);
	} else {
		roleAuthorize.person = new JCFF.JCTree.Lazy({
			title: '授权选择人员',
			container: 'userSelectDiv',
			isShowAll:false,
			ready: function(){
				roleButton.showData(roleId);
			},
			funFormData: function(){
				return {
					weight: remark1
				}
			}
		});
	}
	$('#roleMenusForm #id').val(roleId);
	$('#roleMenusForm #roleId').val(roleId);
}

roleButton.showData = function(roleId){
	$.ajax({
		type: "post", url: getRootPath() + "/sys/role/getMenusByRole.action", data: {"roleId": roleId}, dataType: "json", async:false,
		success: function(data) {
			if (data.length > 0) {
				$.each(data, function(i, o) {
					var treeObj = $.fn.zTree.getZTreeObj("menuTreeDiv");
					var node = treeObj.getNodeByParam("id", o.menuId);
					if(node != null){
						treeObj.checkNode(node, true, false);
					}
				});
			}else{
				roleAuthorize.showOrHideWeight({"value":1});
			}
			$.ajax({
				type: "post",
				url: getRootPath() + "/sys/role/getExtsByRole.action",
				data: {"roleId": roleId},
				dataType: "json",
				success: function(data) {
					if (data.length > 0) {
						roleAuthorize.showOrHideWeight({"value":data[0].permissionType});
						if(data[0].permissionType == '4' && role.custom){
							$.ajax({
								type: "post",
								url: getRootPath()+"/sys/role/getBlcoksByRole.action",
								data: {"roleId": roleId},
								dataType: "json",
								success: function(data) {
									if (data.length > 0) {
										var result = [];
										$.each(data, function(i, o) {
											result.push(o.deptId);
										});
										roleAuthorize.roleTree.setData(result.join(','));
									}
								}
							});
						}
						$("#roleBlocksForm").fill(data[0]);
					}
				}
			});
			$("#roleMenusForm").fill(data[0]);
		}
	});
	roleButton.setPerson(roleId);
};

roleButton.setPerson = function(roleId){
	jQuery.ajax({
		url: getRootPath() + "/sysUserRole/getUserIdByRoleId.action?time=" + new Date().getTime(), async: false, type: 'POST', data: {roleId:roleId},
		success: function(data) {
			var result = [];
			if(data.length){
				for(var i = 0;i < data.length;i++){
					result.push({ id: data[i].userId, text: data[i].userName });
				}
			}
			if(result.length){
				roleAuthorize.person.setData(result);
			}
		},
		error: function() {
			msgBox.tip({ type:"fail", content:"角色人员获取失败!!!" });
		}
	});
};

role.organizationData = function(treeObj,pId,roleBlocks){
	var node = treeObj.getNodeByParam("id", pId);
	if(node == null)return;
	var nodeId = node.id;
	var pId = node.pId;
	for(var i = 0 ; i < roleBlocks.length ; i ++){
		var flag = true;
		if (roleBlocks[i].deptId == nodeId) {
			flag = false;
		}
		if (flag) {
			roleBlocks.push({"roleId":$('#roleMenusForm #roleId').val(),"deptId":nodeId,"isChecked":"0"}); //获取选中节点的值
		}
	}
	role.organizationData(treeObj,pId,roleBlocks);
};

role.loadAddHtml = function (){
	if ($.trim($("#roleEdit").html()) == "") {
		$("#roleEdit").load(getRootPath() + "/sys/role/roleEdit.action",null,function() {
			role.showAddDiv();
			ie8StylePatch();
		});
	} else {
		role.showAddDiv();
	}
};

role.showAddDiv = function (){
	roleEdit.initForm();
	var deptIds = $("#roleListForm #deptIds").val();
	var deptId = $("#roleListForm #deptId").val();
	var deptArray = deptIds.split(",");
	if(deptArray.indexOf(deptId) == -1){
		msgBox.tip({ type:"fail", content:"您无权限在此组织下建立角色" });
		return;
	}
	if ($('#roleListForm #deptId').val() == '') {
		msgBox.tip({ type:"fail", content:"请选择组织" });
		return;
	}
	getToken();
	$("#roleForm #nameOld").val('');
	$("#id").val(0);
	$("#roleClickType").html("新增");
	$("#savaAndClose").html("保存退出");
	$("#savaAndClose").removeClass("dark");
	$("#savaAndKeep").html("保存继续");
	$("#savaAndKeep").show();
	$("#saveOrModify").show();
	$('#myModal-edit').modal('show');

	var deptId = $('#roleListForm #deptId').val();
	var deptName = $('#roleListForm #deptName').val();
	var deptWeight = $("#roleListForm #deptWeight").val();
	if(deptId != "" && deptName != ""){
		$('#roleForm #deptId').val(deptId);
		$('#roleForm #deptName').val(deptName);
		$('#roleForm #deptWeight').val(deptWeight);
	}
};

roleButton.loadUpdateHtml = function (id){
	if ($.trim($("#roleEdit").html()) == "") {
		$("#roleEdit").load(getRootPath()+"/sys/role/roleEdit.action",null,function(){
			roleButton.get(id);
			ie8StylePatch();
		});
	}
	else{
		roleButton.get(id);
	}
};


//初始化
jQuery(function($){
	//计算分页显示条数
	role.pageRows = TabNub>0 ? TabNub: 10;
	//新增按钮事件
	$("#showAddDivS").click(role.loadAddHtml);
	//删除事件
	$("#deleteRoles").click("click", function(){roleButton.deleteRole(0);});
	//初始化列表
	var usertree = JCTree.ztree({
	    container : 'treeDemo',
	    expand: true,
	    rootNode: true,
	    onClick: function(jObj,id,tree){
	    	tree.checkState = true;
	    	$("#roleListForm #deptIds").val(usertree.getChildNodesId(tree));
	    	$("#roleListForm #deptId").val(tree.id);
			$("#roleListForm #deptName").val(tree.name);
			$("#roleListForm #deptWeight").val(tree.weight);
			role.roleList();
	    }
	});
});

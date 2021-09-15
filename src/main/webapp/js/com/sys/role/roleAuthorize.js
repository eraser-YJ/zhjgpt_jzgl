var roleAuthorize = {};
roleAuthorize.subState = false;
roleAuthorize.person = null;  //人员树缓存变量
roleAuthorize.roleTree= null; //自定义权限树缓存变量

roleAuthorize.saveRoleMenu = function () {
	
	if (roleAuthorize.subState)return;
	roleAuthorize.subState = true;
	if ($("#roleBlocksForm").valid()) {
		var url = getRootPath()+"/sys/role/saveRoleMenu.action?time=" + new Date().getTime();
		$("#roleMenusForm #token").val($("#roleForm #token").val());
		var formData = $("#roleMenusForm").serializeArray();
		formData = roleAuthorize.addFormParameters(formData);
		if(formData==null){
			roleAuthorize.subState = false;
			return;
		}
		//添加其他表单信息
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : JSON.stringify(serializeJson(formData)),
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				roleAuthorize.subState = false;
				//getToken();
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content:"保存成功"
					});
					roleAuthorize.initRoleMenusForm();
					$('#myModal-authroize').modal('hide');
					$("#roleForm #token").val(data.token);
					role.roleList();
				} else {
					if(data.labelErrorMessage){
						showErrors("roleForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							type:"fail",
							content:data.errorMessage
						});
					}
				}
			},
			error : function() {
				roleAuthorize.subState = false;
				alertx("保存失败");
			}
		});
	} else {
		roleAuthorize.subState = false;
		msgBox.info({
			content:"表单内容填写不完整"
		});
	}
};

//添加部门、角色、机构表单数据
roleAuthorize.addFormParameters = function (formData){

	var myRoleId = $('#roleMenusForm #roleId').val();
	//根据实际业务组装json对象
	var roleExts = [];
	var roleMenus = [];
	var roleBlocks = [];
	var roleUsers = [];

	var permissionType = $("#permissionType").val();
	var treeObj = $.fn.zTree.getZTreeObj("menuTreeDiv");
	if(treeObj==null){
		msgBox.info({
			content:"此角色无菜单信息，无法保存"
		});
		return null;
	}
	//获取所有访问权限选中的节点
    var nodes = treeObj.getCheckedNodes(true);

    for(var i=0;i<nodes.length;i++){
    	//if(permissionType == '1'){}
    	roleMenus.push({"roleId":myRoleId,"menuId":nodes[i].id,"permissionType":permissionType}); //获取选中节点的值
    }
    //组织菜单数据
	formData.push({"name": "roleMenus", "value": roleMenus});
	
	//组织自定义部门数据
	if(role.custom){
		var selecteds = {};
		//获取选中数据
		var ctomData = roleAuthorize.roleTree.getData();
		$.each(ctomData ,function(index ,item){
			//选中值
			roleBlocks.push({"roleId": myRoleId,"deptId":item.id,"isChecked":"1"});
			selecteds[item.id] = item;
		});
		//获取所有数据
		//var dataAll = roleAuthorize.roleTree.ztreeCacheData;
		//$.each(dataAll ,function(index ,item){
		//	if(!selecteds[item.id]){
		//		//未选值
		//		roleBlocks.push({"roleId": myRoleId,"deptId":item.id,"isChecked":"0"});
		//	}
		//});
	}
    formData.push({"name": "roleBlocks", "value": roleBlocks});

    //数据范围设定
	roleExts.push({"roleId":myRoleId,"permissionType": permissionType});
    formData.push({"name": "roleExts", "value": roleExts});

    //拼装组织部门人员数据
	var users = roleAuthorize.person.getData();  //获取所有选中人员
	$.each(users ,function(index, obj){
		roleUsers.push({"roleId":myRoleId,"userId": obj.id}); //获取选中节点的值
	});
	formData.push({"name": "sysUserRoles", "value": roleUsers});
    
	return formData;
};


roleAuthorize.initRoleMenusForm = function(){
	roleAuthorize.clearRoleMenusForm();
};

roleAuthorize.clearRoleMenusForm = function(){
	$('#roleMenusForm')[0].reset();
	$('#roleBlocksForm')[0].reset();
	hideErrorMessage();
}

roleAuthorize.showOrHideWeight = function(obj){
	if(role.custom){
		var $cus = $('#customContainer');
		if(obj.value == 4){
			$cus.show();
		}else{
			$cus.hide();
		}
	}
	//if(obj.value == 1){
	//	$("#weightLayer1").css('display','none');
	//	$("#weightLayer2").css('display','none');
	//	$("#deptTreeDiv").css('display','none');
	//	$("#deptTreeDiv").removeClass("inline");
	//	$("#deptTreeDiv").addClass("none");
	//}else{
	//	$("#weightLayer1").css('display','');
	//	$("#weightLayer2").css('display','');
	//	$("#deptTreeDiv").css('display','none');
	//	$("#deptTreeDiv").removeClass("inline");
	//	$("#deptTreeDiv").addClass("none");
	//	//自定义，此时加载部门树
	//	if(obj.value == 4){
	//		$("#deptTreeDiv").css('display','none');
	//		$("#deptTreeDiv").removeClass("none");
	//		$("#deptTreeDiv").addClass("inline");
	//	}
	//}
}

jQuery(function($){
	$("#savaRoleMenu").click(function(){roleAuthorize.saveRoleMenu();});
	$("#permissionType").change(function(){roleAuthorize.showOrHideWeight(this);});
	
	$("#closeRoleMenu").click(function(){
		$('#myModal-authroize').modal('hide');
	});

	if(!role.custom){
		$('#customDiv').remove();
		$('#customContainer').remove();
	}
});
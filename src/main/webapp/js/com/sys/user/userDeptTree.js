var userDeptTree = {};

userDeptTree.zNodes = null;
userDeptTree.tree = null;
userDeptTree.treeId = null;
userDeptTree.deptId = null;
userDeptTree.deptName = null;
userDeptTree.deptWeight = null;

userDeptTree.setting = {
	check : {
		enable : false,
		nocheckInherit : true
	},
	view : {
		selectedMulti : false
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		beforeClick : function(treeId, node){
			if(node.pId){
				if(node.deptType == 1){
					alertx($.i18n.prop("JC_SYS_108"));
					return false;
				}
			} else {
				return false;
			}
			if(node.isChecked == 0 && node.pId){
				return false;
			}
		}
	}
};

userDeptTree.show = function (treeId, deptId, deptName, callback, orgState,deptWeight){
	userDeptTree.treeId = treeId;
	userDeptTree.deptId = deptId;
	userDeptTree.deptName = deptName;
	userDeptTree.deptWeight = deptWeight;
	
	if(userDeptTree.zNodes == null){
		var url = getRootPath()+"/system/managerDeptTree.action";
		jQuery.ajax({
			url : url,
			type : 'GET',
			async: false,
			success : function(data) {
				if (data) {
					userDeptTree.zNodes = [];
					$.each(data, function(i, o) {
						userDeptTree.zNodes[i] = {
							id : o.id,
							pId : o.parentId,
							name : o.name + "",
							deptType : o.deptType,
							isChecked : o.isChecked,
							weight : o.weight,
							checkState : false,
							iconSkin : o.deptType==0 ? "fa-flag" : "fa-office"
						};
					});
				}
			}
		});
	}
	userDeptTree.tree = $.fn.zTree.init($("#"+treeId), userDeptTree.setting, userDeptTree.zNodes);
	
	var nodes = userDeptTree.tree.getNodes();
	if(nodes != null){
		userDeptTree.tree.expandNode(nodes[0],true,false);
	}
	
	//默认选中节点
	var deptId_ = $("#"+deptId).val();
	var deptName_ = $("#"+deptName).val();
	if(deptName_ != "" && deptId_ != ""){
		var node = userDeptTree.tree.getNodeByParam("id", deptId_);
		if("${checked}" == "true"){
			node.checkState = true;
			try{userDeptTree.tree.checkNode(node, true, true);}catch(e){}
			userDeptTree.tree.selectNode(node, false);
		}else{
			node.checkState = true;
			userDeptTree.tree.selectNode(node, true);
		}
	}
};

userDeptTree.save = function(){
	var treeObj = $.fn.zTree.getZTreeObj(userDeptTree.treeId);
	var nodes = treeObj.getSelectedNodes();
	if(nodes.length > 0){
		$("#"+userDeptTree.deptId).val(nodes[0].id);
		$("#"+userDeptTree.deptName).val(nodes[0].name);
		$("#"+userDeptTree.deptWeight).val(nodes[0].weight);
	}
	userEdit.validateDeptName();
	$('#myModal').modal('hide');
};

userDeptTree.close = function(){
	$('#myModal').modal('hide');
};

//初始化
jQuery(function($) 
{
	$("#treeSave").click(userDeptTree.save);
	$("#treeClose").click(userDeptTree.close);
});
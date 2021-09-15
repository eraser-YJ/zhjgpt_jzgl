var roleDeptTree = {};

roleDeptTree.zNodes = null;
roleDeptTree.callback = null;
roleDeptTree.rootId = null;
roleDeptTree.rootName = null;
roleDeptTree.treeId = null;

var adminTree = null;

roleDeptTree.setting = {
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
		onClick: function(event, treeId, node){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var node_ = treeObj.getNodeByParam("id", node.id);
			/*
			if(node_.checkState){
				node_.checkState = false;
				adminTree.cancelSelectedNode(node_);
				var rootNode = roleDeptTree.getRootNode();
				var ids = roleDeptTree.getChildNodesId(rootNode);
				roleDeptTree.defaultCheck(ids);
			
			} else {
			*/	
				node_.checkState = true;
				if($("#roleListForm #deptId").val() != 0){
					adminTree.getNodeByParam("id", $("#roleListForm #deptId").val()).checkState = false;
				}
				var ids = roleDeptTree.getChildNodesId(node_);
		        $("#roleListForm #deptIds").val(ids);
				
				$("#roleListForm #deptId").val(node_.id);
				$("#roleListForm #deptName").val(node_.name);
				
			//}
			
			if(roleDeptTree.callback){
				roleDeptTree.callback();
			}
		},
		beforeClick : function(treeId, node){
			if(node.isChecked == 0 && node.pId){
				return false;
			}
		}
	}
};

roleDeptTree.getRootNode = function(){
	var treeObj = $.fn.zTree.getZTreeObj(roleDeptTree.treeId);
	return treeObj.getNodeByParam("id", roleDeptTree.rootId);
};

roleDeptTree.getChildNodesId = function(node){
	var treeObj = $.fn.zTree.getZTreeObj(roleDeptTree.treeId);
	var childNodes = treeObj.transformToArray(node);  
    var nodes = "";
    for(var i = 0; i < childNodes.length; i++) {
    	if(childNodes[i].isChecked == 1){
    		nodes = nodes + childNodes[i].id + ",";
    	}
    }
    return nodes.substring(0, nodes.length-1);
};

roleDeptTree.cancelCheck = function(){
	var treeObj = $.fn.zTree.getZTreeObj(roleDeptTree.treeId);
	var node = treeObj.getNodeByParam("id", $("#roleListForm #deptId").val());
	if(node){
		node.checkState = false;
		treeObj.cancelSelectedNode(node);
		var rootNode = roleDeptTree.getRootNode();
		var ids = roleDeptTree.getChildNodesId(rootNode);
		roleDeptTree.defaultCheck(ids);
	}
};

roleDeptTree.show = function(treeId, callback) {
	roleDeptTree.treeId = treeId;
	roleDeptTree.callback = callback;
	var url = getRootPath()+"/system/managerDeptTree.action";
	jQuery.ajax({
		url : url,
		type : 'post',
		cache: false,
		async : false, 
		success : function(data) {
			if (data) {
				roleDeptTree.zNodes = [];
				$.each(data, function(i, o) {
					if(o.parentId == 0){
						roleDeptTree.rootId = o.id;
						roleDeptTree.rootName = o.name;
					}
					
					roleDeptTree.zNodes[i] = {
						id : o.id,
						pId : o.parentId,
						name : o.name + "",
						deptType : o.deptType,
						isChecked : o.isChecked,
						checkState : false,
						iconSkin : o.deptType==0 ? "fa-flag" : "fa-office"
					};
				});
				adminTree = $.fn.zTree.init($("#"+treeId), roleDeptTree.setting, roleDeptTree.zNodes);
				//adminTree.expandAll(true);
				var nodes = adminTree.getNodes();
				if(nodes != null){
					adminTree.expandNode(nodes[0],true,false);
				}
				
				var rootNode = roleDeptTree.getRootNode();
				var ids = roleDeptTree.getChildNodesId(rootNode);
				roleDeptTree.defaultCheck(ids);
			}
		},
	});
	
	if(callback){
		callback();
	}
};

//默认选中节点
roleDeptTree.defaultCheck = function(value){
	if(roleDeptTree.rootId != null && roleDeptTree.rootName != null){
		var treeObj = $.fn.zTree.getZTreeObj(roleDeptTree.treeId);
		var node = treeObj.getNodeByParam("id", roleDeptTree.rootId);
		treeObj.selectNode(node, true);
		node.checkState = true;
		$("#roleListForm #deptIds").val(value);
		$("#roleListForm #deptId").val(roleDeptTree.rootId);
		$("#roleListForm #deptName").val(roleDeptTree.rootName);
	}
};



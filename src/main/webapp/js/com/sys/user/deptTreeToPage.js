var deptTreeToPage = {};

deptTreeToPage.zNodes = null;
deptTreeToPage.callback = null;
deptTreeToPage.rootId = null;
deptTreeToPage.rootName = null;
deptTreeToPage.treeId = null;

var adminTree = null;

deptTreeToPage.setting = {
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
				var rootNode = deptTreeToPage.getRootNode();
				var ids = deptTreeToPage.getChildNodesId(rootNode);
				deptTreeToPage.defaultCheck(ids);
			
			} else {
			*/	
				node_.checkState = true;
				if($("#userListForm #deptId").val() != 0){
					adminTree.getNodeByParam("id", $("#userListForm #deptId").val()).checkState = false;
				}
				var ids = deptTreeToPage.getChildNodesId(node_);
		        $("#userListForm #deptIds").val(ids);
				
				$("#userListForm #deptId").val(node_.id);
				$("#userListForm #deptName").val(node_.name);
				
			//}
			
			if(deptTreeToPage.callback){
				deptTreeToPage.callback();
			}
		},
		beforeClick : function(treeId, node){
			if(node.isChecked == 0 && node.pId){
				return false;
			}
		}
	}
};

deptTreeToPage.getRootNode = function(){
	var treeObj = $.fn.zTree.getZTreeObj(deptTreeToPage.treeId);
	return treeObj.getNodeByParam("id", deptTreeToPage.rootId);
};

deptTreeToPage.getChildNodesId = function(node){
	var treeObj = $.fn.zTree.getZTreeObj(deptTreeToPage.treeId);
	var childNodes = treeObj.transformToArray(node);  
    var nodes = "";
    for(var i = 0; i < childNodes.length; i++) {
    	if(childNodes[i].isChecked == 1){
    		nodes = nodes + childNodes[i].id + ",";
    	}
    }
    return nodes.substring(0, nodes.length-1);
};

deptTreeToPage.cancelCheck = function(){
	var treeObj = $.fn.zTree.getZTreeObj(deptTreeToPage.treeId);
	var node = treeObj.getNodeByParam("id", $("#userListForm #deptId").val());
	if(node){
		node.checkState = false;
		treeObj.cancelSelectedNode(node);
		var rootNode = deptTreeToPage.getRootNode();
		var ids = deptTreeToPage.getChildNodesId(rootNode);
		deptTreeToPage.defaultCheck(ids);
	}
};

deptTreeToPage.show = function(treeId, callback) {
	deptTreeToPage.treeId = treeId;
	deptTreeToPage.callback = callback;
	
	var url = getRootPath()+"/system/managerDeptTree.action";
	jQuery.ajax({
		url : url,
		type : 'GET',
		cache: false,
		async : true, 
		success : function(data) {
			if (data) {
				deptTreeToPage.zNodes = [];
				$.each(data, function(i, o) {
					if(o.parentId == 0){
						deptTreeToPage.rootId = o.id;
						deptTreeToPage.rootName = o.name;
					}
					
					deptTreeToPage.zNodes[i] = {
						id : o.id,
						pId : o.parentId,
						name : o.name + "",
						deptType : o.deptType,
						isChecked : o.isChecked,
						checkState : false,
						iconSkin : o.deptType==0 ? "fa-flag" : "fa-office"
					};
				});
				adminTree = $.fn.zTree.init($("#"+treeId), deptTreeToPage.setting, deptTreeToPage.zNodes);
				//adminTree.expandAll(true);
				var nodes = adminTree.getNodes();
				if(nodes != null){
					adminTree.expandNode(nodes[0],true,false);
				}
				
				var rootNode = deptTreeToPage.getRootNode();
				var ids = deptTreeToPage.getChildNodesId(rootNode);
				deptTreeToPage.defaultCheck(ids);
			}
		},
	});
	
	if(callback){
		callback();
	}
};

//默认选中节点
deptTreeToPage.defaultCheck = function(value){
	if(deptTreeToPage.rootId != null && deptTreeToPage.rootName != null){
		var treeObj = $.fn.zTree.getZTreeObj(deptTreeToPage.treeId);
		var node = treeObj.getNodeByParam("id", deptTreeToPage.rootId);
		treeObj.selectNode(node, true);
		node.checkState = true;
		$("#userListForm #deptIds").val(value);
		$("#userListForm #deptId").val(deptTreeToPage.rootId);
		$("#userListForm #deptName").val(deptTreeToPage.rootName);
	}
};



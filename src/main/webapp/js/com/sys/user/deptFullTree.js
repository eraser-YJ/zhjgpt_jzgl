var deptFullTree = {};

deptFullTree.tree = null;
deptFullTree.zNodes = null;
deptFullTree.ids = new Array();

deptFullTree.setting = {
	check : {
		enable : true,
		nocheckInherit : true,
		chkStyle: "checkbox",
		chkboxType: { "Y": "", "N": "" }
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
		beforeClick:function(id, node){
			deptFullTree.tree.checkNode(node, !node.checked, true, true);
			return false;
		},
		beforeCheck : function(treeId, node){
			if(node.isChecked == 0){
				return false;
			}
		}
		
	}
};

deptFullTree.show = function(treeId) {
	if(deptFullTree.zNodes == null){
		var url = getRootPath()+"/system/managerOrgTree.action";
		jQuery.ajax({
			url : url,
			type : 'GET',
			async: false,
			success : function(data) {
				if (data) {
					deptFullTree.zNodes = [];
					$.each(data, function(i, o) {
						deptFullTree.zNodes[i] = {
							id : o.id,
							pId : o.parentId,
							name : o.name + "",
							deptType : o.deptType,
							iconSkin : o.deptType==0 ? "fa-flag" : "fa-office",
							isChecked : o.isChecked
						};
					});
				}
			}
		});
	}
	deptFullTree.tree = $.fn.zTree.init($("#"+treeId), deptFullTree.setting,deptFullTree.zNodes);
	var nodes = deptFullTree.tree.getNodes();
	if(nodes != null){
		deptFullTree.tree.expandNode(nodes[0],true,false);
	}
	
	//默认选中节点
	if(deptFullTree.ids.length > 0){
		var arr = new Array();
		for(var i=0; i<deptFullTree.ids.length; i++) {
			if(deptFullTree.ids[i].split(",")[2] == 1)
				arr.push(deptFullTree.ids[i].split(",")[0]);
		}
		deptFullTree.defaultCheck(arr);
	}
};

deptFullTree.defaultCheck = function (arr) {
	$.each(arr, function(i, o) {
		var node = deptFullTree.tree.getNodeByParam("id", o);
		try{deptFullTree.tree.checkNode(node, true, false);}catch(e){}
	});
};

deptFullTree.close = function(){
	//deptFullTree.ids = [];
	$('#myModal1').modal('hide');
};

deptFullTree.save = function(){
	deptFullTree.ids = [];
	//判断半勾选的节点
	var treeObj = $.fn.zTree.getZTreeObj("deptFullTreeDiv");
	var allNode = treeObj.getNodes();
	var childNodes = deptFullTree.tree.transformToArray(allNode);
	for(var i=0;i<childNodes.length;i++){
		var node = childNodes[i];
		if(node.getCheckStatus().half && !node.getCheckStatus().checked){
			var ips = node.id + "," + (node.pId == null ? 0 : node.pId) + ",0";
			if($.inArray(ips, deptFullTree.ids) == -1){
				deptFullTree.ids.push(ips);
			}
		}
	}
	//判断选中节点
	var nodes = deptFullTree.tree.getCheckedNodes(true);
	for(var i=0; i<nodes.length; i++) {
		var ips = nodes[i].id + "," + (nodes[i].pId == null ? 0 : nodes[i].pId) + ",1";
		if($.inArray(ips, deptFullTree.ids) == -1){
			deptFullTree.ids.push(ips);
		}
	}
	$("#adminSideV").val(deptFullTree.ids);
	hideErrorMessage();
	$('#myModal1').modal('hide');
};

deptFullTree.init = function (){
	deptFullTree.ids = [];
	$("#adminSideV").val(deptFullTree.ids);
};

jQuery(function($) 
{
	$("#fullTreeSave").click(deptFullTree.save);
	$("#fullTreeClose").click(deptFullTree.close);
});

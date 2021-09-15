function deptTree(){
};

deptTree.prototype = {
		show : function (treeId, callback,type,flag) {
			deptTree.callback = callback;
			var	setting = {
				check : {
					enable : true
				},
				data : {
					simpleData : {
						enable : true
					}
				},
				callback : {
					onClick: function(event, treeId, node){
						if(deptTree.callback){
							callback(node.id);
						}
					},
					beforeCheck : function(treeId, node){
						if(node.isChecked == 0){
							return false;
						}
					}
				}
				
			};
			var url = getRootPath()+"/system/managerDeptTree.action";

			$.ajax({
				type : "post",
				url : url,
				data : {},
				async : false,
				dataType : "json",
				success : function(data) {
				if (data) {
					deptTree.zNodes = [];
					$.each(data, function(i, o) {
						deptTree.zNodes[i] = {
							id : o.id,
							pId : o.parentId,
							name : o.name + "",
							deptType : o.deptType,
							iconSkin : o.deptType == '0'?"fa-flag":"fa-office",
							leaderName: o.leaderName,
							checkState : false,
							isChecked : o.isChecked
						};
					});
					
					deptTree.tree = $.fn.zTree.init($("#"+treeId), setting, deptTree.zNodes);
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					zTree.setting.check.chkboxType =  { "Y" : "", "N" : "" };
					var nodes = deptTree.tree.getNodes();
					if(nodes != null){
						deptTree.tree.expandNode(nodes[0],true,false);
					}
				}
				}
			});
		},
};


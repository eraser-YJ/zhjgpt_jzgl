function menuTree(roleId){
};

menuTree.prototype = {
		show : function (treeId, roleWeight, deptId, deptName, callback) {
			var	setting = {
				check : {
					enable : true,
					nocheckInherit : true,
					chkStyle: "checkbox"
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
//					onCheck:function(e,treeId,treeNode){
//			            var treeObj=$.fn.zTree.getZTreeObj("menuTreeDiv"),
//			            nodes=treeObj.getCheckedNodes(true),
//			            v="";
//			            for(var i=0;i<nodes.length;i++){
//			                v+=nodes[i].name + ",";
//			                alert(nodes[i].id); //获取选中节点的值
//			            }
//			        }  
				}
			};
			$.ajax({
				type : "post",
				url : getRootPath()+"/sys/menu/menuTree.action",
				data : {weight:roleWeight},
				async : false,
				dataType : "json",
				success : function(data) {
					if (data) {
						zNodes = [];
						$.each(data, function(i, o) {
							zNodes[i] = {
								id : o.id,
								pId : o.parentId,
								name : o.name + "",
								checkState : false
							};
						});
						setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
						menuTree.tree = $.fn.zTree.init($("#"+treeId), setting, zNodes);
						var nodes = menuTree.tree.getNodes();
						if(nodes != null){
							menuTree.tree.expandNode(nodes[0],true,false);
						}
					}
				}
			});
		},
		close : function(){
			$('#myModal').modal('hide');
		},
		open : function(){
			$('#myModal').modal('show');
		},
		cancelCheck : function(treeId, id){
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var node = treeObj.getNodeByParam("id", $("#"+id).val());
			if(node){
				node.checkState = false;
				treeObj.cancelSelectedNode(node);
			}
		}
};
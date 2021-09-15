function deptTree(){
	$("#treeSave").click(this.close);
	$("#treeClose").click(this.close);
};

deptTree.prototype = {
		tree : null,
		show : function (treeId, deptId, deptName, callback, orgState) {
			var	setting = {
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
					onClick : function(event, treeId, node){
						var treeObj = $.fn.zTree.getZTreeObj(treeId);
						var node_ = treeObj.getNodeByParam("id", node.id);
						//根节点不能选择
						if(node_.pId){
							if(node_.checkState){
								node_.checkState = false;
								$("#"+deptId).val(0);
								$("#"+deptName).val("");
								treeObj.cancelSelectedNode(node_);
							} else {
								node_.checkState = true;
								if($("#"+deptId).val() != 0){
									tree.getNodeByParam("id", $("#"+deptId).val()).checkState = false;
								}
								$("#"+deptId).val(node_.id);
								$("#"+deptName).val(node_.name);
							}
							if(callback){
								callback();
							}
						} else {
							treeObj.cancelSelectedNode(node_);
						}
					},
					beforeClick : function(treeId, node){
						if(!orgState){
							if(node.pId){
								if(node.deptType == 1){
									alertx($.i18n.prop("JC_SYS_108"));
									return false;
								}
							} else {
								return false;
							}
						}
						if(node.isChecked == 0 && node.pId){
							return false;
						}
					}
				}
			};
		
			var url = getRootPath()+"/system/managerDeptTree.action";
			$.getJSON(url, function(data) {
				if (data) {
					zNodes = [];
					$.each(data, function(i, o) {
						zNodes[i] = {
							id : o.id,
							pId : o.parentId,
							name : o.name + "",
							deptType : o.deptType,
							isChecked : o.isChecked,
							checkState : false,
							iconSkin : o.deptType==0 ? "fa-flag" : "fa-office"
						};
					});
					tree = $.fn.zTree.init($("#"+treeId), setting, zNodes);
					//tree.expandAll(true);
					
					var nodes = tree.getNodes();
					if(nodes != null){
						tree.expandNode(nodes[0],true,false);
					}
					
					//默认选中节点
					var deptId_ = $("#"+deptId).val();
					var deptName_ = $("#"+deptName).val();
					if(deptName_ != "" && deptId_ != ""){
						var node = tree.getNodeByParam("id", deptId_);
						if("${checked}" == "true"){
							node.checkState = true;
							try{tree.checkNode(node, true, true);}catch(e){}
							tree.selectNode(node, false);
						}else{
							node.checkState = true;
							tree.selectNode(node, true);
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
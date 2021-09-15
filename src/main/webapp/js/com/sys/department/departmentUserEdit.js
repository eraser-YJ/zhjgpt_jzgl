var departmentEdit = {};

departmentEdit.subState = false;
departmentEdit.deptUpdateObj = null;
var leaderName = "";

/**
 * 修改组织
 */
departmentEdit.updateDept = function(){
	var pWeight = $("#departmentUpdateForm #parentWeight").val();
	var dWeight = $("#departmentUpdateForm #weight").val();
	if(parseInt(pWeight) < parseInt(dWeight)){
		msgBox.info({content:"权重系数不能大于上级部门", type:'fail'});
		return;
	}
	if(departmentEdit.subState)	return;
	departmentEdit.subState = true;
	if ($("#departmentUpdateForm").valid()) {
		jQuery.ajax({
			url: getRootPath() + "/department/update.action", type: 'POST', data: $("#departmentUpdateForm").serializeArray(),
			success : function(data) {
				departmentEdit.subState = false;
				if(data.success == "true"){
					$("#departmentUpdateForm #deptToken").val(data.token);
					$("#departmentForm #deptToken").val(data.token);
					msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
					$("input[type=reset]").trigger("click");
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodes = treeObj.getSelectedNodes();
					var node = treeObj.getNodeByParam("id", data.dept.id, null);
					node.name = data.dept.name;
					node.queue = data.dept.queue;
					node.leaderName = data.dept.leaderId == 0 ? "" : leaderName;
					treeObj.updateNode(node);
					while(!node.isFirstNode){
						var pNode = node.getPreNode();
						treeObj.moveNode(pNode, node, "prev");
					}
					while(true){
						var nNode = node.getNextNode();
						if(nNode != null){
							if(node.queue > nNode.queue){
								treeObj.moveNode(nNode, node, "next");
							}else{
								break;
							}
						}else{
							break;
						}
					}
					$("#id").val(data.dept.id);
				    $("#deptName").html(data.dept.name);
					$("#leaderName").html(data.dept.leaderId == 0 ? "" : leaderName);
					$("#deptType").html(data.dept.deptType == 0 ? "部门" : "机构");
					$("#deptId").val(data.dept.id);
					treeObj.selectNode(nodes[0], false);
					treeObj.setting.callback.onClick(null, nodes[0].id, nodes[0]);
					$('#update-dept').modal('hide');
				} else {
					$("#departmentUpdateForm #deptToken").val(data.token);
					$("#departmentForm #deptToken").val(data.token);
					if(data.errorMessage != null && data.errorMessage != ""){
						msgBox.info({content: data.errorMessage, type:'fail'});
					} else {
						msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
					}
				}
			},
			error : function() {
				departmentEdit.subState = false;
				msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			}
		});
	}else{
		msgBox.info({content: $.i18n.prop("JC_SYS_118"), type:'fail'});
		departmentEdit.subState = false;
	}
};

/**
 * 初始化
 */
jQuery(function($) {
	$("#updateDept").click(departmentEdit.updateDept);
	departmentEdit.deptUpdateObj = new JCFF.JCTree.Lazy({
		title: '人员选择树',
		container: 'userUpdateDivId',
		controlId: 'update-leaderId',
		single: true,
		callback: function(obj){
			leaderName = obj ? obj.text : '';
		},
		ready: function(){
			deptDefer.resolve();
		},
        funFormData: function(){
            return { weight : '0' }
        }
	});
});
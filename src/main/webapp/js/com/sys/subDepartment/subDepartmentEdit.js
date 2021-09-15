//初始化方法
var subDepartmentEdit = {};


//重复提交标识
subDepartmentEdit.subState = false;
subDepartmentEdit.deptUpdateObj = null;
var leaderName = "";
/**
 * 修改组织
 */
subDepartmentEdit.updateDept = function(){
	/*var pWeight = $("#subDepartmentUpdateForm #parentWeight").val();
	var dWeight = $("#subDepartmentUpdateForm #weight").val();
	if(parseInt(pWeight) < parseInt(dWeight)){
		msgBox.info({content:"权重系数不能大于上级部门", type:'fail'});
		return;
	}*/
	if(subDepartmentEdit.subState)	return;
	subDepartmentEdit.subState = true;
	if ($("#subDepartmentUpdateForm").valid()) {
		var url = getRootPath()+"/sys/subDepartment/update.action";
		var formData = $("#subDepartmentUpdateForm").serializeArray();

		jQuery.ajax({
			url : url,
			type : 'POST',
			data : formData,
			success : function(data) {
				subDepartmentEdit.subState = false;
				if(data.success == "true"){
					$("#subDepartmentUpdateForm #deptToken").val(data.token);
					$("#subDepartmentForm #deptToken").val(data.token);
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
					$("#deptName").html(data.dept.name); //部门名称
					$("#leaderName").html(data.dept.leaderId == 0 ? "" : leaderName);//负责人
					$("#deptType").html(data.dept.deptType == 0 ? "部门" : "机构");//组织类型
					$("#deptId").val(data.dept.id);
					treeObj.selectNode(nodes[0], false);
					treeObj.setting.callback.onClick(null, nodes[0].id, nodes[0]);
					$('#update-dept').modal('hide');
				} else {
					$("#subDepartmentUpdateForm #deptToken").val(data.token);
					$("#subDepartmentForm #deptToken").val(data.token);
					if(data.errorMessage != null && data.errorMessage != ""){
						msgBox.info({content: data.errorMessage, type:'fail'});
					} else {
						msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
					}
				}
			},
			error : function() {
				subDepartmentEdit.subState = false;
				msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			}
		});
	}else{
		msgBox.info({content: $.i18n.prop("JC_SYS_118"), type:'fail'});
		subDepartmentEdit.subState = false;
	}
}

$(document).ready(function(){
	$("#updateDept").click(subDepartmentEdit.updateDept);//修改

	subDepartmentEdit.deptUpdateObj = JCTree.init({
		container:"leaderIdEditDiv",
		controlId:"leaderIdEdit-leaderId",
		isCheckOrRadio:false,
		callback    : function(obj){
            leaderName = obj ? obj.text : '';
		}
	});

});
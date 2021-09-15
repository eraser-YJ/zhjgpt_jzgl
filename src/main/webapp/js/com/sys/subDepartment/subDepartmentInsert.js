//初始化方法
var subDepartmentInsert = {};


//重复提交标识
subDepartmentInsert.subState = false;
subDepartmentInsert.deptNewObj = null;
/**
 * 保存部门信息
 */
subDepartmentInsert.saveDept = function(){

	/*var pWeight = $("#subDepartmentForm #parentWeight").val();
	var dWeight = $("#subDepartmentForm #weight").val();
	if(parseInt(pWeight) < parseInt(dWeight)){
		msgBox.info({content:"权重系数不能大于上级部门", type:'fail'});
		return;
	}*/
	if(subDepartmentInsert.subState)return;
	subDepartmentInsert.subState = true;
	if ($("#subDepartmentForm").valid()) {
		var url = getRootPath()+"/sys/subDepartment/save.action";
		var formData = $("#subDepartmentForm").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : formData,
			success : function(data) {
				subDepartmentInsert.subState = false;
				if(data.success == "true"){
					$("#subDepartmentForm #deptToken").val(data.token);
					$("#subDepartmentUpdateForm #deptToken").val(data.token);
					msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
					$("input[type=reset]").trigger("click");
					var o = data.dept;
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodes = treeObj.getSelectedNodes();
					var newNode = {
						id : o.id,
						pId : o.parentId,
						name : o.name,
						deptType : o.deptType,
						checkState : false,
						iconSkin : o.deptType==0 ? "fa-flag" : "fa-office",
						leaderName: o.displayName,
						isChecked: o.isChecked,
						userType: o.userType,
						queue: o.queue
					};
					newNode = treeObj.addNodes(nodes[0], newNode);
					if(nodes[0].children != null && nodes[0].children.length > 0){
						var childrens = nodes[0].children;
						var nNode = childrens[childrens.length-1];
						for(var i=childrens.length-1;i>=0;i--){
							if(nNode.queue < childrens[i].queue){
								treeObj.moveNode(childrens[i], nNode, "prev");
							}
						}
					}
					treeObj.setting.callback.onClick(null, nodes[0].id, nodes[0]);
					$('#add-dept').modal('hide');
				} else {
					$("#subDepartmentForm #deptToken").val(data.token);
					$("#subDepartmentUpdateForm #deptToken").val(data.token);
					if(data.errorMessage != null && data.errorMessage != ""){
						msgBox.info({content: data.errorMessage, type:'fail'});
					} else {
						msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
					}
				}
			},
			error : function() {
				subDepartmentInsert.subState = false;
				msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			}
		});
	}else{
		msgBox.info({content:$.i18n.prop("JC_SYS_118"), type:'fail'});
		subDepartmentInsert.subState = false;
	}
}

$(document).ready(function(){
	$("#saveDept").click(subDepartmentInsert.saveDept);//新增

	subDepartmentInsert.deptNewObj = JCTree.init({
		container:"nameInsertDiv",
		controlId:"nameInsert-name",
		isCheckOrRadio:false,
		isPersonOrOrg:false,
		callback:function(obj){
			$("#subDepartmentForm #id").val(obj.id);
		}
	});
	var leaderIdJcTree = JCTree.init({
		container:"leaderIdInsertDiv",
		controlId:"leaderIdInsert-leaderId",
		isCheckOrRadio:false
	});
});
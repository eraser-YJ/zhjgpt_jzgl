var departmentInsert = {};

departmentInsert.subState = false;
departmentInsert.deptNewObj = null;

/**
 * 保存部门信息
 */
departmentInsert.saveDept = function() {
	var pWeight = $("#departmentForm #parentWeight").val();
	var dWeight = $("#departmentForm #weight").val();
	if (parseInt(pWeight) < parseInt(dWeight)) {
		msgBox.info({content:"权重系数不能大于上级部门", type:'fail'});
		return;
	}
	if (departmentInsert.subState) return;
	departmentInsert.subState = true;
	if ($("#departmentForm").valid()) {
		jQuery.ajax({
			url: getRootPath() + "/department/save.action", type: 'POST', data: $("#departmentForm").serializeArray(),
			success : function(data) {
				departmentInsert.subState = false;
				if(data.success == "true"){
					$("#departmentForm #deptToken").val(data.token);
					$("#departmentUpdateForm #deptToken").val(data.token);
					msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
					$("input[type=reset]").trigger("click");
					var o = data.dept;
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodes = treeObj.getSelectedNodes();
					var newNode = {
						id : o.id, pId : o.parentId, name : o.name, deptType : o.deptType,
						checkState : false, iconSkin : o.deptType==0 ? "fa-flag" : "fa-office",
						leaderName: o.displayName, isChecked: o.isChecked, userType: o.userType,
						queue: o.queue
					};
					treeObj.addNodes(nodes[0], newNode);
					if (nodes[0].children != null && nodes[0].children.length > 0) {
						var childrens = nodes[0].children;
						var nNode = childrens[childrens.length-1];
						for (var i = childrens.length - 1; i >= 0; i--) {
							if (nNode.queue < childrens[i].queue) {
								treeObj.moveNode(childrens[i], nNode, "prev");
							}
						}
					}
					treeObj.setting.callback.onClick(null, nodes[0].id, nodes[0]);
					$('#add-dept').modal('hide');
				} else {
					$("#departmentForm #deptToken").val(data.token);
					$("#departmentUpdateForm #deptToken").val(data.token);
					if (data.errorMessage != null && data.errorMessage != "") {
						msgBox.info({content: data.errorMessage, type:'fail'});
					} else {
						msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
					}
				}
			},
			error : function() {
				departmentInsert.subState = false;
				msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			}
		});
	} else {
		msgBox.info({content:$.i18n.prop("JC_SYS_118"), type:'fail'});
		departmentInsert.subState = false;
	}
};

/**
 * 初始化
 */
jQuery(function($) {
	$("#saveDept").click(departmentInsert.saveDept);
	departmentInsert.deptNewObj = new JCFF.JCTree.Lazy({
		title: '人员选择树',
		container: 'userInsertDivId',
		controlId: 'insert-leaderId',
		single: true,
		ready: function(){
			deptDefer.resolve();
		},
        funFormData: function(){
            return { weight : '0' }
        }
	});
});
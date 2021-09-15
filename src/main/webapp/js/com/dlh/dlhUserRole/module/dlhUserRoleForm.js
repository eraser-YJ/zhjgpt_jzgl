//初始化方法
var dlhUserRoleModule = {};




//重复提交标识
dlhUserRoleModule.subState = false;
dlhUserRoleModule.userIds  = "";
dlhUserRoleModule.userNames = "";
//显示表单弹出层
// dlhUserRoleModule.show = function (id){
// 	$("#id").val(0);
// 	$("#userId").val(id);
// 	dlhUserRoleModule.clearForm();
// 	$('#myModal-edit').modal('show');
// };

//获取修改信息
dlhUserRoleModule.get = function (id){
	$("#userId").val(id);
	$.ajax({
		type : "GET",
		url : getRootPath()+"/dlh/dlhUserRole/getList.action",
		data : {"userId": id},
		dataType : "json",
		success : function(data) {
			if (data!=null) {
				//清除验证信息
				hideErrorMessage();
				dlhUserRoleModule.clearForm();
				var pData = [];
				for (var k = 0; k < data.length; k++){
					var obj = data[k];
					pData.push({
						id : obj.dataId,                    // ID
						text : obj.objName         // 显示的内容
					});
					dlhUserRoleModule.userIds+= obj.id+",";
					dlhUserRoleModule.userNames+= obj.text+",";
				}
				dlhUserRoleModule.userIds =  dlhUserRoleModule.userIds.substring(0, dlhUserRoleModule.userIds.length-1);
				dlhUserRoleModule.userNames =  dlhUserRoleModule.userNames.substring(0, dlhUserRoleModule.userNames.length-1);
				dlhUserRoleModule.selectMutual.setData(pData);
				$('#myModal-edit').modal('show');
			}
		},
		error: function() {
			msgBox.info({
				type:"fail",
				content: "数据错误"
			});
		}
	});
};

//添加修改公用方法
dlhUserRoleModule.saveOrModify = function (hide) {
	if(dlhUserRoleModule.subState)return;
		dlhUserRoleModule.subState = true;
	if ($("#dlhUserRoleForm").valid()) {
		var url = getRootPath()+"/dlh/dlhUserRole/save.action";
		var formData = $("#dlhUserRoleForm").serializeArray();
		formData.push({"name": "dataIds", "value": dlhUserRoleModule.userIds});
		formData.push({"name": "dataNames", "value": dlhUserRoleModule.userNames});
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dlhUserRoleModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						dlhUserRoleModule.clearForm();
					}
					$("#token").val(data.token);
					dlhUserRoleList.dlhUserRoleList();
				} else {
					if(data.labelErrorMessage){
						showErrors("dlhUserRoleForm", data.labelErrorMessage);
					} else{
						msgBox.info({
							type:"fail",
							content: data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				dlhUserRoleModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dlhUserRoleModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
dlhUserRoleModule.clearForm = function(){
	var token = $("#dlhUserRoleForm #token").val();
	$('#dlhUserRoleForm')[0].reset();
	dlhUserRoleModule.userIds  = "";
	dlhUserRoleModule.userNames = "";
	$("#dlhUserRoleForm #token").val(token);
	hideErrorMessage();
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});

	dlhUserRoleModule.selectMutual = JCTree.mutual({
		container   : 'controlTree1',
		controlId   : 'name1-name',
		url   : getRootPath()+'/dlh/dlhUserRole/treeList.action',
		callback :  function(node){
			//每次选择回调清空重新添加
			dlhUserRoleModule.userIds  = "";
			dlhUserRoleModule.userNames = "";
            if(node.length>0){
                for (var k = 0; k < node.length; k++){
                    var obj = node[k];
                    dlhUserRoleModule.userIds+= obj.id+",";
                    dlhUserRoleModule.userNames+= obj.text+",";
                }
                dlhUserRoleModule.userIds =  dlhUserRoleModule.userIds.substring(0, dlhUserRoleModule.userIds.length-1);
                dlhUserRoleModule.userNames =  dlhUserRoleModule.userNames.substring(0, dlhUserRoleModule.userNames.length-1);
            }
		}
	});

	$("#saveAndClose").click(function(){dlhUserRoleModule.saveOrModify(true);});
	$("#saveOrModify").click(function(){dlhUserRoleModule.saveOrModify(false);});
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});
});
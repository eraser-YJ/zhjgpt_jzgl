//初始化方法
var subRoleGroupModule = {};




//重复提交标识
subRoleGroupModule.subState = false;

//显示表单弹出层
subRoleGroupModule.show = function (){
	$("#saveAndClose").attr("class","btn dark");
	//$("#saveOrModify").show();
	$("#id").val(0);
    $("#count").html(30-$("#subRoleGroupForm #groupDescription").val().length);
	subRoleGroupModule.clearForm();
    $("#titleSubGroupId").html("新增");
	$('#myModal-edit').modal('show');
};

//获取修改信息
subRoleGroupModule.get = function (id) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/subRoleGroup/get.action",
		data : {"id": id},
		dataType : "json",
		success : function(data) {
			if (data) {
				//清除验证信息
				hideErrorMessage();
				subRoleGroupModule.clearForm();

				$("#subRoleGroupForm").fill(data);

                $("#count").html(30-$("#subRoleGroupForm #groupDescription").val().length);
				//$("#saveOrModify").hide();
				$("#saveAndClose").attr("class","btn dark");
                $("#titleSubGroupId").html("编辑");
				$('#myModal-edit').modal('show');
			}
		}
	});
};

//添加修改公用方法
subRoleGroupModule.saveOrModify = function (hide) {
	if(subRoleGroupModule.subState)return;
		subRoleGroupModule.subState = true;
	if ($("#subRoleGroupForm").valid()) {
		var url = getRootPath()+"/sys/subRoleGroup/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath()+"/sys/subRoleGroup/update.action";
		}
		var formData = $("#subRoleGroupForm").serializeArray();
		
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				subRoleGroupModule.subState = false;
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						subRoleGroupModule.clearForm();
					}
					$("#token").val(data.token);
					subRoleGroupList.subRoleGroupList();
				} else {
					if(data.labelErrorMessage){
						showErrors("subRoleGroupForm", data.labelErrorMessage);
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
				subRoleGroupModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subRoleGroupModule.subState = false;
		msgBox.info({
			content:$.i18n.prop("JC_SYS_067")
		});
	}
};

//清空表单数据
subRoleGroupModule.clearForm = function(){
	var token = $("#subRoleGroupForm #token").val();
	$('#subRoleGroupForm')[0].reset();

	$("#subRoleGroupForm #token").val(token);
	hideErrorMessage();
};

//鼠标右键粘贴事件
$.fn.pasteEvents = function( delay ) {
    if (delay == undefined) delay = 20;
    return $(this).each(function() {
        var $el = $(this);
        $el.on("paste", function() {
            $el.trigger("prepaste");
            setTimeout(function() { $el.trigger("postpaste"); }, delay);
        });
    });
};

//改变输入字数显示
subRoleGroupModule.checkLen  =  function (obj)
{
    var str = $("#subRoleGroupForm #groupDescription").val();
    var namelength = str.length;
    var maxChars = 30-namelength;//最多字符数
    if (obj.value.length > 30){
        obj.value = obj.value.substring(0,30);
        maxChars = 0;
    }
    var curr = maxChars;
    $("#subRoleGroupForm #count").html(curr.toString());
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});



	$("#saveAndClose").click(function(){subRoleGroupModule.saveOrModify(true);});
	/*$("#saveOrModify").click(function(){subRoleGroupModule.saveOrModify(false);});*/
	$("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});

    //内容字数递减控制
    $("#subRoleGroupForm #groupDescription").on("postpaste", function() { subRoleGroupModule.checkLen (this);}).pasteEvents();
    $("#subRoleGroupForm #groupDescription").keyup(function() { subRoleGroupModule.checkLen (this);});
});
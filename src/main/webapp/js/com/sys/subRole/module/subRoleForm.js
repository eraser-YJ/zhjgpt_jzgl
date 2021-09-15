//初始化方法
var subRoleModule = {};

// 重复提交标识
subRoleModule.subState = false;

// 显示表单弹出层
subRoleModule.show = function() {
	$("#saveAndClose").attr("class", "btn dark");
	//$("#saveOrModify").show();
	$("#id").val(0);
	subRoleModule.clearForm();
    $("#count").html(30-$("#subRoleForm #roleDescription").val().length);
    $("#titleSubRoleId").html("新增");
	$('#myModal-edit').modal('show');

    var deptId = $('#subRoleQueryForm #currentDeptId').val();
    var deptName = $('#subRoleQueryForm #deptName').val();
    //如果是部门同步到添加页面
    if(deptId != "" && deptName != ""){
        $('#subRoleForm #deptId').val(deptId);
        $('#subRoleForm #deptNameTd').html(deptName);
    }
};

// 获取修改信息
subRoleModule.get = function(id) {
	$.ajax({
		type : "GET",
		url : getRootPath() + "/sys/subRole/get.action",
		data : {
			"id" : id
		},
		dataType : "json",
		success : function(data) {
			if (data) {
				// 清除验证信息
				hideErrorMessage();
				subRoleModule.clearForm();
				$("#subRoleForm").fill(data);

                $("#count").html(30-$("#subRoleForm #roleDescription").val().length);
				$("#deptNameTd").html(data.deptName);
				$("#saveAndClose").attr("class", "btn dark");
                $("#titleSubRoleId").html("编辑");
				$('#myModal-edit').modal('show');
			}
		}
	});
};

// 添加修改公用方法
subRoleModule.saveOrModify = function(hide) {
	if (subRoleModule.subState)
		return;
	subRoleModule.subState = true;
	if ($("#subRoleForm").valid()) {
		var url = getRootPath() + "/sys/subRole/save.action";
		if ($("#id").val() != 0) {
			url = getRootPath() + "/sys/subRole/update.action";
		}
		var formData = $("#subRoleForm").serializeArray();

		jQuery.ajax({
			url : url,
			type : 'POST',
			cache : false,
			data : formData,
			success : function(data) {
				subRoleModule.subState = false;
				if (data.success == "true") {
					msgBox.tip({
						type : "success",
						content : data.successMessage
					});
					if (hide) {
						$('#myModal-edit').modal('hide');
					} else {
						subRoleModule.clearForm();
					}
					$("#token").val(data.token);
					subRoleList.subRoleList();
				} else {
					if (data.labelErrorMessage) {
						showErrors("subRoleForm", data.labelErrorMessage);
					} else {
						msgBox.info({
							type : "fail",
							content : data.errorMessage
						});
					}
					$("#token").val(data.token);
				}
			},
			error : function() {
				subRoleModule.subState = false;
				msgBox.tip({
					type : "fail",
					content : $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		subRoleModule.subState = false;
		msgBox.info({
			content : $.i18n.prop("JC_SYS_067")
		});
	}
};

// 清空表单数据
subRoleModule.clearForm = function() {
	var token = $("#subRoleForm #token").val();
	$('#subRoleForm')[0].reset();

	$("#subRoleForm #token").val(token);
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
subRoleModule.checkLen  =  function (obj)
{
    var str = $("#subRoleForm #roleDescription").val();
    var namelength = str.length;
    var maxChars = 30-namelength;//最多字符数
    if (obj.value.length > 30){
        obj.value = obj.value.substring(0,30);
        maxChars = 0;
    }
    var curr = maxChars;
    $("#subRoleForm #count").html(curr.toString());
};

$(document).ready(function() {
	ie8StylePatch();
	$(".datepicker-input").each(function() {
		$(this).datepicker();
	});
	$("#deptId").val($("#currentDeptId").val());
	$("#deptNameTd").html($("#deptName").val());
	$("#saveAndClose").click(function() {
		subRoleModule.saveOrModify(true);
	});
	$("#formDivClose").click(function() {
		$('#myModal-edit').modal('hide');
	});
    //内容字数递减控制
    $("#subRoleForm #roleDescription").on("postpaste", function() { subRoleModule.checkLen (this);}).pasteEvents();
    $("#subRoleForm #roleDescription").keyup(function() { subRoleModule.checkLen (this);});
});
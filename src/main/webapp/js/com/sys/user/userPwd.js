var userPwd = {};

//重复提交标识
userPwd.subState = false;

userPwd.modify = function () {
    if (userPwd.subState) return;
    userPwd.subState = true;
    if ($("#userPwdForm").valid()) {
        var url = getRootPath() + "/sys/user/userPwdModify.action";
        var formData = $("#userPwdForm").serializeArray();
        jQuery.ajax({
            url: url,
            type: 'POST',
            data: serializeJson(formData),
            success: function (data) {
                userPwd.subState = false;
                $("#token").val(data.token);
                if (data.success == "true") {
                    $("#password").val("");
                    $("#newPassword").val("");
                    $("#confirmPassword").val("");
                    msgBox.tip({
                        type: "success",
                        content: data.successMessage,
                        callback: function () {
                            //当你在iframe页面关闭自身时
                            var layer = parent.layer;
                            if (layer) {
                                var index = layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //再执行关闭
                            }
                        }
                    });

                    //隐藏强制修改密码对话框
                    $('#dPasswordModal').modal('hide');
                } else {
                    if (data.labelErrorMessage) {
                        showErrors("userPwdForm", data.labelErrorMessage);
                    } else {
                        msgBox.info({
                            content: data.errorMessage
                        });
                    }
                }
            },
            error: function () {
                user.subState = false;
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_002")
                });
            }
        });
    } else {
        userPwd.subState = false;
    }
};

//初始化
jQuery(function ($) {
    $("#saveBtn").click(userPwd.modify);

    /**
     * 特殊符号输入限制
     */
    jQuery.validator.addMethod("pwdN", function (value, element) {
        var pwdType = $("#pwdType").val();
        if(pwdType == 0){
            return this.optional(element) || /^(?:\d+|[a-zA-Z]+|[!@#$%^&*]+)$/.test(value);
        }else if(pwdType == 2){
            return this.optional(element) || /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\d!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/.test(value);
        }else {
            return this.optional(element) || /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/.test(value);
        }
    }, "请输入合法的密码");
});

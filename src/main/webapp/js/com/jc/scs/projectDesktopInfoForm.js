var projectDesktopInfoJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
projectDesktopInfoJsForm.init = function() {
    projectDesktopInfoJsForm.operatorModal('show');
};
projectDesktopInfoJsForm.videoBtnFun = function() {
    var codes = $('#nowProjectCode').val();
    $.ajax({
        type : "POST",
        url : getRootPath() + "/monitors/manage/checkCodes.action",
        data : {codes: codes},
        dataType : "json",
        success : function(data) {
            if (data) {
                if(data.code == '200'){
                    var url = getRootPath() +"/monitors/manage/realtimeInfo.action";
                    window.open(encodeURI(url));
                } else if (data.code == '444'){
                    msgBox.tip({content: "部分所选项目无视频设备，已经过滤,请查看",callback:function () {
                            var url = getRootPath() + "/monitors/manage/playbackInfo.action";
                            window.open(encodeURI(url));
                    }});

                } else {
                    msgBox.info({content: data.message});
                }

            }
        }
    });
};

/**
 * 操作窗口开关
 * @param operator
 */
projectDesktopInfoJsForm.operatorModal = function (operator) {
     $('#form-modal').modal(operator); 
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { projectDesktopInfoJsForm.operatorModal('hide'); });
    $('#videoBtn') .click(function () { projectDesktopInfoJsForm.videoBtnFun(); });
});
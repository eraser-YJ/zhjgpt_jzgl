var cyryJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
cyryJsForm.init = function() {
    var nowId = $("#nowId").val();
    var nowDisPath = $("#nowDisPath").val();
    $.ajax({
        type : "GET",
        data : {id: nowId,disPath:nowDisPath},
        dataType : "json",
        url : getRootPath() + "/mock/view/get.action",
        success : function(data) {
            if (data) {
                hideErrorMessage();
                $("#entityCyryForm").fill(data);
                if(data.class_jcrq){
                    var crtTime = new Date(data.class_jcrq);
                    $("#entityCyryForm #class_jcrq").val(dateFtt("yyyy-MM-dd", crtTime))
                }
                if(data.class_ccrq){
                    var crtTime = new Date(data.class_ccrq);
                    $("#entityCyryForm #class_ccrq").val(dateFtt("yyyy-MM-dd", crtTime))
                }
                cyryJsForm.operatorModal('show');
            }
        }
    });

};

/**
 * 操作窗口开关
 * @param operator
 */
cyryJsForm.operatorModal = function (operator) {
     $('#form-modal-Cyry').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    cyryJsForm.init();
    $('#closeCyryBtn') .click(function () { cyryJsForm.operatorModal('hide'); });
});

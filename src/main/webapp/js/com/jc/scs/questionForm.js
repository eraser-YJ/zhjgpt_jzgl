var questionJsForm = {};

/**
 * 初始化
 * @param config {
 *     title： 标题
 *     operator： 操作 add/modify
 *     id: 修改时的主键id
 * }
 */
questionJsForm.init = function() {
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
                $("#entityQuestionForm").fill(data);
                if(data.create_date){
                    var crtTime = new Date(data.create_date);
                    $("#entityQuestionForm #create_date").val(dateFtt("yyyy-MM-dd hh:mm:ss", crtTime))
                }
                questionJsForm.operatorModal('show');
            }
        }
    });

};

/**
 * 操作窗口开关
 * @param operator
 */
questionJsForm.operatorModal = function (operator) {
     $('#form-modal-Question').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    questionJsForm.init();
    $('#closeQuestionBtn') .click(function () { questionJsForm.operatorModal('hide'); });
});

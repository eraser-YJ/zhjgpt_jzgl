var viewJsForm = {};

//初始化
viewJsForm.init = function(config) {
    $('#entityFormTitle').html(config.title);
    viewJsForm.operatorModal('show');
};

//操作窗口开关
viewJsForm.operatorModal = function (operator) {
    $('#form-modal').modal(operator);
};

$(document).ready(function(){
    ie8StylePatch();
    $(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn') .click(function () { viewJsForm.operatorModal('hide'); });
});
 
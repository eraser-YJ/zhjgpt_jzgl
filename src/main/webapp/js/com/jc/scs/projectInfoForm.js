var projectInfoJsForm = {};

projectInfoJsForm.init = function() {
    var projectCode= $("#nowProjectCode").val();
    var url = getRootPath() +"/desktop/manage/projectInfo.action?projectCode=" + projectCode;
    $.ajax({
        type : "GET",
        dataType : "json",
        url : url,
        success : function(data) {
            if (data) {
                $("#entityProjectInfoForm").fill(data);
            }
        }
    });
};

$(document).ready(function(){
    ie8StylePatch();
    projectInfoJsForm.init();
	$(".datepicker-input").each(function(){$(this).datepicker();});
});

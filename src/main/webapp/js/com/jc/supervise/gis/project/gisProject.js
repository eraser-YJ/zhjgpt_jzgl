var projectListPanel = {};

projectListPanel.resource = 'pt_project_info';
projectListPanel.completion = null;
projectListPanel.projectName = null;

projectListPanel.look = function (value) {
    resourceDetailModal.detail({dataId: value, sign: projectListPanel.resource});
};


//查询项目信息
projectListPanel.queryProject = function() {
    $.ajax({
        url: getRootPath() + "/supervise/gis/projectInfoList.action", type: "POST",
        data : {},
        dataType : "json",
        success : function(data) {
            for (var i = 0; i < data.length; i++) {
                var row = data[i];
            }

        }
    });
};

$(document).ready(function(){

});
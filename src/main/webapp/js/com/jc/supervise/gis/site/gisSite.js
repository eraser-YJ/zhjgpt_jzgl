var projectSitePanel = {};
projectSitePanel.oTable = null;
projectSitePanel.oTableAoColumns = [];
projectSitePanel.resource = 'pt_project_info';
projectSitePanel.completion = null;
projectSitePanel.projectName = null;

projectSitePanel.videoBtnFun = function() {
    debugger
    var projectCode = $("#projectCode").val();//项目编号
    var url =  getScsAddress() + "/monitors/manage/checkCodes.action";
    $.ajax({
        type : "POST",
        url : getScsAddress() + "/monitors/manage/checkCodes.action",
        data : {codes: projectCode},
        dataType : "json",
        success : function(data) {
            if (data) {
                if(data.code == '200'){
                    var url = getScsAddress() +"/monitors/manage/playbackInfo.action";
                    window.open(encodeURI(url));
                } else if (data.code == '444'){
                    msgBox.tip({content: "部分所选项目无视频设备，已经过滤,请查看",callback:function () {
                            var url = getScsAddress() + "/monitors/manage/playbackInfo.action";
                            window.open(encodeURI(url));
                        }});

                } else {
                    msgBox.info({content: data.message});
                }

            }
        }
    });
};

//弹出塔式起重机查看设备页面
projectSitePanel.equipmentView = function() {
    var projectCode = $("#projectCode").val();//项目编号
    $("#equipmentDiv").html("");//先将div置空
    $("#equipmentDiv").load(getRootPath()+"/supervise/gis/tower/page.action?time="+(new Date().getTime()), null, function () {
        towerView.show();
        towerView.equipmentFun(projectCode);
    });
};

//弹出施工升降机查看设备页面
projectSitePanel.hoistView = function() {
    var projectCode = $("#projectCode").val();//项目编号
    $("#hoistDiv").html("");//先将div置空
    $("#hoistDiv").load(getRootPath()+"/supervise/gis/hoist/page.action?time"+(new Date().getTime()), null, function () {
        hoistView.show();
        hoistView.equipmentFun(projectCode);
    });
};



//施工升降机设备信息
projectSitePanel.equipmentHhoistFun = function(projectCode) {
    $.ajax({
        type : "GET",
        url : getScsAddress() + "/api/system/equipmentList.action",
        data : {
            projectCode: projectCode,
            equipmentType : "building_hoist"
        },
        dataType : "json",
        success : function(data) {
            for(var i=0;i<data.length;i++){
                var item = data[i];
                console.log(item.equipmentTypeValue);
            }
        }
    });
};


$(document).ready(function(){

});
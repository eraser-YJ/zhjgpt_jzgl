var projectInfoJsList = {};
projectInfoJsList.oTable = null;

projectInfoJsList.oTableFnServerParams = function(aoData){
    getTableParameters(projectInfoJsList.oTable, aoData);

    var projectNumberCondObj  = $('#searchForm #query_projectNumber').val();
    if (projectNumberCondObj.length > 0) { aoData.push({"name": "projectNumber", "value": projectNumberCondObj}); }
    var projectNameCondObj  = $('#searchForm #query_projectName').val();
    if (projectNameCondObj.length > 0) { aoData.push({"name": "projectName", "value": projectNameCondObj}); }
    aoData.push({"name": "hasMonitor", "value": 'Y'});
};

projectInfoJsList.oTableAoColumns = [    {
        mData: function (source) {
            return "<input type=\"checkbox\" name=\"ids\" value=" + source.projectNumber + ">";
        }, sTitle: "<input type=\"checkbox\" />", bSortable: false
    },
    {mData: 'projectNumber', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'buildDept', sTitle: '建设单位', bSortable: false},
    {mData: 'projectAddress', sTitle: '项目所在地', bSortable: false},
    {mData: 'realStartDate', sTitle: '实际开工时间', bSortable: false},
    {mData: 'realFinishDate', sTitle: '实际竣工时间', bSortable: false}
];

projectInfoJsList.renderTable = function () {
    if (projectInfoJsList.oTable == null) {
        projectInfoJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectInfoJsList.pageCount,
            "sAjaxSource": getRootPath() + "/monitors/manage/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectInfoJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectInfoJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectInfoJsList.oTable.fnDraw();
    }
};

projectInfoJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectInfoJsList.realtimeBtnFun = function () {
    var codesArr = [];
    $("[name='ids']:checked").each(function () {
        codesArr.push($(this).val());
    });
    var codes = codesArr.join(",");
    if (codes.length == 0) {
        msgBox.info({content: "请选择项目"});
        return;
    }

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
                            var url = getRootPath() + "/monitors/manage/realtimeInfo.action";
                            window.open(encodeURI(url));
                    }});

                } else {
                    msgBox.info({content: data.message});
                }

            }
        }
    });


};


$(document).ready(function () {
    projectInfoJsList.pageCount = TabNub > 0 ? TabNub : 10;
    projectInfoJsList.renderTable();
    $('#realtimeBtn').click(projectInfoJsList.realtimeBtnFun);
    $('#queryBtn').click(projectInfoJsList.renderTable);
    $('#resetBtn').click(projectInfoJsList.queryReset);
});
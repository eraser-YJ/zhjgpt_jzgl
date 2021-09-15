var projectInfoFabricatedList = {};
projectInfoFabricatedList.oTable = null;


projectInfoFabricatedList.oTableFnServerParams = function(aoData){
    getTableParameters(projectInfoFabricatedList.oTable, aoData);
    //组装查询条件
    var projectname = $('#projectname').val();
    if (projectname.length > 0) {
        aoData.push({'name': 'projectname', 'value': projectname});
    }
    var projectnumber = $('#projectnumber').val();
    if (projectnumber.length > 0) {
        aoData.push({'name': 'projectnumber', 'value': projectnumber});
    }
};

projectInfoFabricatedList.oTableAoColumns = [
    {mData: 'projectnumber', sTitle: '项目编号', bSortable: false},
    {mData: 'projectname', sTitle: '项目名称', bSortable: false},
    {mData: 'builddept', sTitle: '建设单位', bSortable: false},
    {mData: 'buildingTypesValue', sTitle: '建筑类型', bSortable: false},
    {mData: 'structureTypeValue', sTitle: '结构类型', bSortable: false},
    {mData: 'buildarea', sTitle: '面积(㎡)', bSortable: false},
    {mData: 'realstartdate', sTitle: '开工时间', bSortable: false}
];

projectInfoFabricatedList.renderTable = function () {
    if (projectInfoFabricatedList.oTable == null) {
        projectInfoFabricatedList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": projectInfoFabricatedList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProject/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": projectInfoFabricatedList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                projectInfoFabricatedList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        projectInfoFabricatedList.oTable.fnDraw();
    }
};

projectInfoFabricatedList.queryReset = function(){
    $('#searchForm')[0].reset();
};

projectInfoFabricatedList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/projectSgxk/excel.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};

projectInfoFabricatedList.echartsRegion = function() {
    var datax = [];
    var data1 = [];
    $.ajax({
        type: "GET", dataType: "json",async : false,
        url: getRootPath() + "/csmp/ptProject/queryEchartsForStructureType.action",
        success: function (data) {
            if (data != null) {
                if (data.length > 0) {
                    data.forEach((v, i) => {
                        data1.push({
                            name: v.value,
                            value: v.cc,
                        });
                        datax.push(v.value);
                    })
                }
            }
        }
    });
    var myCharts = echarts.init(document.getElementById('region'));
    var option = {
        title: {
            top:10,
            text: '项目结构类型统计',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        legend: {
            orient: 'vertical',
            top: "center",
            right: "5%",
            data: datax,
            textStyle: {
                fontSize: 16
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
        },
        series: [{
            type: 'pie',
            radius: '70%',
            center: ['40%', '50%'],
            label: {
                normal: {
                    position: 'inner',
                    formatter: params => {
                    return (
                        params.percent.toFixed(0) + '%'
        );
        },
            fontSize: 12
        }
        },
            data: data1
        }]
        };
    myCharts.setOption(option);
};
projectInfoFabricatedList.echarts = function(){
    var datax = [];
    var data1 = [];
    $.ajax({
        type: "GET", dataType: "json",async : false,
        url: getRootPath() + "/csmp/ptProject/queryEchartsForBuildingTypes.action",
        success: function (data) {
            if (data != null) {
                if (data.length > 0) {
                    data.forEach((v, i) => {
                        datax.push(v.value);
                        data1.push(v.cc);
                     })
                }
            }
        }
    });

    var myCharts1 = echarts.init(document.getElementById('appStatus'));
    var option1 = {
        title: {
            top:10,
            text: '项目建筑类型统计',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        tooltip: {
            trigger: 'axis',
            show: false,
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
        },
        legend: {
            selectedMode: false,
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [{
            splitLine: {
                show: false
            },
            type: 'value',
            show: false,
        }],
        yAxis: [{
            splitLine: {
                show: false
            },
            axisLine: { //y轴
                show: false
            },
            type: 'category',
            axisTick: {
                show: false
            },
            data: datax,
            axisLabel: {

            }
        }],
        series: [{
            type: 'bar',
            barWidth: 20, // 柱子宽度
            label: {
                show: true,
                position: 'right', // 位置
                color: '#1CD8A8',
                fontSize: 14,
                fontWeight: 'bold', // 加粗
                distance: 5 // 距离
            }, // 柱子上方的数值
            itemStyle: {
                barBorderRadius: [0, 20, 20, 0], // 圆角（左上、右上、右下、左下）
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                    '#2FAEF2', '#1CD8A8'
                ].map((color, offset) => ({
                    color,
                    offset
                }))), // 渐变
},
    data: data1
}, ]
};
    myCharts1.setOption(option1);
};

$(document).ready(function(){
    projectInfoFabricatedList.pageCount = TabNub > 0 ? TabNub : 10;
    projectInfoFabricatedList.renderTable();
    projectInfoFabricatedList.echarts();
    projectInfoFabricatedList.echartsRegion();
    $('#excel').click(projectInfoFabricatedList.excel);
    $('#queryBtn').click(projectInfoFabricatedList.renderTable);
    $('#resetBtn').click(projectInfoFabricatedList.queryReset);
});
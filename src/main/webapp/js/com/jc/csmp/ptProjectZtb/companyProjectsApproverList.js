var companyProjectsApproverList = {};
companyProjectsApproverList.oTable = null;


companyProjectsApproverList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsApproverList.oTable, aoData);
    //组装查询条件
    var projectName = $('#projectName').val();
    if (projectName.length > 0) {
        aoData.push({'name': 'projectName', 'value': projectName});
    }
    var pcpArea = $('#pcpArea').val();
    if (pcpArea.length > 0) {
        aoData.push({'name': 'pcpArea', 'value': pcpArea});
    }
};

companyProjectsApproverList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'pcpProjectNum', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'projectType', sTitle: '阶段类型', bSortable: false},
    {mData: 'pcpAreaValue', sTitle: '区域', bSortable: false},
    {mData: 'biddingDljgbm', sTitle: '编号', bSortable: false},
    {mData: 'pcpZbrq', sTitle: '日期', bSortable: false}

];

companyProjectsApproverList.renderTable = function () {
    if (companyProjectsApproverList.oTable == null) {
        companyProjectsApproverList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsApproverList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageProjectApproverList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsApproverList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsApproverList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsApproverList.oTable.fnDraw();
    }
};
companyProjectsApproverList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excelApprover.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsApproverList.queryReset = function(){
    $('#searchForm')[0].reset();
};
companyProjectsApproverList.echarts = function(){
    var myCharts = echarts.init(document.getElementById('ztbType'));
    var nameArr= [];
    var valueArr1= [];
    var valueArr2= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProject/queryProjectJd.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].name);
                    valueArr1.push(data[i].cc1);
                    valueArr2.push(data[i].cc2);
                }
            }
        }
    });
    var option ={
        color: ['RGB(1,255,152)', 'RGB(244,191,70)'],
        title: {
            text: '项目办理情况',
            top:10,
            left:10,
            textStyle:{
                color:'black',
                fontSize:18
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        animation: false,
        "grid": {
            "top": "67",
            "left": "32",
            "bottom": "15",
            "right": "39",
            "containLabel": true
        },
        "legend":{
            "show":true,
            top: 35,
            right: 26,
            data:['已办项目数','未办项目数'],
            textStyle:{
                color: "rgba(4,3,4,0.96)"
            }
        },
        "xAxis": [{
            "type": "category",
            // "data": ["三角函数", "数列","平面向量","不等式","立体几何"],
            data:nameArr,
            "axisTick": {
                "alignWithLabel": true
            },
            "nameTextStyle": {
                "color": "rgba(4,3,4,0.96)"
            },
            "axisLine": {
                "lineStyle": {
                    "color": "RGB(47,68,114)"
                }
            },
            "axisLabel": {
                "textStyle": {
                    "color": "rgba(4,3,4,0.96)"
                },
                margin: 20
            }
        }],
        "yAxis": [{
            "type": "value",
            "axisLabel": {
                "textStyle": {
                    "color": "rgba(4,3,4,0.96)"
                },
                "formatter": "{value}"
            },
            "splitLine": {
                "lineStyle": {
                    "type": "dashed",
                    "color": "RGB(47,68,114)"
                }
            },
            "axisLine": {
                "show": true,
                "lineStyle": {
                    "color": "RGB(47,68,114)"
                }
            }
        }],
        "series": [
            {
                name:'已办项目数',
                type: 'bar',
                //silent: true,
                "barWidth": "12",
                label: {
                    normal: {
                        show: true,
                        fontSize: 14,
                        color: '#333',
                        position: 'top',
                    }
                },
                //barGap: '-100%', // Make series be overlap
                "data": valueArr1
            },
            {
                name:'未办项目数',
                type: 'bar',
                //silent: true,
                "barWidth": "12",
                label: {
                    normal: {
                        show: true,
                        fontSize: 14,
                        color: '#333',
                        position: 'top',
                    }
                },
                //barGap: '-100%', // Make series be overlap
                "data":valueArr2
            }
        ]
    };

    myCharts.setOption(option);
};
companyProjectsApproverList.echartsAverageDay = function(){
    var myCharts = echarts.init(document.getElementById('AverageDay'));
    var nameArr= [];
    var valueArr= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProject/queryAverageDay.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].name);
                    valueArr.push(data[i].cc);
                }
            }
        }
    });
    var option = {
        title: {
            text: '项目办理平均用时',
            top:10,
            left:10,
            textStyle:{
                color:'black',
                fontSize:18
            }
        },
        grid: {
            top: '30%',
            right: '6%',
            left: '8%',
            bottom: '16%'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        xAxis: [{
            type: 'category',
            color: '#59588D',
            // data: ['2016', '2017', '2018', '2019', '2020'],
            data:nameArr,
            axisLabel: {
                color: '#040304',
                textStyle: {
                    fontSize: 12
                },
            },
            axisLine: {
                lineStyle: {
                    color: '#d2d2d2',
                }
            },
            axisTick: {
                show: false
            },
        }],
        yAxis: [{
            name: '单位:(天)',
            axisLabel: {
                color: '#040304',
                textStyle: {
                    fontSize: 12
                },
            },
            axisLine: {
                lineStyle: {
                    color: 'rgba(107,107,107,0.37)',
                }
            },
            axisTick: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: 'rgba(131,101,101,0.2)',
                    type: 'dashed',
                }
            }
        }],
        series: [{
            type: 'bar',
            // data: [40, 90, 10, 20, 56],
            data:valueArr,
            barWidth: '20px',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#FF9A22' // 0% 处的颜色
                    }, {
                        offset: 1,
                        color: '#FFD56E' // 100% 处的颜色
                    }], false),
                    barBorderRadius: [30, 30, 0, 0],
                }
            },
            label: {
                show: true,
                fontSize: 12,
                position: 'top',
                color: 'blue',
                // formatter: (params)=>{//单独对第一个label使用样式
                //     if (params.dataIndex === 0) {
                //         return '{a|'+params.value+'}'
                //     }
                // },
                // rich: {//使用富文本编辑字体样式
                //     a: {
                //         color: 'red'
                //     }
                // }
            }
        }]
    };

    myCharts.setOption(option);
};
companyProjectsApproverList.echartsAverageAccept = function(){
    var myCharts = echarts.init(document.getElementById('AverageAccept'));
    var valueArr= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProject/queryAverageAccept.action",
        success: function (data) {
            if (data != null) {
                valueArr.push(data.cc);
                valueArr.push(data.cc1);
                valueArr.push(data.cc2);
            }
        }
    });
    var option = {
        title: {
            text: '项目审批平均受理次数',
            top:10,
            left:10,
            textStyle:{
                color:'black',
                fontSize:18
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            top: '23%',
            right: '3%',
            left: '10%',
            bottom: '15%'
        },
        xAxis: [{
            type: 'category',
            data: ['立项许可', '施工许可', '竣工验收'],
            axisLine: {
                lineStyle: {
                    color: 'rgba(255,255,255,0.12)'
                }
            },
            axisLabel: {
                color: '#040304',
                textStyle: {
                    fontSize: 12
                },
            },
        }],
        yAxis: [{
            axisLabel: {
                color: '#040304',
            },
            axisLine: {
                lineStyle: {
                    color: 'rgba(107,107,107,0.37)',
                }
            },
            splitLine: {
                lineStyle: {
                    color: 'rgba(131,101,101,0.2)',
                    type: 'dashed',
                }
            }
        }],
        series: [{
            type: 'bar',
            // data: [5000, 2600, 1300, 1300, 1250, 1500],
            data:valueArr,
            barWidth: '20px',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgba(0,244,255,1)' // 0% 处的颜色
                    }, {
                        offset: 1,
                        color: 'rgba(0,77,167,1)' // 100% 处的颜色
                    }], false),
                    barBorderRadius: [30, 30, 30, 30],
                    shadowColor: 'rgba(0,160,221,1)',
                    shadowBlur: 4,
                }
            },
            label: {
                show: true,
                fontSize: 12,
                position: 'top',
                color: 'black',
            }
        }]
    };

    myCharts.setOption(option);
};
companyProjectsApproverList.echartsProjectPass = function(){
    var myCharts = echarts.init(document.getElementById('ProjectPass'));
    var nameArr= [];
    var valueArr= [];
    var valueArr1= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProject/queryProjectPass.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].name);
                    valueArr.push(data[i].cc);
                    valueArr1.push(data[i].cc1);
                }
            }
        }
    });
    var option = {
        title: {
            text: '项目审批情况',
            top:10,
            left:10,
            textStyle:{
                color:'black',
                fontSize:18
            }
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['常规办理', '退件'],
            align: 'right',
            right: 10,
            top: 10,
            textStyle: {
                color: "black"
            },
            itemWidth: 10,
            itemHeight: 10,
            itemGap: 35
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true
        },
        xAxis: [{
            type: 'category',
            data:nameArr,
            axisLine: {
                show: true,
                lineStyle: {
                    color: "black",
                    width: 1,
                    type: "solid"
                }
            },
            axisTick: {
                show: false,
            },
            axisLabel: {
                show: true,
                textStyle: {
                    color: "black",
                }
            },
        }],
        yAxis: [{
            type: 'value',
            axisLabel: {
                formatter: '{value} '
            },
            axisTick: {
                show: false,
            },
            axisLine: {
                show: false,
                lineStyle: {
                    color: "black",
                    width: 1,
                    type: "solid"
                },
            },
            splitLine: {
                lineStyle: {
                    color: "black",
                }
            }
        }],
        series: [{
            name: '常规办理',
            type: 'bar',
            // data: [20, 50, 80, 58, 83, 68, 57, 80, 42, 66],
            data:valueArr,
            barWidth: 10, //柱子宽度
            barGap: 1, //柱子之间间距
            label: {
                show: true,
                fontSize: 12,
                position: 'top',
                color: 'black',
            },
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#008cff'
                    }, {
                        offset: 1,
                        color: '#005193'
                    }]),
                    opacity: 1,
                }
            }
        }, {
            name: '退件',
            type: 'bar',
            // data: [50, 70, 60, 61, 75, 87, 60, 62, 86, 46],
            data:valueArr1,
            barWidth: 10,
            barGap: 1,
            label: {
                show: true,
                fontSize: 12,
                position: 'top',
                color: 'black',
            },
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#00da9c'
                    }, {
                        offset: 1,
                        color: '#007a55'
                    }]),
                    opacity: 1,
                }
            }
        }]
    };

    myCharts.setOption(option);
};

$(document).ready(function(){
    companyProjectsApproverList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsApproverList.renderTable();
    companyProjectsApproverList.echarts();
    companyProjectsApproverList.echartsAverageDay();
    companyProjectsApproverList.echartsAverageAccept();
    companyProjectsApproverList.echartsProjectPass();
    $('#excel').click(companyProjectsApproverList.excel);
    $('#queryBtn').click(companyProjectsApproverList.renderTable);
    $('#resetBtn').click(companyProjectsApproverList.queryReset);
});
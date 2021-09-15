var companyProjectsSgxkJsList = {};
companyProjectsSgxkJsList.oTable = null;


companyProjectsSgxkJsList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsSgxkJsList.oTable, aoData);
};

companyProjectsSgxkJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'pcpBdmc', sTitle: '施工许可证号', bSortable: false},
    {mData: 'projectname', sTitle: '工程名称', bSortable: false},
    {mData: 'projectAreaValue', sTitle: '区域', bSortable: false},
    {mData: 'builddept', sTitle: '建设单位', bSortable: false},
    {mData: 'constructionOrganization', sTitle: '施工单位', bSortable: false},
    {mData: 'projectmanager', sTitle: '项目经理', bSortable: false},
    {mData: 'pcpBzry', sTitle: '办证人员', bSortable: false},
    {mData: 'projectaddress', sTitle: '工程地址', bSortable: false},
    {mData: 'pcpHtbh', sTitle: '面积（平方米)', bSortable: false},
    {mData: 'projectmoney', sTitle: '投资金额（万元）', bSortable: false},
    {mData: 'pcpQdrq', sTitle: '许可证发放日期', bSortable: false},
    {mData: 'pcpDh', sTitle: '栋号', bSortable: false}
];

companyProjectsSgxkJsList.renderTable = function () {
    if (companyProjectsSgxkJsList.oTable == null) {
        companyProjectsSgxkJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsSgxkJsList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/projectSgxk/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsSgxkJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsSgxkJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsSgxkJsList.oTable.fnDraw();
    }
};

companyProjectsSgxkJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};

companyProjectsSgxkJsList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/projectSgxk/excel.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};

companyProjectsSgxkJsList.echartsRegion = function() {
    var datax = [];
    var data1 = [];
    $.ajax({
        type: "GET", dataType: "json",async : false,
        url: getRootPath() + "/csmp/ptProject/queryEchartsForArea.action",
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
    var myCharts = echarts.init(document.getElementById('region'));
    // var data1 = [132, 324, 327, 452];
    var data2 = [7.2, -2.6, 4.7, -0.5];
//定义两个上下箭头的矢量路径
    var up = 'path://M286.031,265l-16.025,3L300,223l29.994,45-16.041-3-13.961,69Z';
    var down = 'path://M216.969,292l16.025-3L203,334l-29.994-45,16.041,3,13.961-69Z'
//遍历data2里面元素的正负定义一个矢量路径的数组
    var path = [up,down,up,down,down,down,up]
    var option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        title: {
            top:10,
            text: '各区域办理许可证数量统计',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                // data: ['高新', '北湖', '长德', '空港'],
                data:datax,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            // {
            //     "name": "",
            //     type: 'pictorialBar',
            //     symbol: function(data,params){
            //         return path[params.dataIndex]
            //     },
            //     symbolSize: [20, 30],
            //     symbolOffset: [0, -40],
            //     color:'orange',
            //     symbolPosition: 'end',
            //     label: {
            //         show: true,
            //         position: "top",
            //         formatter:  function(params){
            //             return data2[params.dataIndex]
            //         },
            //         fontSize: 30,
            //         fontWeight: 'bold',
            //         color: '#34DCFF'
            //     },
            //     data: data1
            // },
            {
                name: '数量',
                type: 'bar',
                barWidth: '40%',
                data: data1,
                label: {
                    show: true,
                    position: "top",
                }
            }
        ]
    };
    myCharts.setOption(option);
};
companyProjectsSgxkJsList.echarts = function(){
    var chartData = [];
    var sum = 0;
    $.ajax({
        type: "GET", dataType: "json",async : false,
        url: getRootPath() + "/csmp/ptProject/queryEchartsForSgxk.action",
        success: function (data) {
            if (data != null) {
                chartData = [
                    {
                        name: "未办理",
                        value: data.allCc - data.cc,
                        unit: '个'
                    },
                    {
                        name: "已办理",
                        value: data.cc,
                        unit: '个'
                    }
                ];
                sum = data.allCc;
            }
        }
    });

    var myCharts1 = echarts.init(document.getElementById('appStatus'));
    var dashedPic = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAM8AAAAOBAMAAAB6G1V9AAAAD1BMVEX////Kysrk5OTj4+TJycoJ0iFPAAAAG0lEQVQ4y2MYBaNgGAMTQQVFOiABhlEwCugOAMqzCykGOeENAAAAAElFTkSuQmCC';
    var color = ['#FF8700', '#009DFF'];
    var pieSeries = [],
        lineYAxis = [];


// 图表option整理
    chartData.forEach((v, i) => {
        pieSeries.push({
            name: '学历',
            type: 'pie',
            clockWise: false,
            hoverAnimation: false,
            radius: [65 - i * 15 + '%', 57 - i * 15 + '%'],
            center: ["30%", "50%"],
            label: {
                show: false
            },
            data: [{
                value: v.value,
                name: v.name
            }, {
                value: sum - v.value,
                name: '',
                itemStyle: {
                    color: "rgba(0,0,0,0)"
                }
            }]
        });
    pieSeries.push({
        name: '',
        type: 'pie',
        silent: true,
        z: 1,
        clockWise: false, //顺时加载
        hoverAnimation: false, //鼠标移入变大
        radius: [65 - i * 15 + '%',57 - i * 15 + '%'],
        center: ["30%", "50%"],
        label: {
            show: false
        },
        data: [{
            value: 7.5,
            itemStyle: {
                color: "#E3F0FF"
            }
        }, {
            value: 2.5,
            name: '',
            itemStyle: {
                color: "rgba(0,0,0,0)"
            }
        }]
    });
    v.percent = (v.value / sum * 100).toFixed(1) + "%";
    lineYAxis.push({
        value: i,
        textStyle: {
            rich: {
                circle: {
                    color: color[i],
                    padding: [0, 5]
                }
            }
        }
    });
});
    var option1 = {
        color: color,
        grid: {
            top: '15%',
            bottom: '54%',
            left: "30%",
            containLabel: false
        },
        title: {
            top:10,
            text: '项目施工许可证办理情况统计',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        yAxis: [{
            type: 'category',
            inverse: true,
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                formatter: function(params) {
                    var item = chartData[params];
                    return '{line|}{circle|●}{name|'+ item.name +'}{bd||}{percent|'+item.percent+'}{value|'+ item.value+'}{unit|个}'
                },
                interval: 0,
                inside: true,
                textStyle: {
                    color: "#333",
                    fontSize: 14,
                    rich: {
                        line: {
                            width: 170,
                            height: 10,
                            backgroundColor: {image: dashedPic}
                        },
                        name: {
                            color: '#666',
                            fontSize: 14,
                        },
                        bd: {
                            color: '#ccc',
                            padding: [0, 5],
                            fontSize: 14,
                        },
                        percent:{
                            color: '#333',
                            fontSize: 14,
                        },
                        value: {
                            color: '#333',
                            fontSize: 16,
                            fontWeight: 500,
                            padding: [0, 0, 0, 20]
                        },
                        unit: {
                            fontSize: 14
                        }
                    }
                },
                show: true
            },
            data: lineYAxis
        }],
        xAxis: [{
            show: false
        }],
        series: pieSeries
    }
    myCharts1.setOption(option1);
};

$(document).ready(function(){
    companyProjectsSgxkJsList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsSgxkJsList.renderTable();
    companyProjectsSgxkJsList.echarts();
    companyProjectsSgxkJsList.echartsRegion();
    $('#excel').click(companyProjectsSgxkJsList.excel);
    $('#queryBtn').click(companyProjectsSgxkJsList.renderTable);
    $('#resetBtn').click(companyProjectsSgxkJsList.queryReset);
});
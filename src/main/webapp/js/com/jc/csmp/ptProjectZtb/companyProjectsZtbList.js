var companyProjectsZtbJsList = {};
companyProjectsZtbJsList.oTable = null;


companyProjectsZtbJsList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsZtbJsList.oTable, aoData);
    //组装查询条件
    var projectName = $('#projectName').val();
    if (projectName.length > 0) {
        aoData.push({'name': 'projectName', 'value': projectName});
    }
    var biddingType = $('#biddingType').val();
    if (biddingType.length > 0) {
        aoData.push({'name': 'biddingType', 'value': biddingType});
    }
};

companyProjectsZtbJsList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'pcpProjectNum', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'biddingTypeValue', sTitle: '招标类型', bSortable: false},
    {mData: 'biddingYysl', sTitle: '异议数量', bSortable: false},
    {mData: 'biddingTsss', sTitle: '投诉数量', bSortable: false},
    {mData: 'biddingRemark', sTitle: '备注', bSortable: false}

];

companyProjectsZtbJsList.renderTable = function () {
    if (companyProjectsZtbJsList.oTable == null) {
        companyProjectsZtbJsList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsZtbJsList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsZtbJsList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsZtbJsList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsZtbJsList.oTable.fnDraw();
    }
};
companyProjectsZtbJsList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excel.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsZtbJsList.queryReset = function(){
    $('#searchForm')[0].reset();
};
companyProjectsZtbJsList.echarts = function(){
    var myCharts = echarts.init(document.getElementById('ztbType'));
    var nameArr= [];
    var valueArr= [];
    var valueArr1= [];
    var valueArr2= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProjectZtb/queryEchartsForZ.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].value);
                    valueArr1.push(data[i].cc1);
                    valueArr2.push(data[i].cc2);
                    valueArr.push(data[i].cc1+data[i].cc2);
                }
            }
        }
    });
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        title: {
            top:6,
            text: '各类型招标情况统计',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        grid: {
            left: '2%',
            right: '4%',
            bottom: '8%',
            top:'20%',
            containLabel: true
        },
        legend: {
            data: ['总数量', '异议数量', '投诉数量'],
            right: 10,
            top:12,
            itemWidth: 12,
            itemHeight: 10,
            // itemGap: 35
        },
        xAxis: {
            type: 'category',
            data:nameArr,
            // data: ['勘察','设计','施工','监理','工程总承包','全过程咨询','货物、材料','验收'],
            axisLine: {
                lineStyle: {
                    color: '#000'

                }
            },
            axisLabel: {
                // interval: 0,
                // rotate: 40,
                textStyle: {
                    fontFamily: 'Microsoft YaHei'
                }
            },
        },

        yAxis: {
            type: 'value',
            axisLine: {
                show: false,
                lineStyle: {
                    color: '#000'
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color: 'rgba(255,255,255,0.3)'
                }
            },
            axisLabel: {}
        },
        series: [{
            name: '总数量',
            type: 'bar',
            barWidth: '10%',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#fccb05'
                    }, {
                        offset: 1,
                        color: '#f5804d'
                    }]),
                    barBorderRadius: 12,
                },
            },
            data:valueArr
            // data: [1400, 1400, 1300, 1300, 1300, 1400, 1400, 1400]
        },
            {
                name: '异议数量',
                type: 'bar',
                barWidth: '10%',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#8bd46e'
                        }, {
                            offset: 1,
                            color: '#09bcb7'
                        }]),
                        barBorderRadius: 11,
                    }

                },
                data:valueArr2
                // data: [400, 500, 500, 500, 500, 400,400, 500]
            },
            {
                name: '投诉数量',
                type: 'bar',
                barWidth: '10%',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#248ff7'
                        }, {
                            offset: 1,
                            color: '#6851f1'
                        }]),
                        barBorderRadius: 11,
                    }
                },
                data:valueArr1
                // data: [400, 600, 700, 700, 1000, 400, 400, 600]
            }]
    };

    myCharts.setOption(option);
};

$(document).ready(function(){
    companyProjectsZtbJsList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsZtbJsList.renderTable();
    companyProjectsZtbJsList.echarts();
    $('#excel').click(companyProjectsZtbJsList.excel);
    $('#queryBtn').click(companyProjectsZtbJsList.renderTable);
    $('#resetBtn').click(companyProjectsZtbJsList.queryReset);
});
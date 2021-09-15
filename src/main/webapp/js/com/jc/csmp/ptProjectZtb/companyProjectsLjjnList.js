var companyProjectsLjjnList = {};
companyProjectsLjjnList.oTable = null;


companyProjectsLjjnList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsLjjnList.oTable, aoData);
    //组装查询条件
};

companyProjectsLjjnList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'value', sTitle: '区域', bSortable: false},
    {mData: 'name', sTitle: '年份', bSortable: false},
    {mData: 'cc', sTitle: '一星级', bSortable: false},
    {mData: 'cc1', sTitle: '二星级', bSortable: false},
    {mData: 'cc2', sTitle: '三星级', bSortable: false}

];

companyProjectsLjjnList.renderTable = function () {
    if (companyProjectsLjjnList.oTable == null) {
        companyProjectsLjjnList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsLjjnList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageForLjjn.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsLjjnList.oTableAoColumns,
            "paging": false, // 禁止分页
            "fnServerParams": function ( aoData ) {
                companyProjectsLjjnList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsLjjnList.oTable.fnDraw();
    }
};
companyProjectsLjjnList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excelLjjn.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsLjjnList.queryReset = function(){
    $('#searchForm')[0].reset();
};
companyProjectsLjjnList.echarts = function(){
    var myCharts = echarts.init(document.getElementById('ztbType'));
    var getxlmc =['一星级','二星级','三星级'];
    var nameArr= [];
    var valueArr= [];
    var valueArr1= [];
    var valueArr2= [];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProjectZtb/queryEchartsForLjjn.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].name);
                    valueArr.push(data[i].cc);
                    valueArr1.push(data[i].cc1);
                    valueArr2.push(data[i].cc2);
                }
            }
        }
    });
    var option = {
        grid: {
            left: "45",
            right: "20",
            bottom: "80",
            top: "27",
        },

        tooltip: {
            trigger: 'item',
            axisPointer: {
                type: 'shadow'
            },
        },
        legend: {
            data: getxlmc,
            x: 'center',
            bottom: '15',
            textStyle: {
                color: "#666666",
                fontSize: 13
            },
            icon: 'roundRect',
            itemWidth: 15, // 设置宽度
            itemHeight: 15, // 设置高度
            itemGap: 30 // 设置间距
        },
        xAxis: [{
            data: nameArr,
            axisLabel: {
                margin: 10,
                color: '#999',
                textStyle: {
                    fontSize: 13
                },
            },
            axisLine: {
                lineStyle: {
                    color: '#8E8E8E',
                }
            },
            axisTick: {
                show: false
            },
        }],
        yAxis: [{
            name: '个数',
            nameTextStyle: {
                color: '#666666',
                fontSize: 13,
                padding: [0, 0, -8, 50]
            },
            axisLabel: {
                color: '#999',
                textStyle: {
                    fontSize: 13
                },
            },
            axisLine: {
                lineStyle: {
                    color: '#8E8E8E',
                }
            },
            axisTick: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: '#D8D8D8',
                }
            }
        }],
        animation: false,
        series: [
            {
                name: '一星级',
                type: 'bar',
                data: valueArr,
                barWidth: '12px',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#FEB063' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#1A91FF' // 100% 处的颜色
                        }], false),
                        barBorderRadius: [3, 3, 0, 0],
                    }
                },
            },
            {
                name: '二星级',
                type: 'bar',
                data: valueArr1,
                barWidth: '12px',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#9796FF' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#F1AE55' // 100% 处的颜色
                        }], false),
                        barBorderRadius: [3, 3, 0, 0],
                    }
                },
            },
            {
                name: '三星级',
                type: 'bar',
                data: valueArr2,
                barWidth: '12px',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: '#55D6F3' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#FF6788' // 100% 处的颜色
                        }], false),
                        barBorderRadius: [3, 3, 0, 0],
                    }
                },
            },
        ]
    };

    myCharts.setOption(option);
};
$(document).ready(function(){
    companyProjectsLjjnList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsLjjnList.renderTable();
    companyProjectsLjjnList.echarts();
    $('#excel').click(companyProjectsLjjnList.excel);
    $('#queryBtn').click(companyProjectsLjjnList.renderTable);
    $('#resetBtn').click(companyProjectsLjjnList.queryReset);
});
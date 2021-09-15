var companyProjectsZtbXzjdList = {};
companyProjectsZtbXzjdList.oTable = null;


companyProjectsZtbXzjdList.oTableFnServerParams = function(aoData){
    getTableParameters(companyProjectsZtbXzjdList.oTable, aoData);
    var projectName = $('#projectName').val();
    if (projectName.length > 0) {
        aoData.push({'name': 'projectName', 'value': projectName});
    }
    var jdType = $('#jdType').val();
    if (jdType.length > 0) {
        aoData.push({'name': 'jdType', 'value': jdType});
    }
};

companyProjectsZtbXzjdList.oTableAoColumns = [
    {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
    {mData: 'projectNum', sTitle: '项目编号', bSortable: false},
    {mData: 'projectName', sTitle: '项目名称', bSortable: false},
    {mData: 'jdTypeValue', sTitle: '监督类型', bSortable: false},
    {mData: 'jdTime', sTitle: '日期', bSortable: false},
    {mData: 'jdOrg', sTitle: '监督机构', bSortable: false},
    {mData: 'jdRemark', sTitle: '备注', bSortable: false}

];

companyProjectsZtbXzjdList.renderTable = function () {
    if (companyProjectsZtbXzjdList.oTable == null) {
        companyProjectsZtbXzjdList.oTable =  $('#gridTable').dataTable( {
            "iDisplayLength": companyProjectsZtbXzjdList.pageCount,
            "sAjaxSource": getRootPath() + "/csmp/ptProjectZtb/manageXzjdPmList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": companyProjectsZtbXzjdList.oTableAoColumns,
            "fnServerParams": function ( aoData ) {
                companyProjectsZtbXzjdList.oTableFnServerParams(aoData);
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        companyProjectsZtbXzjdList.oTable.fnDraw();
    }
};
companyProjectsZtbXzjdList.excel = function(){
    var modelTable = $('#gridTable').DataTable();
    var info = modelTable.page.info();
    var dataRows = info.recordsTotal;

    if(dataRows>0){
        window.location.href = getRootPath() + "/csmp/ptProjectZtb/excelXzjd.action";
    }else{
        msgBox.tip({ type:"fail", content: "暂无需要导出的数据" });
    }

};
companyProjectsZtbXzjdList.queryReset = function(){
    $('#searchForm')[0].reset();
};
companyProjectsZtbXzjdList.echarts = function(){
    var myCharts = echarts.init(document.getElementById('xzjd'));
    var nameArr= [];
    var valueArr= [];
    var colors= ["#0092f6","#00d4c7","#5F98A1","#D6886C"];
    $.ajax({
        type: "GET", dataType: "json", async: false,
        url: getRootPath() + "/csmp/ptProjectZtb/queryXzjdCount.action",
        success: function (data) {
            if (data != null) {
                for(var i=0; i<data.length; i++){
                    nameArr.push(data[i].name);
                    valueArr.push(
                        {
                            name:data[i].name,
                            type:'line',
                            // stack: '总量',
                            symbol:'circle',
                            symbolSize: 8,
                            itemStyle: {
                                normal: {
                                    color:colors[i],
                                    lineStyle: {
                                        color: colors[i],
                                        width:1
                                    },
                                }
                            },
                            markPoint:{
                                itemStyle:{
                                    normal:{
                                        color:'red'
                                    }
                                }
                            },
                            data:data[i].arr
                        }
                    );
                }
            }
        }
    });
    debugger
    var option ={
        grid: {
            left: '5%',
            right: '5%',
            top:'20%',
            bottom: '10%',
            containLabel: true
        },
        title: {
            top:6,
            text: '招投标行政监督汇总',
            left: 'left',
            // padding: [20, 0, 0, 0] //设置标题内边距,上，右，下，左
        },
        tooltip : {
            show: true,
            trigger: 'item'
        },
        legend: {
            show:true,
            x:'center',
            y:'15',
            icon: 'stack',
// 			itemWidth:10,
// 			itemHeight:10,
            textStyle:{
                color:'#1bb4f6'
            },
            // data:['招标方案','资格文件','招标文件','中标通知书']
            data:nameArr
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                axisLine:{
                    show:true,
                },
                axisTick:{
                    show:false,
                },
                splitLine:{
                    show:false,
                    lineStyle:{
                        color:'#195384'
                    }
                },
                data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
            }
        ],
        yAxis : [
            {
                type : 'value',
                name : '',
                axisLabel : {
                    formatter: '{value}',
                },
                axisTick:{
                    show:false,
                },
                splitLine:{
                    show:true,
                    lineStyle:{
                        color:'#11366e'
                    }
                }
            },

        ],
        series : valueArr
        //     [
        //     {
        //         name:'招标方案',
        //         type:'line',
        //         // stack: '总量',
        //         symbol:'circle',
        //         symbolSize: 8,
        //         itemStyle: {
        //             normal: {
        //                 color:'#0092f6',
        //                 lineStyle: {
        //                     color: "#0092f6",
        //                     width:1
        //                 },
        //                 //           areaStyle: {
        //                 // 			//color: '#94C9EC'
        //                 // 			color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
        //                 // 				offset: 0,
        //                 // 				color: 'rgba(7,44,90,0.3)'
        //                 // 			}, {
        //                 // 				offset: 1,
        //                 // 				color: 'rgba(0,146,246,0.9)'
        //                 // 			}]),
        //                 //           }
        //             }
        //         },
        //         markPoint:{
        //             itemStyle:{
        //                 normal:{
        //                     color:'red'
        //                 }
        //             }
        //         },
        //         data:[120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
        //     },
        //     {
        //         name:'资格文件',
        //         type:'line',
        //         // stack: '总量',
        //         symbol:'circle',
        //         symbolSize: 8,
        //
        //         itemStyle: {
        //             normal: {
        //                 color:'#00d4c7',
        //                 lineStyle: {
        //                     color: "#00d4c7",
        //                     width:1
        //                 },
        //             }
        //         },
        //         data:[220, 182, 191, 234, 290, 330, 310,201, 154, 190, 330, 410]
        //     },
        //     ,
        //     {
        //         name:'招标文件',
        //         type:'line',
        //         // stack: '总量',
        //         symbol:'circle',
        //         symbolSize: 8,
        //
        //         itemStyle: {
        //             normal: {
        //                 color:'#5F98A1',
        //                 lineStyle: {
        //                     color: "#5F98A1",
        //                     width:1
        //                 },
        //             }
        //         },
        //         data:[220, 182, 191, 611, 290, 330, 310,254, 190, 330, 410]
        //     },
        //
        //     {
        //         name:'中标通知书',
        //         type:'line',
        //         // stack: '总量',
        //         symbol:'circle',
        //         symbolSize: 8,
        //
        //         itemStyle: {
        //             normal: {
        //                 color:'#D6886C',
        //                 lineStyle: {
        //                     color: "#D6886C",
        //                     width:1
        //                 },
        //             }
        //         },
        //         data:[220, 182, 332, 234, 290, 44, 310,201, 154, 190, 111, 643]
        //     },
        // ]
    };

    myCharts.setOption(option);
};

$(document).ready(function(){
    companyProjectsZtbXzjdList.pageCount = TabNub > 0 ? TabNub : 10;
    companyProjectsZtbXzjdList.renderTable();
    companyProjectsZtbXzjdList.echarts();
    $('#excel').click(companyProjectsZtbXzjdList.excel);
    $('#queryBtn').click(companyProjectsZtbXzjdList.renderTable);
    $('#resetBtn').click(companyProjectsZtbXzjdList.queryReset);
});
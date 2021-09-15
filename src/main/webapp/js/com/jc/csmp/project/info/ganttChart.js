var ganttChart  = {};

ganttChart.search = function(projectId, projectName, approvalNumber) {
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json",
        url: getRootPath() + "/project/ganttChart/queryProjectPlan.action",
        success: function (data) {
            if (data != null) {
                if (data.length > 0) {
                    var datay1 = [];//计划开始时间
                    var datay2 = [];//计划结束时间
                    var datay3 = [];//实际开始时间
                    var datay4 = [];//实际结束时间
                    var datax = [];//任务
                    // var datax1 = [];
                    for (var i = 0; i < data.length; i++) {
                        datax.push(data[i].planName);
                        datay1.push(new Date(data[i].planStartDate));
                        datay2.push((new Date(data[i].planEndDate)));
                        if(data[i].actualStartDate==null){
                            datay3.push(new Date());
                        }else{
                            datay3.push(new Date(data[i].actualStartDate));
                        }
                        if(data[i].actualEndDate==null){
                            datay4.push(new Date());
                        }else{
                            datay4.push(new Date(data[i].actualEndDate));
                        }
                        // datax1.push({"value":data[i].planStartDate,"xAxis":data[i].planStartDate,"yAxis":data[i].planName});
                    }
                    ganttChart.createCharts(projectId,projectName,datax,datay1,datay2,datay3,datay4);
                }else{
                    var myCharts = echarts.init(document.getElementById('ganttChart'));
                    myCharts.clear();
                    msgBox.tip({ type:"fail", content: '该项目暂未编制计划' });
                }
            }
        }
    });
}
ganttChart.createCharts = function(projectId,projectName,datax,datay1,datay2,datay3,datay4) {
    // var datay1 = [];
    // var datay2 = [];
    // var datay3 = [];
    // var datay4 = [];
    // var datax = [];
    // for(var i = 0; i < 100; i++){
    //     var date = new Date();
    //     date.setDate(date.getDate() + i);
    //     var date2 = new Date();
    //     date2.setDate(date2.getDate() + i+1);
    //     datax.push("任务"+i);
    //     datay1.push(date);
    //     datay2.push(date2);
    //     datay3.push(date);
    //     datay4.push(date2);
    // }
    var xGroup = [];
    var splitIndex = [];
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json",async : false,
        url: getRootPath() + "/project/ganttChart/queryProjectStage.action",
        success: function (data) {
            if (data != null) {
                if (data.length > 0) {
                    var index = 0;
                    for(var i = 0; i < data.length; i++){
                        if(data[i].cc>0){
                            index+=data[i].cc;
                                splitIndex.push(index+1);
                            for(var k = 0; k < data[i].cc; k++){
                                if(Math.round(data[i].cc/2)==k+1){
                                    xGroup.push(data[i].stageName);
                                }else{
                                    xGroup.push('');
                                }
                            }
                        }
                    }
                }
            }
        }
    });
    // var splitIndex = [3,4,10];
    var myCharts = echarts.init(document.getElementById('ganttChart'));
    var option = {
        title: {
            text: projectName+' 项目甘特图',
            left: 10
        },
        legend: {
            // y: 'bottom',
            data: ['计划时间', '实际时间'],  //修改的地方1
            bottom:12
        },
        grid: {
            containLabel: true,
            left: 100,
            top:50,
            bottom:50,
            right:260
        },
        dataZoom: [
            // {
            //     type: 'slider',
            //     show: true,
            //     xAxisIndex: [0],
            //     start: 0,
            //     end: 100
            // },
            {
                type: 'slider',
                show: true,
                yAxisIndex: [0],
                left: '3%',
                start: 0,
                end: 100
            },
            // {
            //     type: 'inside',
            //     xAxisIndex: [0],
            //     start: 0,
            //     end: 100
            // },
            {
                type: 'inside',
                yAxisIndex: [0],
                start: 0,
                end: 100
            }
        ],
        xAxis: {
            type: "time",
            // maxInterval: 3600 * 24 * 1000*5,
            axisLabel: {
                "show": true,
                "interval": 0
            }
        },

        yAxis: {
            type: 'category',
            axisLabel: {
                show: true,
                interval: 0,
                formatter: function (value, index) {
                    var last = ""
                    var max = 5;
                    var len = value.length;
                    var hang = Math.ceil(len / max);
                    if (hang > 1) {
                        for (var i = 0; i < hang; i++) {
                            var start = i * max;
                            var end = start + max;
                            var temp = value.substring(start, end) + "\n";
                            last += temp; //凭借最终的字符串
                        }
                        return last;
                    } else {
                        return value;
                    }
                }
            },
            // data: datax,
            data: xGroup,
            splitLine: {
                show: true,
                interval: (index, value) => {
                if(index === 0){
                    return true;
                }
                if (splitIndex.indexOf(index) >= 0) {
                    return true
                }
                return false;
            }
            },
                axisTick: {
                    lineStyle: {
                        color: '#000'
                    },
                    inside: false,
                        interval: (index, value) => {
                        if (index === 0) {
                            return true;
                        }
                        if (splitIndex.indexOf(index) >= 0) {
                            return true
                        }
                        return false;
                    },
                    lineStyle: {
                        color: '#000',
                            fontSize: '14px'
                    }
                },
        },
        tooltip: {
            trigger: "axis",
                axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter: function(params) {
                var res = datax[params[0].dataIndex] + "</br>"
                var date0 = params[0].data;
                var date1 = params[1].data;
                var date2 = params[2].data;
                var date3 = params[3].data;
                date0 = date0.getFullYear() + "-" + (date0.getMonth() + 1) + "-" + date0.getDate();
                date1 = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate();
                date2 = date2.getFullYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDate();
                date3 = date3.getFullYear() + "-" + (date3.getMonth() + 1) + "-" + date3.getDate();
                res += params[0].seriesName + "~" + params[1].seriesName + ":</br>" + date1 + "~" + date0 + "</br>"
                res += params[2].seriesName + "~" + params[3].seriesName + ":</br>" + date3 + "~" + date2 + "</br>"
                return res;
            }
        },
        series: [
            {
                name: '计划时间',
                type: 'bar',
                stack: 'test1',
                //修改地方2
                label: {
                    normal: {
                        show: true,
                        color: "#000",
                        position: "right",
                        formatter: function(params) {
                             var date0 = datay1[params.dataIndex].getFullYear() + "-" + (datay1[params.dataIndex].getMonth() + 1) + "-" + datay1[params.dataIndex].getDate();
                            var date1 = datay2[params.dataIndex].getFullYear() + "-" + (datay2[params.dataIndex].getMonth() + 1) + "-" + datay2[params.dataIndex].getDate();
                            return datax[params.dataIndex]+"("+ date0+"-"+date1 +")";
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#C23531',
                        borderColor: "#fff",
                        borderWidth: 2
                    }
                },
                zlevel: -1,
                data:datay2
            },
            {
                name: '计划开始时间',
                type: 'bar',
                stack: 'test1',
                itemStyle: {
                    normal: {
                        color: 'white',
                    }
                },
                zlevel: -1,
                z: 3,
                data:datay1
            },
            {
                name: '实际时间',
                type: 'bar',
                stack: 'test2',
                //修改地方3
                label: {
                    normal: {
                        show: true,
                        color: "#000",
                        position: "right",
                        formatter: function(params) {
                            var date0 = datay3[params.dataIndex].getFullYear() + "-" + (datay3[params.dataIndex].getMonth() + 1) + "-" + datay3[params.dataIndex].getDate();
                            var date1 = datay4[params.dataIndex].getFullYear() + "-" + (datay4[params.dataIndex].getMonth() + 1) + "-" + datay4[params.dataIndex].getDate();
                            return datax[params.dataIndex]+"("+ date0+"-"+date1 +")";
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#2F4554',
                        borderColor: "#fff",
                        borderWidth: 2
                    }
                },
                zlevel: -1,
                data:datay4
            },
            {
                name: '实际开始时间',
                type: 'bar',
                stack: 'test2',
                itemStyle: {
                    normal: {
                        color: 'white',
                    }
                },
                zlevel: -1,
                z: 3,
                data:datay3
            },
        ]
    };
    myCharts.setOption(option);
    // setTimeout(charts.setOption(option), 500);
    // charts.setOption(option);
};

ganttChart.treeObj = null;

ganttChart.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (treeNode.id.indexOf('project_') > -1) {
            var array = treeNode.id.replace('project_', '').split('|');
            ganttChart.search(array[0],treeNode.name);
        }
    }

    ganttChart.treeObj = JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        isSearch: true,
        url: getRootPath() + "/statistics/projectInfoTree.action",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: ganttChart.treeObj, data: data});
        }
    });
};

$(document).ready(function(){
    ganttChart.tree();
});
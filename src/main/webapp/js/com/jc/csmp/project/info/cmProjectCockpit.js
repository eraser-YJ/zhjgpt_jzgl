var cmProjectCockpitPanel = {};

cmProjectCockpitPanel.imageArray = [];
cmProjectCockpitPanel.currentIndex = 0;

cmProjectCockpitPanel.loadQuestion = function(projectId) {
    $.ajax({
        type : "GET", data : {projectId: projectId}, dataType : "json",
        url : getRootPath() + "/statistics/questionByYear.action",
        success : function(data) {
            var xAxis = [], quality = [], safe = [], scene = [];
            for (var i = 0; i < data.length; i++) {
                xAxis.push(data[i].month);
                quality.push(data[i].quality);
                safe.push(data[i].safe);
                scene.push(data[i].scene);
            }
            cmProjectCockpitPanel.createCharts('questionModule', {
                title: {
                    text: '质量/安全/现场问题统计',
                    textStyle:{
                        fontSize:14
                    },
                    padding:[9,0,0,10]
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['质量问题', '安全问题', '现场问题'],
                    right: 10,
                    top:5
                },
                grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
                xAxis: { type: 'category', boundaryGap: false, data: xAxis },
                yAxis: { type: 'value' },
                series: [
                    { name: '质量问题', type: 'line', stack: '个数', data: quality },
                    { name: '安全问题', type: 'line', stack: '个数', data: safe },
                    { name: '现场问题', type: 'line', stack: '个数', data: scene }
                ]
            });
        }
    });
};

cmProjectCockpitPanel.loadPassTimePlan = function(projectId) {
    $.ajax({
        type : "GET", data : {projectId: projectId}, dataType : "json",
        url : getRootPath() + "/statistics/projectPassTimePlan.action",
        success : function(data) {
            var content = '<table class="table table-striped tab_height dataTable no-footer">';
            content += '<thead>';
            content += '<tr>';
            content += '<th class="sorting_disabled" style="padding: 0px;text-align: center;">任务名称</th>';
            content += '<th class="sorting_disabled" style="padding: 0px;text-align: center;">所属阶段</th>';
            content += '<th class="sorting_disabled" style="padding: 0px;text-align: center;">超期天数</th>';
            content += '</tr>';
            content += '</thead>';
            if (data.length == 0) {
                content += "<tr class='odd'><td class='dataTables_empty' colspan='3'>没有匹配的记录</td></tr>";
            } else {
                for (var i = 0; i < data.length; i++) {
                    content += "<tr>";
                    content += '<td style="padding: 0px;text-align: center;">' + data[i].planName + '</td>';
                    content += '<td style="padding: 0px;text-align: center;">' + data[i].stageName + '</td>';
                    content += '<td style="padding: 0px;text-align: center;">' + data[i].day + '</td>';
                    content += "</tr>";
                }
            }
            content += '</table>';
            $('#passTimeModule').html(content);
        }
    });
};

cmProjectCockpitPanel.loadPlanCharts = function(projectId) {
    $('#planModuleEmpty').show();
    $('#planModule').hide();
    $.ajax({
        type : "GET", data : {projectId: projectId}, dataType : "json",
        url : getRootPath() + "/project/plan/info/projectPlanProgress.action",
        success : function(data) {
            if (data.length == 0) {
                return;
            }
            $('#planModuleEmpty').hide();
            $('#planModule').show();
            var planName = [], finish = [], noFinish = [];
            for (var i = 0; i < data.length; i++) {
                planName.push(data[i].planName);
                var completionRatio = data[i].completionRatio == null ? 0 : parseFloat(data[i].completionRatio);
                finish.push(completionRatio);
                noFinish.push(100 - completionRatio);
            }
            cmProjectCockpitPanel.createCharts('planModule', {
                title: {
                    text: '任务进度情况',
                    textStyle:{
                        fontSize:14
                    },
                    padding:[9,0,0,10]
                },
                tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
                legend: { data: ['已完成进度', '未完成进度'], right: 10 ,top:25,  itemWidth: 15,i},
                grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
                xAxis: { type: 'value' },
                yAxis: { type: 'category', data: planName,
                    axisLabel: {
                        margin:11,
                        formatter:function(value){
                                    var ret = "";//拼接加\n返回的类目项
                                    var maxLength = 5;//每项显示文字个数
                                    var valLength = value.length;//X轴类目项的文字个数
                                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                                    if (rowN > 1)//如果类目项的文字大于5,
                                    {
                                        var temp = "";//每次截取的字符串
                                        var start = 0;//开始截取的位置
                                        var end = maxLength;//结束截取的位置
                                        temp = value.substring(start, end)+'\n'+value.substring(end, valLength)
                                        ret += temp; //凭借最终的字符串

                                        return ret;
                                    }
                                    else{
                                        return value;
                                    }
                                },
                        textStyle: {
                            fontSize: '11'
                              },
                    }

                },
                series: [
                    { name: '已完成进度', type: 'bar', barWidth: '20',stack: '%', label: { show: true, position: 'insideRight',fontSize: 12,
                            color: '#fff' }, data: finish },
                    { name: '未完成进度', type: 'bar', barWidth: '20',stack: '%', label: { show: true, position: 'insideRight',fontSize: 12,
                            color: '#fff' }, data: noFinish }
                ]
            });
        }
    });
};

cmProjectCockpitPanel.loadProjectInfo = function(projectId) {
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json",
        url: getRootPath() + "/project/info/projectCockpit.action",
        success: function (data) {
            $('#investmentAmount').html(data.project.investmentAmount);
            $('#companyCount').html(data.companyCount);
            $('#personCount').html(data.personCount);
            cmProjectCockpitPanel.createCharts('moneyModule', {
                title: {
                    text: '投资完成金额(万元)',
                    subtext: data.project.productionTotal,
                    subtextStyle: { color:'#000000', fontSize: 12, fontWeight: 'normal' },
                    textStyle:{fontSize: 14},
                    padding: [10, 0, 0, 0],
                    left:'center'
                },

                series: [{
                    name: '', type: 'pie', radius: ['50%', '70%'], avoidLabelOverlap: false,
                    label: {
                        show: true, position: 'center',
                        formatter: function() { return new CommonToolsLib.CommonTools({}).getPeercent(data.project.productionTotal, data.project.investmentAmount); },
                        textStyle: { fontSize: 12, color: "#000000" }
                    },
                    center: ["50%", "50%"],
                    labelLine: { show: false },
                    data: [
                        {value: 335, name: ''},
                        {value: 310, name: ''}
                    ]
                }]
            });
        }
    });
};

cmProjectCockpitPanel.loadProjectPlan = function(projectId) {
    $.ajax({
        type: "GET", data: {projectId: projectId}, dataType: "json",
        url: getRootPath() + "/project/plan/images/imageList.action",
        success: function (data) {
            if (data) {
                cmProjectCockpitPanel.imageArray = [];
                cmProjectCockpitPanel.currentIndex = 0;
                if (data.length == 0) {
                    $('#imageEmptyModule').show();
                    $('#imageModule').hide();
                    return;
                }
                for (var i = 0; i < data.length; i++) {
                    cmProjectCockpitPanel.imageArray.push({id: data[i].id, title: data[i].title});
                }
                $('#imageEmptyModule').hide();
                $('#imageModule').show();
                cmProjectCockpitPanel.loadImageList();
            }
        }
    });
};

cmProjectCockpitPanel.upImage = function() {
    cmProjectCockpitPanel.currentIndex--;
    if (cmProjectCockpitPanel.currentIndex < 0) {
        cmProjectCockpitPanel.currentIndex = 0;
        return;
    }
    cmProjectCockpitPanel.loadImageList();
};

cmProjectCockpitPanel.downImage = function() {
    cmProjectCockpitPanel.currentIndex++;
    if (cmProjectCockpitPanel.currentIndex > (cmProjectCockpitPanel.imageArray.length - 1) ) {
        cmProjectCockpitPanel.currentIndex = cmProjectCockpitPanel.imageArray.length - 1
        if (cmProjectCockpitPanel.currentIndex < 0) {
            cmProjectCockpitPanel.currentIndex = 0
        }
        return;
    }
    cmProjectCockpitPanel.loadImageList();
};

cmProjectCockpitPanel.loadImageList = function() {
    if (cmProjectCockpitPanel.imageArray.length == 0) {
        return;
    }
    var record = cmProjectCockpitPanel.imageArray[cmProjectCockpitPanel.currentIndex];
    $('#imageTitle').html(record.title);
    $.ajax({
        type: "GET", data: {id: record.id}, dataType: "json",
        url: getRootPath() + "/project/plan/images/images.action",
        success: function (data) {
            if (data) {
                var dataLength = data.length;
                if (dataLength == 0) {
                    $('#imageListModule').hide();
                    $('#imageListEmptyModule').show();
                    $('#imageListModule').html("无图片查看");
                    return;
                }
                var content = "";
                for (var i = 0; i < dataLength; i++) {
                    console.log(data[i]);
                    var url = getRootPath() + data[i].originalUrl;
                    url = url.replace('/original/', '/originalRead/');
                    content += '<img src="' + getRootPath() + data[i].thumbnailUrl + '" onclick="window.open(\'' + url + '\');" style="width:31%;height: 115px; padding:10px 10px 0 10px;" />';
                }
                $('#imageListModule').html(content);
                $('#imageListEmptyModule').hide();
                $('#imageListModule').show();
            }
        }
    });
};

cmProjectCockpitPanel.createCharts = function(container, option) {
    /*var div = document.getElementById(container);
    var tempHeight = window.clientHeight;
    if(tempHeight == undefined){
        var B = document.body, D = document.documentElement;
        tempHeight = Math.min(D.clientHeight, B.clientHeight);
    }
    /!*var tempWidth = window.innerWidth;
    if(tempWidth == undefined){
        var B = document.body, D = document.documentElement;
        tempWidth = Math.min(D.clientWidth, B.clientWidth);
    }
    div.style.width = tempWidth + 'px';*!/
    div.style.height = ((tempHeight) / 2 - 50) + 'px';
    if (tableContainer != null) {
        document.getElementById(tableContainer).style.height = (tempHeight / 2 - 50) + 'px';
    }*/
    var charts = echarts.init(document.getElementById(container));
    charts.clear();
    charts.setOption(option);
};

cmProjectCockpitPanel.search = function(projectId, projectNumber, approvalNumber) {
    cmProjectCockpitPanel.loadQuestion(projectId);
    cmProjectCockpitPanel.loadPassTimePlan(projectId);
    cmProjectCockpitPanel.loadPlanCharts(projectId);
    cmProjectCockpitPanel.loadProjectInfo(projectId);
    cmProjectCockpitPanel.loadProjectPlan(projectId);
};
cmProjectCockpitPanel.treeObj = null;
cmProjectCockpitPanel.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        //$("#searchForm #paramParentId").val(treeNode.id);
        //cmProjectCockpitPanel.renderTable();
        if (treeNode.id.indexOf('project_') > -1) {
            var array = treeNode.id.replace('project_', '').split('|');
            cmProjectCockpitPanel.search(array[0]);
        }
    }

    cmProjectCockpitPanel.treeObj = JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/statistics/projectInfoTree.action",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
        isSearch: true,
        ready: function (data) {
            operatorTreeModule.searchTreeInit({treeObj: cmProjectCockpitPanel.treeObj, data: data});
        }
    });
};

$(document).ready(function(){
    cmProjectCockpitPanel.tree();
});
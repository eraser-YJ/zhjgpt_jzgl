var detailTypeFun = {};
//按钮
detailTypeFun.initBut = function(item){
    var h = "";
    h += '<input type="button" value="模拟监控" onclick="detailTypeFun.mockHoist(\'' + item.id + '\',\'' + item.equipmentCode + '\',\'' + item.projectName + '\')">';
    h += '&nbsp;&nbsp;<input type="button" value="运行数据" onclick="warnInfoJsList.runInfoFun(\''+item.id+'\')">';
    return h;
}


detailTypeFun.mockHoist = function (id, equipmentCode,projectName) {
    var url = getRootPath() + "/monit/realtime/hoist.action?id=" + id + "&code=" + equipmentCode + "&n_" + (new Date().getTime());
    // window.open(url);
    //iframe层-父子操作

    layer.open({
        type: 2,
        title: projectName+"，升降机："+equipmentCode,
        area: ['1000px', '660px'],
        fixed: false, //不固定
        maxmin: false,
        content: url
    });
}
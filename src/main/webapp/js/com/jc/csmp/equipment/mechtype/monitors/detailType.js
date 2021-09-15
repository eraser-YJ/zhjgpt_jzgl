var detailTypeFun = {};
//按钮
detailTypeFun.initBut = function(item){
    var h = "";
    h += '&nbsp;&nbsp;<input type="button" value="运行数据" onclick="warnInfoJsList.runInfoFun(\''+item.id+'\')">';
    return h;
}
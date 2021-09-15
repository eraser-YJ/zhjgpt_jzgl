var ztreeModule = {};
ztreeModule.hiddenNodes = [];

//搜索
ztreeModule.searchChildren = function(keyword, children){
    if (children == null || children.length == 0) {
        return false;
    }
    for (var i = 0; i < children.length; i++) {
        var node = children[i];
        if (node.name.indexOf(keyword) != -1) {
            return true;
        }
        //递归查找子结点
        var result = ztreeModule.searchChildren(keyword, node.children);
        if (result) {
            return true;
        }
    }
    return false;
}
//搜索
ztreeModule.searchParent = function(keyword, node){
    if (node == null) {
        return false;
    }
    if (node.name.indexOf(keyword) != -1) {
        return true;
    }
    //递归查找父结点
    return ztreeModule.searchParent(keyword, node.getParentNode());
}
//搜索
ztreeModule.search = function(){
    var ztreeObj = $.fn.zTree.getZTreeObj("ztreeObj");
    //显示上次搜索后隐藏的结点
    ztreeObj.showNodes(ztreeModule.hiddenNodes);
    //查找不符合条件的结点
    //返回true表示要过滤，需要隐藏，返回false表示不需要过滤，不需要隐藏
    function filterFunc(node) {
        var keyword = $("#keyword").val();
        //如果当前结点，或者其父结点可以找到，或者当前结点的子结点可以找到，则该结点不隐藏
        if (ztreeModule.searchParent(keyword, node) || ztreeModule.searchChildren(keyword, node.children)) {
            return false;
        }
        return true;
    };

    //获取不符合条件的叶子结点
    ztreeModule.hiddenNodes = ztreeObj.getNodesByFilter(filterFunc);
    //隐藏不符合条件的叶子结点
    ztreeObj.hideNodes(ztreeModule.hiddenNodes);
}

//搜索
ztreeModule.getSetting = function(){
    var setting = {
        check: {
            enable: true,   //true / false 分别表示 显示 / 不显示 复选框或单选框
            autoCheckTrigger: true,   //true / false 分别表示 触发 / 不触发 事件回调函数
            chkStyle: "checkbox",   //勾选框类型(checkbox 或 radio）
            chkboxType: {"N": "p", "N": "s"}   //勾选 checkbox 对于父子节点的关联关系
        },
        view: {
            dblClickExpand: false,
            showLine: true,
            selectedMulti: false
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: ""
            }
        }
    };
    return setting;
}
//初始化
ztreeModule.init = function(){
    $.ajax({
        type : "GET",
        dataType : "json",
        url : getRootPath() + "/monitors/manage/queryVideoTree.action",
        success : function(data) {
            $.fn.zTree.init($("#ztreeObj"), ztreeModule.getSetting(), data);
        }
    });
};
//取得选中节点
ztreeModule.getSelectNodes = function(){
    var zTree = $.fn.zTree.getZTreeObj("ztreeObj");
    var changedNodes = zTree.getCheckedNodes(true);
    var codes = [];
    for (var i = 0; i < changedNodes.length; i++) {
        var treeNode = changedNodes[i];
        if (treeNode.checked){
            codes.push(treeNode.id);
        }
    }
    return codes.join(",");
};

$(document).ready(function () {
    $('#search').click(function () {
        ztreeModule.search();
    });
    ztreeModule.init();
});
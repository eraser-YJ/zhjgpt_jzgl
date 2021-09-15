var startAuth = {};

startAuth.pageRows = null;
startAuth.definitionId = -1;
//分页处理 start
//分页对象
startAuth.oTable = null;
//显示列信息
startAuth.oTableAoColumns = [
    {mData: "roleName"},
    //设置权限按钮
    {mData: function(source) {
        return oTableSetButtons(source);
    }}
];


startAuth.roleList = function () {
    //组装后台参数
    startAuth.oTableFnServerParams = function(aoData){
        var definitionId = startAuth.definitionId;
        //组装查询条件
        aoData.push({ "name": "definitionId", "value": definitionId});
    };
    //table对象为null时初始化
    if (startAuth.oTable == null) {
        startAuth.oTable = $('#roleTable').dataTable( {
            iDisplayLength: startAuth.pageRows,//每页显示多少条记录
            sAjaxSource: getRootPath()+"/definitionAuth/getDefinitionRolesByPage.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: startAuth.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                startAuth.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,1]}]
        });
    } else {
        //table不为null时重绘表格
        startAuth.oTable.fnDraw();
    }
};


startAuth.defTree = {}

startAuth.defTree.treeSetting = {
    data: {
        simpleData: {
            enable: true
        }
    },
    async: {
        enable: true,
        url:getRootPath()+"/definitionAuth/getManageDefTreeForStartAuth.action",
    },
    callback:{
        onClick:function(event, treeId, treeNode){
            if(treeNode.type == "def") {
                startAuth.definitionId = treeNode.definitionId;
                startAuth.roleList();
            }
        },
        onAsyncSuccess:function(event, treeId, treeNode, msg){
            startAuth.defTree.tree.expandAll(true );
        }
    }
}



startAuth.defTree.init = function(){
    startAuth.defTree.tree = $.fn.zTree.init($("#defTree"), startAuth.defTree.treeSetting);
}

startAuth.definitionId = null;
startAuth.save = function(){
    var roleId = $("#defRoleForm #roleId").val();
    var postData = {
        definitionId:startAuth.definitionId,
        roleId:roleId
    }
    $.post(getRootPath() + "/definitionAuth/saveDefinitionRole.action",postData,function(data){
        startAuth.roleList();
        $('#roleModal').modal('hide');
    });
}

startAuth.deleteFun = function(id){
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            startAuth.deleteCallback(id);
        }
    });
}

startAuth.deleteCallback = function(id){
    var ajaxData = {
        id:id
    }
    $.post(getRootPath() + "/definitionAuth/deleteDefinitionRole.action",ajaxData,function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "删除成功"
            });
            startAuth.roleList();
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_006")
            });
        }
    });
}

startAuth.showModal = function(){
    var nodes = startAuth.defTree.tree.getSelectedNodes();
    if(nodes.length>1||nodes.length==0){
        msgBox.tip({
            type:"fail",
            content:"请选择流程节点"
        });
        return;
    }
    var node = nodes[0];
    if(node.type!="def"){
        msgBox.tip({
            type:"fail",
            content:"请选择流程节点"
        });
        return;
    }
    startAuth.definitionId = node.definitionId;
    selectControl.openPerson();
}

$(document).ready(function(){

    //计算左侧的树高度
    var windowHeight = $("#content").height(),
        $container   = $("#scrollable");

    //树高度定义
    if($("#defTree").length){
        var treeDom = $("#LeftHeight");
        $(".tree-right").css("padding-left","215px");
        //定义高度
        var leftTreeHeight = windowHeight - 100;
        treeDom.height(leftTreeHeight);
        $container.scroll(function() {
            var sTop = $container.scrollTop();
            treeDom[sTop >= 60?'addClass':'removeClass']('fixedNav').css('height',(sTop >= 60?(windowHeight - 40):leftTreeHeight)+"px")
        });
    }

    startAuth.pageRows = TabNub>0 ? TabNub : 10;
    selectControl.init("showAddDiv","name1-name", true, true);
    startAuth.defTree.init();
    startAuth.roleList();
});

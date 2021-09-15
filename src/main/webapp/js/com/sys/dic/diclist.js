var diclist = {}, pageCount=10;
diclist.subState = false;
diclist.oTable = null;
diclist.dictree = null;

diclist.oTableFnServerParams = function(aoData) {
    getTableParameters(diclist.oTable, aoData);
    aoData.push({ "name": "parentId", "value": $("#dicparentId").val()});
};

diclist.dicManageList = function () {
    if (diclist.oTable == null) {
        diclist.oTable = $('#dicsTable').dataTable( {
            "iDisplayLength": diclist.pageCount,
            "sAjaxSource": getRootPath() + "/dic/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: "value", sTitle: '内容', bSortable: false},
                {mData: "code", sTitle: '编码', bSortable: false},
                {mData: "parentId", sTitle: '父字典类型', bSortable: false},
                {mData: "orderFlag", sTitle: '顺序', bSortable: false},
                {mData: "useFlag", sTitle: '是否启用', bSortable: false, mRender: function(mData) { return mData == "1" ? "已启用" : "未启用"; }},
                {mData: "defaultValue", sTitle: '是否设为默认值', bSortable: false, mRender: function(mData) { return mData == "1" ? "是" : "否"; }},
                {mData: function(source) {
                    var edit = "<a class=\"a-icon i-new\" href=\"#myModal-edit\" onclick=\"diclist.loadUpdateHtml('"+ source.id+ "')\" role=\"button\" data-toggle=\"modal\">" + finalParamEditText + "</a>";
                    var del = "";
                    if (source.useFlag != 1) {
                        del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"diclist.deleteDics('"+ source.id+ "','"+source.code+"')\">" + finalParamDeleteText + "</a>";
                    }
                    return edit + del;
                }, bSortable: false, sTitle: '操作', sWidth: 120}
            ],
            "fnServerParams": function ( aoData ) {
                diclist.oTableFnServerParams(aoData);
            },
            aaSorting: [],
            aoColumnDefs: []
        } );
    } else {
        diclist.oTable.fnDraw();
    }
};

diclist.reDicTree = function() {
    allTopNodes = [];
    var tree = diclist.dictree.getZTreeObj();
    var nodes = tree.getSelectedNodes();
    var treeNode = nodes[0];
    allTopNodes = getAllTopNodes(tree, treeNode);
    diclist.dictree = JCTree.ztree({
        container: 'treeDemo', url: getRootPath()+'/dic/managerDicTree.action',
        expand: false, rootNode: true, selectNode: "-1",
        onClick: treeClick,
        ready: function() {
            var tree = diclist.dictree.getZTreeObj();
            if (allTopNodes.length == 1) {
                var rootNode = diclist.dictree.getRootNode();
                tree.expandNode(rootNode, true, false, false);
                treeNode = rootNode;
            } else {
                for (var i = allTopNodes.length - 1; i > 0 ; i--) {
                    tree.expandNode(allTopNodes[i], true, false, false);
                }
            }
            tree.selectNode(treeNode, true);
            treeClick(diclist.dictree,treeNode.id,treeNode);
        }
    });
};

var allTopNodes = [];
function getAllTopNodes(tree, node){
    if(node == null){
        return;
    }
    allTopNodes.push(node);
    getAllTopNodes(tree,node.getParentNode());
    return allTopNodes;
}

diclist.queryReset = function(){
    $('#manageListForm')[0].reset();
    $("#dicparentId").attr("value","-1");
};

diclist.loadAddHtml = function (){
    if ($.trim($("#dicsEdit").html()) == "") {
        $("#dicsEdit").load(getRootPath() + "/dic/dicEdit.action", null, function(){
            diclist.createDic();
        });
    } else {
        diclist.createDic();
    }
};

diclist.createDic = function(){
    hideErrorMessage();
    $("#id").attr("value","");
    diclist.clearForm(dicForm);
    $("#titledic").html("新增");
    $('#myModal-edit').modal('show');
    $("#dicForm #parentId").attr("value",$("#dicparentId").val());
    $("#dicForm #parentType").attr("value",$("#dicparentType").val());
    $("#dicForm #useFlag1").prop("checked","checked");
    $("#dicForm #defaultValue2").prop("checked","checked");
    ie8StylePatch();
};

diclist.clearForm = function(form){
    $(':input', form).each(function() {
        var type = this.type;
        var tag = this.tagName.toLowerCase();
        if (type == 'text' || type == 'password' || tag == 'textarea') {
            this.value = "";
        }
    });
};

diclist.loadUpdateHtml = function (id){
    if ($.trim($("#dicsEdit").html()) == "") {
        $("#dicsEdit").load(getRootPath() + "/dic/dicEdit.action", null, function(){
            diclist.get(id);
        });
    } else {
        diclist.get(id);
    }
};


diclist.get = function(id){
    hideErrorMessage();
    jQuery.ajax({
        url: getRootPath() + "/dic/get.action?id=" + id + "&time=" + (+new Date().getTime()),
        type: 'post', dataType: 'json',
        success: function(data, textStatus, xhr) {
            diclist.clearForm(dicForm);
            $("#dicForm").fill(data);
            $("#titledic").html("编辑");
            $('#myModal-edit').modal('show');
            ie8StylePatch();
        },
        error:function() {
            msgBox.tip({content: "加载数据错误", type:'fail'});
        }
    });
};

diclist.getChildrenNum =function(id){
	var nodes = diclist.dictree.getZTreeObj().getNodes();
	return diclist.getChildrenNumEach(nodes,id);
};

diclist.getChildrenNumEach=function(nodes,id){
	for (var i = 0; i < nodes.length; i++) {
    	if (nodes[i].id == id) {
    		if (nodes[i].children) {
    	    	num = nodes[i].children.length;
    	    	return num;
    	    } else {
    	    	return 0;
    	    }
    	}
    }
	for (var i = 0; i < nodes.length; i++) {
		if (nodes[i].children) {
			var num = diclist.getChildrenNumEach(nodes[i].children, id);
			if (num > 0) {
				return num;
			}
	    }
    }
	return 0;
};

diclist.deleteDics = function (id, code) {
    var ids = String(id);
    if (id == 0) {
        var idsArr = [];
        $("[name='ids']:checked").each(function() {
            idsArr.push($(this).val());
        });
        ids = idsArr.join(",");
    }
    if (ids.length == 0) {
        msgBox.info({content: "请选择要删除的字典", type:'fail'});
        return;
    }
    var num = diclist.getChildrenNum(code);
    if (num != 0) {
    	msgBox.info({content: "要删除的字典项含有子元素不能删除", type:'fail'});
    	return;
    }
    confirmx($.i18n.prop("JC_SYS_034"),function(){diclist.deleteCallBack(ids,idsArr);});
};

diclist.deleteCallBack = function(ids, idsArr) {
    $.ajax({
        type: "POST", url: getRootPath() + "/dic/deleteByIds.action?time=" + (+new Date().getTime()),
        data: {"ids": ids}, dataType: "json",
        success : function(data) {
            if (data > 0) {
                msgBox.tip({
                    content: "删除成功", type:'success',
                    callback:function(){
                        diclist.reDicTree();
                    }
                });
            }
        }
    });
};

diclist.addNextDics = function(code,parentId){
    document.location.href = getRootPath() + "/dic/manageNext.action?code=" + code + "&parentId=" + parentId;
};

function treeClick(jObj,id,tree){
    tree.checkState = true;
    $("#dicparentId").val(tree.id);
    $("#manageListForm #value").html(tree.name);
    $("#manageListForm #code").html(tree.id);
    $("#manageListForm #parentId").html(tree.pId);
    if (tree.pId) {
        $("#dicparentType").val(tree.id + "=" + tree.pId);
    } else {
        $("#dicparentType").val(tree.id + "=-2");
    }
    diclist.dicManageList();
}

jQuery(function($) {
    $.ajaxSetup ({
        cache: false //设置成false将不会从浏览器缓存读取信息
    });
    diclist.pageCount = TabNub>0 ? TabNub : 10;
    $("#dicsTop").click(diclist.loadAddHtml);
    diclist.dictree = JCTree.ztree({
        container: 'treeDemo',
        url: getRootPath() + '/dic/managerDicTree.action',
        expand: false,
        expandRootNode: true,
        rootNode: true,
        selectNode: "-1",
        onClick: treeClick,
        ready : function(){
            var rootNode = diclist.dictree.getRootNode();
            diclist.dictree.getZTreeObj().expandNode(rootNode, true, false, false);
            diclist.dictree.getZTreeObj().selectNode(rootNode, true);
            treeClick(diclist.dictree,rootNode.id,rootNode);
        }
    });
});


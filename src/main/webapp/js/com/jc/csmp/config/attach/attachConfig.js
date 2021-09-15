var attachConfigModule = {};
attachConfigModule.subState = false;
attachConfigModule.treeObj = null;
attachConfigModule.operatorItemSuccessCallback = null;
attachConfigModule.oTable = null;

attachConfigModule.tree = function(){
    function onBeforeClick(id, node){
        if(node.isChecked == 0 && node.pId != null){
            return false;
        }else{
            return true;
        }
    }

    function onClick(event, treeId, treeNode) {
        if (attachConfigModule.treeObj == null) {
            attachConfigModule.treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        }
        $("#searchForm #paramItemId").val(treeNode.id == '0' ? '' : treeNode.id);
        attachConfigModule.renderTable();
    }

    JCTree.ztree({
        container : 'treeDemo',
        expand : true,
        rootNode : true,
        url: getRootPath() + "/config/attach/itemTree.action",
        onClick: onClick,
        onBeforeClick : onBeforeClick,
    });
};

attachConfigModule.clearItem = function() {
    $('#itemForm #id').val("");
    $('#itemForm #modifyDate').val("");
    $('#itemForm #itemClassify').val("");
    $('#itemForm #itemCode').val("");
};

/**
 * 操作分类
 * @type {Function}
 */
attachConfigModule.operatorItem = function(operator) {
    if ($('#searchForm #paramItemId').val() == "" && operator !== 'add') {
        msgBox.tip({ type: "fail", content: "请选择要操作的分类" });
        return;
    }
    attachConfigModule.clearItem();
    attachConfigModule.operatorItemSuccessCallback = null;
    if (operator == 'add') {
        attachConfigModule.operatorItemSuccessCallback = function (data) {
            operatorTreeModule.addNode(attachConfigModule.treeObj, data.data.id, "0", data.data.itemClassify, 0)
        };
        $('#item-modal').modal('show');
    } else if (operator == 'edit') {
        $.ajax({
            type: "GET", url: getRootPath() + "/config/attach/getItem.action", data: {id: $('#searchForm #paramItemId').val()}, dataType: "json",
            success : function(data) {
                if (data) {
                    console.log(data);
                    $("#itemForm").fill(data);
                    $('#item-modal').modal('show');
                    attachConfigModule.operatorItemSuccessCallback = function (record) {
                        operatorTreeModule.editNode(attachConfigModule.treeObj, record.data.id, record.data.itemClassify, 0);
                    }
                }
            }
        });
    } else if (operator == 'delete') {
        msgBox.confirm({
            content: $.i18n.prop("JC_SYS_034"),
            success: function(){
                $.ajax({
                    type: "POST", url: getRootPath() + "/config/attach/deleteItemById.action", data: {id: $('#searchForm #paramItemId').val()}, dataType: "json",
                    success : function(data) {
                        if (data.code == 0) {
                            msgBox.tip({ type:"success", content: data.msg });
                            operatorTreeModule.deleteNode(attachConfigModule.treeObj, $('#searchForm #paramItemId').val());
                        } else {
                            msgBox.tip({ type:"fail", content: data.msg });
                        }
                    }
                });
            }
        });
    }
};

/**
 * 保存分类
 */
attachConfigModule.saveOrUpdateItem = function() {
    if (attachConfigModule.subState) {
        return;
    }
    attachConfigModule.subState = true;
    if (!$("#itemForm").valid()) {
        attachConfigModule.subState = false;
        return;
    }
    jQuery.ajax({
        url: getRootPath() + "/config/attach/saveOrUpdateItem.action", type: 'POST', cache: false, data: $("#itemForm").serializeArray(),
        success : function(data) {
            attachConfigModule.subState = false;
            if (data.code != 0) {
                msgBox.tip({ type: "fail", content: data.msg });
                return;
            }
            msgBox.tip({ type: "success", content: data.msg });
            $('#item-modal').modal('hide');
            attachConfigModule.operatorItemSuccessCallback(data);
        },
        error : function() {
            attachConfigModule.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

/**
 * 加载表格
 */
attachConfigModule.renderTable = function() {
    if (attachConfigModule.oTable == null) {
        attachConfigModule.oTable = $('#gridTable').dataTable( {
            "iDisplayLength": attachConfigModule.pageCount,
            "sAjaxSource": getRootPath() + "/config/attach/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
                {mData: 'itemName', sTitle: '分类名', bSortable: false},
                {mData: 'itemAttach', sTitle: '附件名', bSortable: false},
                {mData: 'isRequired', sTitle: '是否必填', bSortable: false, mRender : function(mData, type, full) { return full.isRequired == 0 ? '是' : '否 '; }},
                {mData: 'isCheckbox', sTitle: '是否多个', bSortable: false, mRender : function(mData, type, full) { return full.isCheckbox == 0 ? '是' : '否 '; }},
                {mData: 'extNum1', sTitle: '排序', bSortable: false},
                {mData: function(source) {
                    var edit = "<a class=\"a-icon i-cemt\" href=\"javascript:void(0);\" onclick=\"attachConfigModule.operatorAttach('"+ source.id+ "', 'edit')\" role=\"button\">" + finalTableBtnText('修改', 'fa-edit2') + "</a>";
                    var del = "<a class=\"a-icon i-remove\" href=\"javascript:void(0);\" onclick=\"attachConfigModule.operatorAttach('"+ source.id+ "', 'delete')\">" + finalTableBtnText('删除', 'fa-remove') + "</a>";
                    return edit + del;
                }, sTitle: '操作', bSortable: false, sWidth: 145}
            ],
            "fnServerParams": function ( aoData ) {
                getTableParameters(attachConfigModule.oTable, aoData);
                if ($('#searchForm #paramItemId').val() != "") {
                    aoData.push({"name": "itemId", "value": $('#searchForm #paramItemId').val()});
                }
            },
            aaSorting:[],
            aoColumnDefs: []
        });
    } else {
        attachConfigModule.oTable.fnDraw();
    }
};

attachConfigModule.clearAttach = function() {
    $('#attachForm #id').val("");
    $('#attachForm #modifyDate').val("");
    $('#attachForm #itemAttach').val("");
    $('#attachForm #itemId').val("");
    $('#attachForm #isRequired').val("0");
    $('#attachForm #isCheckbox').val("0");
    $('#attachForm #extNum1').val("");
};

attachConfigModule.operatorAttach = function(id, operator) {
    attachConfigModule.clearAttach();
    if (operator == 'add') {
        if ($('#searchForm #paramItemId').val() == "") {
            msgBox.tip({ type: "fail", content: "请选择附件分类" });
            return;
        }
        $('#attach-modal').modal('show');
    } else if (operator == 'edit') {
        $.ajax({
            type: "GET", url: getRootPath() + "/config/attach/getAttach.action", data: {id: id}, dataType: "json",
            success : function(data) {
                if (data) {
                    $("#attachForm").fill(data);
                    $('#attach-modal').modal('show');
                }
            }
        });
    } else if (operator == 'delete') {
        msgBox.confirm({
            content: $.i18n.prop("JC_SYS_034"),
            success: function(){
                $.ajax({
                    type: "POST", url: getRootPath() + "/config/attach/deleteAttachById.action", data: {id: id}, dataType: "json",
                    success : function(data) {
                        if (data.code == 0) {
                            msgBox.tip({ type:"success", content: data.msg });
                            attachConfigModule.renderTable();
                        } else {
                            msgBox.tip({ type:"fail", content: data.msg });
                        }
                    }
                });
            }
        });
    }
};

attachConfigModule.saveOrUpdateAttach = function() {
    if (attachConfigModule.subState) {
        return;
    }
    attachConfigModule.subState = true;
    if (!$("#attachForm").valid()) {
        attachConfigModule.subState = false;
        return;
    }
    $('#attachForm #itemId').val($('#searchForm #paramItemId').val());
    jQuery.ajax({
        url: getRootPath() + "/config/attach/saveOrUpdateAttach.action", type: 'POST', cache: false, data: $("#attachForm").serializeArray(),
        success : function(data) {
            attachConfigModule.subState = false;
            if (data.code != 0) {
                msgBox.tip({ type: "fail", content: data.msg });
                return;
            }
            msgBox.tip({ type: "success", content: data.msg });
            $('#attach-modal').modal('hide');
            attachConfigModule.renderTable();
        },
        error : function() {
            attachConfigModule.subState = false;
            msgBox.tip({ type:"fail", content: $.i18n.prop("JC_SYS_002") });
        }
    });
};

$(document).ready(function(){
    attachConfigModule.pageCount = TabNub > 0 ? TabNub : 10;
    attachConfigModule.tree();
});

$("#itemForm").validate({
    ignore:'ignore',
    rules: {
        itemClassify: { required: true, maxlength: 255 },
        itemCode: { required: true, maxlength: 255 }
    }
});

$("#attachForm").validate({
    ignore:'ignore',
    rules: {
        itemAttach: { required: true, maxlength: 255 },
        isRequired: { required: true },
        isCheckbox: { required: true },
        extNum1: { required: true, number:true }
    },
    messages: {
        extNum1: {number: "请输入有效的数字"}
    }
});
var form_ = {};

form_.pageRows = null;

//重复提交标识
form_.subState = false;

//分页对象
form_.oTable = null;
//显示列信息
form_.oTableAoColumns = [
    {mData: "name"},
    {mData: "tableName"},
    {mData: "createDate"},
    {mData: function(source) {
        return oTableSetButtons(source);
    }}
];

form_.formList = function () {
    //组装后台参数
    form_.oTableFnServerParams = function(aoData){
        getTableParameters(form_.oTable, aoData);
        //组装查询条件
        $.each($("#formQueryForm").serializeArray(), function(i, o) {
            if(o.value != ""){
                aoData.push({ "name": o.name, "value": o.value});
            }
        });
    };
    //table对象为null时初始化
    if (form_.oTable == null) {
        form_.oTable = $('#formTable').dataTable( {
            iDisplayLength: form_.pageRows,//每页显示多少条记录
            sAjaxSource: getRootPath()+"/form/manageList.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: form_.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                form_.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,3]}]
        });
    } else {
        //table不为null时重绘表格
        form_.oTable.fnDraw();
    }
};

form_.save = function(){
    if(form_.subState)return;
        form_.subState = true;
    if(form_.valid()){
        var id = $("#formForm #id").val();
        var url = getRootPath()+"/form/save.action";
        if(id!=null&&id.length>0){
            url = getRootPath()+"/form/update.action";
        }
        var formData = $("#formForm").serializeArray();
        $.post(url,formData,function(data){
            if(data.success == true){
                msgBox.tip({
                    type:"success",
                    content: "保存成功"
                });
                form_.formList();
                $('#formModal').modal('hide');
            }else{
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_002")
                });
            }
            form_.subState = false;
        });
    }else{
        form_.subState = false;
    }
}

form_.valid = function(){
    var result = $("#formForm").valid();
    if(result){
        var id = $("#formForm #id").val();
        var name = $("#formForm #name").val();
        var validData = {
            id:id,
            name:name
        }

        $.ajax({
            url:getRootPath()+"/form/validForUpdate.action",
            async:false,
            data:validData,
            success:function(data){
                if(data.valid==false){
                    result = false;
                    msgBox.tip({
                        content: "存在名字相同的表单",
                        type:"fail"
                    });
                }
            }
        });
    }
    return result;
}

form_.deleteFun = function(id){
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            form_.deleteCallBack(id);
        }
    });
}

form_.deleteCallBack = function(id){
    $.get(getRootPath()+"/form/delete.action",{id:id},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "删除成功"
            });
            form_.formList();
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_006")
            });
        }
    });
}

form_.showModal = function(){
    $("#formModal .modal-title").html("新增表单");
    form_.clearForm();
    $('#formModal').modal('show');
}


form_.clearForm = function(){
    $('#formForm')[0].reset();
    $("#formForm #id").val("");
    var firstTableName = $("#formForm #tableName option:eq(0)").val();
    form_.tableNameSelect2.val(firstTableName).trigger("change");
    hideErrorMessage();
}

form_.loadModal=function(id){
    $("#formModal .modal-title").html("修改表单");
    $.get(getRootPath()+"/form/get.action",{id:id},function(data){
        if(data.success == true){
            form_.clearForm();
            $("#formForm").fill(data.form);
            form_.tableNameSelect2.val(data.form.tableName).trigger("change");
            $('#formModal').modal('show');
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_060")
            });
        }
    });
}

form_.formitem = {}
//重复提交标识
form_.formitem.subState = false;

form_.formitem.formId = null;
form_.formitem.oTableAoColumns = [
    {mData: "itemName"},
    {mData: "formName"},
    {mData: "typeLabel"},
    {mData: function(source) {
        return form_.formitem.oTableSetButtons(source);
    }}
];
form_.formitem.oTableSetButtons = function(item){
    var result = "";
    var update = '<a class="a-icon i-edit" href="#" onclick="form_.formitem.showFormitemUpdateModal('+item.id+')" role="button" data-toggle="modal"><i class="fa fa-edit2" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="编辑"></i></a>';
    var deleteBtnStr = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"form_.formitem.deleteFun("+item.id+")\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
    result = update+deleteBtnStr;
    return result;
}

form_.formitem.formitemList = function () {
    //组装后台参数
    form_.formitem.oTableFnServerParams = function(aoData){
        aoData.push({
            name:"formId",
            value:form_.formitem.formId
        })
    };
    //table对象为null时初始化
    if (form_.formitem.oTable == null) {
        form_.formitem.oTable = $('#formitemTable').dataTable( {
            bPaginate: false,
            sAjaxSource: getRootPath()+"/formitem/manageList.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: form_.formitem.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                form_.formitem.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,1,2,3]}]
        });
    } else {
        //table不为null时重绘表格
        form_.formitem.oTable.fnDraw();
    }
};

form_.formitem.showFormitemModal = function(formId){
    form_.formitem.formId = formId;
    form_.formitem.formitemList();
    $('#formitemModal').modal('show');
}

form_.formitem.showFormitemSaveModal = function(){
    $("#formitemUpdateModal .modal-title").html("新增表单项");
    form_.formitem.clearForm();
    $("#formitemForm #formId").val(form_.formitem.formId);
    $('#formitemModal').modal("hide");
    $('#formitemUpdateModal').modal('show');
}

form_.formitem.showFormitemUpdateModal = function(id){
    $("#formitemUpdateModal .modal-title").html("修改表单项");
    $.get(getRootPath()+"/formitem/get.action",{id:id},function(data){
        if(data.success == true){
            form_.formitem.clearForm();
            $("#formitemForm").fill(data.formitem);
            $('#formitemModal').modal("hide");
            $('#formitemUpdateModal').modal('show');
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_060")
            });
        }
    });
}

form_.formitem.closeFormitemUpdateModal = function(){
    $('#formitemUpdateModal').modal('hide');
    $('#formitemModal').modal('show');
}

form_.formitem.clearForm = function(){
    $('#formitemForm')[0].reset();
    hideErrorMessage();
    $("#formitemForm [name=id]").val("");
    $("#formitemForm #formId").val("");
    $("#formitemForm #columnName").val("");
}

form_.formitem.save = function(){
    if(form_.formitem.subState)return;
    form_.formitem.subState = true;
    if(form_.formitem.valid()){
        var url = getRootPath()+"/formitem/save.action";
        var id = $("#formitemForm [name=id]").val();
        if(id!=null&&id.length>0){
            url = getRootPath()+"/formitem/update.action";
        }
        var formitemData = $("#formitemForm").serializeArray();
        $.post(url,formitemData,function(data){
            form_.formitem.subState = false;
            if(data.success == true){
                msgBox.tip({
                    type:"success",
                    content: "保存成功"
                });
                form_.formitem.formitemList();
                form_.formitem.closeFormitemUpdateModal();
            }else{
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_002")
                });
            }
        });
    }else{
        form_.formitem.subState = false;
    }
}

form_.formitem.importFromDatabase = function(){
    if(form_.formitem.subState)return;
    form_.formitem.subState = true;
    $.get(getRootPath()+"/formitem/importFromTable.action",{formId:form_.formitem.formId},function(data){
        form_.formitem.subState = false;
        msgBox.tip({
            type:"success",
            content: "成功导入"+data.length+"条表单项"
        });
        form_.formitem.formitemList();
    });
}

form_.formitem.deleteFun = function(id){
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            form_.formitem.deleteCallback(id);
        }
    });
}

form_.formitem.deleteCallback = function(id){
    $.get(getRootPath()+"/formitem/delete.action",{id:id},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "删除成功"
            });
            form_.formitem.formitemList();
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_006")
            });
        }
    });
}

form_.formitem.valid = function(){
    var result = $("#formitemForm").valid();
    if(result == false){
        return result;
    }
    //判断表单名是否冲突
    var postData = {
        id:$("#formitemForm #formitemId").val(),
        formId:$("#formitemForm #formId").val(),
        formName:$("#formitemForm #formName").val(),
    }
    $.ajax({
        url:getRootPath()+"/formitem/validForUpdate.action",
        async:false,
        data:postData,
        success:function(data){
            if(data.valid==false){
                result = false;
                msgBox.tip({
                    content: "控件名称重复",
                    type:"fail"
                });
            }
        }
    });
    return result;
}

$("#formForm").validate({
    ignore: ".ignore",
    rules: {
        name:{
            required:true,
            maxlength: 60,
            specialChar: true
        },
        startUrl:{
            required:true,
            maxlength: 100
        },
        loadUrl:{
            required:true,
            maxlength: 100
        },
        listUrl:{
            required:true,
            maxlength: 100
        },
    }
});

$("#formitemForm").validate({
    ignore: ".ignore",
    rules: {
        itemName:{
            required:true,
            maxlength: 60,
            specialChar: true
        },
        formName:{
            required:true,
            maxlength: 60
        }
    }
});

$(document).ready(function(){
    form_.pageRows = TabNub>0 ? TabNub : 10;
    form_.tableNameSelect2 = $('#tableName').select2();
    form_.formList();
    $("#queryForm").click(form_.formList);
});
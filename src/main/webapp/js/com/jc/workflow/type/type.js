var type_ = {};
type_.pageRows = null;

//重复提交标识
type_.subState = false;

//分页对象
type_.oTable = null;
//显示列信息
type_.oTableAoColumns = [
    {mData: "name"},
    {mData: function(source) {
        return oTableSetButtons(source);
    }}
];

type_.typeList = function () {
    //组装后台参数
    type_.oTableFnServerParams = function(aoData){
    };
    //table对象为null时初始化
    if (type_.oTable == null) {
        type_.oTable = $('#typeTable').dataTable( {
            iDisplayLength: type_.pageRows,//每页显示多少条记录
            sAjaxSource: getRootPath()+"/type/manageList.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: type_.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                type_.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,1]}]
        });
    } else {
        //table不为null时重绘表格
        type_.oTable.fnDraw();
    }
};

type_.save = function(){
    if(type_.subState)return;
    type_.subState = true;
    if(type_.valid()){
        var id = $("#typeForm #id").val();
        var url = getRootPath()+"/type/save.action";
        if(id!=null&&id.length>0){
            url = getRootPath()+"/type/update.action";
        }
        var formData = $("#typeForm").serializeArray();
        $.post(url,formData,function(data){
            type_.subState = false;
            if(data.success == true){
                msgBox.tip({
                    type:"success",
                    content: "保存成功"
                });
                type_.typeList();
                $('#typeModal').modal('hide');
            }else{
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_002")
                });
            }
        });
    }else{
        type_.subState = false;
    }
}



type_.deleteFun = function(id){
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            type_.deleteCallback(id);
        }
    });
}

type_.deleteCallback = function(id){
    if(type_.validForDelete(id)==true){
        $.get(getRootPath()+"/type/delete.action",{id:id},function(data){
            if(data.success == true){
                msgBox.tip({
                    type:"success",
                    content: "删除成功"
                });
                type_.typeList();
            }else{
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_006")
                });
            }
        });
    }
}

type_.showModal = function(){
    $("#typeModal .modal-title").html("新增类型");
    type_.clearForm();
    $('#typeModal').modal('show');
}


type_.clearForm = function(){
    hideErrorMessage();
    $('#typeForm')[0].reset();
    $("#typeForm #id").val("");
    $("#typeForm #opt").val("");
}

type_.loadModal=function(id){
    $("#typeModal .modal-title").html("修改类型");
    $.get(getRootPath()+"/type/get.action",{id:id},function(data){
        if(data.success == true){
            type_.clearForm();
            $("#typeForm").fill(data.type);
            $('#typeModal').modal('show');
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_060")
            });
        }
    });
}

type_.valid = function(){
    var result = $("#typeForm").valid();
    if(result == false){
        return result;
    }
    //判断类型名是否重复
    var postData = {
        id:$("#typeForm #id").val(),
        name:$("#typeForm #name").val()
    }
    $.ajax({
        url:getRootPath()+"/type/validForUpdate.action",
        async:false,
        data:postData,
        success:function(data){
            if(data.valid==false){
                result = false;
                msgBox.tip({
                    content: "类型名称重复",
                    type:"fail"
                });
            }
        }
    });
    return result;
}

type_.validForDelete = function(id){
    var result = true;
    //判断类型名是否重复
    var postData = {
        id:id
    }
    $.ajax({
        url:getRootPath()+"/type/validForDelete.action",
        async:false,
        data:postData,
        success:function(data){
            if(data.valid==false){
                result = false;
                msgBox.tip({
                    content: "该类型下存在流程",
                    type:"fail"
                });
            }
        }
    });
    return result;
}

$("#typeForm").validate({
    ignore: ".ignore",
    rules: {
        name:{
            required:true,
            maxlength: 60,
            specialChar: true
        },
        styleStr:{
            maxlength: 60,
            specialChar: true
        }
    }
});

$(document).ready(function(){
    type_.pageRows = TabNub>0 ? TabNub : 10;
    type_.typeList();
});
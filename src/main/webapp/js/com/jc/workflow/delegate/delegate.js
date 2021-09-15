var delegate = {};

//重复提交标识
delegate.subState = false;

delegate.pageRows = null;

//分页对象
delegate.oTable = null;
//显示列信息
delegate.oTableAoColumns = [
    {mData: "delegateUserName"},
    {mData: "startTime"},
    {mData: "endTime"},
    {mData: "definitionName"},
    {mData: "useFlag", mRender : function(mData, type, full) {
        var result = "";
        if(full.useFlag == 1){
            result = '启用';
        }else{
            result = '停用';
        }
        return result;
    }
    },
    {mData: function(source) {
        return oTableSetButtons(source);
    }}
];

delegate.delegateList = function () {
    //组装后台参数
    delegate.oTableFnServerParams = function(aoData){
        getTableParameters(delegate.oTable, aoData);
        //组装查询条件
        $.each($("#delegateQueryForm").serializeArray(), function(i, o) {
            if(o.value != ""){
                aoData.push({ "name": o.name, "value": o.value});
            }
        });

    };
    //table对象为null时初始化
    if (delegate.oTable == null) {
        delegate.oTable = $('#delegateTable').dataTable( {
            iDisplayLength: delegate.pageRows,//每页显示多少条记录
            sAjaxSource: getRootPath()+"/delegate/manageList.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: delegate.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                delegate.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,3,5]}]
        });
    } else {
        //table不为null时重绘表格
        delegate.oTable.fnDraw();
    }
};

delegate.save = function(){
    if(delegate.subState)return;
    delegate.subState = true;
    if(delegate.valid()){
        var id = $("#delegateForm #id").val();
        var url = getRootPath()+"/delegate/save.action";
        if(id!=null&&id.length>0){
            url = getRootPath()+"/delegate/update.action";
        }
        var formData = $("#delegateForm").serializeArray();
        $.post(url,formData,function(data){
            delegate.subState = false;
            if(data.success == true){
                msgBox.tip({
                    type:"success",
                    content: "保存成功"
                });
                delegate.delegateList();
                $('#delegateModal').modal('hide');
            }else{
                msgBox.tip({
                    content: $.i18n.prop("JC_SYS_002")
                });
            }
        });
    }else{
        delegate.subState = false;
    }
}

delegate.deleteFun = function(id){
    msgBox.confirm({
        content: $.i18n.prop("JC_SYS_034"),
        success: function(){
            delegate.deleteCallback(id);
        }
    });
}

delegate.deleteCallback = function(id){
    $.get(getRootPath()+"/delegate/delete.action",{id:id},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "删除成功"
            });
            delegate.delegateList();;
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_006")
            });
        }
    });
}

delegate.stop = function(id){
    msgBox.confirm({
        content: "是否停用该流程",
        success: function(){
            delegate.stopCallback(id);
        }
    });
}

delegate.stopCallback = function(id){
    $.get(getRootPath()+"/delegate/stop.action",{id:id},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "停用成功"
            });
            delegate.delegateList();
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

delegate.showModal = function(){
    $("#delegateModal .modal-title").html("新增委托");
    hideErrorMessage();
    delegate.clearForm();
    $('#delegateModal').modal('show');
}


delegate.clearForm = function(){
    $('#delegateForm')[0].reset();
    $("#delegateForm #id").val("");
    $("#delegateForm #uesFlag").val("");
    delegate.userSelect.clearValue();
}

delegate.loadModal=function(id){
    $("#delegateModal .modal-title").html("修改委托");
    $.get(getRootPath()+"/delegate/get.action",{id:id},function(data){
        if(data.success == true){
            delegate.clearForm();
            $("#delegateForm").fill(data.delegate);
            delegate.userSelect.setData({id:data.delegate.delegateUser,text:data.delegate.delegateUserName})
            $('#delegateModal').modal('show');
        }else{
            msgBox.tip({
                content: $.i18n.prop("JC_SYS_060")
            });
        }
    });
}

delegate.valid = function(){
    var result = $("#delegateForm").valid();
    if(result == false){
        return result;
    }
    //是否有指定并且启动的委托流程
    var postData = {
        id:$("#delegateForm #id").val(),
        definitionId:$("#delegateForm #definitionId").val(),
        startTime:$("#delegateForm #startTime").val(),
        endTime:$("#delegateForm #endTime").val(),
        useFlag:1
    }
    $.ajax({
        url:getRootPath()+"/delegate/validForUpdate.action",
        async:false,
        data:postData,
        success:function(data){
            if(data.valid==false){
                result = false;
                msgBox.tip({
                    content: "存在一个时间冲突的委托",
                    type:"fail"
                });
            }
        }
    });
    return result;
}

delegate.resetQueryForm = function(){
    $("#delegateQueryForm")[0].reset();
    delegate.userSelectForQuery.clearValue();
}

$("#delegateForm").validate({
    ignore: ".ignore",
    rules: {
        delegateUser:{
            validSelect2:'delegateUserDiv'
        },
        startTime:{
            required:true
        },endTime:{
            required:true
        }
    }
});

$(document).ready(function(){
    $(".datepicker-input").each(function(){$(this).datepicker();});
    delegate.pageRows = TabNub>0 ? TabNub : 10;
    delegate.delegateList();

    delegate.userSelect = JCTree.init({
        container   : 'delegateUserDiv',
        controlId   : 'delegateUser-delegateUser',
        isCheckOrRadio: false
    });

    delegate.userSelectForQuery = JCTree.init({
        container   : 'delegateUserForQueryDiv',
        controlId   : 'delegateUserForQuery-delegateUser',
        isCheckOrRadio: false
    });

    $("#queryForm").click(delegate.delegateList);
    $("#queryReset").click(delegate.resetQueryForm);

});
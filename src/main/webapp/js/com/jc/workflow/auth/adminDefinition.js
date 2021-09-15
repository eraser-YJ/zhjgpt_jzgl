var adminDefinition = {};

adminDefinition.pageRows = null;
adminDefinition.definitionId = -1;

adminDefinition.subState = false;

//分页处理 start
//分页对象
adminDefinition.oTable = null;
//显示列信息
adminDefinition.oTableAoColumns = [
    {mData: "title"},
    {mData: "actName"},
    {mData: "assigneeName"},
    {mData: "createDate"},
    {mData: "flowTrueStatus", mRender : function(mData, type, full) {
	    	var statusValue = "";
			
	    	if (full.flowTrueStatus == "CREATE") {
	    		statusValue = "创建";
	    	} else if (full.flowTrueStatus == "SUBMIT") {
	    		statusValue = "提交";
	    	} else if (full.flowTrueStatus == "REJECT") {
	    		statusValue = "退回";
	    	} else if (full.flowTrueStatus == "GETBACK") {
	    		statusValue = "拿回";
	    	} else if (full.flowTrueStatus == "MOVE") {
	    		statusValue = "转办";
	    	} else if (full.flowTrueStatus == "GOTO") {
	    		statusValue = "跳转";
	    	} else if (full.flowTrueStatus == "STOP") {
	    		statusValue = "终止";
	    	} else if (full.flowTrueStatus == "SUSPEND") {
	    		statusValue = "暂停";
	    	} else if (full.flowTrueStatus == "RESUME") {
	    		statusValue = "恢复";
	    	} else if (full.flowTrueStatus == "END") {
	    		statusValue = "结束";
	    	}
		
			return statusValue;
		}
	},
    //设置权限按钮
    {mData: function(source) {
        return oTableSetButtons(source);
    }}
];


adminDefinition.taskList = function () {
    //组装后台参数
    adminDefinition.oTableFnServerParams = function(aoData){
        getTableParameters(adminDefinition.oTable, aoData);
        var definitionId = adminDefinition.definitionId;
        //组装查询条件
        aoData.push({ "name": "definitionId", "value": definitionId});
        $.each($("#queryTaskForm").serializeArray(), function(i, o) {
            if(o.value != ""){
                aoData.push({ "name": o.name, "value": o.value});
            }
        });
    };
    //table对象为null时初始化
    if (adminDefinition.oTable == null) {
        adminDefinition.oTable = $('#taskTable').dataTable( {
            iDisplayLength: adminDefinition.pageRows,//每页显示多少条记录
            sAjaxSource: getRootPath()+"/definitionAuth/getAdminTaskByPage.action",
            fnServerData: oTableRetrieveData,//查询数据回调函数
            aoColumns: adminDefinition.oTableAoColumns,//table显示列
            fnServerParams: function ( aoData ) {//传参
                adminDefinition.oTableFnServerParams(aoData);
            },
            aaSorting:[],//设置表格默认排序列
            aoColumnDefs: [{bSortable: false, aTargets: [0,2,4,5]}]
        });
    } else {
        //table不为null时重绘表格
        adminDefinition.oTable.fnDraw();
    }
};


adminDefinition.defTree = {}

adminDefinition.defTree.treeSetting = {
    data: {
        simpleData: {
            enable: true
        }
    },
    async: {
        enable: true,
        url:getRootPath()+"/definitionAuth/getAdminDefTree.action",
    },
    callback:{
        onClick:function(event, treeId, treeNode){
            if(treeNode.type == "def") {
                adminDefinition.definitionId = treeNode.definitionId;
                adminDefinition.taskList();
            }
        },
        onAsyncSuccess:function(event, treeId, treeNode, msg){
            adminDefinition.defTree.tree.expandAll(true );
        }
    }
}



adminDefinition.defTree.init = function(){
    adminDefinition.defTree.tree = $.fn.zTree.init($("#defTree"), adminDefinition.defTree.treeSetting);
}

adminDefinition.definitionId = null;

adminDefinition.resetQueryForm = function(){
    $("#queryTaskForm")[0].reset();
}

adminDefinition.stop = function(taskId,instanceId,nodeId,definitionId){
    msgBox.confirm({
        content: "是否停止该流程",
        success: function(){
            adminDefinition.stopCallBack(taskId,instanceId,nodeId,definitionId);
        }
    });
}


adminDefinition.deleteFlow = function(instanceId){
    msgBox.confirm({
        content: "是否删除该流程",
        success: function(){
            adminDefinition.deleteCallBack(instanceId);
        }
    });
}

//打开转办对话框
adminDefinition.showAgentModule = function(taskId,instanceId,nodeId,definitionId){
    adminDefinition.taskId = taskId;
    adminDefinition.instanceId = instanceId;
    adminDefinition.nodeId = nodeId;
    adminDefinition.definitionId = definitionId;
    $("#agentUserModal").modal("show")
    adminDefinition.agentUserTree.clearValue();
}

//打开转办对话框
adminDefinition.agent = function(){
    var confirmUserId = $("input[name='agentUser']").val();
    if(confirmUserId.length == 0){
        msgBox.tip({
            type:"fail",
            content: "请选择转办人"
        });
        return;
    }
    if(adminDefinition.subState)return;
    adminDefinition.subState = true;
    $.get(getRootPath()+"/definitionAuth/agentTask.action",{taskId:adminDefinition.taskId,definitionId:adminDefinition.definitionId,instanceId:adminDefinition.instanceId,nodeId:adminDefinition.nodeId,userId:confirmUserId},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "转办任务成功",
                callback:adminDefinition.agentCallBack
            });
        }else{
            msgBox.tip({
                content: data.message
            });
        }
        adminDefinition.subState = false;
    });
}

adminDefinition.agentCallBack = function(){
    adminDefinition.taskList();
    $("#agentUserModal").modal("hide");
}

adminDefinition.stopCallBack = function(taskId,instanceId,nodeId,definitionId){
    if(adminDefinition.subState)return;
    adminDefinition.subState = true;
    $.get(getRootPath()+"/definitionAuth/stopTask.action",{taskId:taskId,definitionId:definitionId,instanceId:instanceId,nodeId:nodeId},function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "停止任务成功",
                callback:adminDefinition.taskList
            });
        }else{
            msgBox.tip({
                content: "停止任务出错"
            });
        }
        adminDefinition.subState = false;
    });
}

adminDefinition.deleteCallBack = function(instanceId){
    if(adminDefinition.subState)return;
    adminDefinition.subState = true;
    $.get(getRootPath()+"/instance/deleteProcess.action",{instanceId_:instanceId},function(data){
        if(data.code == '0'){
            msgBox.tip({
                type:"success",
                content: "删除任务成功",
                callback:adminDefinition.taskList
            });
        }else{
            msgBox.tip({
                content: "删除任务出错"
            });
        }
        adminDefinition.subState = false;
    });
}

adminDefinition.showJumpSelectNodeModule = function(taskId,curNodeId,definitionId,instanceId){
    adminDefinition.taskId = taskId;
    adminDefinition.nodeId = curNodeId;
    adminDefinition.definitionId = definitionId;
    adminDefinition.instanceId = instanceId;
    $.ajax({
        type : "POST",
        url : getRootPath()+"/definitionAuth/getJumpNodes.action",
        async:false,
        data : {"definitionId": definitionId,curNodeId:curNodeId},
        dataType : "json",
        success : function(data) {
            $("#jumpNodeSelectModal #jumpSelectDiv tbody").html("");
            for(var i=0;i<data.length;i++) {
                var node = data[i];
                if (node.componentId.toUpperCase() != curNodeId.toUpperCase()) {
                    var htmlStr = "<tr>";
                    htmlStr += "<td><input type='radio' name='jumpSelectRadio' value='" + node.componentId + "' />" + node.name + "</td>";
                    htmlStr += "</tr>";
                    $("#jumpNodeSelectModal #jumpSelectDiv tbody").append(htmlStr);
                }
            }
            $('#jumpNodeSelectModal').modal('show');
        }
    });
}

adminDefinition.showJumpSelectUserModule = function(){
    var selectNode = ($('input[name="jumpSelectRadio"]').filter(':checked'));
    if(selectNode.length == 0){
        msgBox.tip({
            type:"fail",
            content: "请选节点信息"
        });
        return;
    }
    adminDefinition.jumpNode = selectNode.val();
    adminDefinition.jumpUserTree.clearValue();
    $('#jumpNodeSelectModal').modal('hide');
    $('#jumpUserSelectModal').modal('show');
}

adminDefinition.jumpTask = function(){
    var confirmUserId = $("input[name='jumpUser']").val();
    if(confirmUserId.length == 0){
        msgBox.tip({
            type:"fail",
            content: "请选择人员"
        });
        return;
    }
    var ajaxData = {
        taskId:adminDefinition.taskId,
        nodeId:adminDefinition.nodeId,
        jumpNodeId:adminDefinition.jumpNode,
        userId:confirmUserId,
        instanceId:adminDefinition.instanceId,
        definitionId:adminDefinition.definitionId
    }
    if(adminDefinition.subState)return;
    adminDefinition.subState = true;
    $.post(getRootPath()+"/definitionAuth/jumpTask.action",ajaxData,function(data){
        if(data.success == true){
            msgBox.tip({
                type:"success",
                content: "跳转任务成功",
                callback:adminDefinition.jumpCallBack
            });
        }else{
            msgBox.tip({
                content: "跳转任务出错"
            });
        }
        adminDefinition.subState = false;
    });
}

adminDefinition.jumpCallBack = function(){
    adminDefinition.taskList();
    $("#jumpUserSelectModal").modal("hide");
}

$(document).ready(function(){

    $(".datepicker-input").each(function(){$(this).datepicker();});

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

    adminDefinition.pageRows = TabNub>0 ? TabNub : 10;
    adminDefinition.defTree.init();

    adminDefinition.taskList();

    $("#query").click(adminDefinition.taskList);
    $("#queryReset").click(adminDefinition.resetQueryForm);

    adminDefinition.agentUserTree = JCTree.init({
        container   : 'agentUserDiv',
        controlId   : 'agentUser-agentUser',
        isCheckOrRadio:false
    });

    adminDefinition.jumpUserTree = JCTree.init({
        container: 'jumpUserDiv',
        controlId: 'jumpUser-jumpUser',
        isCheckOrRadio: false
    });
});

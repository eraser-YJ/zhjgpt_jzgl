/**
 * Created by sunpeng on 16-4-28.
 */
var todoList = {};

todoList.getData = function(){
    $.ajax({
        type : "POST",
        url : getRootPath()+"/demo/manageTodoList.action",
        data : '',
        success : function(data) {

            $("#demoTable tbody").html("");
            for(var i=0;i<data.length;i++){
                var item = data[i];
                var htmlStr = "<tr><td>"+item.days+"</td>" +
                    "<td>"+item.business_key_+"</td>" +
                    "<td>"+item.start_time+"</td>"+
                    "<td>"+item.end_time+"</td>"+
                    "<td>"+item.workflowBean.curUserId_+"</td>"+
                    "<td><a class='btn btn-default' href='"+getRootPath()+"/demo/load.action?id="+item.id+"&workflowBean.taskId_="+item.workflowBean.taskId_+"&workflowBean.curNodeId_="+item.workflowBean.curNodeId_+"&workflowBean.instanceId_="+item.workflowBean.instanceId_+"' role='button' >办理</a></td>";
                $("#demoTable tbody").append(htmlStr)
            }
        }
    });
}

$(document).ready(function(){
    todoList.getData();
});
/**
 * Created by sunpeng on 16-4-28.
 */
var doneList = {};

doneList.getData = function(){
    $.ajax({
        type : "POST",
        url : getRootPath()+"/demo/manageDoneList.action",
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
                    "<td><a class='btn btn-default' href='"+getRootPath()+"/demo/load.action?id="+item.id+"&workflowBean.curNodeId_="+item.workflowBean.curNodeId_+"&workflowBean.instanceId_="+item.workflowBean.instanceId_+"&workflowBean.definitionKey_="+item.workflowBean.definitionKey_+"' role='button' >查看</a></td>";
                $("#demoTable tbody").append(htmlStr)
            }
        }
    });
}

$(document).ready(function(){
    doneList.getData();
});
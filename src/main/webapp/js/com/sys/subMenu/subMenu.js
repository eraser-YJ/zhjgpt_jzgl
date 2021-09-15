function selSubDuty(deptId) {
    var url = getRootPath()+"/sys/subUser/changeOrder.action";
    jQuery.ajax({
        url : url,
        type : 'POST',
        data : {"deptId": deptId},
        dataType : "json",
        success : function(data) {
            if(data.success == "true"){
                window.location.reload();
            }
        }
    });
}/**
 * Created by Administrator on 2018-4-24.
 */

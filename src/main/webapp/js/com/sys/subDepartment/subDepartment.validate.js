$(document).ready(function(){

    //初始化校验方法
    var deptAdd = $("#subDepartmentForm").validate({
        //ignore: ".select2-focusser",
        rules: {
            queue:
                {
                    required: true,
                    digits: true,
                    min:1,
                    maxlength: 3
                }
        }
    });

    var deptUpdate = $("#subDepartmentUpdateForm").validate({
        //ignore: ".select2-focusser",
        rules: {
            queue:
                {
                    required: true,
                    digits : true,
                    min:1,
                    maxlength: 3
                }
        }
    });

});
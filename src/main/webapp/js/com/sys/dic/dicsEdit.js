var dicsEdit = {};

//重复提交标识
dicsEdit.subState = false;

//保存方法
dicsEdit.dicSubmit = function(){
    if (dicsEdit.subState)return;
    dicsEdit.subState = true;
    if($("#dicForm").valid()){
        var formData = $("#dicForm").serializeArray();
        var addUrl = getRootPath()+"/dic/save.action?time="+(+new Date().getTime());
        var updateUrl = getRootPath()+"/dic/update.action?time="+(+new Date().getTime());
        var url;

        if($("#id").val().length > 0){
            url = updateUrl;
        }else{
            url = addUrl;
        }
        $("#dataLoad").fadeIn();//为指定按钮添加数据加载动态显示：即将DIV显示出来
        jQuery.ajax({
            url: url,
            type: 'post',
            data: formData,
            success: function(data, textStatus, xhr) {
                dicsEdit.subState = false;
                //getToken();
                hideErrorMessage();
                $("#token").val(data.token);
                if(data.errorMessage!=null){
                    alertx(data.errorMessage);
                }else{
                    // alertx("保存成功");
                    msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
                    $('#myModal-edit').on('hidden.bs.modal', function () {
                        diclist.reDicTree();
                    }).modal('hide');

                }
                $("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
            },error:function(){
                //alertx("保存数据错误。");
                msgBox.tip({content: "保存数据错误", type:'fail'});
                $("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
            }
        });
    } else {
        dicsEdit.subState = false;
    }
};

//初始化
jQuery(function($)
{
    $("#dicsbtn").click(dicsEdit.dicSubmit);
});



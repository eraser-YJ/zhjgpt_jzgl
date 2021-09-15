var dicinfo = {};
//重复提交标识
dicinfo.subState = false;

dicinfo.dicTreeType = function(){//加载数据字典左侧树方法
	jQuery.ajax({
		url : getRootPath()+'/dic/getDicInfoTreeTypes.action',
		type : 'POST',
		success : function(data) {
			$("#treeDemo").html(data);//要刷新的div
		}
	});
};

dicinfo.loaddicinfo = function(code){//加载左侧菜单下包含子菜单方法
	jQuery.ajax({
		url : getRootPath()+"/dic/getDicInfo.action?dicId="+code,
		type : 'POST',
		success : function(data) {
			$("#dicInfoname").html("");
			$("#dicInfoname").html(data);//要刷新的div
			dicinfo.fnDrawCallback();
		}
	});
};


dicinfo.fnDrawCallback=function() {
    if($("#treeDemo")[0]){
    	var content = $("#content").height();
        var headerHeight_1 = $('#header_1').height() || 0;
        var headerHeight_2 = $("#header_2").height() || 0;
        $(".tree-right").css("padding-left","215px");
		$("#LeftHeight").height(content-80-headerHeight_1-headerHeight_2);
        var lh = $("#LeftHeight").height(); 
        if($("#scrollable").scrollTop()>=113){
            $("#LeftHeight").addClass("fixedNav");
            $("#LeftHeight").height(lh + 113);
        }else{
            var a = $("#scrollable").scrollTop()>=113?113:$("#scrollable").scrollTop();
            $("#LeftHeight").height(lh + a);
            $("#LeftHeight").removeClass("fixedNav");
        }
    }
    
};

//保存方法
dicinfo.dicSubmit = function(){
	if(dicinfo.subState)return;
		dicinfo.subState = true;
	if ($("#dicInfoname").valid()) {
		var url = getRootPath()+"/dic/updateDic.action";
		var formData = $("#dicInfoname").serializeArray();
		jQuery.ajax({
			url : url,
			type : 'POST',
			cache: false,
			data : formData,
			success : function(data) {
				dicinfo.subState = false;
				msgBox.tip({
					type:"success",
					content: $.i18n.prop("JC_SYS_001")
				});
				$("#token").val(data.token);
				dicinfo.dicTreeType();
			},
			error : function() {
				dicsModule.subState = false;
				msgBox.tip({
					type:"fail",
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		dicsModule.subState = false;
	}
};


/**
 * 初始化方法
 */
jQuery(function($){
	dicinfo.dicTreeType();
});
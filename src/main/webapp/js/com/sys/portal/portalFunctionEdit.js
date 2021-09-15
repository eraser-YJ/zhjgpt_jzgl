var pFunctionEdit = {};
  	
//重复提交标识
pFunctionEdit.subState = false;

//验证名称是否重复
pFunctionEdit.valNameEcho = function(){
	var returnvalNameEcho = false;
	var formData = $("#pFunctionForm").serializeArray();
	jQuery.ajax({
		url: getRootPath()+"/sys/portalFunction/valNameEcho.action?time="+(+new Date().getTime()),
		type: "post",
		async:false,
		data: formData,
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.info({content: "功能组件名称重复，请重新命名", type:'fail'});
				returnvalNameEcho = false;
			}else {
				returnvalNameEcho = true;
			}
		}
	});
	return returnvalNameEcho;
};

//保存方法
pFunctionEdit.pFunctionSubmit = function(){
	if (pFunctionEdit.subState)return;
	  
	  //alert(pFunctionEdit.checkRepeat);
  	if($("#pFunctionForm").valid() && pFunctionEdit.valNameEcho()){
	  var formData = $("#pFunctionForm").serializeArray();
	  var addUrl = getRootPath()+"/sys/portalFunction/save.action?time="+(+new Date().getTime());
	  var updateUrl = getRootPath()+"/sys/portalFunction/update.action?time="+(+new Date().getTime());
	  var url;
	  
	  if($("#id").val().length > 0){
		  url = updateUrl;
	  }else{
		  url = addUrl;
	  }
	  jQuery.ajax({
		  url: url,
		  type: 'post',
		  data: formData,
		  success: function(data, textStatus, xhr) {
			  pFunctionEdit.subState = false;
			  $("#token").val(data.token);
			  if(data.errorMessage!=null){
				  //alert(data.errorMessage);
				  //msgBox.tip({content: data.errorMessage, type:'fail'});
			  }else{
				  //alertx("保存成功");
				  if($("#id").val().length > 0)
					  msgBox.tip({content: $.i18n.prop("JC_SYS_003"), type:'success'});
				  else
					  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
				  $('#myModal-edit').modal('hide');
				  pFunction.pFunctionList();
			  }
		  },error:function(){
			  //alert("保存数据错误。");
			  if($("#id").val().length > 0)
				  msgBox.tip({content: $.i18n.prop("JC_SYS_004"), type:'fail'});
			  else
				  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
		  }
		});
	} else {
	  pFunctionEdit.subState = false;
	}
};

//初始化
{
	$("#pFunctionbtn").click(pFunctionEdit.pFunctionSubmit);
	$("#closebtn").click(pFunction.clearForm(pFunctionForm));
};	



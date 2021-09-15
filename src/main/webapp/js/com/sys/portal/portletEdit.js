var portletEdit = {};
  	
//重复提交标识
portletEdit.subState = false;

//验证名称是否重复
portletEdit.valNameEcho = function(){
	var returnvalNameEcho = false;
	var formData = $("#portletForm").serializeArray();
	jQuery.ajax({
		url: getRootPath()+"/sys/portlet/valNameEcho.action?time="+(+new Date().getTime()),
		type: "post",
		async:false,
		data: formData,
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.info({content: "同一门户下门户组件标题重复，请重新命名", type:'fail'});
				returnvalNameEcho = false;
			}else {
				returnvalNameEcho = true;
			}
		}
	});
	return returnvalNameEcho;
};
//验证功能是否在友情链接中存在历史数据
portletEdit.valLinkEcho = function(){
	var returnvalLinkEcho = false;
	var formData = $("#portletForm").serializeArray();
	jQuery.ajax({
		url: getRootPath()+"/sys/portlet/valLinkEcho.action?time="+(+new Date().getTime()),
		type: "post",
		async:false,
		data: formData,
		success: function(data, textStatus, xhr) {
			if(data.success == "false"){
				msgBox.info({content: "原友情链接组件存在历史数据，请先删除历史数据再更改组件类型", type:'fail'});
				returnvalLinkEcho = false;
			}else {
				returnvalLinkEcho = true;
			}
		}
	});
	return returnvalLinkEcho;
};

//保存方法
portletEdit.portletSubmit = function(){
	if (portletEdit.subState)return;
	  
  	if($("#portletForm").valid() && portletEdit.valNameEcho() && portletEdit.valLinkEcho()){
	  var formData = $("#portletForm").serializeArray();
	  var addUrl = getRootPath()+"/sys/portlet/save.action?time="+(+new Date().getTime());
	  var updateUrl = getRootPath()+"/sys/portlet/update.action?time="+(+new Date().getTime());
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
			  portletEdit.subState = false;
			  $("#token").val(data.token);
			  $("#tokens").val(data.token);
			  if(data.errorMessage!=null){
				  //alert(data.errorMessage);
				  msgBox.tip({content: data.errorMessage, type:'fail'});
			  }else{
				  //alertx("保存成功");
				  if($("#id").val().length > 0)
					  msgBox.tip({content: $.i18n.prop("JC_SYS_003"), type:'success'});
				  else
					  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
				  $('#myModal-edit').modal('hide');
				  portlet.portletList();
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
		portletEdit.subState = false;
	}
};


//左 --> 右
portletEdit.addFunction = function() {
	var count = $("#portletForm #optionfunctions").find("option:selected").length;
	if(count==0) {
		return;
	}
	var viewType = $("#portletForm #viewType").val();
	if(viewType == 'onetable' || viewType == 'shortcut'){
		$("#portletForm #optionfunctions").find("option:selected").each(function(){
			$("#portletForm #optfunctions").find("option").each(function(){
				$("#portletForm #optionfunctions").append($(this).clone());
				$(this).remove();
			});
			$("#portletForm #functionId").val($(this).val());
			$("#portletForm #functionName").val($(this).text());
			$("#portletForm #optfunctions").append($(this).clone());
			$(this).remove();
		});
	}
	if(viewType == 'moretable'){
		$("#portletForm #optionfunctions").find("option:selected").each(function(){
			$("#portletForm #optfunctions").append($(this).clone());
			var funids = '';
			var funnames = '';
			$("#portletForm #optfunctions").find("option").each(function(){
				if(funids == ''){
					funids = $(this).val();
					funnames = $(this).text();
				} else {
					funids = funids+","+$(this).val();
					funnames = funnames+","+$(this).text();
				}
			});
			
			$("#portletForm #functionId").val(funids);
			$("#portletForm #functionName").val(funnames);
			$(this).remove();
		});
	}
};

//右-->左
portletEdit.removeFunction = function() {
	var count = $("#portletForm #optfunctions").find("option:selected").length;
	if(count==0) {
		return;
	}
	$("#portletForm #optfunctions").find("option:selected").each(function(){
		$("#portletForm #optionfunctions").append($(this).clone());
		$(this).remove();
	});
	
	var funids = '';
	var funnames = '';
	$("#portletForm #optfunctions").find("option").each(function(){
		if(funids == ''){
			funids = $(this).val();
			funnames = $(this).text();
		} else {
			funids = funids+","+$(this).val();
			funnames = funnames+","+$(this).text();
		}
	});
	
	$("#portletForm #functionId").val(funids);
	$("#portletForm #functionName").val(funnames);
};

//初始化
{
	//计算分页显示条数
	$("#portletbtn").click(portletEdit.portletSubmit);
	$("#closebtn").click(portlet.clearForm(portletForm));
};	



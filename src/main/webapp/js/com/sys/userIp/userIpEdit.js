var userIpEdit = {};
  	
//重复提交标识
userIpEdit.subState = false;

userIpEdit.userEditTree = null;

//保存方法
userIpEdit.userIpSubmit = function(){
  if (userIpEdit.subState)return;
  
  if(userIpEdit.checkRepeatName($("#userIpForm #add").val())){
	  userIpEdit.subState = true;

	  if($("#userIpForm").valid()){
		  var formData = $("#userIpForm").serializeArray();
		  var addUrl = getRootPath()+"/sys/userIp/save.action?time="+(+new Date().getTime());
		  var updateUrl = getRootPath()+"/sys/userIp/update.action?time="+(+new Date().getTime());
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
				  userIpEdit.subState = false;
				  $("#token").val(data.token);
				  if(data.errorMessage!=null){
					  //alert(data.errorMessage);
					  msgBox.tip({content: data.errorMessage, type:'fail'});
				  }else{
					  //alertx("保存成功");
					  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
					 // if(userIp.userTree) userIp.userTree.clearValue();
					  $('#myModal-edit').modal('hide');
					  userIp.userIpList();
				  }
			  },error:function(){
				  //alert("保存数据错误。");
				  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
			  }
			});
	  } else {
		  userIpEdit.subState = false;
	  }
  }else {
	  return;
  }
};

//清空IP段值
userIpEdit.ipclear = function(){
	$("#StartIp1").val("");
	$("#StartIp2").val("");
	$("#EndIp").val("");
};


//自定义验证人员选择重复
userIpEdit.checkRepeatName = function(userId){
	var userIpid = $("#userIpForm #id").val();
	var checkRepeat = false;
	if(userId == ''){
		msgBox.info({content: "请选择需要绑定IP的用户", type:'fail'});
		checkRepeat = false;
	}else {checkRepeat = true;
		/*jQuery.ajax({
		       url: getRootPath()+"/sys/userIp/checkRepeatName.action?userId="+userId+"&userIpid="+userIpid,
		       type: "get",
		       async:false,
		       dataType: 'json',
			   success: function(data, textStatus, xhr) {
				   if(data.success == "false"){
					   msgBox.info({content: "该用户已存在绑定的IP", type:'fail'});
					   checkRepeat = false;
				   }else {
					   checkRepeat = true;
				   }
		       }
		});*/	
	}
	return checkRepeat;
};


userIpEdit.treeDivClear= function(){
	//if(userIp.userTree) userIp.userTree.clearValue();
	userIp.clearForm(userIpForm);
};

//初始化
jQuery(function($) 
{
	$("#userIpbtn").click(userIpEdit.userIpSubmit);
	$("#closebtn").click(userIpEdit.treeDivClear);

	userIpEdit.userEditTree = new JCFF.JCTree.Lazy({
		container  	: 'userFormTree',
		controlId		: 'add-userId',
		single		: true,
        funFormData: function(){
            return {
                weight : '0'
            }
        }
	});
});	



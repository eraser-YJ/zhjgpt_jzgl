var rolePortalEdit = {};
  	
//重复提交标识
rolePortalEdit.subState = false;

//门户共享范围保存
rolePortalEdit.shareSubmit = function(){
	if (portalEdit.subState)return;
	var token = $("#tokens").val();
	var url = getRootPath()+"/sys/rolePortal/save.action";
	  jQuery.ajax({
		  type : "POST",
		  url: url,
	      data : {"userids": $("#UserleftRight-UserleftRight").val(),"roleids": "","deptids": $("#DeptleftRight-DeptleftRight").val(),"organids": "","portalId":$("#portalId").val(),"token":token},
		  dataType : "json",
		  success : function(data) {
			  rolePortalEdit.subState = false;
			  $("#tokens").val(data.token);
			  $("#token").val(data.token);
			  $('#myModal-share').modal('hide');
			  portal.portalList();
			  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
			},
	      error:function(){
			  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
		  }
		});
	 
};

//初始化
$(document).ready(function(){
	portal.shareModalLoad();
	$("#portalsharebtn").click(rolePortalEdit.shareSubmit);
});



var rolePortletEdit = {};
  	
//重复提交标识
rolePortletEdit.subState = false;

//门户共享范围保存
rolePortletEdit.shareSubmit = function(){
	if (rolePortletEdit.subState)return;
	var token = $("#tokens").val();
	var url = getRootPath()+"/sys/rolePortal/save.action";
	  jQuery.ajax({
		  type : "POST",
		  url: url,
	      data : {"userids": $("#UserleftRight-UserleftRight").val(),"roleids": "","deptids": $("#DeptleftRight-DeptleftRight").val(),"organids": "","portaletId":$("#portletId").val(),"token":token},
		  dataType : "json",
		  success : function(data) {
			  rolePortletEdit.subState = false;
			  $("#tokens").val(data.token);
			  $("#token").val(data.token);
			  $('#myModal-share').modal('hide');
			  portlet.portletList();
			  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
			},
	      error:function(){
			  msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
		  }
		});
	 
};

//初始化
{
	$("#portletsharebtn").click(rolePortletEdit.shareSubmit);
}



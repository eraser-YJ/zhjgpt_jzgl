var phraseEdit = {};
  	
//重复提交标识
phraseEdit.subState = false;

//保存方法
phraseEdit.phraseSubmit = function(){
  if (phraseEdit.subState)return;
  	  phraseEdit.subState = true;
  if($("#phraseForm").valid()){
	  var formData = $("#phraseForm").serializeArray();
	  var addUrl = getRootPath()+"/sys/phrase/save.action?time="+(+new Date().getTime());
	  var updateUrl = getRootPath()+"/sys/phrase/update.action?time="+(+new Date().getTime());
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
			  phraseEdit.subState = false;
			  //getToken();
			  hideErrorMessage();
			  $("#token").val(data.token);
			  if(data.errorMessage!=null){
				  alertx(data.errorMessage);
			  }else{
				 // alertx("保存成功");
				  msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
				  $('#myModal-edit').modal('hide');
				  phrase.phraseList();
			  }
			  $("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
		  },error:function(){
			  //alertx("保存数据错误。");
			  msgBox.tip({content: "保存数据错误", type:'fail'});
			  $("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
		  }
		});
  } else {
	  phraseEdit.subState = false;
  }
};

//鼠标右键粘贴事件
$.fn.pasteEvents = function( delay ) {    
	if (delay == undefined) delay = 20;     
	return $(this).each(function() {         
		var $el = $(this);         
		$el.on("paste", function() {            
			$el.trigger("prepaste");             
			setTimeout(function() { $el.trigger("postpaste"); }, delay);        
		});     
	}); 
};

//改变输入字数显示
phraseEdit.checkLen  =  function (obj)
{
	var str = $("#phraseForm #content").val();
	var namelength = str.length;
	var maxChars = 30-namelength;//最多字符数
	if (obj.value.length > 30){
		obj.value = obj.value.substring(0,30);
		maxChars = 0;
	}
	var curr = maxChars;
	$("#phraseForm #count").html(curr.toString());
};
  
//初始化
jQuery(function($) 
{
	$("#phrasebtn").click(phraseEdit.phraseSubmit);
	//内容字数递减控制
	$("#phraseForm #content").on("postpaste", function() { phraseEdit.checkLen (this);}).pasteEvents();
	$("#phraseForm #content").keyup(function() { phraseEdit.checkLen (this);}); 
});	



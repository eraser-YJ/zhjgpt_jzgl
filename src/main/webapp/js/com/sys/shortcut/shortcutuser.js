//初始化方法
var shortcutuse = {};

//重复提交标识
shortcutuse.subState = false;

//添加修改公用方法
shortcutuse.saveOrModify = function () {
	
	shortcutuse.evaluaterefresh();
    var count = $(".evaluate_name").size();
    
    var evaluate_id = new Array();
	var evaluate_alert = new Array();
	for(var i=1;i <=count;i++){
	   evaluate_id[i] =  $("input[id='shortcutid_"+i+"']").val();
	   evaluate_alert[i] =  $("input[id='index_alert_time_"+i+"']").val();
	}
	
	var shortcutids = evaluate_id.join('@@@');
	var sequence = evaluate_alert.join('@@@');
	
	$.ajax({
		url:getRootPath()+"/sys/userShortcut/save.action?time="+(+new Date().getTime()),
		type:"POST",
		data:{
			 shortcutids:shortcutids,
			 sequence:sequence
		},
		dataType: 'json',
		success:function(data, textStatus, xhr){
			msgBox.tip({content: $.i18n.prop("JC_SYS_001"), type:'success'});
		},error:function(){
        	msgBox.tip({content: $.i18n.prop("JC_SYS_002"), type:'fail'});
        }
	});
};

shortcutuse.setshortcut = function(e,shortcutName,shortcutid){
	if(e.checked){
		var n = $(".evaluate_name").size();
	    var html = '';
	    html = "<div id='div_"+shortcutid+"'><table class='table port-btn'><tr><td><label class='eName evaluate_name' id='index_name_"+n+"'>"+shortcutName+"</label></td>";
	    html += "<td><input class='eAlert evaluate_alert' readonly id='index_alert_time_"+n+"' value='"+n+"'/><input hidden='hidden' id='shortcutid_"+n+"' value='"+shortcutid+"'/></td>";
	    html += "</table></div>";
	    $("#controlshortcut").append(html);
	    //使div可以拖拽
	    $("#controlshortcut").sortable({
	        update: function (event, ui) {
	        	shortcutuse.evaluaterefresh();
	        }
	    });
	    //指标重新排序
	    shortcutuse.evaluaterefresh();
	} else {
		shortcutuse.evaluatemove(shortcutid);
	}
};

//刷新重载数据排序及绑定拖拽
shortcutuse.reloadevaluate = function(){
	$("#controlshortcut").sortable({
        update: function (event, ui) {
        	shortcutuse.evaluaterefresh();
        }
    });
};

//移除指标
shortcutuse.evaluatemove = function(n){//alert(linkid);
	$("#div_"+n+"").remove();
	shortcutuse.evaluaterefresh();
};

//移除指标后重新编号
shortcutuse.evaluaterefresh = function(){//alert(linkid+"----"+$('.evaluate_name'+linkid).length);
	var count=$('.evaluate_name').length;
	for(var i=0;i<count;i++){
	    var j = i+1;
		$('#controlshortcut div:eq('+i+') label:eq(0)').attr("id","index_name_"+j);
		$('#controlshortcut div:eq('+i+') input:eq(0)').attr("id","index_alert_time_"+j);
		$('#controlshortcut div:eq('+i+') input:eq(1)').attr("id","shortcutid_"+j);
		$('#controlshortcut div:eq('+i+') input:eq(0)').attr("value",j);
	}
};

$(document).ready(function(){
	ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
	$("#settingbtn").click(function(){shortcutuse.saveOrModify();});
	$("#controlshortcut").sortable({
        update: function (event, ui) {
        }
    });
});
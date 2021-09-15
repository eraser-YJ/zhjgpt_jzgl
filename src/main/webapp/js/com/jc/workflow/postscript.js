Date.prototype.format =function(format)
{
var o = {
	"M+" : this.getMonth()+1, //month
	"d+" : this.getDate(), //day
	"h+" : this.getHours(), //hour
	"m+" : this.getMinutes(), //minute
	"s+" : this.getSeconds(), //second
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter
	"S" : this.getMilliseconds() //millisecond
	}
	if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
	(this.getFullYear()+"").substr(4- RegExp.$1.length));
	for(var k in o)if(new RegExp("("+ k +")").test(format))
	format = format.replace(RegExp.$1,
	RegExp.$1.length==1? o[k] :
	("00"+ o[k]).substr((""+ o[k]).length));
	return format;
}


var postscript = {}; 
var index = 1;
var replyIndex = 1;
var postscriptArray = new Array();
var postscriptReplyArray = new Array();

postscript.open = function(isFirst){
	var postscriptNums = 0;
	if(postscriptArray.length > 0 ){
		postscript.firstOpen();
		return;
	}
	if(isFirst){
	var instanceId = $("#workflowBean\\.instanceId_").val();
	jQuery.ajax({
		url : getRootPath()+"/workflow/postscript/queryPostscriptAndReply.action",
		type : 'POST',
		dataType : 'json',
		async: false,
		data :{ instanceId : instanceId},
		success : function(data) {
			var html = "<ul>";
			var postscriptList = data.allPostscripts.postscriptList;
			var postscriptReplyList = data.allPostscripts.postscriptReplyList;
			var userId = data.userId;
			for(var i = 0 ; i < postscriptList.length ; i++){
				html += "<li class='clearfix m-b input-group'>";
				html += "<p class='dialog-text input-group'>";
				html += "<i class='fa fa-comment input-group-btn p-r-m'></i>";
				html += "<strong>"+postscriptList[i].createUserName+":</strong>" + postscriptList[i].content;
				html += "<span>"+postscriptList[i].realCreateDate+"</span>";
				html += "</p>";
				html += "<div class='input-group-btn'>";
				html += "<a href='#' class='a-icon i-new diologReply m-r-xs'  onclick='postscript.showAddPostscript(\""+postscriptList[i].id+"\")'><i class='fa fa-comment'></i>回 复</a>";
				if( userId == postscriptList[i].createUser){
					html += "<a href='#' class='a-icon i-remove'><i onclick='postscript.deletePostscript(2,1,\""+postscriptList[i].id+"\")' class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='删除'></i></a>";
				}
				html += "</div>";
				postscriptNums++;
				for(var x = 0 ; x < postscriptReplyList.length; x++){
					if(postscriptList[i].id == postscriptReplyList[x].postscriptId){
						html += "<div class='dis-row'>";
						html += "<p class='dialog-text input-group'>";
						html += "<i class='fa fa-comment input-group-btn p-r'></i>";
						html += "<strong>"+postscriptReplyList[x].createUserName+":</strong>" + postscriptReplyList[x].content;
						html += "<span>"+postscriptReplyList[x].realCreateDate+"</span>";
						html += "</p>";
						if( userId == postscriptReplyList[x].createUser){
							html += "<div class='input-group-btn'><a class='a-icon i-remove fr' href='javascript:;'><i onclick='postscript.deletePostscript(2,2,\""+postscriptList[i].id+"\",\""+postscriptReplyList[x].id+"\")' class='fa fa-remove' data-toggle='tooltip' title='' data-placement='top' data-container='body' data-original-title='删除'></i></a></div>";
						}
						html += "</div>";
						postscriptNums++;
					}
				}
			}
			html += "</ul>";
			html += "<div class='replyBox m-t hide'>";
			html += "<textarea id='content' name='content' rows='5' style='width:90%'></textarea>";
			html += "<section class='form-btn m-b-lg m-t'>";
			html += "<button class='btn dark' type='button' onclick='postscript.savePostscript()'>提 交</button>";
			html += "<button class='btn replyCancel' type='reset'>取 消</button>";
			html += "</section>";
			html += "</div>";
			$("#postscriptDiv").html(html);
			if(postscriptNums<=99){
				$("#postscriptNums").html(postscriptNums);
			}else{
				$("#postscriptNums").html("99+");
			}
			
		},
		error : function() {
		}
	});
	}
	
	
	$("[data-toggle=tooltip]").tooltip();
	postscript.buildEvent();
}
postscript.firstOpen = function(){
	var postscriptNums = 0;
	var html = "<ul>";
	for(var i = 0 ; i < postscriptArray.length ; i++){
		html += "<li class='clearfix m-b input-group'>";
		html += "<p class='dialog-text input-group'>";
		html += "<i class='fa fa-comment input-group-btn p-r'></i>";
		html += "<strong>"+$("#postscriptUserName").val()+":</strong>" + postscriptArray[i].content;
		html += "<span>"+postscriptArray[i].dataTime+"</span>";
		html += "</p>";
		html += "<div class='input-group-btn'>";
		html += "<a href='#' class='a-icon i-new diologReply m-r-xs' onclick='postscript.showAddPostscript("+postscriptArray[i].id+")'><i class='fa fa-comment'></i>回 复</a>";
		html += "<a href='#' class='a-icon i-remove'><i onclick='postscript.deletePostscript(1,1,"+postscriptArray[i].id+")' class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='删除'></i></a>";
		html += "</div>";
		postscriptNums++;
		for(var x = 0 ; x < postscriptReplyArray.length; x++){
			if(postscriptArray[i].id == postscriptReplyArray[x].postscriptId){
				html += "<div class='dis-row'>";
				html += "<p class='dialog-text input-group'>";
				html += "<i class='fa fa-comment input-group-btn p-r'></i>";
				html += "<strong>"+$("#postscriptUserName").val()+":</strong>" + postscriptReplyArray[x].content;
				html += "<span>"+postscriptReplyArray[x].dataTime+"</span>";
				html += "</p>";
				html += "<div class='input-group-btn'><a class='a-icon i-remove fr' href='javascript:;'><i onclick='postscript.deletePostscript(1,2,"+postscriptReplyArray[x].id+")' class='fa fa-remove' data-toggle='tooltip' title='' data-placement='top' data-container='body' data-original-title='删除'></i></a></div>";
				html += "</div>";
				postscriptNums++;
			}
		}
	}
	html += "</ul>";
	html += "<div class='replyBox m-t hide'>";
	html += "<textarea id='content' name='content' rows='5' style='width:90%'></textarea>";
	html += "<section class='form-btn m-b-lg m-t'>";
	html += "<button class='btn dark' type='button' onclick='postscript.savePostscript()'>提 交</button>";
	html += "<button class='btn replyCancel' type='reset'>取 消</button>";
	html += "</section>";
	html += "</div>";
	$("#postscriptDiv").html(html);
	if(postscriptNums<=99){
		$("#postscriptNums").html(postscriptNums);
	}else{
		$("#postscriptNums").html("99+");
	}
	postscript.buildEvent();
	$("[data-toggle=tooltip]").tooltip();
	var postscriptInfoRecord = "{\"postscriptInfo\":[";
	for(var i = 0 ; i < postscriptArray.length; i++){
		if(i == 0 ){
			postscriptInfoRecord += "{\"id\":\""+postscriptArray[i].id+"\",\"content\":\""+postscriptArray[i].content+"\",\"dateTime\":\""+postscriptArray[i].dataTime+"\"}";
		}else{
			postscriptInfoRecord += ",{\"id\":\""+postscriptArray[i].id+"\",\"content\":\""+postscriptArray[i].content+"\",\"dateTime\":\""+postscriptArray[i].dataTime+"\"}"
		}
	}
	postscriptInfoRecord += "]}";
	
	var postscriptReplyInfoRecord = "{\"postscriptReplyInfo\":[";
	for(var i = 0 ; i < postscriptReplyArray.length; i++){
		if(i == 0 ){
			postscriptReplyInfoRecord += "{\"postscriptId\":\""+postscriptReplyArray[i].postscriptId+"\",\"content\":\""+postscriptReplyArray[i].content+"\",\"dateTime\":\""+postscriptReplyArray[i].dataTime+"\"}";
		}else{
			postscriptReplyInfoRecord += ",{\"postscriptId\":\""+postscriptReplyArray[i].postscriptId+"\",\"content\":\""+postscriptReplyArray[i].content+"\",\"dateTime\":\""+postscriptReplyArray[i].dataTime+"\"}"
		}
	}
	postscriptReplyInfoRecord += "]}";
	$("#workflowBean\\.postscriptData").val(postscriptInfoRecord);
	$("#workflowBean\\.postscriptReplyData").val(postscriptReplyInfoRecord);
}
postscript.getRealTime = function(createDate){
	var year = 1900 + parseInt(createDate.year);
	var month = (parseInt(createDate.month) + 1)>=10?(parseInt(createDate.month) + 1):"0"+(parseInt(createDate.month) + 1);
	var date = (parseInt(createDate.date))>=10?(parseInt(createDate.date)):"0"+(parseInt(createDate.date));
	var hours = (parseInt(createDate.hours))>=10?(parseInt(createDate.hours)):"0"+(parseInt(createDate.hours));
	var minutes = (parseInt(createDate.minutes))>=10?(parseInt(createDate.minutes)):"0"+(parseInt(createDate.minutes));
	var seconds = (parseInt(createDate.seconds))>=10?(parseInt(createDate.seconds)):"0"+(parseInt(createDate.seconds));
	return year + "-" + month + "-" + date +"&nbsp;" + hours + ":" + minutes + ":" + seconds;
}
postscript.savePostscript = function(){
	var content = $("#postscriptForm #content").val();
	content = $.trim(content);
	if(content.length==0){
		msgBox.info({
			type:"fail",
			content:$.i18n.prop("JC_SYS_010")
		});
		return;
	}
	var instanceId = $("#workflowBean\\.instanceId_").val();
	
	if( $("#flowStatus").val() == '7' || $("#flowStatus").val() == '8' || $("#flowStatus").val() == '100X' ){
		msgBox.info({
			type:"fail",
			content:"流程已结束"
		});
		return;
	}
	//不是发起申请时
	if(instanceId.length > 0){
		var formData = $("#postscriptForm").serializeArray();
		var url = "";
		if($("#postscriptId").val() == 0){
			url = getRootPath()+"/workflow/postscript/save.action";
		}else{
			url = getRootPath()+"/workflow/postscriptReply/save.action";
		}
		formData.push({"name": "instanceId", "value": instanceId});
		jQuery.ajax({
			url : url,
			type : 'POST',
			async: false,
			data : formData,
			success : function(data) {
				if(data.success == "false"){
					msgBox.info({
						content:$.i18n.prop("JC_OA_COMMON_015")
					});
				}
				postscript.open(true);
			},
			error : function() {
			}
		});
	}
	//发起申请时添加附言
	else{
		var content = $("#postscriptForm #content").val();
		if($("#postscriptId").val() == 0){
			var postscriptInfo = {
					content:content,
					id:index++,
					dataTime:new Date().format('yyyy-MM-dd hh:mm:ss')
			}
			postscriptArray.push(postscriptInfo);
		}else{
			var postscriptReplyInfo = {
					postscriptId:$("#postscriptId").val(),
					content:content,
					id:replyIndex++,
					dataTime:new Date().format('yyyy-MM-dd hh:mm:ss')
			}
			postscriptReplyArray.push(postscriptReplyInfo);
		}
		postscript.firstOpen();
	}
}
/*
 * type为回复类型（0为增加附言，其他为回复附言id）
 */
postscript.showAddPostscript = function(type){
	$("#postscriptId").val(type);
}
postscript.buildEvent = function(){
	$('.diologReply').click(function(){
	    $('.replyBox ').removeClass('hide');
	    $('.replyBox #content').focus();
	    $("#postscriptDiv").scrollTop($("#postscriptDiv").height());
	})
	$('.replyCancel').click(function(){
		$('.replyBox ').addClass('hide');
	})
}
/*
 * operType操作类型
 * postscriptType 附言或回复
 */
postscript.deletePostscript = function(operType,postscriptType,id,replyId){
	console.log("asdasd");
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_034"),
		success: function(){
			if(operType == 1){
				if(postscriptType == 1){
					for(var i = 0 ; i < postscriptArray.length ; i++){
						if( id == postscriptArray[i].id){
							postscriptArray.splice(i,1);
						}
					}
				}else if(postscriptType == 2){
					for(var i = 0 ; i < postscriptReplyArray.length ; i++){
						if( id == postscriptReplyArray[i].id){
							postscriptReplyArray.splice(i,1);
						}
					}
				}
				
			}else if(operType == 2){
				jQuery.ajax({
					url : getRootPath()+"/workflow/postscript/deletePostscriptIOrReply.action",
					type : 'POST',
					async: false,
					data :{ type : postscriptType,id:id,replyId:replyId},
					success : function(data) {
						if(	data == 1 ){
							msgBox.info({
								content:$.i18n.prop("JC_OA_COMMON_015")
							});
							postscript.open(true);
						}
					},
					error : function() {
					}
				});
			}
			postscript.open(true);
		}
	});
}

postscript.hide = function(){
	$("#postscriptID").hide();
}

postscript.show = function(){
	$("#postscriptID").show();
}

$(document).ready(function(){
	 $("#postscriptID").height($("#scrollable").height()-150);
	 $("#postscriptDiv").height($("#postscriptID").height()-60);
	 postscript.open();
	 $('#postscriptClick').click(
	       function(e){
	           if(!$("#postscriptID").hasClass("sq")){
	        	   	isIeBrowser>0&&setPostscriptInOffice();
	            	postscript.open();
	            	$("#postscriptID").addClass("sq").find(".fa").addClass("fa-caret-right").end().animate({right:'0'},500)
	           }else{
	        	   $("#postscriptID").removeClass("sq").find(".fa").removeClass("fa-caret-right").end().animate({right:'-532px'},500);
		           postscript.buildEvent();
		           isIeBrowser>0&&setTimeout(function(){removePostscriptInOffice();},550);
	           }
	           e.stopPropagation();
	        }
	 );
	 $('#scrollable').scroll(function(){
		 $("#postscriptID").css('bottom',0-$('#scrollable').scrollTop());
	 });
 });
//附言追加iframe
function setPostscriptInOffice(){
    var iframeBodyCover = document.createElement("iframe");
    iframeBodyCover.id = "__PostscriptIframeBodyCovere_g";
    iframeBodyCover.style.backgroundColor= "transparent";
    iframeBodyCover.style.cssText = "position: absolute; bottom: 0px; right: 0px; width: 583px; height: 600px;filter:alpha(opacity=0);";
    iframeBodyCover.setAttribute('frameborder', '0', 0);
    iframeBodyCover.src = "javascript:'';";
    document.body.appendChild(iframeBodyCover);
    $("#__PostscriptIframeBodyCovere_g").css("height",($("#scrollable").height()-150)+"px");
}
//清除附言追加的iframe
function removePostscriptInOffice(){
    $('#__PostscriptIframeBodyCovere_g').remove();
}
//重置附言下边据
function resetPostscript() {
	var p = $("#postscriptID");
	if(p.length){
		p.hide()
		 .css('bottom',0-$('#scrollable').scrollTop())
		 .show();
	}
}
/**
 * 常用词相关js
 */
var phraseComponent = {}
var base64 = new Base64();
phraseComponent.data = null;		//常用词数据

phraseComponent.index = 0;		//常用词数据

phraseComponent.init = function(opt){
	var container = $("#"+opt.containerId);
	
	phraseComponent.index++;
	var divId = 'phrase'+phraseComponent.index;
	if(container.length==0){
		msgBox.info({
			type:"fail",
			content:"没有id为<br>"+opt.containerId+"的组件"
		});
		return;
	}
	$(container[0]).append(phraseComponent.template(divId));
	phraseComponent.initDiv(phraseComponent.data,opt,divId);
}

phraseComponent.getData = function(){
	var result = null;
	var ajaxData = {
			time:new Date()
	};
	$.ajax({
		type : "GET",
		url : getRootPath()+"/sys/phrase/queryPhrase.action",
		data:ajaxData,
		async:false,
		dataType : "json",
		success : function(data) {
			result = data;
		}
	});
	return result;
}

phraseComponent.fillContent = function(fillEleId,content,divId){
	var fillEle = $("#"+fillEleId);
	if(fillEle.length==0){
		msgBox.info({
			type:"fail",
			content:"没有id为<br>"+fillEleId+"的组件"
		});
		return;
	}
	var oldValue = fillEle.val();
	fillEle.val(oldValue+base64.decode(content));
	phraseComponent.close(divId);
}


/**
 * 生成html内容
 */
phraseComponent.template = function(divId){
	var html = '<a href="#" class="a-icon i-new" data-toggle="common" onclick="phraseComponent.open(this,\''+divId+'\')"  name="'+divId+'">常用词</a>';
	return html;
}

phraseComponent.open = function(obj,divId){
	phraseComponent.refreshData(divId);
}

phraseComponent.initDiv = function(data,opt,divId){
	var divHtml = '<div class="panel g-nav-xs w530 clearfix" type="phrase" fillEleId="'+opt.fillEleId+'" id="'+divId+'">'+
						'<section class="tabs-wrap">'+
							'<ul class="nav nav-tabs">'+
								'<li class="active">'+
									'<a href="#phrasePerson" data-toggle="tab">个人</a>'+
								'</li>'+
								'<li>'+
									'<a href="#phraseCommon" data-toggle="tab">公共</a>'+
								'</li>'+
								'<button type="button" class="close" data-toggle="close" onclick="phraseComponent.close(\''+divId+'\')">×</button>'+
							'</ul>'+
							'<div class="tab-content">'+
								'<div class="tab-pane fade active in" id="phrasePerson">'+
									'<div class="table-flow">'+
										'<div class="table">'+
											'<table id="phrasePersonTable" class="table table-striped table-bordered b-light first-td-tc tab_height"><thead><tr><th style="width:80%">内容</th><th>操作</th></tr></thead><tbody>'+
											'</tbody></table>'+
										'</div>'+
									'</div>'+
									'<div class="g-nav-footer">'+
										'<input type="text" class="w300" style="margin-bottom:4px"/>'+
										'<span class="fr" >'+
											'<a href="###" class="a-icon i-new" onclick="phraseComponent.add(this,\''+divId+'\')">新增</a>'+
											'<a href="###" class="a-icon i-new" onclick="phraseComponent.addAndFill(this,\''+divId+'\')">新增并填入文本框</a>'+
										'</span>'+
									'</div>'+
								'</div>'+
								'<div class="tab-pane" id="phraseCommon">'+
									'<div class="table">'+
										'<table id="phraseCommonTable" class="table table-striped table-bordered b-light first-td-tc tab_height"><thead><tr><th style="width:80%">内容</th><th>操作</th></tr></thead><tbody>'+
										'</tbody></table>'+
									'</div>'+
								'</div>'+
							'</div>'+
						'</section>'+
					'</div>';
	//$("#"+opt.containerId).append(divHtml);
	$("#scrollable").append(divHtml);
	//置为可以编辑
	var oTable = $('#'+divId+' #phrasePersonTable').dataTable({
		"bServerSide": false,
		"bPaginate": false,
		aaSorting:[],//设置表格默认排序列
		"aoColumnDefs": [{"bSortable": false, "aTargets": [0,1]}]
	});
	
	$('#'+divId+' #phraseCommonTable').dataTable({
		"bServerSide": false,
		"bPaginate": false,
		aaSorting:[],//设置表格默认排序列
		"aoColumnDefs": [{"bSortable": false, "aTargets": [0,1]}]
	});
	
}

/**
 * 添加数据
 */
phraseComponent.add = function(obj,divId){
	var input = $($(obj).parents(".g-nav-footer")[0]).find("input");
	var inputValue = input.val();
	if(inputValue==null||inputValue.length==0){
		msgBox.info({
			type:"fail",
			content:$.i18n.prop("JC_SYS_010")
		});
		return;
	}else if(inputValue.length>30){
		msgBox.info({
			type:"fail",
			content:$.i18n.prop("JC_SYS_011","30")
		});
		return;
	}
	/*else if($.hasSpecialChar(inputValue,"~`!#$%^&*()-=+{}[]|\\\":;'?/><,")){
		msgBox.info({
			type:"fail",
			content:"输入中含有特殊字符"
		});
		return false;
	}*/
	var ajaxData = {
			content:inputValue,
			time:new Date()
	}
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/phrase/saveForDataTables.action",
		data:ajaxData,
		async:false,
		dataType : "json",
		success : function(data) {
			phraseComponent.refreshData(divId);
		}
	})
}

/**
 * 添加数据并填入到textarea
 */
phraseComponent.addAndFill = function(obj,divId){
	var input = $($(obj).parents(".g-nav-footer")[0]).find("input");
	var content =  input.val();
	if(content==null||content.length==0){
		msgBox.info({
			type:"fail",
			content:$.i18n.prop("JC_SYS_010")
		});
		return;
	}else if(content.length>30){
		msgBox.info({
			type:"fail",
			content:$.i18n.prop("JC_SYS_011","30")
		});
		return;
	}
	else if($.hasSpecialChar(content,"~`!#$%^&*()-=+{}[]|\\\":;'?/><,")){
		msgBox.info({
			type:"fail",
			content:"输入中含有特殊字符"
		});
		return false;
	}
	phraseComponent.add(obj,divId);
	var fillEleId = $("#"+divId).attr("fillEleId");
	phraseComponent.fillContent(fillEleId,base64.encode(content),divId);
}

/**
 * 关闭窗口
 */
phraseComponent.close = function(divId){
	$('#__iframeBodyCovere_Common').remove();
	$("#"+divId).removeClass('g-nav-block');
}

/**
 * 刷新数据函数
 */
phraseComponent.refreshData = function(divId){
	//添加个人常用词
	var data = phraseComponent.getData();
	var fillEleId = $("#"+divId).attr("fillEleId");
	var personPhrase = data.person;
	var trHtml ="";
	$('#'+divId).find("#phrasePersonTable tbody").html("");
	for(var i=0;i<personPhrase.length;i++){
		var p = personPhrase[i].content;
		if (p && p.length > 0) {
			p = base64.encode(p);
		}
		trHtml += '<tr id="'+personPhrase[i].id+'">'+
					'<td id="content" style="white-space: inherit;">'+personPhrase[i].content+'</td>'+
					'<td>'+
						'<a class="a-icon i-new" href="###" onclick="phraseComponent.fillContent(\''+fillEleId+'\',\''+p+'\',\''+divId+'\')">'+
							'<i class="fa fa-audit" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="填入"></i>'+
						'</a>'+
						 '<a class="a-icon i-remove" href="###" onclick="phraseComponent.deleteConfirm(\''+personPhrase[i].id+'\',\''+divId+'\')">'+
							'<i class="fa fa-remove" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="删除"></i>'+
						'</a>'+
					'</td>'+
				 '</tr>';
	}

	$('#'+divId).find("#phrasePersonTable tbody").append(trHtml);
	$('#'+divId).find("#phraseCommonTable tbody").html("");
	//添加公共常用词
	var commonPhrase = data.common;
	trHtml ="";
	for(var i=0;i<commonPhrase.length;i++){
		var p = commonPhrase[i].content;
		if (p && p.length > 0) {
			p = base64.encode(p);
		}
		trHtml += '<tr id="'+commonPhrase[i].id+'">'+
					'<td style="white-space: inherit;">'+commonPhrase[i].content+'</td>'+
					'<td>'+
						'<a class="a-icon i-new" href="#">'+
							'<i class="fa fa-audit" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="填入" onclick="phraseComponent.fillContent(\''+fillEleId+'\',\''+p+'\',\''+divId+'\')"></i>'+
						'</a>'+
					 '</td>'+
				 '</tr>';
	}
	$('#'+divId).find("#phraseCommonTable tbody").append(trHtml);
	
	var oTable = $('#'+divId+' #phrasePersonTable').dataTable();
	$('#'+divId).find('td#content').editable(getRootPath()+'/sys/phrase/updateForDataTables.action', {
		callback: function( sValue, y ) {
			phraseComponent.refreshData(divId)
		},
		submitdata: function ( value, settings ) {
            return {
                "id": this.parentNode.getAttribute('id')
            };
        },onsubmit:function(form,td){
        	var inputValue = $(td).find("input").val();
        	if(inputValue==null||inputValue.length==0){
        		msgBox.info({
        			type:"fail",
        			content:$.i18n.prop("JC_SYS_010")
        		});
        		return false;
        	}else if(inputValue.length>30){
        		msgBox.info({
        			type:"fail",
        			content:$.i18n.prop("JC_SYS_011","30")
        		});
        		return false;
        	}
			/*else if($.hasSpecialChar(inputValue,"~`!#$%^&*()-=+{}[]|\\\":;'?/><,")){
        		msgBox.info({
        			type:"fail",
        			content:"输入中含有特殊字符"
        		});
        		return false;
        	}*/
        },
        name : 'content',
        "width": "100%"
    });
	$("i[data-toggle='tooltip']").tooltip();
	ie8TableStyle();
	$("#"+divId+" input").val("");
	setTimeout(function(){
		$('#'+divId).find("#phrasePersonTable tbody").hide();
		$('#'+divId).find("#phrasePersonTable tbody").show();
	},600);
}

/**
 * 删除确认函数
 */
phraseComponent.deleteConfirm = function(id,divId){
	phraseComponent.deleteId = id;
	phraseComponent.divId = divId;
	msgBox.confirm({
			content:$.i18n.prop("JC_SYS_034"),
			success:phraseComponent.deleteFun
		});
}

/**
 * 删除函数
 */
phraseComponent.deleteFun = function(){
	var id = phraseComponent.deleteId;
	var divId = phraseComponent.divId;
	var ajaxData = {
			ids:id,
			time:new Date()
	}
	$.ajax({
		type : "POST",
		url : getRootPath()+"/sys/phrase/deleteByIds.action",
		data:ajaxData,
		async:false,
		success : function(data) {
			$("#"+divId).find("tr#"+id).remove();
		}
	});
}

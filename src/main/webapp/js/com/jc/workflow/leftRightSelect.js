/**
 * 	依赖:	JQuery控件
 * 			zTree控件
 * 			structure自定义控件
 * 			ChinesePY汉字转拼音
 * 			select2选择控件
 * 
 * 	选择控件调用方法:[
 * 		在相对应需要引入控件的页面引入依赖及其本控件的JS调用
 * 		leftRightSelect = function(opt);
 * 		参数:[
 *			containerId 	是要显示本控件的DivID		注释:页面多次调用控件传入的值不能重复
 *			moduleId 			是本控件中文本框的id与name	注释:页面多次调用控件传入的值不能重复[*规则是"id-name",id不要重名]以便后台取值
 *			isCheck				是多选还是单选				注释:true:多选,flase:单选
 *			url					加载数据url		注释返回leftRight实体类数组，code为存储值，text为显示值
 *			title				标题	
 *			callback			回调函数
 * 		]
 * 		例子:[
 * 				leftRightSelect.init("controlTree1","id-name", false, false);
 * 		]
 * 	]
 * 	
 * 	清除:[引用一次出现多个控件,初始化调用leftRightSelect.clear方法]
 *  清值:[重置按钮中调用leftRightSelect.clearValue方法传入控件ID]
 */

var leftRightSelect = {};

leftRightSelect.itemData = {};

leftRightSelect.index = 0;	//控件ID,Name下标标示为避免同一页面多次调用重复

leftRightSelect.init = function(opt) {
	this.index = parseInt(leftRightSelect.index) + 1;		//更新下标
	
	var clearBtnId = "leftRightClearBtn" + this.index;
	
	var inputStr = leftRightSelect.getInputTemplate(opt.moduleId,opt.isCheck);
	$("#" + opt.containerId).html(inputStr);
	var itemData =null;
	if(opt.data!=null){
		itemData = opt.data;
		leftRightSelect.itemData[opt.moduleId] = itemData;
	}else if(opt.url!=null){
		itemData = leftRightSelect.getData(opt.moduleId,opt.url);
	}
	leftRightSelect.initInputSearch(opt.moduleId,opt.isCheck,itemData);
	leftRightSelect.initPopModel(opt.moduleId,opt.title,itemData,opt.isCheck,opt.callback);
	
	//初始化事件
	$("input#"+ opt.moduleId).on("change", function(e){
		leftRightSelect.openPutAwayClear(opt.moduleId);
	});
	
	$("#"+clearBtnId).bind("click", function(){
		leftRightSelect.clearValue(opt.moduleId);
		leftRightSelect.openPutAwayClear(opt.moduleId);
		if(typeof resetPostscript == 'function'){
			resetPostscript();
		}
	});
}

/**
 * 获得数据
 * 参数：	moduleId:控件id
 * 		url:请求后台路径
 */
leftRightSelect.getData = function(moduleId,url){
	var result = null;
	$.ajax({
		url : url,
		type : 'post',
		async: false,
		data: {
			time: new Date()
		},
		success : function(data) {
			result = data;
		},
		error: function(){
			alertx("获取人员失败");
		}
	});
	leftRightSelect.itemData[moduleId] = result;
	return result;
}

/**
 * 初始化搜索框
 * 参数：		moduleId:控件id
 * 			isCheck:是否为多选
 * 			data:加载的数据
 */
leftRightSelect.initInputSearch = function(moduleId,isCheck,data){
	var item  = [];
	$.each(data, function(i, o) {
		item[i] = {
			id : o.code,								//ID
			text : o.text,							//显示的内容
			hp: Pinyin.GetHP(o.text),				//两个汉字输入全拼,三个以上的汉字，第一个汉字全拼，后面的汉字简拼
			qp: Pinyin.GetQP(o.text),				//汉字的全拼
			jp: Pinyin.GetJP(o.text),				//汉字的简拼
			wc: Pinyin.getWordsCode(o.text)			//单词首字母获取
		};
	});
	leftRightSelect.select2InitToLR(item, moduleId, isCheck);
}

/**
 * 获得输入框模板
 * 参数：			moduleId:控件id
 */
leftRightSelect.getInputTemplate = function(moduleId,isCheck){
	
	var openLeftRightDivId = "openLeftRightDiv"+ this.index;	//弹出层DivId
	var leftRighBtnId = "openLeftRightBtn" + this.index;		//按钮ID
	var clearBtnId = "leftRightClearBtn" + this.index;
	var result = '<div class="select2-wrap input-group  w-p100">'+
    				'<div class="fl w-p100">'+
						'<input type="hidden" id="'+moduleId+'" name="'+moduleId.split("-")[1]+'" search="search" style="width:100%" popDivId ="'+openLeftRightDivId+'"/>'+
					'</div>'+
    				'<a class="btn btn-file no-all input-group-btn" href="#" onClick="leftRightSelect.show(\''+moduleId+'\',\''+openLeftRightDivId+'\',\''+isCheck+'\');" id="'+leftRighBtnId+'" role="button" data-toggle="modal">'+
						'<i class="fa fa-group"></i>'+
					'</a>'+
					'<div class="input-group-btn m-l-xs selection-tree-btn fr">'+
						'<a class="a-icon i-trash fr m-b" href="#" id="'+clearBtnId+'"><i class="fa fa-trash"></i>清空</a>'+
						'<a class="a-icon i-new zk fr" href="#"><i class="fa fa-chevron-down"></i>展开</a>'+
						'<a class="a-icon i-new sq fr" href="#"><i class="fa fa-chevron-up"></i>收起</a>'+
					'</div>'+
				 '</div>'+
				 '<label class="help-block hide"></label>';
	return result;
}

/**
 * 初始化select2控件
 *  参数：	moduleId:控件id
 * 			item:加载的数据
 * 			isCheck:是否为多选
 * 
 */
leftRightSelect.select2InitToLR = function(item, moduleId, isCheck){
	$("#"+moduleId).select2({
	    placeholder: " ",		//文本框占位符显示
	    allowClear: true,			//允许清除
	    multiple: isCheck ? true : false,//单选or多选
	    query: function (query){
	        var data = {results: []};
	        $.each(item, function(){
	            if(query.term.length == 0 || this.text.toUpperCase().indexOf(query.term.toUpperCase()) >= 0 
	            		|| this.hp.toUpperCase().indexOf(query.term.toUpperCase()) >= 0
	            		|| this.qp.toUpperCase().indexOf(query.term.toUpperCase()) >= 0
	            		|| this.jp.toUpperCase().indexOf(query.term.toUpperCase()) >= 0
	            		|| this.wc.toUpperCase().indexOf(query.term.toUpperCase()) >= 0){
	                data.results.push({id: this.id, text: this.text});
	            }
	        });
	        query.callback(data);
	    }
	});
}

/**
 * 初始化弹出页
 * 参数：	moduleId:控件id
 * 		title:标题
 * 		isCheck:加载的数据
 */
leftRightSelect.initPopModel = function(moduleId,title,itemData,isCheck,callback){
	var popHtml = leftRightSelect.getPopHTML(moduleId,title,isCheck);
	$(popHtml).appendTo("body");
	//初始化确定事件
	var openLeftRightDivId = "openLeftRightDiv"+ this.index;
	var rightSelectId = 'rightList'+this.index;
	$("#"+openLeftRightDivId).find("[submitType=true]").click(function(){
		leftRightSelect.fillInput(rightSelectId,moduleId,isCheck,callback);
	});
}

/**
 * 初始化弹出页
 * 参数：	moduleId:控件id
 * 		title:标题
 * 		isCheck:是否单选
 */
leftRightSelect.getPopHTML = function(moduleId,title,isCheck){
	var openLeftRightDivId = "openLeftRightDiv"+ this.index;	//弹出层DivId
	var leftSelectId = 'leftList'+this.index;
	var rightSelectId = 'rightList'+this.index;
	
	var resultHtml = '<div class="modal fade" id="'+openLeftRightDivId+'" ischeck="'+isCheck+'" aria-hidden="false">'+
			'<div class="modal-dialog modal-tree">'+
				'<div class="modal-content">'+
					'<div class="modal-header clearfix">'+
						'<button type="button" class="close" data-dismiss="modal" onClick="">×</button>'+
						'<h4 class="modal-title fl">'+title+'选择</h4>'+
					'</div>'+
					'<div class="modal-body clearfix">'+
						'<div class="ms2side__div">'+
		                    '<div class="ms2side__select">'+
		                        '<div class="ms2side__header">'+title+'选择框</div>'+
		                        '<select title="'+title+'选择框" name="'+leftSelectId+'" id="'+leftSelectId+'" size="0" multiple="multiple" class="select-list-h"></select>'+
		                    '</div>'+
		                    '<div class="ms2side__options" style="padding-top: 6.5px;">'+
		                        '<p class="AddOne ms2side__hide" title="添加" id="toLeft'+this.index+'" onclick="leftRightSelect.toRight(\''+leftSelectId+'\',\''+rightSelectId+'\',\''+isCheck+'\')"><span></span></p>'+(isCheck?
		                        '<p class="AddAll ms2side__hide" title="添加所有" id="allToLeft'+this.index+'" onclick="leftRightSelect.allToRight(\''+leftSelectId+'\',\''+rightSelectId+'\',\''+isCheck+'\')"><span></span></p>':'')+
		                        '<p class="RemoveOne ms2side__hide" title="删除" id="toRight'+this.index+'" onclick="leftRightSelect.toLeft(\''+leftSelectId+'\',\''+rightSelectId+'\',\''+isCheck+'\')"><span></span></p>'+(isCheck?
		                        '<p class="RemoveAll ms2side__hide" title="删除所有" id="allToRight'+this.index+'" onclick="leftRightSelect.allToLeft(\''+leftSelectId+'\',\''+rightSelectId+'\',\''+isCheck+'\')"><span></span></p>':'')+
		                    '</div>'+
		                    '<div class="ms2side__select">'+
		                        '<div class="ms2side__header">已选'+title+'框</div>'+
		                        '<select title="已选'+title+'框" name="'+rightSelectId+'" id="'+rightSelectId+'" size="0" multiple="multiple" class="select-list-h"></select>'+
		                    '</div>'+
		                '</div>'+
					'</div>'+
					'<div class="modal-footer form-btn">'+
						'<button submitType="true" class="btn dark" type="button">保 存</button>'+
						'<button class="btn" type="button" onclick="$(\'#'+openLeftRightDivId+'\').modal(\'hide\')">关 闭</button>'+
					'</div>'+
				'</div>'+
			'</div>'+
		'</div>';
	return resultHtml;
}

/**
 * 设置回显人员
 * 参数：	moduleId:控件id
 * 		dataStr:回显的人员字符串   多个用","分割
 */
leftRightSelect.setData = function(moduleId,dataStr){
	var itemData = leftRightSelect.itemData[moduleId];
	var datas = new Array();
	if(dataStr!=null&&dataStr.length>0){
		var items = dataStr.split(",");
		for(var i=0;i<items.length;i++){
			var code = items[i];
			for(var j=0;i<itemData.length;j++){
				if(itemData[j].code == code){
					var dataTemp = {id:code,text:itemData[j].text};
					datas.push(dataTemp);
					break;
				}
			}
		}
	}
	
	if(datas.length == 1){
		datas = {
				id:datas[0].id,
				text:datas[0].text	
		}
	}
	$("#"+ moduleId).select2("data", datas);
	leftRightSelect.openPutAwayClear(moduleId);
}

/**
 * 将选中的项目填入到div中
 * 参数：	rightSelectId:右侧select控件id
 * 		moduleId:组件id
 * 		isCheck:是否多选
 */
leftRightSelect.fillInput = function(rightSelectId,moduleId,isCheck,callback){
	var choseData = $("#"+rightSelectId).find("option");
	var rov = [];//人员控件返回的数据集合
	if(choseData.length == 0){
		msgBox.info({
			type:"fail",
			content:"请选择数据"
		});
		return;
	}
	$.each(choseData,function(i, val){
		rov[i] = {
			id: this.value,
			text: this.text
		}
	});
	if(isCheck)
		$("#"+moduleId).select2("data", rov);//多选回显值
	else
		$("#"+moduleId).select2("data",{id: rov[0].id, text: rov[0].text});//单选回显值
	$($("#"+rightSelectId).parents(".modal")[0]).modal("hide");
	leftRightSelect.openPutAwayClear(moduleId);
	if(callback!=null){
		callback(choseData);
	}
}

/**
 * 显示并初始化弹出页
 *  参数：moduleId:组件id
 * 		popDivId:弹出divID
 */
leftRightSelect.show = function(moduleId,popDivId,isCheck){
	leftRightSelect.refresh(moduleId);
	var choiceDatas = $("#"+moduleId).select2("data");
	if(choiceDatas!=null){
		if(isCheck == "false"){
			var choiceData = choiceDatas;
			var obj = $("#"+popDivId).find("[id^=leftList]").find("option[value="+choiceData.id+"]");
			var optionObj = $("<option>").val(obj.val()).text(obj.text());
			optionObj.dblclick(leftRightSelect.rightDbclick);
			obj.parents(".ms2side__div").find("[id^=rightList]").append(optionObj);
			obj.remove();
		}
		else{
			for(var i=0;i<choiceDatas.length;i++){
				var choiceData = choiceDatas[i];
				var obj = $("#"+popDivId).find("[id^=leftList]").find("option[value="+choiceData.id+"]");
				var optionObj = $("<option>").val(obj.val()).text(obj.text());
				optionObj.dblclick(leftRightSelect.rightDbclick);
				obj.parents(".ms2side__div").find("[id^=rightList]").append(optionObj);
				obj.remove();
			}
		}
	}
	$("#"+popDivId).modal("show");
}

/**
 * 刷新左右选择框
 * 参数：moduleId:组件id
 */
leftRightSelect.refresh = function(moduleId){
	var itemData = leftRightSelect.itemData[moduleId];
	var popDivId = $("#"+moduleId).attr("popDivId");
	$("#"+popDivId).find("select").html("");
	$.each(itemData, function(i, o) {
		var option = $("<option>").val(o.code).text(o.text);
		$("#"+popDivId).find("[id^=leftList]").append(option);
	});
	$("#"+popDivId).find("[id^=leftList] option").dblclick(leftRightSelect.leftDbclick)
}

/**
 * toRight按钮事件
 * 参数：	leftId:左侧select的id
 * 		rightId:右侧select的id
 */
leftRightSelect.toRight = function(leftId,rightId,ischeck){
	
	var objs = $("#"+leftId).find("option:selected");
	if(ischeck == "false"){
		if(objs.length>1){
			msgBox.info({
				type:"fail",
				content:'只能选择一个条目'
			});
			return;
		}
		if(objs.length==1){
			//判断右侧是否有option,有的话需要移到左侧
			var rightObj = $("#"+rightId+" option");
			if(rightObj.length>0){
				var rightObjTemp = $("<option>").val($(rightObj[0]).val()).text($(rightObj[0]).text());
				rightObjTemp.dblclick(leftRightSelect.leftDbclick);
				$("#"+leftId).append(rightObjTemp);
				$(rightObj[0]).remove();
			}
		}
	}
	for(var i=0;i<objs.length;i++){
		var obj = $(objs[i]);
		var optionObj = $("<option>").val(obj.val()).text(obj.text());
		optionObj.dblclick(leftRightSelect.rightDbclick);
		$("#"+rightId).append(optionObj);
		obj.remove();
	}
	
}

/**
 * allToRight按钮事件
 * 参数：	leftId:左侧select的id
 * 		rightId:右侧select的id
 */
leftRightSelect.allToRight = function(leftId,rightId,ischeck){
	var objs = $("#"+leftId).find("option");
	if(ischeck == "false"){
		//判断右侧是否有option,有的话则不作操作
		var rightObj = $("#"+rightId+" option");
		if(rightObj.length>0){
			return;
		}
		if(objs.length>0){
			objs = $(objs[0]);
		}
		
	}
	for(var i=0;i<objs.length;i++){
		var obj = $(objs[i]);
		var optionObj = $("<option>").val(obj.val()).text(obj.text());
		optionObj.dblclick(leftRightSelect.rightDbclick);
		$("#"+rightId).append(optionObj);
		obj.remove();
	}
}

/**
 * toLeft按钮事件
 * 参数：	leftId:左侧select的id
 * 		rightId:右侧select的id
 */
leftRightSelect.toLeft = function(leftId,rightId,ischeck){
	var objs = $("#"+rightId).find("option:selected");
	for(var i=0;i<objs.length;i++){
		var obj = $(objs[i]);
		var optionObj = $("<option>").val(obj.val()).text(obj.text());
		optionObj.dblclick(leftRightSelect.leftDbclick);
		$("#"+leftId).append(optionObj);
		obj.remove();
	}
}

/**
 * allToLeft按钮事件
 * 参数：	leftId:左侧select的id
 * 		rightId:右侧select的id
 */
leftRightSelect.allToLeft = function(leftId,rightId,ischeck){
	var objs = $("#"+rightId).find("option");
	for(var i=0;i<objs.length;i++){
		var obj = $(objs[i]);
		var optionObj = $("<option>").val(obj.val()).text(obj.text());
		optionObj.dblclick(leftRightSelect.leftDbclick);
		$("#"+leftId).append(optionObj);
		obj.remove();
	}
}

/**
 * 左侧option双击事件
 */
leftRightSelect.leftDbclick = function(event){
	var obj = $(this);
	var ischeck = obj.parents(".modal").attr("ischeck");
	if(ischeck == "false"){
		//判断右侧是否有option,有的话需要移到左侧
		var rightObj = obj.parents(".ms2side__div").find("[id^=rightList]").find("option");
		if(rightObj.length>0){
			var rightObjTemp = $("<option>").val($(rightObj[0]).val()).text($(rightObj[0]).text());
			rightObjTemp.dblclick(leftRightSelect.leftDbclick);
			obj.parents(".ms2side__div").find("[id^=leftList]").append(rightObjTemp);
			$(rightObj[0]).remove();
		}
	}
	var optionObj = $("<option>").val(obj.val()).text(obj.text());
	optionObj.dblclick(leftRightSelect.rightDbclick);
	obj.parents(".ms2side__div").find("[id^=rightList]").append(optionObj);
	obj.remove();
}

/**
 * 右侧侧option双击事件
 */
leftRightSelect.rightDbclick = function(event){
	var obj = $(this);
	var optionObj = $("<option>").val(obj.val()).text(obj.text());
	optionObj.dblclick(leftRightSelect.leftDbclick);
	obj.parents(".ms2side__div").find("[id^=leftList]").append(optionObj);
	obj.remove();
}

/**
 * 左右选择树控件的展开、收起
 */
leftRightSelect.openPutAwayClear = function(moduleId){
	var ell = $("#"+moduleId);
	var el = ell.closest(".select2-wrap") || null;
	var $parent = el.find(".selection-tree-btn").parent();
	var tree_lh = $parent.find(".select2-choices").actual("height");
	if(tree_lh >= 66){
		el.find(".select2-container").css("max-height","67px");
		el.find(".selection-tree-btn").show();
		el.find(".selection-tree-btn .sq").hide();
		el.find(".selection-tree-btn .zk").show();
	}else{
		el.find(".selection-tree-btn .sq").hide();
		el.find(".selection-tree-btn .zk").hide();
		el.find(".selection-tree-btn").hide();
	}
	$parent.find(".zk").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","100%");
		$($this).hide();
		$parent.find(".sq").show();
	});
	$parent.find(".sq").click(function(){
		var $this = this;
		var $parent = el.find(".selection-tree-btn");
		el.find(".select2-container").css("max-height","67px");
		$($this).hide();
		$parent.find(".zk").show();
		if(typeof resetPostscript == 'function'){
			resetPostscript();
		}
	});
}

leftRightSelect.clearValue = function(moduleId){
	$("#"+moduleId).select2("data","");
	leftRightSelect.openPutAwayClear(moduleId);
}
var outSideUser = {};

outSideUser.callBackItemId = "";
outSideUser.index = 1;
//重复提交标识
outSideUser.subState = false;
//鼠标点击div事件
outSideUser.moveMouseFocus = function(itemId){
	$("#"+itemId).find("input").focus();
}
outSideUser.callback = function(){
	$("#"+outSideUser.callBackItemId).find("input").focus();
}
//input框失去焦点触发事件
outSideUser.blurOutSideUserDiv = function(itemId){
	if($("#"+itemId).find("input[type='text']").val().length > 0){
		var phone = $("#"+itemId).find("input[type='text']").val();
		jQuery.ajax({
			url :getRootPath()+"/ic/contacts/queryOutSideUser.action",
			type : 'POST',
			data : {phone:phone},
			success : function(data) {
				var appendHtml = "<li class='select2-search-field email-list-shou'>";
				if(data.length == 0){
					var phonet = /^[1][3-8]\d{9}$/;
					var phoneError = "";
					if( !phonet.test(phone)){
						phoneError = "phoneError = true";
					}
					var nameIndex = "i" + outSideUser.index++;
					if(!(phoneError.length > 0) ){
						var addHtml = getOutSideUserAdd(phone,nameIndex);
						appendHtml += "<span name="+nameIndex+""+phone+" ondblclick=outSideUser.editAnyOneUser(this) class='select2-search-choice' ><input type='hidden' id='outSide' name='outSide' value='"+phone+"'>"
						appendHtml += "<a style='color: #c30!important;font-family: verdana;' onclick=outSideUser.removeParent(this) >x&nbsp;</a><font>" + phone + "</font>&nbsp;"+addHtml+"&nbsp;";
					}else{
						var addHtml = getOutSideUserAdd(phone,nameIndex);
						appendHtml += "<span "+phoneError+" name="+nameIndex+""+phone+" ondblclick=outSideUser.editAnyOneUser(this) class='select2-search-choice' ><input type='hidden' id='outSide' name='outSide' value='"+phone+"'>"
						appendHtml += "<a style='color: #c30!important;font-family: verdana;' onclick=outSideUser.removeParent(this) >x&nbsp;</a><font style='color:red'>" + phone + "</font>&nbsp;"+addHtml+"&nbsp;";

					}
				}else{
					appendHtml += "<span id='"+data.id+"' class='select2-search-choice'><input type='hidden' id='outSide' name='outSide' value='"+phone+"'>"
					appendHtml += "<a style='color: #c30!important;font-family: verdana;' onclick=outSideUser.removeParent(this) >x&nbsp;</a><font>" + data.userName + "</font>&nbsp;<img  style='cursor:hand;vertical-align: middle;margin-top:-1px' width='11' height='11' src="+getRootPath()+"/img/edit.png onclick=outSideUser.showOutSideLayer(1,'"+data.id+"') >&nbsp;";
					
				}
				$("#"+itemId).find("input[type='text']").remove();
				appendHtml += "</span><input type='text' class='email-temp-input' /></li>";
				$("#"+itemId).append(appendHtml);
				outSideUser.initInputEvent(itemId);
				outSideUser.checkValid();
			},
			error : function() {
			}
		});
	}
	
}

outSideUser.checkValid = function(){
	var flag = true;
	//遍历错误联系人如果存在flag置为false
	$.each($("#outIcAreaValid span[phoneerror=true]"),function(i,o){
		flag = false;
		return true;
	});
	if(flag){
		$("#outLinkman").next().html("");
	}else{
		$("#outLinkman").next().remove();
		var error = '<label for="outLinkman" class="error">请输入有效的手机号码</label>';
		$("#outLinkman").after(error);
	}
};

outSideUser.removeParent = function(obj){
	$(obj).parent().remove();
}
//初始化联系人分组
outSideUser.showGroupNameList = function () {
	$.get(getRootPath()+"/ic/contactsGroup/groupList.action",null,function(data){
		$("#groupId").empty();
		$("#groupId").append($("<option>").val("").text("-请选择-"));
		$.each(data, function(i, o) {
			var option = $("<option>").val(o.id).text(o.groupName);
			$("#groupId").append(option);
		});
	});
};

outSideUser.editAnyOneUser = function(obj){
	$("#outSideUserDiv").find("input[type='text']").remove();
	var appendHtml = "<input type='text' style='border: 0' value='"+$(obj).find("input[type='hidden']").val()+"' /></span>";
	$("#outSideUserDiv").append(appendHtml);
	outSideUser.initInputEvent();
	$(obj).remove();
}

//添加修改公用方法
outSideUser.savaOrModify = function (hide) {
	if (outSideUser.subState)return;
	outSideUser.subState = true;
	if ($("#contactsForm").valid()) {
		var url = getRootPath()+"/ic/contacts/save.action?time=" + (+new Date());
		if ($("#contactsForm #id").val() != 0) {
			url = getRootPath()+"/ic/contacts/update.action?time=" + (+new Date());
		}
		var formData = $("#contactsForm").serializeArray();
		formData.push({"name": "token", "value": $("#token").val()});
		jQuery.ajax({
			url : url,
			type : 'POST',
			data : formData,
			success : function(data) {
				outSideUser.subState = false;
//				//刷新弹出层外部联系人token
//				getToken('contactsForm');
//				//刷新短信界面token
//				getToken();
				$("#token").val(data.token);
				if(data.success == "true"){
					msgBox.tip({
						type:"success",
						content: data.successMessage
					});
					
					if ($("#contactsForm #id").val() != 0) {
						$("#"+$("#contactsForm #id").val()).find("font").html($("#contactsForm #userName").val());
						$("#"+$("#contactsForm #id").val()).find("input[type='hidden']").val($("#contactsForm #phone").val())
					}else{
						$("span[name='"+$("#namelayer").val()+"']").attr("id",data.saveOverId);
						$("span[name='"+$("#namelayer").val()+"']").find("img").attr("src",getRootPath()+"/img/edit.png");
						$("span[name='"+$("#namelayer").val()+"']").find("img").attr("width","11");
						$("span[name='"+$("#namelayer").val()+"']").find("img").attr("height","11");
						$("span[name='"+$("#namelayer").val()+"']").find("img").attr("onclick","outSideUser.showOutSideLayer(1,'"+data.saveOverId+"')");
						$("span[name='"+$("#namelayer").val()+"']").find("font").html($("#contactsForm #userName").val());
						$("span[name='"+$("#namelayer").val()+"']").find("input[type='hidden']").val($("#contactsForm #phone").val());
						$("span[name='"+$("#namelayer").val()+"']").attr("phoneError","false");
						$("span[name='"+$("#namelayer").val()+"']").find("font").attr("style","");
					}
					if (hide) {
						$('#editContacts').modal('hide');
					}
				} else {
					if(data.labelErrorMessage){
						showErrors("contactsForm", data.labelErrorMessage);
					}else{
						msgBox.info({
							content: data.errorMessage
						});
					  } 
				}
			},
			error : function() {
				outSideUser.subState = false;
				msgBox.tip({
					content: $.i18n.prop("JC_SYS_002")
				});
			}
		});
	} else {
		outSideUser.subState = false;
		msgBox.info({
			content:"表单信息填写错误",
			success:function(){
				fnCall();
			}
		});
	}
};

var selectEveryItemId = "";
//显示此用户的联系人
outSideUser.showOutSideMain = function(itemId){
	outSideUser.showTree("outSideUserTree",itemId);
}
//显示此用户的点选联系人
outSideUser.showOutSideLayer = function(type,option,nameLayer){
	//设置外部联系人弹出界面token
	//getToken("contactsForm");
	hideErrorMessage();
	$("#contactsForm #userNameOld").val('');
	$("#contactsForm #mailOld").val('');
	$("#contactsForm #phoneOld").val('');
	$('#contactsForm')[0].reset();
	if(type == '0'){
		$("#contactsForm #id").val(0);
		$("#contactsForm #namelayer").val(nameLayer);
		$("#contactsForm #phone").val(option);
	}else if(type == '1'){
		$("#contactsForm #id").val(option);
		jQuery.ajax({
			url :getRootPath()+"/ic/contacts/queryOutSideUser.action",
			type : 'POST',
			data : {id:option},
			success : function(data) {
				$('#contactsForm')[0].reset();
				$("#contactsForm #groupRId").val('');
				$("#contactsForm").fill(data);
				$("#contactsForm #userNameOld").val(data.userName);
				$("#contactsForm #mailOld").val(data.mail);
				$("#contactsForm #phoneOld").val(data.phone);
			},
			error : function() {
				
			}
		});
	}
	$('#editContacts').modal('show');
	ie8StylePatch();
}
//弹出层（增加或修改）
outSideUser.showOutSideUserEditDiv = function(){
	
}
//注册input框事件
outSideUser.initInputEvent = function(itemId){
	$("#"+itemId).find("input").blur(function(){
		outSideUser.blurOutSideUserDiv(itemId)
	});
	//$("#"+itemId).find("input").keyup(outSideUser.keyUpOutSideUserDiv);
}

outSideUser.keyUpOutSideUserDiv = function(event){
	if($("#outSideUserDiv").find("input[type='text']").val() == ""){
		if(event.which == 8){
			if($("#outSideUserDiv").find("span").length >= 1){
				
				document.getElementById("outSideUserDiv").removeChild(document.getElementById("outSideUserDiv").childNodes[(document.getElementById("outSideUserDiv").childNodes.length - 2)])
				//$("#outSideUserDiv").find("span")[($("#outSideUserDiv").find("span").length-1)].remove();
			}
		}
	}
}

//构建当前用户外部联系人树
outSideUser.showTree = function(treeId,itemId){
			var	setting = {
				check : {
					enable : true
				},
				view : {
					selectedMulti : true
				},
				data : {
					simpleData : {
						enable : true
					}
				},
				callback : {
					onClick: function(event, treeId, node){
						
					}
				}
			};
			var url = getRootPath()+"/ic/contacts/getOutSideUserTree.action";
			$.getJSON(url, function(data) {
				if (data) {
					outSideUser.zNodes = [];
					$.each(data, function(i, o) {
						outSideUser.zNodes[i] = {
							id : o.id,
							pId : o.groupId,
							name : o.userName + "",
							mail : o.mail,
							phone : o.phone
						};
					});
					outSideUser.tree = $.fn.zTree.init($("#"+treeId), setting, outSideUser.zNodes);
					var nodes = outSideUser.tree.getNodes();
//					if(nodes != null){
//						outSideUser.tree.expandNode(nodes[0],true,false);
//					}
					
					selectEveryItemId = itemId;
					outSideUser.updateData();
					$('#outSideUserMainDiv').modal('show');
				}
			});
};
//移动到右侧
outSideUser.nameArray = new Array();
outSideUser.idArray = new Array();
outSideUser.phoneArray = new Array();
outSideUser.turnRight = function(){
	var treeObj= $.fn.zTree.getZTreeObj("outSideUserTree");
    nodes = treeObj.getCheckedNodes(true);
    for(var i=0;i<nodes.length;i++){
    	if( nodes[i].id > 0 ){
    		var flag = true;
    		for( var x = 0 ; x < outSideUser.idArray.length ; x++){
    			if(outSideUser.idArray[x] == nodes[i].id){
    				flag = false;
    			}
    		}
    		if(flag){
	    		outSideUser.idArray.push(nodes[i].id);
	    		outSideUser.nameArray.push(nodes[i].name);
	    		outSideUser.phoneArray.push(nodes[i].phone);
    		}
    	}
    }
    outSideUser.updateLeftAndRight();
}
//右侧移除
outSideUser.turnLeft = function(){
	$("#selectOutUserArea  option:selected").each(function(i){
		var index = "";
		var flag = false;
		for( var i = 0 ; i < outSideUser.idArray.length; i++){
			if(outSideUser.idArray[i] == this.value){
				index = i;
				flag = true;
			}
		}
		if(flag){
			outSideUser.idArray.splice(index,1);
			outSideUser.nameArray.splice(index,1);
			outSideUser.phoneArray.splice(index,1);
		}
	})
	outSideUser.updateLeftAndRight();
}

//刷新发短信界面token
outSideUser.closeAddView = function(){
	//getToken();
	outSideUser.checkValid();
};

outSideUser.updateLeftAndRight = function(){
	$("#selectOutUserArea").empty();
	for(var i = 0 ; i < outSideUser.idArray.length ; i++){
		var option = $("<option>").val(outSideUser.idArray[i]).text(outSideUser.nameArray[i]);
		$("#selectOutUserArea").append(option);
	}
	
	var treeObj= $.fn.zTree.getZTreeObj("outSideUserTree");
    nodes = treeObj.getCheckedNodes(true);
    for(var i=0;i<nodes.length;i++){
    	treeObj.checkNode(nodes[i],false,true);
    }
    
    for(var i = 0 ; i < outSideUser.idArray.length ; i++){
    	var node = treeObj.getNodeByParam("id", outSideUser.idArray[i]);
    	treeObj.checkNode(node,true,true);
    }
}
//根据不同按钮展示不同数据(点击按钮初始化)
outSideUser.updateData = function(){
	outSideUser.nameArray = new Array();
	outSideUser.idArray = new Array();
	outSideUser.phoneArray = new Array();
	$("#"+selectEveryItemId).find("span[id]").each(function(index,item){
		if(	$(item).attr("id") != ""){
			var o_id = $(item).attr("id");
			outSideUser.idArray.push(o_id)
			outSideUser.nameArray.push($(item).find("font").html());
			outSideUser.phoneArray.push($(item).find("input").val());
		}
	});
	outSideUser.updateLeftAndRight();
}
//点击确定事件
outSideUser.submitOutSideUser = function(){
	$("#"+selectEveryItemId).find("span").each(function(i){
		if(this.id != "" ){
			$(this).remove();
		}
	});
	
	for(var i = 0 ; i < outSideUser.phoneArray.length; i++){
		var appendHtml = "<li class='select2-search-field email-list-shou'><span name='ench' class='select2-search-choice' id='"+outSideUser.idArray[i]+"'><input type='hidden' id='outSide' name='outSide' value='"+outSideUser.phoneArray[i]+"'>"
		$("#"+selectEveryItemId+" #"+outSideUser.idArray[i]).remove();
		appendHtml += "<a style='color: #c30!important;font-family: verdana;' onclick=outSideUser.removeParent(this) >x&nbsp;</a><font>" + outSideUser.nameArray[i] + "</font>&nbsp;<img width='11' height='11' style='cursor:hand;vertical-align: middle;margin-top:-1px' src="+getRootPath()+"/img/edit.png onclick=outSideUser.showOutSideLayer(1,'"+outSideUser.idArray[i]+"') >&nbsp;";
		$("#"+selectEveryItemId).find("input[type='text']").remove();
		appendHtml += "</span><input class='email-temp-input' type='text' style='border: 0' /></li>";
		$("#"+selectEveryItemId).append(appendHtml);
		outSideUser.initInputEvent(selectEveryItemId);
	}
	$('#outSideUserMainDiv').modal('hide');
	if(outSideUser.phoneArray.length>0){
		outSideUser.checkValid();
	}
}
//初始化事件
jQuery(function($){
	var outSideUserLayerHtml = "<div class='modal fade panel' id='editContacts' aria-hidden='false'>" +
			"<div class='modal-dialog'>" +
			"<form class='cmxform' id='contactsForm'>" +
			"<div class='modal-content'>" +
			"<div class='modal-header'>" +
			"<button type='button' class='close' data-dismiss='modal' id='closeAddViewM'>×</button>" +
			"<h4 class='modal-title' id='mailContacts'>联系人</h4>" +
			"</div>" +
			"<div class='modal-body'>" +
			"<div class='form-table'>" +
			"<input type='hidden' id='groupRId' name='groupRId'>" +
			"<input type='hidden' id='id' name='id' value='0'>" +
			/*"<input type='hidden' id='token' name='token' value='0'>" +*/
			"<input type='hidden' id='modifyDate' name='modifyDate'>" +
			"<input type='hidden' id='namelayer' name='namelayer'>" +
			"<table class='table table-td-striped'>" +
			"<tbody>" +
			"<tr>" +
			"<td style='width:30%;'><span class='required'>*</span>姓名</td>" +
			"<td><input type='text' id='userName' name='userName'><input type='hidden' id='userNameOld' name='userNameOld'/></td>" +
			"</tr>" +
			"<tr>" +
			"<td>简称</td>" +
			"<td><input type='text' id='simpleName' name='simpleName'></td>" +
			"</tr>" +
			"<tr>" +
			"<td>性别</td>" +
			"<td><select name='sex' id='sex'  />" +
			"</td>" +
			"</tr>" +
			"<tr>" +
			"<td><span class='required'>*</span>邮箱地址</td>" +
			"<td><input type='text' id='mail' name='mail'><input type='hidden' id='mailOld' name='mailOld'/></td>" +
			"</tr>" +
			"<tr>" +
			"<td><span class='required'>*</span>移动电话</td>" +
			"<td><input type='text' id='phone' name='phone' class='phone'><input type='hidden' id='phoneOld' name='phoneOld' ></td>" +
			"</tr>" +
			"<tr>" +
			"<td>组别</td>" +
			"<td><div class='input-group'><select  id='groupId' name='groupId'></select></div></td>" +
			"</tr>" +
			"</tbody>" +
			"</table>" +
			"</div>" +
			"</div>" +
			"<div class='modal-footer form-btn'>" +
			"<button class='btn dark' type='button' id='savaAndClose'>保 存</button>" +
			"<button class='btn' type='button' data-dismiss='modal' id='closeAddView'>关 闭</button>" +
			"</div>" +
			"</div>" +
			"</form>" +
			"</div>" +
			"</div>";
	
	var outSideUserMainHtml = '<div class="modal fade" id="outSideUserMainDiv" aria-hidden="false">'+
		'<div class="modal-dialog modal-tree w531">'+
	    	'<div class="modal-content">'+
		        '<div class="modal-header clearfix">'+	//onclick="clear(\''+selectPersonId+'\',\''+selectDeptAndPersonId+'\');"
		            '<button type="button" class="close" data-dismiss="modal">×</button>'+
		            '<h4 class="modal-title fl">外部人员选择</h4>'+
		        '</div>'+
		        '<div class="modal-body clearfix">'+
	            	'<div class="w240 fl">'+
		                '<section class="tree-ul">'+
		                    '<div id="outSideUserTree" class="ztree"></div>'+ // 组织树
		                '</section>'+
		            '</div>'+
		            '<section class="m-l m-r fl tree-operate">'+
		            	'<a href="#" class="tree-move-down" id="turnRightDiv"><i class="fa fa-double-angle-right"></i></a> '+
		            	'<a href="#" class="tree-move-up" id="turnLeftDiv"><i class="fa fa-double-angle-left"></i></a>'+
		            '</section>'+
		            '<section class="fl pos-rlt">'+
		                '<select id="selectOutUserArea" multiple="true" class="w170 tree-scroll-right tree-s-r tree-select"></select>'+	// 组织与人员选择框
		                '<div class="tree-move"> '+
		                	
		                '</div>'+
		            '</section>'+
		        '</div>'+
		        '<div class="modal-footer form-btn">'+
		            '<button id="outSideAreaButton" class="btn dark" type="button">确 定</button>' +
		            '<button id="outSideCloseButton" class="btn" type="button">取 消</button>' +
		        '</div>'+
		    '</div>'+
	    '</div>'+
	'</div>';
	$("#outSideUserTreeDiv").before(outSideUserLayerHtml);
	$("#outSideUserTreeDiv").before(outSideUserMainHtml);
	dic.fillDics("sex", "sex",false,1);
	
	$("[outSideUser='true']").each(function(index,item){
		var itemId = $(item).attr("name");
		var outSideUserSelectHtml = "<div class='select2-wrap input-group  w-p100'>";
		outSideUserSelectHtml += "<div class='select2-container select2-container-multi w-p100'>";
		outSideUserSelectHtml += "<ul id='"+itemId+"' class='select2-choices'>";
		outSideUserSelectHtml += "<li class='select2-search-field email-list-shou'>";
		outSideUserSelectHtml += "<input type='text'  class='email-temp-input' />";
		outSideUserSelectHtml += "</li>";
		outSideUserSelectHtml += "</ul>";
		outSideUserSelectHtml += "</div>";
		outSideUserSelectHtml += getPhoneUserButtons(itemId);
		outSideUserSelectHtml += "</div>";
		$(item).append(outSideUserSelectHtml);
		$(item).click(function(){
			$("#"+itemId).find("input").focus();
		});
		outSideUser.initInputEvent(itemId);
		$("#"+itemId+"Div").click(function(){
			outSideUser.showOutSideMain(itemId);
		});
	});
	
	
	
	
	$("#outSideCloseButton").click(function(){
		$('#outSideUserMainDiv').modal('hide');
	})
	$("#savaAndClose").click(function(){outSideUser.savaOrModify(true);});
	$("#closeAddView").click(outSideUser.closeAddView);
	$("#closeAddViewM").click(outSideUser.closeAddView);
	$("#turnLeftDiv").click(outSideUser.turnLeft);
	$("#outSideAreaButton").click(outSideUser.submitOutSideUser);
	outSideUser.showGroupNameList();
	
	$("#turnRightDiv").click(outSideUser.turnRight);
});
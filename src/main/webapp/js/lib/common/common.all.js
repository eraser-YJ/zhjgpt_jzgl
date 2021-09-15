////////////////////////////////////////////////////////////////////原common.js内容
var tokenMessage = "请不要重复提交";
var concurrentMessage = "并发提交出错";

if (typeof(rootPath) == "undefined") {
	var rootPath = "";
}

function setRootPath(path) {
	rootPath = path;
}

function setManageHostPath(path) {
	manageHostPath = path;
}

function getRootPath() {
	return rootPath;
}

function getManageHostPath() {
	if (typeof(manageHostPath) == "undefined" || manageHostPath == '') {
		return getRootPath();
	}
	return manageHostPath;
}

if (typeof(sysTheme) == "undefined") {
	var sysTheme = "0";
}

function setTheme(theme) {
	sysTheme = theme;
}

function getTheme() {
	return sysTheme;
}
//流程打开表单
function openform(action, workid, dataid, entrance, type, beforeQuery) {
	if (beforeQuery != null && beforeQuery.length > 0) {
		if (!beforeQuery(dataid)) {
			return;
		}
	}
	if (type == null || type.length == 0) {
		type = "todo";
	}
	if (action == null) {
		action = "/workFlow/form/openformToAction.action"
	}
	if (action.indexOf("?") == -1) {
		action += "?entrance=" + entrance + "&scanType=" + type + "&workId=" + workid;
	} else {
		action += "&entrance=" + entrance + "&scanType=" + type + "&workId=" + workid;
	}

	if (workflowVar.dataTableId != null) {
		var dataTableObj = $("#" + workflowVar.dataTableId).dataTable();
		var pagingInfo = dataTableObj.fnPagingInfo();
		if (pagingInfo != null) {
			for (var item in pagingInfo) {
				if (item == "iPage") {
					action += "&" + item + "=" + pagingInfo[item];
				}
			}
		}

		var sortSetting = dataTableObj.fnSettings().aaSorting;
		var sortSettingStr = "";
		for (var i = 0; i < sortSetting.length; i++) {
			var sortItem = sortSetting[i];
			sortSettingStr += sortItem[0] + "_" + sortItem[1] + "_" + sortItem[2] + ";"
		}
		action += "&sortSetting=" + sortSettingStr;
	}

	if (workflowVar.queryFormId != null) {
		var queryData = JSON.stringify(serializeJson($("#" + workflowVar.queryFormId).serializeArray()));
		action += "&queryData=" + queryData;
	}
	if (workflowVar.otherWorkflowData != null) {
		action += "&otherWorkflowData=" + workflowVar.otherWorkflowData;
	}
	//loadrightmenu(action, "unknown");
	JCFF.loadPage({url:action});
}
//流程打开表单(不是相关人)
function openformNoPerson(workid, dataid, entrance, type) {
	if (type == null || type.length == 0) {
		type = "todo";
	}
	var action = "/workFlow/form/openFormWithNoPerson.action?entrance=" + entrance + "&scanType=" + type + "&workId=" + workid;
	if (workflowVar.dataTableId != null) {
		var dataTableObj = $("#" + workflowVar.dataTableId).dataTable();
		var pagingInfo = dataTableObj.fnPagingInfo();
		if (pagingInfo != null) {
			for (var item in pagingInfo) {
				if (item == "iPage") {
					action += "&" + item + "=" + pagingInfo[item];
				}
			}
		}

		var sortSetting = dataTableObj.fnSettings().aaSorting;
		var sortSettingStr = "";
		for (var i = 0; i < sortSetting.length; i++) {
			var sortItem = sortSetting[i];
			sortSettingStr += sortItem[0] + "_" + sortItem[1] + "_" + sortItem[2] + ";"
		}
		action += "&sortSetting=" + sortSettingStr;
	}

	if (workflowVar.queryFormId != null) {
		var queryData = JSON.stringify(serializeJson($("#" + workflowVar.queryFormId).serializeArray()));
		action += "&queryData=" + queryData;
	}
	if (workflowVar.otherWorkflowData != null) {
		action += "&otherWorkflowData=" + workflowVar.otherWorkflowData;
	}
	//loadrightmenu("/workFlow/form/openFormWithNoPerson.action?entrance=" + entrance + "&scanType=" + type + "&workId=" + workid, "unknown");
	JCFF.loadPage({url:"/workFlow/form/openFormWithNoPerson.action?entrance=" + entrance + "&scanType=" + type + "&workId=" + workid});
}
//流程初始化函数
//判断流程状态选择框
function initWorkFlowStatus(formId) {
	var entrance = $("#entrance").val();
	var tdEle = null;
	if (formId != null && formId.length > 0) {
		tdEle = $("#" + formId + " #processStatus");
	} else {
		tdEle = $("#processStatus");
	}

	if (entrance == "workflow" && tdEle.length > 0) {
		tdEle = tdEle.parent();
		var entranceType = $("#entranceType").val();
		if (entranceType == "todo") {
			var inputStr = "<input type='hidden' name='processStatus' id='processStatus' value='1'>";
			tdEle.html(inputStr);
			tdEle.append("待办");
		} else if (entranceType == "done") {
			var inputStr = "<input type='hidden' name='processStatus' id='processStatus' value='3'>";
			tdEle.html(inputStr);
			tdEle.append("已办");
		} else if (entranceType == "agent") {
			var inputStr = "<input type='hidden' name='processStatus' id='processStatus' value='99'>";
			tdEle.html(inputStr);
			tdEle.append("代办");
		}
	}
}
//获得流程列表相应的按钮打开表单
/**
 * opt:beforeQuery	//查看前判断
 * opt:deleteFun 	//删除业务对应数据
 */
var workflowVar = {};

function getWorkflowListButton(opt) {
	var buttonStr = "";
	var openFormBtnStr = "";
	var buttonLeablStr = "查看";
	if (opt.beforeQuery == null) {
		opt.beforeQuery = "";
	}
	if (opt.entranceType != "myBusiness") {
		workflowVar = opt;
		if (opt.entranceType == "todo" || opt.entranceType == "done") {
			if (opt.processStatus.indexOf("1") != -1) {
				buttonLeablStr = "办理";
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\"" + opt.action + "\",\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"todo\")' >" + buttonLeablStr + "</a>";
			} else {
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\"" + opt.action + "\",\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"done\",\"" + opt.beforeQuery + "\")' >" + buttonLeablStr + "</a>";
			}
		} else {
			if (opt.noPerson == true) {
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openformNoPerson(\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"view\",\"" + opt.beforeQuery + "\")' >" + buttonLeablStr + "</a>";
			} else {
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\"" + opt.action + "\",\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"view\",\"" + opt.beforeQuery + "\")' >" + buttonLeablStr + "</a>";
			}

		}
	} else {
		workflowVar = opt;
		if (opt.flowStatus == "0") {
			buttonLeablStr = "申请";
			openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\"" + opt.action + "\",\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"qicao\")' >" + buttonLeablStr + "</a>";
		} else {
			openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\"" + opt.action + "\",\"" + opt.workId + "\",\"" + opt.workflowId + "\",\"" + opt.entrance + "\",\"view\",\"" + opt.beforeQuery + "\")' >" + buttonLeablStr + "</a>";
		}
	}

	buttonStr = openFormBtnStr + "<a class='a-icon i-new view-history' href='javascript:void(0)' onclick='window.open(\"" + getRootPath() + "/workFlow/processHistory/getHistoryDetail.action?workId=" + opt.workId + "\")' >流程历史</a>";

	if (opt.showDeleteBtn == true && opt.flowStatus != null) {
		if (opt.flowStatus == "0") {
			buttonStr += "<a class='a-icon m-r-xs i-remove' href='javascript:void(0)' onclick=\"javascript:" + opt.deleteFun + "\"><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='删除'></i></a>";
		}
	}
	return buttonStr;
}
/**
 * 根据节点获取子节点信息
 * @param node
 * @returns
 */
function getChildNodesId(node, treeDivId) {
	var treeObj = $.fn.zTree.getZTreeObj(treeDivId);
	var childNodes = treeObj.transformToArray(node);
	var nodes = "";
	for (var i = 0; i < childNodes.length; i++) {
		if (childNodes[i].type == 0) {
			nodes += childNodes[i].id + ",";
		}
	}
	return nodes.length > 0 ? nodes.substring(0, nodes.length - 1) : null;
}

function treeHightReset(treeId) {
	if ($("#" + treeId)[0]) {
		var content = $("#content").height();
		var headerHeight_1 = $('#header_1').height() || 0;
		var headerHeight_2 = $("#header_2").height() || 0;
		$(".tree-right").css("padding-left", "215px");
		$("#LeftHeight").height(content - 80 - headerHeight_1 - headerHeight_2);
		var lh = $("#LeftHeight").height()
		if ($("#scrollable").scrollTop() >= 113) {
			$("#LeftHeight").addClass("fixedNav");
			$("#LeftHeight").height(lh + 113)
		} else {
			var a = $("#scrollable").scrollTop() >= 113 ? 113 : $("#scrollable").scrollTop()
			$("#LeftHeight").height(lh + a)
			$("#LeftHeight").removeClass("fixedNav");
		}
	}
}
//////////////////////////////////////////////////////////////////原msgBox.js内容
/**
 * 提示框相关js
 */
var msgBox = {};
/**
 * 提示框(长信息)
 * opt:content:警告内容
 * 	   type:类型success/fail
 */
msgBox.info = function(opt) {
	if (opt.type == null) {
		opt.type = "fail";
	}
	if (opt.callback != null) {
		jBox.setDefaults({
			defaults: {
				closed: opt.callback
			}
		});
	} else {
		jBox.setDefaults({
			defaults: {
				closed: function() {}
			}
		});
	}
	if (opt.type == "success") {
		jBox.info('', opt.content);
	} else {
		jBox.error('', opt.content, 'S');
	}
	ie8InRounded();
}
/**
 * 提示框(短信息)
 * opt:type:类型success/fail
 * 	   content:内容
 */
msgBox.tip = function(opt) {
	if (opt.type == null) {
		opt.type = "fail";
	}
	if (opt.callback != null) {
		jBox.setDefaults({
			defaults: {
				closed: opt.callback
			}
		});
	} else {
		jBox.setDefaults({
			defaults: {
				closed: function() {}
			}
		});
	}
	if (opt.type == "success") {
		jBox.tip(opt.content, '', {
			closed: function() {
				if (opt.callback != null) {
					opt.callback();
				}
			}
		});
	} else {
		jBox.tip(opt.content, 'error', {
			closed: function() {
				if (opt.callback != null) {
					opt.callback();
				}
			}
		});
	}
	ie8InRounded();
}
/**
 * 确认对话框
 * opt:content:提示内容
 * 	   success:确认时回调函数
 * 	   noback :点击离开时回调函数
 * 	   cancel:取消时回调函数
 * 	   fontSize:B/S
 * 	   buttons:  按钮true/false 样例：{"是":"yes","否":"no","取消":"cancel"};
 * 
 *     按钮默认不添加只显示-> 是 否 添加参数后会覆盖，
 *     如需要3个按钮添加值为cancel的按钮名称和noback回调函数 
 */
msgBox.confirm = function(opt) {
	//opt.buttons = {"保存并且继续":"yes", "取消":"no"}
	if (opt.buttons == null || opt.buttons == undefined) {
		opt.buttons = {
			"是": "yes",
			"否": "no"
		};
	}
	jBox.setDefaults({
		defaults: {
			closed: function() {},
			buttons: opt.buttons
		}
	});
	var submit = function(v, h, f) {
		switch (v) {
			case 'yes':
				if (opt.success != null) {
					opt.success();
				}
				break;
			case 'no':
				if (opt.cancel != null) {
					opt.cancel();
				}
				break;
			case 'cancel':
				if (opt.noback != null) {
					opt.noback();
				}
				break;
		}

	};
	var fontSize = "";
	if (opt.fontSize == "S" || opt.fontSize == "s") {
		fontSize = "S";
	}
	jBox.warning("", opt.content, submit, fontSize);
	ie8InRounded();
};

function alertx(mess) {
	msgBox.tip({
		content: mess
	});
}
/**
 * 字典相关js
 */
var dic = {};

/**
 * @description 获得启动的字典，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param typeCode 字典类型code
 * @param async为true时为同步请求
 * @param defaultType 1:请选择  2：全部 3：无
 */
dic.fillDics = function(elementId, typeCode, parentCode, async, defaultType) {

	if (typeCode == null || typeCode.length == 0) {
		//alert("字典类型为空");
		return;
	}
	var selectObj = $('#' + elementId);
	if (selectObj == null || selectObj.length == 0) {
		//alert("没有对应的的组件,id为"+elementId);
		return;
	}
	var ajaxData = {
		typeCode: typeCode,
		parentCode: parentCode,
		time: new Date()
	};
	//改为同步
	//	$.ajaxSetup({ 
	//	    async : false 
	//	});

	$.ajax({
		url: getManageHostPath() + "/dic/getDics.action",
		dataType: "jsonp",
		type: "GET",
		jsonpCallback: "fillDicsJsonpCallback",
		async: false,
		success: function(data) {
			dic.fillElement(selectObj, data, defaultType)
		},
		data: ajaxData
	});
};

/**
 * @description 获得所有字典(包含未启动的)，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param typeCode 字典类型code
 * @param defaultType 1:请选择  2：全部 3：无
 */
dic.fillAllDics = function(elementId, typeCode, parentCode, defaultType) {
	if (typeCode == null || typeCode.length == 0) {
		//alert("字典类型为空");
		return;
	}
	var selectObj = $('#' + elementId);
	if (selectObj == null || selectObj.length == 0) {
		//alert("没有对应的的组件,id为"+elementId);
		return;
	}
	var ajaxData = {
		typeCode: typeCode,
		parentCode: parentCode,
		time: new Date()
	};

	$.ajax({
		url: getManageHostPath() + "/dic/getDicsAll.action",
		dataType: "jsonp",
		type: "GET",
		jsonpCallback: "fillAllDicsJsonpCallback",
		async: false,
		success: function(data) {
			dic.fillElement(selectObj, data, defaultType)
		},
		data: ajaxData
	});
};

/**
 * @description 获得启动的字典，并放到固定的select中
 * @param elementId 字典对应select组件的id
 * @param data 字典项数据
 */
dic.fillElement = function(selectObj, data, defaultType) {
	selectObj.html("");
	if (defaultType == null) {
		defaultType == "1";
	}
	if (defaultType == "1" || defaultType == 1) {
		defaultType = "1";
		selectObj.html("<option value=''>-请选择-</option>");
	} else if (defaultType == "2" || defaultType == 2) {
		defaultType = "2";
		selectObj.html("<option value=''>-全部-</option>");
	} else if (defaultType == "3" || defaultType == 3) {
		defaultType = "3";
		selectObj.html("");
	}

	var selectValue = selectObj.attr("code");
	for (var i = 0; i < data.length; i++) {
		var selectStr = "";
		if (selectValue == null || selectValue.length == 0) {
			if (defaultType != "2" && data[i].defaultValue == 1) {
				selectStr = "selected";
			}
		}
		if (selectValue != null && selectValue.length > 0) {
			selectStr = "selected";
		}

		var optionStr = "<option value='" + data[i].code + "' " + selectStr + ">" + data[i].value + "</option>";
		selectObj.append(optionStr);
	}
};

/**
 * @description 获得启动的字典，并返回toString
 * @param typeCode 字典类型code
 */
dic.toString = function(typeCode) {
	if (typeCode) {
		var ajaxData = {
			typeCode: typeCode,
			time: new Date()
		};
		var html = "<option value=''>请选择</option>";
		$.ajax({
			type: "GET",
			url: getRootPath() + "/dic/getDics.action",
			async: false,
			data: ajaxData,
			dataType: "json",
			success: function(data) {
				if (data) {
					var selectStr = "";
					for (var i = 0; i < data.length; i++) {
						if (data[i].defaultValue == 1) {
							selectStr = "selected";
						}
						var optionStr = "<option value='" + data[i].code + "' " + selectStr + ">" + data[i].value + "</option>";
						html = html.concat(optionStr);
					}
				}
			}
		});
		return html;
	}
};

/**
 * 多级联动下拉菜单方法
 * @param[*]代表必填项
 * @param[*] typeCode 字典码
 * @param[*] selectId 被替换select组件的id值
 * @param[*] multistId 被替换的下拉菜单区域id值
 * @param selectValue 被选中的下拉值
 */
dic.multistepDropDownCache = function(typeCode, selectId) {
	$("#" + selectId).empty();
	$("#" + selectId).html($("#" + typeCode).html());
	$("#" + selectId).change();
};

/**
 * 多级联动下拉菜单方法
 * @param[*]代表必填项
 * @param[*] typeCode 字典码
 * @param[*] selectId 被替换select组件的id值
 * @param selectValue 被选中的下拉值
 */
dic.multistepDropDown = function(typeCode, selectId, selectValue) {
	$("#" + selectId).empty();
	$.ajax({
		type: 'post',
		async: false,
		dataType: 'json',
		url: getRootPath() + "/dic/getDicJSONInfo.action?typeCode=" + typeCode +"&time=" + (+new Date()),
		success: function(json) {
			$(json).each(function() {
				var opt = $("<option/>").text(this.value).attr("value", this.code);
				if (this.code == selectValue)
					opt = $("<option/>").text(this.value).attr("value", this.code).attr('selected', true);
				$("#" + selectId).append(opt);
			});
		}
	});
};


/**
 * 多级联动下拉菜单方法
 * @param[*]代表必填项
 * @param[*] typeCode 字典码
 * @param[*] selectId 被替换select组件的id值
 * @param selectValue 被选中的下拉值
 */
dic.multistepDropDownForP = function(typeCode, selectId, selectP, selectValue) {
	var parentCode = $("#"+selectP).attr("dictName");
	$("#" + selectId).empty();
	$.ajax({
		type: 'post',
		async: false,
		dataType: 'json',
		url: getRootPath() + "/dic/getDicJSONInfo.action?typeCode=" + typeCode + "&parentCode=" + parentCode +"&time=" + (+new Date()),
		success: function(json) {
			$("#" + selectId).attr("dictName",typeCode);
			$("#" + selectId).attr("parentCode",parentCode);
			$(json).each(function() {
				var opt = $("<option/>").text(this.value).attr("value", this.code);
				if (this.code == selectValue)
					opt = $("<option/>").text(this.value).attr("value", this.code).attr('selected', true);
				$("#" + selectId).append(opt);
			});
			$("#" + selectId).change()
		}
	});
};

//////////////////////////////////////////////////////////////////原workFlowUtils.js内容

var workFlow = {};
//打开流程模板图
workFlow.openProcessDefinitionMap = function(flowId) {
	window.open(getRootPath() + "/horizon/workflow/HorizonSummaryInstance.jsp?only=true&flowid=" + flowId + "&identifier=system");
};
/**
 * 返回方法
 */
goBack = function() {
	//获取存储的所有已跳转的历史记录(从点击左菜单开始)
	var histObjs = JCFF.getTop().JCF.histObj;
	histObjs.records.pop();
	if(histObjs.records && histObjs.records.length){
		var menu = histObjs.getLastData();
		//获取之前的分页信息
		var iPage = $("#iPage").val();
		var sortSetting = $("#sortSetting").val();
		var queryData = $("#queryData").val();

		if (menu.url.indexOf("?") != -1) {
			menu.url + "&iPage=" + iPage + "&sortSetting=" + sortSetting + "&queryData=" + queryData;
		} else {
			menu.url + "?iPage=" + iPage + "&sortSetting=" + sortSetting + "&queryData=" + queryData;
		}
		JCFF.loadPage(menu);
	}else{
		JCFF.loadPage({
			url: "/sys/portal/manageView.action?portalId=8&portalType=ptype_org"
		});
	}
	//var historyObj = null;
	//if (parent.historyUrl != null) {
	//	parent.historyUrl.pop();
	//	if (parent.historyUrl.length == 0) {
	//		historyObj = {
	//
	//		};
	//		loadrightmenuForback(historyObj.url, historyObj.newcrumbs, historyObj.loadmenubarurl);
	//		parent.loadleftMenu1('1');
	//	} else {
	//		historyObj = parent.historyUrl[parent.historyUrl.length - 1];
    //
	//		var iPage = $("#iPage").val();
	//		var sortSetting = $("#sortSetting").val();
	//		var queryData = $("#queryData").val();
	//		if (historyObj.url.indexOf("?") != -1) {
	//			var url = historyObj.url + "&iPage=" + iPage + "&sortSetting=" + sortSetting + "&queryData=" + queryData;
	//		} else {
	//			var url = historyObj.url + "?iPage=" + iPage + "&sortSetting=" + sortSetting + "&queryData=" + queryData;
	//		}
	//		loadrightmenuForback(url, historyObj.newcrumbs, historyObj.loadmenubarurl);
	//	}
	//}
};

/*
 * data:参数
 */
setUrlParameter = function(data) {
	if (typeof(window.parent.urlParameter) != 'undefined')
		window.parent.urlParameter.value = data;
};

getUrlParameter = function() {
	if (typeof(window.parent.urlParameter) != 'undefined' && window.parent.urlParameter.value != null)
		return window.parent.urlParameter.value;
	else
		return "";
};

//判断当前用户是否已分配指定菜单并导向指定菜单-----start-----
//参数menuid 为菜单id
loadSpecifyMenu = function(menuid) {
	var returnvalue;
	jQuery.ajax({
		url: getRootPath() + "/sys/menu/valUserMenu.action?id=" + menuid,
		type: 'POST',
		async: false,
		success: function(data) {
			if (data.success == "false") {
				msgBox.info({
					content: "您未被分配此菜单，请<br/>联系管理员",
					type: 'fail'
				});
				returnvalue = false;
			} else {
				//	loadrightmenu(data.menuVo.actionName);
				//	navigationbar(menuid);
				returnvalue = true;
			}
		},
		error: function() {
			$("#dataLoad").fadeOut(); //页面加载完毕后即将DIV隐藏
			returnvalue = false;
		}
	});
	return returnvalue;
};
//判断当前用户是否已分配指定菜单并导向指定菜单-----end-----

//首页链接导入右侧界面---start----
homeloadmenu = function() {
	window.parent.location.reload();
};
//首页链接导入右侧界面---end----

//用于JSON.stringify的第二个参数，避免WinXP+IE8环境下转换JsonString时出现“null"的数据
replaceJsonNull = function(key, value) {
	var agent = navigator.userAgent.toLowerCase();
	if (agent.indexOf("msie 8.0") > 0) {
		if (Object.prototype.toString.apply(value) === '[object Array]') {
			return value;
		} else if (value === "" || value == null || value === "null") {
			return "";
		} else
			return value;
	} else
		return value;
};

//处理session超时
$.ajaxSetup({
	cache : false,		//清除请求缓存,避免新建数据后,刷新列表不出现
	contentType: "application/x-www-form-urlencoded;charset=utf-8",
	complete: function(XMLHttpRequest, textStatus) {
		if (typeof(XMLHttpRequest.getResponseHeader) == "function") {
			var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
			if (sessionstatus == "kickout") {
				window.location = getRootPath() + "/login?kickout=true";
			} else if (sessionstatus == "timeout") {
				window.location = getRootPath() + "/login?timeout=true";
			}
		}
	}
});
/**
 *处理分页返回数据
 */
function pagingDataForGoBack() {
	var result = {};
	var curPage = 0;
	var sortSetting = new Array();
	if ($("#iPage").val() != null && $("#iPage").val().length > 0) {
		curPage = $("#iPage").val();
	}
	result.curPage = curPage;

	if ($("#sortSetting").val() != null && $("#sortSetting").val().length > 0) {
		var sortSettingStr = $("#sortSetting").val();
		var sortSettingArray = sortSettingStr.split(";");
		for (var i = 0; i < sortSettingArray.length; i++) {
			if (sortSettingArray[i].length > 0) {
				sortSetting.push([sortSettingArray[i].split("_")[0], sortSettingArray[i].split("_")[1]]);
			}
		}
		$("#sortSetting").val("");
	}

	var queryData = $("#queryData").val();
	result.sortSetting = sortSetting;
	result.queryData = {};
	if (queryData != null && queryData.length > 0) {
		var queryDataObj = eval("(" + queryData + ")");
		result.queryData = queryDataObj;
	}
	return result;
}

/**处理分页后批量删除所有数据，返回上一页的方法
 * dataTableName 为列表table的ID
 * idsArr 为被删除数据的id数组**/
function pagingDataDeleteAllForGoBack(dataTableName,idsArr){
	if(idsArr==null || idsArr==''){
		return;
	}
	//获得datatable分页当页第一条记录是中条数的第多少条
	var start = $("#"+dataTableName).dataTable().fnSettings()._iDisplayStart;
	//获得datatable一共多少条
	var total = $("#"+dataTableName).dataTable().fnSettings().fnRecordsDisplay();
	//获得datatable每页多少条
	var len   = $("#"+dataTableName).dataTable().fnSettings()._iDisplayLength;
	//如果是最后一页，将当前页记录全部删除跳转至前一页
	if((total-start)<=len&&((total-start)-idsArr.length)==0){
		$("#"+dataTableName).dataTable().fnPageChange( 'previous', true );
	}
}

/**
 * 返回选中的值
 * @param inputId	文本控件ID
 * @returns	单选返回格式[id:name]、多选返回格式[id:name,id:name]
 */
function returnValue(inputId){
	var iId = $("#"+inputId).select2("data"),
			v   = "";
	if(iId == null || iId.length == 0 ){ return null; }
	if(iId.length > 0){
		$.each(iId, function(j, d){
			v += d.id+":"+(d.text==undefined?d.innerText:d.text)+",";
		});
	}else{
		v += iId.id+":"+(iId.text==undefined?iId.innerText:iId.text)+",";
	}
	return v.substring(0, v.length-1);
}

$(document).ready(function() {
	$("#dataLoad").fadeOut(); //页面加载完毕后即将DIV隐藏
	ie8InRounded();
});
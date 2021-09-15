var tokenMessage = "请不要重复提交";
var concurrentMessage = "并发提交出错";

if(typeof(rootPath) == "undefined"){
	var rootPath = "";
}

function setRootPath(path){
	rootPath = path;
}

function getRootPath() {
    return rootPath;
}

/**
 * ie6-8在数组上增加indexOf方法
 */
if (!$.support.leadingWhitespace) {
	if (!Array.prototype.indexOf)
	{
	  Array.prototype.indexOf = function(elt /*, from*/)
	  {
	    var len = this.length >>> 0;
	
	    var from = Number(arguments[1]) || 0;
	    from = (from < 0)
	         ? Math.ceil(from)
	         : Math.floor(from);
	    if (from < 0)
	      from += len;
	
	    for (; from < len; from++)
	    {
	      if (from in this &&
	          this[from] === elt)
	        return from;
	    }
	    return -1;
	  };
	}
}

//根据name获取json数组对应的value
Array.prototype.getValue = function (name){
	for (var i=0; i < this.length; i++) {
        if (this[i].name == name) {
            return this[i].value;
        }
    }
};

//将表单序列化数组转成json格式
function serializeJson(array){
    var serializeObj={};
    $(array).each(function(){
        if(serializeObj[this.name]){
            if($.isArray(serializeObj[this.name])){
                serializeObj[this.name].push(this.value);
            }else{
                serializeObj[this.name]=[serializeObj[this.name],this.value];
            }
        }else{
            serializeObj[this.name]=this.value;
        }
    });
    return serializeObj;
};

//获取token
function getToken(formId) {
	$.ajax({
		type : "GET",
		url : getRootPath()+"/system/token.action",
		data : null,
		cache : false,
		dataType : "json",
		success : function(data) {
			if (data) {
				if(formId){
					$("#"+formId+" #token").val(data.token);
				} else {
					$("#token").val(data.token);
				}
			}
		}
	});
};

//显示表单错误信息
function showErrors(formId, errorMessage){
	$.each(errorMessage, function(i, o) {
		var parent_ = $("#"+formId+" [id='"+o.code+"']").parent();
		var label_ = parent_.children("label");
		if(label_.length == 0){
			$("<label>")
			.attr("for", o.code)
			.addClass("error")
			.html(o.message || "")
			.insertAfter($("#"+formId+" [id='"+o.code+"']"));
		} else {
			label_.html(o.message);
			label_.css('display','block');
		}
	});
}

//清除验证信息
function hideErrorMessage(){
	$("label.error").remove();
	$(".error").removeClass("error");
}

function alertx(mess){
	msgBox.tip({content:mess});
}

//确认对话框
function confirmx(mess,fnCall){
	var opt = {
		content:mess,
		success:function(){
			fnCall();
		}
	}
	msgBox.confirm(opt);
	return false;
}

//流程打开表单
function openform(action,workid,dataid,entrance,type,beforeQuery){
	if(beforeQuery!=null&&beforeQuery.length>0){
		if(!beforeQuery(dataid)){
			return;
		}
	}
	if(type==null||type.length==0){
		type = "todo";
	}
	
	if(action.indexOf("?")==-1){
		action+="?entrance="+entrance+"&scanType="+type+"&workId="+workid;
	}else{
		action+="&entrance="+entrance+"&scanType="+type+"&workId="+workid;
	}
	loadrightmenu(action,"unknown");
}

//流程打开表单(不是相关人)
function openformNoPerson(workid,dataid,entrance,type){
	if(type==null||type.length==0){
		type = "todo";
	}
	loadrightmenu("/workFlow/form/openFormWithNoPerson.action?entrance="+entrance+"&scanType="+type+"&workId="+workid,"unknown");
}

//流程初始化函数
//判断流程状态选择框
function initWorkFlowStatus(formId){
	var entrance = $("#entrance").val();
	var tdEle = null;
	if(formId!=null&&formId.length>0){
		tdEle = $("#"+formId+" #processStatus");
	}else{
		tdEle = $("#processStatus");
	}
	
	if(entrance=="workflow"&&tdEle.length>0){
		tdEle = tdEle.parent();
		var entranceType = $("#entranceType").val();
		if(entranceType=="todo"){
			var inputStr = "<input type='hidden' name='processStatus' id='processStatus' value='1'>";
			tdEle.html(inputStr);
			tdEle.append("待办");
		}else if(entranceType=="done"){
			var inputStr = "<input type='hidden' name='processStatus' id='processStatus' value='3'>";
			tdEle.html(inputStr);
			tdEle.append("已办");
		}else if(entranceType=="agent"){
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
function getWorkflowListButton(opt){
	var buttonStr = "";
	var openFormBtnStr = "";
	var buttonLeablStr = "查看";
	if(opt.beforeQuery==null){
		opt.beforeQuery ="";
	}
	if(opt.entranceType != "myBusiness"){
		if(opt.entranceType =="todo"||opt.entranceType =="done"){
			if(opt.processStatus.indexOf("1")!=-1){
				buttonLeablStr = "办理";
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"todo\")' >"+buttonLeablStr+"</a>";
			}else{
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"done\",\""+opt.beforeQuery+"\")' >"+buttonLeablStr+"</a>";
			}
		}else{
			if(opt.noPerson==true){
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openformNoPerson(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"view\",\""+opt.beforeQuery+"\")' >"+buttonLeablStr+"</a>";
			}else{
				openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"view\",\""+opt.beforeQuery+"\")' >"+buttonLeablStr+"</a>";
			}
			
		}
	}else{
		if(opt.flowStatus == "0"){
			buttonLeablStr = "申请";
			openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"qicao\")' >"+buttonLeablStr+"</a>";
		}else{
			openFormBtnStr = "<a class='a-icon i-new' href='javascript:void(0)' onclick='openform(\""+opt.action+"\",\""+opt.workId+"\",\""+opt.workflowId+"\",\""+opt.entrance+"\",\"view\",\""+opt.beforeQuery+"\")' >"+buttonLeablStr+"</a>";
		}
	}
	 
	buttonStr = openFormBtnStr + "<a class='a-icon i-new view-history' href='javascript:void(0)' onclick='window.open(\""+getRootPath()+"/workFlow/processHistory/getHistoryDetail.action?workId="+opt.workId+"\")' >流程历史</a>";
	
	if(opt.showDeleteBtn == true&&opt.flowStatus!=null){
		if(opt.flowStatus=="0"){
			buttonStr += "<a class='a-icon m-r-xs i-remove' href='javascript:void(0)' onclick=\"javascript:"+opt.deleteFun+"\"><i class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='删除'></i></a>";
		}
	}
	return buttonStr;
}

function isIE(){
    return navigator.appName.indexOf("Microsoft Internet Explorer")!=-1 && document.all;
}

function isIE6() {
    return navigator.userAgent.toLowerCase().indexOf("msie 6.0")=="-1"?false:true;
}

function isIE7(){
    return navigator.userAgent.toLowerCase().indexOf("msie 7.0")=="-1"?false:true;
}

function isIE8(){
    return navigator.userAgent.toLowerCase().indexOf("msie 8.0")=="-1"?false:true;
}

function isIE11(){
    return navigator.appName.indexOf("Netscape")!=-1
}

function isNN(){
   return navigator.userAgent.indexOf("Netscape")!=-1;
}

function isOpera(){
    return navigator.appName.indexOf("Opera")!=-1;
}

function isFF(){
    return navigator.userAgent.indexOf("Firefox")!=-1;
}

function isChrome(){
    return navigator.userAgent.indexOf("Chrome") > -1;
}

/**
 * 比较两个id（逗号分隔）字串差异，返回新增部分
 * @param a
 * @param b
 */
function betw(a,b)
{
var as = a.split(",");
var bs = b.split(",");

var r = new Array();
var rc =0;
for(var i=0;i<bs.length;i++)
{
	var c =0;
	for(var j=0;j<as.length;j++)
	{
		if(bs[i] == as[j])
		{
			c++;
		}
	}
	if(c==0)
	{
		r[rc++] = bs[i];	
	}	
}
return r;
}

/**
 * 格式化人员字符串，返回人员格式数组
 */
function formatUser(userStr){
	var result = new Array();
	var users = userStr.split(",");
	for(var i=0;i<users.length;i++){
		var userItem = {};
		userItem.id = users[i].split(":")[0];
		userItem.name = users[i].split(":")[1];
		result.push(userItem);
	}
	return result;
}

/**
 * IE8下菜单提供跳转不起作用,可以暂时调用这个方法替代
 * @param url 跳转的URL
 */
function jump(url){
	$.ajax({
		url: url,
		type: "post",
		success : function(data){
			$("#scrollable").html(data);
		}
	});
}

/**
 * 将textarea里的内容转换为hmtl
 */
function formatToHtml(str){
	var string=str.replace(/\r\n/g,"<br>")  
    string=string.replace(/\n/g,"<br>"); 
	return string;
}

/**
 * 将html里的内容转换为textarea的内容
 */
function formatToValue(str){
	var string=str.replace(/<br>/g,"\r\n");
	string=string.replace(/<BR>/g,"\r\n")
	return string;
}

/**
 * 根据节点获取子节点信息
 * @param node
 * @returns
 */
function getChildNodesId(node, treeDivId){
	var treeObj = $.fn.zTree.getZTreeObj(treeDivId);
	var childNodes = treeObj.transformToArray(node);
    var nodes = "";
    for(var i = 0; i < childNodes.length; i++) {
    	if(childNodes[i].type == 0){
    		nodes += childNodes[i].id + ",";
    	}
    }
    return nodes.length>0?nodes.substring(0, nodes.length-1):null;
}

function treeHightReset(treeId){
	if($("#"+treeId)[0]){
    	var content = $("#content").height();
        var headerHeight_1 = $('#header_1').height() || 0;
        var headerHeight_2 = $("#header_2").height() || 0;
        $(".tree-right").css("padding-left","215px");
		$("#LeftHeight").height(content-80-headerHeight_1-headerHeight_2);
        var lh = $("#LeftHeight").height()  
        if($("#scrollable").scrollTop()>=113){
            $("#LeftHeight").addClass("fixedNav");
            $("#LeftHeight").height(lh + 113)
        }else{
            var a = $("#scrollable").scrollTop()>=113?113:$("#scrollable").scrollTop()
            $("#LeftHeight").height(lh + a)
            $("#LeftHeight").removeClass("fixedNav");
        }
    }
}
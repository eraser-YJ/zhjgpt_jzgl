function rate(up,down){
	if(up&&up>0&&down&&down>0){
		var rate = parseFloat(up)/parseFloat(down);
		rate = rate *100;
		return rate.toFixed(2)+"%"; 
	}
	return "0.00%"
}


//移除错误信息
function removeErrMsg(key){
	$("#"+key).siblings("label").each(function(){
		$(this).remove()
	});
}

function isNumber(inputData) {
	var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    //var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
	//  if(regPos.test(val) || regNeg.test(val)){
    if(regPos.test(inputData)){
        return true;
    }else{
        return false;
    }        
}

//判断是否为空
function isNull(value){
	if(value){
		if(value == null || value == '' || typeof(value) == 'undefined' || value == 'undefined'){
			return true;
		}
	} else {
		return true;
	}
	return false;
}
//空显示
function nvl(value,defaultValue){
	//为空是的默认值
	var dValue = isNull(defaultValue)?"":defaultValue;
	if(isNull(value) || value == "null"){
		return dValue;
	}
	return value;
}

function trimChar(value){
	if(isNull(value)){
		return "";
	}
	
	while(value.indexOf(",,") >= 0){
		value = value.replace(/,,/i, ","); 
	}
	
	while(value.charAt(0) == ','){
		value = value.substr(1);
	}
	while(value.charAt(value.length - 1) == ','){
		value = value.substr(0,value.length - 1);
	}
	return value;
}

//生成固定长度
function trimDis(value,max){
	if(value.length > max){
		return value.substring(0,max)+"..."
	}
	return ifNull(value);
}


//判断长度
function getByteLen(val){
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        if (val[i].match(/[^\x00-\xff]/ig) != null) //全角
            len += 2;
        else
            len += 1;
    };
    return len;
}

//请求数据
var ReqHelper = {};
//返回的数据
ReqHelper.returnObj = null;
//状态
ReqHelper.runState = false;

//同步post请求
ReqHelper.post = function(url,param){
	if(ReqHelper.runState){
		return null;
	}
	ReqHelper.runState = true;
	ReqHelper.returnObj = null;
	var bTime = new Date().getTime();
	$.ajax({
		type : 'POST',
		cache : false, 
	    async : false,
		url : url,
		data : param,
		dataType : "json",
		success : function(data) {
			ReqHelper.runState = false;
			ReqHelper.returnObj = data;
		},
		error:function(){
			ReqHelper.runState = false;
			ReqHelper.returnObj = null;
		}
	});
	var eTime = new Date().getTime();
	//console.log("http post ("+url+") :耗时 = "+(eTime - bTime))
	return ReqHelper.returnObj;
};

//同步get请求
ReqHelper.get = function(url){
	if(ReqHelper.runState){
		return null;
	}
	ReqHelper.runState = true;
	ReqHelper.returnObj = null;
	var bTime = new Date().getTime();
	$.ajax({
		type : 'GET',
		cache : false, 
	    async : false,
		url : url,
		dataType : "json",
		success : function(data) {
			ReqHelper.runState = false;
			ReqHelper.returnObj = data;
		},
		error:function(){
			ReqHelper.runState = false;
			ReqHelper.returnObj = null;
		}
	});
	var eTime = new Date().getTime();
	//console.log("http get ("+url+") :耗时 = "+(eTime - bTime))
	return ReqHelper.returnObj;
};
//取得所有查询参数
function dataTableGetAllParam(dataTableUserObj){
	var aoData = [];
	dataTableUserObj.oTableFnServerParams(aoData);
	aoData.push({ 'name': 'orderBy', 'value': dataTableGetOrderByStr(dataTableUserObj.oTable)});
	return aoData;
}
//取得排序字段
function dataTableGetOrderByStr(oTable){
	//取得当前表格
  //var oTable = $('#equipmentRepairTable').dataTable();
  //取得属性集合
  var settings = oTable.fnSettings();
  if(settings.aaSorting.length == 0){
  	return null;
  }
  //目前这个方法只是支持单个排序，修个成多个也很简单
  var index = settings.aaSorting[0][0];
  if(index == null){
  	return "";
  }
  var arrow = settings.aaSorting[0][1];
  if(arrow == null || arrow == ''){
  	arrow = "asc";
  }
  var columns = settings.aoColumns;
  var sortField = null;
  if(columns){
  	sortField = columns[index].mData;
  }
  if(sortField == null){
  	return "";
  }
  //构造记过
  var resStr =  sortField+" "+arrow;
  return resStr;
}

//上传Excel文件到后台
importExcelToService = function (inURL,fileId,paramData,callbackFun){
	//取得文件对象
	var excel = document.getElementById(fileId);
	if(!excel){
		msgBox.info({
			content : "请选择导入文件"
		}); 
		return ;
	}
	//判断是否为空，为空返回
	if(excel.value==""){
		msgBox.info({
			content : "请选择导入文件"
		}); 
		return ;
	}
	//获取文件名
	var fileName = new String(excel.value);
	//获取文件类型
	var fileExt = new String(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length));

	//判断文件类型是否为xls、xlsx，否则返回
	if(fileExt!="xls"&&fileExt!="xlsx"){
		msgBox.info({
			content : "导入文件只支持xlsx和xls类型的文件"
		});
		return ;
	}
	if(paramData == null){
		paramData = {};
	}
    //ajax提交
    $.ajaxFileUpload({
        url:inURL,
        secureuri:false,
		data:paramData,
        fileElementId:fileId,
        contentType : 'multipart/form-data;charset=UTF-8',
		dataType : 'json', // 返回值类型 一般设置为json
        success:function(data, status){
			if(callbackFun){
				callbackFun(data);
			}
        }
    });
	
};

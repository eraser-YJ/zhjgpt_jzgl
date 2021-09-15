/***
 *   common.js 存放一些全局的方法。
 */

/**
 *  页面加载后执行 
 */
$(document).ready(function(){
    $("#input-box").on("click",".inputSearch",function(){
    	if(window.mapPage){
    		if(window.uisearch){
    			var rs = uisearch.setObj();
        		if(rs){
        			//审批接入查询
        			if(SearchTools.getInstance().getSecClass()){
        				var resType=SearchTools.getInstance().getResType();
        				location.href = "./mapDataAction.do?method=forwSecretSearch&keysearch=indexSearch&resType="+resType;
        			}else{
        				location.href = "./mapDataAction.do?method=forw&keysearch=indexSearch";
        			}
            	}else{
            		mapPage.search();
            	}
    		}else{
    			mapPage.search();
    		}
    	}else if(window.uisearch){
    		var rs = uisearch.setObj();
        	if(rs){
        		location.href = "./mapDataAction.do?method=forw&keysearch=indexSearch";
        	}
    	}else{
    		var s = $(this).parent().find("input").val();
    		if(s){
    			location.href = "./mapDataAction.do?method=forw&key="+encodeURI(s);
    		}
    	}
    });

    $.post("./user.do?method=checkIsLogin",{},_checkUserLogin,"text");
    var url = window.location.href;
    url = encodeURIComponent(url);
    document.cookie = "sourceUrl=" +url;
    setMenu();

    try{
    	var tmp = {
    			$allBox:$("#input-box"),$input:$("#searchInput")
    			,$showBox:$("#show-default").find(".show-box"),
    			$default:$("#input-default"),$search:$("#input-search")
    		};
    	uisearch = new UISearch(tmp);
    	uisearch.start();
    }catch(e){
    	
    }
}); 

/**
 * 设置菜单
 */
var setMenu = function(){
	var menuFuc = function(rs){
		var list = typeof(rs) === "string" ? eval('('+rs+')') : rs;
		window.typeList = [];
		var $tmpBox = $(".section-nav1 ul").empty();
		for(var i in list){
			if(list[i].typeOwner === 0){
				window.typeList.push(list[i]);
				$tmpBox.append('<li><a class="item" href="./mapDataAction.do?method=forw&resType='+list[i].id+'">'+list[i].typeName+'</a></li>');
			}
			
		}
	}
	if(window.IndexJsonData){
		menuFuc(IndexJsonData.typeList);
	}else if(window.dirTypes){
		menuFuc(window.dirTypes);
	}else{
		$.post("./order.do?method=getAllDataTypeAttr",{},menuFuc,"text");
	}
}
/**
 * 获取用户的消息数量
 */
var _getUserMessCount = function(data){
	var num = parseInt(data);
	if(num > 99){
		$("#messagerNotify").html("消息(99+)");
	}else{
		$("#messagerNotify").html("消息("+num+")");
	}
}

var _checkUserLogin = function(data){
    if(data && data != ""){
    	if(data !== "_5_5_5_5_"){
    		$("#regBtn").remove();
    		$("#loginBtn").remove();
    		$("#btnsBoxRight").append('<a  class="btn" id="userNameBtn"><span title='+data+'>'+data+'</span></a><a  class="btn" id="loginoutBtn"><span>退出</span></a>')
            $("#shopCar").parent().show();
    		$("#loginoutBtn").click(function(){
                $.post("./user.do?method=loginOut",{},function(){
                	ShoppingCart.prototype.clear();
                	location.href="./user.do?method=loginPage";
                },"text");
            });
            $("#userNameBtn").click(function(){
            	window.location="./user.do?method=jumpChangeInfo";
            })
            $.post("./message.do?method=searchNoMessageCount",{},_getUserMessCount,"text");
    	}
        var car = new ShoppingCart();
        car.setLoginFlag("1");//设置登录状态
        car.getToService();//从服务器获取到本地
    }else{
    	var car = new ShoppingCart();
        car.setLoginFlag("0");//设置登录状态
        car.clear();//清除本地内容
    }
};
var initCar = function(){
	
}
//添加验证
//验证手机号 是返回true
String.prototype.isCellPhone = function () {
    var re = /^1\d{10}$/;
	return (re.test(this));
}
//验证邮箱 是返回true
String.prototype.isEmail = function () {
    var re = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	return (re.test(this));
}

String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}
String.prototype.isEmpty = function () {
    return /^\s*$/.test(this);
}
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
}
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
}
if(typeof(String.prototype.startsWith) != "function"){
	String.prototype.startsWith=function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
	}
}
if(typeof(String.prototype.endsWith) != "function"){
	String.prototype.endsWith=function(str){     
		var reg=new RegExp(str+"$");     
		return reg.test(this);        
	}
}

  
Date.prototype.format = function(format) { // 日期格式化
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

/**
 * 对数组进行排序 
 * list 将要排序的数组
 * parm 排序的字段
 * b排序的方向 down从小到大  其他反向
 */
function sot(list,parm,b){
	var down = function (x, y) {
		return (eval("x." + parm) > eval("y." + parm)) ? -1 : 1;
	}
	var up = function (x, y) {
		return (eval("x." + parm) < eval("y." + parm)) ? -1 : 1;
	}
	if(b == "down") {
		list.sort(down);
	}
	else{
		list.sort(up);
	}
	return list;
}
/**
 * 正则验证数据
 * 此函数多处使用，请勿修改(暂时使用)
 */
//验证中文名称
var isChinaName = function(name) {
	var pattern = /^[\u4E00-\u9FA5]{1,6}$/;
	return pattern.test(name);
} 
// 验证手机号
var isPhoneNo = function(phone) { 
	var pattern = /^1[34578]\d{9}$/; 
	return pattern.test(phone); 
} 
// 验证身份证 
var isCardNo = function(card) { 
	var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
	return pattern.test(card); 
} 
//验证邮箱
var isEmail = function(email) { 
	var pattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/; 
	return pattern.test(email); 
} 
//验证邮政编码
var isPostcode = function(postcode) { 
	var pattern = /^[0-9][0-9]{5}$/; 
	return pattern.test(postcode); 
}


/**
 * ajax获取数据
 * 此函数多处使用，请勿修改
 */
var comAjax = function(obj){
	var url = obj.url ? obj.url : "";
	if(url.length == 0){
		alert("未传入url参数");
		return;
	}
	var type = obj.type ? obj.type : "post";
	if(typeof(obj.data) == "object"){
		var data = obj.data;
	}else{
		alert("传入参数不为对象");return;
	}
	var callBack = obj.callBack;
	var isAsync = typeof(obj.isAsync) == "boolean" ? obj.isAsync : true;
	var dataType = obj.dataType ? obj.dataType : "text"; 
	var errorCallBack = obj.errorCallBack;
	var cache = typeof(obj.cache) == "boolean" ? obj.cache : false;
	var otherArgs = obj.otherArgs;//其他参数
	try{
		$.ajax({
			type : type,
			url : url,
			data : data,
			async:isAsync,
			dataType:dataType,
			cache : cache,
			success:function (rspData,status,rspAll){
				if(rspData === "notLogined"){//未登陆
					comGoLogin();
				}else{
					callBack && callBack(rspData,otherArgs);
				}
			},
			error:function(e){
				console.log("获取网址："+url+" 失败");
				SearchDownload && SearchDownload.destroy();
				//alert("获取网址："+url+" 失败");
				errorCallBack ? errorCallBack(e) : "";
			}
		});
	}catch(e){
		console.log(e);
		//alert("调用ajax失败,请检查是否引入了jquery文件");
	}
}

/**
 * 获取缓存中的数据
 */
var getCacheObj = function(key){
	var rsObj = unescape(getStorage().getItem(key));
	if (rsObj != null && rsObj!='undefined') {
        return JSON.parse(rsObj);
    }
    return null;
}

//动态添加css style
function comAddCSS(cssText){
    var style = document.createElement('style'),  //创建一个style元素
        head = document.head || document.getElementsByTagName('head')[0]; //获取head元素
    style.type = 'text/css'; //这里必须显示设置style元素的type属性为text/css，否则在ie中不起作用
    if(style.styleSheet){ //IE
        var func = function(){
            try{ //防止IE中stylesheet数量超过限制而发生错误
                style.styleSheet.cssText = cssText;
            }catch(e){

            }
        }
        //如果当前styleSheet还不能用，则放到异步中则行
        if(style.styleSheet.disabled){
            setTimeout(func,10);
        }else{
            func();
        }
    }else{ //w3c
        //w3c浏览器中只要创建文本节点插入到style元素中就行了
        var textNode = document.createTextNode(cssText);
        style.appendChild(textNode);
    }
    head.appendChild(style); //把创建的style元素插入到head中    
}

var comGoLogin = function(){
	new PopWindow({
		info:"您未登录，请先登录！点击确定登录。",
		btnName:"确定",
		hasCancelBtn:true,
		btnClick:function(){
			location.href = "user.do?method=loginPage";
		}
	});
}

//获取网址中对象的值
function GetQueryString(name) {
    var reg = new RegExp("(^|[&,?])" + name + "=([^&]*)(&|$)");
    var r = decodeURI(location.href).substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}


if (window.FileReader && typeof(FileReader.prototype.readAsBinaryString) !== "function") {
    FileReader.prototype.readAsBinaryString = function (fileData) {
       var binary = "";
       var pt = this;
       var reader = new FileReader();      
       reader.onload = function (e) {
	       var bytes = new Uint8Array(reader.result);
	       var length = bytes.byteLength;
	       for (var i = 0; i < length; i++) {
	           binary += String.fromCharCode(bytes[i]);
	       }
	    var obj = {};
	    obj.target = {result:binary};
	    pt.onload(obj);
       }
       reader.readAsArrayBuffer(fileData);
    }
}

jQuery.fn.createSelect = function(args){
	return this.each(function(){
		var $box = $(this);
		var $selectInput = $('<input type="text" class="common-input" value=""/>').appendTo($box);
    	var $ul = $('<ul class="common-select"></ul>').appendTo($box);

		var _go = function(obj){
			var list = obj.list;//下拉框参数
			var text = obj.text;//搜索值
			var idStr = obj.idStr;//唯一标识符
			var valueStr = obj.valueStr;//显示字段
			var clearFlag = obj.clearFlag;//清除选择标识
			var select = obj.select;//默认选中的
			var selectKey = obj.selectKey || idStr;
			var $selectLi = null;
			$ul.empty().scrollTop();
			if(clearFlag){
				var $clearLi = $('<li>清除选择</li>').data("obj",null)
				$ul.append($clearLi);
			}
			for(var i in list){
				var tmp = list[i];
				var $li = $('<li >'+tmp[valueStr]+'</li>').data("obj",tmp);
				if(text.length != 0 && tmp[valueStr].indexOf(text) != -1){
					$li.html(tmp[valueStr].replaceAll(text,'<span style="color:#528dde">'+text+'</span>'));
					if($clearLi){
						$clearLi.after($li);
					}else{
						$ul.prepend($li);
					}
				}else{
					$ul.append($li);
				}
				if(select && select == tmp[selectKey]){
					$selectLi = $li;
				}
			}
			$selectLi && $selectLi.click();
		}
		$ul.on("click","li",function(){
			var $this = $(this);
			var t = $this.text();
			if(t === "清除选择"){
				t = "";
			}
			$this.parent().hide().prev().data("obj",$this.data("obj")).val(t);
			$this.parent().find("span").css("color","");
			args.liClick && args.liClick($(this));
		});
		var mouseFlag = false;
		$box.mouseenter(function(){
			mouseFlag = true;
		}).mouseleave(function(){
			mouseFlag = false;
		});
		$selectInput.focus(function(){
			$ul.show();
		}).blur(function(){
			if(!mouseFlag){
				$ul.hide();
			}
		}).keyup(function(){
			args.text = $(this).data("obj",null).val();
			args.select = null;
			_go(args);
		});
		_go(args);
	});
}







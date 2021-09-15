
$(document).ready(function() {
    $("#dataLoad").fadeOut(); //页面加载完毕后即将DIV隐藏
    ie8InRounded();
});
	/**
	 * url:链接
	 * oldcrumbs:原页面包屑
	 * newcrumbs:参数值为 方式1.unknown自动获取跳转页面h1中信息 方式2.为空则根据资源菜单进行查询组装 方式3.其他数值则在跳转页自动拼装 方式4.copy自动拷贝上页路径信息
	 * （注：采用方式1和方式3时h1中内容需要自行填写，必填）
	 */
	loadrightmenu =function(url,newcrumbs,loadmenubarurl){//加载右侧功能界面方法
		
		if(url == null || url == ''){
			$("#scrollable").html('');
			$("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
			return false;
		}
		
		if(newcrumbs != null && newcrumbs != ''){
			if(typeof(window.parent.newcrumbs) != 'undefined'){
				window.parent.newcrumbs.value = newcrumbs;
				window.parent.oldcrumbs.value = $(".crumbs").html();
				if($(".con-heading >h1").html() != undefined && $(".con-heading >h1").html() != ''){
					window.parent.copycrumbs.value = $(".con-heading >h1").html();
				}else if($(".con-header >h1").html() != undefined && $(".con-header >h1").html() != ''){
					window.parent.copycrumbs.value = $(".con-header >h1").html();
				}
			}
		}
		
		if(loadmenubarurl != null && loadmenubarurl != ''){
			if(typeof(window.parent.loadmenubarurl) != 'undefined' ){
				window.parent.loadmenubarurl.value = loadmenubarurl;
				if(typeof(window.parent.loadmenubarid) != 'undefined')
					window.parent.loadmenubarid.value = 0;
			}
		}
		
		//记录跳转url
		if(parent.historyUrl!=null){
			parent.historyUrl.push({
				url:url,
				newcrumbs:newcrumbs,
				loadmenubarurl:loadmenubarurl
			});
		}
		
		window.parent.mainFrame.location.href=getRootPath()+url;
	};
	
	/**
	 * 返回跳转页面(不记录跳转url)
	 * url:链接
	 * oldcrumbs:原页面包屑
	 * newcrumbs:参数值为 方式1.unknown自动获取跳转页面h1中信息 方式2.为空则根据资源菜单进行查询组装 方式3.其他数值则在跳转页自动拼装 方式4.copy自动拷贝上页路径信息
	 * （注：采用方式1和方式3时h1中内容需要自行填写，必填）
	 */
	loadrightmenuForback =function(url,newcrumbs,loadmenubarurl){//加载右侧功能界面方法
		
		if(url == null || url == ''){
			$("#scrollable").html('');
			$("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
			return false;
		}
		
		if(newcrumbs != null && newcrumbs != ''){
			if(typeof(window.parent.newcrumbs) != 'undefined'){
				window.parent.newcrumbs.value = newcrumbs;
				window.parent.oldcrumbs.value = $(".crumbs").html();
			}
		}
		
		if(loadmenubarurl != null && loadmenubarurl != ''){
			if(typeof(window.parent.loadmenubarurl) != 'undefined' ){
				window.parent.loadmenubarurl.value = loadmenubarurl;
				if(typeof(window.parent.loadmenubarid) != 'undefined')
					window.parent.loadmenubarid.value = 0;
			}
		}
		
		window.parent.mainFrame.location.href=getRootPath()+url;
	};
	
	/**
	 * 返回方法
	 */
	goBack = function(){
		var historyObj = null;
		if(parent.historyUrl!=null){
			parent.historyUrl.pop();
			if(parent.historyUrl.length==0){
				historyObj ={
					url:"/sys/portal/manageView.action?portalId=8&portalType=ptype_org"
				}
				loadrightmenuForback(historyObj.url,historyObj.newcrumbs,historyObj.loadmenubarurl);
				parent.loadleftMenu1('1');
			}else{
				historyObj = parent.historyUrl[parent.historyUrl.length-1];
				loadrightmenuForback(historyObj.url,historyObj.newcrumbs,historyObj.loadmenubarurl);
			}
		}
	}
	
	navigationbar = function(menuid,url){//加载右侧导航栏方法 //跳转页面链接
		$.ajaxSetup ({
			cache: false //设置成false将不会从浏览器缓存读取信息
		});
		$(".loading").each(function() {
			$(this).fadeOut();
		});
		if(typeof(window.parent.newcrumbs) != 'undefined' && window.parent.newcrumbs.value != null && window.parent.newcrumbs.value != '' 
			&& typeof(window.parent.oldcrumbs) != 'undefined' && window.parent.oldcrumbs.value != null 
			&& window.parent.oldcrumbs.value != 'undefined' && window.parent.oldcrumbs.value != ''){
			if(window.parent.newcrumbs.value == 'unknown'){
				if($(".con-heading >h1").html() != undefined && $(".con-heading >h1").html() != ''){
					$(".crumbs").html(window.parent.oldcrumbs.value+"<i></i>"+$(".con-heading >h1").html());
					window.parent.newcrumbs.value = "";
					window.parent.oldcrumbs.value = "";
					window.parent.copycrumbs.value = "";
					return;
				}else if($(".con-header >h1").html() != undefined && $(".con-header >h1").html() != ''){
					$(".crumbs").html(window.parent.oldcrumbs.value+"<i></i>"+$(".con-header >h1").html());
					window.parent.newcrumbs.value = "";
					window.parent.oldcrumbs.value = "";
					window.parent.copycrumbs.value = "";
					return;
				}
			}else if(window.parent.newcrumbs.value == 'copy'){
				$(".crumbs").html(window.parent.oldcrumbs.value);
				$(".con-heading >h1").html(window.parent.copycrumbs.value);
				$(".con-header >h1").html(window.parent.copycrumbs.value);
				window.parent.newcrumbs.value = "";
				window.parent.oldcrumbs.value = "";
				window.parent.copycrumbs.value = "";
				return;
			}else {
				$(".con-heading >h1").html(window.parent.newcrumbs.value);
				$(".con-header >h1").html(window.parent.newcrumbs.value);
				$(".crumbs").html(window.parent.oldcrumbs.value+"<i></i>"+window.parent.newcrumbs.value);
				window.parent.newcrumbs.value = "";
				window.parent.oldcrumbs.value = "";
				window.parent.copycrumbs.value = "";
				return;
			}
		}
		
		if(typeof(window.parent.loadmenubarurl) != 'undefined' && window.parent.loadmenubarurl.value != null){
			url = window.parent.loadmenubarurl.value;
			//window.parent.loadmenubarurl.value = "";
		}
		
		if((menuid == null || menuid == 0) && (url == null || url == '')){
			$("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
			return;
		}
		jQuery.ajax({
			url : getRootPath()+"/sys/menu/managenAvigation.action?id="+menuid+"&actionName="+url+"&time="+(+new Date().getTime()),
			type : 'POST',
			success : function(data) {
				$("#navigationMenu").html(data);//要刷新的div
				$("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
			}
		});
	};
	/*
	 * data:参数
	 */
	setUrlParameter = function(data){
		if(typeof(window.parent.urlParameter) != 'undefined')
			window.parent.urlParameter.value = data;
	};
	
	getUrlParameter = function(){
		if(typeof(window.parent.urlParameter) != 'undefined' && window.parent.urlParameter.value != null)
			return window.parent.urlParameter.value;
		else 
			return "";
	};
	
	//判断页面是否正常退出系统，不是进行提示；----start----
	function promptOnClose(e){  
		//var promptString = '提示：当前操作可能会造成数据丢失';  
	    //return promptString;  
	}  
	
	function promptOnUnLoad(e){  
		var mainWindow = document.getElementById('mainFrame').contentWindow;
		if(menuswrite.statue && typeof mainWindow.pageRedirecting!='undefined' ){
			mainWindow.pageRedirecting();//名称统一 ，方法内具体实现使用人自行开发
			menuswrite.statue = false;
		}
	}  
	
	if (window != top) {  
		
	} else {
		/*if (window.Event) {  
			window.onbeforeunload = function(event){  
				return promptOnClose(event);  
			};  
		}  
		else {  
			window.onbeforeunload = function(){  
				return promptOnClose(event);  
			};  
		}*/
		if (window.Event) {  
			window.onunload  = function(event){  
				return promptOnUnLoad(event);  
			};  
		}  
		else {  
			window.onunload  = function(){  
				return promptOnUnLoad(event);  
			};  
		}
	}
	//判断页面是否正常退出系统，不是进行提示；----start----
	
	//判断当前用户是否已分配指定菜单并导向指定菜单-----start-----
	//参数menuid 为菜单id
	loadSpecifyMenu = function(menuid){
		var returnvalue;
		jQuery.ajax({
			url : getRootPath()+"/sys/menu/valUserMenu.action?id="+menuid,
			type : 'POST',
			async:false,
			success : function(data) {
				if(data.success == "false"){
					msgBox.info({content: "您未被分配此菜单，请<br/>联系管理员", type:'fail'});
					returnvalue = false;
				}else {
				//	loadrightmenu(data.menuVo.actionName);
				//	navigationbar(menuid);
					returnvalue = true;
				}
			},
			error:function(){
				$("#dataLoad").fadeOut();   //页面加载完毕后即将DIV隐藏
				returnvalue = false;
			}
		});
		return returnvalue;
	};
	//判断当前用户是否已分配指定菜单并导向指定菜单-----end-----
	
	//首页链接导入右侧界面---start----
	homeloadmenu = function(){
		window.parent.location.reload();
	};
	//首页链接导入右侧界面---end----
	
	//初始化
	jQuery(function($) 
	{
		if(typeof(window.parent.loadmenubarid) != 'undefined' && window.parent.loadmenubarid.value != null)
			navigationbar(window.parent.loadmenubarid.value);
		else {
			$(".loading").each(function() {
				$(this).fadeOut();
			});
		}
			
				$.type = function(o) { 
			        var _toS = Object.prototype.toString; 
			        var _types = { 
			            'undefined': 'undefined', 
			            'number': 'number', 
			            'boolean': 'boolean', 
			            'string': 'string', 
			            '[object Function]': 'function', 
			            '[object RegExp]': 'regexp', 
			            '[object Array]': 'array', 
			            '[object Date]': 'date', 
			            '[object Error]': 'error' 
			        }; 
			        return _types[typeof o] || _types[_toS.call(o)] || (o ? 'object' : 'null'); 
			    }; 
			    var $specialChars = { '\b': '\\b', '\t': '\\t', '\n': '\\n', '\f': '\\f', '\r': '\\r', '"': '\\"', '\\': '\\\\' }; 
			    var $replaceChars = function(chr) { 
			        return $specialChars[chr] || '\\u00' + Math.floor(chr.charCodeAt() / 16).toString(16) + (chr.charCodeAt() % 16).toString(16); 
			    }; 

			    $.toJSON = function(o) { 
			        var s = []; 
			        switch ($.type(o)) { 
			            case 'undefined': 
			                return 'undefined'; 
			                break; 
			            case 'null': 
			                return 'null'; 
			                break; 
			            case 'number': 
			            case 'boolean': 
			            case 'date': 
			            case 'function': 
			                return o.toString(); 
			                break; 
			            case 'string': 
			                return '"' + o.replace(/[\x00-\x1f\\"]/g, $replaceChars) + '"'; 
			                break; 
			            case 'array': 
			                for (var i = 0, l = o.length; i < l; i++) { 
			                    s.push($.toJSON(o[i])); 
			                } 
			                return '[' + s.join(',') + ']'; 
			                break; 
			            case 'error': 
			            case 'object': 
			                for (var p in o) { 
			                    s.push(p + ':' + $.toJSON(o[p])); 
			                } 
			                return '{' + s.join(',') + '}'; 
			                break; 
			            default: 
			                return ''; 
		               break; 
			        } 
		    }; 

			$.evalJSON = function(s) { 
			       if ($.type(s) != 'string' || !s.length) return null; 
			        return eval('(' + s + ')'); 
			}; 


	});
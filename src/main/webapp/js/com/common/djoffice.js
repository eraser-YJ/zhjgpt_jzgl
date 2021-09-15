
/*
 * 
setting = {
obj:’weboffice插件对象’,
filePath:’’,支持服务器路径，为空时打开新文件
type:’’,参数如下
doc:打开、新建Word文件
xls:打开、新建Excel文件
ppt:打开、新建PPT  文件
wps:打开、新建WPS  文件

isCopy:’是否准许复制’, true准许，false不准许
isPrint:’是否准许打印’ true准许，false不准许
isSaveToService:’是否准许上传到服务器’,true准许,false不准许
isSaveToLocal:’ 是否准许本地保存’ true准许，false不准许
isReadonly:’是否只读打开’}; true 只读，false编辑
}


{obj:null, filePath:'', type:'doc', isCopy:true, isPrint:true, isSaveToService:true, isSaveToLocal:true, isReadonly:false}
 * 
 * seal_" style="display:none;">盖章</button>
                    <button class="btn" type="button" name="officeButton" id="save_" style="display:none;">保存到本地</button>
                    <button class="btn" type="button" name="officeButton" id="saveToServer_"
 *  newFile:function() {},
     saveFile : function{},
     seal_ : function{},
     print_ : function{},
     openExistFile
 * */
 
var WebOffice = {
	 setting_:null,
	 //获得weboffice对象
	 getWeboffice:function(){
		   return document.getElementById("WebOffice");
	 },
	 //打开文件
	 openFile:function(setting) {
			var defaultSetting = {  
					filePath:"",//支持服务器路径，为空时打开新文件
					height:window.screen.availHeight-260,
					callback:null,//上传服务器后的回调方法
					type:"doc",//参数如下
					/*doc:打开、新建Word文件
					xls:打开、新建Excel文件
					ppt:打开、新建PPT  文件
					wps:打开、新建WPS  文件*/
					isTemplate:false,
					isShowAlert:true,
					alertMsg:$.i18n.prop("JC_SYS_066"),
					isShowToolBar:true,//
					isNew:true,//准许新建
					isOpen:true,//准许打开
					isAddSeal:false,
					isSave:false,
					editFromNoSave:false,//在没有保存按钮的时候设置成可编辑状态，true为设置可编辑
					isCopy:true,// ’是否准许复制’true准许，false不准许
					isPrint:true,//’是否准许打印’ true准许，false不准许
					isSaveToService:true,//’是否准许上传到服务器’,true准许,false不准许
					isSaveToLocal:false,//’ 是否准许本地保存’ true准许，false不准许
					isReadonly:false//’是否只读打开’}; true 只读，false编辑  
			    };  
			$.extend(defaultSetting,setting);
			defaultSetting.filePath = defaultSetting.filePath.replace(new RegExp("\\\\","gm"),"//");
			//defaultSetting.filePath = "http://192.168.100.233:8080/goa//upload//office//template//1460709501148-16-38-21.docx";
			this.setting_ = defaultSetting;
			var webObj = document.getElementById("WebOffice");//defaultSetting.obj;
			if(!webObj) {
				/*msgBox.tip({
					//type:"success",
					content: "office插件不存"
				});*/
				return;
			}
			//webObj.SetWindowText("长春奕迅建筑智能化工程有限公司", 0);
			//webObj.OptionFlag |= 128;
			//webObj.HideMenuItem(0x04 + 0x20 + 0x10);
			// 加载文件
			//webObj.ShowToolBar = 0;
			try{
				webObj.style.height = defaultSetting.height + 'px';
				webObj.ShowToolBar = 0;
				
				//如果设定的是只读
				/* <button class="btn dark" name="officeButton" type="button" id="new_" style="display:none;">新建</button>
                    <button class="btn" name="officeButton" type="button" id="open_" style="display:none;">打开</button>
                    <button class="btn" type="button" name="officeButton" id="print_" style="display:none;">打印</button>
                    <button class="btn" type="button" name="officeButton" id="seal_" style="display:none;">盖章</button>
                    <button class="btn" type="button" name="officeButton" id="save_" style="display:none;">保存到本地</button>
                    <button class="btn" type="button" name="officeButton" id="saveToServer_" style="display:none;">保存到服务器</button>
				 * */
				if(defaultSetting.isReadonly) {
					webObj.LoadOriginalFile(defaultSetting.filePath, defaultSetting.type);
				    webObj.SetSecurity(0x04);
					if(defaultSetting.isPrint) {
						$("#print_").css("display",'inline-block');
						//webObj.HideMenuItem(0x01 + 0x02 + 0x04);
					}  
					//webObj.SetToolBarButton2("Menu Bar",1,8);
					webObj.ProtectDoc(1,2, '1qaz2wsx!@');
				} else {
					//恢复允许复制
					webObj.SetSecurity(0x04 + 0x8000); 
					//webObj.ProtectDoc(0,2, '1qaz2wsx!@');
					//本地保存和复制有一个不可用 俩个功能都屏蔽
					//	defaultSetting.isSaveToLocal = defaultSetting.isCopy = defaultSetting.isCopy && defaultSetting.isSaveToLocal;
					webObj.LoadOriginalFile(defaultSetting.filePath, defaultSetting.type);
					if(!defaultSetting.isSaveToLocal && !defaultSetting.isSave && !defaultSetting.isSaveToService) {
						//不能保存到本地，屏蔽打印
						//defaultSetting.isPrint = false;
						//webObj.HideMenuItem(0x04);
						//没有保存按钮时 可编辑例如  纪要退回 制度退回后可编辑但没有保存按钮
						if(!defaultSetting.editFromNoSave) {
							webObj.SetSecurity(0x04);
						}else{
							webObj.ProtectDoc(0,0, '1qaz2wsx!@');
						}  
						//webObj.HideMenuItem(0x01 + 0x02 + 0x04 + 0x10 + 0x20);
					} else {
						if(defaultSetting.isSaveToService) {
							$("#saveToServer_").css("display",'inline-block');
						} else {
							$("#saveToServer_").css("display",'none');
						}
						if(!defaultSetting.isCopy) {
							webObj.SetSecurity(0x04);
						} else {
						}
						
						if(defaultSetting.isPrint) {
							$("#print_").css("display",'inline-block');
							//webObj.HideMenuItem(0x10 + 0x20);
						} else {
							$("#print_").css("display",'none');
						}
						if(defaultSetting.isSaveToLocal) {
							$("#save_").css("display",'inline-block');
							//webObj.HideMenuItem(0x04);
						} else {
							$("#save_").css("display",'none');
						}
						if(defaultSetting.isNew) {
							$("#new_").css("display",'inline-block');
							//webObj.HideMenuItem(0x01);
						} else {
							$("#new_").css("display",'none');
						}
						if(defaultSetting.isOpen) {
							$("#open_").css("display",'inline-block');
							//webObj.HideMenuItem(0x02);
						} else {
							$("#open_").css("display",'none');
						}
						if(defaultSetting.isAddSeal) {
							$("#seal_").css("display",'inline-block');
						} else {
							$("#seal_").css("display",'none');
						}
					}
				}
				if(defaultSetting.isShowToolBar) {
					$("#officeToolbar_").css("display",'block');
					if(defaultSetting.isSave) {
						$("#tempSave_").css("display",'inline-block');
					}
					if(defaultSetting.isAddSeal) {
						$("#seal_").css("display",'inline-block');
					} else {
						$("#seal_").css("display",'none');
					}
					
					var btn = $("#officeToolbar_ .btn");//.children('.btn');
					btn.each(function(index, el){
						if('none' != $(el).css('display')) {
							$(el).addClass('dark');
							return false;
						}
					});
					
					
				} else {
					$("#officeToolbar_").css("display",'none');
					
				}
				
			//	alert(defaultSetting.filePath);
				//webObj.LoadOriginalFile(defaultSetting.filePath, defaultSetting.type);
			//	webObj.SaveTo('c:\\webOffi222222ce.doc');
				//webObj.PrintDoc(1);
			}catch(e){
				
			}
			
 
			
			/**/
	/*		defaultSetting = {  
					filePath:"",//支持服务器路径，为空时打开新文件
					height:650,
					callback:null,//上传服务器后的回调方法
					type:"doc",//参数如下
					doc:打开、新建Word文件
					xls:打开、新建Excel文件
					ppt:打开、新建PPT  文件
					wps:打开、新建WPS  文件
					isTemplate:false,
					isShowToolBar:true,//
					isNew:true,//准许新建
					isOpen:true,//准许打开
					isAddSeal:false,
					isSave:false,
					isCopy:true,// ’是否准许复制’true准许，false不准许
					isPrint:true,//’是否准许打印’ true准许，false不准许
					isSaveToService:true,//’是否准许上传到服务器’,true准许,false不准许
					isSaveToLocal:false,//’ 是否准许本地保存’ true准许，false不准许
					isReadonly:false//’是否只读打开’}; true 只读，false编辑  
			    }; */
			/**/
		},
		initParam:function(){}
};

function saveFileToService_(){
	if(typeof(WebOffice) != 'undefined' && WebOffice.setting_ != null && typeof(WebOffice.setting_) != 'undefined' && typeof(WebOffice.setting_.isShowAlert) != 'undefined' && WebOffice.setting_.isShowAlert) {
		saveToService_();
		/*msgBox.confirm({
			content: WebOffice.setting_.alertMsg,
			success: function(){
			}
		});*/
	} else {
		saveToService_();
	}
	
	
	//alertSuccess();
	
     
} 
function saveToService_() {
	 try{ 
	        var webObj=  WebOffice.getWeboffice();//document.getElementById("WebOffice"); 
	        //webObj.OptionFlag |= 0x0400;
	        webObj.HttpInit();          //初始化Http引擎   
	        // 添加相应的Post元素    
	       // webObj.HttpAddPostString("fname", "123");   
	       //	webObj.HttpAddPostString("oldFileName",""); 
	        try{
	        	webObj.HttpAddPostCurrFile("file","");
	        } catch(er) {
	        	
	        }
	        //var a = webObj.SaveTo("c:\\webOffice.doc");
	       // alert(a);
	        var param = '/common/dj/uploadFile.action';
			 if(WebOffice.setting_.isTemplate) {
				 param = '/common/dj/uploadFile.action?template=1';
			 } 
			 var  returnValue = '';
			 try{
				 var  returnValue = webObj.HttpPost(path_ + param);    // 判断上传是否成功 
			 } catch(er) {
				 
			 }
	      //  alert("==="+ returnValue);
	        var o = null;
	        if(returnValue) {
     		//返回值包括
     		//fileName
     		//filePath
     		//fileSize
     		//relativePath 相对项目根目录路径
     		o = JSON.parse(returnValue);
     		
     	}  
	        if(WebOffice.setting_.callback) {
	        	if(o) {
	        		//返回值包括
	        		//fileName
	        		//filePath
	        		//fileSize
	        		//relativePath 相对项目根目录路径
	        		//var o = JSON.parse(returnValue);
	        		WebOffice.setting_.callback(o);
	        	} else {
	        		WebOffice.setting_.callback(null);
	        	}
			}
	    }catch(e){
	        //alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);   
	    } 
}
function saveFile_(){
	var webObj = WebOffice.getWeboffice();
	webObj.ShowDialog(84);
}

function alertSuccess() {
	msgBox.tip({
		type:"success",
		content: $.i18n.prop("JC_SYS_001")
	});
}
function tempSaveFile_() {
	if(WebOffice.setting_.callback) {  
		WebOffice.setting_.callback();
	}
	alertSuccess();
	/*var webObj = WebOffice.getWeboffice();
	alert(1);
	var a = webObj.GetFileBase64();
	alert(a);*/
}
function addSeal_(){
	var webObj = WebOffice.getWeboffice();
	//WebOffice.setting_.callback("22");
	//webObj.ProtectDoc(0,2, '1qaz2wsx!@');
	DESSeal.addSeal();
}
function printFile_(){
	var webObj = WebOffice.getWeboffice();
	webObj.PrintDoc(1);
}
//打开本地文件
function openExistFile_(){
	var webObj = WebOffice.getWeboffice();
	webObj.OpenFileDlg();
}

function newFile_() {
	var webObj = WebOffice.getWeboffice();
	webObj.LoadOriginalFile('',WebOffice.setting_.type);
}
function tempClose_() {
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_030"),
		success: function(){
			window.close();
		}});
}

var WebOffice_NotifyToolBarClick = function(index){
	if(index == 32776) {
		//上传
		confirmx("确认提交到服务器?",function(){
			WebOffice.uploadFileToService();
		});
		 
	} else if(index == 32777) {
		if(WebOffice.setting_.addBookmark.callback) {
			WebOffice.setting_.addBookmark.callback();
		}
	}
}
/*var setting_ = null;
function uploadFileToService() {  
	
    try{ 
        var webObj=document.getElementById("WebOffice");   
        webObj.HttpInit();          //初始化Http引擎   
        // 添加相应的Post元素    
       // webObj.HttpAddPostString("fname", "123");   
       //	webObj.HttpAddPostString("oldFileName","");   
        webObj.HttpAddPostCurrFile("file","");
        //var a = webObj.SaveTo("c:\\webOffice.doc");
       // alert(a);
        var  returnValue = webObj.HttpPost("/common/dj/uploadFile.action?id=template");    // 判断上传是否成功 
      //  alert("==="+ returnValue);
        if(WebOffice.setting_.callback) {
        	if(returnValue) {
        		var o = JSON.parse(returnValue);
        		WebOffice.setting_.callback(o);
        	} else {
        		WebOffice.setting_.callback("");
        	}
		}
        //  var c = webObj.SaveAs("C:\\saveas.doc", 0);
  //     alert(c);
       
       //webObj.SaveTo("http://172.16.3.23:8080/goa/doc/test.doc");
          // 上传文件
         var tempPath=webObj.GetTempFilePath();//获取临时文件路径
        alert(tempPath);
		var v=webObj.GetFileBase64("",0);//获取当前文档base64值
		webObj.SaveBinaryFileFromBase64(tempPath,v);	  
         
        //alert(webObj.GetFileBase64("", 0));
       // alert(getRootPath()+"/common/dj/uploadFile.action?id=template");
      //  var  returnValue = webObj.HttpPost("/common/dj/uploadFile.action?id=template");    // 判断上传是否成功 
          $.ajax({
   			 type : "POST",
   			 url : getRootPath()+"/common/dj/uploadFile.action",
   			 data : {fileName: 'name', fileType:'type',base64:webObj.GetFileBase64("", 0)},
   			 //dataType : "json",
   			 success : function(data) {
   				 if(data == '1') {
   					 //更新成功
   				 }
   			 } ,
   			 error:function() {
   				 
   			 }
		 });   
       
    }catch(e){
        //alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);   
    }   
}
 var WebOffice_NotifyToolBarClick = function(index){
	 //alert(index);
	if(index == 32776) {
	//上传
		confirmx("确认提交到服务器?",function(){
			uploadFileToService();
		});
		if(WebOffice.setting_.callback) {
			WebOffice.setting_.callback();
		}
	}  

}
var openFile = function(setting) {
	setting_ = setting;
	var defaultSetting = {  
			filePath:"",//支持服务器路径，为空时打开新文件
			userDefined:null,
			height:650,
			callback:null,
			type:"doc",//参数如下
			doc:打开、新建Word文件
			xls:打开、新建Excel文件
			ppt:打开、新建PPT  文件
			wps:打开、新建WPS  文件

			isCopy:true,// ’是否准许复制’true准许，false不准许
			isPrint:true,//’是否准许打印’ true准许，false不准许
			isSaveToService:true,//’是否准许上传到服务器’,true准许,false不准许
			isSaveToLocal:true,//’ 是否准许本地保存’ true准许，false不准许
			isReadonly:false//’是否只读打开’}; true 只读，false编辑  
	    };  
	      
	$.extend(defaultSetting,setting);
	var webObj = document.getElementById("WebOffice");//defaultSetting.obj;
	if(!webObj) {
		msgBox.tip({
			//type:"success",
			content: "office插件不存"
		});
		return;
	}
	webObj.SetWindowText("长春奕迅建筑智能化工程有限公司", 0);
	webObj.OptionFlag |= 128;
	//webObj.HideMenuItem(0x04 + 0x20 + 0x10);
	// 加载文件
	//webObj.ShowToolBar = 0;
	try{
		webObj.LoadOriginalFile(defaultSetting.filePath, defaultSetting.type);
		webObj.height = defaultSetting.height + 'px';
		//如果设定的是只读
		if(defaultSetting.isReadonly) {
			webObj.SetSecurity(0x04);
			if(defaultSetting.isPrint) {
				webObj.HideMenuItem(0x01 + 0x02 + 0x04);
			} else {
				webObj.HideMenuItem(0x01 + 0x02 + 0x04 + 0x20 + 0x10);
			}
			webObj.ProtectDoc(1,1, '1qaz2wsx!@');
		} else {
			//本地保存和复制有一个不可用 俩个功能都屏蔽
			//	defaultSetting.isSaveToLocal = defaultSetting.isCopy = defaultSetting.isCopy && defaultSetting.isSaveToLocal;
			if(!defaultSetting.isSaveToLocal && !defaultSetting.isSaveToService) {
				//不能保存到本地，屏蔽打印
				//defaultSetting.isPrint = false;
				//webObj.HideMenuItem(0x04);
				webObj.SetSecurity(0x04);
				webObj.HideMenuItem(0x01 + 0x02 + 0x04 + 0x10 + 0x20);
			} else {
				if(defaultSetting.isSaveToService) {
					webObj.SetCustomToolBtn(0,"服务器保存");
				}
				if(!defaultSetting.isCopy) {
					webObj.SetSecurity(0x04);
				}
				
				if(!defaultSetting.isPrint) {
					webObj.HideMenuItem(0x10 + 0x20);
				}
			}
		}
	}catch(e){
		
	}

}

*/
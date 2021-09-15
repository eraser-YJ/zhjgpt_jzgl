/*var HWPostil_NotifyCtrlReady = function() {
	
}*/
var Aip = {
		setting_:null,
		getAip:function(){
			return document.getElementById('HWPostil');
		},
        openFile:function(setting){
        	var defaultSetting = {  
					filePath:"",//支持服务器路径，为空时打开新文件
					fileContent:"",
					height:580,
					callback:null,//上传服务器后的回调方法
					type:"doc",
					isShowAlert:true,
					alertMsg:$.i18n.prop("JC_SYS_066"),
					//参数如下
					/*aip: aip文档文件
					　 doc: doc文档文件
					　 ppt: powerpoint文档文件
					　 xls: excel文档文件
					　 wps: wps文档文件
					　 bmp: bmp图像文件
					　 jpg: jpg图像文件
					　 tif: tif图像文件
					　 png: png图像文件
					　 空字符串: 未知格式*/
					isNoButton:false,
					isTemplate:false,
					isFile:false,
					templateType:0,
					isAddSeal:false,
					isPrint:false,//’是否准许打印’ true准许，false不准许
					gray:0,// 使AIP文档中指定类型变成灰色。
					/*
					    GRAY_OUTPUT_PIC = 1：文档内所有图片变成灰色
						GRAY_OUTPUT_TEXT = 2：文档内所有文字变成灰色
						GRAY_OUTPUT_GLYPH = 4：文档内所有矢量图形变成灰色
						GRAY_OUTPUT_SEAL = 8：文档内所有印章变成灰色
						GRAY_OUTPUT_STROKE = 16：文档内所有手写变成灰色
					 * */
					close:true,
					state:1,//0只读，1编辑， 2审阅
					isSaveToService:true,//’是否准许上传到服务器’,true准许,false不准许
					isSaveToLocal:false//’ 是否准许本地保存’ true准许，false不准许
			    };  
			      
			$.extend(defaultSetting,setting);
			this.setting_ = defaultSetting;
			var aipObj = document.getElementById('HWPostil');//getAip();
			if(!aipObj) {
				return;
			}
			try{
				aipObj.ShowToolBar = 0;//defaultSetting.showToolBar;
				aipObj.ShowDefMenu = 0;
				//aipObj.style.height = defaultSetting.height + 'px';
				
				aipObj.style.height = window.screen.availHeight - 170 + 'px';
				
				aipObj.Login("HWSEALDEMO",4,65535,"DEMO","");
				if(defaultSetting.isPrint) {
					$("#printAIP_").css("display",'inline-block');
				}
				if(!defaultSetting.isNoButton) {
					$("#aipToolbar_").css("display",'block');
				} else {
					$("#aipToolbar_").css("display",'none');
				}
				if(defaultSetting.isSaveToService) {
					$('#saveAIPToServer_').css("display",'inline-block');
				} else{
					$('#saveAIPToServer_').css("display",'none');
				}
				if(defaultSetting.isSaveToLocal) {
					$('#isSaveToLocal').css("display",'inline-block');
				} else {
					$('#isSaveToLocal').css("display",'none');
				}
				if(defaultSetting.close) {
					$('#closeAIP_').css("display",'inline-block');
				} else {
					$('#closeAIP_').css("display",'none');
				}
				if(defaultSetting.state == 0) {
					if(defaultSetting.close) {
						$('#closeAIP_').css("display",'inline-block');
					} else {
						$('#closeAIP_').css("display",'none');
					}
					if(defaultSetting.isFile) {
						//aipObj.CloseDoc(0);
						//aipObj.LoadOriginalFile('', defaultSetting.type);
						//aipObj.LoadFileBase64("");
						aipObj.LoadOriginalFile(defaultSetting.fileContent,'unk');
						$("#addSeal_").css("display",'none');
					} else {
						if(defaultSetting.isAddSeal) {
							$("#addSeal_").css("display",'inline-block');
						}
						aipObj.LoadFileBase64(defaultSetting.fileContent);
					}
					if(defaultSetting.isAddSeal) {
						//aipObj.DocProperty("DocReadonly") = "true";
					} else {
					}
					getAIPFile_();
					if(defaultSetting.gray > 0) {
						aipObj.GrayData(defaultSetting.gray);
					}
					//aipObj.ConvertToAip(1,0);
					
					//$("#openAIP_").css("display",'inline-block');
					//aipObj.LoadFileEx(defaultSetting.filePath, defaultSetting.type,0,1);
				} else if(defaultSetting.state == 1) {
					//$("#newAIP_").css("display",'inline-block');
					$("#openAIP_").css("display",'inline-block');
					/*if(defaultSetting.isSaveToService) {
						$('#saveAIPToServer_').css("display",'inline-block');
					}
					if(defaultSetting.isSaveToLocal) {
						$('#isSaveToLocal').css("display",'inline-block');
					}*/
					if(defaultSetting.fileContent) {
						if(defaultSetting.isFile) {
							if("aip" == defaultSetting.type) {
								aipObj.LoadFile(defaultSetting.fileContent);
							} else {
								$("#newAIP_").css("display",'inline-block');
								aipObj.LoadOriginalFile(defaultSetting.fileContent, defaultSetting.type);
								if(aipObj.CurrDocType != 128){
								}
								aipObj.ShowView = 32;
							}
						} else {
							$("#newAIP_").css("display",'inline-block');
							aipObj.LoadFileBase64(defaultSetting.fileContent);
							//alert(defaultSetting.templateType == 0);
							if(defaultSetting.templateType == 0){
								aipObj.ShowView = 32;
							}
						}
						//aipObj.LoadOriginalFile(defaultSetting.filePath, 'unk');
					} else {
						if("aip" != defaultSetting.type) {
							$("#newAIP_").css("display",'inline-block');
						} 
						aipObj.LoadOriginalFile('', defaultSetting.type);
					}
					/*if("doc" == defaultSetting.type || "xls" == defaultSetting.type || "wps" == defaultSetting.type) {
						//aipObj.LoadOriginalFile(defaultSetting.filePath, defaultSetting.type);
					} else {
						//aipObj.LoadFileEx(defaultSetting.filePath, defaultSetting.type,0,1);
					}*/
					//aipObj.ShowToolBar = 0;
				} else if(defaultSetting.state == 2) {
					$("#viewAip_").css("display",'inline-block');
					$("#removeviewAip_").css("display",'inline-block');
					/*if(defaultSetting.isSaveToService) {
						$('#saveAIPToServer_').css("display",'inline-block');
					}
					if(defaultSetting.isSaveToLocal) {
						$('#isSaveToLocal').css("display",'inline-block');
					}*/
					//aipObj.LoadFileEx(defaultSetting.filePath, 'unk',0,1);
					//alert(defaultSetting.fileContent);
					aipObj.LoadFileBase64(defaultSetting.fileContent);
				
					aipObj.CurrAction=264;
				} else {
					//aipObj.LoadFileBase64(defaultSetting.fileContent);
					aipObj.LoadOriginalFile("", defaultSetting.type);
				}
			
				
				aipObj.ShowToolBar = 0;//defaultSetting.showToolBar;
				aipObj.ShowDefMenu = 0;
				//aipObj.SetFieldValue("mark1","[电子]","::ADDMARK::");
				//alert(aipObj.ShowDialog(0,"","",""));
				//var al = aipObj.ShowDialog(0,"","","");
				//alert(aipObj.SaveTo(al,"doc"));
				//aipObj.CurrAction = 264;
				//aipObj.HideMenuItem();
				//alert(aipObj.GetDocText());
			//	aipObj.PrintDoc(1,1);
				//aipObj.ShowView = defaultSetting.showView;
				//aipObj.Login("HWSEALDEMO",4,65535,"DEMO","");
				var btn = $("#aipToolbar_ .btn");//.children('.btn');
				btn.each(function(index, el){
					if('none' != $(el).css('display')) {
						$(el).addClass('dark');
						return false;
					}
				});
			}catch(e){
				
			}
        },
        clearFile:function(){
        	var aip = Aip.getAip();
        	aip.ShowView = 32;
        	aip.LoadOriginalFile("","doc");
        },
        getData : function() {
        	var mydata = null;
        	var aip = Aip.getAip();
        	var b = '';
        	aip.ShowView = 32;
        	//aip.ConvertToAip(1,1);
        	var tempfile = aip.GetTempFileName('');
        	aip.GetOriginalFile(tempfile, -1);
        	aip.LoadOriginalFile(tempfile, 'doc');
        	//aip.GetOriginalFile("c:\\new1.doc", -1);
        	//aip.LoadOriginalFile("c:\\new1.doc", 'doc');
        	b = aip.GetCurrFileBase64();
        	//$('#base1').text(b);
        	var fileData = getAIPFile_();
        	mydata = {contentFile:b,reviewFile:fileData,viewFile:fileData,docText:aip.GetDocText()};
        	aip.DeleteLocalFile(tempfile);
        	return mydata;
        }
}

 
function closeAIPFile_() {
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_030"),
		success: function(){
			window.close();
		}});
/*	window.close();
	  if(Aip.setting_.callback) {
      		//Aip.setting_.callback();
      	} */ 
}
/**
 * 保存文档数据到页面缓存
 */
function save_() {
	var mydata = null;
	var aip = Aip.getAip();
	var b = '';
	try{
		if(window.opener.Doc['functionType'] == 1) {
//		alert(aip.GetDocText());
			//编辑
			aip.ShowView = 32;
			//aip.ConvertToAip(1,1);
			var tempfile = aip.GetTempFileName('');
			aip.GetOriginalFile(tempfile, -1);
			aip.LoadOriginalFile(tempfile, 'doc');
			//aip.GetOriginalFile("c:\\new1.doc", -1);
			//aip.LoadOriginalFile("c:\\new1.doc", 'doc');
			b = aip.GetCurrFileBase64();
			//$('#base1').text(b);
			var fileData = getAIPFile_();
			var docText = aip.GetDocText().replace(/\s+/g,"");
			if(docText){
				docText = "正文已有内容";
			} else {
				docText = "";
			}
			mydata = {contentFile:b,text:docText,reviewFile:fileData,sealFile:fileData,viewFile:fileData,docText:aip.GetDocText()};
			aip.DeleteLocalFile(tempfile);
		} else if(window.opener.Doc['functionType'] == 2) {
			b = aip.GetCurrFileBase64();
			//$('#base1').text(b);
			//审阅
			mydata = {reviewFile:b};
		} else if(window.opener.Doc['functionType'] == 3){
			//$('#base1').text(b);
			//盖章
			b = aip.GetCurrFileBase64();
			mydata = {sealFile:b,viewFile:b};
		} else if(window.opener.Doc['functionType'] == 4){
			//排版
			b = aip.GetCurrFileBase64();
			mydata = {composingFile:b};
		}
		/*msgBox.tip({
		type:"success",
		content: $.i18n.prop("JC_SYS_001")
	});*/
		if(Aip.setting_.callback) {
			//alert(aip.GetDocText());
			Aip.setting_.callback(mydata);
		}
	} catch(e) {
		
		msgBox.confirm({
			content: "网络或登录异常,请重新进入,点击  '是'关闭页面,'否' 保留页面",
			success: function(){
				window.close();
			}
		});
	}
  /*  var workId = $('#workId').val();
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if(!fso.FolderExists("c:\\aip")){
    	fso.CreateFolder("c:\\aip"); 
    }
    if(!fso.FolderExists("c:\\aip\\"+workId)){
    	fso.CreateFolder("c:\\aip\\"+workId); 
    }   
    var result = aip.SaveTo("c:\\aip\\"+workId+"\\1.aip","aip",0);
	/*aip.HttpInit();          //初始化Http引擎   
	 aip.HttpAddPostString("workId",workId);//通过控件post字符串参数
	 aip.HttpAddPostCurrFile("file");
	 //var a = webObj.SaveTo("c:\\webOffice.doc");
	var result = aip.HttpPost(IPAddress+"common/dj/saveAip.action");*/
 
}


function getAIPFile_() {
	var aip = Aip.getAip();
	/*
	 * 
	 * 128: AIP文档
		11: Word文档
		13: PowerPoint文档
		12: Excel文档
		31: PDF文档
		21: WPS文档
		127: 未知文档
	 * */
	if(aip.CurrDocType != 128) {
		aip.ConvertToAip(1, 0);
	} 
	return aip.GetCurrFileBase64();
}
function newAIPFile_() {
	var aip = Aip.getAip();
	aip.LoadOriginalFile("", Aip.setting_.type);
	aip.ShowToolBar = 0;//defaultSetting.showToolBar;
	//aip.HideMenuItem(2);
	aip.ShowDefMenu = 0;
}
function openAIPExistFile_() {
	var aip = Aip.getAip();
	var file = aip.ShowDialog(0,'','','');
	/*if("doc" == Aip.setting_.type || "xls" == Aip.setting_.type || "wps" == Aip.setting_.type) {
		//aip.ShowView = 2;
		aip.LoadOriginalFile(file, Aip.setting_.type);
		aip.ShowToolBar = 0;//defaultSetting.showToolBar;
		//aip.HideMenuItem(2);
		aip.ShowDefMenu = 0;
	} else {
		aip.LoadFileEx(file,Aip.setting_.type,0,1);
	}*/
	aip.LoadOriginalFile(file, 'unk');
}
function addSealAIP_() {
	var aip = Aip.getAip();
	//document.all.HWPostil1.Login("aip",1,32767,"","");			//本地智能卡用户登录
	//document.all.HWPostil1.Login("HWSEALDEMO",4,65535,"DEMO","");
	aip.Login("HWSEALDEMO",4,65535,"DEMO","");
	if(Aip.setting_.templateType == '2') {
		//骑缝章
		var num = aip.PageCount;
		//aip.AddQiFengSeal(0, 1,"",1);
		//aip.AddQifengSeal(0,"5,50,2,50,1","","");
		//aip.AddQifengSeal(0,0+","+(50000*1/num+6000)+",0,38000,50,1","","AUTO_ADD_SEAL_FROM_PATH");
		//aip.AddQifengSeal(0,0+","+(50000*2/1+6000)+",0,38000,50,1","","AUTO_ADD_SEAL_FROM_PATH");
		//document.getElementById("HWPostil1").AddQifengSeal(0,0+","+(50000*3/docNum+6000)+",0,38000,50,1","","AUTO_ADD_SEAL_FROM_PATH");
		for(i = 0; i < num-1; i++){
			//aip.AddQifengSeal(0,0+","+(50000*(i+1)/num-70)+",0,24000,50,1","","AUTO_ADD_SEAL_FROM_PATH");
		}
	} else {
		//aip.CurrAction = 2568;
	}
	aip.CurrAction = 2568;
}
function printAIPFile_() {
	var aip = Aip.getAip();
	var num = 0;
	var controlPrint = $('#controlPrint_inview').val();//window.opener.Doc['controlPrint'];
	//流程外的打印操作根据开关控制印章颜色，开关开启印章全灰，outWrokflow值是1表示不在流程 内打印
	if("1" == $('#outWrokflow').val() || window.opener.fromInside){
		if(controlPrint == "1") {
			aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
			num = aip.PrintDoc(1,1);
		} else {
			num = aip.PrintDoc(1,1);
		}
	} else {
		if(controlPrint == "1") {
			var url = getRootPath()+"/doc/send/get.action?time=" + new Date();
			jQuery.ajax({
				url : url,
				type : 'get',
				data : {id:window.opener.Doc['id']},
				success : function(data) {
					var p = data.printedNum ? data.printedNum : 0;
					window.opener.Doc['printedNum'] = p;
					//printNum
					aip = Aip.getAip();
					if(!data.printNum || data.printNum < 1) {
						num = aip.PrintDoc(1,1);	
						//不设定打印分数时不控制打印颜色
						window.opener.Doc['printNum'] = 99999999999;
						
					} else {
						window.opener.Doc['printNum'] = data.printNum;
						var aip = Aip.getAip();
						if(p >= data.printNum) {
							msgBox.confirm({
								content: "您已超出规定打印份数，公章将置灰显示，是否继续?",
								success: function(){
									aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
									num = aip.PrintDoc(1,1);	
								}
							});
						} else {
							num = aip.PrintDoc(1,1);
							if(window.opener.addPrintedNum && num > 0) {
								window.opener.addPrintedNum();
								if((+window.opener.Doc['printNum']) <= (+window.opener.Doc['printedNum'])){
									aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
								}
							}
						}
						/*if(p < 1) {
							aip.PrintDoc(1,1);
						} else {
						 	if(p >= (+window.opener.Doc['printNum'])) {
								msgBox.confirm({
									content: "您已超出规定打印份数，公章将置灰显示，是否继续?",
									success: function(){
										aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
										num = aip.PrintDoc(1,1);	
									}
								});
							} else {
								num = aip.PrintDoc(1,1);
								if(window.opener.addPrintedNum && num > 0) {
									window.opener.addPrintedNum();
									if((+window.opener.Doc['printNum']) <= (+window.opener.Doc['printedNum'])){
										aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
									}
								}
							}*/
						}
				},
				error : function() {
					
				}
			});
			//需要控制章的打印颜色
			/*if((+window.opener.Doc['printNum']) <= (+window.opener.Doc['printedNum'])){
				msgBox.confirm({
					content: "您已超出规定打印份数，公章将置灰显示，是否继续?",
					success: function(){
						var aip = Aip.getAip();
						aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
						num = aip.PrintDoc(1,1);	
					}
				});
			} else {*/
			/*	var url = getRootPath()+"/doc/send/get.action?time=" + new Date();
				jQuery.ajax({
					url : url,
					type : 'get',
					data : {id:window.opener.Doc['id']},
					success : function(data) {
						var p = data.printedNum ? data.printedNum : 0;
						window.opener.Doc['printedNum'] = p;
						var aip = Aip.getAip();
						if(p < 1) {
							aip.PrintDoc(1,1);
						} else {
							if(p >= (+window.opener.Doc['printNum'])) {
								msgBox.confirm({
									content: "您已超出规定打印份数，公章将置灰显示，是否继续?",
									success: function(){
										aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
										num = aip.PrintDoc(1,1);	
									}
								});
							} else {
								num = aip.PrintDoc(1,1);
								if(window.opener.addPrintedNum && num > 0) {
									window.opener.addPrintedNum();
									if((+window.opener.Doc['printNum']) <= (+window.opener.Doc['printedNum'])){
										aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
									}
								}
							}
						}
					},
					error : function() {
						
					}
				});*/
			
		} else {
			num = aip.PrintDoc(1,1);	
		}
	}
	/*
	if(8 == Aip.setting_.gray) {
		msgBox.confirm({
			content: "打印份数已到上限，公章将变灰是否继续？",
			success: function(){
				var aip = Aip.getAip();
				aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
				num = aip.PrintDoc(1,1);	
			}
		});
	} else {
		num = aip.PrintDoc(1,1);	
	}
	if(controlPrint == "1" && window.opener.addPrintedNum && num > 0) {
		window.opener.addPrintedNum();
		if((+window.opener.Doc['printNum']) <= (+window.opener.Doc['printedNum'])){
			aip.GrayData(GRAY_OUTPUT_SEAL = 8);  //文档内所有的章变成灰色
		}
	}
	*/
}
function viewAIP_() {
	var aip = Aip.getAip();
	//aip.CurrAction=520;
	if(aip.CurrAction==264) {
		aip.CurrAction=1;
	} else {
		aip.CurrAction=264;
	}
}
function get64_(){
	var aip = Aip.getAip();
//	alert(aip.GetDocText());
	aip.ShowView = 32;
	//aip.ConvertToAip(1,1);
	var b = aip.GetCurrFileBase64();
	$('#base1').text(b);
}
function test2_() {
	
	var aip = Aip.getAip();
	aip.ShowView = 1;
}
function edit_(){
	//var aip = Aip.getAip();
//	alert(aip.GetDocText());
//	Aip.openFile({filePath:"http://172.16.3.23:8080/goa/upload\\office\\1401065534000-8-52-14.aip",state:2,type:"aip",callback:function(a){
	var aip = Aip.getAip();
	aip.LoadOriginalFile('','doc');
/*	Aip.openFile({filePath:"",state:1,type:"doc",callback:function(a){
		// window.opener.Template['fileName'] = a['fileName'];
		// window.opener.Template['fileSize'] = a['fileSize'];
		// window.opener.Template['filePath'] = a['filePath'];
		 //alert(window.opener.Template['filePath']);
		 //window.close();
	}});*/
}
function view_(){
	var aip = Aip.getAip();
	Aip.openFile({filePath:"http://172.16.3.23:8080/goa/upload\\office\\1401072239337-10-43-59.aip",state:1,type:"doc",callback:function(a){
		 
	}});
	//aip.ShowView = 32;
}
function set64_(a) {
	var aip = Aip.getAip();
	var a="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" +
			"fVMpAAA=";
	aip.LoadFileBase64(a);
	aip.ShowView = 32;
}
function test_() {
	var aip = Aip.getAip();
	//aip.CurrAction=2568;
	//aip.ShowView = 128;
	var tempfile = aip.GetTempFileName('');
	
	//alert(tempfile);
	//aip.GetOriginalFileType(11);
	//aip.GetOriginalFile("c:\\1.doc", 0);
	//aip.GetOriginalFile(tempfile, -1);
	aip.ShowView = 32;
	//aip.GetOriginalFile("http://172.16.3.23:8080/goa/upload\\office\\1401072239337-10-43-59111111.doc", -1);
	aip.GetOriginalFile("c:\\new1.doc", -1);
	aip.LoadOriginalFile("c:\\new1.doc", 'doc');
	/*var a = aip.LoadFile(tempfile);
	aip.HttpInit();          //初始化Http引擎   
	aip.HttpAddPostCurrFile("file");
   // aip.HttpAddPostFile("FileContent", "c:\\new2.doc");
	 var  returnValue = aip.HttpPost(path_ + "/common/dj/uploadFile.action?id=template");    // 判断上传是否成功
	 aip.DeleteLocalFile(tempfile);*/
     // 添加相应的Post元素    
    // webObj.HttpAddPostString("fname", "123");   
    //	webObj.HttpAddPostString("oldFileName","");   
     //var a = webObj.SaveTo("c:\\webOffice.doc");
    // alert(a);
	//aip.GetOriginalFileName(0);
	//alert(aip.GetDocText());
	//aip.LoadOriginalFile(tempfile,'doc');
	//aip.ShowAllNotes();
	//aip.ConvertToAip(1,0);
}

/**
 * 将文件保存到服务器
 * 返回文件文件属性
 * 文件的服务器全路径
 * 文件名称
 * 文件大小 例如 2m
 */
function saveAIPFileToService_() {
	//aip.CurrAction=2568;
	//aip.ShowView = 128;
	//alert(Aip.setting_.isShowAlert);
	if(Aip.setting_.isShowAlert) {
		msgBox.confirm({
			content: Aip.setting_.alertMsg,
			success: function(){
				saveToService_();
			}
		});
	} else {
		saveToService_();
	}
	/*msgBox.confirm({
		content: $.i18n.prop("JC_SYS_066"),
		success: function(){
			
		}
	});*/
	
}

function saveToService_(){
	 try{ 
		 var aip = Aip.getAip();
		 if(Aip.setting_.state == 1) {
			    if("aip" == Aip.setting_.type && Aip.setting_.isFile) {
			    	
			    } else {
			    	aip.ShowView = 32;
			    	var tempfile = aip.GetTempFileName('');
			    	aip.GetOriginalFile(tempfile, -1);
			    	aip.LoadOriginalFile(tempfile, 'doc');
			    }
	        }
		 var docText = aip.GetDocText();
		 var  returnValue = null;
		 if(docText) {
			 aip.HttpInit();          //初始化Http引擎   
			 aip.HttpAddPostString("Company","Dianju");//通过控件post字符串参数
			 aip.HttpAddPostCurrFile("file");
			 //var a = webObj.SaveTo("c:\\webOffice.doc");
			//var result = aip.HttpPost("http://172.16.3.20:8080/goa/common/dj/saveAip.action");
			 var param = '/common/dj/uploadFile.action';
			 if(Aip.setting_.isTemplate) {
				 param = '/common/dj/uploadFile.action?template=1';
			 }  
			 returnValue = aip.HttpPost(path_ + param);    // 判断上传是否成功 
			 
			 if(Aip.setting_.callback) {
				 //var b = aip.GetCurrFileBase64();
				 //Aip.setting_.callback({fileContent:b});
				 if(returnValue) {
					 //返回值包括
					 //fileName
					 //filePath
					 //fileSize
					 var o = JSON.parse(returnValue);
					 Aip.setting_.callback(o);
				 } else {
					 Aip.setting_.callback(null);
				 }
			 }
		 } else{
			 if(Aip.setting_.callback) {
				 Aip.setting_.callback(null);
			 }
		 }
	      //  alert("==="+ returnValue);
	     /*   */
	    }catch(e){
	        //alert("异常\r\nError:"+e+"\r\nError Code:"+e.number+"\r\nError Des:"+e.description);   
	    } 
}
function removeViewAIP_() {
	var aip = Aip.getAip();
	if(aip.CurrAction==16) {
		aip.CurrAction=1;
	} else {
		aip.CurrAction=16;
	}
	
}

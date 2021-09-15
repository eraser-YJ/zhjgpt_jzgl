/**
 *  @description 手写相关js
 */

var handWritten = {}
handWritten.signId = "";
/**
 * 初始化函数
 */
handWritten.init = function(){
	var s = ""
	s += "<object id=DWebSignSeal classid='CLSID:77709A87-71F9-41AE-904F-886976F99E3E' style='position:absolute;width:0px;height:0px;left:0px;top:0px;' codebase='"+getRootPath()+"/plugin/WebSign.dll#version=4,5,5,0' >"
	s += "</OBJECT>";
	//document.write(s);
}

handWritten.getSignTable = function(divId, id){
	var html = "<table width='60%' border=1 height='100%' id='sign_table_"+divId+"'><tr><td style='width:100%;'>"+
			"<object name='"+id+"' id='"+id+"' style='display:none;' codebase='"+getRootPath()+"/plugin/iWebRevision.ocx#version=6,7,0,376' classid='clsid:2294689C-9EDF-40BC-86AE-0438112CA439' width='100%' height='100%'>"+
			" <param name='SignatureType' value='0'>"+
			" <param name='BorderStyle' value='0'>"+
			"</object>"+
			"</td></tr></table>";
	return html;
}
var WebRevision = {
	getWebRevision:function(divId) {
		var id = (new Date()).getTime().toString();
		if(document.getElementById("suggestType").value == 1){
			var str = '';
			str += "<OBJECT id='"+id+"' width='100%' height='100%' style='display:none' classid='clsid:2294689C-9EDF-40BC-86AE-0438112CA439' codebase='"+getRootPath()+"/plugin/iWebRevision.ocx#version=6,7,0,376' >"
			str += "</OBJECT>";
		 	//$('#'+divId).append(str);
			//$(document).append(str);
		 	//$('#'+divId).append(handWritten.getSignTable(divId, id));

		} else {
			//handWritten.objectId = id;
			var newSign = document.createElement('object');
			newSign.id=id;
			newSign.style.width = '100%';
			newSign.style.height = '100%';
			newSign.classid = 'clsid:2294689C-9EDF-40BC-86AE-0438112CA439';
			newSign.codebase=getRootPath()+"/plugin/iWebRevision.ocx#version=6,7,0,376' >";
			//$('#'+handWritten.objectId).remove();
			//document.getElementById(divId).appendChild(newSign);
			/*if(!handWritten.objectId) {
			 document.getElementById(divId).appendChild(newSign);
			 } else {
			 id=handWritten.objectId;
			 var webObj = document.getElementById(id);
			 webObj.Clear();
			 }*/
		}

		var divObj = document.getElementById(divId);
		var r = document.getElementById('sign_'+divId);
	//	divObj.insertAdjacentElement("beforeBegin",r);
		//var signTable = document.getElementById("sign_table_"+divId);
		//divObj.insertAdjacentElement("beforeBegin",signTable);
		//var signTable = document.getElementById("sign_table_"+divId);
		r.style.display = 'block';
		r.WebUrl=getRootPath()+"/workflow/suggestWebRevision/ExecuteRun.action";//"<%=mServerUrl%>";
		//r.WebUrl = "http://localhost:8080/iWebRevision.jsp.mysql/iWebServer.jsp";
		r.RecordID = $('#workId').val();
		r.FieldName = divId;
		r.UserName = 'admin';
		r.Enabled = "1";                       //Enabled:是否允许修改，0:不允许 1:允许  默认值:1
		r.PenColor = "#FF0066";
		r.BorderStyle = "0";                    //BorderStyle:边框，0:无边框 1:有边框  默认值:1
		r.EditType = "0";                       //EditType:默认签章类型，0:签名 1:文字  默认值:0
		r.ShowPage = "0";                       //ShowPage:设置默认显示页面，0:电子印章,1:网页签批,2:文字批注  默认值:0
		r.InputText = "";                       //InputText:设置署名信息，  为空字符串则默认信息[用户名+时间]内容
		r.PenWidth = "3";                       //PenWidth:笔的宽度，值:1 2 3 4 5   默认值:2
		r.FontSize = "14";                      //FontSize:文字大小，默认值:11
		r.SignatureType = "0";                  //SignatureType:签章来源类型，0表示从服务器数据库中读取签章，1表示从硬件密钥盘中读取签章，2表示从本地读取签章，并与ImageName(本地签章路径)属性相结合使用  默认值:0}
		//控制控件是否支持KUPA平板电脑，默认值为“FALSE”，不支持；“TRUE”，支持。
		r.SetFieldByName("1","TRUE");
		r.AppImgZoomParam = 1;//按款缩放   2 按高缩放
		//控制文本框光标自动定位的功能，当Value值为”FALSE”，则关闭该功能，默认值为“TRUE”，开启该功能。
		r.SetFieldByName("CURSORAUTOLOCATION"," FALSE");

		//当该参数设置为’TRUE’时，表示启用自动保存用户在“签名盖章”窗口中设置的笔宽、颜色和缩放比例的大小及字体名称、大小
		// 、颜色，以便下次重新打开该窗口时，自动读取上次的设置信息。如果设置其它值，或不进行设置的话，则不启用。
		r.SetFieldByName('AutoSaveParams', 'TRUE');



		/*
		 *
		 *
		 *  DivID.insertAdjacentElement("beforeBegin",DocForm.Consult);
		 DocForm.Consult.WebUrl = "http://www.kinggrid.com:8080/iWebRevision/iWebServer.jsp";           //WebUrl:系统服务器路径，与服务器交互操作，如打开签章信息
		 DocForm.Consult.RecordID = "1487145923301";           //RecordID:本文档记录编号
		 DocForm.Consult.FieldName = "Consult";                //FieldName:签章窗体可以根据实际情况再增加，只需要修改控件属性 FieldName 的值就可以
		 DocForm.Consult.UserName = "演示人";           //UserName:签名用户名称
		 DocForm.Consult.Enabled = "1";     //Enabled:是否允许修改，0:不允许 1:允许  默认值:1
		 DocForm.Consult.PenColor = "#FF0066";                 //PenColor:笔的颜色，采用网页色彩值  默认值:#000000
		 DocForm.Consult.BorderStyle = "0";                    //BorderStyle:边框，0:无边框 1:有边框  默认值:1
		 DocForm.Consult.EditType = "0";                       //EditType:默认签章类型，0:签名 1:文字  默认值:0
		 DocForm.Consult.ShowPage = "0";                       //ShowPage:设置默认显示页面，0:电子印章,1:网页签批,2:文字批注  默认值:0
		 DocForm.Consult.InputText = "";                       //InputText:设置署名信息，  为空字符串则默认信息[用户名+时间]内容
		 DocForm.Consult.PenWidth = "3";                       //PenWidth:笔的宽度，值:1 2 3 4 5   默认值:2
		 DocForm.Consult.FontSize = "14";                      //FontSize:文字大小，默认值:11
		 DocForm.Consult.SignatureType = "0";                  //SignatureType:签章来源类型，0表示从服务器数据库中读取签章，1表示从硬件密钥盘中读取签章，2表示从本地读取签章，并与ImageName(本地签章路径)属性相结合使用  默认值:0}
		 DocForm.Consult.InputList = "同意\r\n不同意\r\n请上级批示\r\n已阅"; //InputList:设置文字批注信息列表
		 DocForm.Consult.Width = "100%";
		 DocForm.Consult.Height = "100%";
		 DocForm.Consult.style.display="block";
		 */
		handWritten.signObject = r;
		return r;
		//return document.getElementById('SendOut');
	},
	addHandWrite:function(divId){

	}

};
handWritten.init();
handWritten.signObject = null;
handWritten.objectId = '';
handWritten.newObjectDivId = '';
handWritten.signDataLength = 0;
handWritten.signData = null;
handWritten.screenHeight = window.screen.height;
handWritten.relativeLength = 2800;
handWritten.clearSignData = function() {
	if(handWritten.signObject) {
		handWritten.signObject.ClearAll();
	}
}
handWritten.initEditSuggestSignObject = function() {
	var objects = $("div[signContainer='true']");

	if(objects && objects.length > 0) {
		handWritten.objectId = "sign_"+objects[0].id;
		var divId = objects[0].id;
		$('#signIds').val(divId);
		handWritten.signObject = document.getElementById(handWritten.objectId);//objects[0];
		handWritten.signObject.WebUrl=getRootPath()+"/workflow/suggestWebRevision/ExecuteRun.action";//"<%=mServerUrl%>";
		//r.WebUrl = "http://localhost:8080/iWebRevision.jsp.mysql/iWebServer.jsp";
		handWritten.signObject.RecordID = $('#workId').val();
		handWritten.signObject.FieldName = divId;
		handWritten.signObject.UserName = 'admin';
		handWritten.signObject.Enabled = "1";                       //Enabled:是否允许修改，0:不允许 1:允许  默认值:1
		handWritten.signObject.PenColor = "#FF0066";
		handWritten.signObject.BorderStyle = "0";                    //BorderStyle:边框，0:无边框 1:有边框  默认值:1
		handWritten.signObject.EditType = "0";                       //EditType:默认签章类型，0:签名 1:文字  默认值:0
		handWritten.signObject.ShowPage = "0";                       //ShowPage:设置默认显示页面，0:电子印章,1:网页签批,2:文字批注  默认值:0
		handWritten.signObject.InputText = "";                       //InputText:设置署名信息，  为空字符串则默认信息[用户名+时间]内容
		handWritten.signObject.PenWidth = "3";                       //PenWidth:笔的宽度，值:1 2 3 4 5   默认值:2
		handWritten.signObject.FontSize = "14";                      //FontSize:文字大小，默认值:11
		handWritten.signObject.SignatureType = "0";                  //SignatureType:签章来源类型，0表示从服务器数据库中读取签章，1表示从硬件密钥盘中读取签章，2表示从本地读取签章，并与ImageName(本地签章路径)属性相结合使用  默认值:0}
		//控制控件是否支持KUPA平板电脑，默认值为“FALSE”，不支持；“TRUE”，支持。
		handWritten.signObject.SetFieldByName("1","TRUE");
		handWritten.signObject.AppImgZoomParam = 1;//按款缩放   2 按高缩放
		//控制文本框光标自动定位的功能，当Value值为”FALSE”，则关闭该功能，默认值为“TRUE”，开启该功能。
		handWritten.signObject.SetFieldByName("CURSORAUTOLOCATION"," FALSE");

		//当该参数设置为’TRUE’时，表示启用自动保存用户在“签名盖章”窗口中设置的笔宽、颜色和缩放比例的大小及字体名称、大小
		// 、颜色，以便下次重新打开该窗口时，自动读取上次的设置信息。如果设置其它值，或不进行设置的话，则不启用。
		handWritten.signObject.SetFieldByName('AutoSaveParams', 'TRUE');

	}
}
handWritten.checkSignData = function() {
	var existText = true;
	var existSign = true;
	var textObj = $('textarea[id*="Textarea"]');
	if(textObj && textObj.length > 0){
		var textValue = textObj.val();
		if(!textValue) {
			existText = false;
		}
		var webRion = handWritten.signObject;//document.getElementById(handWritten.objectId);// .getWebRevision();
		if(!webRion) {
			existSign = false;
		} else {
			var isEmpty = webRion.DocEmpty;
			if(isEmpty){
				existSign = false;
			}
		}
	}

	return existSign || existText;
}
handWritten.showWritePannel_JG = function(divId) {
	//handWritten.WebSign_AddSeal_JG(divId);
	if(handWritten.objectId) {
		//$('#'+handWritten.objectId).remove();
		//handWritten.objectId = '';
	}
	if(handWritten.newObjectDivId) {
		//	$('#'+handWritten.newObjectDivId).remove();
		//$('#'+handWritten.objectId).remove();
		//$('#handWritten.newObjectDivId').css('height','260px');

	}

	if(handWritten.screenHeight > 768) {
		handWritten.relativeLength = 2800;
	}else if(handWritten.screenHeight <= 768) {
		handWritten.relativeLength = 2550;
	}
	/*InputText

	 FieldName

	 DocEmpty
	 Value
	 Clear()*/
	handWritten.newObjectDivId = divId;
	var webRion = WebRevision.getWebRevision(divId);
	webRion.style.display = 'block';
	handWritten.signData = webRion.Value;
	webRion.ClearAll();
	var divObj = document.getElementById(divId);
	//webRion.style.width = divObj.parentNode.parentNode.style.width;
	var divPH = $(divObj.parentNode.parentNode).attr('divHeight');
	if(!divPH) {
		divPH = 260;//默认高度300
	}
	//webRion.style.height = divPH+"px";
	webRion.SetFieldByName('Flag', '2');
	if(document.getElementById("suggestType").value == 1){
		//var index = divPH.indexOf("px");
		//var divH = divPH.substring(0,index);

		var flag =webRion.ShowZoomInHandWrite();
		$('#signIds').val(divId);
		var result = webRion.WebGetMsgByName("STATUS");   //如果交互成功，接受服务器端返回的信息。
		var signValue = webRion.Value;

		var isEmpty = webRion.DocEmpty;
		//alert(webRion.ImgHeight);
		//webR
		//var signvaluelength = signValue.length;
		//handWritten.signDataLength = signvaluelength;
		if(isEmpty) {
			//$(webRion).remove();
			//$('#handWritten.newObjectDivId').css('height','260px');
			if(handWritten.signData) {
				webRion.Value = handWritten.signData;
			}
		} else {
			handWritten.signData = signValue;
			if(handWritten.objectId) {
				//$('#'+handWritten.objectId).remove();
				//handWritten.objectId = '';
			}
			handWritten.objectId = webRion.id;
		}
	} else {
		webRion.SignatureType = '0';
		var signResult = webRion.OpenMiniSignature();
		if(signResult) {
			/*if(handWritten.objectId) {
			 $('#'+handWritten.objectId).remove();
			 }*/
			$('#signIds').val(divId);
			handWritten.objectId = webRion.id;
			handWritten.signData = signValue;
		} else {
			handWritten.objectId = '';
			if(handWritten.signData) {
				webRion.Value = handWritten.signData;
			}
			//$(webRion).remove();
		}
		//webRion.InvisiblePages('1,2');
	}
	//webRion.EditType = 0;
}
handWritten.initWebRevision = function() {
	// webRion.LoadSignature();
	$("[workflowSuggest='true']").each(function(){
		var suggestDiv = $(this);
		//signContainer
		if($(this).find("div").length>0){
			var signDivs = $(this).find("[signId]");
			var containerId = '';//$(this).find("[signid]").attr("id");
			$(signDivs).each(function(){
				var containerId = $(this).attr("id");
				if(containerId) {
					var webRion = WebRevision.getWebRevision(containerId);
					var divObj = document.getElementById(containerId);
					webRion.Enabled = '0';
					webRion.ShowPage = '1';
					//webRion.EditType = '0';
					//webRion.SignatureType = '0';
					//divObj.insertAdjacentElement("beforeBegin",webRion);
					//webRion.EditType = 0;
					webRion.style.width = $(this).css('width');
					var divPH = $(this).attr('divHeight');
					if(!divPH) {
						divPH = 300;//默认高度300
					}
					//var index = divPH.indexOf("px");
					//var divH = divPH.substring(0,index);
					//webRion.style.height = divPH+"px";
					webRion.SetFieldByName('Flag', '2');
					webRion.LoadSignature();
				///	alert(webRion.ImgHeight);
					webRion.style.display = 'block';
					var result = webRion.WebGetMsgByName("STATUS");   //如果交互成功，接受服务器端返回的信息。
					if(result == "-1") {
						$(webRion).remove();
					}
				}
			});

			if($(this).find("textarea").length>0){
				//初始化常用词
				var textareaId = $(this).find("textarea").attr("id");
				var containerId = $(this).find("[id^=phrase]").attr("id");
				phraseComponent.init({
					containerId:containerId,
					fillEleId:textareaId
				});
			}
		}
		/*$(this).find(">div").each(function(index,item){
		 suggestToHeight[suggestToHeight.length]={
		 divId:$(item).attr("id"),
		 signId:$(item).attr("signId")
		 }
		 });*/
	});
}
handWritten.saveWebRevision = function() {
	var webRion = handWritten.signObject;//document.getElementById(handWritten.objectId);//WebRevision.getWebRevision();
	if(webRion && webRion.Modify) {
		webRion.SaveSignature();
	}
}


handWritten.webRevision = function(divId) {
	var webRion = WebRevision.getWebRevision();
	var div = document.getElementById(divId);
	div.insertAdjacentElement("beforeBegin",webRion);
	//webRion.ShowZoomInHandWrite();
}
/**
 * 弹出手写窗口
 */
handWritten.showWritePannel = function(divId){
	var signName = "";

	try{
		//设置当前印章绑定的表单域
		handWritten.Enc_onclick();

		if(document.getElementById("suggestType").value == 1){
			document.all.DWebSignSeal.SetCurrUser("手写人");
			document.all.DWebSignSeal.SetPosition(0,0,divId);
			var strObjectName =$("#signId").val()
			signName = document.all.DWebSignSeal.HandWritePop(0,0,50, handWritten.getWidth(),handWritten.getHeight(),"");
			if(signName.length==0){
				return;
			}
			if(strObjectName!=null&&strObjectName.length>0){
				document.all.DWebSignSeal.DelSeal(strObjectName);
			}
			//锁定位置
			document.DWebSignSeal.LockSealPosition(signName);
			var width = document.DWebSignSeal.GetSealWidth(signName);
			var height = document.DWebSignSeal.GetSealHeight(signName);
			document.DWebSignSeal.MoveSealPosition(signName,20,0,divId);
			//更改div高度
			document.getElementById(divId).style.height = height+80;
			document.all.DWebSignSeal.ShowWebSeals();

			$("#"+divId).find("textarea").css("height",height);
		}else{
			document.all.DWebSignSeal.SetCurrUser("盖章人");
			document.all.DWebSignSeal.RemoteID = "0100018";
			document.all.DWebSignSeal.SetPosition(0,0,divId);
			var strObjectName =$("#signId").val();
			signName = document.all.DWebSignSeal.AddSeal("", "");
			if(signName.length==0){
				return;
			}
			if(strObjectName!=null&&strObjectName.length>0){
				document.all.DWebSignSeal.DelSeal(strObjectName);
			}
			//锁定位置
			document.DWebSignSeal.LockSealPosition(signName);
			var width = document.DWebSignSeal.GetSealWidth(signName);
			var height = document.DWebSignSeal.GetSealHeight(signName);
			document.DWebSignSeal.MoveSealPosition(signName,(document.getElementById(divId).clientWidth - (width + 120)),0,"divId");
			//更改div高度
			document.getElementById(divId).style.height = height+80;
			document.all.DWebSignSeal.ShowWebSeals();

			$("#"+divId).find("textarea").css("height",height);
		}
		if("" == signName){
			//alert("全屏幕签名失败");
			return false;
		}
		$("[id^=DDWSign]").css("z-index",1)
		//设置标志位
		$("#signInfoFlag").val("true");
		return signName;
	}catch(e) {
		alert("控件没有安装，请刷新本页面，控件会自动下载。\r\n或者下载安装程序安装。" +e);
	}
}

/**\
 *
 *金格插件 页面盖章
 */
handWritten.WebSign_AddSeal_JG = function(name) {
	var signatureControl = document.getElementById('SignatureControl');
	signatureControl.ServiceUrl = $('#mServerUrl').val()+getRootPath()+"/workflow/suggestjg/ExecuteRun.action?workId="+$('#workId').val();
	signatureControl.UserName="demo";                         //文件版签章用户
	//alert(name);
	//name = name+"text";
	var workId = $('#workId').val();
	signatureControl.FieldsList = "myBusinessUrl="+workId;
	signatureControl.AutoSave = true;
	if(document.getElementById("suggestType").value == 1){
		//手写
		var index = name.lastIndexOf("_");
		var divId = name.substring(0, index) + "Textarea"+name.substring(index+1);
		//	alert(divId);
		// signatureControl.DivId = divId;
		//signatureControl.Position(0,0);
		signatureControl.HandPenWidth = 1;                        //设置、读取手写签名的笔宽
		signatureControl.HandPenColor = 100;                      //设置、读取手写签名笔颜色
		signatureControl.SetPositionRelativeTag(name,1);        //设置签章位置是相对于哪个标记的什么位置
		//signatureControl.DivId = name;        //设置签章位置是相对于哪个标记的什么位置
		//signatureControl.PositionByTagType = 1;                 //设置签章所处位置，1表示中间
		//signatureControl.PositionBySignType = 2;                 //设置签章所处位置，1表示中间
		//DocForm.signatureControl.UserName="lyj";                        //文件版签章用户
		var result = signatureControl.RunHandWrite(true);                          //执行手写签名
		if(result) {
			var sId = signatureControl.SIGNATUREID;
			var signs = document.getElementsByName("iHtmlSignature");
			var vItem = signs[signs.length - 1];
			var difPosition = vItem.ScalingSign("","","0.7");
			var difWidth = difPosition.substring(0,difPosition.indexOf(";"));
			var difHeight = difPosition.substring(difPosition.indexOf(";") + 1,difPosition.length);
			vItem.MovePositionByNoSave(difWidth/2,difHeight/2);
			var defaultHeight = document.getElementById(name).style.height;
			if(difHeight < 40) {
				difHeight = 40;
			}
			if(defaultHeight) {
				defaultHeight = +defaultHeight;
				if(defaultHeight > difHeight) {
					difHeight = defaultHeight/2.5;
				}
			}
			document.getElementById(name).style.height = difHeight*2.5;
			$("#"+name).find("textarea").css("height",difHeight*2.5);
		}
	} else {
		//盖章
		//   SignatureControl.DocumentID = $('#workId').val()//签章位置
		signatureControl.SaveHistory="False";                    //是否自动保存历史记录,true保存  false不保存  默认值false
		signatureControl.DivId = name;        //设置签章位置是相对于哪个标记的什么位置
		signatureControl.SetPositionRelativeTag(name,1);         //设置签章位置是相对于哪个标记的什么位置
		//SignatureControl.PositionByTagType = 2;                 //设置签章所处位置，1表示中间
		//SignatureControl.PositionBySignType = 2;                 //设置签章所处位置，1表示中间
		// SignatureControl.ValidateCertTime = '1';                 //检测数字证书有效性，安装目前下必须有根证书Root.cer和吊销列表Crl.crl
		//signatureControl.RunHandWrite();
		// signatureControl.AutoSave = false;
		signatureControl.ShowSignatureWindow = "0";
		//signatureControl.Position(-100,100);
		signatureControl.PassWord = "123456";
		var i = signatureControl.RunSignature(true);
		//signatureControl.EnableMove = false;
	}
	$('#signInfo').val(name);
	var signs = document.getElementsByName("iHtmlSignature");
	for(var i = 0; i < signs.length; i++) {
		//var node = signs[i].cloneNode(true);
		$(signs[i]).css('z-index',1000);
		$(signs[i]).appendTo("#"+name);
	}
	signs = document.getElementsByName("iHtmlSignature");
	var lll = signs.length;
}
function WebSign_AddSeal(sealName, sealPostion,signData){
	try{
		//是否已经盖章
		var strObjectName ;
		strObjectName = DWebSignSeal.FindSeal("",0);
		while(strObjectName  != ""){
			if(sealName == strObjectName){
				alert("当前页面已经加盖过印章：【"+sealName+"】请核实");
				return false;
			}
			strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
		}

		//设置当前印章绑定的表单域
		Enc_onclick(signData);
		//设置盖章人，可以是OA的用户名
		document.all.DWebSignSeal.SetCurrUser("盖章人");
		//document.all.DWebSignSeal.SealSetID = "001";
		//公章否？ 1:公章 0：私章
		//document.all.DWebSignSeal.CurrSealType = 0;
		//网络版的设置服务器路径地址 如果不设置，系统会自动到注册表中读取。
		//document.all.DWebSignSeal.HttpAddress = "127.0.0.1:1127";
		//document.all.DWebSignSeal.HttpAddress = "http://192.168.1.11:80/inc/seal_interface/";
		//网络版的唯一页面ID ，SessionID
		document.all.DWebSignSeal.RemoteID = "0100018";
		//附加信息
		//document.all.DWebSignSeal.AppendTipInfo = "IP:192.168.0.11\r\nMAC:00-00-00-11-02";
		//设置盖章时间，可以有服务器传过来
		//document.all.SetCurrTime("2006-02-07 11:11:11");
		//设置当前印章的位置,相对于sealPostion1 (<div id="sealPostion1"> </div> 也可以是 td) 的位置相左偏移50px,向上偏移50px
		//这样就可以很好的固定印章的位置
		//document.all.DWebSignSeal.SetPosition(50,-50,"sealPostion1");
		document.all.DWebSignSeal.SetPosition(0,0,sealPostion);
		//调用盖章的接口
		if("" == document.all.DWebSignSeal.AddSeal("", sealName)){
			alert("盖章失败");
			return false;
		}
	}catch(e) {
		alert("控件没有安装，请刷新本页面，控件会自动下载。\r\n或者下载安装程序安装。" +e);
	}
}

/**
 * 获取意见域对应值
 */
handWritten.aa = function(){
}

/***********************************************
 说明：
 Enc_onclick 主要设置绑定的表单域。
 WebSign的SetSignData接口支持两种绑定数据方式：
 1.字符串数据
 2.表单域
 一旦数据发生改变，WebSign会自动校验，并提示修改。
 ***********************************************/
handWritten.Enc_onclick = function(tex_name) {
	try{
		//清空原绑定内容
		document.all.DWebSignSeal.SetSignData("-");
		// str为待绑定的字符串数据
		//var str = "";
		//设置绑定的表单域
		//来文单位
		document.all.DWebSignSeal.SetSignData("+LIST:laiwendanwei;");
		//来文日期
		document.all.DWebSignSeal.SetSignData("+LIST:laiwenDate;");
		//事由
		document.all.DWebSignSeal.SetSignData("+LIST:shiyou;");
		//时间要求
		document.all.DWebSignSeal.SetSignData("+LIST:time;");
		//意见
		document.all.DWebSignSeal.SetSignData("+LIST:"+tex_name+";");

		/*根据表单域内容自己组织绑定内容,当前例子仅仅做与表单域绑定
		 如果绑定字符串数据,需要做如下调用
		 document.all.DWebSignSeal.SetSignData("+DATA:"+str);
		 */
	}catch(e) {
		alert("控件没有安装，请刷新本页面，控件会自动下载。\r\n或者下载安装程序安装。" +e);
	}
}

handWritten.getWidth = function(){
	return document.documentElement.clientWidth *0.8 > 800 ? 800 : document.documentElement.clientWidth *0.8;
}
handWritten.getHeight = function(){
	return document.documentElement.clientHeight*0.8 > 800 ? 800 : document.documentElement.clientHeight *0.8;
}

/**
 * 获得签章接口
 */
handWritten.getSuggestWrite = function(workId){
	var url = getRootPath()+"/workFlow/form/getSuggestWrite.action";
	var ajaxData = {
		workId:workId
	}
	$.ajax({
		url:url,
		data:ajaxData,
		async:false,
		success:function(data){
			if(!isChrome()&&!isFF()&&document.all.DWebSignSeal!=null){
				var signInfo = data.signInfo;
				if(signInfo!=null&&signInfo.length>0){
					document.all.DWebSignSeal.SetStoreData(signInfo);
					document.all.DWebSignSeal.ShowWebSeals();
					$("[workflowSuggest=true]>div").each(function(index,item){
						var objItem = $(this);
						var signId = objItem.attr("signId");
						var px = document.all.DWebSignSeal.GetSealPosX (signId);
						var py = document.all.DWebSignSeal.GetSealPosY (signId)
						var swidth = document.all.DWebSignSeal.GetSealWidth (signId);
						var divWidth = objItem.actual("width");
						if(px+swidth>divWidth){
							document.all.DWebSignSeal.MoveSealPosition(signId,divWidth-swidth,py,objItem.attr("id"));
						}
					});
					//document.all.DWebSignSeal.ShowWebSeals();
				}
			}
		}
	});
}

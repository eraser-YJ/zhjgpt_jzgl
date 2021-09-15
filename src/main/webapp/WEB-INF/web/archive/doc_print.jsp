<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html id="content">
<head>
<title>表单打印</title>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<meta name="renderer" content="ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="edge" />
	<link href="${sysPath}/css/font-face.css" rel="stylesheet" type="text/css"/>
	<link href="${sysPath}/css/classics/jcap.css?v=7b89ce1005" rel="stylesheet" type="text/css"/>
<%--<c:choose>--%>
	<%--<c:when test="${sessionScope.theme=='classics'}">--%>
		<%--<link href="${sysPath}/css/${sessionScope.theme}/jcap.css?v=7b89ce1005" rel="stylesheet" type="text/css"/>--%>
	<%--</c:when>--%>
	<%--<c:otherwise>--%>
		<%--<link href="${sysPath}/css/${sessionScope.theme}/${sessionScope.font}/${sessionScope.color}/jcap.css?v=7b89ce1005" rel="stylesheet" type="text/css"/>--%>
	<%--</c:otherwise>--%>
<%--</c:choose>--%>
	<link rel="shortcut icon" href="${sysPath}/favicon.ico" type="image/x-icon" />
	<script type="text/javascript" src='${sysPath}/js/jquery.min.js?v=517bc0d319'></script>
<%--<script type="text/javascript" src='${sysPath}/js/lib/jquery/jquery-1.10.2.js'></script>--%>
<script type="text/javascript" src='${sysPath}/js/jctree/select2.all.js'></script>
<script type='text/javascript' src='${sysPath}/js/lib/common/common.all.js'></script>
<%--<script type='text/javascript' src='${sysPath}/js/lib/common/jquery.plugin.js'></script>--%>
	<script type="text/javascript" src="${sysPath}/js/app.v2.js"></script>
<script type="text/javascript">setRootPath('${sysPath}');</script>

<script src="${sysPath}/js/com/signature/handwritten.js" type="text/javascript"></script>
<style type="text/css" media=print>
.noprint{display : none }
</style>
</head>
<body>

<%-- <%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/base.jsp"%>
<script src="${sysPath}/js/com/signature/handwritten.js" type="text/javascript"></script>

<style type="text/css" media=print>
.noprint{display : none }
</style>--%>
<!--start 表格-->
<div class="scrollable padder" id="scrollable" >
	<div class="panel m-t-md clearfix">
		<%--<%@ include file="/plugin/websign.jsp"%>--%>
	   	<div class="table-wrap m-20-auto" style="width:720px" id="sendSimulationForm">
	    </div>
		<div class="form-btn m-l m-b">
			<a id="print_btn" class="btn dark noprint" type="button" onclick="docFormPrint()">打 印</a>
			<!-- <a class="btn" type="button" onclick="docFormClose()">关 闭</a> -->
		</div>
	</div>
</div >
<!--end 表格-->

<script type="text/javascript">
if($("#formContent", window.opener.document).val() == ""){
	//document.getElementById('sendSimulationForm').innerHTML = $("#sendSimulationForm", window.opener.document).html();
	//$("#sendSimulationForm").html($("#sendSimulationForm", window.opener.document).html());
}else{
	//document.getElementById('sendSimulationForm').innerHTML = $("#formContent", window.opener.document).val();
	//$("#sendSimulationForm").html($("#formContent", window.opener.document).val());
}
//document.getElementById('sendSimulationForm').innerHTML = $('#formContentBody').val();//$("#formContentBody", window.opener.document).val();
</script>
<script src="${sysPath}/js/com/archive/docFormView.js" type="text/javascript"></script>
<script type="text/javascript">
/*$("#sendSimulationForm").find("[workFlowForm]").each(function(){
	var item = $(this);
	var itemType = $(this).attr("workFlowForm");
	//对于浏览模式,所有组件都为只读状态
	docFromView.type[itemType].read(item);
});
$("#sendSimulationForm").find("div").each(function(){
	var item = $(this);

	if(item.hasClass('signature-box')) {
		item.removeClass('signature-box');
	}
});*/
var subContent = "";
/*if(Number($("#sendSimulationForm").html().indexOf('id="workId"'))!=-1){
	subContent = $("#sendSimulationForm").html().substr(Number($("#sendSimulationForm").html().indexOf('id="workId"')),$("#sendSimulationForm").html().length);
}else if(Number($("#sendSimulationForm").html().indexOf('id=workId'))!=-1){
	subContent = $("#sendSimulationForm").html().substr(Number($("#sendSimulationForm").html().indexOf('id=workId')),$("#sendSimulationForm").html().length);
}else if(Number($("#sendSimulationForm").html().indexOf("id='workId'"))!=-1){
	subContent = $("#sendSimulationForm").html().substr(Number($("#sendSimulationForm").html().indexOf("id='workId'")),$("#sendSimulationForm").html().length);
}else{
	subContent = $("#sendSimulationForm").html();
}*/

/*var objects = $('Object');
if(objects) {
	objects.each(function(index,item){
		var owidthStr = $(item).css('width');
		var sign = document.getElementById(item.id);
		if(sign) {
			//暂时无用
			sign.SetFieldByName("Flag","3");
			sign.SetFieldByName("Scale","30");
		}
		var owidthStrLength = owidthStr.length;
		var owidth = owidthStr.substring(0,owidthStrLength-2)
		owidth = +owidth;
		owidth = owidth*0.8;
		$(item).css('width',owidth+"px");
		//$(item).css('margin-left','-70px');
	});
}*/
//var workId = subContent.substr(Number(subContent.indexOf("HZ")),32);
//if(workId){
	//金格
/*	var objects = $('Object');
	if(objects) {
		objects.each(function(index,item){
			var owidthStr = $(item).css('width');
			var owidthStrLength = owidthStr.length;
			var owidth = owidthStr.substring(0,owidthStrLength-2)
			owidth = +owidth;
			owidth = owidth*0.85;
			$(item).css('width',owidth+"px");
		});
	}*/

///点聚
	/*handWritten.getSuggestWrite(workId);

	//处理签章失效
	var strObjectName = document.all.DWebSignSeal.FindSeal("",0);
	while(strObjectName){
		document.all.DWebSignSeal.SetDocAutoVerify(strObjectName,0);
		strObjectName = document.all.DWebSignSeal.FindSeal(strObjectName,0);
	};*/
//}

function docFormPrint() {
	window.print();
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
function isChrome() {
	return navigator.userAgent.indexOf("Chrome") > -1;
}
$(document).ready(function(){
	if($("#formContent", window.opener.document).val() == ""){
		document.getElementById('sendSimulationForm').innerHTML = $("#sendSimulationForm", window.opener.document).html();
		//$("#sendSimulationForm").html($("#sendSimulationForm", window.opener.document).html());
	}else{
		document.getElementById('sendSimulationForm').innerHTML = $("#formContent", window.opener.document).val();
		//$("#sendSimulationForm").html($("#formContent", window.opener.document).val());
	}
	$("#sendSimulationForm").find("[workFlowForm]").each(function(){
		var item = $(this);
		var itemType = $(this).attr("workFlowForm");
		//对于浏览模式,所有组件都为只读状态
		docFromView.type[itemType].read(item);
	});
	$("#sendSimulationForm").find("div").each(function(){
		var item = $(this);

		if(item.hasClass('signature-box')) {
			item.removeClass('signature-box');
		}
	});
	var objects = $('object');
	if(objects) {
		objects.each(function(index,item){
			var owidthStr = $(item).css('width');
			//item.style.width =item.parentNode.style.width;
			//item.style.height ='220px';//item.parentNode.style.height;
			//item.parentNode.parentNode.parentNode.parentNode.style.width='100%';
			//alert(item.parentNode.style.width);
			//item.Visible = '1';
			item.Enabled = '0';
			var sign = document.getElementById(item.id);
			//sign.SignatureSize = "0";
			if(sign) {
				sign.AppImgZoomParam = 1;
				//暂时无用
			}
		});
	}
});
/* $(function(){
    $("#print_btn").click(function(){
    	//alert($("#sendSimulationForm").html());
        //$("#sendSimulationForm").printArea();
        //$("#sendSimulationForm").jqprint();
    	window.print();

    });
}); */

/* function docFormClose() {
	msgBox.confirm({
		content: $.i18n.prop("JC_SYS_030"),
		success: function(){
			window.close();
			//document.write("<script>window.close();<\/script>");
			//WebBrowser.ExecWB(45,1);
	}});
} */

</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
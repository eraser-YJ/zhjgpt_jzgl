<%@ page language="java" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%
String base = request.getContextPath(); 
  String IPAddress = null;
  IPAddress = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
  
  String filePath = request.getParameter("filePath");
%>
<title>文档</title>
 <script src="${sysPath}/js/app.v2.js" type="text/javascript"></script>
 <%--<script src="${sysPath}/js/com/common/djoffice.js" type="text/javascript"></script> --%>
   <script src="${sysPath}/js/com/common/Base64.js" type="text/javascript"></script> 
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<META content="MSHTML 6.00.2900.5921" name=GENERATOR>
	<link rel="shortcut icon" href="${sysPath}/favicon.ico" type="image/x-icon" />
	<base href="<%=base%>">
 
<SCRIPT type="text/javascript">

function judge_enter(){
    if(window.event.keyCode==13){
         window.event.keyCode = 0;//阻止刷新页面
    }
}
window.onresize=function(){
	if(document.getElementById("WebOffice")) {
		document.getElementById("WebOffice").style.height =  ($(window).height() - 180) + 'px';
	}
}

var filePath = "<%=filePath%>";
function saveCallback(q) {
	//physicalPath
	//var id = window.opener.archive_doc.getFolderId();
	//var dType = window.opener.archive_doc.documentType;
	//var docId = window.opener.archive_doc.currentDocumentId;
	//var model = window.opener.archive_doc.model;
	var id="${folderId}";
	var dType="${documentType}";
	var docId="${currentDocumentId}";
	var model="${model}";
	
	var docType = 1;
	if('xls' == dType) {
		docType = 2;
	} else if('ppt' == dType) {
		docType = 3;
	}
	var doc = {
			physicalPath:q['relativePath'],
			dmSize:q['fileSize'],
			dmSuffix:q['dmSuffix'],
			dmName:$('#name').val(),
			dmAlias:q['fileName'],
			folderId:id,
			model:model,
			id:docId,
			fileType:0,
			dmLockStatus:0,
			contentType:docType
			//dmSuffix:dType
	};

	if ($('#docForm').valid()) {
	 $.ajax({
		 type : "post",
			url : getRootPath() + "/archive/document/save.action?token="+$('#token').val(),
			data : doc,
			//dataType : "json",
			cache:false,
			success : function(data){
			$('#token').val(data.token);
				if("true" == data.success) {
					msgBox.tip({
						type : "success",
						content : data.successMessage,
						callback: function(){
							try{
								if(typeof ( window.opener.archive_doc)!=undefined && window.opener.archive_doc!="undefined"){
									window.opener.archive_doc.currentDocumentId = null;
									window.opener.archive_doc.reloadData();
								}
							}
							catch(e){
								
							}
							window.close();
						}
					});

				} else {
					if(data.labelErrorMessage){
						//labelErrorMessage
							msgBox.tip({
								content : data.labelErrorMessage
							});
					 }
					else if(data.errorMessage){
						msgBox.tip({
							content : data.errorMessage
						});
					}
					 
				}
			 	//window.opener.archive_doc.insertDocRow(doc, true);
			},
			error:function(m){
				
			}
	 }); 
	}
}
$(document).ready(function(){
	/*  $.ajaxSetup ({
			cache: false //close AJAX cache
		}); */	
		$(window).unload(function(){
			if(window.opener && typeof (window.opener.archive_doc)!=undefined && window.opener.archive_doc!="undefined" && window.opener.archive_doc && window.opener.archive_doc.clearParam){
				window.opener.archive_doc.clearParam();
			}
		});
	  //var path = window.opener.archive_doc.documentPath;
	   //var dType = window.opener.archive_doc.documentType;
	   //var viewType = window.opener.archive_doc.viewType;
	   //var path ="${physicalPath}"; //DJweboffice使用
	    var path ="${dmAlias}"; //JGweboffice使用
	   var dType = "${documentType}";
	   var viewType = ${editable};
	   //alert(path+'\n'+dType+'\n'+viewType);
	   //默认编辑
	   var setting = {filePath:path,type:dType,isArchive:true,isNew:true,isOpen:true, isPrint:true,isSaveToService:true, callback:saveCallback};
	   $('#searchFlowForm').css('display','none');
	   if(!viewType) {
		   //只读
		   var windowHeight = window.screen.availHeight-180;
		   setting = {filePath:path,type:dType,isArchive:true,isNew:false,isOpen:false, isShowToolBar:true,isPrint:false,isReadonly:true, callback:saveCallback};
	   } else {
		   $('#searchFlowForm').css('display','block');
		   if(window.opener && window.opener.archive_doc && typeof (window.opener.archive_doc)!=undefined && window.opener.archive_doc!="undefined" && window.opener.archive_doc.currentDMName) {
			   $('#name').val(window.opener.archive_doc.currentDMName);
		   }
	   }
	   //getToken();
	 	WebOffJG.openFile(setting);
	
	 	$('#docForm').validate({
			ignore : ".ignore",
			rules : {
				name : {
					required : true,
					maxlength : 64,
					fileName:true
				}
			}
		});
	
});
 
//设置控件显示or隐藏
function set_A_W_State(isAip) {
	if(isAip) {
		$("#webofficeTR").css("display",'none');
		$("#buttonTR").css("display",'none');
	} else {
		$("#webofficeTR").css("display",'inline-block');
		$("#buttonTR").css("display",'inline-block');
	}
}
function myUnload() {
	if(typeof(window.opener.archive_doc) != undefined && typeof ( window.opener.archive_doc.hasOpen) != undefined) {
		window.opener.archive_doc.hasOpen = false;
	}
}
function selectTemplate() {
	var t = $("#templates").find("option:selected").attr("type");
    var path = $("#templates").val();
    if(path) {
    	window.opener.Doc['templateType'] = t;
    	if(t == "0") {
    		set_A_W_State(false);
		    WebOffJG.openFile({filePath:path,isArchive:true,isNew:false,isSaveToService:false,isSave:true,isOpen:false, isPrint:false, callback:function(q){
		    	assembleData();
				window.close();
		    }});
		  	Aip.openFile({fileContent:docContent,state:0,isNoButton:true,type:"aip",callback:function(a){
		 	}}); 
	    	typeSetting();
    	} else {
    		set_A_W_State(true);
    		Aip.openFile({fileContent:path,state:0,isFile:true,close:false,isNoButton:false,isSaveToLocal:true,isSaveToService:false,type:"aip",callback:function(a){
    			window.opener.Doc['composingFile'] = a['composingFile'];
    			window.opener.Doc['reviewFile'] = a['composingFile'];
    			window.opener.Doc['sealFile'] = a['composingFile'];
    			window.opener.Doc['viewFile'] = a['composingFile'];
    			window.opener.Doc['contentFile'] = a['composingFile'];
    			window.close();
    	 	}});
    	}
    } else {
        WebOffJG.openFile({filePath:"",isNew:false,isArchive:true,isShowToolBar:false,isSaveToService:false,isSave:true,isOpen:false, isPrint:false, callback:function(q){
        	assembleData();
			window.close();
	    }});
    }
	//$(".selector").val();
	//$(".selector").find("option:selected").text();
	//$(".selector2").empty();
	// var option = $("<option>").val(1).text("pxx");

    //$(".selector2").append(option);
}
 
 
</SCRIPT>
</HEAD>
<BODY onunload="myUnload()">
<section class="jcGOA-wrap">
        <section>
            <section class="jcGOA-con">
                <section id="content">
                    <section class="jcGOA-wrap">
                         <section class="scrollable padder" id="scrollable" style="overflow-y:auto">
                           <!--  <header class="con-header pull-in" style="height:60px">
                                <div class="con-heading fl" >
                                    <h1>公文套红排版</h1>
                                </div>
                            </header> -->
                  <section class="table-wrap panel" id="searchFlowForm" style="margin-top:10px;display:none">
                  <form id="docForm"  class=" form-table ">
                <table class="table table-td-striped">
                <input type="hidden" id="token" name="token" value="${token }" />
                    <tbody>
                        <tr>
                            <td class="w140"><span class="required">*</span>文档名称</td>
                            <td>

                                 <input type="text" id="name" name="name" maxlength="64" placeholder ="不要包含特殊字符"  style="width:300px;" onkeypress="judge_enter();"/>
                            </td>
                             
                        </tr>
                         
                    </tbody>
                </table></form>
            </section>
                                 <!--  <section class="table-wrap">
                                    <label class="inline m-r">模版名称</label>
                                    <select class="m-t">
                                        <option>请选择</option>
                                        <option>1</option>
                                        <option>2</option>
                                    </select>
                                </section>  --> 
                            <section style="margin-top:-30px">
                            <table cellspacing="0" cellpadding="0" border="0" width="100%">
                            	<tr height="*" id="webofficeTR">
                            		<td>
		               <%-- <%@ include file="/plugin/weboffice.jsp"%>--%>
                                        暂时不支持
                            		</td>
                            	</tr>
                            	 
                            	 
                            </table>
                            </section>
                        </section>
                    </section>
                </section>
            </section>
        </section>
    </section>
</BODY>

<!-- IE8水印 -->
 <%@ include file="/WEB-INF/web/include/foot.jsp"%>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%
String base = request.getContextPath(); 
  String IPAddress = null;
  IPAddress = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
  
 // String filePath = request.getParameter("filing");
%>
<title>查看正文</title>
<%-- <script src="${sysPath}/js/com/common/djaip.js" type="text/javascript"></script> --%>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<META content="MSHTML 6.00.2900.5921" name=GENERATOR>
	<link rel="shortcut icon" href="${sysPath}/favicon.ico" type="image/x-icon" />
	<base href="<%=base%>">
 
<SCRIPT type="text/javascript">
/* var aa = ${filing}; */

var path_ = "<%=IPAddress%>";
$(document).ready(function(){
	 $.ajaxSetup ({
			cache: false //close AJAX cache
		});	
	 $.ajax({
			type : "get",
			url : getRootPath() + "/archive/document/getContent.action",
			data : {id:window.opener.Doc['id']},
			//dataType : "json",
			cache:false,
			success : function(data) {
				WebPDF.openFile({recordId:data.formContent,isReadonly:true,model:0,isModelEdit:true});
				/*Aip.openFile({fileContent:path_+data.filePath,isFile:true,state:0,isPrint:true,isSaveToLocal:false,isSaveToService:false,type:"aip",callback:function(a){
					window.close();
			 	}});*/
			}
		});

	//alert(data.filePath);
	//$("#base1").html(data.filePath);
	
		//window.opener.Template['filePath'] = b.decode(window.opener.Template['filePath']);
/* 	 $.ajaxSetup ({
			cache: false //close AJAX cache
		});	
	 $.ajax({
			type : "get",
			url : getRootPath() + "/doc/send/get.action",
			data : {id:window.opener.Doc['id']},
			//dataType : "json",
			cache:false,
			success : function(data) {
				Aip.openFile({fileContent:data.viewFile,state:0,isPrint:true,isSaveToLocal:false,isSaveToService:false,type:"aip",callback:function(a){
					window.close();
			 	}});
			}
		}); */
 
	//Aip.openFile({filePath:window.opener.Template['filePath'],state:1,type:"aip",callback:function(a){
	//Aip.openFile({filePath:"http://172.16.3.23:8080/goa/upload\\office\\1400741948715-14-59-8.docx",state:1,type:"doc",callback:function(a){
});
</SCRIPT>
</HEAD>
<BODY >
<section class="jcGOA-wrap">
        <section>
            <section class="jcGOA-con">
                <section id="content">
                    <section class="jcGOA-wrap">
                        <section class="scrollable padder" id="scrollable">
                             <section>
        <div id="base1"></div>
		                 <%@ include file="/plugin/webPDF.jsp"%>
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

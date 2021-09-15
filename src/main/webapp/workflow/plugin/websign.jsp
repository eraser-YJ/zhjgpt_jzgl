<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%-- <%@ include file="/WEB-INF/web/include/taglib.jsp"%> --%>

<%-- <%

/* String mScriptName = "/DocumentEdit.jsp"; */
String mServerName="/Service.jsp";
String mHttpUrlName=request.getRequestURI();
String mServerUrl="http://"+request.getServerName()+":"+request.getServerPort();
%>
 --%>
<%-- <input type="hidden" name="mServerUrl" id="mServerUrl"  value="<%=mServerUrl%>"/> --%>
<script type="text/javascript">
/* 	var dianju = "<object id='DWebSignSeal' style='position:absolute;width:0px;height:0px;left:0px;top:0px;'";
	dianju += "classid='clsid:77709A87-71F9-41AE-904F-886976F99E3E' codebase='${sysPath }/install/setup/WebSign.dll#version=4,5,5,0'>";
	dianju += "</OBJECT>";
	document.write(dianju); */
<%-- if("0" == $('#signType').val()) {
	//点聚
	var dianju = "<object id='DWebSignSeal' style='position:absolute;width:0px;height:0px;left:0px;top:0px;'";
	dianju += "classid='clsid:77709A87-71F9-41AE-904F-886976F99E3E' codebase='${sysPath }/install/setup/WebSign.dll#version=4,5,5,0'>";
	dianju += "</OBJECT>";
	document.write(dianju);
} else if("1" == $('#signType').val()) {
	//金格
	var jinge = "<OBJECT id='SignatureControl'  classid='clsid:D85C89BE-263C-472D-9B6B-5264CD85B36E' codebase='${sysPath }/install/setup/iSignatureHTML.cab#version=8,0,0,368' width=0 height=0 VIEWASTEXT>";
    jinge += '<param name="WebAutoSign" value="0">';
    jinge += '<param name="ServiceUrl" value="<%=mServerUrl%>">';
    jinge += '<param name="WebAutoSign" value="0">';
    jinge += '<param name="PrintControlType" value=0>';
    jinge += '<param name="MenuDocVerify" value=false>';
    jinge += '<param name="MenuServerVerify" value=false>';
    jinge += '<param name="MenuDigitalCert" value=false>';
    jinge += '<param name="MenuDocLocked" value=false>';
    jinge += '<param name="MenuDeleteSign" value=false>';
    jinge += '<param name="MenuMoveSetting" value=false>';
    jinge += '<param name="MenuAbout" value=false>';
    jinge += '</OBJECT>';
    document.write(jinge);
}
 --%>
</script>
<%--  <object id='DWebSignSeal' style='position:absolute;width:0px;height:0px;left:0px;top:0px;'
        classid='clsid:77709A87-71F9-41AE-904F-886976F99E3E' codebase='${sysPath }/install/setup/WebSign.dll#version=4,5,5,0'>
</OBJECT> 
--%>
<%-- <OBJECT id="SignatureControl"  classid="clsid:D85C89BE-263C-472D-9B6B-5264CD85B36E" codebase="${sysPath }/install/setup/iSignatureHTML.cab#version=8,0,0,368" width=0 height=0 VIEWASTEXT>
<param name="ServiceUrl" value="<%=mServerUrl%>"><!--读去数据库相关信息-->
<param name="WebAutoSign" value="0">             <!--是否自动数字签名(0:不启用，1:启用)-->
<param name="PrintControlType" value=0>               <!--打印控制方式（0:不控制  1：签章服务器控制  2：开发商控制）-->
<param name="MenuDocVerify" value=false>                  <!--菜单验证文档-->
<param name="MenuServerVerify" value=false>               <!--菜单在线验证-->
<param name="MenuDigitalCert" value=false>                <!--菜单数字签名-->
<param name="MenuDocLocked" value=false>                  <!--菜单文档锁定-->
<param name="MenuDeleteSign" value=false>                 <!--菜单撤消签章-->
<param name="MenuMoveSetting" value=false>                <!--菜单禁止移动-->
<param name="MenuAbout" value=false>
<!--param name="Weburl"  value="">        <签章服务器响应-->
</OBJECT>  --%>

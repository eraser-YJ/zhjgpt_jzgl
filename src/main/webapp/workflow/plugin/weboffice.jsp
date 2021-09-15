<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

 <head> 
 <!-- msgboxjs引用更改 2014、10、29 renlx -->
<script type="text/javascript" src="${sysPath}/js/jQuery_jbox/jquery.box_datepicker.js"></script>
<%-- <script type="text/javascript" src="${sysPath}/js/lib/common/msgBox.js"></script> --%>
<%-- <script type="text/javascript" src="${sysPath}/js/lib/jquery-jbox/i18n/jquery.jBox-zh-CN.min.js"></script> --%>
<%--  <%
String base = request.getContextPath(); 
  String IPAddr = null;
  IPAddr = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%> --%>   
</head>  
<!-- <SCRIPT type="text/javascript" event=NotifyToolBarClick(iIndex) for=WebOffice>

	//WebOffice_NotifyToolBarClick(iIndex);
</SCRIPT> -->
<!--  <header class="con-header pull-in" id="header_1" style="height:60px;">
<input type="hidden" id="id" name="id" value="0">
 <input type="hidden" id="token" name="token" value="0">
 <input type="hidden" id="modifyDate" name="modifyDate">
    <div class="con-heading fl" style="margin-top:-10px;">
        <h1>***************a*****正文</h1>
    </div>
</header> -->
 
<script type="text/javascript">
//"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
 var path_ = 'http://' + '<%=request.getServerName()%>' + ':' + '<%=request.getServerPort()%>' + '<%=request.getContextPath()%>' + '/';
 
</script>
<section class="panel table-wrap m-t" id="officeToolbar_" style="margin-top:20px">
                <section class="form-btn m-b-lg"  style="height:30px;padding-top:15px;padding-left:20px"> 
                   <a class="btn" type="button" id="new_" onclick="newFile_()" style="display:none;">新 建</a> 
                    <a class="btn" type="button" id="open_" onclick="openExistFile_()" style="display:none;">打 开</a>
                    <a class="btn" type="button"  id="print_" onclick="printFile_()" style="display:none;">打 印</a>
                    <a class="btn" type="button"  id="seal_" onclick="addSeal_()" style="display:none;">盖 章</a>
                    <a class="btn" type="button"  id="save_" onclick="saveFile_()" style="display:none;">保存到本地</a>
                   <a class="btn" type="button"  id="tempSave_" onclick="tempSaveFile_()" style="display:none;">保 存</a>  
                    <a class="btn" type="button"  id="saveToServer_" onclick="saveFileToService_()" style="display:none;">保 存</a>
                   <a class="btn" type="button"  id="tempClose_" onclick="tempClose_()" style="">关 闭</a>  
                </section>
            <!-- </form> -->
</section>
<object id=WebOffice style='width:100%;LEFT: 0px; TOP: 0px;padding-right:5px'
        classid='clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5' codebase='/WebOffice.cab#Version=7,0,1,0'>
		<param name='_ExtentX' value='6350'>
		<param name='_ExtentY' value='6350'>
</object> 
<%@ include file="/workflow/plugin/desseal.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 
<!--  <SCRIPT  type="text/javascript" FOR=HWPostil EVENT=NotifyCtrlReady>
		 HWPostil_NotifyCtrlReady();
 </SCRIPT> -->
<!--  <header class="con-header pull-in" id="header_1" style="height:60px;">
<input type="hidden" id="id" name="id" value="0">
 <input type="hidden" id="token" name="token" value="0">
 <input type="hidden" id="modifyDate" name="modifyDate">
    <div class="con-heading fl" style="margin-top:-10px;">
        <h1>***************a*****正文</h1>
    </div>
</header> -->
<%-- <script type="text/javascript" src="${sysPath}/js/jQuery_jbox/jquery.box_datepicker.js"></script>
<script type="text/javascript" src="${sysPath}/js/lib/common/msgBox.js"></script> --%>
<SCRIPT type="text/javascript" FOR=HWPostil EVENT=NotifyCtrlReady>
<!--
document.all.HWPostil.JSEnv = 1;
 //HWPostil1_NotifyCtrlReady()
//-->
</SCRIPT>
<script type="text/javascript">
//"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
 var path_ = 'http://' + '<%=request.getServerName()%>' + ':' + '<%=request.getServerPort()%>' + '<%=request.getContextPath()%>' + '/';
</script>

		<SCRIPT type="text/javascript" FOR=HWPostil EVENT=JSNotifyBeforeAction(lActionType,lType,strName,plContinue)> 
		<!--
		//document.all.HWPostil1.JSValue  = 0;
		if(window.opener.Doc) {
			
			var docPrintedNum = window.opener.Doc['printedNum'];
			var docPrintNum = window.opener.Doc['printNum'];
			var controlPrint = window.opener.Doc['controlPrint'];
			var print_num = lType;
			if($('#outWrokflow').val() == "1" || window.opener.fromInside){
				controlPrint = "0"
			}
			//alert("b"+"\n"+controlPrint+"\n"+parseInt(docPrintedNum)+"\n"+parseInt(docPrintNum));
			if("1" == controlPrint && lActionType == 1) {
				//控制打印章的颜色
				if(docPrintNum > 0) {
					if(parseInt(docPrintedNum) != parseInt(docPrintNum)){
						if((lType + parseInt(docPrintedNum)) > parseInt(docPrintNum))
						{
							document.all.HWPostil.JSValue  = 0;
							msgBox.info({
								content : "您已超过允许打印的有效文件数量，还可以打印"+(parseInt(docPrintNum) - parseInt(docPrintedNum))+"份有效文件，请重新设置打印份数。"
							});
							//return
							//alert("超过允许打印的有效文件数量\n还可以打印"+(parseInt(docPrintNum) - parseInt(docPrintedNum))+"份有效文件");
						} else {
							window.opener.Doc['printNumThisTime'] = lType;
						}
					}
				}
			}
		}

		//lActionType = 1为打印动作 此时lType为打印份数；strName=打印机名称 
		///plContinue = 1;执行默认操作
		//plContinue = 0;表示终止默认操作
		// HWPostil1_NotifyBeforeAction(lActionType,lType,strName,plContinue);
		//-->
		</SCRIPT>
<section class="panel table-wrap m-t">
                <section class="form-btn m-b-lg" id="aipToolbar_" style="height:30px;padding-top:12px;padding-left:20px">
                    <a class="btn"  type="button" id="newAIP_" onclick="newAIPFile_()" style="display:none;">新 建</a>
                    <a class="btn"   type="button" id="openAIP_" onclick="openAIPExistFile_()" style="display:none;">打 开</a>
                    <a class="btn" type="button"   id="printAIP_" onclick="printAIPFile_()" style="display:none;">打 印</a>
                    <a class="btn" type="button"   id="viewAip_" onclick="viewAIP_()" style="display:none;">审 阅</a>
                    <a class="btn" type="button"   id="addSeal_" onclick="addSealAIP_()" style="display:none;">盖 章</a>
                    <a class="btn" type="button"   id="removeviewAip_" onclick="removeViewAIP_()" style="display:none;">擦 除</a>
                    <a class="btn" type="button"   id="saveAIPToServer_" onclick="saveAIPFileToService_()" style="display:none;">保 存</a>
                    <a class="btn" type="button"   id="isSaveToLocal" style="display:none;" onclick="save_()" style="">保 存</a>
                    <a class="btn"  type="button" id="closeAIP_" onclick="closeAIPFile_()" style="display:none;">关 闭</a>
                    <!-- <a class="btn" type="button"   id="" onclick="test_()" style="">test</a>
                    <a class="btn" type="button"   id="" onclick="edit_()" style="">编辑正文</a>
                    <a class="btn" type="button"   id="" onclick="view_()" style=""> 再编辑正文</a>
                    <a class="btn" type="button"   id="" onclick="get64_()" style="">get64</a>
                    <a class="btn" type="button"   id="" onclick="set64_()" style="">set64</a> -->
                </section>
            <!-- </form> -->
</section>
 
        <!-- classid='clsid:FF1FE7A0-0578-4FEE-A34E-FB21B277D561' codebase='/HWPostil.ocx#Version=3,1,1,0'> -->
<object id='HWPostil' style='width:100%;height:100%;left:0px;top:0px;'
        classid='clsid:FF1FE7A0-0578-4FEE-A34E-FB21B277D561' codebase='/HWPostil.ocx#Version=3,1,2,0'>
</OBJECT> 

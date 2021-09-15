<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<script src="${sysPath}/js/com/signature/handwritten.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/archive/docFormView.js" type="text/javascript"></script>
<style>
.memu{width:100%;border:1px solid #fb6b5b;}
	.memu.b-btm-1{border-bottom:1px solid #fb6b5b;}
	.memu th,.memu td{padding:8px;line-height:20px;text-align:left;border-top:1px solid #fb6b5b}
	.memu th{font-weight:bold}
	.memu thead th{vertical-align:bottom}
	.memu caption+thead tr:first-child th,.memu caption+thead tr:first-child td,.memu colgroup+thead tr:first-child th,.memu colgroup+thead tr:first-child td,.memu thead:first-child tr:first-child th,.memu thead:first-child tr:first-child td{border-top:0}
	.memu tbody+tbody{border-top:1px solid #fb6b5b;}
	.memu .table{background-color:#fff}
	.memu th,.memu td{border-left:1px solid #fb6b5b}
	.memu caption+thead tr:first-child th,.memu caption+tbody tr:first-child th,.memu caption+tbody tr:first-child td,.memu colgroup+thead tr:first-child th,.memu colgroup+tbody tr:first-child th,.memu colgroup+tbody tr:first-child td,.memu thead:first-child tr:first-child th,.memu tbody:first-child tr:first-child th,.memu tbody:first-child tr:first-child td{border-top:0}
	.panel .memu thead>tr>th,.panel .memu>tbody>tr>td{height:45px;font-weight: bold;}
	.panel .memu>tbody>tr>td>ul>li>a{font-weight:normal;}
   	.panel-heading,.modal-heading{font-size:18px;margin:0;line-height:18px;border:0 none;padding:24px 20px 15px;}
</style>
<!--start 表格-->
<section class="panel m-t-md clearfix">
	<%--<%@ include file="/plugin/websign.jsp"%>--%>
	<input type="hidden" id="workId"/>
   	<div class="table-wrap w800 m-20-auto" id="sendSimulationForm">
		${formContent}
    </div>
</section>
<!--end 表格-->
<script type="text/javascript">

/*var workId = subContent.substr(Number(subContent.indexOf("HZ")),32);
if(workId){
	$('#workId').val(workId);
	var objects = $('Object');
	if(objects) {
		objects.each(function(index,item){
			var owidthStr = $(item).css('width');
			var owidthStrLength = owidthStr.length;
			var owidth = owidthStr.substring(0,owidthStrLength-2)
			owidth = +owidth;
			owidth = owidth*0.7;
			$(item).css('width',owidth+"px");
		});
	}
	//handWritten.initWebRevision();
	/!*handWritten.getSuggestWrite(workId);
	
	//处理签章失效
	var strObjectName = document.all.DWebSignSeal.FindSeal("",0);
	while(strObjectName){
		document.all.DWebSignSeal.SetDocAutoVerify(strObjectName,0);
		strObjectName = document.all.DWebSignSeal.FindSeal(strObjectName,0);
	};*!/
}*/
$(document).ready(function (){
	var view_ = $('#sendSimulationForm').innerHTML;
	document.getElementById('sendSimulationForm').innerHTML = view_;
	$(document).find("[workFlowForm]").each(function(){
		try{
			var item = $(this);
			var itemType = $(this).attr("workFlowForm");
			alert(itemType);
			if("button" == itemType) {
				docFromView.type[itemType].hide(item);
			} else {
				//对于浏览模式,所有组件都为只读状态
				docFromView.type[itemType].read(item);
			}

		}catch(e) {}
	});


	var objects = $('Object');
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
			owidth = owidth*0.7;
			$(item).css('width',owidth+"px");
			//$(item).css('margin-left','-70px');
		});
	}
	ie8StylePatch();
	var li = $('#attachList li');
	li.each(function(i,e){
		var l = $(e)[0].lastChild;
		if(l) {
			$(l).remove();
		}
	});
});



</script>
<!-- IE8水印 -->

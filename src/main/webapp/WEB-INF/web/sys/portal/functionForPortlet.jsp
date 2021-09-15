<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
<%@ taglib prefix="fn" uri="fn" %>
<script>
	function getFunctionView(url,divname,functionId,funViewType,funParametertype,funUrlparameter,funUrlmore,dataRows){
		if(funViewType == 'friendlyLink'){
		 	if('${layoutStatus}' == 'view')
				runJsForfriendlylink('${portletId}_'); 
			if('${layoutStatus}' == 'edit'){
				$("div[name=processdiv${portletId}_]").sortable({
			        update: function (event, ui) {
			        	portalset.evaluaterefresh('${portletId}_');
			        }
			    });
			}
			$("#dataLoad_"+${portletId}).fadeOut();
			return;
		}
		
		if(url == '空'){
			if('${viewType}' == 'shortcut'){
				$('#shortcut >li:eq(0)').attr("id","liset_"+functionId);
				$('#shortcut >li:eq(0)').attr("title",functionId);
				$('#'+divname).html($('#shortcut').html());
			}else
				$('#'+divname).html($('#onetable').html());
			$("#dataLoad_"+${portletId}).fadeOut();
			return;
		}
		
		var strValue = "{'funViewType':'"+funViewType+"','funUrlmore':'"+funUrlmore+"','functionId':'"+functionId+"','portletId':'"+${portletId}+"','dataRows':'"+dataRows+"','userids':'"+${userids}+"','roleids':'"+${roleids}+"','deptids':'"+${deptids}+"','organids':'"+${organids}+"'}";
		/* if(funParametertype == 2)
			url = url + "?parameter="+funUrlparameter;
		else */
			url = url + "?parameter="+funUrlparameter;
		jQuery.ajax({
	        url: getRootPath()+url,
	        type: 'post',
	        //async:false,
	        data : {'mydata':strValue},
	        success: function(data, textStatus, xhr) {
	        	if('${viewType}' == 'shortcut'){
	        		//$('#'+divname).html("<li id=\"liset_"+functionId+"\" title=\""+functionId+"\">"+data+"</li>");	
	        		$('#liout_'+${portletId}).append(data);
	        		$('#liout_'+${portletId}+" #shortcutName").html('${portlet.letTitle}');
	        		ie8InRounded();
	        	}else {
	        		if(funViewType == "pviewType_10"){
	        			$('#href_'+${portletId}).css("display","none");
	        		}
	        		$('#'+divname).html(data);	
	        	}
	        	
	        	$("#dataLoad_"+${portletId}).fadeOut();
	        },error:function(e){
	        	// alert("加载数据错误。");
	        	//msgBox.tip({content: "加载数据错误", type:'fail'});
	        }
	    });
	}
	
	function runJsForfriendlylink(divname){
		$("#funlink"+divname).slide({
		       mainCell:".bd ul",
		       pnLoop:true,
		       effect:"topMarquee",
		       interTime:'50',
		       vis:6,
		       mouseOverStop:true,
		       autoPlay: true,
		       trigger:"click"
		});
	}
	
	jQuery(function($){
		if('${viewType}' == 'moretable'){
    		var light = $('#tabs${portletId}').children().length;
            $('#tabs${portletId} li').each(function(){
                $(this).css('width',((100 / light)-${tabsize})+'%');
            });
            $( "#tabs" ).tabs();
    	};
	});
</script>
<style>
	.slide-li-portlet{
		border-bottom: 1px solid #f3f3f3;
	}
	.slide-li-portlet a:hover{
		background:#fdfcfb;
	}
	.slide-li-portlet a{
		padding:8px 0 8px 10px;
		display: block;
	}
	.port-btn .btn{
		width:40px;
		padding: 2px;
		background-color: #fff;
		border: 1px solid #ccc;
		color: #555;
	}
	
	.port-btn .btn:hover{
		background-color: #f3f3f3;
	}
	
	.table.port-btn tr:first-child td:first-child,
	.table.port-btn tr:first-child td:first-child+td+td,
	.table.port-btn tr:first-child td:first-child+td+td+td+td{
		width:10%;
		text-align:right;
		background-color: #f9f8f5;
	}
	
	.table.port-btn tr:first-child td:first-child+td+td+td+td+td+td{
		width:10%;
		text-align: center;
	}
	
	.table.port-btn tr:first-child td:first-child+td,
	.table.port-btn tr:first-child td:first-child+td+td+td,
	.table.port-btn tr:first-child td:first-child+td+td+td+td+td{
		width:20%;
	}
	
	
</style>
<div class="loadfunctionview">
<c:forEach items="${refunctions }" var="functionvo" >
<script>
	getFunctionView('${functionvo.funUrl}','funportlet${portletId}_${functionvo.id }','${functionvo.id }','${functionvo.viewType }','${functionvo.funParametertype }','${functionvo.funUrlparameter }','${functionvo.funUrlmore }','${functionvo.funRows }');
</script>
</c:forEach>
</div>
<c:choose>
	<c:when test="${viewType == 'moretable'}">
		<div id="tabs" <c:if test="${sessionScope.theme == 'standard'}">style="margin:0;"</c:if> class="table-wrap tabs-wrap tabs-wrap-in">
			<ul id="tabs${portletId}" class="nav nav-tabs">
			<c:forEach items="${refunctions }" var="functionvo" varStatus="counts">
			<c:if test="${counts.count == 1 }">
				<li class="active span3"><a href="#funportlet${portletId}_${functionvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portletId }','${functionvo.id }')" data-toggle="hover">
				<c:choose><c:when test="${fn:indexOf(functionvo.funName,'（') != -1}">${fn:substringBefore(functionvo.funName,'（')}</c:when><c:otherwise>${functionvo.funName }</c:otherwise></c:choose></a></li>
			</c:if>
			<c:if test="${counts.count != 1 }">
				<li ><a href="#funportlet${portletId}_${functionvo.id }" style="cursor: pointer;" onclick="portalview.loadMore('${portletId }','${functionvo.id }')" data-toggle="hover">
				<c:choose><c:when test="${fn:indexOf(functionvo.funName,'（') != -1}">${fn:substringBefore(functionvo.funName,'（')}</c:when><c:otherwise>${functionvo.funName }</c:otherwise></c:choose></a></li>
			</c:if>
			</c:forEach>
			</ul>
			<div class="tab-content super-slide">
			<c:forEach items="${refunctions }" var="functionvo" varStatus="counts">
			<c:if test="${counts.count == 1 }">
				<div id="funportlet${portletId}_${functionvo.id }" class="tab-pane fade active in"></div>
			</c:if>
			<c:if test="${counts.count != 1 }">
				<div id="funportlet${portletId}_${functionvo.id }" class="tab-pane fade "></div>
			</c:if>
			</c:forEach>
			</div>
		</div>
	</c:when>
	<c:when test="${viewType == 'shortcut'}">
		<c:forEach items="${refunctions }" var="functionvo">
			<ul id="funportlet${portletId}_${functionvo.id }"></ul>
		</c:forEach>
	</c:when>
	<c:when test="${viewType == 'friendlyLink' && layoutStatus == 'view'}">
		<div id="funportlet${portletId}_">
			<div id="funlink${portletId}_" class="slideBox play-list friendly">
			<div class="bd">
            	<ul>
            		<c:forEach items="${friendlinks }" var="linksvo">
            			<li class="slide-li-portlet"><a href="${linksvo.linkUrl}" target="_black">${linksvo.linkName}</a></li>
            		</c:forEach>
            	</ul>
        	</div>
			</div>
		</div>
	</c:when>
	<c:when test="${viewType == 'friendlyLink' && layoutStatus == 'edit'}">
		<div id="funportlet${portletId}_">
				<div style="display:block;">
					<div style="width:100%">
	    				<div action-type="processdiv" id="processdiv${portletId}_" name="processdiv${portletId}_" style="width:100%;">
	    					<c:forEach items="${friendlinks }" var="linksvo" varStatus="counts">
	    						<div id='div_${counts.count-1}'>
	    						<table class='table port-btn' >
	    							<tr>
	    							<td style="padding:0px 0px;border-top:2px;">名称:</td>
	    							<td style="padding:0px 0px;border-top:2px;"><input class='eName evaluate_name${portletId}_' size='10' maxlength='20' value="${linksvo.linkName}" name='index_name_${portletId}_${counts.count-1}' type='text'/>
	    							</td>
    								<td style="padding:0px 0px;border-top:2px;">地址:</td>
    								<td style="padding:0px 0px;border-top:2px;"><input class='eLimit evaluate_limit${portletId}_' value="${linksvo.linkUrl}" name='index_limit_time_${portletId}_${counts.count-1}' type='text' size='10' maxlength='100'/>
    								</td>
									<%-- 将排序隐藏处理
									<td style="padding:0px 0px;border-top:2px;">排序:</td>
    								<td style="padding:0px 0px;border-top:2px;"><input class='eAlert evaluate_alert${portletId}_' name='index_alert_time_${portletId}_${counts.count-1}' type='text' value='${counts.count}' size='6' maxlength='3'/>
    								</td>
									--%>
   									<td style="padding:0px 0px;border-top:2px;"><input class='eAlert evaluate_alert${portletId}_' name='index_alert_time_${portletId}_${counts.count-1}' type='hidden' value='${counts.count}'/><button class="btn" type='button' name='evaluatemove${portletId}_' onclick='portalset.evaluatemove(${counts.count-1},"${portletId}_")'>移除</button>
   									</td>
   									</tr>
   								</table>
   								</div>
	    					</c:forEach>
						</div>
					</div>
					<input class="btn dark" type="button" name="insert" onclick="portalset.addevaluate('${portletId}_')" value="增行">
					<input class="btn dark" type="button" name="save" onclick="portalset.evaluatesave('${portletId}_')" value="保存">
				</div>
		</div>
	</c:when>
	<c:otherwise>
		<c:forEach items="${refunctions }" var="functionvo">
			<div id="funportlet${portletId}_${functionvo.id }">
			</div>
		</c:forEach>
	</c:otherwise>
</c:choose>
<div id="onetable" style="display: none;"><!-- 列表样例 -->
	<table class="table table-striped table-bordered b-light first-td-tc over-hide-wrap">
		<thead>
			<tr><TD>暂无链接</TD></tr>
		</thead>
		<tbody>

		</tbody>
	</table>
</div>
<ul id="shortcut" style="display: none;">
	<li> <i class="fa fa-inform icon fl"><b class="bg-green-dark">&nbsp;</b></i>
         <div class="fl panel-con-wrap inform-con"> <strong>0</strong> <span>暂无链接</span> </div>
    </li>
</ul>
<c:if test="${viewType != 'friendlyLink' || layoutStatus == 'view'}">
	<div class="loading" id="dataLoad_${portletId}"></div>
</c:if>
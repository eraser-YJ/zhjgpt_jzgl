<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script>
if('${funViewType}' == 'pviewType_8'){
	/**
	 * 根据配置文件生成echarts的图表
	 */
	$.createChart({
	    title: '单一折线图',
	    subTitle: '单一折线图子标题',
	    elementId : 'container3',
	    height : '400',
	    //设定X轴内容
	    xAxisType: 'category',
	    //X轴显示的内容
	    xAxisData: [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ],
	    //Y轴自动根据内容生成数值
	    yAxisType: 'value',
	    series: [
	      {
	          //图形类型
	          type: 'line',
	          //请求的参数
	          data: {id:"1", state:"2"},
	          //请求地址
	          url: getRootPath()+"/example/echartsData/lineData.jsp",
	          //鼠标移动到图形上的提示内容
	          name: '折线图'
	      }
	    ]
	});
};
if('${funViewType}' == 'pviewType_3'){
	$("#Projector${portletId }${functionId}").slide({
        titCell:".play_hd ul",
        mainCell:".play_bd ul",
        autoPage:true,
        effect:"left",
        autoPlay:true
    });
};
if('${funViewType}' == 'pviewType_5'){
	var grbW = $("#slider").width($("#slider").parents().width()-40)&&$("#slider").width();
    var grbImg = $("#Projectors${portletId }${functionId} div.glory-con"),grbImgW = grbImg.width();
    var leng = Math.floor(grbW/grbImgW);
    var lengN = Math.ceil(grbImg.length/leng);
    //alert(lengN)
    var mar = (grbW - (leng*grbImgW))/(leng-1);
    var mar2 = mar/2;
    
    $("#Projectors${portletId }${functionId} div.glory-con").css({"margin-right":mar2,"margin-left":mar2}).last().css("margin-right","0").end().first().css("margin-left","0");
    $("#Projectors${portletId }${functionId}").css("width",(grbImg.length*grbImgW + (grbImg.length - 1 )*mar+mar2));
    var ml = parseInt($("#Projectors${portletId }${functionId}").css("margin-left"));
    var mn = 0;
    for(var i = 0; i < lengN ; i++){
        $(".cyclyImg_pager2").append("<span>&bull;</span>");
    }
    $(".cyclyImg_pager2 span").first().addClass("cyclyImg_pager_active2");
    $(".cyclyImg_pager2 span").click(function(e){
        if($("#Projectors${portletId }${functionId}").is(":animated")) return false;
        mn=$(e.target).index();
        grbMove(mn);
    });
    $(".cyclyImg_next").click(function(e){
        if($("#Projectors${portletId }${functionId}").is(":animated")) return false;
        grbMove(++mn);
    });
    $(".cyclyImg_prev").click(function(e){
        if($("#Projectors${portletId }${functionId}").is(":animated")) return false;
        grbMove(--mn);
    });
    function grbMove(e){
        if(e<0){
            mn = lengN-1;
        }
        if(e>=lengN){
            mn = 0;
        }
        ml = -mn*(grbW+mar);
        $("#Projectors${portletId }${functionId}").animate({marginLeft:ml+"px"},500);
        $(".cyclyImg_pager2 span").removeClass("cyclyImg_pager_active2");
        $(".cyclyImg_pager2 span").eq(mn).addClass("cyclyImg_pager_active2");
    }
    var grb = setInterval(function(){grbMove(--mn);},3000);
    $("#grb").on({
        mouseenter:function(){clearInterval(grb);},
        mouseleave:function(){grb = setInterval(function(){grbMove(--mn);},3000);}
    })
    var modules_li=$("#topnav-other_modules ul").find("li").width();//读取li宽度
    var modules_liNum=$("#topnav-other_modules ul").find("li").length;//读取li数量
    $("#topnav-other_modules ul").width(modules_li*modules_liNum+modules_liNum);//动态添加ul
    $("#topnav-other_modules").click(function(e){
        $(this).css("background-color","#004f84");
        $(this).find("ul").toggle();
        navigator.userAgent.indexOf("MSIE 8.0")>0&&$(this).find("ul").css("right","-100px");
        e.stopPropagation();
    });
}
</script>
<input id="morelink_${portletId }" type="hidden" value="${funUrlmore }"/><!-- 更多链接 -->
<input id="morelink_${portletId }_${functionId}" type="hidden" value="${funUrlmore }"/><!-- 更多链接  -->
<c:choose>
	<c:when test="${funViewType == 'pviewType_9' }"><!-- 单一大列表 -->
	 <div class="index-inform panel">
		<ul>
			<c:forEach items="${loginlogList }" var="loginlogvo" begin="0" end="${dataRows }">
			<li>
				<i class="fa fa-clipboard icon fl"></i>
				<div class="fl index-inform-con">
					<a href="javascript:void(0)">
						<span>${loginlogvo.displayName }</span>
						<font>来源：时政新闻环球时报[微博]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发布人：某某</font>
					</a>
				</div>
				<em class="fr">2014-07-21</em>
			</li>
			</c:forEach>
		</ul>
	</div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_3' }"><!-- 图文混合 -->
        <div id="Projector${portletId }${functionId}" class="play-list panel projector">
	        <div class="play_bd">
	        <ul>
			<c:forEach items="${loginlogList }" var="loginlogvo" begin="0" end="${dataRows }">
			<li>
	           	<div class="play-img"><img src="/goa/images/demoimg/play_img.jpg" /></div>
	           	<div class="play-con">
	           	<h2><a href="javascript:void(0)">${loginlogvo.displayName }${loginlogvo.id }</a></h2>
				<p>1月21日，重庆“两会”工作人员拉线整齐排列领导座位牌。当日，中国人民政治协商会议重庆市第四届委员会第二次会议闭幕。陈超 摄</p>
	           	</div>
	        </li>
			</c:forEach>
			</ul>    
	        </div>
	        <div class="play_hd"><ul><li></li></ul></div>
        </div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_2' }"><!-- 图表混合 -->
	</c:when>
	<c:when test="${funViewType == 'pviewType_4' }"><!-- 单一图片 -->
	<div class="index-inform panel h320">
		<div align="center" style="margin:0 10px 15px;" class="dbzx"><img src="../../../../images/demoimg/play_img.jpg"></div>
	</div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_5' }"><!-- 多图滚动 -->
		<div class="index-inform panel">
			<div class="glory-box" id="grb">
            	<div class="cyclyImg_pager2 com2"></div>
                <div class="cyclyImg_next next"></div>
                <div class="cyclyImg_prev prev"></div>
                <div id="slider" class="case_box">
                	<div class="case_con" id="Projectors${portletId }${functionId}">
                		<c:forEach items="${loginlogList }" var="loginlogvo" begin="0" end="${dataRows }">
                			 <div class="glory-con">
                                <div class="glory-img m-b-sm"><a href="javascript:void(0)"><img src="/goa/images/demoimg/glory-img1.jpg" /></a></div>
                                   <p class="m-b-sm">
                                     <a href="">${loginlogvo.displayName }</a>
                                     <a href="">${loginlogvo.displayName }</a>
                                   </p>
                                   <div class="glory-btm">
                                     <a href="javascript:void(0)"><i class="fa fa-heart icon">${loginlogvo.id }</i></a>
                                     <a href="javascript:void(0)"><i class="fa fa-chat icon">${loginlogvo.id }</i></a>
                                   </div>
                                </div>
                		</c:forEach>
                	</div>
                </div>
            </div>
		</div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_6' }"><!-- 单一文本 -->
	 <div class="index-inform panel h320">
		<div class="table-wrap">
            <p class="index-p-text">	1月21日，重庆“两会”工作人员拉线整齐排列领导座位牌。当日，中国人民政治协商会议重庆市第四届委员会第二次会议闭幕。1月21日，重庆“两会”工作人员拉线整齐排列领导座位牌。当日，中国人民政治协商会议重庆市第四届委员会第二次会议闭幕陈超 摄1月21日，重庆“两会”工作人员拉线整齐排列领导座位牌。当日，中国人民政治协商会议重庆市第四届委员会第会”工作人员拉线整齐排列领导座位牌。中国人民政治协商会议重庆市第四届委员会第二次会议闭幕陈超 摄1月21日.</p>
        </div>
     </div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_7' }"><!-- 快捷方式 -->
		<i class="fa fa-inform icon fl"><b class="bg-green-dark">&nbsp;</b></i>
         <div class="fl panel-con-wrap inform-con"> <strong>${loginlogListSize }</strong><a href="#" onclick="portalview.loadMore('${portletId }')"><span>登录日志</span></a>  </div>
	</c:when>
	<c:when test="${funViewType == 'pviewType_8' }"><!-- 图表 -->
		<div id="container3"></div>
	</c:when>
	<c:otherwise><!-- 单一小列表 -->
	 <div class="index-inform">
		<table class="table table-striped table-bordered b-light first-td-tc over-hide-wrap">
		<thead>
			<tr>
				<th style="display: none;"></th>
				<th class="w115">显示名</th>
				<th class="w115">登录状态</th>
				<th >操作时间</th>
				<th style="display: none;"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${loginlogList }" var="loginlogvo" begin="0" end="${dataRows }">
				<tr>
				<td style="display: none;"></td>
				<td>${loginlogvo.displayName } </td>
				<td><c:choose><c:when test="${loginlogvo.loginTime != null && loginlogvo.loginTime != '' }">登录</c:when><c:otherwise>退出</c:otherwise></c:choose></td>
				<td><c:choose><c:when test="${loginlogvo.loginTime != null && loginlogvo.loginTime != '' }"><fmt:formatDate value="${loginlogvo.loginTime }" pattern="yyyy-MM-dd HH:mm:ss" /></c:when><c:otherwise><fmt:formatDate value="${loginlogvo.logoutTime }" pattern="yyyy-MM-dd HH:mm:ss" /></c:otherwise></c:choose></td>
				<td style="display: none;"></td>
				</tr>
			</c:forEach>
		</tbody>
		</table>
	</div>
	</c:otherwise>
</c:choose>
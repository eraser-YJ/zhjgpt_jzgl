<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>样式模板</title>
    <link href="../../css/yx_frame.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<section class="scrollable padder jcGOA-section"  style="margin-top:60px;">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>样式例子</span>
        </div>
    </header>
    <section class="panel m-t-md clearfix" style="position: absolute;bottom:15px; top:50px; left: 15px;right:15px;">

        <%--<div style="height: 350px;width: 350px; margin-bottom:15px; border: 1px solid #DDDDDD;">--%>
            <%--<ul class="nav-tabs">--%>
                <%--<li class="tab-item-active">我是第一个tab</li>--%>
                <%--<li>我是第二个tab</li>--%>
            <%--</ul>--%>
            <%--<div class="tab-box">--%>
                <%--<div>--%>
                    <%--我是第一个tab--%>
                <%--</div>--%>
                <%--<div>--%>
                    <%--我是第二个tab--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>

        <div style="height: 350px;width: 350px; border: 1px solid #DDDDDD; display: flex;">
            <ul class="gis-nav-tabs">
                <li class="gis-tab-active" >我是第一个tab</li>
                <li>我是第二个tab</li>
            </ul>
            <div class="gis-tab-box">
                <div>
                    我是第一个tab
                </div>
                <div>
                    我是第二个tab
                </div>
            </div>
        </div>
    </section>
</section>
</body>
<script>
    // $(window).ready(function(){
    //     $('.tab-box>div').hide();
    //     $('.tab-box>div').eq(0).show();
    //     $('.nav-tabs li').click(function(){
    //         $('.nav-tabs li').removeClass('tab-item-active');
    //         $(this).addClass('tab-item-active');
    //         var index=$(this).index();
    //         $('.tab-box>div').hide().eq(index).show();
    //     })
    // })


    $(window).ready(function(){
        $('.gis-tab-box>div').hide();
        $('.gis-tab-box>div').eq(0).show();
        $('.gis-nav-tabs li').click(function(){
            $('.gis-nav-tabs li').removeClass('gis-tab-active');
            $(this).addClass('gis-tab-active');
            var index=$(this).index();
            $('.gis-tab-box>div').hide().eq(index).show();
        })
    })
</script>
</html>

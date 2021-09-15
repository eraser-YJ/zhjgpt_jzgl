<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%-- <%@ include file="/WEB-INF/web/include/taglib.jsp"%> --%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>

 <html>
   <head>
    <title>FlexPaper</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
    <style type="text/css" media="screen">
        html, body	{ height:100%; }
        body { margin:0; padding:0; overflow:auto; }
        #flashContent { display:none; }
    </style>

   <link rel="stylesheet" type="text/css" href="${sysPath}/js/flexpaper/css/flexpaper.css" /> 
    <script type="text/javascript" src="${sysPath}/js/flexpaper/js/jquery.min.js"></script>
    <script type="text/javascript" src="${sysPath}/js/flexpaper/js/flexpaper.js"></script>
    <script type="text/javascript" src="${sysPath}/js/flexpaper/js/flexpaper_handlers.js"></script>
     <script type="text/javascript" src="${sysPath}/js/com/archive/showPdf.js"></script>

</head>
<body>
<div style="position:absolute;left:10px;top:10px;">
<div id="documentViewer" class="flexpaper_viewer"></div>
<input type="hidden" id = "physicalPath" value=${physicalPath} />
<script type="text/javascript">

    var startDocument = "Paper";
	
    $('#documentViewer').FlexPaperViewer(
            { config : {

                SWFFile : $('#physicalPath').val(),
                Scale : 1.2,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : 'false',
                FitWidthOnLoad : 'false',
                FullScreenAsMaxWindow : 'False',
                ProgressiveLoading : 'False',
                MinZoomSize : 0.2,
                MaxZoomSize : 5,
                SearchMatchAll : 'False',
   /*              PrintEnabled:'False',
			     PrintVisible:'False', */
                InitViewMode : 'Portrait',
                RenderingOrder : 'flash',
                StartAtPage : '',
	
                ViewModeToolsVisible :'False',
                ZoomToolsVisible : 'true',
                NavToolsVisible : 'true',
                CursorToolsVisible : 'False',
                SearchToolsVisible : 'False',
                WMode : 'window',
                localeChain: 'zh_CN'
              /*   Scale : 0.6,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : true,
                FitWidthOnLoad : false,
                FullScreenAsMaxWindow : false,
                ProgressiveLoading : false,
                MinZoomSize : 0.2,
                MaxZoomSize : 5,
                SearchMatchAll : false,
                InitViewMode : 'Portrait',
                RenderingOrder : 'flash',
                StartAtPage : '',

                ViewModeToolsVisible : false,
                ZoomToolsVisible : true,
                NavToolsVisible : true,
                CursorToolsVisible : false,
                SearchToolsVisible : true,
                WMode : 'window',
                localeChain: 'zh_CN' */
            }}
    );
</script>
</div>

<!-- THE FOLLOWING CODE BLOCK CAN SAFELY BE REMOVED, IT IS ONLY PLACED HERE TO HELP YOU GET STARTED. -->


<!-- 
<script type="text/javascript">
    var url = window.location.href.toString();

    if(location.length==0){
        url = document.URL.toString();
    }

    if(url.indexOf("file:")>=0){
        jQuery('#documentViewer').html("<div style='position:relative;background-color:#ffffff;width:420px;font-family:Verdana;font-size:10pt;left:22%;top:20%;padding: 10px 10px 10px 10px;border-style:solid;border-width:5px;'><img src='http://flexpaper.devaldi.com/resources/warning_icon.gif'>&nbsp;<b>You are trying to use FlexPaper from a local directory.</b><br/><br/> FlexPaper needs to be copied to a web server before the viewer can display its document properlty.<br/><br/>Please copy the FlexPaper files to a web server and access the viewer through a http:// url.</div>");
    }
</script> -->
</body>
 </html> 
 
 

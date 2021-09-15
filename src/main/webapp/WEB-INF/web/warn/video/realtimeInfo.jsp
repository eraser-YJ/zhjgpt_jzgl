<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>实时视频监控</title>
	<link rel="stylesheet" href="${sysPath}/video/css/demo.css" type="text/css">
	<link rel="stylesheet" href="${sysPath}/video/css/common.css" type="text/css">
	<link rel="stylesheet" href="${sysPath}/video/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${sysPath}/video/js/jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="${sysPath}/video/js/jsencrypt.min.js"></script>
	<script type="text/javascript" src="${sysPath}/video/js/jsWebControl-1.0.0.min.js"></script>
	<script type="text/javascript" src="${sysPath}/video/js/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="${sysPath}/video/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="${sysPath}/video/js/jquery.ztree.exhide.js"></script>
	<script>
		function getRootPath() {
			return '${sysPath}';
		}
		var videoConfig = {};
		//综合安防管理平台提供的appkey，必填
		videoConfig.appKey = '${videoConfig_appKey}';
		//综合安防管理平台提供的secret，必填
		videoConfig.secret = '${videoConfig_secret}';
		//综合安防管理平台IP地址，必填
		videoConfig.ip = '${videoConfig_ip}';
	</script>
</head>
<body>
<input type="hidden" id="projectCodes" name="projectCodes" value="${projectCodes}">
<div id="playWnd" class="playWnd"  ></div>
<div class="module">
	<div class="search-bar">
		<input id="keyword" type="text" class="treeSearchInput" style="width:180px;" placeholder="请输入...">
		<button id="search" class="treeSearchBtn">搜索</button>
	</div>
	<div class="content">
		<!-- 用于显示ztree的html元素的class一定要设置为ztree-->
		<ul id="ztreeObj" class="ztree"></ul>
	</div>
	<div class="item" style="margin: 10px;">

		<div class="item" style="padding-top:10px;">
			<button style="width:99%;padding:0;margin:0;" id="startPreview" class="btn">打开监测点</button>
		</div>
		<br>
		<!--抓图 -->
		<div class="item" >
			<span style="width:100%;padding:0;margin:0;" class="label"><B>抓图信息</B></span>
		</div>	
		<div class="item">
			<span style="padding:0;margin:0;" class="label">保存路径：</span>
			<input id="snapName" type="text" value="d:\SnapDir\test.jpg">
		</div>
		<div class="item">
			<button style="width:99%;padding:0;margin:0;" id="playbackSnap" class="btn">抓取选中窗口图片</button>
		</div>
		<br>
		<div class="item" >
			<span style="width:100%;padding:0;margin:0;" class="label"><B>提示信息</B></span>
		</div>

		<div class="item" style="border: 1px solid red;min-height: 100px;height: 100px;width:242px;">
            <span style="width:100%;padding:0;margin:0;word-break:break-all;" class="label" id="errorMsg">

            </span>
		</div>
	</div>
</div>

</body>

<script type="text/javascript" src="${sysPath}/video/js/busi.ztree.js"></script>
<script type="text/javascript" src="${sysPath}/video/js/busi_base.js"></script>
<script type="text/javascript" src="${sysPath}/video/js/busi_realtime.js"></script>
</html>
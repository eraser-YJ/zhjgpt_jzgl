<!DOCTYPE html>
<html class="jcGOA" id="content">
<head>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<meta http-equiv="Content-type" content="text/html; UTF-8"/>
<meta name="renderer" content="ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="edge" />
<!--[if gt IE 8]><!--> <link href="${sysPath}/css/font-face.css" rel="stylesheet" type="text/css"/> <!--<![endif]-->
<%@ include file="/WEB-INF/web/include/base.jsp"%>
</head>
<body ondragstart="return false">
<div class="loading" id="dataLoad"></div>
<input type="hidden" id="iPage" value="${iPage}">
<input type="hidden" id="sortSetting" value="${sortSetting}">
<input type="hidden" id="queryData" value='${queryData}'>
<input type="hidden" id="otherWorkflowData" value='${otherWorkflowData}'>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<!--[if lt IE 7]>
<html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>
<html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>
<html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>工作流</title>
    <link rel="shortcut icon" href="../favicon.ico?v=jcdesigner" />
    <meta name="description" content="">
    <meta name="viewport"
          content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, width=device-width">
    <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->

    <link rel="Stylesheet" media="screen" href="${sysPath}/process-editor/editor-app/libs/ng-grid-2.0.7.min.css" type="text/css"/>
    <link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/bootstrap_3.1.1/css/bootstrap.min.css"/>

    <link rel="Stylesheet" media="screen" href="${sysPath}/process-editor/editor-app/editor/css/editor.css" type="text/css"/>
    <link rel="stylesheet" href="${sysPath}/process-editor/editor-app/css/style.css" type="text/css"/>

    <link rel="stylesheet" href="${sysPath}/process-editor/editor-app/css/style-common.css">
    <link rel="stylesheet" href="${sysPath}/process-editor/editor-app/css/style-editor.css">
    <link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/ztree_3.5.23/zTreeStyle/metroStyle.css">
</head>
<body>

<div class="navbar navbar-fixed-top navbar-inverse"  role="navigation" id="main-header">
    <div class="navbar-header">
        <a href="${sysPath}/def/designer.action" ng-click="backToLanding()" class="navbar-brand"
           title="{{'GENERAL.MAIN-TITLE' | translate}}"><span
                class="sr-only">{{'GENERAL.MAIN-TITLE' | translate}}</span></a>
    </div>
</div>

<!--[if lt IE 9]>
<div class="unsupported-browser">
    <p class="alert error">你是使用不受支持的浏览器。请升级您的浏览器来使用编辑器。</p>
</div>
<![endif]-->

<div class="alert-wrapper" ng-cloak>
    <div class="alert fadein {{alerts.current.type}}" ng-show="alerts.current" ng-click="dismissAlert()">
        <i class="glyphicon"
           ng-class="{'glyphicon-ok': alerts.current.type == 'info', 'glyphicon-remove': alerts.current.type == 'error'}"></i>
        <span>{{alerts.current.message}}</span>

        <div class="pull-right" ng-show="alerts.queue.length > 0">
            <span class="badge">{{alerts.queue.length + 1}}</span>
        </div>
    </div>
</div>

<div id="main" class="wrapper full clearfix" ng-style="{height: window.height + 'px'}" ng-app="activitiModeler" ng-include="'${sysPath}/process-editor/editor-app/editor.html'">
</div>

<!--[if lt IE 9]>
<script src="${sysPath}/process-editor/editor-app/libs/es5-shim-15.3.4.5/es5-shim.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/json3_3.2.6/lib/json3.min.js"></script>
<![endif]-->

<script src="${sysPath}/process-editor/editor-app/libs/jquery_1.11.0/jquery.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/jquery-ui-1.10.3.custom.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/ztree_3.5.23/jquery.ztree.all.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/webuploader/webuploader.html5only.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/layer/layer.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/webuploader/uploader.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular_1.2.13/angular.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular_1.2.13/angular-animate.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/bootstrap_3.1.1/js/bootstrap.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-resource_1.2.13/angular-resource.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-cookies_1.2.13/angular-cookies.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-sanitize_1.2.13/angular-sanitize.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-route_1.2.13/angular-route.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-translate_2.4.2/angular-translate.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-translate-storage-cookie/angular-translate-storage-cookie.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-translate-loader-static-files/angular-translate-loader-static-files.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-strap_2.0.5/angular-strap.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-strap_2.0.5/angular-strap.tpl.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/momentjs_2.5.1/momentjs.min.js"></script>

<script src="${sysPath}/process-editor/editor-app/libs/ui-utils.min-0.0.4.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/libs/ng-grid-2.0.7-min.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/libs/angular-dragdrop.min-1.0.3.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/libs/mousetrap-1.4.5.min.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/libs/prototype-1.5.1.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/libs/path_parser.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/libs/angular-scroll_0.5.7/angular-scroll.min.js" type="text/javascript"></script>

<!-- Configuration -->
<script src="${sysPath}/process-editor/editor-app/app-cfg.js?v=1"></script>
<script src="${sysPath}/process-editor/editor-app/editor-config.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/url-config.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/editor/i18n/translation_en_us.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/editor/i18n/translation_signavio_en_us.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/editor/oryx.debug.js" type="text/javascript"></script>

<script src="${sysPath}/js/com/jc/workflow/customProperty.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/app.js"></script>

<script src="${sysPath}/process-editor/editor-app/eventbus.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/editor-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/stencil-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/toolbar-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/header-controller.js" type="text/javascript"></script>
<%-- 选择脚本任务，骆驼任务，Mule任务，发送任务 Controller
 这个Controller里面可以实现切换当前节点样式类型等功能
 --%>
<%--<script src="${sysPath}/process-editor/editor-app/select-shape-controller.js" type="text/javascript"></script>--%>
<script src="${sysPath}/process-editor/editor-app/shape-setings-controller.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/editor-utils.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/toolbar-default-actions.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/configuration/properties-default-controllers.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-execution-listeners-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-event-listeners-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-assignment-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-fields-controller.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/configuration/properties-person-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-variable-controller.js" type="text/javascript"></script>
<%--
<script src="${sysPath}/process-editor/editor-app/configuration/properties-form-properties-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-in-parameters-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-multiinstance-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-out-parameters-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-task-listeners-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-sequenceflow-order-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-condition-expression-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-signal-definitions-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-signal-scope-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-message-definitions-controller.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/properties-message-scope-controller.js" type="text/javascript"></script>--%>

<script src="${sysPath}/process-editor/editor-app/configuration/toolbar.js" type="text/javascript"></script>
<script src="${sysPath}/process-editor/editor-app/configuration/toolbar-custom-actions.js" type="text/javascript"></script>

<script src="${sysPath}/process-editor/editor-app/configuration/properties.js" type="text/javascript"></script>
</body>
</html>

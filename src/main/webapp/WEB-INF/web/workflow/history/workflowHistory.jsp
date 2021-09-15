<%@ page language="java" pageEncoding="UTF-8" import="com.jc.foundation.util.GlobalContext" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String basePath = GlobalContext.getProperty("api.dataServer")+"/";
%>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html style="height:100%;">
<head>
    <title>流程历史</title>
    <link href="${sysPath}/css/workflowHistory.css?v=633bf02e1b" rel="stylesheet" type="text/css"/>
    <!-- Bootstrap -->
    <!--[if lt IE 9]>
    <script src="${sysPath}/process-editor/editor-app/libs/html5shiv.js"></script>
    <![endif]-->
    <script src="${sysPath}/process-editor/editor-app/libs/jquery_1.11.0/jquery.min.js"></script>
    <%--<script src="${sysPath}/js/app.v2.js"></script>--%>
    <script type="text/javascript">
        function getWidth(){
            return $('#scrollable').width();
        }
        function getHeight(){
            return $(document.body).height() - 80;
        }
        function getRootPath(){
            return "${sysPath}";
        }
    </script>
<body style="height:100%;">
<section class="scrollable padder jcGOA-section" id="scrollable">
    <section class="tabs-wrap m-t-md">
        <ul class="nav nav-tabs">
            <li class="active" id="m1">
                <a href="#messages1" data-toggle="tab-flow">历史信息</a>
            </li>
            <!-- 根据a标签href属性相对应的id，切换tab-pane -->
            <li id="m2">
                <a href="#messages2" data-toggle="tab-flow" type="history">流程图</a>
            </li>
        </ul>
    </section>
    <!-- tabs end -->
    <section class="panel tab-content">
        <div class="tab-pane fade active in" id="messages1">
            <div class="table-wrap">
                <table class="table table-striped" >
                    <thead>
                    <tr>
                        <!-- <th class="w46"><input type="checkbox"/></th> -->
                        <!-- <th>节点名称</th> -->
                        <th style="width:10%">处理人</th>
                        <th style="width:10%">节点名称</th>
                        <th style="width:10%">操作时间</th>
                        <!-- <th>操作</th> -->
                        <th style="width:70%">操作内容</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${workflowHistoryList}" var="historyInfo">
                        <tr class="odd">
                            <td nowrap="nowrap">${historyInfo.userName}</td>
                            <td nowrap="nowrap">${historyInfo.curNodeName}</td>
                            <td nowrap="nowrap">${historyInfo.createTime}</td>
                            <td>${historyInfo.content}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="tab-pane fade" id="messages2" >
            <div class="table-wrap">
                <iframe id="frameT" name="frameT"
                        style="overflow:visible;"
                        scrolling="yes"
                        frameborder="no">
                </iframe>
            </div>
        </div>
    </section>
</section>
<script>
    var $iframe = $("#frameT");

    var iframeUrl = '<%=basePath%>workflowHistory/showHistoryMap.action?definitionId=${definitionId }&instanceId=${instanceId}';

    function setFrameWindow(){
        $iframe.css({
            width : getWidth() + 'px',
            height: getHeight() + 'px'
        });
    }
    $(window).resize(function(){
        setFrameWindow();
    });

    function TabFlow(element){
        this.element = $(element);
    }

    TabFlow.prototype = {
        show: function(callback){
            var $this    = this.element;
            var $ul      = $this.closest('ul');
            var selector = $this.data('target');
            if (!selector) {
                selector = $this.attr('href')
                selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
            }
            if ($this.parent('li').hasClass('active')){
                return false;
            }
            var $target = $(selector);

            this.activate($this.parent('li'), $ul);
            this.activate($target, $target.parent() ,callback);
        },
        activate: function(element, container ,callback){
            var $active    = container.find('> .active');
            $active.removeClass('active').find('> .active').removeClass('active');
            element.addClass('active in');
            $active.removeClass('in');
            callback && callback();
        }
    }

    var old = $.fn.tabFlow;

    $.fn.tabFlow = function ( option ,callback) {
        return this.each(function () {
            var $this = $(this)
            var data  = $this.data('tab.flow');
            if (!data) $this.data('tab.flow', (data = new TabFlow(this)));
            if (typeof option == 'string') data[option](callback);
        })
    }

    $.fn.tabFlow.Constructor = TabFlow;
    // TAB NO CONFLICT
    // ===============
    $.fn.tabFlow.noConflict = function () {
        $.fn.tabFlow = old;
        return this;
    };


    var isDrawCanvas = true;

    $(document).ready(function(){
        $(document).on('click' , '[data-toggle="tab-flow"]',function(e){
            var $self = $(this);
            $self.tabFlow('show' ,function(){
                if($self.attr('type') == 'history' && isDrawCanvas) {
                    $iframe.attr('src' ,iframeUrl);
                    isDrawCanvas = false;
                }
            });
            e.preventDefault();
        });
        setFrameWindow();
    });
</script>
</body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div id="jumpContainer" style="width: 960px; text-align: center;">

</div>
<input id="jumpUrl" type="hidden" value="${jumpUrl}"/>
<script>
    var jumpUrl = $("#jumpUrl").val();
    // window.open(getRootPath()+jumpUrl,"");

    var form = document.createElement('form');
    form.action = jumpUrl;
    form.target = '_blank';
    form.method = 'POST';

    document.body.appendChild(form);
    form.submit();

</script>
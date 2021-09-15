<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<style>
    #u17797_img {
        border-width: 0px;
        position: absolute;
        left: 745px;
        top: 95px;
        width: 60px;
        height: 60px;
    }

    #u17797_text {
        position: absolute;
        align-self: center;
        left: 760px;
        top: 95px;
        padding: 2px 2px 2px 2px;
        box-sizing: border-box;
        width: 100%;
    }
</style>
<div>
    <img id="u17789_img" src="${sysPath}/video/img/u17789.png">
    <div class="ax_default marker" id="u17797">
        <img tabindex="0" id="u17797_img" onclick="clickProjectTarget('0852111')" src="${sysPath}/video/img/u17790.svg">
    </div>
    <div id="projectDisDiv"></div>
    <div id="formDialogDisDiv"></div>
</div>
<script>
    function getRootPath() {
        return '${sysPath}';
    }

    function clickProjectTarget(projectCode) {
        $("#projectDisDiv").load(getRootPath() + "/desktop/manage/projectDesktop.action?projectCode=" + projectCode+"&n="+new Date().getTime(), null, function () {
            projectDesktopInfoJsForm.init({title: '查看'});
        });
    }
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>
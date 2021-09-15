<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<script>
    var tabDefaultid='${id}';
    var loadTablePapPage = {};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>项目管理</span>
            <span>工程信息管理 > </span><span>项目查看 > </span><span>招标信息</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div style="background: #ededed;margin-top:10px;">
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=detail&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">详细信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhaobiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl  gxxxgl-active">招标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhongbiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">中标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongbeian&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同备案</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongzhifu&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同支付情况</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=biangengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">变更单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">工程签证单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">问题协同</a>
        </div>
        <input type="hidden" id="query_${id}" name = "query_${id}"
               operationAction="" operationType="varchar" operationKey="project_num" value="${projectNumber}">
    </section>
    <section class="panel " id="messages1">

    </section>
    <div id="formModule"></div>
</section>
<script>
    $(document).ready(function(){
        $("#messages1").html("")
        $("#messages1").load(getRootPath()+"/dlh/dlhQuery/loadTableMapPage.action?objUrl=pt_bidding_info",null,function(){
            /*myDetailFormModalShow();*/
            // $(".datepicker-input").each(function(){$(this).datepicker();});
            //计算分页显示条数
            // loadTablePapPage.pageCount = TabNub>0 ? TabNub : 10;
            //初始化列表方法
            // loadTablePapPage.dlhDataobjectList();
            // openTableMapPage2()
        });
    });

</script>
<%--<script src='${sysPath}/js/com/jc/csmp/project/info/detail/zhaobiao.js'></script>--%>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
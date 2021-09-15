<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/jctree.jsp" %>

<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>年度计划</span>
            <span>年度计划 > </span><span>年度计划上报汇总</span>
        </div>
    </header>

    <section class="panel clearfix" id="sendPassTransact-list">
        <table class="table table-striped tab_height" id="projectPlanTablexxx">
            <tbody>
            <tr>
                <td align="center" valign="center">
                年度：
                <input type="text" id="selectYear" name="selectYear" value="" style="width:280px;margin-top:10px;">
                &nbsp;
                <input type="button"  class="btn" value="查询" onclick="selectYearOnSearch()">
                &nbsp;
                <input type="button"  class="btn" value="导出" onclick="selectYearOnExport()">
                </td>
            </tr>
            </tbody>
        </table>
        <div class="table-wrap">
            <div id="statisDisplayDiv">
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
    <!--end 查询结果-->
</section>
<script>
    function selectYearOnSearch(){
        projectPlanStatisFun.search();
    }
    function selectYearOnExport(){
        projectPlanStatisFun.selectYearOnExport();
    }
    jQuery(function ($) {
        var myDate = new Date();
        var tYear = myDate.getFullYear();
        $("#selectYear").val(tYear);
        projectPlanStatisFun.search();
    });

</script>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/projectYearPlan/projectYearPlan_statis.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>
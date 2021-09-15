<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/jctree.jsp" %>

<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>年度计划</span>
            <span>年度计划 > </span><span>年度计划上报</span>
        </div>
    </header>

    <section class="panel clearfix" id="sendPassTransact-list">
        <!--   <h2 class="panel-heading clearfix">查询结果</h2>   -->
        <div class="table-wrap">
            <input type="hidden" id="query_planSeqno" name="query_planSeqno" value="${planSeqno}">
            <table class="table table-striped tab_height" id="projectPlanTablexxx">
                <tbody>
                <tr>
                    <td>年度</td>
                    <td><div style="display: inline-block;border: 1px solid #cccccc;height: 32px;" ><select id="selectYear" name="selectYear" value="" style="width:280px;border:none"></select></div></td>
                </tr>
                <tr>
                    <td>地区</td>
                    <td>
                        <div style="display: inline-block;border: 1px solid #cccccc;height: 32px;" > <dic:select name="selectArea" id="selectArea" dictName="plan_area" parentCode="project_year_plan" headName="-请选择-" headValue="" defaultValue="" cssStyle="width:280px;border:none"/></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
                <a class="btn dark" role="button" id="addBtn" onclick="addFun()">新 增</a>
                <a class="btn" role="button" id="returnBtn" onclick="returnFun()">返 回</a>
                <br>
            </section>
        </section>
    </section>
    <!--end 查询结果-->
</section>
<script>
    function returnFun() {
        window.location.href = getRootPath() + "/plan/projectYearPlan/manageReq.action?n_=" + (new Date().getTime());
    }
    function addFun() {
        var selectYear = $("#selectYear").val();
        if (!isRealNum(selectYear) || parseInt(selectYear) < 2005 || parseInt(selectYear) > 2100) {
            msgBox.tip({type: "fail", content: "请输入合法年度"});
            return;
        }
        var areaCode = $("#selectArea").val();
        if (areaCode == '') {
            msgBox.tip({type: "fail", content: "请选择地区"});
            return;
        }
        var planSeqno = $("#query_planSeqno").val();
        var areaName = $("#selectArea").find("option:selected").text();
        var condition = "0," + selectYear + "," + areaCode + "," + areaName + "," +planSeqno;
        var chheckUrl = getRootPath() + "/plan/projectYearPlan/checkRepetition.action?selectYear=" + selectYear + "&selectAreaCode=" + areaCode + "&planSeqno=" + planSeqno + "&n_=" + (new Date().getTime());
        jQuery.ajax({
            url: chheckUrl,
            async: false,
            type: 'GET',
            success: function (data) {
                if (data.code == "0") {
                    window.location.href = getRootPath() + "/instance/toStartProcess.action?definitionKey_=yearplan001&condition=" + condition;
                } else {
                    msgBox.info({
                        type: "fail",
                        content: data.msg
                    });
                }
            }
        });


    }

    jQuery(function ($) {
        initSelectYear($("#selectYear"));
    });

</script>
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>
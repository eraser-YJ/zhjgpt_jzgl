<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/jctree.jsp" %>

<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>年度计划</span>
            <span>年度计划 > </span><span>年度计划统计</span>
        </div>
    </header>

    <section class="panel clearfix" id="sendPassTransact-list">
        <!--   <h2 class="panel-heading clearfix">查询结果</h2>   -->
        <div class="table-wrap">
            <table class="table table-striped tab_height" id="projectPlanTablexxx">
                <tbody>
                <tr>
                    <td>年度</td>
                    <td><div style="display: inline-block;border: 1px solid #cccccc;height: 32px;" > <select id="selectYear" name="selectYear" value="" style="width:280px;border:none"></select></div></td>
                </tr>
                <tr>
                    <td>月度</td>
                    <td>
                        <div style="display: inline-block;border: 1px solid #cccccc;height: 32px;" >
                        <select id="selectMonth" name="selectMonth" style="width:280px;border:none">
                            <option value="1">1月</option>
                            <option value="2">2月</option>
                            <option value="3">3月</option>
                            <option value="4">4月</option>
                            <option value="5">5月</option>
                            <option value="6">6月</option>
                            <option value="7">7月</option>
                            <option value="8">8月</option>
                            <option value="9">9月</option>
                            <option value="10">10月</option>
                            <option value="11">11月</option>
                            <option value="12">12月</option>
                        </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>地区</td>
                    <td>
                        <div style="display: inline-block;border: 1px solid #cccccc;height: 32px;" >
                        <dic:select name="selectArea" id="selectArea" dictName="plan_area"
                                    parentCode="project_year_plan" headName="-请选择-" headValue="" defaultValue=""
                                    cssStyle="width:280px;border:none"/>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
                <a class="btn dark" role="button" id="addBtn" onclick="addFun()">生 成</a>
                <a class="btn" role="button" id="returnBtn" onclick="returnFun()">返 回</a>
                <br>
            </section>
        </section>
    </section>
    <!--end 查询结果-->
</section>
<script>
    function returnFun() {
        window.location.href = getRootPath() + "/plan/projectMonthPlan/manage.action?n_=" + (new Date().getTime());
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
        var areaName = $("#selectArea").find("option:selected").text();
        var selectMonth = $("#selectMonth").val();
        var condition = "0," + selectYear + "," + areaCode + "," + areaName + "," + selectMonth;
        var chheckUrl = getRootPath() + "/plan/projectMonthPlan/checkRepetition.action?selectYear=" + selectYear + "&selectAreaCode=" + areaCode + "&selectMonth=" + selectMonth + "&n_=" + (new Date().getTime());
        jQuery.ajax({
            url: chheckUrl,
            async: false,
            type: 'POST',
            success: function (data) {
                if (data.code == "0") {
                    window.location.href = getRootPath() + "/plan/projectMonthPlan/loadForm.action?n_=" + (new Date().getTime());
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
<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContainer" style="width: 648px; text-align: center;">
    <div style="width: 100%; height: 50px; line-height: 50px; text-align: center; font-size: 24px; font-weight: bold;">工程签证单</div>
    <div style="width: 100%; text-align: left; margin: 20px 0px 10px 20px; font-size: 16px;">
        <div style="margin: 10px 0px 10px 20px; float: left; text-align: left; font-size: 16px; width: 45%;" id="printCodeHtml">编号：</div>
        <div style="margin: 10px 20px 10px 0px; float: left; text-align: right; font-size: 16px; width: 45%;" id="printDateHtml">日期：</div>
    </div>
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <tr style="height: 50px;">
            <td style="text-align: center; width: 107px; border:1px solid #000000;">工程名称</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printProjectHtml"></td>
            <td style="text-align: center; width: 107px; border:1px solid #000000;">签证部门</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printDeptHtml"></td>
        </tr>
        <tr style="min-height: 280px;">
            <td style="text-align: center; border:1px solid #000000;">签证原因</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printReasonHtml"></td>
        </tr>
        <tr style="min-height: 320px;">
            <td style="text-align: center; border:1px solid #000000;">签证内容<br />及工作量</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printContentHtml"></td>
        </tr>
        <tr style="height: 220px;">
            <td style="border:1px solid #000000; padding:0px !important; text-align: left; width: 216px;" colspan="2">
                <ul style="line-height:50px;list-style: none; text-align: left;padding-left:5px !important;"><li>施工单位签证意见</li><li>项目经理：</li><li>(单位盖章)</li><li>日期：</li></ul>
            </td>
            <td style="border:1px solid #000000; padding:0px !important; text-align: left; width: 216px;" colspan="2">
                <ul style="line-height:50px;list-style: none; text-align: left;padding-left:5px !important;"><li>监理单位签证意见</li><li>项目经理：</li><li>(单位盖章)</li><li>日期：</li></ul>
            </td>
            <td style="border:1px solid #000000; padding:0px !important; text-align: left; width: 216px;" colspan="2">
                <ul style="line-height:50px;list-style: none; text-align: left;padding-left:5px !important;"><li>建设单位签证意见</li><li>项目经理：</li><li>(单位盖章)</li><li>日期：</li></ul>
            </td>
        </tr>
    </table>
</div>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
    var visaPage = {};
    visaPage.data= null;
    visaPage.init = function (option) {
        visaPage.data= option.data;
        console.log(visaPage.data);
        $('#' + option.btnId).show();
        $('#' + option.btnId).click(function () { visaPage.print() });
    };
    visaPage.print = function() {
        $('#printCodeHtml').html(visaPage.data.code == null ? "" : "编号：" + visaPage.data.code);
        $('#printDateHtml').html(visaPage.data.visaDate == null ? "" : "日期：" + visaPage.data.visaDate);
        $('#printProjectHtml').html(visaPage.data.projectName == null ? "" : visaPage.data.projectName);
        $('#printDeptHtml').html(visaPage.data.createUserDeptValue == null ? "" : visaPage.data.createUserDeptValue);
        $('#printReasonHtml').html(visaPage.data.visaReason == null ? "" : visaPage.data.visaReason);
        $('#printContentHtml').html(visaPage.data.visaChange == null ? "" : visaPage.data.visaChange);
        lodopFunction.print({content: document.getElementById("printContainer").innerHTML});
    };
</script>
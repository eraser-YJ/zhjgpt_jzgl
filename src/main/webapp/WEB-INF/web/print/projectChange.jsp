<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContainer" style="width: 648px; text-align: center;">
    <div style="width: 100%; height: 50px; line-height: 50px; text-align: center; font-size: 24px; font-weight: bold;">工程变更单</div>
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <tr style="height: 50px;">
            <td style="text-align: center; width: 107px; border:1px solid #000000;">工程名称</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printProjectHtml"></td>
            <td style="text-align: center; width: 107px; border:1px solid #000000;">变更日期</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printChangeDateHtml"></td>
        </tr>
        <tr style="height: 50px;">
            <td style="text-align: center; border:1px solid #000000;">提出单位</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printDeptHtml"></td>
            <td style="text-align: center; border:1px solid #000000;">变更单号</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printCodeHtml"></td>
        </tr>
        <tr style="min-height: 300px;">
            <td style="text-align: center; border:1px solid #000000;">变更理由</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printReasonHtml"></td>
        </tr>
        <tr style="min-height: 300px;">
            <td style="text-align: center; border:1px solid #000000;">变更内容</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printContentHtml"></td>
        </tr>
        <tr style="height: 200px;">
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">建设单位代表<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">设计单位代表<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">项目监理机构<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
        </tr>
    </table>
</div>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
    var projectChangePage = {};
    projectChangePage.data= null;
    projectChangePage.init = function (option) {
        projectChangePage.data= option.data;
        $('#' + option.btnId).show();
        $('#' + option.btnId).click(function () { projectChangePage.print() });
    };
    projectChangePage.print = function() {
        $('#printProjectHtml').html(projectChangePage.data.projectName == null ? "" : projectChangePage.data.projectName);
        $('#printChangeDateHtml').html(projectChangePage.data.changeDate == null ? "" : projectChangePage.data.changeDate);
        $('#printDeptHtml').html(projectChangePage.data.deptName == null ? "" : projectChangePage.data.deptName);
        $('#printCodeHtml').html(projectChangePage.data.code == null ? "" : projectChangePage.data.code);
        $('#printReasonHtml').html(projectChangePage.data.changeReason == null ? "" : projectChangePage.data.changeReason);
        $('#printContentHtml').html(projectChangePage.data.changeContent == null ? "" : projectChangePage.data.changeContent);
        lodopFunction.print({content: document.getElementById("printContainer").innerHTML});
    };
</script>
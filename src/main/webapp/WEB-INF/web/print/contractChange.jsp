<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContractContainer" style="width: 648px; text-align: center;">
    <div style="width: 100%; height: 50px; line-height: 50px; text-align: center; font-size: 24px; font-weight: bold;">工程变更单</div>
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <tr style="height: 50px;">
            <td style="text-align: center; width: 107px; border:1px solid #000000;">工程名称</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractProjectHtml"></td>
            <td style="text-align: center; width: 107px; border:1px solid #000000;">变更日期</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractChangeDateHtml"></td>
        </tr>
        <tr style="height: 50px;">
            <td style="text-align: center; border:1px solid #000000;">提出单位</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractDeptHtml"></td>
            <td style="text-align: center; border:1px solid #000000;">变更单号</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractCodeHtml"></td>
        </tr>
        <tr style="height: 50px;">
            <td style="text-align: center; width: 107px; border:1px solid #000000;">合同编号</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractContractCodeHtml"></td>
            <td style="text-align: center; width: 107px; border:1px solid #000000;">合同名称</td>
            <td colspan="2" style="border:1px solid #000000; padding-left: 10px;" id="printContractContractNameHtml"></td>
        </tr>
        <tr style="min-height: 250px;">
            <td style="text-align: center; border:1px solid #000000;">变更理由</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printContractReasonHtml"></td>
        </tr>
        <tr style="min-height: 300px;">
            <td style="text-align: center; border:1px solid #000000;">变更内容</td>
            <td colspan="5" style="border:1px solid #000000; padding-left: 10px;" id="printContractContentHtml"></td>
        </tr>
        <tr style="height: 200px;">
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">建设单位代表<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">设计单位代表<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
            <td style="border:1px solid #000000; padding-left: 10px; width: 216px;" colspan="2">项目监理机构<br /><br /><br /><br />签字<br /><br /><br /><br />日期：</td>
        </tr>
    </table>
</div>
<script>
    var contractChangePage = {};
    contractChangePage.data= null;
    contractChangePage.init = function (option) {
        contractChangePage.data= option.data;
        $('#' + option.btnId).show();
        $('#' + option.btnId).click(function () { contractChangePage.print() });
    };
    contractChangePage.print = function() {
        $('#printContractProjectHtml').html(contractChangePage.data.projectName == null ? "" : contractChangePage.data.projectName);
        $('#printContractChangeDateHtml').html(contractChangePage.data.changeDate == null ? "" : contractChangePage.data.changeDate);
        $('#printContractContractCodeHtml').html(contractChangePage.data.contractCode == null ? "" : contractChangePage.data.contractCode);
        $('#printContractContractNameHtml').html(contractChangePage.data.contractName == null ? "" : contractChangePage.data.contractName);
        $('#printContractDeptHtml').html(contractChangePage.data.deptName == null ? "" : contractChangePage.data.deptName);
        $('#printContractCodeHtml').html(contractChangePage.data.code == null ? "" : contractChangePage.data.code);
        $('#printContractReasonHtml').html(contractChangePage.data.changeReason == null ? "" : contractChangePage.data.changeReason);
        $('#printContractContentHtml').html(contractChangePage.data.changeContent == null ? "" : contractChangePage.data.changeContent);
        lodopFunction.print({content: document.getElementById("printContractContainer").innerHTML});
    };
</script>
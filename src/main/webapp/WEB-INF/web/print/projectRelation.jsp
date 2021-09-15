<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContainer" style="width: 648px; text-align: center; display: none;">
    <div style="width: 100%; text-align: center; font-size: 24px; font-weight: bold;">工程联系单</div>
    <div style="width: 100%; text-align: left; margin: 20px 0px 10px 20px; font-size: 16px;" id="printCodeHtml"></div>
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <tr style="height: 50px;">
            <td style="text-align: center; width: 150px; border:1px solid #000000;">工程名称</td>
            <td style="border:1px solid #000000; padding-left: 10px;" id="printProjectHtml"></td>
        </tr>
        <tr style="height: 50px;">
            <td style="text-align: center; width: 150px; border:1px solid #000000;">接收单位</td>
            <td style="border:1px solid #000000; padding-left: 10px;" id="printDeptHtml"></td>
        </tr>
        <tr style="min-height: 700px;">
            <td style="text-align: center; width: 150px; border:1px solid #000000;">联系内容</td>
            <td style="border:1px solid #000000; padding-left: 10px;" id="printContentHtml"></td>
        </tr>
        <tr style="height: 100px;">
            <td style="text-align: center; width: 150px; border:1px solid #000000;">抄送组织</td>
            <td style="border:1px solid #000000; padding-left: 10px;" id="printOtherDeptHtml"></td>
        </tr>
    </table>
</div>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
    var relationPage = {};
    relationPage.data= null;
    relationPage.init = function (option) {
        relationPage.data= option.data;
        $('#' + option.btnId).show();
        $('#' + option.btnId).click(function () { relationPage.print() });
    };
    relationPage.print = function() {
        $('#printCodeHtml').html(relationPage.data.code == null ? "" : "编号：" + relationPage.data.code);
        $('#printProjectHtml').html(relationPage.data.projectName == null ? "" : relationPage.data.projectName);
        $('#printDeptHtml').html(relationPage.data.signerDeptValue == null ? "" : relationPage.data.signerDeptValue);
        $('#printContentHtml').html(relationPage.data.content == null ? "" : relationPage.data.content);
        var receiverDeptValue = relationPage.data.receiverDeptValue;
        if (receiverDeptValue == null || receiverDeptValue.length == 0) {
            $('#printOtherDeptHtml').html("");
        } else {
            var array = [];
            for (var i = 0; i < receiverDeptValue.length; i++) {
                array.push(receiverDeptValue[i].text);
            }
            $('#printOtherDeptHtml').html(array.join(','));
        }
        lodopFunction.print({content: document.getElementById("printContainer").innerHTML});
    };
</script>
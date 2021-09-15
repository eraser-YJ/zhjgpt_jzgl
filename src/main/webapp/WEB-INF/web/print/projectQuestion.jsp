<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContainer" style="width: 960px; text-align: center;">
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <thead>
        <tr style="height: 50px;">
            <th style="text-align: center; font-size: 24px; font-weight: bold;" colspan="8" id="printTitle"></th>
        </tr>
        <tr style="height: 50px;">
            <th style="text-align: center; width: 60px; border:1px solid #000000;">编号</th>
            <th style="text-align: center; width: 140px; border:1px solid #000000;">问题名称</th>
            <th style="text-align: center; width: 140px; border:1px solid #000000;">所属合同</th>
            <th style="text-align: center; width: 110px; border:1px solid #000000;">处置负责人</th>
            <th style="text-align: center; width: 140px; border:1px solid #000000;">问题描述</th>
            <th style="text-align: center; width: 140px; border:1px solid #000000;">整改要求</th>
            <th style="text-align: center; width: 140px; border:1px solid #000000;">整改结果</th>
            <th style="text-align: center; width: 100px; border:1px solid #000000;">备注</th>
        </tr>
        </thead>
        <tbody id="printContainerTbody"></tbody>
    </table>
</div>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
    var projectQuestion = {};
    projectQuestion.toEmpty = function(value) {
        return value == null || value == 'null' ? "" : value;
    }
    projectQuestion.print = function(projectNumber, questionType) {
        if (projectNumber == "") {
            msgBox.info({ content: "请输入项目统一编码进行查询并打印" });
            return;
        }
        $.ajax({
            type: "GET", url: getRootPath() + "/project/question/getListByProjectNumber.action", data: {projectNumber: projectNumber, questionType: questionType}, dataType: "json",
            success : function(data) {
                if (data.code != 0) {
                    msgBox.info({ content: data.msg });
                    return;
                }
                var content = "";
                if (data.data.length == 0) {
                    content += "<tr><td style='border:1px solid #000000;' colspan='8'>暂无现场问题</td></tr>";
                } else {
                    for (var i = 0; i < data.data.length; i++) {
                        var row = data.data[i];
                        if (i == 0) {
                            $('#printTitle').html(row.projectName + "项目现场问题登记表");
                        }
                        content += "<tr>";
                        content += "<td style='border:1px solid #000000; text-align: center; font-size: 12px;'>" + row.code + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + row.title + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + row.contractName + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + row.problemDeptValue + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectQuestion.toEmpty(row.questionMeta) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectQuestion.toEmpty(row.rectificationAsk) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectQuestion.toEmpty(row.rectificationResult) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectQuestion.toEmpty(row.remark) + "</td>";
                        content += "</tr>";
                    }
                }
                $('#printContainerTbody').html(content);
                var LODOP = getLodop();
                LODOP.PRINT_INIT("");
                LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "");
                LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
                LODOP.ADD_PRINT_TABLE(25, 70, 970, 648, document.getElementById("printContainer").innerHTML);
                LODOP.PREVIEW();
            }
        });
    };
</script>
<%@ page language="java" pageEncoding="UTF-8"%>
<div id="printContainer" style="width: 960px; text-align: center;">
    <table style="width: 100%; margin: 0 auto; border-collapse:collapse;border:none #0066CC;background-color: #ffffff;color: #000000;">
        <thead>
        <tr style="height: 50px;">
            <th style="text-align: center; font-size: 24px; font-weight: bold;" colspan="7" id="printTitle">项目进度计划表</th>
        </tr>
        <tr style="height: 50px;">
            <th style="text-align: center; width: 60px; border:1px solid #000000;">序号</th>
            <th style="text-align: center; width: 220px; border:1px solid #000000;">阶段名称</th>
            <th style="text-align: center; width: 220px; border:1px solid #000000;">任务名称</th>
            <th style="text-align: center; width: 120px; border:1px solid #000000;">计划开始时间</th>
            <th style="text-align: center; width: 120px; border:1px solid #000000;">计划完成时间</th>
            <th style="text-align: center; width: 70px; border:1px solid #000000;">已完进度</th>
            <th style="text-align: center; width: 170px; border:1px solid #000000;">备注</th>
        </tr>
        </thead>
        <tbody id="printContainerTbody"></tbody>
    </table>
</div>
<script src='${sysPath}/js/com/jc/common/LodopFuncs.js'></script>
<script>
    var projectInfo = {};
    projectInfo.toEmtpy = function(value) {
        return value == null || value == 'null' ? "" : value;
    };
    projectInfo.print = function(projectId) {
        $.ajax({
            type: "GET", url: getRootPath() + "/project/plan/info/getPlanByProjectId.action", data: {projectId: projectId}, dataType: "json",
            success : function(data) {
                if (data.code != 0) {
                    msgBox.info({ content: data.msg });
                    return;
                }
                var content = "";
                if (data.data.length == 0) {
                    content += "<tr><td style='border:1px solid #000000;' colspan='7'>暂无计划信息</td></tr>";
                } else {
                    content += "<tr>";
                    for (var i = 0; i < data.data.length; i++) {
                        var row = data.data[i];
                        if (i == 0) {
                            $('#printTitle').html(row.projectName + "项目进度计划表");
                        }
                        content += "<tr>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + (i + 1) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + row.stageName + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + row.planName + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectInfo.toEmtpy(row.planStartDate) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectInfo.toEmtpy(row.planEndDate) + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + (row.completionRatio == null ? "0%" : row.completionRatio + "%") + "</td>";
                        content += "<td style='border:1px solid #000000; text-align: center;'>" + projectInfo.toEmtpy(row.remark) + "</td>";
                        content += "</tr>";
                    }
                    content += "</tr>";
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
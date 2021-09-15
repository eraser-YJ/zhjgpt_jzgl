<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<style>
.lc-content{width:1040px; margin: 0 auto; }
.work{ width: 1040px; height: 145px; background: #eeeced; margin: 30px auto; position: relative; border-radius: 10px;}
.frame{display: inline-block; background: #bababa; width: 140px; height: 45px; line-height: 45px; border-radius: 8px; color: #fff; text-align: center; font-size: 14px;}
.frame.active{ background: #0c9ae3; color: #fff; }
.start{ position: absolute; margin-top: 50px; margin-left: 82px; }
.startMoreLine { position: absolute; margin-top: 50px; margin-left: 82px; margin-top: 20px; }
.moreItem{ width: 140px; position: absolute; top: 20px;}
.moreItemDown{ position: relative; margin-top: 13px; }
.moreItemUp{ position: relative; }
.moreItemHe{ margin-top: 13px; }
.item{position: absolute; top:47px;}
.dottedLine {display: block; background: #FFFFFF; color:#000; border:2px dotted #000;}

.fenLeft{ display: block; width: 48px; height: 70px; background: url(${sysPath}/images/flow/fenLeft.png) no-repeat; position: absolute; left: -45px; top: -11px; }
.fenRight{ display: block; width: 48px; height: 80px; background: url(${sysPath}/images/flow/fenRight.png) no-repeat; position: absolute; left: 140px; top: -12px;}
.lineLeft{ display: block; width: 45px; height: 20px; background: url(${sysPath}/images/flow/lineLeft.png) no-repeat; position: absolute; left: -44px; top: 15px; }
.lineRight{ display: block; width: 45px; height: 20px; background: url(${sysPath}/images/flow/lineRight.png) no-repeat; position: absolute; left: 140px; top: 15px;}
.heLeft{ display: block; width: 48px; height: 70px; background: url(${sysPath}/images/flow/heLeft.png) no-repeat; position: absolute; left: -45px; top: 17px; }
.heRight{ display: block; width: 48px; height: 80px; background: url(${sysPath}/images/flow/heRight.png) no-repeat; position: absolute; left: 140px; top: 15px; }
.zheFenLeft{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheFenLeft.png) no-repeat; position: absolute; left: 140px; top: 20px; z-index: 100; }
.zheFenRight{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheFenRight.png) no-repeat; position: absolute; left: -45px; top: 20px; z-index: 100; }
</style>
<%
    int text = 140, start = 82, line = 45;
%>
<div class="modal fade panel" id="flowImages-modal" aria-hidden="false">
    <div class="modal-dialog w1100">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#flowImages-modal').modal('hide')">×</button>
                <h4 class="modal-title" id="entityFormTitle"></h4>
            </div>
            <div class="modal-body">
                <div class="lc-content">
                    <!-- line1 -->
                    <div class="work">
                        <div class="start"><span class="frame active">项目报建</span><i class="fenRight"></i></div>
                        <div class="moreItem" style="left: <%=(start + text + line) %>px;">
                            <div class="moreItemUp"><span class="frame">勘察中标通知书</span><i class="lineRight"></i></div>
                            <div class="moreItemDown"><span class="frame">设计中标通知书</span><i class="lineRight"></i></div>
                        </div>
                        <div class="moreItem" style="left: <%=(start + text * 2 + line * 2) %>px;">
                            <div><span class="frame">勘察合同备案</span></div>
                            <div class="moreItemHe"><span class="frame">设计合同备案</span><i class="heRight"></i></div>
                        </div>
                        <div class="item" style="left: <%=(start + text * 3 + line * 3) %>px;"><span class="frame">初步设计审查</span><i class="lineRight"></i></div>
                        <div class="item" style="left: <%=(start + text * 4 + line * 4 ) %>px;"><span class="frame">初步设计审查</span><i class="zheFenLeft"></i></div>
                    </div>
                    <!-- line2 -->
                    <div class="work">
                        <div class="startMoreLine">
                            <div><span class="frame">工程质量监督登记</span></div>
                            <div class="moreItemHe"><span class="frame">工程安全监督登记</span><i class="zheFenRight"></i></div>
                        </div>
                        <div class="item" style="left: <%=(start + text + line) %>px;"><span class="frame">施工许可证</span><i class="fenLeft"></i></div>
                        <div class="moreItem" style="left: <%=(start + text * 2 + line * 2) %>px; ">
                            <div><span class="frame">工程质量监督登记</span></div>
                            <div class="moreItemHe"><span class="frame">工程安全监督登记</span><i class="heLeft"></i></div>
                        </div>
                        <div class="moreItem" style="left: <%=(start + text * 3 + line * 3) %>px;">
                            <div class="moreItemUp"><span class="frame">施工合同备案</span><i class="lineLeft"></i></div>
                            <div class="moreItemDown"><span class="frame">监理合同备案</span><i class="lineLeft"></i></div>
                        </div>
                        <div class="moreItem" style="left: <%=(start + text * 4 + line * 4) %>px;">
                            <div class="moreItemUp"><span class="frame">施工中标通知书</span><i class="lineLeft"></i></div>
                            <div class="moreItemDown"><span class="frame">监理中标通知书</span><i class="lineLeft"></i></div>
                        </div>
                    </div>
                    <!-- line3 -->
                    <div class="work">
                        <div class="start"><span class="frame">工程档案预验收</span><i class="lineRight"></i></div>
                        <div class="item" style="left: <%=(start + text + line) %>px;"><span class="dottedLine frame">建设工程竣工验收</span><i class="lineRight"></i></div>
                        <div class="item" style="left: <%=(start + text * 2 + line * 2) %>px;"><span class="dottedLine frame">竣工结算备案</span><i class="lineRight"></i></div>
                        <div class="item" style="left: <%=(start + text * 3 + line * 3) %>px;"><span class="frame">竣工验收备案</span></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" onclick="$('#flowImages-modal').modal('hide')">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    var resourceProjectFlowImagesPanel = {};
    resourceProjectFlowImagesPanel.init = function (config) {
        $('#entityFormTitle').html(config.title);
        $('#flowImages-modal').modal('show');
        /*
        $.ajax({
            url: getRootPath() + "/supervise/resource/detail.action",
            type : "GET", data : {busColumn: config.column, busPk: config.id, resObj: config.resObj}, dataType : "json",
            success : function(data) {
                var content = "";
                if (data != null) {
                    for (var i = 0 ; i < data.length; i++) {
                        var row = data[i];
                        if (row.formShow == '0') {
                            continue;
                        }
                        content += "<tr>";
                        content += "<td style='width: 150px; text-align: right;'>" + row.title + "</td>";
                        content += "<td style='text-align: left;'>" + row.value + "</td>";
                        content += "</tr>";
                    }
                }
                $('#renderTable').html(content);
                $('#flowImages-modal').modal('show');
            }
        });*/
    };
</script>
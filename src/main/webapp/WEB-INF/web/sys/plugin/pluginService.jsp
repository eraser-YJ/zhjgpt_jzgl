<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-pluginService" aria-hidden="false">
        <div class="modal-dialog" style="width:1180px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">依赖</h4>
                </div>
                <div class="modal-body">
	                <div class="tab-content">
	                	<div id="pluginServiceDiv" class="w1100" style="width: 1100px"></div>
		            </div>
	              
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" id="savePluginService">保  存</button>
                    <button class="btn" type="button" id="closePluginService">关  闭</button>
                </div>
            </div>
        </div>
        <input type="hidden" id="pluginId" value="0" />
    </div>

<script type="text/javascript" src="${sysPath}/js/com/sys/plugin/pluginService.js"></script>

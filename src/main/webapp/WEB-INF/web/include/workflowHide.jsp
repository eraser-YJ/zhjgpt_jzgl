<%@ taglib prefix="workflow" uri="/WEB-INF/tlds/workflow.tld" %>
<script type='text/javascript' src='${sysPath}/js/com/jc/workflow/signature/handwritten.js'></script>
<script src="${sysPath}/js/com/jc/workflow/leftRightSelect.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/jc/workflow/customProperty.js" type="text/javascript"></script>
<input type="hidden" id="suggestType" value="2">
<%@ include file="/workflow/plugin/websign.jsp"%>

<input type="hidden" workFlowType='true' id="workflowBean.curNodeId_" name="workflowBean.curNodeId_" value="${workflowBean.curNodeId_}" />
<input type="hidden" workFlowType='true' id="workflowBean.definitionId_" name='workflowBean.definitionId_' value="${workflowBean.definitionId_}" />
<input type="hidden" id="workflowBean.subProcessId_" value="${workflowBean.subProcessId_}" />
<input type="hidden" id="workflowBean.flowStatus_" value="${workflowBean.flowStatus_}" />
<input type="hidden" id="workflowBean.lastOperType_" value="${workflowBean.lastOperType_}" />
<input type="hidden" id="workflowBean.isChangeDone" value="false" />
<input type="hidden" id="workflowBean.openType_" value="${workflowBean.openType_}" />
<input type="hidden" workFlowType='true' id="workflowBean.business_Key_" name='workflowBean.business_Key_' value="${workflowBean.business_key_}" />
<input type='hidden' workFlowType='true' id='workflowBean.confirmUserId_' name='workflowBean.confirmUserId_' />
<input type='hidden' workFlowType='true' id='workflowBean.confirmNodeId_' name='workflowBean.confirmNodeId_' />
<input type='hidden' workFlowType='true' id='workflowBean.confirmRouteId_' name='workflowBean.confirmRouteId_' />
<input type='hidden' workFlowType='true' id='workflowBean.submitType_' name='workflowBean.submitType_' />
<input type="hidden" workFlowType='true' id="workflowBean.taskId_" name='workflowBean.taskId_' value="${workflowBean.taskId_}" />
<input type="hidden" workFlowType='true' id="workflowBean.instanceId_" name='workflowBean.instanceId_' value="${workflowBean.instanceId_}" />
<input type="hidden" id="workflowBean.buttonJsonStr" value='${workflowBean.buttonJsonStr}'/>
<input type="hidden" id="workflowBean.buttonId" value=''/>
<input type="hidden" id="workflowBean.buttonOptJsonStr" value='${workflowBean.buttonOptJsonStr}'/>
<input type="hidden" id="workflowBean.formItemPrivJsonStr" value='${workflowBean.formItemPrivJsonStr}'/>
<input type="hidden" id="workflowBean.jctreeUrls" value=''/>

<!-- 意见域相关 -->
<input type="hidden" name="signInfoOld" id="signInfoOld" value="${workflowBean.signInfo_}">
<input type="hidden" id="workflowBean.signInfoFlag_" value="false">
<input workFlowType="true" type="hidden" name="workflowBean.signInfo_" id="workflowBean.signInfo_">
<input workFlowType="true" type="hidden" name="workflowBean.message_" id="workflowBean.message_">
<input workFlowType="true" type="hidden" name="workflowBean.suggestId_" id="workflowBean.suggestId_">
<input workFlowType="true" type="hidden" name="workflowBean.signContainerId_" id="workflowBean.signContainerId_">
<input workFlowType="true" type="hidden" name="messageArray" id="messageArray">
<input workFlowType="true" type="hidden" name="suggestIdArray" id="suggestIdArray">
<input workFlowType="true" type="hidden" name="signContainerIdArray" id="signContainerIdArray">

<!-- 附言相关 -->
<input workFlowType="true" type="hidden" name="workflowBean.postscriptData" id="workflowBean.postscriptData" >
<input workFlowType="true" type="hidden" name="workflowBean.postscriptReplyData" id="workflowBean.postscriptReplyData" >
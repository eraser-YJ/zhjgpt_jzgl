var form = {};

form.submitState = false;

form.type = {};
//提交相关
form.type["Submit"] = {
    show: function () {

        if(!initSuggestValidate()){
            return false;
        }

        if (!handleWorkflowFieldCustomProperty("Submit")) {
            return false;
        }
        
        if(!handWritten.checkSignData()) {
        	msgBox.info({
	    		content: "请填写或签批意见"
	    	});
        	
			return false;
		}
        
        if(typeof validateForm=='function'){
            var validResult = validateForm("Submit");
            if(!validResult||validResult==null){
                if(typeof validateFormFail=='function'){
                    validateFormFail();
                }
                return false;
            }else{
                if(typeof(validResult) == "string") {
                    if(typeof validateFormFail=='function'){
                        validateFormFail(validResult);
                    }
                    return false;
                }
            }
        }

        var formDataJson = flowVariable.getFlowVariable($("#workflowBean\\.curNodeId_").val(), $("#workflowBean\\.definitionId_").val(), $("#workflowBean\\.instanceId_").val()).join('');
        $.ajax({
            type: "POST",
            url: getRootPath() + "/task/getNextNodes.action",
            data: eval('(' + formDataJson + ')'),
            dataType: "json",
            success: function (data) {
                var choice = data.curNode.choice;
                var nextNodesList = data.nextNodesList;
				var tempNodesList = [];
                var buttonOptJsonStr = $("#workflowBean\\.buttonOptJsonStr").val();
                
                if (buttonOptJsonStr != "") {
                	var buttonOptJson = eval("(" + buttonOptJsonStr + ")");
                    var submitNodeList = buttonOptJson.operateSubmitNode;
                    
                    if (typeof submitNodeList != 'undefined' && submitNodeList.length > 0) {
                    	if (submitNodeList.filter(function(e) {return e.selected;}).length > 0) {
                    		for (var i = 0; i < nextNodesList.length; i++) {
                        		var tempList = submitNodeList.filter(function(e) {
                        			if (e.id === nextNodesList[i].componentId && e.selected) {
                        				if (e.group) {
                        					nextNodesList[i].group = e.group;                        					
                        				} else {
                        					nextNodesList[i].group = "";
                        				}
                        				
                        				return true;
                        			} else {
                        				return false;
                        			}
                        		});
                        		
                        		if (tempList && tempList.length > 0) {
                        			tempNodesList.push(nextNodesList[i]);
                            	}
                            }
                    		
                    		if (tempNodesList && tempNodesList.length > 1) {
                    			tempNodesList.sort(function(a,b){
	                				return a.group - b.group;
                				});
                    		}
                        	
                        	nextNodesList = tempNodesList;
                    	}
                    }
                }
                
                var nodesHtml = "";
                if (nextNodesList.length == 1) {
                    nodesHtml += "<input type='radio' name='selectNodes' checked='checked' value='" + nextNodesList[0].componentId + "' />" + nextNodesList[0].name;
                    $("#nodesDiv").html(nodesHtml);
                    form.toSelectUser(nextNodesList);
                } else {
                    $('#submitNodesModal').modal('show');
                    
                    var preGroup = "";
                    for (var i = 0; i < nextNodesList.length; i++) {
                     //   console.log(nextNodesList[i]);
                        if (choice == "single") {
                            nodesHtml += "<label class='radio inline'><input type='radio' name='selectNodes' value='" + nextNodesList[i].componentId + "' />" + nextNodesList[i].name + "</label>";
                        } else {
                        	if (preGroup && preGroup != "" && preGroup != nextNodesList[i].group) {
                        		nodesHtml += "<br>";
                        	}
                        	
                    		nodesHtml += "<input type='checkbox' name='selectNodes_"+nextNodesList[i].group+"' value='" + nextNodesList[i].componentId + "' onClick='workflowCheckNodes(this)' />" + nextNodesList[i].name;
                        	
                        	preGroup = nextNodesList[i].group;
                        }
                    }
                }
                $("#nodesDiv").html(nodesHtml);
                $("#submitNodesModal #submitBtn").unbind('click').bind('click',function () {
                    form.toSelectUser(nextNodesList);
                });
            }
        });
    },
    submit: function () {
        if (form.submitState) {
            return;
        }
        form.submitState = true;
        var confirmUserId = "";
        var confirmNodeId = "";
        var confirmRouteId = "";
        var confirmUserFlag = false;
        $('[id^=userSelectValue]').each(function (index, item) {

            if ($(item).val() == null || $(item).val() == '') {
                confirmUserFlag = true;
            }
            if (confirmUserId.length == 0) {
                confirmUserId = $(item).val();
            } else {
                confirmUserId += "&" + $(item).val();
            }
        });

        if (confirmUserFlag) {
            msgBox.tip({
                type: "fail",
                content: "请选择人员"
            });
            form.submitState = false;
            return false;
        }

        $("#submitUserSelectModal [name='confirmNodeId']").each(function (index, item) {
            if (confirmNodeId.length == 0) {
                confirmNodeId = $(item).val();
            } else {
                confirmNodeId += "&" + $(item).val();
            }
        });

        $("#submitUserSelectModal [name='confirmSequencesId']").each(function (index, item) {
            if (confirmRouteId.length == 0) {
                confirmRouteId = $(item).val();
            } else {
                confirmRouteId += "&" + $(item).val();
            }
        });


        $("#workflowBean\\.confirmUserId_").val(confirmUserId);
        $("#workflowBean\\.confirmNodeId_").val(confirmNodeId);
        $("#workflowBean\\.confirmRouteId_").val(confirmRouteId);
        $("#workflowBean\\.submitType_").val("SUBMIT");

        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        $("[flowVariable]").each(function () {
            var item = $(this);
            var itemType = item.attr("workFlowForm");
            var key = item.attr("flowVariable");
            var eleHtml = "<input type='hidden' workFlowType='true' id=workflowBean.workflowVar_['" + key + "'] name=workflowBean.workflowVar_['" + key + "'] value='" + flowVariable.type[itemType].get(item) + "'>"
            $("form").append(eleHtml);
        })
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        $('#submitUserSelectModal').modal('hide');
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                form.submitState = false;
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                form.submitState = false;
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}

//暂存相关
form.type["Save"] = {
    show: function () {

    	if (!handleWorkflowFieldCustomProperty("Save")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Save")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        msgBox.confirm({
            content: "是否要暂存该流程",
            success: form.type["Save"].submit
        });
    },
    submit: function () {
        $("#workflowBean\\.submitType_").val("SAVE");
        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}

form.type["GetBack"] = {
    show: function () {

    	if (!handleWorkflowFieldCustomProperty("GetBack")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("GetBack")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        msgBox.confirm({
            content: "是否要拿回该流程",
            success: form.type["GetBack"].submit
        });
    },
    submit: function () {
        $("#workflowBean\\.confirmNodeId_").val($("#workflowBean\\.curNodeId_").val());
        $("#workflowBean\\.submitType_").val("GETBACK");
        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}

//退回相关
form.type["Reject"] = {
    show: function () {

        if(!initSuggestValidate()){
            return false;
        }

        if (!handleWorkflowFieldCustomProperty("Reject")) {
            return false;
        }
        
        if (typeof validateForm == 'function') {
            if (!validateForm("Reject")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        var buttonOptJsonStr = $("#workflowBean\\.buttonOptJsonStr").val();
        if (buttonOptJsonStr == "") {
            msgBox.tip({
                type: "fail",
                content: "无可退回节点"
            });
            return;
        }
        var buttonOptJson = eval("(" + buttonOptJsonStr + ")");
        var rejectNodeList = buttonOptJson.operateBackNode;
        if (typeof rejectNodeList == 'undefined') {
            msgBox.tip({
                type: "fail",
                content: "无可退回节点"
            });
            return;
        }
        var rejectButtonHtml = ["<table class='table'><tbody>"];
        var curNodeId = $("#workflowBean\\.curNodeId_").val();
        var rejectSelectRadioNum = 0;
        for (var i = 0; i < rejectNodeList.length; i++) {
            if (rejectNodeList[i].id.toUpperCase() != curNodeId.toUpperCase() && rejectNodeList[i].selected) {
                rejectButtonHtml.push("<tr>");
                rejectButtonHtml.push("<td><input type='radio' name='rejectSelectRadio' value='" + rejectNodeList[i].id + "' />" + rejectNodeList[i].name + "</td>");
                rejectButtonHtml.push("</tr>");
                rejectSelectRadioNum = rejectSelectRadioNum + 1;
            }
        }
        rejectButtonHtml.push("</tbody></table>");
        $("#rejectSelectModal #rejectSelectDiv").html(rejectButtonHtml.join(''));
        $('#rejectSelectModal').modal('show');
        $("#rejectSelectModal #submitBtn").unbind('click').bind('click',form.type["Reject"].submit);
        
        if (rejectSelectRadioNum == 1) {
        	$("input[name='rejectSelectRadio']").attr("checked",'checked');
        }
    },
    submit: function () {
        var selectNode = ($('input[name="rejectSelectRadio"]').filter(':checked'));
        if (selectNode.length == 0) {
            msgBox.tip({
                type: "fail",
                content: "请选择退回节点"
            });
            return;
        }

        $("#workflowBean\\.confirmNodeId_").val($(selectNode).val());
        $("#workflowBean\\.submitType_").val("REJECT");

        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
	$("[flowVariable]").each(function () {
            var item = $(this);
            var itemType = item.attr("workFlowForm");
            var key = item.attr("flowVariable");
            var eleHtml = "<input type='hidden' workFlowType='true' id=workflowBean.workflowVar_['" + key + "'] name=workflowBean.workflowVar_['" + key + "'] value='" + flowVariable.type[itemType].get(item) + "'>"
            $("form").append(eleHtml);
        })
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}
//转办相关
form.type["Move"] = {
    show: function () {

    	if (!handleWorkflowFieldCustomProperty("Move")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Move")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        var userSelectHtml = ["<table class='table table-td-striped'><tbody>"];
        userSelectHtml.push("<tr>");
        userSelectHtml.push("<td style='width:18%;'><span class='required'></span>转办人选择</td>");
        userSelectHtml.push("<td><div id='movePersonSingle'></div></td>");
        userSelectHtml.push("</tr>");
        userSelectHtml.push("</tbody></table>");
        $("#moveSelectModal #userSelectDiv").html(userSelectHtml.join(''));
        JCTree.init({
            container: 'movePersonSingle',
            controlId: 'moveUserSelectValue',
            isCheckOrRadio: false
        });

        $('#moveSelectModal').modal('show');
        $("#moveSelectModal #submitBtn").unbind('click').bind('click',form.type["Move"].submit);
    },
    submit: function () {
        var confirmUserId = $("#moveUserSelectValue").val();
        if (confirmUserId.length == 0) {
            msgBox.tip({
                type: "fail",
                content: "请选择转办人"
            });
            return;
        }
        $("#workflowBean\\.confirmUserId_").val(confirmUserId);
        $("#workflowBean\\.submitType_").val("MOVE");

        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}
//跳转相关
form.type["Goto"] = {
    show: function () {

    	if (!handleWorkflowFieldCustomProperty("Goto")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Goto")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        $.ajax({
            type: "POST",
            url: getRootPath() + "/task/getGotoNodes.action",
            async: false,
            data: {
                "definitionId_": $("#workflowBean\\.definitionId_").val(),
                "subProcessId_": $("#workflowBean\\.subProcessId_").val()
            },
            dataType: "json",
            success: function (data) {
                var gotoNodesList = data.gotoNodesList;
                var curNodeId = $("#workflowBean\\.curNodeId_").val()
                var gotoSelectHtml = ["<table class='table'><tbody>"];
                for (var i = 0; i < gotoNodesList.length; i++) {
                    if (gotoNodesList[i].componentId.toUpperCase().indexOf("USERTASK") != -1 && gotoNodesList[i].componentId.toUpperCase() != curNodeId.toUpperCase()) {
                        gotoSelectHtml.push("<tr>");
                        gotoSelectHtml.push("<td><input type='radio' name='gotoSelectRadio' value='" + gotoNodesList[i].componentId + "' />" + gotoNodesList[i].name + "</td>");
                        gotoSelectHtml.push("</tr>");
                    }
                }
                gotoSelectHtml.push("</tbody></table>");
                $("#gotoNodeSelectModal #gotoSelectDiv").html(gotoSelectHtml.join(''));
                $("#gotoNodeSelectModal #submitBtn").unbind('click').bind('click', function () {
                    form.toSelectGotoUser(gotoNodesList);
                });
            }
        });
        $('#gotoNodeSelectModal').modal('show');
    },
    submit: function () {
        var confirmUserId = "";
        var confirmUserFlag = false;
        $('[id^=gotoUserSelectValue]').each(function (index, item) {
            if ($(item).val() == null || $(item).val() == '') {
                confirmUserFlag = true;
            }
            
            if (confirmUserId.length == 0) {
                confirmUserId = $(item).val();
            } else {
                confirmUserId += "&" + $(item).val();
            }
        });

        if (confirmUserFlag) {
            msgBox.tip({
                type: "fail",
                content: "请选择人员"
            });
            
            return false;
        }
        
        var confirmNodeId = "";
        
        $("#gotoUserSelectModal [name='confirmNodeId']").each(function (index, item) {
            if (confirmNodeId.length == 0) {
                confirmNodeId = $(item).val();
            } else {
                confirmNodeId += "&" + $(item).val();
            }
        });
        
        if (confirmUserId.length == 0) {
            msgBox.tip({
                type: "fail",
                content: "请选择人员"
            });
            return;
        }
        
        $("#workflowBean\\.confirmUserId_").val(confirmUserId);
        $("#workflowBean\\.confirmNodeId_").val(confirmNodeId);
        $("#workflowBean\\.submitType_").val("GOTO");

        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}
//终止相关
form.type["Stop"] = {
    show: function () {

    	if (!handleWorkflowFieldCustomProperty("Stop")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Stop")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }

        msgBox.confirm({
            content: "是否要终止该流程",
            success: form.type["Stop"].submit
        });
    },
    submit: function () {
        $("#workflowBean\\.submitType_").val("STOP");
        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus, form.afterSubmit)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}
form.type["Suspend"] = {
    show: function () {
    	if (!handleWorkflowFieldCustomProperty("Suspend")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Suspend")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }
        msgBox.confirm({
            content: "是否要暂停该流程",
            success: form.type["Suspend"].submit
        });
    },
    submit: function () {
        $("#workflowBean\\.submitType_").val("SUSPEND");
        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}

form.type["Resume"] = {
    show: function () {
    	if (!handleWorkflowFieldCustomProperty("Resume")) {
            return false;
        }
    	
        if (typeof validateForm == 'function') {
            if (!validateForm("Resume")) {
                if (typeof validateFormFail == 'function') {
                    validateFormFail();
                }
                return false;
            }
        }
        msgBox.confirm({
            content: "是否要恢复该流程",
            success: form.type["Resume"].submit
        });
    },
    submit: function () {
        $("#workflowBean\\.submitType_").val("RESUME");
        $("form").find("[workFlowType='true']").remove();
        $("[workFlowType='true']").each(function (index, item) {
            item = $(item);
            var eleHtml = "<input type='hidden' workFlowType='true' id='" + item.attr("id") + "' name='" + item.attr("name") + "' value='" + item.val() + "'>"
            $("form").append(eleHtml);
        });
        var flowStatus = $("#workflowBean\\.flowStatus_").val();
        if (flowStatus == "CREATE") {
            if (insert(flowStatus)) {
                $(".modal").modal("hide");
            }
        } else {
            if (update(flowStatus)) {
                $(".modal").modal("hide");
            }
        }
        handWritten.saveWebRevision();
    }
}
form.toSelectUser = function (nextNodesList) {
    var selectNodes = ($('input[name^="selectNodes"]').filter(':checked'));
    if (selectNodes.length == 0) {
        msgBox.tip({
            type: "fail",
            content: "请选择节点信息"
        });
        return;
    }

    $('#submitNodesModal').modal('hide');

    var returnObj = form.showSelectUser(selectNodes, nextNodesList, "submitUserSelectModal", "userSelectDiv");
    var needInitTreeIds;
    var freeDataMap;
    var candidateFlagMap;
    var userSelectHtml;
    
    if (returnObj == null) {
    	return;
    } else {
    	needInitTreeIds = returnObj.get("needInitTreeIds");
        freeDataMap = returnObj.get("freeDataMap");;
        candidateFlagMap = returnObj.get("candidateFlagMap");;
        userSelectHtml = returnObj.get("userSelectHtml");;
    }
    
    $("#submitUserSelectModal #userSelectDiv").html(userSelectHtml.join(''));
    
    $('#submitUserSelectModal').modal('show');
    
    for (var i = 0; i < needInitTreeIds.length; i++) {
        var personId = needInitTreeIds[i];
        var workflowJctreeUrls = $("#workflowBean\\.jctreeUrls").val();
        
        if (personId.indexOf("personSingle") != -1) {
        	if (workflowJctreeUrls != "") {
        		JCTree.init({
                    container: personId,
                    controlId: 'userSelectValue' + personId,
                    isCheckOrRadio: false,
                    urls: JSON.parse(workflowJctreeUrls)
                });
        	} else {
        		JCTree.init({
                    container: personId,
                    controlId: 'userSelectValue' + personId,
                    isCheckOrRadio: false
                });
        	}
        } else if (personId.indexOf("personMultiple") != -1) {
        	if (workflowJctreeUrls != "") {
        		JCTree.init({
                    container: personId,
                    controlId: 'userSelectValue' + personId,
                    urls: JSON.parse(workflowJctreeUrls)
                });
        	} else {
        		JCTree.init({
                    container: personId,
                    controlId: 'userSelectValue' + personId,
                });
        	}
        } else if (personId.indexOf("personSelect") != -1) {
            leftRightSelect.init({
                containerId: personId,
                data: freeDataMap.get(personId),
                moduleId: "userSelectValue" + personId,
                isCheck: candidateFlagMap.get(personId),
                title: "候选人"
            });
        }
    }
    
    $("#submitUserSelectModal #submitBtn").unbind('click').bind('click', form.type["Submit"].submit);
}

form.showSelectUser = function (selectNodes, nextNodesList, userSelectModalId, userSelectDivId) {
	var returnObj = new Map();
	var needInitTreeIds = new Array();
    var freeDataMap = new Map();
    var candidateFlagMap = new Map();
    var userSelectHtml = ["<table class='table table-td-striped'><tbody>"];
    var needChoose = false;
    
    for (var z = 0; z < selectNodes.length; z++) {
        var nextNodeId = $(selectNodes[z]).val();
        for (var x = 0; x < nextNodesList.length; x++) {
            if (nextNodeId == nextNodesList[x].componentId) {
                var confirmSequences = "";
                
                if (nextNodesList[x].channel) {
                	$(nextNodesList[x].channel).each(function (index, item) {
                        if (confirmSequences == "") {
                            confirmSequences = item.sequenceId;
                        } else {
                            confirmSequences += "&" + item.sequenceId;
                        }
                    })                	
                }
                
                userSelectHtml.push("<tr>");
                userSelectHtml.push("<td style='width:18%;'><input type='hidden' name='confirmNodeId' value='" + nextNodeId + "'/>");
                
                if (confirmSequences != "") {
                	userSelectHtml.push("<input type='hidden' name='confirmSequencesId' value='" + confirmSequences + "'/>");
                }
                
                userSelectHtml.push("节点名称</td>");
                userSelectHtml.push("<td>" + nextNodesList[x].name + "</td>");
                userSelectHtml.push("</tr>");

                userSelectHtml.push("<tr>");
                userSelectHtml.push("<td style='width:18%;'><span class='required'></span>人员选择</td>");

                if (nextNodesList[x].type == "end" && selectNodes.length == 1) {
                    userSelectHtml.push("<td></td>");
                    $("#"+userSelectModalId+" #"+userSelectDivId).html(userSelectHtml.join(''));
                    userSelectHtml.push("</tr>");
                    userSelectHtml.push("</tbody></table>");
                    form.type["Submit"].submit();
                    
                    return null;
                }

                //候选人
                if (nextNodesList[x].userType == "2") {
                	needChoose = true;
                	
                    needInitTreeIds.push("personSelect" + x);
				    if(nextNodesList[x].dealType == 4){
                        candidateFlagMap.put("personSelect" + x,false);
                    }else{
                        candidateFlagMap.put("personSelect" + x,true);
                    }
                    userSelectHtml.push("<td><div id=personSelect" + x + "></div></td>");
                    var freeData = new Array();
                    if (nextNodesList[x].assignees.length != 0) {
                        var assigness = nextNodesList[x].assignees;
                        for (var y = 0; y < assigness.length; y++) {
                            freeData.push({code: assigness[y].id, text: assigness[y].name});
                        }
                    }
                    freeDataMap.put("personSelect" + x, freeData);
                    continue;
                }
                //配置人员为空,显示人员选择
                if (nextNodesList[x].assignees.length == 0) {
                	needChoose = true;
                	
                    //单一签核(人员树单选)
                    if (nextNodesList[x].dealType == 4) {
                        needInitTreeIds.push('personSingle' + x);
                        userSelectHtml.push("<td><div id=personSingle" + x + "></div></td>");
                    }
                    //多人处理及多人单一签核(人员树多选)
                    else {
                        needInitTreeIds.push('personMultiple' + x);
                        userSelectHtml.push("<td><div id=personMultiple" + x + "></div></td>");
                    }
                } else {
                    var assigness = nextNodesList[x].assignees;
                    var assigneesIds = "", assigneesNames = "";
                    for (var i = 0; i < assigness.length; i++) {
                        if (assigneesIds != "") {
                            if (assigness[i].oldName != null) {
                                assigneesIds += "," + assigness[i].oldId;
                                assigneesNames += ",(" + assigness[i].oldName + ")委托给(" + assigness[i].name + ")";
                            } else {
                                assigneesIds += "," + assigness[i].id;
                                assigneesNames += "," + assigness[i].name;
                            }
                        } else {
                            if (assigness[i].oldName != null) {
                                assigneesIds = assigness[i].oldId;
                                assigneesNames += "(" + assigness[i].oldName + ")委托给(" + assigness[i].name + ")";
                            } else {
                                assigneesIds = assigness[i].id;
                                assigneesNames += assigness[i].name;
                            }
                        }
                    }
                    userSelectHtml.push("<td><input type='hidden' id='userSelectValue' value='" + assigneesIds + "' />" + assigneesNames + "</td>");
                    
                    if (z == selectNodes.length-1 && !needChoose) {
                    	userSelectHtml.push("</tr>");
                        userSelectHtml.push("</tbody></table>");
                        $("#"+userSelectModalId+" #"+userSelectDivId).html(userSelectHtml.join(''));
                        form.type["Submit"].submit();
                        
                        return null;
                    }
                }
                userSelectHtml.push("</tr>");
            }
        }
    }
    userSelectHtml.push("</tbody></table>");
    
    returnObj.put("needInitTreeIds", needInitTreeIds);
    returnObj.put("freeDataMap", freeDataMap);
    returnObj.put("candidateFlagMap", candidateFlagMap);
    returnObj.put("userSelectHtml", userSelectHtml);
    
    return returnObj;
}

form.toSelectGotoUser = function (nextNodesList) {
    var selectNode = ($('input[name="gotoSelectRadio"]').filter(':checked'));
    if (selectNode.length == 0) {
        msgBox.tip({
            type: "fail",
            content: "请选节点信息"
        });
        return;
    }
    
    $('#gotoNodeSelectModal').modal('hide');
    
    var returnObj = form.showSelectUser(selectNode, nextNodesList, "gotoUserSelectModal", "userSelectDiv");
    var needInitTreeIds;
    var freeDataMap;
    var candidateFlagMap;
    var userSelectHtml;
    
    if (returnObj == null) {
    	return;
    } else {
    	needInitTreeIds = returnObj.get("needInitTreeIds");
        freeDataMap = returnObj.get("freeDataMap");;
        candidateFlagMap = returnObj.get("candidateFlagMap");;
        userSelectHtml = returnObj.get("userSelectHtml");;
    }
    
    $("#gotoUserSelectModal #userSelectDiv").html(userSelectHtml.join(''));
    
    $('#gotoUserSelectModal').modal('show');

    for (var i = 0; i < needInitTreeIds.length; i++) {
        var personId = needInitTreeIds[i];
        if (personId.indexOf("personSingle") != -1) {
            JCTree.init({
                container: personId,
                controlId: 'gotoUserSelectValue' + personId,
                isCheckOrRadio: false
            });
        } else if (personId.indexOf("personMultiple") != -1) {
            JCTree.init({
                container: personId,
                controlId: 'gotoUserSelectValue' + personId,
            });
        } else if (personId.indexOf("personSelect") != -1) {
            leftRightSelect.init({
                containerId: personId,
                data: freeDataMap.get(personId),
                moduleId: "gotoUserSelectValue" + personId,
                isCheck: candidateFlagMap.get(personId),
                title: "候选人"
            });
        }
    }
    
    $("#gotoUserSelectModal #submitBtn").unbind('click').bind('click', form.type["Goto"].submit);
}
/**
 * 执行完业务后可选回调函数
 */
form.afterSubmit = function () {
    //跳转我的列表
    var url = $("#myBusinessUrl").val();
    if(url && url.length > 0){
        JCFF.loadPage({url :url});
    }
}

form.initButton = function () {

    var openType = $("#workflowBean\\.openType_").val();

    var buttonJsonStr = $("#workflowBean\\.buttonJsonStr").val();
    var buttonHtml = [];
    if (buttonJsonStr == "") {
        buttonHtml.push("<button class='btn' id='workflowButtongoBack' type='button'>返回</button>");
        $("#workflowFormButton").html(buttonHtml.join(''));
        return;
    }
    var buttonJson = eval("(" + buttonJsonStr + ")");
	var buttonId = "";

    //<button class='btn dark' id='workflowSubmitBtn' type='button'>提交</button>
    //加载待办按钮
    if (openType == "TODO") {
        var todoButtonList = buttonJson.todoSystemList;
        if (typeof todoButtonList != "undefined") {
            for (var i = 0; i < todoButtonList.length; i++) {
                if (todoButtonList[i].selected) {
                    buttonHtml.push("<button class='btn dark' id='workflowButtonId" + todoButtonList[i].id + "' type='button'>" + todoButtonList[i].name + "</button>");
                    buttonId = buttonId + "," + "workflowButtonId" + todoButtonList[i].id;
                }
            }
            buttonHtml.push("<button class='btn' id='workflowButtongoBack' type='button'>返回</button>");
        }
    }
    //加载已办按钮
    else if (openType == "DONE") {
        var doneButtonList = buttonJson.haveSystemList;
        if (typeof doneButtonList != "undefined") {
            for (var i = 0; i < doneButtonList.length; i++) {
                if (doneButtonList[i].selected) {
                    buttonHtml.push("<button class='btn dark' id='workflowButtonId" + doneButtonList[i].id + "' type='button'>" + doneButtonList[i].name + "</button>");
                    buttonId = buttonId + "," + "workflowButtonId" + doneButtonList[i].id;
                }
            }
            buttonHtml.push("<button class='btn' id='workflowButtongoBack' type='button'>返回</button>");
        }
    }
    $("#workflowFormButton").html(buttonHtml.join(''));
    
    if (buttonId.length > 0) {
    	buttonId = buttonId.substring(1);
    }
    
    $("#workflowBean\\.buttonId").val(buttonId);
}

form.showRoute = function (type) {
    var workId = $("#workId").val();
    //将意见赋值到隐藏域
    if ((navigator.userAgent.indexOf("Chrome") == -1) && (navigator.userAgent.indexOf("Firefox") == -1) && document.all.DWebSignSeal != null) {
        var v = document.all.DWebSignSeal.GetStoreData();
        var signInfoFlag = $("#workflowBean\\.signInfoFlag_").val();
        if (signInfoFlag == "true") {
            $("#workflowBean\\.signInfo_").val(v);
        }
    }
    var suggestEle = $("[workflowSuggest]").find("textarea");
    if (suggestEle.length > 0) {
        suggestEle = $(suggestEle.get(0));
        $("#workflowBean\\.message_").val(form.htmlEncode(suggestEle.val()));
        $("#workflowBean\\.suggestId_").val(suggestEle.parents("[workflowSuggest]").attr("id"));
        $("#workflowBean\\.signContainerId_").val(suggestEle.parents("[workflowSuggest]").find("[signContainer]").attr("id"));
    }
    form.type[type].show();


}

form.htmlEncode = function (str) {
	var s = str;
	
	if (str.length === 0) {
		return "";
	}
	
	s = s.replace(/&/g, "&amp;");
	s = s.replace(/\n|\r\n/g, "<br>");
	s = s.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
	//s = s.replace(/\s/g, "&nbsp;");
	s = s.replace(/\'/g, "&#39;");
	s = s.replace(/\"/g, "&quot;");
	
	return s;
}

form.htmlDecode = function (str) {
	var s = str;
	
	if (str.length === 0) {
		return "";
	}
	
	s = s.replace(/&amp;/g, "&");
	s = s.replace(/&lt;br&gt;/g, "\r\n");
	s = s.replace(/&lt;/g, "<");
	s = s.replace(/&gt;/g, ">");
	//s = s.replace(/&nbsp;/g, "\s");
	s = s.replace(/&#39;/g, "\'");
	s = s.replace(/&quot;/g, "\"");
	
	return s;
}

formPriv = {};

var map = new Map();

formPriv.getPrivilege = function(itemName){
    var formItemPrivJsonStr = $("#workflowBean\\.formItemPrivJsonStr").val();
    var formItemPrivJson;
    if ($.trim(formItemPrivJsonStr) != '') {
        formItemPrivJson = eval("(" + formItemPrivJsonStr + ")");
    }
    var openType = $("#workflowBean\\.openType_").val();
    var fieldList = "";
    if (openType == 'TODO') {
        fieldList = formItemPrivJson.todo;
    } else if (openType == 'DONE') {
        fieldList = formItemPrivJson.have;
    }

    for (var i = 0; i < fieldList.length; i++) {
        if(fieldList[i].formName == itemName){
            return fieldList[i].privilege;
        }
    }
}

formPriv.initType = function () {
    map.put("1", "textinput");
    map.put("2", "textinput");
    map.put("3", "textinput");
    map.put("4", "radio");
    map.put("5", "checkbox");
    map.put("6", "select");
    map.put("7", "userSelect");
    map.put("8", "attach");
    map.put("9", "editor");
    map.put("10", "autoRow");
    map.put("11", "autoRow-Column");
    map.put("12", "suggest");
    map.put("13", "button");
    map.put("14", "container");
    map.put("-1", "textinput");
}

formPriv.doForm = function (opt) {
    var jqueryForm = "form";
    var formObj = $(jqueryForm);
    if (formObj.length == 0) {
        msgBox.tip({
            type: "fail",
            content: "没有找到对应的表单"
        });
        return;
    }
    formObj = $(formObj[0]);
    formPriv.initType();
    var openType = $("#workflowBean\\.openType_").val();


    var formItemPrivJsonStr = $("#workflowBean\\.formItemPrivJsonStr").val();
    var formItemPrivJson;
    if ($.trim(formItemPrivJsonStr) != '') {
        formItemPrivJson = eval("(" + formItemPrivJsonStr + ")");
    }
    var fieldList;
    if (openType == 'TODO') {
        fieldList = formItemPrivJson.todo;
    } else if (openType == 'DONE') {
        fieldList = formItemPrivJson.have;
    } else if (openType == 'VIEW') {
        formObj.find("[workFlowForm]").each(function () {
            var item = $(this);
            var itemType = $(this).attr("workFlowForm");
            if(item.attr('type') != 'hidden' && item.attr("autoFlowForm") != itemType){
                formPriv.type[itemType].read(item);
            }
        });
        handWritten.initWebRevision();
        handWritten.initEditSuggestSignObject();
        return;
    }

    for (var i = 0; i < fieldList.length; i++) {
        var privilege = fieldList[i].privilege;
        if (openType == 'VIEW' && $(this).attr("viewShow") != "true") {
            formPriv.type[map.get(fieldList[i].type)].read($(formObj.find("[itemName='" + fieldList[i].formName + "']")));
            continue;
        }
        if (privilege == 'readonly') {
            formPriv.type[map.get(fieldList[i].type)].read($(formObj.find("[itemName='" + fieldList[i].formName + "']")));
        } else if (privilege == 'hidden') {
            formPriv.type[map.get(fieldList[i].type)].hide($(formObj.find("[itemName='" + fieldList[i].formName + "']")));
        } else {
        	if (fieldList[i].type == "12") {
				var tempMessage = formObj.find("[itemId='" + fieldList[i].formName + "']").find("textarea");
				
				if (tempMessage.length > 0) {
					tempMessage.val(form.htmlDecode(tempMessage.val()));
				}
			}
        }
    }
    
    handWritten.initWebRevision();
    handWritten.initEditSuggestSignObject();
}

//各个组件的状态判断
formPriv.type = {};
formPriv.type["hidden"] = {
    hide: function (obj) {
    },
    read: function (obj) {
    }
}
formPriv.type["textinput"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			if (obj.is('input') || obj.is('textarea')) {
    	            var label = obj.val();
    	            label = label.replace(/\r\n/g, "<BR>");
    	            label = label.replace(/\n/g, "<BR>");
    	            obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + label + '</div>');
    	        } else {
    	            var label = obj.find('input').val();
    	            obj.html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + label + '</div>');
    	        }
            }
    	} else {
    		if (obj.is('input') || obj.is('textarea')) {
                var label = obj.val();
                label = label.replace(/\r\n/g, "<BR>");
                label = label.replace(/\n/g, "<BR>");
                obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + label + '</div>');
            } else {
                var label = obj.find('input').val();
                obj.html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + label + '</div>');
            }
    	}
    }
}

formPriv.type["radio"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var checkedLabel = "";
    	        var checkEle = obj.find("input:radio:checked");
    	        if (checkEle.length == 1) {
    	            checkedLabel = checkEle.attr("label");
    	        }
    	        obj.html(checkedLabel);
            }
    	} else {
    		var checkedLabel = "";
            var checkEle = obj.find("input:radio:checked");
            if (checkEle.length == 1) {
                checkedLabel = checkEle.attr("label");
            }
            obj.html(checkedLabel);
    	}
    }
}

formPriv.type["checkbox"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var checkedLabel = "";
    	        obj.find("input:checkbox:checked").each(function (index, item) {
    	            checkedLabel += $(item).attr("label") + ",";
    	        });
    	        if (checkedLabel.length > 0) {
    	            checkedLabel = checkedLabel.substring(0, checkedLabel.length - 1);
    	        }
    	        obj.html(checkedLabel);
            }
    	} else {
    		var checkedLabel = "";
            obj.find("input:checkbox:checked").each(function (index, item) {
                checkedLabel += $(item).attr("label") + ",";
            });
            if (checkedLabel.length > 0) {
                checkedLabel = checkedLabel.substring(0, checkedLabel.length - 1);
            }
            obj.html(checkedLabel);
    	}
    }
}

formPriv.type["attach"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			if (obj.is('button')) {
    	            obj.remove();
    	        } else {
    	            var button = obj.find('button');
    	            obj.find('a').first().remove();
    	            $(button).remove();
    	        }
            }
    	} else {
    		if (obj.is('button')) {
                obj.remove();
            } else {
                var button = obj.find('button');
                obj.find('a').first().remove();
                $(button).remove();
            }
    	}
    }
}

formPriv.type["select"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var selectLabel = "";
    	        obj.find("select:not(.noneWorkflow) option:checked").each(function (index, item) {
    	            if ($(item).val() == null || $(item).val().length == 0) {
    	                selectLabel = "";
    	            } else {
    	                selectLabel = $(item).html();
    	            }
    	        });
    	        obj.html(selectLabel);
            }
    	} else {
    		var selectLabel = "";
            obj.find("select:not(.noneWorkflow) option:checked").each(function (index, item) {
                if ($(item).val() == null || $(item).val().length == 0) {
                    selectLabel = "";
                } else {
                    selectLabel = $(item).html();
                }
            });
            obj.html(selectLabel);
    	}
    }
}

formPriv.type["button"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			//暂时没有添加只读操作
    	        obj.hide();
            }
    	} else {
    		//暂时没有添加只读操作
            obj.hide();
    	}
    }
}

//动态添加行
formPriv.type["editor"]={
    hide:function(obj){
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.hide();
            }
    	} else {
    		obj.hide();
    	}
    },
    read:function(obj){
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var itemId = obj.attr("itemName");
    	        var editor = UE.getEditor(itemId);
    	        editor.ready( function( editor ) {
    	            UE.getEditor(itemId).setDisabled();
    	        });
            }
    	} else {
    		var itemId = obj.attr("itemName");
            var editor = UE.getEditor(itemId);
            editor.ready( function( editor ) {
                UE.getEditor(itemId).setDisabled();
            });
    	}
    }
}
//人员选择框
formPriv.type["userSelect"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var itemId = obj.attr("itemName");
    	        var divId = itemId.split("!")[0];
    	        var controlId = itemId.split("!")[1];
    	        var results = returnValue(controlId);
    	        var resultStr = "";
    	        if (results != null) {
    	            var users = results.split(",");
    	            for (var i = 0; i < users.length; i++) {
    	                resultStr += users[i].split(":")[1] + ",";
    	            }
    	        }
    	        if (resultStr.length > 0) {
    	            resultStr = resultStr.substring(0, resultStr.length - 1);
    	        }
    	        $("#" + divId).html(resultStr);
            }
    	} else {
    		var itemId = obj.attr("itemName");
            var divId = itemId.split("!")[0];
            var controlId = itemId.split("!")[1];
            var results = returnValue(controlId);
            var resultStr = "";
            if (results != null) {
                var users = results.split(",");
                for (var i = 0; i < users.length; i++) {
                    resultStr += users[i].split(":")[1] + ",";
                }
            }
            if (resultStr.length > 0) {
                resultStr = resultStr.substring(0, resultStr.length - 1);
            }
            $("#" + divId).html(resultStr);
    	}
    }
}

formPriv.type["suggest"]={
    hide:function(obj){
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.hide();
            }
    	} else {
    		obj.hide();
    	}
    },
    read:function(obj){
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    }
}

formPriv.type["container"]={
    hide:function(obj){
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.hide();
            }
    	} else {
    		obj.hide();
    	}
    },
    read:function(obj){
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    }
}

formPriv.type["autoRow-Column"]={
    hide:function(obj){
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    },
    read:function(obj){
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    }
}
//动态添加行
formPriv.type["autoRow"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			obj.find("[autoFlowForm]").each(function () {
    	            var autoItem = $(this);
    	            var autoItemType = $(this).attr("autoFlowForm");
    	            formPriv.type["autoRow"].type[autoItemType].read(autoItem);
    	        });
    	        //去操作列
    	        obj.find("[operate]").each(function (index, item) {
    	            $(item).remove();
    	        })
            }
    	} else {
    		obj.find("[autoFlowForm]").each(function () {
                var autoItem = $(this);
                var autoItemType = $(this).attr("autoFlowForm");
                formPriv.type["autoRow"].type[autoItemType].read(autoItem);
            });
            //去操作列
            obj.find("[operate]").each(function (index, item) {
                $(item).remove();
            })
    	}
    }
}
formPriv.type["editor"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
    }
}
//动态添加行具体类型
formPriv.type["autoRow"].type = {};

//动态添加行-文本输入框
formPriv.type["autoRow"].type["textinput"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			if (obj.attr('type') == 'hidden') {
    	            return
    	        }else if (obj.is('input') || obj.is('textarea')) {
    	            obj.parent().html("<input type='hidden' name='" + $(obj).attr("name") + "' id = '" + $(obj).attr("id") + "' value = '" + obj.val() + "' >");
    	        } else {
    	            var textObj = obj.find("input");
    	            obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
    	        }
            }
    	} else {
    		if (obj.attr('type') == 'hidden') {
                return
            }else if (obj.is('input') || obj.is('textarea')) {
                obj.parent().html("<input type='hidden' name='" + $(obj).attr("name") + "' id = '" + $(obj).attr("id") + "' value = '" + obj.val() + "' >");
            } else {
                var textObj = obj.find("input");
                obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
            }
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			if (obj.attr('type') == 'hidden') {
    	            return
    	        }else if (obj.is('input') || obj.is('textarea')) {
    	            obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + obj.val() + "</div><input type='hidden' name='" + $(obj).attr("name") + "' id = '" + $(obj).attr("id") + "' value = '" + obj.val() + "' >");
    	        } else {
    	            var textObj = obj.find("input");
    	            obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + textObj.val() + "</div><input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
    	        }
            }
    	} else {
    		if (obj.attr('type') == 'hidden') {
                return
            }else if (obj.is('input') || obj.is('textarea')) {
                obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + obj.val() + "</div><input type='hidden' name='" + $(obj).attr("name") + "' id = '" + $(obj).attr("id") + "' value = '" + obj.val() + "' >");
            } else {
                var textObj = obj.find("input");
                obj.parent().html('<div style="word-wrap:break-word;white-space: pre-wrap;">' + textObj.val() + "</div><input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
            }
    	}
    }
}

//动态添加行-文本输入域
formPriv.type["autoRow"].type["textarea"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			var textObj = obj.find("textarea");
    	        obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
            }
    	} else {
    		var textObj = obj.find("textarea");
            obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var textObj = obj.find("textarea");
    	        obj.parent().html(textObj.val() + "<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
            }
    	} else {
    		var textObj = obj.find("textarea");
            obj.parent().html(textObj.val() + "<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
    	}
    }
}

//动态添加行-文本输入框
formPriv.type["autoRow"].type["checkbox"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			var textObj = obj.find("checkbox");
    	        obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
            }
    	} else {
    		var textObj = obj.find("checkbox");
            obj.parent().html("<input type='hidden' name='" + $(textObj).attr("name") + "' id = '" + $(textObj).attr("id") + "' value = '" + textObj.val() + "' >");
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var autoCheckedLabel = "";
    	        var autoCheckedValue = "";
    	        var autoHtml = "";
    	        obj.find("input:checkbox:checked").each(function (index, item) {
    	            autoCheckedLabel += $(item).attr("label") + ",";
    	            autoCheckedValue += $(item).attr("value") + ",";
    	            autoHtml += "<input type='hidden' id='" + $(item).attr("id") + "' name='" + $(item).attr("name") + "' value = '" + $(item).attr("value") + "' >";
    	        });
    	        if (autoCheckedLabel.length > 0) {
    	            autoCheckedLabel = autoCheckedLabel.substring(0, autoCheckedLabel.length - 1);
    	            autoCheckedValue = autoCheckedValue.substring(0, autoCheckedValue.length - 1);
    	        }
    	        obj.parent().html(autoCheckedLabel + autoHtml);
            }
    	} else {
    		var autoCheckedLabel = "";
            var autoCheckedValue = "";
            var autoHtml = "";
            obj.find("input:checkbox:checked").each(function (index, item) {
                autoCheckedLabel += $(item).attr("label") + ",";
                autoCheckedValue += $(item).attr("value") + ",";
                autoHtml += "<input type='hidden' id='" + $(item).attr("id") + "' name='" + $(item).attr("name") + "' value = '" + $(item).attr("value") + "' >";
            });
            if (autoCheckedLabel.length > 0) {
                autoCheckedLabel = autoCheckedLabel.substring(0, autoCheckedLabel.length - 1);
                autoCheckedValue = autoCheckedValue.substring(0, autoCheckedValue.length - 1);
            }
            obj.parent().html(autoCheckedLabel + autoHtml);
    	}
    }
}

//动态添加行-select
formPriv.type["autoRow"].type["select"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			var selectHtml = "";
    	        var selectValue = "";
    	        obj.find("select option:checked").each(function (index, item) {
    	            if ($(item).val() == null || $(item).val().length == 0) {
    	                selectHtml = "";
    	            } else {
    	                selectHtml = $(item).html();
    	                selectValue = $(item).val();
    	            }
    	        });
    	        obj.parent().html("<input type='hidden' name='" + $(obj.find("select")).attr("name") + "' id = '" + $(obj.find("select")).attr("id") + "' value = '" + selectValue + "' >");
            }
    	} else {
    		var selectHtml = "";
            var selectValue = "";
            obj.find("select option:checked").each(function (index, item) {
                if ($(item).val() == null || $(item).val().length == 0) {
                    selectHtml = "";
                } else {
                    selectHtml = $(item).html();
                    selectValue = $(item).val();
                }
            });
            obj.parent().html("<input type='hidden' name='" + $(obj.find("select")).attr("name") + "' id = '" + $(obj.find("select")).attr("id") + "' value = '" + selectValue + "' >");
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var selectHtml = "";
    	        var selectValue = "";
    	        obj.find("select option:checked").each(function (index, item) {
    	            if ($(item).val() == null || $(item).val().length == 0) {
    	                selectHtml = "";
    	            } else {
    	                selectHtml = $(item).html();
    	                selectValue = $(item).val();
    	            }
    	        });
    	        obj.parent().html(selectHtml + "<input type='hidden' name='" + $(obj.find("select")).attr("name") + "' id = '" + $(obj.find("select")).attr("id") + "' value = '" + selectValue + "' >");
            }
    	} else {
    		var selectHtml = "";
            var selectValue = "";
            obj.find("select option:checked").each(function (index, item) {
                if ($(item).val() == null || $(item).val().length == 0) {
                    selectHtml = "";
                } else {
                    selectHtml = $(item).html();
                    selectValue = $(item).val();
                }
            });
            obj.parent().html(selectHtml + "<input type='hidden' name='" + $(obj.find("select")).attr("name") + "' id = '" + $(obj.find("select")).attr("id") + "' value = '" + selectValue + "' >");
    	}
    }
}

//动态添加行-文本输入框
formPriv.type["autoRow"].type["button"] = {
	hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
	},
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			obj.remove();
            }
    	} else {
    		obj.remove();
    	}
    }
}

//动态添加行-人员选择
formPriv.type["autoRow"].type["userSelect"] = {
    hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			var itemId = obj.attr("itemId").split("-")[0];
    	        var itemName = obj.attr("itemId").split("-")[1];
    	        var results = returnValue(itemId);
    	        var resultStr = "";
    	        var resultIds = "";
    	        if (results != null) {
    	            var users = results.split(",");
    	            for (var i = 0; i < users.length; i++) {
    	                resultStr += users[i].split(":")[1] + ",";
    	                resultIds += users[i].split(":")[0] + ",";
    	            }
    	        }
    	        if (resultStr.length > 0) {
    	            resultStr = resultStr.substring(0, resultStr.length - 1);
    	            resultIds = resultIds.substring(0, resultIds.length - 1);
    	        }
    	        obj.html(resultStr);
    	        var appendHtml = "<input type='hidden' value='" + resultIds + "' id='" + itemId + "' name='" + itemName + "'>";
    	        obj.after(appendHtml);
            }
    	} else {
    		var itemId = obj.attr("itemId").split("-")[0];
            var itemName = obj.attr("itemId").split("-")[1];
            var results = returnValue(itemId);
            var resultStr = "";
            var resultIds = "";
            if (results != null) {
                var users = results.split(",");
                for (var i = 0; i < users.length; i++) {
                    resultStr += users[i].split(":")[1] + ",";
                    resultIds += users[i].split(":")[0] + ",";
                }
            }
            if (resultStr.length > 0) {
                resultStr = resultStr.substring(0, resultStr.length - 1);
                resultIds = resultIds.substring(0, resultIds.length - 1);
            }
            obj.html(resultStr);
            var appendHtml = "<input type='hidden' value='" + resultIds + "' id='" + itemId + "' name='" + itemName + "'>";
            obj.after(appendHtml);
    	}
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var itemId = obj.attr("itemId").split("-")[0];
    	        var itemName = obj.attr("itemId").split("-")[1];
    	        var results = returnValue(itemId);
    	        var resultStr = "";
    	        var resultIds = "";
    	        if (results != null) {
    	            var users = results.split(",");
    	            for (var i = 0; i < users.length; i++) {
    	                resultStr += users[i].split(":")[1] + ",";
    	                resultIds += users[i].split(":")[0] + ",";
    	            }
    	        }
    	        if (resultStr.length > 0) {
    	            resultStr = resultStr.substring(0, resultStr.length - 1);
    	            resultIds = resultIds.substring(0, resultIds.length - 1);
    	        }
    	        obj.html(resultStr);
    	        var appendHtml = "<input type='hidden' value='" + resultIds + "' id='" + itemId + "' name='" + itemName + "'>";
    	        obj.after(appendHtml);
            }
    	} else {
    		var itemId = obj.attr("itemId").split("-")[0];
            var itemName = obj.attr("itemId").split("-")[1];
            var results = returnValue(itemId);
            var resultStr = "";
            var resultIds = "";
            if (results != null) {
                var users = results.split(",");
                for (var i = 0; i < users.length; i++) {
                    resultStr += users[i].split(":")[1] + ",";
                    resultIds += users[i].split(":")[0] + ",";
                }
            }
            if (resultStr.length > 0) {
                resultStr = resultStr.substring(0, resultStr.length - 1);
                resultIds = resultIds.substring(0, resultIds.length - 1);
            }
            obj.html(resultStr);
            var appendHtml = "<input type='hidden' value='" + resultIds + "' id='" + itemId + "' name='" + itemName + "'>";
            obj.after(appendHtml);
    	}
    }
}

//动态添加行-div
formPriv.type["autoRow"].type["content"] = {
	hide: function (obj) {
    	if (typeof customHideWorkflowField == 'function') {
    		var hideResult = customHideWorkflowField(obj);
    		if(!hideResult){
    			
            }
    	} else {
    		
    	}
	},
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			var text = obj.html();
    	        text = text.replace(/\r\n/g, "<BR>");
    	        text = text.replace(/\n/g, "<BR>");
    	        obj.parent().html(text);
            }
    	} else {
    		var text = obj.html();
            text = text.replace(/\r\n/g, "<BR>");
            text = text.replace(/\n/g, "<BR>");
            obj.parent().html(text);
    	}
    }
}

formPriv.type["autoRow"].type["attach"] = {
    hide: function (obj) {
        if (typeof customHideWorkflowField == 'function') {
            var hideResult = customHideWorkflowField(obj);
            if(!hideResult){
                obj.remove();
            }
        } else {
            obj.remove();
        }
    },
    read: function (obj) {
    	if (typeof customReadWorkflowField == 'function') {
    		var hideResult = customReadWorkflowField(obj);
    		if(!hideResult){
    			obj.attr('flow-read', 'true');
            }
    	} else {
    		obj.attr('flow-read', 'true');
    	}
    }
}

var jcSignature = {};

/**
 * @description 加载意见域相关数据
 * @para    workId:流程实例id
 *        opt:隐藏，编辑，只读权限
 */
jcSignature.initSuggest = function () {
    var suggestToHeight = [];
    $("[workflowSuggest='true']").each(function () {
        var suggestDiv = $(this);
        if ($(this).find("textarea").length > 0) {
            //初始化常用词
            var textareaId = $(this).find("textarea").attr("id");
            var containerId = $(this).find("[id^=phrase]").attr("id");
            if (typeof phraseComponent != "undefined") {
                phraseComponent.init({
                    containerId: containerId,
                    fillEleId: textareaId
                });
            }
        }
        $(this).find(">div").each(function (index, item) {
            suggestToHeight[suggestToHeight.length] = {
                divId: $(item).attr("id"),
                signId: $(item).attr("signId")
            }
        });
    });
    var signInfo = $("#signInfoOld").val();
    if (signInfo.length > 0) {
        if (!isChrome() && !isFF() && document.all.DWebSignSeal != null) {
            document.all.DWebSignSeal.SetStoreData(signInfo);
            document.all.DWebSignSeal.ShowWebSeals();
            jcSignature.caseHeight(suggestToHeight);
            document.all.DWebSignSeal.ShowWebSeals();
            $("[workflowSuggest=true]>div").each(function (index, item) {
                var objItem = $(this);
                var signId = objItem.attr("signId");
                var px = document.all.DWebSignSeal.GetSealPosX (signId);
                var py = document.all.DWebSignSeal.GetSealPosY (signId)
                var swidth = document.all.DWebSignSeal.GetSealWidth (signId);
                var divWidth = objItem.actual("width");
                if (px + swidth > divWidth) {
                    document.all.DWebSignSeal.MoveSealPosition(signId, divWidth - swidth, py, objItem.attr("id"));
                }
            });
        }
    }
}

/**
 * 意见域匹配高度
 */
jcSignature.caseHeight = function (suggestToHeight) {
    for (var i = 0; i < suggestToHeight.length; i++) {
        var height = document.DWebSignSeal.GetSealHeight(suggestToHeight[i].signId);
        if (height > 0) {
            $("#" + suggestToHeight[i].divId).css("min-height", height + 80);
        }
    }
}

/**
 * @description 弹出手写框
 */
jcSignature.showWritePannel = function (divId) {
   // var signId = handWritten.showWritePannel(divId);
    //$("#signId").val(signId);
    handWritten.showWritePannel_JG(divId);
}

var flowVariable = {};
//获得流程表单变量
flowVariable.getFlowVariable = function (curNodeId_, definitionId_,instanceId_) {
    var returnValue = ["{"];
    var jqueryForm = "form";
    var formObj = $(jqueryForm);
    if (formObj.length == 0) {
        alertx("没有找到对应的表单");
        return;
    }
    formObj = $(formObj[0]);
    returnValue.push('"curNodeId_": "' + curNodeId_ + '"');
    returnValue.push(',"definitionId_": "' + definitionId_ + '"');
    returnValue.push(',"instanceId_":"'+instanceId_+'"');
    formObj.find("[flowVariable]").each(function () {
        var item = $(this);
        var itemType = item.attr("workFlowForm");
        var key = item.attr("flowVariable");
        //returnValue.push({ "key": key, "value": flowVariable.type[itemType].get(item)});
        returnValue.push(',"workflowVar_[' + key + ']": "' + flowVariable.type[itemType].get(item) + '"');
    })
    returnValue.push("}");
    return returnValue;
}


flowVariable.type = {};
flowVariable.type["textinput"] = {
    get: function (obj) {
        return obj.val();
    }
}

flowVariable.type["select"] = {
    get: function (obj) {
        return obj.find("select option:checked").val();
    }
}

flowVariable.type["radio"] = {
    get: function (obj) {
        return obj.find("radio option:checked").val();
    }
}

flowVariable.type["hidden"] = {
    get: function (obj) {
        return obj.val();
    }
}


jQuery(function ($) {
    form.initButton();
    jcSignature.initSuggest();
    $("#workflowButtonId1").click(function () {
        form.showRoute("Submit");
    });
    $("#workflowButtonId2").click(function () {
        form.showRoute("Save");
    });
    $("#workflowButtonId3").click(function () {
        form.showRoute("Reject");
    });
    $("#workflowButtonId4").click(function () {
        form.showRoute("Move");
    });
    $("#workflowButtonId5").click(function () {
        form.showRoute("Goto");
    });
    $("#workflowButtonId6").click(function () {
        form.showRoute("Stop");
    });
    $("#workflowButtonId7").click(function () {
        form.showRoute("Suspend");
    });
    $("#workflowButtonId8").click(function () {
        form.showRoute("Resume");
    });
    $("#workflowButtonId1001").click(function () {
        form.showRoute("GetBack");
    });
    $("#workflowButtongoBack").click(function () {
        history.go(-1);
    });
    //initHistoryList();
    if(!$("#workflowBean\\.instanceId_").val()){
        //初始不显示流程历史
        $("#workflowTab2").hide();
    }
});

function initHistoryList(){
    $.ajax({
        type: "POST",
        url: getRootPath() + "/workflowHistory/getHistoryData.action",
        data: {definitionId:$("#workflowBean\\.definitionId_").val(),instanceId:$("#workflowBean\\.instanceId_").val()},
        dataType: "json",
        success: function (data) {
            $(data).each(function(index,item){
                var html = "<tr class=\"odd\">";
                html += "<td nowrap=\"nowrap\">"+item.userName+"</td>";
                html += "<td nowrap=\"nowrap\">"+item.curNodeName+"</td>";
                html += "<td nowrap=\"nowrap\" >"+item.createTime+"</td>";
                html += "<td>"+item.content+"</td>";
                html += "<td>"+(item.suggestMessage==null?"":item.suggestMessage)+"</td>";
                html += "</tr>";

                if($(historyPosition)) {
                    $(historyPosition).append(html);
                }
            })
        }
    });
}



function Map() {
    this.container = new Object();
}


Map.prototype.put = function (key, value) {
    this.container[key] = value;
}


Map.prototype.get = function (key) {
    return this.container[key];
}
function initSuggestValidate(){
    var flag = true;
    $("[workflowSuggest='true']").each(function () {
        var maxLength = $(this).find("textarea").attr("maxLengths");
        if(typeof maxLength != "undefined" && maxLength > 0){
            var content = $(this).find("textarea").val();
            if(content.length > maxLength){
                flag = false;
            }
        }
    });
    if(!flag){
        msgBox.tip({
            type: "fail",
            content: "意见域填写错误"
        });
    }
    return flag;
}

/**
 * 自定义属性处理
 */
function handleWorkflowFieldCustomProperty(type) {
	var returnResult = {};
	returnResult['type'] = type;//流程操作类型
	
	returnResult = fieldCustomProperty.handleFieldCustomProperty(returnResult);//处理流程当前节点各个字段的自定义属性
	
	//判断返回结果（code），1：成功，0：失败
	if (returnResult["code"] != "1") {
		return false;
	} else {
		return true;
	}
}

function workflowCheckNodes(checkNode) {
	$('input[name^="selectNodes"]').each(function (index, item) {
		if (checkNode.name != item.name) {
			$(item).prop('checked', false);
		}
    });
}

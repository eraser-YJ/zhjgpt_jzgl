//初始化方法
var safetySupervisionJsForm = {};
//重复提交标识
safetySupervisionJsForm.subState = false;
safetySupervisionJsForm.companyTypeDic;

safetySupervisionJsForm.safetyUnitIndex = 0;
function insert(type,jumpFun) {

    if(safetySupervisionJsForm.subState)  return;
        safetySupervisionJsForm.subState = true;

    var url = getRootPath()+"/safe/supervision/saveWorkflow.action";
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url : url,
        type : 'POST',
        cache: false,
        data : formData,
        success : function(data) {
            safetySupervisionJsForm.subState = false;
            if(data.success == "true"){
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: safetySupervisionJsForm.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({
                        type:"fail",
                        content: data.errorMessage
                    });
                }
                $("#token").val(data.token);
            }
        },
        error : function() {
            safetySupervisionJsForm.subState = false;
            msgBox.tip({
                type:"fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function update(type, jumpFun) {
    if(safetySupervisionJsForm.subState)  return;
        safetySupervisionJsForm.subState = true;
    var url = getRootPath()+"/safe/supervision/updateWorkflow.action";
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url : url,
        async : false,
        type : 'POST',
        data : formData,
        success : function(data) {
            safetySupervisionJsForm.subState = false;
            if(data.success == "true"){
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: safetySupervisionJsForm.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if(data.labelErrorMessage){
                    showErrors("entityForm", data.labelErrorMessage);
                } else{
                    msgBox.info({
                        type: "fail",
                        content: data.errorMessage
                    });
                }
            }
        },
        error : function() {
            safetySupervisionJsForm.subState = false;
            msgBox.tip({
                type:"fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function validateForm(){
    return $("#entityForm").valid();
}

//校验失败调用的方法
function validateFormFail(){
    safetySupervisionJsForm.subState = false;
    msgBox.info({
        content : $.i18n.prop("JC_SYS_067"),
    });
}

safetySupervisionJsForm.gotoTodo = function(){
    JCFF.loadPage({url:"/safe/supervision/todoList.action"});
}

function projectInit() {
    if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
        $('#entityForm #projectName').click(function () {
            chooseProjectPanel.init(function (resultdata) {
                $('#entityForm #projectId').val(resultdata.id);
                $('#entityForm #projectName').val(resultdata.projectName);
                $('#entityForm #planStartDate').val(resultdata.planStartDate);
                $('#entityForm #planEndDate').val(resultdata.planEndDate);
                $('#entityForm #projectAddress').val(resultdata.projectAddress);
                $('#entityForm #buildArea').val(resultdata.buildArea);
                $('#entityForm #investmentAmount').val(resultdata.investmentAmount);
                if (resultdata.buildDeptId && resultdata.buildDeptIdValue) {
                    safetySupervisionJsForm.companyBuildTree.setData({
                        "id": resultdata.buildDeptId,
                        "text": resultdata.buildDeptIdValue
                    });
                }
                if (resultdata.superviseDeptId && resultdata.superviseDeptIdValue) {
                    safetySupervisionJsForm.companySuperviseTree.setData({
                        "id": resultdata.superviseDeptId,
                        "text": resultdata.superviseDeptIdValue
                    });
                }
                $('#entityForm #projectLeader0').val(resultdata.buildDeptPersion);
                $('#entityForm #projectLeader1').val(resultdata.superviseDeptPersion);
                /*获取单位信息*/
                $.ajax({
                    type: "GET",
                    data: {projectId: resultdata.id, canShow: 1},
                    dataType: "json",
                    url: getRootPath() + "/project/person/queryAll.action",
                    success: function (projectdata) {
                        if (projectdata) {
                            $.ajax({
                                type: "GET",
                                data: {typeCode: 'company_type', parentCode: 'csmp', useFlag: false},
                                dataType: "json",
                                url: getRootPath() + "/dic/getAllDicsByTypeCode.action",
                                success: function (data) {
                                    if (data) {
                                        $.each(projectdata, function (index, person) {
                                            $.each(data, function (i, v) {
                                                if (v.code == person.partakeType){
                                                    safetySupervisionJsForm.companyTypeTree[person.partakeType].setData({
                                                        "id": person.companyId,
                                                        "text": person.companyName
                                                    });

                                                    $('#entityForm #projectLeader'+(index+2)).val(person.leader);
                                                    $('#entityForm #phone'+(index+2)).val(person.phone);
                                                    return false;
                                                }
                                                ;
                                            });
                                        });
                                    };
                                }
                            });

                        }
                    }
                });
            });
        });
    }
}

function companyInit(){
    safetySupervisionJsForm.companyBuildTree = JCTree.init({

        container: "companyBuildFormDiv",
        controlId: "companyBuildFormDiv-companyprojectId",
        isCheckOrRadio: false,
        isPersonOrOrg: false,
        urls: {
            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
        },
        callback: function(obj){
            if(obj){
                $('#entityForm #unitName0').val(obj.id);
            }
        }
    });
  safetySupervisionJsForm.companyTypeTree={};
    safetySupervisionJsForm.companySuperviseTree = JCTree.init({
        container: "companySuperviseFormDiv",
        controlId: "companySuperviseFormDiv-companyprojectId",
        isCheckOrRadio: false,
        isPersonOrOrg: false,
        urls: {
            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
        },
        callback: function(obj){
            if(obj){
                $('#entityForm #unitName1').val(obj.id);
            }
        }
    });
    $.ajax({
        type : "GET",
        data : {typeCode: 'company_type',parentCode:'csmp',useFlag:false},
        dataType : "json",
        url : getRootPath() + "/dic/getAllDicsByTypeCode.action",
        success : function(data) {
            if (data) {
                $.each(data,function(i,v){
                    safetySupervisionJsForm.companyTypeTree[v.code] = JCTree.init({
                        container: "companyFormDiv"+v.code,
                        controlId: "companyFormDiv"+v.code+"-companyprojectId",
                        isCheckOrRadio: false,
                        isPersonOrOrg: false,
                        urls: {
                            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                        },
                        callback: function(obj){
                            if(obj){
                                $('#entityForm #unitName'+(i+2)).val(obj.id);
                            }
                        }
                    });
                })
            }
        }
    });
}


safetySupervisionJsForm.getcompanyTypeDic=function(code){

    /**获取单位类型字典*/
    var  companyTypeDic;
    $.ajax({
        type : "GET",
        data : {typeCode: code,parentCode:'csmp',useFlag:false},
        dataType : "json",
        async:false,
        url : getRootPath() + "/dic/getAllDicsByTypeCode.action",
        success : function(data) {
            if (data) {
                companyTypeDic=data;
            }
        }
    });
return companyTypeDic;
}



$(document).ready(function(){
    ie8StylePatch();

    $(".datepicker-input").each(function(){$(this).datepicker();});

    $("#saveAndClose").click(function(){safetySupervisionJsForm.saveOrModify(true);});
    $("#saveOrModify").click(function(){safetySupervisionJsForm.saveOrModify(false);});
    $("#formDivClose").click(function(){$('#myModal-edit').modal('hide');});


    var businessJson = $("#businessJson").val();
    if(businessJson.length>0){
    var businessObj = eval("("+businessJson+")");
        $("#entityForm").fill(businessObj);
        $("#entityForm #companyTypeDiv").empty();
        var tableHtml="";
        tableHtml+='<table class="table table-striped tab_height" id="projectPlanTable">';
        tableHtml+='<thead>';
        tableHtml+='<tr>';
        tableHtml+='<th style="text-align: center">单位类型</th>';
        tableHtml+='<th style="text-align: center">单位名称</th>';
        tableHtml+='<th style="text-align: center">项目负责人</th>';
        tableHtml+='<th style="text-align: center">联系电话</th>';
        tableHtml+='</tr>';


        for(var i=0;i<businessObj.safetyUnitList.length;i++){
            var safetyUnit=businessObj.safetyUnitList[i];
            tableHtml+='<tr>';
            tableHtml+='<td style="text-align: center">';
            tableHtml+=safetyUnit.partakeTypeValue;
            tableHtml+='</td>';
            tableHtml+='<td style="text-align: center">';
            tableHtml+=safetyUnit.unitNameValue;
            tableHtml+='</td>';
            tableHtml+='<td style="text-align: left">';
            tableHtml+=safetyUnit.projectLeader;
            tableHtml+='</td>';
            tableHtml+='<td style="text-align: left">';
            tableHtml+=safetyUnit.phone;
            tableHtml+='</td>';
            tableHtml+='</tr>';
        }

        tableHtml+='</thead>';
        tableHtml+='</table>';
        $("#entityForm #companyTypeDiv").append(tableHtml);



    /*var companyTypeDic=safetySupervisionJsForm.getcompanyTypeDic('company_type');

         for(var i=0;i<businessObj.safetyUnitList.length;i++){
             var otherIndex=2;
             var safetyUnit=businessObj.safetyUnitList[i];
             if(safetyUnit.partakeType=='projectBuild'){
                 if (safetyUnit.unitName && safetyUnit.unitNameValue) {
                     safetySupervisionJsForm.companyBuildTree.setData({
                         "id": safetyUnit.unitName,
                         "text": safetyUnit.unitNameValue
                     });
                 }
                 $('#entityForm #partakeType0').val(safetyUnit.partakeType);
                 $('#entityForm #unitName0').val(safetyUnit.unitName);
                 $('#entityForm #projectLeader0').val(safetyUnit.projectLeader);
                 $('#entityForm #phone0').val(safetyUnit.phone);


             } else if(safetyUnit.partakeType=='projectSupervise'){
                 if (safetyUnit.unitName && safetyUnit.unitNameValue) {
                     safetySupervisionJsForm.companySuperviseTree.setData({
                         "id": safetyUnit.unitName,
                         "text": safetyUnit.unitNameValue
                     });
                 }
                 $('#entityForm #partakeType1').val(safetyUnit.partakeType);
                 $('#entityForm #unitName1').val(safetyUnit.unitName);
                 $('#entityForm #projectLeader1').val(safetyUnit.projectLeader);
                 $('#entityForm #phone1').val(safetyUnit.phone);
             }else{
                 otherIndex++;
                 debugger;
                 for(var m=0;m<companyTypeDic.length;m++ ){

                     if (companyTypeDic[m].code == safetyUnit.partakeType){
                         safetySupervisionJsForm.companyTypeTree[safetyUnit.partakeType].setData({
                             "id": safetyUnit.unitName,
                             "text": safetyUnit.unitNameValue
                         });
                         $('#entityForm #projectLeader'+(otherIndex)).val(safetyUnit.leader);
                         $('#entityForm #phone'+(otherIndex)).val(safetyUnit.phone);
                         return false;
                     };
                 }


             }
         }
       /!* if (resultdata.superviseDeptId && resultdata.superviseDeptIdValue) {
            safetySupervisionJsForm.companySuperviseTree.setData({
                "id": resultdata.superviseDeptId,
                "text": resultdata.superviseDeptIdValue
            });
        }*!/


*/
        if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1') {
            liuAttachPool.initPageAttach(1, $('#entityForm #id').val() ,"cm_safety_supervision");
        } else {
            liuAttachPool.initPageAttachOnView(1, $('#entityForm #id').val() ,"cm_safety_supervision");
        }

    }else{
        liuAttachPool.initPageAttach(1 , 0 ,"cm_safety_supervision");
        projectInit();
        companyInit();
    }

    formPriv.doForm();
});

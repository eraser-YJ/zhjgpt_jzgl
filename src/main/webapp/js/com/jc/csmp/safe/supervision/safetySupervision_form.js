//初始化方法
var safetySupervisionJsForm = {};
//重复提交标识
safetySupervisionJsForm.subState = false;
safetySupervisionJsForm.companyTypeDic;

safetySupervisionJsForm.safetyUnitIndex = 0;
/*是否已经生成通知书*/
var isCreateNote=false;
/*当前节点是否需要生成通知书;*/
var needNote=false;
function insert(type, jumpFun) {

    if (safetySupervisionJsForm.subState) return;
    safetySupervisionJsForm.subState = true;

    var url = getRootPath() + "/safe/supervision/saveWorkflow.action";
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getAllFiles()});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: formData,
        success: function (data) {
            safetySupervisionJsForm.subState = false;
            if (data.success == "true") {
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: safetySupervisionJsForm.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else {
                    msgBox.info({
                        type: "fail",
                        content: data.errorMessage
                    });
                }
                $("#token").val(data.token);
            }
        },
        error: function () {
            safetySupervisionJsForm.subState = false;
            msgBox.tip({
                type: "fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function update(type, jumpFun) {
    if (safetySupervisionJsForm.subState) return;
    safetySupervisionJsForm.subState = true;
    var url = getRootPath() + "/safe/supervision/updateWorkflow.action";
    var formData = $("#entityForm").serializeArray();
    formData.push({"name": "attachFile", "value": liuAttachPool.getFiles(1)});
    formData.push({"name": "deleteAttachFile", "value": liuAttachPool.getDeleteFiles(1)});
    jQuery.ajax({
        url: url,
        async: false,
        type: 'POST',
        data: formData,
        success: function (data) {
            safetySupervisionJsForm.subState = false;
            if (data.success == "true") {
                msgBox.tip({
                    type: "success",
                    content: data.successMessage,
                    callback: safetySupervisionJsForm.gotoTodo
                });
                $("#token").val(data.token);
            } else {
                if (data.labelErrorMessage) {
                    showErrors("entityForm", data.labelErrorMessage);
                } else {
                    msgBox.info({
                        type: "fail",
                        content: data.errorMessage
                    });
                }
            }
        },
        error: function () {
            safetySupervisionJsForm.subState = false;
            msgBox.tip({
                type: "fail",
                content: $.i18n.prop("JC_SYS_002")
            });
        }
    });
}

function validateForm() {
    /*if(validateNote()==true){
        return $("#entityForm").valid();
    }*/
/*当前是否需要生成通知书验证*/
    validateNote();
    if(needNote==true){
        if( isCreateNote==true){
            return $("#entityForm").valid();
        }else{
            return false;
        }
    }else{
        return $("#entityForm").valid();
    }
}

function  validateNote(){
    var formItemPrivJsonStr = $("[id='workflowBean.formItemPrivJsonStr']").val();
    if (formItemPrivJsonStr.length > 0) {
        var formItemPrivJsonStrObj = eval("(" + formItemPrivJsonStr + ")");
        $.each(formItemPrivJsonStrObj.todo, function (index, item) {
            if(formItemPrivJsonStrObj.todo[index].formName=="createAdviceNote"){
                if(formItemPrivJsonStrObj.todo[index].privilege=="edit"){
                    needNote=true;
                    $.ajax({
                        type: "GET",
                        data: {id: $("#id").val()},
                        dataType: "json",
                        async: false,
                        url: getRootPath() + "/safe/supervision/get.action",
                        success: function (item) {
                            if (item) {
                                if (item.isAdvice == 1) {
                                    /*通知书已经生成了*/
                                    isCreateNote=true;
                                }

                            }
                        }
                    })
                   return false;
                }
            }
        });
    }

}

//校验失败调用的方法
function validateFormFail() {

    safetySupervisionJsForm.subState = false;
    if(needNote==true){
        if( isCreateNote==false) {
            msgBox.info({
                content: '请先生成通知书',
            });
        }
    }else{
        msgBox.info({
            content: $.i18n.prop("JC_SYS_067"),
        });

    }
}

safetySupervisionJsForm.gotoTodo = function () {
    JCFF.loadPage({url: "/safe/supervision/myList.action"});
}
function projectInit() {
    var isExist = false;



    $('#entityForm #projectName').click(function () {
        chooseSafeytProjectPanel.init(function (resultdata) {
            $.ajax({
                type: "GET",
                data: {projectId: resultdata.id},
                dataType: "json",
                async: false,
                url: getRootPath() + "/safe/supervision/get.action",
                success: function (item) {
                    if (item.id) {
                        isExist = true;
                        /*   debugger;
                           alert("存在了");
                           msgBox.info({
                               content: '该工程已经申请',
                           });
                           return;*/
                    }
                }
            });
            if (isExist==false) {
                $('#entityForm #projectId').val(resultdata.id);
                $('#entityForm #projectNumber').val(resultdata.projectNumber);
                $('#entityForm #projectName').val(resultdata.projectName);
                $('#entityForm #planStartDate').val(resultdata.planStartDate);
                $('#entityForm #planEndDate').val(resultdata.planEndDate);
                $('#entityForm #projectAddress').val(resultdata.projectAddress);
                $('#entityForm #buildArea').val(resultdata.buildArea);
                $('#entityForm #investmentAmount').val(resultdata.investmentAmount);
                $('#entityForm #buildUnit').val(resultdata.buildDeptId);
                $('#entityForm #supervisionUnit').val(resultdata.superviseDeptId);

                safetySupervisionJsForm.safetyUnitIndex = 0;
                safetySupervisionJsForm.autoRowSafetyUnit.replacement();

                if (resultdata.buildDeptId && resultdata.buildDeptIdValue) {

                    safetySupervisionJsForm.companyTypeTree = {};

                    /**增加建设单位*/
                    safetySupervisionJsForm.autoRowSafetyUnit.addOneLine();
                    safetySupervisionJsForm.safetyUnitIndex -= 1;
                    safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex] = JCTree.init({
                        container: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex,
                        controlId: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex + "-unitId-" + safetySupervisionJsForm.safetyUnitIndex,
                        isCheckOrRadio: false,
                        isPersonOrOrg: false,
                        urls: {
                            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                        },
                        callback: function (obj) {
                            if (obj) {
                                var containerIndex = obj.container.containerId.substr(obj.container.containerId.lastIndexOf("-") + 1);
                                $('#entityForm #unitName' + containerIndex).val(obj.id);


                            }
                        }
                    });


                    $('#partakeTypeTD' + safetySupervisionJsForm.safetyUnitIndex).html('建设单位<input type="hidden" id="partakeType' + safetySupervisionJsForm.safetyUnitIndex + '" name="safetyUnitList[' + safetySupervisionJsForm.safetyUnitIndex + '].partakeType" value="projectBuild">');
                    $('#entityForm #unitName' + safetySupervisionJsForm.safetyUnitIndex).val(resultdata.buildDeptId);
                    $('#entityForm #projectLeader' + safetySupervisionJsForm.safetyUnitIndex).val(resultdata.buildDeptPersion);


                    safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex].setData({
                        "id": resultdata.buildDeptId,
                        "text": resultdata.buildDeptIdValue
                    });

                    /**增加监管单位*/
                    safetySupervisionJsForm.safetyUnitIndex += 1;
                    safetySupervisionJsForm.autoRowSafetyUnit.addOneLine();
                    safetySupervisionJsForm.safetyUnitIndex -= 1;
                    safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex] = JCTree.init({
                        container: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex,
                        /** 最后的 safetySupervisionJsForm.safetyUnitIndex作为人员选择树的动态脚标值，用于动态添加行人员选择树选择之后定位是那棵树。*/
                        controlId: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex + "-unitId-" + safetySupervisionJsForm.safetyUnitIndex,
                        isCheckOrRadio: false,
                        isPersonOrOrg: false,
                        urls: {
                            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                        },
                        callback: function (obj) {
                            if (obj) {
                                var containerIndex = obj.container.containerId.substr(obj.container.containerId.lastIndexOf("-") + 1);
                                $('#entityForm #unitName' + containerIndex).val(obj.id);
                            }
                        }
                    });
                    $('#partakeTypeTD' + safetySupervisionJsForm.safetyUnitIndex).html('监管单位<input type="hidden" id="partakeType' + safetySupervisionJsForm.safetyUnitIndex + '" name="safetyUnitList[' + safetySupervisionJsForm.safetyUnitIndex + '].partakeType" value="projectSupervise">');
                    $('#entityForm #unitName' + safetySupervisionJsForm.safetyUnitIndex).val(resultdata.superviseDeptId);
                    $('#entityForm #projectLeader' + safetySupervisionJsForm.safetyUnitIndex).val(resultdata.superviseDeptPersion);

                    safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex].setData({
                        "id": resultdata.superviseDeptId,
                        "text": resultdata.superviseDeptIdValue
                    });
                }

                $.ajax({
                    type: "GET",
                    data: {projectId: resultdata.id, canShow: 1},
                    dataType: "json",
                    url: getRootPath() + "/project/person/queryAll.action",
                    success: function (data) {
                        if (data) {
                            $.each(data, function (index, item) {
                                safetySupervisionJsForm.safetyUnitIndex += 1;
                                safetySupervisionJsForm.autoRowSafetyUnit.addOneLine();
                                safetySupervisionJsForm.safetyUnitIndex -= 1;

                                safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex] = JCTree.init({
                                    container: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex,
                                    controlId: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex + "-unitId-" + safetySupervisionJsForm.safetyUnitIndex,
                                    isCheckOrRadio: false,
                                    isPersonOrOrg: false,
                                    urls: {
                                        org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                                    },
                                    callback: function (obj) {
                                        var containerIndex = obj.container.containerId.substr(obj.container.containerId.lastIndexOf("-") + 1);
                                        $('#entityForm #unitName' + containerIndex).val(obj.id);
                                    }
                                });
                                safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex].setData({
                                    "id": item.companyId,
                                    "text": item.companyName
                                });
                                $('#entityForm #unitName' + safetySupervisionJsForm.safetyUnitIndex).val(item.companyId);
                                $('#entityForm #projectLeader' + safetySupervisionJsForm.safetyUnitIndex).val(item.leader);
                                $('#entityForm #phone' + safetySupervisionJsForm.safetyUnitIndex).val(item.phone);
                                $('#entityForm #partakeType' + safetySupervisionJsForm.safetyUnitIndex).val(item.partakeType);

                            });

                        }
                    }
                });
                safetySupervisionJsForm.safetyUnitIndex += 1;
            } else {
                isExist = false;
                msgBox.info({
                    content: '该工程已经申请',
                });
                return;
            }
        });
    });
}

function companyInit() {
    safetySupervisionJsForm.companyBuildTree = JCTree.init({

        container: "companyBuildFormDiv",
        controlId: "companyBuildFormDiv-companyprojectId",
        isCheckOrRadio: false,
        isPersonOrOrg: false,
        urls: {
            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
        },
        callback: function (obj) {
            if (obj) {
                $('#entityForm #unitName0').val(obj.id);
            }
        }
    });
    safetySupervisionJsForm.companyTypeTree = {};
    safetySupervisionJsForm.companySuperviseTree = JCTree.init({
        container: "companySuperviseFormDiv",
        controlId: "companySuperviseFormDiv-companyprojectId",
        isCheckOrRadio: false,
        isPersonOrOrg: false,
        urls: {
            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
        },
        callback: function (obj) {
            if (obj) {
                $('#entityForm #unitName1').val(obj.id);
            }
        }
    });
    $.ajax({
        type: "GET",
        data: {typeCode: 'company_type', parentCode: 'csmp', useFlag: false},
        dataType: "json",
        url: getRootPath() + "/dic/getAllDicsByTypeCode.action",
        success: function (data) {
            if (data) {
                $.each(data, function (i, v) {
                    safetySupervisionJsForm.companyTypeTree[v.code] = JCTree.init({
                        container: "companyFormDiv" + v.code,
                        controlId: "companyFormDiv" + v.code + "-companyprojectId",
                        isCheckOrRadio: false,
                        isPersonOrOrg: false,
                        urls: {
                            org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                        },
                        callback: function (obj) {
                            if (obj) {
                                $('#entityForm #unitName' + (i + 2)).val(obj.id);
                            }
                        }
                    });
                })
            }
        }
    });
}


safetySupervisionJsForm.getcompanyTypeDic = function (code) {

    /**获取单位类型字典*/
    var companyTypeDic;
    $.ajax({
        type: "GET",
        data: {typeCode: code, parentCode: 'csmp', useFlag: false},
        dataType: "json",
        async: false,
        url: getRootPath() + "/dic/getAllDicsByTypeCode.action",
        success: function (data) {
            if (data) {
                companyTypeDic = data;
            }
        }
    });
    return companyTypeDic;
}

safetySupervisionJsForm.getcompanyEntity = function (companyId) {
    var deptEntity;

    $.ajax({
        type: "GET",
        data: {id: companyId},
        dataType: "json",
        async: false,
        url: getRootPath() + "/department/get.action",
        success: function (data) {
            if (data) {
                deptEntity = data;
            }
        }
    });
    return deptEntity;
}

/** 初始化动态添加行*/
function initdynrow() {

    safetySupervisionJsForm.autoRowSafetyUnit = new Dynrow("safetyUnitTable", {
        tmplId: "autoRowSafetyUnitTmpl",
        deflutOne: false,
        addNextId: "safetyUniTr",// tr的id从这行开始追加行数 选填
        callback: function (e) {
            safetySupervisionJsForm.safetyUnitIndex++;
        },
        deleteBefore: function (obj) {
        },
        deleteback: function (len) {
        }
    });
    /** 动态添加行添加行*/
    $("#addRow").click(function () {

        if ($('#entityForm #projectId').val() == '') {
            msgBox.info({
                type: "fail",
                content: '请先选择项目'
            });
            return;
        }

        safetySupervisionJsForm.safetyUnitIndex += 1;
        safetySupervisionJsForm.autoRowSafetyUnit.addOneLine();
        safetySupervisionJsForm.safetyUnitIndex -= 1;

        safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex] = JCTree.init({
            container: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex,
            controlId: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex + "-unitId-" + safetySupervisionJsForm.safetyUnitIndex,
            isCheckOrRadio: false,
            isPersonOrOrg: false,
            urls: {
                org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
            },
            callback: function (obj) {
                var companyEntity = safetySupervisionJsForm.getcompanyEntity(obj.id);
                var containerIndex = obj.container.containerId.substr(obj.container.containerId.lastIndexOf("-") + 1);
                $('#entityForm #unitName' + containerIndex).val(obj.id);
                $('#entityForm #projectLeader' + containerIndex).val(companyEntity.displayName);

            }
        });

    });
}

function initfromData(resultData) {
    if (resultData) {
        $.each(resultData, function (index, item) {
            if (item.unitName) {
                safetySupervisionJsForm.companyTypeTree = {};
                safetySupervisionJsForm.autoRowSafetyUnit.addOneLine();
                safetySupervisionJsForm.safetyUnitIndex -= 1;
                safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex] = JCTree.init({
                    container: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex,
                    controlId: "companyFormDiv" + safetySupervisionJsForm.safetyUnitIndex + "-unitId-" + safetySupervisionJsForm.safetyUnitIndex,
                    isCheckOrRadio: false,
                    isPersonOrOrg: false,
                    urls: {
                        org: getRootPath() + "/common/api/deptTree.action?rootCode=COMPANY"
                    },
                    callback: function (obj) {
                        var companyEntity = safetySupervisionJsForm.getcompanyEntity(obj.id);
                        var containerIndex = obj.container.containerId.substr(obj.container.containerId.lastIndexOf("-") + 1);
                        $('#entityForm #unitName' + containerIndex).val(obj.id);
                        $('#entityForm #projectLeader' + containerIndex).val(companyEntity.displayName);

                    }
                });

                safetySupervisionJsForm.companyTypeTree[safetySupervisionJsForm.safetyUnitIndex].setData({
                    "id": item.unitName,
                    "text": item.unitNameValue
                });
                $('#entityForm #unitId' + safetySupervisionJsForm.safetyUnitIndex).val(item.id);
                $('#entityForm #unitName' + safetySupervisionJsForm.safetyUnitIndex).val(item.unitName);
                $('#entityForm #projectLeader' + safetySupervisionJsForm.safetyUnitIndex).val(item.projectLeader);
                $('#entityForm #phone' + safetySupervisionJsForm.safetyUnitIndex).val(item.phone);


                if (item.partakeType == 'projectSupervise') {
                    $('#partakeTypeTD' + safetySupervisionJsForm.safetyUnitIndex).html('监管单位<input type="hidden" id="partakeType' + safetySupervisionJsForm.safetyUnitIndex + '" name="safetyUnitList[' + safetySupervisionJsForm.safetyUnitIndex + '].partakeType" value="projectSupervise">');
                } else if (item.partakeType == 'projectBuild') {
                    $('#partakeTypeTD' + safetySupervisionJsForm.safetyUnitIndex).html('建设单位<input type="hidden" id="partakeType' + safetySupervisionJsForm.safetyUnitIndex + '" name="safetyUnitList[' + safetySupervisionJsForm.safetyUnitIndex + '].partakeType" value="projectBuild">');
                } else {
                    $('#entityForm #partakeType' + safetySupervisionJsForm.safetyUnitIndex).val(item.partakeType);
                }

                safetySupervisionJsForm.safetyUnitIndex += 1;
            }
        });
    }

}
function preview(oper)
{
    if (oper < 10)
    {
        $("#printDiv").show();
        bdhtml=window.document.body.innerHTML;//获取当前页的html代码
        sprnstr="<!--startprint"+oper+"-->";//设置打印开始区域
        eprnstr="<!--endprint"+oper+"-->";//设置打印结束区域
        prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html

        prnhtmlprnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html
        window.document.body.innerHTML=prnhtml;
        window.print();
        window.document.body.innerHTML=bdhtml;
    } else {
        window.print();
    }
    $("#printDiv").hide();
}

function initButton() {

    $("#workflowButtongoBack").before('<button class="btn dark" id="createAdviceNote" type="button" workFlowForm="button" itemName="createAdviceNote">生成通知书</button>');


   if($("[id='workflowBean.flowStatus_']").val()=="END"){
        $("#workflowButtongoBack").before('<button class="btn dark" id="printTheForm" type="button" >打  印</button>');
   }
    $("#printTheForm").click(function () {
        preview(1);

    });
    $("#createAdviceNote").click(function () {
        $.ajax({
            type: "GET",
            data: {id: $("#id").val()},
            dataType: "json",
            async: false,
            url: getRootPath() + "/safe/supervision/get.action",
            success: function (item) {
                if (item) {
                    // if (item.isAdvice == 1) {
                    //     msgBox.info({
                    //         content: '已生成通知书，不能重复生成',
                    //     });
                    // } else {
                        $.ajax({
                            type: "GET",
                            data: {id: $("#id").val()},
                            dataType: "json",
                            async: false,
                            url: getRootPath() + "/safe/supervision/createAdviceNote.action",
                            success: function (data) {
                                if (data) {
                                    if (data.success == 'true') {
                                        msgBox.info({
                                            content: '通知书生成成功',
                                        });
                                    } else {
                                        msgBox.info({
                                            content: '通知书生成失败',
                                        });

                                    }
                                }
                            }
                        });
                    }
                // }
            }
        });


    })
    $("#downAdviceNote").click(function () {
        window.location.href = getRootPath() + "/safe/supervision/downAdviceNote.action?id=" + $("#id").val();
    })

}

function initItemAttach() {

    var itemCode = $("#itemCode").val();

    $.ajax({
        type: "GET",
        data: {itemCode: itemCode},
        dataType: "json",
        async: false,
        url: getRootPath() + "/csmp/item/classify/get.action",
        success: function (data) {
            if (data) {
                $.each(data.itemClassifyAttachList, function (index, item) {
                    liuAttachPool.inittMultiplePageAttach(index + 1, 0, "cm_safety_supervision", "cm_safety_supervision-" + item.id);
                });
            }
        }
    });

}
function initPrintData(printData){
$("#projectName_print").html(printData.projectName);
    $("#projectAddress_print").html(printData.projectAddress);
    $("#extStr1_print").html(printData.extStr1);
    $("#planStartDate_print").html(printData.planStartDate);
    $("#planEndDate_print").html(printData.planEndDate);
    $("#buildArea_print").html(printData.buildArea);
    $("#investmentAmount_print").html(printData.investmentAmount);
    $("#projectType_print").html(printData.projectTypeValue);
    $("#extDate1_print").html(printData.extDate1);
    $("#extStr2_print").html(printData.extStr2);
    $("#structureType_print").html(printData.structureTypeValue);

    var  safetyUnitTr="";

    $.each(printData.safetyUnitList, function (index, item) {
        if(null!=item.partakeType){



        safetyUnitTr += ' <tr>\n';
        if (item.partakeType == 'projectSupervise') {
            safetyUnitTr +=  '<td>监管单位</td>\n';
        }else if (item.partakeType == 'projectBuild') {
            safetyUnitTr +=  '<td>建设单位</td>\n';
        }else{
            safetyUnitTr +=  '<td>'+item.partakeTypeValue+'</td>\n';
        }


        safetyUnitTr += '<td>'+item.unitNameValue+'</td>\n' +
            '<td>'+item.projectLeader+'</td>\n' +
            '<td>'+item.phone+'</td>\n'+
            '</tr>';
        }
    });

    $("#printTable tbody").append(safetyUnitTr);
}


$(document).ready(function () {
    ie8StylePatch();

    $(".datepicker-input").each(function () {
        $(this).datepicker();
    });

    $("#saveAndClose").click(function () {
        safetySupervisionJsForm.saveOrModify(true);
    });
    $("#saveOrModify").click(function () {
        safetySupervisionJsForm.saveOrModify(false);
    });
    $("#formDivClose").click(function () {
        $('#myModal-edit').modal('hide');
    });
    initdynrow();
    initButton();
    var businessJson = $("#businessJson").val();

    if (businessJson.length > 0) {
        var businessObj = eval("(" + businessJson + ")");
        $("#entityForm").fill(businessObj);
        initfromData(businessObj.safetyUnitList);
        $.each(businessObj.itemClassify.itemClassifyAttachList, function (index, item) {
            if ($("[id='workflowBean.curNodeId_']").val() == 'usertask1'&&$("[id='workflowBean.openType_']").val()!="DONE") {
                liuAttachPool.inittMultiplePageAttach(index + 1, $('#entityForm #id').val(), "cm_safety_supervision", "cm_safety_supervision-" + item.id);
            } else {
                liuAttachPool.initMultiplePageAttachOnView(index + 1, $('#entityForm #id').val(), "cm_safety_supervision", "cm_safety_supervision-" + item.id);
            }
        });
        /**审批结束之后打印*/
        if($("[id='workflowBean.flowStatus_']").val()=="END"){
            initPrintData(businessObj);
        }



    } else {
        initItemAttach();
        projectInit();
    }
    formPriv.doForm();
});

var def_ = {};

def_.designer = {}

def_.designer.submit = function(){
    var typeId = def_.defTree.selectNode.id.split("_")[1];
    var jsonStr = $("#designerTextarea").val();
    var postData = {
        typeId:typeId,
        jsonStr:jsonStr
    }
    $.post(getRootPath()+"/def/deploy.action",postData,function(data){
        if(data.code == 1){
            var def = data.definition;
            var typeId_ = "type_"+typeId;
            var typeNode = def_.defTree.tree.getNodeByParam("id",typeId_);
            var newNode = {
                id:"def_"+def.id,
                pId:typeId_,
                name:def.name,
                type:"def"
            }
            def_.defTree.tree.addNodes(typeNode,newNode,false);
        }
    });
}

def_.defTree = {}

def_.defTree.onRightClick = function(event, treeId, treeNode){
    if(treeNode){
        if(treeNode.type == "type"){
            $("#rMenu li[type='type']").show();
            $("#rMenu li[type='def']").hide();
        }else if(treeNode.type == "def"){
            $("#rMenu li[type='def']").show();
            $("#rMenu li[type='type']").hide();
        }
        def_.defTree.selectNode = treeNode;
        def_.defTree.tree.selectNode(treeNode);
        def_.defTree.showRMenu("node", event.clientX, event.clientY);
    }
}

def_.defTree.showRMenu = function(type, x, y) {
    $("#rMenu ul").show();
    if (type=="root") {
        $("#m_del").hide();
        $("#m_check").hide();
        $("#m_unCheck").hide();
    } else {
        $("#m_del").show();
        $("#m_check").show();
        $("#m_unCheck").show();
    }
    def_.defTree.rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
    $("body").bind("mousedown", def_.defTree.onBodyMouseDown);
}

def_.defTree.hideRMenu = function(){
    def_.defTree.rMenu.css({"visibility" : "hidden"});
}

def_.defTree.onBodyMouseDown = function(event){
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
        def_.defTree.rMenu.css({"visibility" : "hidden"});
    }
}

def_.defTree.rMenuCreateProcess = function(){
    def_.defTree.hideRMenu();
    $("#designerTextarea").val(def_.demo.bpmnStr);
}

def_.defTree.rMenuDeleteProcess = function(){
    var id = def_.defTree.selectNode.id.split("_")[1];
    $.get(getRootPath()+"/def/delete.action",{id:id},function(data){
        if(data.code == 1){
            var defId = "def_"+id;
            var defNode = def_.defTree.tree.getNodeByParam("id",defId);
            def_.defTree.tree.removeNode(defNode)
        }
        def_.defTree.hideRMenu();
    });
}

def_.defTree.treeSetting = {
    data: {
        simpleData: {
            enable: true
        }
    },
    async: {
        enable: true,
        url:getRootPath()+"/def/getDefAndTypeTreeData.action",
    },
    callback: {
        onRightClick: def_.defTree.onRightClick
    }
}

def_.defTree.init = function(){
    def_.defTree.rMenu = $("#rMenu");
    def_.defTree.tree = $.fn.zTree.init($("#defTree"), def_.defTree.treeSetting);
}

$(document).ready(function(){
    def_.defTree.init();
});


/*---------------------------------------------------------------------------------------------------------------------------------------*/
def_.demo = {}
def_.demo.bpmnStr = '{'+
                        '"resourceId": "1",'+
                        '"properties": {'+
                            '"process_id": "process",'+
                            '"name": "process",'+
                            '"documentation": "",'+
                            '"process_author": "",'+
                            '"process_version": "",'+
                            '"process_namespace": "http://www.activiti.org/processdef",'+
                            '"executionlisteners": "",'+
                            '"eventlisteners": "",'+
                            '"signaldefinitions": "",'+
                            '"messagedefinitions": ""'+
                        '},'+
                        '"stencil": {'+
                            '"id": "BPMNDiagram"'+
                        '},'+
                        '"childShapes": [{'+
                                '"resourceId": "sid-894A1D20-ED5B-4C0B-AE6B-B02D125F54C9",'+
                                '"properties": {'+
                                    '"overrideid": "start1",'+
                                    '"name": "开始",'+
                                    '"documentation": "",'+
                                    '"executionlisteners": "",'+
                                    '"initiator": "",'+
                                    '"formkeydefinition": "",'+
                                    '"formproperties": ""'+
                                '},'+
                                '"stencil": {'+
                                    '"id": "StartNoneEvent"'+
                                '},'+
                                '"childShapes": [],'+
                                '"outgoing": [{'+
                                    '"resourceId": "sid-92A7CA19-3700-4948-9EB0-71521DC124E0"'+
                                '}],'+
                                '"bounds": {'+
                                    '"lowerRight": {'+
                                        '"x": 123.5,'+
                                        '"y": 122'+
                                    '},'+
                                    '"upperLeft": {'+
                                        '"x": 93.5,'+
                                        '"y": 92'+
                                    '}'+
                                '},'+
                                '"dockers": []'+
                            '}, {'+
                                '"resourceId": "sid-20252904-30E1-4C0C-926B-34D2BB8B58B9",'+
                                '"properties": {'+
                                    '"overrideid": "userTask1",'+
                                    '"name": "审批1",'+
                                    '"documentation": "",'+
                                    '"asynchronousdefinition": "false",'+
                                    '"exclusivedefinition": "false",'+
                                    '"executionlisteners": "",'+
                                    '"multiinstance_type": "None",'+
                                    '"multiinstance_cardinality": "",'+
                                    '"multiinstance_collection": "",'+
                                    '"multiinstance_variable": "",'+
                                    '"multiinstance_condition": "",'+
                                    '"isforcompensation": "false",'+
                                    '"usertaskassignment": "",'+
                                    '"formkeydefinition": "",'+
                                    '"duedatedefinition": "",'+
                                    '"prioritydefinition": "",'+
                                    '"formproperties": "",'+
                                    '"tasklisteners": ""'+
                                '},'+
                                '"stencil": {'+
                                    '"id": "UserTask"'+
                                '},'+
                                '"childShapes": [],'+
                                '"outgoing": [{'+
                                    '"resourceId": "sid-CC3851F6-0A36-4530-A89E-4DF7F308C467"'+
                                '}],'+
                                '"bounds": {'+
                                    '"lowerRight": {'+
                                        '"x": 265,'+
                                        '"y": 140'+
                                    '},'+
                                    '"upperLeft": {'+
                                        '"x": 165,'+
                                        '"y": 60'+
                                    '}'+
                                '},'+
                                '"dockers": []'+
                            '}, {'+
                                '"resourceId": "sid-92A7CA19-3700-4948-9EB0-71521DC124E0",'+
                                '"properties": {'+
                                    '"overrideid": "sequenceFlow1",'+
                                    '"name": "",'+
                                    '"documentation": "",'+
                                    '"conditionsequenceflow": "",'+
                                    '"executionlisteners": "",'+
                                    '"defaultflow": "false"'+
                                '},'+
                                '"stencil": {'+
                                    '"id": "SequenceFlow"'+
                                '},'+
                                '"childShapes": [],'+
                                '"outgoing": [{'+
                                    '"resourceId": "sid-20252904-30E1-4C0C-926B-34D2BB8B58B9"'+
                                '}],'+
                                '"bounds": {'+
                                    '"lowerRight": {'+
                                        '"x": 164.25390625,'+
                                        '"y": 107'+
                                    '},'+
                                    '"upperLeft": {'+
                                        '"x": 124.0234375,'+
                                        '"y": 100'+
                                    '}'+
                                '},'+
                                '"dockers": [{'+
                                    '"x": 15,'+
                                    '"y": 15'+
                                '}, {'+
                                    '"x": 144.25,'+
                                    '"y": 107'+
                                '}, {'+
                                    '"x": 144.25,'+
                                    '"y": 100'+
                                '}, {'+
                                    '"x": 50,'+
                                    '"y": 40'+
                                '}],'+
                                '"target": {'+
                                    '"resourceId": "sid-20252904-30E1-4C0C-926B-34D2BB8B58B9"'+
                                '}'+
                            '}, {'+
                                '"resourceId": "sid-C46DF9DA-8C30-431D-9FFC-0B9FA1141CAB",'+
                                '"properties": {'+
                                    '"overrideid": "userTask2",'+
                                    '"name": "审批2",'+
                                    '"documentation": "",'+
                                    '"asynchronousdefinition": "false",'+
                                    '"exclusivedefinition": "false",'+
                                    '"executionlisteners": "",'+
                                    '"multiinstance_type": "None",'+
                                    '"multiinstance_cardinality": "",'+
                                    '"multiinstance_collection": "",'+
                                    '"multiinstance_variable": "",'+
                                    '"multiinstance_condition": "",'+
                                    '"isforcompensation": "false",'+
                                    '"usertaskassignment": "",'+
                                    '"formkeydefinition": "",'+
                                    '"duedatedefinition": "",'+
                                    '"prioritydefinition": "",'+
                                    '"formproperties": "",'+
                                    '"tasklisteners": ""'+
                                '},'+
                            '"stencil": {'+
                                '"id": "UserTask"'+
                            '},'+
                            '"childShapes": [],'+
                            '"outgoing": [{'+
                                '"resourceId": "sid-F8453352-0401-438C-81A5-3A3CAA36093B"'+
                            '}],'+
                            '"bounds": {'+
                                '"lowerRight": {'+
                                    '"x": 430,'+
                                    '"y": 140'+
                                '},'+
                                '"upperLeft": {'+
                                    '"x": 330,'+
                                    '"y": 60'+
                                '}'+
                            '},'+
                            '"dockers": []'+
                        '}, {'+
                            '"resourceId": "sid-67E03B48-296A-4A46-B488-13F02AA082ED",'+
                            '"properties": {'+
                                '"overrideid": "end1",'+
                                '"name": "结束节点",'+
                                '"documentation": "",'+
                                '"executionlisteners": ""'+
                            '},'+
                            '"stencil": {'+
                                '"id": "EndNoneEvent"'+
                            '},'+
                            '"childShapes": [],'+
                            '"outgoing": [],'+
                            '"bounds": {'+
                                '"lowerRight": {'+
                                    '"x": 883,'+
                                    '"y": 121'+
                                '},'+
                                '"upperLeft": {'+
                                    '"x": 855,'+
                                    '"y": 93'+
                                '}'+
                            '},'+
                            '"dockers": []'+
                        '}, {'+
                            '"resourceId": "sid-CC3851F6-0A36-4530-A89E-4DF7F308C467",'+
                            '"properties": {'+
                                '"overrideid": "sequenceFlow2",'+
                                '"name": "方法",'+
                                '"documentation": "",'+
                                '"conditionsequenceflow": "",'+
                                '"executionlisteners": "",'+
                                '"defaultflow": "false"'+
                            '},'+
                            '"stencil": {'+
                                '"id": "SequenceFlow"'+
                            '},'+
                             '"childShapes": [],'+
                            '"outgoing": [{'+
                                '"resourceId": "sid-C46DF9DA-8C30-431D-9FFC-0B9FA1141CAB"'+
                            '}],'+
                            '"bounds": {'+
                                '"lowerRight": {'+
                                    '"x": 329.37109375,'+
                                    '"y": 100'+
                                '},'+
                                '"upperLeft": {'+
                                    '"x": 265.62890625,'+
                                    '"y": 100'+
                                '}'+
                            '},'+
                            '"dockers": [{'+
                                '"x": 50,'+
                                '"y": 40'+
                            '}, {'+
                                '"x": 50,'+
                                '"y": 40'+
                            '}],'+
                            '"target": {'+
                                '"resourceId": "sid-C46DF9DA-8C30-431D-9FFC-0B9FA1141CAB"'+
                            '}'+
                        '}, {'+
                            '"resourceId": "sid-F8453352-0401-438C-81A5-3A3CAA36093B",'+
                            '"properties": {'+
                                '"overrideid": "sequenceFlow3",'+
                                '"name": "VVVV",'+
                                '"documentation": "",'+
                                '"conditionsequenceflow": "",'+
                                '"executionlisteners": "",'+
                                '"defaultflow": "false"'+
                            '},'+
                            '"stencil": {'+
                                '"id": "SequenceFlow"'+
                            '},'+
                            '"childShapes": [],'+
                            '"outgoing": [{'+
                                '"resourceId": "sid-67E03B48-296A-4A46-B488-13F02AA082ED"'+
                            '}],'+
                            '"bounds": {'+
                                '"lowerRight": {'+
                                    '"x": 854.629008692844,'+
                                    '"y": 106.79428028803662'+
                                '},'+
                                '"upperLeft": {'+
                                    '"x": 430.66396005715603,'+
                                    '"y": 100.72525096196338'+
                                '}'+
                            '},'+
                            '"dockers": [{'+
                                    '"x": 50,'+
                                    '"y": 40'+
                                '}, {'+
                                    '"x": 14,'+
                                    '"y": 14'+
                                '}],'+
                                '"target": {'+
                                    '"resourceId": "sid-67E03B48-296A-4A46-B488-13F02AA082ED"'+
                                '}'+
                            '}],'+
                        '"bounds": {'+
                            '"lowerRight": {'+
                                '"x": 1200,'+
                                '"y": 1050'+
                            '},'+
                            '"upperLeft": {'+
                                '"x": 0,'+
                                '"y": 0'+
                            '}'+
                        '},'+
                        '"stencilset": {'+
                            '"url": "stencilsets/bpmn2.0/bpmn2.0.json",'+
                            '"namespace": "http://b3mn.org/stencilset/bpmn2.0#"'+
                        '},'+
                        '"ssextensions": []'+
                    '}';
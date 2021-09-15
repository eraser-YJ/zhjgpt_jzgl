/**
 * 操作设置
 * @propertyPackages    operationpackage
 */
var ActivitiOperationCtrl = ['$scope', '$q', '$translate', '$timeout', function($scope ,$q ,$translate ,$timeout){
    var defaultValue = null;

    if($scope.property.value !== '' && $scope.property.value !== undefined && $scope.property.value !== null){
        if($scope.property.value.constructor == String){
            defaultValue = angular.fromJson($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }
    //  允许退回       允许拿回       允许提交
    var backnode = [],aimnode = [],submitnode = [];
    //  当前点击的节点
    var thisShape = $scope.currentSelectedShape,shapesArray = [];

    $scope.outputData = {};
    $scope.ohterLeftAll = false;
    $scope.ohterRightAll = false;
    $scope.submitAll = false;
    $scope.operation = {todo : true,have : false};  //待办 已办显示状态
    var shapes = [],canvasChilds = $scope.editor.getCanvas().getChildShapes(),allShapes = [];

    //判断当前节点是否是子流程内的节点
    if(thisShape.parent.getStencil().idWithoutNs() === 'SubProcess'){
        var parentShapes = thisShape.parent.getChildShapes();
        var sequences = [];
        $A(canvasChilds).each(function(shape){
            var type = shape.getStencil().idWithoutNs();
            if(type === 'SequenceFlow'){
                sequences.push(shape);
            }
        });
        shapes = parentShapes.concat(sequences);
    }else{
        shapes = canvasChilds;
    }
    
    try{
    	/**
         * 递归查询所有节点
         */
        function getAllShapes(nodes) {
            $A(nodes).each(function (node) {
                var type = node.getStencil().idWithoutNs();
                
                if (type === 'SubProcess') {
                	getAllShapes(node.getChildShapes());
                } else {
                	allShapes.push(node);
                }
            });
        }
        
        if (canvasChilds && canvasChilds.length > 0) {
        	getAllShapes(canvasChilds);
        }
    	
    	if(allShapes && allShapes.length > 0) {
    		$A(allShapes).each(function (shape) {
    	    	if (shape.getStencil().idWithoutNs() === 'UserTask' && shape.resourceId !== thisShape.resourceId) {
    	    		//允许提交至以下指定节点
                    submitnode.push({
                        id: shape.properties['oryx-overrideid'],
                        name: shape.properties['oryx-name'],
                        group: "",
                        selected: false
                    });
    	    	}
    	    });
    	}
    	
        if(shapes && shapes.length > 0) {
            /**
             * 递归查询当前节点的上一步节点，
             * 如上一步节点是网关类型则继续查找上一步节点，
             * 如上一步节点是路由类型则继续查找上一步节点，
             * 如上一步节点是用户办理类型则停止查找
             */
            function recursive(nodes){
                $A(nodes).each(function (node) {
                    var shape = shapesArray[node.resourceId];
                    if(shape){
                        var type = shape.getStencil().idWithoutNs();
                        if(type === 'UserTask'){
                            //允许从此节点拿回的节点  获取方法
                            aimnode.push({
                                id : shape.properties['oryx-overrideid'],
                                name : shape.properties['oryx-name'],
                                selected : false
                            });
                        }else if(type === 'SequenceFlow'){
                            recursive(shape.incoming);
                        }else if(type === 'InclusiveGateway'){
                            recursive(shape.incoming);
                        }
                    }
                });
            }

            $A(shapes).each(function (shape) {
                /**
                 * 获取所有用户办理节点（不包含当前点击节点）
                 */
                if (shape.getStencil().idWithoutNs() === 'UserTask' && shape.resourceId !== thisShape.resourceId) {
                    //允许退回至以下指定节点
                    backnode.push({
                        id: shape.properties['oryx-overrideid'],
                        name: shape.properties['oryx-name'],
                        selected: false
                    });
                }
                shapesArray[shape.resourceId] = shape;
            });

            recursive(thisShape.incoming);
        }
    }catch (e){}
	
    $scope.nodes = {
        //允许退回至以下指定节点
        operateBackNode : backnode,
        //允许从此节点拿回的节点
        operateAsternNode : aimnode,
		//允许从此节点拿回的节点
        operateSubmitNode : submitnode
    };

    $scope.buttons = defaultValue.buttons;

    if(defaultValue){
        for(var key in defaultValue){
            if(defaultValue.hasOwnProperty(key)){
                if(key === 'nodes'){
                    selected(defaultValue[key] ,$scope.nodes);
                }
            }
        }
    }

    function selected(echo , datas){
        for(var key in datas){
            if(echo.hasOwnProperty(key)){
                var data  = datas[key],
                    voltage   = echo[key];
                for(var i = 0;i < data.length;i++){
                    var itemd = data[i];
                    for(var k = 0;k < voltage.length;k++){
                        var itemv = voltage[k];
                        if(itemv.selected && itemv.id === itemd.id){
                            data[i].group = itemv.group;
                            data[i].selected = true;
                        }
                    }
                }
            }
        }
    }
    //判断对象是否为空
    //function isEmptyObject(e) {
    //    var t;
    //    for (t in e)
    //        return !1;
    //    return !0
    //}
    //系统操作列表点击函数
    $scope.changeNodeValue = function(type){
        var changes = [],selected = [];
        if(type === 'left'){
            changes = $scope.nodes.operateBackNode;
        }

        if(type === 'right'){
            changes = $scope.nodes.operateAsternNode;
        }
        
        if(type === 'submit'){
            changes = $scope.nodes.operateSubmitNode;
        }

        $A(changes).each(function(node){
            if(node.selected){
            	var node_group = document.getElementById(node.id+"_group").value;
            	
            	if(node_group == "" || /^\+?[1-9][0-9]*$/.test(node_group)){
					document.getElementById(node.id+"_exceedError").style.display = "none";
                	node.group = node_group;
                }else{
                	document.getElementById(node.id+"_exceedError").style.display = "block";
            		document.getElementById(node.id+"_group").value = "";
            		node.group = "";
                }
            	
            	selected.push(node);
            }
        });

        if(type === 'left'){
            $scope.ohterLeftAll = selected.length === changes.length;
        }
        if(type === 'right'){
            $scope.ohterRightAll = selected.length === changes.length;
        }
        if(type === 'submit'){
            $scope.submitAll = selected.length === changes.length;
        }
        $scope.save();
    }
    //待办其他设置中全选功能
    $scope.selectAllOperation = function($event ,type){
        var changes = [], status = $event.target.checked;
        if(type === 'left'){
            changes = $scope.nodes.operateBackNode;
        }
        if(type === 'right'){
            changes = $scope.nodes.operateAsternNode;
        }
        if(type === 'submit'){
            changes = $scope.nodes.operateSubmitNode;
        }
        $A(changes).each(function(node){
            node.selected = status;
            
            if (node.selected) {
            	var node_group = document.getElementById(node.id+"_group").value;
                
                if(node_group == "" || /^\+?[1-9][0-9]*$/.test(node_group)){
    				document.getElementById(node.id+"_exceedError").style.display = "none";
                	node.group = node_group;
                }else{
                	document.getElementById(node.id+"_exceedError").style.display = "block";
            		document.getElementById(node.id+"_group").value = "";
            		node.group = "";
                }
            }
        });
        $scope.save();
    }
    //切换待办已办事件
    $scope.tab = function(type){
        if(type === 'have'){
            $scope.safeApply(function(){
                $scope.operation = {todo : false,have : true};
            });

        }
        if(type === 'todo'){
            $scope.safeApply(function(){
                $scope.operation = {todo : true,have : false};
            });
        }
    }
    //获取需要赋值给节点的属性
    //function getParameter(){
    //    var reslutBtn = {};
    //    for(var key in $scope.buttons){
    //        if($scope.buttons.hasOwnProperty(key)){
    //            var parms = $scope.buttons[key];
    //            if(parms.length){
    //                $A(parms).each(function(obj){
    //                    if(obj.selected){
    //                         if(!reslutBtn[key]){
    //                             reslutBtn[key] = [];
    //                         }
    //                        reslutBtn[key].push(obj);
    //                    }
    //                });
    //            }
    //        }
    //    }
    //    var reslutNode = {};
    //    for(var key in $scope.nodes){
    //        if($scope.nodes.hasOwnProperty(key)){
    //            var parms = $scope.nodes[key];
    //            if(parms.length){
    //                $A(parms).each(function(obj){
    //                    if(obj.selected){
    //                        if(!reslutNode[key]){
    //                            reslutNode[key] = [];
    //                        }
    //                        reslutNode[key].push(obj);
    //                    }
    //                });
    //            }
    //        }
    //    }
    //    var reslut = {};
    //    if(!isEmptyObject(reslutBtn)) reslut.buttons = reslutBtn;
    //    if(!isEmptyObject(reslutNode)) reslut.nodes = reslutNode;
    //    return reslut;
    //}
    // Click handler for save button
    $scope.save = function() {
        $scope.property.value = {
            buttons : $scope.buttons,
            nodes : $scope.nodes
        };
        $scope.updatePropertyInModel($scope.property);
    };
    $scope.save();
}];
/**
 * 流程变量
 * @propertyPackages    signaldefinitionspackage
 */
var ActivitiVariableSelectCtrl =  ['$scope', '$q', '$translate', '$modal','$timeout',
    function ($scope, $q, $translate, $modal, $timeout) {
    if($scope.property.group === 'variable'){
        // Put json representing signal definitions on scope
        if ($scope.property.value !== undefined && $scope.property.value !== null && $scope.property.value.length > 0) {

            if ($scope.property.value.constructor == String) {
                $scope.variableDefinitions = JSON.parse($scope.property.value);
            }
            else {
                // Note that we clone the json object rather then setting it directly,
                // this to cope with the fact that the user can click the cancel button and no changes should have happened
                $scope.variableDefinitions = angular.copy($scope.property.value);
            }
        } else {
            $scope.variableDefinitions = [];
        }
        // Array to contain selected signal definitions (yes - we only can select one, but ng-grid isn't smart enough)
        $scope.selectedVariable = [];
        $scope.variableRetrieved = false;
        $scope.variableId = '';
        $scope.variableType = 'number';
        $scope.variableLabels = {};

        var idPromise = $translate('PROPERTY.SIGNALDEFINITIONS.ID');
        var typePromise = $translate('PROPERTY.SIGNALDEFINITIONS.TYPE');

        $q.all([idPromise, typePromise]).then(function (results) {
            $scope.variableLabels.idLabel = results[0];
            $scope.variableLabels.typeLabel = results[1];
            $scope.variableRetrieved = true;

            // Config for grid
            $scope.gridVariableOptions = {
                data: 'variableDefinitions',
                headerRowHeight: 28,
                enableRowSelection: true,
                enableRowHeaderSelection: false,
                multiSelect: false,
                keepLastSelected : false,
                selectedItems: $scope.selectedVariable,
                columnDefs: [
                    {field: 'id', displayName: $scope.variableLabels.idLabel},
                    {field: 'type', displayName: $scope.variableLabels.typeLabel}]
            };
        });
        var variableValidation = function(){
            if(!$scope.variableId) return false;

            var result = true;

            $A($scope.variableDefinitions).each(function(variable){
                if($scope.variableId === variable.id){
                    result = false;
                }
            });

            return result;
        }
        // Click handler for add button
        $scope.addNewVariableDefinition = function () {
            if(variableValidation()){
                var newSignalDefinition = {id: $scope.variableId, type: $scope.variableType};
                $scope.variableDefinitions.push(newSignalDefinition);
                $timeout(function () {
                    $scope.gridVariableOptions.selectItem($scope.variableDefinitions.length - 1, true);
                });

                $scope.variableId = '';
                $scope.variableType = 'number';
                $scope.saveVariable();
            }
        };
        // Click handler for remove button
        $scope.removeVariableDefinition = function () {
            if ($scope.selectedVariable && $scope.selectedVariable.length > 0) {
                var index = $scope.variableDefinitions.indexOf($scope.selectedVariable[0]);
                $scope.gridVariableOptions.selectItem(index, false);
                $scope.variableDefinitions.splice(index, 1);

                $scope.selectedVariable.length = 0;
                if (index < $scope.variableDefinitions.length) {
                    $scope.gridVariableOptions.selectItem(index + 1, true);
                } else if ($scope.variableDefinitions.length > 0) {
                    $scope.gridVariableOptions.selectItem(index - 1, true);
                }
                $scope.saveVariable();
            }
        };
        // Click handler for save button
        $scope.saveVariable = function () {
            if ($scope.variableDefinitions.length > 0) {
                $scope.property.value = $scope.variableDefinitions;
            } else {
                $scope.property.value = null;
            }
            $scope.updatePropertyInModel($scope.property);
        };
    }
}];
/**
 * 字段属性
* @propertyPackages    field_attributepackage
 */
var ActivitiFieldAttributeCtrl = ['$scope' ,'$http' , '$rootScope' , function($scope ,$http ,$rootScope){
    if($scope.property.group === 'field'){
        if($scope.property.value.have == null || $scope.property.value.have.length == 0){
            var dataArray = $rootScope.fieldData ? $rootScope.fieldData : [];
            if(!getNodeType()){
                $scope.property.value.todo = dataArray;
            }
            $scope.property.value.have = dataArray;
        }

        $scope.sheetType = { todo : true, have : true }
        $scope.fieldTypeActive = 'todo';
        $scope.todoCustomProperty = $rootScope.todoCustomProperty;//自定义属性数组
        $scope.todoCustomPropertyArray = $rootScope.todoCustomPropertyArray;//自定义属性数组（页面展示使用）

        var nodetype = $scope.getSelectedNodeType();

        function getNodeType(){
            return nodetype === 'EndNoneEvent';
        }
        function __parseData(newArray , oldArray){
            var result = [];
            if(newArray){
                for(var i = 0; i < newArray.length; i++){
                    var newName = newArray[i].formName;
                    if(oldArray){
                        for(var k = 0; k < oldArray.length; k++){
                            var oldName = oldArray[k].formName;
                            if(newName === oldName){
                                newArray[i].privilege = oldArray[k].privilege;
                                
								//更新数组中的自定义属性
                                if (oldArray[k].customProperty && Object.prototype.toString.call(oldArray[k].customProperty)=='[object Object]') {
                                	newArray[i].customProperty = oldArray[k].customProperty;
                                }
                                
                                break;
                            }
                        }
                        result.push(newArray[i]);
                    }
                }
            }
            return result;
        }
        if(getNodeType()){
            $scope.sheetType.todo = false;
            $scope.fieldTypeActive = 'have';
        }
        var defaultValue = null;

        if ($scope.property.value.constructor == String) {
            if($scope.property.value.length > 0){
                defaultValue = JSON.parse($scope.property.value);
            }
        }else{
            defaultValue = angular.copy($scope.property.value);
        }

        if (defaultValue.version == $scope.staticIncludeVersion) {
            if(!getNodeType()){
                $scope.fieldTodoList = defaultValue.todo;
            }
            $scope.fieldHaveList = defaultValue.have;
        }else{
            var newData = angular.copy($rootScope.fieldData);
            if(!getNodeType()){
                var newDataTodo = angular.copy($rootScope.fieldData);
                $scope.fieldTodoList = angular.copy(__parseData(newDataTodo ,defaultValue.todo));
            }
            $scope.fieldHaveList = __parseData(newData ,defaultValue.have);
        }

        $scope.fieldTodoClick = function(index ,privilege){
            $scope.fieldTodoList[index].privilege = privilege;
            $scope.save();
        }

        $scope.fieldHaveClick = function(index ,privilege){
            $scope.fieldHaveList[index].privilege = privilege;
            $scope.save();
        }
        
        /**
         * 监听自定义属性复选框的操作
         */
        $scope.changeTodoCustomProperty = function(index, customPropertyId, event){
        	var action = event.target;//复选框操作
        	var todoCustomPropertyMap = $scope.todoCustomProperty;//自定义属性数组
        	var customPropertyMap = $scope.fieldTodoList[index].customProperty;//当前字段的自定义属性
        	
        	//选中复选框，新增当前字段的自定义属性
        	if (action.checked) {
    			todoCustomPropertyMap[customPropertyId].value = document.getElementById("todo_"+customPropertyId+"_value_"+index).value;
    			customPropertyMap[customPropertyId] = todoCustomPropertyMap[customPropertyId];
        	} else {//取消复选框，删除当前字段的自定义属性
        		if (customPropertyMap[customPropertyId]) {
        			delete customPropertyMap[customPropertyId];
        		}
        	}
        	
        	$scope.fieldTodoList[index].customProperty = customPropertyMap;//更新当前字段的自定义属性
        	
            $scope.save();
        }
        
        /**
         * 判断自定义属性复选框是否默认选中
         */
        $scope.checkTodoCustomProperty = function(index, customPropertyId){
        	var customPropertyMap = $scope.fieldTodoList[index].customProperty;//当前字段的自定义属性
        	
        	//当前字段存在该自定义属性，默认选中
        	if (customPropertyMap[customPropertyId]) {
        		return true;
        	} else {//当前字段不存在该自定义属性，默认未选中
        		return false;
        	}
        }
        
        /**
         * 监听自定义属性对应的值，如果该值发生变化，并且该自定义属性已选中，则更新当前字段的自定义属性
         */
        $scope.changeTodoCustomPropertyValue = function(index, customPropertyId){
        	var action = document.getElementById("todo_"+customPropertyId+"_"+index);//自定义属性复选框
        	var todoCustomPropertyMap = $scope.todoCustomProperty;//自定义属性数组
        	var customPropertyMap = $scope.fieldTodoList[index].customProperty;//当前字段的自定义属性
        	
        	//自定义属性复选框已选中
        	if (action && action.checked) {
        		var customPropertyValue = document.getElementById("todo_"+customPropertyId+"_value_"+index).value;//自定义属性对应的值
        		
        		//当前字段不存在该自定义属性，则新增当前字段的自定义属性
        		if (!customPropertyMap[customPropertyId]) {
        			todoCustomPropertyMap[customPropertyId].value = customPropertyValue;
        			customPropertyMap[customPropertyId] = todoCustomPropertyMap[customPropertyId];
        		} else {//当前字段存在该自定义属性，则更新对应的值
        			customPropertyMap[customPropertyId].value = customPropertyValue;
        		}
        		
        		$scope.fieldTodoList[index].customProperty = customPropertyMap;//更新当前字段的自定义属性
        		
        		$scope.save();
        	}
        }

        $scope.save = function () {
            var result = {have : $scope.fieldHaveList}
            if(!getNodeType()){
                result.todo = $scope.fieldTodoList;
            }
            //添加一个版本号码,用来识别是否是本次访问,不是本次访问则需要从新查询一次数据源
            result.version = $scope.staticIncludeVersion;
            $scope.property.value = result;
            $scope.updatePropertyInModel($scope.property);
        };

        $scope.save();
    }
}];
/**
 * 事件设置
* @propertyPackages    event_applypackage
 */
var ActivitiEventApplyCtrl = ['$scope', '$q', '$translate','$timeout','$modal', function($scope, $q, $translate,$timeout,$modal) {
    if($scope.property.group === 'event'){
        if ($scope.property.value !== undefined && $scope.property.value !== null && $scope.property.value !== '') {
            if ($scope.property.value.constructor == String) {
                $scope.eventparameters = JSON.parse($scope.property.value);
            }else{
                $scope.eventparameters = angular.copy($scope.property.value);
            }
        } else {
            $scope.eventparameters = [];
        }
        // Array to contain selected properties (yes - we only can select one, but ng-grid isn't smart enough)
        $scope.selectedEventParameters = [];
        $scope.translationsEventRetrieved = false;

        $scope.eventLabels = {};

        var eventNamePromise = $translate('PROPERTY.EVENTSETTING.NAME'),
            eventTypePromise = $translate('PROPERTY.EVENTSETTING.TYPE'),
            eventClassPromise = $translate('PROPERTY.EVENTSETTING.ID'),
            eventParamPromise = $translate('PROPERTY.EVENTSETTING.PARAM');

        $q.all([eventNamePromise, eventTypePromise, eventClassPromise, eventParamPromise]).then(function(results) {
            $scope.eventLabels.eventNameLabel = results[0];
            $scope.eventLabels.eventTypeLabel = results[1];
            $scope.eventLabels.eventClassLabel = results[2];
            $scope.eventLabels.eventParamLabel = results[3];
            $scope.translationsEventRetrieved = true;

            // Config for grid
            $scope.gridEventOptions = {
                data: 'eventparameters',
                enableRowReordering: true,
                headerRowHeight: 28,
                multiSelect: false,
                keepLastSelected : false,
                width:'100px',
                selectedItems: $scope.selectedEventParameters,
                columnDefs: [{ field: 'name', displayName: $scope.eventLabels.eventNameLabel},
                    { field: 'type', displayName: $scope.eventLabels.eventTypeLabel},
                    { field: 'clazz', displayName: $scope.eventLabels.eventClassLabel},
                    { field: 'param', displayName: $scope.eventLabels.eventParamLabel}]
            };
        });

        function showModal(){
            var opts = {
                backdrop: 'static',
                template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/form/event-modal.html?version=' + $scope.staticIncludeVersion,
                scope   : $scope
            };
            $modal(opts);
        }
        $scope.addEventSetting = function(){
            var nodetype = $scope.getSelectedNodeType();
            var deflutData = [];
            switch (nodetype){
                case 'StartNoneEvent':
                    deflutData.push({id : 'out' , name :'离开'});
                    break;
                case 'UserTask':
                    deflutData.push({id : 'in' , name :'进入'});
                    deflutData.push({id : 'out' , name :'离开'});
                    break;
                case 'SequenceFlow':
                    deflutData.push({id : 'consequent' , name :'顺向'});
                    deflutData.push({id : 'reverse' , name :'逆向'});
                    break;
                case 'EndNoneEvent':
                    deflutData.push({id : 'in' , name :'进入'});
                    break;
            }
            $scope.deflutData = deflutData;
            showModal();
        }

        $scope.deleteEventSetting = function(){
            if ($scope.selectedEventParameters.length > 0) {
                var index = $scope.eventparameters.indexOf($scope.selectedEventParameters[0]);
                $scope.gridEventOptions.selectItem(index, false);
                $scope.eventparameters.splice(index, 1);

                $scope.selectedEventParameters.length = 0;
                if (index < $scope.eventparameters.length) {
                    $scope.gridEventOptions.selectItem(index + 1, true);
                } else if ($scope.eventparameters.length > 0) {
                    $scope.gridEventOptions.selectItem(index - 1, true);
                }
                $scope.save();
            }
        }
        $scope.deleteAllEventSetting = function(){
            $scope.eventparameters = [];
        }
        $scope.save = function () {
            $scope.property.value = $scope.eventparameters;
            $scope.updatePropertyInModel($scope.property);
        };
    }
}];
/**
 * 添加事件
 * @type {*[]}
 */
var ActivitiEventSetingModalCtrl = ['$scope' , function($scope){
    $scope.eventTypeData = $scope.deflutData;
    $scope.eventName = '';
    $scope.eventType = $scope.eventTypeData[0];
    $scope.eventClass = '';
    $scope.eventParam = '';
    $scope.eventvaild = false;

    function required(str){
        if(str && str.replace(/^\s\s*/, '').replace(/\s\s*$/, '').length){
            return true
        }
        return false;
    }

    $scope.confirm = function(){
        if(required($scope.eventName) && required($scope.eventClass)){
            $scope.eventvaild = false;
            $scope.eventparameters.push({
                name : $scope.eventName,
                type : $scope.eventType.name,
                clazz: $scope.eventClass,
                param: $scope.eventParam
            });
            $scope.save();
            $scope.cancel();
        }else{
            $scope.eventvaild = true;
        }
    }
    $scope.cancel = function() {
        $scope.close();
    };
    // Close button handler
    $scope.close = function() {
        $scope.$hide();
    };
}];
/**
 * 超期处理
 * @type {*[]}
 */
var KisBpmExceedPropertyCtrl = ['$scope' , function($scope){
    var defaultValue = {};
    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            defaultValue = JSON.parse($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }

    $scope.exceedError = false;

    $scope.exceedType = defaultValue.type ? defaultValue.type : 'natural';

    $scope.exceedDays = defaultValue.days ? defaultValue.days : "";

    $scope.clickExceedRadioValue = function(type){
        if(type) $scope.exceedType = type;
        $scope.save();
    }

    $scope.save = function() {
        if($scope.exceedDays == ''){
            $scope.exceedError = false;
            $scope.property.value = '';
            $scope.updatePropertyInModel($scope.property);
            return false;
        }
        if(/^\+?[1-9][0-9]*$/.test($scope.exceedDays)){
            $scope.exceedError = false;
            $scope.property.value = {
                type : $scope.exceedType,
                days : $scope.exceedDays
            };
            $scope.updatePropertyInModel($scope.property);
        }else{
            $scope.exceedError = true;
        }
    };
}];
/**
 * 超期处理方式
 * @type {*[]}
 */
var KisBpmExceedModePropertyCtrl = ['$scope' , function($scope){

    var defaultValue = {};
    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            defaultValue = JSON.parse($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }

    $scope.myStyleObj = {
        "line-height": "20px",
        "margin-left": "20px",
        "margin-top":"8px"
    }

    $scope.exceedType = defaultValue.type ? defaultValue.type : 'stop';

    $scope.customEvent = defaultValue.event ? defaultValue.event : '';

    if(defaultValue.type === 'custom'){
        $scope.myStyleObj['margin-top'] = '0';
    }

    $scope.clickRadio = function(type){
        $scope.exceedType = type;
        if(type === 'custom'){
            $scope.customEvent = '';
            $scope.myStyleObj['margin-top'] = '0';
        }else{
            $scope.myStyleObj['margin-top'] = '8px';
            $scope.save();
        }
    }

    $scope.save = function() {
        $scope.property.value = {
            type : $scope.exceedType,
            event : $scope.exceedType === 'custom' ? $scope.customEvent : ''
        };
        $scope.updatePropertyInModel($scope.property);
    };
}];
/**
 * 路由条件
 * @propertyPackages    routing_conditionspackage
 */
var ActivitiRoutingConditionsCtrl = ['$scope', function($scope){

    $scope.routingProperties = '';
    if($scope.property.value !== '' && $scope.property.value !== null && $scope.property.value !== undefined){
        $scope.routingProperties = $scope.property.value;
    }

    $scope.addRouting = function(){
        var rout = $scope.routingProperties;

        if($scope.parameter1 && $scope.parameter2){
            rout = (rout.length ? jQuery('#condition').val() : '')+ '(' + ($scope.parameter1 + jQuery('#arithmetic').val() +$scope.parameter2 )+')';
        }

        $scope.routingProperties = $scope.routingProperties + rout;
        $scope.saveRouting();
    }

    $scope.removeRouting = function(){
        $scope.routingProperties = '';
        $scope.saveRouting();
    }

    $scope.saveRouting = function () {
        if ($scope.routingProperties.length > 0) {
            $scope.property.value = $scope.routingProperties;
        } else {
            $scope.property.value = '';
        }
        $scope.updatePropertyInModel($scope.property);
    };
}];
/**
 *  路由 -> 关系定义
 * @type {*[]}
 */
var ActivitiRelationCtrl = ['$scope', '$q', '$translate', '$timeout','$modal','$rootScope', function ($scope, $q, $translate, $timeout, $modal, $rootScope) {

    var obj = null;

    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            obj = JSON.parse($scope.property.value);
        } else {
            obj = angular.copy($scope.property.value);
        }
    }

    var $relationDom = jQuery('#selectRelationQuery');

    $scope.newName = '';
    $scope.newClass= '';

    $scope.selectedRelationSignals = [];          //列表选中选项的容器
    $scope.relationRetrieved = false;             //控制列表加载完成显示
    $scope.labelsRelation = {};                   //列表输出项容器
    $scope.relationDefinitions = obj || [];
    $scope.selectRelationProperty = '';

    var typeRelationPromise = $translate('PROPERTY.SIGNALDEFINITIONS.RELATIONTYPE');
    //配置一个表格，用来显示已选人员的列表
    $q.all([typeRelationPromise]).then(function (results) {
        $scope.labelsRelation.typeLabel = results[0];
        $scope.relationRetrieved = true;
        // 配置表格
        $scope.gridRelationOptions = {
            data: 'relationDefinitions',
            headerRowHeight: 28,
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            enableSorting: false,
            keepLastSelected : false,
            selectedItems: $scope.selectedRelationSignals,
            columnDefs: [{field: 'typeName', displayName: $scope.labelsRelation.typeLabel}]
        };
    });


    $scope.addRelation = function(){
        var selects = $relationDom.find('option:selected');
        if(selects.length){
            selects.each(function(index , item){
                var id = item.value;
                if(hasObj(id ,'id')){
                    $scope.relationDefinitions.push({
                        id : id,
                        typeName : item.innerHTML
                    });
                }
            });
            $scope.save();
        }
    }
    /**
     * 过滤重复
     * @param id
     * @returns {boolean}
     */
    function hasObj(id ,type){
        var result = true;
        for(var i = 0;i < $scope.relationDefinitions.length;i++){
            var relation = $scope.relationDefinitions[i];
            if(id == relation[type]){ result = false;  break;}
        }
        return result;
    }

    $scope.newRelation = function(){
        if($scope.newName && $scope.newClass){
            if(hasObj($scope.newName , 'typeName')){
                $scope.relationDefinitions.push({
                    id : $scope.newClass,
                    typeName : $scope.newName
                });
                $scope.save();
            }
        }
    }

    $scope.deleteRelation = function(){
        if ($scope.selectedRelationSignals && $scope.selectedRelationSignals.length > 0) {
            var index = $scope.relationDefinitions.indexOf($scope.selectedRelationSignals[0]);
            $scope.gridRelationOptions.selectItem(index, false);
            $scope.relationDefinitions.splice(index, 1);

            $scope.selectedRelationSignals.length = 0;
            if (index < $scope.relationDefinitions.length) {
                $scope.gridRelationOptions.selectItem(index + 1, true);
            } else if ($scope.relationDefinitions.length > 0) {
                $scope.gridRelationOptions.selectItem(index - 1, true);
            }
            $scope.save();
        }
    }

    $scope.save = function () {
        if ($scope.relationDefinitions.length > 0) {
            $scope.property.value = $scope.relationDefinitions;
        } else {
            $scope.property.value = "";
        }
        $scope.updatePropertyInModel($scope.property);
    };
}];
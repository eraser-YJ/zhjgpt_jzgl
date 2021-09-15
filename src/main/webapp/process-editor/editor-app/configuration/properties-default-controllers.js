/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
/*
 * String controller
 */

var KisBpmStringPropertyCtrl = [ '$scope', '$rootScope' ,function ($scope , $rootScope) {
    function validStringLength(str ,type){
        var result = '';
        if(!/[~#^$@%&//!*]/g.test(str)){
            if(type == 'oryx-process_id'){
                if(!/^[a-zA-Z]{1}([a-zA-Z0-9]){1,29}$/.test(str)){
                    result = '流程ID只能由小写字母、大写字母和数字组成,第一位必须是字母,且长度不能超过30';
                }
            }else if(type == 'oryx-process_name' || type == 'oryx-name'){
                if(str.length > 30){
                    result = '名称长度不可以大于30个字.';
                }
            }
        }else{
            result = '名称不可以包含特殊字符!'
        }
        return result;
    }
	$scope.shapeId = $scope.selectedShape.id;
	$scope.valueFlushed = false;
    /** Handler called when input field is blurred */
    $scope.inputBlurred = function() {
    	$scope.valueFlushed = true;
    	if ($scope.property.value) {
    		$scope.property.value = $scope.property.value.replace(/(<([^>]+)>)/ig,"");
    	}

        var validStr = validStringLength($scope.property.value ,$scope.property.key);

        $rootScope.vaildDefErrorInfo = validStr;

        if(validStr) return false;

        //if($scope.property.key === 'oryx-process_name'){
        //    console.log($scope.property.value);
        //}

        $scope.updatePropertyInModel($scope.property);
    };

    $scope.enterPressed = function(keyEvent) {
    	if (keyEvent && keyEvent.which === 13) {
    		keyEvent.preventDefault();
	        $scope.inputBlurred(); // we want to do the same as if the user would blur the input field
    	}
    };
    
    $scope.$on('$destroy', function controllerDestroyed() {
    	if(!$scope.valueFlushed) {
    		if ($scope.property.value) {
        		$scope.property.value = $scope.property.value.replace(/(<([^>]+)>)/ig,"");
        	}
    		$scope.updatePropertyInModel($scope.property, $scope.shapeId);
    	}
    });

}];

/*
 * Boolean controller
 */

var KisBpmBooleanPropertyCtrl = ['$scope', function ($scope) {

    $scope.changeValue = function() {
        if ($scope.property.key === 'oryx-defaultflow' && $scope.property.value) {
            var selectedShape = $scope.selectedShape;
            if (selectedShape) {
                var incomingNodes = selectedShape.getIncomingShapes();
                if (incomingNodes && incomingNodes.length > 0) {
                    // get first node, since there can be only one for a sequence flow
                    var rootNode = incomingNodes[0];
                    var flows = rootNode.getOutgoingShapes();
                    if (flows && flows.length > 1) {
                        // in case there are more flows, check if another flow is already defined as default
                        for (var i = 0; i < flows.length; i++) {
                            if (flows[i].resourceId != selectedShape.resourceId) {
                                var defaultFlowProp = flows[i].properties['oryx-defaultflow'];
                                if (defaultFlowProp) {
                                    flows[i].setProperty('oryx-defaultflow', false, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        $scope.updatePropertyInModel($scope.property);
    };

}];

/*
 * Text controller
 */

var KisBpmTextPropertyCtrl = [ '$scope', '$modal', function($scope, $modal) {
    if($scope.property.group === 'transactor') {
        return false;
    };
    var opts = {
        template:  ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/configuration/properties/text-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmTextPropertyPopupCtrl = ['$scope', function($scope) {

    $scope.save = function() {
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    $scope.close = function() {
        $scope.property.mode = 'read';
        $scope.$hide();
    };
}];

var KisBpmRadioPropertyPopupCtrl = ['$scope', function($scope) {
    var defaultValue = {};
    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            defaultValue = JSON.parse($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }

    $scope.radioDataArray = [
        {id : 1 , name : '单选' , selected : defaultValue.id === 1},
        {id : 2 , name : '多选' , selected : defaultValue.id === 2}
    ];

    $scope.changeRadioValue = function(index){
        $scope.property.value = $scope.radioDataArray[index];
        $scope.save();
    }

    $scope.save = function() {
        $scope.updatePropertyInModel($scope.property);
    };
}];

var KisBpmCandidatePropertyPopupCtrl = ['$scope', function($scope) {
    var defaultValue = {};
    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            defaultValue = JSON.parse($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }

    $scope.candidateDataArray = [
        {id : 1 , name : '否' , selected : defaultValue.id === 1},
        {id : 2 , name : '是' , selected : defaultValue.id === 2}
    ];

    $scope.changeCandidateValue = function(index){
        $scope.property.value = $scope.candidateDataArray[index];
        $scope.save();
    }

    $scope.save = function() {
        $scope.updatePropertyInModel($scope.property);
    };
}];


var KisBpmUnionPropertyPopupCtrl = ['$scope', function ($scope) {
    var defaultValue = {};
    if($scope.property.value != '' && $scope.property.value != null && $scope.property.value != undefined){
        if ($scope.property.value.constructor == String) {
            defaultValue = JSON.parse($scope.property.value);
        }else{
            defaultValue = angular.copy($scope.property.value);
        }
    }

    $scope.unionData = [
        {id : 1 , name : '并集' , selected : defaultValue.id === 1},
        {id : 2 , name : '交集' , selected : defaultValue.id === 2}
    ];

    $scope.changeValue = function(index){
        $scope.property.value = $scope.unionData[index];
        $scope.save();
    }

    $scope.save = function() {
        $scope.updatePropertyInModel($scope.property);
    };
}];

var KisBpmBaseSelectPropertyCtrl = ['$scope', '$timeout','$http','$rootScope', function($scope, $timeout, $http, $rootScope){

    $scope.selectPropertyData = [];
    var defaultProperty       = [];

    if($scope.property.value !== undefined && $scope.property.value !== null && $scope.property.value !== ''){
        if ($scope.property.value.constructor == String) {
            defaultProperty = JSON.parse($scope.property.value);
        }else {
            defaultProperty = angular.copy($scope.property.value);
        }
    }

    function joinSelectData(type , idName){
        var array = [],obj = null;
        if(type === 'tType'){
            array = [{id : '1001',name : "单人签核"},{id : '1002',name : "多人单人签核"},{id : '1003',name : "多人处理"}];
            if(defaultProperty.id) {
                $A(array).each(function(item){if(defaultProperty.id == item.id) obj = item;});
            }else{
                obj = array[0];
            }
        }

        if($scope.deployInfoData[type] && $scope.deployInfoData[type].length){
            $A($scope.deployInfoData[type]).each(function(item){
                var objItem = {id : item[idName], name:item.name}
                if(defaultProperty.id == objItem.id) obj = objItem;
                array.push(objItem);
            });
        }

        $scope.safeApply(function(){
            if(obj) {
                $scope.selectProperty = obj;
            }else{
                $scope.property.value = '';
                $scope.updatePropertyInModel($scope.property);
            }
            $scope.selectPropertyData = array;
        });
    }

    if($scope.property.key === 'oryx-transactor_type'){
        //节点中办理类型
        joinSelectData('tType');
    }else if($scope.property.key === 'oryx-process_org'){
        //流程中 配置结构数据
        joinSelectData('orgTree', 'dataId');
    }else if($scope.property.key === 'oryx-form_apply'){
        //流程中 表单选择
        joinSelectData('forms', 'id');
    }else if($scope.property.key === 'oryx-process_type'){
        //流程中 流程类型选择
        joinSelectData('types', 'id');
    }

    $scope.changeSelectValue = function(){
        if($scope.selectProperty !== null){
            var reslut = {
                id : $scope.selectProperty.id + '',
                name : $scope.selectProperty.name
            };
            $scope.property.value = reslut;
            $scope.updatePropertyInModel($scope.property);
            if($scope.property.key === 'oryx-form_apply'){
                $rootScope.getFieldData();
            }else if($scope.property.key === 'oryx-transactor_type'){
                $rootScope.buttonSaveState = 'new';
            }
        }
    }
}];

/*
 * 这个是用来实现变换用户节点样式的Controller
 * Boolean controller
 */

var KisBpmSelectPropertyCtrl = [ '$rootScope', '$scope', '$timeout', '$translate', function($rootScope, $scope, $timeout, $translate)  {
    var selectTransactArray = [];

    for (var i = 0; i < $scope.morphShapes.length; i++) {
        if ($scope.morphShapes[i].id != $scope.currentSelectedShape.getStencil().idWithoutNs())
        {
            selectTransactArray.push($scope.morphShapes[i]);
        }
    }

    $scope.selectTransactData = selectTransactArray;

    $scope.selectTransactStatus = false;

    $scope.changeSelectValue = function() {
        $scope.selectTransactStatus = true;
        if($scope.transact != null){
            var MorphTo = ORYX.Core.Command.extend({
                construct: function(shape, stencil, facade){
                    this.shape = shape;
                    this.stencil = stencil;
                    this.facade = facade;
                },
                execute: function(){

                    var shape = this.shape;
                    var stencil = this.stencil;
                    var resourceId = shape.resourceId;

                    // Serialize all attributes
                    var serialized = shape.serialize();
                    stencil.properties().each((function(prop) {
                        if(prop.readonly()) {
                            serialized = serialized.reject(function(serProp) {
                                return serProp.name==prop.id();
                            });
                        }
                    }).bind(this));

                    // Get shape if already created, otherwise create a new shape
                    if (this.newShape){
                        newShape = this.newShape;
                        this.facade.getCanvas().add(newShape);
                    } else {
                        newShape = this.facade.createShape({
                            type: stencil.id(),
                            namespace: stencil.namespace(),
                            resourceId: resourceId
                        });
                    }

                    // calculate new bounds using old shape's upperLeft and new shape's width/height
                    var boundsObj = serialized.find(function(serProp){
                        return (serProp.prefix === "oryx" && serProp.name === "bounds");
                    });

                    var changedBounds = null;

                    if (!this.facade.getRules().preserveBounds(shape.getStencil())) {

                        var bounds = boundsObj.value.split(",");
                        if (parseInt(bounds[0], 10) > parseInt(bounds[2], 10)) { // if lowerRight comes first, swap array items
                            var tmp = bounds[0];
                            bounds[0] = bounds[2];
                            bounds[2] = tmp;
                            tmp = bounds[1];
                            bounds[1] = bounds[3];
                            bounds[3] = tmp;
                        }
                        bounds[2] = parseInt(bounds[0], 10) + newShape.bounds.width();
                        bounds[3] = parseInt(bounds[1], 10) + newShape.bounds.height();
                        boundsObj.value = bounds.join(",");

                    }  else {

                        var height = shape.bounds.height();
                        var width  = shape.bounds.width();

                        // consider the minimum and maximum size of
                        // the new shape

                        if (newShape.minimumSize) {
                            if (shape.bounds.height() < newShape.minimumSize.height) {
                                height = newShape.minimumSize.height;
                            }


                            if (shape.bounds.width() < newShape.minimumSize.width) {
                                width = newShape.minimumSize.width;
                            }
                        }

                        if(newShape.maximumSize) {
                            if(shape.bounds.height() > newShape.maximumSize.height) {
                                height = newShape.maximumSize.height;
                            }

                            if(shape.bounds.width() > newShape.maximumSize.width) {
                                width = newShape.maximumSize.width;
                            }
                        }

                        changedBounds = {
                            a : {
                                x: shape.bounds.a.x,
                                y: shape.bounds.a.y
                            },
                            b : {
                                x: shape.bounds.a.x + width,
                                y: shape.bounds.a.y + height
                            }
                        };

                    }

                    var oPos = shape.bounds.center();
                    if(changedBounds !== null) {
                        newShape.bounds.set(changedBounds);
                    }

                    // Set all related dockers
                    this.setRelatedDockers(shape, newShape);

                    // store DOM position of old shape
                    var parentNode = shape.node.parentNode;
                    var nextSibling = shape.node.nextSibling;

                    // Delete the old shape
                    this.facade.deleteShape(shape);

                    // Deserialize the new shape - Set all attributes
                    newShape.deserialize(serialized);
                    /*
                     * Change color to default if unchanged
                     * 23.04.2010
                     */
                    if(shape.getStencil().property("oryx-bgcolor")
                        && shape.properties["oryx-bgcolor"]
                        && shape.getStencil().property("oryx-bgcolor").value().toUpperCase()== shape.properties["oryx-bgcolor"].toUpperCase()){
                        if(newShape.getStencil().property("oryx-bgcolor")){
                            newShape.setProperty("oryx-bgcolor", newShape.getStencil().property("oryx-bgcolor").value());
                        }
                    }
                    if(changedBounds !== null) {
                        newShape.bounds.set(changedBounds);
                    }

                    if(newShape.getStencil().type()==="edge" || (newShape.dockers.length==0 || !newShape.dockers[0].getDockedShape())) {
                        newShape.bounds.centerMoveTo(oPos);
                    }

                    if(newShape.getStencil().type()==="node" && (newShape.dockers.length==0 || !newShape.dockers[0].getDockedShape())) {
                        this.setRelatedDockers(newShape, newShape);

                    }

                    // place at the DOM position of the old shape
                    if(nextSibling) parentNode.insertBefore(newShape.node, nextSibling);
                    else parentNode.appendChild(newShape.node);

                    // Set selection
                    this.facade.setSelection([newShape]);
                    this.facade.getCanvas().update();
                    this.facade.updateSelection();
                    this.newShape = newShape;

                },
                rollback: function(){

                    if (!this.shape || !this.newShape || !this.newShape.parent) {return;}

                    // Append shape to the parent
                    this.newShape.parent.add(this.shape);
                    // Set dockers
                    this.setRelatedDockers(this.newShape, this.shape);
                    // Delete new shape
                    this.facade.deleteShape(this.newShape);
                    // Set selection
                    this.facade.setSelection([this.shape]);
                    // Update
                    this.facade.getCanvas().update();
                    this.facade.updateSelection();
                },

                /**
                 * Set all incoming and outgoing edges from the shape to the new shape
                 * @param {Shape} shape
                 * @param {Shape} newShape
                 */
                setRelatedDockers: function(shape, newShape){

                    if(shape.getStencil().type()==="node") {

                        (shape.incoming||[]).concat(shape.outgoing||[])
                            .each(function(i) {
                                i.dockers.each(function(docker) {
                                    if (docker.getDockedShape() == shape) {
                                        var rPoint = Object.clone(docker.referencePoint);
                                        // Move reference point per percent

                                        var rPointNew = {
                                            x: rPoint.x*newShape.bounds.width()/shape.bounds.width(),
                                            y: rPoint.y*newShape.bounds.height()/shape.bounds.height()
                                        };

                                        docker.setDockedShape(newShape);
                                        // Set reference point and center to new position
                                        docker.setReferencePoint(rPointNew);
                                        if(i instanceof ORYX.Core.Edge) {
                                            docker.bounds.centerMoveTo(rPointNew);
                                        } else {
                                            var absXY = shape.absoluteXY();
                                            docker.bounds.centerMoveTo({x:rPointNew.x+absXY.x, y:rPointNew.y+absXY.y});
                                            //docker.bounds.moveBy({x:rPointNew.x-rPoint.x, y:rPointNew.y-rPoint.y});
                                        }
                                    }
                                });
                            });

                        // for attached events
                        if(shape.dockers.length>0&&shape.dockers.first().getDockedShape()) {
                            newShape.dockers.first().setDockedShape(shape.dockers.first().getDockedShape());
                            newShape.dockers.first().setReferencePoint(Object.clone(shape.dockers.first().referencePoint));
                        }

                    } else { // is edge
                        newShape.dockers.first().setDockedShape(shape.dockers.first().getDockedShape());
                        newShape.dockers.first().setReferencePoint(shape.dockers.first().referencePoint);
                        newShape.dockers.last().setDockedShape(shape.dockers.last().getDockedShape());
                        newShape.dockers.last().setReferencePoint(shape.dockers.last().referencePoint);
                    }
                }
            });

            var stencil = undefined;
            var stencilSets = $scope.editor.getStencilSets().values();

            var stencilId = $scope.transact.id;
            if ($scope.transact.genericTaskId) {
                stencilId = $$scope.transact.genericTaskId;
            }

            for (var i = 0; i < stencilSets.length; i++)
            {
                var stencilSet = stencilSets[i];
                var nodes = stencilSet.nodes();
                for (var j = 0; j < nodes.length; j++)
                {
                    if (nodes[j].idWithoutNs() === stencilId)
                    {
                        stencil = nodes[j];
                        break;
                    }
                }
            }

            if (!stencil) return;

            // Create and execute command (for undo/redo)
            var command = new MorphTo($scope.currentSelectedShape, stencil, $scope.editor);
            $scope.editor.executeCommands([command]);

            $scope.selectTransactStatus = false;

            $scope.close();
        }
    };
}];
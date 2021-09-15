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
'use strict';

var KISBPM = KISBPM || {};
KISBPM.TOOLBAR = {
    ACTIONS: {

        saveModel: function (services) {
            var modal = services.$modal({
                backdrop: 'static',
                keyboard: true,
                template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/save-model.html?version=' + Date.now(),
                scope: services.$scope
            });
        },
        undo: function (services) {

            // Get the last commands
            var lastCommands = services.$scope.undoStack.pop();

            if (lastCommands) {
                // Add the commands to the redo stack
                services.$scope.redoStack.push(lastCommands);

                // Force refresh of selection, might be that the undo command
                // impacts properties in the selected item
                if (services.$rootScope && services.$rootScope.forceSelectionRefresh) 
                {
                	services.$rootScope.forceSelectionRefresh = true;
                }
                
                // Rollback every command
                for (var i = lastCommands.length - 1; i >= 0; --i) {
                    lastCommands[i].rollback();
                }
                
                // Update and refresh the canvas
                services.$scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_UNDO_ROLLBACK,
                    commands: lastCommands
                });
                
                // Update
                services.$scope.editor.getCanvas().update();
                services.$scope.editor.updateSelection();
            }
            
            var toggleUndo = false;
            if (services.$scope.undoStack.length == 0)
            {
            	toggleUndo = true;
            }
            
            var toggleRedo = false;
            if (services.$scope.redoStack.length > 0)
            {
            	toggleRedo = true;
            }

            if (toggleUndo || toggleRedo) {
                for (var i = 0; i < services.$scope.items.length; i++) {
                    var item = services.$scope.items[i];
                    if (toggleUndo && item.action === 'KISBPM.TOOLBAR.ACTIONS.undo') {
                        services.$scope.safeApply(function () {
                            item.enabled = false;
                        });
                    }
                    else if (toggleRedo && item.action === 'KISBPM.TOOLBAR.ACTIONS.redo') {
                        services.$scope.safeApply(function () {
                            item.enabled = true;
                        });
                    }
                }
            }
        },

        redo: function (services) {

            // Get the last commands from the redo stack
            var lastCommands = services.$scope.redoStack.pop();

            if (lastCommands) {
                // Add this commands to the undo stack
                services.$scope.undoStack.push(lastCommands);
                
                // Force refresh of selection, might be that the redo command
                // impacts properties in the selected item
                if (services.$rootScope && services.$rootScope.forceSelectionRefresh) 
                {
                	services.$rootScope.forceSelectionRefresh = true;
                }

                // Execute those commands
                lastCommands.each(function (command) {
                    command.execute();
                });

                // Update and refresh the canvas
                services.$scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_UNDO_EXECUTE,
                    commands: lastCommands
                });

                // Update
                services.$scope.editor.getCanvas().update();
                services.$scope.editor.updateSelection();
            }

            var toggleUndo = false;
            if (services.$scope.undoStack.length > 0) {
                toggleUndo = true;
            }

            var toggleRedo = false;
            if (services.$scope.redoStack.length == 0) {
                toggleRedo = true;
            }

            if (toggleUndo || toggleRedo) {
                for (var i = 0; i < services.$scope.items.length; i++) {
                    var item = services.$scope.items[i];
                    if (toggleUndo && item.action === 'KISBPM.TOOLBAR.ACTIONS.undo') {
                        services.$scope.safeApply(function () {
                            item.enabled = true;
                        });
                    }
                    else if (toggleRedo && item.action === 'KISBPM.TOOLBAR.ACTIONS.redo') {
                        services.$scope.safeApply(function () {
                            item.enabled = false;
                        });
                    }
                }
            }
        },

        cut: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope ,services.$rootScope).editCut();
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.paste') {
                    services.$scope.safeApply(function () {
                        item.enabled = true;
                    });
                }
            }
        },

        saveBtnShow : function(services){
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.saveModel') {
                    services.$scope.safeApply(function () {
                        item.enabled = true;
                    });
                }
            }
        },

        saveBtnHide : function(services){
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.saveModel') {
                    services.$scope.safeApply(function () {
                        item.enabled = false;
                    });
                }
            }
        },

        copy: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope ,services.$rootScope).editCopy();
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.paste') {
                    services.$scope.safeApply(function () {
                        item.enabled = true;
                    });
                }
            }
        },

        paste: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope ,services.$rootScope).editPaste();
        },

        deleteItem: function (services) {
            services.$rootScope.buttonSaveState = 'new';
            //console.log(services.$scope.facade.getSelection());//
            //console.log(services.$rootScope.currentSelectedShape);
            //console.log('132123123');
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope ,services.$rootScope).editDelete();
        },

        addBendPoint: function (services) {

            var dockerPlugin = KISBPM.TOOLBAR.ACTIONS._getOryxDockerPlugin(services.$scope);

            var enableAdd = !dockerPlugin.enabledAdd();
            dockerPlugin.setEnableAdd(enableAdd);
            if (enableAdd)
            {
            	dockerPlugin.setEnableRemove(false);
            	document.body.style.cursor = 'pointer';
            }
            else
            {
            	document.body.style.cursor = 'default';
            }
        },

        removeBendPoint: function (services) {

            var dockerPlugin = KISBPM.TOOLBAR.ACTIONS._getOryxDockerPlugin(services.$scope);

            var enableRemove = !dockerPlugin.enabledRemove();
            dockerPlugin.setEnableRemove(enableRemove);
            if (enableRemove)
            {
            	dockerPlugin.setEnableAdd(false);
            	document.body.style.cursor = 'pointer';
            }
            else
            {
            	document.body.style.cursor = 'default';
            }
        },

        /**
         * Helper method: fetches the Oryx Edit plugin from the provided scope,
         * if not on the scope, it is created and put on the scope for further use.
         *
         * It's important to reuse the same EditPlugin while the same scope is active,
         * as the clipboard is stored for the whole lifetime of the scope.
         */
        _getOryxEditPlugin: function ($scope ,$rootScope) {
            if ($scope.oryxEditPlugin === undefined || $scope.oryxEditPlugin === null || $rootScope.oryxEditPlugin === null) {
                $scope.oryxEditPlugin = new ORYX.Plugins.Edit($scope.editor);
            }
            return $scope.oryxEditPlugin;
        },

        zoomIn: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoom([1.0 + ORYX.CONFIG.ZOOM_OFFSET]);
        },

        zoomOut: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoom([1.0 - ORYX.CONFIG.ZOOM_OFFSET]);
        },
        
        zoomActual: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).setAFixZoomLevel(1);
        },
        
        zoomFit: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoomFitToModel();
        },
        
        alignVertical: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_MIDDLE]);
        },
        
        alignHorizontal: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_CENTER]);
        },
        
        sameSize: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_SIZE]);
        },
        
        closeEditor: function(services) {
        	window.location.href = "./../";
        },

        addDef: function(services){
            services.$modal({
                backdrop: 'static',
                keyboard: true,
                template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/save-def.html?version=' + services.$scope.staticIncludeVersion,
            });
        },

        deleteDef: function(services){
            var oryxtree = services.$rootScope.zTreeObj;
            var ztreeobj = oryxtree.getZtreeObj();

            var nodes = ztreeobj.getSelectedNodes();

            if(!nodes.length || nodes[0].type !== 'def'){
                layer.msg('请选中一个流程', {icon: 5});
                return false;
            }
            services.$modal({
                backdrop: 'static',
                keyboard: true,
                template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/delete-model.html?version=' + services.$scope.staticIncludeVersion
            });
        },
        /**
         * Helper method: fetches the Oryx View plugin from the provided scope,
         * if not on the scope, it is created and put on the scope for further use.
         */
        _getOryxViewPlugin: function ($scope) {
            if ($scope.oryxViewPlugin === undefined || $scope.oryxViewPlugin === null) {
                $scope.oryxViewPlugin = new ORYX.Plugins.View($scope.editor);
            }
            return $scope.oryxViewPlugin;
        },
        
        _getOryxArrangmentPlugin: function ($scope) {
            if ($scope.oryxArrangmentPlugin === undefined || $scope.oryxArrangmentPlugin === null) {
                $scope.oryxArrangmentPlugin = new ORYX.Plugins.Arrangement($scope.editor);
            }
            return $scope.oryxArrangmentPlugin;
        },

        _getOryxDockerPlugin: function ($scope) {
            if ($scope.oryxDockerPlugin === undefined || $scope.oryxDockerPlugin === null) {
                $scope.oryxDockerPlugin = new ORYX.Plugins.AddDocker($scope.editor);
            }
            return $scope.oryxDockerPlugin;
        }
    }
};

var DeleteModelCtrl = [ '$rootScope', '$scope', '$modal', function ($rootScope, $scope ,$modal) {

    $scope.title = '删除';

    var oryxtree = $rootScope.zTreeObj;

    var ztreeobj = oryxtree.getZtreeObj();

    var nodes = ztreeobj.getSelectedNodes();

    $scope.close = function () {
        $scope.$hide();
    };

    $scope.delete = function(){
        var node = nodes[0];
        new Ajax.Request(KISBPM.URL.deleteDef(node.dataId) , {
            asynchronous: false,
            method: 'get',
            onSuccess: function(transport){
                var data = JSON.parse(transport.responseText);
                if(data.code == 1){
                	layer.msg("删除成功", {icon: 6});
                	
                    oryxtree.updateData();
                    $scope.loading(true);
                    KISBPM.TOOLBAR.ACTIONS.saveBtnHide({'$scope' : $scope});
                    $scope.close();
                }else{
                    layer.msg(data.message, {icon: 5});
                }
            }.bind(this),
            onFailure: (function(transport){
                ORYX.Log.error("删除流程发生错误：" + transport);
            }).bind(this)
        });
    }
}];

/** Custom controller for the save dialog */
var SaveModelCtrl = [ '$rootScope', '$scope', '$http', '$route', '$location', function ($rootScope, $scope, $http, $route, $location) {

    $scope.status = {
        loading: false
    };

    switch ($rootScope.buttonSaveState){
        case 'save' :
            $scope.title = '新建';
            $scope.status.save = true;
            $scope.status.update = false;
            $scope.status.modify = false;
            break;
        case  'new' :
            $scope.title = '发布新版本';
            $scope.status.save = false;
            $scope.status.new = true;
            $scope.status.modify = false;
            break;
        case 'modify' :
            $scope.title = '修改';
            $scope.status.save = false;
            $scope.status.new = false;
            $scope.status.modify = true;
            break;
    }

    $scope.close = function () {
    	$scope.$hide();
    };

    $scope.save = function (successCallback) {
        // Indicator spinner image
        $scope.status.loading = true;

        var json = $scope.editor.getJSON();

        var valid = KISBPM.validNode(json);

        if(valid.length){
            layer.msg(valid[0].message, {icon: 5});
            $scope.status.loading = false;
            return false;
        }

        json = JSON.stringify(json);

        var selection = $scope.editor.getSelection();

        $scope.editor.setSelection([]);
        
        // Get the serialized svg image source
        var svgClone = $scope.editor.getCanvas().getSVGRepresentation(true);
        $scope.editor.setSelection(selection);
        if ($scope.editor.getCanvas().properties["oryx-showstripableelements"] === false) {
            var stripOutArray = jQuery(svgClone).find(".stripable-element");
            for (var i = stripOutArray.length - 1; i >= 0; i--) {
            	stripOutArray[i].remove();
            }
        }

        // Remove all forced stripable elements
        var stripOutArray = jQuery(svgClone).find(".stripable-element-force");
        for (var i = stripOutArray.length - 1; i >= 0; i--) {
            stripOutArray[i].remove();
        }

        // Parse dom to string
        //var svgDOM = DataManager.serialize(svgClone);

        var params = {
            jsonStr: json,
            //svg_xml: svgDOM,
            //typeId: $scope.zTreeTypeNode.dataId
        };
        if($scope.status.new || $scope.status.modify){
            params.definitionId = $rootScope.updateModalId;
        }
        // Update
        $http({
            method: 'POST',
            data: params,
            ignoreErrors: true,
            headers: {'Accept': 'application/json',
                      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            transformRequest: function (obj) {
                var str = [];
                for (var p in obj) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            },
            url: ($scope.status.modify ? KISBPM.URL.updateModel() : KISBPM.URL.putModel())
        }).success(function (data, status, headers, config) {
                $scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_SAVED
                });

                $scope.status.loading = false;
                if(data.code == 1){
                	layer.msg("保存成功", {icon: 6});
                	
                    var mydata = data.definition;

                    var nodes = {
                        id      : 'def_' + mydata.id,
                        name    : mydata.name,
                        type    : 'def',
                        dataId  : mydata.definitionId
                    }

                    $scope.zTreeObj.updateData();

                    $rootScope.buttonSaveState = 'modify';

                    // Fire event to all who is listening
                    var saveEvent = {
                        type: KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED,
                        model: params,
                        modelId: mydata.id,
                        eventType: 'update-model'
                    };
                    KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED, saveEvent);
                }else{
                    layer.msg(data.message, {icon: 5});
                }

                $scope.close();
                // Reset state
                $scope.status.loading = false;

                // Execute any callback
                if (successCallback) {
                    successCallback();
                }

            })
            .error(function (data, status, headers, config) {
                console.log('Something went wrong when updating the process model:' + JSON.stringify(data));
                $scope.status.loading = false;
            });
    };

}];

var SaveModelDefController = ['$scope' , '$rootScope','$timeout' , function($scope , $rootScope ,$timeout){
    function getData(data){
        if(data.length) return data;
        return [];
    }
    $scope.status = {loading : false};
    $scope.formvaild= '';
    $scope.orgTree  = getData($scope.deployInfoData.orgTree);
    $scope.types    = getData($scope.deployInfoData.types);
    $scope.forms    = getData($scope.deployInfoData.forms);

    $scope.newDef = function(){
        $scope.status.loading = true;
        if($scope.defId && $scope.defName && $scope.form && $scope.type && $scope.org){
            if(!/^[a-zA-Z]{1}([a-zA-Z0-9]){1,29}$/.test($scope.defId)){
                $scope.formvaild = '流程ID只能由小写字母、大写字母和数字组成,第一位必须是字母,且长度不能超过30';
                $scope.status.loading = false;
                return false;
            }
            $scope.formvaild = '';
            var propData = {
                "modelId" : 1035,
                "model": {
                    "stencil": {
                       "id": "BPMNDiagram"
                    },
                    "stencilset" : {
                        "url": "stencilsets/bpmn2.0/bpmn2.0.json",
                            "namespace": "http://b3mn.org/stencilset/bpmn2.0#"
                    },
                    "properties" : {
                        "process_id"    : $scope.defId,
                        "process_name"  : $scope.defName,
                        "process_org"   : {
                            "id"  : $scope.org.dataId,
                            "name": $scope.org.name
                        },
                        "process_type"  : {
                            "id"  : $scope.type.id,
                            "name": $scope.type.name
                        },
                        "namespace" : "http://www.activiti.org/processdef",
                        "form_apply"    : {
                            "id"  : $scope.form.id,
                            "name": $scope.form.name
                        }
                    }
                }
            }

            $timeout(function(){
                $rootScope.initCanvas(propData);
                $scope.close();
                $scope.safeApply(function(){
                    $scope.loading(false);
                    $rootScope.buttonSaveState = 'save';//标识为新建流程
                    $rootScope.oryxEditPlugin = null;   //重置面板操作参数  解决删除失效问题
                    KISBPM.TOOLBAR.ACTIONS.saveBtnShow({'$scope': $scope});
                });
            },0);
        }else{
            $scope.formvaild        = '流程信息填写错误';
            $scope.status.loading   = false;
        }
    }

    $scope.close = function(){
        $scope.$hide();
    }
}];
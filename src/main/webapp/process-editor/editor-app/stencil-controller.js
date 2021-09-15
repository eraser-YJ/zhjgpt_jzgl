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

angular.module('activitiModeler')
    .controller('StencilController', ['$rootScope', '$scope', '$http', '$modal', '$timeout', function ($rootScope, $scope, $http, $modal, $timeout) {

        $rootScope.zTreeObj = null;

        $rootScope.vaildDefErrorInfo = '';

        $rootScope.oryxEditPlugin = null;

        // Property window toggle state
        $scope.propertyWindowState = {'collapsed': false};

        // Add reference to global header-config
        $scope.headerConfig = KISBPM.HEADER_CONFIG;

        $scope.propertyWindowState.toggle = function () {
            $scope.propertyWindowState.collapsed = !$scope.propertyWindowState.collapsed;
            $timeout(function () {
                jQuery(window).trigger('resize');
            });
        };

        $rootScope.loading = function(falg){
            $scope.safeApply(function(){
                $scope.waitingView = falg;
            });
        }

        /**
         * 初始化左侧树
         */
        $rootScope.zTreeObj = new ORYX.Plugins.Ztree({
            element     : jQuery('#ztreeSection'),
            nodeCheck   : null,
            callback    : {
                onClick: function(event, treeId, treeNode) {
                    $rootScope.clickSelectNode = treeNode;
                    $rootScope.buttonSaveState = 'modify';//标识为修改流程
                    $rootScope.oryxEditPlugin = null;     //重置面板操作参数  解决删除失效问题
                    if(treeNode.type === 'def'){
                        //console.log('你点击了ID为 '+ treeNode.dataId + ' 的流程');
                        $scope.fetchModel(treeNode.dataId);
                        $scope.loading(false);
                        //重置保存按钮不可点击状态
                        KISBPM.TOOLBAR.ACTIONS.saveBtnShow({'$scope': $scope});
                    }
                }
            }
        });
        //初始化导入组件

        $timeout(function(){
            $rootScope.uploader = new JCMUploader({
                el: '#uploader',
                style: 'jcupload',
                label: '导入',
                accept: {
                    title: 'Files',
                    extensions: 'json',
                    mimeTypes: 'application/json'
                },
                server: 'importJson.action',
                success: function () {
                	layer.msg('导入成功', {icon: 6});
                	
                	$rootScope.zTreeObj.updateData();
                }
            });
        },500);
        
        $timeout(function(){
            $rootScope.multiuploader = new JCMUploader({
                el: '#multiuploader',
                style: 'jcuploadlg',
                label: '批量导入',
                accept: {
                    title: 'Files',
                    extensions: 'zip',
                    mimeTypes: 'application/zip'
                },
                server: 'multiImportJson.action',
                success: function () {
                	layer.msg('批量导入成功', {icon: 6});
                	
                	$rootScope.zTreeObj.updateData();
                }
            });
        },500);

        $scope.export = function(){
            if($rootScope.clickSelectNode && $rootScope.clickSelectNode.type === 'def'){
                var definitionId = $rootScope.clickSelectNode.definitionId;
                var form=jQuery("<form>");//定义一个form表单
                form.attr("style","display:none");
                form.attr("target","");
                form.attr("method","post");
                form.attr("action","exportJson.action");
                var input1=jQuery("<input>");
                input1.attr("type","hidden");
                input1.attr("name","definitionId");
                input1.attr("value",definitionId);
                jQuery("body").append(form);//将表单放置在web中
                form.append(input1);
                form.submit();
                form.remove();
            }else{
                layer.msg('请选中一个流程', {icon: 5});
            }
        }
        
        $scope.multiexport = function(){
        	$modal({
                backdrop: 'static',
                keyboard: true,
                template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/multiexport.html?version=' + $scope.staticIncludeVersion
            });
        }

        /**
         * 左侧机构流程树  添加流程事件
         */
        $scope.addDef = function(){
            var services = {'$scope' : $scope,'$modal' : $modal};
            KISBPM.TOOLBAR.ACTIONS.addDef(services);
        }

        // Code that is dependent on an initialised Editor is wrapped in a promise for the editor
        $rootScope.actionEditor = function(){
            /* Build stencil item list */

            // Build simple json representation of stencil set
            var stencilItemGroups = [];

            // Helper method: find a group in an array
            var findGroup = function (name, groupArray) {
                for (var index = 0; index < groupArray.length; index++) {
                    if (groupArray[index].name === name) {
                        return groupArray[index];
                    }
                }
                return null;
            };

            // Helper method: add a new group to an array of groups
            var addGroup = function (groupName, groupArray) {
                var group = { name: groupName, items: [], paletteItems: [], groups: [], visible: true };
                groupArray.push(group);
                return group;
            };

            /*
             StencilSet items
             */
            $http({method: 'GET', url: KISBPM.URL.getStencilSet()}).success(function (data, status, headers, config) {

                var quickMenuDefinition = ['SequenceFlow' ,'UserTask', 'EndNoneEvent',
                    //'TextAnnotation', 'Association','ExclusiveGateway',
                    'CatchTimerEvent', 'ThrowNoneEvent'];

                var ignoreForPaletteDefinition = ['SequenceFlow', 'MessageFlow', 'Association', 'DataAssociation', 'DataStore', 'SendTask'];
                var quickMenuItems = [],
                    quickMenuItemArray = [];


                var morphRoles = [];
                for (var i = 0; i < data.rules.morphingRules.length; i++)
                {
                    var role = data.rules.morphingRules[i].role;
                    var roleItem = {'role': role, 'morphOptions': []};
                    morphRoles.push(roleItem);
                }

                // Check all received items
                for (var stencilIndex = 0; stencilIndex < data.stencils.length; stencilIndex++)
                {
                    // Check if the root group is the 'diagram' group. If so, this item should not be shown.
                    var currentGroupName = data.stencils[stencilIndex].groups[0];
                    if (currentGroupName === 'Diagram' || currentGroupName === 'Form') {
                        continue;  // go to next item
                    }

                    var removed = false;
                    if (data.stencils[stencilIndex].removed) {
                        removed = true;
                    }

                    var currentGroup = undefined;
                    if (!removed) {
                        // Check if this group already exists. If not, we create a new one

                        if (currentGroupName !== null && currentGroupName !== undefined && currentGroupName.length > 0) {

                            currentGroup = findGroup(currentGroupName, stencilItemGroups); // Find group in root groups array
                            if (currentGroup === null) {
                                currentGroup = addGroup(currentGroupName, stencilItemGroups);
                            }

                            // Add all child groups (if any)
                            for (var groupIndex = 1; groupIndex < data.stencils[stencilIndex].groups.length; groupIndex++) {
                                var childGroupName = data.stencils[stencilIndex].groups[groupIndex];
                                var childGroup = findGroup(childGroupName, currentGroup.groups);
                                if (childGroup === null) {
                                    childGroup = addGroup(childGroupName, currentGroup.groups);
                                }

                                // The current group variable holds the parent of the next group (if any),
                                // and is basically the last element in the array of groups defined in the stencil item
                                currentGroup = childGroup;

                            }

                        }
                    }

                    // Construct the stencil item
                    var stencilItem = {'id': data.stencils[stencilIndex].id,
                        'name': data.stencils[stencilIndex].title,
                        'description': data.stencils[stencilIndex].description,
                        'icon': data.stencils[stencilIndex].icon,
                        'type': data.stencils[stencilIndex].type,
                        'roles': data.stencils[stencilIndex].roles,
                        'removed': removed,
                        'customIcon': false,
                        'canConnect': false,
                        'visible'   : true,
                        'canConnectTo': false,
                        'canConnectAssociation': false};

                    if(data.stencils[stencilIndex].hidden){
                        stencilItem.visible = false;
                    }

                    if (data.stencils[stencilIndex].customIconId && data.stencils[stencilIndex].customIconId > 0) {
                        stencilItem.customIcon = true;
                        stencilItem.icon = data.stencils[stencilIndex].customIconId;
                    }

                    if (!removed) {
                        if (quickMenuDefinition.indexOf(stencilItem.id) >= 0) {
                            quickMenuItems[quickMenuDefinition.indexOf(stencilItem.id)] = stencilItem;
                        }
                    }

                    if (stencilItem.id === 'TextAnnotation' || stencilItem.id === 'BoundaryCompensationEvent') {
                        stencilItem.canConnectAssociation = true;
                    }



                    for (var i = 0; i < data.stencils[stencilIndex].roles.length; i++) {
                        var stencilRole = data.stencils[stencilIndex].roles[i];
                        if (stencilRole === 'sequence_start') {
                            stencilItem.canConnect = true;
                        } else if (stencilRole === 'sequence_end') {
                            stencilItem.canConnectTo = true;
                        }

                        for (var j = 0; j < morphRoles.length; j++) {
                            if (stencilRole === morphRoles[j].role) {
                                if (!removed) {
                                    morphRoles[j].morphOptions.push(stencilItem);
                                }
                                stencilItem.morphRole = morphRoles[j].role;
                                break;
                            }
                        }
                    }

                    if (currentGroup) {
                        // Add the stencil item to the correct group
                        currentGroup.items.push(stencilItem);
                        if (ignoreForPaletteDefinition.indexOf(stencilItem.id) < 0) {
                            currentGroup.paletteItems.push(stencilItem);
                            if(currentGroup.items && currentGroup.visible){
                                quickMenuItemArray.push(stencilItem);
                            }
                        }

                    } else {
                        // It's a root stencil element
                        if (!removed) {
                            stencilItemGroups.push(stencilItem);
                        }
                    }
                }

                for (var i = 0; i < stencilItemGroups.length; i++)
                {
                    if (stencilItemGroups[i].paletteItems && stencilItemGroups[i].paletteItems.length == 0)
                    {
                        stencilItemGroups[i].visible = false;
                    }
                }

                $scope.stencilItemGroups = stencilItemGroups;
                $scope.quickMenuItemArray = quickMenuItemArray;

                var containmentRules = [];
                for (var i = 0; i < data.rules.containmentRules.length; i++)
                {
                    var rule = data.rules.containmentRules[i];
                    containmentRules.push(rule);
                }
                $scope.containmentRules = containmentRules;

                // remove quick menu items which are not available anymore due to custom pallette
                var availableQuickMenuItems = [];
                for (var i = 0; i < quickMenuItems.length; i++)
                {
                    if (quickMenuItems[i] && quickMenuItems[i].id != 'ExclusiveGateway') {
                        availableQuickMenuItems[availableQuickMenuItems.length] = quickMenuItems[i];
                    }
                }

                $scope.quickMenuItems = availableQuickMenuItems;
                $scope.morphRoles = morphRoles;
            }).

            error(function (data, status, headers, config) {
                console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
            });

            var showSettingsShapeDialog = function(){
                $modal({
                    backdrop: 'static',
                    keyboard: true,
                    template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/shape-settings.html?version=' + $scope.staticIncludeVersion
                });
            }

            $scope.processUpdate = function(){
                $scope.editor.setSelection([]);//取消设计器中节点选中状态
                var shapes = [$scope.editor.getCanvas()];
                $rootScope.getShapeProperties(shapes);
                showSettingsShapeDialog();
            }

            $rootScope.getShapeProperties = function(shapes , isCanvas){

                if(shapes && shapes.length > 0){

                    var selectedShape = shapes.first();

                    var stencil = selectedShape.getStencil();

                    if ($rootScope.selectedElementBeforeScrolling && stencil.id().indexOf('BPMNDiagram') !== -1){
                        // ignore canvas event because of empty selection when scrolling stops
                        return;
                    }

                    if ($rootScope.selectedElementBeforeScrolling && $rootScope.selectedElementBeforeScrolling.getId() === selectedShape.getId()){
                        $rootScope.selectedElementBeforeScrolling = null;
                        return;
                    }

                    $scope.previousSelectedShape = $scope.selectedShape;

                    // Only do something if another element is selected (Oryx fires this event multiple times)
                    if ($scope.selectedShape !== undefined && $scope.selectedShape.getId() === selectedShape.getId()) {
                        if ($rootScope.forceSelectionRefresh) {
                            // Switch the flag again, this run will force refresh
                            $rootScope.forceSelectionRefresh = false;
                        } else {
                            // Selected the same element again, no need to update anything
                            return;
                        }
                    }

                    var selectedItem = {'title': '', 'properties': []};

                    if(isCanvas){
                        selectedItem.auditData = {
                            'author': $scope.modelData.createdByUser,
                            'createDate': $scope.modelData.createDate
                        };
                    }

                    var properties = stencil.properties();

                    for (var i = 0; i < properties.length; i++) {
                        var property = properties[i];
                        if (property.popular() == false) continue;

                        var key = property.prefix() + "-" + property.id();

                        if (key === 'oryx-name' || key == 'oryx-process_name') {
                            selectedItem.title = selectedShape.properties[key];
                            selectedItem.type = stencil.idWithoutNs();
                        }
                        // First we check if there is a config for 'key-type' and then for 'type' alone
                        var propertyConfig = KISBPM.PROPERTY_CONFIG[key + '-' + property.type()];

                        if (propertyConfig === undefined || propertyConfig === null) {
                            propertyConfig = KISBPM.PROPERTY_CONFIG[property.type()];
                        }

                        if (propertyConfig === undefined || propertyConfig === null) {
                            console.log('WARNING: no property configuration defined for ' + key + ' of type ' + property.type());
                        } else {

                            if (selectedShape.properties[key] === 'true') {
                                selectedShape.properties[key] = true;
                            }

                            if (KISBPM.CONFIG.showRemovedProperties == false && property.isHidden()) {
                                continue;
                            }
                            var currentProperty = {
                                'key': key,
                                'title': property.title(),
                                'type': property.type(),
                                'mode': 'read',
                                'group': property.group(),
                                'groupName': property.groupName(),
                                'hidden': property.isHidden(),
                                'hiddenRequired': property.hiddenRequired(),
                                'value': selectedShape.properties[key]
                            };

                            if ((currentProperty.type === 'complex' || currentProperty.type === 'multiplecomplex') && currentProperty.value && currentProperty.value.length > 0) {
                                try {
                                    currentProperty.value = JSON.parse(currentProperty.value);
                                } catch (err) {
                                    // ignore
                                }
                            }
                            //根据节点自身携带的propertyConfig属性判断需要angular显示哪些页面
                            if (propertyConfig.readModeTemplateUrl !== undefined && propertyConfig.readModeTemplateUrl !== null) {
                                currentProperty.readModeTemplateUrl = propertyConfig.readModeTemplateUrl + '?version=' + $scope.staticIncludeVersion;
                            }
                            if (propertyConfig.writeModeTemplateUrl !== undefined && propertyConfig.writeModeTemplateUrl !== null) {
                                currentProperty.writeModeTemplateUrl = propertyConfig.writeModeTemplateUrl + '?version=' + $scope.staticIncludeVersion;
                            }

                            if (propertyConfig.templateUrl !== undefined && propertyConfig.templateUrl !== null) {
                                currentProperty.templateUrl = propertyConfig.templateUrl + '?version=' + $scope.staticIncludeVersion;
                                currentProperty.hasReadWriteMode = false;
                            }
                            else {
                                currentProperty.hasReadWriteMode = true;
                            }

                            if (currentProperty.value === undefined
                                || currentProperty.value === null
                                || currentProperty.value.length == 0) {
                                currentProperty.noValue = true;
                            }

                            selectedItem.properties.push(currentProperty);
                        }
                    }

                    // Need to wrap this in an $apply block, see http://jimhoskins.com/2012/12/17/angularjs-and-apply.html
                    $scope.safeApply(function () {
                        $scope.selectedItem = selectedItem;
                        $rootScope.selectedItem = selectedItem;
                        $scope.selectedShape = selectedShape;
                        $rootScope.selectedShape = selectedShape;
                    });
                }else{
                    // Need to wrap this in an $apply block, see http://jimhoskins.com/2012/12/17/angularjs-and-apply.html
                    $scope.safeApply(function () {
                        $scope.selectedItem = {};
                        $rootScope.selectedItem = {};
                        $scope.selectedShape = null;
                        $rootScope.selectedShape = null;
                    });
                }
            }
            /**
             * 获取当前页面选中节点的类型,
             * 如,  任务节点,开始节点,路由节点,结束节点
             */
            $rootScope.getSelectedNodeType = function(){
                var shape = $rootScope.currentSelectedShape;
                return shape.getStencil().idWithoutNs();
            }
            /**
             * 判断页面是是否存在某种节点
             * @param  nodeType  String  节点类型
             * @sample
             *      判断页面是否存在结束节点
             *      $scope.nodeTypeIsAppear('EndNoneEvent');
             */
            $rootScope.nodeTypeIsAppear = function(nodeType){
                var shapes = $scope.editor.getCanvas().getChildShapes(),
                    reslut = false;
                $A(shapes).each(function(shape){
                    if(shape.getStencil().id().indexOf(nodeType) !== -1){
                        reslut = true;
                    }
                });
                return reslut;
            }
            /**
             * 动态生成节点id
             */
            $scope.getOverride = function(type){
                var types = type.split(',');

                var shapes = $scope.editor.getCanvas().getChildShapes(),
                    result = {};

                $A(types).each(function(ty){
                    $A(shapes).each(function(shape){
                        var idtype = shape.getStencil().idWithoutNs(),
                            overid = shape.properties['oryx-overrideid'];
                        if(idtype === ty){
                            var userid = Number(overid.toLowerCase().split(ty.toLowerCase())[1]);
                            if(!result[ty]) result[ty] = 1;
                            result[ty] = result[ty] < userid ? userid : result[ty];
                        }
                        if(shape.getChildShapes().length){
                            $A(shape.getChildShapes()).each(function(sha){
                                var chilendtype = sha.getStencil().idWithoutNs(),
                                    chilenover = sha.properties['oryx-overrideid'];
                                if(chilendtype === ty){
                                    var chilenoveruserid = Number(chilenover.toLowerCase().split(ty.toLowerCase())[1]);
                                    if(!result[ty]) result[ty] = 1;
                                    result[ty] = result[ty] < chilenoveruserid ? chilenoveruserid : result[ty];
                                }
                            });
                        }
                    });
                });

                result.field = {
                    version : $rootScope.staticIncludeVersion,
                    have : $rootScope.fieldData,
                    todo : angular.copy($rootScope.fieldData)
                }
                return result;
            };
            /*
             * 点击节点下方显示属性事件
             * Listen to selection change events: show properties
             */

            $scope.editor.registerOnEvent(ORYX.CONFIG.EVENT_SELECTION_CHANGED, function (event) {
                var shapes = event.elements;
                if (shapes && shapes.length == 0) {
                    return false;
                }
                $rootScope.getShapeProperties(shapes);
            });


            //$scope.editor.unregisterOnEvent
            $scope.editor.registerOnEvent(ORYX.CONFIG.EVENT_EXECUTE_UNDO, function( event , shape){
                //这个是想实现回滚路由功能的
                //KISBPM.TOOLBAR.ACTIONS.undo({ '$scope' : $scope, '$rootScope' : $rootScope});
            });

            //点击显示隐藏节点中各种功能组件
            $scope.editor.registerOnEvent(ORYX.CONFIG.EVENT_SELECTION_CHANGED, function (event) {
                KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_HIDE_SHAPE_BUTTONS);

                var shapes = event.elements;

                if (shapes && shapes.length == 1) {

                    var selectedShape = shapes.first();

                    var a = $scope.editor.getCanvas().node.getScreenCTM();

                    var absoluteXY = selectedShape.absoluteXY();

                    absoluteXY.x *= a.a;
                    absoluteXY.y *= a.d;

                    var additionalIEZoom = 1;
                    if (!isNaN(screen.logicalXDPI) && !isNaN(screen.systemXDPI)) {
                        var ua = navigator.userAgent;
                        if (ua.indexOf('MSIE') >= 0) {
                            //IE 10 and below
                            var zoom = Math.round((screen.deviceXDPI / screen.logicalXDPI) * 100);
                            if (zoom !== 100) {
                                additionalIEZoom = zoom / 100
                            }
                        }
                    }

                    if (additionalIEZoom === 1) {
                        absoluteXY.y = absoluteXY.y - jQuery("#canvasSection").offset().top + 5;
                        absoluteXY.x = absoluteXY.x - jQuery("#canvasSection").offset().left;

                    } else {
                        var canvasOffsetLeft = jQuery("#canvasSection").offset().left;
                        var canvasScrollLeft = jQuery("#canvasSection").scrollLeft();
                        var canvasScrollTop = jQuery("#canvasSection").scrollTop();

                        var offset = a.e - (canvasOffsetLeft * additionalIEZoom);
                        var additionaloffset = 0;
                        if (offset > 10) {
                            additionaloffset = (offset / additionalIEZoom) - offset;
                        }
                        absoluteXY.y = absoluteXY.y - (jQuery("#canvasSection").offset().top * additionalIEZoom) + 5 + ((canvasScrollTop * additionalIEZoom) - canvasScrollTop);
                        absoluteXY.x = absoluteXY.x - (canvasOffsetLeft * additionalIEZoom) + additionaloffset + ((canvasScrollLeft * additionalIEZoom) - canvasScrollLeft);
                    }

                    var bounds = new ORYX.Core.Bounds(a.e + absoluteXY.x, a.f + absoluteXY.y, a.e + absoluteXY.x + a.a*selectedShape.bounds.width(), a.f + absoluteXY.y + a.d*selectedShape.bounds.height());
                    var shapeXY = bounds.upperLeft();

                    var stencilItem = $scope.getStencilItemById(selectedShape.getStencil().idWithoutNs());
                    var morphShapes = [];
                    if (stencilItem && stencilItem.morphRole)
                    {
                        for (var i = 0; i < $scope.morphRoles.length; i++)
                        {
                            if ($scope.morphRoles[i].role === stencilItem.morphRole)
                            {
                                morphShapes = $scope.morphRoles[i].morphOptions;
                            }
                        }
                    }

                    var x = shapeXY.x;
                    if (bounds.width() < 48) {
                        x -= 24;
                    }
                    //注释掉的代码是为了 路由节点上可以显示编辑按钮
                    //if (morphShapes && morphShapes.length > 0) {
                    // In case the element is not wide enough, start the 2 bottom-buttons more to the left
                    // to prevent overflow in the right-menu
                    var morphButton = document.getElementById('morph-button');
                    morphButton.style.display = "block";
                    morphButton.style.left = x + 24 +'px';
                    morphButton.style.top = (shapeXY.y+bounds.height() + 2) + 'px';
                    //}

                    var deleteButton = document.getElementById('delete-button');
                    deleteButton.style.display = "block";
                    deleteButton.style.left = x + 'px';
                    deleteButton.style.top = (shapeXY.y+bounds.height() + 2) + 'px';

                    if (stencilItem && (stencilItem.canConnect || stencilItem.canConnectAssociation)) {
                        var quickButtonCounter = 0;
                        var quickButtonX = shapeXY.x+bounds.width() + 5;
                        var quickButtonY = shapeXY.y;
                        jQuery('.Oryx_button').each(function(i, obj) {
                            if (obj.id !== 'morph-button' && obj.id != 'delete-button') {
                                quickButtonCounter++;
                                if (quickButtonCounter > 3) {
                                    quickButtonX = shapeXY.x+bounds.width() + 5;
                                    quickButtonY += 24;
                                    quickButtonCounter = 1;
                                }
                                else if (quickButtonCounter > 1) {
                                    //修改为quickButtonY +24 是因为更改成节点上只显示3个工具,且竖着显示
                                    quickButtonY += 24;
                                    // quickButtonX +24是为了如果有多个工具组件时 每3个一行第4个换行
                                    //quickButtonX += 24;
                                }
                                obj.style.display = "block";
                                obj.style.left = quickButtonX + 'px';
                                obj.style.top = quickButtonY + 'px';
                            }
                        });
                    }
                }
            });

            if (!$rootScope.stencilInitialized) {
                KISBPM.eventBus.addListener(KISBPM.eventBus.EVENT_TYPE_HIDE_SHAPE_BUTTONS, function (event) {
                    jQuery('.Oryx_button').each(function(i, obj) {
                        obj.style.display = "none";
                    });
                });

                /*
                 * Listen to property updates and act upon them
                 */
                KISBPM.eventBus.addListener(KISBPM.eventBus.EVENT_TYPE_PROPERTY_VALUE_CHANGED, function (event) {
                    if (event.property && event.property.key) {
                        // If the name property is been updated, we also need to change the title of the currently selected item
                        if (event.property.key === 'oryx-name' && $scope.selectedItem !== undefined && $scope.selectedItem !== null) {
                            $scope.selectedItem.title = event.newValue;
                        }

                        // Update "no value" flag
                        event.property.noValue = (event.property.value === undefined
                        || event.property.value === null
                        || event.property.value.length == 0);
                    }
                });

                $rootScope.stencilInitialized = true;
            }

            $scope.exposeMorph = function(){
                var shapes = $rootScope.editor.getSelection();
                if (shapes && shapes.length == 1) {
                    $rootScope.currentSelectedShape = shapes.first();
                    var stencilItem = $scope.getStencilItemById($rootScope.currentSelectedShape.getStencil().idWithoutNs());

                    var morphShapes = [];
                    for (var i = 0; i < $scope.morphRoles.length; i++)
                    {
                        if ($scope.morphRoles[i].role === stencilItem.morphRole)
                        {
                            morphShapes = $scope.morphRoles[i].morphOptions.slice();
                        }
                    }
                    $rootScope.morphShapes = morphShapes;
                    showSettingsShapeDialog();
                }
            }

            $scope.morphShape = function() {
                $scope.exposeMorph();
            };

            $scope.deleteShape = function() {
                KISBPM.TOOLBAR.ACTIONS.deleteItem({'$scope': $scope ,'$rootScope' : $rootScope});
            };

            $scope.quickAddItem = function(newItemId) {
                //添加生成的节点id
                $scope.safeApply(function () {
                    var shapes = $rootScope.editor.getSelection();
                    if (shapes && shapes.length == 1)
                    {
                        $rootScope.currentSelectedShape = shapes.first();

                        var containedStencil = undefined;
                        var stencilSets = $scope.editor.getStencilSets().values();
                        for (var i = 0; i < stencilSets.length; i++)
                        {
                            var stencilSet = stencilSets[i];
                            var nodes = stencilSet.nodes();
                            for (var j = 0; j < nodes.length; j++)
                            {
                                if (nodes[j].idWithoutNs() === newItemId)
                                {
                                    containedStencil = nodes[j];
                                    break;
                                }
                            }
                        }

                        if (!containedStencil) return;

                        var option = {type: $scope.currentSelectedShape.getStencil().namespace() + newItemId, namespace: $scope.currentSelectedShape.getStencil().namespace()};

                        option['connectedShape'] = $rootScope.currentSelectedShape;
                        option['parent'] = $rootScope.currentSelectedShape.parent;
                        option['containedStencil'] = containedStencil;
                        option['overrideid'] = $scope.getOverride(option.type.split('#')[1]+',SequenceFlow');

                        var args = { sourceShape: $rootScope.currentSelectedShape, targetStencil: containedStencil };
                        var targetStencil = $scope.editor.getRules().connectMorph(args);
                        if (!targetStencil){ return; }// Check if there can be a target shape
                        option['connectingType'] = targetStencil.id();

                        var command = new KISBPM.CreateCommand(option, undefined, undefined, $scope.editor);
                        $scope.setSaveStatus(command);
                        $scope.editor.executeCommands([command]);
                    }
                });
            };
        }

        $scope.editorFactory.promise.then(function () {
            $rootScope.actionEditor();
            $scope.editor.registerOnEvent(ORYX.CONFIG.EVENT_DBLCLICK , function(event,shape){
                if(shape.properties){
                    $rootScope.getShapeProperties([shape]);
                    $scope.exposeMorph();
                }
            });
        });

        /**
         * Helper method that searches a group for an item with the given id.
         * If not found, will return undefined.
         */
        $scope.findStencilItemInGroup = function (stencilItemId, group) {

            var item;

            // Check all items directly in this group
            for (var j = 0; j < group.items.length; j++) {
                item = group.items[j];
                if (item.id === stencilItemId) {
                    return item;
                }
            }

            // Check the child groups
            if (group.groups && group.groups.length > 0) {
                for (var k = 0; k < group.groups.length; k++) {
                    item = $scope.findStencilItemInGroup(stencilItemId, group.groups[k]);
                    if (item) {
                        return item;
                    }
                }
            }

            return undefined;
        };
        /**
         * 设置状态 ,判断当前流程处于 新建还是修改或发布新版本
         */
        $scope.setSaveStatus = function(command){
            //command.option.type = http://b3mn.org/stencilset/bpmn2.0#UserTask
            if($rootScope.buttonSaveState !== 'save'){
                $rootScope.buttonSaveState = 'new';
            }
        }
        /**
         * Helper method to find a stencil item.
         */
        $scope.getStencilItemById = function (stencilItemId) {
            for (var i = 0; i < $scope.stencilItemGroups.length; i++) {
                var element = $scope.stencilItemGroups[i];

                // Real group
                if (element.items !== null && element.items !== undefined) {
                    var item = $scope.findStencilItemInGroup(stencilItemId, element);
                    if (item) {
                        return item;
                    }
                } else { // Root stencil item
                    if (element.id === stencilItemId) {
                        return element;
                    }
                }
            }
            return undefined;
        };
        /*
         * DRAG AND DROP FUNCTIONALITY
         */

        $scope.dropCallback = function (event, ui) {
            //判断设计器是否canvas面板是否已初始化
            if(!$rootScope.buttonSaveState){
               return false;
            }
            //判断设计器中是否含有开始节点,如果有则不润许添加第二个
            //if($scope.nodeTypeIsAppear('StartNoneEvent')){
            //    if(ui.draggable[0].id === 'StartNoneEvent'){
            //        alert('已存在一个开始节点,无法创建第二个...');
            //        return false;
            //    }
            //}
            //添加生成的节点id
            $scope.editor.handleEvents({
                type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                highlightId: "shapeRepo.attached"
            });
            $scope.editor.handleEvents({
                type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                highlightId: "shapeRepo.added"
            });
            
            $scope.editor.handleEvents({
                type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                highlightId: "shapeMenu"
            });
            
            KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_HIDE_SHAPE_BUTTONS);

            if ($scope.dragCanContain) {

            	var item = $scope.getStencilItemById(ui.draggable[0].id);
            	
            	var pos = {x: event.pageX, y: event.pageY};
            	
            	var additionalIEZoom = 1;
                if (!isNaN(screen.logicalXDPI) && !isNaN(screen.systemXDPI)) {
                    var ua = navigator.userAgent;
                    if (ua.indexOf('MSIE') >= 0) {
                        //IE 10 and below
                        var zoom = Math.round((screen.deviceXDPI / screen.logicalXDPI) * 100);
                        if (zoom !== 100) {
                            additionalIEZoom = zoom / 100;
                        }
                    }
                }
            	
                var screenCTM = $scope.editor.getCanvas().node.getScreenCTM();

                // Correcting the UpperLeft-Offset
                pos.x -= (screenCTM.e / additionalIEZoom);
                pos.y -= (screenCTM.f / additionalIEZoom);
                // Correcting the Zoom-Factor
                pos.x /= screenCTM.a;
                pos.y /= screenCTM.d;
                
                // Correcting the ScrollOffset
                pos.x -= document.documentElement.scrollLeft;
                pos.y -= document.documentElement.scrollTop;
                
                var parentAbs = $scope.dragCurrentParent.absoluteXY();
                pos.x -= parentAbs.x;
                pos.y -= parentAbs.y;
                
            	if ($scope.quickMenu)
            	{
            		var shapes = $scope.editor.getSelection();
            		if (shapes && shapes.length == 1)
            		{
            			var currentSelectedShape = shapes.first();

	            		var containedStencil = undefined;
	                	var stencilSets = $scope.editor.getStencilSets().values();
	                	for (var i = 0; i < stencilSets.length; i++)
	                	{
	                		var stencilSet = stencilSets[i];
	            			var nodes = stencilSet.nodes();
	            			for (var j = 0; j < nodes.length; j++)
	                    	{
	            				if (nodes[j].idWithoutNs() === ui.draggable[0].id)
	            				{
	            					containedStencil = nodes[j];
	            					break;
	            				}
	                    	}
	            			
	            			if (!containedStencil)
	            			{
	            				var edges = stencilSet.edges();
	                			for (var j = 0; j < edges.length; j++)
	                        	{
	                				if (edges[j].idWithoutNs() === ui.draggable[0].id)
	                				{
	                					containedStencil = edges[j];
	                					break;
	                				}
	                        	}
	            			}
	                	}
	                	
	                	if (!containedStencil) return;
	        			
	        			var option = {};
	        			option.type = currentSelectedShape.getStencil().namespace() + ui.draggable[0].id;
	        			option.namespace = currentSelectedShape.getStencil().namespace();
	        			option.connectedShape = currentSelectedShape;
	        			option.parent = $scope.dragCurrentParent;
	        			option.containedStencil = containedStencil;
	        			
	        			// If the ctrl key is not pressed, 
	        			// snapp the new shape to the center 
	        			// if it is near to the center of the other shape
	        			if (!event.ctrlKey){
	        				// Get the center of the shape
	        				var cShape = currentSelectedShape.bounds.center();
	        				// Snapp +-20 Pixel horizontal to the center 
	        				if (20 > Math.abs(cShape.x - pos.x)){
	        					pos.x = cShape.x;
	        				}
	        				// Snapp +-20 Pixel vertical to the center 
	        				if (20 > Math.abs(cShape.y - pos.y)){
	        					pos.y = cShape.y;
	        				}
	        			}
	        			
	        			option.position = pos;

                        option.overrideid = $scope.getOverride(option.type.split('#')[1]+',SequenceFlow');

	        			if (containedStencil.idWithoutNs() !== 'SequenceFlow' && containedStencil.idWithoutNs() !== 'Association' && 
	        			        containedStencil.idWithoutNs() !== 'MessageFlow' && containedStencil.idWithoutNs() !== 'DataAssociation')
	        			{
		        			var args = { sourceShape: currentSelectedShape, targetStencil: containedStencil };
		        			var targetStencil = $scope.editor.getRules().connectMorph(args);
		        			if (!targetStencil){ return; }// Check if there can be a target shape
		        			option.connectingType = targetStencil.id();
	        			}

	        			var command = new KISBPM.CreateCommand(option, $scope.dropTargetElement, pos, $scope.editor);
                        $scope.setSaveStatus(command);
	        			$scope.editor.executeCommands([command]);
            		}
            	}
            	else
            	{

	                var option = {};

	                option['type'] = $scope.modelData.model.stencilset.namespace + item.id;
					option['namespace'] = $scope.modelData.model.stencilset.namespace;
					option['position'] = pos;
					option['parent'] = $scope.dragCurrentParent;
                    option['overrideid'] = $scope.getOverride(option.type.split('#')[1]);
	
	                var commandClass = ORYX.Core.Command.extend({
	                    construct: function (option, currentParent, canAttach, position, facade) {
	                        this.option = option;
	                        this.currentParent = currentParent;
	                        this.canAttach = canAttach;
	                        this.position = position;
	                        this.facade = facade;
	                        this.selection = this.facade.getSelection();
	                        this.shape;
	                        this.parent;
	                    },
	                    execute: function () {
	                        if (!this.shape) {
	                            this.shape = this.facade.createShape(option);
	                            this.parent = this.shape.parent;
	                        } else {
	                            this.parent.add(this.shape);
	                        }
	
	                        if (this.canAttach && this.currentParent instanceof ORYX.Core.Node && this.shape.dockers.length > 0) {
	
	                            var docker = this.shape.dockers[0];
	
	                            if (this.currentParent.parent instanceof ORYX.Core.Node) {
	                                this.currentParent.parent.add(docker.parent);
	                            }
	
	                            docker.bounds.centerMoveTo(this.position);
	                            docker.setDockedShape(this.currentParent);
	                            //docker.update();
	                        }
	
	                        this.facade.setSelection([this.shape]);
	                        this.facade.getCanvas().update();
	                        this.facade.updateSelection();
	
	                    },
	                    rollback: function () {
	                        this.facade.deleteShape(this.shape);
	                        //this.currentParent.update();
	                        this.facade.setSelection(this.selection.without(this.shape));
	                        this.facade.getCanvas().update();
	                        this.facade.updateSelection();
	
	                    }
	                });
	
	                // Update canvas
	                var command = new commandClass(option, $scope.dragCurrentParent, false, pos, $scope.editor);
                    $scope.setSaveStatus(command);
	                $scope.editor.executeCommands([command]);
	
	                // Fire event to all who want to know about this
	                var dropEvent = {
	                    type: KISBPM.eventBus.EVENT_TYPE_ITEM_DROPPED,
	                    droppedItem: item,
	                    position: pos
	                };
	                KISBPM.eventBus.dispatch(dropEvent.type, dropEvent);
            	}
            }

            $scope.dragCurrentParent = undefined;
            $scope.dragCurrentParentId = undefined;
            $scope.dragCurrentParentStencil = undefined;
            $scope.dragCanContain = undefined;
            $scope.quickMenu = undefined;
            $scope.dropTargetElement = undefined;
        };


        $scope.overCallback = function (event, ui) {
            $scope.dragModeOver = true;
        };

        $scope.outCallback = function (event, ui) {
            $scope.dragModeOver = false;
        };

        $scope.startDragCallback = function (event, ui) {
            $scope.dragModeOver = false;
            $scope.quickMenu = false;
        };
        
        $scope.startDragCallbackQuickMenu = function (event, ui) {
            $scope.dragModeOver = false;
            $scope.quickMenu = true;
        };
        
        $scope.dragCallback = function (event, ui) {

            if ($scope.dragModeOver != false) {
                var coord = $scope.editor.eventCoordinatesXY(event.pageX, event.pageY);
                
                var additionalIEZoom = 1;
                if (!isNaN(screen.logicalXDPI) && !isNaN(screen.systemXDPI)) {
                    var ua = navigator.userAgent;
                    if (ua.indexOf('MSIE') >= 0) {
                        //IE 10 and below
                        var zoom = Math.round((screen.deviceXDPI / screen.logicalXDPI) * 100);
                        if (zoom !== 100) {
                            additionalIEZoom = zoom / 100
                        }
                    }
                }
                
                if (additionalIEZoom !== 1) {
                     coord.x = coord.x / additionalIEZoom;
                     coord.y = coord.y / additionalIEZoom;
                }
                
                var aShapes = $scope.editor.getCanvas().getAbstractShapesAtPosition(coord);
                
                if (aShapes.length <= 0) {
                    if (event.helper) {
                        $scope.dragCanContain = false;
                        return false;
                    }
                }

                if (aShapes[0] instanceof ORYX.Core.Canvas) {
                    $scope.editor.getCanvas().setHightlightStateBasedOnX(coord.x);
                }

                if (aShapes.length == 1 && aShapes[0] instanceof ORYX.Core.Canvas)
                {
                    var parentCandidate = aShapes[0];

                    $scope.dragCanContain = true;
                    $scope.dragCurrentParent = parentCandidate;
                    $scope.dragCurrentParentId = parentCandidate.id;

                    $scope.editor.handleEvents({
                        type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                        highlightId: "shapeRepo.attached"
                    });
                    $scope.editor.handleEvents({
                        type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                        highlightId: "shapeRepo.added"
                    });
                    return false;
                }
                else 
                {
                    var item = $scope.getStencilItemById(event.target.id);
                    
                    var parentCandidate = aShapes.reverse().find(function (candidate) {
                        return (candidate instanceof ORYX.Core.Canvas
                            || candidate instanceof ORYX.Core.Node
                            || candidate instanceof ORYX.Core.Edge);
                    });
                    
                    if (!parentCandidate) {
                        $scope.dragCanContain = false;
                        return false;
                    }
                    
                    if (item.type === "node") {
                        
                        // check if the draggable is a boundary event and the parent an Activity
                        var _canContain = false;
                        var parentStencilId = parentCandidate.getStencil().id();

                        if ($scope.dragCurrentParentId && $scope.dragCurrentParentId === parentCandidate.id) {
                            return false;
                        }

                        var parentItem = $scope.getStencilItemById(parentCandidate.getStencil().idWithoutNs());
                        if (parentItem.roles.indexOf("Activity") > -1) {
                            if (item.roles.indexOf("IntermediateEventOnActivityBoundary") > -1) {
                                _canContain = true;
                            }
                        }
                        else if (parentCandidate.getStencil().idWithoutNs() === 'Pool')
                        {
                        	if (item.id === 'Lane')
                        	{
                        		_canContain = true;
                        	}
                        }
                        
                        if (_canContain)
                        {
                        	$scope.editor.handleEvents({
                                type: ORYX.CONFIG.EVENT_HIGHLIGHT_SHOW,
                                highlightId: "shapeRepo.attached",
                                elements: [parentCandidate],
                                style: ORYX.CONFIG.SELECTION_HIGHLIGHT_STYLE_RECTANGLE,
                                color: ORYX.CONFIG.SELECTION_VALID_COLOR
                            });

                            $scope.editor.handleEvents({
                                type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                                highlightId: "shapeRepo.added"
                            });
                        }
                        else
                        {
                            for (var i = 0; i < $scope.containmentRules.length; i++) {
                                var rule = $scope.containmentRules[i];
                                if (rule.role === parentItem.id) {
                                    for (var j = 0; j < rule.contains.length; j++) {
                                        if (item.roles.indexOf(rule.contains[j]) > -1) {
                                            _canContain = true;
                                            break;
                                        }
                                    }

                                    if (_canContain) {
                                        break;
                                    }
                                }
                            }

                            // Show Highlight
                            $scope.editor.handleEvents({
                                type: ORYX.CONFIG.EVENT_HIGHLIGHT_SHOW,
                                highlightId: 'shapeRepo.added',
                                elements: [parentCandidate],
                                color: _canContain ? ORYX.CONFIG.SELECTION_VALID_COLOR : ORYX.CONFIG.SELECTION_INVALID_COLOR
                            });

                            $scope.editor.handleEvents({
                                type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                                highlightId: "shapeRepo.attached"
                            });
                        }

                        $scope.dragCurrentParent = parentCandidate;
                        $scope.dragCurrentParentId = parentCandidate.id;
                        $scope.dragCurrentParentStencil = parentStencilId;
                        $scope.dragCanContain = _canContain;
                        
                    } else  {
                    	var canvasCandidate = $scope.editor.getCanvas();
                    	var canConnect = false;
                    	
                    	var targetStencil = $scope.getStencilItemById(parentCandidate.getStencil().idWithoutNs());
            			if (targetStencil) {
            				var associationConnect = false;
            				if (stencil.idWithoutNs() === 'Association' && (curCan.getStencil().idWithoutNs() === 'TextAnnotation' || curCan.getStencil().idWithoutNs() === 'BoundaryCompensationEvent')) {
            				    associationConnect = true;
            				} else if (stencil.idWithoutNs() === 'DataAssociation' && curCan.getStencil().idWithoutNs() === 'DataStore') {
                                associationConnect = true;
                            }
            				
            				if (targetStencil.canConnectTo || associationConnect) {
            					canConnect = true;
            				}
            			}
                    	
                    	//Edge
                    	$scope.dragCurrentParent = canvasCandidate;
                    	$scope.dragCurrentParentId = canvasCandidate.id;
                        $scope.dragCurrentParentStencil = canvasCandidate.getStencil().id();
                        $scope.dragCanContain = canConnect;
                        
                    	// Show Highlight
                        $scope.editor.handleEvents({
                            type: ORYX.CONFIG.EVENT_HIGHLIGHT_SHOW,
                            highlightId: 'shapeRepo.added',
                            elements: [canvasCandidate],
                            color: ORYX.CONFIG.SELECTION_VALID_COLOR
                        });

                        $scope.editor.handleEvents({
                            type: ORYX.CONFIG.EVENT_HIGHLIGHT_HIDE,
                            highlightId: "shapeRepo.attached"
                        });
        			}
                }
            }
        };
        
        $scope.dragCallbackQuickMenu = function (event, ui) {

            if ($scope.dragModeOver != false) {
                var coord = $scope.editor.eventCoordinatesXY(event.pageX, event.pageY);
                
                var additionalIEZoom = 1;

                if (!isNaN(screen.logicalXDPI) && !isNaN(screen.systemXDPI)) {
                    var ua = navigator.userAgent;
                    if (ua.indexOf('MSIE') >= 0) {
                        //IE 10 and below
                        var zoom = Math.round((screen.deviceXDPI / screen.logicalXDPI) * 100);
                        if (zoom !== 100) {
                            additionalIEZoom = zoom / 100
                        }
                    }
                }
                
                if (additionalIEZoom !== 1) {
                     coord.x = coord.x / additionalIEZoom;
                     coord.y = coord.y / additionalIEZoom;
                }
                
                var aShapes = $scope.editor.getCanvas().getAbstractShapesAtPosition(coord);
               
                if (aShapes.length <= 0) {
                    if (event.helper) {
                        $scope.dragCanContain = false;
                        return false;
                    }
                }

                if (aShapes[0] instanceof ORYX.Core.Canvas) {
                    $scope.editor.getCanvas().setHightlightStateBasedOnX(coord.x);
                }
                
        		var stencil = undefined;
            	var stencilSets = $scope.editor.getStencilSets().values();
            	for (var i = 0; i < stencilSets.length; i++)
            	{
            		var stencilSet = stencilSets[i];
        			var nodes = stencilSet.nodes();
        			for (var j = 0; j < nodes.length; j++)
                	{
        				if (nodes[j].idWithoutNs() === event.target.id)
        				{
        					stencil = nodes[j];
        					break;
        				}
                	}
        			
        			if (!stencil)
        			{
        				var edges = stencilSet.edges();
            			for (var j = 0; j < edges.length; j++)
                    	{
            				if (edges[j].idWithoutNs() === event.target.id)
            				{
            					stencil = edges[j];
            					break;
            				}
                    	}
        			}
            	}
        		
                var candidate = aShapes.last();
                
                var isValid = false;

                if (stencil.type() === "node")
                {
    				//check containment rules
    				var canContain = $scope.editor.getRules().canContain({containingShape:candidate, containedStencil:stencil});
    				
    				var parentCandidate = aShapes.reverse().find(function (candidate) {
                        return (candidate instanceof ORYX.Core.Canvas
                            || candidate instanceof ORYX.Core.Node
                            || candidate instanceof ORYX.Core.Edge);
                    });

                    if (!parentCandidate) {
                        $scope.dragCanContain = false;
                        return false;
                    }
    				
    				$scope.dragCurrentParent = parentCandidate;
                    $scope.dragCurrentParentId = parentCandidate.id;
                    $scope.dragCurrentParentStencil = parentCandidate.getStencil().id();
                    $scope.dragCanContain = canContain;
                    $scope.dropTargetElement = parentCandidate;
                    isValid = canContain;
    	
    			} else { //Edge
    			
    				var shapes = $scope.editor.getSelection();
            		if (shapes && shapes.length == 1)
            		{
            			var currentSelectedShape = shapes.first();
            			var curCan = candidate;
            			var canConnect = false;
            			
            			var targetStencil = $scope.getStencilItemById(curCan.getStencil().idWithoutNs());
            			if (targetStencil)
            			{
            				var associationConnect = false;
            				if (stencil.idWithoutNs() === 'Association' && (curCan.getStencil().idWithoutNs() === 'TextAnnotation' || curCan.getStencil().idWithoutNs() === 'BoundaryCompensationEvent'))  
            				{
            					associationConnect = true;
            				}
            				else if (stencil.idWithoutNs() === 'DataAssociation' && curCan.getStencil().idWithoutNs() === 'DataStore')
            				{
            				    associationConnect = true;
            				}
            				
            				if (targetStencil.canConnectTo || associationConnect)
	            			{
		        				while (!canConnect && curCan && !(curCan instanceof ORYX.Core.Canvas))
		        				{
		        					candidate = curCan;
		        					//check connection rules
		        					canConnect = $scope.editor.getRules().canConnect({
		        											sourceShape: currentSelectedShape, 
		        											edgeStencil: stencil, 
		        											targetShape: curCan
		        											});	
		        					curCan = curCan.parent;
		        				}
	            			}
            			}
            			var parentCandidate = $scope.editor.getCanvas();
        				isValid = canConnect;
        				$scope.dragCurrentParent = parentCandidate;
                        $scope.dragCurrentParentId = parentCandidate.id;
                        $scope.dragCurrentParentStencil = parentCandidate.getStencil().id();
        				$scope.dragCanContain = canConnect;
        				$scope.dropTargetElement = candidate;
            		}		
    				
    			}
                $scope.editor.handleEvents({
					type:		ORYX.CONFIG.EVENT_HIGHLIGHT_SHOW, 
					highlightId:'shapeMenu',
					elements:	[candidate],
					color:		isValid ? ORYX.CONFIG.SELECTION_VALID_COLOR : ORYX.CONFIG.SELECTION_INVALID_COLOR
				});
            }
        };

    }]);

var KISBPM = KISBPM || {};
//create command for undo/redo
KISBPM.CreateCommand = ORYX.Core.Command.extend({
	construct: function(option, currentReference, position, facade){
		this.option = option;
		this.currentReference = currentReference;
		this.position = position;
		this.facade = facade;
		this.shape;
		this.edge;
		this.targetRefPos;
		this.sourceRefPos;
		/*
		 * clone options parameters
		 */
        this.connectedShape = option.connectedShape;
        this.connectingType = option.connectingType;
        this.namespace = option.namespace;
        this.type = option.type;
        this.containedStencil = option.containedStencil;
        this.parent = option.parent;
        this.currentReference = currentReference;
        this.shapeOptions = option.shapeOptions;
	},			
	execute: function(){
		if (this.shape) {
			if (this.shape instanceof ORYX.Core.Node) {

				this.parent.add(this.shape);
				if (this.edge) {
					this.facade.getCanvas().add(this.edge);
					this.edge.dockers.first().setDockedShape(this.connectedShape);
					this.edge.dockers.first().setReferencePoint(this.sourceRefPos);
					this.edge.dockers.last().setDockedShape(this.shape);
					this.edge.dockers.last().setReferencePoint(this.targetRefPos);
				}
				
				this.facade.setSelection([this.shape]);
				
			} else if (this.shape instanceof ORYX.Core.Edge) {
				this.facade.getCanvas().add(this.shape);
				this.shape.dockers.first().setDockedShape(this.connectedShape);
				this.shape.dockers.first().setReferencePoint(this.sourceRefPos);
			}
			resume = true;
		}
		else {
			this.shape = this.facade.createShape(this.option);
			this.edge = (!(this.shape instanceof ORYX.Core.Edge)) ? this.shape.getIncomingShapes().first() : undefined;
		}
		
		if (this.currentReference && this.position) {
			
			if (this.shape instanceof ORYX.Core.Edge) {
			
				if (!(this.currentReference instanceof ORYX.Core.Canvas)) {
					this.shape.dockers.last().setDockedShape(this.currentReference);
					
					if (this.currentReference.getStencil().idWithoutNs() === 'TextAnnotation')
					{
						var midpoint = {};
						midpoint.x = 0;
						midpoint.y = this.currentReference.bounds.height() / 2;
						this.shape.dockers.last().setReferencePoint(midpoint);
					}
					else
					{
						this.shape.dockers.last().setReferencePoint(this.currentReference.bounds.midPoint());
					}
				}
				else {
					this.shape.dockers.last().bounds.centerMoveTo(this.position);
				}
				this.sourceRefPos = this.shape.dockers.first().referencePoint;
				this.targetRefPos = this.shape.dockers.last().referencePoint;
				
			} else if (this.edge){
				this.sourceRefPos = this.edge.dockers.first().referencePoint;
				this.targetRefPos = this.edge.dockers.last().referencePoint;
			}
		} else {
			var containedStencil = this.containedStencil;
			var connectedShape = this.connectedShape;
			var bc = connectedShape.bounds;
			var bs = this.shape.bounds;
			
			var pos = bc.center();
			if(containedStencil.defaultAlign()==="north") {
				pos.y -= (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET + (bs.height()/2);
			} else if(containedStencil.defaultAlign()==="northeast") {
				pos.x += (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.width()/2);
				pos.y -= (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.height()/2);
			} else if(containedStencil.defaultAlign()==="southeast") {
				pos.x += (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.width()/2);
				pos.y += (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.height()/2);
			} else if(containedStencil.defaultAlign()==="south") {
				pos.y += (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET + (bs.height()/2);
			} else if(containedStencil.defaultAlign()==="southwest") {
				pos.x -= (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.width()/2);
				pos.y += (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.height()/2);
			} else if(containedStencil.defaultAlign()==="west") {
				pos.x -= (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET + (bs.width()/2);
			} else if(containedStencil.defaultAlign()==="northwest") {
				pos.x -= (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.width()/2);
				pos.y -= (bc.height() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET_CORNER + (bs.height()/2);
			} else {
				pos.x += (bc.width() / 2) + ORYX.CONFIG.SHAPEMENU_CREATE_OFFSET + (bs.width()/2);
			}
			
			// Move shape to the new position
			this.shape.bounds.centerMoveTo(pos);
			
			// Move all dockers of a node to the position
			if (this.shape instanceof ORYX.Core.Node){
				(this.shape.dockers||[]).each(function(docker){
					docker.bounds.centerMoveTo(pos);
				});
			}
			
			//this.shape.update();
			this.position = pos;
			
			if (this.edge){
				this.sourceRefPos = this.edge.dockers.first().referencePoint;
				this.targetRefPos = this.edge.dockers.last().referencePoint;
			}
		}
		
		this.facade.getCanvas().update();
		this.facade.updateSelection();

	},
	rollback: function(){
		this.facade.deleteShape(this.shape);
		if(this.edge) {
			this.facade.deleteShape(this.edge);
		}
		//this.currentParent.update();
		this.facade.setSelection(this.facade.getSelection().without(this.shape, this.edge));
	}
});


var MultiExportCtrl = ['$scope', '$q', '$translate', '$timeout', '$http', '$rootScope',
    function ($scope, $q, $translate, $timeout, $http, $rootScope) {
		//初始化人员选择树
		$timeout(function () {
			new ORYX.Plugins.Ztree({
	            element     : jQuery('#ztreeSectionExport'),
	            nodeCheck   : true,
	            callback    : {
	            	onCheck: function(event, treeId, treeNode) {
	                	$scope.allCheckNode = this.getZTreeObj(treeId).getCheckedNodes(true);
	                }
	            }
	        });
		}, 100);
	    
	    $scope.export = function() {
            if ($scope.allCheckNode && $scope.allCheckNode.length > 0) {
            	var definitionIds = "";
            	
            	for (var i = 0; i < $scope.allCheckNode.length; i++) {
            		var item = $scope.allCheckNode[i];
            		
            		if (item.type === 'def') {
            			definitionIds += "," + item.definitionId;
                    }
				}
            	
            	if (definitionIds.length > 0) {
            		definitionIds = definitionIds.substring(1);
            		
            		var form=jQuery("<form>");//定义一个form表单
                    form.attr("style","display:none");
                    form.attr("target","");
                    form.attr("method","post");
                    form.attr("action","multiExportJson.action");
                    var input1=jQuery("<input>");
                    input1.attr("type","hidden");
                    input1.attr("name","definitionIds");
                    input1.attr("value",definitionIds);
                    jQuery("body").append(form);//将表单放置在web中
                    form.append(input1);
                    form.submit();
                    form.remove();
            	} else {
            		layer.msg('请选中一个流程', {icon: 5});
            	}
            } else {
                layer.msg('请选中一个流程', {icon: 5});
            }
        }
	    
	    $scope.cancel = function() {
	    	$scope.$hide();
	    };

	    $scope.close = function() {
	    	$scope.$hide();
	    };
	}
];
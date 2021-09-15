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
var KISBPM = KISBPM || {};

/** Inspired by https://github.com/krasimir/EventBus/blob/master/src/EventBus.js */
KISBPM.eventBus = {

    /** Event fired when the editor is loaded and ready */
    EVENT_TYPE_EDITOR_READY: 'event-type-editor-ready',

    /** Event fired when a selection is made on the canvas. */
    EVENT_TYPE_SELECTION_CHANGE: 'event-type-selection-change',

    /** Event fired when a toolbar button has been clicked. */
    EVENT_TYPE_TOOLBAR_BUTTON_CLICKED: 'event-type-toolbar-button-clicked',

    /** Event fired when a stencil item is dropped on the canvas. */
    EVENT_TYPE_ITEM_DROPPED: 'event-type-item-dropped',

    /** Event fired when a property value is changed. */
    EVENT_TYPE_PROPERTY_VALUE_CHANGED: 'event-type-property-value-changed',

    /** Event fired on double click in canvas. */
    EVENT_TYPE_DOUBLE_CLICK: 'event-type-double-click',

    /** Event fired on a mouse out */
    EVENT_TYPE_MOUSE_OUT: 'event-type-mouse-out',

    /** Event fired on a mouse over */
    EVENT_TYPE_MOUSE_OVER: 'event-type-mouse-over',

    /** Event fired when a model is saved. */
    EVENT_TYPE_MODEL_SAVED: 'event-type-model-saved',
    
    /** Event fired when the quick menu buttons should be hidden. */
    EVENT_TYPE_HIDE_SHAPE_BUTTONS: 'event-type-hide-shape-buttons',

    /** A mapping for storing the listeners*/
    listeners: {},

    /** The Oryx editor, which is stored locally to send events to */
    editor: null,

    /**
     * Add an event listener to the event bus, listening to the event with the provided type.
     * Type and callback are mandatory parameters.
     *
     * Provide scope parameter if it is important that the callback is executed
     * within a specific scope.
     */
    addListener: function (type, callback, scope) {

        // Add to the listeners map
        if (typeof this.listeners[type] !== "undefined") {
            this.listeners[type].push({scope: scope, callback: callback});
        } else {
            this.listeners[type] = [
                {scope: scope, callback: callback}
            ];
        }
    },

    /**
     * Removes the provided event listener.
     */
    removeListener: function (type, callback, scope) {
        if (typeof this.listeners[type] != "undefined") {
            var numOfCallbacks = this.listeners[type].length;
            var newArray = [];
            for (var i = 0; i < numOfCallbacks; i++) {
                var listener = this.listeners[type][i];
                if (listener.scope === scope && listener.callback === callback) {
                    // Do nothing, this is the listener and doesn't need to survive
                } else {
                    newArray.push(listener);
                }
            }
            this.listeners[type] = newArray;
        }
    },

    hasListener:function(type, callback, scope) {
        if(typeof this.listeners[type] != "undefined") {
            var numOfCallbacks = this.listeners[type].length;
            if(callback === undefined && scope === undefined){
                return numOfCallbacks > 0;
            }
            for(var i=0; i<numOfCallbacks; i++) {
                var listener = this.listeners[type][i];
                if(listener.scope == scope && listener.callback == callback) {
                    return true;
                }
            }
        }
        return false;
    },

    /**
     * Dispatch an event to all event listeners registered to that specific type.
     */
    dispatch:function(type, event) {
        if(typeof this.listeners[type] != "undefined") {
            var numOfCallbacks = this.listeners[type].length;
            for(var i=0; i<numOfCallbacks; i++) {
                var listener = this.listeners[type][i];
                if(listener && listener.callback) {
                    listener.callback.apply(listener.scope, [event]);
                }
            }
        }
    },

    dispatchOryxEvent: function(event, uiObject) {
        KISBPM.eventBus.editor.handleEvents(event, uiObject);
    }

};


KISBPM.validNode = function(canvas){
    if(canvas.stencil.id !== 'BPMNDiagram'){
        throw Error('初始化验证参数不是canvas对象');
    }
    var validPlugins = {
        errorList : [],
        sequences : [],
        endnode : [],
        subprocess : [],
        startnode : []
    };

    var form = canvas.properties;

    if(!form.process_id.replace(/(^s*)|(s*$)/g, "").length){
        validPlugins.errorList.push({message : '流程id不能为空'});
        return validPlugins.errorList;
    }
    if(!form.process_name.replace(/(^s*)|(s*$)/g, "").length){
        validPlugins.errorList.push({message : '流程name不能为空'});
        return validPlugins.errorList;
    }

    if(Object.prototype.toString.call(form.process_org) != '[object Object]'){
        validPlugins.errorList.push({message : '请在配置流程中选择流程机构'});
        return validPlugins.errorList;
    }
    if(Object.prototype.toString.call(form.process_type) != '[object Object]'){
        validPlugins.errorList.push({message : '请在配置流程中选择流程类型'});
        return validPlugins.errorList;
    }
    if(Object.prototype.toString.call(form.form_apply) != '[object Object]'){
        validPlugins.errorList.push({message : '请在配置流程中选择表单'});
        return validPlugins.errorList;
    }
    /**
     * 验证canvas面板上所有节点路由
     * 1.  排除结束节点后所有节点必须要有指向的路由
     * 2.  判断是否包含开始和结束节点
     * 3.  验证所有路由是否有被指
     */
    validPlugins.valid = function(shapes){
        var i = 0,len = shapes.length,nodes = [];
        for(; i < len;i++){
            var shape = shapes[i];
            //第一步先验证所有节点
            if(shape.stencil.id !== 'EndNoneEvent' && !shape.outgoing.length){
                validPlugins.errorList.push({message : shape.properties.name+' 未连接'});
            }else if(shape.stencil.id !== 'StartNoneEvent' && !shape.pointToMy.length){
                validPlugins.errorList.push({message : shape.properties.name+' 未连接'});
            }else if(shape.stencil.id === 'EndNoneEvent'){
                validPlugins.endnode.push(shape);
            }else if(shape.stencil.id === 'StartNoneEvent'){
                validPlugins.startnode.push(shape);
            }else if(shape.stencil.id === 'SubProcess'){
                validPlugins.subprocess.push(shape);
            }

            if(shape.stencil.id === 'SequenceFlow') {
                //添加旋转角度参数用于绘制流程历史箭头角度
                var shapeDom = jQuery(document.getElementById('svg-'+shape.resourceId));
                shape.line = shapeDom.find('path').attr('d');
                var text = shapeDom.find('text');
                shape.textPath = [text.attr('transform'),text.attr('x'),text.attr('y')]
                validPlugins.sequences[shape.resourceId] = shape;
            }else{
                nodes.push(shape);
            }
        }
        if(!validPlugins.endnode.length){
            validPlugins.errorList.push({message : '未包含结束节点'});
        }

        if(!validPlugins.startnode.length){
            validPlugins.errorList.push({message : '未包含开始节点'});
        }

        if(validPlugins.startnode.length > 1){
            validPlugins.errorList.push({message : '不允许包含多个开始节点'});
        }

        if(validPlugins.subprocess.length){
            validPlugins.validSubProcess(validPlugins.subprocess);
        }
    }
    /**
     * 验证子流程内部
     * 1. 是否包含开始结束节点
     * 2. 排除结束节点后所有节点必须要有指向的路由
     * 3. 验证子流程内路由指向
     */
    validPlugins.validSubProcess = function(pros){
        for(var i = 0; i < pros.length;i++){
            var pro = pros[i],nodeType = {};
            for(var k = 0; k < pro.childShapes.length;k++){
                var shape = pro.childShapes[k];
                if(shape.stencil.id !== 'EndNoneEvent' && !shape.outgoing.length){
                    validPlugins.errorList.push({message : shape.properties.name+'节点 未连接'});
                }
                if(!nodeType[shape.stencil.id]){
                    nodeType[shape.stencil.id] = [];
                }
                nodeType[shape.stencil.id].push(shape);
                for(var j = 0; j < shape.outgoing.length;j++){
                    validPlugins.sequenceScope(pro.childShapes,shape.outgoing[j]);
                }
            }
            if(!nodeType['StartNoneEvent'] || !nodeType['EndNoneEvent']){
                validPlugins.errorList.push({message : '子流程未包含开始或结束节点'});
            }

            if(nodeType['StartNoneEvent'] && nodeType['StartNoneEvent'].length > 1){
                validPlugins.errorList.push({message : '子流程不能包含多个开始节点'});
            }
        }
    }
    /**
     * 验证子流程内路由指向的作用域不得超出子流程的范围
     */
    validPlugins.sequenceScope = function(subs ,seque){
        var sequence = validPlugins.sequences[seque.resourceId],
            result = false;
        if(sequence.outgoing.length){
            for(var i = 0;i < subs.length;i++){
                var subnode = subs[i];
                if(subnode.resourceId == sequence.outgoing[0].resourceId){
                    result = true;
                }
            }
        }
        if(!result){
            validPlugins.errorList.push({message : sequence.properties.name +'不应该连接到子流程以外的节点'});
        }
    }

    validPlugins.valid(canvas.childShapes);
    return validPlugins.errorList;
}

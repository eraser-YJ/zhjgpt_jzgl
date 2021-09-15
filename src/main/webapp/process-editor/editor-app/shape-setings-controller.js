angular.module('activitiModeler').factory('$person', ['$http',function($http) {
    return {
        /**
         * @param type　String 选择人员模式
         * 可选四种参数：all , role , dept , org
         *   all  表示可以按照机构，部门，角色　选择人员
         *  role  表示按照角色选择人员
         *  dept　表示按照部门选择人员
         *  org   表示按照机构选择人员
         * @param　url  Object 数据源URL
         *  all ， role ， dept ，org
         * @param element   jQuery      初始化树容器
         * @param nodeCheck Object      定义节点样式
         * @param callback  Function    回调函数
         */
        defaults: {
            type    : 'all',
            url     : KISBPM.URL.getAssignZtree(),
            element : null,
            nodeCheck   : null,
            callback    : {
                onClick: function(event, treeId, treeNode) {
                    console.log(treeNode);
                }
            }
        },
        //初始化
        init: function(options){
            var opt = jQuery.extend({}, this.defaults, options) ;
            this.ztreeObj = new ORYX.Plugins.Ztree({
                element : jQuery('#' +opt.element),
                url     : opt.url,
                callback: opt.callback
            });
            //this.ztreeObj.myTree 可获取当前ztree初始化对象
        },
        getZtreeObj : function (){
           return this.ztreeObj;
        }
    };
}]);

/*
 * 配置节点Controller
 * Created by gezhigang on 2016/5/16.
 * Controller for morph shape settings
 */
var KisBpmShapeSettingsCtrl = [ '$rootScope', '$scope', '$timeout', '$translate', '$person','$timeout',
    function($rootScope, $scope, $timeout, $translate, $person, $timeout) {

    //$scope.selectedShape.getCanvas().getId()             //canvas   ID
    //$scope.selectedShape.getCanvas().resourceId          //当前工作流ID
    if($scope.selectedItem && $scope.selectedShape){
        var listItem = [];

        $A($scope.selectedItem.properties).each(function(obj){
            if(obj.group !== null && obj.group !== undefined && obj.group !== ''){
                if(listItem[obj.group] instanceof Array){
                    listItem[obj.group].push(obj);
                }else{
                    listItem[obj.group] = [];
                    listItem[obj.group].push(obj);
                }
            }
        });
        var nodeInfoString = '';
        switch($scope.selectedItem.type){
            case 'BPMNDiagram':
                nodeInfoString = '流程';
                break;
            case 'StartNoneEvent':
                nodeInfoString = '开始';
                break;
            case 'EndNoneEvent':
                nodeInfoString = '结束';
                break;
            case 'SequenceFlow':
                nodeInfoString = '路由';
                break;
            default :
                nodeInfoString = '节点';
                break;
        }

        $scope.title = nodeInfoString + '设置(' +$scope.selectedItem.title+')';

        $scope.listItems = (function(){
            var list = [];
            for(var key in listItem){
                if(listItem.hasOwnProperty(key)){
                    list.push({
                        type : key,
                        name : listItem[key][0].groupName
                    });
                }
            }
            return list;
        })();

        $scope.switchType = function(type){
            $scope.shapeFormUrl = '';
            $timeout(function(){
                $scope.safeApply(function(){
                    $scope.shapeFormUrl = KISBPM.PROPERTY_CONFIG.FORM_URL[type] +'?version='+ $scope.staticIncludeVersion;
                    $scope.selectedItem = listItem[type];
                });
            }, 100);
        }

        $scope.selectedItem = (listItem.base ?　listItem.base : []);

        $scope.shapeFormUrl = KISBPM.PROPERTY_CONFIG.FORM_URL.base + '?version='+ $scope.staticIncludeVersion;

        /* Helper method to retrieve the template url for a property */
        $scope.getPropertyNodeTemplateUrl = function (index) {
            return $scope.selectedItem[index].templateUrl;
        };

        $scope.getPropertyNodeReadModeTemplateUrl = function (index) {
            return $scope.selectedItem[index].readModeTemplateUrl;
        };

        $scope.getPropertyNodeWriteModeTemplateUrl = function (index) {
            return $scope.selectedItem[index].writeModeTemplateUrl;
        };

        //点击属性事件
        $scope.propertyNodeClicked = function (index) {
            if (!$scope.selectedItem[index].hidden) {
                $scope.selectedItem[index].mode = "write";
            }
        };
        /**
         * 修改属性方法
         * @parms  property
         * @parms  shapeId
         * */
        $scope.updatePropertyInModel = function (property, shapeId) {
            var shape = $scope.selectedShape;
            // Some updates may happen when selected shape is already changed, so when an additional
            // shapeId is supplied, we need to make sure the correct shape is updated (current or previous)
            if (shapeId) {
                if (shape.id != shapeId && $scope.previousSelectedShape && $scope.previousSelectedShape.id == shapeId) {
                    shape = $scope.previousSelectedShape;
                } else {
                    shape = null;
                }
            }

            if (!shape) {
                // When no shape is selected, or no shape is found for the alternative
                // shape ID, do nothing
                return;
            }
            var key = property.key;
            var newValue = property.value;
            var oldValue = shape.properties[key];

            if (newValue != oldValue) {
                var commandClass = ORYX.Core.Command.extend({
                    construct: function () {
                        this.key = key;
                        this.oldValue = oldValue;
                        this.newValue = newValue;
                        this.shape = shape;
                        this.facade = $scope.editor;
                    },
                    execute: function () {
                        this.shape.setProperty(this.key, this.newValue);
                        this.facade.getCanvas().update();
                        this.facade.updateSelection();
                    },
                    rollback: function () {
                        this.shape.setProperty(this.key, this.oldValue);
                        this.facade.getCanvas().update();
                        this.facade.updateSelection();
                    }
                });
                // Instantiate the class
                var command = new commandClass();

                // Execute the command
                $scope.editor.executeCommands([command]);
                $scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_PROPWINDOW_PROP_CHANGED,
                    elements: [shape],
                    key: key
                });

                // Switch the property back to read mode, now the update is done
                property.mode = 'read';

                // Fire event to all who is interested
                // Fire event to all who want to know about this
                var event = {
                    type: KISBPM.eventBus.EVENT_TYPE_PROPERTY_VALUE_CHANGED,
                    property: property,
                    oldValue: oldValue,
                    newValue: newValue
                };
                KISBPM.eventBus.dispatch(event.type, event);
            } else {
                // Switch the property back to read mode, no update was needed
                property.mode = 'read';
            }

        };
    }

    $scope.cancel = function() {
        $scope.$hide();
    };

    // Close button handler
    $scope.close = function() {
        $scope.$hide();
    };
}];
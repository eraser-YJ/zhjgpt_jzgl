/**
 * 过滤器，过滤选中容器中的属性，
 * ng-repeat="person in personDefinitions | unique: 'id'"
 */
angular.module('activitiModeler').filter('unique', function () {
    return function (collection, keyname) {
        var output = [],
            keys = [];

        angular.forEach(collection, function (item) {
            var key = item[keyname];
            if (keys.indexOf(key) === -1) {
                keys.push(key);
                output.push(item);
            }
        });
        return output;
    };
});

var ActivitiPersonModalSelectCtrl = ['$scope', '$q', '$translate', '$timeout', '$person', '$http', '$rootScope',
    function ($scope, $q, $translate, $timeout, $person, $http, $rootScope) {

        $scope.ztreePersonSelsed = [];          //备选人员容器
        $scope.personDefinitions = $scope.pDefinitions.length ? $scope.pDefinitions : []; //选中人员容器
        $scope.statusAll = {
            checkbox: false,           //备用的全选按钮
            error: false,           //备用容器无数据时错误信息输出控制
            name: ''
        }
        $scope.ztreeObj = null;
        $scope.addStatus = false;
        /**
         * 获取当前节点的子节点数据
         * @param nodes     当前节点
         * @returns {Array} 节点数组
         */
        //function getLocalTreeData(nodes){
        //    var resluts = [];
        //    $scope.ztreeObj.getNodesByFilter(function(node){
        //        if(node.type !== 'org'){
        //            node.checked = false;
        //            switch ($scope.selectTypeActive){
        //                case 'all' :
        //                    resluts.push(node);
        //                    break;
        //                case 'dept' :
        //                    if(node.type === 'dept')  resluts.push(node);
        //                    break;
        //                case 'role' :
        //                    if(node.type === 'role') resluts.push(node);
        //                    break;
        //                case 'user' :
        //                    break;
        //                default :
        //                    resluts.push(node);
        //                    return;
        //            }
        //        }
        //    },false,nodes);
        //    return resluts;
        //}

        function getUserById(nodes) {
            // var localData = getLocalTreeData(nodes);
            /**
             * 1.判断是机构则不查询用户
             * 2.判断显示类型是role则只显示角色，不查询用户
             * 3.判断显示类型是dept则只显示部门，不查询用户
             */
            //if(nodes.type === 'org' || $scope.selectTypeActive === 'role' || $scope.selectTypeActive === 'dept'){
            //    $scope.safeApply(function(){
            //        $scope.ztreePersonSelsed = localData;
            //        $scope.statusAll         = {
            //            error    : !!localData.length,
            //            name     : nodes.name
            //        }
            //    });
            //}else{
            /**
             * 查询用户
             */
            $http({
                method: 'GET',
                url: ($scope.property.key === 'oryx-administrator') ? KISBPM.URL.getOnlyUser() : KISBPM.URL.getZtreeUser(),
                params: {
                    id: nodes.dataId,
                    type: nodes.type
                }
            }).success(function (data) {
                if (data.length) {
                    //将本地的数据与查询到的用户数据合并显示
                    //$scope.ztreePersonSelsed = localData.concat(data);
                    $scope.ztreePersonSelsed = data;
                    $scope.statusAll = {error: true, name: nodes.name}
                } else {
                    //$scope.ztreePersonSelsed = localData;
                    $scope.ztreePersonSelsed = [];
                    $scope.statusAll.error = false;
                }
            }).error(function (data) {
                $scope.statusAll.error = false;
            });
            //}
        }

        //初始化人员选择树
        $timeout(function () {
            $person.init({
                type: 'all',
                element: "personTreeSection",
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        if ($scope.ztreeObj == null) {
                            $scope.ztreeObj = $person.getZtreeObj().myTree;
                        }
                        $scope.statusAll.error = false;
                        getUserById(treeNode);
                    }
                }
            });
        }, 100);

        function addSelect(person) {
            if ($scope.addStatus) return false;
            $scope.addStatus = true;
            var output = $scope.personDefinitions,
                flag = true;
            if (output.length) {
                $A(output).each(function (item) {
                    if (item.id === person.id) {
                        flag = false;
                    }
                });
                if (flag) output.push({
                    type: person.type,
                    name: person.name,
                    id: person.id,
                    dataId: person.dataId,
                    typeName: $scope.getType(person.type)
                });
            } else {
                output.push({
                    type: person.type,
                    name: person.name,
                    id: person.id,
                    dataId: person.dataId,
                    typeName: $scope.getType(person.type)
                });
            }
            $scope.safeApply(function () {
                $scope.personDefinitions = output;
                $scope.addStatus = false;
            });
        }

        function removeSelect(person) {
            if ($scope.addStatus) return false;
            $scope.addStatus = true;
            var output = $scope.personDefinitions;
            for (var i = 0; i < output.length; i++) {
                if (output[i].id === person.id) {
                    if (!isNaN(i) || i < this.length) {
                        for (var k = 0; k < $scope.ztreePersonSelsed.length; k++) {
                            if ($scope.ztreePersonSelsed[k].dataId == output[i].dataId) {
                                $scope.ztreePersonSelsed[k].checked = false;
                            }
                        }
                        output.splice(i, 1);
                    }
                }
            }
            $scope.statusAll.checkbox = false;

            $scope.safeApply(function () {
                $scope.personDefinitions = output;
                $scope.addStatus = false;
            });
        }

        /**
         * 全选
         * @param event
         */
        $scope.alternativeAllSelect = function (event) {
            var chekd = event.target.checked;
            if ($scope.ztreePersonSelsed.length > 0) {
                $scope.statusAll.checkbox = chekd;
                $A($scope.ztreePersonSelsed).each(function (person) {
                    person.checked = chekd;
                    if (chekd) {
                        addSelect(person);
                    } else {
                        removeSelect(person);
                    }
                });
            }
        }
        //点击事件，判断是添加还是删除
        $scope.alternativeSelect = function (index, event) {
            var person = $scope.ztreePersonSelsed[index],
                checkbox = event.target.checked;
            person.checked = checkbox;
            if (checkbox) {
                addSelect(person);
            } else {
                removeSelect(person);
            }
        }
        //用户回显类型
        $scope.getType = function (type) {
            switch (type) {
                case 'user' :
                    return '（用户）';
                case 'dept' :
                    return '（部门）';
                case 'role' :
                    return '（角色）';
                default :
                    return '（未知）';
            }
        }
        //删除选中项
        $scope.removeSelectPerson = function () {
            var $container = jQuery('#alternativeContainer'),
                seleds = $container.find(':checked');
            if (seleds.length) {
                seleds.each(function (index, item) {
                    $timeout(function () {
                        removeSelect($scope.personDefinitions[item.value]);
                    }, 10);
                });
            }
        }
        //删除全部
        $scope.removeAllSelectPerson = function () {
            $scope.personDefinitions = [];
            $scope.statusAll.checkbox = false;
            $A($scope.ztreePersonSelsed).each(function (person) {
                person.checked = false;
            });
        }

        $scope.close = function () {
            $scope.$hide();
        }

        $scope.cancel = function () {
            $scope.close();
        };

        $scope.save = function () {
            if ($scope.personDefinitions.length > 0) {
                $rootScope.setPersonDefinitions($scope.personDefinitions);
            } else {
                $rootScope.setPersonDefinitions([]);
            }
            $scope.$hide();
        };

    }];
/**
 * Activiti Modeler component part of the Activiti project
 * 人员选择树的功能插件
 */
var ActivitiPersonSelectCtrl = ['$scope', '$q', '$translate', '$timeout', '$modal', '$rootScope',
    function ($scope, $q, $translate, $timeout, $modal, $rootScope) {
        if ($scope.property.group === 'transactor') {
            var defaultData = null;
            //获取已经选中的值
            if ($scope.property.value !== undefined && $scope.property.value !== null && $scope.property.value !== '') {
                if ($scope.property.value.constructor == String) {
                    defaultData = JSON.parse($scope.property.value);
                } else {
                    defaultData = angular.copy($scope.property.value);
                }
                $scope.personDefinitions = defaultData.tissue;

                $scope.unionType = defaultData.union;
                $scope.selectedVariableData = (function(){
                    var result = [];
                    $A(defaultData.variable).each(function(item){
                        result[item.id] = item;
                    });
                    return result;
                })();
            } else {
                $scope.personDefinitions = [];
                $scope.unionType = 'a';
                $scope.selectedVariableData = [];
            }

            $scope.selectedPersonSignals = [];                                      //列表选中选项的容器
            $scope.translationsPersonRetrieved = false;                             //控制列表加载完成显示
            $scope.labelsPerson = {};                                               //列表输出项容器
            $scope.isAdministrator = $scope.property.key === 'oryx-administrator';  //判断当前是否管理员设置
            $scope.selectTypeActive = 'tissue';      //当前选择人员类型 all全部 role 角色 dept部门 user用户

            var namePromise = $translate('PROPERTY.SIGNALDEFINITIONS.NAME');
            var typePromise = $translate('PROPERTY.SIGNALDEFINITIONS.TYPE');
            var cacheDefinitions = $scope.personDefinitions;

            //配置一个表格，用来显示已选人员的列表
            $q.all([namePromise, typePromise]).then(function (results) {

                $scope.labelsPerson.nameLabel = results[0];
                $scope.labelsPerson.typeLabel = results[1];
                $scope.translationsPersonRetrieved = true;

                // 配置表格
                $scope.gridPersonOptions = {
                    data: 'personDefinitions',
                    headerRowHeight: 28,
                    enableRowSelection: true,
                    enableRowHeaderSelection: false,
                    multiSelect: false,
                    enableSorting: false,
                    keepLastSelected: false,
                    selectedItems: $scope.selectedPersonSignals,
                    columnDefs: [
                        {field: 'name', displayName: $scope.labelsPerson.nameLabel},
                        {field: 'typeName', displayName: $scope.labelsPerson.typeLabel}]
                };
            });

            //判断不是管理员设置才去获取流程变量的数据
            if(!$scope.isAdministrator){
                //获取流程变量的节点
                var canvas = $scope.editor.getCanvas();
                var variables = canvas.properties['oryx-signaldefinitions'];

                $scope.variableData = variables ? variables : [];
                //流程变量点击事件
                $scope.selectedVariable = function(event){
                    var obj = event.target;
                    if(obj.checked){
                        $scope.selectedVariableData[obj.id] = {
                            id : obj.id,
                            type: obj.name
                        }
                    }else{
                        delete $scope.selectedVariableData[obj.id];
                    }
                    $scope.updatePropertyPerson();
                }

                $scope.changeUnionType = function(type){
                    $scope.unionType = type;
                    $scope.updatePropertyPerson();
                }
            }


            //过滤类型
            function filterArray(type) {
                var result = [];
                for(var key in $scope.selectedVariableData){
                    if($scope.selectedVariableData.hasOwnProperty(key)){
                        result.push($scope.selectedVariableData[key]);
                    }
                }
                return result;
                //if (type === 'all') {
                //    $scope.safeApply(function () {
                //        $scope.personDefinitions = cacheDefinitions;
                //    });
                //    return false;
                //}
                //var nodes = [];
                //$A(cacheDefinitions).each(function (node) {
                //    if (node.type === type) {
                //        nodes.push(node);
                //    }
                //});
                //$scope.personDefinitions = nodes;
            }

            //切换tab页 事件
            $scope.tabs = function (type) {
                $scope.selectTypeActive = type;
               // filterArray(type);
            }
            //是否显示向上向下按钮
            $scope.showSort = function () {
                //是管理员设置显示
                if ($scope.isAdministrator) {
                    return true;
                }
                //办理人设置只有在全部的情况下才显示
                if ($scope.selectTypeActive == 'all') {
                    return true;
                }
                return false;
            }
            //暴露给外层，使弹出层中的controller可以调同保存方法
            $rootScope.setPersonDefinitions = function (definitions) {
                $scope.safeApply(function () {
                    $scope.personDefinitions = cacheDefinitions = definitions;
                    $scope.updatePropertyPerson();
                });
            }

            //保存参数方法
            $scope.updatePropertyPerson = function () {
                var vars = filterArray($scope.selectedVariableData);


                //$scope.isAdministrator

                $scope.property.value = {
                    tissue: $scope.personDefinitions,
                    variable: vars,
                    union: $scope.unionType
                }
                $scope.updatePropertyInModel($scope.property);
            }
            //添加人员弹出选择框事件
            $scope.addNewPersonDefinition = function () {
                $rootScope.pDefinitions = $scope.personDefinitions;
                var opts = {
                    backdrop: 'static',
                    template: ACTIVITI.CONFIG.rootPath + '/process-editor/editor-app/popups/ztreemodal.html?version=' + $scope.staticIncludeVersion,
                    scope: $scope
                };
                $modal(opts);
            }
            //删除选项事件
            $scope.removePersonDefinition = function () {
                if ($scope.selectedPersonSignals && $scope.selectedPersonSignals.length > 0) {
                    var index = $scope.personDefinitions.indexOf($scope.selectedPersonSignals[0]);
                    $scope.gridPersonOptions.selectItem(index, false);
                    $scope.personDefinitions.splice(index, 1);

                    $scope.selectedPersonSignals.length = 0;
                    if (index < $scope.personDefinitions.length) {
                        $scope.gridPersonOptions.selectItem(index + 1, true);
                    } else if ($scope.personDefinitions.length > 0) {
                        $scope.gridPersonOptions.selectItem(index - 1, true);
                    }
                    $scope.updatePropertyPerson();
                }
            }
            // 点击向上移动事件
            $scope.movePersonUp = function () {
                if ($scope.selectedPersonSignals.length > 0) {
                    var index = $scope.personDefinitions.indexOf($scope.selectedPersonSignals[0]);
                    if (index != 0) { // If it's the first, no moving up of course
                        // Reason for funny way of swapping, see https://github.com/angular-ui/ng-grid/issues/272
                        var temp = $scope.personDefinitions[index];
                        $scope.personDefinitions.splice(index, 1);
                        $timeout(function () {
                            $scope.personDefinitions.splice(index + -1, 0, temp);
                            $scope.updatePropertyPerson();
                        }, 100);
                    }
                }
            };
            // 点击向下移动事件
            $scope.movePersonDown = function () {
                if ($scope.selectedPersonSignals.length > 0) {
                    var index = $scope.personDefinitions.indexOf($scope.selectedPersonSignals[0]);
                    if (index != $scope.personDefinitions.length - 1) { // If it's the last element, no moving down of course
                        // Reason for funny way of swapping, see https://github.com/angular-ui/ng-grid/issues/272
                        var temp = $scope.personDefinitions[index];
                        $scope.personDefinitions.splice(index, 1);
                        $timeout(function () {
                            $scope.personDefinitions.splice(index + 1, 0, temp);
                            $scope.updatePropertyPerson();
                        }, 100);
                    }
                }
            };
        }
    }];
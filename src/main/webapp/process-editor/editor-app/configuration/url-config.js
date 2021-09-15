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

KISBPM.URL = {
    /**
     * 流程数据地址
     * @param modelId       当前流程ID
     * @returns {string}
     */
    getModel: function(modelId) {
        return ACTIVITI.CONFIG.rootPath + '/def/get.action?definitionId=' + (modelId ? modelId : '') +'&version=' + Date.now();
    },
    /**
     * 汉化json地址
     * @returns {string}
     */
    getStencilSet: function() {
        return ACTIVITI.CONFIG.rootPath + '/process-editor/res/stencilset.json';
    },
    /**
     * 删除流程
     * @returns {string}
     */
    deleteDef: function(id){
        return ACTIVITI.CONFIG.rootPath + '/def/delete.action?id=' + id;
    },
    /**
     * 保存流程地址
     */
    putModel: function() {
        return ACTIVITI.CONFIG.rootPath + '/def/deploy.action?version=' + Date.now();
    },
    /**
     * 修改流程
     * @returns {string}
     */
    updateModel : function(){
        return ACTIVITI.CONFIG.rootPath + '/def/updateDefinition.action?version=' + Date.now();
    },
    /**
     * 左侧获得管理的流程模板树-数据    可获取机构，子机构，流程类型
     */
    getAuthZtree : function(){
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getManageDefTree.action?version=' + Date.now();
    },
    /**
     *流程设计器-人员选择树-组织树接口，可获取机构，部门，人员
     */
    getAssignZtree: function() {
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getAssignTree.action?version=' + Date.now();
    },
    /**
     *  流程设计器-人员选择树-获得人员接口  可获取人员接口
     *  @param type     dept-部门  role-角色
     *  @param id       唯一标识
     */
    getZtreeUser: function() {
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getUser.action?version=' + Date.now();
    },
    /**
     *  流程设计器-人员选择树-获得人员接口  只能获取人员
     *  @param type     dept-部门  role-角色
     *  @param id       唯一标识
     */
    getOnlyUser: function() {
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getOnlyUser.action?version=' + Date.now();
    },
    /**
     *  流程设计器-配置流程所属机构的接口
     */
    getOrgTree: function() {
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getOrgTree.action?version=' + Date.now();
    },
    /**
     *  流程设计器-配置流程所属表单数据接口
     */
    getFormTree: function() {
        return ACTIVITI.CONFIG.rootPath + '/form/queryAll.action?version=' + Date.now();
    },
    /**
     *  流程设计器-获得流程发布相关信息接口   机构列表orgTree，表单列表forms，流程类型类表types
     */
    getDeployInfo : function(){
        return ACTIVITI.CONFIG.rootPath + '/definitionAuth/getDeployInfo.action?version=' + Date.now();
    },
    /**
     * 获取所有的字段属性
     */
    getFields : function(formId){
        return ACTIVITI.CONFIG.rootPath + '/formitem/query.action?formId=' + formId+'&version=' + Date.now();
    }
};
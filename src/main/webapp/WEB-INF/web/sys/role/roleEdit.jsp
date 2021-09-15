<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
	<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
        <div class="modal-dialog">
        <form class="cmxform" id="roleForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title"><div id="roleClickType">编辑</div></h4>
                </div>
                <div class="modal-body">
                    <section class="table-wrap form-table">
                        
                        <!-- tabs模块下紧接panel模块 -->
                        <div class="panel-body">
                            <div class="tab-content">
                                <div class="tab-pane fade active in " id="">
                                    <!-- tab-pane tab切换显示的模块 内部panel的类名替换为panel-in-box -->
                                    <section class="panel-in-box">
                                        
                                        	<input type="hidden" id="id" name="id" value="0">
                        					<input type="hidden" id="token" name="token" value="${data.token}">
                        					<input type="hidden" id="modifyDate" name="modifyDate">
                                            <div class="table-wrap form-table">
                                                <table class="table table-td-striped form-table table-bordered b-light table-icon">
                                                    <tbody>
                                                        <tr>
                                                        	<td style="width:120px;">所属组织</td>
                                                        	<td>
                                                        		<input type="text" id="deptName" name="deptName" readonly/><input type="hidden" id="deptId" name="deptId"/>
                                                                <input type="hidden" id="deptWeight" name="deptWeight" />
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td  class=" b-green-dark b-tc"><span class="required">*</span>角色名称</td>
                                                            <td>
                                                            	<input type="text" name="name" id="name">
                                                            	<input type="hidden" name="nameOld" id="nameOld" >
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class=" b-green-dark b-tc"><span class="required">*</span>角色权重</td>
                                                            <td><input type="text" id="weight" maxlength="2" name="weight"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class=" b-green-dark b-tc"><span class="required">*</span>跨域权重</td>
                                                            <td><input type="text" id="bestrideWeight" maxlength="2" name="bestrideWeight"></td>
                                                        </tr>
                                                        <tr>
                                                            <td  class=" b-green-dark b-tc"><span class="required">*</span>角色描述</td>
                                                            <td>
                                                            	<input type="text" name="description" id="description">
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                       
                                    </section>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" id="savaAndKeep">保存继续</button>
                    <button class="btn" type="button" id="savaAndClose">保存退出</button>
                    <button class="btn" type="button" id="roleDivClose">关 闭</button>
                </div>
            </div>
             </form>
        </div>
    </div>
    
    <script type="text/javascript" src="${sysPath}/js/com/sys/role/roleEdit.js"></script>
	<script type="text/javascript" src="${sysPath}/js/com/sys/role/role.validate.js"></script>
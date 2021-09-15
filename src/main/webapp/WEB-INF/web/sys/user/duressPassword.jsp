<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<script src="${sysPath}/js/com/sys/user/userPwd.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/user/userPwd.validate.js" type="text/javascript"></script>

   <div class="modal fade panel" id="dPasswordModal" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    
                    <h4 class="modal-title">修改密码</h4>
              </div>
                 <div class="modal-body">
                	<form class="table-wrap form-table" id="userPwdForm">
                        <input type="hidden" id="token" name="token" value="${token}">
                        <input type="hidden" id="pwdType" name="pwdType" value="${pwdType}">
                        <div class="m-b-sm "><span class="required">为了您的安全请修改密码</span></div>
                        <table class="table table-td-striped">
                        
                            <tbody>
                                <tr>
                                    <td class="w140"><span class="required">*</span>旧密码</td>
                                    <td>
                                        <input type="password" id="password" name="password" style="width:60%;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>新密码</td>
                                    <td>
                                        <input type="password" id="newPassword" name="newPassword" style="width:60%;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>确认密码</td>
                              <td>
                                    <input  type="password" id="confirmPassword" name = "confirmPassword" style="width:60%;"/>
                                </td>
                              </tr>
                                
                
                            </tbody>
                        </table>
                     
                    </form>
                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="submit" id="saveBtn">确 定</button>
                </div>
            </div>
        </div>
    </div>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
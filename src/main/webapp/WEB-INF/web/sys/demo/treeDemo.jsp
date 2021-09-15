<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script>
    $(document).ready(function() {
        JCTree.ztree({
            container : 'subDept',
            url : getRootPath() + "/api/subDepartment/managerDeptTree.action",
            expand : true,
            rootNode : true
        });
        JCTree.init({
            container : 'subUser',
            controlId   : 'name1-name1',
            urls : {
                //所有部门和用户
                user : getRootPath() + "/api/subDepartment/getAllDeptAndUser.action"
            },
            classification : false
        });
        JCTree.init({
            container   : 'centerUser',
            controlId   : 'name0-name0'
        });
        new JCFF.JCTree.Lazy({
            container      : 'subUserNew', //or $('#userUpdateDivId)
            url:getRootPath() + '/api/subDepartment/managerDeptTree.action',
            selectUrl:getRootPath() + '/api/pinSubDepartment/getSelUsers.action',
            ztreeUrl:getRootPath() + '/api/pinSubDepartment/getAllDeptAndUser.action',
            searchUrl:getRootPath() + '/api/pinSubDepartment/getSelDeptAndUser.action',
            single    : true
        });
    });
</script>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="con-heading fl">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
    </header>
    <section class="panel m-t-md" id="body">
        <div class="table-wrap">
            <table class="table table-td-striped">
                <tbody>
                <tr>
                    <td>子系统部门树</td>
                    <td>
                        <div id="subDept"></div>
                    </td>
                </tr>
                <tr>
                    <td>子系统人员树</td>
                    <td>
                        <div id="subUser"></div>
                    </td>
                </tr>
                <tr>
                    <td>子系统新人员树</td>
                    <td>
                        <div id="subUserNew"></div>
                    </td>
                </tr>
                <tr>
                    <td>中心系统人员树</td>
                    <td>
                        <div id="centerUser"></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>
</section>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<%@ include file="/WEB-INF/web/include/jctree.jsp" %>
<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
    <section class="panel search-shrinkage clearfix">
        <div class="search-line hide">
            <input type="hidden" id="query_disPath" value="${disPath}">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <c:forEach items="${pageCond}" var="itemRowData" varStatus="abcCond">
                        <tr>
                            <c:forEach items="${itemRowData.rowList}" var="itemData" varStatus="abcItemCond">
                                <td style="width:120px;">${itemData.itemName}</td>
                                <td colspan="${itemData.condShowLen}" >
                                    <c:choose>
                                        <c:when test="${itemData.placeholder eq 'Y' }">
                                            <div></div>
                                        </c:when>
                                        <c:when test="${itemData.itemType eq 'Date' }">
                                            <div class="input-group w-p100">
                                                <input class="datepicker-input" type="text"
                                                       id="query_${itemData.itemCode}Begin"
                                                       name="${itemData.itemCode}Begin" data-pick-time="false"
                                                       data-date-format="yyyy-MM-dd"
                                                       data-ref-obj="#query_${itemData.itemCode}End lt"/>
                                                <div class="input-group-btn w30">-</div>
                                                <input class="datepicker-input" type="text"
                                                       id="query_${itemData.itemCode}End"
                                                       name="${itemData.itemCode}End" data-pick-time="false"
                                                       data-date-format="yyyy-MM-dd"
                                                       data-ref-obj="#query_${itemData.itemCode}Begin gt"/>
                                            </div>
                                        </c:when>
                                        <c:when test="${itemData.itemType eq 'Dic' }">
                                            <select id="query_${itemData.itemCode}" name="${itemData.itemCode}" dictName="${itemData.dicCode}" parentCode="${itemData.dicParentId}">
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" id="query_${itemData.itemCode}"
                                                   name="${itemData.itemCode}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <section class="form-btn m-b-lg">
                    <button class="btn dark" type="button" id="queryBtn">查 询</button>
                    <button class="btn" type="button" id="queryReset">重 置</button>
                </section>
            </form>
        </div>
        <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp" %>
    </section>
    <section class="panel m-t-md" id="body">
        <div class="table-wrap">
            <table class="table table-striped tab_height " id="viewDataTable">
                <thead>
                <tr>
                    <c:forEach items="${pageList}" var="itemData">
                        <th>${itemData.itemName}</th>
                    </c:forEach>
                    <th style="width:100px;">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>

        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
            </section>
        </section>
    </section>
</section>
</section>

<script type="text/javascript">
    viewList_oTableAoColumns = [
        <c:forEach items="${pageList}" var="itemData" varStatus="abc">
        {mData: '${itemData.itemCode}'},
        </c:forEach>
        {
            mData: function (source) {
                return openFormPageOnDetailList(source.id);
            }
        }
    ];

    function openFormPageOnDetailList(id) {
        return "<a class=\"a-icon i-edit m-r-xs\" href=\"#myModal-edit\" onclick=\"openFormPageOnDetailListBtn('" + id + "')\" ><i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"查看\"></i></a>";
    }

    function openFormPageOnDetailListBtn(id) {
        var disPath = $("#query_disPath").val();
        $("#dlhDataFormModuleDiv").html("")
        $("#dlhDataFormModuleDiv").load(getRootPath() + "/view/loadForm.action?disPath=" + disPath + "&id=" + id + "&n_" + (new Date().getTime()), null, function () {
            viewJsForm.init({title:"详细数据"});
        });
    }
</script>
<div id="dlhDataFormModuleDiv"></div>
<script src='${sysPath}/js/com/common/dateUtils.js'></script>
<script src='${sysPath}/js/com/jc/view/dataList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp" %>
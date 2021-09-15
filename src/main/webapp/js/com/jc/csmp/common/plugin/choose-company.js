var ChooseCompany = window.ChooseCompany = ChooseCompany || {};
(function () {
    /**
     * option: {
     *     renderElement: 弹出层渲染页面,
     *     checkbox: 是否支持复选框(true: 支持  false: 不支持)
     *     tableId: 表格id
     *     tableUrl: 表格数据url
     *     treeUrl: 左侧树的url
     *     callback: 回调函数 eg callback: function(data){ data{ id: '', name: ''} }
     * }
     * @type {Window.ChooseCompany.CompanyDataTable}
     */
    var CompanyDataTable = ChooseCompany.CompanyDataTable = function (option) {
        this._option = option;
        this._option.checkbox = this._option.checkbox || false;
        this._option.renderElement = this._option.renderElement || 'formModule';
        $c(this._option.renderElement).innerHTML = "";
        this._option.tableId = this._option.tableId || 'treeChangeGridTable';
        this._option.tableUrl = this._option.tableUrl || getRootPath() + "/common/api/companyTree.action";
        this._option.treeUrl = this._option.treeUrl || getRootPath() + "/common/api/companyLeftTree.action";
        if (this._option.column == undefined || this._option.column == null) {
            this._option.column = [{mData: 'name', sTitle: '项目统一编号', bSortable: false}];
        }
        this.init();
    };

    /**
     * 初始化方法
     */
    CompanyDataTable.prototype.init = function() {
        createHtml(this);
        createTree(this);
    };

    var createTree = function(me) {
        function onBeforeClick(id, node){
            if(node.isChecked == 0 && node.pId != null){
                return false;
            }else{
                return true;
            }
        }

        function onClick(event, treeId, treeNode) {
            $('#treeChangeSearchForm #treeQuery_code').val(treeNode.id == 0 ? "" : treeNode.id);
            renderTable(me);
            createListener(me);
        }

        JCTree.ztree({
            container : 'treeChangeTreeDemo',
            expand : true,
            rootNode : true,
            url: me._option.treeUrl,
            onClick: onClick,
            onBeforeClick : onBeforeClick,
        });
    };

    /**
     * 创建html页面
     */
    var createHtml = function (me) {
        var htmlContent = '<div class="modal fade panel" id="tree-change-modal" aria-hidden="false">'
            + '<div class="modal-dialog" style="width: 900px;">'
            + ' <div class="modal-content">'
            + '     <div class="modal-header">'
            + '         <button type="button" class="close" onclick="$(\'#tree-change-modal\').modal(\'hide\');">×</button>'
            + '         <h4 class="modal-title" style="padding: 2px 2px;">选择信息</h4>'
            + '     </div>'
            + '     <div class="modal-body" style="margin-top: -15px;">'
            + '         <section class="tree-fluid m-t-md" id="treeChangeWindow">'
            + '             <aside class="tree-wrap">'
            + '                 <section class="panel" id="treeChangeLeftHeight" style="overflow-y: auto;">'
            + '                     <div id="treeChangeTreeDemo" class="ztree"></div>'
            + '                 </section>'
            + '             </aside>'
            + '             <section class="tree-right">'
            + '                 <section class="panel" style="margin-top: -10px;">'
            + '                     <form class="table-wrap form-table" id="treeChangeSearchForm">'
            + '                         <input type="hidden" id="treeQuery_code" name="treeQuery_code" columnName="code" />'
            + '                         <table class="table table-td-striped">'
            + '                             <tbody>'
            + '                                 <tr>'
            + '                                     <td style="width: 80px">名称</td><td><input type="text" id="treeQuery_name" name="treeQuery_name" columnName="name" /></td>'
            + '                                     <td style="text-align: center; background: #FFFFFF;">'
            + '                                         <button class="btn" id="treeChangeSearchBtn" type="button">查 询</button>'
            + '                                         <button class="btn" type="button" onclick="$(\'#treeChangeSearchForm\')[0].reset()">重 置</button>'
            + '                                     </td>'
            + '                                 </tr>'
            + '                             </tbody>'
            + '                         </table>'
            + '                     </form>'
            + '                     <div class="tab-panel" style="margin-top: -15px;">'
            + '                         <div class="table-wrap"><table class="table table-striped tab_height" id="' + me._option.tableId + '"></table></div>'
            + '                         <section class="clearfix"><section class="pagination m-r fr m-t-none"></section></section>'
            + '                     </div>'
            + '                 </section>'
            + '             </section>'
            + '         </section>'
            + '     </div>'
            + ' </div>'
            + '</div></div>';
        $c(me._option.renderElement).innerHTML = htmlContent;
        $('#tree-change-modal').modal('show');
    };

    var renderTable = function (me) {
        if (me._option.oTable == undefined) {
            me._option.oTable = null;
        }
        var column = me._option.column;
        column.push({mData: function(source) {
            return "<a class=\"a-icon i-new treeChageRow\"  href=\"#javascript:void(0);\" dataId=\"" + source.id + "\" dataName=\"" + source.name + "\" role=\"button\">&nbsp;选择&nbsp;</a>";
        }, sTitle: '操作', bSortable: false, sWidth: 80})
        if (me._option.oTable == null) {
            me._option.oTable = $('#' + me._option.tableId).dataTable( {
                "iDisplayLength": 10,
                "sAjaxSource": me._option.tableUrl,
                "fnServerData": oTableRetrieveData,
                "aoColumns": column,
                "fnServerParams": function ( aoData ) {
                    getTableParameters(me._option.oTable, aoData);
                    $("input[name^='treeQuery_']").each(function(objIndex,object){
                        var value = $(object).val();
                        if(value != ''){
                            aoData.push({"name": $(object).attr("columnName"), "value": value});
                        }
                    });
                },
                aaSorting:[],
                aoColumnDefs: [],
                fnDrawCallback : function() {
                    var height = $('.tree-right').height();
                    $('#treeChangeWindow').height((height < 400 ? 400 : height));
                    listenerTableRow(me);
                }
            });
        } else {
            me._option.oTable.fnDraw();
        }
    };

    var createListener = function (me) {
        document.getElementById("treeChangeSearchBtn").onclick = function() {
            renderTable(me);
        };
    };

    var listenerTableRow = function(me) {
        var chooseTableRowObject = $class("treeChageRow", "a");
        for (var i = 0 ; i < chooseTableRowObject.length ; i++) {
            chooseTableRowObject[i].onclick = function () {
                me._option.callback({id: this.getAttribute("dataId"), name: this.getAttribute("dataName")});
                $('#tree-change-modal').modal('hide');
            }
        }
    };

    /**
     * 获取元素对象
     * @param elementId
     * @return {HTMLElement}
     */
    var $c = function(elementId) {
        return document.getElementById(elementId);
    };

    /**
     * 根据classname和标签名获取对象
     * @param classStr
     * @param tagName
     * @return {*}
     */
    var $class = function (classStr, tagName) {
        if (document.getElementsByClassName)    {
            return document.getElementsByClassName(classStr)
        } else {
            var nodes = document.getElementsByTagName(tagName), ret = [];
            for (var i = 0; i < nodes.length; i++) {
                if (hasClass(nodes[i], classStr)) {
                    ret.push(nodes[i]);
                }
            }
            return ret;
        }

        function hasClass(tagStr, classStr){
            var arr = tagStr.className.split(/\s+/);
            for ( var i = 0; i < arr.length; i++) {
                if (arr[i] == classStr) {
                    return true;
                }
            }
            return false;
        };
    };
})();

var ResourcePlugin = window.ResourcePlugin = ResourcePlugin || {};
(function () {
    /**
     * option: {
     *     renderElement: 弹出层渲染页面,
     *     url: '表格请求的url'
     *     modalId: '弹出层的id'
     *     tableId: '表格的id'
     *     sign: 资源名
     *     title: '弹出页面的标题'
     *     autoClose: '是否自动关闭'
     *     search: {
     *         text: 搜索框的文本
     *         column: 搜索的属性
     *     }
     *     callback: 回调函数 eg callback: function(data){ data{ id: '', name: ''} }
     * }
     * @type {Window.ChooseCompany.CompanyDataTable}
     */
    var ResourceData = ResourcePlugin.ResourceData = function (option) {
        this._option = option;
        this._option.modalId = this._option.modalId || 'resource-change-modal';
        this._option.tableId = this._option.tableId || 'resourceChangeGridTable';
        if (this._option.autoClose == undefined) {
            this._option.autoClose = true;
        }
        this._option.url = this._option.url || getRootPath() + '/resource/system/manageList.action';
        $c(this._option.renderElement).innerHTML = "";
        this.init();
    };

    var tableRowData = {};

    /**
     * 初始化方法
     */
    ResourceData.prototype.init = function() {
        createTable(this._option);
    };

    /**
     * 关闭窗口
     */
    ResourceData.prototype.closeWindow = function() {
        $('#' + this._option.modalId).modal('hide');
    };

    var createTable = function(config) {
        var array = [];
        if (config.search != undefined && config.search.length > 0) {
            for (var i = 0; i < config.search.length; i++) {
                array.push('<td class="w140">' + config.search[i].text + '</td><td><input type="text" id="resourceChangeQuery_' + config.search[i].column + '" name="resourceChangeQuery_' + config.search[i].column + '" /></td>');
            }
        }
        var searchStyle = 'style="display:none;"';
        var searchTable = '';
        if (array.length > 0) {
            //有搜索条件
            searchStyle = '';
            var index = 0;
            for (var i = 0; i < array.length; i++) {
                if (i == 0 && index == 0) {
                    searchTable += "<tr>";
                } else if (i != 0 && index == 0) {
                    searchTable += "</tr><tr>";
                }
                searchTable += array[i];
                index++;
                if (index == 2) {
                    index = 0;
                }
            }
            if (index == 0) {
                searchTable += '<tr><td style="text-align: right; background: #FFFFFF;" colspan="4"><button class="btn" type="button" id="resourceChangeQuerySearchBtn">查 询</button><button class="btn" type="button" onclick="$(\'#resourceChangeSearchForm\')[0].reset()">重 置</button></td></tr>';
            } else {
                searchTable += '<td style="text-align: right; background: #FFFFFF;" colspan="2"><button class="btn" type="button" id="resourceChangeQuerySearchBtn">查 询</button><button class="btn" type="button" onclick="$(\'#resourceChangeSearchForm\')[0].reset()">重 置</button></td></tr>';
            }
        }
        var body = '<div class="modal fade panel" id="' + config.modalId + '" aria-hidden="false">'
            + ' <div class="modal-dialog w1100">'
            + '     <div class="modal-content">'
            + '         <div class="modal-header">'
            + '             <button type="button" class="close" onclick="$(\'#' + config.modalId + '\').modal(\'hide\');">×</button>'
            + '             <h4 class="modal-title">' + config.title + '</h4>'
            + '         </div>'
            + '         <div class="modal-body">'
            + '             <section class="panel clearfix search-box search-shrinkage" ' + searchStyle + '>'
            + '                 <div class="search-line">'
            + '                     <form class="table-wrap form-table" id="resourceChangeSearchForm">'
            + '                         <input type="hidden" id="sign" name="sign" value="' + config.sign + '" />'
            + '                         <table class="table table-td-striped"><tbody>' + searchTable + '</tbody></table>'
            + '                     </form>'
            + '                 </div>'
            + '             </section>'
            + '             <section class="panel">'
            + '                 <div class="table-wrap"><table class="table table-striped tab_height" id="' + config.tableId + '"></table></div>'
            + '                 <section class="clearfix" id="footer_height"></section>'
            + '             </section>'
            + '         </div>'
            + '     </div>'
            + ' </div>'
            + '</div>';
        $c(config.renderElement).innerHTML = body;
        $('#' + config.modalId).modal('show');
        createListener(config);
        renderTable(config);
    };

    var renderTable = function (config) {
        tableRowData = {};
        if (config.oTable == undefined) {
            config.oTable = null;
        }
        var column = config.column;
        column.push({mData: function(source) {
            tableRowData[source.dlh_data_id_] = source;
            if ($('#resourceChangeSearchForm #sign').val() == 'pt_project_info') {
                if (source.canChoose != undefined && source.canChoose != null && source.canChoose != "true") {
                    return '<a class="a-icon i-new" style="border: 1px solid #cccccc; background-color: #cccccc" href="javascript:void(0);" role="button">&nbsp;选择&nbsp;</a>';
                }
            }
            return '<a class="a-icon i-new tableClickChoose" href="javascript:void(0);" dataId="' + source.dlh_data_id_ + '" role="button">&nbsp;选择&nbsp;</a>';
        }, sTitle: '操作', bSortable: false, sWidth: 80})
        if (config.oTable == null) {
            config.oTable = $('#' + config.tableId).dataTable( {
                "iDisplayLength": 10,
                "sAjaxSource": config.url,
                "fnServerData": oTableRetrieveData,
                "aoColumns": column,
                "fnServerParams": function ( aoData ) {
                    getTableParameters(config.oTable, aoData);
                    var condition = [];
                    $("input[name^='resourceChangeQuery_']").each(function(objIndex,object){
                        var value = $(object).val();
                        if (value != '') {
                            condition.push({operationAction: 'like', operationKey: $(object).attr("id").replace('resourceChangeQuery_', ''), operationType: 'varchar',"value":value})
                        }
                    });
                    aoData.push({name: 'condJson', value: JSON.stringify(condition)});
                    aoData.push({name: 'sign', value: $('#resourceChangeSearchForm #sign').val()});
                },
                aaSorting:[],
                aoColumnDefs: [],
                fnDrawCallback : function() {
                    listenerTableRow(config);
                }
            });
        } else {
            config.oTable.fnDraw();
        }
    };

    var createListener = function (config) {
        document.getElementById("resourceChangeQuerySearchBtn").onclick = function() {
            renderTable(config);
        };
    };

    var listenerTableRow = function(config) {
        var chooseTableRowObject = $class("tableClickChoose", "a");
        for (var i = 0 ; i < chooseTableRowObject.length ; i++) {
            chooseTableRowObject[i].onclick = function () {
                config.callback(tableRowData[this.getAttribute("dataId")]);
                if (config.autoClose) {
                    $('#' + config.modalId).modal('hide');
                }
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

var cmProjectInfoFormPanel = {};

cmProjectInfoFormPanel.attach = new AttachControl.AttachListControl({
    renderElement: 'attachTable',
    businessTable: 'cm_project_info'
});

/**
 * 查看项目
 */
cmProjectInfoFormPanel.init = function(id) {
    $.ajax({
        type: "GET", data: {id: id}, dataType: "json", url: getRootPath() + "/project/info/get.action",
        success : function(data) {
            if (data) {
                $("#entityForm").fill(data);
                cmProjectInfoFormPanel.attach.initAttachOnView(id);
                cmProjectInfoFormPanel.loadPerson(id);
            }
        }
    });
};

cmProjectInfoFormPanel.personOTable = null;

cmProjectInfoFormPanel.loadPerson = function(projectId) {
    $('#personDiv').show();
    if (cmProjectInfoFormPanel.personOTable == null) {
        cmProjectInfoFormPanel.personOTable =  $('#personTable').dataTable( {
            "iDisplayLength": 100000,
            "sAjaxSource": getRootPath() + "/project/person/manageList.action",
            "fnServerData": oTableRetrieveData,
            "aoColumns": [
                {mData: 'tableRowNo', sTitle: '序号', bSortable: false, sWidth: 60},
                {mData: 'partakeTypeValue', sTitle: '责任主体', bSortable: false},
                {mData: 'companyName', sTitle: '单位名称', bSortable: false},
                {mData: 'leader', sTitle: '负责人', bSortable: false},
                {mData: 'phone', sTitle: '负责人电话', bSortable: false}
            ],
            "fnServerParams": function ( aoData ) {
                getTableParameters(cmProjectInfoFormPanel.personOTable, aoData);
                aoData.push({name: "projectId", value: projectId});
                aoData.push({name: "canShow", value: '1'});
            },
            aaSorting:[],
            pagination: false,
            aoColumnDefs: []
        });
    } else {
        cmProjectInfoFormPanel.personOTable.fnDraw();
    }
};

cmProjectInfoFormPanel.jumpList = function() {
    parent.JCF.LoadPage({url: '/project/info/manage.action' });
};

$(document).ready(function(){
    ie8StylePatch();
	$(".datepicker-input").each(function(){$(this).datepicker();});
    $('#closeBtn').click(function () { cmProjectInfoFormPanel.jumpList(); });
    cmProjectInfoFormPanel.init(getUrlParam('id'));
});
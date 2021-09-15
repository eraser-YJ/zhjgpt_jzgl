var companyListPanel = {};
companyListPanel.oTable = null;
companyListPanel.oTableAoColumns = [];
companyListPanel.resource = 'pt_company_info';
companyListPanel.companyType = null;
companyListPanel.companyName = null;


//动态加载项目坐标点
companyListPanel.searchCompany = function (companyType,companyTypeId){
    window.location.href = getRootPath() + "/supervise/gis/company/page.action?companyType="+companyType+"&companyTypeId="+companyTypeId;
};

companyListPanel.searchCompanyName = function (companyName){
    var companyName = $("#companyName").val();
    window.location.href = getRootPath() + "/supervise/gis/company/page.action?companyName="+companyName;
};

$(document).ready(function(){
    var companyTypeId = $("#companyTypeId").val();
    //企业类别选择div样式
    if(companyTypeId != ''){
        $("#"+companyTypeId).addClass("check-change-active");
    }
    // $('#searchBtn').click(companyListPanel.search('all'));
});
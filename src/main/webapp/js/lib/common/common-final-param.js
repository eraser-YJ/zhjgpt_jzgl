/*
var finalParamLookText = "&nbsp;查看&nbsp;";
var finalParamDeleteText = "&nbsp;删除&nbsp;";
*/
// var finalParamEditText = "<i class=\"fa fa-edit2\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"编辑\"></i>";
// var finalParamDeleteText = "<i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"删除\"></i>";
var finalParamEditText = "编辑";
var finalParamDeleteText = "删除";
var textOrIcon = 'text';
//finalTableBtnText('修改', 'fa-edit2')
//finalTableBtnText('查看', 'fa-infor-search')
//finalTableBtnText('修改', 'fa-edit2')
function finalTableBtnText(text, icon) {
    if (textOrIcon == 'icon') {
        return "<i class=\"fa " + icon + "\" data-toggle=\"tooltip\" data-placement=\"top\" data-container=\"body\" data-original-title=\"" + text + "\"></i>";
    }
    return "&nbsp;" + text + "&nbsp;"
}

/**
 * 设置表单页面高度
 */
function formPageScrollableHeight() {
    $('.scrollable').css('height', ($('.scrollable').height() - $('.form-btn').height() - 10) + 'px');
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    } else {
        return null;
    }
}
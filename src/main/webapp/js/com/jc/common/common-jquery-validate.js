/**验证正整数*/
jQuery.validator.addMethod("common_check_positive_integer", function (value, element) {
    if (value == "") {
        return true;
    }
    if (value === '0') {
        $(element).data('error-msg', '请输入大于0的数字');
        return false;
    }
    if (value.length > 9) {
        $(element).data('error-msg', '长度不能大于9位');
        return false;
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

jQuery.validator.addMethod("common_check_positive_integer_can_zore", function (value, element) {
    if (value == "" || value == "0") {
        return true;
    }
    if (value.length > 9) {
        $(element).data('error-msg', '长度不能大于9位');
        return false;
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

/**验证两位小数*/
jQuery.validator.addMethod("common_check_positive_double_two", function (value, element) {
    if (value == "") {
        return true;
    }
    if (value === '0') {
        $(element).data('error-msg', '请输入大于0的数字');
        return false;
    }
    if (value.indexOf('.') > -1) {
        if (value.split('.')[0].length > 13) {
            $(element).data('error-msg', '整数位长度不能大于13位');
            return false;
        }
    } else {
        if (value.length > 13) {
            $(element).data('error-msg', '整数位长度不能大于13位');
            return false;
        }
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.##'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

/**验证四位小数*/
jQuery.validator.addMethod("common_check_positive_double_four", function (value, element) {
    if (value == "") {
        return true;
    }
    if (value === '0') {
        $(element).data('error-msg', '请输入大于0的数字');
        return false;
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.####'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

jQuery.validator.addMethod("common_check_positive_double_four_can_zore", function (value, element) {
    if (value == "" || value == "0") {
        return true;
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.####'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });

/**验证一位小数*/
jQuery.validator.addMethod("common_check_positive_double_one", function (value, element) {
    if (value == "") {
        return true;
    }
    if (value === '0') {
        $(element).data('error-msg', '请输入大于0的数字');
        return false;
    }
    if (value.length > 14) {
        $(element).data('error-msg', '长度不能大于14位');
        return false;
    }
    var json = new CommonToolsLib.CommonTools({}).validateNumber({value: value, format: '+#.#'});
    if(!json.result){
        $(element).data('error-msg', json.msg);
    }
    return json.result;
}, function(params, element) { return $(element).data('error-msg'); });
var numIndexChinaSet = {
    "index0": "一、",
    "index1": "二、",
    "index2": "三、",
    "index3": "四、",
    "index4": "五、",
    "index5": "六、",
    "index6": "七、",
    "index7": "八、",
    "index8": "九、",
    "index9": "十、"
}

function initSelectYear(selectElementObject) {
    selectElementObject.empty();
    var year = new Date().getFullYear();
    for (var idx = 0; idx < 5; idx++) {
        selectElementObject.append("<option value='" + (year + idx) + "'>" + (year + idx) + "</option>");
    }
}

function initSelectYearSearch(selectElementObject) {
    selectElementObject.empty();
    selectElementObject.append("<option value=''>-全部-</option>");
    var year = new Date().getFullYear();
    for (var idx = 0; idx < 5; idx++) {
        selectElementObject.append("<option value='" + (year + idx) + "'>" + (year + idx) + "</option>");
    }
}

function nvl(value, defaultValue) {
    if (value) {
        return value;
    }
    if (defaultValue) {
        return defaultValue;
    }
    return "";
}


function isDic(value) {
    if (value == 'Y') {
        return "是";
    }
    return "否";
}

function isRealNum(val) {
    // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，

    if (val === "" || val == null) {
        return false;
    }
    if (!isNaN(val)) {
        //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
        //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
        return true;
    } else {
        return false;
    }
}

//转浮点数字
function xyParseToFloat(value) {
    if (value) {
        return parseFloat(value);
    }
    return 0;
}

//转浮点数字
function xyParseToFloatStr(value) {
    if (value) {
        return parseFloat(value).toFixed(4) + "";
    }
    return "0.0000";
}

//产生一个行id
function xyGuid() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }

    return ((new Date()).getTime() + S4() + S4() + S4() + S4() + S4() + S4() + S4() + S4());
}
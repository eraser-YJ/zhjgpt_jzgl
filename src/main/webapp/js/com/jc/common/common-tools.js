/**
 * 自定义工具命名空间
 * 常用工具类，慢慢维护
 */
var CommonToolsLib = window.CommonToolsLib = CommonToolsLib || {};
(function () {
    var CommonTools = CommonToolsLib.CommonTools = function (option) {
        this._option = option;
    };

    /**
     * 验证数字格式
     * @param param: 要验证的数字和验证类型
     * format: {format: '要验证的格式', value: '要验证的值'}
     * 格式说明： (+/-)正负数验证，没有不验证。+：包含0
     *  ''： 不进行格式验证
     *  '#'： 整数
     *  '#.': 不限制位数的小数
     *  '#.#': 一位小数，多个继续往后加'#'
     * @return {*}
     */
    CommonTools.prototype.validateNumber = function (param) {
        param.value = this.isEmpty(param.value) ? param.value : '';
        if (param.value === '' || isNaN(param.value)) {
            return {result: false, msg: '请输入正确的数字'};
        }
        param.format = param.format || "#.";
        var firstChar =  param.format.substr(0, 1);
        if(firstChar === '+' && parseFloat(param.value) < parseFloat(0)){
            return {result: false, msg: '请输入大于等于0的数字'};
        }else if(firstChar === '-' && parseFloat(param.value) >= parseFloat(0)){
            return {result: false, msg: '请输入小于0的数字'};
        }
        var paramValue = param.value.replace('-', '');
        var valueArray = paramValue.split('.');
        var formatArray = param.format.split('.');
        if (formatArray.length === 1) {
            if (paramValue.indexOf('0') === 0) {/**说明是整数，判断首位不为0*/
                return {result: false, msg: '首位不能为0'};
            }
            if (valueArray.length > 1) {
                return {result: false, msg: '请输入整数'};
            }
        } else {
            /**
             * 1、验证整数位是否为null。
             * 2、验证小数位是否为null。
             * 2、整数位长度大于1，并且第一位是0
             */
            if (valueArray[0] === '' || (valueArray.length > 1 && valueArray[1] === '') || (valueArray[0].length > 1 && valueArray[0].indexOf('0') === 0)) {
                return {result: false, msg: '请输入正确的小数'};
            }
            if(formatArray[1] !== ''){
                /**需要验证小数的位数长度*/
                if (valueArray.length > 1 && valueArray[1].length > formatArray[1].length) {
                    return {result: false, msg: '小数位长度不能大于' + formatArray[1].length};
                }
            }
        }
        return {result: true, msg: ''};
    };

    /**
     * 计算百分比
     */
    CommonTools.prototype.getPeercent = function(num, total) {
        num = parseFloat(num);
        total = parseFloat(total);
        if (isNaN(num) || isNaN(total)) {
            return "-";
        }
        return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00)+"%";
    }

    /**
     * 指定毫秒数后的日期及指定格式
     * @param millisecond: 毫秒数，支持负数, 不传递默认当前时间
     * @param format: 转换格式，如果不传递默认转换成时间类型,传递为字符格式
     * @return {string/date}
     */
    CommonTools.prototype.dateFormat  = function (millisecond, format) {
        millisecond = millisecond || 0;
        millisecond = new Date().getTime() + millisecond;
        var resultDate = new Date(millisecond);
        if(format){
            var dateJson = {
                "y+": resultDate.getFullYear(),
                "M+": resultDate.getMonth() + 1,
                "d+": resultDate.getDate(),
                "H+": resultDate.getHours(),
                "m+": resultDate.getMinutes(),
                "s+": resultDate.getSeconds(),
            };
            for(var key in dateJson){
                if(new RegExp("(" + key + ")").test(format)){
                    format = format.replace(RegExp.$1, (dateJson[key] + "").length < 2 ? "0" + dateJson[key] : dateJson[key]);
                }
            }
            return format;
        }
        return resultDate;
    };

    CommonTools.prototype.millisecondFormat  = function (millisecond, format) {
        var resultDate = new Date(millisecond);
        if(format){
            var dateJson = {
                "y+": resultDate.getFullYear(),
                "M+": resultDate.getMonth() + 1,
                "d+": resultDate.getDate(),
                "H+": resultDate.getHours(),
                "m+": resultDate.getMinutes(),
                "s+": resultDate.getSeconds(),
            };
            for(var key in dateJson){
                if(new RegExp("(" + key + ")").test(format)){
                    format = format.replace(RegExp.$1, (dateJson[key] + "").length < 2 ? "0" + dateJson[key] : dateJson[key]);
                }
            }
            return format;
        }
        return resultDate;
    };

    /**
     * 比较两个日期的大小
     * @param sdate: 开始日期
     * @param edate：结束日期
     * @return {boolean} true：结束日期>开始日期 false: 结束日期<开始日期
     */
    CommonTools.prototype.dateCompare = function (sdate, edate) {
        var stime = new Date(sdate);
        var etime = new Date(edate);
        if(etime.getTime() >= stime.getTime()){
            return true;
        } else {
            return false;
        }
    };

    /**
     * 验证各类证件号码
     * @param param {type: '证件的编号', value: '要验证的值'}
     * 类型可以选:
     *      idcard(身份证号)
     * @return {result: boolean, msg: string}
     */
    CommonTools.prototype.validateCardNo = function (param) {
        if (param.type === 'idcard') {
            var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            if (!reg.test(param.value)) {
                return {result: false, msg: '请输入正确的身份证号'};
            }
            return {result: true, msg: ''};
        }
        return {result: false, msg: '方法完善中'};
    };

    /**验证是否为null */
    CommonTools.prototype.isEmpty = function (obj) {
        if (obj == undefined || obj == null || obj == "") {
            return false;
        }
        return true;
    };

    /**
     * 截取字符串
     * @param value: 要截取的文本
     * @param textLength: 截取长度
     * @param elementType: 包围元素
     */
    CommonTools.prototype.cutText = function(value, textLength, elementType) {
        var content = "<" + elementType + " title='" + value + "'>";
        if (value.length > textLength) {
            content += value.substr(0, textLength) + '...';
        } else {
            content += value;
        }
        content += "</" + elementType + ">";
        return content;
    };

    /**
     * 去掉左右空格
     * @param value
     */
    CommonTools.prototype.trim = function (value) {
        if (!this.isEmpty(value)) {
            return value;
        }
        value = value.replace( /^\s*/, "");
        value = value.replace( /\s*$/, "");
        return value;
    };
    /**
     * 获取地址参数值
     * @param value：参数名字
     */
    CommonTools.prototype.getUrlValue = function (value) {
        var reg = new RegExp("(^|&)" + value + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {return unescape(r[2])}; return null;
    };

    /**
     * 创建元素并添加到页面上
     * @param tag：标签类型
     * @param parentObj： 父元素对象
     * @param html: 内容，可不传递，传递话未innerHTML内的值
     * @param attr: 元素标签属性json串
     */
    CommonTools.prototype.$createElement = function (tag, parentObj, html, attr) {
        var obj = document.createElement(tag);
        parentObj.appendChild(obj);
        if (attr !== undefined && attr !== null)  {
            for (var key in attr) {
                obj.setAttribute(key, attr[key]);
            }
        }
        if (html !== undefined && html !== null) {
            try { obj.innerHTML = html; } catch (e) {}
        }
        return obj;
    };

    /**
     * 获取元素对象
     * @param elementId
     * @return {HTMLElement}
     */
    CommonTools.prototype.$c = function(elementId) {
        return document.getElementById(elementId);
    };

    /**
     * 创建下拉框
     * @param config: {
     *     formId: '表单id，非必填',
     *     elementId: '元素id，必填',
     *     defaultValue: '默认值'
     *     initValue: '初始值',
     *     initText: '初始值文本'
     *     url: '请求数据的url',
     *     key: '交互传递的值',
     *     value: '显示的值',
     *     param: {}, 参数
     * }
     */
    CommonTools.prototype.$createSelect = function(config){
        var initText = config.initText === undefined ? "-请选择-" : config.initText;
        var initValue = config.initValue === undefined ? "" : config.initValue;
        var param = config.param || {};
        var selectObject = null;
        if (config.formId !== undefined) {
            selectObject = $('#' + config.formId + " #" + config.elementId);
        } else {
            selectObject = $("#" + config.elementId);
        }
        selectObject.empty();
        selectObject.append("<option value='" + initValue + "'>" + initText + "</option>");
        $.ajax({
            type : "POST", url : config.url, data : param, dataType : "json",
            success : function(data) {
                if(data != null){
                    for(var i = 0 ; i < data.length ; i++){
                        selectObject.append("<option value='" + data[i][config.key] + "'>" + data[i][config.value] +"</option>");
                    }
                    if (config.defaultValue !== undefined && config.defaultValue !== null) {
                        selectObject.val(config.defaultValue);
                    }
                }
            }
        });
    };

    /**
     * 鉴定只读属性文本框或文本域删除键
     */
    CommonTools.prototype.$listenerEventCode8 = function () {
        document.documentElement.onkeydown = function(evt){
            var b = !!evt, oEvent = evt || window.event;
            if (oEvent.keyCode == 8) {
                var node = b ? oEvent.target : oEvent.srcElement;
                var reg = /^(input|textarea)$/i, regType = /^(text|textarea)$/i;
                if(node.type != "password"){
                    if (!reg.test(node.nodeName) || !regType.test(node.type) || node.readOnly || node.disabled) {
                        if (b) {
                            return false;
                        }else{
                            oEvent.cancelBubble = true;
                            oEvent.keyCode = 0;
                            oEvent.returnValue = false;
                        }
                    }
                }
            }
        };
    };
})();

/**调用方式：
 1、 new CommonToolsLib.CommonTools({}).dateFormat();
 2、 new window.CommonToolsLib.CommonTools().dateFormat('aaa');
 */

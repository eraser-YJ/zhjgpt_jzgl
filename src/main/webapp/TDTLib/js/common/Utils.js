var Utils = function(){};

/***
 *  将源Object内部的元素复制到目标Object里面。可选择是否强制覆盖相同 键的属性。
 * @param {Object} target  目标Object
 * @param {Object} source  数据源Object
 * @param {Object} flag  是否强制覆盖相同键的属性，true为覆盖，false为不覆盖。默认为不覆盖。
 * 
 * @return {Object} 
 */
Utils.ObejctCopy = function(target, source, flag){
    for(var key in source){
        var value = source[key];
        if(flag){
            target[key] = value;
        }else{
            var tmp = target[key];
            if(tmp == undefined){
                target[key] = value;
            }
        }
    }
    return target;
};

Utils.ArrayCopy = function(array){
    var target = [];
    for(var i = 0; i < array.length; i++){
        target.push(array[i]);
    }
    return target;
};

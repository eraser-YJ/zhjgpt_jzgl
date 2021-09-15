//声明公用变量
var busiFun = {};
//录像回放功能
busiFun.startPlaybackFun = function () {
    hkFun.showCBInfo("");
    //获取输入的监控点编号值，必填
    var codes = ztreeModule.getSelectNodes();
    if (codes == '') {
        hkFun.showCBInfo("<b>请选择视频设备</b>");
        return;
    }
    var codeList = codes.split(",");
    //页面布局
    hkFun.initLayout(codeList);


    var startTimeStamp = new Date($("#startTimeStamp").val().replace('-', '/').replace('-', '/')).getTime();    //回放开始时间戳，必填
    var endTimeStamp = new Date($("#endTimeStamp").val().replace('-', '/').replace('-', '/')).getTime();        //回放结束时间戳，必填
    var recordLocation = 1;                                     //录像存储位置：0-中心存储，1-设备存储
    var transMode = 1;                                          //传输协议：0-UDP，1-TCP
    var gpuMode = 0;                                            //是否启用GPU硬解，0-不启用，1-启用
    //设备初始化
    for (var disIndex = 0; disIndex < codeList.length; disIndex++) {
        var cameraIndexCode = codeList[disIndex];
        var wndId = (disIndex + 1);
        hkFun.oWebControl.JS_RequestInterface({
            funcName: "startPlayback",
            argument: JSON.stringify({
                cameraIndexCode: cameraIndexCode,                   //监控点编号
                startTimeStamp: Math.floor(startTimeStamp / 1000).toString(),  //录像查询开始时间戳，单位：秒
                endTimeStamp: Math.floor(endTimeStamp / 1000).toString(),      //录像结束开始时间戳，单位：秒
                recordLocation: recordLocation,                     //录像存储类型：0-中心存储，1-设备存储
                transMode: transMode,                               //传输协议：0-UDP，1-TCP
                gpuMode: gpuMode,                                   //是否启用GPU硬解，0-不启用，1-启用
                wndId:wndId                                         //可指定播放窗口
            })
        })
    }

};

// 停止回放
busiFun.stopAllPlaybackFun = function () {
    hkFun.oWebControl.JS_RequestInterface({
        funcName: "stopAllPlayback"
    })
}

//页面加载时创建播放实例初始化
$(window).load(function () {
    hkFun.initPlugin(1);
});

$(document).ready(function () {
    $("#startPlayback").click(function () {
        busiFun.startPlaybackFun();
    });
    $("#stopAllPlayback").click(function () {
        busiFun.stopAllPlaybackFun();
    });
    $("#SnapType").change(function () {
        busiFun.snapTypeChange();
    });
    $("#playbackSnap").change(function () {
        busiFun.playbackSnapFun();
    });
    var end = new Date();
    var temp = end.getTime() + (1 * 24 * 60 * 60 * 1000) * -1;
    var  begin =  new Date(temp);
    $("#startTimeStamp").val(dateFtt("yyyy-MM-dd hh:mm:ss", begin));
    $("#endTimeStamp").val(dateFtt("yyyy-MM-dd hh:mm:ss", end));
});
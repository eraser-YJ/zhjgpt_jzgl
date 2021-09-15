//声明公用变量
var busiFun = {};
//预览功能
busiFun.startPreviewFun = function () {
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
    //设备初始化
    for (var disIndex = 0; disIndex < codeList.length; disIndex++) {
        var cameraIndexCode = codeList[disIndex];
        var streamMode = 0;                                      //主子码流标识：0-主码流，1-子码流
        var transMode = 1;                                       //传输协议：0-UDP，1-TCP
        var gpuMode = 0;                                         //是否启用GPU硬解，0-不启用，1-启用
        var wndId = (disIndex + 1);                              //播放窗口序号（在2x2以上布局下可指定播放窗口）

        cameraIndexCode = cameraIndexCode.replace(/(^\s*)/g, "");
        cameraIndexCode = cameraIndexCode.replace(/(\s*$)/g, "");

        hkFun.oWebControl.JS_RequestInterface({
            funcName: "startPreview",
            argument: JSON.stringify({
                cameraIndexCode: cameraIndexCode,                //监控点编号
                streamMode: streamMode,                         //主子码流标识
                transMode: transMode,                           //传输协议
                gpuMode: gpuMode,                               //是否开启GPU硬解
                wndId: wndId                                     //可指定播放窗口
            })
        })

    }

};

//停止全部预览
busiFun.stopAllPlaybackFun = function () {
    hkFun.oWebControl.JS_RequestInterface({
        funcName: "stopAllPreview"
    })
}

// 抓图
busiFun.playbackSnapFun = function () {
    hkFun.showCBInfo("");
    var snapName = $("#snapName").val();
    if (snapName == '') {
        hkFun.showCBInfo("<b>请输入保存路径</b>");
        return;
    }
    var wndId = 0; //选中窗口抓图    
    snapName = snapName.replace(/(^\s*)/g, "");
    snapName = snapName.replace(/(\s*$)/g, "");
    hkFun.oWebControl.JS_RequestInterface({
        funcName: "snapShot",
        argument: JSON.stringify({
            name: snapName,
            wndId: wndId
        })
    }).then(function (oData) {
        if (oData && (oData.errorCode == 0)) {
            hkFun.showCBInfo("抓图成功，请及时查看文件" + snapName);
        } else {
            hkFun.showCBInfo(JSON.stringify(oData ? oData.responseMsg : ''));
        }
    });
};

//页面加载时创建播放实例初始化
$(window).load(function () {
    hkFun.initPlugin(0);
});

$(document).ready(function () {
    $("#startPreview").click(function () {
        busiFun.startPreviewFun();
    });
    $("#stopAllPlayback").click(function () {
        busiFun.stopAllPlaybackFun();
    });
    $("#playbackSnap").click(function () {
        busiFun.playbackSnapFun();
    });
});
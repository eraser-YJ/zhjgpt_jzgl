<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>塔吊监控</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="referrer" content="always"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=0,user-scalable=no,viewport-fit=cover"/>
    <meta http-equiv="x-dns-prefetch-control" content="on"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta name="x5-fullscreen" content="true"/>
    <style>
        .html, body, div, ul, li, a {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-size: 14px;
            color: #fff;
        }

        .html, body, #app {
            height: 100%;
            height: 100vh;
            overflow: hidden;
            background-color: #000;
        }

        .container {
            position: relative;
            margin-right: auto;
            margin-left: auto;
            width: 600px;
            margin-top: 30px;
        }

        .tower .top {
            position: absolute;
            top: 130px;
            left: 451px;
            width: 28px;
            height: 10px;
            background-image: url(${sysPath}/mockequi/tower/images/top.png);
            background-repeat: no-repeat;
            background-size: 100% 100%;
            transition: all 3s;
        }

        .tower .hook {
            position: absolute;
            top: 140px;
            left: 451px;
            width: 25px;
            height: 45px;
            background-image: url(${sysPath}/mockequi/tower/images/hook.png);
            background-repeat: no-repeat;
            background-size: 100% auto;
            background-position: left bottom;
            transition: all 3s;
        }

        .tower .hook::after {
            content: " ";
            position: absolute;
            right: 6px;
            height: calc(100% - 25px);
            width: 2px;
            background-color: #f90;
        }

        .tower .hook::before {
            content: " ";
            position: absolute;
            left: 6px;
            height: calc(100% - 25px);
            width: 2px;
            background-color: #f90;
        }

        .tower .weight {
            position: absolute;
            left: -7px;
            bottom: -27px;
            width: 40px;
            height: 35px;
            background-image: url(${sysPath}/mockequi/tower/images/weight.png);
            background-repeat: no-repeat;
            background-size: 100% 100%;
        }

        .table-container {
            border-collapse: collapse;
            width: 300px;
            position: fixed;
            bottom: 30px;
            left: 60px;
            z-index: 10000;

        }

        .table-container, th, td {
            text-align: left;
            /*border: 1px solid #fff;*/
            color:#fff;
        }

        .table-container td, .table-container thead tr {
            line-height: 20px !important;
            height: 20px !important;
        }
    </style>
    <!-- 使用CDN的JS文件 -->
    <script charset="utf-8" type="text/javascript" src="${sysPath}/mockequi/vue.min.js"></script>
</head>
<body>
<div id="app" onclick="app.moveToPosition()">
    <input type="hidden" id="query_id" name="query_id" value="${id}"/>
    <input type="hidden" id="query_code" name="query_code" value="${id}"/>
    <div class="container tower">
        <img src="${sysPath}/mockequi/tower/images/background.png" alt="" style="width:1000px;">
        <div class="top" :style="{left: left+'px'}"></div>
        <div class="hook" :style="{left: left+'px',height: height+'px'}">
            <div v-show="weight" class="weight"></div>
        </div>
    </div>
</div>
<div>
    <table class="table-container">
        <tbody>
        <tr>
            <td>起重量：<span id="crane_elevating_capacity"></span></td>
        </tr>
        <tr>
            <td>高度：<span id="crane_height"></span></td>
        </tr>
        <tr>
            <td>幅度：<span id="crane_range"></span></td>
        </tr>
        <tr>
            <td>回转：<span id="crane_rotation"></span></td>
        </tr>
        <tr>
            <td>倾角：<span id="crane_tilt_angle"></span></td>
        </tr>
        <tr>
            <td>倾斜百分比：<span id="crane_tilt_percentage"></span></td>
        </tr>
        <tr>
            <td>力距百分比：<span id="crane_torque_percentage"></span></td>
        </tr>
        <tr>
            <td>重量百分比：<span id="crane_weight_percentage"></span></td>
        </tr>
        <tr>
            <td>风速：<span id="crane_wind_speed"></span></td>
        </tr>
        <tr>
            <td>风速百分比：<span id="crane_wind_speed_percentage"></span></td>
        </tr>
        <tr>
            <td>风级：<span id="beaufort_scale"></span></td>
        </tr>
        </tbody>
    </table>
</div>
<script>
    var leftbase = 250;
    var hookbase = 50;

    var leftMax = 550;
    var hookMax = 400;

    var app = new Vue({
        el: '#app',
        data: function () {
            return {
                left: leftbase,
                height: hookbase,
                weight: true
            }
        },
        methods: {
            moveToPosition1: function () {
                this.leftMove(leftbase)
                this.hookMove(hookbase)
            },
            moveToPosition2: function () {
                this.leftMove(leftMax)
                this.hookMove(hookMax)
            },
            moveToPosition: function () {
                /**
                 * 这里走http请求, 返回值 调用leftMove，hookMove方法传入参数
                 */
                var queryId = $("#query_id").val();
                $.ajax({
                    type: "GET",
                    data: {id: queryId},
                    dataType: "json",
                    url: getRootPath() + "/monit/realtime/getTowerRealInfo.action",
                    success: function (resData) {
                        if (resData.success) {
                            var data = resData.data;
                            var heigth = data.crane_height_num;
                            var range = data.crane_range_num;
                            var heigthData;
                            var leftData;
                            if (range) {
                                leftData = parseFloat(range) * 10;
                            } else {
                                leftData = leftbase;
                            }
                            if (heigth) {
                                heigthData = parseFloat(heigth) * 8;

                            } else {
                                heigthData = hookbase;
                            }
                            //纠正10 - 44;
                            leftData  = leftData +250;
                            if (leftData < leftbase) {
                                leftData = leftbase;
                            } else if (leftData > leftMax) {
                                leftData = leftMax;
                            }
                            app.leftMove(leftData)
                            if (heigthData < hookbase) {
                                heigthData = hookbase;
                            } else if (heigthData > hookMax) {
                                heigthData = hookMax;
                            }
                            heigthData = hookMax - heigthData;
                            if(heigthData<hookbase){
                                heigthData = hookbase;
                            }
                            app.hookMove(heigthData)

                            for (key in data) {
                                $("#" + key).html(data[key]);
                            }

                        } else {
                            app.leftMove(leftMax)
                            app.hookMove(hookMax- hookbase)
                        }
                    }
                });
            },
            hookMove: function (num) {
                this.height = num;
            },
            leftMove: function (num) {
                this.left = num;
            }
        }
    })

    setInterval(resetPageInfo, 1000 * 6);

    var i = 0;

    function resetPageInfo() {
        // if (i % 2 == 0) {
        //     app.moveToPosition1();
        // } else {
        //     app.moveToPosition2();
        // }
        // i++;
        app.moveToPosition();
    }

    setTimeout(resetPageInfo, 1000);
</script>
<div></div>
</body>

</html>
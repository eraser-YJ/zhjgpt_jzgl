# TDTLib

#### 项目介绍
完成了对天地图WMTS服务的封装,该模块实现了对天地图矢量 (矢量注记)、影像(影像注记)、地形图(地形注记)服务的调用。在该lib下面有5个js，分别为TDTIMGAnnoLayer.js，TDTIMGLayer.js，TDTVecAnnoLayer.js，TDTVecLayer.js，TDTMapLayer.js，分别为加载影像注记，影像图层，矢量注记，矢量图层，加载（矢量、影像、地形）图层的模块。


#### 使用说明

- *TDTIMGAnnoLayer.js，TDTIMGLayer.js，TDTVecAnnoLayer.js，TDTVecLayer.js*，这四个JS可以通过dojoConfig进行配置使用，当然也可以放在arcgis api 的部署路径下面使用。

**1.使用dojoConfig进行配置**

```
var dojoConfig = {
     async: true,
     packages: [{
                name: "lib",
                location:  "/arcgisDemo/lib"
     }],
    locale: 'zh-cn',
 
  }
```

其中，name为location路径的别名，location为自定义模块放置的位置。

**2.直接放在arcgis api  路径下**

如果你的arcgis api部署目录为安装 **C:\inetpub\wwwroot**\3.25\，其中C:\inetpub\wwwroot，为IIS服务器默认网站，只需要将lib文件夹及其下面的js放在3.25文件夹下面即可。

- TDTMapLayer.js的使用

  TDTMapLayer 中包含四个参数，分别为
  1.mapType：该值的默认值为vec,代表矢量图层，可供选择的值有vec|img|ter，分别代表矢量、影像、地形
  2.mapCoordinate：该值的默认值为w,代表投影坐标，可供选择的值有c|w分比为：地理坐标，投影坐标
  3.isAnnoLayer：默认值为"",可供选择的值为""|"ano",分别代表非注记图层，注记图层
  4.mapAnnotation：默认值为cva,不同注记类型的默认值分别为cva,cia,cta,代表矢量中文注记图层，不同底图的矢量注记可选项有：
  vec(矢量):cva(中文)、eva(英文)、mva(蒙古文)、wva(维吾尔文)
  img(影像):cia(中文)、eia(英文)、mia(蒙古文)、wia(维吾尔文)
  ter(地形图):cta(中文)、eta(英文)、mta(蒙古文)、wta(维吾尔文)

  ```
  //1.加载矢量图层（默认投影web墨卡托）
  let verLayer = new TDTMapLayer();
  //2.加载矢量注记图层
  let verAnnoLayer = new TDTMapLayer("ano");
  //3.加载影像图层
  let imgLayer = new TDTMapLayer("img","w");
  //4.加载影像注记图层
  let imgAnnoLayer = new TDTMapLayer("img","w","ano");
  //5.当我们需要加载不同语言的注记的时候，无论是矢量、影像、还是地形，我们都需要同时传入4个参数，如：
  let imgAnnoLayerM = new TDTMapLayer("img","w","ano","mia");//加载蒙古语矢量图层
  ```

#### 示例代码

```
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Search widget tutorial</title>
    <link rel="stylesheet" href="../lib/3.25/esri/css/esri.css">
    <style>
        html,
        body,#map {
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
        }
    </style>
    <script src="../lib/jquery/jquery-3.3.1.js"></script>
    <script>
        var package_path = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
        console.log(package_path);
        var dojoConfig = {
            baseUrl: "../lib/3.25/dojo",
            packages:[
                {name:'lib',location:'/arcgisDemo/lib/TDTLib'}
            ],
            async: true,
            locale: 'zh-cn'
        }
    </script>
    <script src="../lib/3.25/init.js"></script>
    <script>
        require([
            "lib/TDTMapLayer",
            "esri/map",
            "dojo/domReady!"
        ], function (TDTMapLayer, Map) {
            const map = new Map('map', {
                logo: false
            });
            /**
             * TDTMapLayer 中包含四个参数，分别为
             * 1.mapType：该值的默认值为vec,代表矢量图层，可供选择的值有vec|img|ter，分别代表矢量、影像、地形
             * 2.mapCoordinate：该值的默认值为w,代表投影坐标，可供选择的值有c|w分比为：地理坐标，投影坐标
             * 3.isAnnoLayer：默认值为"",可供选择的值为""|"ano",分别代表非注记图层，注记图层
             * 4.mapAnnotation：默认值为cva,不同注记类型的默认值分别为cva,cia,cta,代表矢量中文注记图层，不同底图的矢量注记可选项有：
             *   vec(矢量):cva(中文)、eva(英文)、mva(蒙古文)、wva(维吾尔文)
             *   img(影像):cia(中文)、eia(英文)、mia(蒙古文)、wia(维吾尔文)
             *   ter(地形图):cta(中文)、eta(英文)、mta(蒙古文)、wta(维吾尔文)
             */
            //1.加载矢量图层（默认投影web墨卡托）
            let verLayer = new TDTMapLayer();
            //2.加载矢量注记图层
            let verAnnoLayer = new TDTMapLayer("ano");
            //3.加载影像图层
            let imgLayer = new TDTMapLayer("img","w");
            //4.加载影像注记图层
            let imgAnnoLayer = new TDTMapLayer("img","w","ano");
            //5.当我们需要加载不同语言的注记的时候，无论是矢量、影像、还是地形，我们都需要同时传入4个参数，如：
            let imgAnnoLayerM = new TDTMapLayer("img","w","ano","mia");//加载蒙古语矢量图层
            map.addLayer(imgAnnoLayerM);
        });
    </script>
</head>
<body>
<div id="map"></div>
</body>
</html>
```


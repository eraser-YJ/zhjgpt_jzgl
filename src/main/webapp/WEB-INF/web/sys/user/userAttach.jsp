<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!--[if IE]>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<![endif]-->
<meta charset="utf-8">
<link rel="stylesheet" href="${sysPath}/css/jQuery_File/jquery.fileupload.all.css">
<!--link rel="stylesheet" href="${sysPath}/css/jQuery_File/blueimp-gallery.min.css">
<link rel="stylesheet" href="${sysPath}/css/jQuery_File/jquery.fileupload.css">
<link rel="stylesheet" href="${sysPath}/css/jQuery_File/jquery.fileupload-ui.css"-->
<noscript><link rel="stylesheet" href="${sysPath}/css/jQuery_File/jquery.fileupload-noscript.css"></noscript>
<noscript><link rel="stylesheet" href="${sysPath}/css/jQuery_File/jquery.fileupload-ui-noscript.css"></noscript>

<div class="navbar navbar-default navbar-fixed-top">
</div>
<div class="container">
    <form id="fileupload"  method="POST" enctype="multipart/form-data">
        <div class="row fileupload-buttonbar">
            <div class="col-lg-7">
                <span class="btn btn-success fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>添加文件</span>
                    <input type="file" name="files[]"  id="files"  >
                </span>
               
                <span class="fileupload-process"></span>            </div>
            
        </div>
        <table role="presentation" class="table table-striped" id="attacthItem">
        	<thead>
        	<tr>
        		<th width="15%">缩略图</th><th width="30%">名称</th><th width="25%">大小</th><th width="30%">操作</th>
        	</tr>
            </thead>
        	<tbody class="files"></tbody>
        </table>
    </form>
    <br>
</div>

<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">&lt;</a>
    <a class="next">&gt;</a>
    <a class="close">X</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>

<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
    	<td></td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
       <td></td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>上传</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>

<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td width="120">
            <span class="preview">
                    <img src="${sysPath}/{%=file.resourcesName%}" width="50">
            </span> 
        </td> 
        <td>
            <p class="name"> 
                {% if (file.url) { %}
                    {%=file.fileName%}
                {% } else { %} 
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">错误</span> {%=file.error%}</div> 
            {% } %}
        </td>
        <td>
            <span class="size" > {%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" {% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>删除</span>
                </button>
            {% } else { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td> 
    </tr>
{% } %}
</script>
<script src="${sysPath}/js/jQuery_File/jquery.fu.all.js"></script>
<!--script src="${sysPath}/js/jQuery_File/vendor/jquery.ui.widget.js"></script>
<script src="${sysPath}/js/jQuery_File/tmpl.min.js"></script>
<script src="${sysPath}/js/jQuery_File/load-image.min.js"></script>
<script src="${sysPath}/js/jQuery_File/canvas-to-blob.min.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.blueimp-gallery.min.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.iframe-transport.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-process.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-image.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-audio.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-video.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-validate.js"></script>
<script src="${sysPath}/js/jQuery_File/jquery.fileupload-ui.js"></script-->
<script src="${sysPath}/js/jQuery_File/userMain.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="${sysPath}/js/jQuery_File/cors/jquery.xdr-transport.js"></script>
<![endif]-->


<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
<section class="panel search-shrinkage clearfix">
<div class="search-line" style="padding-left:50px;">
<h2>数据接收标准</h2>
<h5>1.1 系统说明</h5>
通过此接口，将外部服务数据发送到平台。<br><br>
<h5>1.2 接入说明</h5>
接口连接Webservice SOAP方式连接，使用非对称密钥加密方式，将用户名以及密码写入报文头中。下面是XML样例:
<br>
<br>
&lt;?xml version="1.0" encoding="utf-8"?&gt;<br>
<br>
&lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"&gt;<br>
  &lt;soapenv:Header&gt;<br>
    &lt;wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"&gt;<br>
      &lt;wsse:Username&gt;administrator&lt;/wsse:Username&gt;<br>
      &lt;wsse:Password&gt;ZIKLwcU1KepgSqni2b3C4OK9IwUpkxVqmeZFOOsXmItKDVelYh21iiRWPK9g14caFQOqT4xbS03SyNUHdjRjcw==&lt;/wsse:Password&gt;<br>
      &lt;wsse:Timestamp&gt;1584603102879&lt;/wsse:Timestamp&gt;<br>
      &lt;wsse:Token&gt;IYfTM6N3e+ufrE/16Y8Jyw==&lt;/wsse:Token&gt;<br>
    &lt;/wsse:Security&gt;<br>
  &lt;/soapenv:Header&gt;<br>
  &lt;soapenv:Body&gt;<br>
    &lt;sendData xmlns="http://webservice.dlh.com"&gt;<br>
      &lt;arg0 xmlns=""&gt;地址参数&lt;/arg0&gt;<br>
      &lt;arg1 xmlns=""&gt;加密好的JSON数据&lt;/arg1&gt;<br>
    &lt;/sendData&gt;<br>
  &lt;/soapenv:Body&gt;<br>
&lt;/soapenv:Envelope&gt;<br>
<br>
<br>
<h5>1.3 接入步骤</h5>
1）申请用户和公共密钥；<br>
2）自己使用3DES随机参数对称密钥；<br>
3）根据公共密钥加密对称密钥，放入XML头部的Password字段；<br>
4）读取当前时间戳，放入XML头部的Timestamp字段；<br>
5）根据对称密钥加密时间戳，放入XML头部的Token字段；<br>
6）根据对称密钥加密JSON参数；
7）全部调用sendData接口发送数据，第一个参数为地址，第二个为业务数据（加密JSON参数），等待处理结果；<br>
7）处理返回结果；<br>
<br>
<br>
<h5>参考代码</h5>
<a  href="${sysPath}/resource/StandardPushDemo.zip">StandardPushDemo.zip</a>
<br>
<br>
</div>
</section>
</section> 

<%@ include file="/WEB-INF/web/include/foot.jsp"%>
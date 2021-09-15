<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<style>
	.picFocus{ margin:0 auto;  width:1000px; padding:0;  position:relative;  overflow:hidden;  zoom:1;   }
	.picFocus .hd{ width:100%; height: auto; padding-top:5px;  overflow:hidden; }
	.picFocus .hd ul{ margin-right:-5px;  overflow:hidden; zoom:1; }
	.picFocus .hd ul li{float:left; padding-top:5px;  width: 200px; text-align:center;  }
	.picFocus .hd ul li img{ width:196px; height:110px; border:3px solid #ddd; cursor:pointer; margin-right:5px;   }
	.picFocus .hd ul li.on img{ border-color:#f60;  }
	.picFocus .bd li{ vertical-align:middle;}
	.picFocus .bd img{ width:1000px; height:450px; display:block;  }
	/* 下面是前/后按钮代码，如果不需要删除即可 */
	.picFocus .prev,.picFocus .next{  position:absolute; left:3%;top:30%;margin-top:-25px; display:block; width:32px; height:40px; background:url(${sysPath}/images/demoimg/slider-arrow.png) -110px 5px no-repeat; filter:alpha(opacity=50); opacity:0.5;  }
	.picFocus .next{ left:auto; right:3%; background-position:8px 5px; }
	.picFocus .prev:hover,
	.picFocus .next:hover{ filter:alpha(opacity=100);opacity:1;  }
	.picFocus .prevStop{ display:none;}
	.picFocus .nextStop{ display:none;}
</style>

<section class="scrollable padder jcGOA-section" id="scrollable">
	<section class="panel clearfix">
		<section class="table-wrap">
			<div id="loginSlide" class="picFocus">
				<div class="bd">
					<ul>
						<c:forEach items="${logoList}" var="logovo1">
						<li>
							<img src="${sysPath}/${logovo1.logoPath}"/>
						</li>
						</c:forEach>
					</ul>
				</div>
				<!-- 下面是前/后按钮代码，如果不需要删除即可 -->
				<a class="prev" href="javascript:void(0)"></a>
				<a class="next" href="javascript:void(0)"></a>
				<div class="hd">
					<input type="hidden" id="id" name="id">
					<ul>
						<c:forEach items="${logoList}" var="logovo">
							<li>
								<img src="${sysPath}/${logovo.logoPath}" id="defaultImg${logovo.id}"/>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<section class="form-btn m-t-sm">
				<button class="btn dark" type="button" onclick="logoList.save();">保存</button>
			</section>
		</section>
	</section>
</section>
<script>
	var logojson = '${logoJson}';
</script>
<script src="${sysPath}/js/com/sys/portal/jquery.superSlide.js"></script>
<script src='${sysPath}/js/com/sys/logo/logoSet.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
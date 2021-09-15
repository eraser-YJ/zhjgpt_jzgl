(function(a) {
    a.jBox = function(b, c) {
        c = a.extend({},
        a.jBox.defaults, c);
        c.showFade = c.opacity > 0x0;
//      c.showFade = false;
        c.opacity = 0.7;
        c.isTip = c.isTip || false;
        c.isG = c.isG || false;
        c.isMessager = c.isMessager || false;
        if (b == undefined) {
            b = ''
        };
 
        if (c.border < 0x0) {
            c.border = 0x0
        };
        if (c.id == undefined) {
            c.id = 'jBox_' + Math.floor(Math.random() * 0xf4240)
        };
        if(isIeBrowser || $("html").hasClass("ie11")) setIframeInOffice(c.id,'jbox');
        if(!c.isTip){closeIframeMenu();};
        var d = false;
        var e = a('#' + c.id);
        if (e.length > 0x0) {
            c.zIndex = a.jBox.defaults.zIndex++;
            e.css({
                zIndex: c.zIndex
            });
            e.find('#jbox').css({
                zIndex: c.zIndex + 0x1
            });
            return e
        };
        var f = {
            url: '',
            type: '',
            html: '',
            isObject: b.constructor == Object
        };
        if (!f.isObject) {
            b = b + '';
            var N = b.toLowerCase();
            if (N.indexOf('id:') == 0x0) f.type = 'ID';
            else if (N.indexOf('get:') == 0x0) f.type = 'GET';
            else if (N.indexOf('post:') == 0x0) f.type = 'POST';
            else if (N.indexOf('iframe:') == 0x0) f.type = 'IFRAME';
            else if (N.indexOf('html:') == 0x0) f.type = 'HTML';
            else {
                b = 'html:' + b;
                f.type = 'HTML'
            };
            b = b.substring(b.indexOf(":") + 0x1, b.length)
        };
        if (!c.isTip && !c.isMessager && !c.showScrolling) {
            var browserM = /msie/.test(navigator.userAgent.toLowerCase());
            a(browserM ? 'html': 'body').attr('style', 'overflow:hidden;padding-right:17px;')
        };
        var g = !c.isTip && !(c.title == undefined);
        var h = f.type == 'GET' || f.type == 'POST' || f.type == 'IFRAME';
        var i = typeof c.width == 'number' ? (c.width - 0x32) + 'px': "90%";
        var j = [];
        j.push('<div id="' + c.id + '" class="jbox-' + (c.isTip ? 'tip': (c.isMessager ? 'messager': 'body')) + '">');
        if (c.showFade) {
            j.push('<div id="jbox-fade" class="jbox-fade" style="position:absolute;background-color:#505050;"></div>')
        };
        j.push('<div id="jbox-temp" class="jbox-temp" style="width:0px;height:0px;background-color:#ff3300;position:absolute;z-index:1005;fdisplay:none;"></div>');
        if (c.draggable) {
            j.push('<div id="jbox-drag" class="jbox-drag" style="position:absolute;z-index:1005;display:none;"></div>')
        };
 
        j.push('<div id="jbox" class="jbox" style="position:absolute;width:auto;height:auto;background-color:#fff;border-radius: 12px;border:5px solid #5696cb;'+(c.isTip?'background-color:#fff;':'')+'">');
        j.push('<div class="jbox-help-title jbox-title-panel" style="height:30px;display:none;"></div>');
 //       j.push('<div class="jbox-help-button jbox-button-panel" style="height:25px;padding:5px 0 5px 0;display:none;"></div>');
        j.push('<div class="jbox-con-wrap" style="margin:0px;padding:0px;border:none;'+(c.icon == 'error'?'background-colorrgb(255, 249, 249)':'')+'">');
        
        j.push('<div class="jbox-container" style="width:auto; height:auto;">');
        
        j.push('<a class="fa jbox-close" title="' + a.jBox.languageDefaults.close + '" onmouseover="$(this).addClass(\'jbox-close-hover\');" onmouseout="$(this).removeClass(\'jbox-close-hover\');" style="position:absolute; display:block; color:#8d8d8d;cursor:pointer; top:' + (0x2 + c.border-(c.isTip?1:5)) + 'px; right:' + (0x2 + (c.isTip?c.border+5:c.border)) + 'px;font-size:20px; width:15px; height:15px;' + (c.showClose ? '': 'display:none;') + '" id="jbox-close-focus">×</a>');
        if (g) {
            j.push('<div class="jbox-title-panel" style="height:20px;">');
//            j.push('<div class="jbox-title' + (c.showIcon == true ? ' jbox-title-icon': (c.showIcon == false ? '': ' ' + c.showIcon)) + '" style="float:left; width:' + i + '; line-height:' + 0x18 + 'px;overflow:hidden;text-overflow:ellipsis;word-break:break-all;">' + (c.title == '' ? '&nbsp;': '') + '</div>');
            j.push('</div>')
        };
        j.push('<div id="jbox-states"></div>');
        j.push('</div></div></div></div>');
        var k = '<iframe name="jbox-iframe" id="jbox-iframe" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" scrolling="' + c.iframeScrolling + '"></iframe>';
        var l = a(window);
        var m = a(document.body);
        //var l = a(window.parent || window);
        //var m = a(window.parent.document.body || document.body);
        var n = a(j.join('')).appendTo(m);
        var o = n.children('#jbox');
        var p = n.children('#jbox-fade');
        var q = n.children('#jbox-temp');
        if (!f.isObject) {
            switch (f.type) {
            case "ID":
                f.html = a('#' + b).html();
                break;
            case "GET":
            case "POST":
                f.html = '';
                f.url = b;
                break;
            case "HTML":
                f.html = b;
                break;
            case "IFRAME":
                f.html = k;
                if (b.indexOf('#') == -0x1) {
                    f.url = b + (b.indexOf('?') == -0x1 ? '?___t': '&___t') + Math.random()
                } else {
                    var N = b.split('#');
                    f.url = N[0x0] + (N[0x0].indexOf('?') == -0x1 ? '?___t': '&___t') + Math.random() + '#' + N[0x1]
                };
                break;
            };
            b = {
                state0: {
                    content: f.html,
                    buttons: c.buttons,
                    buttonsFocus: c.buttonsFocus,
                    submit: c.submit
                }
            }
        };
        var r = [];
        var s = o.find('.jbox-help-title').outerHeight(true);
        var t = o.find('.jbox-help-button').outerHeight(true);
        var u =  'padding:0px 10px 0px 10px;';
        a.each(b,
        function(N, O) {
            if (f.isObject) {
                O = a.extend({},
                a.jBox.stateDefaults, O)
            };
            b[N] = O;
            if (O.buttons == undefined) {
                O.buttons = {}
            };
            var P = false;
            a.each(O.buttons,
            function(T, U) {
                P = true
            });
            var Q = 'auto';
            if (typeof c.height == 'number') {
                Q = c.height;
                if (g) {
                    Q = Q - s
                };
                if (P) {
                    Q = Q - t
                };
                Q = (Q - 0x1) + 'px'
            };
            var R = '';
            var S = '25px';
            if (!f.isObject && h) {
                var T = c.height;
                if (typeof c.height == 'number') {
                    if (g) {
                        T = T - s
                    };
                    if (P) {
                        T = T - t
                    };
                    S = ((T / 0x5) * 0x2) + 'px';
                    T = (T - 0x1) + 'px'
                };
                R = ['<div id="jbox-content-loading" class="jbox-content-loading" style="min-height:70px;height:' + T + '; text-align:center;">', '<div class="jbox-content-loading-image" style="display:block; margin:auto; width:220px; height:19px; padding-top: ' + S + ';"></div>', '</div>'].join('')
                
            };
            r.push('<div id="jbox-state-' + N + '" class="jbox-state" style="display:none;">');
            r.push('<div style="min-width:50px;height:' + Q + ';">' + R + '<div id="jbox-content" class="jbox-content" style="height:' + Q + ';max-height:270px;">' + O.content + '</div>');
            
            if(c.icon == 'success'){
                r.push('<div style="margin-top:10px;margin-left:20px;font-size:16px;">共<b>12</b>人<span style="margin-left:20px;color:#5696cb;cursor:pointer;" id="alert-content"><i class="fa fa-caret-right" style="margin-right:5px;"></i>查看全部</span></div>');
            }
            r.push('</div>');
            r.push('<div class="jbox-button-panel form-btn" style="height:auto;'+(c.icon=='success'?'padding:5px 0 10px 0;':c.icon=='info' || c.icon=='error'?'margin-bottom:15px;':'padding-right:10px;margin-bottom:10px;')+'text-align: right;' + (P ? '': 'display:none;') + '">');
            
            if (!c.isTip && c.icon === 'warning') {
                r.push('<button class="jbox-bottom-text btn"  type="button" ondragstart="return false" style="min-width:50px;margin-right:0;margin-left:10px;"></button><button class="jbox-bottom-text1 btn" type="button" style="min-width:50px;margin-right:0;margin-left:10px;"></button><button class="jbox-bottom-text2 btn" type="button" style="min-width:50px;margin-right:0;margin-left:10px;"></button>')
 
            };
            
            if(c.icon == 'success' || c.icon == 'error' || c.icon == 'info'){
                a.each(O.buttons,
                function(T, U) {
                    r.push('<span value="' + U + '" ondragstart="return false" style="margin: 0px 10px 20px 0px;"><a href="###" ondragstart="return false" id="'+c.id+'_find" class="btn jbox-close" style="height:30px;min-width:60px;line-height:20px;">确定</a></span>')
                });
            }
            r.push('</div></div>')
        });
        o.find('#jbox-states').html(r.join('')).children('.jbox-state:first').css('display', 'block');
        if (h) {
            var N = o.find('#jbox-content').css({
                position: (d) ? "absolute": "fixed",
                left: -0x2710
            })
        };
        /*戈志刚添加 显示收起功能*/
        a(document).on('click','#alert-content',function(){
            a('#alert-conent-gang').css({"max-height":"200px","height":"auto","overflow-y":"auto"})
            a(this).hide();
        });
        a.each(b,
        function(N, O) {
            var P = o.find('#jbox-state-' + N);
            P.children('.jbox-button-panel').children('button').click(function() {
                var Q = P.find('#jbox-content');
                var R = O.buttons[a(this).text()];
                var S = {};
                a.each(o.find('#jbox-states :input').serializeArray(),
                function(U, V) {
                    if (S[V.name] === undefined) {
                        S[V.name] = V.value
                    } else if (typeof S[V.name] == Array) {
                        S[V.name].push(V.value)
                    } else {
                        S[V.name] = [S[V.name], V.value]
                    }
                });
                var T = O.submit(R, Q, S);
            
                if (T === undefined || T) {
                    //点击是按钮不关闭窗口，点击否则关闭窗口if(!a(this).hasClass('jbox-bottom-text'))
                    I()
                }
            }).bind('mousedown',
            function() {
                a(this).addClass('jbox-button-active')
            }).bind('mouseup',
            function() {
                a(this).removeClass('jbox-button-active')
            }).bind('mouseover',
            function() {
                a(this).addClass('jbox-button-hover')
            }).bind('mouseout',
            function() {
                a(this).removeClass('jbox-button-active').removeClass('jbox-button-hover')
            });
            
        });
        var v = function() {
            n.css({
                top: l.scrollTop()
            });
            if (c.isMessager) {
                o.css({
                    position: (d) ? "absolute": "fixed",
                    right: 0x1,
                    bottom: 0x1
                })
            }
        };
        var w = function() {
            var N = l.width();
            return document.body.clientWidth < N ? N: document.body.clientWidth
        };
        var x = function() {
            var N = l.height();
            return document.body.clientHeight < N ? N: document.body.clientHeight
        };
        var y = function() {
            if (!c.showFade) {
                return
            };
            if (c.persistent) {
                var N = 0x0;
                n.addClass('jbox-warning');
                var O = setInterval(function() {
                    n.toggleClass('jbox-warning');
                    if (N++>0x1) {
                        clearInterval(O);
                        n.removeClass('jbox-warning')
                    }
                },
                0x64)
            } else {
                I()
            }
        };
        var z = function(N) {
            if (c.isTip || c.isMessager) {
                return false
            };
            var O = (window.event) ? event.keyCode: N.keyCode;
            if (O == 0x1b) {
                I()
            };
            if (O == 0x9) {
                var P = a(':input:enabled:visible', n);
                var Q = !N.shiftKey && N.target == P[P.length - 0x1];
                var R = N.shiftKey && N.target == P[0x0];
                if (Q || R) {
                    setTimeout(function() {
                        if (!P) return;
                        var S = P[R === true ? P.length - 0x1: 0x0];
                        if (S) S.focus()
                    },
                    0xa);
                    return false
                }
            }
        };
        var A = function() {
            if (c.showFade) {
                p.css({
                    position: "absolute",
                    height: c.isTip ? x() : l.height(),
                    width: d ? l.width() : "100%",
                    top: 0x0,
                    left: 0x0,
                    right: 0x0,
                    bottom: 0x0
                })
            }
        };
        var B = function() {
            if (c.isMessager) {
                o.css({
                    position: (d) ? "absolute": "fixed",
                    right: 0x1,
                    bottom: 0x1
                })
            } else {
                q.css({
                    top: c.top
                });
                /* o就是弹出信息   -- 居中*/
                o.css({
                    position: "absolute",
                    top: ((l.height() - o.outerHeight()) / 2.5),
                    left: ((l.width() - o.outerWidth()) / 0x2)
                })
            };
            if ((c.showFade && !c.isTip) || (!c.showFade && !c.isTip && !c.isMessager)) {
                n.css({
                    position: (d) ? "absolute": "fixed",
                    height: c.showFade ? l.height() : 0x0,
                    width: "100%",
                    top: (d) ? l.scrollTop() : 0x0,
                    left: 0x0,
                    right: 0x0,
                    bottom: 0x0
                })
            };
            A()
        };
        var C = function() {
            c.zIndex = a.jBox.defaults.zIndex++;
            n.css({
                zIndex: c.zIndex
            });
            o.css({
                zIndex: c.zIndex + 0x1
            })
        };
        var D = function() {
            c.zIndex = a.jBox.defaults.zIndex++;
            n.css({
                zIndex: c.zIndex
            });
            o.css({
                display: "none",
                zIndex: c.zIndex + 0x1
            });
            if (c.showFade) {
                p.css({
                    display: "none",
                    zIndex: c.zIndex,
                    opacity: c.opacity
                })
            }
        };
        var E = function(N) {
            var O = N.data;
            O.target.find('iframe').hide();
            if (c.dragClone) {
                O.target.prev().css({
                    left: O.target.css('left'),
                    top: O.target.css('top'),
                    marginLeft: -0x2,
                    marginTop: -0x2,
                    width: O.target.width() + 0x2,
                    height: O.target.height() + 0x2
                }).show()
            };
            return false
        };
        var F = function(N) {
            var O = N.data;
            var P = O.startLeft + N.pageX - O.startX;
            var Q = O.startTop + N.pageY - O.startY;
            if (c.dragLimit) {
                var R = 0x1;
                var S = document.documentElement.clientHeight - N.data.target.height() - 0x1;
                var T = 0x1;
                var U = document.documentElement.clientWidth - N.data.target.width() - 0x1;
                if (Q < R) Q = R + (c.dragClone ? 0x2: 0x0);
                if (Q > S) Q = S - (c.dragClone ? 0x2: 0x0);
                if (P < T) P = T + (c.dragClone ? 0x2: 0x0);
                if (P > U) P = U - (c.dragClone ? 0x2: 0x0)
            };
            
            if (c.dragClone) {
                O.target.prev().css({
                    left: P,
                    top: Q
                })
            } else {
                O.target.css({
                    left: P,
                    top: Q
                })
            };
            return false
        };
        var G = function(N) {
            a(document).unbind('.draggable');
            if (c.dragClone) {
                var O = N.data.target.prev().hide();
                N.data.target.css({
                    left: O.css('left'),
                    top: O.css('top')
                }).find('iframe').show()
            } else {
                N.data.target.find('iframe').show()
            };
            return false
        };
        var H = function(N) {
            var O = N.data.target.position();
            var P = {
                target: N.data.target,
                startX: N.pageX,
                startY: N.pageY,
                startLeft: O.left,
                startTop: O.top
            };
            a(document).bind('mousedown.draggable', P, E).bind('mousemove.draggable', P, F).bind('mouseup.draggable', P, G)
        };
        var I = function() {
            if (!c.isTip && !c.isMessager) {
                if (a('.jbox-body').length == 0x1) {
                    a('body').removeAttr('style')
                };
                J()
            } else {
                if (c.isTip) {
                    var tip = a(document.body).data('tip');
                    if (tip && tip.next == true) {
                        q.css('top', tip.options.top);
                        var N = q.offset().top + l.scrollTop();
                        if (N == o.offset().top) {
                            J()
                        } else {
                            o.find('#jbox-content').html(tip.options.content.substr(0x5)).end().css({
                                left: ((l.width() - o.outerWidth()) / 0x2)
                            }).animate({
                                top: N,
                                opacity: 0.1
                            },
                            0x1f4, J)
                        }
                    } else {
                        o.animate({
                            top: '-=200',
                            opacity: 0x0
                        },
                        0x1f4, J)
                    }
                } else {
                    switch (c.showType) {
                    case 'slide':
                        o.slideUp(c.showSpeed, J);
                        break;
                    case 'fade':
                        o.fadeOut(c.showSpeed, J);
                        break;
                    case 'show':
                    default:
                        o.hide(c.showSpeed, J);
                        break
                    }
                }
            }
        };
        var J = function() {
            l.unbind('resize', A);
            if (c.draggable && !c.isTip && !c.isMessager) {
                o.find('.jbox-title-panel').unbind('mousedown', H)
            };
            if (f.type != 'IFRAME') {
                o.find('#jbox-iframe').attr({
                    'src': 'about:blank'
                })
            };
            o.html('').remove();
            if (d && !c.isTip) {
                m.unbind('scroll', v)
            };
            if (c.showFade) {
                p.fadeOut('fast',
                function() {
                    p.unbind('click', y).unbind('mousedown', C).html('').remove()
                })
            };
            n.unbind('keydown keypress', z).html('').remove();
            if (d && c.showFade) {
                a('select').css('visibility', 'visible')
            };
            if (typeof c.closed == 'function') {
                c.closed();
                if(!c.isTip){
                    openIframeMenu();
                    var  oth  = document.getElementById('topnav-other_modules') || window.parent.document.getElementById('topnav-other_modules');
                    setTimeout(function(){
                        if(!$(".modal").hasClass("in") && !$(".jbox-body").length)$(oth).show().other();
                    },300);
                };
                if(isIeBrowser || $("html").hasClass("ie11")) removeIframeInOffice(c.id);
            }
        };
        var K = function() {
            if (c.timeout > 0x0) {
                o.data('autoClosing', window.setTimeout(I, c.timeout));
                if (c.isMessager) {
                    o.hover(function() {
                        window.clearTimeout(o.data('autoClosing'))
                    },
                    function() {
                        o.data('autoClosing', window.setTimeout(I, c.timeout))
                    })
                }
            }
        };
        var L = function() {
            if (typeof c.loaded == 'function') {
                c.loaded(o.find('.jbox-state:visible').find('.jbox-content'))
            }
        };
        if (!f.isObject) {
            switch (f.type) {
            case "GET":
            case "POST":
                a.ajax({
                    type:
                    f.type,
                    url: f.url,
                    data: c.ajaxData == undefined ? {}: c.ajaxData,
                    dataType: 'html',
                    cache: false,
                    success: function(N, O) {
                        o.find('#jbox-content').css({
                            position: "static"
                        }).html(N).show().prev().hide();
                        L()
                    },
                    error: function() {
                        o.find('#jbox-content-loading').html('<div style="padding-top:50px;padding-bottom:50px;text-align:center;">Loading Error.</div>')
                    }
                });
                break;
            case "IFRAME":
                o.find('#jbox-iframe').attr({
                    'src':
                    f.url
                }).bind("load",
                function(N) {
                    a(this).parent().css({
                        position: "static"
                    }).show().prev().hide();
                    o.find('#jbox-states .jbox-state:first .jbox-button-focus').focus();
                    L()
                });
                break;
            default:
                o.find('#jbox-content').show();
                break
            }
        };
        B();
        D();
        if (d && !c.isTip) {
            l.scroll(v)
        };
        if (c.showFade) {
            p.click(y)
        };
        l.resize(A);
        n.bind('keydown keypress', z);
        o.find('.jbox-close').click(I);
        if (c.showFade) {
            p.fadeIn('fast')
        };
        var M = 'show';
        if (c.showType == 'slide') {
            M = 'slideDown'
        } else if (c.showType == 'fade') {
            M = 'fadeIn'
        };
        if (c.isMessager) {
            o[M](c.showSpeed, K)
        } else {
            var tip = a(document.body).data('tip');
            if (tip && tip.next == true) {
                a(document.body).data('tip', {
                    next: false,
                    options: {}
                });
                o.css('display', '')
            } else {
                if (!f.isObject && h) {
                    o[M](c.showSpeed)
                } else {
                    o[M](c.showSpeed, L);
                }
            }
        };
        if (!c.isTip) {
            var btnName = {};
            for(var i in c.buttons){
                if(c.buttons[i] == "yes"){
                    btnName.yes = i;
                }else if(c.buttons[i] == "no"){
                    btnName.no = i;
                }else if(c.buttons[i] == "cancel"){
                    btnName.cancel = i;
                }
            }
            if(btnName == null || btnName == undefined){
                btnName.yes = "是";
                btnName.cancel = "取消";
                btnName.no = "否";
            }
            if(!btnName.cancel){
                o.find('.jbox-bottom-text1').hide();
            }
            o.find('.jbox-bottom-text').html(btnName.yes);
            o.find('.jbox-bottom-text1').html(btnName.cancel);
            o.find('.jbox-bottom-text2').html(btnName.no);
        } else {
            o.find('.jbox-container').addClass('jbox-tip-color jbox-tip-box')
        };
        a('#'+c.id+'_find').focus();
        o.find('.jbox-bottom-text2').focus();
        if(a('.modal').hasClass('in')){
            document.onkeydown = function(event_e){
                if( window.event )event_e = window.event;
                var int_keycode = event_e.charCode||event_e.keyCode;
                //去掉回车绑定关闭事件
//              if(int_keycode ==13){I();}
            }
        }
 
        c.isG&&o.find('.jbox-container').addClass('jbox-g-color');
        
        if (f.type != 'IFRAME') {
            o.find('#jbox-states .jbox-state:first .jbox-button-focus').focus()
        } else {
            o.focus()
        };
        if (!c.isMessager) {
            K()
        };
        n.bind('mousedown', C);
        if (c.draggable && !c.isTip && !c.isMessager) {
            o.find('.jbox-title-panel').bind('mousedown', {
                target: o
            },
            H).css('cursor', 'move')
        };
        return n
    };
    a.jBox.version = 2.3;
    a.jBox.defaults = {
        id: null,
        top: "12%",
        zIndex: '99999',
        border: 0x5,
        opacity: 0.3,
        timeout: 0x0,
        showType: 'fade',
        showSpeed: 'fast',
        showIcon: true,
        showClose: true,
        draggable: false,
        dragLimit: true,
        dragClone: false,
        persistent: true,
        showScrolling: true,
        ajaxData: {},
        iframeScrolling: 'auto',
        title: '',
        width: 0x15e,
        height: 'auto',
        bottomText: '',
        buttons: {},
        buttonsFocus: 0x0,
        loaded: function(b) {},
        submit: function(b, c, d) {
            return true
        },
        closed: function(b) {}
    };
    a.jBox.stateDefaults = {
        content: '',
        buttons: {
            '确定': 'ok'
        },
        buttonsFocus: 0x0,
        submit: function(b, c, d) {
            return true
        }
    };
    //timeout: 0xbb8,关闭
    a.jBox.tipDefaults = {
        content: '',
        icon: 'info',
        top: '40%',
        width: 'auto',
        height: 'auto',
        opacity: 0x0,
        timeout: 1000,
        closed: function() {}
    };
    a.jBox.messagerDefaults = {
        content: '',
        title: 'jBox',
        icon: 'none',
        width: 0x15e,
        height: 'auto',
        timeout: 0xbb8,
        showType: 'slide',
        showSpeed: 0x258,
        border: 0x0,
        buttons: {},
        buttonsFocus: 0x0,
        loaded: function() {},
        submit: function(b, c, d) {
            return true
        },
        closed: function() {}
    };
    a.jBox.languageDefaults = {
        close: '关闭',
        ok: '确定',
        yes: '是',
        no: '否',
        cancel: '取消'
    };
    a.jBox.setDefaults = function(b) {
        a.jBox.defaults = a.extend({},
        a.jBox.defaults, b.defaults);
        a.jBox.stateDefaults = a.extend({},
        a.jBox.stateDefaults, b.stateDefaults);
        a.jBox.tipDefaults = a.extend({},
        a.jBox.tipDefaults, b.tipDefaults);
        a.jBox.messagerDefaults = a.extend({},
        a.jBox.messagerDefaults, b.messagerDefaults);
        a.jBox.languageDefaults = a.extend({},
        a.jBox.languageDefaults, b.languageDefaults)
    };
    a.jBox.getBox = function() {
        return a('.jbox-body').eq(a('.jbox-body').length - 0x1)
    };
    a.jBox.getIframe = function(b) {
        var c = (typeof b == 'string') ? a('#' + b) : a.jBox.getBox();
        return c.find('#jbox-iframe').get(0x0)
    };
    a.jBox.getContent = function() {
        return a.jBox.getState().find('.jbox-content').html()
    };
    a.jBox.setContent = function(b) {
        return a.jBox.getState().find('.jbox-content').html(b)
    };
    a.jBox.getState = function(b) {
        if (b == undefined) {
            return a.jBox.getBox().find('.jbox-state:visible')
        } else {
            return a.jBox.getBox().find('#jbox-state-' + b)
        }
    };
    a.jBox.getStateName = function() {
        return a.jBox.getState().attr('id').replace('jbox-state-', '')
    };
    a.jBox.goToState = function(b, c) {
        var d = a.jBox.getBox();
        if (d != undefined && d != null) {
            var e;
            b = b || false;
            d.find('.jbox-state').slideUp('fast');
            if (typeof b == 'string') {
                e = d.find('#jbox-state-' + b)
            } else {
                e = b ? d.find('.jbox-state:visible').next() : d.find('.jbox-state:visible').prev()
            };
            e.slideDown(0x15e,
            function() {
                window.setTimeout(function() {
                    e.find('.jbox-button-focus').focus();
                    if (c != undefined) {
                        e.find('.jbox-content').html(c)
                    }
                },
                0x14)
            })
        }
    };
    a.jBox.nextState = function(b) {
        a.jBox.goToState(true, b)
    };
    a.jBox.prevState = function(b) {
        a.jBox.goToState(false, b)
    };
    a.jBox.close = function(b, c) {
        b = b || false;
        c = c || 'body';
        if (typeof b == 'string') {
            a('#' + b).find('.jbox-close').click()
        } else {
            var d = a('.jbox-' + c);
            if (b) {
                for (var e = 0x0,
                l = d.length; e < l; ++e) {
                    d.eq(e).find('.jbox-close').click()
                }
            } else {
                if (d.length > 0x0) {
                    d.eq(d.length - 0x1).find('.jbox-close').click()
                }
            }
        }
    };
    a.jBox.open = function(b, c, d, e, f) {
        var defaults = {
            content: b,
            title: c,
            width: d,
            height: e
        };
        f = a.extend({},
        defaults, f);
        f = a.extend({},
        a.jBox.defaults, f);
        a.jBox(f.content, f)
    };
    a.jBox.prompt = function(b, c, d, e) {
        var fontSizeStatus = e;

        var defaults = {
            content: b,
            title: c,
            icon: d,
            buttons: eval('({ "' + a.jBox.languageDefaults.ok + '": "ok" })')
        };
        e = a.extend({}, defaults, e);
        e = a.extend({}, a.jBox.defaults, e);
        if (e.border < 0x0) {
            e.border = 0x0
        };
        if (e.icon != 'info' && e.icon != 'warning' && e.icon != 'success' && e.icon != 'error' && e.icon != 'question') {
            padding = '';
            e.icon = 'none'
        };
        
        var f = e.title == undefined ? 0xa: 0x23;
        var g = e.icon == 'none' ? 'height:auto;': 'min-height:80px;' + 'height:auto;';
        var h = [];
        h.push('html:');
        h.push('<div style="width:380px;' + g + 'text-align:left;font-size:16px;line-height:35px;">');
        h.push('<div style="margin-bottom:15px;">');
        //h.push('<span class="fa jbox-icon-' + e.icon + '" style="position:absolute; top:' + (f + e.border) + 'px;left:' + (0xa + e.border) + 'px; width:32px; height:32px;"></span>');
        h.push('<span style="font-size: '+(fontSizeStatus=='S'?'18':'22')+'px;max-height:220px;overflow-y:auto;word-wrap: break-word;line-height:1.5;margin-top:10px;margin-left:10px;display:block;"><span class="fa jbox-icon-' + e.icon + '" style="float:left;width:45px; height:45px;margin-right:10px;background:url('+getRootPath()+'/images/demoimg/alert-'+(e.icon=='error'?'1':e.icon=='warning'?'3':'2')+'.png)"></span>'+e.title+'</span>');

        h.push('</div>');
        if(e.icon == 'success'){
            h.push('<div style="border:1px dashed #f3f3f3;"></div>');
        }
        h.push('<div style="overflow:hidden;' + (e.icon == 'success' ? 'height:100px;': '') + '" id="alert-conent-gang">');
        h.push('<div style="height:100%; margin-left:10px;margin-right:10px;line-height:26px;" class="jbox-span">');
        if(e.icon == 'success'){
            h.push('<p style="margin:0;color:#60c060;">已发送至</p>');
        }
        h.push(e.content);
        h.push('</div>');
        h.push('</div>');
        h.push('</div>');
        e.content = h.join('');
        a.jBox(e.content, e)
    };
    a.jBox.alert = function(b, c, d) {
        a.jBox.prompt(b, c, 'none', d)
    };
    a.jBox.info = function(b, c, d) {
        a.jBox.prompt(b, c, 'info', d)
    };
    a.jBox.success = function(b, c, d) {
        a.jBox.prompt(b, c, 'success', d)
    };
    a.jBox.error = function(b, c, d) {
        a.jBox.prompt(b, c, 'error', d)
    };
    a.jBox.confirm = function(b, c, d, e) {
        var defaults = {
            buttons: eval('({ "' + a.jBox.languageDefaults.ok + '": "ok", "' + a.jBox.languageDefaults.cancel + '": "cancel" })')
        };
        if (d != undefined && typeof d == 'function') {
            defaults.submit = d
        } else {
            defaults.submit = function(f, g, h) {
                return true
            }
        };
        e = a.extend({},
        defaults, e);
        a.jBox.prompt(b, c, 'question', e)
    };
    a.jBox.warning = function(b, c, d, e) {
        var defaults = {
            buttons: a.jBox.defaults.buttons
        };
        if (d != undefined && typeof d == 'function') {
            defaults.submit = d
        } else {
            defaults.submit = function(f, g, h) {
                return true
            }
        };
        e = a.extend({},
        defaults, e);
        
        a.jBox.prompt(b, c, 'warning', e)
    };
    a.jBox.tip = function(b, c, d) {
        var defaults = {
            content: b,
            icon: c,
            terror:c,
            opacity: 0x0,
            border: 0x0,
            showClose: true,
            buttons: {},
            isTip: true
        };
        
        if (defaults.icon == 'loading') {
            defaults.timeout = 0x0;
            defaults.opacity = 0.1
        };
        d = a.extend({},
        defaults, d);
        d = a.extend({},
        a.jBox.tipDefaults, d);
        d = a.extend({},
        a.jBox.defaults, d);
        if (d.timeout < 0x0) {
            d.timeout = 0x0
        };
        //d.timeout = 800;
        if (d.border < 0x0) {
            d.border = 0x0
        };
        if (d.icon != 'info' && d.icon != 'warning' && d.icon != 'success' && d.icon != 'error' && d.icon != 'loading') {
            d.icon = 'info'
        };
        var e = [];
        e.push('html:');
        e.push('<div style="width:310px;font-size:20px;"><div style="position:relative;margin:20px;vertical-align:middle;font-size: 22px;max-height:250px;overflow:auto;line-height:1.5;word-break:break-all;white-space:normal;"><span class="fa jbox-icon-' + e.icon + '" style="width:45px; height:45px;background:url('+getRootPath()+'/images/demoimg/alert-'+(defaults.terror=='error'?'1':'2')+'.png)"></span>&nbsp;<span style="position:relative;bottom:15px;line-height: 1.8;">');
        e.push(d.content);
        e.push('</span></div></div>');
        d.content = e.join('');
        if (a('.jbox-tip').length > 0x0) {
            a(document.body).data('tip', {
                next: true,
                options: d
            });
            a.jBox.closeTip()
        };
        if (d.focusId != undefined) {
            a('#' + d.focusId).focus();
            top.$('#' + d.focusId).focus()
        };
        a.jBox(d.content, d);
    };
    a.jBox.closeTip = function() {
        a.jBox.close(false, 'tip')
    };
    a.jBox.messager = function(b, c, d, e) {
        a.jBox.closeMessager();
        var defaults = {
            content: b,
            title: c,
            timeout: (d == undefined ? a.jBox.messagerDefaults.timeout: d),
            opacity: 0x0,
            showClose: true,
            draggable: false,
            isMessager: true
        };
        e = a.extend({},
        defaults, e);
        e = a.extend({},
        a.jBox.messagerDefaults, e);
        var f = a.extend({},
        a.jBox.defaults, {});
        f.title = null;
        e = a.extend({},
        f, e);
        if (e.border < 0x0) {
            e.border = 0x0
        };
        if (e.icon != 'info' && e.icon != 'warning' && e.icon != 'success' && e.icon != 'error' && e.icon != 'question') {
            padding = '';
            e.icon = 'none'
        };
        var g = e.title == undefined ? 0xa: 0x23;
        var h = e.icon == 'none' ? 'height:auto;': 'min-height:30px;' + ((browserM && parseInt(a.browser.version) < 0x7) ? 'height:auto !important;height:100%;_height:30px;': 'height:auto;');
        var i = [];
        i.push('html:');
        i.push('<div style="margin:10px;' + h + 'padding-left:' + (e.icon == 'none' ? 0x0: 0x28) + 'px;text-align:left;font-size:23px;">');
        i.push('<span class="fa jbox-icon-' + e.icon + '" style="position:absolute; top:' + (g + e.border) + 'px;left:' + (0xa + e.border) + 'px; width:35px; height:35px;"></span>');
        
        i.push(e.content);
        i.push('</div>');
        e.content = i.join('');
        a.jBox(e.content, e)
    };
    a.jBox.closeMessager = function() {
        a.jBox.close(false, 'messager')
    };
    window.jBox = a.jBox
})(jQuery);
 

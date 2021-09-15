var DetailManager = function() {
    this.closeClickFn = function() {

        $(".close-s-btn").click(function() {
            $('#detailes-window').hide();
        });
        $('.dlgCloseBtn').click(function() {
            $('#plBufferDlg').hide();
        })
        $('#mapNum .com-close-btn').click(function() {
            $('#mapNum').hide();
        })
        $('#latLong .com-close-btn').click(function() {
            $('#latLong').hide();
        })

    }

    this.open = function() {
        $('.magnifierBlue').click(function() {

            $('#detailes-window').show();
        });
    }

    this.init = function() {

        //绑定关闭事件
        this.closeClickFn();

        //打开事件
        this.open();
        $('.hidenBtnt').click(function() {
            if ($(this).parent('.showData').css('right') == '0px') {
                $(this).parent('.showData').animate({
                    'right': "-410px"
                }, 5);
                $(this).find('i').addClass('rightImg')
            } else {
                $(this).parent('.showData').animate({
                    'right': "0px"
                }, 5);
                $(this).find('i').removeClass('rightImg')
            }

        })
        $('.z-list-show .z-list-checkbox').click(function() {

            if ($(this).html() == '') {
                $(this).html('√')
            } else {
                $(this).html('')
            }
        })
        $('.lat-long-add').click(function() {
            var lan = $('.lat-long-line').length;

            var str = '<div class="lat-long-line ">' +
                '<div class="line-left ">' + (lan + 1) + '</div>' +
                ' <div class="line-right ">' +
                '<div class="lat-long-input "><span>经度：</span><input type="text "><span>度</span><input type="text "><span>分</span><input type="text "><span>秒</span></div>' +
                '<div class="lat-long-input "><span>纬度：</span><input type="text "><span>度</span><input type="text "><span>分</span><input type="text "><span>秒</span></div>' +
                '</div>' +
                '</div>';
            $('.lat-long-0').eq(1).append(str);

        })
        $('.lat-long-select i').click(function() {
            var index = $(this).index();
            alert(index)
            $(this).addClass('com-check').siblings('i').removeClass('com-check');

            $('#lat-long-box .lat-long-0').eq(index - 1).show().siblings().hide();
            if (index == 1) {
                $('.lat-long-add').hide()
            } else {
                $('.lat-long-add').show()
            }
        })

/*        $('#lineCtrl').click(function() {
            $('#plBufferDlg').show();
        })*/
        $('#latandlong').click(function() {
            $('#latLong').show();
        })
        $('#mapnum').click(function() {
            $('#mapNum').show();
        })

        $('#start-date').click(function() {
            laydate({
                elem: '#start-date',
                isclear: true
            });
        })
        $('#end-date').click(function() {
            laydate({
                elem: '#end-date',
                isclear: true
            });
        })
        $('.iconOfLayer').hover(function() {
            $(this).css('height', '185px');
        }, function() {
            $(this).css('height', '56px');
        });
        $('.summary-btn').click(function() {
            $(this).css('border-color', '#1b6abd')
            $('.detail-btn').css('border-color', 'rgb(232, 232, 232)')
            $('.summary-content').show();
            $('.details-content').hide();
        })
        $('.detail-btn').click(function() {debugger;
            $(this).css('border-color', '#1b6abd')
            $('.summary-btn').css('border-color', 'rgb(232, 232, 232)')
            $('.summary-content').hide();
            $('.details-content').show();
        })
        $('#terrain').click(function() {
            $(this).css('top', '0')
            $('#img').css('top', '55px')
            $('#vector').css('top', '110px')
        })
        $('#vector').click(function() {
            $(this).css('top', '0')
            $('#terrain').css('top', '55px')
            $('#img').css('top', '110px')
        })
        $('#img').click(function() {
            $(this).css('top', '0')
            $('#vector').css('top', '55px')
            $('#terrain').css('top', '110px')
        })
    }
}
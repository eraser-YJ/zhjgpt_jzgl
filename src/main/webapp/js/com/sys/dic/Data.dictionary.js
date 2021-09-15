$(function(){
    $(document).on('click',".dictionary-nav li a",function(e){$(this).hasClass('collapsed')?$(this).find("i").removeClass().addClass("fa fa-plus3"):$(this).find("i").removeClass().addClass("fa fa-minus3")});
})

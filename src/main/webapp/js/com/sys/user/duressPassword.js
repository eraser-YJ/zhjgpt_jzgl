var duressPassword = {};

duressPassword.isFirstLogin = function () {
    jQuery.ajax({
        url: getRootPath() + "/sys/user/isFirstLogin.action",
        type: 'GET',
        cache: false,
        success: function (data) {
            if (data) {
                duressPassword.showPasswordModal();
                
                $('#dPasswordModal').on('hide.bs.modal', function () {
                	linceseMes();
        		});
            } else {
            	linceseMes();
            }
        }
    });
};

duressPassword.showPasswordModal = function () {
    $('#dPasswordModal').modal('show');
};
//初始化
jQuery(function ($) {
    duressPassword.isFirstLogin();
});
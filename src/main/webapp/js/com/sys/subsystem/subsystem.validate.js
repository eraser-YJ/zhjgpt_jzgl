$("#subsystemForm").validate({
    ignore:'ignore',
    rules: {
        name:{
            required: true,
            maxlength:4,
        },
        icon:{
            required: false,
            maxlength:64,
        },
        url:{
            required: true,
            url:true,
            maxlength:255,
        },
        firstUrl:{
            required: false,
            maxlength:255,
        },
        menuid:{
            required: false,
        },
        queue:{
            required: true,
            digits: true,
            maxlength: 2
        },
        isShow:{
            required: false,
        },
        openType:{
            required: false,
        },
        permission:{
            required: false,
            maxlength:200,
        },
        userSyncUrl:{
            required: false,
            maxlength:200,
        },
        extStr1:{
            required: false,
            maxlength:200,
        }
    }
});
$("#uniqueForm").validate({
    ignore:'ignore',
    rules: {
        createCount:{
            required: true,
            digits: true,
            min:1,
            max:10000,
            minlength:1,
            maxlength:4
        }
    }
});
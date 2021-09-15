$("#pinUserForm").validate({
    ignore:'ignore',
    rules: {
userId:{
    required: false,
},
userName:{
    required: false,
    maxlength:64,
},
userInitials:{
    required: false,
    maxlength:64,
},
userAbbreviate:{
    required: false,
    maxlength:64,
},
userFull:{
    required: false,
    maxlength:64,
},
    }
});
$("#subUserRoleForm").validate({
    ignore:'ignore',
    rules: {
userId:{
    required: false,
},
roleId:{
    required: false,
},
roleName:{
    required: true,
    maxlength:256
},
weight:{
    required: false,
},
  secret:{
    required: false,
    maxlength:32
}

    }
});
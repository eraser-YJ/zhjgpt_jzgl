$("#subRoleMenuForm").validate({
    ignore:'ignore',
    rules: {
roleId:{
    required: false,
},
menuId:{
    required: false,
},
menuName:{
    required: false,
    maxlength:64
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
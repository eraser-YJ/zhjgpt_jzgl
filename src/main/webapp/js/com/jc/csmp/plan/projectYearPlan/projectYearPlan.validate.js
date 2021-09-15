$("#projectPlanForm").validate({
    ignore:'ignore',
    rules: {
piId:{
    required: false,
    maxlength:50
},
projectId:{
    required: false,
    maxlength:50
},
planCode:{
    required: false,
    maxlength:255
},
planName:{
    required: false,
    maxlength:255
},
dutyCompany:{
    required: false,
    maxlength:50
},
dutyPerson:{
    required: false,
    maxlength:255
},
planStartDate:{
    required: false,
},
planEndDate:{
    required: false,
},
actualStartDate:{
    required: false,
},
actualEndDate:{
    required: false,
},
remark:{
    required: false,
    maxlength:1000
},
  finishResult:{
    required: false,
    maxlength:65535
}

    }
});
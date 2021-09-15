/**
 * jquery.validate
 */
$("#entityForm").validate({
    ignore: 'ignore',
    rules: {
        piId: {required: false, maxlength: 50},
        projectId: {required: false, maxlength: 50},
        projectName: {required: true, maxlength: 255},
        projectAddress: {required: true, maxlength: 255},
        buildProperties: {required: true, maxlength: 50},
        investmentCategory: {required: true, maxlength: 50},
        buildArea: {
            required: true,
            number:true,
            maxlength: 16
        },
        investmentAmount: {required: true, maxlength: 16},
        extStr2: {required: true, maxlength: 200},
        extStr1: {required: true, maxlength: 200},
        projectType: {required: true, maxlength: 50},
        extDate1: {required: true, maxlength: 50},
        structureType: {required: true, maxlength: 50},
        planStartDate: {required: true, maxlength: 19},
        planEndDate: {required: true, maxlength: 19},
    }
});
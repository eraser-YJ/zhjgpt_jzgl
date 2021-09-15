$("#entityForm").validate({
    ignore: 'ignore',
    rules: {
        projectCode: {required: false, maxlength: 100},
        projectName: {required: true, maxlength: 200},
        equipmentName: {required: true, maxlength: 10},
        useCompany: {required: false, maxlength: 100},
        workPosition: {required: false, maxlength: 100},
        workArea: {required: false, maxlength: 100},
        workSpace: {required: false, maxlength: 200},
        useCompanyName: {required: true, maxlength: 100},
        equipmentType: {required: true, maxlength: 100},
        equipmentCode: {required: true, maxlength: 100},
        videoCode: {required: false, maxlength: 100},
        warnInfo: {required: false, maxlength: 16383},
        driver1: {required: false, maxlength: 100},
        driver1Mobile: {required: false, maxlength: 100},
        driver2: {required: false, maxlength: 100},
        driver2Mobile: {required: false, maxlength: 100},
        signalman: {required: false, maxlength: 100},
        signalmanMobile: {required: false, maxlength: 100},
        remark: {required: false, maxlength: 500},
    }
});

$("#entityFormWarn").validate({
    ignore: 'ignore',
    rules: {
        minDistance: {required: true, maxlength: 10, number: true},
        maxDistance: {required: true, maxlength: 10, number: true},
        minHeigth: {required: true, maxlength: 10, number: true},
        maxHeigth: {required: true, maxlength: 10, number: true},
        minWeigthRate: {required: true, maxlength: 10, number: true},
        maxWeigthRate: {required: true, maxlength: 10, number: true},
        minTorqueRate: {required: true, maxlength: 10, number: true},
        maxTorqueRate: {required: true, maxlength: 10, number: true},
        minWindSpeed: {required: true, maxlength: 10, number: true},
        maxWindSpeed: {required: true, maxlength: 10, number: true},
        minAngle: {required: true, maxlength: 10, number: true},
        maxAngle: {required: true, maxlength: 10, number: true},
    }
});
$(function () {
    /* ====================================================== Commons =============================================== */
    // Context path and notice modal
    var contextPath = $("#span-context-path").text();
    // Response code from server
    var SUCCESS_CODE = 0;
    var ERROR_CODE = 1;
    var INFO_CODE = 2;
    var WARNING_CODE = 3;

    // Prevent the default submit action of form
    $("form").on("submit", function () {
        return false;
    });

    // Show the notice modal
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // Clear the existed style of the notice object
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        if (INFO_CODE === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (SUCCESS_CODE === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (WARNING_CODE === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (ERROR_CODE === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        $noticeContent.text(msg);
        $("#div-notice-modal").modal('show');
    };

    /* ====================================================== Image upload ========================================== */
    var HEALTH_IMAGE;
    var SCHEDULE_IMAGE;
    var CLOSED_IMAGE;
    var HEALTH_SUFFIX = "";
    var SCHEDULE_SUFFIX = "";
    var CLOSED_SUFFIX = "";

    // Choose image files event
    $('.health-image').on('change', function (e) {
        // console.log("a");
        HEALTH_IMAGE = e.target.files[0];
        var fileName = $('.health-image').val();
        HEALTH_SUFFIX = fileName.substring(fileName.lastIndexOf("."));
        let src = window.URL.createObjectURL(this.files[0]);
        let $img = $(this).parent().prev();
        // console.log($img);
        $img.attr('src', src);
        $(".image-submit").attr("disabled", false);
    })
    $('.schedule-image').on('change', function (e) {
        // console.log("a");
        SCHEDULE_IMAGE = e.target.files[0];
        var fileName = $('.schedule-image').val();
        SCHEDULE_SUFFIX = fileName.substring(fileName.lastIndexOf("."));
        let src = window.URL.createObjectURL(this.files[0]);
        let $img = $(this).parent().prev();
        // console.log($img);
        $img.attr('src', src);
        $(".image-submit").attr("disabled", false);
    })
    $('.closed-image').on('change', function (e) {
        // console.log("a");
        CLOSED_IMAGE = e.target.files[0];
        var fileName = $('.closed-image').val();
        CLOSED_SUFFIX = fileName.substring(fileName.lastIndexOf("."));
        let src = window.URL.createObjectURL(this.files[0]);
        let $img = $(this).parent().prev();
        // console.log($img);
        $img.attr('src', src);
        $(".image-submit").attr("disabled", false);
    })


    // Upload the image file to the Qiniu server
    function uploadFileToQiniuServer(fileType, file, key, token, mimeType, uploadErrorMsg, uploadSuccessMsg) {
        var putExtra = {
            fname: {key},
            params: {},
            mimeType: [mimeType]
        }
        var config = {
            shouldUseQiniuFileName: false,
            region: qiniu.region.z2,
            forceDirect: true,
            useCdnDomain: true,
        };

        const options = {
            quality: 0.92,
            noCompressIfLarger: true
        }
        qiniu.compressImage(file, options).then(data => {
            const observable = qiniu.upload(data.dist, key, token, putExtra, config)
            // Print the upload message
            var observer = {
                next(next) {
                    var rate = next.total.percent + "";
                    // console.log(rate.substring(0, rate.indexOf(".") + 3));
                },
                error(err) {
                    console.log(err);
                    showNoticeModal(ERROR_CODE, uploadErrorMsg);
                },
                complete(res) {
                    // console.log(res)
                    if (3 === fileType) {
                        showNoticeModal(SUCCESS_CODE, uploadSuccessMsg);
                    }
                }
            }
            // Upload start
            const subscription = observable.subscribe(observer);
        })
    };

    // Get qiniu upload token from the server
    function getUploadToken(requestUrl, fileType, file, requestErrorMsg, uploadErrorMsg, uploadSuccessMsg) {
        $.ajax({
            url: contextPath + requestUrl,
            dataType: "json",
            type: "post",
            async: false,
            success: function (response) {
                if (SUCCESS_CODE === response.code) {
                    uploadFileToQiniuServer(fileType, file, response.resultMap.key, response.resultMap.token, "image/*", response.msg, response.msg);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(ERROR_CODE, requestErrorMsg);
            }
        })
    }

    // Images upload button click event
    $(".image-submit").click(function () {
        if (HEALTH_SUFFIX.length === 0) {
            showNoticeModal(WARNING_CODE, "请选择您的健康码");
            return false;
        }
        if (SCHEDULE_SUFFIX.length === 0) {
            showNoticeModal(WARNING_CODE, "请选择您的行程卡");
            return false;
        }
        if (CLOSED_SUFFIX.length === 0) {
            showNoticeModal(WARNING_CODE, "请选择您的密接查");
            return false;
        }
        // if (!(".jpg" === HEALTH_SUFFIX || ".png" === HEALTH_SUFFIX)) {
        //     console.log(HEALTH_SUFFIX)
        //     showNoticeModal(WARNING_CODE, "请选择 .jpg 或 .png 图片文件");
        //     return false;
        // }
        // if (!(".jpg" === SCHEDULE_SUFFIX || ".png" === SCHEDULE_SUFFIX)) {
        //     showNoticeModal(WARNING_CODE, "请选择 .jpg 或 .png 图片文件");
        //     return false;
        // }
        // if (!(".jpg" === CLOSED_SUFFIX || ".png" === CLOSED_SUFFIX)) {
        //     showNoticeModal(WARNING_CODE, "请选择 .jpg 或 .png 图片文件");
        //     return false;
        // }

        $(this).attr("disabled", "disabled");
        getUploadToken("transfer/upload/health", 1, HEALTH_IMAGE, "请求上传健康码失败", "健康码文件上传失败", "健康码文件上传成功");
        getUploadToken("transfer/upload/schedule", 2, SCHEDULE_IMAGE, "请求上传行程卡失败", "行程卡文件上传失败", "行程卡文件上传成功");
        getUploadToken("transfer/upload/closed", 3, CLOSED_IMAGE, "请求上传密接查失败", "密接查文件上传失败", "今日【两码一查】已完成");
    });

    /* =================================================== Password update ========================================== */
    // Open the login modal when click login link
    $(".link-update-password").click(function () {
        // Can not close it unless click close symbol
        $("#modal-update-password").modal({
            backdrop: "static"
        })
    });

    // Clear the content when close the modal
    function resetFormItem() {
        $("#input-old-password").val("");
        $("#input-new-password").val("");
        $("#input-new-password-again").val("");
    }

    // Close the password update modal
    $(".modal-update-close").click(function () {
        resetFormItem();
    });

    // Submit button click event
    $("#btn-update-password").click(function () {
        var oldPassword = $("#input-old-password").val();
        var newPassword = $("#input-new-password").val();
        var newPasswordAgain = $("#input-new-password-again").val();

        if (newPassword !== newPasswordAgain) {
            showNoticeModal(WARNING_CODE, "两次输入的密码不一致");
            return false;
        }

        // Ask server to update the user's password
        $.ajax({
            url: contextPath + "user",
            method: "POST",
            data: "_method=PUT&" + $("#form-update-password").serialize(),
            dataType: "json",
            success: function (response) {
                if (SUCCESS_CODE == response.code) {
                    resetFormItem();
                    $("#modal-update-password").modal('hide');
                    showNoticeModal(response.code, response.msg);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(WARNING_CODE, "请求修改密码失败");
            }
        })
    });
});
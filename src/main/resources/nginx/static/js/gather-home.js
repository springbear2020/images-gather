$(function () {
    /* =================================================== Commons ================================================== */
    // Context path
    var contextPath = "https://whut.springbear2020.cn/";
    var notChooseImageUrl = "https://whut.springbear2020.cn/static/img/notChoose.png";

    // Response code agreed with the server
    var CODE_SUCCESS = 0;
    var CODE_INFO = 1;
    var CODE_WARN = 2;
    var CODE_ERROR = 3;
    // Open the notice modal and show message
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // Clear the style existed
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        // Add different style of the modal
        if (CODE_SUCCESS === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (CODE_INFO === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (CODE_WARN === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (CODE_ERROR === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        // Set the content of notice then show it
        $noticeContent.text(msg);
        $("#div-notice-modal").modal('show');

        // Counting down to close the notice modal
        var countingTime = 3;
        var timer = setInterval(function () {
            countingTime--;
            if (countingTime <= 0) {
                $("#div-notice-modal").modal('hide');
                clearInterval(timer);
            }
        }, 1000)
    };

    /* ============================================= Update password ================================================ */

    // Clear the content in the update password modal
    function clearContentOfUpdatePassword() {
        $("#input-old-password").val("");
        $("#input-new-password").val("");
        $("#input-new-password-again").val("");
    }

    // Show the password update modal
    $(".link-update-password").click(function () {
        $("#modal-update-password").modal({
            backdrop: "static"
        })
    });

    // Reset content in the password input element
    $(".modal-update-close").click(function () {
        clearContentOfUpdatePassword();
    });

    // Prevent the default submit behavior of the password update form
    $("#form-update-password").on("submit", function () {
        return false;
    });

    // Before the update form submit, do some verify of it
    $("#form-update-password").submit(function () {
        // Verify the password entered in two times
        var newPassword = $("#input-new-password").val();
        var newPasswordAgain = $("#input-new-password-again").val();
        if (newPassword !== newPasswordAgain) {
            showNoticeModal(CODE_WARN, "两次输入的新密码不一致");
            return false;
        }

        // Length too short of the new password
        if (newPassword.length < 6) {
            showNoticeModal(CODE_WARN, "新密码长度应大于 6 位，请重新输入");
            return false;
        }

        // Whether the old password id equals to the new password
        var oldPassword = $("#input-old-password").val();
        if (oldPassword === newPassword) {
            showNoticeModal(CODE_WARN, "新旧密码一致，请重新输入新密码");
            return false;
        }

        // Send an ajax request to ask server for updating the password of user
        $.ajax({
            url: contextPath + "password.do",
            method: "POST",
            data: "_method=PUT&" + $("#form-update-password").serialize(),
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    // Password update successfully, close the modal and clear the content
                    $("#modal-update-password").modal('hide');
                    clearContentOfUpdatePassword();
                }
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求修改登录密码失败，请稍后重试");
            }
        });
    });

    /* =============================================== Upload images ================================================ */
    // Prevent the default behavior of the password update form
    $(".images-form").on("submit", function () {
        return false;
    });

    // Three image files object
    var healthImageFileObj;
    var scheduleImageFileObj;
    var closedImageFileObj;
    // Suffix of the three image files
    var healthFileNameSuffix = "";
    var scheduleFileNameSuffix = "";
    var closedFileNameSuffix = "";

    // Health image choose event
    $('.health-image').on('change', function (e) {
        // Get the file source stream
        healthImageFileObj = e.target.files[0];

        // Get the file suffix
        var fileName = $('.health-image').val();
        healthFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        // If choose anything, clear the existed image preview
        if (healthFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择健康码图片");
            return false;
        }
        // Display the image user chosen by user
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));

        // Unlock the upload button
        $(".image-submit").attr("disabled", false);
    })
    // Schedule image choose event
    $('.schedule-image').on('change', function (e) {
        scheduleImageFileObj = e.target.files[0];
        var fileName = $('.schedule-image').val();
        scheduleFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (scheduleFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择行程码图片");
            return false;
        }
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));
        $(".image-submit").attr("disabled", false);
    })
    // Closed image choose event
    $('.closed-image').on('change', function (e) {
        closedImageFileObj = e.target.files[0];
        var fileName = $('.closed-image').val();
        closedFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (closedFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择密接查图片");
            return false;
        }
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));
        $(".image-submit").attr("disabled", false);
    })

    // Before submit the form, do some verify
    $("#btn-images-upload").click(function () {
        // Ensure the three images are chosen by user
        if (healthFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请先选择您的今日健康码图片");
            return false;
        }
        if (scheduleFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请先选择您的今日行程码图片");
            return false;
        }
        if (closedFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请先选择您的今日密接查图片");
            return false;
        }

        $(this).attr("disabled", "disabled");
        showNoticeModal(CODE_INFO, "图片文件上传中，请稍后···");

        // Get the image files data then combine to form data
        var formData = new FormData();
        formData.append("healthImage", healthImageFileObj);
        formData.append("scheduleImage", scheduleImageFileObj);
        formData.append("closedImage", closedImageFileObj);

        $.ajax({
            url: contextPath + "transfer.do",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (response) {
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求上传图片文件失败，请稍后重试");
            }
        })
    });
});
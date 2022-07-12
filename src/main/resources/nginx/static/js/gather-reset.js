$(function () {
    /* =================================================== Commons ================================================== */
    // Context path
    var contextPath = "https://whut.springbear2020.cn/";

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

    /* =========================================== Get email verify code ============================================ */
    // Validation type
    var STATUS_SUCCESS = "success";
    var STATUS_WARNING = "warning"
    var STATUS_ERROR = "error";
    // The click status of the button
    var isObtainVerifyCodeButtonClicked = false;

    // Set the validation status of the form item and show the validation message
    function setValidateStatusOfFormItem(element, status, msg) {
        // Reset the existing style and clear the notice msg
        element.parent().removeClass("has-success has-error has-warning");
        element.next("span").removeClass("glyphicon-ok glyphicon-remove glyphicon-warning-sign");
        $(".error-msg").text("");

        // Give the item different style according to the validation status
        if (STATUS_SUCCESS === status) {
            element.parent().addClass("has-success");
            element.next("span").addClass("glyphicon-ok");
        } else if (STATUS_ERROR === status) {
            element.parent().addClass("has-error");
            element.next("span").addClass("glyphicon-remove");
        } else if (STATUS_WARNING === status) {
            element.parent().addClass("has-warning");
            element.next("span").addClass("glyphicon-warning-sign");
        }

        // Show the validation message
        $(".error-msg").text(msg);
    }

    // Get email verify code click event
    $("#btn-obtain-verify-code").click(function () {
        var $usernameElement = $("#reset-username");
        var $emailElement = $("#reset-email");
        var username = $usernameElement.val();
        var email = $emailElement.val();

        // Empty username not allowed
        if (username == null || username.length <= 0) {
            setValidateStatusOfFormItem($usernameElement, STATUS_ERROR, "用户名不能为空，请重新输入");
            return;
        }
        // Empty email address not allowed
        if (email == null || email.length <= 0) {
            setValidateStatusOfFormItem($emailElement, STATUS_ERROR, "邮箱不能为空，请重新输入");
            return;
        }
        // Verify the format of the email address
        var emailRegexp = new RegExp("^([a-z0-9_-]+)@([\\da-z-]+)\\.([a-z]{2,6})$");
        if (!emailRegexp.test(email)) {
            setValidateStatusOfFormItem($emailElement, STATUS_ERROR, "邮箱地址格式不正确，请重新输入");
            return;
        }

        isObtainVerifyCodeButtonClicked = true;
        // Block the obtain code button until time out
        var $getCodeButton = $(this);
        $getCodeButton.attr("disabled", 'disabled');
        var countingTime = 120;
        var timer = setInterval(function () {
            $getCodeButton.val(countingTime);
            if (countingTime <= 0) {
                $getCodeButton.val("获取");
                $getCodeButton.attr("disabled", false);
                isObtainVerifyCodeButtonClicked = false;
                clearInterval(timer);
            }
            countingTime--;
        }, 1000)

        // Send an request to the server for email verify code
        $.ajax({
            url: contextPath + "email.do",
            type: "post",
            async: false,
            data: "username=" + username + "&email=" + email,
            dataType: "json",
            success: function (response) {
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求发送邮箱验证码失败，请稍后重试");
            }
        })
    });

    /* ============================================= Reset the password ============================================= */
    // Prevent the default submit behavior of the password rest form
    $("#form-reset-password").on("submit", function () {
        return false;
    });

    // Reset password form submit
    $("#form-reset-password").submit(function () {
        // Verify the length of the verify code entered by user
        var $codeInputElement = $("#reset-verify-code");
        var verifyCode = $("#reset-verify-code").val();
        if (verifyCode == null || verifyCode.length != 6) {
            setValidateStatusOfFormItem($codeInputElement, STATUS_ERROR, "邮箱验证码长度不正确，请重新输入");
            return false;
        } else {
            setValidateStatusOfFormItem($codeInputElement, STATUS_SUCCESS, "");
        }

        // Verify the password length before from submit
        var $passwordElement = $("#reset-password");
        var password = $passwordElement.val();
        if (password == null || password.length < 6) {
            setValidateStatusOfFormItem($passwordElement, STATUS_ERROR, "新密码长度不小于 6 位，请重新输入");
            return false;
        } else {
            setValidateStatusOfFormItem($passwordElement, STATUS_SUCCESS, "");
        }

        // Judge whether the obtain verify code button is clicked
        if (!isObtainVerifyCodeButtonClicked) {
            setValidateStatusOfFormItem($codeInputElement, STATUS_ERROR, "请先点击【获取】按钮获取验证码")
            return false;
        }

        // Send an request to the server for update user password
        $.ajax({
            url: contextPath + "reset.do",
            type: "post",
            data: "_method=put&" + $("#form-reset-password").serialize(),
            dataType: "json",
            success: function (response) {
                showNoticeModal(response.code, response.msg);
                if (CODE_SUCCESS == response.code) {
                    // Go the the login page util time out
                    let countingTime = 3;
                    let timer = setInterval(function () {
                        if (countingTime <= 0) {
                            location.href = contextPath;
                            clearInterval(timer);
                        }
                        countingTime--;
                    }, 1000)
                }
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求重置密码失败，请稍后重试");
            }
        });
    });
});
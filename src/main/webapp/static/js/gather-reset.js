$(function () {
    /* =========================================== Get email verify code ============================================ */
    // Validation type
    var STATUS_SUCCESS = "success";
    var STATUS_WARNING = "warning"
    var STATUS_ERROR = "error";
    var isObtainButtonClicked = false;

    // Set the validation status of the form item and show the validation message
    function setFormItemValidateStatus(element, status, msg) {
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

    // Content of username change event
    $("#reset-username").change(function () {
        var $usernameElement = $("#reset-username");
        var username = $usernameElement.val();
        if (username == null || username.length <= 0) {
            setFormItemValidateStatus($usernameElement, STATUS_ERROR, "用户名不能为空，请重新输入");
        } else {
            setFormItemValidateStatus($usernameElement, STATUS_SUCCESS, "");
        }
    });

    // Content of email change event
    $("#reset-email").change(function () {
        var $emailElement = $("#reset-email");
        var email = $emailElement.val();
        if (email == null || email.length <= 0) {
            setFormItemValidateStatus($emailElement, STATUS_ERROR, "邮箱不能为空，请重新输入");
            return;
        } else {
            setFormItemValidateStatus($emailElement, STATUS_SUCCESS, "");
        }
    });

    // Get email verify code click event
    $("#btn-obtain-verify-code").click(function () {
        var $usernameElement = $("#reset-username");
        var $emailElement = $("#reset-email");

        var username = $usernameElement.val();
        var email = $emailElement.val();

        // Username and email address can not be empty when try to get the email verify code
        if (username == null || username.length <= 0) {
            setFormItemValidateStatus($usernameElement, STATUS_ERROR, "用户名不能为空，请重新输入");
            return;
        }
        if (email == null || email.length <= 0) {
            setFormItemValidateStatus($emailElement, STATUS_ERROR, "邮箱不能为空，请重新输入");
            return;
        }

        // Verify the format of the email address
        var emailRegexp = new RegExp("^([a-z0-9_-]+)@([\\da-z-]+)\\.([a-z]{2,6})$");
        if (!emailRegexp.test(email)) {
            setFormItemValidateStatus($emailElement, STATUS_ERROR, "邮箱地址格式不正确，请重新输入");
            return;
        }

        // Block the obtain code button until time out
        var $getCodeButton = $(this);
        $getCodeButton.attr("disabled", 'disabled');
        isObtainButtonClicked = true;
        let countingTime = 120;
        let timer = setInterval(function () {
            $getCodeButton.val(countingTime);
            if (countingTime <= 0) {
                $getCodeButton.val("获取");
                $getCodeButton.attr("disabled", false);
                isObtainButtonClicked = false;
                clearInterval(timer);
            }
            countingTime--;
        }, 1000)

        // Send an request to the server for email verify code
        $.ajax({
            url: contextPath + "email.do",
            type: "post",
            data: "username=" + username + "&email=" + email,
            dataType: "json",
            success: function (response) {
                if (CODE_WARN == response.code) {
                    setFormItemValidateStatus($emailElement, STATUS_ERROR, response.msg);
                } else if (CODE_ERROR == response.code) {
                    setFormItemValidateStatus($usernameElement, STATUS_ERROR, response.msg);
                    setFormItemValidateStatus($emailElement, STATUS_ERROR, response.msg);
                }
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求发送邮箱验证码失败，请稍后重试");
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
        var verifyCode = $("#reset-verify-code").val();
        if (verifyCode == null || verifyCode.length != 6) {
            showNoticeModal(CODE_WARN, "邮箱验证码长度不正确，请重新输入");
            return false;
        }

        // Verify the password length before from submit
        var $passwordElement = $("#reset-password");
        var password = $passwordElement.val();
        if (password == null || password.length < 6) {
            setFormItemValidateStatus($passwordElement, STATUS_ERROR, "新密码长度不小于 6 位，请重新输入");
            return false;
        } else {
            setFormItemValidateStatus($passwordElement, STATUS_SUCCESS, "");
        }

        // Judge whether the obtain verify code button is clicked
        if (!isObtainButtonClicked) {
            showNoticeModal(CODE_WARN, "请先点击【获取】按钮获取验证码");
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
                            location.href = contextPath + "static/html/login.html";
                            clearInterval(timer);
                        }
                        countingTime--;
                    }, 1000)
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求重置密码失败，请稍后重试");
            }
        });
    });
});
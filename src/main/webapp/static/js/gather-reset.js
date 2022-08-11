$(function () {
    /*
     * =================================================================================================================
     * Form items validation
     * =================================================================================================================
     */
    // Validation type
    var statusSuccess = "success";
    var statusWarning = "warning"
    var statusError = "error";

    // Set the validation status of the form item and show the validation message
    function setFormItemValidateStatus(element, status, msg) {
        // Reset the existing style and clear the notice msg
        element.parent().removeClass("has-success has-error has-warning");
        element.next("span").removeClass("glyphicon-ok glyphicon-remove glyphicon-warning-sign");
        $(".error-msg").text("");

        // Give the item different style according to the validation status
        if (statusSuccess === status) {
            element.parent().addClass("has-success");
            element.next("span").addClass("glyphicon-ok");
        } else if (statusError === status) {
            element.parent().addClass("has-error");
            element.next("span").addClass("glyphicon-remove");
        } else if (statusWarning === status) {
            element.parent().addClass("has-warning");
            element.next("span").addClass("glyphicon-warning-sign");
        }

        // Show the validation message
        $(".error-msg").text(msg);
    }

    // Content of username change event
    $("#reset-username").change(function () {
        var $username = $("#reset-username");
        var username = $username.val();
        if (username == null || username.length <= 0) {
            setFormItemValidateStatus($username, statusWarning, "用户名不能为空，请重新输入");
        } else {
            setFormItemValidateStatus($username, statusSuccess, "");
        }
    });

    // Content of email change event
    $("#reset-email").change(function () {
        var $email = $("#reset-email");
        var email = $email.val();
        if (email == null || email.length <= 0) {
            setFormItemValidateStatus($email, statusWarning, "邮箱不能为空，请重新输入");
            return;
        } else {
            setFormItemValidateStatus($email, statusSuccess, "");
        }
    });

    /*
     * =================================================================================================================
     * Get email verify code
     * =================================================================================================================
     */
    var isObtainButtonClicked = false;

    // Get email verify code click event
    $("#btn-obtain-verify-code").click(function () {
        var $username = $("#reset-username");
        var $email = $("#reset-email");
        var username = $username.val();
        var email = $email.val();

        // Username and email address can not be empty when try to get the email verify code
        if (username == null || username.length <= 0) {
            setFormItemValidateStatus($username, statusWarning, "用户名不能为空，请重新输入");
            return;
        }
        if (email == null || email.length <= 0) {
            setFormItemValidateStatus($email, statusWarning, "邮箱不能为空，请重新输入");
            return;
        }

        // Verify the format of the email address
        var emailRegexp = new RegExp("^([a-z0-9_-]+)@([\\da-z-]+)\\.([a-z]{2,6})$");
        if (!emailRegexp.test(email)) {
            setFormItemValidateStatus($email, statusWarning, "邮箱格式不正确，请重新输入");
            return;
        }

        var $btn = $(this);
        $btn.attr("disabled", 'disabled');
        isObtainButtonClicked = true;
        // Block the obtain code button until time out
        let countingTime = 120;
        let timer = setInterval(function () {
            $btn.val(countingTime);
            if (countingTime <= 0) {
                $btn.val("获取");
                $btn.attr("disabled", false);
                isObtainButtonClicked = false;
                clearInterval(timer);
            }
            countingTime--;
        }, 1000)

        // Send an request to server for email verify code
        $.ajax({
            url: contextPath + "email.do",
            type: "post",
            data: "username=" + username + "&email=" + email,
            dataType: "json",
            success: function (response) {
                if (CODE_ERROR == response.code) {
                    setFormItemValidateStatus($username, statusError, response.msg);
                    setFormItemValidateStatus($email, statusError, response.msg);
                }
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求邮箱验证码失败，请稍后重试");
            }
        })
    });

    /*
     * =================================================================================================================
     * Reset the password
     * =================================================================================================================
     */
    // Prevent the default submit behavior of the password rest form
    $("#form-reset-password").on("submit", function () {
        return false;
    });

    // Reset password form submit
    $("#form-reset-password").submit(function () {
        // Verify the length of the verify code entered by user
        var $code = $("#reset-verify-code");
        var verifyCode = $code.val();
        if (verifyCode == null || verifyCode.length != VERIFY_CODE_LEN) {
            setFormItemValidateStatus($code, statusWarning, "验证码长度不正确，请重新输入");
            return false;
        }

        // Verify the password length before from submit
        var $password = $("#reset-password");
        var password = $password.val();
        if (password == null || password.length < 6) {
            setFormItemValidateStatus($password, statusWarning, "新密码长度不小于 6 位，请重新输入");
            return false;
        } else {
            setFormItemValidateStatus($password, statusSuccess, "");
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
                // Go to login page after 3s
                if (CODE_SUCCESS == response.code) {
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
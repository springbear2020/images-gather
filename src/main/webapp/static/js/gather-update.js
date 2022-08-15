$(function () {
    /*
     * =================================================================================================================
     * Update user login password
     * =================================================================================================================
     */

    // Reset update password modal content
    function clearContentOfUpdatePassword() {
        $("#input-old-password").val("");
        $("#input-new-password").val("");
        $("#input-new-password-again").val("");
    }

    // Open modal
    $(".link-update-password").click(function () {
        $("#modal-update-password").modal({
            backdrop: "static"
        })
    });

    // Close modal
    $(".modal-update-close").click(function () {
        clearContentOfUpdatePassword();
    });

    // Prevent the default submit behavior of the form
    $("#form-update-password").on("submit", function () {
        return false;
    });

    // Form submit
    $("#form-update-password").submit(function () {
        var $errorMsg = $(".error-msg");
        // Verify the password entered in two times
        var newPassword = $("#input-new-password").val();
        var newPasswordAgain = $("#input-new-password-again").val();
        if (newPassword !== newPasswordAgain) {
            $errorMsg.text("两次输入的新密码不一致，请检查后重新输入");
            return false;
        }

        // Length too short of the new password
        if (newPassword.length < 6) {
            $errorMsg.text("新密码长度不小于 6 位，请重新输入");
            return false;
        }

        // Whether the old password id equals to the new password
        var oldPassword = $("#input-old-password").val();
        if (oldPassword === newPassword) {
            $errorMsg.text("新旧密码一致，请重新输入新密码");
            return false;
        } else {
            $errorMsg.text("");
        }

        // Encrypt the password
        oldPassword = hex_md5(oldPassword);
        oldPassword = oldPassword.split('').reverse().join('');
        oldPassword = hex_md5(oldPassword);
        newPassword = hex_md5(newPassword);
        newPassword = newPassword.split('').reverse().join('');
        newPassword = hex_md5(newPassword);
        // Send an ajax request to ask server for updating the password of user
        $.ajax({
            url: contextPath + "update.do",
            method: "POST",
            data: "_method=PUT&oldPassword=" + oldPassword + "&newPassword=" + newPassword,
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
                showNoticeModal(CODE_WARN, "请求修改密码失败，请稍后重试");
            }
        });
    });
});
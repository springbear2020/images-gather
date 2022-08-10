$(function () {
    /* =============================================== Update password ============================================== */

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
            showNoticeModal(CODE_WARN, "新密码长度小于 6 位，请重新输入");
            return false;
        }

        // Whether the old password id equals to the new password
        var oldPassword = $("#input-old-password").val();
        if (oldPassword === newPassword) {
            showNoticeModal(CODE_WARN, "新旧密码一致，请重新输入");
            return false;
        }

        // Send an ajax request to ask server for updating the password of user
        $.ajax({
            url: contextPath + "user.do",
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
                showNoticeModal(CODE_WARN, "请求修改密码失败，请稍后重试");
            }
        });
    });
});
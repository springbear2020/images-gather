$(function () {
    /* ============================================================================================================== */
    // 工程上下文路径
    var contextPath = $("#span-context-path").text();
    // 与服务器约定的响应码
    var SUCCESS_CODE = 0;
    var ERROR_CODE = 1;
    var INFO_CODE = 2;
    var WARNING_CODE = 3;

    // 阻止表单的提交跳转页面问题
    $("form").on("submit", function () {
        return false;
    });

    // 提示信息
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // 清除原有样式
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        // 根据不同的响应码添加不同的样式
        if (INFO_CODE === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (SUCCESS_CODE === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (WARNING_CODE === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (ERROR_CODE === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        // 设置通知的内容
        $noticeContent.text(msg);
        // 显示
        $("#div-notice-modal").modal('show');
    };

    /* =================================================== 修改密码 ================================================== */
    // 显示密码修改模态框
    $(".link-update-password").click(function () {
        // 除非点击 x 关闭按钮，否则一直
        $("#modal-update-password").modal({
            backdrop: "static"
        })
    });

    // 重置修改密码模态框中的内容
    function resetFormItem() {
        $("#input-old-password").val("");
        $("#input-new-password").val("");
        $("#input-new-password-again").val("");
    }

    // 密码修改模态框关闭按钮点击事件
    $(".modal-update-close").click(function () {
        resetFormItem();
    });

    // 确认按钮
    $("#btn-update-password").click(function () {
        var oldPassword = $("#input-old-password").val();
        var newPassword = $("#input-new-password").val();
        var newPasswordAgain = $("#input-new-password-again").val();

        // 原密码、新密码不允许为空
        if (oldPassword.length == 0) {
            showNoticeModal(WARNING_CODE, "请输入原密码");
            return false;
        }
        if (newPassword.length == 0) {
            showNoticeModal(WARNING_CODE, "请输入新密码");
            return false;
        }
        if (newPasswordAgain.length == 0) {
            showNoticeModal(WARNING_CODE, "请确认新密码");
            return false;
        }

        if (newPassword !== newPasswordAgain) {
            showNoticeModal(WARNING_CODE, "两次输入的新密码不一致");
            return false;
        }

        // 请求服务器修改用户密码
        $.ajax({
            url: contextPath + "user",
            method: "POST",
            data: "_method=PUT&" + $("#form-update-password").serialize(),
            dataType: "json",
            success: function (response) {
                if (SUCCESS_CODE == response.code) {
                    // 密码修改成功，关闭模态框，重置内容
                    $("#modal-update-password").modal('hide');
                    resetFormItem();
                    // 显示来自服务器的密码修改成功信息
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
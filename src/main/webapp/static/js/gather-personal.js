$(function () {
    /* =================================================== Commons ================================================== */
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
    // Context path
    var contextPath = $("#context-path").val();

    /* ============================================ Update personal info ============================================ */
    $("#edit-personal-info").click(function () {
        // Open the edit element, including sex, phone and email
        $("#personal-sex-man").attr("disabled", false);
        $("#personal-sex-woman").attr("disabled", false);
        $("#personal-phone").attr("disabled", false);
        $("#personal-email").attr("disabled", false);

        var originSex = $("input[name='sex']:checked").val();
        var originPhone = $("#personal-phone").val();
        var originEmail = $("#personal-email").val();

        // Let the edit button disappear and add a new save button to save the changes by the user
        function disappearEditAddSaveButton() {
            var $ancestor = $("#edit-save-info");
            $ancestor.empty();
            $("<a></a>").append("保存").attr("role", "button").addClass("btn btn-success").attr("id", "save-personal-info").appendTo($("<li></li>").appendTo($ancestor));

            // Save the changes
            $("#save-personal-info").click(function () {
                var newSex = $("input[name='sex']:checked").val();
                var newPhone = $("#personal-phone").val();
                var newEmail = $("#personal-email").val();

                // Judge whether the information have changed
                if (originSex == newSex && originPhone == newPhone && originEmail == newEmail) {
                    showNoticeModal(CODE_WARN, "信息未发生变化，无需保存");
                    return;
                }

                // Verify the format of the new phone
                var phoneRegexp = /^1[3-9]\d{9}$/;
                if (!phoneRegexp.test(newPhone)) {
                    showNoticeModal(CODE_WARN, "手机号格式不正确，请重新输入");
                    return;
                }

                // Verify the format of the new email
                var emailRegexp = new RegExp("^([a-z0-9_-]+)@([\\da-z-]+)\\.([a-z]{2,6})$");
                if (!emailRegexp.test(newEmail)) {
                    showNoticeModal(CODE_WARN, "邮箱地址格式不正确，请重新输入");
                    return;
                }

                // Ask the server to update the user's info
                $.ajax({
                    url: contextPath + "update/personal",
                    type: "post",
                    data: "_method=put&newSex=" + newSex + "&newPhone=" + newPhone + "&newEmail=" + newEmail,
                    dataType: "json",
                    success: function (response) {
                        showNoticeModal(response.code, response.msg);
                        if (CODE_SUCCESS == response.code) {
                            // Go the the login page util time out
                            let countingTime = 3;
                            let timer = setInterval(function () {
                                if (countingTime <= 0) {
                                    location.href = contextPath + "personal.html";
                                    clearInterval(timer);
                                }
                                countingTime--;
                            }, 1000)
                        }
                    },
                    error: function () {
                        showNoticeModal(CODE_ERROR, "请求更新个人信息失败，请稍后重试");
                    }
                })
            });
        }

        // Listening the content change of the sex ration button
        $("input:radio[name='sex']").change(function () {
            disappearEditAddSaveButton();
        });
        // Listening the content change of the phone input
        $("#personal-phone").change(function () {
            disappearEditAddSaveButton();
        });
        // Listening the content email of the phone input
        $("#personal-email").change(function () {
            disappearEditAddSaveButton();
        });
    });
});
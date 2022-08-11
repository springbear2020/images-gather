$(function () {
    /*
     * =================================================================================================================
     * Sign out of the website
     * =================================================================================================================
     */
    $(".link-logout").click(function () {
        $.ajax({
            url: contextPath + "logout.do",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    window.location.href = contextPath + "static/html/login.html";
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求注销登录失败，请稍后重试");
            }
        });
    });
});
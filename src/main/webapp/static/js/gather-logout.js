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
                window.location.href = contextPath;
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求注销登录失败，请稍后重试");
            }
        });
    });
});
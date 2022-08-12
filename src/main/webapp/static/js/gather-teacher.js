$(function () {
    /*
     * =================================================================================================================
     * User information with people and the latest login log data
     * =================================================================================================================
     */

    var USER_TYPE = -1;

    // Set the latest login data of the teacher
    function setLatestLoginData(loginLog) {
        $(".login-location").text(loginLog.location);
        $(".login-ip").text("IP：" + loginLog.ip);
        $(".login-datetime").text(loginLog.loginDatetime);
    }

    $.ajax({
        url: contextPath + "people.do",
        method: "get",
        dataType: "json",
        async: false,
        success: function (response) {
            if (CODE_SUCCESS == response.code) {
                // Type of user
                USER_TYPE = response.resultMap.item.userType;
                // Welcome user
                $(".welcome-teacher").text(response.resultMap.item.people.name);
                // Latest login data
                setLatestLoginData(response.resultMap.loginLog);
            }
        },
        error: function () {
            showNoticeModal(CODE_WARN, "请求个人信息失败，请稍后重试");
        }
    });

    /*
    * =================================================================================================================
    * Page dispatcher
    * =================================================================================================================
    */
    pageDispatcher(USER_TYPE);
});
$(function () {
    /* =================================================== Cookie =================================================== */

    // Get the cookie info from the disk file
    function getCookieByKey(key) {
        var cookieArray = document.cookie.split("; ");
        var len = cookieArray.length;
        for (var i = 0; i < len; i++) {
            // key - value
            var val = cookieArray[i].split("=");
            // Return the value of the specified key
            if (val[0] == key) {
                return val[1];
            }
        }
        // The key not exists in the cookie, return the ""
        return "";
    }

    /*
     * Auto fill text for the username and password input element,
     * get the username and password from the cookie,
     * username and password exists in the cookie,
     * set the content of the input text element
     */
    var usernameFromCookie = getCookieByKey("username");
    var passwordFromCookie = getCookieByKey("password");
    if (usernameFromCookie.length > 0 && passwordFromCookie.length > 0) {
        // Check the remember me checkbox
        $("input[type='checkbox']").attr("checked", true);
        // Set the text of the input text element
        $("#inputUsername").val(usernameFromCookie);
        $("#inputPassword").val(passwordFromCookie);
    }

    // Status change event of the readme me element
    $(".remember-me").change(function () {
        var status = this.checked;
        // Not choose remember me, remove the cookies named username and password
        if (!status) {
            document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 GMT;";
            document.cookie = "password=; expires=Thu, 01 Jan 1970 00:00:00 GMT;";
        }
    })

    /* =================================================== Login ==================================================== */

    // Prevent the default submit behavior of the form
    $("form").on("submit", function () {
        return false;
    });

    // User login by username and password
    $(".btn-block").click(function () {
        $.ajax({
            url: contextPath + "login.do",
            method: "get",
            data: $(".form-signin").serialize(),
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    var type = response.resultMap.type;
                    // Student and monitor
                    if (USER_TYPE_STUDENT == type || USER_TYPE_MONITOR == type) {
                        window.location.href = contextPath + "static/html/student.html";
                    } else if (USER_TYPE_TEACHER == type) {
                        window.location.href = contextPath + "static/html/teacher.html"
                    }
                } else {
                    $(".error-msg").text(response.msg);
                    // showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求登录失败，请稍后重试");
            }
        });
    });
});
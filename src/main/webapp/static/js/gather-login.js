$(function () {
    /*
     * =================================================================================================================
     * Auto fill content from the cookies
     * =================================================================================================================
     */
    // Auto fill text for the username and password input element from the cookie if exists
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

    /*
     * =================================================================================================================
     * User login
     * =================================================================================================================
     */
    // Prevent the default submit behavior of the form
    $("form").on("submit", function () {
        return false;
    });

    // User login button click event
    $(".btn-block").click(function () {
        // TODO MD5 password encode
        var password = $("#inputPassword").val();
        var params = "username=" + $("#inputUsername").val() + "&password=" + password;
        var $rememberMe = $(".remember-me");
        if ($rememberMe.is(':checked')) {
            params = params + "&rememberMe=" + $rememberMe.val();
        }
        $.ajax({
            url: contextPath + "login.do",
            method: "get",
            data: params,
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    var userType = response.resultMap.item;
                    // Student and monitor
                    if (USER_TYPE_STUDENT == userType || USER_TYPE_MONITOR == userType) {
                        window.location.href = contextPath + "static/html/student.html";
                    } else if (USER_TYPE_HEAD_TEACHER == userType || USER_TYPE_GRADE_TEACHER == userType) {
                        window.location.href = contextPath + "static/html/teacher.html";
                    } else if (USER_TYPE_ADMIN == userType) {
                        window.location.href = contextPath + "static/html/admin.html";
                    }
                } else {
                    $(".error-msg").text(response.msg);
                }
            },
            error: function () {
                $(".error-msg").text("请求登录失败，请稍后重试");
            }
        });
    });
});
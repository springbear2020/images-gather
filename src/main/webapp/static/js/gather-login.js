$(function () {
    /*
     * =================================================================================================================
     * Auto fill username and password from cookie if exists
     * =================================================================================================================
     */
    // Auto fill text for the username and password input element from the cookie if exists
    var usernameFromCookie = getCookieByKey("username");
    var passwordFromCookie = getCookieByKey("password");
    if (usernameFromCookie.length > 0 && passwordFromCookie.length > 0) {
        // Check the remember me checkbox and auto fill text
        $("input[type='checkbox']").attr("checked", true);
        $("#inputUsername").val(usernameFromCookie);
        $("#inputPassword").val(passwordFromCookie);

        // TODO Auto login after 1 minute later
        // let countingTime = 1;
        // let timer = setInterval(function () {
        //     if (countingTime <= 0) {
        //         login(usernameFromCookie, passwordFromCookie, true);
        //         clearInterval(timer);
        //     }
        //     countingTime--;
        // }, 1000)
    }

    // Status change event of the readme me element
    $(".remember-me").change(function () {
        var status = this.checked;
        // Not choose remember me, remove the cookies named username and password
        if (!status) {
            usernameFromCookie = "";
            passwordFromCookie = "";
            $("#inputUsername").val("");
            $("#inputPassword").val("");
            document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";
            document.cookie = "password=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";
        }
    });

    // Don't remember if the username and password changed
    $("#inputUsername").change(function () {
        $("input[type='checkbox']").attr("checked", false);
        usernameFromCookie = "";
        passwordFromCookie = "";
        document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";
        document.cookie = "password=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";

    });
    $("#inputPassword").change(function () {
        $("input[type='checkbox']").attr("checked", false);
        usernameFromCookie = "";
        passwordFromCookie = "";
        document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";
        document.cookie = "password=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/static/html/login.html";

    });

    /*
     * =================================================================================================================
     * User login
     * =================================================================================================================
     */
    // Prevent the default submit behavior of the form
    $("form").on("submit", function () {
        return false;
    });

    // User login
    function login(username, password, rememberMe) {
        $.ajax({
            url: contextPath + "login.do",
            method: "get",
            data: "username=" + username + "&password=" + password + "&rememberMe=" + rememberMe,
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
    }

    // User login button click event
    $(".btn-block").click(function () {
        var username = $("#inputUsername").val();
        var password = $("#inputPassword").val();
        var rememberMe = false;
        if ($(".remember-me").is(':checked')) {
            rememberMe = true;
        }
        // Don't encrypt the password saved in the cookie
        if (!(usernameFromCookie.length > 0 && passwordFromCookie.length > 0)) {
            // Encrypt the password entered by user
            password = hex_md5(password);
            password = password.split('').reverse().join('');
            password = hex_md5(password);
        }
        if (username.length > 0 && password.length > 0) {
            login(username, password, rememberMe);
        }
    });
});
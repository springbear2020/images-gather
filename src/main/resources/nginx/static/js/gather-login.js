$(function () {
    // Get the cookie info from the disk file
    function getCookieByKey(key) {
        var cookieArray = document.cookie.split("; ");
        var len = cookieArray.length;
        for (var i = 0; i < len; i++) {
            // key - value
            var keyValue = cookieArray[i].split("=");
            // Return the value of the specified key
            if (keyValue[0] == key) {
                return keyValue[1];
            }
        }
        // The key not exists in the cookie, return the ""
        return "";
    }

    // Get the username and password from the cookie
    var username = getCookieByKey("username");
    var password = getCookieByKey("password");

    // Username and password exists in the cookie, set the content of the input text element
    if (username.length > 0 && password.length > 0) {
        // Check the remember me checkbox
        $("input[type='checkbox']").attr("checked", true);
        // Set the text of the input text element
        $("#inputUsername").val(username);
        $("#inputPassword").val(password);
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
});
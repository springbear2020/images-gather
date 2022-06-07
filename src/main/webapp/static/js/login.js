$(function () {
    // 获取 Cookie
    function getCookie(key) {
        var cookieArray = document.cookie.split("; ");
        // 遍历 Cookie
        var len = cookieArray.length;
        for (var i = 0; i < len; i++) {
            // key - value
            var keyValue = cookieArray[i].split("=");
            // 返回 key 对应的 value
            if (keyValue[0] == key) {
                return keyValue[1];
            }
        }
        // 不存在对应的 cookie 信息
        return "";
    }

    // 获取复选框、Cookie 中的用户名和密码信息
    var $checkbox = $("input[type='checkbox']");
    var username = getCookie("username");
    var password = getCookie("password");

    // Cookie 中存在用户名和密码，则选中【记住我】
    if (username.length > 0 && password.length > 0) {
        $checkbox.attr("checked", true);
        $("#inputUsername").val(username);
        $("#inputPassword").val(password);
    }
});
$(function () {
    /* ============================================ Back to index page ============================================== */

    // Go to the different page according to the user type
    function toIndexPage(userType) {
        if ("1" == userType) {
            // Student home page
            $(".navbar-brand").attr("href", contextPath + "static/html/student.html");
        } else if ("2" == userType) {
            // Teacher home page
            $(".navbar-brand").attr("href", contextPath + "static/html/teacher.html");
        }
    }

    toIndexPage(getUrlParam("type"));
});
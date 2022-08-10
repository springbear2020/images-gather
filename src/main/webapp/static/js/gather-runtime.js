$(function () {
    <!-- Set running time of this website -->
    function showRunningTime() {
        window.setTimeout("showRunningTime()", 1000);
        X = new Date("06/02/2022 00:00:00");
        Y = new Date();
        T = (Y.getTime() - X.getTime());
        M = 24 * 60 * 60 * 1000;
        a = T / M;
        A = Math.floor(a);
        b = (a - A) * 24;
        B = Math.floor(b);
        c = (b - B) * 60;
        C = Math.floor((b - B) * 60);
        D = Math.floor((c - C) * 60);
        runtime.innerHTML = A + " 天 " + B + " 时 " + C + " 分 " + D + " 秒"
    }

    showRunningTime();

    var $copyrightElement = $('.copyright');
    $copyrightElement.css('opacity', '0');
    // Display
    $copyrightElement.mouseover(function () {
        $copyrightElement.css('opacity', '1');
    });
    // Hide
    $copyrightElement.mouseleave(function () {
        $copyrightElement.css('opacity', '0');
    });
});
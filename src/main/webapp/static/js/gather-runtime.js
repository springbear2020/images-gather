window.onload = function () {
    /*
     * =================================================================================================================
     * Set running time of the website
     * =================================================================================================================
     */
    function showRunningTime(startTimeStr) {
        var datetimeNow = new Date();
        var startDatetime = new Date(startTimeStr);
        var datetimeSpan = (datetimeNow.getTime() - startDatetime.getTime());
        var daySeconds = 24 * 60 * 60 * 1000;

        // days
        var days = datetimeSpan / daySeconds;
        var realDays = Math.floor(days);
        // realHours
        var hours = (days - realDays) * 24;
        var realHours = Math.floor(hours);
        // realMinutes
        var minutes = (hours - realHours) * 60;
        var realMinutes = Math.floor(minutes);
        // seconds
        var seconds = (minutes - realMinutes) * 60;
        var realSeconds = Math.floor(seconds);

        // Set runtime for the element
        var runtimeElement = document.getElementById("runtime");
        runtimeElement.innerHTML = realDays + " 天 " + realHours + " 时 " + realMinutes + " 分 " + realSeconds + " 秒"
    }

    setInterval(function () {
        showRunningTime("06/02/2022 00:00:00");
    }, 1000)

    /*
     * =================================================================================================================
     * Runtime display or hide
     * =================================================================================================================
     */
    // Display
    var $copyrightElement = $(".copyright");
    $copyrightElement.mouseover(function () {
        $copyrightElement.css('opacity', '1');
    });
    // Hide
    $copyrightElement.mouseleave(function () {
        $copyrightElement.css('opacity', '0');
    });
};
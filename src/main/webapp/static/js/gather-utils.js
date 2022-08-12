/*
 * =====================================================================================================================
 * Methods
 * =====================================================================================================================
 */

// Get value from the url address
function getUrlParam(key) {
    var url = document.location.toString();
    var array = url.split("?");
    if (array.length > 1) {
        // key=val
        var keyValArray = array[1].split("&");
        for (var i = 0; i < keyValArray.length; i++) {
            var curKeyVal = keyValArray[i].split("=");
            // Find the key given by user then return the val about it
            if (curKeyVal != null && curKeyVal[0] == key) {
                return curKeyVal[1];
            }
        }
        return "";
    }
    return "";
}

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
    // Not find then return ""
    return "";
}

// Parse the JavaScript Date like this format '2022-07-06' 
function parseDate(date) {
    var nowMonth = date.getMonth() + 1;
    var dateStr = date.getDate();
    var separator = "-";
    if (nowMonth >= 1 && nowMonth <= 9) {
        nowMonth = "0" + nowMonth;
    }
    if (dateStr >= 0 && dateStr <= 9) {
        dateStr = "0" + dateStr;
    }
    return date.getFullYear() + separator + nowMonth + separator + dateStr;
}

// Generate now date back to the specified days before, return the date array descending
function goBackDays(date, dayNums) {
    var dateStrArr = []
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()

    // Generate the specified days
    for (var i = 0; i < dayNums; i++) {
        if (i > 0) {
            // The day before today
            day--;
            if (day <= 0) {
                month--;
                // 31 days at this month number
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    day = 31;
                }
                // 30 days at this month number
                if (month == 4 || month == 6 || month == 9 || month == 11) {
                    day = 30;
                }
                // Determining whether it is a leap year
                if (month == 2) {
                    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                }

                // The month before this month
                if (month <= 0) {
                    year--;
                    month = 12;
                }
            }
        }

        // year-month-day like this '2022-08-11'
        dateStrArr[i] = year + '-';
        if (month < 10) {
            dateStrArr[i] += '0';
        }
        dateStrArr[i] += month + '-';
        if (day < 10) {
            dateStrArr[i] += '0';
        }
        dateStrArr[i] += day;
    }
    return dateStrArr;
}
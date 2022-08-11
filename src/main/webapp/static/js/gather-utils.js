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
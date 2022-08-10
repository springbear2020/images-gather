$(function () {
    /* ================================================= Pagination ================================================= */
    var loginLogDataType = 1;
    var uploadDataType = 2;

    // Build the pagination
    function buildPagination(pageData, whoClickedMe) {
        // Parent: login data display container
        var $parentElement = $(".data-table-container");

        // Navigation
        var $nav = $("<nav></nav>").attr("aria-label", "Page navigation").addClass("text-center").appendTo($parentElement);
        var $ul = $("<ul></ul>").addClass("pagination").appendTo($nav);
        var curPageNum = pageData.pageNum;

        // Not the first page, build the previous page element
        if (!pageData.isFirstPage) {
            var $previousPageLi = $("<li></li>").appendTo($ul);
            var $previousPageLink = $("<a></a>").addClass("previous-page").attr("role", "button").attr("aria-label", "Previous").appendTo($previousPageLi);
            $("<span></span>").attr("aria-hidden", "true").append("&laquo;").appendTo($previousPageLink);
        }

        // Build the pagination numbers
        var pageNumArray = pageData.navigatepageNums;
        $.each(pageNumArray, function (index, val) {
            var $li;
            // Current page num add the active style, prevent click it and give it a highlight style
            if (val == curPageNum) {
                $li = $("<li></li>").addClass("active").appendTo($ul);
            } else {
                $li = $("<li></li>").appendTo($ul);
            }
            // Give the page number a click event except the current page number
            var $pageTurnLink = $("<a></a>").attr("role", "button").append(val).appendTo($li);
            if (val != curPageNum) {
                $pageTurnLink.addClass("page-turn");
            }
        });

        // Not the last page, build the next page element
        if (!pageData.isLastPage) {
            var $nextPageLi = $("<li></li>").appendTo($ul);
            var $nextPageLink = $("<a></a>").attr("role", "button").addClass("next-page").attr("aria-label", "Next").appendTo($nextPageLi);
            $("<span></span>").attr("aria-hidden", "true").append("&raquo;").appendTo($nextPageLink);
        }

        // Previous page click event
        $(".previous-page").click(function () {
            var pageNum = curPageNum - 1;
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });

        // Next page click event
        $(".next-page").click(function () {
            var pageNum = curPageNum + 1;
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });

        // Go to specified page click event
        $(".page-turn").click(function () {
            var pageNum = $(this).text();
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });
    }

    /* ================================================== Login log ================================================= */

    // Get the user's login log page data from the server
    function getLoginLogPageData(pageNum) {
        $.ajax({
            url: contextPath + "record/login.do",
            type: "get",
            data: "pageNum=" + pageNum,
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    buildLoginLogDateTable(response);
                    buildPagination(response.resultMap.pageData, loginLogDataType);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求个人登录记录失败，请稍后重试");
            }
        });
    }

    // Build the user's login record page data display table
    var buildLoginLogDateTable = function (response) {
        // Parent: login data display container
        var $loginDataTable = $(".data-table-container");
        $loginDataTable.empty();

        var loginLogList = response.resultMap.pageData.list;
        // Head title of the page
        $("<h2></h2>").append("登录记录").addClass("sub-header").appendTo($loginDataTable);
        // Parent of table
        var $tableParent = $("<div></div>").addClass("table-responsive").appendTo($loginDataTable);
        var $table = $("<table></table>").addClass("table table-hover").appendTo($tableParent);
        // Table thead and tbody
        var $thead = $("<thead></thead>").appendTo($table);
        var $tbody = $("<tbody></tbody>").appendTo($table);
        var $trOfThead = $("<tr></tr>").appendTo($thead);
        $("<th></th>").append("序号").appendTo($trOfThead);
        $("<th></th>").append("时间").appendTo($trOfThead);
        $("<th></th>").append("IP").appendTo($trOfThead);
        $("<th></th>").append("地点").appendTo($trOfThead);
        // Go though the record list in loop to display the data
        $.each(loginLogList, function (index, data) {
            var $trOfBody = $("<tr></tr>").appendTo($tbody);
            var pageSize = response.resultMap.pageData.pageSize;
            var pageNum = response.resultMap.pageData.pageNum;
            var num = (pageNum - 1) * pageSize + index + 1;
            $("<td></td>").append(num).appendTo($trOfBody);
            $("<td></td>").append(data.loginDatetime).appendTo($trOfBody);
            $("<td></td>").append(data.ip).appendTo($trOfBody);
            $("<td></td>").append(data.location).appendTo($trOfBody);
        });
    };

    /* ================================================== Upload history ================================================= */

    // Get the user's upload record history page data from the server
    function getUploadHistoryPageData(pageNum) {
        $.ajax({
            url: contextPath + "record/upload.do",
            type: "get",
            data: "pageNum=" + pageNum,
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    buildUploadDateTable(response);
                    buildPagination(response.resultMap.pageData, uploadDataType);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求个人上传记录失败，请稍后重试");
            }
        });
    }

    // Build the user's upload record page data display table
    var buildUploadDateTable = function (response) {
        // Parent: data display container
        var $dataTable = $(".data-table-container");
        $dataTable.empty();

        var uploadList = response.resultMap.pageData.list;
        // Head title of the page
        $("<h2></h2>").append("上传记录").addClass("sub-header").appendTo($dataTable);
        // Traverser the images have uploaded list
        $.each(uploadList, function (index, upload) {
            // Health image access url
            var localHealthUrl = upload.localHealthUrl;
            var cloudHealthUrl = upload.cloudHealthUrl;
            var healthAccessUrl = cloudHealthUrl.length == 0 ? contextPath + localHealthUrl : cloudHealthUrl;
            healthAccessUrl = healthAccessUrl == contextPath ? invalidImageUrl : healthAccessUrl;
            // Schedule image access url
            var localScheduleUrl = upload.localScheduleUrl;
            var cloudScheduleUrl = upload.cloudScheduleUrl;
            var scheduleAccessUrl = cloudScheduleUrl.length == 0 ? contextPath + localScheduleUrl : cloudScheduleUrl;
            scheduleAccessUrl = scheduleAccessUrl == contextPath ? invalidImageUrl : scheduleAccessUrl;
            // Closed image access url
            var localClosedUrl = upload.localClosedUrl;
            var cloudClosedUrl = upload.cloudClosedUrl;
            var closedAccessUrl = cloudClosedUrl.length == 0 ? contextPath + localClosedUrl : cloudClosedUrl;
            closedAccessUrl = closedAccessUrl == contextPath ? invalidImageUrl : closedAccessUrl;

            // Student name
            $("<h3></h3>").addClass("title-real-name").append(upload.uploadDatetime).appendTo($dataTable);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", healthAccessUrl).attr("alt", "健康码图片已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", scheduleAccessUrl).attr("alt", "行程码图片已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", closedAccessUrl).attr("alt", "密接查图片已失效").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($dataTable);
        });
    };

    /* ============================================= Record type change ============================================= */
    // Record change click event
    $(".personal-record").click(function () {
        $(".personal-record-li").removeClass("active");
        $(this).parent().addClass("active");
        var $recordType = $(this).text();
        if ("登录记录" === $recordType) {
            getLoginLogPageData(1);
        } else if ("上传记录" === $recordType) {
            getUploadHistoryPageData(1);
        }
    });

    // Display different data according to the user type
    var userType = getUrlParam("type");
    if ("1" == userType || "0" == userType) {
        getUploadHistoryPageData(1);
    } else {
        $(".personal-record-li").attr("style", "display: none;opacity 100%");
        getLoginLogPageData(1);
    }
});
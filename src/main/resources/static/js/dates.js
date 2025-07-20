$(function () {

    //----------------- variables ----------------------------
    // views
    let dayDiv = $("#dayDiv");
    let monthDiv = $("#monthDiv");
    let yearDiv = $("#yearDiv");
    let dateRefreshBtn = $("#dateRefreshBtn");
    let importanceIFrame = $("#importanceIframe");

    let url = "";
    let timeOutId;
    let day;
    let month;
    let year;

    const BY_EXACT_DATE = "ByExactDate";
    const BY_MONTH_AND_DAY = "ByMonth&Day";
    const BY_MONTH_AND_YEAR = "ByMonth&Year";

    let checkedImportanceTypeValue = BY_MONTH_AND_DAY;

    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    // ====================================================

    setRandomDate()

    // ---------------- Event Listeners --------------------------

    dateRefreshBtn.click(function () {

        dateRefreshBtn.animate({
            opacity: '0.5'
        }, "fast", "swing", function () {

            dateRefreshBtn.animate({
                opacity: '1'
            }, "fast");
        });

        setRandomDate();
    });

    $("#byExactDateLabel").click(function () {

        if (checkedImportanceTypeValue !== BY_EXACT_DATE) {

            initUrlAndIFrame(BY_EXACT_DATE);
        }

    });

    $("#byMonthAndDayLabel").click(function () {

        if (checkedImportanceTypeValue !== BY_MONTH_AND_DAY) {

            initUrlAndIFrame(BY_MONTH_AND_DAY);
        }
    });

    $("#byMonthAndYearLabel").click(function () {

        if (checkedImportanceTypeValue !== BY_MONTH_AND_YEAR) {

            initUrlAndIFrame(BY_MONTH_AND_YEAR);
        }
    });

    // =====================================================


    // --------------------- functions ----------------------

    function setRandomDate() {

        try {

            clearTimeout(timeOutId);

        } catch (e) {

        }

        $(".loadingObject").show();
        importanceIFrame.hide();

        let rDate = getRandomDate(new Date(1994, 0, 1), new Date());

        day = rDate.getUTCDate();
        month = monthNames[rDate.getMonth()];
        year = rDate.getUTCFullYear();

        initDivText();
        initUrlAndIFrame($("input[name = 'importance_type']:checked").val());
    }

    function initUrlAndIFrame(checkedValue) {

        $(".loadingObject").show();
        importanceIFrame.hide();

        checkedImportanceTypeValue = checkedValue;

        switch (checkedValue) {

            case BY_EXACT_DATE : {

                $(".menuDropdown strong").text("By exact date")
                url = "https://en.wikipedia.org/wiki/Portal:Current_events/" + year + "_" + month + "_" + day;
            }
                break;
            case BY_MONTH_AND_DAY : {

                $(".menuDropdown strong").text("By Month and Day")
                url = "https://en.wikipedia.org/wiki/" + month + "_" + day;
            }
                break;
            case BY_MONTH_AND_YEAR : {

                $(".menuDropdown strong").text("By Month and Year")
                url = "https://en.wikipedia.org/wiki/Portal:Current_events/" + month + "_" + year;
            }
                break;
        }

        initIFrame();
    }

    function initDivText() {

        if (day < 10) {

            dayDiv.text("0" + day);
        } else {

            dayDiv.text(day);
        }

        monthDiv.text(month);
        yearDiv.text(year);

    }

    function initIFrame() {

        timeOutId = setTimeout(function () {

            importanceIFrame.attr({
                "src": url,
                "title": "Importance of " + month + " " + day
            });

            importanceIFrame.on("load", function () {

                $(".loadingObject").hide();
                importanceIFrame.show();

            });

        }, 500);

    }

    function getRandomDate(start, end) {
        return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
    }

    // =====================================================
});
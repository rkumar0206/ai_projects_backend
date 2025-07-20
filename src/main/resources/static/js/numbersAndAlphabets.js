/**
 * Number related
 * Will generate random numbers based on selected parameters
 */
$(function () {

    // vars
    let isRangeSelected = true;
    let startValue = 1;
    let endValue = 1000;
    let length = 4;
    let selectedNumberType = "none";
    let selectedNumberBase = "decimal"
    let theNumber;

    // views
    let resultArea = $("#numberResultArea");
    let numberRefreshBtn = $("#numberRefreshBtn");
    let range_div = $("#range");
    let numberLength_div = $("#numberLengthDiv");
    let length_range = $("#numberLengthRange");
    let start_range_input = $("#startRangeInput")
    let end_range_input = $("#endRangeInput")
    let number_type_div = $("#evenOdd")
    let base_div = $("#base")

    // initial changes
    range_div.css({"border-color": "blueviolet", "border-width": "1px", "border-style": "solid"});
    $("#range_error").hide();

    selectedNumberType = $("input[name='evenOddPrime']:checked").val();
    selectedNumberBase = $("input[name='base']:checked").val();

    resultArea.text(getRandomNumberResult());

    numberRefreshBtn.click(function () {

        numberRefreshBtn.animate({
            opacity: '0.5'
        }, "fast", "swing", function () {

            numberRefreshBtn.animate({
                opacity: '1'
            }, "fast");
        });


        selectedNumberType = $("input[name='evenOddPrime']:checked").val();
        selectedNumberBase = $("input[name='base']:checked").val();

        if (isRangeSelected) {

            startValue = Number(start_range_input.val());
            endValue = Number(end_range_input.val());

            if (endValue <= startValue) {

                start_range_input.css({"border": "1px solid orangered"});
                end_range_input.css({"border": "1px solid orangered"});
                $("#range_error").show();
            } else {

                start_range_input.css({"border": "0px"});
                end_range_input.css({"border": "0px"});
                $("#range_error").hide();
                resultArea.text(getRandomNumberResult());
            }

        } else {

            length = Number(length_range.val());
            resultArea.text(getRandomNumberResult());
        }
    });

    /*
    When range div is clicked the length range should be disabled and
    will enable all the parameters / filters i.e. number type and base.
     */
    range_div.click(function () {

        isRangeSelected = true;
        range_div.css({"border-color": "blueviolet", "border-width": "1px", "border-style": "solid"});
        numberLength_div.css("border-width", "0px");

        enableInputsInsideDiv(number_type_div)
        enableInputsInsideDiv(base_div)

    });

    /*
    When length div is clicked all the other views i.e. range, number type and base should be
    disabled and number should be generated according to the selected length
     */
    numberLength_div.click(function () {

        isRangeSelected = false;

        numberLength_div.css({"border-color": "blueviolet", "border-width": "1px", "border-style": "solid"});
        range_div.css("border-width", "0px");

        // disable all the filters
        disableInputsInsideDiv(number_type_div)
        disableInputsInsideDiv(base_div)

    });

    length_range.on("input", function () {

        $("#numberLengthRangeLabel").text($(this).val());
    });

    length_range.on("change", function () {

        numberRefreshBtn[0].click();
    });

    // will disable all the inputs inside a div
    function disableInputsInsideDiv(div) {

        div.addClass("disabled")

        div.find('input').each(function () {
            $(this).attr('disabled', 'disabled');
        });
    }

    // will enable all the inputs inside a div
    function enableInputsInsideDiv(div) {

        div.removeClass("disabled")

        div.find('input').each(function () {
            $(this).removeAttr('disabled');
        });
    }

    function getRandomNumberResult() {

        if (isRangeSelected) {

            switch (selectedNumberType) {

                case "none": {

                    theNumber = getRandomNumber();
                }
                    break;
                case "even" : {

                    theNumber = Number(getRandomEvenNumber());
                }
                    break;
                case "odd" : {

                    theNumber = Number(getRandomOddNumber());
                }
                    break;
                case "prime" : {

                    theNumber = Number(getRandomPrimeNumber());
                }
                    break;
            }

            switch (selectedNumberBase) {

                case "hexadecimal" : {

                    return theNumber.toString(16) + "  (Decimal: " + theNumber + ")";
                }

                case "octal" : {

                    return theNumber.toString(8) + "  ( Decimal: " + theNumber + ")";
                }

                case "binary" : {

                    return (theNumber >>> 0).toString(2) + "  (Decimal: " + theNumber + ")";
                }
            }

            return theNumber;
        } else {

            return getRandomNumber();
        }
    }

    function getRandomNumber() {

        if (isRangeSelected) {

            return Math.floor(startValue + Math.random() * (endValue - startValue + 1));
        } else {

            const num = "0123456789"

            let result = "";

            for (let i = 0; i < length; i++) {

                result += num.charAt(Math.floor(Math.random() * num.length));
            }
            return result;
        }
    }

    function getRandomEvenNumber() {

        return Math.floor(startValue + Math.random() * ((endValue - startValue + 1) / 2)) * 2;
    }

    function getRandomOddNumber() {

        if (startValue === 0) {
            startValue = 1;
        }

        return (Math.floor((startValue - 1) + Math.random() * ((endValue - startValue + 1) / 2)) * 2) + 1;
    }

    function getRandomPrimeNumber() {

        const isPrime = (num) => {

            for (let i = 2, s = Math.sqrt(num); i <= s; i++) {

                if (num % i === 0) {

                    return false;
                }
            }
            return num > 1;
        }

        let n = getRandomOddNumber();

        while (true) {

            if (isPrime(n)) {

                return n;
            } else {

                n = getRandomOddNumber();
            }
        }
    }
});

/**
 * Password related
 * Will generate random password based on selected parameters
 */
$(function () {

    // vars
    let length = 8;

    // views
    let specialCharactersCB = $("#specialCharactersCB");
    let includeDigitsCB = $("#includeDigitsCB");
    let includeUpperCaseCB = $("#includeUpperCaseCB");
    let includeLowerCaseCB = $("#includeLowerCaseCB");
    let passwordLengthRange = $("#passwordLengthRange");
    let refreshBtn = $("#passwordRefreshBtn");
    let mustIncludeCharacters = $("#mustIncludeCharacters");

    length = Number(passwordLengthRange.val());

    $("#passwordResultArea").text(refreshRandomPasswordString());

    passwordLengthRange.on("input", function () {

        length = Number($(this).val());

        $("#passwordLengthRangeLabel").text($(this).val());
    });

    passwordLengthRange.on("change", function () {

        $("#passwordResultArea").text(refreshRandomPasswordString());
    });

    refreshBtn.click(function () {

        refreshBtn.animate({
            opacity: '0.5'
        }, "fast", "swing", function () {

            refreshBtn.animate({
                opacity: '1'
            }, "fast");
        });

        $("#passwordResultArea").text(refreshRandomPasswordString());
    });

    function refreshRandomPasswordString() {

        let upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        let lowerCaseLetters = upperCaseLetters.toLowerCase();
        let numbers = "0123456789";
        let specialCharacters = "!@#$&%^*_+";

        let password = "";

        let combined = "";

        if (specialCharactersCB.is(":checked")) {

            combined += specialCharacters;
        }

        if (includeUpperCaseCB.is(":checked")) {

            combined += upperCaseLetters;
        }

        if (includeLowerCaseCB.is(":checked")) {

            combined += lowerCaseLetters;
        }

        if (includeDigitsCB.is(":checked")) {

            combined += numbers;
        }

        if (combined === "") {

            combined += lowerCaseLetters;
            includeLowerCaseCB.prop('checked', true);
        }

        console.log("mustIncludeCharacters : " + mustIncludeCharacters.val())

        let isAdditionalCharacterIncluded = false;

        if (mustIncludeCharacters.val() !== '') {

            let str = shuffle(Array.from(mustIncludeCharacters.val()));

            if (str.length >= length) {

                return str;
            }

            isAdditionalCharacterIncluded = true;
            length -= str.length;
            password += str;
        }

        for (let i = 0; i < length; i++) {

            password += combined.charAt(Math.floor(Math.random() * combined.length));
        }

        length += mustIncludeCharacters.val().length;

        // if must include characters were not empty then shuffling them with result password
        if (isAdditionalCharacterIncluded) {

            return shuffle(Array.from(password));
        }

        return password;
    }

    function shuffle(array) {
        let currentIndex = array.length, randomIndex;

        // While there remain elements to shuffle...
        while (currentIndex !== 0) {

            // Pick a remaining element...
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex--;

            // And swap it with the current element.
            [array[currentIndex], array[randomIndex]] = [
                array[randomIndex], array[currentIndex]];
        }

        return array.join("");
    }

});

/**
 * UUID related
 * Will generate random UUID (Universally Unique Identifier)
 */
$(function () {

    setUUId();

    let refreshBtn = $("#uuidRefreshBtn");

    refreshBtn.click(function () {

        refreshBtn.animate({
            opacity: '0.5'
        }, "fast", "swing", function () {

            refreshBtn.animate({
                opacity: '1'
            }, "fast");
        });


        setUUId();
    });

    function setUUId() {

        $.get("/categories/Numbers & Alphabets/uuid", null, function (data) {

            $("#uuidResultArea").text(data);
        })

    }

});

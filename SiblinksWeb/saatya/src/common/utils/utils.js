var SERVICE_URL = 'http://ec2-54-200-200-106.us-west-2.compute.amazonaws.com:8080/brot/services';
//var SERVICE_URL = 
var NEW_SERVICE_URL //@webserviceurl
    ;

var serverDateTime = null;

var SUBJECT_MAP_IMG = {
    'maths': {
        'white': 'assets/img/calc.png',
        'green': 'assets/img/calc_green.png'
    },
    'biology': {
        'white': 'assets/img/biology.png',
        'green': 'assets/img/biology_green.png'
    },
    'chemistry': {
        'white': 'assets/img/chemistry.png',
        'green': 'assets/img/chemistry_green.png'
    },
    'physics': {
        'white': 'assets/img/physics.png',
        'green': 'assets/img/physics_green.png'
    }
};

var UPLOAD_SUCCESS = {
    'uploadSuccess': 'Upload successfully'
};

var DATA_ERROR = {
    'noDataFound': 'There is currently no data'
};

var TEXT_ERROR = {
    'emptyText': 'Please enter a discussion !'
};

var TITLE_RESULT = {
    'titleResult': 'Video Result'
};

var DATA_ERROR_NOTIFICATION = {
    'noNewNotification': 'No new notification'
};

var PANEL_VIDEO_TUTORIAL = {
    'labelTutorial': 'Click on the links below to watch videos for selected subjects'
};


var StatusError = {
    MSG_DATA_NOT_FOUND: "Found no data",
    MSG_USER_ID_NOT_EXIST: "User is not exists",
    MSG_UNKNOWN: "Unknown"
};

// Limit when upload image 5M
var MAX_SIZE_IMG_UPLOAD = 5242880;
var MAX_IMAGE = 4;

function IsEmail(email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    return emailReg.test(email);
}

function subDate(d) {
    // Set the unit values in milliseconds.
    var msecPerMinute = 1000 * 60;
    var msecPerHour = msecPerMinute * 60;
    var msecPerDay = msecPerHour * 24;

    // Set a date and get the milliseconds
    var dateold = new Date(moment(d, "YYYY-MM-DD HH:mm:ss"));
    var dateMsec = dateold.getTime();

    // Set the date to January 1, at midnight, of the specified year.
    var curDate = new Date();

    // Get the difference in milliseconds.
    var interval = curDate.getTime() - dateMsec;

    // Calculate how many days the interval contains. Subtract that
    // many days from the interval to determine the remainder.
    var days = Math.floor(interval / msecPerDay);
    interval = interval - (days * msecPerDay );

    // Calculate the hours, minutes, and seconds.
    var hours = Math.floor(interval / msecPerHour);
    interval = interval - (hours * msecPerHour );

    var minutes = Math.floor(interval / msecPerMinute);
    interval = interval - (minutes * msecPerMinute );

    var seconds = Math.floor(interval / 1000);

    // Display the result.
    var re = '';
    if (days !== 0) {
        re += days + " days ";
    }
    if (hours !== 0) {
        re += hours + " hours ";
    }
    if (minutes !== 0) {
        re += minutes + " minutes ";
    }
    re += seconds + " seconds";
    return re;
}
function setStorage(name, value, expire) {
    if (expire === undefined || expire == null) {
        expire = 3600; //default
    }
    var date = new Date();
    var schecule = Math.round((date.setSeconds(date.getSeconds() + expire)) / 1000);
    localStorage.setItem(name, value);
    localStorage.setItem(name + '_time', schecule);
}
function removeStorage(name) {
    localStorage.removeItem(name);
    localStorage.removeItem(name + '_time');
}

function capitaliseFirstLetter(str) {
    return str.charAt(0).toUpperCase() + str.substring(1).toLowerCase();
}

function convertUnixTimeToTime(time) {

    if (serverDateTime == null || serverDateTime === undefined) {
        console.log('serverDateTime is null');
        timeBackEnd();
    }

    var _now = Math.floor(serverDateTime);
    var _space = _now - time;
    var _secondDay = 3600 * 24;
    var _secondMonth = _secondDay * 30;
    if (_space < 60) {
        if (_space <= 0) {
            serverDateTime = null;
            timeBackEnd();
            _now = Math.floor(serverDateTime);
            _space = _now - time;
            return _space + ' seconds ago';
        } else
            return _space + ' seconds ago';
    } else if (_space < 3600) {
        return Math.floor(_space / 60) > 1 ? Math.floor(_space / 60) + ' minutes ago' : Math.floor(_space / 60) + ' minute ago';
    } else if (_space < _secondDay) {
        return Math.floor(_space / 3600) > 1 ? Math.floor(_space / 3600) + ' hours ago' : Math.floor(_space / 3600) + ' hour ago';
    } else if (_space >= _secondDay && _space < _secondMonth) {
        return Math.floor(_space / _secondDay) > 1 ? Math.floor(_space / _secondDay) + ' days ago' : ' yesterday';
    } else {
        return Math.floor(_space / _secondMonth) > 1 ? Math.floor(_space / _secondMonth) + ' months ago' : Math.floor(_space / _secondMonth) + ' month ago';
    }
}
function loginFBService(callback) {
    FB.init({
        appId: APPFBID,
        status: true, // check login status
        cookie: true, // enable cookies to allow the server to access the session
        xfbml: true  // parse XFBML
    });

    FB.login(function (response) {
        if (response.authResponse) {
            FB.api('/me', {fields: 'email, last_name, first_name, picture'}, function (response) {
                callback(response);
            });
        }
    }, {
        scope: 'public_profile, email'
    });
}

function showPage(numberPage, page, callback) {
    var listPage = [];
    numberPage = parseInt(numberPage, 10);
    page = parseInt(page, 10);
    var min = (page - 3 < 1) ? 1 : page - 3;
    var max = (page + 3 > numberPage) ? numberPage : page + 3;
    for (var i = min; i <= max; i++) {
        listPage.push(i);
    }
    callback(listPage);
}

function revertCKeditor(showCKEditor) {
    if (showCKEditor) {
        $('.content .discus .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('.content .discus .ckComment').addClass('hide');
    $('.content .discus .go').removeClass('hide');
    $('.content .discus .btnHide').addClass('hide');
}

function revertCKeditorArticleDetail(showCKEditor) {
    if (showCKEditor) {
        $('.bottom_detailArticle .discussion .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('.bottom_detailArticle .discussion .ckComment').addClass('hide');
    $('.bottom_detailArticle .discussion .go').removeClass('hide');
    $('.bottom_detailArticle .discussion .btnHide').addClass('hide');
}

function revertCKeditorEssayDetail(showCKEditor) {
    if (showCKEditor) {
        $('#essay_detail .discussion .boxCkComment').toggle('slow');
        showCKEditor = false;
    }
    CKEDITOR.instances["txtDiscus"].setData('');
    $('#essay_detail .discussion .ckComment').addClass('hide');
    $('#essay_detail .discussion .go').removeClass('hide');
    $('#essay_detail .discussion .btnHide').addClass('hide');
}

function showRating(rating, callback) {
    var rate = 0;
    if (rating != null && rating !== '') {
        rate = Math.floor(rating);
    }

    var unrate = 5 - rate;
    var arr_rate = [];
    for (var j = 0; j < rate; j++) {
        arr_rate.push('assets/img/yellow _star.png');
    }
    for (var k = 0; k < unrate; k++) {
        arr_rate.push('assets/img/grey_star.png');
    }
    callback(arr_rate);
}

function showClickRating(callback) {
    var arr_rate = [];
    for (var i = 0; i < 5; i++) {
        arr_rate.push('assets/img/grey_star.png');
    }
    callback(arr_rate);
}
function isEmpty(str) {
    return typeof str == 'string' && !str.trim() || str === undefined || str === null || str == 'null';

}

/*
 * convert string subject id to array
 */
function getSubjectNameById(strSubjectId, listcate) {
    if (strSubjectId == null || strSubjectId === undefined) {
        return;
    }
    if (listcate == null || listcate === undefined) {
        return;
    }
    var subject = {};
    var listSubject = [];
    if (isEmpty(strSubjectId)) {
        listSubject.push(subject);
        return listSubject;
    }
    if (strSubjectId.indexOf(',') < 0) {
        for (var y = 0; y < listcate.length; y++) {
            if (listcate[y].subjectId == strSubjectId) {
                subject.id = strSubjectId;
                subject.name = listcate[y].subject;
                subject.level = listcate[y].level;
                subject.parentId = listcate[y].parentId;
                listSubject.push(subject);
                return listSubject;

            }

        }
    }
    else {
        var list = strSubjectId.split(',');
        for (var i = 0; i < list.length; i++) {
            for (var y = 0; y < listcate.length; y++) {
                if (listcate[y].subjectId == list[i]) {
                    subject = [];
                    subject.name = listcate[y].subject;
                    subject.id = listcate[y].subjectId;
                    subject.level = listcate[y].level;
                    subject.parentId = listcate[y].parentId;
                    listSubject.push(subject);
                }

            }
        }
    }

    return listSubject;

}

function isValidEmailAddress(emailAddress) {
	var pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
    return pattern.test(emailAddress);
}

function validateGender(key) {
    switch (key) {
        case "M":
            return "Male";
        case "F":
            return "Female";
        case "O":
            return "Decline to state";
        default :
            return "Decline to state";
    }
}


var FormatDateTimeType = {
    DD_MM_YY: 'dd_mm,yy',
    DD_MM_YY_HH: "dd_mm_yy_hh",
    MM_DD_YY_HH: "mm_dd_yy_hh",
    YY_DD_YY: "yy_dd_yy",
    MM_YY: "mm_yy"
};

/**
 * @param timeStamp
 * @param{FormatDateTimeType} typeFormat
 * @returns {*}
 */
function timeConverter(timeStamp, typeFormat) {
    if (!timeStamp) {
        return '';
    }
    var a = new Date(timeStamp * 1000);
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    if(date<10) {
        date='0'+date;
    }
    switch (String(typeFormat)) {
        case FormatDateTimeType.DD_MM_YY:
            return date + ' ' + month + ', ' + year;
        case FormatDateTimeType.DD_MM_YY_HH:
            return date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
        case FormatDateTimeType.MM_DD_YY_HH:
            return month + ' ' + date + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
        case FormatDateTimeType.YY_DD_YY:
            return year + ' ' + date + ' ' + month;
        case FormatDateTimeType.MM_YY:
            return month + ', ' + year;
        default :
            return date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
    }
    // var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
    // return time;
}

/**
 * GetTimes DB from backEnd
 */
function timeBackEnd() {
    if (!serverDateTime) {
        var xmlhttp = new XMLHttpRequest();
        try {
            xmlhttp.open("GET", NEW_SERVICE_URL + "/timeDB", false);
            xmlhttp.send();
            serverDateTime = JSON.parse(xmlhttp.responseText);
    	} catch(e) {
    		console.log("can't connect to server");
    		serverDateTime = (Date.now() / 1000);
    	}
    }
    // Increment time by 1 second
    serverDateTime++;
    return serverDateTime;
    //console.log(serverDateTime);
}

function resetUI() {
    showToken('loading...');
    messaging.getToken()
        .then(function (currentToken) {
            if (currentToken) {
                sendTokenToServer(currentToken);
                showToken(currentToken);
            } else {
                // Show permission request.
                console.log('No Instance ID token available. Request permission to generate one.');
                // Show permission UI.
                requestPermission();
                setTokenSentToServer(false);
            }
        })
        .catch(function (err) {
            console.log('An error occurred while retrieving token. ', err);
            showToken('Error retrieving Instance ID token. ', err);
            setTokenSentToServer(false);
        });
}
// [END get_token]
function showToken(currentToken) {
    // Show token in console and UI.
    var tokenElement = document.querySelector('#token');
    tokenElement.textContent = currentToken;
}

function sendTokenToServer(currentToken) {
    if (!isTokenSentToServer()) {
        console.log('Sending token to server...');
        setTokenSentToServer(true);
    } else {
        console.log('Token already sent to server so won\'t send it again ' +
            'unless it changes');
    }
}

function isTokenSentToServer() {
    if (window.localStorage.getItem('sentToServer') == 1) {
        return true;
    }
    return false;
}

function setTokenSentToServer(sent) {
    if (sent) {
        window.localStorage.setItem('sentToServer', 1);
    } else {
        window.localStorage.setItem('sentToServer', 0);
    }
}

function requestPermission() {
    console.log('Requesting permission...');
    // [START request_permission]
    messaging.requestPermission()
        .then(function () {
            console.log('Notification permission granted.');
            resetUI();
        })
        .catch(function (err) {
            console.log('Unable to get permission to notify. ', err);
        });
    // [END request_permission]
}
function deleteToken() {
    // Delete Instance ID token.
    // [START delete_token]
    messaging.getToken()
        .then(function (currentToken) {
            messaging.deleteToken(currentToken)
                .then(function () {
                    console.log('Token deleted.');
                    setTokenSentToServer(false);
                    // [START_EXCLUDE]
                    // Once token is deleted update UI.
                    resetUI();
                    // [END_EXCLUDE]
                })
                .catch(function (err) {
                    console.log('Unable to delete token. ', err);
                });
            // [END delete_token]
        })
        .catch(function (err) {
            console.log('Error retrieving Instance ID token. ', err);
            showToken('Error retrieving Instance ID token. ', err);
        });
}
// Add a message to the messages element.
function appendMessage(payload) {
//    const messagesElement = document.querySelector('#messages');
//    const dataHeaderELement = document.createElement('h5');
//    const dataElement = document.createElement('pre');
//    dataElement.style = 'overflow-x:hidden;'
//    dataHeaderELement.textContent = 'Received message:';
//    dataElement.textContent = JSON.stringify(payload, null, 2);
//    messagesElement.appendChild(dataHeaderELement);
//    messagesElement.appendChild(dataElement);
}
// Clear the messages element of all children.
function clearMessages() {
//    const messagesElement = document.querySelector('#messages');
//    while (messagesElement.hasChildNodes()) {
//        messagesElement.removeChild(messagesElement.lastChild);
//    }
}
function updateUIForPushEnabled(currentToken) {
    showHideDiv(tokenDivId, true);
    showHideDiv(permissionDivId, false);
    showToken(currentToken);
}
function updateUIForPushPermissionRequired() {
    showHideDiv(tokenDivId, false);
    showHideDiv(permissionDivId, true);
}

//Check first name or last name had special character
function isNotValidName(strName) {
    return /\`|\~|\!|\@|\#|\$|\%|\^|\&|\*|\(|\)|\+|\=|\[|\{|\]|\}|\||\\|\'|\<|\,|\>|\?|\/|\""|\;|\:|[0-9]/.test(strName);
  }
  
function isValidPhoneUSA(p) {
	  var phoneRe = /^[2-9]\d{2}[2-9]\d{2}\d{4}$/;
  var digits = p.replace(/\D/g, "");
  return (digits.match(phoneRe) !== null);
}

//Validate input before trim()
function checkNameToTrim(str) {
    if (!isEmpty(str)) {
        return str.trim();
    }
    return str;
}

/**
 * Show name user
 * @param firstName
 * @param lastName
 * @param userName
 * @returns
 */
 
function displayUserName(firstName, lastName, userName) {
    if (!isEmpty(firstName) && !isEmpty(lastName)) {
    	return firstName + ' ' + lastName;
    } else if(isEmpty(firstName) && isEmpty(lastName)) {
    	if(isEmpty(userName)) {
    		return 'No name';
    	} else {
    		return capitaliseFirstLetter(userName.substr(0, userName.indexOf('@')));
    	}
    } else {
    	return  !isEmpty(firstName)?firstName:lastName;
    }
}

function removeItem(data){
    for (var i = data.length - 1; i >= 0; i--) {
        if(data[i].isForum && data[i].isForum == true)
            data.splice(i, 1);
    }
    return data;
}

function toFixed(value, precision) {
    var power = Math.pow(10, precision || 0);
    return String(Math.round(value * power) / power);
}

function closePopupAskQuestion() {
	$("body").removeClass('disableScroll');
    $(".popup-images, .form-ask-question").css({"left": "100%"});
}

function showNumberFormat(value, precision) {
	var nStr = toFixed(value, precision);
	return thousandFormatNumber(nStr);
}

function thousandFormatNumber(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}
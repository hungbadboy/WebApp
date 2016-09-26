var SERVICE_URL = 'http://ec2-54-200-200-106.us-west-2.compute.amazonaws.com:8080/brot/services';
//var SERVICE_URL = 
var NEW_SERVICE_URL //@webserviceurl
;
var SUBJECT_MAP_IMG = {
						'maths':{
									'white':'assets/img/calc.png',
									'green':'assets/img/calc_green.png'
								},
						'biology': {
									'white':'assets/img/biology.png',
									'green':'assets/img/biology_green.png'
								},
						'chemistry': {
									'white':'assets/img/chemistry.png',
									'green':'assets/img/chemistry_green.png'
								},
						'physics': {
									'white':'assets/img/physics.png',
									'green':'assets/img/physics_green.png'
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
	var days = Math.floor(interval / msecPerDay );
	interval = interval - (days * msecPerDay );

	// Calculate the hours, minutes, and seconds.
	var hours = Math.floor(interval / msecPerHour );
	interval = interval - (hours * msecPerHour );

	var minutes = Math.floor(interval / msecPerMinute );
	interval = interval - (minutes * msecPerMinute );

	var seconds = Math.floor(interval / 1000 );

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
function setStorage(name, value, expire){
    if(expire === undefined || expire == null) {
       expire = 3600; //default
    }
    var date = new Date();
    var schecule = Math.round((date.setSeconds(date.getSeconds()+expire))/1000);
    localStorage.setItem(name, value);
    localStorage.setItem(name+'_time', schecule);
}
function removeStorage(name){
    localStorage.removeItem(name);
    localStorage.removeItem(name+'_time');
}

function capitaliseFirstLetter(str) {
	return str.charAt(0).toUpperCase() + str.substring(1).toLowerCase();
}

function convertUnixTimeToTime (time) {
  var _now = Math.floor(Date.now() / 1000);
  var _space = _now - time;
  var _secondDay = 3600 * 24;
  var _secondMonth = _secondDay * 30;
  if(_space < 60) {
    return _space + ' seconds';
  } else if(_space < 3600) {
    return Math.floor(_space / 60) + ' minutes';
  } else if (_space < _secondDay) {
    return Math.floor(_space / 3600) + ' hours';
  } else if (_space >= _secondDay && _space < _secondMonth) {
    return Math.floor(_space / _secondDay) + ' days';
  } else {
    return Math.floor(_space / _secondMonth) + ' months';
  }
}
function loginFBService (callback) {
	  FB.init({
	    appId: APPFBID,
	    status: true, // check login status
	    cookie: true, // enable cookies to allow the server to access the session
	    xfbml: true  // parse XFBML
	  });

	  FB.login(function(response) {
	    if (response.authResponse) {
	      FB.api('/me', {fields: 'email, last_name, first_name, picture'}, function(response) {
	        callback(response);
	      });
	    }
	  },{
	    scope: 'public_profile, email'
	  });
	}

	function showPage (numberPage, page, callback) {
	  var listPage = [];
	  numberPage = parseInt(numberPage, 10);
	  page = parseInt(page, 10);
	  var min = (page - 3 < 1) ? 1 : page - 3;
	  var max = (page + 3 > numberPage) ? numberPage : page + 3;
	  for(var i = min; i <= max; i++) {
	    listPage.push(i);
	  }
	  callback(listPage);
	}

	function revertCKeditor(showCKEditor) {
	  if(showCKEditor) {
	    $('.content .discus .boxCkComment').toggle('slow');
	    showCKEditor = false;
	  }
	  CKEDITOR.instances["txtDiscus"].setData('');
	  $('.content .discus .ckComment').addClass('hide');
	  $('.content .discus .go').removeClass('hide');
	  $('.content .discus .btnHide').addClass('hide');
	}

	function revertCKeditorArticleDetail(showCKEditor) {
	  if(showCKEditor) {
	    $('.bottom_detailArticle .discussion .boxCkComment').toggle('slow');
	    showCKEditor = false;
	  }
	  CKEDITOR.instances["txtDiscus"].setData('');
	  $('.bottom_detailArticle .discussion .ckComment').addClass('hide');
	  $('.bottom_detailArticle .discussion .go').removeClass('hide');
	  $('.bottom_detailArticle .discussion .btnHide').addClass('hide');
	}

	function revertCKeditorEssayDetail(showCKEditor) {
	  if(showCKEditor) {
	    $('#essay_detail .discussion .boxCkComment').toggle('slow');
	    showCKEditor = false;
	  }
	  CKEDITOR.instances["txtDiscus"].setData('');
	  $('#essay_detail .discussion .ckComment').addClass('hide');
	  $('#essay_detail .discussion .go').removeClass('hide');
	  $('#essay_detail .discussion .btnHide').addClass('hide');
	}

	function showRating (rating, callback) {
	  var rate = 0;
	  if(rating != null && rating !== '') {
	    rate = Math.floor(rating);
	  }
	  
	  var unrate = 5 - rate; 
	  var arr_rate = [];
	  for(var j = 0; j < rate; j++) {
	    arr_rate.push('assets/img/yellow _star.png');
	  }
	  for(var k = 0; k < unrate; k++) {
	    arr_rate.push('assets/img/grey_star.png');
	  }
	  callback(arr_rate);
	}

	function showClickRating (callback) {
	  var arr_rate = [];
	  for(var i = 0; i < 5; i++) {
	    arr_rate.push('assets/img/grey_star.png');
	  }
	  callback(arr_rate);
	}
	function isEmpty(str) {
		return typeof str == 'string' && !str.trim() || typeof str == 'undefined' || str === null;

	}
	
 function caculateTimeElapsed(time){
	var current = new Date().getTime();
	var inputTime = new Date(time);
	var secondElapsed = parseInt(Math.floor((current - inputTime)/1000));
	secondElapsed = (secondElapsed < 1)? 1 : secondElapsed;
	var text = '';

	if (years = parseInt((Math.floor(secondElapsed / 31536000))))
		text = years + (years > 1 ? ' years' : ' year') + ' ago';
	else if (months = parseInt((Math.floor(secondElapsed / 2592000))))
		text = months + (months > 1 ? ' months' : ' month') + ' ago';
	else if (weeks = parseInt((Math.floor(secondElapsed / 604800))))
		text = weeks + (weeks > 1 ? ' weeks' : ' week') + ' ago';
	else if (days = parseInt((Math.floor(secondElapsed / 86400))))
		text = days + (days > 1 ? ' days' : ' day') + ' ago';
	else if (hours = parseInt((Math.floor(secondElapsed / 3600))))
		text = hours + (hours > 1 ? ' hours' : ' hour') + ' ago';
	else if (minutes = parseInt((Math.floor(secondElapsed / 60))))
		text = minutes + (minutes > 1 ? ' minutes' : ' minute') + ' ago';
	else
		text = secondElapsed + (secondElapsed > 1 ? ' seconds' : ' second') + ' ago';

	return text;
}

/*
* convert string subject id to array
*/
function getSubjectNameById(strSubjectId, listcate) {
	var subject = {};
	var listSubject = [];
	if (isEmpty(strSubjectId)) {
		listSubject.push(subject);
		return listSubject;
	}
	if (strSubjectId.indexOf(',') < -1) {
		for (var y = 0; y < listcate.length; y++) {
			if (listcate[y].subjectId == strSubjectId) {
				subject.id = strSubjectId;
				subject.name = listcate[y].subject
				return listSubject.push(subject);
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
					listSubject.push(subject);
				}

			}
		}
	}

	return listSubject;

}

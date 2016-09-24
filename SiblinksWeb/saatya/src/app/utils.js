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

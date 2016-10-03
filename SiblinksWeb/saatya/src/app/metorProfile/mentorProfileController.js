brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache',
        function ($sce, $scope, $modal, $routeParams, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache) {

    var userId = $routeParams.mentorId != undefined ? $routeParams.mentorId : localStorage.getItem('userId');
    var userType = localStorage.getItem('userType');

    var isInit = true;
    $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
    // $scope.subscribeText = 'subscribed';
    // $scope.icon = 'N';

    init();


    function init() {
        // checkStudentSubscribe();
        getMentorProfile();
        // getNewestAnswer();
        // getVideosRecently();
    }

    // function checkStudentSubscribe(){
    //   if (userType == 'S') {
    //     MentorService.checkStudentSubscribe(mentorId, userId).then(function(data){          
    //       $scope.subscribeInfo = data.data.request_data_result;
    //     });
    //   }
    // }


    function getMentorProfile() {        
        StudentService.getUserProfile(userId).then(function (data) {
            if (data.data.request_data_result && data.data.request_data_result != "Found no data") {
                $scope.mentor = data.data.request_data_result;

                if ($scope.mentor.imageUrl == null || $scope.mentor.imageUrl.length == 0) {
                    $scope.mentor.imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
                } else {
                    $scope.mentor.imageUrl = $scope.mentor.imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + $scope.mentor.imageUrl : $scope.mentor.imageUrl;
                }

                var firstname = $scope.mentor.firstname != null ? $scope.mentor.firstname : '';
                var lastname = $scope.mentor.lastName != null ? $scope.mentor.lastName : '';
                $scope.mentor.fullName = firstname + ' ' + lastname;
                // $scope.mentor.bio = moment($scope.mentor.bio, "MM/DD/YYYY").month(0).from(moment().month(0)).replace('years ago', '');
                $scope.mentor.bio = $scope.mentor.bio != null ? moment($scope.mentor.bio).format("DD MMM, YYYY") : null;
                $scope.mentor.registrationTime = moment($scope.mentor.registrationTime).format("MMM, YYYY");
                if ($scope.mentor.skills != null) {
                    $scope.mentor.skills = $scope.mentor.skills.replace(/\"/g, '');
                }
                $scope.mentor.averageRating = $scope.mentor.averageRating == null ? 0 : $scope.mentor.averageRating;
                $scope.mentor.numRatings = $scope.mentor.numRatings == null ? 0 : $scope.mentor.numRatings;
                $scope.mentor.numViews = $scope.mentor.numViews == null ? 0 : $scope.mentor.numViews;

                displayInformation($scope.mentor);
            }            
        });
    }

    function displayInformation(mentor){
        $('#txtFirstName').val(mentor.firstname);
        $('#txtLastName').val(mentor.lastName);
        $('#txtEmail').val(mentor.email);
        $('#txtLastName').val(mentor.lastName);
        $('#txtBIO').val(mentor.bio);
        $('#txtDescription').val(mentor.description);

        if (mentor.gender == "M") {
           $('#male').prop('checked', true);
        } else if (mentor.gender == "F") {
           $('#female').prop('checked', true);
        } else {
           $('#other').prop('checked', true);
        }

        if(mentor.favorite !=null && mentor.favorite !== undefined){
          var favorite = mentor.favorite.split(',');
          for (var i = 0; i < favorite.length; i++) {
              if (favorite[i] == 'Music') {
                  $('#music').prop('checked', true);
              } 
              if (favorite[i] == 'Art') { 
                  $('#art').prop('checked', true);
              }
              if (favorite[i] == 'Sport') { 
                  $('#sport').prop('checked', true);
              }
          }
      }
    }

    // function getNewestAnswer() {
    //     MentorService.getNewestAnswer(userId).then(function (data) {
    //         $scope.answers = formatData(data.data.request_data_result);
    //     });
    // }

    // function getVideosRecently() {
    //     VideoService.getVideosRecently(userId).then(function (data) {
    //         $scope.videos = data.data.request_data_result;
    //     });
    // }

    // function formatData(data) {
    //     for (var i = 0; i < data.length; i++) {
    //         data[i].numLike = data[i].numLike == null ? 0 : data[i].numLike;
    //         data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
    //     }
    //     return data;
    // }

    MentorService.getStudentSubscribed(userId, 6, 0).then(function (data) {
        if(data.data.status){
            var response = data.data.request_data_result;
            if(response && response != "Found no data"){
                var students = [];
                for(var i = 0; i < response.length ; i++){
                    var obj = {};
                    obj.userId = response[i].userid;
                    obj.lastName = (response[i].lastName == null || response[i].lastName === undefined)? "" : response[i].lastName;
                    obj.firstName= (response[i].firstName == null || response[i].firstName === undefined)? "" : response[i].firstName;
                    obj.userName = response[i].userName != null ? response[i].userName : ' ';
                    obj.avatar = response[i].imageUrl;
                    obj.defaultSubjectId = response[i].defaultSubjectId;
                    obj.school = response[i].school;
                    students.push(obj);
                }
                $scope.listStudentSubscribed = students;
            }
        }
    });
    
    

    $scope.saveChange = function(){
      var check = true;
      var error = '';

      var favorite = "";
      if ($('#music').is(':checked')) {
        favorite += $('#music').val();
      }
      if ($('#art').is(':checked')) {
        if (favorite.length > 0)
          favorite += ',' + $('#art').val();
        else
          favorite += $('#art').val();
      }
      if ($('#sport').is(':checked')) {
        if (favorite.length > 0)
          favorite += ',' + $('#sport').val();
        else
          favorite += $('#sport').val();
      }

      var gender = '';
      if ($('#male').is(':checked')) {
        gender = "M";
      }
      else if ($('#female').is(':checked')) {
        gender = "F";
      }
      else{
        gender = "O";
      }

      if (!isValidEmailAddress($('#txtEmail').val())) {
        check = false;
        error += "Email is not valid";
      }

      if (check) {        
        var mentor = {
          'userid': userId,
          'firstName': $('#txtFirstName').val(),
          'lastName': $('#txtLastName').val(),
          'email': $('#txtEmail').val(),
          'gender':gender,
          'school': $('#txtSchool').val(),
          'bio': $('#txtBIO').val(),
          'description':$('#txtDescription').val(),
          'favorite':favorite
        };
        console.log(mentor);
        StudentService.updateUserProfile(mentor).then(function(data){
          if (data.data.request_data_result == "Success") {
            console.log(data.data.request_data_result);
          }
          else{
              console.log(data.data.request_data_result);
          }
        });
      }
      else{
        console.log(error);
      } 
    }

    // $scope.subcribeMentor = function () {
    //     VideoService.setSubscribeMentor(userId, mentorId).then(function (data) {
    //         console.log(data);
    //         if (data.data.request_data_type == "subs") {
    //             // change icon to unsubscribe
    //             $scope.mentor.count_subscribers += 1;
    //             $scope.mentor.subscribed = true;
    //         }
    //         else if (data.data.request_data_type == "unsubs") {
    //             // change icon to subscribe
    //             $scope.mentor.count_subscribers -= 1;
    //             $scope.mentor.subscribed = false;
    //         }
    //     });
    // }

    // $scope.hoverOut = function () {
    //     $scope.subscribeText = 'subscribed';
    //     $scope.icon = "N";
    // }
    // $scope.hoverIn = function () {
    //     $scope.subscribeText = 'unsubscribe';
    //     $scope.icon = "K";
    // }
}]);

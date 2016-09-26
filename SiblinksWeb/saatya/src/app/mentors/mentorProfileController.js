brotControllers.controller('MentorProfileController',
    ['$sce', '$scope', '$modal', '$routeParams', '$http', '$location', 'MentorService', 'TeamMentorService', 'VideoService', 'StudentService', 'myCache',
        function ($sce, $scope, $modal, $routeParams, $http, $location, MentorService, TeamMentorService, VideoService, StudentService, myCache) {


            var mentorId = $routeParams.mentorId;
            var userId = localStorage.getItem('userId');
            var userType = localStorage.getItem('userType');

            var isInit = true;
            $scope.baseIMAGEQ = NEW_SERVICE_URL + '/comments/getImageQuestion/';
            $scope.subscribeText = 'subscribed';
            $scope.icon = 'N';

            init();


            function init() {
                checkStudentSubscribe();
                getMentorProfile();
                getNewestAnswer();
                getVideosRecently();
            }

    function checkStudentSubscribe(){
      if (userType == 'S') {
        MentorService.checkStudentSubscribe(mentorId, userId).then(function(data){          
          $scope.subscribeInfo = data.data.request_data_result;
        });
      }
    }


    function getMentorProfile() {
        StudentService.getUserProfile(mentorId).then(function (data) {
            $scope.mentor = data.data.request_data_result;
            if ($scope.mentor.imageUrl == null || $scope.mentor.imageUrl.length == 0) {
                $scope.mentor.imageUrl = 'http://www.capheseo.com/Content/T000/Images/no-avatar.png';
            } else {
                $scope.mentor.imageUrl = $scope.mentor.imageUrl.indexOf('http') == -1 ? $scope.baseIMAGEQ + $scope.mentor.imageUrl : $scope.mentor.imageUrl;
            }
            $scope.mentor.fullName = $scope.mentor.firstname + ' ' + $scope.mentor.lastName;
            $scope.mentor.bio = moment($scope.mentor.bio, "MM/DD/YYYY").month(0).from(moment().month(0)).replace('years ago', '');
            if ($scope.mentor.skills != null) {
                $scope.mentor.skills = $scope.mentor.skills.replace(/\"/g, '');
            }
            $scope.mentor.averageRating = $scope.mentor.averageRating == null ? 0 : $scope.mentor.averageRating;
            $scope.mentor.numRatings = $scope.mentor.numRatings == null ? 0 : $scope.mentor.numRatings;
            $scope.mentor.numViews = $scope.mentor.numViews == null ? 0 : $scope.mentor.numViews;
        });
    }

    function getNewestAnswer() {
        MentorService.getNewestAnswer(mentorId).then(function (data) {
            $scope.answers = formatData(data.data.request_data_result);
        });
    }

    function getVideosRecently() {
        VideoService.getVideosRecently(mentorId).then(function (data) {
            $scope.videos = data.data.request_data_result;
        });
    }

    function formatData(data) {
        for (var i = 0; i < data.length; i++) {
            data[i].numLike = data[i].numLike == null ? 0 : data[i].numLike;
            data[i].timeStamp = convertUnixTimeToTime(data[i].timeStamp);
        }
        return data;
    }

    MentorService.getStudentSubscribed(userId, 6, 0).then(function (data) {
        if(data.data.status){
            var response = data.data.request_data_result;
            if(response){
                var students = [];
                for(var i = 0; i < response.length ; i++){
                    var obj = {};
                    obj.userId = response[i].userid;
                    obj.userName = response[i].userName;
                    obj.avatar = response[i].imageUrl;
                    obj.defaultSubjectId = response[i].defaultSubjectId;
                    students.push(obj);
                }
            }
            $scope.listStudentSubscribed = students;
        }
    });

    $scope.subcribeMentor = function () {
        VideoService.setSubscribeMentor(userId, mentorId).then(function (data) {
            console.log(data);
            if (data.data.request_data_type == "subs") {
                // change icon to unsubscribe
                $scope.mentor.count_subscribers += 1;
                $scope.mentor.subscribed = true;
            }
            else if (data.data.request_data_type == "unsubs") {
                // change icon to subscribe
                $scope.mentor.count_subscribers -= 1;
                $scope.mentor.subscribed = false;
            }
        });
    }

    $scope.hoverOut = function () {
        $scope.subscribeText = 'subscribed';
        $scope.icon = "N";
    }
    $scope.hoverIn = function () {
        $scope.subscribeText = 'unsubscribe';
        $scope.icon = "K";
    }
}]);

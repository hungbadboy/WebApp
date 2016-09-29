/**
 * Created by Tavv on 28/09/2016.
 */
brotControllers.controller('SideBarRightController', ['$scope', '$http', 'MentorService', 'VideoService', 'myCache', 'SideBarRightService',
    function ($scope, $http, MentorService, VideoService, myCache, SideBarRightService) {

        var userId = localStorage.getItem('userId');
        //data test
        var defaultLimit = 6;
        var defaultOffet = 0;

        init();

        function init() {
            loadStudentSubscribed();
            getActivityStudent();
        }


        function loadStudentSubscribed() {
            MentorService.getStudentSubscribed(userId, 6, 0).then(function (data) {
                if (data.data.status) {
                    var response = data.data.request_data_result;
                    if (response != null && response != "Found no data") {
                        var students = [];
                        for (var i = 0; i < response.length; i++) {
                            var obj = {};
                            obj.userId = response[i].userid;
                            obj.userName = response[i].userName;
                            obj.avatar = response[i].imageUrl;
                            obj.defaultSubjectId = response[i].defaultSubjectId;
                            obj.isOnline = response[i].isOnline;
                            students.push(obj);
                        }
                        $scope.listStudentSubscribed = students;
                    }else{
                        $scope.listStudentSubscribed = [];
                    }

                }
            });
        }

        function getActivityStudent() {
            SideBarRightService.getStudentActivity(userId, defaultLimit, defaultOffet).then(function (response) {
                if(response.data.status){
                    var result = response.data.request_data_result;
                    if(result){
                        $scope.listStudentActivity = result;
                    }
                }
            });
        }
        $scope.convertUnixTimeToTime = function (time) {
            return convertUnixTimeToTime(time);
        }

    }]);
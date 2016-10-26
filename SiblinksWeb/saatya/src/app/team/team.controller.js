brotControllers.filter('fillterTeam', function() {
    return function(items, name) {
        var filtered = [];
        var fullName = "";
        var nameSearch = name;
        if(!isEmpty(name)){
            nameSearch = name.toLowerCase();
        }
        angular.forEach(items, function(el) {
            fullName =  el.firstName + ' ' + el.lastName;
            if((!isEmpty(fullName) && fullName.toLowerCase().indexOf(nameSearch)>-1)
                || (!isEmpty(el.accomplishments) && el.accomplishments.toLowerCase().indexOf(nameSearch)>-1) || isEmpty(name)) {
                filtered.push(el);
            }
        });
        return filtered;
    }
});
brotControllers.controller('TeamCtrl', ['$scope', '$rootScope', '$log', '$location', '$sce', 'TeamMentorService','myCache','VideoService',
    function ($scope, $rootScope, $log, $location, $sce, TeamMentorService,myCache,VideoService) {

        var limit = "500";
        var offset = "0";
        $scope.type = "subs";
        brot.signin.statusStorageHtml();
        var LIMIT_SUBJECT = 4;
        $scope.login = 0;

        var userId = localStorage.getItem('userId');
        init();

        function init() {
            if(isEmpty(userId)){
                userId = -1;
            }
            selectMentorTop(limit, offset, $scope.type,userId);

        }

        $scope.selectType = function (type) {
            $scope.type = type + "";
            selectMentorTop(limit, offset, type);
        }
        $scope.isSubscribe = 0;

        $scope.hoverSubcribe = function (isSubs,userid) {
            if(isSubs != '1'||isEmpty(userid))
            {
                return ;
            }
            $("#subscribers_"+userid).attr("data-icon","M");

            $("#span_"+userid).text("Unsubscribe");

        };
        $scope.unHoverSubcribe = function (isSubs,userid) {
            if(isSubs != '1'||isEmpty(userid))
            {
                return ;
            }
            $("#subscribers_"+userid).attr("data-icon","N");
            $("#span_"+userid).text("Subscribe");

        };

        $scope.setSubscribeMentor = function (mentorId) {
            if(isEmpty(userId)||userId == -1){
                return ;
            }
            VideoService.setSubscribeMentor(userId, mentorId+"").then(function (data) {
                if(data.data.status =="true") {
                    if (data.data.request_data_type == "subs") {
                        $scope.isSubscribe = 1;
                        //$('#subscribers_'+mentorId).addClass('unsubcrib');
                    }
                    else {
                        $scope.isSubscribe = 0;
                        $("#span_"+mentorId).text('Subscribe');
                        $("#subscribers_"+mentorId).attr("data-icon","N");
                        $('#subscribers_'+mentorId).removeClass('unsubcrib');
                    }
                    for(var i = 0;i < $scope.listTopmentors.length;i++){
                        if(mentorId == $scope.listTopmentors[i].userid){
                            $scope.listTopmentors[i].isSubs = $scope.isSubscribe;
                        }
                    }


                }
            });
        };


        function selectMentorTop(limit, offset, type) {
            TeamMentorService.getTopMentorsByType(limit, offset, type,userId).then(function (data) {
                var listTopMentors =[];

                var subjects = myCache.get("subjects");
                if (data.data.status) {
                    $scope.countAll = data.data.request_data_result.length;

                    if ($scope.countAll == null) {
                        $scope.errorData = DATA_ERROR.noDataFound;
                    }
                    else {
                        var data_result = data.data.request_data_result;
                        for (var i = 0; i < data_result.length; i++) {

                            var mentor = {};
                            mentor.userid = data_result[i].userid;
                            mentor.lastName = data_result[i].lastName;
                            mentor.firstName= data_result[i].firstName;
                            mentor.accomplishments = (isEmpty(data_result[i].accomplishments) ? "" : data_result[i].accomplishments);
                            mentor.bio= data_result[i].bio;
                            mentor.numsub= data_result[i].numsub;
                            mentor.imageUrl = data_result[i].imageUrl;
                            mentor.numlike= data_result[i].numlike;
                            mentor.numvideos= data_result[i].numvideos;
                            mentor.numAnswers= data_result[i].numAnswers;
                            mentor.isSubs= data_result[i].isSubs;
                            mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                            mentor.avgrate = data_result[i].avgrate;
                            if(isEmpty(mentor.lastName+mentor.firstName)){
                                mentor.firstName = data_result[i].loginName;
                            }
                            var listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                            var strSubject="";
                            if (listSubject != null && listSubject !== undefined) {
                            	var listSubjectName =[];
                                var len =listSubject.length;
                                var isMax = false;
                                if(len > LIMIT_SUBJECT){
                                    len = LIMIT_SUBJECT;
                                    isMax = true;
                                }
                            	for (var j = 0; j < len; j++) {
                            		listSubjectName.push(listSubject[j].name);
                            	}
                            	strSubject = listSubjectName.join(", ");
                                if(isMax){
                                    strSubject = strSubject + '...';
                                }
                            }
                            mentor.listSubject =strSubject;
                            listTopMentors.push(mentor);
                        }
                        $scope.listTopmentors = listTopMentors;
                        $scope.showItem = true;
                    }


                }
            });
        }

    }]);

brotControllers.controller('TeamCtrl', ['$scope', '$rootScope', '$log', '$location', '$sce', 'TeamMentorService','myCache','VideoService',
    function ($scope, $rootScope, $log, $location, $sce, TeamMentorService,myCache,VideoService) {


        var limit = "50";
        var offset = "0";
        $scope.type = "subs";
        brot.signin.statusStorageHtml();
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

            $("#span_"+userid).text("unsubscribe");

        };
        $scope.unHoverSubcribe = function (isSubs,userid) {
            if(isSubs != '1'||isEmpty(userid))
            {
                return ;
            }
            $("#subscribers_"+userid).attr("data-icon","N");
            $("#span_"+userid).text("subscribe");

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
                            mentor.lastName = (data_result[i].lastName == null || data_result[i].lastName === undefined)? "" : data_result[i].lastName;
                            mentor.firstName= (data_result[i].firstName == null || data_result[i].firstName === undefined)? "" : data_result[i].firstName;
                            mentor.accomplishments= (data_result[i].accomplishments == null || data_result[i].accomplishments === undefined)? "" : data_result[i].accomplishments;
                            mentor.bio= data_result[i].bio;
                            mentor.numsub= data_result[i].numsub;
                            mentor.imageUrl = data_result[i].imageUrl;
                            mentor.numlike= data_result[i].numlike;
                            mentor.numvideos= data_result[i].numvideos;
                            mentor.numAnswers= data_result[i].numAnswers;
                            mentor.isSubs= data_result[i].isSubs;
                            mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                            mentor.avgrate = data_result[i].avgrate;
                            var listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                            var strSubject="";
                            if (listSubject != null && listSubject !== undefined) {
                            	var listSubjectName =[];
                            	for (var j = 0; j < listSubject.length; j++) {
                            		listSubjectName.push(listSubject[j].name);
                            	}
                            	strSubject = listSubjectName.join(", ");
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
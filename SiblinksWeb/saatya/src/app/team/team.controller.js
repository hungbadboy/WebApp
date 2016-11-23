brotControllers.filter('fillterTeam', function() {
    return function(items, name) {
        var filtered = [];
        var fullName = "";
        var nameSearch = name;
        if(!isEmpty(name)){
            nameSearch = name.toLowerCase();
        }
        angular.forEach(items, function(el) {
            fullName =  displayUserName(el.firstName,el.lastName,el.loginName);
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
        $scope.listTopmentors = [];
        var subjects = JSON.parse(localStorage.getItem('subjects'));
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
            $("#span_"+userid).text("Subscribed");

        };

        $scope.setSubscribeMentor = function (mentorId) {
            if(isEmpty(userId)||userId == -1){
                return ;
            }
            try {
            	$rootScope.$broadcast('open');
	            VideoService.setSubscribeMentor(userId, mentorId+"").then(function (data) {
	                if(data.data.status =="true") {
	                    if (data.data.request_data_type == "subs") {
	                        $scope.isSubscribe = 1;
	                        //$('#subscribers_'+mentorId).addClass('unsubcrib');
	                    } else {
	                        $scope.isSubscribe = 0;
	                        $("#span_"+mentorId).text('Subscribe');
	                        $("#subscribers_"+mentorId).attr("data-icon","N");
	                        $('#subscribers_'+mentorId).removeClass('unsubcrib');
	                    }
	                    for(var i = 0;i < $scope.listTopmentors.length;i++){
	                        if(mentorId == $scope.listTopmentors[i].userid){
	                            $scope.listTopmentors[i].isSubs = $scope.isSubscribe;
	                            $scope.listTopmentors[i].numsub = ($scope.isSubscribe == 0)?$scope.listTopmentors[i].numsub -1:$scope.listTopmentors[i].numsub +1;
	                        }
	                    }
	                }
	            });
            } catch(er) {
            	console.log(er.description);
            } finally {
            	$rootScope.$broadcast('close');
            }
        };


        function selectMentorTop(limit, offset, type) {
        	$scope.listTopmentors = [];
            TeamMentorService.getTopMentorsByType(limit, offset, type,userId).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result == null) {
                        $scope.errorData = DATA_ERROR.noDataFound;
                    } else {
                        var data_result = data.data.request_data_result;
                        for (var i = 0; i < data_result.length; i++) {
                            var listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                            var strSubject="";
                            var strSubjectLimit ="";
                            if (listSubject != null && listSubject !== undefined) {
                            	var listSubjectName= [];
                            	var listSubjectNameLimit= [];
                            	for (var j = 0; j < listSubject.length; j++) {
                            		listSubjectName.push(listSubject[j].name);
                            		if(j < LIMIT_SUBJECT){
                            			listSubjectNameLimit.push(listSubject[j].name);
                            		} else if (j == LIMIT_SUBJECT) {
                            			listSubjectNameLimit.push(listSubject[j].name +'...');
                            		}
                            	}
                            	strSubjectLimit = listSubjectNameLimit.join(", ");
                            	strSubject = listSubjectName.join(", ");
                            }
                            data_result[i].listSubject = strSubject;
                            data_result[i].subjectLimit =strSubjectLimit;
                            $scope.listTopmentors.push(data_result[i]);
                        }
                        $scope.countAll = data_result.length;
                        $scope.showItem = true;
                    }
                }
            });
        }
        
        $scope.toFixed = function roundNumber(value, precision) {
        	return toFixed(value, precision);
        }
    }]);
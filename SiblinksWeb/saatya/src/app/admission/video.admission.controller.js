brotControllers.controller('VideoAdmissionController', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$sce', '$timeout',
    '$window', 'VideoService', 'CommentService', 'videoAdmissionService',
    function ($scope, $rootScope, $routeParams, $http, $location, $sce, $timeout, $window,
              VideoService,
              CommentService, videoAdmissionService) {
        $scope.userId = localStorage.getItem('userId');
        $scope.avartar = localStorage.getItem('imageUrl');

        var videoid = $routeParams.videoid;

        $scope.listVideos = null;
        $scope.clickD = 1;

        $scope.keySearch = $rootScope.keySearch;

        $scope.addressCurrent = $location.absUrl();
        $scope.rated = false;
        $scope.videosRelatedError = "";
        $scope.loadRate = false;

        var LIMIT_VIDEO = 5;
        var OFFSET = 0;

        var player;
        init();

        function init() {

            if (isEmpty(videoid)) {
                return;
            }

            videoAdmissionService.getVideoDetailById(videoid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.errorInfo = "Video not found";
                    }
                    else {
                        $scope.videoInfo = data.data.request_data_result[0];
                        $scope.rateNum = $scope.videoInfo.averageRating;
                        if(isEmpty($scope.videoInfo.description)){
                            $scope.videoInfo.description = "No description";
                        }
                        $scope.loadRate = true;
                        currentvid = data.data.request_data_result[0].vid;
                        var url = data.data.request_data_result[0].url;
                        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                        if (videoid != null) {
                            onYouTubeIframeAPIReady(videoid[1]);
                            videoAdmissionService.getVideoByAdmissionId($scope.videoInfo.idAdmission, LIMIT_VIDEO, OFFSET).then(function (data) {
                                if (data.data.status == 'true') {
                                    if (data.data.request_data_result.length == 0) {
                                        $scope.videosRelatedError = "No videos";
                                    }
                                    else {
                                        $scope.videosRelated = data.data.request_data_result;
                                    }

                                }

                            });
                          
                          // Get user rating
                          videoAdmissionService.getUserRatingVideoAdmission($scope.userId, $scope.videoInfo.vid).then(function (data) {
                          if (data.data.status == 'true') {
                              if (data.data.request_data_result.length > 0) {
                                  $scope.numRating = data.data.request_data_result[0].rating;
                              } else {
                            	  $scope.numRating = 0
                              }
                           }
                          });
                        }
                    }

                }

            });


            videoAdmissionService.getCommentVideoById(videoid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.nocommentInfo = "Have no comment";
                    }
                    else {
                        $scope.comments = data.data.request_data_result;
                    }

                }

            });

            videoAdmissionService.updateViewVideo(videoid).then(function (data) {
                if (data.status == 'false') {
                    console.log("Update view error");
                }
            });

        }

        $scope.viewVideo = function (vid) {
            videoAdmissionService.getVideoDetailById(vid + '').then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.errorInfo = "Video not found";
                    }
                    else {
                        $scope.videoInfo = data.data.request_data_result[0];
                        if(isEmpty($scope.videoInfo.description)){
                            $scope.videoInfo.description = "No description";
                        }
                        $scope.rateNum = $scope.videoInfo.averageRating;
                        var url = data.data.request_data_result[0].url;
                        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                        if (videoid != null) {
                            onYouTubeIframeAPIReady(videoid[1]);
                            $location.path('/videos/detailVideo/' + data.data.request_data_result[0].vid);
                        }

                    }

                }

            });


            videoAdmissionService.getCommentVideoById(vid + '').then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.nocommentInfo = "Have no comment";
                    }
                    else {
                        $scope.comments = data.data.request_data_result;
                    }

                }

            });

        }

        $scope.clickTxtComent = function(){
            $(".comment-action").show();
        };
        $scope.cencelComment = function(){
            $("#add-comment").val('');
            $(".comment-action").hide();
        };


        $scope.convertTime = function (str) {
            return convertUnixTimeToTime(str);
        }

        $scope.rateFunction = function (rate) {
            var ratenumOld = $scope.rateNum;
            if (isEmpty($scope.userId) || $scope.userId == "-1") {
            	window.location.href ='#/student/signin?continue='+encodeURIComponent($location.absUrl());
                return;
            }
            try {
	            $rootScope.$broadcast('open');
	            videoAdmissionService.rateVideoAdmission($scope.userId, videoid, parseInt(rate)).then(function (data) {
	                if (data.data.status == 'true') {
	                	var averageRatingOld = $scope.videoInfo.averageRating == null ? 0: $scope.videoInfo.averageRating;
		            	var numRatingsOld = $scope.videoInfo.numRatings == null ? 0: $scope.videoInfo.numRatings;
		            	var ratingOld = ($scope.numRating == null)? 0 : $scope.numRating;
		            	$scope.numRating = parseInt(rate);
		            	$scope.videoInfo.averageRating = toFixed((( averageRatingOld * numRatingsOld) + parseInt(rate - ratingOld))/ ((ratingOld == 0)? numRatingsOld + 1 : numRatingsOld),1);
		            	$scope.videoInfo.numRatings = (ratingOld == 0)? numRatingsOld + 1 : numRatingsOld;
	                }
	            });
            } catch(e){
            	console.log(e.description)
            } finally{
            	$rootScope.$broadcast('close');
            }
        }

        function onYouTubeIframeAPIReady(youtubeId) {
            player = new YT.Player('video', {
                height: '430',
                width: '100%',
                videoId: youtubeId,
                events: {
                    'onReady': onPlayerReady,
                    'onStateChange': onPlayerStateChange
                },
                playerVars: {
                    showinfo: 0,
                    autohide: 1,
                    theme: 'dark'
                }
            });
        }

        function onPlayerReady(event) {
            event.target.playVideo();
        }

        function onPlayerStateChange(event) {
            if (event.data === 0) {
                $location.path( '#/videos/detailVideo/' + videoid);
            }
        }


        $scope.deleteComment = function (cid) {
            $('#deleteItem').modal('show');
            idRemove = cid;
        };
        $scope.decodeContent= function (str) {
           return decodeURIComponent(str);
        };


        $scope.addComment = function () {
            var content = $('#add-comment').val();
            if (isEmpty(content)) {
                return;
            }
            if (isEmpty($scope.userId)) {
                $scope.errorVideo = "Please login";
                return;
            }
            try {
	            $rootScope.$broadcast('open');
	            videoAdmissionService.addCommentVideo($scope.userId, content, videoid).success(function (data) {
	                if (data.status == 'true') {
	                    $("#add-comment").val('');
	                    $(".comment-action").hide();
	                    videoAdmissionService.getCommentVideoById(videoid).then(function (data) {
	                        if (data.data.status == 'true') {
	                            if (data.data.request_data_result.length == 0) {
	                                $scope.nocommentInfo = "Have no comment";
	                            } else {
	                                $scope.comments = data.data.request_data_result;
	                            }
	                            // Update numComments
		                        $scope.videoInfo.numComments = ($scope.videoInfo.numComments == null)? 1 : $scope.videoInfo.numComments +1;
	                        }
	                    });
	                }
	            });
            } catch(e){
            	console.log(e.description)
            } finally{
            	$rootScope.$broadcast('close');
            }
        }

        $scope.updateComment = function () {
            var content = CKEDITOR.instances['discuss'].getData();
            CommentService.editComment(editCommentId, content).then(function (data) {
                if (data.data.request_data_result) {
                    listDiscuss = [];
                    for (var i = 1; i <= $scope.clickD; i++) {
                        getDiscuss(i);
                    }
                }
            });
        };

        $scope.deleteItem = function () {
        	try {
        		$rootScope.$broadcast('open');
        		CommentService.deleteComment(idRemove).then(function (data) {
        			if (data.data.status == 'true') {
        				listDiscuss = [];
        				for (var i = 1; i <= $scope.clickD; i++) {
        					getDiscuss(i);
        				}
        				// Update numComments
        				$scope.videoInfo.numComments = $scope.videoInfo.numComments - 1;
        			}
        		});
        	} catch(e){
            	console.log(e.description)
            } finally{
            	$rootScope.$broadcast('close');
            }
        };
    }]);
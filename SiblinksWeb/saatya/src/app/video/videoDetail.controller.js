brotControllers.controller('VideoDetailCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$sce', '$timeout',
    '$window', 'VideoService', 'CommentService', 'videoDetailService',
    function ($scope, $rootScope, $routeParams, $http, $location, $sce, $timeout, $window,
              VideoService,
              CommentService, videoDetailService) {
        $scope.userId = localStorage.getItem('userId');
        $scope.avartar = localStorage.getItem('imageUrl');

        var userName = localStorage.getItem('nameHome') != null ?  localStorage.getItem('nameHome') : "";

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

        var currentVid = 0;
        var player;
        var idRemove, editCommentId, listDiscuss = [];

        init();

        function init() {

            if (isEmpty(videoid)) {
                return;
            }

            videoDetailService.getVideoDetailById(videoid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.errorInfo = "Video not found";
                    } else {
                        $scope.videoInfo = data.data.request_data_result[0];
                        $scope.rateNum = $scope.videoInfo.averageRating;
                        if(isEmpty($scope.videoInfo.description)){
                            $scope.videoInfo.description = "No description";
                        }
                        $scope.loadRate = true;
                        currentVid = data.data.request_data_result[0].vid;
                        var url = data.data.request_data_result[0].url;
                        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                        if (videoid != null) {
                            onYouTubeIframeAPIReady(videoid[1]);
                            videoDetailService.getVideoByCategoryId($scope.videoInfo.subjectId, LIMIT_VIDEO, OFFSET).then(function (data) {
                                if (data.data.status == 'true') {
                                    if (data.data.request_data_result.length == 0) {
                                        $scope.videosRelatedError = "No videos";
                                    }
                                    else {
                                        $scope.videosRelated = data.data.request_data_result;
                                    }

                                }

                            });

                            videoDetailService.checkSubscribe($scope.videoInfo.userid, $scope.userId).then(function (data) {
                                if (data.data.status == 'true') {
                                    if (data.data.request_data_result[0].isSubs == '0') {
                                        $scope.isSubscribe = 0;
                                    }
                                    else {
                                        $scope.isSubscribe = 1;
                                    }
                                }
                            });
                            VideoService.checkFavouriteVideo($scope.userId, $scope.videoInfo.vid).then(function (data) {
                                if (data.data.status == 'true') {
                                    $scope.isFavorite = 1;
                                }
                                else {
                                    $scope.isFavorite = 0;
                                }
                            });
                            
                            // Get User Rate
                            if ($scope.userId != null && $scope.userId !== undefined) {
                          	  VideoService.getUserRatingVideo($scope.userId, $scope.videoInfo.vid).then(function (data) {
                          		  if (data.data.status == 'true') {
                          			  if (data.data.request_data_result.length > 0) {
                          				  	$scope.numRating = data.data.request_data_result[0].rating;
                                      } else {
                                      	$scope.numRating = 0;
                                      }
                          		  }
                          	  });
                          }
                        }
                    }

                }

            });


            videoDetailService.getCommentVideoById(videoid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.nocommentInfo = "Have no comment";
                    }
                    else {
                        $scope.comments = data.data.request_data_result;
                    }

                }

            });

            if (!isEmpty($scope.userId)) {
                videoDetailService.updateVideoHistory(videoid, $scope.userId).then(function (data) {
                    if (data.status == 'false') {
                        console.log("Update view error");
                    }
                });
            }

        }

        $scope.viewVideo = function (vid) {
            videoDetailService.getVideoDetailById(vid + '').then(function (data) {
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
                            if (player === undefined) {
                                onYouTubeIframeAPIReady(videoid[1]);
                            }
                            else {
                                player.loadVideoById(videoid[1]);
                            }
                            $location.path('#/videos/detailVideo/' + data.data.request_data_result[0].vid);
                        }
                    }
                }
            });

            videoDetailService.getCommentVideoById(vid + '').then(function (data) {
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

        $scope.nextVideo = function (str) {
            if (!isEmpty($scope.videosRelatedError)) {
                return;
            }
            var newvid;
            if ((str + '') == 'back') {
                newvid = checkVideoInListToBack($scope.videosRelated, currentVid);
            }
            else {
                newvid = checkVideoInListToNext($scope.videosRelated, currentVid);
            }
            currentVid = newvid;

            videoDetailService.getVideoDetailById(newvid).then(function (data) {
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
                            if (player === undefined) {
                                onYouTubeIframeAPIReady(videoid[1]);
                            }
                            else {
                                player.loadVideoById(videoid[1]);
                            }
                            $location.path('/videos/detailVideo/' + data.data.request_data_result[0].vid,false);
                        }

                    }

                }

            });


            videoDetailService.getCommentVideoById(newvid).then(function (data) {
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

        function checkVideoInListToNext(videosRelated, _currentVid) {
            var leng = videosRelated.length;
            for (var i = 0; i < videosRelated.length; i++) {
                if (_currentVid == videosRelated[i].vid) {
                    if (i == leng - 1) {
                        return videosRelated[0].vid;
                    }
                    else {
                        return videosRelated[i + 1].vid;
                    }
                }
            }
            return videosRelated[0].vid;
        }

        function checkVideoInListToBack(videosRelated, _currentVid) {
            var leng = videosRelated.length;
            for (var i = 0; i < videosRelated.length; i++) {
                if (_currentVid == videosRelated[i].vid) {
                    if (i == 0) {
                        return videosRelated[leng - 1].vid;;
                    }
                    else {
                        return videosRelated[i - 1].vid;
                    }
                }
            }
            return videosRelated[leng - 1].vid;
        }

        $scope.convertTime = function (str) {
            return convertUnixTimeToTime(str);
        };

        $scope.searchEnter = function () {
            var searchValue = angular.element("input#srch-term").val();
            $window.location.href = '#/videos?search='+encodeURIComponent(searchValue);
        };

        // Rating video detail
        $scope.rateFunction = function (rate) {
            if (isEmpty($scope.userId) || $scope.userId == "-1") {
                $window.location.href ='#/student/signin?continue='+encodeURIComponent($location.absUrl());
            }
            
            try {
	            $rootScope.$broadcast('open');
		        VideoService.rateVideo($scope.userId, videoid, parseInt(rate)).then(function (data) {
		            if (data.data.status == 'true') {
		            	var averageRatingOld = $scope.videoInfo.averageRating == null ? 0: $scope.videoInfo.averageRating;
		            	var numRatingsOld = $scope.videoInfo.numRatings == null ? 0: $scope.videoInfo.numRatings;
		            	var ratingOld = ($scope.numRating == null)? 0 : $scope.numRating;
		            	$scope.numRating = parseInt(rate);
		            	$scope.videoInfo.averageRating = toFixed((( averageRatingOld * numRatingsOld) + parseInt(rate - ratingOld))/ ((ratingOld == 0)? numRatingsOld + 1 : numRatingsOld),1);
		            	$scope.videoInfo.numRatings = (ratingOld == 0)? numRatingsOld + 1 : numRatingsOld;
		            }
		        });
            } catch (e) {
                console.log(e.description);
             } finally {
            	 $rootScope.$broadcast('close');
             }

        };
        $scope.range = function (min, max, step) {
            step = step || 1;
            var input = [];
            for (var i = min; i <= max; i += step) {
                input.push(i);
            }
            return input;
        };
        $scope.addfavourite = function (vid) {
            if ($scope.isFavorite == 1) {
                return;
            }
            try {
	            $rootScope.$broadcast('open');
	            VideoService.addfavourite($scope.userId, vid).then(function (data) {
	                if (data.data.request_data_result == 'Favourite add successful') {
	                    $('#btnFavorite').addClass('btn-warning');
	                    $scope.isFavorite = 1;
	                } else {
	                    $scope.isFavorite = 0;
	                    $('#btnFavorite').removeClass('btn-warning');
	                }
	            });
         } catch (e) {
            console.log(e.description);
         } finally {
        	 $rootScope.$broadcast('close');
         }

        };

        function onYouTubeIframeAPIReady(youtubeId) {
            player = new YT.Player('video', {
                height: '430',
                width: '100%',
                videoId: youtubeId,
                events: {
                    'onReady': onPlayerReady
                    //'onStateChange': onPlayerStateChange
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
        	 try {
	            var content = $('#add-comment').val();
	            if (isEmpty(content)) {
	                return;
	            }
	            if (isEmpty($scope.userId)) {
	                $scope.errorVideo = "Please login";
	                return;
	            }
	            if($scope.videoInfo){
	                var objRequest = {
	                    authorID : $scope.videoInfo.userid,
	                    content : content,
	                    vid : videoid,
	                    uid: $scope.userId,
	                    title : $scope.videoInfo.title,
	                    subjectId : $scope.videoInfo.subjectId,
	                    author : userName
	                };
	                $rootScope.$broadcast('open');
	                videoDetailService.addCommentVideo(objRequest).success(function (data) {
	                    if (data.status == 'true') {
	                        $("#add-comment").val('');
	                        $(".comment-action").hide();
	                        videoDetailService.getCommentVideoById(videoid).then(function (data) {
	                            if (data.data.status == 'true') {
	                                if (data.data.request_data_result.length == 0) {
	                                    $scope.nocommentInfo = "Have no comment";
	                                } else {
	                                    $scope.comments = data.data.request_data_result;
	                                }
	                            }
	                        });
	                        // Update numComments
	                        $scope.videoInfo.numComments = $scope.videoInfo.numComments + 1;
	                    }
	                });
	            }
        	 } catch (e) {
                console.log(e.description);
             } finally {
            	 $rootScope.$broadcast('close');
             }
        };

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

        $scope.setSubscribeMentor = function (mentorId) {
        	    if (isEmpty($scope.userId)) {
	                $window.location.href = '#/student/signin?continue='+encodeURIComponent($location.absUrl());
	                return;
	            }
        	    try {
		            $rootScope.$broadcast('open');
		            VideoService.setSubscribeMentor($scope.userId, mentorId + "").then(function (data) {
		                if (data.data.status == "true") {
		                    if (data.data.request_data_type == "subs") {
		                        $scope.isSubscribe = 1;
		                        angular.element('#spansubs').text('Subscribed');
		                    }
		                    else {
		                        $scope.isSubscribe = 0;
		                        angular.element('#spansubs').text('Subscribe');
		                    }
		                }
		            });
        	 } catch(e) {
            	 console.log(e.description);
             } finally {
            	 $rootScope.$broadcast('close'); 
             }
        };

        $scope.deleteItem = function () {
        	try {
        	$rootScope.$broadcast('open');
            CommentService.deleteComment(idRemove).then(function (data) {
                if (data.data.status == true) {
                    listDiscuss = [];
                    for (var i = 1; i <= $scope.clickD; i++) {
                        getDiscuss(i);
                    }
                    // Update numComments
                    $scope.videoInfo.numComments = $scope.videoInfo.numComments - 1;
                }
            });
         } catch(e) {
        	 console.log(e.description);
         } finally {
        	 $rootScope.$broadcast('close'); 
         }
        };

        $scope.hoverSubscribe = function (isSub) {
            if(isSub != '1' || isEmpty(isSub)){
                return;
            }
            angular.element('.subscribers').attr('data-icon', 'M');
            angular.element('#spansubs').text('Unsubscribe');
        };

        $scope.unHoverSubscribe = function (isSub) {
            if(isSub != 1 || isEmpty(isSub)){
                return;
            }
            angular.element('.subscribers').attr('data-icon', 'N');
            angular.element('#spansubs').text('Subscribed');
        };
        
    }]);
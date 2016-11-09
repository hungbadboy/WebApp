brotControllers.controller('PlaylistDetailCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$sce', '$timeout',
    '$window', 'VideoService', 'SubCategoryVideoService', 'StudentService', 'CommentService', 'videoDetailService',
    function ($scope, $rootScope, $routeParams, $http, $location, $sce, $timeout, $window,
              VideoService, SubCategoryVideoService, StudentService,
              CommentService, videoDetailService) {
        $scope.userId = localStorage.getItem('userId');
        $scope.avartar = localStorage.getItem('imageUrl');

        var pid = $routeParams.playlistid;
        var index = $routeParams.index;
        $scope.index = 0;
        if(!isEmpty(index)){
            $scope.index = parseInt(index);
        }

        $scope.listVideos = null;
        $scope.clickD = 1;

        $scope.keySearch = $rootScope.keySearch;

        $scope.addressCurrent = $location.absUrl();
        $scope.rated = false;
        $scope.videosRelatedError = "";
        $scope.loadRate = false;

        var userName = localStorage.getItem('nameHome') != null ?  localStorage.getItem('nameHome') : "";

        var LIMIT_VIDEO = 5;
        var OFFSET = 0;

        $scope.currentvid = 0;
        var idRemove, editCommentId;
        var player;
        init();

        function init() {

            if (isEmpty(pid)) {
                return;
            }

            videoDetailService.getVideoByPlaylistId(pid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.errorInfo = "Playlist not found";
                        return;
                    }

                    $scope.videosList = data.data.request_data_result;
                    $scope.videoPlaylistInfo = data.data.request_data_result[0];
                    $scope.currentvid = $scope.videosList[$scope.index].vid;
                    videoDetailService.getVideoDetailById($scope.currentvid).then(function (data) {
                        if (data.data.status == 'true') {
                            if (data.data.request_data_result.length == 0) {
                                $scope.errorInfo = "Video not found";
                                return;
                            }

                            $scope.videoInfo = data.data.request_data_result[0];
                            $scope.rateNum = $scope.videoInfo.averageRating;
                            $scope.loadRate = true;
                            $scope.currentvid = data.data.request_data_result[0].vid;
                            var url = data.data.request_data_result[0].url;
                            var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                            if (videoid != null) {
                                onYouTubeIframeAPIReady(videoid[1]);
                                getCommentVideo($scope.currentvid);
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

                            }


                        }

                    });
                }
            });

        }

        function getCommentVideo(videoid) {
        	$scope.comments = {};
            if (!isEmpty($scope.userId)) {
                videoDetailService.updateVideoHistory(videoid, $scope.userId).then(function (data) {
                    if (data.status == 'false') {
                        console.log("Update history error");
                    }
                });
            }
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

        }

        $scope.viewVideo = function (vid) {
            videoDetailService.getVideoDetailById(vid + '').then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.errorInfo = "Video not found";
                    }
                    else {
                        $scope.videoInfo = data.data.request_data_result[0];
                        $scope.rateNum = $scope.videoInfo.averageRating;
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

        $scope.selectVideo = function (index) {
            if (index > $scope.videosList.length) {
                index = 0;
            }
            $scope.index = index;
            var newvid = $scope.videosList[index].vid;
            $scope.currentvid = newvid;
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
                        var url = data.data.request_data_result[0].url;
                        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                        if (videoid != null) {
                            if (player === undefined) {
                                onYouTubeIframeAPIReady(videoid[1]);
                            }
                            else {
                                player.loadVideoById(videoid[1]);
                            }
                            $location.path('/videos/detailPlaylist/' + pid + '/' + $scope.index,false);
                        }

                    }

                }

            });


            videoDetailService.getCommentVideoById(newvid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.nocommentInfo = "Have no comment";
                        $scope.comments={};
                    }
                    else {
                        $scope.comments = data.data.request_data_result;
                    }

                }

            });
        }
        $scope.decodeContent= function (str) {
            return decodeURIComponent(str);
        };
        $scope.searchEnter = function () {
            var txtSearch = $('#srch-term').val();
            $window.location.href = '#/videos?search='+encodeURIComponent(txtSearch);
        }
        //scroll to #id
        $scope.loadTo = function () {
            angular.element(document.getElementById('series-video-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentvid);
        }

        $scope.nextVideo = function (str) {
            if (!isEmpty($scope.videosRelatedError)) {
                return;
            }
            var newvid;
            if ((str + '') == 'back') {
                newvid = checkVideoInListToBack($scope.videosList, $scope.currentvid);

            }
            else {
                newvid = checkVideoInListToNext($scope.videosList, $scope.currentvid);
            }
            $scope.currentvid = newvid;
          // angular.element(document.getElementById('series-video-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentvid);
           //$("#mCSB_5_container").mCustomScrollbar("scrollTo",'#listPlaylist'+$scope.currentvid);
            angular.element(document.getElementById('series-video-list')).mCustomScrollbar('scrollTo','#listPlaylist' + $scope.currentvid);
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
                        VideoService.checkFavouriteVideo($scope.userId, $scope.videoInfo.vid).then(function (data) {
                            if (data.data.status == 'true') {
                                $scope.isFavorite = 1;
                            }
                            else {
                                $scope.isFavorite = 0;
                            }
                        });
                        VideoService.checkFavouriteVideo($scope.userId, $scope.videoInfo.vid).then(function (data) {
                            if (data.data.status == 'true') {
                                if (data.data.status == 'true') {
                                    $scope.isFavorite = 1;
                                }
                                else {
                                    $scope.isFavorite = 0;
                                }
                            }
                        });
                        var url = data.data.request_data_result[0].url;
                        var videoid = url.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
                        if (videoid != null) {
                           if (player === undefined) {
                            onYouTubeIframeAPIReady(videoid[1]);
                            }
                            else {
                                player.loadVideoById(videoid[1]);
                            }
                            $location.path('/videos/detailPlaylist/' + pid +'/'+ $scope.index,false);
                        }

                    }

                }

            });


            videoDetailService.getCommentVideoById(newvid).then(function (data) {
                if (data.data.status == 'true') {
                    if (data.data.request_data_result.length == 0) {
                        $scope.nocommentInfo = "Have no comment";
                        $scope.comments = {};
                    }
                    else {
                        $scope.comments = data.data.request_data_result;
                    }

                }

            });

        }

        function checkVideoInListToNext(videosRelated, currentVid) {
            var leng = videosRelated.length;
            for (var i = 0; i < videosRelated.length; i++) {
                if (currentVid == videosRelated[i].vid) {
                    if (i == leng - 1) {
                        $scope.index = 0;
                        return videosRelated[0].vid;
                    }
                    $scope.index = $scope.index + 1;
                    return videosRelated[i+1].vid;

                }
            }
        }

        function checkVideoInListToBack(videosRelated, currentVid) {
            var leng = videosRelated.length;
            for (var i = 0; i < videosRelated.length; i++) {
                if (currentVid == videosRelated[i].vid) {
                    if (i == 0) {
                        $scope.index = leng-1;
                        return videosRelated[leng-1].vid;
                    }
                    else {
                        $scope.index = $scope.index-1;
                        return videosRelated[i - 1].vid;
                    }
                }
            }

        }

        $scope.convertTime = function (str) {
            return convertUnixTimeToTime(str);
        }

        $scope.rateFunction = function (rate) {
            var ratenumOld = $scope.rateNum;
            if (isEmpty($scope.userId) || $scope.userId == "-1") {
                $window.location.href = '#/student/signin?continue='+encodeURIComponent($location.absUrl());
                return;
            }
            VideoService.checkUserRatingVideo($scope.userId, $scope.videoInfo.vid).then(function (data) {

                if (data.data.status) {
                    if (data.data.request_data_result.length > 0) {
                        $scope.rated = true;
                        $scope.errorVideo = "You are rated!";
                        $scope.rateNum = ratenumOld;
                    } else {
                        VideoService.rateVideo($scope.userId, $scope.videoInfo.vid, parseInt(rate)).then(function (data) {
                            if (data.data.status) {
                                $scope.rateNum = parseInt(rate);
                            }
                        });
                    }
                }
            });

        }


        function onYouTubeIframeAPIReady(youtubeId) {
            player = new YT.Player('video', {
                height: '380',
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
            // if (event.data === 0) {
            //     $location.path( '#/videos/detailVideo/' + $scope.currentvid,false);
            // }
            if (event.data == YT.PlayerState.ENDED) {
               $scope.nextVideo('next');

            }
        }


        $scope.deleteComment = function (cid) {
            $('#deleteItem').modal('show');
            idRemove = cid;
        };

        $scope.editComment = function (cid, discuss) {
            editCommentId = cid;
            $scope.discussOld = discuss;
            $('#editDiscuss').modal('show');
            CKEDITOR.instances['discuss'].setData(decodeURIComponent($scope.discussOld));
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
            $rootScope.$broadcast('open');
            var objRequest = {
                authorID : $scope.videoInfo.userid,
                content : content,
                vid : $scope.currentvid,
                uid: $scope.userId,
                title : $scope.videoInfo.title,
                subjectId : $scope.videoInfo.subjectId,
                author : userName
            };
            videoDetailService.addCommentVideo(objRequest).success(function (data) {
                if (data.status == 'true') {
                    $("#add-comment").val('');
                    $(".comment-action").hide();
                    videoDetailService.getCommentVideoById($scope.currentvid).then(function (data) {
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
                $rootScope.$broadcast('close');
            });

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

        $scope.setSubscribeMentor = function (mentorId) {
            if (isEmpty($scope.userId)) {
                $window.location.href = '#/student/signin?continue='+encodeURIComponent($location.absUrl());
            }
            $rootScope.$broadcast('open');
            VideoService.setSubscribeMentor($scope.userId, mentorId + "").then(function (data) {
                if (data.data.status == "true") {
                    if (data.data.request_data_type == "subs") {
                        $scope.isSubscribe = 1;
                        if($scope.videoInfo){
                            $scope.videoInfo.numsub += 1;
                        }
                    }
                    else {
                        $scope.isSubscribe = 0;
                        if($scope.videoInfo){
                            $scope.videoInfo.numsub -= 1;
                        }
                    }
                }
                $rootScope.$broadcast('close');
            });
        };

        $scope.addfavourite = function (vid) {
            if ($scope.isFavorite == 1) {
                return;
            }
            $rootScope.$broadcast('open');
            VideoService.addfavourite($scope.userId, vid).then(function (data) {
                if (data.data.request_data_result == 'Favourite add successful') {
                    $('#btnFavorite').addClass('btn-warning');
                    $scope.isFavorite = 1;
                }
                else {
                    $scope.isFavorite = 0;
                    $('#btnFavorite').removeClass('btn-warning');
                }
                $rootScope.$broadcast('close');
            });


        }



        $scope.deleteItem = function () {
            CommentService.deleteComment(idRemove).then(function (data) {
                if (data.data.status) {
                    listDiscuss = [];
                    for (var i = 1; i <= $scope.clickD; i++) {
                        getDiscuss(i);
                    }
                }
            });
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
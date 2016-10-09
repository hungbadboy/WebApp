/**
 * Created by Tavv on 06/09/2016.
 */
brotControllers.controller('VideoCtrl', ['$scope', '$http', '$location', '$rootScope', '$timeout', '$log', 'HomeService', 'MentorService', 'TeamMentorService',
    'StudentService', 'VideoService', 'myCache', 'QuestionsService',
    function ($scope, $http, $location, $rootScope, $timeout, $log, HomeService, MentorService, TeamMentorService, StudentService, VideoService, myCache, QuestionsService) {
        /* Video Subscription begin */
        $scope.listCategorySubscription = null;
        $scope.totalSubscription = 0;
        $scope.displayNumberOfVideoRecent = 6;
        $scope.displayNumberOfVideoWeek = 6;
        $scope.displayNumberOfVideoOlder = 6;
        $scope.coutWeek = 0;
        // $scope.isSubscribe = 0;

        $scope.listRecentVideo = {};
        $scope.listWeekVideo = {};
        $scope.listOlderVideo = {};

        $scope.flagShowMoreRecent = true;
        $scope.flagShowMoreWeek = true;
        $scope.flagShowMoreOlder = true;

        $scope.showDivRecent = false;
        $scope.showDivWeek = false;
        $scope.showDivOlder = false;
        $scope.loadRateHistory = false;

        $scope.limitSearchResult = 8;

        $scope.listMentorOlder = [];
        var checkExist = false;

        $scope.userId = localStorage.getItem('userId');

        var masterSubjects = JSON.parse(localStorage.getItem('subjects'));

        $scope.fillListVideoByDefault = function () {

            VideoService.getListCategorySubscription().then(function (data) {
                $scope.listCategorySubscription = data.data.request_data_result;
                resetAttributes();
                if ($scope.listCategorySubscription.length > 0) {
                    VideoService.getListVideoSubscription($scope.userId, -2).then(function (data) {
                        $scope.listVideoSubscription = data.data.request_data_result;
                        if (userId != null && userId !== undefined) {
                            fillAllVideo($scope.listVideoSubscription);
                        }
                        $scope.subjectIdSort = "-2";
                    });
                }
                $scope.searchItem = null;
                $scope.selected = null;
                $scope.isSearchAction = false;
                clearSearch();
            });

        };


        $scope.fillListVideoByDefault();

        $scope.showVideoWithCategory = function (subjectId) {
            $scope.subjectIdSort = subjectId;
            resetAttributes();
            if (userId != null && userId !== undefined) {
                VideoService.getListVideoSubscription($scope.userId, subjectId).then(function (data) {
                    $scope.listVideoSubscription = data.data.request_data_result;
                    fillAllVideo($scope.listVideoSubscription);
                });
            }
        };


        // $scope.isActive = function (index) {
        //     return $scope.selected === index.toString();
        // };


        function resetAttributes() {
            $scope.displayNumberOfVideoRecent = 6;
            $scope.displayNumberOfVideoWeek = 6;
            $scope.displayNumberOfVideoOlder = 6;
            $scope.totalSubscription = 0;
            $scope.listRecentVideo = {};
            $scope.listWeekVideo = {};
            $scope.listOlderVideo = {};
            $scope.showDivRecent = false;
            $scope.showDivWeek = false;
            $scope.showDivOlder = false;
            $scope.flagShowMoreRecent = true;
            $scope.flagShowMoreWeek = true;
            $scope.flagShowMoreOlder = true;
            $scope.listMentorOlder = [];
            clearSearch();
        }

        $scope.isReadyLoadRatedRecently = false;
        $scope.isReadyLoadRatedRecommended = false;
        $scope.isReadyLoadRatedR4U = false;
        $scope.loadRateSubscription = false;
        function fillAllVideo(allVideo) {
            if (typeof allVideo[1] != 'undefined') {
                $scope.listRecentVideo = allVideo[1];
                $scope.totalSubscription += $scope.listRecentVideo.length;
                $scope.showDivRecent = true;
                if ($scope.listRecentVideo.length <= 6) {
                    $scope.flagShowMoreRecent = false;
                }
            }
            if (typeof allVideo[2] != 'undefined') {
                $scope.listWeekVideo = allVideo[2];
                $scope.totalSubscription += $scope.listWeekVideo.length;
                $scope.showDivWeek = true;
                if ($scope.listWeekVideo.length <= 6) {
                    $scope.flagShowMoreWeek = false;
                }
            }
            if (typeof allVideo[3] != 'undefined') {
                $scope.listOlderVideo = allVideo[3];
                $scope.totalSubscription += $scope.listOlderVideo.length;
                $scope.showDivOlder = true;
                if ($scope.listOlderVideo.length <= 6) {
                    $scope.flagShowMoreOlder = false;
                }

                for (var i = 0; i < $scope.listOlderVideo.length; i++) {
                    checkExist = false;
                    for (var j = 0; j < $scope.listMentorOlder.length; j++) {
                        if ($scope.listOlderVideo[i].authorID == $scope.listMentorOlder[j].authorID) {
                            checkExist = true;
                            break;
                        }
                    }
                    if (!checkExist) {
                        $scope.listMentorOlder.push({
                            "authorID": $scope.listOlderVideo[i].authorID,
                            "authorName": $scope.listOlderVideo[i].author,
                            "vid": $scope.listOlderVideo[i].vid,
                            "image": $scope.listOlderVideo[i].image,
                            "title": $scope.listOlderVideo[i].title,
                            "timeStamp": $scope.listOlderVideo[i].timeStamp,
                            "numComments": $scope.listOlderVideo[i].numComments,
                            "numViews": $scope.listOlderVideo[i].numViews,
                            "averageRating": $scope.listOlderVideo[i].averageRating
                        });
                    }
                }
                $scope.loadRateSubscription = true;
            }
        }

        $scope.showMoreVideoRecent = function () {
            if ($scope.listRecentVideo.length > 0 && $scope.displayNumberOfVideoRecent <= $scope.listRecentVideo.length) {
                $scope.displayNumberOfVideoRecent += 6;
            }

            if ($scope.displayNumberOfVideoRecent > $scope.listRecentVideo.length) {
                $scope.flagShowMoreRecent = false;
            }
        };

        $scope.showMoreVideoWeek = function () {
            if ($scope.listWeekVideo.length > 0 && $scope.displayNumberOfVideoWeek <= $scope.listWeekVideo.length) {
                $scope.displayNumberOfVideoWeek += 6;
            }

            if ($scope.displayNumberOfVideoWeek > $scope.listWeekVideo.length) {
                $scope.flagShowMoreWeek = false;
            }
        };

        $scope.showMoreVideoOlder = function () {
            if ($scope.listOlderVideo.length > 0 && $scope.displayNumberOfVideoOlder <= $scope.listOlderVideo.length) {
                $scope.displayNumberOfVideoOlder += 6;
            }

            if ($scope.displayNumberOfVideoOlder > $scope.listOlderVideo.length) {
                $scope.flagShowMoreOlder = false;
            }
        };

        $scope.rangeSubscription = function (count) {
            var ratings = [];
            for (var i = 0; i < count; i++) {
                ratings.push(i)
            }
            return ratings;
        };

        $scope.caculateTimeElapsed = function (time) {
            return caculateTimeElapsed(time);
        };

        $scope.subscrible = function (mentorId) {
            if (isEmpty($scope.userId)) {
                return;
            }
            VideoService.setSubscribeMentor($scope.userId, mentorId).then(function (data) {
                if (data.data.status == "true") {
                    if (data.data.request_data_type == "subs") {
                        $scope.isSubscribe = 1;
                        removeMentor(mentorId);
                    }
                    else {
                        $scope.isSubscribe = 0;
                        $("#subscribers_" + mentorId).attr("data-icon", "N");
                        $('#subscribers_' + mentorId).removeClass('unsubcrib');
                    }
                }
            });
        };

        function removeMentor(mentorId) {
            if ($scope.listMentorOlder !== undefined) {
                for (var i = 0; i < $scope.listOlderVideo.length; i++) {
                    if ($scope.listOlderVideo[i].authorID == mentorId) {
                        updateTopMentorSubscribled($scope.listOlderVideo[i]);
                    }
                }

                for (var i = 0; i < $scope.listMentorOlder.length; i++) {
                    if ($scope.listMentorOlder[i].authorID == mentorId) {
                        $scope.listMentorOlder.splice(i, 1);
                        if ($scope.listMentorOlder.length == 0) {
                            $scope.showDivOlder = false;
                        }
                    }
                }
            }
        }

        function updateTopMentorSubscribled(item) {
            if (item) {
                var listNewTopMentor = $scope.listMentorSubscribed;
                var subjects = myCache.get("subjects");
                var itemConvert = {};
                itemConvert.MentorName = item.author;
                itemConvert.MentorId = item.authorID;
                itemConvert.avatar = item.imageUrl;

                itemConvert.subjects = [];

                for (var i = 0; i < $scope.listCategorySubscription.length; i++) {
                    if ($scope.listCategorySubscription[i].subjectId == item.subjectId) {
                        itemConvert.subjects.push($scope.listCategorySubscription[i].subject);
                        break;
                    }
                }

                listNewTopMentor.unshift(itemConvert);
                $scope.listMentorSubscribed = listNewTopMentor;
            }
        }

        $scope.showProfile = function (event) {
            window.location.href = '#/student/mentorProfile/' + event.currentTarget.getAttribute("id");
        };
        /* Video Subscription end */


        var subjects = [];
        brot.signin.statusStorageHtml();
        $scope.login = 0;

        var userId = localStorage.getItem('userId');

        var nameOfUser = localStorage.getItem("nameHome");

        var ParseType = {
            RECOMMENDED: 0,
            RECENTLY: 1,
            RECOMMENDED_FOR_YOU: 2,
            SUBCRIBE: 3
        };

        var CountType = {
            HOME: 0,
            MENTOR: 1,
            SUBCRIPTION: 2,
            HISTORY: 3,
            FAVOURITE: 4
        };

        var BlockName = {
            RECOMMENDED: 'Recommended',
            RECENTLY: 'Recently Uploaded',
            RECOMMENDED_FOR_YOU: 'Recommended'
        };


        var limit = 8;
        var offset = 0;
        var limitOfLoadMore = 8;
        var increamented = 2;
        var currentPageRecommended = 0, currentPageRecently = 0;
        var isInitRecommended = true;
        var isInitRecently = true;
        var isInitRecommendedForYou = true;
        $scope.totalVideosRecommended = 0;
        $scope.totalVideosRecently = 0;
        $scope.scrollWithBlock = undefined;
        $scope.limitOfRecommendedForU = 2;
        $scope.isShowMoreRecommendedForU = true;
        $scope.isShowMoreRecommended = true;
        $scope.isShowMoreRecently = true;
        $scope.subsWithIndex = -1;
        var hasLoadMore = false;


        init();

        function init() {
            // isInitRecommended = true;
            // isInitRecently = true;
            if (userId !== null) {
                $scope.login = 1;
                //getMentorSubscribed(userId);
                getNewVideoMentorSubscribe(userId);
                getVideoBySubject(userId, -1, limit, 0);
                getCountFactory(CountType.HOME);

            } else {
                getVideoBySubject(-1, -1, limit, 0);
            }
            getAllVideos();
            reloadHistory();
        }

        function reloadHistory() {
            VideoService.getHistoryVideosList(localStorage.getItem('userId')).then(function (data) {
                if (data.data.status) {
                    $scope.listVideos = data.data.request_data_result;
                    for (var i = 0; i < $scope.listVideos.length; i++) {
                        $scope.listVideos[i].timeStamp = $scope.caculateTimeElapsed($scope.listVideos[i].timeStamp)
                    }
                    myCache.put("listVideos", $scope.listVideos);
                    $scope.loadRateHistory = true;
                }
            });
            $scope.numberOfHistoryVideo = 4;
            $scope.flagShowMoreButton = true;
            $scope.searchItem = null;
            $scope.selected = null;
            $scope.isSearchAction = false;
            clearSearch();
        }


        VideoService.getSubjects().then(function (response) {
            if (response.data.status) {
                var subjects = response.data.request_data_result;
                if (subjects) {
                    $scope.listSubjects = subjects;
                }
            }
        });

        function parseDataFactory(type, data) {
            switch (Number(type)) {
                case ParseType.RECOMMENDED:
                    return parseDataVideoRecommended(data);
                case ParseType.RECENTLY:
                    return parseDataVideoRecently(data);
                case ParseType.RECOMMENDED_FOR_YOU:
                    return parseDataVideoRecommendedForU(data);
                case ParseType.SUBCRIBE:
                    return parseDataVideoSubcribe(data);
                default:
                    return null;
            }
        }

        $scope.subjectId = -1;
        $scope.sortBySubject = function (subjectId) {
            resetAllData();
            $scope.subjectId = subjectId;
            if (userId) {
                getVideoBySubject(userId, subjectId, limit, 0);
            } else {
                getVideoBySubject(-1, subjectId, limit, 0);
            }
        };

        function getVideoBySubject(userid, subjectId, limit, offset) {
            if (userid) {
                VideoService.getVideoBySubject(userid, subjectId, limit, offset).then(function (response) {
                    var allData = response.data.request_data_result;
                    if (allData) {
                        var rs_recommended = allData.recommended;
                        if (rs_recommended) {
                            var resultListRecommended = parseDataFactory(ParseType.RECOMMENDED, rs_recommended);
                            if (resultListRecommended != null) {
                                $scope.listVideoRecommended = !hasLoadMore ? resultListRecommended : $scope.listVideoRecommended.concat(resultListRecommended);
                                $scope.totalVideosRecommended = $scope.listVideoRecommended.length;
                                if ($scope.listVideoRecommended.length < limitOfLoadMore) {
                                    $scope.isShowMoreRecommended = false;
                                } else {
                                    $scope.isShowMoreRecommended = true;
                                }
                            }
                        }
                        var data_recommend_4_u = allData.recommended_for_you;
                        if (data_recommend_4_u) {
                            $scope.listVideoRecommendedForU = parseDataFactory(ParseType.RECOMMENDED_FOR_YOU, data_recommend_4_u);
                            if ($scope.listVideoRecommendedForU.length < increamented) {
                                $scope.isShowRecommendedForU = false;
                            } else {
                                $scope.isShowRecommendedForU = true;
                            }
                        }
                        var data_recently = allData.recently;
                        if (data_recently) {
                            var resultVideoRecently = parseDataFactory(ParseType.RECENTLY, data_recently);
                            if (resultVideoRecently != null) {
                                $scope.listVideoRecently = !hasLoadMore ? resultVideoRecently : $scope.listVideoRecently.concat(resultVideoRecently);
                                $scope.totalVideosRecently = $scope.listVideoRecently.length;
                                if ($scope.listVideoRecently.length < limitOfLoadMore) {
                                    $scope.isShowMoreRecently = false;
                                } else {
                                    $scope.isShowMoreRecently = true;
                                }
                            }
                        }
                    }
                });
                return subjectId;
            }
        }


        function getAllVideos() {
            VideoService.getAllVideos().then(function (response) {
                if (response.data.status) {
                    $scope.listAllVideos = response.data.request_data_result;
                }
            });
        }

        $scope.onSelect = function (item) {
            var itemSelected = [];
            itemSelected.push(item.description);
            $scope.searchItem = itemSelected;
            $scope.isSearchAction = true;
        };

        $scope.clearDataSearched = function () {
            $scope.searchItem = null;
            $scope.selected = null;
            $scope.isSearchAction = false;
            clearSearch();
        };


        $scope.isSearchAction = false;
        $scope.msgSearchNotFound = "Search not found";
        $scope.searchEnter = function (data) {
            var searchValue = $("input#srch-term").val();
            var result = $scope.listAllVideos.filter(function (obj) {
                if (obj.title.toLowerCase().indexOf(searchValue.toLowerCase()) != -1) {
                    return obj;
                }
                return null;
            });
            $scope.isSearchAction = true;
            $scope.searchItem = result;
        };
        function findScrollWithBlock() {
            var valueOfBlock = $('.mCSB_scrollTools_onDrag');
            var blockName = valueOfBlock[0].previousSibling.firstElementChild.innerText;
            if (blockName == BlockName.RECOMMENDED) {
                return 1;
            } else if (blockName == BlockName.RECENTLY) {
                return 2;
            } else if (blockName.indexOf('1') != -1) {
                return 3;
            }
        };


        function parseDataVideoRecommendedForU(data) {
            var listVideoRecommended = [];
            var imgTemp = "/images/default_video.png";
            var result_data = data;
            for (var i = 0; i < result_data.length; i++) {
                var item = result_data[i];
                var videosByMentorName = [];
                if (item) {
                    var conditionOfLoop = item.length > 8 ? 8 : item.length;
                    for (var y = 0; y < conditionOfLoop; y++) {
                        var obj = item[y];
                        var videoOfMentor = {};
                        videoOfMentor.userId = obj.userid;
                        videoOfMentor.name = obj.name;
                        videoOfMentor.image = obj.image == null ? imgTemp : obj.image;
                        videoOfMentor.link = obj.url;
                        videoOfMentor.vid = obj.vid;
                        videoOfMentor.title = obj.title;
                        videoOfMentor.avatar = obj.imageUrl;
                        videoOfMentor.defaultSubjectId = obj.defaultSubjectId;
                        videoOfMentor.runningTime = obj.runningTime;
                        videoOfMentor.avgRating = obj.averageRating;
                        videoOfMentor.numFavourite = obj.numFavourite;
                        videoOfMentor.numViews = obj.numViews;
                        videoOfMentor.numComments = obj.numComments;
                        videoOfMentor.countSubscribe = obj.countSubscribe;
                        videoOfMentor.time = convertUnixTimeToTime(obj.timeStamp);
                        videoOfMentor.isSubs = 0;
                        videosByMentorName.push(videoOfMentor);
                    }
                    $scope.isReadyLoadRatedR4U = true;
                    listVideoRecommended.push(videosByMentorName);
                }
            }
            return listVideoRecommended;
        }

        function parseDataVideoRecommended(data) {
            if (data) {
                if (isInitRecommended) {
                    var listVideos = [];
                    for (var i = 0; i < data.length; i++) {
                        var objVideo = {};
                        var video = data[i];
                        objVideo.vid = video.vid;
                        objVideo.title = video.title;
                        objVideo.authorID = video.authorID;
                        objVideo.userName = video.name;
                        objVideo.description = video.description;
                        objVideo.image = video.image;
                        objVideo.link = video.url;
                        objVideo.time = video.runningTime;
                        objVideo.topicId = video.topicId;
                        objVideo.numFavourite = video.numFavourite;
                        objVideo.subjectId = video.subjectId;
                        objVideo.numViews = video.numViews;
                        objVideo.numRatings = video.numRatings;
                        objVideo.numComments = video.numComments;
                        objVideo.averageRating = video.averageRating;
                        objVideo.timeAgo = convertUnixTimeToTime(video.timeStamp);
                        listVideos.push(objVideo);
                    }
                    $scope.isReadyLoadRatedRecommended = true;
                    return listVideos;
                }
                return null;
            }

        }


        function parseDataVideoRecently(data) {
            if (data) {
                if (isInitRecently) {
                    var listVideosRecently = [];
                    for (var i = 0; i < data.length; i++) {
                        var objVideo = {};
                        var video = data[i];
                        objVideo.vid = video.vid;
                        objVideo.authorID = video.authorID;
                        objVideo.userName = video.name;
                        objVideo.title = video.title;
                        objVideo.image = video.image;
                        objVideo.link = video.url;
                        objVideo.numViews = video.numViews;
                        objVideo.subjectId = video.subjectId;
                        objVideo.numRatings = video.numRatings;
                        objVideo.averageRating = video.averageRating;
                        objVideo.numFavourite = video.numFavourite;
                        objVideo.numComments = video.numComments;
                        objVideo.timeAgo = convertUnixTimeToTime(video.timeStamp);
                        objVideo.type = video.type;
                        objVideo.href = video.type == "2" ? "#/videos/detailPlaylist/" + video.vid + "/0" : "#/videos/detailVideo/" + video.vid;
                        listVideosRecently.push(objVideo);
                    }
                    $scope.isReadyLoadRatedRecently = true;
                    return listVideosRecently;
                }
            }
            return null;
        }

        function parseDataVideoSubcribe(data) {
            if (data) {
                var listVideosSubcribe = [];
                for (var i = 0; i < data.length; i++) {
                    var objVideo = {};
                    var video = data[i];
                    objVideo.vid = video.vid;
                    objVideo.authorID = video.authorID;
                    objVideo.userName = video.userName;
                    objVideo.title = video.title;
                    objVideo.image = video.image;
                    objVideo.link = video.url;
                    objVideo.numViews = video.numViews;
                    objVideo.subjectId = video.subjectId;
                    objVideo.numRatings = video.numRatings;
                    objVideo.runningTime = video.runningTime;
                    objVideo.averageRating = video.averageRating;
                    objVideo.timeAgo = convertUnixTimeToTime(video.timeStamp);
                    listVideosSubcribe.push(objVideo);
                }
            }
            return listVideosSubcribe;
        };

        function search(nameKey, myArray) {
            for (var i = 0; i < myArray.length; i++) {
                if (myArray[i].name === nameKey) {
                    return myArray[i];
                }
            }
        };

        function getMentorSubscribed(userId) {
            if (userId) {
                VideoService.getMentorSubscribed(userId, 5, 0).then(function (response) {
                    if (response.data.status) {
                        var result = response.data.request_data_result;
                        if (result) {
                            var listMentorSubscribe = [];
                            for (var i = 0; i < result.length; i++) {
                                var element = result[i];
                                var subscribe = {};
                                subscribe.mentorId = element.userid;
                                subscribe.mentorName = element.name;
                                subscribe.subjectId = element.defaultSubjectId;
                                subscribe.subjects = convertToArray(element.subjects);
                                subscribe.avatar = element.imageUrl;
                                listMentorSubscribe.push(subscribe);
                            }
                            $scope.listMentorSubscribe = listMentorSubscribe;
                            return listMentorSubscribe;
                        }
                    }
                });
            }
        }


        TeamMentorService.getTopMentorsByType(5, 0, 'subcribe', userId).then(function (data) {
            var data_result = data.data.request_data_result;
            var subjects = myCache.get("subjects");
            if (data_result) {
                var listTopMentors = [];
                for (var i = 0; i < data_result.length; i++) {
                    var mentor = {};
                    if (data_result[i].isSubs == 1) {
                        mentor.userid = data_result[i].userid;
                        mentor.userName = data_result[i].userName;
                        mentor.imageUrl = data_result[i].imageUrl;
                        mentor.numsub = data_result[i].numsub;
                        mentor.numvideos = data_result[i].numvideos;
                        mentor.defaultSubjectId = data_result[i].defaultSubjectId;
                        mentor.listSubject = getSubjectNameById(data_result[i].defaultSubjectId, subjects);
                        mentor.numAnswers = data_result[i].numAnswers;
                        listTopMentors.push(mentor);
                    }
                }
                $scope.listMentorBySubs = listTopMentors;
            }
        });


        function convertToArray(strSubjects) {
            return strSubjects != null ? strSubjects.split(',') : null;
        }

        function getNewVideoMentorSubscribe(userId) {
            if (userId) {
                VideoService.getNewVideoMentorSubscribe(userId, 5, 0).then(function (response) {
                    if (response.data.status) {
                        var subjects = myCache.get("subjects");
                        var result = response.data.request_data_result;
                        if (result) {
                            var listMentorSubscribe = [];
                            for (var i = 0; i < result.length; i++) {
                                var element = result[i];
                                var subscribe = {};
                                subscribe.MentorName = element.mentorName;
                                subscribe.MentorId = element.MentorId;
                                subscribe.avatar = element.imageUrl;
                                subscribe.videoNew = element.values_count;
                                subscribe.isOnline = element.isOnline;
                                subscribe.subjects = getSubjectNameById(element.defaultSubjectId, subjects);
                                //console.log(subscribe.subjects);
                                listMentorSubscribe.push(subscribe);
                            }
                            $scope.listMentorSubscribed = listMentorSubscribe;
                            return listMentorSubscribe;
                        }
                    }
                });
            }
        }

        $scope.addCommentVideo = function (content, videoId) {
            //TODO: Handle event when user comment
            VideoService.addComment(userId, nameOfUser, content, videoId).then(function (response) {
                if (response.data.request_data_result[0]) {
                    console.log("Add Comment Successful");
                }
            });

        };

        function getCountAnsSubVideoMentor(mentorId, key) {
            if (mentorId) {
                VideoService.getCountFactory(mentorId, key).then(function (response) {
                    if (response.data.status) {
                        var result = response.data.request_data_result[0];
                        $scope.countResult = result;
                        return result;
                    }
                });
            }
        }

        function getVideoRecommendedForYou(userId) {
            if (userId) {
                VideoService.getVideoRecommendedForYou(userId).then(function (data) {
                    if (data.data.status) {
                        // var listVideoRecommended = {};
                        if (isInitRecommendedForYou) {
                            var listVideoRecommended = [];
                            var imgTemp = "http://ndl.mgccw.com/mu3/app/20140318/19/1395152622265/sss/4_small.png";
                            var result_data = data.data.request_data_result;
                            for (var i = 0; i < result_data.length; i++) {
                                var item = result_data[i];
                                var videosByMentorName = [];
                                if (item) {
                                    // var mentorName = Object.keys(item)[i];
                                    // nameOfMentor.push(mentorName);
                                    var conditionOfLoop = item.length > 8 ? 8 : item.length;
                                    for (var y = 0; y < conditionOfLoop; y++) {
                                        var obj = item[y];
                                        var videoOfMentor = {};
                                        videoOfMentor.userId = obj.userid;
                                        videoOfMentor.name = obj.name;
                                        videoOfMentor.image = obj.image == null ? imgTemp : obj.image;
                                        videoOfMentor.link = obj.url;
                                        videoOfMentor.vid = obj.vid;
                                        videoOfMentor.title = obj.title;
                                        videoOfMentor.avatar = obj.imageUrl;
                                        videoOfMentor.defaultSubjectId = obj.defaultSubjectId;
                                        videoOfMentor.runningTime = obj.runningTime;
                                        videoOfMentor.values_count = obj.values_count;
                                        videoOfMentor.avgRating = obj.averageRating;
                                        videoOfMentor.numViews = obj.numViews;
                                        videoOfMentor.numComments = obj.numComments;
                                        videoOfMentor.countSubscribe = obj.countSubscribe;
                                        videoOfMentor.time = convertUnixTimeToTime(obj.timeStamp);
                                        videosByMentorName.push(videoOfMentor);
                                    }
                                    listVideoRecommended.push(videosByMentorName);
                                }
                            }
                            $scope.isReadyLoadRatedR4U = true;
                            $scope.listVideoRecommended = listVideoRecommended;
                        }
                    }
                });
            }
        }


        function removeObject(index) {
            if ($scope.listVideoRecommendedForU !== undefined) {
                updateTopMentorBlock($scope.listVideoRecommendedForU[0][0]);
                $scope.listVideoRecommendedForU.splice(index, 1);
                if ($scope.listVideoRecommendedForU.length == 0) {
                    var target = angular.element(document.querySelector("div.video-member-title h5"));
                    console.log(target);
                }
            }
        };


        function getCountFactory(type) {
            var key = null;
            switch (Number(type)) {
                case CountType.HOME:
                    key = "home";
                    break;
                case CountType.MENTOR:
                    key = "mentor";
                    break;
                case CountType.SUBSCRIPTION:
                    key = "subcription";
                    break;
                case CountType.HISTORY:
                    key = "history";
                    break;
                case CountType.FAVOURITE:
                    key = "favourite";
                    break;
            }
            if (userId) {
                VideoService.getCountFactory(userId, key).then(function (response) {
                    if (response.data.status) {
                        var result = response.data.request_data_result[0];
                        $scope.countResult = result;
                        return result;
                    }

                    // var countByCategory = [];
                    // for (var i = 0; i < result.length; i++) {
                    //     var category = {};
                    //     category.history = result[i].video_watched;
                    //     category.subcription = result[i].subcribed;
                    //     category.favourite = result[i].video_like;
                    //     countByCategory.push(category);
                    // }
                    //
                    // $scope.countResult = countByCategory;
                });
            }
        }


        $scope.redirectPageFactory = function (page) {
            switch (page) {
                case 'top_mentor':
                    $location.path('/team');
                    break;
                case 'manage_subscription':
                    $location.path('/studentprofile/' + userId);
                    break;
            }
        };

        // $scope.setSubscribeMentor = function (mentorId, index) {
        //     if (isEmpty(userId)) {
        //         return;
        //     }
        //     VideoService.setSubscribeMentor(userId, mentorId + "").then(function (response) {
        //         if (response.data.status == "true") {
        //             if (response.data.request_data_type == "subs") {
        //                 $scope.isSubscribe = 1;
        //                 // removeObject(index);
        //                 console.log("Set Subscribe Mentor Successful");
        //             }
        //             else {
        //                 $scope.isSubscribe = 0;
        //             }
        //         }
        //     });
        // };

        function updateTopMentorBlock(item) {
            if (item) {
                var listNewTopMentor = $scope.listMentorSubscribed;
                var itemConvert = {};
                itemConvert.MentorName = item.name;
                itemConvert.MentorId = item.userId;
                itemConvert.avatar = item.avatar;
                itemConvert.subjects = getSubjectNameById(item.defaultSubjectId, masterSubjects);
                listNewTopMentor.unshift(itemConvert);
                $scope.listMentorSubscribed = listNewTopMentor;
            }
        }


        $scope.isLoadMoreRecommended = false;
        $scope.isLoadMoreRecently = false;
        $scope.loadMoreVideo = function (blockToLoad) {
            // var blockToLoad = findScrollWithBlock();
            // if (blockToLoad == undefined || blockToLoad == null) {
            //     return;
            // }
            hasLoadMore = true;
            var subjectId = $scope.subjectId;
            switch (blockToLoad) {
                case 1 :
                    currentPageRecommended++;
                    if ($scope.isLoadMoreRecommended)
                        return;
                    //$scope.isLoadMoreRecommended = true;
                    var newoffsetRecommended = limitOfLoadMore * currentPageRecommended;
                    isInitRecommended = true;
                    isInitRecently = false;
                    isInitRecommendedForYou = false;
                    if (newoffsetRecommended > $scope.totalVideosRecommended) {
                        newoffsetRecommended = $scope.totalVideosRecommended;
                        $scope.isLoadMoreRecommended = true;
                        $scope.isShowRecommended = false;
                    } else {
                        $scope.isLoadMoreRecommended = false;
                        $scope.isShowRecommended = true;
                        if (userId) {
                            getVideoBySubject(userId, subjectId, limitOfLoadMore, newoffsetRecommended);
                        } else {
                            getVideoBySubject(-1, subjectId, limitOfLoadMore, newoffsetRecommended);
                        }
                    }
                    break;
                case 2 :
                    currentPageRecently++;
                    if ($scope.isLoadMoreRecently)
                        return;
                    // $scope.isLoadMoreRecently = true;
                    var newoffsetRecently = limitOfLoadMore * currentPageRecently;
                    isInitRecently = true;
                    isInitRecommended = false;
                    isInitRecommendedForYou = false;
                    if (newoffsetRecently > $scope.totalVideosRecently) {
                        newoffsetRecently = $scope.totalVideosRecently;
                        $scope.isLoadMoreRecently = true;
                        $scope.isShowRecently = false;
                    }
                    else {
                        $scope.isLoadMoreRecently = false;
                        $scope.isShowRecently = true;
                        if (userId) {
                            getVideoBySubject(userId, subjectId, limitOfLoadMore, newoffsetRecently);
                        } else {
                            getVideoBySubject(-1, subjectId, limitOfLoadMore, newoffsetRecently);
                        }
                    }
                    break;
                case 3 :
                    if ($scope.limitOfRecommendedForU < $scope.listVideoRecommendedForU.length) {
                        $scope.limitOfRecommendedForU += increamented;
                        $scope.isShowMoreRecommendedForU = true;
                    } else {
                        $scope.isShowMoreRecommendedForU = false;
                    }
                    break;
            }
        };


        function clearSearch() {
            $("input#srch-term").val("");
        }

        function detectLengthText() {
            console.log($('.video-description-history').width());
        }


        $scope.flagShowMoreButton = true;
        $scope.loadMoreSearch = function () {
            if ($scope.searchItem.length > 0 && $scope.limitSearchResult < $scope.searchItem.length) {
                $scope.limitSearchResult += 8;
            } else if ($scope.limitSearchResult >= $scope.searchItem.length) {
                $scope.flagShowMoreSearch = false;
            }
        };

        $scope.searchVideos = function (keyword, limit, offset) {
            VideoService.searchVideo(keyword, limit, offset).then(function (response) {
                if (response.data.status) {
                    $scope.results = response.data.request_data_result;
                }
            });
        };

        //MTDU
        var listVideos = '';

        function loadHistory() {
            if (myCache.get("listVideos") !== undefined) {
                $log.info("My cache already exists");
                $scope.listVideos = myCache.get("listVideos");
            } else {
                VideoService.getHistoryVideosList(localStorage.getItem('userId')).then(function (data) {
                    if (data.data.status) {
                        $scope.listVideos = data.data.request_data_result;
                        for (var i = 0; i < $scope.listVideos.length; i++) {
                            $scope.listVideos[i].timeStamp = $scope.caculateTimeElapsed($scope.listVideos[i].timeStamp)
                        }
                        myCache.put("listVideos", $scope.listVideos);
                    }
                });
            }
        }

        $scope.numberOfHistoryVideo = 4;
        $scope.flagShowMoreButton = true;
        $scope.showMore = function () {
            if ($scope.listVideos.length > 0 && $scope.numberOfHistoryVideo < $scope.listVideos.length)
                $scope.numberOfHistoryVideo += 4;
            if ($scope.numberOfHistoryVideo >= $scope.listVideos.length)
                $scope.flagShowMoreButton = false;
        }

        $scope.range = function (num) {
            return new Array(num);
        }


        $scope.resetFlagShowMore = reloadHistory();

        $scope.deleteHistoryVideo = function (video) {
            if ($scope.listVideos == null || $scope.listVideos.length == 0)
                return true;
            if (video == null || video == '')
                video = {"vid": ''};
            VideoService.deleteHistoryVideo(localStorage.getItem('userId'), video.vid).then(function (data) {
                if (data.data.status) {
                    if (video.vid != null && video.vid != "") {
                        $scope.listVideos.splice($scope.listVideos.indexOf(video), 1);
                        if ($scope.listVideos == null || $scope.listVideos.length == 0)
                            $scope.flagShowMoreButton = false;
                    } else {
                        $scope.listVideos.splice(0, $scope.listVideos.length);
                        $scope.flagShowMoreButton = false;
                    }
                    return true;
                }
            });
        };


        /* Favourite Video begin */
        var listVideoFavourite = '';
        $scope.loadRateFavorite = false;
        if (myCache.get("listVideoFavourite") !== undefined) {
            $log.info("My cache already exists");
            $scope.listVideoFavourite = myCache.get("listVideoFavourite");
        } else {
            if (userId === null || userId === undefined) {
                return;
            }
            VideoService.getFavouriteVideosList(localStorage.getItem('userId')).then(function (data) {
                if (data.data.status) {
                    $scope.listVideoFavourite = data.data.request_data_result;
                    for (var i = 0; i < $scope.listVideoFavourite.length; i++) {
                        $scope.listVideoFavourite[i].timeStamp = $scope.caculateTimeElapsed($scope.listVideoFavourite[i].timeStamp)
                    }
                    $scope.loadRateFavorite = true;
                    myCache.put("listVideoFavourite", $scope.listVideoFavourite);
                }
            });
        }

        $scope.numberOfFavouriteVideo = 4;
        $scope.flagShowMoreFavourite = true;
        $scope.showMoreFavourite = function () {
            if (userId === null || userId === undefined) {
                return;
            }
            if ($scope.listVideoFavourite.length > 0 && $scope.numberOfFavouriteVideo < $scope.listVideoFavourite.length)
                $scope.numberOfFavouriteVideo += 4;
            if ($scope.numberOfFavouriteVideo >= $scope.listVideoFavourite.length)
                $scope.flagShowMoreFavourite = false;
        }

        $scope.range = function (num) {
            return new Array(num);
        }

        $scope.resetFlagShowMoreFavourite = function () {
            if (userId === null || userId === undefined) {
                return;
            }
            VideoService.getFavouriteVideosList(localStorage.getItem('userId')).then(function (data) {
                if (data.data.status) {
                    $scope.listVideoFavourite = data.data.request_data_result;
                    for (var i = 0; i < $scope.listVideoFavourite.length; i++) {
                        $scope.listVideoFavourite[i].timeStamp = $scope.caculateTimeElapsed($scope.listVideoFavourite[i].timeStamp)
                    }
                    myCache.put("listVideoFavourite", $scope.listVideoFavourite);
                }
            });
            $scope.numberOfFavouriteVideo = 4;
            $scope.flagShowMoreFavourite = true;
            $scope.searchItem = null;
            $scope.selected = null;
            $scope.isSearchAction = false;
        };


        $scope.deleteFavouriteVideo = function (video) {
            if (userId === null || userId === undefined) {
                return;
            }
            if ($scope.listVideoFavourite == null || $scope.listVideoFavourite.length == 0)
                return true;
            if (video == null || video == '')
                video = {"vid": ''};
            VideoService.deleteFavouriteVideo(localStorage.getItem('userId'), video.vid).then(function (data) {
                if (data.data.status) {
                    if (video.vid != null && video.vid != "") {
                        $scope.listVideoFavourite.splice($scope.listVideoFavourite.indexOf(video), 1);
                        if ($scope.listVideoFavourite == null || $scope.listVideoFavourite.length == 0)
                            $scope.flagShowMoreFavourite = false;
                    } else {
                        $scope.listVideoFavourite.splice(0, $scope.listVideoFavourite.length);
                        $scope.flagShowMoreFavourite = false;
                    }
                    return true;
                }
            });
        }
        /* Favourite Video end */

        function resetAllData() {
            if ($scope.listVideoRecommended) {
                $scope.listVideoRecommended = [];
                currentPageRecommended = 0;
                $scope.totalVideosRecommended = 0;
            }
            if ($scope.listVideoRecently) {
                $scope.listVideoRecently = [];
                currentPageRecently = 0;
                $scope.totalVideosRecently = 0;
            }
            if (!isInitRecommended) {
                isInitRecommended = true;
            }
            if (!isInitRecently) {
                isInitRecently = true;
            }
        }


        $scope.isSubscribe = 0;

        $scope.hoverSubcribe = function (isSubs, userid) {
            if (isSubs != '1' || isEmpty(userid)) {
                return;
            }
            $("#subscribers_" + userid).attr("data-icon", "M");

            $("#span_" + userid).text("Unsubscribe");

        };
        $scope.unHoverSubcribe = function (isSubs, userid) {
            if (isSubs != '1' || isEmpty(userid)) {
                return;
            }
            $("#subscribers_" + userid).attr("data-icon", "N");
            $("#span_" + userid).text("Subscribed");
        };

        $scope.setSubscribeMentor = function (mentorId) {
            if (isEmpty(userId) || userId == -1) {
                return;
            }
            VideoService.setSubscribeMentor(userId, mentorId + "").then(function (data) {
                if (data.data.status == "true") {
                    if (data.data.request_data_type == "subs") {
                        $scope.isSubscribe = 1;

                        //$('#subscribers_'+mentorId).addClass('unsubcrib');
                        for (var i = 0; i < $scope.listVideoRecommendedForU.length; i++) {
                            if (mentorId == $scope.listVideoRecommendedForU[i][0].userId) {
                                $scope.listVideoRecommendedForU[i][0].isSubs = $scope.isSubscribe;
                                updateTopMentorBlock($scope.listVideoRecommendedForU[i][0]);
                            }
                        }
                    }else {
                        $scope.isSubscribe = 0;
                        $("#subscribers_" + mentorId).attr("data-icon", "N");
                        $('#subscribers_' + mentorId).removeClass('unsubcrib');
                        var listMentor = $scope.listMentorSubscribed;
                        for (var i = 0; i < listMentor.length; i++) {
                            if(listMentor[i].MentorId == mentorId){
                                listMentor.splice(i, 1);
                            }
                        }
                        $scope.listMentorSubscribed = listMentor;
                    }
                }
            });
        };


    }]);
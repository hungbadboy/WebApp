brotControllers.controller('VideoTutorialController', ['$route', '$scope', '$rootScope', '$routeParams', '$http', '$location', '$log', 'VideoService', 'SubjectServices', 'CategoriesService', 'HomeService', 'myCache', function ($route, $scope, $rootScope, $routeParams, $http, $location, $log, VideoService, SubjectServices, CategoriesService, HomeService, myCache) {

    $scope.totalSubscription = 0;
    $scope.displayNumberOfVideoRecent = 6;
    $scope.displayNumberOfVideoWeek = 6;
    $scope.displayNumberOfVideoOlder = 6;
    $scope.coutWeek = 0;

    $scope.listRecentVideo = {};
    $scope.listWeekVideo = {};
    $scope.listOlderVideo = {};

    $scope.flagShowMoreRecent = true;
    $scope.flagShowMoreWeek = true;
    $scope.flagShowMoreOlder = true;

    $scope.userId = localStorage.getItem('userId');
    //$scope.userId = 82;

    VideoService.getListCategorySubscription().then(function (data) {
        $scope.listCategorySubscription = data.data.request_data_result;
        resetAttributes();
        if ($scope.listCategorySubscription.length > 0) {
            VideoService.getListVideoSubscription($scope.userId, $scope.listCategorySubscription[0].subjectId).then(function (data) {
                $scope.listVideoSubscription = data.data.request_data_result;
                if (typeof data.data.request_data_result[1] != 'undefined') {
                    $scope.listRecentVideo = data.data.request_data_result[1];
                    $scope.totalSubscription += $scope.listRecentVideo.length;
                }
                if (typeof data.data.request_data_result[2] != 'undefined') {
                    $scope.listWeekVideo = data.data.request_data_result[2];
                    $scope.totalSubscription += $scope.listWeekVideo.length;
                }

                if (typeof data.data.request_data_result[3] != 'undefined') {
                    $scope.listOlderVideo = data.data.request_data_result[3];
                    $scope.totalSubscription += $scope.listOlderVideo.length;
                }

                $scope.selected = $scope.listCategorySubscription[0].subjectId.toString();
            });
        }
    });


    $scope.isActive = function (index) {
        return $scope.selected === index.toString();
    };

    function resetAttributes() {
        $scope.displayNumberOfVideoRecent = 6;
        $scope.displayNumberOfVideoWeek = 6;
        $scope.displayNumberOfVideoOlder = 6;
        $scope.totalSubscription = 0;
        $scope.listRecentVideo = {};
        $scope.listWeekVideo = {};
        $scope.listOlderVideo = {};
    }

    $scope.showVideoWithCategory = function (event) {
        $scope.selected = event.target.id;
        resetAttributes();

        VideoService.getListVideoSubscription($scope.userId, event.target.id).then(function (data) {
            $scope.listVideoSubscription = data.data.request_data_result;
            if (typeof data.data.request_data_result[1] != 'undefined') {
                $scope.listRecentVideo = data.data.request_data_result[1];
                $scope.totalSubscription += $scope.listRecentVideo.length;
            }
            if (typeof data.data.request_data_result[2] != 'undefined') {
                $scope.listWeekVideo = data.data.request_data_result[2];
                $scope.totalSubscription += $scope.listWeekVideo.length;
            }

            if (typeof data.data.request_data_result[3] != 'undefined') {
                $scope.listOlderVideo = data.data.request_data_result[3];
                $scope.totalSubscription += $scope.listOlderVideo.length;
            }
        });
    }

    $scope.showMoreVideoRecent = function () {
        if ($scope.listRecentVideo.length > 0 && $scope.displayNumberOfVideoRecent < $scope.listRecentVideo.length) {
            $scope.displayNumberOfVideoRecent += 6;
        }

        if ($scope.displayNumberOfVideoRecent > $scope.listRecentVideo.length) {
            $scope.flagShowMoreRecent = false;
        }
    }

    $scope.showMoreVideoWeek = function () {
        if ($scope.listWeekVideo.length > 0 && $scope.displayNumberOfVideoWeek < $scope.listWeekVideo.length) {
            $scope.displayNumberOfVideoWeek += 3;
        }

        if ($scope.displayNumberOfVideoWeek > $scope.listWeekVideo.length) {
            $scope.flagShowMoreWeek = false;
        }
    }

    $scope.showMoreVideoOlder = function () {
        if ($scope.listOlderVideo.length > 0 && $scope.displayNumberOfVideoOlder < $scope.listOlderVideo.length) {
            $scope.displayNumberOfVideoOlder += 6;
        }

        if ($scope.displayNumberOfVideoOlder > $scope.listOlderVideo.length) {
            $scope.flagShowMoreOlder = false;
        }
    }

    $scope.rangeSubscription = function (count) {
        var ratings = [];
        for (var i = 0; i < count; i++) {
            ratings.push(i)
        }
        return ratings;
    }

    $scope.caculateTimeElapsed = function (time) {
        var current = new Date().getTime();
        var inputTime = new Date(time);
        var secondElapsed = parseInt(Math.floor((current - inputTime) / 1000));
        secondElapsed = (secondElapsed < 1) ? 1 : secondElapsed;
        var text = '';

        if (years = parseInt((Math.floor(secondElapsed / 31536000))))
            text = years + (years > 1 ? ' years' : ' year') + ' ago';
        else if (months = parseInt((Math.floor(secondElapsed / 2592000))))
            text = months + (months > 1 ? ' months' : ' month') + ' ago';
        else if (weeks = parseInt((Math.floor(secondElapsed / 604800))))
            text = weeks + (weeks > 1 ? ' weeks' : ' week') + ' ago';
        else if (days = parseInt((Math.floor(secondElapsed / 86400))))
            text = days + (days > 1 ? ' days' : ' day') + ' ago';
        else if (hours = parseInt((Math.floor(secondElapsed / 3600))))
            text = hours + (hours > 1 ? ' hours' : ' hour') + ' ago';
        else if (minutes = parseInt((Math.floor(secondElapsed / 60))))
            text = minutes + (minutes > 1 ? ' minutes' : ' minute') + ' ago';
        else
            text = secondElapsed + (secondElapsed > 1 ? ' seconds' : ' second') + ' ago';

        return text;
    }


    var lastRoute = $route.current;
    $rootScope.keySearch = "";
    $scope.$on('$locationChangeSuccess', function (event) {
        if ($route.current.$$route.controller === 'VideoTutorialController') {
            // Will not load only if my view use the same controller
            $route.current = lastRoute;
        }
    });

    $scope.user = localStorage.getItem('userId');
    $scope.labelTutorial = PANEL_VIDEO_TUTORIAL.labelTutorial;
    var subjects = '';
    var subjectId = $routeParams.subjectId;

    if (subjectId === undefined) {
        subjectId = 2;
    }

    if (myCache.get("subjects") !== undefined) {
        $log.info("My cache already exists");
        $scope.subjects = myCache.get("subjects");
        getCategories(subjectId);
    } else {
        HomeService.getSubjectsWithTag().then(function (data) {
            if (data.data.status) {
                $scope.subjects = data.data.request_data_result;
                myCache.put("subjects", data.data.request_data_result);
            }
            getCategories(subjectId);
        });
    }

    $scope.showMore = function () {
        $('#listSubject').toggle('slow');
    };

    function getCategories(subjectId) {
        CategoriesService.getCategories(subjectId).then(function (json) {
            var objCategories = json.data.request_data_result;
            $scope.listofCategeris = objCategories;
        });
        for (var i = 0; i < $scope.subjects.length; i++) {
            if (subjectId == $scope.subjects[i][0].subject_id) {
                var subject = {
                    "subject_id": $scope.subjects[i][0].subject_id,
                    "subject_name": $scope.subjects[i][0].subject_name,
                    "subject_icon": $scope.subjects[i][0].image,
                    "subject_description": $scope.subjects[i][0].description
                };
                $scope.subject = subject;
            }
        }
    }

    $scope.iconImgs = SUBJECT_MAP_IMG;
    $scope.title = "Video Tutorials";
    // var location = window.history.location || window.location;
    $scope.loadMoreVideo = function (categoryId) {
        $location.url('/listVideo/' + subjectId + '/' + categoryId + '/' + 1);
    };
    // load list category when click by subject

    $scope.getOrtherSubject = function (event, subject_id) {
        var ele = event.currentTarget;
        subjectId = subject_id;
        getCategories(subject_id);
        $location.path('/video_tutorial').search('subjectId', subject_id);
        $(ele).parent().find('.active').removeClass('active');
        $(ele).addClass('active');
    };

    $('#video_tutorial .searchForum').find('.searchbox').keypress(function (event) {
        /*var pathWS = SERVICE_URL + "/subjects/filterSubCategories?subject="+ $scope.subjectId +"&category="+ $scope.cateId +"&searchtxt="+searchText;*/
        if (event.keyCode == 13) {
            var searchText = $('#video_tutorial .searchForum').find('.searchbox').val();
            $rootScope.keySearch = searchText;
            $scope.$apply(function () {
                $location.path('/searchVideo/' + searchText + '/' + 1);
            });
        }
    });

    //MTDU
    var listVideos = '';
    if (myCache.get("listVideos") !== undefined) {
        $log.info("My cache already exists");
        $scope.listVideos = myCache.get("listVideos");
    } else {
        VideoService.getHistoryVideosList(localStorage.getItem('userId')).then(function (data) {
            if (data.data.status) {
                $scope.listVideos = data.data.request_data_result;
                for (var i = 0; i < $scope.listVideos.length; i++) {
                    $scope.listVideos[i].timeStamp = caculateTimeElapsed($scope.listVideos[i].timeStamp)
                }
                myCache.put("listVideos", $scope.listVideos);
            }
        });
    }

    $scope.range = function (num) {
        return new Array(num);
    }

    $scope.numberOfHistoryVideo = 4;
    $scope.flagShowMoreButton = true;
    $scope.showMore = function () {
        if ($scope.listVideos.length > 0 && $scope.numberOfHistoryVideo < $scope.listVideos.length)
            $scope.numberOfHistoryVideo += 4;
        if ($scope.numberOfHistoryVideo > $scope.listVideos.length)
            $scope.flagShowMoreButton = false;
    }

}]);

var paginationListVideo = angular.module('paginationListVideo', ['ui.bootstrap']);
paginationListVideo.controller('ListVideoCtrl',
    ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'VideoService', 'SubjectServices', 'CategoriesService', 'SubCategoryVideoService', 'myCache',
        function ($scope, $rootScope, $routeParams, $http, $location, VideoService, SubjectServices, CategoriesService, SubCategoryVideoService, myCache) {
            var searchText = $('#main .data').find('.search').val();
            var numberPage;
            var listPage = [];
            var userId = localStorage.getItem('userId');

            $scope.listPage = '';

            $scope.currentPage = 1;
            $scope.keySearch = $rootScope.keySearch;
            $scope.page = $routeParams.page;
            $scope.orderType = $routeParams.order;

            $scope.subjects = myCache.get("subjects");

            $rootScope.page = 1;

            $scope.setPage = function (pageNo) {
                $scope.currentPage = pageNo;
            };
            $scope.maxSize = 5;

            if ($routeParams.p !== undefined) {
                $scope.currentPage = $routeParams.p;
            }

            $scope.subjectId = $routeParams.subjectId;
            $scope.cateId = $routeParams.categoryId;
            $scope.orderType = $routeParams.order;
            $scope.totalItems = 18;

            $scope.seletedCatName = null;
            $scope.iconImgs = SUBJECT_MAP_IMG;

            init();

            function init() {

                if ($routeParams.categoryId === undefined || $routeParams.subjectId === undefined) {
                    $location.url('/video_tutorial');
                }

                if ($scope.orderType != 'syllabus' && $scope.orderType != 'rating' && $scope.orderType != 'popular' && $scope.orderType != 'mentorPoints') {
                    $scope.orderType = 'syllabus';
                }

                for (var i = 0; i < $scope.subjects.length; i++) {
                    if ($scope.subjects[i][0].subject_id == $scope.subjectId) {
                        $scope.nameSubject = $scope.subjects[i][0].subject_name;
                        $scope.iconImgs = $scope.subjects[i][0].image;
                        $scope.descriptionSubject = $scope.subjects[i][0].description;
                    }
                }

                CategoriesService.getCategories($scope.subjectId).then(function (json) {
                    if (json.data.status == "true") {
                        var cat = json.data.request_data_result;
                        for (var i in cat) {
                            if (cat[i].topicId == $scope.cateId) {
                                $scope.seletedCatName = cat[i].topic;
                                delete json.data.request_data_result[i];
                                $scope.listofCategeris = json.data.request_data_result;
                                break;
                            }
                        }
                        if ($scope.seletedCatName == null) {
                            $location.url('/video_tutorial');
                        }
                    } else {
                        $location.url('/video_tutorial');
                    }
                });

                VideoService.getVideoWithTopicPN($scope.subjectId, $scope.cateId, $rootScope.page, $scope.orderType).then(function (data) {
                    numberPage = Math.ceil(data.data.count / 10);
                    showPage(numberPage, $rootScope.page, function (response) {
                        $scope.listPage = response;
                        loadVideos(data);
                    });
                });

            }

            var loadVideos = function (data) {
                if (data.data.status == 'true') {
                    $scope.showItem = false;
                    var count = '';
                    var temp = data.data.request_data_result;
                    var totalVideo = data.data.count;
                    if (temp == 'No Data Found') {
                        //$location.url('/video_tutorial');
                        $scope.errorData = DATA_ERROR.noDataFound;
                        $scope.videosWithTopic = '';
                    } else {
                        $scope.videosWithTopic = temp;
                        for (var i = 0; i < temp.length; i++) {
                            if (temp[i].vid == null) {
                                count += 1;
                            }
                        }
                        if (count < temp.length) {
                            var max;
                            if (temp.length > 10) {
                                max = $scope.page * 10;
                            } else {
                                max = totalVideo;
                            }
                            // $scope.errorData = (data.data.count) + ' Videos (showing ' + (($scope.page - 1)*10 + 1) + '-' + max + ')';
                            // $scope.errorData = '(showing ' + (($scope.page - 1)*10 + 1) + '-' + max + ')';
                        } else {
                            $scope.errorData = DATA_ERROR.noDataFound;
                        }

                        $.each(data.data.request_data_result, function (index, value) {
                            var rate = value.rating;
                            if (rate > 0) {
                                showRating(rate, function (response) {
                                    $scope.videosWithTopic[index].arr_rate = response;
                                });
                            }
                        });
                    }
                    $scope.showItem = true;
                    if (userId != null) {
                        VideoService.getVideoUserWatched(userId).then(function (data) {
                            var listWatched = data.data.request_data_result;
                            for (var i in listWatched) {
                                $('#' + listWatched[i].vid).find('.status').css({'opacity': 1});
                            }
                        });
                    }
                } else {
                    $location.url('/video_tutorial');
                }
            };

            $scope.prevPage = function () {
                if ($rootScope.page > 1) {
                    var prevPage = parseInt($rootScope.page, 10) - 1;
                    $rootScope.page = prevPage;
                    init();
                }
            };

            $scope.nextPage = function () {
                if ($rootScope.page < numberPage) {
                    var nextPage = parseInt($rootScope.page, 10) + 1;
                    $rootScope.page = nextPage;
                    init();
                }
            };

            $scope.addClassActive = function (page) {
                if (page == $rootScope.page) {
                    return 'span_active';
                }
                return '';
            };

            $scope.showListVideoWithPage = function (page) {
                $rootScope.page = page;
                init();
            };

            $('#main .data').find('.search').keypress(function (event) {
                /*var pathWS = SERVICE_URL + "/subjects/filterSubCategories?subject="+ $scope.subjectId +"&category="+ $scope.cateId +"&searchtxt="+searchText;*/
                if (event.keyCode == 13) {
                    var searchText = $('#main .data').find('.search').val();
                    $rootScope.keySearch = searchText;
                    $scope.$apply(function () {
                        $location.path('/searchVideo/' + searchText + '/' + 1);
                    });
                }
            });

            $scope.openAnotherCat = function (cat_id) {
                $location.url('/listVideo/' + $scope.subjectId + '/' + cat_id + '/' + 1);
            };

            $scope.orderVideos = function (typeOrder) {
                $location.url('/listVideo/' + $scope.subjectId + '/' + $scope.cateId + '/' + $scope.page + '?order=' + typeOrder);
            };

            // change spring 72;
            // $scope.$watch('currentPage', function(newPage) {
            // $scope.watchPage = newPage;
            // $location.path('/listVideo/'+ $scope.subjectId +'/'+ $scope.cateId +'/'+ $scope.page).search('p', newPage);
            // });

            $scope.showVideoDetail = function (videoId, obj) {
                var id = videoId;
                $('#' + id).find('.status').css({'opacity': 1});
                var userId = localStorage.getItem('userId');
                // if(userId == null) {
                // brot.signin.signin();
                // $('#popSignIn').modal('show');
                // } else {
                $location.url('/detailVideo/' + $scope.subjectId + '/' + $scope.cateId + '/' + videoId);
                // }
            };

            $scope.backVideoTutorial = function () {
                $location.path('/video_tutorial/' + $scope.subjectId);
            };

            $('.select_topic').on('show.bs.dropdown', function (e) {
                var $dropdown = $(this).find('.dropdown-menu');
                var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
                $dropdown.css({
                    'margin-top': (orig_margin_top + 10) + 'px',
                    opacity: 0
                }).animate({'margin-top': orig_margin_top + 'px', opacity: 1}, 300, function () {
                    $(this).css({'margin-top': ''});
                });
            });
            // Add slidedown & fadeout animation to dropdown
            $('.select_topic').on('hide.bs.dropdown', function (e) {
                var $dropdown = $(this).find('.dropdown-menu');
                var orig_margin_top = parseInt($dropdown.css('margin-top'), 10);
                $dropdown.css({
                    'margin-top': orig_margin_top + 'px',
                    opacity: 1,
                    display: 'block'
                }).animate({'margin-top': (orig_margin_top + 10) + 'px', opacity: 0}, 300, function () {
                    $(this).css({'margin-top': '', display: ''});
                });
            });
        }]);

brotControllers.filter('trusted', ['$sce', function ($sce) {
    return function (url) {
        return $sce.trustAsResourceUrl(url);
    };
}])

    .controller('searchVideoCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'VideoService', 'SubjectServices', 'SubCategoryVideoService',
        function ($scope, $rootScope, $routeParams, $http, $location, VideoService, SubjectServices, SubCategoryVideoService) {

            var listCat = [];
            var searchText = [];
            var listPage = [];
            var numberPage;

            $scope.keyWord = $routeParams.keyWord;
            $scope.page = $routeParams.page;
            $scope.videosWithTopic = "";
            $scope.subjectId = "";
            $scope.topicId = "";
            $scope.keySearch = $rootScope.keySearch;

            $scope.listPage = listPage;

            $scope.addClassActive = function (page) {
                if (page == $scope.page) {
                    return 'span_active';
                }
                return '';
            };

            VideoService.searchVideos($scope.keyWord, $scope.page).then(function (data) {
                numberPage = Math.ceil(data.data.count / 10);
                for (var i = 1; i <= numberPage; i++) {
                    listPage.push(i);
                }
                loadVideos(data);
            });

            $scope.showVideoWithPage = function (page) {
                $location.path('/searchVideo/' + $scope.keyWord + '/' + page);
            };

            var loadVideos = function (data) {
                if (data.status == 200) {
                    $scope.showItem = false;
                    var count = '';

                    var temp = data.data.request_data_result;
                    listCat = temp;
                    if (temp == 'No Data Found') {
                        $scope.errorData = DATA_ERROR.noDataFound;
                    } else {
                        $scope.videosWithTopic = temp;
                        for (var i = 0; i < temp.length; i++) {
                            if (temp[i].vid == null) {
                                count += 1;
                            }
                        }
                        if (count < temp.length) {
                            var max;
                            var totalVideo = data.data.count;
                            if (temp.length > 10) {
                                max = $scope.page * 10;
                            } else {
                                max = totalVideo;
                            }
                            $scope.errorData = (data.data.count) + ' Videos (showing ' + (($scope.page - 1) * 10 + 1) + '-' + max + ')';
                        } else {
                            $scope.errorData = DATA_ERROR.noDataFound;
                        }

                        $.each(data.data.request_data_result, function (index, value) {
                            var rate = value.numRatings;
                            if (rate > 0) {
                                var unrate = 5 - rate;
                                var arr_rate = [];
                                for (var j = 0; j < rate; j++) {
                                    arr_rate.push('assets/img/yellow _star.png');
                                }
                                for (var k = 0; k < unrate; k++) {
                                    arr_rate.push('assets/img/grey_star.png');
                                }
                                $scope.videosWithTopic[index].arr_rate = arr_rate;
                            }
                        });
                    }
                    $scope.showItem = true;
                } else {
                    $location.url('/video_tutorial');
                }
            };

            $('#main .wrap_search').find('.search').keypress(function (event) {
                /*var pathWS = SERVICE_URL + "/subjects/filterSubCategories?subject="+ $scope.subjectId +"&category="+ $scope.cateId +"&searchtxt="+searchText;*/
                if (event.keyCode == 13) {
                    searchText = $('#main .wrap_search').find('.search').val();
                    $rootScope.keySearch = searchText;
                    $scope.$apply(function () {
                        $location.path('/searchVideo/' + searchText + '/' + 1);
                    });
                }
            });

            $scope.showVideoDetail = function (videoId, obj) {
                var id = videoId;
                $('#' + id).find('.status').css({'opacity': 1});
                var userId = localStorage.getItem('userId');
                // if(userId == null) {
                // brot.signin.signin();
                // $('#popSignIn').modal('show');
                // } else {
                for (var i = 0; i < $scope.videosWithTopic.length; i++) {
                    if (videoId == $scope.videosWithTopic[i].vid) {
                        $scope.subjectId = $scope.videosWithTopic[i].subjectId;
                        $scope.topicId = $scope.videosWithTopic[i].topicId;
                    }
                }
                $location.path('/detailVideo/' + $scope.subjectId + '/' + $scope.topicId + '/' + videoId);
                // }
            };
        }])

    .controller('searchAllVideoCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$log', 'VideoService', 'SubjectServices', 'SubCategoryVideoService',
        function ($scope, $rootScope, $routeParams, $http, $location, $log, VideoService, SubjectServices, SubCategoryVideoService) {

            var userId = localStorage.getItem('userId');
            var searchText = [];
            var listPage = [];
            var numberPage;

            $scope.keyWord = $routeParams.keyWord;
            $scope.page = parseInt($routeParams.page, 10);
            $scope.videosWithTopic = "";
            $scope.subjectId = "";
            $scope.topicId = "";
            $scope.keySearch = $rootScope.keySearch;
            $scope.orderType = $routeParams.order;

            if ($scope.orderType != 'syllabus' && $scope.orderType != 'rating' && $scope.orderType != 'popular' && $scope.orderType != 'mentorPoints') {
                $scope.orderType = 'syllabus';
            }

            init();

            function init() {
                VideoService.searchAllVideo($scope.keyWord, $scope.page, $scope.orderType).then(function (data) {
                    $scope.countVideo = data.data.count;
                    numberPage = Math.ceil(data.data.count / 10);
                    showPage(numberPage, $scope.page, function (response) {
                        $scope.listPage = response;
                        loadVideos(data);
                    });
                });
            }

            $scope.addClassActive = function (page) {
                if (page == $scope.page) {
                    return 'span_active';
                }
                return '';
            };

            $scope.prevPage = function () {
                if ($scope.page > 1) {
                    var prevPage = parseInt($scope.page, 10) - 1;
                    $location.url('/searchVideo/' + $scope.keyWord + '/' + prevPage);
                }
            };

            $scope.nextPage = function () {
                if ($scope.page < numberPage) {
                    var nextPage = parseInt($scope.page, 10) + 1;
                    $location.url('/searchVideo/' + $scope.keyWord + '/' + nextPage);
                }
            };

            $scope.showVideoWithPage = function (page) {
                $location.path('/searchVideo/' + $scope.keyWord + '/' + page);
            };

            var loadVideos = function (data) {
                if (data.status == 200) {
                    $scope.showItem = false;
                    var count = '';

                    var temp = data.data.request_data_result;

                    listCat = temp;
                    if (temp == 'No Data Found') {
                        $scope.errorData = DATA_ERROR.noDataFound;
                    } else {
                        $scope.videosWithTopic = temp;
                        for (var i = 0; i < temp.length; i++) {
                            if (temp[i].vid == null) {
                                count += 1;
                            }
                        }
                        if (count < temp.length) {
                            var max;
                            var totalVideo = data.data.count;
                            if (temp.length >= 10) {
                                max = $scope.page * 10;
                            } else {
                                max = totalVideo;
                            }
                            $scope.errorData = (data.data.count) + ' Videos (showing ' + (($scope.page - 1) * 10 + 1) + '-' + max + ')';
                        } else {
                            $scope.errorData = DATA_ERROR.noDataFound;
                        }

                        $.each(data.data.request_data_result, function (index, value) {
                            var rate = value.rating;
                            if (rate > 0) {
                                showRating(rate, function (response) {
                                    $scope.videosWithTopic[index].arr_rate = response;
                                });
                            }
                        });
                    }
                    $scope.showItem = true;
                    if (userId != null) {
                        VideoService.getVideoUserWatched(userId).then(function (data) {
                            var listWatched = data.data.request_data_result;
                            for (var i in listWatched) {
                                $('#' + listWatched[i].vid).find('.status').css({'opacity': 1});
                            }
                        });
                    }
                } else {
                    $location.url('/video_tutorial');
                }
            };

            $('#main .wrap_search').find('.search').keypress(function (event) {
                /*var pathWS = SERVICE_URL + "/subjects/filterSubCategories?subject="+ $scope.subjectId +"&category="+ $scope.cateId +"&searchtxt="+searchText;*/
                if (event.keyCode == 13) {
                    searchText = $('#main .wrap_search').find('.search').val();
                    $rootScope.keySearch = searchText;
                    $scope.$apply(function () {
                        $location.path('/searchVideo/' + searchText + '/' + 1);
                    });
                }
            });

            $scope.orderVideos = function (typeOrder) {
                $location.url('/searchVideo/' + $scope.keyWord + '/1' + '?order=' + typeOrder);
            };

            $scope.showVideoDetail = function (videoId, obj) {
                var id = videoId;
                $('#' + id).find('.status').css({'opacity': 1});
                var userId = localStorage.getItem('userId');
                // if(userId == null) {
                //   // brot.signin.signin();
                //   $('#popSignIn').modal('show');
                // } else {
                for (var i = 0; i < $scope.videosWithTopic.length; i++) {
                    if (videoId == $scope.videosWithTopic[i].vid) {
                        $scope.subjectId = $scope.videosWithTopic[i].subjectId;
                        $scope.topicId = $scope.videosWithTopic[i].topicId;
                        $scope.idSubAdmission = $scope.videosWithTopic[i].idSubAdmission;
                        $scope.idTopicSubAdmission = $scope.videosWithTopic[i].idTopicSubAdmission;
                    }
                }
                if ($scope.subjectId !== '') {
                    $location.path('/detailVideo/' + $scope.subjectId + '/' + $scope.topicId + '/' + videoId);
                } else {
                    $location.url('/admission/videoadmission/videodetail/' + $scope.idSubAdmission + '/' + $scope.idTopicSubAdmission + '/' + videoId);
                }
                // }
            };


        }]);
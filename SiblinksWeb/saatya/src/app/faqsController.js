brotControllers.controller('FaqsCtrl', ['$scope', 'FaqsService', '$routeParams', '$location', '$http', function($scope, FaqsService, $routeParams, $location, $http){

    $scope.faqsAbout = [];
    $scope.faqsBecome = [];
    $scope.helpAndSupport = [];
    $scope.topQuestions = [];

    function init() {
        $scope.busy =   false;
        $scope.page = 1 ;
        key = $routeParams.ViewAll;
        if(key === null || key === undefined) {
            $('#has, #bam, #as').css({display: ''});
            $('#viewAll').css({display: ''});
            FaqsService.getFaqsAbout(2,1).then(function(data) {
                $scope.faqsAbout = data.data.request_data_result;
            });

            FaqsService.getFaqsHelp(2,1).then(function(data) {
                $scope.helpAndSupport = data.data.request_data_result;
            });

            FaqsService.getFaqsMentor(2,1).then(function(data) {
                $scope.faqsBecome = data.data.request_data_result;
            });
        } else {
            load(key, 1);
        }

        FaqsService.getTopQuestions("creation_date", 10).then(function(data) {
            $scope.topQuestions = data.data.request_data_result;
        });
    }

    function load(key, page) {
        $('#has, #bam, #as').css({display: 'none'});
        $('#viewAllA').css({display: 'none'});
        $('#viewAllB').css({display: 'none'});
        $('#viewAllC').css({display: 'none'});
        switch (key) {
            case '1':
                FaqsService.getFaqsAbout(10, page).then(function(data) {
                    if( $scope.busy ) {
                        var items = data.data.request_data_result;
                        var n = items.length;
                        for(var i = 0; i < n; i++) {
                              $scope.faqsAbout.push(items[i]);
                        }
                        if(n > 0) {
                            $scope.busy = false;
                        }
                    }
                    else {
                        $scope.faqsAbout = data.data.request_data_result;
                    }
                    
                });
                $('#as').css({display: ''});
                break;
            case '2':
                FaqsService.getFaqsHelp(10, page).then(function(data) {
                    if( $scope.busy ) {
                        var items = data.data.request_data_result;
                        var n = items.length;
                        for (var i = 0; i < n; i++) {
                          $scope.helpAndSupport.push(items[i]);
                        }
                        if(n > 0) {
                            $scope.busy = false;
                        }
                    }
                    else {
                        $scope.helpAndSupport = data.data.request_data_result;
                    }
                });
                $('#has').css({display: ''});
                break;
            case '3':
                FaqsService.getFaqsMentor(10, page).then(function(data) {
                    if( $scope.busy ) {
                        var items = data.data.request_data_result;
                        var n = items.length;
                        for(var i = 0; i < items.length; i++) {
                          $scope.faqsBecome.push(items[i]);
                        }
                        if(n > 0) {
                            $scope.busy = false;
                        }
                    }
                    else {
                        $scope.faqsBecome = data.data.request_data_result;
                    }
                });
                $('#bam').css({display: ''});
                break;
        }
    }

    $scope.getVideoId = function (videoLink) {
        videoLink = videoLink.replace('http://www.youtube.com/embed/', '');
        videoLink = videoLink.replace('?wmode=opaque', '');
        return 'https://www.youtube.com/embed/' + videoLink;
    };

    $scope.onClick=function(key) {
        $location.path('faqs/').search({'ViewAll': key+""});
    };

    $scope.loadMore = function() {

        key = $routeParams.ViewAll;
        if(key !== null && key !== undefined && $scope.busy === false) {
            $scope.busy = true;
            $scope.page += 1;
            load(key, $scope.page);

        }
        
    };

    init();

}]);
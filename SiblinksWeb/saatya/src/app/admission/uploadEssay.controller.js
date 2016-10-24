// =========================================== ADMINSSION.CONTROLLER.JS==============
brotControllers.controller('uploadEssayController', ['$scope', '$rootScope', '$log', '$location', '$http', '$timeout', 'AdmissionService', 'myCache', '$sce','uploadEssayService','$window',
    function ($scope, $rootScope, $log, $location, $http, $timeout, AdmissionService, myCache, $sce,uploadEssayService,$window) {

        var userId = localStorage.getItem('userId');
        var fileUpload;
        $scope.fileName = "Drop file (word, excel, pdf....) here or click to upload";
        $scope.selectMajor = 0;
        $scope.selectSchool = 0;
        $scope.txtDesc = "";
        $scope.txtTitle = "";
        $scope.essaySusscesMsg = "";
        var MAX_SIZE_ESSAY_UPLOAD = 5242880;
        init();

        function init() {
            uploadEssayService.listOfMajors().then(function (data) {
                if (data.data.status) {
                    $scope.listMajors = data.data.request_data_result;
                }
            });
            uploadEssayService.collegesOrUniversities().then(function (data) {
                if (data.data.status) {
                    $scope.listSchools = data.data.request_data_result;
                }
            });
        }

        // $scope.onFileSelect = function ($files) {
        //     $scope.essayErrorMsg = "";
        //     fileUpload = $files[0];
        //     $scope.fileName = fileUpload.name;
        // };
        $scope.$watch('files', function () {
            $scope.essayErrorMsg = "";
            if(isEmpty($scope.files)){
                return ;
            }
            fileUpload = $scope.files;
            $scope.fileName = fileUpload.name;
        });

        $scope.uploadEssay = function () {
            var fd = new FormData();
            if(isEmpty(userId)){
                $window.location.href = '#/student/signin?continue='+encodeURIComponent($location.absUrl());
                return;
            }
            if(isEmpty(fileUpload)){
                $scope.essayErrorMsg = "Please select file to upload";
                return;
            }
            if(fileUpload.size > MAX_SIZE_ESSAY_UPLOAD){
                $scope.essayErrorMsg='Essay over 10M';
            }
            if(isEmpty($scope.txtTitle)){
                $scope.essayErrorMsg = "Please input title essay";
                $('#txtTitle').focus();
                return;
            }
            if(isEmpty($scope.txtDesc)){
                $scope.essayErrorMsg = "Please input description essay";
                $('#txtDesc').focus();
                return;
            }

            if($scope.selectMajor == 0){
                $scope.essayErrorMsg = "Please select major";
                $('#listMajors').focus();
                return;
            }
            if($scope.selectSchool == 0){
                $scope.essayErrorMsg = "Please select school";
                $('#listSchools').focus();
                return;
            }

            fd.append('file',fileUpload);
            fd.append('desc',$scope.txtDesc);
            fd.append('userId',userId);
            fd.append('title',$scope.txtTitle);
            fd.append('majorId',$scope.selectMajor );
            fd.append('schoolId',$scope.selectSchool);
            fd.append('fileName',fileUpload.name);
            $rootScope.$broadcast('open');
            uploadEssayService.uploadEssayStudent(fd).then(function (data) {
                 if (data.data.status) {
                     $scope.essayErrorMsg = "";
                     $scope.essaySusscesMsg = "You essay has been submitted for review";
                     $scope.selectSchool = 0;
                     $scope.selectMajor = 0;
                     $scope.fileName = 'Upload your file (word, excel, pdf....)';
                     fileUpload = null;
                     $scope.txtDesc = "";
                     $scope.txtTitle = "";
                     $scope.$emit('reloadYourEssay', 'load')
                }
                else {
                     $scope.essayErrorMsg = data.data.request_data_result;
                     $scope.essaySusscesMsg = "";
                 }
                $rootScope.$broadcast('close');
            });
        }

    }]);
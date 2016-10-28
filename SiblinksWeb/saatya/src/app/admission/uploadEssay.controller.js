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
        $scope.isUpdate = false;
        var idEssay;

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

        $timeout(function () {
            var objectEdit = JSON.parse(localStorage.getItem('currentEssay'));
            if(!isEmpty(objectEdit)){
                setValueEdit(objectEdit);
                localStorage.removeItem('currentEssay');
            }
        }, 300);


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

        function setValueEdit(object) {
            $scope.selectMajor = object.majorId;
            $scope.selectSchool = object.schoolId;
            $scope.txtDesc = object.descriptionOfEssay;
            $scope.txtTitle = object.nameOfEssay;
            $scope.fileName = object.urlFile;
            $scope.isUpdate = true;
            idEssay = object.uploadEssayId;
        }

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
                 if (data.data.status ==  'true') {
                     $scope.essayErrorMsg = "";
                     $scope.essaySusscesMsg = "Your essay has been submitted for review";
                     $scope.selectSchool = 0;
                     $scope.selectMajor = 0;
                     $scope.fileName = "Drop file (word, excel, pdf....) here or click to upload";
                     fileUpload = null;
                     $scope.txtDesc = "";
                     $scope.txtTitle = "";
                     $scope.$emit('reloadYourEssay', 'load');
                }
                else {
                     $scope.essayErrorMsg = data.data.request_data_result;
                     $scope.essaySusscesMsg = "";
                 }
                $rootScope.$broadcast('close');
            });
        }

        $scope.updateEssay = function () {
            var fd = new FormData();
            if(isEmpty(userId)){
                $window.location.href = '#/student/signin?continue='+encodeURIComponent($location.absUrl());
                return;
            }

            if(fileUpload != null&&fileUpload.size > MAX_SIZE_ESSAY_UPLOAD){
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
            if(!isEmpty(fileUpload)){
                fd.append('file',fileUpload);
                fd.append('fileName',fileUpload.name);
            }

            fd.append('desc',$scope.txtDesc);
            fd.append('userId',userId);
            fd.append('title',$scope.txtTitle);
            fd.append('majorId',$scope.selectMajor );
            fd.append('schoolId',$scope.selectSchool);
            fd.append('essayId',idEssay);
            $rootScope.$broadcast('open');
            uploadEssayService.updateEssayStudent(fd).then(function (data) {
                if (data.data.status ==  'true') {
                    $scope.essayErrorMsg = "";
                    $scope.essaySusscesMsg = "You essay has been updated for review";
                    $scope.selectSchool = 0;
                    $scope.selectMajor = 0;
                    $scope.fileName = "Drop file (word, excel, pdf....) here or click to upload";
                    fileUpload = null;
                    $scope.txtDesc = "";
                    $scope.txtTitle = "";
                    $scope.$emit('reloadYourEssay', 'load');
                }
                else {
                    $scope.essayErrorMsg = data.data.request_data_result;
                    $scope.essaySusscesMsg = "";
                }
                $rootScope.$broadcast('close');
            });
        }

    }]);
/**
 * Created by HoaiNT on 02/10/2016.
 */
brotControllers.controller('SideLeftRightController', ['$scope', '$http', '$rootScope', function ($scope, $http, $rootScope) {

    var userId = localStorage.getItem('userId');



    init();

    function init() {

        if ($rootScope.imageUrl != undefined) {
            $rootScope.imageUrl = $rootScope.imageUrl;
        } else {
            $rootScope.imageUrl = localStorage.getItem('imageUrl');
        }

        if ($rootScope.fullName == undefined) {
            var firstname = localStorage.getItem('firstName');
            var lastname = localStorage.getItem('lastname');
            var isEmptyName = false;
            if (firstname == null || firstname === undefined || firstname.length == 0 || firstname == "null") {
                isEmptyName = true;
            }
            if (lastname == null || lastname === undefined || lastname.length == 0 || lastname == "null") {
                isEmptyName = true;
            }
            if(isEmptyName){
                var userName =  localStorage.getItem('userName');
                $rootScope.firstName = userName.indexOf('@') != -1 ? userName.substr(0, userName.indexOf('@')) : userName;
            }else{
                $rootScope.firstName = firstname;
            }
        }
    }

    /**
     * Menu active when click
     */
    $scope.showSubMenu = function showSubMenu(path, subMenuId) {
        if (!angular.element("#" + subMenuId ).hasClass('show')) {
            angular.element("#" + subMenuId +", #menu-video").addClass('show');
            $('#menu-video').attr('data-icon','3');
        } else {
            angular.element("#" + subMenuId +", #menu-video").removeClass('show');
            $('#menu-video').attr('data-icon','4')
        }
        $scope.showFullLeftSideBar(path);
    }
    
    /**
     * Show small left side bar
     */
    $scope.showFullLeftSideBar = function showFullLeftSideBar(path) {
    	$rootScope.isMiniMenu = false;
    	window.location.href='#'+path;
    }
}]);
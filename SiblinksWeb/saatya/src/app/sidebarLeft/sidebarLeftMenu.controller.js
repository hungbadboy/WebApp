/**
 * Created by HoaiNT on 02/10/2016.
 */
brotControllers.controller('SideLeftRightController', ['$scope', '$http', '$rootScope', function ($scope, $http, $rootScope) {

    var userId = localStorage.getItem('userId');

    if ($rootScope.imageUrl != undefined) {
        $rootScope.imageUrl = $rootScope.imageUrl;
    } else {
        $rootScope.imageUrl = localStorage.getItem('imageUrl');
    }

    init();

    function init() {
        var firstname = localStorage.getItem('firstname');
        var lastname = localStorage.getItem('lastName');
        if (firstname == null || firstname === undefined || firstname.length == 0 || firstname == "null") {
            firstname = '';
        }
        if (lastname == null || lastname === undefined || lastname.length == 0 || lastname == "null") {
            lastname = '';
        }

        $scope.fullName = firstname + ' ' + lastname;
    }

    /**
     * Menu active when click
     */
    $scope.showSubMenu = function showSubMenu(subMenuId) {
        if (!angular.element("#" + subMenuId ).hasClass('show')) {
            angular.element("#" + subMenuId +", #menu-video").addClass('show');
        } else {
            angular.element("#" + subMenuId +", #menu-video").removeClass('show');
        }
    }
}]);
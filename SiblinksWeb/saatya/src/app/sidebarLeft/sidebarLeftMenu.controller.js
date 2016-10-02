/**
 * Created by HoaiNT on 02/10/2016.
 */
brotControllers.controller('SideLeftRightController', ['$scope', '$http',
    function ($scope, $http) {

        var userId = localStorage.getItem('userId');

        $scope.avatar = localStorage.getItem('imageUrl');
       
        init();

        function init(){
            var firstname = localStorage.getItem('firstname');
            var lastname = localStorage.getItem('lastName');
            if (firstname == null || firstname.length == 0 || firstname == "null") {
                firstname = '';
            }
            if (lastname == null || lastname.length == 0 || lastname == "null") {
                lastname = '';
            }

            $scope.fullName = firstname + ' ' + lastname;
        }

    }]);
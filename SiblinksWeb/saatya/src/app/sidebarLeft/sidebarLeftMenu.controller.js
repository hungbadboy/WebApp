/**
 * Created by HoaiNT on 02/10/2016.
 */
brotControllers.controller('SideLeftRightController', ['$scope', '$http', '$rootScope', function ($scope, $http, $rootScope) {

    var userId = localStorage.getItem('userId');



    init();

    function init() {

        if ($rootScope.imageUrl == undefined) {
            $rootScope.imageUrl = localStorage.getItem('imageUrl');
        }

        if ($rootScope.fullName == undefined) {
            var firstname = localStorage.getItem('firstName');
            var isEmptyName = false;
            if (isEmpty(firstname)) {
                isEmptyName = true;
            }
            if(isEmptyName){
                var userName =  localStorage.getItem('userName');
                $rootScope.firstName = userName.indexOf('@') != -1 ? capitaliseFirstLetter(userName.substr(0, userName.indexOf('@'))) : userName;
            }else{
                $rootScope.firstName = firstname;
            }
        }
    }

    /**
     * Menu active when click
     */
    $scope.showSubMenu = function showSubMenu(subMenuId) {
        if (!angular.element("#" + subMenuId ).hasClass('show')) {
            angular.element("#" + subMenuId +", #menu-video").addClass('show');
            $('#menu-video').attr('data-icon','3');
        } else {
            angular.element("#" + subMenuId +", #menu-video").removeClass('show');
            $('#menu-video').attr('data-icon','2')
        }
    }
    
    /**
     * Show small left side bar
     */
    $scope.showFullLeftSideBar = function showFullLeftSideBar(path) {
    	if (!path){
    		return;
    	}
      	$rootScope.isMiniMenu = false;
    	$rootScope.isMiniSideRightBar = true;
    	var sidebarCol = $('.center-content')
    	if (sidebarCol.hasClass('in')) {
        	$(".center-content.in").css({"width":"calc(100% - 500px)","margin-left":"250px","transition":"margin-left 1s"}).one("webkitTransitionEnd otransitionend oTransitionEnd msTransitionEnd transitionend",
                    function(event) {       
        		window.location.href = path;
        	  });		
    	} else {
    		window.location.href = path;
    	}  
    }
}]);
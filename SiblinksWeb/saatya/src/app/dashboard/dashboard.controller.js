brotControllers.controller('DashboardController',['$scope','$http', function($scope, $http) {


  //Author: Hoai Nguyen;
  //event click on "Submit" button;

  $scope.loadContent = function(){
      id = 84;
      $http({
      method: 'GET',
      url: NEW_SERVICE_URL + 'mentor/getNewestAnswer/'+ id+'',
      data: {
        "request_data_type": "mentor",
        "request_data_method": "getNewestAnswer",
        "id": id
      }
      
      // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).success(function(data) {
        console.log(data.request_data_result);
        var data = data.request_data_result;
        displayContentData(data);
      }).error(function(data){
        console.log(data);
      });
  };

  $scope.signIn = function(){
    console.log('singIn');
    // brot.signin.signin();
    $('#popSignIn').modal('show');
  };

  function displayContentData(data){
      for (var i = 0; i < data.length; i++) {
        var numViews = data[i].numViews;
        console.log(numViews);
        if (numViews == null) 
          numViews = 0;

        html = '<div class="newest_item">';
        html += '<div class="title">' + data[i].title + '</div>';
        html += '<div><span>' + numViews + ' views</span> <span> <a href="#">' + data[i].firstName + '</a></span> <span>questioned</span></div>';
        html += '</div>';
        $('#newestData').append(html);
      }
  }

window.onload = function () {
  angular.element(document.getElementById('myelement')).scope().loadContent();
}

}]);
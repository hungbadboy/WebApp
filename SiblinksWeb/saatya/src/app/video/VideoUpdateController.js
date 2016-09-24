brotControllers.controller('VideoUpdateController', ['$scope', '$modalInstance', '$routeParams', '$http', '$location', 'vid',
                                       function ($scope, $modalInstance, $routeParams, $http, $location, vid) {

    init();

    function init(){
        getVideoById();
    }

    function getVideoById(){
        $http({
          method: 'GET',
          url: NEW_SERVICE_URL + 'video/getVideoById/'+ vid +'',
          data: {
            "request_data_type": "video",
            "request_data_method": "getVideoById"
        }
        
        // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data) {          
          $('#title').val(data.request_data_result[0].title);
          $('#description').val(data.request_data_result[0].description);
          $scope.vid = data.request_data_result[0].vid;
        }).error(function(data){
          console.log(data);
        });
    }

    $scope.update = function (){
        var check = true;
        var error = '';

        var title = $('#title').val();
        // var url = $scope.video_url;
        // var imageurl = $scope.video_image;
        var description = $('#description').val();

        if (title == null || title.trim().length == 0){
          error += 'Please input Title. \n';
          check = false;
        }
        // if (url == null || url.trim().length == 0){
        //   error += 'Please input video url. \n';
        //   check = false;
        // }
        // if (imageurl == null || imageurl.trim().length == 0){
        //   error += 'Please input video image. \n';
        //   check = false;
        // }
        
        
        if(!check){
          alert(error);
          return;
        }
        else{
          $http({
            method: 'POST',
            url: NEW_SERVICE_URL + 'video/updateVideo',
            data: {
                "request_data_type": "video",
                "request_data_method": "updateVideo",
                "request_data":{
                  "vid": $scope.vid,
                  "title": title.trim(),
                  // "url": url.trim(),
                  // "image": imageurl.trim(),
                  "description": description
                }            
            }  
        
          // headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          }).success(function(data) {
            alert('success');
            $modalInstance.dismiss('cancel');
          }).error(function(data){
            console.log(data);
          });
        }
    }

    function clearInput(){
      $('input').empty();
      $('textarea').empty();
    }

    function initImage(){
        $('#image_preview').hide();
    }

    $scope.check = function(){
        $("<img>", {
          src: $scope.vide0_image,
          error: function() { console.log($scope.image + ': ' + false); },
          load: function() { $('#image_preview').show(); }
      });
    }

    

  $scope.edit = function(vid){
    console.log('editVideo' + vid);
    var modalInstance = $modal.open({
        templateUrl: '#/video_edit.tpl.html',
    });
  }

  $scope.signIn = function(){
    console.log('singIn');
    // brot.signin.signin();
    $('#popSignIn').modal('show');
  };




  function clearContent(){
    $('.newest').empty();
    $('.most_viewed').empty();
    $('.top_rates').empty();
  }

}]);

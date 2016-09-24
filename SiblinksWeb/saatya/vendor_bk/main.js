jQuery(document).ready(function($) {


    $(".item-img").mouseenter(function(){
     	$(this).find(".item-hover").show(); //show click button when hover tutorial
    });
    $(".item-img").mouseleave(function(){
     	$(this).find(".item-hover").hide(); //hide click button when hover tutorial
    });
    $(".add-img").click(function(){
        $(".media-manager").show(); //show manager upload when click on add more image
    })

    $(".navbar-toggle").click(function(){
        $(".mobimenu").toggle(); //show menu on mobile
    })

    $(".media-uploaded li img").click(function(){
        $(this).next(".choose-uploaded").addClass("active"); //Click on image uploaded then add class active  
    })
    $(".choose-uploaded").click(function(){
        $(this).removeClass("active"); //Click again on image uploaded then remove class active
    })

    // Get images has choosen
    function handleFileSelect(evt) {
        var files = evt.target.files; // FileList object

        // Loop through the FileList and render image files as thumbnails.
        for (var i = 0, f; f = files[i]; i++) {

          // Only process image files.
          if (!f.type.match('image.*')) {
            continue;
          }

          var reader = new FileReader();

          // Closure to capture the file information.
          reader.onload = (function(theFile) {
            return function(e) {
              // Render thumbnail.
              var div = document.createElement('div');
              div.innerHTML = ['<img class="thumb" src="', e.target.result,
                                '" title="', escape(theFile.name), '"/><div class="remove-image"><img src="../images/remove-img.png"></div>'].join('');
              document.getElementById('list').insertBefore(div, null);
              $(".media-manager").hide();

            };
          })(f);


          // Read in the image file as a data URL.
          reader.readAsDataURL(f);
        }

      }

  document.getElementById('files').addEventListener('change', handleFileSelect, false);


  
})
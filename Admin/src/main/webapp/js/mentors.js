function showDeleteForm(element) {
  $("#delete-mentor-dialog").dialog('open');
  var id = $(element).attr("id");
  $('#delete-mentor-dialog').data('id', id);
  return false;
};

function showAddForm(event) {
  $('#add-mentor-dialog .title').val('');
  $('#upload-file-input').val('');
  CKEDITOR.instances['txtIntroduceAdd'].setData('');
  $("#add-mentor-dialog").dialog('open');
  return false;
};

function showEditForm(element) {
  $('#edit-mentor-dialog').data('id', $(element).attr("id"));
  CKEDITOR.instances['txtIntroduceEdit'].setData(decodeURIComponent($(element).attr("txtIntroduceEdit")));   
  
  $("#edit-mentor-dialog").dialog('open'); 
  return false;
};

function deleteAboutMentor(id) {
    
  var RequestMentor = new Object();
  var Mentor = new Object();
  Mentor.id = id;
  RequestMentor.request_data_type = 'mentor';
  RequestMentor.request_data_method = 'deleteAboutMentor';
  RequestMentor.request_data_aboutMentor = Mentor;
  
  $.ajax({
    url: endPointUrl + 'mentor/deleteAboutMentor',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestMentor),
    success : function(data) {
      $("#delete-mentor-dialog").dialog("close");
      $('#manageTabs-ul a:eq(8)').click();
    },
    error : function(data) {
      $("#delete-mentor-dialog").dialog("close");
      $('#manageTabs-ul a:eq(8)').click();
    }
  });
}

$("#delete-mentor-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Confirm" : function() {
      var dvd = $('#delete-mentor-dialog');
      var id = dvd.data('id');
      deleteAboutMentor(id);
    },
    Cancel : function() {
      $(this).dialog("close");
    }
  },
  open : function() {
    $(this).parent().css('position', 'fixed');
  },
  close : function() {
  }
});

//Add Article Dialog
$("#add-mentor-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Save" : function() {
      
      var introduce = CKEDITOR.instances['txtIntroduceAdd'].getData();
      var stripped = introduce.replace(/(<([^>]+)>)/ig, "");
      var description = stripped.replace(/&nbsp;/ig, " ");

      if(description.length > 340) {
        description = description.substring(0, 340) + '...';  
      }

      var image = $('#add-mentor-dialog #upload-file-input').val();

      var Mentor = new Object();
      Mentor.description = description;
      Mentor.introduce = encodeURIComponent(introduce);
      var RequestMentor = new Object();
      RequestMentor.request_data_type = 'mentor';
      RequestMentor.request_data_method = 'createMentor';
      if(image) {
        $.ajax({
          url: endPointUrl + 'mentor/uploadFile',
          type: "POST",
          data: new FormData($("#add-mentor-dialog #upload-file-add-form")[0]),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false,
          cache: false,
          success: function(data) {
            var imageUrl = data.request_data_result;
            Mentor.image = imageUrl;
            RequestMentor.request_data_aboutMentor = Mentor;

            $.ajax({
              url: endPointUrl + '/mentor/createAboutMentor',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestMentor),
              
              success : function(data) {
                $("#add-mentor-dialog").dialog("close");
                $('#manageTabs-ul a:eq(8)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(8)').click();
              }
            });
          }
        });
      } else {
        image = null;
        Mentor.image = image;
        RequestMentor.request_data_aboutMentor = Mentor;

        $.ajax({
          url: endPointUrl + '/mentor/createAboutMentor',
          type: "POST",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(RequestMentor),
          
          success : function(data) {
            $("#add-mentor-dialog").dialog("close");
            $('#manageTabs-ul a:eq(8)').click();
          },
          error : function(data) {
            $('#manageTabs-ul a:eq(8)').click();
          }
        });
      }
    },
    Cancel : function() {
      $(this).dialog("close");
    }
  },
  open : function() {
    $(this).parent().css('position', 'fixed');
  },
  close : function() {
  }
});

//Edit Article Details Dialog
$("#edit-mentor-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Save" : function() {

      var $ead = $("#edit-mentor-dialog");
      var id = $ead.data('id');
      
      var introduce = CKEDITOR.instances['txtIntroduceEdit'].getData();
      var stripped = introduce.replace(/(<([^>]+)>)/ig, "");

      var description = stripped.replace(/&nbsp;/ig, " ");
      if(description.length > 340) {
        description = description.substring(0, 340) + '...';  
      }

      var image = $('#edit-mentor-dialog #upload-file-input').val();
      var RequestMentor = new Object();
      var Mentor = new Object();
      Mentor.id = id;
      Mentor.description = description;
      Mentor.introduce = encodeURIComponent(introduce);

      if(image) {
        $.ajax({
          url: endPointUrl + 'mentor/uploadFile',
          type: "POST",
          data: new FormData($("#edit-mentor-dialog #upload-file-form")[0]),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false,
          cache: false,
          success: function(data) {
            image = data.request_data_result;
            Mentor.image = image;
            RequestMentor.request_data_type = 'mentor';
            RequestMentor.request_data_method = 'updateAboutMentor';
            RequestMentor.request_data_aboutMentor = Mentor;

            $.ajax({
              url: endPointUrl + 'mentor/updateAboutMentor',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestMentor),
              
              success : function(data) {
                $("#edit-mentor-dialog").dialog("close");
                $('#manageTabs-ul a:eq(8)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(8)').click();
              }
            });
          }
        });
      } else {
        image = null;
        Mentor.image = image;
        RequestMentor.request_data_type = 'mentor';
        RequestMentor.request_data_method = 'updateAboutMentor';
        RequestMentor.request_data_aboutMentor = Mentor;

        $.ajax({
          url: endPointUrl + 'mentor/updateAboutMentor',
          type: "POST",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(RequestMentor),
          
          success : function(data) {
            $("#edit-mentor-dialog").dialog("close");
            $('#manageTabs-ul a:eq(8)').click();
          },
          error : function(data) {
            $('#manageTabs-ul a:eq(8)').click();
          }
        });
      }
    },
    Cancel : function() {
      $(this).dialog("close");
    }
  },
  open : function() {
    $(this).parent().css('position', 'fixed');
  },
  close : function() {
  }
});

function ckeditor(option) {
  CKEDITOR.replace(option);
  CKEDITOR.config.allowedContent = true;
  CKEDITOR.config.width = 590;
  CKEDITOR.config.height = 250;
}

$(function() {
    
  // Grab the template script
  var theTemplateScript = $("#list-mentor").html();

  // Compile the template
  var theTemplate = Handlebars.compile(theTemplateScript);

  if(CKEDITOR.instances['txtIntroduceAdd']) {
    CKEDITOR.instances['txtIntroduceAdd'].destroy(true);
    ckeditor('txtIntroduceAdd');
  } else {
    ckeditor('txtIntroduceAdd');
  }

  if(CKEDITOR.instances['txtIntroduceEdit']) {
    CKEDITOR.instances['txtIntroduceEdit'].destroy(true);
    ckeditor('txtIntroduceEdit');
  } else {
    ckeditor('txtIntroduceEdit');
  }
  
  // Define our data object

  var RequestMentor = new Object();
  var Mentor = new Object();
  RequestMentor.request_data_type = 'mentor';
  RequestMentor.request_data_method = 'getAllAboutMentor';
  RequestMentor.request_data_aboutMentor = Mentor;
  $.ajax({
    url: endPointUrl + 'mentor/getAllAboutMentor',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestMentor),
    success: function(data) {
      var mentors = data.request_data_result;
      var context = new Object();
      context.mentors = data.request_data_result;
      // Pass our data to the template
      var theCompiledHtml = theTemplate(context);
      // Add the compiled html to the page
      $('#mentor-items').html(theCompiledHtml).dataTable({});
    }
  });

  $(".deleteBtn").button({
    icons : {
      primary : "ui-icon-trash"
    },
    text : false
  }).on('click', showDeleteForm);

  $(".addBtn").button({
	icons : {
	  primary : "ui-icon-plus"
	},
	text : false
  }).unbind('click').click(showAddForm);
  
  $(".editBtn").button({
    icons : {
      primary : "ui-icon-pencil"
    },
    text : false
  }).on('click', showEditForm);
});
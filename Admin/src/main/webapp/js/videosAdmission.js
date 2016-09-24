/**
 * Created by hieu on 9/9/15.
 */
function showDeleteForm(element) {
  $("#delete-video-dialog").dialog('open');
  var vId = $(element).attr("vId");
  $('#delete-video-dialog').data('vId', vId);
  return false;
};

function showAddForm(element) {

  if(CKEDITOR.instances['descriptionAdd']) {
    CKEDITOR.instances['descriptionAdd'].destroy(true);
    ckeditor('descriptionAdd');
  } else {
    ckeditor('descriptionAdd');
  }

  $('#add-video-dialog .title').val('');
  $('#add-video-dialog .fileUpload').val('');
  $('#add-video-dialog .youtubeUrl').val('');
  $('#add-video-dialog .runningTime').val('');
  CKEDITOR.instances['descriptionAdd'].setData('');
  $("#add-video-dialog").dialog('open');
  return false;
};

function showEditForm(element) {
  $('#edit-video-dialog').data('vId', $(element).attr("vId"));
  $('#edit-video-dialog .title').val($(element).attr("titleEdit"));
  $('#edit-video-dialog .youtubeUrl').val($(element).attr("youtubeUrl"));
  $('#edit-video-dialog .runningTime').val($(element).attr("runningTime"));
  CKEDITOR.instances['descriptionEdit'].setData($(element).attr("descriptionEdit"));   
  $("#edit-video-dialog").dialog('open');
  return false;
};

function deleteVideo(vId) {
    
  var RequestVideo = new Object();
  var Video = new Object();
  Video.vId = vId;
  RequestVideo.request_data_type = 'videoAdmission';
  RequestVideo.request_data_method = 'deleteVideoAdmission';
  RequestVideo.request_data_videoAdmission = Video;
  
  $.ajax({
    url: endPointUrl + 'videoAdmission/deleteVideoAdmission',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestVideo),
    success : function(data) {
      $("#delete-video-dialog").dialog("close");
      $('#manageTabs-ul a:eq(6)').click();
    },
    error : function(data) {
      $("#delete-video-dialog").dialog("close");
      $('#manageTabs-ul a:eq(6)').click();
    }
  });
}

$("#delete-video-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Confirm" : function() {
      var dvd = $('#delete-video-dialog');
      var vId = dvd.data('vId');
      deleteVideo(vId);
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
$("#edit-video-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Save" : function() {

      var $evd = $("#edit-video-dialog");
      var vId = $evd.data('vId');
      
      var title = $('#edit-video-dialog .title').val();
      var youtubeUrl = $('#edit-video-dialog .youtubeUrl').val();
      var runningTime = $('#edit-video-dialog .runningTime').val();
      var description = CKEDITOR.instances['descriptionEdit'].getData();

      var image = $('#edit-video-dialog #upload-file-input').val();
      var RequestVideo = new Object();
      RequestVideo.request_data_type = 'videoAdmission';
      RequestVideo.request_data_method = 'updateVideoAdmission';
      var Video = new Object();
      Video.vId = vId;
      Video.title = title;
      Video.youtubeUrl = youtubeUrl;
      Video.runningTime = runningTime;
      Video.description = description;

      if(image) {
        $.ajax({
          url: endPointUrl + 'videoAdmission/uploadFile',
          type: "POST",
          data: new FormData($("#edit-video-dialog #upload-file-form")[0]),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false,
          cache: false,
          success: function(data) {
            image = data.request_data_result;
            Video.image = image;
            RequestVideo.request_data_videoAdmission = Video;

            $.ajax({
              url: endPointUrl + 'videoAdmission/updateVideoAdmission',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestVideo),
              
              success : function(data) {
                $("#edit-video-dialog").dialog("close");
                $('#manageTabs-ul a:eq(6)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(6)').click();
              }
            });
          }
        });
      } else {
        image = null;
        Video.image = image;
        RequestVideo.request_data_videoAdmission = Video;

        $.ajax({
          url: endPointUrl + 'videoAdmission/updateVideoAdmission',
          type: "POST",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(RequestVideo),
          
          success : function(data) {
            $("#edit-video-dialog").dialog("close");
            $('#manageTabs-ul a:eq(6)').click();
          },
          error : function(data) {
            $('#manageTabs-ul a:eq(6)').click();
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

function getAllTopicSubAdmission(callback) {
  var theTemplateOptionScript = $("#list-topic").html();

  // Compile the template
  var theTemplateOption = Handlebars.compile(theTemplateOptionScript);

  var RequestVideo = new Object();
  var Video = new Object();
  Video.idSubAdmission = 1;
  RequestVideo.request_data_type = 'admission';
  RequestVideo.request_data_method = 'getAllTopicSubAdmission';
  RequestVideo.request_data_admission = Video;

  $.ajax({
    url: endPointUrl + 'admission/getAllTopicSubAdmission',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestVideo),
    success: function(data) {
      var listTopic = new Object();
      listTopic.topics = data.request_data_result;
      // Pass our data to the template
      var theCompiledHtml = theTemplateOption(listTopic);
      // Add the compiled html to the page
      $('#topic-items').html(theCompiledHtml);

      if(typeof callback === "function") {
        callback();
      }
    }
  });
}

function getVideoTopicSubAdmission(idTopicSubAdmission) {

  // Grab the template script
  var theTemplateScript = $("#list-video").html();

  var theTemplate = Handlebars.compile(theTemplateScript);

  var RequestVideo = new Object();
  var Video = new Object();
  Video.idTopicSubAdmission = idTopicSubAdmission;
  RequestVideo.request_data_type = 'videoAdmission';
  RequestVideo.request_data_method = 'getVideoTopicSubAdmission';
  RequestVideo.request_data_videoAdmission = Video;

  $.ajax({
    url: endPointUrl + 'videoAdmission/getVideoTopicSubAdmission',
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(RequestVideo),
    success : function(data) {
      var listVideo = new Object();
      listVideo.videos = data.request_data_result;
      var theCompiledHtml = theTemplate(listVideo);
      $('#video-items').html(theCompiledHtml).dataTable({"bDestroy": true});
    }
  });
}

function ckeditor(option) {
  CKEDITOR.replace(option);
  CKEDITOR.config.allowedContent = true;
  CKEDITOR.config.width = 590;
  CKEDITOR.config.height = 250;
}

$(function() {

  var selectSubAdmission = 1;

  if(CKEDITOR.instances['descriptionEdit']) {
    CKEDITOR.instances['descriptionEdit'].destroy(true);
    ckeditor('descriptionEdit');
  } else {
    ckeditor('descriptionEdit');
  }

  // load all subject
  getAllTopicSubAdmission(function() {
    // load all video of subject
    getVideoTopicSubAdmission(1);
  });

  $('#topic-items').on('change', function() {
    selectSubAdmission = $('option:selected', this).val();
    getVideoTopicSubAdmission(selectSubAdmission);
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

  //Add Article Dialog
    $("#add-video-dialog").dialog({
      autoOpen : false,
      height : 'auto',
      width : 'auto',
      modal : true,
      buttons : {
        "Save" : function() {
          var title = $('#add-video-dialog .title').val();
          var youtubeUrl = $('#add-video-dialog .youtubeUrl').val();
          var runningTime = $('#add-video-dialog .runningTime').val();
          var description = CKEDITOR.instances['descriptionAdd'].getData();
          var image = $('#add-video-dialog #upload-file-input').val();

          var Video = new Object();
          Video.title = title;
          Video.youtubeUrl = youtubeUrl;
          Video.runningTime = runningTime;
          Video.description = description;
          Video.idTopicSubAdmission = selectSubAdmission;

          var RequestVideo = new Object();
          RequestVideo.request_data_type = 'videoAdmission';
          RequestVideo.request_data_method = 'createVideoAdmission';

          if(image) {
            $.ajax({
              url: endPointUrl + 'videoAdmission/uploadFile',
              type: "POST",
              data: new FormData($("#add-video-dialog #upload-file-add-form")[0]),
              enctype: 'multipart/form-data',
              processData: false,
              contentType: false,
              cache: false,
              success: function(data) {
                var imageUrl = data.request_data_result;
                Video.image = imageUrl;
                RequestVideo.request_data_videoAdmission = Video;

                $.ajax({
                  url: endPointUrl + 'videoAdmission/createVideoAdmission',
                  type: "POST",
                  dataType: "json",
                  contentType: "application/json; charset=utf-8",
                  data: JSON.stringify(RequestVideo),

                  success : function(data) {
                    $("#add-video-dialog").dialog("close");
                    $('#manageTabs-ul a:eq(6)').click();
                  },
                  error : function(data) {
                    $('#manageTabs-ul a:eq(6)').click();
                  }
                });
              }
            });
          } else {
            image = null;
            Video.image = image;
            RequestVideo.request_data_videoAdmission = Video;
            $.ajax({
              url: endPointUrl + 'videoAdmission/createVideoAdmission',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestVideo),

              success : function(data) {
                $("#add-video-dialog").dialog("close");
                $('#manageTabs-ul a:eq(6)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(6)').click();
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
});
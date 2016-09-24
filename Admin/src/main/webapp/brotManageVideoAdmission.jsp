<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript">
  $(function() {
    var oTable = $('#manageVideosTable').dataTable({});
    var selectSubAdmission;
    
    // Populate Videos Information
    $(populateSubAdmission);
    CKEDITOR.replace('txtDescription');
    CKEDITOR.replace('txtDescriptionAdd');

    function populateSubAdmission() {
      var RequestData = new Object();
      var Video = new Object();
      Video.idAdmission = 1;
      RequestData.request_data_type = 'admission';
      RequestData.request_data_method = 'getAllTopicSubAdmission';
      RequestData.request_data_admission = Video;
      $.ajax({
        url: endPointUrl + '/admission/getAllTopicSubAdmission',
        type : "POST",
        dataType : "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(RequestData),
            
        success : function(data) {
          var options = '<option selected=true value="selectOne">Select One</option>';
          if(data != null) {
            
            data = data.request_data_result;
            for (var i = 0; i < data.length; i++) {
              options += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
              
              //rowHtml += '<input type=hidden name=subject_id'+data[i].subject_id+' id='+data[i].subject_id+' value'+data[i].subject_id;
            }
          }

          /* for ( var i = 0; i < data.subjectList.length; i++) {
            options += '<option value="'+data.subjectList[i][0]+'">'
                + data.subjectList[i][1] + '</option>';
          } */
          $('#selectSubAdmission').html(options).trigger("liszt:updated");
        },
        error : function(data) {
          $('#selectSubAdmission').html('<option value="-1">Error</option>');
          $('#selectSubAdmission').trigger("liszt:updated");
          /* $('#dispAlertWrapper').dispAlert('show', 'error', null, {
            scrollToTop : true
          }); */
        }
      });
    }

    $('#selectSubAdmission').on('change', function() {
      selectSubAdmission = $('option:selected', this).val();
      if (selectSubAdmission != 'selectOne') {
        getVideoDetails(selectSubAdmission);
      }
    });

    function getVideoDetails(selectSubAdmission) {
      
      // Destroy the delta definiton table if it exists
      if ($.fn.DataTable.fnIsDataTable($('#manageVideosTable')[0])) {
        //alert('getVideoDetails');
        $('#manageVideosTable').dataTable().fnDestroy();
        $("#manageVideosTable tbody").html('');
        $('#manageVideosTable').css('width', '100%');
      }
      
      var RequestData = new Object();
      var Video = new Object();
      Video.idTopicSubAdmission = selectSubAdmission;
      RequestData.request_data_type = 'videoAdmission';
      RequestData.request_data_method = 'getVideoTopicSubAdmission';
      RequestData.request_data_videoAdmission = Video;
      
      $.ajax({
        url: endPointUrl + '/videoAdmission/getVideoTopicSubAdmission',
        type : "POST",
        dataType : "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(RequestData), 

          success : function(data) {
            console.log(data);
            if (data != null) {
              var rowHtml = '';
              data = data.request_data_result;
              for ( var i = 0; i < data.length; i++) {
                rowHtml += '<tr><td>' + data[i].vId
                    + '</td>';
                rowHtml += '<td>' + data[i].title
                    + '</td>';
                rowHtml += '<td>' + data[i].description
                    + '</td>';
                rowHtml += '<td>' + data[i].youtubeUrl
                    + '</td>';
                rowHtml += '<td>' + data[i].runningTime
                    + '</td>';    

                rowHtml += '<td class="actions"><button class="editBtn" videoId="' + data[i].vId + '" videoName="' + data[i].title + '" description="' + data[i].description + '" active="' + data[i].active + '" title="' + data[i].title + '">Edit Details</button>';
                
                rowHtml += '<button class="deleteBtn" videoId="' + data[i].vId + '" title="Delete">Delete</button>';
                rowHtml += '</td></tr>';
              }
              $('#manageVideosTable tbody').append(rowHtml);
            }

            var nCloneTh = document.createElement( 'th' );
            var nCloneTd = document.createElement( 'td' );
            nCloneTd.innerHTML = '<img class="openimg" src="css/images/details_open.png">';
            nCloneTd.className = "center";
            
            $('#manageVideosTable thead tr').each( function () {
              this.insertBefore( nCloneTh, this.childNodes[0] );
            } );
            
            $('#manageVideosTable tbody tr').each( function () {
              this.insertBefore(  nCloneTd.cloneNode( true ), this.childNodes[0] );
            } );
            
            $('#manageVideosTable').dataTable({
                "sPaginationType" : "full_numbers",
                "bJQueryUI" : true,
                //"sDom" : "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
                "bRetrieve" : true,
                "bFilter" : true,
                "bProcessing" : true, // show loading icon during table sorting
                "sScrollX" : "100%", // allow horizontal scrolling
                "bScrollCollapse" : true,
                "iDisplayLength" : 10,
                "aLengthMenu" : [ [ 10, 25, 50, -1 ],
                    [ 10, 25, 50, "All" ] ],
                               // Initialize buttons before drawing the row
                               "fnRowCallback" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                                 //Edit  Button Logic

                      $(".editBtn").button({
                        icons : {
                          primary : "ui-icon-pencil"
                        },
                        text : false
                      }).unbind('click').click(function(event) {
                        $('#edit-video-dialog').data('videoId',$(this).attr('videoId'));
                        $('#edit-video-dialog .title').val($(this).attr('title'));
                        
                        $('#edit-video-dialog .description').val($(this).attr('description'));

                        $("#edit-video-dialog").dialog('open'); 
                        return false;
                      }); 
                                  
                    //Delete  Logic
                    $(".deleteBtn").button({
                      icons : {
                        primary : "ui-icon-trash"
                      },
                      text : false
                    }).unbind('click').click(function(event) {
                      $("#delete-video-dialog").dialog('open');
                      var videoId = $(this).attr("videoId");
                      $('#delete-video-dialog').data('videoId', videoId);
                      return false;
                    });
                },
                "fnInitComplete" : function(oSettings, json) {
                  //          $('#dispDBTabs-1 fieldset').show();
                  this.fnAdjustColumnSizing();
                  $("#manageVideosTable td").shorten({
                    "showChars" : 20,
                    "moreText" : "More",
                    "lessText" : "Less",
                  });
                }
            });
          },
          error : function(data) {
            /* $('#dispAlertWrapper').dispAlert('show', 'error', null, {
              scrollToTop : true
            }); */
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
          var videoId = dvd.data('videoId');
          deleteVideo(videoId);
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

    function deleteVideo(videoId) {
      var RequestData = new Object();
      var VideoAdmission = new Object();
      VideoAdmission.vId = videoId;
      RequestData.request_data_type = 'videoAdmission';
      RequestData.request_data_method = 'deleteVideoAdmission';
      RequestData.request_data_videoAdmission = VideoAdmission;
      
      $.ajax({
        url: endPointUrl + '/videoAdmission/deleteVideoAdmission',
        type : "POST",
        dataType : "json",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(RequestData),
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

    $(".addBtn").button({
      icons : {
        primary : "ui-icon-plus"
      },
      text : false
    }).unbind('click').click(function(event) {

      $('#add-video-dialog .title').val('');
      $('#add-video-dialog .youtubeUrl').val('');
      $('#add-video-dialog .runningTime').val('');
      $('#add-video-dialog .fileUpload').val('');
      $('#add-video-dialog .description').val('');
      
      $("#add-video-dialog").dialog('open'); 
      return false;
    });

    $("#add-video-dialog").dialog({
      autoOpen : false,
      height : 'auto',
      width : 'auto',
      modal : true,
      buttons : {
        "Save" : function() {
          
          var title = $('#add-video-dialog .title').val();
          var youtubeUrl = $('#add-video-dialog .videoLink').val();
          var runningTime = $('#add-video-dialog .time').val();
          var description = CKEDITOR.instances['txtDescriptionAdd'].getData();

          var Video = new Object();
          Video.title = title;
          Video.youtubeUrl = youtubeUrl;
          Video.runningTime = runningTime;
          Video.description = description;
          Video.idTopicSubAdmission = selectSubAdmission;

          $.ajax({
            url: endPointUrl + 'videoAdmission/uploadFile',
            type: "POST",
            data: new FormData($("#upload-file-form")[0]),
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            success: function(data) {
              var RequestData = new Object();
              var imageUrl = data.request_data_result;
              Video.image = imageUrl;

              RequestData.request_data_type = 'videoAdmission';
              RequestData.request_data_method = 'createVideoAdmission';
              RequestData.request_data_videoAdmission = Video;

              $.ajax({
                url: endPointUrl + '/videoAdmission/createVideoAdmission',
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(RequestData),
                
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

    //Edit Video Details Dialog
    $("#edit-video-dialog").dialog({
      autoOpen : false,
      height : 'auto',
      width : 'auto',
      modal : true,
      buttons : {
        "Save" : function() {
          
          var $evd = $("#edit-video-dialog");
          var video_id = $evd.data('videoId');
          var title = $('#edit-video-dialog .title').val();
          var youtubeUrl = $('#edit-video-dialog .videoLink').val();
          var runningTime = $('#edit-video-dialog .time').val();
          var description = CKEDITOR.instances['txtDescription'].getData();
          var image = $('#edit-video-dialog #upload-file-input').val();
          
          var RequestData = new Object();
          var Video = new Object();
          Video.vId = video_id;
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
                RequestData.request_data_type = 'videoAdmission';
                RequestData.request_data_method = 'updateVideoAdmission';
                RequestData.request_data_videoAdmission = Video;

                $.ajax({
                  url: endPointUrl + '/videoAdmission/updateVideoAdmission',
                  type : "POST",
                  dataType : "json",
                  contentType: "application/json; charset=utf-8",
                  data: JSON.stringify(RequestData),
                  
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
            RequestData.request_data_type = 'videoAdmission';
            RequestData.request_data_method = 'updateVideoAdmission';
            RequestData.request_data_videoAdmission = Video;
            
            $.ajax({
              url: endPointUrl + '/videoAdmission/updateVideoAdmission',
              type : "POST",
              dataType : "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestData),
              
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

  });//End of global function
</script>

<div class="ui-widget-content" style="padding: 12px;">
  <table>
    <tbody>
      <tr>
        <td><label for="selectSubAdmission">Sub Admission Name</label></td>
        <td><select id="selectSubAdmission" style="width: 200px"></select></td>
        <button class="addBtn btnAdd" title="Add Article">Button</button>
      </tr>
    </tbody>
  </table>
</div>

<div >
  <!-- <img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" /> -->
  <fieldset>
    <legend>Manage Videos Admission</legend>
    <div style="">
      <div id="manageVideosTableWrapper">
        <table id="manageVideosTable" class="display">
          <thead>
            <tr>
              <th style="width: 3%; text-align: center;">vId</th>
              <th style="width: 28%; text-align: center;">Title</th>
              <th style="width: 30%; text-align: center;">Description</th>
              <th style="width: 25%; text-align: center;">Video Link</th>
              <th style="width: 4%; text-align: center;">Running Time</th>
              <th style="width: 10%; text-align: center;">Action</th>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </fieldset>
</div>

<div id="edit-video-dialog" title="Edit Video Details:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td>Title:<span class="required">*</span></td>
          <td><input class="title" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td>Video Link:</td>
          <td><input class="youtubeUrl" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td>Running Time:</td>
          <td><input class="runningTime" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td colspan="2">
            <form id="upload-file-form" ng-hide="true">
              Image: <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Description:</td>
          <td>
            <textarea class="description" id="txtDescription" value="" style="width: 250px; height: 150px;"></textarea>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<div id="delete-video-dialog"
  title="Manage Videos : Delete" class="displayNone">
  <div class="ui-widget-content" style="padding: 20px;">
  <p>Do you want to delete?</p>
  </div>
</div>

<div id="add-video-dialog" title="Video Admission:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td>Title:<span class="required">*</span></td>
          <td><input class="title" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td>Video Link:</td>
          <td><input class="youtubeUrl" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td>Running Time:</td>
          <td><input class="runningTime" type="text" value="" style="width: 250px" /></td>
        </tr>
        <tr>
          <td colspan="2">Image: 
            <form id="upload-file-form" ng-hide="true">
              <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Description:</td>
          <td>
            <textarea class="description" id="txtDescriptionAdd" value="" style="width: 250px; height: 150px;"></textarea> 
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
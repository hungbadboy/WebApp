/**
 * Created by hieu on 9/9/15.
 */

function showDeleteForm(element) {
  $("#delete-comment-dialog").dialog('open');
  var cId = $(element).attr("cId");
  $('#delete-comment-dialog').data('cId', cId);
  return false;
};

function deleteArticle(cId) {
    
  var RequestComment = new Object();
  var Comment = new Object();
  Comment.cid = cId;
  RequestComment.request_data_type = 'comments';
  RequestComment.request_data_method = 'deleteComment';
  RequestComment.request_data = Comment;
  
  $.ajax({
    url: endPointUrl + 'comments/deleteComment',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestComment),
    success : function(data) {
      $("#delete-comment-dialog").dialog("close");
      $('#manageTabs-ul a:eq(7)').click();
    },
    error : function(data) {
      $("#delete-comment-dialog").dialog("close");
      $('#manageTabs-ul a:eq(7)').click();
    }
  });
}

$("#delete-comment-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Confirm" : function() {
      var dvd = $('#delete-comment-dialog');
      var cId = dvd.data('cId');
      deleteArticle(cId);
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

$(function() {
    
  // Grab the template script
  var theTemplateScript = $("#list-comment").html();

  // Compile the template
  var theTemplate = Handlebars.compile(theTemplateScript);
  
  // Define our data object

  var RequestComment = new Object();
  var Comment = new Object();
  RequestComment.request_data_type = 'comments';
  RequestComment.request_data_method = 'getAllComment';
  RequestComment.request_data = Comment;

  $.ajax({
    url: endPointUrl + '/comments/getAllComment',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestComment),
    success: function(data) {
      var context = new Object();
      context.comments = data.request_data_result;
      // Pass our data to the template
      var theCompiledHtml = theTemplate(context);
      // Add the compiled html to the page
      $('#comment-items').html(theCompiledHtml).dataTable({});
    }
  });

  $(".deleteBtn").button({
    icons : {
      primary : "ui-icon-trash"
    },
    text : false
  }).on('click', showDeleteForm);
});
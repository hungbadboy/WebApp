/**
 * Created by hieu on 9/9/15.
 */

function showDeleteForm(element) {
  $("#delete-article-dialog").dialog('open');
  var arId = $(element).attr("arId");
  $('#delete-article-dialog').data('arId', arId);
  return false;
};

function showAddForm(event) {
  $('#add-article-dialog .title').val('');
  $('#upload-file-input').val('');
  CKEDITOR.instances['txtDescriptionAdd'].setData('');
  $("#add-article-dialog").dialog('open');
  return false;
};

function showEditForm(element) {
  $('#edit-article-dialog').data('arId', $(element).attr("arId"));
  $('#edit-article-dialog .title').val($(element).attr("titleEdit"));
  
  CKEDITOR.instances['contentEdit'].setData(decodeURIComponent($(element).attr("contentEdit")));   
  
  $("#edit-article-dialog").dialog('open'); 
  return false;
};

function deleteArticle(arId) {
    
  var RequestArticle = new Object();
  var Article = new Object();
  Article.arId = arId;
  RequestArticle.request_data_type = 'article';
  RequestArticle.request_data_method = 'deleteArticle';
  RequestArticle.request_data_article = Article;
  
  $.ajax({
    url: endPointUrl + '/article/deleteArticle',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestArticle),
    success : function(data) {
      $("#delete-article-dialog").dialog("close");
      $('#manageTabs-ul a:eq(5)').click();
    },
    error : function(data) {
      $("#delete-article-dialog").dialog("close");
      $('#manageTabs-ul a:eq(5)').click();
    }
  });
}

$("#delete-article-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Confirm" : function() {
      var dvd = $('#delete-article-dialog');
      var arId = dvd.data('arId');
      deleteArticle(arId);
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
$("#add-article-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Save" : function() {
      
      var title = $('#add-article-dialog .title').val();
      var content = CKEDITOR.instances['txtDescriptionAdd'].getData();
      var stripped = content.replace(/(<([^>]+)>)/ig, "");
      var description = stripped.replace(/&nbsp;/ig, " ");
      if(description.length > 340) {
        description = description.substring(0, 340) + '...';  
      }
      var image = $('#add-article-dialog #upload-file-input').val();

      var Article = new Object();
      Article.authorId = 6;
      Article.title = title;
      Article.description = description;
      Article.content = encodeURIComponent(content);
      var RequestData = new Object();
      RequestData.request_data_type = 'article';
      RequestData.request_data_method = 'createArticle';
      if(image) {
        $.ajax({
          url: endPointUrl + 'article/uploadFile',
          type: "POST",
          data: new FormData($("#add-article-dialog #upload-file-add-form")[0]),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false,
          cache: false,
          success: function(data) {
            var imageUrl = data.request_data_result;
            Article.image = imageUrl;
            RequestData.request_data_article = Article;

            $.ajax({
              url: endPointUrl + '/article/createArticle',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestData),
              
              success : function(data) {
                $("#add-article-dialog").dialog("close");
                $('#manageTabs-ul a:eq(5)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(5)').click();
              }
            });
          }
        });
      } else {
        image = null;
        Article.image = image;
        RequestData.request_data_article = Article;

        $.ajax({
          url: endPointUrl + '/article/createArticle',
          type: "POST",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(RequestData),
          
          success : function(data) {
            $("#add-article-dialog").dialog("close");
            $('#manageTabs-ul a:eq(5)').click();
          },
          error : function(data) {
            $('#manageTabs-ul a:eq(5)').click();
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
$("#edit-article-dialog").dialog({
  autoOpen : false,
  height : 'auto',
  width : 'auto',
  modal : true,
  buttons : {
    "Save" : function() {

      var $ead = $("#edit-article-dialog");
      var arId = $ead.data('arId');
      var title = $('#edit-article-dialog .title').val();
      
      var content = CKEDITOR.instances['contentEdit'].getData();
      var stripped = content.replace(/(<([^>]+)>)/ig, "");
      var description = stripped.replace(/&nbsp;/ig, " ");
      if(description.length > 340) {
        description = description.substring(0, 340) + '...';  
      }

      var image = $('#edit-article-dialog #upload-file-input').val();
      var RequestArticle = new Object();
      var Article = new Object();
      Article.arId = arId;
      Article.title = title;
      Article.description = description;
      Article.content = encodeURIComponent(content);

      if(image) {
        $.ajax({
          url: endPointUrl + 'article/uploadFile',
          type: "POST",
          data: new FormData($("#edit-article-dialog #upload-file-form")[0]),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false,
          cache: false,
          success: function(data) {
            image = data.request_data_result;
            Article.image = image;
            RequestArticle.request_data_type = 'article';
            RequestArticle.request_data_method = 'updateArticle';
            RequestArticle.request_data_article = Article;

            $.ajax({
              url: endPointUrl + 'article/updateArticle',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestArticle),
              
              success : function(data) {
                $("#edit-article-dialog").dialog("close");
                $('#manageTabs-ul a:eq(5)').click();
              },
              error : function(data) {
                $('#manageTabs-ul a:eq(5)').click();
              }
            });
          }
        });
      } else {
        image = null;
        Article.image = image;
        RequestArticle.request_data_type = 'article';
        RequestArticle.request_data_method = 'updateArticle';
        RequestArticle.request_data_article = Article;

        $.ajax({
          url: endPointUrl + 'article/updateArticle',
          type: "POST",
          dataType: "json",
          contentType: "application/json; charset=utf-8",
          data: JSON.stringify(RequestArticle),
          
          success : function(data) {
            $("#edit-article-dialog").dialog("close");
            $('#manageTabs-ul a:eq(5)').click();
          },
          error : function(data) {
            $('#manageTabs-ul a:eq(5)').click();
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
  var theTemplateScript = $("#list-article").html();

  // Compile the template
  var theTemplate = Handlebars.compile(theTemplateScript);

  if(CKEDITOR.instances['txtDescriptionAdd']) {
    CKEDITOR.instances['txtDescriptionAdd'].destroy(true);
    ckeditor('txtDescriptionAdd');
  } else {
    ckeditor('txtDescriptionAdd');
  }

  if(CKEDITOR.instances['contentEdit']) {
    CKEDITOR.instances['contentEdit'].destroy(true);
    ckeditor('contentEdit');
  } else {
    ckeditor('contentEdit');
  }
  
  // Define our data object

  var RequestArticle = new Object();
  var Article = new Object();
  RequestArticle.request_data_type = 'article';
  RequestArticle.request_data_method = 'getAllArticles';
  RequestArticle.request_data = Article;

  $.ajax({
    url: endPointUrl + 'article/getAllArticles',
    type : "POST",
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(RequestArticle),
    success: function(data) {
      var articles = data.request_data_result;
      var context = new Object();
      context.articles = data.request_data_result;
      // Pass our data to the template
      var theCompiledHtml = theTemplate(context);
      // Add the compiled html to the page
      $('#article-items').html(theCompiledHtml).dataTable({});
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
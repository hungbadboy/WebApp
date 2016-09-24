<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript">

  $(function() {
   var endPointUrl = '<s:property value="#session.dataServiceURL"/>';
	console.log("endPointUrl=console======" + endPointUrl);

     var oTable = $('#managePostsTable').dataTable({}); 
    
    $(getVideoDetails);

    CKEDITOR.replace('txtDescription');
    CKEDITOR.replace('txtDescriptionAdd');

		function getVideoDetails() {

			// Destroy the delta definiton table if it exists
			if ($.fn.DataTable.fnIsDataTable($('#managePostsTable')[0])) {
				
				$('#managePostsTable').dataTable().fnDestroy();
				$("#managePostsTable tbody").html('');
				$('#managePostsTable').css('width', '100%');
			}
			
			var RequestData = new Object();
			var Video = new Object();
			RequestData.request_data_type = 'article';
			RequestData.request_data_method = 'getAllArticles';
			RequestData.request_data = Video;
			
		$.ajax({
			url: endPointUrl + '/article/getAllArticles',
			type : "POST",
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(RequestData),
				success : function(data) {
					if (data != null) {
						var rowHtml = '';
						data = data.request_data_result;

						for ( var i = 0; i < data.length; i++) {
							rowHtml += '<tr><td>' + data[i].arId
									+ '</td>';
							rowHtml += '<td>' + data[i].title
									+ '</td>';
							rowHtml += '<td>' + data[i].description
									+ '</td>';
							rowHtml += '<td>' + data[i].numComments
									+ '</td>';
							// var shortQue_text = data[i].content.toString().substr(0, 10);
							// rowHtml += '<td class="dqlQueryBtn" style="cursor:pointer;" question_text="'+ encodeURI(data[i].content) + '">' + shortQue_text + '</td>';		
									
							rowHtml += '<td class="actions"><button class="editBtn" arId="' + data[i].arId + '" title="'+ data[i].title +'" description="' + data[i].description + '" " title="Edit Details">Edit Details</button>';
							
							rowHtml += '<button class="deleteBtn" arId="' + data[i].arId + '" title="Delete">Delete</button>';
							rowHtml += '</td></tr>';
						}
						$('#managePostsTable tbody').append(rowHtml);
					}
					
					$('#managePostsTable').dataTable({
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
                             
                        	   $(".dqlQueryBtn").unbind('click').click(function() {
                   				
									var dqlQuery = $(this).attr("question_text");
									$('#dqlQuery').val(decodeURI(dqlQuery));
									$("#view-dqlQuery-dialog").dialog('open');
									return false;
								  });
                        	  
                  //Edit  Button Logic

  							 $(".editBtn").button({
  									icons : {
  										primary : "ui-icon-pencil"
  									},
  									text : false
  								}).unbind('click').click(function(event) {
  									$('#edit-article-dialog').data('arId',$(this).attr('arId'));
  									$('#edit-article-dialog .title').val($(this).attr('title'));
  									
                    CKEDITOR.instances['txtDescription'].setData($(this).attr('description')); 
  									$("#edit-article-dialog").dialog('open'); 
  									return false;
  								}); 
   
  							//Delete  Logic

								$(".deleteBtn").button({
									icons : {
										primary : "ui-icon-trash"
									},
									text : false
								}).unbind('click').click(function(event) {
									$("#delete-article-dialog").dialog('open');
									var arId = $(this).attr("arId");
									$('#delete-article-dialog').data('arId', arId);
									return false;
								});
  							
                           },

						"fnInitComplete" : function(oSettings, json) {
							//					$('#dispDBTabs-1 fieldset').show();
							this.fnAdjustColumnSizing();
							$("#managePostsTable td").shorten({
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
	
    // View DQL Query Dialog Box

    $("#view-dqlQuery-dialog").dialog({
      autoOpen : false,
      height : 500,
      width : 900,
      modal : true,
      buttons : {
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

	function deleteArticle(arId) {
		
		var RequestData = new Object();
		var Video = new Object();
		Video.arId = arId;
		RequestData.request_data_type = 'article';
		RequestData.request_data_method = 'deleteArticle';
		RequestData.request_data_article = Video;
		
		$.ajax({
			url: endPointUrl + '/article/deleteArticle',
			type : "POST",
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(RequestData),
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

		//Edit Article Details Dialog
		$("#edit-article-dialog").dialog({
			autoOpen : false,
			height : 'auto',
			width : 'auto',
			modal : true,
			buttons : {
				"Save" : function() {

          var $evd = $("#edit-article-dialog");
          var arId = $evd.data('arId');
          var title = $('#edit-article-dialog .title').val();
          
          var description = CKEDITOR.instances['txtDescription'].getData();
          CKEDITOR.instances['txtDescription'].setData('');
          var image = $('#upload-file-input').val();

          var RequestData = new Object();
          var Article = new Object();
          Article.arId = arId;
          Article.title = title;
          Article.description = description;

          if(image) {
            $.ajax({
              url: endPointUrl + 'article/uploadFile',
              type: "POST",
              data: new FormData($("#upload-file-form")[0]),
              enctype: 'multipart/form-data',
              processData: false,
              contentType: false,
              cache: false,
              success: function(data) {
                image = data.request_data_result;
                Article.image = image;
                RequestData.request_data_type = 'article';
                RequestData.request_data_method = 'updateArticle';
                RequestData.request_data_article = Article;

                $.ajax({
                  url: endPointUrl + '/article/updateArticle',
                  type: "POST",
                  dataType: "json",
                  contentType: "application/json; charset=utf-8",
                  data: JSON.stringify(RequestData),
                  
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
            RequestData.request_data_type = 'article';
            RequestData.request_data_method = 'updateArticle';
            RequestData.request_data_article = Article;

            $.ajax({
              url: endPointUrl + '/article/updateArticle',
              type: "POST",
              dataType: "json",
              contentType: "application/json; charset=utf-8",
              data: JSON.stringify(RequestData),
              
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

		$(".addBtn").button({
			icons : {
				primary : "ui-icon-plus"
			},
			text : false
		}).unbind('click').click(function(event) {
			$('#add-article-dialog .title').val('');
      $('#add-article-dialog .fileUpload').val('');
			CKEDITOR.instances['txtDescriptionAdd'].setData('');
			
			$("#add-article-dialog").dialog('open'); 
			return false;
		});

		$("#add-article-dialog").dialog({
			autoOpen : false,
			height : 'auto',
			width : 'auto',
			modal : true,
			buttons : {
				"Save" : function() {
					
					var title = $('#add-article-dialog .title').val();
          var description = CKEDITOR.instances['txtDescriptionAdd'].getData();
					var Article = new Object();

					Article.title = title;
					Article.description = description;

					$.ajax({
				    url: endPointUrl + 'article/uploadFile',
				    type: "POST",
				    data: new FormData($("#upload-file-add-form")[0]),
				    enctype: 'multipart/form-data',
				    processData: false,
				    contentType: false,
				    cache: false,
				    success: function(data) {

				    	var RequestData = new Object();
				    	var imageUrl = data.request_data_result;

				    	Article.image = imageUrl;
				    	RequestData.request_data_type = 'article';
							RequestData.request_data_method = 'createArticle';
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

<div >
	<fieldset>
		<legend>Manage Article Admission</legend>
		<div style="">
			<div id="manageVideosTableWrapper">
				<button class="addBtn btnAdd" title="Add Article">Button</button>
				<table id="managePostsTable" class="display">
					<thead>
						<tr>
							<th align="center">ArID</th>
							<th align="center">TITLE</th>
							<th align="center">DESCRIPTION</th>
							<th align="center">NUM COMMENTS</th>
							<th align="center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</fieldset>
</div>

<div id="view-dqlQuery-dialog" title="View DQL Query" class="displayNone">
	<div class="dialogAlertWrapper" class="displayNone"></div>
	<div class="contentWrapper1">
		<dl>
			<dt>
				<br> <br> <label for="dqlQuery" class="">Question Text :</label> <br>
			</dt>
			<dd>
				<textarea id="dqlQuery" class="ui-corner-all monospaceFont greyBorder wideTextArea" rows="35" cols="100"></textarea>
			</dd>
		</dl>
	</div>
</div>

<div id="edit-article-dialog" title="Article Details:" class="displayNone">
	<div class="dialogAlertWrapper" class="displayNone"></div>
	<div class="contentWrapper">
		<table cellpadding="15">
			<thead>
			</thead>
			<tbody>
				<tr>
					<td>Title:<span class="required">*</span></td>
					<td><input class="title" type="text" value="" style="width: 590px" /></td>
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
            <textarea class="description" id="txtDescription" value="" style="width: 250px; height: 150px;"></textarea>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<div id="delete-article-dialog"
	title="Manage Article : Delete" class="displayNone">
	<div class="ui-widget-content" style="padding: 20px;">
	<p>Do you want to delete?</p>
	</div>
</div>

<div id="add-article-dialog" title="Article:" class="displayNone">
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
					<td colspan="2">Image: 
					  <form id="upload-file-add-form" ng-hide="true">
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
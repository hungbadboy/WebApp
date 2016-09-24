<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript">
	$(function() {
		
		<%
		
		String endPointUrl=(String)application.getAttribute("endPointUrl");
		System.out.println("endPointUrl=============jspjsp==--=============--------="+endPointUrl);
		%>
		var endPointUrl='<%=endPointUrl%>';
		console.log("endPointUrl=console======"+endPointUrl);

		 var oTable = $('#managePostsTable').dataTable({}); 
		
		
		$(getVideoDetails);

		

	
		
		

		function getVideoDetails() {
			
			
			
			// Destroy the delta definiton table if it exists
			if ($.fn.DataTable.fnIsDataTable($('#managePostsTable')[0])) {
				//alert('getVideoDetails');
				$('#managePostsTable').dataTable().fnDestroy();
				$("#managePostsTable tbody").html('');
				$('#managePostsTable').css('width', '100%');
			}
			
			var RequestData = new Object();
			var Video = new Object();
			Video.vid=1;
			RequestData.request_data_type = 'post';
			RequestData.request_data_method = 'getPosts';
			RequestData.request_data = Video;
			
			
		$.ajax({
			url:endPointUrl+'/post/getPosts',
			type : "POST",
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(RequestData),
						success : function(data) {
							if (data != null) {
								var rowHtml = '';
								data = data.request_data_result;
								for ( var i = 0; i < data.length; i++) {
									rowHtml += '<tr><td>' + data[i].pid
											+ '</td>';
									rowHtml += '<td>' + data[i].subject
											+ '</td>';
									rowHtml += '<td>' + data[i].topic
											+ '</td>';
									rowHtml += '<td>' + data[i].title
											+ '</td>';
									var shortQue_text = data[i].content.toString().substr(0, 10);
									rowHtml += '<td class="dqlQueryBtn" style="cursor:pointer;" question_text="'+ encodeURI(data[i].content) + '">' + shortQue_text + '</td>';		
											
									rowHtml += '<td class="actions"><button class="editBtn" pId="'+data[i].pid+'" title="'+data[i].title+'" content="'+data[i].content+'" " title="Edit Details">Edit Details</button>';
									
									rowHtml += '<button class="deleteBtn" pId="'+data[i].pid+'" title="Delete">Delete</button>';
									rowHtml += '</td></tr>';
								}
								$('#managePostsTable tbody').append(rowHtml);
							}

							
							
							$('#managePostsTable').dataTable(
									{
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
				  									$('#edit-post-dialog').data('pId',$(this).attr('pId'));
				  									$('#edit-post-dialog .title').val($(this).attr('title'));
				  									
				  									$('#edit-post-dialog .content').val($(this).attr('content'));
				  									
				  								
				  									$("#edit-post-dialog").dialog('open'); 
				  									return false;
				  								}); 
				                        	   
				                        	   
				  							//Delete  Logic

												$(".deleteBtn").button({
													icons : {
														primary : "ui-icon-trash"
													},
													text : false
												}).unbind('click').click(function(event) {
													$("#delete-post-dialog").dialog('open');
													var pId = $(this).attr("pId");
													$('#delete-post-dialog').data('pId', pId);
													return false;
													
													
												});
				  							
				                           },

										"fnInitComplete" : function(oSettings,
												json) {
											//					$('#dispDBTabs-1 fieldset').show();
											this.fnAdjustColumnSizing();
											$("#managePostsTable td").shorten(
													{
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
		
	
	$("#delete-post-dialog").dialog({
			autoOpen : false,
			height : 'auto',
			width : 'auto',
			modal : true,
			buttons : {
				"Confirm" : function() {
					var dvd = $('#delete-post-dialog');
					var pId = dvd.data('pId');
					deletePost(pId);
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

	function deletePost(pId) {
		
		var RequestData = new Object();
		var Video = new Object();
		Video.pid=pId;
		RequestData.request_data_type = 'post';
		RequestData.request_data_method = 'removePost';
		RequestData.request_data = Video;
		
		$.ajax({
			url:endPointUrl+'/post/removePost',
			type : "POST",
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(RequestData),
			success : function(data) {
				$("#delete-post-dialog").dialog("close");
				$('#manageTabs-ul a:eq(3)').click();
				

			},
			error : function(data) {
				$("#delete-post-dialog").dialog("close");
				$('#manageTabs-ul a:eq(3)').click();
			}
		});

	}
	
		function fnFormatDetails(oTable, nTr, sOut) {
			
			return sOut;
		}

	
		

	

		

	

		//Edit Video Details Dialog
		$("#edit-post-dialog").dialog(
				{
					autoOpen : false,
					height : 'auto',
					width : 'auto',
					modal : true,
					buttons : {
						"Save" : function() {

							/* if (isEmptyOrNull($('.videoId', this).val()) ){
								return false;
							} */
							// var updateJson;
							var $evd = $("#edit-post-dialog");
							var p_id = $evd.data('pId');
							var title = $('#edit-post-dialog .title')
									.val();
							//var video_link = $('#edit-video-dialog .videoLink').val();
							var content = $(
									'#edit-post-dialog .content').val();
							
							
						
							
							var RequestData = new Object();
							var Video = new Object();
							Video.pid=p_id;
							Video.title=title;
							Video.description=content;
							RequestData.request_data_type = 'post';
							RequestData.request_data_method = 'editPost';
							RequestData.request_data = Video;
							
						
							
							$.ajax({
								url:endPointUrl+'/post/editPost',
								type : "POST",
								dataType : "json",
								contentType: "application/json; charset=utf-8",
								data: JSON.stringify(RequestData),
								
								success : function(data) {
									//$(this).dialog("close");
									$("#edit-post-dialog").dialog("close");
									//$('#manageTabs-ul a:eq(1)').click();
									$('#manageTabs-ul a:eq(3)').click();
									
									//processMessage(data.responseStatus, data.responseMsg);
									//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
								},
								error : function(data) {
									$('#manageTabs-ul a:eq(3)').click();
									//processMessage(data.responseStatus, data.responseMsg);
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

	});//End of global function
</script>






<div >

	<!-- <img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" /> -->
	<fieldset>
		<legend>Manage Post Data</legend>
		<div style="">
			<div id="manageVideosTableWrapper">
				<table id="managePostsTable" class="display">
					<thead>
						<tr>
							
							<th align="center">PID</th>
							<th align="center">SUBJECT</th>
							<th align="center">TOPIC</th>
							<th align="center">TITLE</th>
							<th  align="center">CONTENT</th>
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

<div id="edit-post-dialog" title="Post Video Details:" class="displayNone">
	<div class="dialogAlertWrapper" class="displayNone"></div>
	<div class="contentWrapper">
		<table cellpadding="15">
			<thead>
			</thead>
			<tbody>
				<tr>
					<td>TITLE:<span class="required">*</span></td>
					<td><input class="title" type="text" value="" style="width: 250px" /></td>
				</tr>
				<!-- <tr>
					<td>Video Link:</td>
					<td><input class="videoLink" type="text" value="" style="width: 250px" /></td>
				</tr> -->
				<tr>
					<td>CONTENT :</td>
					<td><input class="content" type="text" value="" style="width: 250px" /></td>
				</tr>
				
			</tbody>
		</table>
	</div>
</div>

<div id="delete-post-dialog"
	title="Manage Posts : Delete" class="displayNone">
	<div class="ui-widget-content" style="padding: 20px;">
	<p>Do you want to delete?</p>
	</div>
</div>
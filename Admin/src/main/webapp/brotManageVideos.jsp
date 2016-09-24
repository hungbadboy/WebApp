<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript">
	$(function() {

		 var oTable = $('#manageVideosTable').dataTable({}); 
		
		//$("#sub_category").hide();
		
		// Populate Videos Information
		$(populateSubjects);

		function populateSubjects() {
			/* $('#selectSubject').html('<option value="-1">Loading...</option>');
			$('#selectSubject').chosen(); */
			var RequestData = new Object();
			var Video = new Object();
			Video.vid=1;
			RequestData.request_data_type = 'subjects';
			RequestData.request_data_method = 'fetchSubjects';
			RequestData.request_data = Video;
			$.ajax({
				url:endPointUrl+'/subjects/fetchSubjects',
				type : "POST",
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(RequestData),
						
						success : function(data) {
							var options = '<option selected=true value="selectOne">Select One</option>';
							if(data != null){
								
								data = data.request_data_result;
								
								for ( var i = 0; i < data.length; i++) {
									options += '<option value="'+data[i].subject_id+'">'+ data[i].subject_name + '</option>';
									
									//rowHtml += '<input type=hidden name=subject_id'+data[i].subject_id+' id='+data[i].subject_id+' value'+data[i].subject_id;
								}
								
							}

							/* for ( var i = 0; i < data.subjectList.length; i++) {
								options += '<option value="'+data.subjectList[i][0]+'">'
										+ data.subjectList[i][1] + '</option>';
							} */
							$('#selectSubject').html(options).trigger(
									"liszt:updated");
						},
						error : function(data) {
							$('#selectSubject').html(
									'<option value="-1">Error</option>');
							$('#selectSubject').trigger("liszt:updated");
							/* $('#dispAlertWrapper').dispAlert('show', 'error', null, {
								scrollToTop : true
							}); */
						}
					});

		}

		$('#selectSubject').on('change', function() {
			var selectedSubject = $('option:selected', this).val();
			if (selectedSubject != 'selectOne') {
				
				getVideoDetails(selectedSubject);
			}
		});
		
		

		function getVideoDetails(selectedSubject) {
			
			
			/* $('#selectSubject').html('<option value="-1">Loading...</option>');
			$('#selectSubject').chosen(); */
			// Populate the Subjects select
			// Destroy the delta definiton table if it exists
			if ($.fn.DataTable.fnIsDataTable($('#manageVideosTable')[0])) {
				//alert('getVideoDetails');
				$('#manageVideosTable').dataTable().fnDestroy();
				$("#manageVideosTable tbody").html('');
				$('#manageVideosTable').css('width', '100%');
			}
			
			var RequestData = new Object();
			var Video = new Object();
			Video.stringJson=selectedSubject;
			RequestData.request_data_type = 'video';
			RequestData.request_data_method = 'getVideoDetails';
			RequestData.request_data = Video;
			
			$
					.ajax({
						url:endPointUrl+'/video/getVideoDetails',
						type : "POST",
						dataType : "json",
						contentType: "application/json; charset=utf-8",
						data: JSON.stringify(RequestData), 

						
						success : function(data) {
							if (data != null) {
								var rowHtml = '';
								data = data.request_data_result;
								for ( var i = 0; i < data.length; i++) {
									rowHtml += '<tr><td>' + data[i].subject_category_id
											+ '</td>';
									rowHtml += '<td>' + data[i].subject_category_name
											+ '</td>';
									rowHtml += '<td>' + data[i].description
											+ '</td>';
									rowHtml += '<td>' + data[i].active
											+ '</td>';
											
									rowHtml += '<td class="actions"><button class="editBtn" videoId="'+data[i].subject_category_id+'" videoName="'+data[i].subject_category_name+'" description="'+data[i].description+'" active="'+data[i].active+'" title="Edit Details">Edit Details</button>';
									
									rowHtml += '<button class="deleteBtn" videoId="'+data[i].subject_category_id+'" title="Delete">Delete</button>';
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
							
							$('#manageVideosTable').dataTable(
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
				                             
				                        	   $(".openimg", nRow).unbind('click').click(function(event) {
													var nTr = $(this).parents('tr')[0];
													if ( oTable.fnIsOpen(nTr) )
													{
														this.src = "css/images/details_open.png";
														oTable.fnClose( nTr );
													} else{
														this.src = "css/images/details_close.png";
														var subject_id;
																var selectedSubject = $('#selectSubject option:selected').val();
																var selectedSubjectName = $('#selectSubject option:selected').text();
																console.log("selectedSubjectName=console======"+selectedSubjectName);
																
																if (selectedSubject != 'selectOne') {
																	subject_id = selectedSubject;
																	//alert("subject_id -- " + subject_id);
																} else {
																	alert("Please select the Subject");
																}
																
																var aData = oTable.fnGetData( nTr );
																var categoryId = aData[1];
																
																var categoryName = aData[2];
																console.log("aData=console======"+categoryName);
																//subject_category_name:categoryName
																//alert(categoryName);
																
																
																//$("#sub_category").show();
																
																/* if ($.fn.DataTable.fnIsDataTable($('#manageSubVideosTable')[0])) {
																	$('#manageSubVideosTable').dataTable().fnDestroy();
																	$("#manageSubVideosTable tbody").html('');
																	$('#manageSubVideosTable').css('width', '100%');
																} */
																var sOut = '<p><a id="new">Add new row</a></p>';
																//var sOut = '<a id="AddNewRow" >Add new row</a>';
																sOut += '<table id="manageSubVideosTable" class="display">';
																			sOut += '<thead>';
																			sOut += '<tr>';
																			sOut += '<th>TITLE</th>';
																			sOut += '<th>SUB_CATEGORY_NAME</th>';
																			sOut += '<th>DESCRIPTION</th>';
																			sOut += '<th>VIDEO_LINK</th>';
																			sOut += '<th>IMAGE</th>';
																			sOut += '<th>ACTIVE</th>';
																			sOut += '<th>EDIT</th>';
																			sOut += '<th>DELETE</th>';
																			sOut += '</tr>';
																			sOut += '</thead>';
																			sOut += '<tbody>';
																			
																			var RequestData = new Object();
																			var Video = new Object();
																			Video.subjectId=subject_id;
																			Video.cid=aData[1];
																			RequestData.request_data_type = 'video';
																			RequestData.request_data_method = 'getSubCategoryData';
																			RequestData.request_data = Video;
																		
																$.ajax({
																	url:endPointUrl+'/video/getSubCategoryData',
																	type : "POST",
																	dataType : "json",
																	contentType: "application/json; charset=utf-8",
																	data: JSON.stringify(RequestData),
																	
																	success : function(data) {
																	//alert(' #######x data '+data);
																		if(data != null && data.request_data_result != null ){
																			console.log("endPointUrl=console=====request_data_result=");
																			//SELECT vid,title,subTopic,description,image, url,runningtime,videoEnable  FROM Sib_Videos where topicId ='7'  AND subjectId  ='3' ORDER BY subTopic desc

																			//SELECT vid,title,subTopic,image,,description, url,runningtime,videoEnable  FROM Sib_Videos where topicId ='http.categoryid'  AND subjectId  ='http.subjectid' ORDER BY subTopic desc
																			data = data.request_data_result;
																			//alert('befoore dynamic row'+data.length);
																			for ( var i = 0; i < data.length; i++) {
																				//sOut += '<tr><td>' + data[i].subject_sub_category_id + '</td>';
																				sOut += '<tr><td>' + data[i].title + '</td>';
																				sOut += '<td>' + data[i].subTopic + '</td>';
																				sOut += '<td>' + data[i].description + '</td>';
																				sOut += '<td>' + data[i].url + '</td>';
																				sOut += '<td>' + data[i].image + '</td>';
																				sOut += '<td>' + data[i].videoEnable + '</td>';
																				sOut += '<td><a class="editSCBtn" sub_cat_id="'+data[i].vid+'" title="Edit">Edit</a></td>';
																				sOut += '<td><a class="deleteSCBtn" sub_cat_id="'+data[i].vid+'" videoId="'+data[i].vid+'" title="Delete">Delete</a></td>';
																				sOut += '</tr>';
																			}
																			sOut += '</tbody>';
																			sOut += '</table>';
																			//$('.dataTable').dataTable();
																			//sOut += '$("#manageSubVideosTable").dataTable({"sPaginationType": "full_numbers","bJQueryUI" : "true"});';
																			//alert('sOut '+sOut);
																			}else{
																				sOut += '</tbody>';
																				sOut += '</table>';
																			}
																		console.log("endPointUrl=console=====sOut=");
																		var nEditing = null;
																		
																			oTable.fnOpen( nTr,  sOut, 'details' );
																			var oTable1 = $('#manageSubVideosTable').dataTable({
																				
																				"bPaginate": false, //hide pagination control
																				"bFilter": false, //hide filter control 
																				// Initialize buttons before drawing the row
																				"fnRowCallback" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
																				
																				$(".editSCBtn", nRow).unbind('click').click(function(event) {
																					
																					var subCategoryId = $(this).attr("sub_cat_id");
																					var nRow = $(this).parents('tr')[0];
																					
																					//var aData = oTable1.fnGetData(nRow);
																					
																					
													                        	   	var jqTds = $('>td', nRow);
													                        	   	jqTds[0].innerHTML = '<input type="text" value="'+aData[0]+'">';
													                        	   	jqTds[1].innerHTML = '<input type="text" value="'+aData[1]+'">';
													                        	   	jqTds[2].innerHTML = '<input type="text" value="'+aData[2]+'">';
													                        	   	jqTds[3].innerHTML = '<input type="text" value="'+aData[3]+'">';
													                        	   	jqTds[4].innerHTML = '<input type="text" value="'+aData[4]+'">';
													                        	   	jqTds[5].innerHTML = '<input type="text" value="'+aData[5]+'">';
													                        	   	jqTds[6].innerHTML = '<a class="saveSCBtn">Save</a>';
													                        	   	
													                        	   	$(".saveSCBtn", nRow).unbind('click').click(function(event) {
																						
																						
																						var jqInputs = $('input', nRow);
														                        	   	oTable1.fnUpdate( jqInputs[0].value, nRow, 0, false );
														                        	   	oTable1.fnUpdate( jqInputs[1].value, nRow, 1, false );
														                        	   	oTable1.fnUpdate( jqInputs[2].value, nRow, 2, false );
														                        	   	oTable1.fnUpdate( jqInputs[3].value, nRow, 3, false );
														                        	   	oTable1.fnUpdate( jqInputs[4].value, nRow, 4, false );
														                        	   	oTable1.fnUpdate( jqInputs[5].value, nRow, 5, false );
														                        	   	oTable1.fnUpdate( '<a class="editSCBtn" >Edit</a>', nRow, 6, false );
														                        	   	oTable1.fnDraw();
														                        	   	
														                        	   	var title = jqInputs[0].value;
														                        	   	var subCategoryName = jqInputs[1].value;
														                        	   	var description = jqInputs[2].value;
														                        		var videoLink = jqInputs[3].value;
														                        		var image = jqInputs[4].value;
														                        	   	var active = jqInputs[5].value;
														                        	   	
														                        	  	var inputData = {vid:subCategoryId,subjectid:subject_id,subject:selectedSubjectName,title:title,subject_category_id:categoryId,subject_category_name:categoryName,subject_sub_category_name:subCategoryName,description:description,video_link:videoLink,image:image,active:active};
																                		
														                        		
														                        	 	//var inputData = {subjectid:subject_id,subject:selectedSubjectName,title:title,subject_sub_category_id:categoryId,subject_category_name:categoryName,subject_sub_category_name:subCategoryName,description:description,video_link:videoLink,image:image,active:active};
														                				//	$('.dialogAlertWrapper', this).dispAlert('hide');
														                				var	updateJson = JSON.stringify(inputData);
														                				
														                				var RequestData = new Object();
																						var Video = new Object();
																						Video.stringJson=updateJson;
																						RequestData.request_data_type = 'video';
																						RequestData.request_data_method = 'updateSubCategory';
																						RequestData.request_data = Video;
																			
														                					
														                				$.ajax({
														                					url:endPointUrl+'/video/updateSubCategory',
																							type : "POST",
																							dataType : "json",
																							contentType: "application/json; charset=utf-8",
																							data: JSON.stringify(RequestData),
														            						success : function(data) {
														            							//processMessage(data.responseStatus, data.responseMsg);
														            							//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
														            						},
														            						error : function(data) {
														            							//processMessage(data.responseStatus, data.responseMsg);
														            						}
														            					});
																					});
													                        	   	
													                        	   	$(".deleteSCBtn", nRow).unbind('click').click(function(event) {
																						
													                        	   		
														                        		var subCategoryId = $(this).attr("sub_cat_id");
														                        		
														                        		var RequestData = new Object();
																						var Video = new Object();
																						Video.stringJson=subCategoryId;
																						RequestData.request_data_type = 'video';
																						RequestData.request_data_method = 'deleteSubCategory';
																						RequestData.request_data = Video;
																			
														                        		
														                        		
														                        		$.ajax({
														                        			url:endPointUrl+'/video/deleteSubCategory',
																							type : "POST",
																							dataType : "json",
																							contentType: "application/json; charset=utf-8",
																							data: JSON.stringify(RequestData),
														            						
														            						success : function(data) {
														            							//processMessage(data.responseStatus, data.responseMsg);
														            							//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
														            							//$(".openimg", nRow).click();
														            							$('#manageTabs-ul a:eq(1)').click();
														            							// $(".openimg", nRow).unbind('click').click(function(event) {
														            						},
														            						error : function(data) {
														            							//processMessage(data.responseStatus, data.responseMsg);
														            						}
														            					});
														                        	   	
														                        	   	
																					});
													                        	   	
																				});
																				
																				$(".saveSCBtn", nRow).unbind('click').click(function(event) {
																					
																					
																					var jqInputs = $('input', nRow);
													                        	   	oTable1.fnUpdate( jqInputs[0].value, nRow, 0, false );
													                        	   	oTable1.fnUpdate( jqInputs[1].value, nRow, 1, false );
													                        	   	oTable1.fnUpdate( jqInputs[2].value, nRow, 2, false );
													                        	   	oTable1.fnUpdate( jqInputs[3].value, nRow, 3, false );
													                        	   	oTable1.fnUpdate( jqInputs[4].value, nRow, 4, false );
													                        	   	oTable1.fnUpdate( jqInputs[5].value, nRow, 5, false );
													                        	   	oTable1.fnUpdate( '<a class="editSCBtn" >Edit</a>', nRow, 6, false );
													                        	   	oTable1.fnDraw();
													                        	   	
													                        	   	
													                        	 /* 	var subCategoryName = jqInputs[0].value;
													                        	   	var description = jqInputs[1].value;
													                        	   	var populatVideoRating = jqInputs[2].value;
													                        		var mentorName = jqInputs[3].value;
													                        		var videoLink = jqInputs[4].value;
													                        		var image = jqInputs[5].value; */
													                        		
													                        		var title = jqInputs[0].value;
													                        	   	var subCategoryName = jqInputs[1].value;
													                        	   	var description = jqInputs[2].value;
													                        		var videoLink = jqInputs[3].value;
													                        		var image = jqInputs[4].value;
													                        	   	var active = jqInputs[5].value;
													                        		
													                        	   	//var inputData = {subject_category_id:categoryId,subject_sub_category_name:subCategoryName,description:description,popular_video:populatVideoRating,mentor_name:mentorName,video_link:videoLink};
													                        	   	var inputData = {subjectid:subject_id,subject:selectedSubjectName,title:title,subject_category_id:categoryId,subject_category_name:categoryName,subject_sub_category_name:subCategoryName,description:description,video_link:videoLink,image:image,active:active};
														                				
													                        	   	
													                				//	$('.dialogAlertWrapper', this).dispAlert('hide');
													                				var	saveSubjectSubCategoryJson = JSON.stringify(inputData);
													                				var RequestData = new Object();
																					var Video = new Object();
																					Video.stringJson=saveSubjectSubCategoryJson;
																					RequestData.request_data_type = 'video';
																					RequestData.request_data_method = 'saveSubCategory';
																					RequestData.request_data = Video;
																		
													                				
													                				$.ajax({
													                					url:endPointUrl+'/video/saveSubCategory',
																						type : "POST",
																						dataType : "json",
																						contentType: "application/json; charset=utf-8",
																						data: JSON.stringify(RequestData),
													            						complete : function(data) {
													            							
													            						},
													            						success : function(data) {
													            							//processMessage(data.responseStatus, data.responseMsg);
													            							//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
													            						},
													            						error : function(data) {
													            							//processMessage(data.responseStatus, data.responseMsg);
													            						}
													            					});
													                        	   	
																				});
																				
																				$(".deleteSCBtn", nRow).unbind('click').click(function(event) {
																					
													                        		var subCategoryId = $(this).attr("sub_cat_id");
													                        		
													                        		var RequestData = new Object();
																					var Video = new Object();
																					Video.stringJson=subCategoryId;
																					RequestData.request_data_type = 'video';
																					RequestData.request_data_method = 'deleteSubCategory';
																					RequestData.request_data = Video;
																															                        		
													                        		$.ajax({
													                        			url:endPointUrl+'/video/deleteSubCategory',
																						type : "POST",
																						dataType : "json",
																						contentType: "application/json; charset=utf-8",
																						data: JSON.stringify(RequestData),
													            						
													            						success : function(data) {
													            							//processMessage(data.responseStatus, data.responseMsg);
													            							//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
													            							$('#manageTabs-ul a:eq(1)').click();
													            						},
													            						error : function(data) {
													            							//processMessage(data.responseStatus, data.responseMsg);
													            						}
													            					});
													                        	   	
													                        	   	
																				});
																				}, 
																				"fnInitComplete" : function(oSettings,json) {
																				// $('#dispDBTabs-1 fieldset').show();
																				/* this.fnAdjustColumnSizing();
																				$("#manageAAndAnsTable td").shorten({
																				"showChars" : 20,
																				"moreText" : "More",
																				"lessText" : "Less",
																				}); */
																				}
																				
																				
																			});
																			
																			//Delete  Logic

																			/* $(".addSubCatBtn").button({
																				icons : {
																					primary : "ui-icon-plusthick"
																				},
																				text : false
																			}).unbind('click').click(function(event) {
																				e.preventDefault();
																				
																				var aiNew = oTable1.fnAddData( [ '', '', '', '', '', '',
																					'<a class="edit" href="">Edit</a>', '<a class="delete" href="">Delete</a>' ] );
																				var nRow = oTable.fnGetNodes( aiNew[0] );
																				editRow( oTable, nRow );
																				nEditing = nRow;
																			});  */
																			$('#new').click( function (e) {
																				
																				e.preventDefault();
																				
																				var aiNew = oTable1.fnAddData( [ '', '', '', '', '','', 
																					'<a class="saveSCBtn">Save</a>', '<a class="deleteSCBtn">Delete</a>' ] );
																				var nRow = oTable1.fnGetNodes( aiNew[0] );
																				editRow( oTable1, nRow );
																				nEditing = nRow;
																			} );
																			
																			$('#manageSubVideosTable a.delete').live('click', function (e) {
																				e.preventDefault();
																				
																				var nRow = $(this).parents('tr')[0];
																				oTable1.fnDeleteRow( nRow );
																			} );
																			
																			$('#manageSubVideosTable a.edit').live('click', function (e) {
																				e.preventDefault();
																				
																				/* Get the row as a parent of the link that was clicked on */
																				var nRow = $(this).parents('tr')[0];
																				
																				if ( nEditing !== null && nEditing != nRow ) {
																					/* Currently editing - but not this row - restore the old before continuing to edit mode */
																					restoreRow( oTable1, nEditing );
																					editRow( oTable1, nRow );
																					nEditing = nRow;
																				}
																				else if ( nEditing == nRow && this.innerHTML == "Save" ) {
																					/* Editing this row and want to save it */
																					saveRow( oTable1, nEditing );
																					nEditing = null;
																				}
																				else {
																					/* No edit in progress - let's start one */
																					editRow( oTable1, nRow );
																					nEditing = nRow;
																				}
																			} );
																			
																			
																			},
																			error : function(data) {
																			}
																		}); //end of $ajax
																
																		
														
													}
												});
				                        	   
				                        	   function saveRow ( oTable, nRow )
				                        	   {
				                        	   	var jqInputs = $('input', nRow);
				                        	   	oTable.fnUpdate( jqInputs[0].value, nRow, 0, false );
				                        	   	oTable.fnUpdate( jqInputs[1].value, nRow, 1, false );
				                        	   	oTable.fnUpdate( jqInputs[2].value, nRow, 2, false );
				                        	   	oTable.fnUpdate( jqInputs[3].value, nRow, 3, false );
				                        	   	oTable.fnUpdate( jqInputs[4].value, nRow, 4, false );
				                        	   	oTable.fnUpdate( '<a class="edit" href="">Edit</a>', nRow, 5, false );
				                        	   	oTable.fnDraw();
				                        	   }
				                        	   
				                        	   function restoreRow ( oTable, nRow )
												{
													var aData = oTable.fnGetData(nRow);
													var jqTds = $('>td', nRow);
													
													for ( var i=0, iLen=jqTds.length ; i<iLen ; i++ ) {
														oTable.fnUpdate( aData[i], nRow, i, false );
													}
													
													oTable.fnDraw();
												}
				                        	   
				                        	   function editRow ( oTable1, nRow )
				                        	   {
				                        	   	var aData = oTable1.fnGetData(nRow);
				                        	   	var jqTds = $('>td', nRow);
				                        	   	jqTds[0].innerHTML = '<input type="text" value="'+aData[0]+'">';
				                        	   	jqTds[1].innerHTML = '<input type="text" value="'+aData[1]+'">';
				                        	   	jqTds[2].innerHTML = '<input type="text" value="'+aData[2]+'">';
				                        	   	jqTds[3].innerHTML = '<input type="text" value="'+aData[3]+'">';
				                        	   	jqTds[4].innerHTML = '<input type="text" value="'+aData[4]+'">';
				                        	   	jqTds[5].innerHTML = '<input type="text" value="'+aData[5]+'">';
				                        	   //	jqTds[6].innerHTML = '<a class="edit" href="#">Save</a>';
				                        	   }
				                        	   //Edit  Button Logic

				  							 $(".editBtn").button({
				  									icons : {
				  										primary : "ui-icon-pencil"
				  									},
				  									text : false
				  								}).unbind('click').click(function(event) {
				  									$('#edit-video-dialog').data('videoId',$(this).attr('videoId'));
				  									$('#edit-video-dialog .videoName').val($(this).attr('videoName'));
				  									
				  									$('#edit-video-dialog .description').val($(this).attr('description'));
				  									var isChecked = $(this).attr('active');
				  									if(isChecked == 'Y'){
				  										$('#edit-video-dialog .active').attr('checked',true);
				  									}
				  								
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
													
													
													/* $.ajax({
														url : "ajax/deleteVideo.action",
														data : ({
															'videoId' : videoId,
														}),
														complete : function(data) {

														},
														success : function(data, textStatus, JqXHR) {
															
															return false;
														}
													});
													return false; */
												});
				  							
				                           },

										"fnInitComplete" : function(oSettings,
												json) {
											//					$('#dispDBTabs-1 fieldset').show();
											this.fnAdjustColumnSizing();
											$("#manageVideosTable td").shorten(
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
		var Video = new Object();
		Video.stringJson=videoId;
		RequestData.request_data_type = 'video';
		RequestData.request_data_method = 'deleteVideo';
		RequestData.request_data = Video;
		
		$.ajax({
			url:endPointUrl+'/video/deleteVideo',
			type : "POST",
			dataType : "json",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(RequestData),
			success : function(data) {
				$("#delete-video-dialog").dialog("close");
				$('#manageTabs-ul a:eq(1)').click();
				

			},
			error : function(data) {
				$("#delete-video-dialog").dialog("close");
				$('#manageTabs-ul a:eq(1)').click();
			}
		});

	}
	
		function fnFormatDetails(oTable, nTr, sOut) {
			/* var aData = oTable.fnGetData( nTr );
			
			alert("subject id : " + subject_id + " Category Id " + aData[1]);
			//$("#sub_category").show();
			
			alert('before 12345');
			
			
			$.ajax({
				url : "ajax/getSubCategoryData.action",
				type : "POST",
				dataType : "json",
				data : ({
					subject_id:subject_id,
					category_id:aData[1]
				}),
				complete : function(data) {
				},
				success : function(data, textStatus, jqXHR) {
				alert(' #######x data '+data);
					if(data != null){
						
						var sOut = '<table id="manageSubVideosTable" class="display">';
						sOut += '<thead>';
						sOut += '<tr>';
						sOut += '<th>SUBJECT_SUB_CATEGORY_ID</th>';
						sOut += '<th>SUBJECT_SUB_CATEGORY_NAME</th>';
						sOut += '<th>DESCRIPTION</th>';
						sOut += '<th>POPULAR_VIDEO_RATING</th>';
						sOut += '<th>EMAIL_ID MENTOR_NAME</th>';
						sOut += '<th>IMAGE</th>';
						sOut += '<th>EDIT</th>';
						sOut += '<th>DELETE</th>';
						sOut += '</tr>';
						sOut += '</thead>';
						sOut += '<tbody>';
						data = data.subCategoriesDetails;
						alert('befoore dynamic row'+data.length);
						for ( var i = 0; i < data.length; i++) {
							sOut += '<tr><td>' + data[i].subject_sub_category_id + '</td>';
							sOut += '<td>' + data[i].subject_sub_category_name + '</td>';
							sOut += '<td>' + data[i].description + '</td>';
							sOut += '<td>' + data[i].popular_video_rating + '</td>';
							sOut += '<td>' + data[i].mentor_name + '</td>';
							sOut += '<td>' + data[i].image + '</td>';
							sOut += '<td><button class="editSCBtn" sub_cat_id="'+data[i].subject_category_id+'" sub_cat_name="'+data[i].subject_category_name+'" description="'+data[i].description+'" pop_video_rating="'+data[i].popular_video_rating+'" mentor_name="'+data[i].mentor_name+'" image="'+data[i].image+'" title="Edit">Edit</button></td>';
							sOut += '<td><button class="deleteSCBtn" videoId="'+data[i].subject_category_id+'" title="Delete">Delete</button></td>';
							sOut += '</tr>';
						}
						sOut += '</tbody>';
						sOut += '</table>';
						alert('sOut '+sOut);
						}
					
						},
						error : function(data, textStatus, jqXHR) {
						}
					}); //end of $ajax */
			return sOut;
		}

		function getSubCategoryData(subject_id, category_id) {

		}
		// Initialize appendGrid
		$('#tblVideosAppendGrid').appendGrid({
			caption : 'Add Subject Category',
			initRows : 1,
			columns : [ {
				name : 'VideoName',
				display : 'Category Name',
				type : 'text',
				ctrlAttr : {
					maxlength : 100
				},
				ctrlCss : {
					width : '200px'
				}
			}, {
				name : 'Description',
				display : 'Description',
				type : 'text',
				ctrlAttr : {
					maxlength : 250
				},
				ctrlCss : {
					width : '250px'
				}
			}, {
				name : 'Active',
				display : 'Active',
				type : 'checkbox',
				onClick : function(evt, rowIndex) {
				}
			}

			],
			initData : []
		});

		$("#btnSave").button({
			icons : {
				primary : "ui-corner-right"
			},
		//text : false
		}).unbind('click').click(function(event) {
			var subject_id;
			//	for ( var int = 0; int < array.length; int++) {
			var selectedSubject = $('#selectSubject option:selected').val();
			if (selectedSubject != 'selectOne') {
				subject_id = selectedSubject;
			} else {
				alert("Please select the Subject");
			}
			//	}
			var data = $('#tblVideosAppendGrid').appendGrid('getAllValue');
			var videoJson;

			if (data.length > 0) {
				var jsonObjItems = [];
				for ( var i = 0; i < data.length; i++) {

					jsonObjItems.push({
						subject_id : subject_id,
						subject_category_name : data[i].VideoName,
						description : data[i].Description,
						active : data[i].Active

					});
				} // for i
			}
			videoJson = JSON.stringify(jsonObjItems,subject_id);
			createVideo(videoJson,subject_id);
			return false;
		});

		function createVideo(videoJson,subject_id) {
			//alert('subject_id=='+subject_id);
			var RequestData = new Object();
			var Video = new Object();
			Video.stringJson=videoJson;
			RequestData.request_data_type = 'video';
			RequestData.request_data_method = 'createVideo';
			RequestData.request_data = Video;
			$.ajax({
				url:endPointUrl+'/video/createVideo',
				type : "POST",
				dataType : "json",
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(RequestData),
				
				success : function(data) {
					/* $('#dispAlertWrapper').dispAlert('show', 'success', 'Application Creation Successful.', {
						scrollToTop : true
					}); */
					//$('#manageTabs-ul a:eq(1)').click();
					//alert(subject_id);
					$('#manageTabs-ul a:eq(1)').click();
					//getVideoDetails(subject_id);
					//$('#manageTabs-ul li a[href="##manageTabs-1"]').click();
					//$("#reloadSubjectTableBtn").click();
					//processMessage(data.responseStatus, data.responseMsg);
					/* var selectedSubject = $('#selectSubject option:selected').val();
					alert("after success --- " + selectedSubject);
					getVideoDetails(selectedSubject); */
				},
				error : function(data) {
					/* $('#dispAlertWrapper').dispAlert('show', 'error', 'Couldnt create Application.', {
						scrollToTop : true
					}); */

					//$("#reloadApplicationsTableBtn").click();
					$('#manageTabs-ul a:eq(1)').click();
					//processMessage(data.responseStatus, data.responseMsg);
				}
			});

		}

		function saveSubCategory(updateJson) {
			$.ajax({
				url : "ajax/saveSubCategory.action",
				type : "POST",
				dataType : "json",
				data : ({
					'updateJson' : updateJson,
				}),
				complete : function(data) {

				},
				success : function(data, textStatus, JqXHR) {
					/* $('#dispAlertWrapper').dispAlert('show', 'success', 'Application Creation Successful.', {
						scrollToTop : true
					}); */
					//$('#manageTabs-ul a:eq(1)').click();
					//$('#manageTabs-ul li a[href="##manageTabs-1"]').click();
					//$("#reloadSubjectTableBtn").click();
					//processMessage(data.responseStatus, data.responseMsg);
					/* var selectedSubject = $('#selectSubject option:selected').val();
					alert("after success --- " + selectedSubject);
					getVideoDetails(selectedSubject); */
				},
				error : function(data, textStatus, jqXHR) {
					/* $('#dispAlertWrapper').dispAlert('show', 'error', 'Couldnt create Application.', {
						scrollToTop : true
					}); */

					//$("#reloadApplicationsTableBtn").click();
					//$('#manageTabs-ul a:eq(1)').click();
					//processMessage(data.responseStatus, data.responseMsg);
				}
			});

		}

		//Edit Video Details Dialog
		$("#edit-video-dialog").dialog(
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
							var updateJson;
							var $evd = $("#edit-video-dialog");
							var video_id = $evd.data('videoId');
							var video_name = $('#edit-video-dialog .videoName')
									.val();
							//var video_link = $('#edit-video-dialog .videoLink').val();
							var description = $(
									'#edit-video-dialog .description').val();
							var isActive = $('#edit-video-dialog .active').is(
									":checked");
							var active;
							if (isActive == true) {
								active = "Y";
							} else {
								active = "N";
							}
							var inputData = {
								subject_category_id : video_id,
								subject_category_name : video_name,
								description : description,
								active : active
							};
							//	$('.dialogAlertWrapper', this).dispAlert('hide');
							updateJson = JSON.stringify(inputData);
							
							var RequestData = new Object();
							var Video = new Object();
							Video.stringJson=updateJson;
							RequestData.request_data_type = 'video';
							RequestData.request_data_method = 'updateVideoDetails';
							RequestData.request_data = Video;
							
						
							
							$.ajax({
								url:endPointUrl+'/video/updateVideoDetails',
								type : "POST",
								dataType : "json",
								contentType: "application/json; charset=utf-8",
								data: JSON.stringify(RequestData),
								
								success : function(data) {
									//$(this).dialog("close");
									$("#edit-video-dialog").dialog("close");
									$('#manageTabs-ul a:eq(1)').click();
									
									//processMessage(data.responseStatus, data.responseMsg);
									//$('#dispDBTabs li a[href="#dispDBTabs-2"]').click();
								},
								error : function(data) {
									$('#manageTabs-ul a:eq(1)').click();
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

	});//End of global function
</script>

<div class="ui-widget-content" style="padding: 12px;">
	<table>
		<tbody>
			<tr>
				<td><label for="selectSubject">Subject Name</label></td>
				<td><select id="selectSubject" style="width: 200px"></select></td>
			</tr>
		</tbody>
	</table>
	<br>
	<table id="tblVideosAppendGrid">
	</table>
	<br>
	<button id="btnSave" class="" type="button">Save</button>
</div>


<!-- <div>

	<img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" />
	<fieldset>
		<legend>Manage Videos Data</legend>
		<div style="">
			<div id="manageVideosTableWrapper">
				<table id="manageVideosTable" class="display">
					<thead>
						<tr>
							<th>Video Name</th>
							<th>Video Link</th>
							<th>Description</th>
							<th>Active</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</fieldset>
</div> -->

<div >

	<!-- <img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" /> -->
	<fieldset>
		<legend>Manage Categories Data</legend>
		<div style="">
			<div id="manageVideosTableWrapper">
				<table id="manageVideosTable" class="display">
					<thead>
						<tr>
							<th>Category Id</th>
							<th>Category Name</th>
							<th>Description</th>
							<th>Active</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</fieldset>
</div>

<!--  div id="sub_category">
	<fieldset>
		<legend>Sub Categories Data</legend>
		<div >
			<div id="manageSubVideosTableWrapper">
				<p><a id="new1" class="new" href="">Add new row</a></p>
				<table id="manageSubVideosTable" class="display">
					<thead>
						<tr>
							<th>SUBJECT_SUB_CATEGORY_ID</th>
							<th>SUBJECT_SUB_CATEGORY_NAME</th>
							<th>DESCRIPTION</th>
							<th>POPULAR_VIDEO_RATING</th>
							<th>EMAIL_ID MENTOR_NAME</th>
							<th>IMAGE</th>
							<th>EDIT</th>
							<th>DELETE</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</fieldset>
</div> -->

<div id="edit-video-dialog" title="Edit Video Details:" class="displayNone">
	<div class="dialogAlertWrapper" class="displayNone"></div>
	<div class="contentWrapper">
		<table cellpadding="15">
			<thead>
			</thead>
			<tbody>
				<tr>
					<td>Sub Category Name:<span class="required">*</span></td>
					<td><input class="videoName" type="text" value="" style="width: 250px" /></td>
				</tr>
				<!-- <tr>
					<td>Video Link:</td>
					<td><input class="videoLink" type="text" value="" style="width: 250px" /></td>
				</tr> -->
				<tr>
					<td>Description :</td>
					<td><input class="description" type="text" value="" style="width: 250px" /></td>
				</tr>
				<tr>
					<td>Active :</td>
					<td><input class="active" type="checkbox" value="" style="width: 250px" /></td>
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
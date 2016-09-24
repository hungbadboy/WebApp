function loadManageSubjects() {

	// Destroy the delta definiton table if it exists
	if ($.fn.DataTable.fnIsDataTable($('#manageSubjectsTable')[0])) {
		$('#manageSubjectsTable').dataTable().fnDestroy();
		$("#manageSubjectsTable tbody").html('');
		$('#manageSubjectsTable').css('width', '100%');
	}

	var RequestData = new Object();
	var Video = new Object();
	Video.vid = 1;
	RequestData.request_data_type = 'subjects';
	RequestData.request_data_method = 'listofSubjects';
	RequestData.request_data = Video;

	$.ajax({
		url : endPointUrl + '/subjects/listOfSubjects',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(RequestData),
		success : function(data) {
			if (data != null) {
				var rowHtml = '<tr>';
				data = data.request_data_result;

				for (var i = 0; i < data.length; i++) {
					rowHtml += '<td>' + data[i].subject_name + '</td>';
					rowHtml += '<td>' + data[i].description + '</td>';
					rowHtml += '<td>' + data[i].creation_date + '</td>';
					rowHtml += '<td>' + data[i].active + '</td>';
					rowHtml += '<td><button class="deleteBtn" id='
							+ data[i].subject_id + ' value='
							+ data[i].subject_id + '></button></td>';
					rowHtml += '</tr>';
				}
				$('#manageSubjectsTable tbody').append(rowHtml);
			}

			$('#manageSubjectsTable').dataTable(
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
						"fnRowCallback" : function(nRow, aData, iDisplayIndex,
								iDisplayIndexFull) {

							// Setup delete buttons
							$(".deleteBtn", nRow).button({
								icons : {
									secondary : "ui-icon-trash"
								}
							}).unbind('click').click(
									function(event) {
										//var subjectId = $(".deleteBtn").val();
										var subjectId = $(this).attr('id');
										//alert('subjectId '+subjectId);
										$('#delete-subject-dialog').dialog(
												"open");
										$('#delete-subject-dialog').data(
												'subjectId', subjectId);
										return false;
									});
						},
						"fnInitComplete" : function(oSettings, json) {
							//					$('#dispDBTabs-1 fieldset').show();
							this.fnAdjustColumnSizing();
							//					 $("#manageSubjectsTable td").shorten({
							//						"showChars" : 20,
							//						"moreText" : "More",
							//						"lessText" : "Less",
							//					});
						}

					});
		},

		error : function(res) {
			/*$('#dispAlertWrapper').disp
			Alert('show', 'error', null, {
						scrollToTop : true
					});*/
		}

	}); //end of $ajax
}

$('#reloadSubjectTableBtn').button({
	icons : {
		primary : 'ui-icon-arrowrefresh-1-w'
	},
	text : false
}).unbind('click').click(function(event) {
	$("#reloadSubjectTableBtn").click();
});

// Add New Subject Dialog Box

$('#createNewBtn').button({
	icons : {
		primary : 'ui-icon-plusthick'
	},
	text : false
}).unbind('click').click(function(event) {
	$("#add-new-subject-dialog").dialog("open");
});

$("#add-new-subject-dialog").dialog({
	autoOpen : false,
	height : 'auto',
	width : 'auto',
	modal : true,
	buttons : {
		"Save" : function() {

			// Validate user input
			/* if (isEmptyOrNull($('.appName', this).val()) || isEmptyOrNull($('.propelId', this).val()) || isEmptyOrNull($('.response', this).val())
					|| isEmptyOrNull($('.storage', this).val()) || isEmptyOrNull($('.runtimeLoad', this).val()) || isEmptyOrNull($('.devDB', this).val())
					|| isEmptyOrNull($('.appOwner', this).val())) {
				$('.dialogAlertWrapper', this).dispAlert('show', 'error', 'Please complete all required fields.', {
					scrollToTop : false
				});
				return false;
			} */
			var data = $('#tblSubjectsAppendGrid').appendGrid('getAllValue');
			var subjectJson;

			if (data.length > 0) {
				var jsonObjItems = [];
				for (var i = 0; i < data.length; i++) {

					jsonObjItems.push({
						subject_name : data[i].SubjectName,
						description : data[i].Description,
						creation_date : data[i].StartDate,
						active : data[i].Status

					});
				} // for i
			}
			subjectJson = JSON.stringify(jsonObjItems);

			createSubject(subjectJson);

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

function createSubject(subjectJson) {
	var RequestData = new Object();
	var Video = new Object();
	Video.stringJson = subjectJson;
	RequestData.request_data_type = 'subjects';
	RequestData.request_data_method = 'createSubject';
	RequestData.request_data = Video;

	$.ajax({
		url : endPointUrl + '/subjects/createSubject',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(RequestData),

		success : function(data) {
			$('#tblSubjectsAppendGrid').trigger("reloadGrid");
		},
		error : function(res) {
			/* $('#dispAlertWrapper').dispAlert('show', 'error', 'Couldnt create Application.', {
				scrollToTop : true
			}); */
			$("#add-new-subject-dialog").dialog("close");
			//$("#reloadApplicationsTableBtn").click();
			$('#manageTabs-ul a:eq(0)').click();
			//processMessage(data.responseStatus, data.responseMsg);
		}
	});

}

/* 
 $('#deleteBtn').button({
 icons : {
 primary : 'ui-icon-plusthick'
 },
 text : false
 }).unbind('click').click(function(event) {
 $("#delete-subject-dialog").dialog("open");
 }); */

$("#delete-subject-dialog").dialog({
	autoOpen : false,
	height : 'auto',
	width : 'auto',
	modal : true,
	buttons : {
		"Confirm" : function() {
			var dsd = $('#delete-subject-dialog');
			var subjectId = dsd.data('subjectId');

			//alert("Subject Id "+subjectId);
			//var data = $('#tblSubjectsAppendGrid').appendGrid('getAllValue');
			var subjectJson;
			var jsonObjItems = [];
			jsonObjItems.push({
				subject_id : subjectId

			});
			subjectJson = JSON.stringify(jsonObjItems);

			deleteSubject(subjectJson);

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

function deleteSubject(subjectJson) {

	var RequestData = new Object();
	var Video = new Object();
	Video.stringJson = subjectJson;
	RequestData.request_data_type = 'subjects';
	RequestData.request_data_method = 'deleteSubject';
	RequestData.request_data = Video;

	$.ajax({
		url : endPointUrl + '/subjects/deleteSubject',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(RequestData),

		success : function(data) {
			$("#delete-subject-dialog").dialog("close");
			$('#manageTabs-ul a:eq(0)').click();

		},
		error : function(data) {
			$("#delete-subject-dialog").dialog("close");
			$('#manageTabs-ul a:eq(0)').click();
		}
	});

}

//Initialize appendGrid
$('#tblSubjectsAppendGrid').appendGrid({
	caption : 'New Subject Information',
	initRows : 1,
	columns : [ {
		name : 'SubjectName',
		display : 'Subject Name',
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
			maxlength : 200
		},
		ctrlCss : {
			width : '300px'
		}
	}, {
		name : 'StartDate',
		display : 'Start Date',
		type : 'ui-datepicker',
		ctrlAttr : {
			maxlength : 20
		},
		ctrlCss : {
			width : '100px'
		}
	}, {
		name : 'Status',
		display : 'Status',
		type : 'checkbox',
		onClick : function(evt, rowIndex) {
		}
	}

	],
	initData : []
});

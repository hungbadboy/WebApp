<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript">
$(function() {
	// Populate College Application Table Details
	//$(populateCollegeApp);

function populateCollegeApp(){
	
	
	// Destroy the delta definiton table if it exists
	if ($.fn.DataTable.fnIsDataTable($('#manageCollegeAppTable')[0])) {
		$('#manageCollegeAppTable').dataTable().fnDestroy();
		$("#manageCollegeAppTable tbody").html('');
		$('#manageCollegeAppTable').css('width', '100%');
	}
	
	$.ajax({
		url : "ajax/listSubjectsData.action",
		type : "POST",
		dataType : "json",
		data : ({}),
		complete : function(data) {
//			showLoadingImg('#dispDBTabs-1', false);
		},

		success : function(data, textStatus, jqXHR) {
			if(data != null){
				var rowHtml = '<tr>';
				data = data.manageSubjectsDetails;
				
				for ( var i = 0; i < data.length; i++) {
					rowHtml += '<td>' + data[i].subject_name + '</td>';
					rowHtml += '<td>' + data[i].description + '</td>';
					rowHtml += '<td>' + data[i].creation_date + '</td>';
					rowHtml += '<td>' + data[i].active + '</td>';
					rowHtml += '<td><button class="deleteBtn" id='+data[i].subject_id+' value='+data[i].subject_id+'></button></td>';
					rowHtml += '</tr>';
					//rowHtml += '<input type=hidden name=subject_id'+data[i].subject_id+' id='+data[i].subject_id+' value'+data[i].subject_id;
				}
				$('#manageCollegeAppTable tbody').append(rowHtml);
			}
			

			$('#manageCollegeAppTable').dataTable({
				"sPaginationType": "full_numbers",
                "bJQueryUI": true,
				//"sDom" : "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
				"bRetrieve" : true,
				"bFilter" : true,
				"bProcessing" : true, // show loading icon during table sorting
				"sScrollX" : "100%", // allow horizontal scrolling
				"bScrollCollapse" : true,
				"iDisplayLength" : 10,
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				// Initialize buttons before drawing the row
				"fnRowCallback" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
					
				},
				"fnInitComplete" : function(oSettings, json) {
//					$('#dispDBTabs-1 fieldset').show();
					this.fnAdjustColumnSizing();
					 $("#manageCollegeAppTable td").shorten({
						"showChars" : 20,
						"moreText" : "More",
						"lessText" : "Less",
					}); 
				}

			});
		},

		error : function(data, textStatus, jqXHR) {
			/*$('#dispAlertWrapper').disp
	Alert('show', 'error', null, {
						scrollToTop : true
					});*/
				}

			}); //end of $ajax
		}

});
</script>


<div>
	<!-- <img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" /> -->
	<fieldset>
		<legend>Manage College Application Data</legend>
		<div style="">
			<div id="manageCollegeAppTableWrapper">
				<button id="Stats" class="statsBtn">Stats</button>
				<table id="manageCollegeAppTable" class="display">
					<thead>
						<tr>
							<th>User Name</th>
							<th>Essay Text</th>
							<th>Essay Status</th>
							<th>Downloaded By</th>
							<th>Replies</th>
							<th>Creation Date</th>
							<th>Remove</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</fieldset>
</div>

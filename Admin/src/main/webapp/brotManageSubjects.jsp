<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<script type="text/javascript"	src="js/subjects.js"></script>
<script type="text/javascript">
	$(function() {
		// Populate Manage Subjects Table Details
		$(loadManageSubjects);
	});
</script>
	
	<div>
<!-- <img src="ASONAdmin_files/loadingLg.gif" alt="Loading" class="loadingImg loadingImgCentered" /> -->
		<fieldset >
			<legend>Manage Subjects Data</legend>
			<div style="">
				<div id="manageSubjectsTableWrapper">
					<button id="createNewBtn" class="submitBtn">Add New Subject</button>
					<button id="reloadSubjectTableBtn" class="reloadSubjectTableBtn">Refresh</button>
					<table id="manageSubjectsTable" class="display">
						<thead>
							<tr>
								<th>Subject Name</th>
								<th>Description</th>
								<th>Start Date</th>
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
		
<div id="add-new-subject-dialog" title="Manage Subjects : Add New Subject" class="displayNone">
	<div class="ui-widget-content" style="padding: 12px;">
		<table id="tblSubjectsAppendGrid"></table>
	</div>
</div>

<div id="delete-subject-dialog"
	title="Manage Subjects : Delete" class="displayNone">
	<div class="ui-widget-content" style="padding: 12px;">
	<p>Do you want to delete?</p>
	</div>
</div>

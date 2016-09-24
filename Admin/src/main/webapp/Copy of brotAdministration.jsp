<%@page import="org.json.JSONObject"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>

<script type="text/javascript" src="js/jquery.multiselect.js"></script>
<script type="text/javascript" src="js/admin.js"></script>
</head>
<script >
$(function () {
	$('select[multiple]').multiselect({
        columns: 1,
        text: {
        	placeholder: 'Select options',
        	selectAll	: 'Select all'
        },
        selectAll	: true,
   	});
	
	$(loadAllMenuData(endPointUrl));
});
</script>

<div >
<h3 style="border-top: 2px solid #000;border-bottom: 2px solid #000; padding: 5px;">Manage Menu</h3>
</div>

<div>
	<span>
		<button id="createNewMenuBtn" class="addBtn" style="margin-bottom: 10px;" >Add Menu</button>
		User: 
		<select id="filter_Type" name="filter_Type">
			<option value="All">All</option>
			<option value="S">Student</option>
			<option value="M">Mentor</option>
			<option value="A">Admin</option>
		</select>
	</span>
	
	
</div>

<div>
	<table id="menuDataTable" class="display">
		<thead>
			<tr>
				<th>Name</th>
				<th>Users</th>
                <th>Image</th>
				<th>Action</th>
				<th>CreateBy</th>
				<th>Sort</th>
				<th>Active</th>
				<th>Option</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>

<div id="add-new-menu-dialog"
	title="Manage Menu : Add New Menu" class="displayNone">
	<div class="ui-widget-content col-md-12" style="padding: 12px;">
		<div class="clearfix">
			<label class="col-md-4 add_title">Menu name:</label>
			<div class="col-md-8 add_input"> <input class="form-control" id="menuName" type="text"></div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title">Menu Image:</label>
			<div class="col-md-8 add_input"> <input  class="form-control" id="menuImage" type="text"></div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title">Action:</label>
			<div class="col-md-8 add_input"><input class="form-control" id="menuAction" type="text"></div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title">Sort:</label>
			<div class="col-md-8 add_input"><input class="form-control" id="menuSort" type="text"></div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title">Assign to:</label>
			<div class="col-md-8 add_input">
				<select id="role" multiple class="form-control">
	             <option value="S">Student</option>
	             <option value="M">Mentor</option>
	             <option value="A">Admin</option>
	         	</select>	
			</div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title"></label>
			<div class="col-md-8 add_input"><input id="active" type="checkbox" checked> Active</div>
		</div>
	</div>
</div>

<div id="edit-menu-dialog"
	title="Manage Menu : Edit Menu" class="displayNone">
	<div class="ui-widget-content col-md-12" style="padding: 12px;">
		<div class="form-group clearfix">
            <label class="col-md-4 add_title">Menu name:</label>
			<div class="col-md-8 add_input"> <input  class="form-control" id="edit_menuName" type="text"></div>
		</div>
		<div class="form-group clearfix">
            <label class="col-md-4 add_title">Menu Image:</label>
			<div class="col-md-8 add_input"> <input  class="form-control" id="edit_menuImage" type="text"></div>
		</div>
        <div class="form-group clearfix">
            <label class="col-md-4 add_title">Description: </label>
            <div class="col-md-8 add_input"><textarea class="form-control" id="edit_description"/></div>
        </div>
		<!-- <div>
			<div class="col-md-4 add_title"> Action:</div> 
			<div class="col-md-8 add_input"><input id="edit_menuAction" type="text"></div>
		</div>
		<div>
			<div class="col-md-4 add_title"> Sort:</div> 
			<div class="col-md-8 add_input"><input id="edit_menuSort" type="text"></div>
		</div>-->
		<div>
			<div class="col-md-4 add_title">Assign to:</div> 
			<div class="col-md-8 add_input">
				<select id="edit_role" multiple class="form-control">
	             <option value="S">Student</option>
	             <option value="M">Mentor</option>
	             <option value="A">Admin</option>
	         	</select>	
			</div>
		</div>
		<div class="clearfix">
            <label class="col-md-4 add_title"></label>
			<div class="col-md-8 add_input"><input id="edit_active" type="checkbox" checked> Active</div>
		</div>
	</div>
</div>

<div id="delete-menu-dialog"
	title="Manage Menu : Delete" class="displayNone">
	<div class="ui-widget-content" style="padding: 12px;">
	<p>Do you want to delete?</p>
	</div>
</div>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
<script type="text/javascript" src="js/brotManagerUniversities.js"></script>
<script type="text/javascript">
</script>
</head>
<body id="univervisty-manage">
	<div>
		<h2>Manager University</h2>
	</div>
	<div style="padding:30px">
		<div class="col-md-4" style="margin-bottom:10px;">
			<button id="btnAddSchool" type="button" class="btn btn-info">
				<span class="glyphicon glyphicon-plus"></span> Add School
			</button>
		</div>
		<div id="box-add-school" title="Add New School">
			<div class="message">
				<span id="msgRegisterSchool"></span>
			</div>
			<div id="loading-div-background-admin">
				<div id="loading-div-admin" class="ui-corner-all">
					<img style="height: 32px; width: 32px; margin: 30px;"
						src="css/images/waiting.gif" alt="Loading.." /><br>PROCESSING.
					PLEASE WAIT...
				</div>
			</div>
			<form>
				<div class="clearfix">
					<div class="left-site">
						<div class="form-group clearfix">
							<label>Name:</label><input type="text" placeholder="Name" name="schoolName" tabindex="1">
						</div>
						<div class="form-group clearfix">
							<label>First Address:</label> <input type="text" placeholder="First Address" name="firstAddress" tabindex="2">
						</div>
						<div class="form-group clearfix">
							<label>Second Address:</label> <input type="text" placeholder="Second Address" name="secondAddress" tabindex="3">
						</div>
						<div class="form-group clearfix">
							<label>Type:</label> 
							<select id="type" name="typeSchool" tabindex="7">
							 	<option value="" disabled selected>School's type</option>
								<option value="U">UNIVERSITY</option>
								<option value="C">COLLEGE</option>
							</select>
						</div>
					</div>
					<div class="right-site">	
						<div class="form-group list-state clearfix">
							<label>State:</label> 
							<select id="state" name="state" tabindex="4">
								<option value="" disabled selected >State</option>
							</select>
						</div>	
						<div class="form-group list-city clearfix">
							<label>City:</label> 
							<input id="list-cities" type="text" placeholder="City ( Choose a State first )" name="city" tabindex="5" readonly>
						</div>						
						<div class="form-group clearfix" id="zipcode">
							<label>Zip code:</label> <input type="text" oninput="this.value=this.value.replace(/[^0-9]/g,'');"  placeholder="Zip code" name="zipcode" tabindex="6">
						</div>
					</div>
				</div>
			</form>
		</div>
		
		<div id="confirmDelete" title="Delete School ?"></div> 
		
		<div class="col-md-2">
			<select id="selectUniversity">
				<option value="All" selected>Select One</option>
				<option value="U">UNIVERSITY</option>
				<option value="C">COLLEGE</option>
			</select>
		</div>
	</div>
	<table id="universityMgr"></table>
	<div id="jqGridPager"></div>
</body>
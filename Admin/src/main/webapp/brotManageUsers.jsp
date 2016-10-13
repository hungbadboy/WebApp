<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
<script type="text/javascript" src="js/brotManagerUsers.js"></script>
</head>
<body>
	<div>
		<h2>Manager User</h2>
	</div>
	<div style="padding: 30px">
		<div class="col-md-4">
			<button id="btnAddMentor" type="button" class="btn btn-info">
				<span class="glyphicon glyphicon-plus"></span> Add Mentor
			</button>
			<button id="btnAddAdmin" type="button" class="btn btn-info">
				<span class="glyphicon glyphicons-user-asterisk"></span> Add Admin
			</button>
		</div>
		<div id="box-add-admin" title="Add New Admin">
			<div class="message">
				<span id="msgRegister"></span>
			</div>
			<form>
				<div class="clearfix">
					<div class="left-site">
						<div class="form-group clearfix">
							<label>Email:</label> <input type="email" placeholder="Email"
								name="email" tabindex="1">
						</div>

						<div class="form-group clearfix">
							<label>First Name:</label> <input type="text"
								placeholder="Firstname" name="firstname" tabindex="3">
						</div>
						<div class="form-group clearfix">
							<label>Password:</label> <input type="password"
								placeholder="Password" name="password" tabindex="5">
						</div>
					</div>
					<div class="right-site">
						<div class="form-group clearfix">
							<label>Birthday:</label> <input type="text"
								placeholder="Birthday" id="datepicker" name="birthday"
								tabindex="2">
						</div>
						<div class="form-group clearfix">
							<label>Last Name:</label> <input type="text"
								placeholder="Lastname" name="lastname" tabindex="4">
						</div>
						<div class="form-group clearfix">
							<label>Confirm Password:</label> <input type="password"
								placeholder="Confirm Password" name="confirmPwd" tabindex="6">
						</div>
					</div>
				</div>
				<div class="active">
					<input type="checkbox" value="active" id="chk" name="active"><label
						for="chk">Active</label>
				</div>
			</form>
		</div>

		<div id="box-add-mentor" title="Add New Mentor">
			<div class="message">
				<span id="msgRegisterMentor"></span>
			</div>
			<div class="container clearfix">
				<div class="defaultAvatar">
					<img src="css/images/noavartar.jpg" alt="default-avatar">
				</div>
				<div id="loading-div-background">
					<div id="loading-div" class="ui-corner-all">
						<img style="height: 32px; width: 32px; margin: 30px;"
							src="css/images/waiting.gif" alt="Loading.." /><br>PROCESSING.
						PLEASE WAIT...
					</div>
				</div>
				<div class="form-content-mentor">
					<form>
						<div class="control-group clearfix">
							<span class="label-form">Basic Information</span>
							<div class="left-site">
								<div class="form-group clearfix">
									<label>Email:</label> <input type="email" placeholder="Email"
										name="emailMentor" tabindex="1">
								</div>

								<div class="form-group clearfix">
									<label>First Name:</label> <input type="text"
										placeholder="First Name" name="firstnameMentor" tabindex="3">
								</div>
							</div>
							<div class="right-site">
								<div class="form-group clearfix">
									<label>Birthday:</label> <input type="text"
										placeholder="Birthday" id="datepickerMentor" name="birthday"
										tabindex="2">
								</div>
								<div class="form-group clearfix">
									<label>Last Name:</label> <input type="text"
										placeholder="Last Name" name="lastnameMentor" tabindex="4">
								</div>
							</div>
						</div>
						<div class="control-group control-large">
							<span class="label-form">Education Information</span>
							<div class="left-site">
								<div class="form-group clearfix">
									<label>School/University :</label> <input type="text"
										placeholder="School/Unisversity" name="school" tabindex="5">
								</div>
							</div>
							<div class="form-group list-check clearfix">
								<label>Want Help In:</label>
								<ul>
									<!-- <li><input type="checkbox" id="01" name=""><label
										for="01">Mathematics</label></li>
									<li><input type="checkbox" id="02" name=""><label
										for="02">Physics</label></li>
									<li><input type="checkbox" id="03" name=""><label
										for="03">Biology</label></li>
									<li><input type="checkbox" id="04" name=""><label
										for="04">Chemistry</label></li> -->
								</ul>
							</div>
							<div class="form-group clearfix">
								<label>Achievements :</label> <input type="text"
									placeholder="Achievements" name="achievement"
									class="input-large" tabindex="6">
							</div>
						</div>
						<div class="control-group">
							<span class="label-form">Other</span>
							<textarea id="bio" tabindex="7"></textarea>
						</div>
						<div class="active-mentor">
							<input type="checkbox" value="active" id="chkMentor"
								name="active" tabindex="8"><label for="chk">Active</label>
						</div>
					</form>
				</div>
			</div>
		</div>

		<div class="col-md-2">
			<select id="selectUser" style="width: 200px; margin-top: 10px">
				<option value="All" selected>Select One</option>
				<option value="S">STUDENT</option>
				<option value="M">MENTOR</option>
				<option value="A">ADMIN</option>
			</select>
		</div>
	</div>
	<table id="userMgr"></table>
	<div id="pager"></div>
</body>
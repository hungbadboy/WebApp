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
			 <button id="btnSearch" type="button" class="btn btn-info">
		      <span class="glyphicon glyphicon-plus"></span> Add Mentor
		    </button>
	         <button id="btnSearch" type="button" class="btn btn-info">
		      <span class="glyphicon glyphicons-user-asterisk"></span> Add Admin
		    </button>
	    </div>
	    <div class="col-md-2">
			<select id="selectUser" style="width: 200px">
				<option value="selectOne" >Select One</option>
				<option value="S" selected>STUDENT</option>
				<option value="M">MENTOR</option>
				<option value="A">ADMIN</option>
			</select>
		</div>
	</div>
	<table id="userMgr"></table>
	<div id="pager"></div>
</body>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
<script type="text/javascript" src="js/brotManagerUniversities.js"></script>
<script type="text/javascript">
	var userId = '<s:property value="#session.user_info.getUserid()"/>';
	var userName = '<s:property value="#session.user_info.getUsername()"/>';
	var type = '<s:property value="#session.user_info.getUserType()"/>';
	var firstName = '<s:property value="#session.user_info.getFirstname()"/>';
	var lastName = '<s:property value="#session.user_info.getLastname()"/>';
	var status = '<s:property value="#session.user_info.getActiveFlag()"/>';
	var email = '<s:property value="#session.user_info.getEmail()"/>';
	var bod = '<s:property value="#session.user_info.getBirthDay()"/>';
	var UserInfo = {
		userId : userId,
		userName : userName,
		firstName : firstName,
		lastName : lastName,
		status : status,
		email : email,
		type : type,
		bod : bod
	}
</script>
</head>
<body>
	<div>
		<h2>Manager University</h2>
	</div>
	<div style="padding: 30px">
		<div class="col-md-2">
			<select id="selectUser" style="width: 200px; margin-top: 10px">
				<option value="All" selected>Select One</option>
				<option value="U">UNIVERSITY</option>
				<option value="C">COLLEGE</option>

			</select>
		</div>
	</div>
	<table id="universityMgr"></table>
	<div id="jqGridPager"></div>
</body>
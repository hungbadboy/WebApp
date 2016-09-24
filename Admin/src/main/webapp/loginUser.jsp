<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Saatya | The Continuous Learning</title>

<script src="js/jquery/jquery-1.9.1.min.js"></script>
<script src="js/jquery/jquery-ui-1.10.3.custom.min.js"></script>
<link href="css/style1.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" />
<s:head />
<script type="text/javascript"	src="js/plugins/DataTables-1.9.4/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"	src="js/plugins/DataTables-1.9.4/dataTables.bootstrap.js"></script>
<link rel="stylesheet" type="text/css"	href="js/plugins/DataTables-1.9.4/css/dataTables.bootstrap.css" />
<link rel="stylesheet"	href="js/plugins/DataTables-1.9.4/css/jquery.dataTables.css" />
<script type="text/javascript"	src="js/plugins/appendGrid/jquery.appendGrid-1.3.1.js"></script>
<link rel="stylesheet" type="text/css"	href="js/plugins/appendGrid/jquery.appendGrid-1.3.1.css" />
<%--  <script src="js/jquery/jtable/jquery.jtable.js"></script> --%>
<!--  <link href="css/jtable.css" rel="stylesheet" type="text/css" /> -->
</head>
<body id="root">
	<div id="container-wrapper">
		<div class="header">
			<a href="#" title="Saatya Home" class="saatya-logo"></a>
			<div class="clr"></div>
		</div>
		<div id="login" >
			<%
			  String errorMessage= (String)request.getAttribute("ERROR");
			if(errorMessage != null && !errorMessage.isEmpty()){%>
				<p> <font color='red'>Error Message:<b>	<%=errorMessage %> </b></font>	</p>
			<%}%>
			<%
			  String successMessage= (String)request.getAttribute("SUCCESS");
			if(successMessage != null && !successMessage.isEmpty()){%>
				<p > <font color='green'><b>	<%=successMessage %> </b></font>	</p>
			<%}%>				
		  <s:form action="login" validate="true">
				<s:textfield name="username" label="Email Id: "/>
				<s:password name="password" label="Password: "/>
				<s:submit/>
		</s:form>
		</div>   
      <!-- Start of Footer -->
		<div class="footer">
			<div class="footer_resize">
			    <p class="lf"><s:a href="register.action"> Register  </s:a>&nbsp;&nbsp;&nbsp;&nbsp;</p>
				<p class="lf">&copy; Copyright <a href="#">MyWebSite</a>.</p>
				<p class="rf">Design by Tech <a href="#">Team</a></p>
				<div style="clear: both;"></div>
			</div>
		</div>
		<!-- End of Footer -->
	</div>
</body>
</html>
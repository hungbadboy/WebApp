<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Saatya | Create an User Account</title>

<link href="css/style1.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" />
<s:head />

<script>
	
</script>
</head>



<body id="root">
	
		<div id="container-wrapper">
			<div class="header">

				<a href="#" title="Saatya Home" class="saatya-logo"></a>


			</div>
			<s:form action="createNewUser.action" method="post" validate="false">
			
			<div id="register-new-user">
			<%
					  String errorMessage= (String)request.getAttribute("ERROR");
					if(errorMessage != null && !errorMessage.isEmpty()){%>
						<p > <font color='red'>Error:	<%=errorMessage %> </font>	</p>	
					<%}
					
					%>
				
				
		  <br>
				<table cellpadding="10" align="center">
						<tr>
							<td>firstName:</td> <td><s:textfield name="firstName"  size="30" /></td>
						</tr>
						<tr>
							<td>lastName:</td> <td><s:textfield name="lastName"  size="30"/></td>
						</tr>
						<tr>
							<td>email:</td> <td><s:textfield name="email"  size="50"/></td>
						</tr>
						
						<tr>
							<td>password: </td><td><s:password name="password"  size="30"/></td>
						</tr>
				
					<tr>
						
						<s:submit />

					</tr>
				</table>
			</div>
			<!-- Start of Footer -->
			<div class="footer">
				<div class="footer_resize">
					<p class="lf">
						&copy; Copyright <a href="#">MyWebSite</a>.
					</p>
					<p class="rf">
						Design by Tech <a href="#">Team</a>
					</p>
					<div style="clear: both;"></div>
				</div>
			</div>
			<!-- End of Footer -->
		</div>
	</s:form>
</body>



</html>





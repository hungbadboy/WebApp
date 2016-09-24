<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Join Chat</title>
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css">
<link rel="stylesheet" href="css/mentorChat.css">
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
  $(document).ready(function() {

  });
</script>
</head>
<body>
  <%
  	String roomid = request.getParameter("roomid");
  	String mentorID = request.getParameter("mentorID");
  	String regId = request.getParameter("regId");
  %>
  <div class="container">
    <div class="jumbotron">
      <h1>Siblinks Chat Requested By Student</h1>
      <p class="lead">A student has requested a chat session with you. Please join using either the application or browser links below.</p>
      <div class="row">
        <div class="col-xs-6">
          <a role="button" class="btn btn-lg btn-primary" href="siblinks://openchat?roomid=<%=roomid%>&mentorID=<%=mentorID%>&regId=<%=regId%>">Join Via Mobile App</a>
        </div>
        <div class="col-xs-6">
          <a role="button" class="btn btn-lg btn-primary" href="chat/index.html?roomid=<%=roomid%>&mentorID=<%=mentorID%>&regId=<%=regId%>">Join Via Browser</a>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
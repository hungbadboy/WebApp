<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
	<script type="text/javascript">
		var logedUserId = <s:property value="#session.user_info.userid"/>;
	</script>
	<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="js/brotManagerVideos.js"></script>
</head>
<body>
	<div>
		<h2>Manager Videos</h2>
	</div>
	
	<table id="videosMgr"></table>
	<div id="pager"></div>
</body>
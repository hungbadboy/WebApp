<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
<script type="text/javascript" src="js/jquery.multiselect.js"></script>
<script type="text/javascript" src="js/admin.js"></script>
</head>
<body>
	<div>
		<h2>Manager Menus</h2>
	</div>
	<div style="padding: 30px">
		<div>
		<label for="search_menu"> Search: </label>
		 <input id="search_menu" type="text" />
		 <button id="btnSearch" type="button" class="btn btn-info">
	      <span class="glyphicon glyphicon-search"></span> Search
	    </button>
		 </div>
		<table id="tree"></table>
		<div id="pager"></div>
	</div>
</body>

<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="com.opensymphony.xwork2.ActionContext"%>
<head>
<script type="text/javascript" src="js/jquery.multiselect.js"></script>
<script type="text/javascript" src="js/category.js"></script>
</head>
<body>
	<div>
		<h2>Manager Category</h2>
	</div>
	<div style="padding: 30px">
		<div>
		<label for="search_category"> Search: </label>
		 <input id="search_category" type="text" />
		 <button id="btnSearch" type="button" class="btn btn-info">
	      <span class="glyphicon glyphicon-search"></span> Search
	    </button>
		 </div>
		<table id="tree_category"></table>
		<div id="pager_category"></div>
	</div>
</body>

<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<!doctype html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Saatya | The Continuous Learning</title>
<!-- The jQuery library is a prerequisite for all jqSuite products -->

<!-- <link rel="stylesheet" type="text/css" href="css/bootstrap.css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css"  href="css/jquery-ui-1.10.3.custom.min.css"/>
<link rel="stylesheet" type="text/css"  href="js/plugins/DataTables-1.9.4/css/dataTables.bootstrap.css" />
<link rel="stylesheet" type="text/css"  href="js/plugins/DataTables-1.9.4/css/jquery.dataTables.css" />
<link rel="stylesheet" type="text/css"	href="js/plugins/appendGrid/jquery.appendGrid-1.3.1.css" /> -->
<!-- <link rel="stylesheet"  type="text/css"	href='css/bootstrap.min.css'/>-->
<link rel="stylesheet"  type="text/css"	href='css/bootstrap-theme.min.css'/>
<link rel="stylesheet" type="text/css"	href='css/sm-core-css.css'/>
<link rel="stylesheet" type="text/css"	href='css/sm-blue.css'/>
<link rel="stylesheet" type="text/css"  href="css/style1.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="css/trirand/ui.jqgrid.css"/>
<s:head />
<script type="text/ecmascript" src="js/jquery.min.js"></script>
<script type="text/ecmascript" src="js/bootstrap.min.js"></script>
<script type="text/ecmascript" src="js/trirand/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="js/trirand/i18n/grid.locale-en.js"></script>
<%-- <script type="text/javascript" src="js/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="js/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="js/handlebars/handlebars.js"></script>
<script type="text/javascript" src="js/plugins/DataTables-1.9.4/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/plugins/DataTables-1.9.4/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/plugins/appendGrid/jquery.appendGrid-1.3.1.js"></script>--%>
<script type="text/javascript" src="js/jquery/jquery-ui-1.10.3.custom.min.js"></script> 
<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="js/menu.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
	var $ = jQuery.noConflict();;
	var endPointUrl = '<s:property value="#session.dataServiceURL"/>';
	jQuery(function() {
		jQuery('#main-menu').smartmenus({
			subMenusSubOffsetX: 1,
			subMenusSubOffsetY: -8
		}).find('li:first > a').addClass('current');
		
		jQuery('.user-setting-wrapper span.current').click(function() {
        if(jQuery(this).hasClass('selected')) {
         jQuery(this).removeClass('selected');
         jQuery('.user-setting').addClass('hide');
       } else {
         jQuery(this).addClass('selected');
			jQuery('.user-setting').removeClass('hide');
		}
     	
     	});
	});
	</script>
</head>
<body id="root">
	<div id="container-wrapper">		
		<div><tiles:insertAttribute name="header" /></div>
		<div><tiles:insertAttribute name="menu" /></div>
		<div><tiles:insertAttribute name="body" /></div>
		<div id="footer">
			<!-- Start of Footer -->
			<div class="footer"><tiles:insertAttribute name="footer" /></div>
			<!-- End of Footer -->
		</div>
	</div>


</body>
</html>
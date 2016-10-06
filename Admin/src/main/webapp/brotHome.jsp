<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page import="java.util.*"%>

<style type="text/css">
.clear {
	display: inline-block;
}

.clear:after {
	display: block;
	visibility: hidden;
	clear: both;
	height: 0;
	content: ".";
}

#portfolio-list li {
	float: left;
	margin: 0 10px;
}

#portfolio-list li a,#portfolio-list li a:visited {
	display: block;
	border: 5px solid #4f8603;
	width: 120px;
	height: 120px;
}

#portfolio-list li a:hover,#portfolio-list li a:active {
	border-color: #315301;
}
</style>

<script>

    function displayDescription(srcimg, show) {
	
		if ( srcimg ) {
			
			var div_id = srcimg.title + '_description' ;
			var div_ele = document.getElementById(div_id) ;
			if(show){
				$('.tabDescription').stop(true, true);
				$('.tabDescription').hide();
				$(div_ele).slideDown('fast');
			}else{
				$('.tabDescription').stop(true, true);
				$(div_ele).slideUp('fast');
			}
		}
	}
    
	 
</script>

<br />
<br />
<br />

<%-- <s:form name="homeForm" action="appHome">
</s:form> --%>

<div align="center">
<h2>Manager Brother Hood</h2>
	<ul id="portfolio-list" class="clear">
		
		<li><a href="#"><img style='width:100px; height:100px' src="css/images/manage.png" onMouseOver="displayDescription(this, true);" onMouseOut="displayDescription(this, false);"
				alt="Brother Hood Manage" title="Borther Hood Manage" /></a>
		</li>

		<li><a href="#" ><img style='width:100px; height:100px' src="css/images/admin.jpg" onMouseOver="displayDescription(this, true);" onMouseOut="displayDescription(this, false);"
				alt="Brother Hood Administrator" title="Brother Hood Administrator" /> </a>
		</li>
	</ul>

</div>

<hr />

<div id="Brother Hood Manage_description" class="tabDescription" style="display: none;" align="center">
	<h3>Brother Hood Manage</h3>
	<p>Manage Subject | Manage Video | Manage User Information</p>
</div>

<div id="Brother-Hood" class="tabDescription" style="display: none;" align="center">
	<h3>Brother Hood Application Administrator</h3>
	<p>Admin can manage information.</p>
</div>



<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="header" class="header">
	<a href="#" title="Saatya Home" class="saatya-logo"></a>
	<div class="log_out user-setting-wrapper">
	   <span class="current">
	   	<s:property value="#session.user_info.firstname"/>
		<s:property value="#session.user_info.lastname"/>
	</span>
	    <ul class="user-setting hide">
	      <li>
	        <a class="logout" href="logout">Logout</a>
	      </li>
	    </ul>
	</div>
</div>
<div class="clr"></div>


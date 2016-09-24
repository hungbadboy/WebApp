<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div>
<nav id="main-nav" role="navigation">
  <!-- Sample menu definition -->
  <ul id="main-menu" class="sm sm-blue">
 <s:iterator  value="#session.listmenu" id="menu" status="master">
	<s:if test="%{#menu.parentid==null}">
		<s:if test="#menu.name == 'Home'">
			<li><a id='<s:property value="%{#master.index}"/>' href='<s:property value="#menu.action"/>'><span><i class="fa fa-home fa-2x"></i></span><s:property value="#menu.name" /></a>
		</s:if>
		<s:elseif test="#menu.name == 'Admin'">
			<li><a id='<s:property value="%{#master.index}"/>' href='<s:property value="#menu.action"/>'><span><i class="fa fa-user-secret"></i><s:property value="#menu.name" /></span></a>
		</s:elseif>
		<s:elseif test="#menu.name == 'Manager'">
			<li><a id='<s:property value="%{#master.index}"/>' href='<s:property value="#menu.action"/>'><span><i class="fa fa-user-secret"></i><s:property value="#menu.name" /></span></a>
		</s:elseif>
		<s:else>
			<li><a id='<s:property value="%{#master.index}"/>' href='<s:property value="#menu.action"/>'><s:property value="#menu.name" /></a>
		</s:else>
		<s:set id="counter" name="counter" value="0"/>
		<s:iterator  value="#session.listmenu" id="submenu" status="status">
			<s:if test="#menu.id==#submenu.parentid">
				<s:set id="counter" name="counter" value="%{#counter +1}"/>
				<s:if test="#counter == 1">
					<ul>
				</s:if>
					<li>
						<a id='<s:property value="%{#master.index+':'+	#status.index}"/>' href="<s:property value="#submenu.action"/>">
							<span><i class=""></i><s:property value="#submenu.name"/></span>
						</a>
				 	</li>
			</s:if>
		</s:iterator>
		 <s:if test="#counter>0">
			</ul>
		</s:if>
	  	</li>
	  </s:if>
</s:iterator>
  </ul>
</nav>
</div>
<div class="clr"></div>
<%--
  Created by IntelliJ IDEA.
  User: hieu
  Date: 9/9/15
  Time: 3:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>

<%
  String endPointUrl=(String)application.getAttribute("endPointUrl");
  System.out.println("endPointUrl=============jspjsp==--=============--------=" + endPointUrl);
%>  
<script type="text/javascript">
  var endPointUrl = '<%=endPointUrl%>';
</script>    

<script id="list-comment" type="text/x-handlebars-template">
    <thead>
    <tr>
        <th style="width: 3%; text-align: center;">cId</th>
        <th style="width: 20%; text-align: center;">Author</th>
        <th style="width: 45%; text-align: center;">Content</th>
        <th style="width: 7%; text-align: center;">Num Like</th>
        <th style="width: 15%; text-align: center;">Creation Date</th>
        <th style="width: 10%; text-align: center;">Action</th>
    </tr>
    </thead>
    <tbody>
    {{#each comments}}
      <tr>
        <td>{{cid}}</td>
        <td>{{author}}</td>
        <td>{{content}}</td>
        <td>{{{numLike}}}</td>
        <td>{{timestamp}}</td>
        <td>
          <button onclick="showDeleteForm(this)" class="deleteBtn ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" cId = {{cid}} title="Delete Comment">
            <span class="ui-button-icon-primary ui-icon ui-icon-trash"></span>
          </button>
         </td>
      </tr>
    {{/each}}
    </tbody>
</script>

<script type="text/javascript" src="js/comments.js"></script>

<div>
  <fieldset>
    <legend>Manage Comment</legend>
    <div style="">
      <div id="manageVideosTableWrapper">
        <!-- <button class="addBtn btnAdd" title="Add Article">Button</button> -->
        <table id="comment-items" class="display">
          
        </table>
      </div>
    </div>
  </fieldset>
</div>

<div id="delete-comment-dialog"
  title="Manage Comment : Delete" class="displayNone">
  <div class="ui-widget-content" style="padding: 20px;">
  <p>Do you want to delete?</p>
  </div>
</div>
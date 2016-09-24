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

<script id="list-mentor" type="text/x-handlebars-template">
    <thead>
    <tr>
        <th style="width: 5%; text-align: center;">id</th>
        <th style="width: 33%; text-align: center;">description</th>
        <th style="width: 8%; text-align: center;">Action</th>
    </tr>
    </thead>
    <tbody>
    {{#each mentors}}
      <tr>
        <td>{{id}}</td>
        <td>{{description}}</td>
        <td>
		      <button title="Edit Mentor" onclick="showEditForm(this)" class="editBtn ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" id = {{id}} txtIntroduceEdit = '{{introduce}}'>
            <span class="ui-button-icon-primary ui-icon ui-icon-pencil"></span>
          </button>
		      <button onclick="showDeleteForm(this)" class="deleteBtn ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" id = {{id}} title="Delete Mentor">
            <span class="ui-button-icon-primary ui-icon ui-icon-trash"></span>
          </button>
	       </td>
      </tr>
    {{/each}}
    </tbody>
</script>

<script type="text/javascript" src="js/mentors.js"></script>

<div>
  <fieldset>
    <legend>Manage Mentor Admission</legend>
    <div style="">
      <div id="manageVideosTableWrapper">
        <button class="addBtn btnAdd" title="Add Mentor">Button</button>
        <table id="mentor-items" class="display">
          
        </table>
      </div>
    </div>
  </fieldset>
</div>

<div id="delete-mentor-dialog"
  title="Manage Mentor : Delete" class="displayNone">
  <div class="ui-widget-content" style="padding: 20px;">
  <p>Do you want to delete?</p>
  </div>
</div>

<div id="add-mentor-dialog" title="About Mentor:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td colspan="2"> 
            <form id="upload-file-add-form" ng-hide="true">
              Image: <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Introduce:</td>
          <td>
            <textarea class="introduce" id="txtIntroduceAdd" value=""></textarea>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<div id="edit-mentor-dialog" title="About Mentor Details:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td colspan="2">Image: 
            <form id="upload-file-form" ng-hide="true">
              <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Introduce:</td>
          <td>
            <textarea class="introduceEdit" id="txtIntroduceEdit" value=""></textarea>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
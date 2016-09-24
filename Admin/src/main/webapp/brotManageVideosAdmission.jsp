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

<script id="list-topic" type="text/x-handlebars-template">
  <tbody>
    <tr>
      <td>
        <label>Topic Sub Admission Name: </label>
        <select id="selectSubAdmission" style="width: 200px">
          {{#each topics}}  
            <option value = {{id}}>{{name}}</option>
          {{/each}}
        </select>
      </td>
    </tr>
  </tbody>
</script>

<script id="list-video" type="text/x-handlebars-template">
  <thead>
  <tr>
    <th style="width: 3%; text-align: center;">vId</th>
    <th style="width: 28%; text-align: center;">Title</th>
    <th style="width: 30%; text-align: center;">Description</th>
    <th style="width: 25%; text-align: center;">Video Link</th>
    <th style="width: 4%; text-align: center;">Running Time</th>
    <th style="width: 10%; text-align: center;">Action</th>
  </tr>
  </thead>
  <tbody>
  {{#each videos}}
    <tr>
      <td>{{vId}}</td>
      <td>{{title}}</td>
      <td>{{{description}}}</td>
      <td>{{youtubeUrl}}</td>
      <td>{{runningTime}}</td>
      <td>
        <button class="editBtn ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" title="Edit Video" onclick="showEditForm(this)" vId = {{vId}} titleEdit = '{{title}}' descriptionEdit = '{{{description}}}' youtubeUrl = '{{youtubeUrl}}' runningTime = {{runningTime}}>
          <span class="ui-button-icon-primary ui-icon ui-icon-pencil"></span>
        </button>
        <button class="deleteBtn ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" onclick="showDeleteForm(this)" vId = {{vId}} title="Delete Video">
          <span class="ui-button-icon-primary ui-icon ui-icon-trash"></span>
        </button>
       </td>
    </tr>
  {{/each}}
  </tbody>
</script>

<script type="text/javascript" src="js/videosAdmission.js"></script>

<div>
  <fieldset>
    <legend>Manage Video Admission</legend>
    <div style="">
      <div id="manageVideosTableWrapper">
        <div class="ui-widget-content" style="padding: 12px;">
          <table id="topic-items">
            
          </table>
        </div>
        <button class="addBtn btnAdd" title="Add Video Admission">Button</button>
        <table id="video-items" class="display table table-condensed table-hover">
          
        </table>
      </div>
    </div>
  </fieldset>
</div>

<div id="delete-video-dialog"
  title="Manage Video : Delete" class="displayNone">
  <div class="ui-widget-content" style="padding: 20px;">
  <p>Do you want to delete?</p>
  </div>
</div>

<div id="add-video-dialog" title="Video:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td>Title:<span class="required">*</span></td>
          <td><input class="title" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td>Youtube Url:</td>
          <td><input class="youtubeUrl" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td>Running Time:</td>
          <td><input class="runningTime" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td colspan="2"> 
            <form id="upload-file-add-form" ng-hide="true">
              Image: <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Description:</td>
          <td id="ck-field">
            <textarea class="description" id="descriptionAdd" value=""></textarea>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<div id="edit-video-dialog" title="Video Details:" class="displayNone">
  <div class="dialogAlertWrapper" class="displayNone"></div>
  <div class="contentWrapper">
    <table cellpadding="15">
      <thead>
      </thead>
      <tbody>
        <tr>
          <td>Title:<span class="required">*</span></td>
          <td><input class="title" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td>Youtube Url:</td>
          <td><input class="youtubeUrl" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td>Running Time:</td>
          <td><input class="runningTime" type="text" value="" style="width: 590px" /></td>
        </tr>
        <tr>
          <td colspan="2">Image: 
            <form id="upload-file-form" ng-hide="true">
              <input id="upload-file-input" class="fileUpload" type="file" name="uploadfile" accept="*" />
            </form>
          </td>
        </tr>
        <tr>
          <td>Description:</td>
          <td>
            <textarea class="description" id="descriptionEdit" value=""></textarea>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
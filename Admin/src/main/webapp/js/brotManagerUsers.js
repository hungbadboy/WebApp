/**
 * @author Tavv
 */

var Role = {
	SuperAdmin : "U",
	Admin : "A",
	Mentor : "M",
	Student : "S"
};

var UserInfo;
var userMgr;
$(document).ready(function() {
	userMgr = $("#userMgr");
	init();
	var userType = type;
	UserInfo = UserInfo; 
	getSubjects(function(response){
		localStorage.setItem("subjects", JSON.stringify(response));
		var container = $(".form-group.list-check > ul");
		if(response){
			for(var i = 0;i<response.length;i++){
				container.append('<li><input type="checkbox" class="subjects" value="'+
						response[i].subjectId+'" name="'+
						response[i].subject+'>"<label for="'+
						response[i].subjectId+'">'+
						response[i].subject+'</label></li>');
			}
		}
	});
	userMgr.jqGrid({
		url : endPointUrl + 'user/getAllUsers',
		editurl : 'clientArray',
		mtype : "GET",
		datatype : "json",
		page : 1,
		colModel : [
		{name:'Actions',index:'Actions', width:75,sortable:false},
		{name: 'userid', key: true, hidden:true}, 
		{
			label : 'User Name',
			name : 'userName',
			editable : true,
			width : 150
		}, {
			label : 'User Type',
			name : 'userType',
			width : 75,
			align : 'center',
			editable : true,
			edittype : "select",
			editoptions : {
				value : "A:Admin;M:Mentor;S:Student"
			}
		}, {
			label : 'First Name',
			name : 'firstName',
			width : 150,
			editable : true,
			edittype : "text"
		}, {
			label : 'Last Name',
			name : 'lastName',
			width : 150,
			editable : true,
			edittype : "text",
		}, {
			label : 'Register Date',
			name : 'registrationTime',
			formatter:'date', 
			formatoptions: {srcformat: 'U', newformat:'d/m/Y'},
			width : 150,
			editable : false,
			edittype : "text",
		}, {
			label : 'Last online',
			name : 'lastOnline',
			width : 150,
			formatter:'date', 
			formatoptions: {srcformat: 'U', newformat:'d/m/Y'},
			editable : false,
			edittype : "text",
		}, {
			label : 'Status',
			name : 'enableFlag',
			width : 150,
			editable : true,
			edittype : "select",
			editoptions : {
				value : "Y:Active;N:Inactive"
			}
		}, {
			name : 'firstName',
			hidden:true,
		}, {
			name : 'lastName',
			hidden:true,
		}, {
			name : 'timeStamp',
			hidden:true,
		},{name: 'email', hidden:true}],
		loadonce : true,
		viewrecords : true,
		autowidth : true,
		search : true,
		height : 250,
		rowNum : 10,
		gridComplete: function(){
			var ids = userMgr.jqGrid('getDataIDs');
			for(var i= 0 ;i < ids.length;i++){
				var clId = ids[i];
				var role = userMgr.jqGrid('getCell',clId,'userType');
				var userName = userMgr.jqGrid('getCell',clId,'userName');
				var enable = userMgr.jqGrid('getCell',clId,'enableFlag');
				var hasEditPermission = (role == Role.Mentor || role == Role.Admin || role == Role.Student);
				var isEnable = enable == "Y";
				var icon = isEnable ? 'ui-icon ui-icon-locked' : 'ui-icon ui-icon-unlocked';
				var title = isEnable ? "Disable" : "Enable"; 
				if(userType == Role.Admin && (role == Role.Mentor ||  role == Role.Student)){
					edt = "<div title='Edit User' style='display:inline-block;cursor:pointer;' class='ui-pg-div ui-inline-edit' " +
							"onmouseover='hoverIconActions("+clId+", true)' onmouseout='unHoverIconActions("+clId+", true)'" +
							"onclick=\"getInfoUser("+clId+");\"><span class='ui-icon ui-icon-pencil'></span></div>";
					userMgr.jqGrid('setRowData',ids[i],{Actions:edt});
				}else if(userType == Role.SuperAdmin && hasEditPermission){
					edt = "<div title='Edit User' style='display:inline-block;cursor:pointer;' class='ui-pg-div ui-inline-edit' " +
					"onmouseover='hoverIconActions("+clId+", true)' onmouseout='unHoverIconActions("+clId+", true)'" +
					"onclick=\"getInfoUser("+clId+");\"><span class='ui-icon ui-icon-pencil'></span></div>";
					del = "<div title='"+title+"' style='display:inline-block;' class='ui-pg-div ui-inline-del' " +
							"onmouseover='hoverIconActions("+clId+", false)' onmouseout='unHoverIconActions("+clId+", false)'" +
							"onclick=\"confirmDelete("+clId+");\"><span class='"+icon+"'></span></div>"; 
					userMgr.jqGrid('setRowData',ids[i],{Actions:edt+del});
				}
			}	
		},
		pager : "#pager"
	});
	
	var valueSelected;
	$("#selectUser").change(function() {
		var value = $(this).val();
		valueSelected = value;
		checkShowHideActions(userType, valueSelected);
		if (valueSelected == "All") {
			userMgr.jqGrid('setGridParam', {
				search : false,
				postData : {
					"filters" : ""
				}
			}).trigger("reloadGrid");
		} else {
			checkShowHideActions(userType, valueSelected);
			var f = {
				groupOp : "AND",
				rules : []
			};
			f.rules.push({
				field : "userType",
				op : "cn",
				data : valueSelected
			});
			$.extend(userMgr[0].p.search = true, userMgr[0].p.postData, {
				filters : JSON.stringify(f)
			});
			userMgr.trigger("reloadGrid", [ {
				page : 1
			} ]);
		}
		return false;
	});
	
	$('#btnAddMentor').click(function() {
		showDialogFormUser("Add", null);
	});

	$('#btnAddAdmin').click(function() {
		showDialogFormAdmin("Add", null);
	});

	$('#btnEditMyProfile').click(function() {
		tabProfileDialog();
	});
	
	
	$('#tabs-profile').on('tabsbeforeactivate', function (event, ui) {
		
	    $("#msgChangePwd").text("");
	    $("#msgUpdateProfile").text("");
	});
	
	userMgr.navGrid("#pager", {
		edit : false,
		add : false,
		del : false,
		search : false,
		refresh : true,
		view : false,
		align : "left",
		
	});
});

/**
 * @param isEditAdmin true or false
 * @returns json object after validated form
 */
function validateFormAdmin(isEditAdmin) {
	var message = "";
	var email = isEditAdmin ? $("input[name=emailAdmin]").val() :  $("input[name=email]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = isEditAdmin ? $("input[name=firstnameAdmin]").val() : $("input[name=firstname]").val();
	var	dob = formatOutputDatePicker(isEditAdmin ? "#datepickerAdmin" : "#datepicker");
	var lastName = isEditAdmin ? $("input[name=lastnameAdmin]").val() : $("input[name=lastname]").val();
	var statusActive;
	if(isEditAdmin){
		if ($("#chkEditAdmin").prop("checked") == true) {
			statusActive = 'Y';
		} else {
			statusActive = 'N';
		}
	}else{
		if ($("#chk").prop("checked") == true) {
			statusActive = 'Y';
		} else {
			statusActive = 'N';
		}
	}
	if (firstName == "" || lastName == "") {
		message += "First name or Last name is not empty,"
	}
	if(!isEditAdmin){
		var password = $("input[name=password]").val();
		var confirmPwd = $("input[name=confirmPwd]").val();
		if (password == "" || confirmPwd == "") {
			message += "Password or Confirm password is not empty,"
		}
		if (password != confirmPwd) {
			message += "Password not match,";
		}
	}
	if (!message && !isEditAdmin) {
		var jsonRegister = {
			username : email,
			firstName : firstName,
			lastName : lastName,
			role : Role.Admin,
			password : password,
			dob : dob,
			active : statusActive
		};
		return jsonRegister;
	}else if (!message && isEditAdmin) {
		var jsonEdit = {
				username : email,
				firstName : firstName,
				lastName : lastName,
				role : Role.Admin,
				dob : dob,
				active : statusActive
		};
		return jsonEdit;
	}else {
		message = message.replace(/.$/,"");
		return {message : message};
	};
}

/**
 * @param isAddNew true or false
 * @param userId 
 * @returns json with type add new mentor or update mentor 
 * @exception return json with message error
 */
function validateFormRegisterMentor(isAddNew, userId) {
	var message = "";
	var email = $("input[name=emailMentor]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = $("input[name=firstnameMentor]").val();
	var dob = formatOutputDatePicker("#datepickerMentor");
	var lastName = $("input[name=lastnameMentor]").val();
	var school = $("input[name=school]").val();
	var accomplishments = $("input[name=achievement]").val();
	var bio = $("#bio").val();
	if(bio.length > 1024){
		message += "BIO is too long"
	}
	var statusActive;
	if ($("#chkMentor").prop("checked") == true) {
		statusActive = 'Y';
	} else {
		statusActive = 'N';
	}
	if (firstName == "" || lastName == "") {
		message += "First name or Last name is not empty,"
	}
	// Selected Subject
    var arrSubjectSelected = [];
    var subjectSelected = $('.subjects:checked');
    for (var i = 0; i < subjectSelected.length; i++) {
        arrSubjectSelected.push(subjectSelected[i].defaultValue);
    }
    var defaultSubjectId = arrSubjectSelected.join(',');
	
	if (!message && isAddNew) {
		var jsonRegister = {
			username : email,
			firstName : firstName,
			lastName : lastName,
			role : Role.Mentor,
			accomplishment : accomplishments,
			dob : dob,
			school : school,
			defaultSubjectId : defaultSubjectId,
			bio : bio,
			active : statusActive
		};
		return jsonRegister;
	}else if (!message && !isAddNew) {
		var jsonRegister = {
				userid : userId,
				email : email,
				firstName : firstName,
				lastName : lastName,
				role : Role.Mentor,
				accomplishment : accomplishments,
				dob : dob,
				school : school,
				defaultSubjectId : defaultSubjectId,
				bio : bio,
				active : statusActive
			};
			return jsonRegister;
	} else {
		return {
			message : message
		};
	}
}


/**
 * @param email is email want to validating
 * @returns email validated
 */
function IsEmail(email) {
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	return emailReg.test(email);
}

/**
 * @param tagOfDatePicker is tag of datepicker in html
 * @returns format date type dd M, yy
 */
function formatOutputDatePicker(tagOfDatePicker) {
	var date = $(tagOfDatePicker).datepicker("getDate");
	if (!date) {
		return null;
	}
	var result = $.datepicker.formatDate("dd M, yy", date);
	return result;
}

/**
 * @param jsonRegister
 */
function addNewAdminMentor(jsonRegister) {
	var RequestAdmin = new Object();
	RequestAdmin.request_data_type = 'user';
	RequestAdmin.request_data_method = 'registerAdmin';
	var role = jsonRegister.role;
	$.ajax({
		url : endPointUrl + 'user/registerAdminMentor',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonRegister),
		success : function(data) {
			if(role==Role.Mentor){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'yellowgreen');
				showDialogLoading(Role.Mentor, false);
				clearFormRegMentor();
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'yellowgreen');
				showDialogLoading(Role.Admin, false);
				clearFormRegAdmin();
			}
			disableButtonSubmitForm(false);
		},
		error : function(data) {
			if(role==Role.Mentor){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
				showDialogLoading(Role.Mentor, false);
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'red');
				showDialogLoading(Role.Admin, false);
			}
			disableButtonSubmitForm(false);
		}
	});
}

function clearFormRegAdmin(){
	var firstName = $("input[name=firstname]").val("");
	var dob = $("input[name=birthday]").val("");
	var lastName = $("input[name=lastname]").val("");
	var password = $("input[name=password]").val("");
	var confirmPwd = $("input[name=confirmPwd]").val("");
	var email = $("input[name=email]").val("");
	$("#chk").prop('checked', false);
}

function clearFormRegMentor(){
	$("input[name=emailMentor]").val("");
	$("input[name=firstnameMentor]").val("");
	$("#datepickerMentor").val("");
	$("input[name=lastnameMentor]").val("");
	$("input[name=school]").val("");
	$("input[name=achievement]").val("");
	$("#bio").val("");
	$("#chkMentor").prop('checked', false);
	// Selected Subject
    var arrSubjectSelected = [];
    var subjectSelected = $('.subjects');
    for (var i = 0; i < subjectSelected.length; i++) {
    	if(subjectSelected[i].checked == true){
    		subjectSelected[i].checked = false;
    	}
    }
    $(".defaultAvatar > img").attr("src","css/images/noavartar.jpg");
}

/**
 * @param callback
 * @returns list subjects with id and name 
 */
function getSubjects(callback){
	$.ajax({
		url : endPointUrl + 'categoryService/getSubjects',
		type : "GET",
		success : function(data){
			var subjects = data.request_data_result;
			callback(subjects);
		},
		error : function(data){
			console.log(data);
		}
	})
}

function init(){
	$("#datepicker").datepicker({
		changeMonth: true,
	    changeYear: true,
	    yearRange: '1900:' + new Date().getFullYear()
	});
	$("#datepickerMentor").datepicker({
		changeMonth: true,
	    changeYear: true,
	    yearRange: '1900:' + new Date().getFullYear()
	});
	$("#datepickerAdmin").datepicker({
		changeMonth: true,
	    changeYear: true,
	    yearRange: '1900:' + new Date().getFullYear()
	});
	$("#datepickerProfile").datepicker({
		changeMonth: true,
	    changeYear: true,
	    yearRange: '1900:' + new Date().getFullYear()
	});
	$('#box-add-mentor').hide();
	$('#box-add-admin').hide();
	$('#box-edit-admin').hide();
	$('#loading-div-background').css('display', 'none');
	$('#loading-div-background-admin').hide();
	$('#confirmDelete').hide();
	$('#dialog-profile-info').hide();
}

/**
 * @param type are "M" or "A"
 * @param enable true or false
 */
function showDialogLoading(type,enable){
	if(type==Role.Mentor){
		if(enable){
			$("#loading-div-background").css('display', 'block');
		}else{
			$("#loading-div-background").css('display', 'none');
		}
	}else{
		if(enable){
			$("#loading-div-background-admin").show();
		}else{
			$("#loading-div-background-admin").hide();
		}
	}
}


/**
 * @param isDisable true or false
 */
function disableButtonSubmitForm(isDisable){
	if(isDisable){
		$(":button:contains('Ok')").prop("disabled", true).addClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", true).addClass("ui-state-disabled");
	}else{
		$(":button:contains('Ok')").prop("disabled", false).removeClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", false).removeClass("ui-state-disabled");
	}
}

/**
 * @param data is request_data_result callback from server
 */
function setValueFormMentor(data){
	$("input[name=emailMentor]").val(data.email);
	$("input[name=firstnameMentor]").val(data.firstname);
	$("#datepickerMentor").val(formatDate(data.birthDay) == 0 ? "" : formatDate(data.birthDay));
	$("input[name=lastnameMentor]").val(data.lastName);
	$("input[name=school]").val(data.school);
	$("input[name=achievement]").val(data.accomplishments);
	$("#bio").val(data.bio);
	if(data.imageUrl != null){
		var image = data.imageUrl;
		if(image.startsWith("assets")){
			$(".defaultAvatar > img").attr("src","http://localhost:9000/"+data.imageUrl);	
		}else{
			$(".defaultAvatar > img").attr("src",data.imageUrl);
		}
	}
	if(data.enableFlag == "N"){
		$("#chkMentor").prop('checked', false);
	}else{
		$("#chkMentor").prop('checked', true);
	}
	
	// Selected Subject
	var defaultSubject; 
	if(data.defaultSubjectId != null && data.defaultSubjectId != ""){
		defaultSubject = data.defaultSubjectId.split(",");
		for (var i = 0; i < defaultSubject.length; i++) {
			$('.subjects[value='+defaultSubject[i]+']').prop('checked', true);
		}
	}
}

/**
 * @param id of mentor when edit row 
 */
function getInfoUser(id){
	var myGrid = $('#userMgr'),
    userId = myGrid.jqGrid ('getCell', id, 'userid');
	role = myGrid.jqGrid ('getCell', userId, 'userType');
	if(role == Role.Admin){
		showDialogEditAdmin(userId);
	}else{
		$.ajax({
			url : endPointUrl + 'user/getUserProfile?userid='+userId,
			type : "GET",
			datatype : "json",
			success : function(data) {
				setValueFormMentor(data.request_data_result);
				showDialogFormUser("Edit", userId);
			},
			error : function(data){
				
			}
		});
	}
}

/**
 * @param json is Object send to server
 * @param userId is id of user
 */
function updateProfileUser(json, userId){
	if (json.hasOwnProperty("message")) {
		$("#msgRegisterMentor").text("Plz, Enter the correct form !!!").css('color', 'red');
	} else {
		showDialogLoading(Role.Mentor,true);
		disableButtonSubmitForm(true);
		var RequestAdmin = new Object();
		RequestAdmin.request_data_type = 'user';
		RequestAdmin.request_data_method = 'adminUpdateProfileUser';
		$.ajax({
			url : endPointUrl + 'user/adminUpdateProfileUser',
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(json),
			success : function(data) {
				if(role==Role.Mentor || role == Role.Student){
					if(data.status == "true"){
						$("#msgRegisterMentor").text(data.request_data_result).css('color', 'yellowgreen');	
						//$.jgrid.gridUnload('#userMgr');
					}else{
						$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
					}
					showDialogLoading(Role.Mentor, false);
				}
				userMgr.jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
				disableButtonSubmitForm(false);
			},
			error : function(data) {
				if(role==Role.Mentor || role == Role.Student){
					$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
					showDialogLoading(Role.Mentor, false);
				}
				disableButtonSubmitForm(false);
			}
		});
	}
}

/**
 * @param json is object to update
 * @param userId
 */
//function updateInfoAdmin(json, userId){
//	if (json.hasOwnProperty("message")) {
//		$("#msgRegister").text("Plz, Enter the correct form !!!").css('color', 'red');
//	} else {
//		showDialogLoading(Role.Admin,true);
//		disableButtonSubmitForm(true);
//		var RequestAdmin = new Object();
//		RequestAdmin.request_data_type = 'user';
//		RequestAdmin.request_data_method = 'updateInfoAdmin';
//		$.ajax({
//			url : endPointUrl + 'user/updateInfoAdmin',
//			type : "POST",
//			dataType : "json",
//			contentType : "application/json; charset=utf-8",
//			data : JSON.stringify(json),
//			success : function(data) {
//				if(role==Role.Admin){
//					$("#msgRegister").text(data.request_data_result).css('color', 'yellowgreen');
//					showDialogLoading(Role.Admin, false);
//				}
//				disableButtonSubmitForm(false);
//			},
//			error : function(data) {
//				if(role==Role.Admin){
//					$("#msgRegister").text(data.request_data_result).css('color', 'red');
//					showDialogLoading(Role.Admin, false);
//				}
//				disableButtonSubmitForm(false);
//			}
//		});
//	}
//}

/**
 * @param type are String "Add" or "Edit"
 * @param id of user
 */
function showDialogFormUser(type, id){
	$(".ui-dialog-title").text(type == "Add" ? "Add New Mentor" : "Edit Mentor");
	$("#emailMentor").text(type == "Add" ? "UserID :" : "Email");
	var isUpdateProfile = id != null ? true : false;
	$('#box-add-mentor').dialog({
		dialogClass: 'no-close',
		display : 'block',
		width : 1200,
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Ok" : function() {
				var json;
				if(isUpdateProfile){
					json = validateFormRegisterMentor(false, id);
				}else{
					json = validateFormRegisterMentor(true, null);
				}
				if (json.hasOwnProperty("message")) {
					$("#msgRegisterMentor").text("Plz, Enter the correct form !!!").css('color', 'red');
				} else {
					showDialogLoading(Role.Mentor,true);
					disableButtonSubmitForm(true);
					if(isUpdateProfile){
						updateProfileUser(json, id);
					}else{
						addNewAdminMentor(json);
					}
				}
			},
			"Cancel" : function() {
				$("#msgRegisterMentor").text("");
				clearFormRegMentor();
				$(this).dialog("close");
			}

		},
	});
}

/**
 * @param type is "Add" Or "Edit"
 * @param id
 */
function showDialogFormAdmin(type, id){
	$(".ui-dialog-title").text(type == "Add" ? "Add New Admin" : "Edit Admin");
	$("#emailAdmin").text(type == "Add" ? "UserID :" : "Email");
	$('#box-add-admin').dialog({
		dialogClass: 'no-close',
		display : 'block',
		width : 700,
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Ok" : function() {
				var json = validateFormAdmin(false);
				if (json.hasOwnProperty("message")) {
					$("#msgRegister").text("Plz, Enter the correct form !!!").css('color', 'red');
				} else {
					showDialogLoading(Role.Admin,true);
					disableButtonSubmitForm(true);
					addNewAdminMentor(json);
				}
			},
			"Cancel" : function() {
				$("#msgRegister").text("");
				$(this).dialog("close");
			}

		},
	});
}


/**
 * @param timeStamp is UNIX_TIMESTAMP get from server
 * @returns {String} String date format type mm/dd/yyyy
 */
function formatDate(timeStamp){
	if(timeStamp == null || isEmpty(timeStamp)){
		return 0;
	}
	var fullDate = new Date(timeStamp*1000);
	var month = fullDate.getMonth() +1;
		month = month < 10 ? ('0'+month) : month;
	var date = fullDate.getDate();
		date = date < 10 ? ('0'+date) : date;
	var hours = fullDate.getHours();
		hours = hours < 10 ? ('0'+hours) : hours;
	var minutes = fullDate.getMinutes();
		minutes = minutes < 10 ? ('0'+minutes) : minutes;
	var seconds = fullDate.getSeconds();
		seconds = seconds < 10 ? ('0'+seconds) : seconds;
	return month+'/'+date+'/'+fullDate.getFullYear();
//	return fullDate.getFullYear()+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;
}

/**
 * Show Dialog No Data 
 */
function showDialogNoData(){
	$(".ui-dialog-title").text("Edit Mentor");
	$('#box-add-mentor').dialog({
		dialogClass: 'no-close',
		display : 'block',
		width : 1200,
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Ok" : function() {
			},
			"Cancel" : function() {
			}

		},
	});
	showDialogLoading(Role.Mentor,true);
	disableButtonSubmitForm(true);
}

/**
 * Check Empty String
 */
function isEmpty(str) {
    return typeof str == 'string' && !str.trim() || typeof str == 'undefined' || str === null;

}

/**
 * @param userType
 * @param filterSelected
 */
function checkShowHideActions(userType, filterSelected){
	if(userType == Role.Admin && filterSelected == Role.Admin){
		userMgr.hideCol("Actions");
	}else{
		userMgr.showCol("Actions");
	}
}

/**
 * @param userId
 * @param isEdit
 */
function hoverIconActions(userId, isEdit){
	if(isEdit){
		$("#"+userId+".jqgrow.ui-row-ltr td .ui-inline-edit").addClass('ui-state-hover');
	}else{
		$("#"+userId+".jqgrow.ui-row-ltr td .ui-inline-del").addClass('ui-state-hover');
	}
}

/**
 * @param userId
 * @param isEdit
 */
function unHoverIconActions(userId, isEdit){
	if(isEdit){
		$("#"+userId+".jqgrow.ui-row-ltr td .ui-inline-edit").removeClass('ui-state-hover');
	}else{
		$("#"+userId+".jqgrow.ui-row-ltr td .ui-inline-del").removeClass('ui-state-hover');
	}
}

/**
 * @param userId 
 */
function confirmDelete(userId) {
	var userName = userMgr.jqGrid('getCell',userId,'userName');
	var enableFlag = userMgr.jqGrid('getCell',userId,'enableFlag');
	var status  = enableFlag == "Y" ? "disable" : "enable";
	var isEnable = enableFlag == "Y";
	$('#confirmDelete').html("Are you sure you want to "+status+" user "+ userName + "'");
	$('#confirmDelete').dialog({
		dialogClass: 'no-close',
		display : 'block',
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Yes": function () {
				changeStatusUser(isEnable ? "N":"Y", userId);
			},
			"No": function () {
				$(this).dialog("close");
			}
		},
	});
}

function changeStatusUser(status, userId){
	var json = {
			userid : userId,
			active : status
	}
	disableButtonSubmitForm(true);
	$.ajax({
		url : endPointUrl + 'user/setStatusUser',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(json),
		success : function(data){
			if(data.status == "true"){
				userMgr.jqGrid("setCell", userId, "enableFlag", data.request_data_result);
				userMgr.trigger("reloadGrid");
			}
			disableButtonSubmitForm(false);
			$('#confirmDelete').dialog("close");
		},
		error : function(data){
			alert(data.request_data_result);
			$('#confirmDelete').dialog("close");
		}
	});
}

function showDialogEditAdmin(userId){
	setDataFormEditAdmin(userId);
	$("#box-edit-admin").dialog({
		dialogClass: 'no-close',
		display : 'block',
		width :650,
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Ok" : function() {
				var json = validateFormAdmin(true);
				if(json.hasOwnProperty("message")){
					$("#msgEditAdmin").text(json.message).css('color', 'red');
				}else{
					$("#msgEditAdmin").text("");
					json.userid = userId;
					updateAdminInfor(json);
				}
			},
			"Cancel" : function() {
				$("#msgEditAdmin").text("");
				$(this).dialog('close');
			}

		},
	});
}

function setDataFormEditAdmin(userId){
	var email = userMgr.jqGrid('getCell',userId,'email');
	var firstName = userMgr.jqGrid('getCell',userId,'firstName');
	var lastName = userMgr.jqGrid('getCell',userId,'lastName');
	var dob = formatDate(userMgr.jqGrid('getCell',userId,'timeStamp'));
	var enableFlag = userMgr.jqGrid('getCell',userId,'enableFlag');
	$("input[name=emailAdmin]").val(email);
	$("input[name=firstnameAdmin]").val(firstName);
	if(dob == 0){
		$("#datepickerAdmin").val("");
	}else{
		$("#datepickerAdmin").val(dob);
	}
	$("input[name=lastnameAdmin]").val(lastName);
	if(enableFlag == "N"){
		$("#chkEditAdmin").prop('checked', false);
	}else{
		$("#chkEditAdmin").prop('checked', true);
	}
}

function updateAdminInfor(json){
	disableButtonSubmitForm(true);
	var isUpdateProfile = json.hasOwnProperty("userid");
	$.ajax({
		url : endPointUrl + 'user/updateInfoAdmin',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(json),
		success : function(data){
			if(data.status == "true"){
				$(isUpdateProfile ? "#msgUpdateProfile" : "#msgEditAdmin").text(data.request_data_result).css('color', 'yellowgreen');
			}else{
				$(isUpdateProfile ? "#msgUpdateProfile" : "#msgEditAdmin").text(json.request_data_result).css('color', 'red');
			}
			if(isUpdateProfile){
				setUserInfoAfterUpdate(json);
			}
			disableButtonSubmitForm(false);
		},
		error : function(data){
			$(isUpdateProfile ? "#msgUpdateProfile" : "#msgEditAdmin").text(json.request_data_result).css('color', 'red');
		}
	});
}


function changePassword(json){
	disableButtonSubmitForm(true);
	$.ajax({
		url : endPointUrl + 'user/changePassword',
		type : "POST",
		contentType : "application/json; charset=utf-8",
		data: JSON.stringify({
            "request_data_type": "user",
            "request_data_method": "changePassword",
            "request_user": json
        }),
		success : function(data){
			if(data.status == "true"){
				$("#msgChangePwd").text(data.request_data_result).css('color', 'yellowgreen');
			}else{
				$("#msgChangePwd").text(data.request_data_result).css('color', 'red');
			}
			disableButtonSubmitForm(false);
			clearDataChangePwd();
		},
		error : function(data){
			$("#msgChangePwd").text(data.request_data_result).css('color', 'red');
		}
	});
}

function reload(pageIndex){
	   var page = userMgr.getGridParam("page");    //Add this
	   userMgr.setGridParam({
		   
	       datatype : 'json',
	       page : pageIndex            //Replace the '1' here
	    }).trigger("reloadGrid",[{page:pageIndex}]);    
}

function tabProfileDialog(){
	setDataFormUpdateProfile();
	$('#dialog-profile-info').dialog({
	    draggable: false,
	    resizable: false,
	    modal: true,
	    width: 650,
	    
	    //position: ['center', 35],
	    create: function() {
	        $('#tabs-profile').tabs({
	            create: function(e, ui) {
	            }            
	        });
	       // remove the title of the dialog as we want to use the tab's one 
	       $(this).parent().children('.ui-dialog-titlebar').remove();
	    },
	    buttons:{
	     Ok:function () {
	    	 showDialogLoading("A", true);
	    	 var index = getSelectedTabIndex();
	    	 if(index == 0){
	    		 var json = validateUpdateProfile();
	    		 if (json.hasOwnProperty("message")) {
	    				$("#msgUpdateProfile").text(json.message).css('color', 'red');
	    		 }else{ 
		    		 json.userid = UserInfo.userId;
		    		 updateAdminInfor(json);
	    		 }
	    	 }else{
	    		 var jsonPwd = validateChangePwd();
	    		 if(jsonPwd.hasOwnProperty("message")){
	    			 $("#msgChangePwd").text(jsonPwd.message).css('color', 'red');
	    		 }else{
	    			 changePassword(jsonPwd);
	    		 }
	    	 }
	     },
	     Cancel:function () {
	    	 
	    	 $(this).dialog("close");
	     }
	    }    
	});
}

function setDataFormUpdateProfile(){
	$("input[name=emailProfile]").val(UserInfo.email);
	$("input[name=firstnameProfile]").val(UserInfo.firstName);
	var dob = formatDate(UserInfo.dob);
	if(dob == 0){
		$("#datepickerProfile").val("");
	}else{
		$("#datepickerProfile").val(dob);
	}
	$("input[name=lastnameProfile]").val(UserInfo.lastName);
	if(UserInfo.status == "N"){
		$("#chkProfile").prop('checked', false);
	}else{
		$("#chkProfile").prop('checked', true);
	}
}

function getSelectedTabIndex() { 
    return $("#tabs-profile").tabs('option', 'active');
}

function validateChangePwd(){
	var message;
	var oldPwd = $("input[name=oldPwd]").val();
	if(isEmpty(oldPwd)){
		message = "Old password is not empty,"
	}
	var password = $("input[name=newPwdProfile]").val();
	var confirmPwd = $("input[name=confirmPwdProfile]").val();
	if (password == "" || confirmPwd == "") {
		message += "Password or Confirm password is not empty,"
	}
	if (password != confirmPwd) {
		message += "Password not match,";
	}
	if (!message) {
		var jsonChangePwd = {
	        'username': UserInfo.userName,
	        'password': oldPwd,
	        'newpassword': password
		};
		return jsonChangePwd;
	}else{
		message = message.replace(/.$/,"");
		return {
			message : message
		};
	}
}

function validateUpdateProfile(){
	var message = "";
	var email = $("input[name=emailProfile]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = $("input[name=firstnameProfile]").val();
	var lastName = $("input[name=lastnameProfile]").val();
	var dob = formatOutputDatePicker("#datepickerProfile");
	var statusActive;
	if ($("#chkProfile").prop("checked") == true) {
			statusActive = 'Y';
	} else {
			statusActive = 'N';
	}
	if (firstName == "" || lastName == "") {
		message += "First name or Last name is not empty,";
	}
	if(!message){
		var jsonProfile = {
				username : email,
				firstName : firstName,
				lastName : lastName,
				role : Role.Admin,
				dob : dob,
				active : statusActive
		};
		return jsonProfile;
	}else{
		message = message.replace(/.$/,"");
		return {message : message};
	}
}

function setUserInfoAfterUpdate(json){
	UserInfo.userId = json.userid;
	UserInfo.firstName = json.firstName;
	UserInfo.lastName = json.lastName;
	UserInfo.status = json.active;
	UserInfo.email = json.username;
	UserInfo.dob = json.dob;
}

function clearDataChangePwd(){
	$("input[name=oldPwd]").val("");
	$("input[name=newPwdProfile]").val("");
	$("input[name=confirmPwdProfile]").val("");
}


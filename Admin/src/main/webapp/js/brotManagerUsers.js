/**
 * @author Tavv
 */
$(document).ready(function() {
	var userMgr = $("#userMgr");
	init();
	var userType = type;
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
	$("#userMgr").jqGrid({
		url : endPointUrl + 'user/getAllUsers',
		editurl : 'clientArray',
		mtype : "GET",
		datatype : "json",
		page : 1,
		colModel : [
		{name:'act',index:'act', width:75,sortable:false},
		{name: 'userid', key: true, hidden:true 
		}, {
			label : 'User Name',
			name : 'userName',
			editable : true,
			width : 75
		}, {
			label : 'User Type',
			name : 'userType',
			width : 150,
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
			width : 150,
			editable : false,
			edittype : "text",
		}, {
			label : 'Last online',
			name : 'lastOnline',
			width : 150,
			editable : false,
			edittype : "text",
		}, {
			label : 'Status',
			name : 'enableFlag',
			width : 150,
			editable : true,
			edittype : "select",
			editoptions : {
				value : "A:Active;I:Inactive"
			}
		} ],
		loadonce : true,
		viewrecords : true,
		autowidth : true,
		search : true,
		height : 250,
		rowNum : 10,
		gridComplete: function(){
			var ids = $("#userMgr").jqGrid('getDataIDs');
			for(var i= 0 ;i < ids.length;i++){
				var clId = ids[i];
				var role = userMgr.jqGrid('getCell',clId,'userType');
				if(userType == "A" && role == "M"){
					edt = "<input style='height:22px;width:20px;display:block;margin:auto' type='button' value='E' onclick=\"getInfoMentor("+clId+");\"  />";
					$("#userMgr").jqGrid('setRowData',ids[i],{act:edt});
				}else if(userType == "U"){
					edt = "<input style='height:22px;width:20px;' type='button' value='E' onclick=\"getInfoMentor("+clId+");\"  />";
					del = "<input style='height:22px;width:20px;' type='button' value='D' onclick=\"jQuery('#userMgr').saveRow('"+clId+"');\"  />"; 
					$("#userMgr").jqGrid('setRowData',ids[i],{act:edt+del});
				}
			}	
		},
		pager : "#pager"
	});
	
	var valueSelected;
	$("#selectUser").change(function() {
		var value = $(this).val();
		valueSelected = value;
		if (valueSelected == "All") {
			userMgr.jqGrid('setGridParam', {
				search : false,
				postData : {
					"filters" : ""
				}
			}).trigger("reloadGrid");
		} else {
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
		showDialogFormMentor("Add", null);
	});

	$('#btnAddAdmin').click(function() {
		showDialogFormAdmin("Add", null);
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
//	,{// options for the Edit Dialog
//    	beforeShowForm : function (formid) {
//    		var myGrid = $('#userMgr'),
//    	    selRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
//    	    userId = myGrid.jqGrid ('getCell', selRowId, 'userid');
//    		role = myGrid.jqGrid ('getCell', selRowId, 'userType');
//    		if(role == "M"){
//    			$.ajax({
//    				url : endPointUrl + 'user/getUserProfile?userid='+userId,
//    				type : "GET",
//    				datatype : "json",
//    				success : function(data) {
//    					setValueFormMentor(data.request_data_result);
//    					showDialogFormMentor("Edit");
//    				},
//    				error : function(data){
//    					
//    				}
//    			});
//    		}
//    	},
//    	closeAfterEdit: true 
//    });
});

/**
 * @param isEditAdmin true or false
 * @returns json object after validated form
 */
function validateFormAdmin(isEditAdmin) {
	var message = "";
	var email = $("input[name=email]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = $("input[name=firstname]").val();
	var bod = formatOutputDatePicker("#datepicker");
	var lastName = $("input[name=lastname]").val();
	var oldPwd = isEditAdmin ? $("input[name=oldPwd]").val() : null;
	if(isEmpty(oldPwd)){
		message += "Old password is not empty,"
	}
	var password = !isEditAdmin ? $("input[name=password]").val() : $("input[name=newPwd]").val();
	var confirmPwd = $("input[name=confirmPwd]").val();
	var statusActive;
	if ($("#chk").prop("checked") == true) {
		statusActive = 'Y';
	} else {
		statusActive = 'N';
	}
	if (firstName == "" || lastName == "") {
		message += "First name or Last name is not empty,"
	}
	if (password == "" || confirmPwd == "") {
		message += "Password or Confirm password is not empty,"
	}
	if (password != confirmPwd) {
		message += "Password not match,";
	}
	if (!message && !isEditAdmin) {
		var jsonRegister = {
			username : email,
			firstName : firstName,
			lastName : lastName,
			role : "A",
			password : password,
			bod : bod,
			active : statusActive
		};
		return jsonRegister;
	}else if (!message && isEditAdmin) {
		var jsonEdit = {
				username : email,
				firstName : firstName,
				lastName : lastName,
				role : "A",
				oldPwd : oldPwd,
				password : password,
				bod : bod,
				active : statusActive
		};
		return jsonEdit;
	}else {
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
	var bod = formatOutputDatePicker("#datepickerMentor");
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
			role : "M",
			accomplishment : accomplishments,
			bod : bod,
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
				role : "M",
				accomplishment : accomplishments,
				bod : bod,
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
 * @returns format date tyep dd M, yy
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
			if(role=="M"){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'yellowgreen');
				showDialogLoading("M", false);
				clearFormRegMentor();
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'yellowgreen');
				showDialogLoading("A", false);
				clearFormRegAdmin();
			}
			disableButtonSubmitForm(false);
		},
		error : function(data) {
			if(role=="M"){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
				showDialogLoading("M", false);
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'red');
				showDialogLoading("A", false);
			}
			disableButtonSubmitForm(false);
		}
	});
}

function clearFormRegAdmin(){
	var firstName = $("input[name=firstname]").val("");
	var bod = $("input[name=birthday]").val("");
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
	$('#box-add-mentor').hide();
	$('#box-add-admin').hide();
	$('#box-edit-admin').hide();
	$('#loading-div-background').css('display', 'none');
	$('#loading-div-background-admin').hide();
}

/**
 * @param type are "M" or "A"
 * @param enable true or false
 */
function showDialogLoading(type,enable){
	if(type=="M"){
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
	$("#datepickerMentor").val(formatDate(data.birthDay));
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
function getInfoMentor(id){
	var myGrid = $('#userMgr'),
    userId = myGrid.jqGrid ('getCell', id, 'userid');
	role = myGrid.jqGrid ('getCell', userId, 'userType');
	if(role == "M"){
		$.ajax({
			url : endPointUrl + 'user/getUserProfile?userid='+userId,
			type : "GET",
			datatype : "json",
			success : function(data) {
				setValueFormMentor(data.request_data_result);
				showDialogFormMentor("Edit", userId);
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
function updateProfileMentor(json, userId){
	if (json.hasOwnProperty("message")) {
		$("#msgRegisterMentor").text("Plz, Enter the correct form !!!").css('color', 'red');
	} else {
		showDialogLoading("M",true);
		disableButtonSubmitForm(true);
		var RequestAdmin = new Object();
		RequestAdmin.request_data_type = 'user';
		RequestAdmin.request_data_method = 'adminUpdateProfileMentor';
		$.ajax({
			url : endPointUrl + 'user/adminUpdateProfileMentor',
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(json),
			success : function(data) {
				if(role=="M"){
					$("#msgRegisterMentor").text(data.request_data_result).css('color', 'yellowgreen');
					showDialogLoading("M", false);
				}
				disableButtonSubmitForm(false);
			},
			error : function(data) {
				if(role=="M"){
					$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
					showDialogLoading("M", false);
				}
				disableButtonSubmitForm(false);
			}
		});
	}
}

function updateInfoAdmin(json, userId){
	if (json.hasOwnProperty("message")) {
		$("#msgRegister").text("Plz, Enter the correct form !!!").css('color', 'red');
	} else {
		showDialogLoading("A",true);
		disableButtonSubmitForm(true);
		var RequestAdmin = new Object();
		RequestAdmin.request_data_type = 'user';
		RequestAdmin.request_data_method = 'updateInfoAdmin';
		$.ajax({
			url : endPointUrl + 'user/updateInfoAdmin',
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(json),
			success : function(data) {
				if(role=="A"){
					$("#msgRegister").text(data.request_data_result).css('color', 'yellowgreen');
					showDialogLoading("A", false);
				}
				disableButtonSubmitForm(false);
			},
			error : function(data) {
				if(role=="A"){
					$("#msgRegister").text(data.request_data_result).css('color', 'red');
					showDialogLoading("A", false);
				}
				disableButtonSubmitForm(false);
			}
		});
	}
}

/**
 * @param type are String "Add" or "Edit"
 * @param id of user
 */
function showDialogFormMentor(type, id){
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
					showDialogLoading("M",true);
					disableButtonSubmitForm(true);
					if(isUpdateProfile){
						updateProfileMentor(json, id);
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
					showDialogLoading("A",true);
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
	showDialogLoading("M",true);
	disableButtonSubmitForm(true);
}

/**
 * Check Empty String
 */
function isEmpty(str) {
    return typeof str == 'string' && !str.trim() || typeof str == 'undefined' || str === null;

}
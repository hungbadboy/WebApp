$(document).ready(function() {
	var userMgr = $("#userMgr");
	init();
	getSubjects(function(response){
		localStorage.setItem("subjects", JSON.stringify(response));
		var container = $(".form-group.list-check > ul");
		if(response){
			for(var i = 0;i<response.length;i++){
				container.append('<li><input type="checkbox" class="subjects" value="'+
						response[i].subjectId+'" name="'+
						response[i].subject+'"<label for="'+
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
		colModel : [ {
			label : 'User Name',
			name : 'userName',
			key : true,
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
			name : 'status',
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
		$('#box-add-mentor').dialog({
			dialogClass: 'no-close',
			display : 'block',
			width : 1200,
			modal : true,
			resizable : false,
			draggable : false,
			buttons : {
				"Ok" : function() {
					var json = validateFormRegisterMentor();
					if (json.hasOwnProperty("message")) {
						$("#msgRegisterMentor").text("Plz, Enter the correct form !!!").css('color', 'red');
					} else {
						showDialog("M",true);
						disableButton(true);
						addNewAdminMentor(json);
					}
				},
				"Cancel" : function() {
					$("#msgRegisterMentor").text("");
					$(this).dialog("close");
					
				}

			},
		});
	});

	$('#btnAddAdmin').click(function() {
		$('#box-add-admin').dialog({
			dialogClass: 'no-close',
			display : 'block',
			width : 700,
			modal : true,
			resizable : false,
			draggable : false,
			buttons : {
				"Ok" : function() {
					var json = validateFormRegisterAdmin();
					if (json.hasOwnProperty("message")) {
						$("#msgRegister").text("Plz, Enter the correct form !!!").css('color', 'red');
					} else {
						showDialog("A",true);
						disableButton(true);
						addNewAdminMentor(json);
					}
				},
				"Cancel" : function() {
					$("#msgRegister").text("");
					$(this).dialog("close");
				}

			},
		});
	});

	userMgr.navGrid("#pager", {
		edit : true,
		add : false,
		del : true,
		search : false,
		refresh : true,
		view : false,
		align : "left"
	}, {
		closeAfterEdit : true
	});
});

function validateFormRegisterAdmin() {
	var message = "";
	var email = $("input[name=email]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = $("input[name=firstname]").val();
	var bod = formatDate("#datepicker");
	var lastName = $("input[name=lastname]").val();
	var password = $("input[name=password]").val();
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
	if (!message) {
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
	} else {
		return {
			message : message
		};
	}
}

function validateFormRegisterMentor() {
	var message = "";
	var email = $("input[name=emailMentor]").val();
	if (!IsEmail(email)) {
		message = "Email not valid !,";
	}
	var firstName = $("input[name=firstnameMentor]").val();
	var bod = formatDate("#datepickerMentor");
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
	
	if (!message) {
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
	} else {
		return {
			message : message
		};
	}
}

function IsEmail(email) {
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	return emailReg.test(email);
}

function formatDate(typeDatePicker) {
	var date = $(typeDatePicker).datepicker("getDate");
	if (!date) {
		return null;
	}
	var result = $.datepicker.formatDate("dd M, yy", date);
	return result;
}

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
			if(role="M"){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'yellowgreen');
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'yellowgreen');
			}
			clearFormRegMentor();
			showDialog(false);
			disableButton(false);
		},
		error : function(data) {
			if(role="M"){
				$("#msgRegisterMentor").text(data.request_data_result).css('color', 'red');
			}else{
				$("#msgRegister").text(data.request_data_result).css('color', 'red');
			}
			showDialog(false);
			disableButton(false);
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
	$("#chk").prop("checked") == false;
}

function clearFormRegMentor(){
	$("input[name=emailMentor]").val("");
	$("input[name=firstnameMentor]").val("");
	$("#datepickerMentor").val("");
	$("input[name=lastnameMentor]").val("");
	$("input[name=school]").val("");
	$("input[name=achievement]").val("");
	$("#bio").val("");
	$("#chkMentor").prop("checked") == false;
	// Selected Subject
    var arrSubjectSelected = [];
    var subjectSelected = $('.subjects');
    for (var i = 0; i < subjectSelected.length; i++) {
    	if(subjectSelected[i].checked == true){
    		subjectSelected[i].checked = false;
    	}
    }
}


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
	$('#loading-div-background').css('display', 'none');
	$('#loading-div-background-admin').hide();
}

function showDialog(type,enable){
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



function disableButton(isDisable){
	if(isDisable){
		$(":button:contains('Ok')").prop("disabled", true).addClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", true).addClass("ui-state-disabled");
	}else{
		$(":button:contains('Ok')").prop("disabled", false).removeClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", false).removeClass("ui-state-disabled");
	}
}


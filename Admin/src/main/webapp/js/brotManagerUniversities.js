$(document).ready(function() {
	$('#box-add-school').hide();
	var universityMgr = $("#universityMgr");	
	getStates(function(response){
		localStorage.setItem("states", JSON.stringify(response));
		var container = $(".form-group.list-state > select");
		if(response){
			for(var i=0;i<response.length;i++){
				container.append('<option value="'+response[i].id+'">'+response[i].name+'</option>');
			}
		}
	});
	$(".list-state select").change(function(){
        $(".list-city input").attr("readonly", false);
        $(".list-city input").removeAttr('placeholder');
        getCities(function(response){
    		var Cities = [];
    		localStorage.setItem("cities", JSON.stringify(response));
    		if(response){
    			for(var i=0;i<response.length;i++){
    			Cities.push(response[i].name);
    			}
    		}	
    		$("#list-cities").autocomplete({
    	      source: Cities
    	    });
    	});
    });
	
	$("#type").change(function(){
		$("#type").css("color","black");
	})
	
	$("#state").change(function(){
		$("#state").css("color","black");
	})	
	
	
	$("#universityMgr").jqGrid({
		url : endPointUrl + 'university/getAllUniversities',
		editurl : 'clientArray',
		mtype : "GET",
		datatype : "json",
		page : 1,
		colModel : [
		{name:'Actions',index:'Actions', width:75,sortable:false},
		{name: 'sch_colle_degree_id', key: true, hidden:true}, 
		{
			label : 'Name',
			name : 'name',
			editable : true,
			width : 250
		}, {
			label : 'Type',
			name : 'type',
			width : 75,
			align : 'center',
			editable : true,
			edittype : "select",
			editoptions : {
				value : "U:Mentor;C:STUDENT"}
		}, {
			label : 'First Address',
			name : 'address1',
			width : 200,
			editable : true,
			edittype : "text"
		}, {
			label : 'Second Address',
			name : 'address2',
			width : 200,
			editable : true,
			edittype : "text"
		}, {
			label : 'City',
			name : 'city',
			width : 150,
			editable : true,
			edittype : "text"
		}, {
			label : 'State',
			name : 'state',
			width : 150,
			editable : true,
			edittype : "select"
		}, {
			label : 'Zipcode',
			name : 'zip',
			width : 100,
			editable : true,
			edittype : "text"
		} ],
		loadonce : true,
		viewrecords : true,
		autowidth : true,
		search : true,
		height : 250,
		rowNum : 10,
		gridComplete: function(){
			var ids = $("#universityMgr").jqGrid('getDataIDs');
			for(var i= 0 ;i < ids.length;i++){
				var clId = ids[i];
				edt = "<div title='Edit User' style='display:inline-block;cursor:pointer;' class='ui-pg-div ui-inline-edit' " +
				"onmouseover='hoverIconActions("+clId+", true)' onmouseout='unHoverIconActions("+clId+", true)'" +
				"onclick=\"getInfoSchool("+clId+");\"><span class='ui-icon ui-icon-pencil'></span></div>";
				del = "<div title='Delete School' style='display:inline-block;' class='ui-pg-div ui-inline-del' " +
				"onmouseover='hoverIconActions("+clId+", false)' onmouseout='unHoverIconActions("+clId+", false)'" +
				"onclick=\"confirmDelete("+clId+");\"><span class='ui-icon ui-icon-trash'></span></div>";
				$("#universityMgr").jqGrid('setRowData',ids[i],{Actions:edt+del});
			}	
		},
		pager : "#jqGridPager"
	});
	$("#universityMgr").showCol("Actions");
	
	var valueSelected;
	$("#selectUniversity").change(function() {
		var value = $(this).val();
		valueSelected = value;
		if (valueSelected == "All") {
			universityMgr.jqGrid('setGridParam', {
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
				field : "type",
				op : "cn",
				data : valueSelected
			});
			$.extend(universityMgr[0].p.search = true, universityMgr[0].p.postData, {
				filters : JSON.stringify(f)
			});
			universityMgr.trigger("reloadGrid", [ {
				page : 1
			} ]);
		}
		return false;
	});
	$('#btnAddSchool').click(function() {
		showDialogFormAddSchool("Add", null);
	});
});

function validateFormRegisterSchool(isAddNew, schoolId) {
	var message = "";
	var name = $("input[name=schoolName]").val();
	var type = $("select[name=typeSchool]").val();
	var address1 = $("input[name=firstAddress]").val();
	var address2 = $("input[name=secondAddress]").val();
	var city = $("input[name=city]").val();
	var state = $("select[name=state]").val();
	var zip = $("input[name=zipcode]").val();
	
	if (!message && isAddNew) {
		var jsonRegister = {
			name : name,
			type : type,
			address1 : address1,
			address2 : address2,
			city : city,
			state : state,
			zip : zip
		};
		return jsonRegister;
	}else if (!message && !isAddNew) {
		var objectUniversity = {};
		objectUniversity.schoolId =schoolId;
		objectUniversity.name =name;
		objectUniversity.type =type;
		objectUniversity.address1 =address1;
		objectUniversity.address2=address2;
		objectUniversity.city =city;
		objectUniversity.state =state;
		objectUniversity.zip =zip;
		return objectUniversity;
	} else {
		return {
			message : message
		};
	}
}

function addNewSchool(jsonRegister) {
	$.ajax({
		url : endPointUrl + 'university/registerSchool',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(jsonRegister),
		success : function(data) {
			if(data.status == "true"){
				$("#msgRegisterSchool").text("Successfully registered").css({"color": "yellowgreen", "font-size": "24px", "padding-left": "250px"});
				showDialogLoading(false);
				clearFormRegSchool();
				disableButtonSubmitForm(false);
			}else{
				$("#msgRegisterSchool").text("Please Enter the correct form !!!").css({"color": "red", "font-size": "24px", "padding-left": "250px"});
			}
		},
		error : function(data) {
			showDialogLoading(false);
			disableButtonSubmitForm(false);
		}
	});
}

function clearFormRegSchool(){
	$("input[name=schoolName]").val("");
	$("select[name=typeSchool]").val("");
	$("input[name=firstAddress]").val("");
	$("input[name=secondAddress]").val("");
	$("input[name=city]").val("");
	$("select[name=state]").val("");
	$("input[name=zipcode]").val("");

 $(".defaultAvatar > img").attr("src","css/images/noavartar.jpg");
}

function getStates(callback){
	$.ajax({
		url : endPointUrl + 'university/getStates',
		type : "GET",
		success : function(data){
			var states = data.request_data_result;
			callback(states);
		},
		error : function(data){
			console.log(data);
		}
	})
}

function getCities(callback){
    var state_id = $("select[name=state]").val();
	$.ajax({
		url : endPointUrl + 'university/getCities?state_id='+state_id,
		type : "GET",
		success : function(data){
			var cities = data.request_data_result;
			callback(cities);
		},
		error : function(data){
			console.log(data);
		}
	})
}

function showDialogLoading(enable){
	if(enable){
		$("#loading-div-background").css('display', 'block');
	}else{
		$("#loading-div-background").css('display', 'none');
	}
}

function disableButtonSubmitForm(isDisable){
	if(isDisable){
		$(":button:contains('Ok')").prop("disabled", true).addClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", true).addClass("ui-state-disabled");
	}else{
		$(":button:contains('Ok')").prop("disabled", false).removeClass("ui-state-disabled");
		$(":button:contains('Cancel')").prop("disabled", false).removeClass("ui-state-disabled");
	}
}

function setValueFormSchool(data){
	$("input[name=schoolName]").val(data.name);
	$("select[name=typeSchool]").val(data.type);
	$("input[name=firstAddress]").val(data.address1);
	$("input[name=secondAddress]").val(data.address2);
	$("input[name=city]").val(data.city);
	$("select[name=state]").val(data.state);
	$("input[name=zipcode]").val(data.zip);
}

function getInfoSchool(id){
	var myGrid = $('#universityMgr'),
    schoolId = myGrid.jqGrid ('getCell', id, 'sch_colle_degree_id');
	$.ajax({
		url : endPointUrl + 'university/getSchoolInfo?schoolid='+schoolId,
		type : "GET",
		datatype : "json",
		success : function(data) {
			setValueFormSchool(data.request_data_result);
			showDialogFormAddSchool("Edit", schoolId);
		},
		error : function(data){		
		}
	});
}

function updateSchoolInfo(json,schoolId){
	$.ajax({
		url : endPointUrl + 'university/updateSchoolInfo',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(json),
		success : function(data) {
			if(data.status == "true"){
				$("#msgRegisterSchool").text(data.request_data_result).css({"color": "yellowgreen", "font-size": "24px", "padding-left": "250px"});	
				showDialogLoading(true);
				clearFormRegSchool();
			}else{
				$("#msgRegisterSchool").text(data.request_data_result).css({"color": "red", "font-size": "24px", "padding-left": "250px"});
			}
			$("#universityMgr").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');	
			disableButtonSubmitForm(false);	
		},
		error : function(data) {
			$("#msgRegisterSchool").text("Plz check again !!!").css({"color": "red", "font-size": "24px", "padding-left": "250px"});
			showDialogLoading(false);
			disableButtonSubmitForm(false);
		}
	});
}

function showDialogFormAddSchool(type,id){
	var isUpdateProfile = id != null ? true : false;
	$('#box-add-school').dialog({
		dialogClass: 'no-close',
		display : 'block',
		width : 800,
		modal : true,
		resizable : false,
		draggable : false,
		buttons: {
			"OK" : function() {
				var json;
				if(isUpdateProfile){
					json = validateFormRegisterSchool(false, id);
				}else{
					json = validateFormRegisterSchool(true, null);
				}
				if (json.hasOwnProperty("message")) {
					$("#msgRegisterSchool").text("Plz, Enter the correct form !!!").css('color', 'red');
				} else {
					showDialogLoading(true);
					disableButtonSubmitForm(true);
					if(isUpdateProfile){
						updateSchoolInfo(json, id);
					}else{
						addNewSchool(json);
					}
				}
			},
			"Cancel" : function() {
				$("#msgRegisterSchool").text("");
				clearFormRegSchool();
				$(this).dialog("close");
			}
		}
	});
}

function hoverIconActions(schoolId, isEdit){
	if(isEdit){
		$("#"+schoolId+".jqgrow.ui-row-ltr td .ui-inline-edit").addClass('ui-state-hover');
	}else{
		$("#"+schoolId+".jqgrow.ui-row-ltr td .ui-inline-del").addClass('ui-state-hover');
	}
}

function unHoverIconActions(schoolId, isEdit){
	if(isEdit){
		$("#"+schoolId+".jqgrow.ui-row-ltr td .ui-inline-edit").removeClass('ui-state-hover');
	}else{
		$("#"+schoolId+".jqgrow.ui-row-ltr td .ui-inline-del").removeClass('ui-state-hover');
	}
}

function confirmDelete(schoolId){
	$('#confirmDelete').html("Are you sure you want to delete this school ?");
	$('#confirmDelete').dialog({
		dialogClass: 'no-close',
		display : 'block',
		width : 400,
		modal : true,
		resizable : false,
		draggable : false,
		buttons : {
			"Yes": function () {
				deleteSChool(schoolId);
			},
			"No": function () {
				$(this).dialog("close");
			}
		},
	});
}

function deleteSChool(schoolId){
	var json = {
			schoolId : schoolId
	}
	$.ajax({
		url : endPointUrl + 'university/deleteSChool',
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(json),
		success : function(data){
			$('#confirmDelete').dialog("close");
			$("#universityMgr").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');	
		},
		error : function(data){
			alert(data.request_data_result);
			$('#confirmDelete').dialog("close");
		}
	});
}


$(document).ready(function () {
	var userMgr = $("#userMgr");
	$("#userMgr").jqGrid({
	    url: endPointUrl + 'user/getAllUsers',
	    editurl: 'clientArray',
	    mtype: "GET",
	    datatype: "json",
	    page: 1,
	    colModel: [
	        {label: 'User Name', name: 'userName', key: true, width: 75 },
	        {label: 'User Type', name: 'userType', width: 150, align:'center',
	            editable: true,
	            edittype:"select",
	            editoptions: {
	                 value: "A:Admin;M:Mentor;S:Student"
	             }
	         },
	        {label: 'First Name', name: 'firstName', width: 150,
	            editable: true,
	            edittype: "text"
	        },
	        {label: 'Last Name', name: 'lastName', width: 150,
	            editable: true,
	            edittype: "text",
	        },
	        {label: 'Register Date', name: 'registrationTime', width: 150,
	            editable: false,
	            edittype: "text",
	        },
	        {label: 'Last online', name: 'lastOnline', width: 150,
	            editable: false,
	            edittype: "text",
	        },
	        {label: 'Status', name: 'status', width: 150,
	            editable: true,
	            edittype:"select",
	            editoptions: {
	                 value: "A:Active;I:Inactive"
	             }
	        }
	    ],
		loadonce : true,
		viewrecords: true,
		autowidth: true,
	    height: 250,
	    rowNum: 10,
	    pager: "#pager"
	});
	
	userMgr.navGrid("#pager",
	    { edit: true, add: false, del: false, search: false, refresh: true, view: false, align: "left" },
	    { closeAfterEdit: true }
	);	
});

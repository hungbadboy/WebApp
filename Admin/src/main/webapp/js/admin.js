jQuery(document).ready(function($) {
	var menuTree = jQuery('#tree');
		menuTree.jqGrid({
			"autowidth":true,
			"hoverrows":false,
			"viewrecords":false,
			"gridview":true,
			url : endPointUrl + '/adminService/getAllMenu',
			"ExpandColumn":"name",
			"height":"300",
			"sortname":"displaysort",
			"scrollrows":true,
			"treeGrid":true,
			"treedatatype":"json",
			"treeGridModel":"adjacency",//
			"loadonce":false,
			"rowNum":1000,
			"treeReader":{
				"parent_id_field":"parentid",
				"level_field":"level",
				"leaf_field":"isLeaf",
				"expanded_field":"expanded",
				"loaded":"loaded",
				"icon_field":"icon"
			},
			"datatype":"json",
			"colModel":[
				{
					"name":"id",
					"key":true,
					"hidden":true
				},{
					"name":"name",
					"label":"Name",
					"width":170,
					editoptions:{maxlength:"50"},
					editrules:{
						required: true
                    },
					"editable":true,
					searchoptions:{
						
					}
				},{
					"name":"description",
					"label":"Description",
					"width":170,
					edittype: "textarea",
					editoptions: {rows:"5", maxlength:"100"},
					"editable":true
				
				},{
					"name":"action",
					"label":"Action",
					"width":170,
					"editable":true
				},{
					"name":"usertype",
					"label":"User Type",
					"width":170,
					"editable":true,
					"align":"center",
					edittype: "select",
                    editoptions: {
                        value: "A:Admin;M:Mentor;S:Student",
                        multiple: true,
                    },
                    editrule:{
                    	editrules:true
                    }
				},{
					"name":"status",
					"label":"Status",
					"width":90,
					"align":"center",
					 edittype: "select",
	                    editoptions: {
	                        value: "A:Active;I:Inactive"
	                    },
					"editable":true
				},{
					"name":"displaysort",
					"sorttype":"numeric",
					"formatter": "integer",
					edittype:"numeric",
					"label":"Display Sort",
					"width":90,
					"align":"right",
					"editable":true,
					editrules:{
						required: true,
						integer: true
                    },
				},{
					"name":"level",
					"hidden":true
				},{
					"name":"parentid",
					"hidden":true
				}
			],
			"pager":"#pager"
		});
		// navGrid add
		menuTree.jqGrid('navGrid','#pager',
		{
			"edit":true,
			"add":true,
			"del":true,
			"search":false,
			"refresh":true,
		},
		// Edit
		{ closeOnEscape: true, reloadAfterSubmit: true, modal: true,
            beforeShowForm: function (formid) {
                $("#pData, #nData").hide();
            },
            onInitializeForm: function (formid) {
                $(formid).attr('method', 'POST');
                $(formid).attr('action', '');
            },
            serializeEditData: function(data) {
                delete data.oper;
                return JSON.stringify(data);
              },
            ajaxEditOptions: { contentType: "application/json" },
            onclickSubmit: function (params, postdata) {
                params.url = endPointUrl + '/adminService/updateMenu';
            },
            afterSubmit: function (response, postdata) {
            	return true;
            },
            afterComplete: function (response, postdata, formid) {
//            	alert('ok');
//                if (response.responseText == "true") {
//                  
//                } else {
//                }
            }
        },
        // Begin Option Add
        { closeOnEscape: true, reloadAfterSubmit: true,
            closeAfterAdd: true,
            left: 400, top: 150, width: 500, height:431,
            mtype: "POST",
            onInitializeForm: function (formid) {
                $(formid).attr('method', 'POST');
                $(formid).attr('action', '');
            },
            serializeEditData: function(data) {
                delete data.oper;
                return JSON.stringify(data);
              },
            ajaxEditOptions: { contentType: "application/json" },
            onclickSubmit: function (params, postdata) {
                params.url = endPointUrl + '/adminService/insertMenu';
                
            },
            afterSubmit: function (response, postdata) {
            	return true;
            },
            afterComplete: function (response, postdata, formid) {
//            	 alert('ok1');
            }
        }, // End Option Add
        // Delete
        { closeOnEscape: true, reloadAfterSubmit: true, left: 300, top: 80, width: 220,
        	serializeDelData: function(data) {
                delete data.oper;
                return JSON.stringify(data);
              },
            ajaxDelOptions: { contentType: "application/json" },
            onclickSubmit: function (params, postdata) {
            	params.url = endPointUrl + '/adminService/deleteMenu';
                
            },
            afterSubmit: function (response, postdata) {
            	
               return true;
            },
            afterComplete: function (response, postdata, formid) {
            }
        },
//        // Sear Options
//        { closeAfterSearch: true, closeOnEscape: true,  left: 400, top: 150, enableClear: true,
//        	multipleSearch: true, uniqueSearchFields : true, multipleGroup : true,
//            onClose: function () {
//                var postdata = $("#grid").jqGrid('getGridParam', 'postData');
//                postdata._search = false;
//                postdata.searchString = "";
//                $("#grid").trigger('reloadGrid');
//                return true;
//            }
//        },
		{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150},
		{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150}
		);
		menuTree.jqGrid('bindKeys');
		
		// Search 
		var timer;
		$("#btnSearch").on("click", function() {
			if(timer) { clearTimeout(timer); }
			timer = setTimeout(function(){
				//timer = null;
				menuTree.jqGrid('filterInput', $("#search_menu").val());
			},0);
		});
	});


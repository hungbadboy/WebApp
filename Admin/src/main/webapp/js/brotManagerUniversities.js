/**
 * @author Tavv
 */

$(document).ready(function () {
            $("#universityMgr").jqGrid({
            	url : endPointUrl + 'university/getAllUniversities',
        		editurl : 'clientArray',
        		mtype : "GET",
        		datatype : "json",
        		page : 1,
                colModel : [
        		{name: 'userid', key: true, hidden:true}, 
        		{
        			label : 'Name',
        			name : 'name',
        			editable : true,
        			width : 150
        		}, {
        			label : 'Type',
        			name : 'type',
        			width : 75,
        			align : 'center',
        			editable : true,
        			edittype : "select",
        			editoptions : {
        				value : "U:University;C:College"
        			}
        		}, {
        			label : 'Address1',
        			name : 'address1',
        			width : 150,
        			editable : true,
        			edittype : "text"
        		}, {
        			label : 'Address2',
        			name : 'address2',
        			width : 150,
        			editable : true,
        			edittype : "text",
        		}, {
        			label : 'City',
        			name : 'city',
        			width : 150,
        			editable : true,
        			edittype : "text",
        		}, {
        			label : 'State',
        			name : 'state',
        			width : 150,
        			editable : true,
        			edittype : "text",
        		}, {
        			label : 'Zipcode',
        			name : 'zip',
        			width : 150,
        			editable : true,
        			edittype : "text",
        		}],
        		loadonce : true,
        		viewrecords : true,
        		autowidth : true,
        		search : true,
        		height : 250,
        		rowNum : 10,
                pager: "#jqGridPager"
            });


            // add navigation bar with some built in actions for the grid
            $('#universityMgr').navGrid('#jqGridPager',
               {
                   edit: false,
                   add: false,
                   del: false,
                   search: true,
                   refresh: true,
                   view: true,
                   position: "left",
                   cloneToTop: false
               });
        });



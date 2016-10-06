$(document).ready(function () {
	var videosMgr = $("#videosMgr");
	$("#videosMgr").jqGrid({
	    url: endPointUrl + 'videoAdmission/getAllVideosAdmission',
	    editurl: 'clientArray',
	    mtype: "GET",
	    datatype: "json",
	    page: 1,
	    colModel: [
	        {name: 'vId', key: true, hidden:true },
	        {label: 'Video Title', name: 'title', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype:"text",
	         },
	        {label: 'Youtube Url', name: 'youtubeUrl', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype:"text",
	        },
	        {label: 'Description', name: 'description', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype: "textarea",
	            formatter: function(v){
	            	return "<div style='max-height:20px'>"+v+"</div>";
	            },
	            unformat: function(v){
	            	return v;
	            }
	        },
	        {label: 'Image', name: 'image', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype: "text",
	        },
	        {label: 'Create Date', name: 'creationDate', width: 150,
	            edittype: "text",
	            formatter: formatDate
	        },
	        {label: 'Create By', name: 'author', width: 150,
	            edittype: "text",
	        },
	        {label: 'Active', name: 'active', width: 150,
	            editable: true,
	            edittype:"checkbox",
	            formatter: function(v){
	            	if (v==='Y')
	            		return 'Active';
	            	else
	            		return 'Inactive';
	            }
	        },
	    ],
		loadonce : true,
		viewrecords: true,
		autowidth: true,
	    height: 250,
	    rowNum: 10,
	    pager: "#pager"
	});
	
	videosMgr.navGrid("#pager",
	    { edit: true, add: true, del: true, search: false, refresh: true, view: false, align: "left" },
	    {// options for the Edit Dialog
	    	caption: "Edit Video Adminission",
	    	onclickSubmit: function(){
	    		var myGrid = $('#videosMgr'),
	    	    selRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
	    	    celValue = myGrid.jqGrid ('getCell', selRowId, 'vId');
	    		
	    		var RequestData = new Object();
				var VideoAdmission = new Object();
				
				VideoAdmission.vId=celValue;
				VideoAdmission.title=$('#title').val();
				VideoAdmission.youtubeUrl=$('#youtubeUrl').val();
				VideoAdmission.description=$('#description').val();
				VideoAdmission.image=$('#image').val();
				VideoAdmission.active=$('input[id="active"]:checked').length ? 'Y': 'N';
				
				RequestData.request_data_type = 'VideoAdmission';
				RequestData.request_data_method = 'deleteArticle';
				RequestData.request_data_videoAdmission = VideoAdmission;
	    		$.ajax({
					url:endPointUrl+'videoAdmission/updateVideoAdmission',
					type : "POST",
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(RequestData),
					success : function(data) {
						if (data.request_data_result){
							myGrid.jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
							return true;
						}					
					}
	    		});
	    	},
	    	closeAfterEdit: true 
	    },
	    {// options for the Add Dialog
	    	caption: "Create Video Adminission",
	    	onclickSubmit: function(){
	    		var RequestData = new Object();
				var VideoAdmission = new Object();
				
				VideoAdmission.authorId= logedUserId;
				VideoAdmission.title=$('#title').val();
				VideoAdmission.youtubeUrl=$('#youtubeUrl').val();
				VideoAdmission.description=$('#description').val();
				VideoAdmission.image=$('#image').val();
				VideoAdmission.active=$('input[id="active"]:checked').length ? 'Y': 'N';
				
				RequestData.request_data_type = 'VideoAdmission';
				RequestData.request_data_method = 'deleteArticle';
				RequestData.request_data_videoAdmission = VideoAdmission;
	    		$.ajax({
					url:endPointUrl+'videoAdmission/createVideoAdmission',
					type : "POST",
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(RequestData),
					success : function(data) {
						if (data.request_data_result){
							$('#videosMgr').jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
							return true;
						}
					}
	    		});
	    	},
	    	closeAfterAdd: true 
	    },
	    {// options for the Delete Dialog
	    	caption: "Delete Video Adminission",
	    	onclickSubmit: function(){
	    		var myGrid = $('#videosMgr'),
	    	    selRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
	    	    celValue = myGrid.jqGrid ('getCell', selRowId, 'vId');
	    		
	    		var RequestData = new Object();
				var VideoAdmission = new Object();
				VideoAdmission.vId=celValue;
				RequestData.request_data_type = 'VideoAdmission';
				RequestData.request_data_method = 'deleteVideoAdmission';
				RequestData.request_data_videoAdmission = VideoAdmission;
	    		$.ajax({
					url:endPointUrl+'videoAdmission/deleteVideoAdmission',
					type : "POST",
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(RequestData),
					success : function(data) {
						if (data.request_data_result)
							return true;
					}
	    		});
	    	},
	    	closeAfterEdit: true 
	    }
	);	
});

//Function convert timeStamp to YYYY-MM-DD HH:MM:SS
function formatDate(v){
	var fullDate = new Date(v);
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
	return fullDate.getFullYear()+'-'+month+'-'+date+' '+hours+':'+minutes+':'+seconds;
}
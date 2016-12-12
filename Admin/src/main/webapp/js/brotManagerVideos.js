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
	        {label: 'Video Title', name: 'title', width: 250, classes:'ellipsis',
	            editable: true,
	            edittype:"text",
	         },
	        {label: 'Youtube Url', name: 'youtubeUrl', width: 250, classes:'ellipsis',
	            editable: true,
	            edittype:"text",
	            editoptions: { dataEvents: [{ type: 'change', fn: function(e) {validateLink(); } },]}
	        },
	        {label: 'Duration', name: 'runningTime', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype:"text",
	        },
	        {label: 'Description', name: 'description', width: 250, classes:'ellipsis',
	            editable: true,
	            edittype: "textarea",
	            formatter: function(v){
	            	return "<div style='max-height:20px'>"+v+"</div>";
	            },
	            unformat: function(v){
	            	return v;
	            }
	        },
	        {label: 'Image', name: 'image', width: 250, classes:'ellipsis',
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
				VideoAdmission.runningTime=$('#runningTime').val();
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

function getVideoInfo(vid) {
	 var url = "https://www.googleapis.com/youtube/v3/videos?id=" + vid + "&key=AIzaSyDYwPzLevXauI-kTSVXTLroLyHEONuF9Rw&part=snippet,contentDetails";
	 $.ajax({
         async: false,
         type: 'GET',
         url: url,
         dataType : "json",
 		contentType: "application/json; charset=utf-8",
 		success : function(data) {
 			var result = data.items;
 			if (result && result.length > 0) {
 			      var contentDetails = result[0].contentDetails;
 			      $('#runningTime').val(convertTime(contentDetails.duration));
 			      $('#title').val(result[0].snippet.title);
 			      $('#description').val(result[0].snippet.description);
 			   
 			    } else{
 			    	 $('#runningTime').val('');
 	 			     $('#title').val('');
 	 			     $('#description').val('');
 			      //$scope.error = "Please input valid link.";
 			      $('#youtubeUrl').focus();
 			    }
 		}
     });
}

	 function validateLink(){
	      var link = $('#youtubeUrl').val();
	      if (link == null || link.length == 0){
	        clearContent();
	        return;
	      }
	      checkLink(link);
	}

	    function checkLink(link){
	      var videoid = link.match(/(?:https?:\/{2})?(?:w{3}\.)?youtu(?:be)?\.(?:com|be)(?:\/watch\?v=|\/)([^\s&]+)/);
	      if (videoid != null) {
	        var vid = videoid[1];
	        getVideoInfo(vid);
//	        if (player === undefined) {
//	          onYouTubeIframeAPIReady($scope.vid);
//	        } else {
//	          player.cueVideoById($scope.vid);
//	        }
	      } else{
	        //$scope.error = "Please input valid link.";
	        //angular.element('#txtTutorialUrl').trigger('focus');
	      }
	    }
	    
	    function convertTime(duration) {
	        var a = duration.match(/\d+/g);

	        if (duration.indexOf('M') >= 0 && duration.indexOf('H') == -1 && duration.indexOf('S') == -1) {
	            a = [0, a[0], 0];
	        }

	        if (duration.indexOf('H') >= 0 && duration.indexOf('M') == -1) {
	            a = [a[0], 0, a[1]];
	        }
	        if (duration.indexOf('H') >= 0 && duration.indexOf('M') == -1 && duration.indexOf('S') == -1) {
	            a = [a[0], 0, 0];
	        }

	        duration = 0;

	        if (a.length == 3) {
	            duration = duration + parseInt(a[0]) * 3600;
	            duration = duration + parseInt(a[1]) * 60;
	            duration = duration + parseInt(a[2]);
	        }

	        if (a.length == 2) {
	            duration = duration + parseInt(a[0]) * 60;
	            duration = duration + parseInt(a[1]);
	        }

	        if (a.length == 1) {
	            duration = duration + parseInt(a[0]);
	        }
	        var h = Math.floor(duration / 3600);
	        var m = Math.floor(duration % 3600 / 60);
	        var s = Math.floor(duration % 3600 % 60);
	        return ((h > 0 ? h + ":" + (m < 10 ? "0" : "") : "") + m + ":" + (s < 10 ? "0" : "") + s);
	    }
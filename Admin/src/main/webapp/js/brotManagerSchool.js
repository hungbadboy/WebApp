$(document).ready(function () {
	var schoolsMgr = $("#schoolsMgr");
	var listMentor = null;
	$.ajax({
		url:endPointUrl+'/user/getListMentor',
		type : "GET",
		dataType : "json",
		contentType: "application/json; charset=utf-8",
		success : function(data) {
			listMentor = data.rows.map(function(v){ return v.userid+':'+v.fullName;});
		},
		async:false
	});
	
	schoolsMgr.jqGrid({
	    url: endPointUrl + 'article/getAllArticles',
	    editurl: 'clientArray',
	    mtype: "GET",
	    datatype: "json",
	    page: 1,
	    colModel: [
	        {name: 'arId', key: true, hidden:true },
	        {name: 'authorId', hidden:true },
	        {label: 'Artical Title', name: 'title', width: 150, align:'center',
	            editable: true,
	            edittype:"text",
	         },
	        {label: 'Content', name: 'content', width: 150,
	            editable: true,
	            edittype: "textarea",
	            formatter: function(v){
	            	return "<div style='max-height:20px'>"+decodeURIComponent(v)+"</div>";
	            }
	        },
	        {label: 'Description', name: 'description', width: 150, classes:'ellipsis',
	            editable: true,
	            edittype: "text",
	        },
	        {label: 'Image', name: 'image', width: 150,
	            editable: true,
	            edittype: "text",
	        },
	        {label: 'Author', name: 'authorId', width: 150,
	            editable: true,
	            edittype: "select",
	            editoptions:{value:listMentor.toString().replace(/,/g,";")},
	            formatter: 'select'
	        },
	        {label: 'Num Comment', name: 'numComments', width: 150, sorttype:'int',
	            edittype: "text",
	        },
	        {label: 'Create Date', name: 'createDate', width: 150,
	            edittype: "text",
	            formatter: formatDate
	        },
	        {label: 'Create By', name: 'createByUser', width: 150,
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
	        }
	    ],
		loadonce : true,
		viewrecords: true,
		autowidth: true,
	    height: 250,
	    rowNum: 10,
	    pager: "#schoolsPager"
	});
	
	schoolsMgr.navGrid("#schoolsPager",
	    { edit: true, add: true, del: true, search: false, refresh: true, view: false, align: "left" },
	    {// options for the Edit Dialog
	    	caption: "Edit Artical Adminission",
	    	afterShowForm : function (formid) {
	    		CKEDITOR.replace('content',
                        {
                            customConfig : 'miniEditor.js',
                            width :   '350px',
                            height: '150px'
                        }
	    		);
	    		var selRowId = schoolsMgr.jqGrid ('getGridParam', 'selrow'),
	    	    celValue = schoolsMgr.jqGrid ('getCell', selRowId, 'authorId');
	    		$('#authorId').val(celValue);
	    		console.info(celValue);
	    	},
	    	onclickSubmit: function(){
	    		var selRowId = schoolsMgr.jqGrid ('getGridParam', 'selrow'),
	    	    celValue = schoolsMgr.jqGrid ('getCell', selRowId, 'arId');
	    		
	    		var RequestData = new Object();
				var Article = new Object();
				
				Article.arId=celValue;
				Article.title=$('#title').val();
				Article.authorId=$('#authorId').val();
				Article.description=$('#description').val();
				Article.image=$('#image').val();
				Article.content=encodeURIComponent(CKEDITOR.instances.content.getData());
				Article.active=$('input[id="active"]:checked').length ? 'Y': 'N';
				
				RequestData.request_data_type = 'Article';
				RequestData.request_data_method = 'deleteArticle';
				RequestData.request_data_article = Article;
	    		$.ajax({
					url:endPointUrl+'article/updateArticle',
					type : "POST",
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(RequestData),
					success : function(data) {
						if (data.request_data_result){
							schoolsMgr.jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
							return true;
						}					
					}
	    		});
	    	},
	    	closeAfterEdit: true 
	    },
	    {// options for the Add Dialog
	    	caption: "Create Artical Adminission",
	    	afterShowForm : function (formid) {
	    		CKEDITOR.replace('content',
                        {
                            customConfig : 'miniEditor.js',
                            width :   '350px',
                            height: '150px'
                        }
	    		);
	    	}, 
	    	onclickSubmit: function(){
	    		var RequestData = new Object();
				var Article = new Object();
				
				Article.title=$('#title').val();
				Article.authorId=$('#authorId').val();
				Article.description=$('#description').val();
				Article.image=$('#image').val();
				Article.content=encodeURIComponent(CKEDITOR.instances.content.getData());
				Article.active=$('input[id="active"]:checked').length ? 'Y': 'N';
				Article.createBy=logedUserId;
				
				RequestData.request_data_type = 'Article';
				RequestData.request_data_method = 'deleteArticle';
				RequestData.request_data_article = Article;
	    		$.ajax({
					url:endPointUrl+'article/createArticle',
					type : "POST",
					dataType : "json",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(RequestData),
					success : function(data) {
						if (data.request_data_result){
							$('#schoolsMgr').jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
							return true;
						}
					}
	    		});
	    	},
	    	closeAfterAdd: true 
	    },
	    {// options for the Delete Dialog
	    	caption: "Delete Artical Adminission",
	    	onclickSubmit: function(){
	    		var selRowId = schoolsMgr.jqGrid ('getGridParam', 'selrow'),
	    	    celValue = schoolsMgr.jqGrid ('getCell', selRowId, 'arId');
	    		
	    		var RequestData = new Object();
				var Article = new Object();
				Article.arId=celValue;
				RequestData.request_data_type = 'Article';
				RequestData.request_data_method = 'deleteArticle';
				RequestData.request_data_article = Article;
	    		$.ajax({
					url:endPointUrl+'/article/deleteArticle',
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
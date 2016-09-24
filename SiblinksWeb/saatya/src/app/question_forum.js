jQuery(document).ready(function($) {
	initEvent();
});

function initEvent(){
	$('#btnShowQuestion').click(function(){
    $('#content_question').show();
    $('#btnHideQuestion').show();
    $('#btnShowQuestion').hide();
    });

    $('#btnHideQuestion').click(function(){
    $('#content_question').hide();
    $('#btnHideQuestion').hide();
    $('#btnShowQuestion').show();
    });
}

brot.forum = {};

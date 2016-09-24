var dashboard = {};

dashboard.createModal = function(id){
    var popup = dashboard.get_ele(id);
    // remove all element had this id
    if (dashboard.is_ele(popup)) {
        popup.parentNode.removeChild(popup);
    }
    $('<div></div>').attr({
        'id': id,
        'class': 'modal',
        'tabindex' : '-1'
    }).appendTo('body');
};

dashboard.get_ele = function(id){
    return document.getElementById(id);
};

dashboard.is_ele = function(ele) {
    return (ele && ele.tagName && ele.nodeType == 1);
};

dashboard.getUrlFolder = function() {
    var indOf = window.location.pathname.indexOf("/",0);
    var myStr = window.location.pathname.substr(0,indOf+1 );
    return myStr;
};

dashboard.displaySuccessTip = function(str) {

    var id = 'tipAlert' + Math.floor((Math.random()*10000000000000)+1);
    var content = '<div class="alert alert-success" style="min-weight:335px">';
        content += '<button style="margin-left: 10px;" type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>';
        content +=  str;
        content +=  '</div>';

    var popup = dashboard.get_ele(id);
    // remove all element had this id
    if (dashboard.is_ele(popup)) {
        popup.parentNode.removeChild(popup);
    }

    $("<div></div>").attr({
        'id': id,
        'class': 'jTipCenter'
    }).appendTo('body').html(content);

    // remove on click close icon
    $('#' + id).find('.close').click(function() {
        $(this).parent().fadeOut().remove();
    });
    // auto hide message
    setTimeout(function() {
        $('#' + id).fadeOut(3000).remove();
    }, 5000);

};
dashboard.join = function(str) {
    var store = [str];
    return function extend(other) {
        if(other != null && 'string' == typeof other) {
            store.push(other);
            return extend;
        }
        return store.join('');
    };
};
brotApp.filter('moment', function() {
    return function(dateString, format) {
        return moment(dateString).format(format);
    };
});
/*

 Angular normalizes an element's tag and attribute name to determine which elements match which directives.
 We typically refer to directives by their case-sensitive camelCase normalized name (e.g. ngModel).
 However, since HTML is case-insensitive, we refer to directives in the DOM by lower-case forms,
 typically using dash-delimited attributes on DOM elements (e.g. ng-model).

 The normalization process is as follows:

 Strip x- and data- from the front of the element/attributes.
 Convert the :, -, or _-delimited name to camelCase.

 note : name attribute should NamePageHtml NameAttribute
 ex   : mentor-profile-att-show-more
 page : mentorProfile.html

 */



// mentor show more description
brotControllers.directive('mentorProfileAttShowMore', function () {
    return {
        restrict : 'A',
        link: function(scope, ele) {
            ele.click(function(event) {
                var showhide = ele.attr('data-id');
                if(showhide === 0){ // 0 is hide
                    ele.parent().prev().addClass('hide_show_descripton');
                    ele.text('Hide');
                    ele.attr('data-id','1');
                }
                if(showhide == 1){ // 1 is show
                    ele.parent().prev().removeClass('hide_show_descripton');
                    ele.text('Show more');
                    ele.attr('data-id','0');
                }
            });
        }
    };
});

// date picker
brotApp.directive('brotdatepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            dateFormat: "@dateformat"
        },
         link: function (scope, element, attrs, ngModelCtrl) {
            element.datepicker({
                changeYear:true,
                changeMonth:true,
                dateFormat: scope.dateFormat||'mm-dd,yy',
                maxDate: new Date(),
                yearRange: '1920:2050',
                onSelect: function (date) {
                    scope.date = date;
                    scope.$apply();
                }
            });
        }
    };
});
// student show more description
brotControllers.directive('studentProfileAttShowMore', function () {
    return {
        restrict : 'A',
        link: function(scope, ele) {
            ele.click(function(event) {
                var showhide = ele.attr('data-id');
                if(showhide === 0){ // 0 is hide
                    ele.parent().prev().addClass('hide_show_descripton');
                    ele.text('Hide');
                    ele.attr('data-id','1');
                }
                if(showhide == 1){ // 1 is show
                    ele.parent().prev().removeClass('hide_show_descripton');
                    ele.text('Show more');
                    ele.attr('data-id','0');
                }
            });
        }
    };
});

// order by list mentor by name
brotControllers.directive('videoChatMentorAttGetName' ,['MentorService', function (MentorService) {
    return {
        restrict : 'A',
        link: function(scope, ele) {
            ele.click(function(event) {
                // remove all .active
                ele.parent().parent().find('.active').removeClass('active').addClass('drop_menu');
                // this add class active
                ele.addClass('active');
                ele.removeClass('drop_menu');
                // add image for .active
                if(ele.hasClass('active')){
                    // remove image not active
                    ele.parent().parent().find('img').attr('src', 'assets/img/arrow_down_black.png');
                    // add image active
                    ele.find('img').attr('src', 'assets/img/arrow_down_blue.png');
                }
                // get name attribute
                var name = ele.attr('data-name');
                scope.order = name;
                // get list mentor
                scope.filterFunc();
                // MentorService.getListMentors(name).then(function(value){
                //   if(value.data.status == "true"){
                //     scope.listMentor = value.data.request_data_result;
                //   }
                // });
            });
        }
    };
}]);

// hide how it work in video chat
brotControllers.directive('videoChatMentorAttClose', function () {
    return {
        restrict : 'A',
        link: function(scope, ele) {
            ele.click(function(event) {
                ele.parent().parent().parent().addClass('hide');
            });
        }
    };
});
brotControllers.directive('backPrevious', function($window){
    return {
        restrict : 'A',
        link: function(scope, element) {
            element.on('click', function() {
                $window.history.back();
            });
        }
    };
});


brotControllers.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

brotControllers.directive('angucompleteAlt', ['$q', '$parse', '$http', '$sce', '$timeout', '$templateCache', '$interpolate', function ($q, $parse, $http, $sce, $timeout, $templateCache, $interpolate) {
    // keyboard events
    var KEY_DW  = 40;
    var KEY_RT  = 39;
    var KEY_UP  = 38;
    var KEY_LF  = 37;
    var KEY_ES  = 27;
    var KEY_EN  = 13;
    var KEY_TAB =  9;

    var MIN_LENGTH = 3;
    var MAX_LENGTH = 524288;  // the default max length per the html maxlength attribute
    var PAUSE = 500;
    var BLUR_TIMEOUT = 200;

    // string constants
    var REQUIRED_CLASS = 'autocomplete-required';
    var TEXT_SEARCHING = 'Searching...';
    var TEXT_NORESULTS = 'No results found';
    var TEMPLATE_URL = '/angucomplete-alt/index.html';

    // Set the default template for this directive
    $templateCache.put(TEMPLATE_URL,
        '<div class="angucomplete-holder" ng-class="{\'angucomplete-dropdown-visible\': showDropdown}">' +
        '  <input rows ={{rows}} id="{{id}}" name="{{inputName}}" tabindex="{{fieldTabindex}}" ng-class="{\'angucomplete-input-not-empty\': notEmpty}" ng-model="searchStr" ng-disabled="disableInput" type="{{inputType}}" placeholder="{{placeholder}}" maxlength="{{maxlength}}" ng-focus="onFocusHandler()" class="{{inputClass}}" ng-focus="resetHideResults()" ng-blur="hideResults($event)"  autocapitalize="off" autocorrect="off" autocomplete="off" ng-change="inputChangeHandler(searchStr)"/>' +
        '  <div id="{{id}}_dropdown" class="angucomplete-dropdown" ng-show="showDropdown">' +
        '    <div class="angucomplete-searching" ng-show="searching" ng-bind="textSearching"></div>' +
        '    <div class="angucomplete-searching" ng-show="!searching && (!results || results.length == 0)" ng-bind="textNoResults"></div>' +
        '    <div class="angucomplete-row" ng-repeat="result in results" ng-click="selectResult(result)" ng-mouseenter="hoverRow($index)" ng-class="{\'angucomplete-selected-row\': $index == currentIndex}">' +
        '      <div ng-if="imageField" class="angucomplete-image-holder">' +
        '        <img ng-if="result.image && result.image != \'\'" ng-src="{{result.image}}" class="angucomplete-image"/>' +
        '        <div ng-if="!result.image && result.image != \'\'" class="angucomplete-image-default"></div>' +
        '      </div>' +
        '      <div class="angucomplete-title" ng-if="matchClass" ng-bind-html="result.title"></div>' +
        '      <div class="angucomplete-title" ng-if="!matchClass">{{ result.title }}</div>' +
        '      <div ng-if="matchClass && result.description && result.description != \'\'" class="angucomplete-description" ng-bind-html="result.description"></div>' +
        '      <div ng-if="!matchClass && result.description && result.description != \'\'" class="angucomplete-description">{{result.description}}</div>' +
        '    </div>' +
        '  </div>' +
        '</div>'
    );

    function link(scope, elem, attrs, ctrl) {
        var inputField = elem.find('input');
        var minlength = MIN_LENGTH;
        var searchTimer = null;
        var hideTimer;
        var requiredClassName = REQUIRED_CLASS;
        var responseFormatter;
        var validState = null;
        var httpCanceller = null;
        var httpCallInProgress = false;
        var dd = elem[0].querySelector('.angucomplete-dropdown');
        var isScrollOn = false;
        var mousedownOn = null;
        var unbindInitialValue;
        var displaySearching;
        var displayNoResults;
        var placeHolder = scope.placeholder;

        elem.on('mousedown', function(event) {
            if (event.target.id) {
                mousedownOn = event.target.id;
                if (mousedownOn === scope.id + '_dropdown') {
                    document.body.addEventListener('click', clickoutHandlerForDropdown);
                }
            }
            else {
                mousedownOn = event.target.className;
            }
        });

        scope.currentIndex = scope.focusFirst ? 0 : null;
        scope.searching = false;
        unbindInitialValue = scope.$watch('initialValue', function(newval) {
            if (newval) {
                // remove scope listener
                unbindInitialValue();
                // change input
                handleInputChange(newval, true);
            }
        });

        scope.$watch('fieldRequired', function(newval, oldval) {
            if (newval !== oldval) {
                if (!newval) {
                    ctrl[scope.inputName].$setValidity(requiredClassName, true);
                }
                else if (!validState || scope.currentIndex === -1) {
                    handleRequired(false);
                }
                else {
                    handleRequired(true);
                }
            }
        });

        scope.$on('angucomplete-alt:clearInput', function (event, elementId) {
            if (!elementId || elementId === scope.id) {
                scope.searchStr = null;
                callOrAssign();
                handleRequired(false);
                clearResults();
            }
        });

        scope.$on('angucomplete-alt:changeInput', function (event, elementId, newval) {
            if (!!elementId && elementId === scope.id) {
                handleInputChange(newval);
            }
        });

        function handleInputChange(newval, initial) {
            if (newval) {
                if (typeof newval === 'object') {
                    scope.searchStr = extractTitle(newval);
                    callOrAssign({originalObject: newval});
                } else if (typeof newval === 'string' && newval.length > 0) {
                    scope.searchStr = newval;
                } else {
                    if (console && console.error) {
                        console.error('Tried to set ' + (!!initial ? 'initial' : '') + ' value of angucomplete to', newval, 'which is an invalid value');
                    }
                }

                handleRequired(true);
            }
        }

        // #194 dropdown list not consistent in collapsing (bug).
        function clickoutHandlerForDropdown(event) {
            mousedownOn = null;
            scope.hideResults(event);
            document.body.removeEventListener('click', clickoutHandlerForDropdown);
        }

        // for IE8 quirkiness about event.which
        function ie8EventNormalizer(event) {
            return event.which ? event.which : event.keyCode;
        }

        function callOrAssign(value) {
            if (typeof scope.selectedObject === 'function') {
                scope.selectedObject(value, scope.selectedObjectData);
            }
            else {
                scope.selectedObject = value;
            }

            if (value) {
                handleRequired(true);
            }
            else {
                handleRequired(false);
            }
        }

        function callFunctionOrIdentity(fn) {
            return function(data) {
                return scope[fn] ? scope[fn](data) : data;
            };
        }

        function setInputString(str) {
            callOrAssign({originalObject: str});

            if (scope.clearSelected) {
                scope.searchStr = null;
            }
            clearResults();
        }

        function extractTitle(data) {
            // split title fields and run extractValue for each and join with ' '
            return scope.titleField.split(',')
                .map(function(field) {
                    return extractValue(data, field);
                })
                .join(' ');
        }

        function extractValue(obj, key) {
            var keys, result;
            if (key) {
                keys= key.split('.');
                result = obj;
                for (var i = 0; i < keys.length; i++) {
                    result = result[keys[i]];
                }
            }
            else {
                result = obj;
            }
            return result;
        }

        function findMatchString(target, str) {
            var result, matches, re;
            // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions
            // Escape user input to be treated as a literal string within a regular expression
            re = new RegExp(str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i');
            if (!target) { return; }
            if (!target.match || !target.replace) { target = target.toString(); }
            matches = target.match(re);
            if (matches) {
                result = target.replace(re,
                    '<span class="'+ scope.matchClass +'">'+ matches[0] +'</span>');
            }
            else {
                result = target;
            }
            return $sce.trustAsHtml(result);
        }

        function handleRequired(valid) {
            scope.notEmpty = valid;
            validState = scope.searchStr;
            if (scope.fieldRequired && ctrl && scope.inputName) {
                ctrl[scope.inputName].$setValidity(requiredClassName, valid);
            }
        }

        function keyupHandler(event) {
            var which = ie8EventNormalizer(event);
            if (which === KEY_LF || which === KEY_RT) {
                // do nothing
                return;
            }

            if (which === KEY_UP || which === KEY_EN) {
                event.preventDefault();
            }
            else if (which === KEY_DW) {
                event.preventDefault();
                if (!scope.showDropdown && scope.searchStr && scope.searchStr.length >= minlength) {
                    initResults();
                    scope.searching = true;
                    searchTimerComplete(scope.searchStr);
                }
            }
            else if (which === KEY_ES) {
                clearResults();
                scope.$apply(function() {
                    inputField.val(scope.searchStr);
                });
            }
            else {
                if (minlength === 0 && !scope.searchStr) {
                    return;
                }

                if (!scope.searchStr || scope.searchStr === '') {
                    scope.showDropdown = false;
                } else if (scope.searchStr.length >= minlength) {
                    initResults();

                    if (searchTimer) {
                        $timeout.cancel(searchTimer);
                    }

                    scope.searching = true;

                    searchTimer = $timeout(function() {
                        searchTimerComplete(scope.searchStr);
                    }, scope.pause);
                }

                if (validState && validState !== scope.searchStr && !scope.clearSelected) {
                    scope.$apply(function() {
                        callOrAssign();
                    });
                }
            }
        }

        function handleOverrideSuggestions(event) {
            if (scope.overrideSuggestions &&
                !(scope.selectedObject && scope.selectedObject.originalObject === scope.searchStr)) {
                if (event) {
                    event.preventDefault();
                }

                // cancel search timer
                $timeout.cancel(searchTimer);
                // cancel http request
                cancelHttpRequest();

                setInputString(scope.searchStr);
            }
        }

        function dropdownRowOffsetHeight(row) {
            var css = getComputedStyle(row);
            return row.offsetHeight +
                parseInt(css.marginTop, 10) + parseInt(css.marginBottom, 10);
        }

        function dropdownHeight() {
            return dd.getBoundingClientRect().top +
                parseInt(getComputedStyle(dd).maxHeight, 10);
        }

        function dropdownRow() {
            return elem[0].querySelectorAll('.angucomplete-row')[scope.currentIndex];
        }

        function dropdownRowTop() {
            return dropdownRow().getBoundingClientRect().top -
                (dd.getBoundingClientRect().top +
                parseInt(getComputedStyle(dd).paddingTop, 10));
        }

        function dropdownScrollTopTo(offset) {
            dd.scrollTop = dd.scrollTop + offset;
        }

        function updateInputField(){
            var current = scope.results[scope.currentIndex];
            if (scope.matchClass) {
                inputField.val(extractTitle(current.originalObject));
            }
            else {
                inputField.val(current.title);
            }
        }

        function keydownHandler(event) {
            var which = ie8EventNormalizer(event);
            var row = null;
            var rowTop = null;


            if (which === KEY_EN && scope.results) {
                if (scope.currentIndex >= 0 && scope.currentIndex < scope.results.length) {
                    event.preventDefault();
                    scope.selectResult(scope.results[scope.currentIndex]);
                } else {
                    handleOverrideSuggestions(event);
                    clearResults();
                }
                scope.$apply();
            } else if (which === KEY_DW && scope.results) {
                event.preventDefault();
                if ((scope.currentIndex + 1) < scope.results.length && scope.showDropdown) {
                    scope.$apply(function() {
                        scope.currentIndex ++;
                        updateInputField();
                    });

                    if (isScrollOn) {
                        row = dropdownRow();
                        if (dropdownHeight() < row.getBoundingClientRect().bottom) {
                            dropdownScrollTopTo(dropdownRowOffsetHeight(row));
                        }
                    }
                }
            } else if (which === KEY_UP && scope.results) {
                event.preventDefault();
                if (scope.currentIndex >= 1) {
                    scope.$apply(function() {
                        scope.currentIndex --;
                        updateInputField();
                    });

                    if (isScrollOn) {
                        rowTop = dropdownRowTop();
                        if (rowTop < 0) {
                            dropdownScrollTopTo(rowTop - 1);
                        }
                    }
                }
                else if (scope.currentIndex === 0) {
                    scope.$apply(function() {
                        scope.currentIndex = -1;
                        inputField.val(scope.searchStr);
                    });
                }
            } else if (which === KEY_TAB) {
                if (scope.results && scope.results.length > 0 && scope.showDropdown) {
                    if (scope.currentIndex === -1 && scope.overrideSuggestions) {
                        // intentionally not sending event so that it does not
                        // prevent default tab behavior
                        handleOverrideSuggestions();
                    }
                    else {
                        if (scope.currentIndex === -1) {
                            scope.currentIndex = 0;
                        }
                        scope.selectResult(scope.results[scope.currentIndex]);
                        scope.$digest();
                    }
                }
                else {
                    // no results
                    // intentionally not sending event so that it does not
                    // prevent default tab behavior
                    if (scope.searchStr && scope.searchStr.length > 0) {
                        handleOverrideSuggestions();
                    }
                }
            } else if (which === KEY_ES) {
                // This is very specific to IE10/11 #272
                // without this, IE clears the input text
                event.preventDefault();
            }
        }

        function httpSuccessCallbackGen(str) {
            return function(responseData, status, headers, config) {
                // normalize return obejct from promise
                if (!status && !headers && !config && responseData.data) {
                    responseData = responseData.data;
                }
                scope.searching = false;
                processResults(
                    extractValue(responseFormatter(responseData), scope.remoteUrlDataField),
                    str);
            };
        }

        function httpErrorCallback(errorRes, status, headers, config) {
            scope.searching = httpCallInProgress;

            // normalize return obejct from promise
            if (!status && !headers && !config) {
                status = errorRes.status;
            }

            // cancelled/aborted
            if (status === 0 || status === -1) { return; }
            if (scope.remoteUrlErrorCallback) {
                scope.remoteUrlErrorCallback(errorRes, status, headers, config);
            }
            else {
                if (console && console.error) {
                    console.error('http error');
                }
            }
        }

        function cancelHttpRequest() {
            if (httpCanceller) {
                httpCanceller.resolve();
            }
        }

        function getRemoteResults(str) {
            var params = {},
                url = scope.remoteUrl + encodeURIComponent(str);
            if (scope.remoteUrlRequestFormatter) {
                params = {params: scope.remoteUrlRequestFormatter(str)};
                url = scope.remoteUrl;
            }
            if (!!scope.remoteUrlRequestWithCredentials) {
                params.withCredentials = true;
            }
            cancelHttpRequest();
            httpCanceller = $q.defer();
            params.timeout = httpCanceller.promise;
            httpCallInProgress = true;
            $http.get(url, params)
                .success(httpSuccessCallbackGen(str))
                .error(httpErrorCallback)
                .finally(function(){httpCallInProgress=false;});
        }

        function getRemoteResultsWithCustomHandler(str) {
            cancelHttpRequest();

            httpCanceller = $q.defer();

            scope.remoteApiHandler(str, httpCanceller.promise)
                .then(httpSuccessCallbackGen(str))
                .catch(httpErrorCallback);

            /* IE8 compatible
             scope.remoteApiHandler(str, httpCanceller.promise)
             ['then'](httpSuccessCallbackGen(str))
             ['catch'](httpErrorCallback);
             */
        }

        function clearResults() {
            scope.showDropdown = false;
            scope.results = [];
            if (dd) {
                dd.scrollTop = 0;
            }
        }

        function initResults() {
            scope.showDropdown = displaySearching;
            scope.currentIndex = scope.focusFirst ? 0 : -1;
            scope.results = [];
        }

        function getLocalResults(str) {
            var i, match, s, value,
                searchFields = scope.searchFields.split(','),
                matches = [];
            if (typeof scope.parseInput() !== 'undefined') {
                str = scope.parseInput()(str);
            }
            for (i = 0; i < scope.localData.length; i++) {
                match = false;

                for (s = 0; s < searchFields.length; s++) {
                    value = extractValue(scope.localData[i], searchFields[s]) || '';
                    match = match || (value.toString().toLowerCase().indexOf(str.toString().toLowerCase()) >= 0);
                }

                if (match) {
                    matches[matches.length] = scope.localData[i];
                }
            }
            return matches;
        }

        function checkExactMatch(result, obj, str){
            if (!str) { return false; }
            for(var key in obj){
                if(obj[key].toLowerCase() === str.toLowerCase()){
                    scope.selectResult(result);
                    return true;
                }
            }
            return false;
        }

        function searchTimerComplete(str) {
            // Begin the search
            if (!str || str.length < minlength) {
                return;
            }
            if (scope.localData) {
                scope.$apply(function() {
                    var matches;
                    if (typeof scope.localSearch() !== 'undefined') {
                        matches = scope.localSearch()(str, scope.localData);
                    } else {
                        matches = getLocalResults(str);
                    }
                    scope.searching = false;
                    processResults(matches, str);
                });
            }
            else if (scope.remoteApiHandler) {
                getRemoteResultsWithCustomHandler(str);
            } else {
                getRemoteResults(str);
            }
        }

        function processResults(responseData, str) {
            var i, description, image, text, formattedText, formattedDesc;

            if (responseData && responseData.length > 0) {
                scope.results = [];

                for (i = 0; i < responseData.length; i++) {
                    if (scope.titleField && scope.titleField !== '') {
                        text = formattedText = extractTitle(responseData[i]);
                    }

                    description = '';
                    if (scope.descriptionField) {
                        description = formattedDesc = extractValue(responseData[i], scope.descriptionField);
                    }

                    image = '';
                    if (scope.imageField) {
                        image = extractValue(responseData[i], scope.imageField);
                    }

                    if (scope.matchClass) {
                        formattedText = findMatchString(text, str);
                        formattedDesc = findMatchString(description, str);
                    }

                    scope.results[scope.results.length] = {
                        title: formattedText,
                        description: formattedDesc,
                        image: image,
                        originalObject: responseData[i]
                    };
                }

            } else {
                scope.results = [];
            }

            if (scope.autoMatch && scope.results.length === 1 &&
                checkExactMatch(scope.results[0],
                    {title: text, desc: description || ''}, scope.searchStr)) {
                scope.showDropdown = false;
            } else if (scope.results.length === 0 && !displayNoResults) {
                scope.showDropdown = false;
            } else {
                scope.showDropdown = true;
            }
        }

        function showAll() {
            if (scope.localData) {
                scope.searching = false;
                processResults(scope.localData, '');
            }
            else if (scope.remoteApiHandler) {
                scope.searching = true;
                getRemoteResultsWithCustomHandler('');
            }
            else {
                scope.searching = true;
                getRemoteResults('');
            }
        }

        scope.onFocusHandler = function() {
            if (scope.focusIn) {
                scope.focusIn();
            }
           // if (minlength === 0 && (!scope.searchStr || scope.searchStr.length === 0)) {
                scope.currentIndex = scope.focusFirst ? 0 : scope.currentIndex;
                scope.showDropdown = true;
                showAll();
            //}
        };

        scope.hideResults = function() {
            if (mousedownOn &&
                (mousedownOn === scope.id + '_dropdown' ||
                mousedownOn.indexOf('angucomplete') >= 0)) {
                mousedownOn = null;
            }
            else {
                hideTimer = $timeout(function() {
                    clearResults();
                    scope.$apply(function() {
                        if (scope.searchStr && scope.searchStr.length > 0) {
                            inputField.val(scope.searchStr);
                        }
                    });
                }, BLUR_TIMEOUT);
                cancelHttpRequest();

                if (scope.focusOut) {
                    scope.focusOut();
                }

                if (scope.overrideSuggestions) {
                    if (scope.searchStr && scope.searchStr.length > 0 && scope.currentIndex === -1) {
                        handleOverrideSuggestions();
                    }
                }
            }
        };

        scope.resetHideResults = function() {
            if (hideTimer) {
                $timeout.cancel(hideTimer);
            }
        };

        scope.hoverRow = function(index) {
            scope.currentIndex = index;
        };

        scope.selectResult = function(result) {
            // Restore original values
            if (scope.matchClass) {
                result.title = extractTitle(result.originalObject);
                result.description = extractValue(result.originalObject, scope.descriptionField);
            }

            if (scope.clearSelected) {
                scope.searchStr = null;
            }
            else {
                scope.searchStr = result.title;
            }
            callOrAssign(result);
            clearResults();
        };

        scope.inputChangeHandler = function(str) {
            if (str.length < minlength) {
                cancelHttpRequest();
                clearResults();
            }
            else if (str.length === 0 && minlength === 0) {
                showAll();
            }

            if(scope.searchStr == null||scope.searchStr == ''){
                scope.selectedObject = null;
            }

            if (scope.inputChanged) {
                str = scope.inputChanged(str);
            }
            return str;
        };

        // check required
        if (scope.fieldRequiredClass && scope.fieldRequiredClass !== '') {
            requiredClassName = scope.fieldRequiredClass;
        }

        // check min length
        if (scope.minlength && scope.minlength !== '') {
            minlength = parseInt(scope.minlength, 10);
        }

        // check pause time
        if (!scope.pause) {
            scope.pause = PAUSE;
        }

        // check clearSelected
        if (!scope.clearSelected) {
            scope.clearSelected = false;
        }

        // check override suggestions
        if (!scope.overrideSuggestions) {
            scope.overrideSuggestions = false;
        }

        // check required field
        if (scope.fieldRequired && ctrl) {
            // check initial value, if given, set validitity to true
            if (scope.initialValue) {
                handleRequired(true);
            }
            else {
                handleRequired(false);
            }
        }

        scope.inputType = attrs.type ? attrs.type : 'text';

        // set strings for "Searching..." and "No results"
        scope.textSearching = attrs.textSearching ? attrs.textSearching : TEXT_SEARCHING;
        scope.textNoResults = attrs.textNoResults ? attrs.textNoResults : TEXT_NORESULTS;
        displaySearching = scope.textSearching === 'false' ? false : true;
        displayNoResults = scope.textNoResults === 'false' ? false : true;

        // set max length (default to maxlength deault from html
        scope.maxlength = attrs.maxlength ? attrs.maxlength : MAX_LENGTH;

        // register events
        inputField.on('keydown', keydownHandler);
        inputField.on('keyup', keyupHandler);

        // set response formatter
        responseFormatter = callFunctionOrIdentity('remoteUrlResponseFormatter');

        // set isScrollOn
        $timeout(function() {
            var css = getComputedStyle(dd);
            isScrollOn = css.maxHeight && css.overflowY === 'auto';
        });
    }

    return {
        restrict: 'EA',
        require: '^?form',
        scope: {
            selectedObject: '=',
            selectedObjectData: '=',
            disableInput: '=',
            initialValue: '=',
            localData: '=',
            localSearch: '&',
            remoteUrlRequestFormatter: '=',
            remoteUrlRequestWithCredentials: '@',
            remoteUrlResponseFormatter: '=',
            remoteUrlErrorCallback: '=',
            remoteApiHandler: '=',
            id: '@',
            type: '@',
            placeholder: '@',
            textSearching: '@',
            textNoResults: '@',
            remoteUrl: '@',
            remoteUrlDataField: '@',
            titleField: '@',
            descriptionField: '@',
            imageField: '@',
            inputClass: '@',
            pause: '@',
            searchFields: '@',
            minlength: '@',
            matchClass: '@',
            clearSelected: '@',
            overrideSuggestions: '@',
            fieldRequired: '=',
            fieldRequiredClass: '@',
            inputChanged: '=',
            autoMatch: '@',
            focusOut: '&',
            focusIn: '&',
            fieldTabindex: '@',
            inputName: '@',
            focusFirst: '@',
            parseInput: '&',
            rows:'@'
        },
        templateUrl: function(element, attrs) {
            return attrs.templateUrl || TEMPLATE_URL;
        },
        compile: function(tElement) {
            var startSym = $interpolate.startSymbol();
            var endSym = $interpolate.endSymbol();
            if (!(startSym === '{{' && endSym === '}}')) {
                var interpolatedHtml = tElement.html()
                    .replace(/\{\{/g, startSym)
                    .replace(/\}\}/g, endSym);
                tElement.html(interpolatedHtml);
            }
            return link;
        }

    };
}]);
brotControllers.directive('ngReallyClick', ['$modal',
    function($modal) {

        var ModalInstanceCtrl = function($scope, $modalInstance) {
            $scope.ok = function() {
                $modalInstance.close();
            };

            $scope.cancel = function() {
                $modalInstance.dismiss('cancel');
            };
        };

        return {
            restrict: 'A',
            scope:{
                ngReallyClick:"&",
                item:"="
            },
            link: function(scope, element, attrs) {
                element.bind('click', function() {
                    var message = attrs.ngReallyMessage || "Are you sure ?";

                    var modalHtml = ' <div class="modal-body">' + message + '</div>';
                    modalHtml += '<div class="modal-footer"><button class="btn btn-danger" ng-click="ok()">' +
                        'Delete</button><button class="btn btn-default" ng-click="cancel()">Cancel</button></div>';

                    var modalInstance = $modal.open({
                        template: modalHtml,
                        size: 'sm',
                        controller: ModalInstanceCtrl
                    });

                    modalInstance.result.then(function() {
                        scope.ngReallyClick({item:scope.item}); //raise an error : $digest already in progress
                    }, function() {
                        //Modal dismissed
                    });

                });

            }
        }
    }
]);
brotControllers.directive('scroller', function($timeout, $parse) {
    return {
        restrict:'A',
        link: function(scope, el, attrs) {
            var options = {
                theme: 'dark',
                scrollInertia: 250,
                advanced: {
                    updateOnContentResize: true
                },
                callbacks: {
                    onTotalScrollOffset: 20,
                    onTotalScrollBackOffset: 20,
                }
            };
            var initialized = false;

            scope.$watch(attrs.scroller, function(model) {
                if (!initialized) {
                    $timeout(function() { // wait for ngRepeat to render
                        el.mCustomScrollbar(options);
                        // el.mCustomScrollbar('scrollTo', 'bottom');
                    });
                }
            });

            $timeout(function() {
                options.callbacks.onInit = $parse(attrs.onInit)(scope);
                options.callbacks.onTotalScroll = $parse(attrs.onTotalScroll)(scope);
                options.callbacks.onTotalScrollBack = $parse(attrs.onTotalScrollBack)(scope);
            });
        }
    };
});

brotControllers.directive('readMore', function() {
    return {
        restrict: 'A',
        replace: true,
        scope: {
            text: '=ngModel'
        },
        template: '<div class="video-detail-script"><p class="pre-wrap">{{text | readMoreFilter:[text, countingWords, textLength] }}</p>'+
           ' <div ng-if="showLinks" ng-class="{\'hide-text\': !isExpanded }"></div>'+
        '<button  ng-show="showLinks"  ng-click="changeLength()" class="read-more">'+
        '<span  ng-show="!isExpanded">Read more <span data-icon="&#x33;"></span></span>' +
       ' <span ng-show="isExpanded ">Less <span data-icon="&#x32;"></span></span></button>' +

        '</div>',

        // "<a style='cursor:pointer;' ng-show='showLinks' ng-click='changeLength()' class='color3'>" +
        // "<strong id='showMoreLess' ng-show='isExpanded'>  Show Less</strong>" +
        // "<strong id='showMoreLess'  ng-show='!isExpanded'>  Show More</strong>" +
        //

        controller: ['$scope', '$attrs', '$element',
            function($scope, $attrs) {
                $scope.$watch("text",function(newValue,oldValue) {
                    $scope.isExpanded = false;
                    $scope.textLength = $attrs.length;
                    if($scope.text===undefined||$scope.text==null){
                        $scope.text = "No content";
                    }
                    $scope.isExpanded = false; // initialise extended status
                    $scope.countingWords = $attrs.words !== undefined ? ($attrs.words === 'true') : true; //if this attr is not defined the we are counting words not characters

                    if (!$scope.countingWords && $scope.text.length > $attrs.length) {
                        $scope.showLinks = true;
                    } else if ($scope.countingWords && $scope.text.split(" ").length > $attrs.length) {
                        $scope.showLinks = true;
                    } else {
                        $scope.showLinks = false;
                    }

                    //This gets called when data changes.
                    $scope.changeLength = function (card) {
                        $scope.isExpanded = !$scope.isExpanded;
                        $scope.textLength = $scope.textLength !== $attrs.length ?  $attrs.length : $scope.text.length;
                    };
                });
            }]
    };
});

brotControllers.directive('clickAnywhereButHere', function($document){
    return {
        restrict: 'A',
        link: function(scope, elem, attr, ctrl) {
            elem.bind('click', function(e) {
                // this part keeps it from firing the click on the document.
                e.stopPropagation();
            });
            $document.bind('click', function() {
                // magic here.
                scope.$apply(attr.clickAnywhereButHere);
            })
        }
    }
});

brotControllers.directive('slideit',function() {
    return {
        restrict: 'A',
        replace: true,
        scope: {
          slideit: '=',
          bestDealClicked: "=click"
        },
        template: '<ul class="slider clearfix">'
        +'<li>'
        +'<div class="cont-mainslider">'
        +'<div class="thumb-mainslider">'
        +'<a href="#">'
        +'<img src="assets/images/user.jpg"/>'
        +'</a>'
        +'</div><!-- thumb-mainslider -->'
        +'<p>Proin sodales imperdiet elit a efficitur. Donec ullamcorper tristique lobortis. Mauris iaculis venenatis iaculis. Donec non enim hendrerit, finibus orci faucibus, feugiat turpis. Sed commodo ex in lorem pharetra varius</p>'
        +'<div class="author">'
        +'<b>Landy Moore</b><span> - Harvard University faculty</span>'
        +'</div>'
        +'</div><!-- cont-mainslider -->'
        +'<span class="icon-p"><img src="assets/images/icon-p-slider.png" alt=""></span>'
        +'</li>'
        +'<li>'
        +'<div class="cont-mainslider">'
        +'<div class="thumb-mainslider">'
        +'<a href="#">'
        +'<img src="assets/images/user.jpg"/>'
        +'</a>'
        +'</div><!-- thumb-mainslider -->'
        +'<p>Proin sodales imperdiet elit a efficitur. Donec ullamcorper tristique lobortis. Mauris iaculis venenatis iaculis. Donec non enim hendrerit, finibus orci faucibus, feugiat turpis. Sed commodo ex in lorem pharetra varius</p>'
        +'<div class="author">'
        +'<b>Landy Moore</b><span> - Harvard University faculty</span>'
        +'</div>'
        +'</div><!-- cont-mainslider -->'
        +'<span class="icon-p"><img src="assets/images/icon-p-slider.png" alt=""></span>'
        +'</li>'
        +'<li>'
        +'<div class="cont-mainslider">'
        +'<div class="thumb-mainslider">'
        +'<a href="#">'
        +'<img src="assets/images/user.jpg"/>'
        +'</a>'
        +'</div><!-- thumb-mainslider -->'
        +'<p>Proin sodales imperdiet elit a efficitur. Donec ullamcorper tristique lobortis. Mauris iaculis venenatis iaculis. Donec non enim hendrerit, finibus orci faucibus, feugiat turpis. Sed commodo ex in lorem pharetra varius</p>'
        +'<div class="author">'
        +'<b>Landy Moore</b><span> - Harvard University faculty</span>'
        +'</div>'
        +'</div><!-- cont-mainslider -->'
        +'<span class="icon-p"><img src="assets/images/icon-p-slider.png" alt=""></span>'
        +'</li>'
        +'</ul>',
        link: function(scope, elm, attrs) {
           elm.ready(function() {
//             scope.$applyAsync (function() {
//                 scope.bestDeals = scope.slideit;
//             });
             elm.bxSlider
             ({
            	 speed:1000,
                 nextText:'',
                 prevText:''
                 });   
           });
        }
     }; 
 });

brotControllers.directive('btfcarousel', function($timeout) {
    return {
       restrict: 'E',
       scope: {
         links: '='
       },
       templateUrl: 'src/app/carousel/carousel.tpl.html',
       link: function(scope, element) {
         $timeout(function() {
           $('.carousel-indicators li',element).first().addClass('active');
           $('.carousel-inner .item',element).first().addClass('active');
           $('.carousel[data-type="multi"] .item').each(function(){
        	   var next = $(this).next();
        	   if (!next.length) {
        	     next = $(this).siblings(':first');
        	   }
        	   next.children(':first-child').clone().appendTo($(this));
        	   
        	   for (var i=0;i<2;i++) {
        	     next=next.next();
        	     if (!next.length) {
        	     	next = $(this).siblings(':first');
        	   	}
        	     next.children(':first-child').clone().appendTo($(this));
        	   }
        	 });
         });
       }
    }
 });

brotControllers.directive("averageStarRating", function() {
  return {
    restrict : "EA",
    template : "<div class='list-star pull-left'>" +
               "<div class='average-rating-container'>" +
               "  <ul class='rating background' class='readonly'>" +
               "    <li ng-repeat='star in stars' class='star'>" +
               "      <i class='fa fa-star'></i>" + //&#9733
               "    </li>" +
               "  </ul>" +
               "  <ul class='rating foreground' class='readonly' style='width:{{filledInStarsContainerWidth}}%'>" +
               "    <li ng-repeat='star in stars' class='star filled'>" +
               "      <i class='fa fa-star'></i>" + //&#9733
               "    </li>" +
               "  </ul>" +
               "</div>" +
               "</div>",
    scope : {
      averageRatingValue : "=ngModel",
      max : "=?", //optional: default is 5
    },
    link : function(scope, elem, attrs) {
      if (scope.max == undefined) { scope.max = 5; }
      function updateStars() {
        scope.stars = [];
        for (var i = 0; i < scope.max; i++) {
          scope.stars.push({});
        }
        var starContainerMaxWidth = 100; //%
        scope.filledInStarsContainerWidth = scope.averageRatingValue / scope.max * starContainerMaxWidth;
      };
      scope.$watch("averageRatingValue", function(oldVal, newVal) {
        if (newVal) { updateStars(); }
      });
    }
  };
});

/**
 * @author Tavv
 * @description case image load failure
 */
brotControllers.directive('errSrc', function() {
    return {
        link: function(scope, element, attrs) {
            element.bind('error', function() {
                if (attrs.src != attrs.errSrc) {
                    attrs.$set('src', attrs.errSrc);
                }
            });
        }
    }
});

/**
 * Loading process call ajax
 */
brotControllers.directive('loadingDialog', ['$timeout', function($timeout) {
    return {
        restrict   : 'EA',
        transclude : true,
        scope: true,
        link : function(scope, iElement, iAttrs, controller, iTransclude) {
        	 iElement.dialog({
  	           autoOpen: false,
  	           dialogClass: "loadingScreenWindow",
  	           closeOnEscape: false,
  	           draggable: false,
  	           modal: true,
  	           width:'auto',
  	           buttons: {},
  	           resizable: false,
  	           zIndex: 999999,
  	           open: function() {
  	               // scrollbar fix for IE
  	               $('body').css('overflow','hidden');
  	           },
  	           close: function() {
  	               // reset overflow
  	               $('body').css('overflow','auto');
  	           }
  	       }); // end of dialog
        	 
        	 scope.open= function (waiting){
        		 //console.log('waitingDialog');
        		 angular.element('#loadingScreen').show();
        		 angular.element('.ui-widget-header').hide();
        		 angular.element('#header').addClass('loading-disable-header');
        		 iElement.dialog('open');
        	 }
        	 
        	 scope.close= function (){
        		 angular.element('.ui-widget-header').show();
        		 angular.element('#loadingScreen').hide();
        		 angular.element('#header').removeClass('loading-disable-header');
        		 iElement.dialog('close');
        	 }
        	 
        	 scope.destroy = function () {
        		 iElement.dialog('destroy');
        	 }
        	 
        	 scope.$on('$destroy', function () {
					scope.stop();
					scope.spinner = null;
				});
        	 /**
        	  * Open dialog before call ajax
        	  * @returns
        	  */
        	 scope.$on('open', function(waiting) {
        		 scope.open(waiting);
             });

        	 /**
        	  * After call ajax return successfull
        	  * @returns
        	  */
        	 scope.$on('close', function() {
        		 scope.close();
        	 });
        }
    }
}]);

brotControllers.directive('autoHeightRecentActivity', function($window) {
    return {
        link: function(scope, element) {
            var w = angular.element($window);
            var headerRightHeight = angular.element('.mentor-right-header')[0].offsetHeight;
            var screenHeight =  screen.height;
            var changeHeight = function() {element.css('height', (screenHeight - headerRightHeight) + 'px' );};
            w.bind('resize', function () {
                changeHeight();   // when window size gets changed
            });
            changeHeight();
        }
    }
});

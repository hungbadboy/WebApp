/**
 * Created by hieu on 3/14/15.
 */
/**
 * This file/module contains all configuration for the build process.
 */
module.exports = {
    /**
     * The `build_dir` folder is where our projects are compiled during
     * development and the `compile_dir` folder is where our app resides once it's
     * completely built.
     */
    build_dir: 'build', 
    compile_dir: 'bin',
    webservice_url : {
        variable_pattern : /\/\/@webserviceurl/ig,
        //pro : ' = \'http://ec2-52-34-250-166.us-west-2.compute.amazonaws.com:8181/siblinks/services/\'',
        pro : ' = \'http://localhost:8070/siblinks/services/\'',
        dev: ' = \'http://localhost:8070/siblinks/services/\''
        //dev: ' = \'http://ec2-54-200-200-106.us-west-2.compute.amazonaws.com:8181/siblinks/services/\''
    },
    /**
     * This is a collection of file patterns that refer to our app code (the
     * stuff in `src/`). These file paths are used in the configuration of
     * build tasks. `js` is all project javascript, less tests. `ctpl` contains
     * our reusable components' (`src/common`) template HTML files, while
     * `atpl` contains the same, but for our app's code. `html` is just our
     * main HTML file, `less` is our main stylesheet, and `unit` contains our
     * app's unit tests.
     */
    app_files: {
        js: [ 'src/**/*.js', '!src/**/*.spec.js', '!src/assets/**/*.js' ],
        jsunit: [ 'src/**/*.spec.js' ],
        extends_files : [ 'ckeditor/**', 'json/**'],
        atpl: [ 'src/app/**/*.tpl.html' ],
        ctpl: [ 'src/common/**/*.tpl.html' ],

        html: [ 'src/index.html','src/firebase-message-ws.js' ],
        less: 'src/less/main.less'
    },

    /**
     * This is a collection of files used during testing only.
     */
    test_files: {
        js: [

        ]
    },

    /**
     * This is the same as `app_files`, except it contains patterns that
     * reference vendor code (`vendor/`) that we need to place into the build
     * process somewhere. While the `app_files` property ensures all
     * standardized files are collected for compilation, it is the user's job
     * to ensure non-standardized (i.e. vendor-related) files are handled
     * appropriately in `vendor_files.js`.
     *
     * The `vendor_files.js` property holds files to be automatically
     * concatenated and minified with our project source files.
     *
     * The `vendor_files.css` property holds any CSS files to be automatically
     * included in our app.
     *
     * The `vendor_files.assets` property holds any assets to be copied along
     * with our app's assets. This structure is flattened, so it is not
     * recommended that you use wildcards.
     */
    vendor_files: {
        js: [
			'vendor/jquery.min.js',
			'vendor/jquery-ui.min.js',
			//'vendor/main.js',
			'vendor/bootstrap.js',
			'vendor/jquery.bxslider.min.js',
			'vendor/angular.min.js',
            'vendor/jquery.sticky-kit.js',
            'vendor/angular-sticky-kit.js',
            'vendor/rating.js',
            'vendor/ng-infinite-scroll.min.js',
			'vendor/angular-file-upload-shim.min.js',
			'vendor/angular-file-upload.min.js',
			'vendor/angular-route.js',
			'vendor/angular-resource.js',
			'vendor/ui-bootstrap-tpls-0.11.0.js',
            'vendor/jquery.mCustomScrollbar.js',
            'vendor/jquery.mousewheel.min.js',
            'vendor/jquery.1.7.js',
            'vendor/d3.min.js',
            'vendor/nv.d3.js',
            'ckeditor/ckeditor.js',
            'vendor/moment.min.js',
            'vendor/css_browser_selector.js'
            ],
        css: [
        ],
        assets: [
        ]
    }
};

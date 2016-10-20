
/**
 * Created by hieu on 3/14/15.
 */
module.exports = function ( grunt ) {

    /**
     * Load required Grunt tasks. These are installed based on the versions listed
     * in `package.json` when you do `npm install` in this directory.
     */
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-html2js');
    grunt.loadNpmTasks('grunt-string-replace');

    /**
     * Load in our build configuration file.
     */
    var userConfig = require( './build.config.js' );

    /**
     * This is the configuration object Grunt uses to give each plugin its
     * instructions.
     */
    var taskConfig = {
        /**
         * We read in our `package.json` file so we can access the package name and
         * version. It's already there, so we don't repeat ourselves here.
         */
        pkg: grunt.file.readJSON("package.json"),

        /**
         * The banner is the comment that is placed at the top of our compiled
         * source files. It is first processed as a Grunt template, where the `<%=`
         * pairs are evaluated based on this very configuration object.
         */
        meta: {
            banner:
            '/**\n' +
            ' * <%= pkg.name %> - v<%= pkg.version %> - <%= grunt.template.today("yyyy-mm-dd") %>\n' +
            ' * <%= pkg.homepage %>\n' +
            ' *\n' +
            ' * Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author %>\n' +
            ' */\n'
        },


        /**
         * The directories to delete when `grunt clean` is executed.
         */
        clean: [
            '<%= build_dir %>',
            '<%= compile_dir %>'
        ],

        /**
         * The `copy` task just copies files from A to B. We use it here to copy
         * our project assets (images, fonts, etc.) and javascripts into
         * `build_dir`, and then to copy the assets to `compile_dir`.
         */
        copy: {
        	firebase_messaging_sw: {
        		files: [
                        {
                            src: [ 'firebase-messaging-sw.js'],
                            dest: '<%= build_dir %>/',
                            cwd: 'src/',
                            expand: true,
                            flatten: true
                        }
                    ]
        	},
            build_app_assets: {
                files: [
                    {
                        src: [ '**' ],
                        dest: '<%= build_dir %>/assets/',
                        cwd: 'src/assets',
                        expand: true
                    }
                ]
            },
            build_vendor_assets: {
                files: [
                    {
                        src: [ '<%= vendor_files.assets %>' ],
                        dest: '<%= build_dir %>/assets/',
                        cwd: '.',
                        expand: true,
                        flatten: true
                    }
                ]
            },
            build_appjs: {
                files: [
                    {
                        src: [ '<%= app_files.js %>'],
                        dest: '<%= build_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            build_apptemplate : {
                files: [
                    {
                        src: [ '**/*.tpl.html' ],
                        dest: '<%= build_dir %>/src/app',
                        cwd : 'src/app/',
                        expand : true,
                        filter: 'isFile'
                    }
                ]
            },
            build_extends_files : {
                files: [
                        {
                        src: [ '<%= app_files.extends_files %>' ],
                        dest: '<%= build_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            build_vendorjs: {
                files: [
                    {
                        src: [ '<%= vendor_files.js %>' ],
                        dest: '<%= build_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            build_vendorcss: {
                files: [
                    {
                        src: [ '<%= vendor_files.css %>' ],
                        dest: '<%= build_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            },
            compile_assets: {
                files: [
                    {
                        src: [ '**' ],
                        dest: '<%= compile_dir %>/assets',
                        cwd: '<%= build_dir %>/assets',
                        expand: true
                    },
                    {
                        src: [ '<%= vendor_files.css %>' ],
                        dest: '<%= compile_dir %>/',
                        cwd: '.',
                        expand: true
                    }
                ]
            }
        },

        /**
         * `grunt concat` concatenates multiple source files into a single file.
         */
        concat: {
            /**
             * The `build_css` target concatenates compiled CSS and vendor CSS
             * together.
             */
            build_css: {
                src: [
                    '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css'
                ],
                dest: '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css'
            },

            build_utils: {
                src: ['src/common/utils/utils.js'],
                dest: '<%= build_dir %>/src/app/utils.js'
            },

            build_js: {
                src: ['src/common/config/config.js', 'src/app/app.js','src/common/routes/routes.js',
                      'src/common/filters/filters.js',
                      'src/common/utils/core.js',
                      'src/common/utils/ie.js',                      
                      'src/common/directives/directive.js',
                      'src/app/home/home.controller.js',
                      'src/app/home/home.service.js',
                      'src/app/app.js',
                      // Header
                      'src/app/header/header.controller.js',
                      'src/app/header/logout.serivce.js',
                      		// Sign In
                      'src/app/signin/app.sign_in.js',
                      'src/app/signin/sign_in.controller.js',
                      		// Sing Up
                      'src/app/signup/mentor.sign_up.service.js',
                      'src/app/signup/signup.js',
                      'src/app/signup/signup.controller.js',
                      		// Notification
                      'src/app/notification/notification.controller.js',
                      'src/app/notification/notification.service.js',
                      // Forgot password
                      'src/app/forgotPassword/forgotPassword.controller.js',
                      // Change password
                      'src/app/changePassword/forgotPassword.controller.js',

                      'src/app/comment/comment.service.js',
                      'src/app/essay/allessay.controller.js',
                      'src/app/essay/essayDetail.controller.js',
                      'src/app/essay/essay.service.js',
                      'src/app/faq/faq.service.js',
                      'src/app/mentors/mentors.service.js',
                      'src/app/search/searchForum.controller.js',
                      'src/app/students/studentEdit.controller.js',
                      'src/app/students/student.service.js',
                      'src/app/question/answer.service.js',
                      'src/app/question/catagory.service.js',
                      'src/app/question/subject.service.js',
                      'src/app/video/video.service.js',
                      // Question
                      'src/app/question/question.service.js',
                      'src/app/question/question.controller.js',
                      'src/app/questionDetail/question.detail.controller.js',
                      'src/app/admission/admission.controller.js',
                      'src/app/admission/admission.service.js',
                      'src/app/uploadessay/uploadEssay.service.js',
                      'src/app/uploadessay/upload.essay.controller.js',
                      'src/app/contact/contactController.js',
                      'src/app/faq/faq.service.js',
                      'src/app/dashboard/dashboard.controller.js',
                      'src/app/dashboard/dashboard.services.js',
                      'src/app/sidebarRight/sidebarRight.services.js',
                      'src/app/sidebarRight/sidebarRight.controller.js',
                      'src/app/sidebarLeft/sidebarLeftMenu.controller.js',
                      'src/app/managerQA/managerQA.controller.js',
                      'src/app/managerQA/managerQA.service.js',
                      'src/app/managerQA/popupAnswer.controller.js',
                      'src/app/video/videoTutorialController.js', 
                      'src/app/question_detail.js',
                      'src/app/faqsController.js', 'src/app/mentorSignUpController.js',
                      'src/app/question/popupAskQuestion.controller.js',                    
                      'src/app/video.js', 'src/app/videoChatController.js', 'src/app/uploadEssayController.js',
                      //'src/app/admissionController.js',
                      'src/app/team/team.controller.js',
                      'src/app/team/team.service.js',
                      'src/app/video/videoDetail.controller.js',
                      'src/app/video/playlistDetail.controller.js',
                      'src/app/video/videoDetail.service.js',
                      'src/app/subCategoryVideo/subCategoryVideo.service.js',
                      'src/app/video/videoController.js',
                      'src/app/video/video.controller.js',
                      'src/app/video/VideoUploadController.js',
                      'src/app/video/VideoUpdateController.js',
                      // Playlist
                      'src/app/mentors/playlist/PlaylistController.js',
                      'src/app/playlist/AddPlaylistController.js',
                      'src/app/mentors/playlist/Playlist.service.js',
                      'src/app/playlist/updatePlaylistController.js',
                      'src/app/mentors/playlist/mentorPlaylistDetailController.js',
                      'src/app/mentors/playlist/chooseVideoController.js',
                      'src/app/mentors/playlist/addUpdatePlaylist.controller.js',
                      // Profile
                      'src/app/students/studentProfileController.js',
                      'src/app/metorProfile/mentorProfileController.js',
                      // Mentor Manage Video
                      'src/app/mentors/video/mentorVideoManageController.js',
                      'src/app/mentors/video/videoManagerController.js',
                      'src/app/mentors/video/choosePlaylistController.js',
                      'src/app/mentors/video/uploadTutorialController.js',
                      'src/app/mentors/video/mentorVideoDetailController.js',
                      // Leftside bar mentor
                      'src/app/sidebarLeft/sidebarLeftMenu.controller.js',
                      'src/app/admission/video.admission.service.js',
                      'src/app/admission/video.admission.controller.js',

                      // upload essay
                      'src/app/admission/uploadEssay.controller.js',
                      'src/app/admission/yourEssay.controller.js',
                      'src/app/admission/uploadEssay.service.js'
                      ],
                dest: '<%= build_dir %>/src/app/<%= pkg.name %>-<%= pkg.version %>.js'
            },
            /**
            build_js: {
                src: ['<%= app_files.js %>'],
                dest: '<%= build_dir %>/src/app/<%= pkg.name %>-<%= pkg.version %>.js'
            },
            */
            build_vendorjs: {
                src: ['vendor/Chart.min.js', 'vendor/angular-file-upload-shim.min.js', 'vendor/angular-file-upload.min.js',
                      'vendor/angular-resource', 'vendor/angular-route.js', 'vendor/angular.min.js', 'vendor/bootstrap-progressbar.js',
                      'vendor/bootstrap.min.js', 'vendor/jquery-1.11.0.min.js', 'vendor/jquery-1.11.1.js',
                      'vendor/jquery.mCustomScrollbar.concat.min.js', 'vendor/jquery.mask.min.js', 'vendor/jquery.maskedinput-1.2.2.js',
                      'vendor/jquery.selectbox-0.1.3.js', 'vendor/moment.min.js', 'vendor/nanoScroller.js',
                      'vendor/ng-infinite-scroll.min.js', 'vendor/ngProgress.js', 'vendor/ui-bootstrap-tpls-0.11.0.js'],
                dest: '<%= build_dir %>/vendor/vendor.js'
            },
            /**
            build_vendorjs: {
                src: ['<%= vendor_files.js %>'],
                dest: '<%= build_dir %>/vendor/vendor.js'
            },
            */
            /**
             * The `compile_js` target is the concatenation of our application source
             * code and all specified vendor source code into a single file.
             */
            compile_js: {
                options: {
                    banner: '<%= meta.banner %>'
                },
                src: [
                    '<%= vendor_files.js %>',
                    'module.prefix',
                    '<%= build_dir %>/src/**/*.js',
                    '<%= html2js.app.dest %>',
                    '<%= html2js.common.dest %>',
                    'module.suffix'
                ],
                dest: '<%= compile_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.js'
            }
        },
        'string-replace': {
            pro: {
                files: {
                    'build/': 'build/src/app/utils.js'
                },
                options: {
                    replacements: [{
                        pattern: '<%= webservice_url.variable_pattern %>',
                        replacement: '<%= webservice_url.pro %>'
                     }]
                }
            },
            dev: {
                files: {
                    'build/': 'build/src/app/utils.js'
                },
                options: {
                    replacements: [{
                        pattern: '<%= webservice_url.variable_pattern %>',
                        replacement: '<%= webservice_url.dev %>'
                     }]
                }
            }
        },


        /**
         * `grunt coffee` compiles the CoffeeScript sources. To work well with the
         * rest of the build, we have a separate compilation task for sources and
         * specs so they can go to different places. For example, we need the
         * sources to live with the rest of the copied JavaScript so we can include
         * it in the final build, but we don't want to include our specs there.
         */
        coffee: {
            source: {
                options: {
                    bare: true
                },
                expand: true,
                cwd: '.',
                src: [ '<%= app_files.coffee %>' ],
                dest: '<%= build_dir %>',
                ext: '.js'
            }
        },

        /**
         * `ngAnnotate` annotates the sources before minifying. That is, it allows us
         * to code without the array syntax.
         */
        ngAnnotate: {
            compile: {
                files: [
                    {
                        src: [ '<%= app_files.js %>' ],
                        cwd: '<%= build_dir %>',
                        dest: '<%= build_dir %>',
                        expand: true
                    }
                ]
            }
        },

        /**
         * Minify the sources!
         */
        uglify: {

            compile: {
                options: {
                    banner: '<%= meta.banner %>'
                },
                files: {
                    '<%= concat.compile_js.dest %>': '<%= concat.compile_js.dest %>'
                }
            }
        },

        /**
         * `grunt-contrib-less` handles our LESS compilation and uglification automatically.
         * Only our `main.less` file is included in compilation; all other files
         * must be imported from this file.
         */
        less: {
            build: {
                files: {
                    '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css': '<%= app_files.less %>'
                }
            },
            compile: {
                files: {
                    '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css': '<%= app_files.less %>'
                },
                options: {
                    cleancss: true,
                    compress: true
                }
            }
        },

        /**
         * `jshint` defines the rules of our linter as well as which files we
         * should check. This file, all javascript sources, and all our unit tests
         * are linted based on the policies listed in `options`. But we can also
         * specify exclusionary patterns by prefixing them with an exclamation
         * point (!); this is useful when code comes from a third party but is
         * nonetheless inside `src/`.
         */
        jshint: {
            src: [
                '<%= app_files.js %>'
            ],
            test: [
                '<%= app_files.jsunit %>'
            ],
            gruntfile: [
                'Gruntfile.js'
            ],
            options: {
                curly: true,
                newcap: true,
                noarg: true,
                sub: true,
                boss: true,
                eqnull: true
            },
            globals: {}
        },

        /**
         * HTML2JS is a Grunt plugin that takes all of your template files and
         * places them into JavaScript files as strings that are added to
         * AngularJS's template cache. This means that the templates too become
         * part of the initial payload as one JavaScript file. Neat!
         */
        html2js: {
            /**
             * These are the templates from `src/app`.
             */
            app: {
                options: {
                    base: 'src/app'
                },
                src: [ '<%= app_files.atpl %>' ],
                dest: '<%= build_dir %>/templates-app.js'
            },

            /**
             * These are the templates from `src/common`.
             */
            common: {
                options: {
                    base: 'src/common'
                },
                src: [ '<%= app_files.ctpl %>' ],
                dest: '<%= build_dir %>/templates-common.js'
            }
        },


        /**
         * The `index` task compiles the `index.html` file as a Grunt template. CSS
         * and JS files co-exist here but they get split apart later.
         */
        index: {

            /**
             * During development, we don't want to have wait for compilation,
             * concatenation, minification, etc. So to avoid these steps, we simply
             * add all script files directly to the `<head>` of `index.html`. The
             * `src` property contains the list of included files.
             */
            build: {
                dir: '<%= build_dir %>',
                src: [
                    // '<%= build_dir %>/vendor/*.js',
                    '<%= vendor_files.js %>',
                    '<%= build_dir %>/src/**/*.js',
                    '<%= html2js.common.dest %>',
                    '<%= html2js.app.dest %>',
                    '<%= vendor_files.css %>',
                    '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css'
                ]
            },

            /**
             * When it is time to have a completely compiled application, we can
             * alter the above to include only a single JavaScript and a single CSS
             * file. Now we're back!
             */
            compile: {
                dir: '<%= compile_dir %>',
                src: [
                    '<%= concat.compile_js.dest %>',
                    '<%= vendor_files.css %>',
                    '<%= build_dir %>/assets/<%= pkg.name %>-<%= pkg.version %>.css'
                ]
            }
        },


        /**
         * And for rapid development, we have a watch set up that checks to see if
         * any of the files listed below change, and then to execute the listed
         * tasks when they do. This just saves us from having to type "grunt" into
         * the command-line every time we want to see what we're working on; we can
         * instead just leave "grunt watch" running in a background terminal. Set it
         * and forget it, as Ron Popeil used to tell us.
         *
         * But we don't need the same thing to happen for all the files.
         */
        delta: {
            /**
             * By default, we want the Live Reload to work for all tasks; this is
             * overridden in some tasks (like this file) where browser resources are
             * unaffected. It runs by default on port 35729, which your browser
             * plugin should auto-detect.
             */
            options: {
                livereload: true
            },

            /**
             * When the Gruntfile changes, we just want to lint it. In fact, when
             * your Gruntfile changes, it will automatically be reloaded!
             */
            gruntfile: {
                files: 'Gruntfile.js',
                tasks: [ 'jshint:gruntfile' ],
                options: {
                    livereload: false
                }
            },

            /**
             * When our JavaScript source files change, we want to run lint them and
             * run our unit tests.
             */
            jssrc: {
                files: [
                    '<%= app_files.js %>'
                ],
                tasks: [
                    'jshint:src',
                    'copy:build_appjs',
                    'string-replace:dev'
                ]
            },

            /**
             * When assets are changed, copy them. Note that this will *not* copy new
             * files, so this is probably not very useful.
             */
            assets: {
                files: [
                    'src/assets/**/*'
                ],
                tasks: [ 'copy:build_app_assets', 'copy:build_vendor_assets' ]
            },
            /**
             * When templates are changed
             */
            templatesfiles : {
                files : [ 'src/app/**/*.html'],
                tasks : [ 'copy:build_apptemplate' ]
            },
            /**
             * When index.html changes, we need to compile it.
             */
            html: {
                files: [ '<%= app_files.html %>' ],
                tasks: [ 'index:build' ]
            },

            /**
             * When our templates change, we only rewrite the template cache.
             */
            tpls: {
                files: [
                    '<%= app_files.atpl %>',
                    '<%= app_files.ctpl %>'
                ],
                tasks: [ 'html2js' ]
            },

            /**
             * When the CSS files change, we need to compile and minify them.
             */
            less: {
                files: [ 'src/**/*.less',
                         'src/**/**/*.less',
                         'src/**/**/**/*.less' ],
                tasks: [ 'less:build' ]
            },
            messagesw: {
                files: [ 'src/firebase-messaging-sw.js' ],
                tasks: [ 'js:copy' ]
            },
            /**
             * When a JavaScript unit test file changes, we only want to lint it and
             * run the unit tests. We don't want to do any live reloading.
             */
            jsunit: {
                files: [
                    '<%= app_files.jsunit %>'
                ],
                tasks: [ 'jshint:test' ],
                options: {
                    livereload: false
                }
            }
        }
    };

    grunt.initConfig( grunt.util._.extend( taskConfig, userConfig ) );

    /**
     * In order to make it safe to just compile or copy *only* what was changed,
     * we need to ensure we are starting from a clean, fresh build. So we rename
     * the `watch` task to `delta` (that's why the configuration var above is
     * `delta`) and then add a new task called `watch` that does a clean build
     * before watching for changes.
     */
    grunt.renameTask( 'watch', 'delta' );
    grunt.registerTask( 'watch', [ 'build:pro', 'delta' ] );

    /**
     * The default task is to build and compile.
     */
    grunt.registerTask( 'default', [ 'build', 'compile' ] );

    /**
     * The `build` task gets your app ready to run for development and testing.
     */
    grunt.registerTask( 'build', function(arg1) {

        grunt.task.run([
            'clean', 'less:build', 'concat:build_css', 'concat:build_js', 'concat:build_utils',
            'copy:build_extends_files', 'copy:build_app_assets',
            'copy:build_vendor_assets', 'copy:build_vendorjs',
            'copy:build_vendorcss', 'copy:build_apptemplate', 'index:build','copy:firebase_messaging_sw'
        ]);

        if (arguments.length !== 0) {

            if(arg1 === 'pro') {
                grunt.task.run(['string-replace:pro']);
            } else {
                grunt.task.run(['string-replace:dev']);
            }

        }

    });

    /**
     * The `compile` task gets your app ready for deployment by concatenating and
     * minifying your code.
     */
    grunt.registerTask( 'compile', [
        'less:compile', 'copy:compile_assets', 'ngAnnotate', 'concat:compile_js', 'uglify', 'index:compile'
    ]);

    /**
     * A utility function to get all app JavaScript sources.
     */
    function filterForJS ( files ) {
        return files.filter( function ( file ) {
            return file.match( /\.js$/ );
        });
    }

    /**
     * A utility function to get all app CSS sources.
     */
    function filterForCSS ( files ) {
        return files.filter( function ( file ) {
            return file.match( /\.css$/ );
        });
    }

    /**
     * The index.html template includes the stylesheet and javascript sources
     * based on dynamic names calculated in this Gruntfile. This task assembles
     * the list into variables for the template to use and then runs the
     * compilation.
     */
    grunt.registerMultiTask( 'index', 'Process index.html template', function () {
        var dirRE = new RegExp( '^('+grunt.config('build_dir')+'|'+grunt.config('compile_dir')+')\/', 'g' );
        var jsFiles = filterForJS( this.filesSrc ).map( function ( file ) {
            return file.replace( dirRE, '' );
        });
        var cssFiles = filterForCSS( this.filesSrc ).map( function ( file ) {
            return file.replace( dirRE, '' );
        });

        grunt.file.copy('src/index.html', this.data.dir + '/index.html', {
            process: function ( contents, path ) {
                return grunt.template.process( contents, {
                    data: {
                        scripts: jsFiles,
                        styles: cssFiles,
                        version: grunt.config( 'pkg.version' )
                    }
                });
            }
        });
    });

};

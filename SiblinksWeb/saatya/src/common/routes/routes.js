brotApp.config(['$routeProvider', '$locationProvider', '$sceDelegateProvider',
	function($routeProvider, $locationProvider, $sceDelegateProvider) {
	// $locationProvider.tpl.html5Mode(true);
	// $locationProvider.tpl.html5Mode(true).hashPrefix('!');
	$routeProvider.
	when('/', {
		templateUrl: 'src/app/home/home.tpl.html',
		controller: 'HomeController'
	}).
	when('/dashboard', {
		templateUrl: 'src/app/dashboard/dashboard.tpl.html',
		controller : 'DashboardController'
	}).
	// when('/mentorProfile/:mentorId', {
	// 	templateUrl: 'src/app/mentorProfile.tpl.html',
	// 	controller : 'MentorProfileController'
	// }).
	when('/mentorProfile/:mentorId', {
		templateUrl: 'src/app/mentors/mentorProfile.tpl.html',
		controller : 'MentorProfileController'
	}).
	when('/signup', {
		templateUrl: 'src/app/signup/signUpEmail.tpl.html',
		controller : 'SignUpController'
	}).
	when('/signin', {
		templateUrl: 'src/app/signin/signIn.tpl.html',
		controller : 'SignIn'
	}).
	when('/signupcomplete', {
		templateUrl: 'src/app/signUpComplete.tpl.html',
		controller : 'SignUpCompleteController'
	}).
	// when('/studentProfile/:userid', {
	// 	templateUrl: 'src/app/student_profile.tpl.html',
	// 	controller: 'StudentProfileController'
	// }).
	when('/studentProfile', {
		templateUrl: 'src/app/students/student_profile.tpl.html',
		controller: 'StudentProfileController'
	}).
	when('/video_general', {
		templateUrl: 'src/app/video/video_general.tpl.html',
		controller: 'VideoController',
		reloadOnSearch: false
	}).
	when('/mentor_video', {
		templateUrl: 'src/app/mentors/mentor-manage-video.tpl.html',
		controller: 'MentorVideoManageController',
		reloadOnSearch: false
	}).
	when('/video_upload', {
		templateUrl: 'src/app/video/video_upload.tpl.html',
		controller: 'VideoUploadController',
		reloadOnSearch: false
	}).
	when('/videos', {
		templateUrl: 'src/app/video/videos.tpl.html',
		controller: 'VideoCtrl',
		reloadOnSearch: false
	}).
	when('/videos/all', {
		templateUrl: 'src/app/video/videos.tpl.html',
		controller: 'VideoCtrl',
		reloadOnSearch: false
	}).
//	when('/videos/history', {
//		templateUrl: 'src/app/video/video_tutorial.tpl.html',
//		controller: 'VideoTutorialController',
//		reloadOnSearch: false
//	}).
//	when('/videos/:subjectId', {
//		templateUrl: 'src/app/video/video_tutorial.tpl.html',
//		controller: 'VideoTutorialController'
//	}).
	when('/listVideo/:subjectId/:categoryId/:page', {
		templateUrl: 'src/app/video/subCategory.tpl.html',
		controller: 'ListVideoCtrl'
	}).
	when('/videos/detailVideo/:videoid', {
		templateUrl: 'src/app/video/video_detail.tpl.html',
		controller: 'VideoDetailCtrl'
	}).
	when('/videos/detailPlaylist/:playlistid/:index', {
		templateUrl: 'src/app/video/playlist_detail.tpl.html',
		controller: 'PlaylistDetailCtrl'
	}).
	when('/ask_a_question/:subjectid', {
		templateUrl: 'src/app/question/askQuestion.tpl.html',
		controller: 'QuestionController',
		reloadOnSearch: false
	}).
	when('/first-ask', {
		templateUrl: 'src/app/question/firstAsk.tpl.html',
		controller: 'QuestionController',
	}).
	when('/editStudent/:type', {
		templateUrl: 'src/app/editStudentProfile.tpl.html',
		controller: 'EditStudentController'
	}).
	when('/register_success', {
		templateUrl: 'src/app/register_success.tpl.html',
		controller: 'RegisterSuccessController'
	}).
	when('/contact', {
		templateUrl: 'src/app/contact/contact.tpl.html',
		controller: 'ContactController'
	}).
	when('/question_detail/:question_id', {
		templateUrl: 'src/app/questionDetail/questionDetail.tpl.html',
		controller: 'QuestionDetailCtrl'
	}).
	when('/video_chat', {
		templateUrl: 'src/app/video_chat_mentor.tpl.html',
		controller: 'VideoChatCtrl',
		reloadOnSearch: false
	}).
	when('/upload_essay', {
		templateUrl: 'src/app/uploadessay/upload_essay.tpl.html',
		controller: 'UploadEssayCtrl'
	}).
	when('/faqs', {
		templateUrl: 'src/app/faqs.tpl.html',
		controller: 'FaqsCtrl'
	}).
	// when('/searchVideo/:keyWord/:page', {
	// templateUrl: 'src/app/search_video.tpl.html',
	// controller: 'searchVideoCtrl'
	// }).
	when('/searchVideo/:keyWord/:page', {
		templateUrl: 'src/app/search_video.tpl.html',
		controller: 'searchAllVideoCtrl'
	}).
	when('/searchForum/:keyWord', {
		templateUrl: 'src/app/search/search_forum.tpl.html',
		controller: 'SearchForumCtrl'
	}).
	when('/forgotPassword', {
		templateUrl: 'src/app/changePassword/changePassword.tpl.html',
		controller: 'ChangePasswordCtrl'
	}).
	when('/policy', {
		templateUrl: 'src/app/policy.tpl.html',
		controller: 'PolicyCtrl'
	}).
	when('/notification', {
		templateUrl: 'src/app/notification.tpl.html',
		controller: 'NotificationCtrl'
	}).
	when('/college_admission', {
		templateUrl: 'src/app/admission/admission.tpl.html',
		controller: 'AdmissionCtrl'
	}).
	when('/college_admission/videoadmission/:idSubAdmission', {
		templateUrl: 'src/app/videoAdmission.tpl.html',
		controller: 'VideoAdmissionCtrl'
	}).
	when('/college_admission/article', {
		templateUrl: 'src/app/article.tpl.html',
		controller: 'ArticleCtrl'
	}).
 	 when('/college_admission/articledetail/:idArtilce', {
    templateUrl: 'src/app/admission/articleDetail.tpl.html',
    controller: 'ArticleDetailCtrl'
  	}).
	when('/college_admission/videoadmission/listvideoadmission/:idSubAdmission/:idTopic', {
		templateUrl: 'src/app/admission/listVideoAdmission.tpl.html',
		controller: 'ListVideoAdmissionCtrl'
	}).
	when('/college_admission/videoadmission/videodetail/:idSubAdmission/:idTopicSubAdmission/:vId', {
		templateUrl: 'src/app/admission/videoAdmissionDetail.tpl.html',
		controller: 'VideoAdmissionDetailCtrl'
	}).
	when('/team', {
		templateUrl: 'src/app/team/team.tpl.html',
		controller: 'TeamCtrl'
	}).
	when('/about', {
		templateUrl: 'src/app/about/about.tpl.html'
	}).
	when('/all_essay', {
		templateUrl: 'src/app/viewAllEssay.tpl.html',
		controller: 'AllEssayCtrl'
	}).
	when('/essay_detail/:essayId', {
		templateUrl: 'src/app/uploadEssayDetail.tpl.html',
		controller: 'EssayDetailCtrl'
	}).
	when('/mentor/managerQA', {
		templateUrl: 'src/app/managerQA/managerQA.tpl.html',
		controller: 'managerQAController'
	}).
	// when('/video/home', {
	// 	templateUrl: 'src/app/video/videos.tpl.html',
	// 	controller: 'VideoCtrl'
	// }).
	when('/forgotPwd', {
		templateUrl: 'src/app/forgotPassword/forgotpassword.tpl.html',
		controller: 'ForgotPassword'
	}).
	when('/playlist', {
		templateUrl: 'src/app/playlist/playlist.tpl.html',
		controller: 'PlaylistController'
	}).
	when('/mentor', {
		templateUrl: 'src/app/dashboard/dashboard.tpl.html',
		controller: 'HomeController'
	}).
	otherwise({
        redirectTo: '/'
    });
}]);
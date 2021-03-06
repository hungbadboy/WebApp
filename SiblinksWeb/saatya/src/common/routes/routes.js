brotApp.config(['$routeProvider', '$locationProvider', '$sceDelegateProvider',
	function($routeProvider, $locationProvider, $sceDelegateProvider) {
	// $locationProvider.tpl.html5Mode(true);
	// $locationProvider.tpl.html5Mode(true).hashPrefix('!');
	$routeProvider.
	when('/', {
		templateUrl: 'src/app/home/home.tpl.html',
		controller: 'HomeController'
	}).
	//Profile
	when('/student/mentorProfile/:mentorId', {
		templateUrl: 'src/app/students/studentMentorProfile.tpl.html',
		controller : 'StudentProfileController'
	}).
	when('/student/studentProfile', {
		templateUrl: 'src/app/students/student_profile.tpl.html',
		controller: 'StudentProfileController'
	}).
	when('/mentor/mentorProfile/:mentorId', {
		templateUrl: 'src/app/mentorProfile/mentorMentorProfile.tpl.html',
		controller: 'MentorProfileController'
	}).
	when('/mentor/mentorProfile', {
		templateUrl: 'src/app/mentorProfile/mentorProfile.tpl.html',
		controller: 'MentorProfileController'
	}).
	when('/mentor/studentProfile/:studentId', {
		templateUrl: 'src/app/mentorProfile/mentorStudentProfile.tpl.html',
		controller: 'MentorProfileController'
	}).
	// End Profile
	when('/signup', {
		templateUrl: 'src/app/signup/signUpEmail.tpl.html',
		controller : 'SignUpController'
	}).
	when('/student/signin', {
		templateUrl: 'src/app/signin/signInStudent.tpl.html',
		controller : 'SignInCtrl'
	}).
	when('/mentor/signin', {
		templateUrl: 'src/app/signin/signInMentor.tpl.html',
		controller : 'SignInCtrl'
	}).
	when('/signupcomplete', {
		templateUrl: 'src/app/signUpComplete.tpl.html',
		controller : 'SignUpCompleteController'
	}).
	when('/mentor/mentorVideo', {
		templateUrl: 'src/app/mentors/video/mentor-manage-video.tpl.html',
		controller: 'MentorVideoManageController',
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
	when('/listVideo/:subjectId/:categoryId/:page', {
		templateUrl: 'src/app/video/subCategory.tpl.html',
		controller: 'ListVideoCtrl'
	}).
	when('/videos/detailVideo/:videoid', {
		templateUrl: 'src/app/video/video_detail.tpl.html',
		controller: 'VideoDetailCtrl',
		reloadOnSearch: false
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
	when('/contact', {
		templateUrl: 'src/app/contact/contact.tpl.html',
		controller: 'ContactCtrl'
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
	when('/searchVideo/:keyWord/:page', {
		templateUrl: 'src/app/search_video.tpl.html',
		controller: 'searchAllVideoCtrl'
	}).
	when('/searchForum/:keyWord', {
		templateUrl: 'src/app/search/search_forum.tpl.html',
		controller: 'SearchForumCtrl'
	}).
	when('/studentForgotPassword', {
		templateUrl: 'src/app/changePassword/studentChangePassword.tpl.html',
		controller: 'ChangePasswordCtrl'
	}).
	when('/mentor/mentorForgotPassword', {
		templateUrl: 'src/app/changePassword/mentorChangePassword.tpl.html',
		controller: 'ChangePasswordCtrl'
	}).
	when('/policy', {
		templateUrl: 'src/app/policy/policy.tpl.html'
	}).
    when('/terms', {
        templateUrl: 'src/app/term/term_us.tpl.html'
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
		controller: 'managerQAController',
		reloadOnSearch: false
	}).
	when('/forgotPwd', {
		templateUrl: 'src/app/forgotPassword/forgotpassword.tpl.html',
		controller: 'ForgotPassword'
	}).
	when('/mentor/playlistManager', {
		templateUrl: 'src/app/mentors/playlist/manage_playlist.tpl.html',
		controller: 'PlaylistController'
	}).
	when('/mentor/essay', {
		templateUrl: 'src/app/essay/mentorEssay.tpl.html',
		controller: 'AllEssayCtrl'
	}).
	
	when('/mentor/dashboard', {
		templateUrl: 'src/app/dashboard/dashboard.tpl.html',
		controller: 'DashboardController'
	}).
	// Videos Mentor
	when('/mentor/videoManager', {
		templateUrl: 'src/app/mentors/video/managevideo-videolist.tpl.html',
		controller: 'VideoManagerController'
	}).
	when('/mentor/video/detail/:vid', {
		templateUrl: 'src/app/mentors/video/mentor-video-detail.tpl.html',
		controller: 'MentorVideoDetailController'
	}).
	when('/mentor/playlist/detail/:plid', {
		templateUrl: 'src/app/mentors/playlist/mentor-playlist-detail.tpl.html',
		controller: 'MentorPlaylistDetailController',
		reloadOnSearch: false
	}).
	when('/mentor/video/detail/:vid/:plid', {
		templateUrl: 'src/app/mentors/video/mentor-video-detail.tpl.html',
		controller: 'MentorVideoDetailController'
	}).
	when('/mentor/video/view/detail/:authorId/:vid', {
		templateUrl: 'src/app/mentors/video/mentor-video-detail-video-only.tpl.html',
		controller: 'MentorVideoDetailViewOnlyController'
	}).
	when('/mentor/video/view/detail/:authorId/:vid/:plid', {
		templateUrl: 'src/app/mentors/video/mentor-video-detail-video-only.tpl.html',
		controller: 'MentorVideoDetailViewOnlyController'
	}).
	when('/video_admission/:videoid', {
		templateUrl: 'src/app/admission/video-admission.tpl.html',
		controller: 'VideoAdmissionController'
	}).

	otherwise({
        redirectTo: '/'
    });
}]);

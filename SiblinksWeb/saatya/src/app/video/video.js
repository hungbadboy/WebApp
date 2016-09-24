jQuery(document).ready(function($) {
	checkBoxSearchClick();
	clickCloseStepWindown();
	checkBoxSignUpComplete();
	// run js sroll bar
    // $(".croll_bar_01").mCustomScrollbar();
    // $(".croll_bar_02").mCustomScrollbar();
    // $(".croll_bar_03").mCustomScrollbar();
});

//Author: Nhut Nguyen;
//Click checkbox;
function checkBoxSearchClick() {
	var isClickAll = 0;
	var isClickShool = 0;
	var isClickName = 0;
	var isClickExtra = 0;
	var isClickMajor = 0;
	var isClickTutor = 0;
	
	$('#checkAll').click(function() {
		if(isClickAll === 0 ) {
			$('#imgCheckAll').show();
			isClickAll = 1;
		} else {
			$('#imgCheckAll').hide();
			isClickAll = 0;
		}
	});

	$('#checkShool').click(function() {
		if(isClickShool === 0 ) {
			$('#imgCheckShool').show();
			isClickShool = 1;
		} else {
			$('#imgCheckShool').hide();
			isClickShool = 0;
		}
	});

	$('#checkName').click(function() {
		if(isClickName === 0 ) {
			$('#imgCheckName').show();
			isClickName = 1;
		} else {
			$('#imgCheckName').hide();
			isClickName = 0;
		}
	});

	$('#checkMajor').click(function() {
		if(isClickMajor === 0 ) {
			$('#imgCheckMajor').show();
			isClickMajor = 1;
		} else {
			$('#imgCheckMajor').hide();
			isClickMajor = 0;
		}
	});

	$('#checkExtra').click(function() {
		if(isClickExtra === 0 ) {
			$('#imgCheckExtra').show();
			isClickExtra = 1;
		} else {
			$('#imgCheckExtra').hide();
			isClickExtra = 0;
		}
	});

	$('#checkTutor').click(function() {
		if(isClickTutor === 0 ) {
			$('#imgCheckTutor').show();
			isClickTutor = 1;
		} else {
			$('#imgCheckTutor').hide();
			isClickTutor = 0;
		}
	});
}

//Author: Nhut Nguyen;
//click close button;
function clickCloseStepWindown() {
	$('#btnCloseStepWindown').click(function() {
		$('.tutorial').hide();
	});
}

//Author: Nhut Nguyen;
//click check box at sigup complete;
function checkBoxSignUpComplete() {
	var isArchitecture = 0;
	var isDivinity = 0;
	var isEnglish = 0;
	var isHistory = 0;
	var isMedicine = 0;
	var isSciences = 0;

	var isActing = 0;
	var isReading = 0;
	var isRunning = 0;
	var isSoccer = 0;
	var isTennis = 0;
	var isFootball = 0;

	var isPersonal = 0;
	var isAlgebra = 0;
	var isBiology = 0;

	//Potential College Major*
	$('#clickArchitecture').click(function() {
		if(isArchitecture === 0 ) {
			$('#imgArchitecture').show();
			isArchitecture = 1;
		} else {
			$('#imgArchitecture').hide();
			isArchitecture = 0;
		}
	});

	$('#clickDivinity').click(function() {
		if(isDivinity === 0 ) {
			$('#imgDivinity').show();
			isDivinity = 1;
		} else {
			$('#imgDivinity').hide();
			isDivinity = 0;
		}
	});

	$('#clickEnglish').click(function() {
		if(isEnglish === 0 ) {
			$('#imgEnglish').show();
			isEnglish = 1;
		} else {
			$('#imgEnglish').hide();
			isEnglish = 0;
		}
	});

	$('#clickHistory').click(function() {
		if(isHistory === 0 ) {
			$('#imgHistory').show();
			isHistory = 1;
		} else {
			$('#imgHistory').hide();
			isHistory = 0;
		}
	});

	$('#clickMedicine').click(function() {
		if(isMedicine === 0 ) {
			$('#imgMedicine').show();
			isMedicine = 1;
		} else {
			$('#imgMedicine').hide();
			isMedicine = 0;
		}
	});

	$('#clickSciences').click(function() {
		if(isSciences === 0 ) {
			$('#imgSciences').show();
			isSciences = 1;
		} else {
			$('#imgSciences').hide();
			isSciences = 0;
		}
	});

	//click Extracurricular Activities
	$('#clickActing').click(function() {
		if(isActing === 0 ) {
			$('#imgActing').show();
			isActing = 1;
		} else {
			$('#imgActing').hide();
			isActing = 0;
		}
	});

	$('#clickReading').click(function() {
		if(isReading === 0 ) {
			$('#imgReading').show();
			isReading = 1;
		} else {
			$('#imgReading').hide();
			isReading = 0;
		}
	});

	$('#clickRunning').click(function() {
		if(isRunning === 0 ) {
			$('#imgRunning').show();
			isRunning = 1;
		} else {
			$('#imgRunning').hide();
			isRunning = 0;
		}
	});

	$('#clickSoccer').click(function() {
		if(isSoccer === 0 ) {
			$('#imgSoccer').show();
			isSoccer = 1;
		} else {
			$('#imgSoccer').hide();
			isSoccer = 0;
		}
	});

	$('#clickTennis').click(function() {
		if(isTennis === 0 ) {
			$('#imgTennis').show();
			isTennis = 1;
		} else {
			$('#imgTennis').hide();
			isTennis = 0;
		}
	});

	$('#clickFootball').click(function() {
		if(isFootball === 0 ) {
			$('#imgFootball').show();
			isFootball = 1;
		} else {
			$('#imgFootball').hide();
			isFootball = 0;
		}
	});

	//click Want help in;
	$('#clickPersonal').click(function() {
		if(isPersonal === 0 ) {
			$('#imgPersonal').show();
			isPersonal = 1;
		} else {
			$('#imgPersonal').hide();
			isPersonal = 0;
		}
	});

	$('#clickAlgebra').click(function() {
		if(isAlgebra === 0 ) {
			$('#imgAlgebra').show();
			isAlgebra = 1;
		} else {
			$('#imgAlgebra').hide();
			isAlgebra = 0;
		}
	});

	$('#clickBiology').click(function() {
		if(isBiology === 0 ) {
			$('#imgBiology').show();
			isBiology = 1;
		} else {
			$('#imgBiology').hide();
			isBiology = 0;
		}
	});
}
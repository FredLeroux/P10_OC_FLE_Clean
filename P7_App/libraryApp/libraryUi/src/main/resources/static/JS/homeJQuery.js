/**
 *
 */

function homeJQueryElmt(){
	$('.homeLink').button();
	//libraryAppUiButtonCss('.homeLink');
}


function setHomeIframeHeight(){
	setIframeHeight('homeIframe');
}

function changeSrcIframeOnClick(elmtId,src){
	$(jqueryName(elmtId)).click(function(){
		setIframeSrc('homeIframe',src);
	});
}
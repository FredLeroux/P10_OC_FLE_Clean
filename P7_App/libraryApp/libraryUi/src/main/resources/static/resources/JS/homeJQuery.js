/**
 *
 */




function setHomeIframeHeight(){
	setIframeHeight('homeIframe');
}

function changeSrcIframeOnClick(elmtId,src){
	$(jqueryName(elmtId)).click(function(){
		setIframeSrc('homeIframe',src);
	});
}
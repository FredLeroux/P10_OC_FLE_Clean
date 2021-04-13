/**
 *
 */




function setReservationsListIframeHeight(){
	setIframeHeight('reservationsIframe');
}

function changeSrcIframeOnClick(elmtId,src){
	$(jqueryName(elmtId)).click(function(){
		setIframeSrc('reservationsIframe',src);
	});
}
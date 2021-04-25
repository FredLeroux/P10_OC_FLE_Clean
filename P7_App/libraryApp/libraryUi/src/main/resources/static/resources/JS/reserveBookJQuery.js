/**
 *
 */

function reserveBook(bookparmaName,inputId){
	bookId = $('#'+inputId).val();
	ajaxReserveBookModal('/reserveBook',bookparmaName,bookId)
}

function ajaxReserveBookModal(callFilter,bookParamName, bookId) {
	$.post(buildInUrlSimpleParameter(callFilter,bookParamName,bookId)).done(function(data) {
		if(!data){
		 $(jqueryName('reserveBookErrorModal')).show();}
	else{
		$(jqueryName('reserveBookModal')).show();
	}

	})
}
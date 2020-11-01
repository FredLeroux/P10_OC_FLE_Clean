/**
 *
 */

function datePickerSetting(selector,list){
	$( selector ).datepicker({
	"datepicker" :	$.datepicker.regional[ "fr" ],
	"beforeShowDay": function(date){
		if(date.setHours(0,0,0) === datum.setHours(0,0,0) ){
			// for calculation date difference	alert((Math.floor(parseInt(today.setHours(0,0,0)-datum.setHours(0,0,0))/(1000*60*60*24))));

			return [true, 'css-class-to-highlight', 'retouner \n le seigneur de s anneaux les deux tours'];

			}
		return [true, '', ''];
		},

	});
	//highlightDateToolTip('.css-class-to-highlight');
	 $( "#date" ).datepicker().addClass("body-font");


}
/**
Date format expected yyyy-mm-dd */
function createDateFromString(date){
	let array = splitDateString(date);
	let year = array[0];
	/* as JavaScript date count month fron 0 to 11 in order to get the right month, minus month by 1
	example for a date 2020-10-18 w/o -1 return november instead of october*/
	let month = array[1]-1
	let day = array[2];
	return new Date(year,month,day);
}

function creteDate(year,month,day){
	return new Date(year,month,day);
}


function splitDateString(date){
	let strArray = date.split("-");
	return strArray;
}


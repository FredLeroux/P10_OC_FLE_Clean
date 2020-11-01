/**
 *
 */

function localDateFormater(date,baseName,lang){
	$.getScript(baseName.concat("_".concat(lang).concat(".js")))
	.done(function(){
		var day = date.getDay();
		var dayName = days[day];
		var month = date.getMonth();
		var monthName = months[month];
		localDateFormat(dayName,date.getDate(),monthName,date.getFullYear()) ;
	})
	.fail(function(){
		alert("nnnnnull");
	});

}
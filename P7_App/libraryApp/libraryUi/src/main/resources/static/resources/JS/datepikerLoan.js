/**
 *
 */

function datePickerSetting(selector, list) {
	$(selector).datepicker({
		"datepicker": $.datepicker.regional["fr"],
		"beforeShowDay": function(date) {
			var datesList = loanReturnDatesArray(list);
			if (dateIsToday(date)&& !isTodayInList(datesList)) {
				return [true, 'date-highlight-today', 'aujourd\'hui'];
			}
			if (list != null) {
				let i = indexOfLoanList(date,datesList);
				if (i != -1) {
					// for calculation date difference	alert((Math.floor(parseInt(today.setHours(0,0,0)-datum.setHours(0,0,0))/(1000*60*60*24))));
					let daysLeft = list[i]['daysLeft'];
					if (daysLeft > 5) {
						//alert("here  "+$(this).attr("data-year"));
						return [true, 'date-highlight-ok',
						 'Dans '+daysLeft+' jours(s) retouner\n' + list[i]["booksTitle"]];
					}
					else if (daysLeft > 0 && daysLeft <= 5) {
						return [true, 'date-highlight-warn',
						 'Dans '+daysLeft+' jours(s) retouner\n' + list[i]["booksTitle"]];
					} else if (dateIsToday(date)&& daysLeft ==0) {
						return [true, 'date-highlight-today-alert', 'aujourd\'hui retouner\n' + list[i]["booksTitle"]];
					}


					else if (daysLeft < 0) {
						return [true, 'date-highlight-alert',
						 'Retard de ' + Math.abs(daysLeft) + ' jour(s) pour \n' + list[i]["booksTitle"]];
					}


				}
			}
			return [true, 'date-highlight-neutral', ''];
		}

	});
	//highlightDateToolTip('.css-class-to-highlight');
	$(selector).datepicker().addClass("body-font");


}

function indexOfLoanList(date, toDateStringsList) {
	return toDateStringsList.indexOf(date.toDateString());
}

function isTodayInList(toDateStringsList){
	let today = new Date();
	return toDateStringsList.includes(today.toDateString());
}


function dateIsToday(date) {
	let today = new Date();
	return date.toDateString().includes(today.toDateString());


}


function loanReturnDatesArray(loanList) {
	var array = [];
	$.each(loanList, function(index) {
		var object = loanList[index];
		/**
		month -1 as per date month are set from 0-11 instead of 1-12 */
		var date = new Date(object["year"], object["month"] - 1, object["day"]).toDateString();
		array.push(date);
	})
	return array;
}



/**
Date format expected yyyy-mm-dd */
function createDateFromString(date) {
	let array = splitDateString(date);
	let year = array[0];
	/* as JavaScript date count month fron 0 to 11 in order to get the right month, minus month by 1
	example for a date 2020-10-18 w/o -1 return november instead of october*/
	let month = array[1] - 1
	let day = array[2];
	return new Date(year, month, day);
}

function creteDate(year, month, day) {
	return new Date(year, month, day);
}


function splitDateString(date) {
	let strArray = date.split("-");
	return strArray;
}


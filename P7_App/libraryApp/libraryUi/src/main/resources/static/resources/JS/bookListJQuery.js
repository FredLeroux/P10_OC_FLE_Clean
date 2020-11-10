/** needs jQUeryTools file to be imported before in html */

/**
Using JqueryUi Selectmenu function will apply function on select menu change
this is the way to use JQueryUi selecmenu()
see https://jqueryui.com/selectmenu/#product-selection
 */
function bookListJQueryElmt() {
	$("#buildingSelect").selectmenu({
		"change": function() {
			storeBuildingChoice(this.value)
			filterBooksList();
		}
	});


}


function filterBooksList() {
	var building = getSessionStoredValue("libraryBuilding");
	var kinds = getSessionStoredArray("kinds");
	if (building == null) {
		building = 0;
	}
	var newURL = buildInUrlSimpleParameter('/booksListFiltered', "libraryBuilding", building);
	newURL = buildInUrlArrayParameter(newURL, "kinds", kinds);
	ajaxCallFilterAndUpdateIframe('/bookListFiltering', 'booksListIframe', newURL)
}

function initFilterOnload() {
	var building = getSessionStoredValue("libraryBuilding");
	var kinds = getSessionStoredArray("kinds");
	var bool1 = building != null;
	var bool2 = kinds.length != 0;
	if (building != null) {
		selectValue('#buildingSelect', building);
	}
	if (bool2) {
		$.each(kinds, function(index, value) {
			/** index not used however allow to use value  */
			$('#' + value).prop('checked', true);
			$('#' + value).button("refresh");
		})
	}
	if (bool1 || bool2) {
		filterBooksList()
	}
}

function toggleDisplayClearKindFilterButton() {
	var array = getSessionStoredArray("kinds");
	let buttonId = "#clearKindsFilter"
	if (array.length != 0) {
		$(buttonId).show();
	} else {
		$(buttonId).hide();
	}
}

function clearKindsFilter(loc, checkBoxclassName) {
	uncheckCheckBox(loc, checkBoxclassName)
	var array = [];
	sessionStoreArray('kinds', array);
}

function storeBuildingChoice(value) {
	setSessionStoredValue("libraryBuilding", value);
}

function storeKindsChoice(elmt, value) {
	setkindsArrayOnchecked(elmt, 'kinds', value);
}

function setkindsArrayOnchecked(elmt, arrayName, value) {
	var kindsArray = getSessionStoredArray(arrayName);
	if ($(elmt).prop("checked") == true) {
		kindsArray.push(value);
	} else {
		var index = kindsArray.indexOf(value);
		kindsArray.splice(index, 1);
	}
	sessionStoreArray(arrayName, kindsArray);
}

function kindOnClickAction() {
	$(".kind").click(function() {
		storeKindsChoice(this, this.value);
		filterBooksList('/bookListFiltering', '/booksListFiltered', 'booksListIframe');
		toggleDisplayClearKindFilterButton("clearKindsFilter");
	});
}

function clearButtonOnClickAction() {
	$("#clearKindsFilter").click(function() {
		clearKindsFilter('check', 'kind');
		toggleDisplayClearKindFilterButton("clearKindsFilter");
		filterBooksList();
	});
}

function setBookTableIframe(){
	setIframeHeight('booksListIframe');
}




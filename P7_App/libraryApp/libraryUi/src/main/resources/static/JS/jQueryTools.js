function setSessionStoredValue(name, value) {
	sessionStorage.setItem(name, value);
}

function getSessionStoredValue(name) {
	return sessionStorage.getItem(name);
}



/** set iframe height function of content height + 100 px */
function setIframeHeight(iFrameName){
    $(jqueryName(iFrameName)).height($(jqueryName(iFrameName)).contents().height()+100+'px');
}

function setIframeSrc(iFrameName,src){
	 $(jqueryName(iFrameName)).attr("src",src);
}


function ajaxCallFilterAndUpdateIframe(callFilter, iFrameName, srcURL) {
	$.get(callFilter).done(function() {
		$('#'+iFrameName).get(0).contentWindow.location.replace(srcURL);
		//document.getElementById(iFrameName).contentWindow.location.replace(srcURL);
	})
}

function buildInUrlSimpleParameter(url, parameterName, value) {
	return addParameterNameToUrl(url, parameterName).concat(value);
}
/**
set an array to url parameter
i.e. for an array containing [a,b,c]
add to url the parameter as follow "?||&[paramaterName]=[a,b,c]"
 */
function buildInUrlArrayParameter(url, parameterName, array) {
	var urlWithArray = addParameterNameToUrl(url, parameterName);
	return urlWithArray.concat(array.join(','));
}



/**
add to url  "?||&[paramaterName=]"
 */
function addParameterNameToUrl(url, parameterName) {
	return url.concat(interrogationMarkOrAndMark(url).concat(parameterName).concat("="));
}

function interrogationMarkOrAndMark(url) {
	if (url.includes("?")) {
		return "&";
	}
	else {
		return "?";
	}
}




function uncheckCheckBox(loc, checkBoxclassName) {
	$("#" + loc).find('.' + checkBoxclassName).each(function() {
		var checkbox = $(this);
		checkbox.prop('checked', false);
		checkbox.button("refresh");
	});
}

function createArrayAndFillValue(arrayName, value) {
	var array = getSessionStoredArray(arrayName);
	array.push(value);
	sessionStoreArray(arrayName, array);
}

function getSessionStoredArray(arrayName) {
	var rawArray = getSessionStoredValue(arrayName);
	var array = [];
	if (rawArray !== null) {
		array = JSON.parse(rawArray);
	}
	return array
}

function sessionStoreArray(name, array) {
	setSessionStoredValue(name, JSON.stringify(array));

}

function selectValue(selector, value) {
	$(selector).val(value).selectmenu("refresh");
}

function jqueryName(elmtName){
	return "#".concat(elmtName);
}




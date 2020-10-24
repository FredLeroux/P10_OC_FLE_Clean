/**
Needs JQueryUi to be setted
 */


/**
Will set selector as JQuery Ui button and if a cssClass is present will apply this one to it.
 */
function uiButton(selector, cssClass) {
	let elmt = $(selector);
	elmt.button();
	if (cssClass != null) {
		elmt.addClass(cssClass);
	};
}

/**
Will set selector as JQuery Ui checkbox radio and if a cssClass is present will this one apply to it.
Note: if selector is "class", i.e. using ".classAttrName" then function
will iterate through all element with ".class" attribut

 */
function uiCheckBoxRadio(selector, cssClass) {
	let elmt = $(selector);
	elmt.checkboxradio();
	if (cssClass != null) {
		if (selector.includes(".")) {
			elmt.each(function() {
				$(this).checkboxradio("widget").addClass(cssClass);
			});
		} else {
			elmt.checkboxradio("widget").addClass(cssClass);
		}
	};

}

/**
Will set selector as JQuery Ui SelectMenu,
if cssClass and/or width are present and/or not null will apply these one's to it .
In order to use width param w/o cssClass put "null" for cssClass:
	uiSelectMenu("selector",null,"size");
 */
function uiSelectMenu(selector, cssClass, widthParam) {
	let elmt = $(selector);
	elmt.selectmenu();
	if (cssClass != null) {
		elmt.selectmenu("widget").addClass(cssClass);
		elmt.selectmenu("menuWidget").addClass(cssClass);
	};
	if (widthParam != null) {
		elmt.selectmenu({ "width": widthParam });
	}
}

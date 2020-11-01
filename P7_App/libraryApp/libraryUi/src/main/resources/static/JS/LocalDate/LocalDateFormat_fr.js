/**
 *
 */
const months = [
	"janvier",
	"février",
	"mars",
	"avril",
	"mai",
	"juin",
	"juillet",
	"août",
	"septembre",
	"octobre",
	"novembre",
	"décembre"
]

const monthsShort = [
	"janv.",
	"févr.",
	"mars",
	"avr.",
	"mai",
	"juin",
	"juil.",
	"août",
	"sept.",
	"oct.",
	"nov.",
	"déc."
]
/**
note: days list beguns by "sunday" as per javascript Date "sunday"=0 for .getDays() function
 */
const days = [
	"dimanche",
	"lundi",
	"mardi",
	"mercredi",
	"jeudi",
	"vendredi",
	"samedi"
]

const daysShort = [
	"dim.",
	"lun.",
	"mar.",
	"mer.",
	"jeu.",
	"ven.",
	"sam."
]

function localDateFormat(day,date,month,year){
	alert(day.concat(" ").concat(" ".concat(date)).concat(" ".concat(month)).concat(" ".concat(year)));
	return "yo";
}


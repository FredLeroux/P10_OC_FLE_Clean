/** to be use in association with jquery plug-in  datatable  ""https://datatables.net/"
example using thymeleaf
<script
	th:src="@{//cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js}"></script>
<script
	th:src="@{//cdn.datatables.net/1.10.22/js/dataTables.jqueryui.min.js}"></script>
"*/


function bookListTable() {
	$("#booksList").DataTable({
		language: {
			"url": "/resources/JQuery/JQueryDataTable/i18n/French/booksTable_fr.json"
		},
		columns: [
			{ "searchable": false },
			null,
			null,
			{ "searchable": false },
			{ "searchable": false }
		],
		fnInitComplete: function() {
			$('#booksList').css({ opacity: 1 });
		},
		columnDefs: [
			{ targets: [0, 1, 2, 3], className: 'dt-left' },
			{ targets: 4, className: 'dt-center' }
		]
	});

}

function triggerAdjustIFrameHeight() {
	$('#booksList').on('draw.dt', function() {
		window.top.window.setBookTableIframe();
	});
}
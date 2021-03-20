$(document).ready(function () {	
	
	$('#startDate').prop("min",new Date().toISOString().split("T")[0]);
	$('#endDate').prop("min",new Date().toISOString().split("T")[0]);
	
	
});
checkUserRole("ROLE_SUBJECT");
var subjectId = getUserIdFromToken();
$(document).ready(function () {	
	
	$.ajax({
		type:"GET", 
		url: "/api/certificate/" + subjectId + "/subject",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(certificates){
			$('#certificates').empty();
			for (let c of certificates){
				addRowInTable(c);
			}
		},
		error:function(){
			console.log('error getting certificates');
		}
	});
	
});

function addRowInTable(c){
	
	let btnDownload = '<button class="btn btn-info" type="button" id="' + c.id +'" onclick="download(this.id)" style="margin-top:-10px;">Download</button>';
	
	let row = $('<tr><td>' + c.startDate + '</td><td>' + c.endDate + '</td>' + 
				'<td>' + c.isCA + '</td><td>' + c.isValid + '</td><td>' + c.isRevoked + '</td><td>' + btnDownload +
				'</tr>');
				
	$('#certificates').append(row);
};


function download(certificateId){
	$.ajax({
		type:"GET", 
		url: "/api/certificate/download/" + certificateId,
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(){
			alert('success');
		},
		error:function(){
			console.log('error');
		}
	});

}
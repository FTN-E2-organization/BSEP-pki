var subjectId = 2; //promijeniti kada se uradi logovanje
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
	
	let btnDownload = '<button class="btn btn-info" type="button" id="' + c.id +'" onclick="download(this.id)" style="margin-top:-10px; margin-left:-20px; margin-right:-20px;">Download</button>';
	let btnCheckValidity = '<button class="btn btn-info" type="button" id="' + c.id +'" onclick="checkValidity(this.id)" style="margin-top:-10px;  margin-left:-20px; margin-right:-20px;">Validity</button>';
	let btnIssuer = '<button class="btn btn-info" type="button" id="' + c.id +'" onclick="getIssuer(this.id)" style="margin-top:-10px;  margin-left:-20px; margin-right:-20px;">Issuer</button>';
	
	let row = $('<tr><td>' + c.startDate + '</td><td>' + c.endDate + '</td>' + 
				'<td>' + c.isCA + '</td><td>' + c.isRevoked + '</td><td>' + btnCheckValidity + '</td>' +
				'<td>' + btnIssuer + '</td><td>' + btnDownload + '</td>' +
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
			let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Successfully downloaded.'
			+'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		},
		error:function(){
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Unsuccessfully downloaded.'
			+'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}
	});
}

function checkValidity(certificateId){
	
};

function btnIssuer(certificateId){
	
};
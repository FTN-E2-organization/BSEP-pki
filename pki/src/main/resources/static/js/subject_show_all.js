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
	
	let btnDownload = '<button class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="download(this.id)">Download</button>';
	let btncheckValidation = '<button data-toggle="modal" data-target="#centralModalCheckValidation" class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="checkValidation(this.id)" >Validity</button>';
	let btnIssuer = '<button data-toggle="modal" data-target="#centralModalViewIssuer" class="btn btn-info btn-sm" type="button" id="' + c.issuerId +'" onclick="getIssuer(this.id)">Issuer</button>';
	
	let row = $('<tr><td>' + c.organization + '</td><td>' + c.organizationUnit + '</td>' + 
				'<td>' + c.commonName + '</td><td>' + c.givenName + '</td>' + 
				'<td>' + c.surname + '</td><td>' + c.email + '</td>' + 
				'<td>' + c.countryCode + '</td><td>' + c.state + '</td>' + 
				'<td>' + c.locality + '</td><td>' + c.startDate + '</td>' + 
				'<td>' + c.endDate + '</td><td>' + c.isCA + '</td>' + 
				'<td>' + c.isRevoked + '</td><td>' + btncheckValidation + '</td>' +
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

function checkValidation(certificateId){
	$.ajax({
		type:"GET", 
		url: "/api/certificate/" + certificateId + "/valid",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(isValid){
			if(isValid == true){
				document.getElementById('idCheckValidation').innerHTML = '<h5 style="color:green;">The certificate is valid!</h5>';
			}else{
				document.getElementById('idCheckValidation').innerHTML = '<h5 style="color:red;">The certificate is not valid!</h5>';
			}
		},
		error:function(){
			console.log('error getting certificate validation');
		}
	});
};

function getIssuer(issuerId){
	
	$('#div_info').empty();
	$.ajax({
		type:"GET", 
		url: "/api/certificate/" + issuerId,
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(certificate){
			let table_info =  '<table style="margin-top:30px; margin-bottom:30px;">'
						+ '<tr> <td>Organization:</td><td>' + certificate.organization + '</td> </tr>' 
						+ '<tr> <td>Organization unit:</td><td>' + certificate.organizationUnit + '</td> </tr>' 
						+ '<tr> <td>Common name:</td><td>' + certificate.commonName + '</td></tr>' 
						+ '<tr> <td>Given name:</td><td>' + certificate.givenName + '</td> </tr>'
						+ '<tr> <td>Surname:</td><td>' + certificate.surname + '</td> </tr>' 
						+ '<tr> <td>Email:</td><td>' + certificate.email + '</td> </tr>' 
						+ '<tr> <td>Country code:</td><td>' + certificate.countryCode + '</td> </tr>' 
						+ '<tr> <td>State:</td><td>' + certificate.state + '</td> </tr>' 
						+ '<tr> <td>Locality:</td><td>' + certificate.locality + '</td> </tr>' 
						+ '<tr> <td>Start date:</td><td>' + certificate.startDate + '</td> </tr>' 
						+ '<tr> <td>End date:</td><td>' + certificate.startDate + '</td> </tr>' 
						+ '<tr> <td>CA:</td><td>' + certificate.isCA + '</td> </tr>' 
						+ '<tr> <td>Is revoked:</td><td>' + certificate.isRevoked + '</td> </tr>' 
                        + '</table>';
                        
            $(div_info).append(table_info);
            document.getElementById("div_info").style.display = "initial";
		},
		error:function(){
			console.log('error getting certificate');
		}
	});
	
};
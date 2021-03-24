checkUserRole("ROLE_ADMIN");
$(document).ready(function () {	
	
	$.ajax({
		type:"GET", 
		url: "/api/certificate/all",
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
	
	let btnCheckValidity = '<button data-toggle="modal" data-target="#centralModalCheckValidation" class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="validation(this.id)">Validity</button>';
	let btnRevoke = '';
	if(c.isRevoked == false){
		btnRevoke = '<button data-toggle="modal" data-target="#modalConfirmRevoke" class="btn btn-danger btn-sm" type="button" id="' + c.id +'" onclick="revoke(this.id)">Revoke</button>';
	}
	let btnIssuer = '<button data-toggle="modal" data-target="#centralModalViewIssuer" class="btn btn-info btn-sm" type="button" id="' + c.issuerId +'" onclick="getIssuer(this.id)">Issuer</button>';
	let btnDetails = '<button data-toggle="modal" data-target="#centralModalViewDetails" class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="getDetails(this.id)">Details</button>';
	localStorage.setItem(c.id,JSON.stringify(c));
	let row = $('<tr><td>' + c.organization + '</td><td>' + c.organizationUnit + '</td>' +
				'<td>' + c.email + '</td>' +
				'<td>' + c.startDate + '</td><td>' + c.endDate + '</td>' + 
				'<td>' + c.isCA + '</td><td>' + c.isRevoked + '</td><td>' + btnCheckValidity +
				'</td><td>' + btnIssuer + '</td><td>' + btnRevoke + '</td><td>' + btnDetails + '</td>'  + 
				'</tr>');
				
	$('#certificates').append(row);
};

function getDetails(certificateId) {
	$('#div_details').empty();
	let certificate = JSON.parse(localStorage.getItem(certificateId));
	
	let keyUsageList = [];

  	certificate.keyUsage.forEach(function(number) {
		if(number == 0){
			keyUsageList.push(" Digital signature");
		}
		if(number == 1){
			keyUsageList.push(" Non repudiation");
		}
		if(number == 2){
			keyUsageList.push(" Key encipherment");
		}
		if(number == 3){
			keyUsageList.push(" Data encipherment");
		}
		if(number == 4){
			keyUsageList.push(" Key agreement");
		}
		if(number == 5){
			keyUsageList.push(" Key cert sign");
		}
		if(number == 6){
			keyUsageList.push(" CRL sign");
		}
		if(number == 7){
			keyUsageList.push(" Encipher only");
		}
		if(number == 8){
			keyUsageList.push(" Decipher only");
		}
  	});
	
	
	let table_details =  '<table style="margin-top:30px; margin-bottom:30px;">'
						+ '<tr> <td style="width:40%">Subject common name:</td><td>' + certificate.commonName + '</td></tr>' 
						+ '<tr> <td style="width:40%">Subject given name:</td><td>' + certificate.givenName + '</td> </tr>'
						+ '<tr> <td style="width:40%">Subject surname:</td><td>' + certificate.surname + '</td> </tr>' 
						+ '<tr> <td style="width:40%">Country code:</td><td>' + certificate.countryCode + '</td> </tr>' 
						+ '<tr> <td style="width:40%">State:</td><td>' + certificate.state + '</td> </tr>' 
						+ '<tr> <td style="width:40%">Locality:</td><td>' + certificate.locality + '</td> </tr>' 
						+ '<tr> <td style="width:40%">Subject alternative name:</td><td>' +( certificate.subjectAlternativeName ? certificate.subjectAlternativeName:"") + '</td> </tr>' 
						+ '<tr> <td style="width:40%">Issuer alternative name:</td><td>' + (certificate.issuerAlternativeName ? certificate.issuerAlternativeName:"") + '</td> </tr>' 
                        + '<tr> <td style="width:40%">Subject date of birth:</td><td>' + (certificate.dateOfBirth ? certificate.dateOfBirth:"") + '</td> </tr>' 
                        + '<tr> <td style="width:40%">Subject place of birth:</td><td>' + (certificate.placeOfBirth ? certificate.placeOfBirth:"") + '</td> </tr>' 
                        + '<tr> <td style="width:40%">Key usages:</td><td>' + keyUsageList +'</td></tr>';   	

      $(div_details).append(table_details);
			
};

function validation(certificateId) {
	
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

function revoke(certificateId) {

	$('a#yes_revoke').click(function(event){
		
		event.preventDefault();
		
		$.ajax({
		type:"PUT", 
		url: "/api/certificate/" + certificateId + "/revoke",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(){
			location.reload();
		},
		error:function(){
			console.log('error revoking certificates');
		}
	});
		
	}); 	

};

function getIssuer(issuerId) {

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
						+ '<tr> <td style="width:40%">Organization:</td><td>' + certificate.organization + '</td> </tr>' 
						+ '<tr> <td>Organization unit:</td><td>' + certificate.organizationUnit + '</td> </tr>' 
						+ '<tr> <td>Common name:</td><td>' + certificate.commonName + '</td></tr>' 
						+ '<tr> <td>Given name:</td><td>' + certificate.givenName + '</td> </tr>'
						+ '<tr> <td>Surname:</td><td>' + certificate.surname + '</td> </tr>' 
						+ '<tr> <td>Email:</td><td>' + certificate.email + '</td> </tr>' 
						+ '<tr> <td>Country code:</td><td>' + certificate.countryCode + '</td> </tr>' 
						+ '<tr> <td>State:</td><td>' + certificate.state + '</td> </tr>' 
						+ '<tr> <td>Locality:</td><td>' + certificate.locality + '</td> </tr>' 
						+ '<tr> <td>Start date:</td><td>' + certificate.startDate + '</td> </tr>' 
						+ '<tr> <td>End date:</td><td>' + certificate.endDate + '</td> </tr>' 
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
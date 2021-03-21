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
	
	let btnCheckValidity = '<button class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="checkValidity(this.id)">Validity</button>';
	let btnRevoke = '<button class="btn btn-danger btn-sm" type="button" id="' + c.id +'" onclick="revoke(this.id)">Revoke</button>';
	let btnIssuer = '<button class="btn btn-info btn-sm" type="button" id="' + c.id +'" onclick="getIssuer(this.id)">Issuer</button>';
	
	let row = $('<tr><td>' + c.organization + '</td><td>' + c.organizationUnit + '</td>' +
				'<td>' + c.commonName + '</td><td>' + c.givenName + '</td><td>' + c.surname + '</td>' +
				'<td>' + c.email + '</td><td>' + c.countryCode + '</td><td>' + c.state + '</td>' +
				'<td>' + c.locality + '</td><td>' + c.startDate + '</td><td>' + c.endDate + '</td>' + 
				'<td>' + c.isCA + '</td><td>' + c.isRevoked + '</td><td>' + btnCheckValidity +
				'</td><td>' + btnIssuer + '</td><td>' + btnRevoke + '</td>' +
				'</tr>');
				
	$('#certificates').append(row);
};

function checkValidity(certificateId){}

function revoke(certificateId) {}

function getIssuer(certificateId) {}
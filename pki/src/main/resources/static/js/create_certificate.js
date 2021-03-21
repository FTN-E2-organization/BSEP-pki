let subjectArray = [];
$(document).ready(function () {	
	
	$('#startDate').val("");
	$('#endDate').val("");
	$('#startDate').prop("min",new Date().toISOString().split("T")[0]);
	$('#endDate').prop("min",new Date().toISOString().split("T")[0]);
	
	$.ajax({
		type:"GET", 
		url: "/api/subject",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(subjects){
			$('#subjects').empty();
			for (let s of subjects){
				let  out = "";
				if(s.typeOfSubject == "Person"){
					out = s.givenName + ' ' + s.surname + ', ' + s.commonName + ', ' + s.email + ', ' + s.countryCode + ', ' + s.state + ', ' + s.locality;
				}
				else{
					out = s.organization + ', ' + s.commonName + ', ' + s.organizationUnit + ', ' + s.countryCode + ', ' + s.state + ', ' + s.locality;
				}
				$('#subjects').append('<option id="' + s.id + '">' + out +'</option>');
				subjectArray.push({"id":s.id, "commonName":s.commonName, "givenName":s.givenName, "surname":s.surname, "organization":s.organization, 
							   "organizationUnit":s.organizationUnit, "email":s.email, "countryCode":s.countryCode, "state":s.state, 
							   "locality":s.locality, "typeOfSubject": s.typeOfSubject});
			}
			fillSubjectFields();
		},
		error:function(){
			console.log('error getting subjects');
		}
	});
	
	$.ajax({
		type:"GET", 
		url: "/api/certificate/ca",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(issuers){
			if(issuers.length == 0){
				$('#nss').prop('disabled',true);
				return;
			}
			$('#nss').prop('disabled',false);
			$('#issuers').empty();
			for (let i of issuers){
				let  out = "";
				if(i.givenName != ""){
					out = "GN=" + i.givenName + ' ' + "SN=" + i.surname + ', ' + "CN=" + i.commonName + ', ' + "EMAIL=" + i.email + ', ' + 
					"C=" + i.countryCode + ', ' + "S=" + i.state + ', ' + "L=" + i.locality;
				}
				else{
					out = "O=" + i.organization + ', ' + "CN=" + i.commonName + ', ' + "OU=" + i.organizationUnit + ', ' + 
					"C=" + i.countryCode + ', ' + "S=" + i.state + ', ' + "L=" + i.locality;
				}
				$('#issuers').append('<option id="' + i.id + '">' + out +'</option>');
			}
		},
		error:function(){
			console.log('error getting issuers');
		}
	});
	
	$("input:radio[name=nss]").on("change", function() {
		$('#ss').prop('checked',false);
		$('#nss').prop('checked',true);
		$('#isCa').prop('disabled',false);
		$('#issuers_div').attr('hidden',false);
    });

	$("input:radio[name=ss]").on("change", function() {
		$('#nss').prop('checked',false);
		$('#ss').prop('checked',true);
		$('#isCa').prop('disabled',true);
		$('#issuers_div').attr('hidden',true);
    });

	$('#subjects').on("change", function() {
		fillSubjectFields();
    });

	$('#create_cert').submit(function(event){
		event.preventDefault();
		
		let url = "";
		if($('#ss').is(':checked')){
			url = "/api/certificate/self-signed";
		}else{
			url = "/api/certificate/non-self-signed";
		}
		
		let selectedSubjectId = $('#subjects option:selected').attr('id');
		let selectedSubject = subjectArray[selectedSubjectId - 1];
		let selectedIssuerId = $('#issuers option:selected').attr('id');
		
		if($('#ss').is(':checked')){
			selectedIssuerId = selectedSubjectId;
		}
		
		$.ajax({
			type:"POST", 
			url: url,
			headers: {
	            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
	        },
			data: JSON.stringify({ 
				subjectId: selectedSubject.id,
				issuerId : selectedIssuerId,
				startDate: $('#startDate').val(),
				endDate: $('#endDate').val(),
				isCA: $('#isCa').is(':checked'),
				typeOfSubject: selectedSubject.typeOfSubject,
				commonName: selectedSubject.commonName,
				givenName: selectedSubject.givenName,
				surname: selectedSubject.surname,
				organization: selectedSubject.organization,
				organizationUnit: selectedSubject.organizationUnit,
				email: selectedSubject.email,
				countryCode: selectedSubject.countryCode,
				state: selectedSubject.state,
				locality: selectedSubject.locality}),
			contentType: "application/json",
			success:function(){
				let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Successfully certificate creating.'
					+'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
				window.setTimeout(function(){location.reload();},1000);
				return;
			},
			error:function(xhr){
				if(xhr.responseText == "") xhr.responseText = "Bad request.";
				let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">' + xhr.responseText
					+'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
				return;
			}
		});
	});
	
});

function fillSubjectFields(){
	let selectedSubjectId = $('#subjects option:selected').attr('id');
	let selectedSubject = subjectArray[selectedSubjectId - 1];
	$('#o').text(selectedSubject.organization);
	$('#ou').text(selectedSubject.organizationUnit);
	$('#gn').text(selectedSubject.givenName);
	$('#sn').text(selectedSubject.surname);
	$('#cn').text(selectedSubject.commonName);
	$('#e').text(selectedSubject.email);
	$('#c').text(selectedSubject.countryCode);
	$('#s').text(selectedSubject.state);
	$('#l').text(selectedSubject.locality);
};
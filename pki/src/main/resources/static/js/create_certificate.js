checkUserRole("ROLE_ADMIN");
let subjectArray = [];
$(document).ready(function () {	
	
	$('#startDate').val("");
	$('#endDate').val("");
	$('#startDate').prop("min",new Date().toISOString().split("T")[0]);
	$('#endDate').prop("min",new Date().toISOString().split("T")[0]);
	
	$.ajax({
		type:"GET", 
		url: "/api/user/subjects",
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(subjects){
			$('#subjects').empty();
			for (let s of subjects){
				$('#subjects').append('<option id="' + s.id + '">' + s.username +'</option>');
				subjectArray.push({"id":s.id, "username":s.username});
			}
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

	$("input:radio[name=person]").on("change", function() {
		$('#system').prop('checked',false);
		$('#person').prop('checked',true);
		
		$("#name_surname_div").attr("hidden",false);
		$('#gn').prop("required",true);
		$('#sn').prop("required",true);
    });

	$("input:radio[name=system]").on("change", function() {
		$('#system').prop('checked',true);
		$('#person').prop('checked',false);
		
		$("#name_surname_div").attr("hidden",true);
		$('#gn').prop("required",false);
		$('#sn').prop("required",false);
    });

	$("#templates").on("change", function() {
		let selectedOptionId = $('#templates option:selected').attr('id');
		
		if(selectedOptionId == "keyUsage"){
			keyUsageVisibility(false);
			issuerAltNameVisibility(true);
			subjectAltNameVisibility(true);
			subjectDirAttrVisibility(true);
		}else if(selectedOptionId == "issuerAltName"){
			keyUsageVisibility(true);
			issuerAltNameVisibility(false);
			subjectAltNameVisibility(true);
			subjectDirAttrVisibility(true);
		}
		else if(selectedOptionId == "subjectDirAttr"){
			keyUsageVisibility(true);
			issuerAltNameVisibility(true);
			subjectAltNameVisibility(true);
			subjectDirAttrVisibility(false);
		}else if(selectedOptionId == "subjectAltName"){
			keyUsageVisibility(true);
			issuerAltNameVisibility(true);
			subjectAltNameVisibility(false);
			subjectDirAttrVisibility(true);
		}
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
		let selectedIssuerId = $('#issuers option:selected').attr('id');
		
		if($('#ss').is(':checked')){
			selectedIssuerId = selectedSubjectId;
		}
		
		let typeOfSubject = "Person";
		if($('#system').is(':checked')){
			typeOfSubject = "System";
		}
		
		let subjectDirectoryAttributes = [];
		
		for(let i=0;i<=8;i++){
			let idKu = i + 'ku';
			if($('#' + idKu).is(':checked')){
				subjectDirectoryAttributes.push(i);
			}
		}
		
		
		$.ajax({
			type:"POST", 
			url: url,
			headers: {
	            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
	        },
			data: JSON.stringify({ 
				subjectId: selectedSubjectId,
				issuerId : selectedIssuerId,
				startDate: $('#startDate').val(),
				endDate: $('#endDate').val(),
				isCA: $('#isCa').is(':checked'),
				typeOfSubject: typeOfSubject,
				commonName: $('#cn').val(),
				givenName: $('#gn').val(),
				surname: $('#sn').val(),
				organization: $('#o').val(),
				organizationUnit: $('#ou').val(),
				email: $('#e').val(),
				countryCode: $('#c').val(),
				state: $('#s').val(),
				locality: $('#l').val()}),
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

function keyUsageVisibility(flag){
	$("#ku1").attr("hidden",flag);
	$("#ku2").attr("hidden",flag);
}

function issuerAltNameVisibility(flag){
	$("#issAltNameDiv").attr("hidden",flag);
	$('#issAltName').prop("required",!flag);
}

function subjectAltNameVisibility(flag){
	$("#subAltNameDiv").attr("hidden",flag);
	$('#subAltName').prop("required",!flag);
}

function subjectDirAttrVisibility(flag){
	$("#subDirAttrDiv").attr("hidden",flag);
}



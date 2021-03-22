checkUserRole("ROLE_SUBJECT");
var subjectId = getUserIdFromToken();
$(document).ready(function () {	
	
	$.ajax({
		type:"GET", 
		url: "/api/subject/" + subjectId,
		headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
		contentType: "application/json",
		success:function(subject){
			fillSubjectInfo(subject);
		},
		error:function(){
			console.log('error getting subjects');
		}
	});
	
});

function fillSubjectInfo(subject){
	$('#o').text(subject.organization);
	$('#ou').text(subject.organizationUnit);
	$('#gn').text(subject.givenName);
	$('#sn').text(subject.surname);
	$('#cn').text(subject.commonName);
	$('#e').text(subject.email);
	$('#c').text(subject.countryCode);
	$('#s').text(subject.state);
	$('#l').text(subject.locality);
};
var entityMap = {
	'&': '&amp;',
	'<': '&lt;',
	'>': '&gt;',
	'"': '&quot;',
	"'": '&#39;',
	'/': '&#x2F;',
	'`': '&#x60;',
	'=': '&#x3D;'
};


var ipAddress;

$(document).ready(function () {	
	
	$("#email").on('input',function(){
		let email = $('#email').val();
	
		if(email === "" || email == null){
			$('#emailDescription').text("This is required field.");
			$('#emailDescription').css("color","red");
		}else{
			$('#emailDescription').text("");
		}
	});
	
	$("#password").on('input',function(){
		let password = $('#password').val();
	
		if(password === "" || password == null){
			$('#pswDescription').text("This is required field.");
			$('#pswDescription').css("color","red");
		}else{
			$('#pswDescription').text("");
		}
	});
	

	/*Login patient on submit*/
	$('form#logging_in').submit(function (event) {

		event.preventDefault();
		$('#div_alert').empty();

		let username = escapeHtml($('#email').val());
		let password = escapeHtml($('#password').val());

		var userInfoDTO = {
			"username": username,
			"password": password,
			"ipAddress": ipAddress
		};

		if ((username == "") || (password == "")) {
			return;
		}
		else {
			$("form#logging_in").removeClass("unsuccessful");
			$('#login').attr("disabled",true);
			
			
			$.getJSON('https://api.ipify.org?format=json', function(data){
			ipAddress = data.ip;
			
			$.ajax({
				url: "/api/auth/login",
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify(userInfoDTO),
				success: function (token) {
					localStorage.setItem('token', token.accessToken);
					redirectUser(token.accessToken);
				},
				error: function () {
					let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Invalid email or password.'
						+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
					$('#div_alert').append(alert);
					$('#login').attr("disabled",false);
					return;
				}
			});
					
			});	//getJSON
		}
	});
});

function escapeHtml(string) {
	return String(string).replace(/[&<>"'`=\/]/g, function (s) {
		return entityMap[s];
	});
}

function redirectUser(token){
	let role = decodeToken(token).role;
	if(role == "ROLE_ADMIN"){
    	 window.location.href = "admin_show_all.html";
	}else if(role == "ROLE_SUBJECT"){
		window.location.href = "subject_show_all.html";
	}
}

function decodeToken(token) {
   var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function sendNewLink() {	
	let username = $('#email').val();
	
	if ((username == "")) {
		let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Enter your email!'
			+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
		$('#div_alert').append(alert);
		return;
	} 
	$.ajax({
		url: "/api/auth/new-activation-link",
		type: 'POST',
		contentType: 'application/json',
		data: username,
		success: function () {
			let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Success.'
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		},
		error: function (jqXHR) {
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">ERROR. ' + jqXHR.responseText 
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}
	});		
};


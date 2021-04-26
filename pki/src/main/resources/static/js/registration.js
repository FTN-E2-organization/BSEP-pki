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

$(document).ready(function () {	
	
	$("#password").on('input',function(){
 		let password = $('#password').val();
		let passwordRepeat = $('#passwordRepeat').val();
	
	  	let numCharacter = /[0-9]+/i
		let lowercaseCharacter = /[a-z]+/g
		let uppercaseCharacter = /[A-Z]+/g
		let specialSymbol = /[?|!@#.$%/]+/i
		let pswLength = $('#password').val().length;
		
		let strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{10,})");
		let mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{10,})");

	  	if(numCharacter.test(password))
			$('#numCharacter').css("color","green");
		else
			$('#numCharacter').css("color","red");
		
		if(lowercaseCharacter.test(password))
			$('#lowercaseCharacter').css("color","green");
		else
			$('#lowercaseCharacter').css("color","red");
			
		if(uppercaseCharacter.test(password))
			$('#uppercaseCharacter').css("color","green");
		else
			$('#uppercaseCharacter').css("color","red");
			
		if(specialSymbol.test(password))
			$('#specialSymbol').css("color","green");
		else
			$('#specialSymbol').css("color","red");
		
		if(pswLength >= 10 && pswLength <= 32)
			$('#pswLength').css("color","green");
		else
			$('#pswLength').css("color","red");
			
		if(strongRegex.test(password)){
			$('#pswDescription').text("Strong password");
			$('#pswDescription').css("color","green");
		}
		else if(mediumRegex.test(password)){
			$('#pswDescription').text("Medium password");
			$('#pswDescription').css("color","orange");
		}
		else{
			$('#pswDescription').text("Weak password");
			$('#pswDescription').css("color","red");
		}
		
		if(passwordRepeat != '' && password != passwordRepeat){
			$('#pswRepeatDescription').text("Passwords do not match");
			$('#pswRepeatDescription').css("color","red");
		}else{
			$('#pswRepeatDescription').text("");
		}
			
	});
	
	$("#passwordRepeat").on('input',function(){
		let password = $('#password').val();
		let passwordRepeat = $('#passwordRepeat').val();
		
		if(passwordRepeat != '' && password != passwordRepeat){
			$('#pswRepeatDescription').text("Passwords do not match");
			$('#pswRepeatDescription').css("color","red");
		}else{
			$('#pswRepeatDescription').text("");
		}
		
	});

	$('form#registration').submit(function(event) {
		
		event.preventDefault();
		$('#div_alert').empty();
		
		let username = $('#email').val();
		let password = escapeHtml($('#password').val());
		let passwordRepeat = escapeHtml($('#passwordRepeat').val());
		
		if (username == "" || username == null) {
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Username is required field.'
			+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}
		if(password == "" || password == null){
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Password is required field.'
			+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}
		if(password != passwordRepeat){
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Passwords do not match.'
			+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}

		var addUserDTO = {
			"username": username,
			"password": password
		};
		
		$('#register').attr("disabled",true);
		
		$.ajax({
			url: "/api/user/subjects",
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(addUserDTO),
			success: function () {
				let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Successful registration.Activate Your account and log in.'
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
				setTimeout(function () {
		        	location.href = "login.html";
		    	}, 2000);
			},
			error: function (xhr) {
				let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">' + xhr.responseText
					+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
				$('#register').attr("disabled",false);
				return;
			}
		});	
	});
});

function escapeHtml(string) {
	return String(string).replace(/[&<>"'`=\/]/g, function (s) {
		return entityMap[s];
	});
}

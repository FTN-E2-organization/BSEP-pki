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
	
	$("#new-password").on('input',function(){
		
		let password = $('#new-password').val();
		let passwordRepeat = $('#confirm-password').val();
	
	  	let numCharacter = /[0-9]+/i
		let lowercaseCharacter = /[a-z]+/g
		let uppercaseCharacter = /[A-Z]+/g
		let specialSymbol = /[?|!@#.$%/]+/i
		let pswLength = $('#new-password').val().length;
		
		let strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{10,})");
		let mediumRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{10,})");

		if(password === "" || password == null){
			$('#pswDescription').text("This is required field.");
			$('#pswDescription').css("color","red");
			
			$('#numCharacter').css("color","red");
			$('#lowercaseCharacter').css("color","red");
			$('#uppercaseCharacter').css("color","red");
			$('#specialSymbol').css("color","red");
			$('#pswLength').css("color","red");
		}else{
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
		}
	});
	
	
	$("#confirm-password").on('input',function(){
		let password = $('#new-password').val();
		let passwordRepeat = $('#confirm-password').val();
		
		if(passwordRepeat === "" || passwordRepeat == null){
			$('#pswRepeatDescription').text("This is required field.");
			$('#pswRepeatDescription').css("color","red");
		}else{
			if(passwordRepeat != '' && password != passwordRepeat){
				$('#pswRepeatDescription').text("Passwords do not match");
				$('#pswRepeatDescription').css("color","red");
			}else{
				$('#pswRepeatDescription').text("");
			}
		}
	});
		
});


function changePassword(e) {	
	e.preventDefault();
	$('#div_alert').empty();
	let password = escapeHtml($('#new-password').val());
	let passwordRepeat = escapeHtml($('#confirm-password').val());
	
	if(password == "" || password == null){
		let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Password is required field.'
		+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
		$('#div_alert').append(alert);
		return;
	}
	if(passwordRepeat == "" || passwordRepeat == null){
		let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Repeat password is required field.'
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
		
	$('#update').attr("disabled",true);
	
	$.ajax({
		type:"POST", 
		url: "/api/auth/password-change" ,
        data:JSON.stringify({"token": (window.location.href).split('=')[1] ,"password":password}),
		contentType: "application/json",
		success: function (ret) {
			if(!ret){
				let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">This user account is not enabled or you did not follow the recovery instructions properly.'
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
			}else{
				let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Your password has been changed successfully!'
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
				$('#div_alert').append(alert);
				setTimeout(function () {
		        	location.href = "login.html";
		    	}, 2000);
	    	}
		},
		error: function (xhr) {
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">' + xhr.responseText
				+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			$('#update').attr("disabled",false);
			return;
		}
	});
	
}

function escapeHtml(string) {
	return String(string).replace(/[&<>"'`=\/]/g, function (s) {
		return entityMap[s];
	});
}


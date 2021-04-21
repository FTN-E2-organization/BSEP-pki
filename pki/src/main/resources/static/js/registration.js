$(document).ready(function () {	

	$('form#registration').submit(function(event) {
		
		event.preventDefault();
		$('#div_alert').empty();
		
		let username = $('#email').val();
		let password = $('#password').val();
		let passwordRepeat = $('#passwordRepeat').val();
		
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
		
		$.ajax({
			url: "/api/user/subjects",
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(addUserDTO),
			success: function () {
				let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Successful registration.Please, log in.'
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
				return;
			}
	});
		
	});
	
});

function changePassword(e) {	
	e.preventDefault();
	$('#div_alert').empty();
	let password = $('#new-password').val();
	let passwordRepeat = $('#confirm-password').val();
	
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
				return;
		}
	});
	
}

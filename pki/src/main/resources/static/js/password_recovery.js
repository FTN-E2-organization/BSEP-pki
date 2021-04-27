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
	
	$("#email").on('input',function(){
		let email = escapeHtml($('#email').val());
	
		if(email === "" || email == null){
			$('#emailDescription').text("This is required field.");
			$('#emailDescription').css("color","red");
		}else{
			$('#emailDescription').text("");
		}
	});
	
});


function escapeHtml(string) {
	return String(string).replace(/[&<>"'`=\/]/g, function (s) {
		return entityMap[s];
	});
}

function requestPassword(e) {
	e.preventDefault();
	let email = escapeHtml($("#email").val());
	if (email == "" || email == null) {
			let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">Username is required field.'
			+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
			$('#div_alert').append(alert);
			return;
		}
	$.ajax({
		type:"POST", 
		url: "/api/auth/password-recovery" ,
		
        data:email,
		contentType: "application/text",
		success: function (ret) {
				if(!ret){
					let alert = $('<div class="alert alert-danger alert-dismissible fade show m-1" role="alert">This email address does not exist or format is wrong.'
					+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
					$('#div_alert').append(alert);
				}else{
					let alert = $('<div class="alert alert-success alert-dismissible fade show m-1" role="alert">Your password recovery instructions will be sent to your email. Make sure to check your spam box.'
					+ '<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + '</div >')
					$('#div_alert').append(alert);
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
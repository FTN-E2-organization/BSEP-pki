$(document).ready(function () {
	
    token = localStorage.getItem("token");

    if (token == null) {
		localStorage.clear();
		
		if (window.location.href.indexOf("login.html") ==  -1)
        	document.body.appendChild(document.createElement('script')).src='../../js/navbars/unauthenticated_user.js';
            window.location.href = "../html/login.html"; 

        return;
    }
    else
    {
		if(getRoleFromToken() == "ROLE_ADMIN"){
			 document.body.appendChild(document.createElement('script')).src='../../js/navbars/admin.js';
				
		}else if(getRoleFromToken() == "ROLE_SUBJECT"){
			 document.body.appendChild(document.createElement('script')).src='../../js/navbars/subject.js';
		
		}
		
	}
});

function decodeToken(token) {
   var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function getRoleFromToken() {
	try{
		return decodeToken(localStorage.getItem("token")).role;
	}
    catch(err){
		window.location.href = "../html/login.html";
	}
}


function checkUserRole(trueRole) {
    var role = getRoleFromToken();
    if (role != trueRole) {
		if(role == "ROLE_ADMIN"){
			window.location.href = "../html/admin_show_all.html";
		}
		else if(role == "ROLE_SUBJECT"){
			window.location.href = "../html/subject_show_all.html";
		}
	}
}

function logOut() {
    localStorage.clear();
    window.location.href = "../html/login.html";
}
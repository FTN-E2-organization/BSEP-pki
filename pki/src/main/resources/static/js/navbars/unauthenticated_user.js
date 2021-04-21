$(document).ready(function () {
    $('body').prepend($(
        '<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">'
        + '<div class="collapse navbar-collapse" id="navbarNav">'
        + ' <ul class="navbar-nav">'
        + '  <li class="nav-item">'
         + '  <a class="nav-link" href="/html/login.html">Log in</a>'
        + '  </li>'
		+ '  <li class="nav-item">'
         + '  <a class="nav-link" href="/html/registration.html">Register</a>'
        + '  </li>'
        + ' </ul>'
        + ' </div>'
        + ' </nav>'
    ));
});
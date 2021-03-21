$(document).ready(function () {
    $('body').prepend($(
        '<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">'
        + '<div class="collapse navbar-collapse" id="navbarNav">'
        + ' <ul class="navbar-nav">'
        + '  <li class="nav-item">'
        + '  <a class="nav-link" href="/html/admin_show_all.html">Show all</a>'
        + '  </li>'
        + '  <li class="nav-item">'
        + '  <a class="nav-link" href="/html/create_certificate.html">Create new</a>'
        + '  </li>'
        + ' </ul>'
        + ' </div>'
        + ' </nav>'
    ));
});